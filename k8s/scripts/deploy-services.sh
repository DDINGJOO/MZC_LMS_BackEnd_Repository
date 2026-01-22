#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$SCRIPT_DIR/../.."
NAMESPACE="lms-services"
INFRA_NAMESPACE="lms-infra"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

echo "=========================================="
echo "  LMS Backend - Services Deployment"
echo "=========================================="

# Infrastructure endpoints
MYSQL_HOST="mysql.$INFRA_NAMESPACE.svc.cluster.local"
REDIS_HOST="redis-master.$INFRA_NAMESPACE.svc.cluster.local"
KAFKA_HOST="kafka.$INFRA_NAMESPACE.svc.cluster.local:9092"
MONGODB_HOST="mongodb.$INFRA_NAMESPACE.svc.cluster.local"
ELASTICSEARCH_HOST="elasticsearch.$INFRA_NAMESPACE.svc.cluster.local"

# Common Helm values
COMMON_VALUES="
  env.MYSQL_HOST=$MYSQL_HOST
  env.REDIS_HOST=$REDIS_HOST
  env.KAFKA_BOOTSTRAP_SERVERS=$KAFKA_HOST
"

# Deploy a service
deploy_service() {
    local service_name=$1
    local port=$2
    local extra_values=${3:-""}

    log_info "Deploying $service_name..."

    local chart_path="$PROJECT_ROOT/services/$service_name/helm"

    if [ ! -d "$chart_path" ]; then
        log_warn "Helm chart not found for $service_name, skipping"
        return
    fi

    helm upgrade --install $service_name "$chart_path" \
        --namespace $NAMESPACE \
        --set image.repository=lms/$service_name \
        --set image.tag=latest \
        --set image.pullPolicy=Never \
        --set service.port=$port \
        --set env.MYSQL_HOST=$MYSQL_HOST \
        --set env.REDIS_HOST=$REDIS_HOST \
        --set env.KAFKA_BOOTSTRAP_SERVERS=$KAFKA_HOST \
        --set env.SPRING_PROFILES_ACTIVE=k8s \
        $extra_values \
        --wait --timeout 3m || log_warn "Failed to deploy $service_name"

    log_info "$service_name deployed"
}

# Build Docker images in Minikube context
build_images() {
    log_info "Building Docker images in Minikube context..."

    # Point Docker to Minikube's Docker daemon
    eval $(minikube docker-env)

    local services=(
        "identity-service"
        "user-profile-service"
        "organization-service"
        "catalog-service"
        "course-management-service"
        "enrollment-service"
        "learning-progress-service"
        "assessment-service"
        "board-service"
        "media-service"
        "messaging-service"
        "notification-service"
        "search-service"
    )

    for service in "${services[@]}"; do
        local service_path="$PROJECT_ROOT/services/$service"
        if [ -f "$service_path/build.gradle" ]; then
            log_info "Building $service..."

            # Build JAR
            cd "$service_path"
            ./gradlew bootJar -x test || {
                log_warn "Failed to build $service"
                continue
            }

            # Build Docker image
            docker build -t lms/$service:latest . || {
                log_warn "Failed to build Docker image for $service"
            }
            cd "$PROJECT_ROOT"
        fi
    done

    log_info "All images built"
}

# Deploy all services
deploy_all_services() {
    # Core services
    deploy_service "identity-service" 8080
    deploy_service "user-profile-service" 8081
    deploy_service "organization-service" 8082

    # Course services
    deploy_service "catalog-service" 8083
    deploy_service "course-management-service" 8084
    deploy_service "enrollment-service" 8085
    deploy_service "learning-progress-service" 8086
    deploy_service "assessment-service" 8087

    # Supporting services
    deploy_service "board-service" 8092
    deploy_service "media-service" 8090

    # Communication services
    deploy_service "messaging-service" 8088 "--set env.MONGODB_HOST=$MONGODB_HOST"
    deploy_service "notification-service" 8089
    deploy_service "search-service" 8091 "--set env.ELASTICSEARCH_HOST=$ELASTICSEARCH_HOST"
}

# Wait for all pods
wait_for_pods() {
    log_info "Waiting for all service pods to be ready..."

    kubectl wait --for=condition=ready pod \
        --all \
        --namespace $NAMESPACE \
        --timeout=300s || log_warn "Some pods may not be ready"

    kubectl get pods -n $NAMESPACE
}

# Print service info
print_service_info() {
    echo ""
    log_info "=========================================="
    log_info "  Deployed Services"
    log_info "=========================================="

    kubectl get svc -n $NAMESPACE

    echo ""
    log_info "Access via Istio Gateway:"
    INGRESS_PORT=$(kubectl -n istio-system get service istio-ingress -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}' 2>/dev/null || echo "N/A")
    INGRESS_HOST=$(minikube ip 2>/dev/null || echo "localhost")

    echo "  Add to /etc/hosts: $INGRESS_HOST api.lms.local"
    echo "  API Base URL: http://api.lms.local:$INGRESS_PORT"
}

# Main
main() {
    local skip_build=false

    while [[ $# -gt 0 ]]; do
        case $1 in
            --skip-build)
                skip_build=true
                shift
                ;;
            *)
                shift
                ;;
        esac
    done

    if [ "$skip_build" = false ]; then
        build_images
    fi

    deploy_all_services
    wait_for_pods
    print_service_info

    echo ""
    log_info "Services deployment completed!"
    log_info "Use 'minikube tunnel' to access LoadBalancer services"
}

main "$@"

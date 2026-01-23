#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
VALUES_DIR="$SCRIPT_DIR/../values"
NAMESPACE="lms-infra"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

echo "=========================================="
echo "  LMS Backend - Infrastructure Deployment"
echo "=========================================="

# Deploy MySQL
deploy_mysql() {
    log_info "Deploying MySQL..."

    helm upgrade --install mysql bitnami/mysql \
        --namespace $NAMESPACE \
        --values "$VALUES_DIR/mysql-values.yaml" \
        --wait --timeout 5m

    log_info "MySQL deployed"
}

# Deploy Redis
deploy_redis() {
    log_info "Deploying Redis..."

    helm upgrade --install redis bitnami/redis \
        --namespace $NAMESPACE \
        --values "$VALUES_DIR/redis-values.yaml" \
        --wait --timeout 3m

    log_info "Redis deployed"
}

# Deploy Kafka
deploy_kafka() {
    log_info "Deploying Kafka..."

    helm upgrade --install kafka bitnami/kafka \
        --namespace $NAMESPACE \
        --values "$VALUES_DIR/kafka-values.yaml" \
        --wait --timeout 5m

    log_info "Kafka deployed"
}

# Deploy MongoDB
deploy_mongodb() {
    log_info "Deploying MongoDB..."

    helm upgrade --install mongodb bitnami/mongodb \
        --namespace $NAMESPACE \
        --values "$VALUES_DIR/mongodb-values.yaml" \
        --wait --timeout 3m

    log_info "MongoDB deployed"
}

# Deploy Elasticsearch
deploy_elasticsearch() {
    log_info "Deploying Elasticsearch..."

    helm upgrade --install elasticsearch bitnami/elasticsearch \
        --namespace $NAMESPACE \
        --values "$VALUES_DIR/elasticsearch-values.yaml" \
        --wait --timeout 5m

    log_info "Elasticsearch deployed"
}

# Wait for all pods
wait_for_pods() {
    log_info "Waiting for all infrastructure pods to be ready..."

    kubectl wait --for=condition=ready pod \
        --all \
        --namespace $NAMESPACE \
        --timeout=300s

    log_info "All infrastructure pods are ready"
}

# Print connection info
print_connection_info() {
    echo ""
    log_info "=========================================="
    log_info "  Infrastructure Connection Info"
    log_info "=========================================="

    echo ""
    echo "MySQL:"
    echo "  Host: mysql.$NAMESPACE.svc.cluster.local"
    echo "  Port: 3306"
    echo "  Root Password: kubectl get secret mysql -n $NAMESPACE -o jsonpath='{.data.mysql-root-password}' | base64 -d"

    echo ""
    echo "Redis:"
    echo "  Host: redis-master.$NAMESPACE.svc.cluster.local"
    echo "  Port: 6379"

    echo ""
    echo "Kafka:"
    echo "  Bootstrap: kafka.$NAMESPACE.svc.cluster.local:9092"

    echo ""
    echo "MongoDB:"
    echo "  Host: mongodb.$NAMESPACE.svc.cluster.local"
    echo "  Port: 27017"

    echo ""
    echo "Elasticsearch:"
    echo "  Host: elasticsearch.$NAMESPACE.svc.cluster.local"
    echo "  Port: 9200"
}

# Main
main() {
    # Check namespace exists
    if ! kubectl get namespace $NAMESPACE &> /dev/null; then
        log_error "Namespace $NAMESPACE not found. Run setup-minikube.sh first."
        exit 1
    fi

    deploy_mysql
    deploy_redis
    deploy_kafka
    deploy_mongodb
    deploy_elasticsearch
    wait_for_pods
    print_connection_info

    echo ""
    log_info "Infrastructure deployment completed!"
    log_info "Next: ./setup-istio.sh"
}

main "$@"

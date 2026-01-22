#!/bin/bash
set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }
log_success() { echo -e "${GREEN}[PASS]${NC} $1"; }
log_fail() { echo -e "${RED}[FAIL]${NC} $1"; }

echo "=========================================="
echo "  LMS Backend - Service Communication Test"
echo "=========================================="

NAMESPACE="lms-services"

# Get Istio Ingress info
get_gateway_url() {
    INGRESS_PORT=$(kubectl -n istio-system get service istio-ingress -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}' 2>/dev/null)
    INGRESS_HOST=$(minikube ip 2>/dev/null)

    if [ -z "$INGRESS_PORT" ] || [ -z "$INGRESS_HOST" ]; then
        log_error "Could not get Istio Ingress Gateway URL"
        exit 1
    fi

    echo "http://$INGRESS_HOST:$INGRESS_PORT"
}

# Test endpoint
test_endpoint() {
    local name=$1
    local endpoint=$2
    local expected_status=${3:-200}

    response=$(curl -s -o /dev/null -w "%{http_code}" -H "Host: api.lms.local" "$endpoint" --connect-timeout 5 2>/dev/null)

    if [ "$response" = "$expected_status" ]; then
        log_success "$name - HTTP $response"
        return 0
    else
        log_fail "$name - Expected $expected_status, got $response"
        return 1
    fi
}

# Test health endpoints
test_health_endpoints() {
    log_info "Testing service health endpoints..."

    local base_url=$(get_gateway_url)
    local services=(
        "identity-service:8080"
        "user-profile-service:8081"
        "organization-service:8082"
        "catalog-service:8083"
        "course-management-service:8084"
        "enrollment-service:8085"
        "learning-progress-service:8086"
        "assessment-service:8087"
        "messaging-service:8088"
        "notification-service:8089"
        "media-service:8090"
        "search-service:8091"
        "board-service:8092"
    )

    local passed=0
    local failed=0

    for service_info in "${services[@]}"; do
        local service_name="${service_info%%:*}"
        local port="${service_info##*:}"

        # Port-forward and test health
        kubectl port-forward "svc/$service_name" "$port:$port" -n $NAMESPACE &>/dev/null &
        local pf_pid=$!
        sleep 2

        if curl -s "http://localhost:$port/actuator/health" | grep -q "UP"; then
            log_success "$service_name health check"
            ((passed++))
        else
            log_fail "$service_name health check"
            ((failed++))
        fi

        kill $pf_pid 2>/dev/null || true
    done

    echo ""
    log_info "Health check results: $passed passed, $failed failed"
}

# Test Kafka connectivity
test_kafka() {
    log_info "Testing Kafka connectivity..."

    # Check if Kafka pod is running
    if kubectl get pods -n lms-infra -l app.kubernetes.io/name=kafka | grep -q Running; then
        log_success "Kafka is running"

        # List topics
        kubectl exec -n lms-infra kafka-controller-0 -- kafka-topics.sh --list --bootstrap-server localhost:9092 2>/dev/null && \
            log_success "Kafka topics accessible" || \
            log_fail "Cannot list Kafka topics"
    else
        log_fail "Kafka is not running"
    fi
}

# Test inter-service communication via Istio
test_istio_routing() {
    log_info "Testing Istio routing..."

    local base_url=$(get_gateway_url)

    # Test via Istio Gateway
    test_endpoint "Identity Service via Gateway" "$base_url/api/v1/auth/health"
    test_endpoint "Catalog Service via Gateway" "$base_url/api/v1/catalog/categories"
    test_endpoint "Search Service via Gateway" "$base_url/api/v1/search?q=test"
}

# Test event flow
test_event_flow() {
    log_info "Testing event-driven communication..."

    # Create a test course and check if:
    # 1. course-events topic receives message
    # 2. search-service indexes the course
    # 3. notification is triggered

    echo "Event flow test requires manual verification."
    echo "Steps:"
    echo "  1. Create a course via course-management-service"
    echo "  2. Check Kafka topic: kubectl exec -n lms-infra kafka-controller-0 -- kafka-console-consumer.sh --topic course-events --bootstrap-server localhost:9092 --from-beginning --max-messages 1"
    echo "  3. Search for the course via search-service"
}

# Print Kiali access
print_observability_access() {
    echo ""
    log_info "=========================================="
    log_info "  Observability Access"
    log_info "=========================================="
    echo ""
    echo "To view service mesh topology and traffic:"
    echo "  kubectl port-forward svc/kiali -n istio-system 20001:20001"
    echo "  Open: http://localhost:20001"
    echo ""
    echo "Distributed tracing (Jaeger):"
    echo "  kubectl port-forward svc/tracing -n istio-system 16686:80"
    echo "  Open: http://localhost:16686"
}

# Main
main() {
    test_health_endpoints
    test_kafka
    test_istio_routing
    test_event_flow
    print_observability_access

    echo ""
    log_info "Service communication test completed!"
}

main "$@"

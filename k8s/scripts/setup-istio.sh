#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ISTIO_DIR="$SCRIPT_DIR/../istio"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

echo "=========================================="
echo "  LMS Backend - Istio Service Mesh Setup"
echo "=========================================="

# Install Istio using Helm
install_istio() {
    log_info "Installing Istio base..."

    helm upgrade --install istio-base istio/base \
        --namespace istio-system \
        --wait

    log_info "Installing Istiod (control plane)..."

    helm upgrade --install istiod istio/istiod \
        --namespace istio-system \
        --set pilot.resources.requests.memory=256Mi \
        --set pilot.resources.requests.cpu=100m \
        --wait --timeout 5m

    log_info "Istio control plane installed"
}

# Install Istio Ingress Gateway
install_ingress_gateway() {
    log_info "Installing Istio Ingress Gateway..."

    helm upgrade --install istio-ingress istio/gateway \
        --namespace istio-system \
        --set service.type=NodePort \
        --wait

    log_info "Ingress Gateway installed"
}

# Install Kiali (Observability Dashboard)
install_kiali() {
    log_info "Installing Kiali dashboard..."

    kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.20/samples/addons/kiali.yaml
    kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.20/samples/addons/prometheus.yaml
    kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.20/samples/addons/grafana.yaml
    kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.20/samples/addons/jaeger.yaml

    log_info "Kiali and observability tools installed"
}

# Apply Istio configuration
apply_istio_config() {
    log_info "Applying Istio configuration..."

    kubectl apply -f "$ISTIO_DIR/"

    log_info "Istio configuration applied"
}

# Verify installation
verify_istio() {
    log_info "Verifying Istio installation..."

    kubectl get pods -n istio-system

    echo ""
    log_info "Checking Istio injection status..."
    kubectl get namespace -L istio-injection
}

# Print access info
print_access_info() {
    echo ""
    log_info "=========================================="
    log_info "  Istio Access Information"
    log_info "=========================================="

    INGRESS_PORT=$(kubectl -n istio-system get service istio-ingress -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')
    INGRESS_HOST=$(minikube ip)

    echo ""
    echo "Istio Ingress Gateway:"
    echo "  URL: http://$INGRESS_HOST:$INGRESS_PORT"

    echo ""
    echo "Observability Dashboards (port-forward):"
    echo "  Kiali:      kubectl port-forward svc/kiali -n istio-system 20001:20001"
    echo "  Grafana:    kubectl port-forward svc/grafana -n istio-system 3000:3000"
    echo "  Jaeger:     kubectl port-forward svc/tracing -n istio-system 16686:80"
    echo "  Prometheus: kubectl port-forward svc/prometheus -n istio-system 9090:9090"
}

# Main
main() {
    install_istio
    install_ingress_gateway
    install_kiali
    apply_istio_config
    verify_istio
    print_access_info

    echo ""
    log_info "Istio setup completed!"
    log_info "Next: ./deploy-services.sh"
}

main "$@"

#!/bin/bash
set -e

echo "=========================================="
echo "  LMS Backend - Minikube Setup"
echo "=========================================="

# Configuration
MINIKUBE_CPUS=${MINIKUBE_CPUS:-4}
MINIKUBE_MEMORY=${MINIKUBE_MEMORY:-8192}
MINIKUBE_DISK=${MINIKUBE_DISK:-50g}
KUBERNETES_VERSION=${KUBERNETES_VERSION:-v1.29.0}

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# Check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites..."

    if ! command -v minikube &> /dev/null; then
        log_error "minikube not found. Install: brew install minikube"
        exit 1
    fi

    if ! command -v kubectl &> /dev/null; then
        log_error "kubectl not found. Install: brew install kubectl"
        exit 1
    fi

    if ! command -v helm &> /dev/null; then
        log_error "helm not found. Install: brew install helm"
        exit 1
    fi

    log_info "All prerequisites satisfied"
}

# Start Minikube
start_minikube() {
    log_info "Starting Minikube cluster..."

    # Check if minikube is already running
    if minikube status | grep -q "Running"; then
        log_warn "Minikube is already running"
        read -p "Delete and recreate? (y/N): " confirm
        if [[ $confirm == [yY] ]]; then
            minikube delete
        else
            log_info "Using existing cluster"
            return
        fi
    fi

    minikube start \
        --cpus=$MINIKUBE_CPUS \
        --memory=$MINIKUBE_MEMORY \
        --disk-size=$MINIKUBE_DISK \
        --kubernetes-version=$KUBERNETES_VERSION \
        --driver=docker \
        --addons=ingress,metrics-server,dashboard

    log_info "Minikube started successfully"
}

# Configure kubectl
configure_kubectl() {
    log_info "Configuring kubectl context..."
    kubectl config use-context minikube
    kubectl cluster-info
}

# Add Helm repositories
add_helm_repos() {
    log_info "Adding Helm repositories..."

    helm repo add bitnami https://charts.bitnami.com/bitnami
    helm repo add elastic https://helm.elastic.co
    helm repo add istio https://istio-release.storage.googleapis.com/charts
    helm repo update

    log_info "Helm repositories added"
}

# Create namespaces
create_namespaces() {
    log_info "Creating namespaces..."

    kubectl create namespace lms-infra --dry-run=client -o yaml | kubectl apply -f -
    kubectl create namespace lms-services --dry-run=client -o yaml | kubectl apply -f -
    kubectl create namespace istio-system --dry-run=client -o yaml | kubectl apply -f -

    # Label for Istio injection
    kubectl label namespace lms-services istio-injection=enabled --overwrite

    log_info "Namespaces created"
}

# Main
main() {
    check_prerequisites
    start_minikube
    configure_kubectl
    add_helm_repos
    create_namespaces

    echo ""
    log_info "=========================================="
    log_info "  Minikube setup completed!"
    log_info "=========================================="
    log_info "Next steps:"
    log_info "  1. ./deploy-infra.sh    - Deploy infrastructure"
    log_info "  2. ./setup-istio.sh     - Install Istio"
    log_info "  3. ./deploy-services.sh - Deploy microservices"
    echo ""
    log_info "Useful commands:"
    log_info "  minikube dashboard      - Open K8s dashboard"
    log_info "  minikube tunnel         - Expose LoadBalancer services"
}

main "$@"

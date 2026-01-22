#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "============================================"
echo "  LMS Backend - Full Deployment"
echo "============================================"
echo ""
echo "This script will:"
echo "  1. Setup Minikube cluster"
echo "  2. Deploy infrastructure (MySQL, Redis, Kafka, MongoDB, ES)"
echo "  3. Install Istio Service Mesh"
echo "  4. Build and deploy all microservices"
echo "  5. Run communication tests"
echo ""
read -p "Continue? (y/N): " confirm

if [[ ! $confirm == [yY] ]]; then
    echo "Aborted."
    exit 0
fi

cd "$SCRIPT_DIR/scripts"

# Step 1: Minikube
echo ""
echo "Step 1/5: Setting up Minikube..."
./setup-minikube.sh

# Step 2: Infrastructure
echo ""
echo "Step 2/5: Deploying infrastructure..."
./deploy-infra.sh

# Step 3: Istio
echo ""
echo "Step 3/5: Installing Istio..."
./setup-istio.sh

# Step 4: Services
echo ""
echo "Step 4/5: Building and deploying services..."
./deploy-services.sh

# Step 5: Test
echo ""
echo "Step 5/5: Running communication tests..."
./test-services.sh

echo ""
echo "============================================"
echo "  Deployment Complete!"
echo "============================================"
echo ""
echo "Quick Access:"
echo "  minikube dashboard     - K8s Dashboard"
echo "  minikube tunnel        - Expose services"
echo ""
echo "Add to /etc/hosts:"
echo "  $(minikube ip) api.lms.local"

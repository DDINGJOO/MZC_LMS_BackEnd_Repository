#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "=========================================="
echo "  LMS Infrastructure - Official Images"
echo "=========================================="

# Cleanup existing Bitnami installations
echo "[INFO] Cleaning up existing installations..."
helm uninstall mysql kafka elasticsearch mongodb redis -n lms-infra 2>/dev/null || true
kubectl delete pvc --all -n lms-infra 2>/dev/null || true

# Wait for cleanup
sleep 5

# Apply infrastructure
echo "[INFO] Deploying MySQL..."
kubectl apply -f "$SCRIPT_DIR/mysql.yaml"

echo "[INFO] Deploying Redis..."
kubectl apply -f "$SCRIPT_DIR/redis.yaml"

echo "[INFO] Deploying MongoDB..."
kubectl apply -f "$SCRIPT_DIR/mongodb.yaml"

echo "[INFO] Deploying Kafka..."
kubectl apply -f "$SCRIPT_DIR/kafka.yaml"

echo "[INFO] Deploying Elasticsearch..."
kubectl apply -f "$SCRIPT_DIR/elasticsearch.yaml"

echo ""
echo "[INFO] Waiting for pods to be ready..."
kubectl get pods -n lms-infra -w &
WATCH_PID=$!

# Wait up to 5 minutes
sleep 300 &
SLEEP_PID=$!

# Wait for either all pods ready or timeout
while kill -0 $SLEEP_PID 2>/dev/null; do
    READY=$(kubectl get pods -n lms-infra --no-headers 2>/dev/null | grep -c "1/1" || echo 0)
    TOTAL=$(kubectl get pods -n lms-infra --no-headers 2>/dev/null | wc -l | tr -d ' ')

    if [ "$READY" = "$TOTAL" ] && [ "$TOTAL" != "0" ]; then
        kill $WATCH_PID 2>/dev/null || true
        kill $SLEEP_PID 2>/dev/null || true
        echo ""
        echo "[INFO] All pods are ready!"
        break
    fi
    sleep 5
done

kill $WATCH_PID 2>/dev/null || true

echo ""
echo "=========================================="
echo "  Infrastructure Status"
echo "=========================================="
kubectl get pods -n lms-infra

echo ""
echo "Connection Info:"
echo "  MySQL:         mysql.lms-infra.svc.cluster.local:3306"
echo "  Redis:         redis.lms-infra.svc.cluster.local:6379"
echo "  MongoDB:       mongodb.lms-infra.svc.cluster.local:27017"
echo "  Kafka:         kafka.lms-infra.svc.cluster.local:9092"
echo "  Elasticsearch: elasticsearch.lms-infra.svc.cluster.local:9200"

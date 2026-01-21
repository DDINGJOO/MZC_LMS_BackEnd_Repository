# Redis Cluster Configuration Guide

This guide explains how to configure and use Redis Cluster mode for the LMS application.

## Overview

The application now supports both **Standalone** and **Cluster** Redis modes:

- **Standalone Mode** (default): Single Redis instance, suitable for development and small deployments
- **Cluster Mode**: Redis Cluster with multiple nodes, suitable for production and high availability

## Configuration Files

### New Files Created

1. **RedisClusterConfig.java**
   - Location: `springboot/src/main/java/com/mzc/backend/lms/common/config/cluster/RedisClusterConfig.java`
   - Purpose: Lettuce-based Redis Cluster connection configuration
   - Features:
     - Periodic topology refresh (30 seconds)
     - Adaptive refresh on MOVED/ASK redirections
     - Read from replica preference for load distribution

2. **CacheKeyGenerator.java**
   - Location: `springboot/src/main/java/com/mzc/backend/lms/common/config/cluster/CacheKeyGenerator.java`
   - Purpose: Generate hash tag-based cache keys for cluster mode
   - Benefits:
     - Ensures related keys are stored in the same slot
     - Prevents cross-slot errors in multi-key operations
     - Example: `{user:123}:profile` and `{user:123}:courses` share the same slot

3. **application-cluster.yaml**
   - Location: `springboot/src/main/resources/application-cluster.yaml`
   - Purpose: Spring profile for cluster mode configuration
   - Configuration: Enables cluster mode and sets cluster-specific settings

4. **docker-compose.cluster.yml**
   - Location: `docker-compose.cluster.yml`
   - Purpose: Docker Compose setup for local Redis Cluster development
   - Includes: 3 Redis nodes with automatic cluster initialization

### Modified Files

1. **RedissonConfig.java**
   - Updated to support both standalone and cluster modes
   - Uses `@ConditionalOnProperty` to switch configurations
   - Cluster mode uses `useClusterServers()` with node discovery

2. **application-dev.yaml**
   - Added cluster configuration section (disabled by default)
   - Cluster settings available for easy activation

## Quick Start

### Standalone Mode (Default)

No changes required. The application runs in standalone mode by default.

```yaml
spring:
  data:
    redis:
      cluster:
        enabled: false  # Default
      host: localhost
      port: 6379
```

### Cluster Mode

#### Option 1: Using application-cluster.yaml Profile

```bash
# Set active profile to cluster
export SPRING_PROFILES_ACTIVE=dev,cluster

# Or in application.yaml:
spring:
  profiles:
    active: dev,cluster
```

#### Option 2: Enable Cluster in application-dev.yaml

Update `application-dev.yaml`:

```yaml
spring:
  data:
    redis:
      cluster:
        enabled: true  # Enable cluster mode
        nodes:
          - localhost:6379
          - localhost:6380
          - localhost:6381
        max-redirects: 3
```

## Docker Setup

### Standalone Redis

Use the default `docker-compose.yml`:

```bash
docker-compose up -d redis
```

### Redis Cluster

Use `docker-compose.cluster.yml`:

```bash
# Start Redis Cluster
docker-compose -f docker-compose.cluster.yml up -d

# Check cluster status
docker exec -it lms-redis-cluster-1 redis-cli cluster info
docker exec -it lms-redis-cluster-1 redis-cli cluster nodes
```

The cluster initialization happens automatically via the `redis-cluster-init` service.

## Using CacheKeyGenerator

### Why Use Hash Tags?

Redis Cluster distributes keys across 16384 slots using CRC16 hash. Multi-key operations (e.g., MGET, MSET, transactions) require all keys to be in the same slot. Hash tags ensure this.

### How It Works

```java
// Without hash tag (may be in different slots)
"user:123:profile"
"user:123:courses"

// With hash tag (guaranteed same slot)
"{user:123}:profile"  // CRC16({user:123}) % 16384 = slot X
"{user:123}:courses"  // CRC16({user:123}) % 16384 = slot X (same!)
```

### Usage Examples

```java
import com.mzc.backend.lms.common.config.cluster.CacheKeyGenerator;

// User-related keys
String profileKey = CacheKeyGenerator.userKey(userId, "profile");
// Result: {user:123}:profile

String coursesKey = CacheKeyGenerator.userKey(userId, "courses");
// Result: {user:123}:courses

// Course-related keys
String courseKey = CacheKeyGenerator.courseKey(courseId, "details");
// Result: {course:456}:details

// Term-related keys
String termKey = CacheKeyGenerator.termKey(termId, "info");
// Result: {term:789}:info

// Global keys (no hash tag, use sparingly)
String globalKey = CacheKeyGenerator.globalKey("config", "settings");
// Result: config:settings
```

### Migration Guide

If you have existing cache keys without hash tags:

1. **For new keys**: Use CacheKeyGenerator
2. **For existing keys**: Plan a migration:
   - Option A: Cache warmup (let old keys expire, new keys use hash tags)
   - Option B: Background migration job
   - Option C: Dual-write during transition period

## Production Deployment

### Environment Variables

For production, use environment variables:

```bash
# Cluster nodes
export REDIS_CLUSTER_NODE1=redis-node-1.prod.example.com:6379
export REDIS_CLUSTER_NODE2=redis-node-2.prod.example.com:6379
export REDIS_CLUSTER_NODE3=redis-node-3.prod.example.com:6379

# Enable cluster mode
export SPRING_DATA_REDIS_CLUSTER_ENABLED=true
```

Or in `application-prod.yaml`:

```yaml
spring:
  data:
    redis:
      cluster:
        enabled: true
        nodes:
          - ${REDIS_CLUSTER_NODE1}
          - ${REDIS_CLUSTER_NODE2}
          - ${REDIS_CLUSTER_NODE3}
        max-redirects: 3
```

### High Availability Setup

For production, use Redis Cluster with replicas:

```bash
# 3 master nodes + 3 replica nodes
redis-cli --cluster create \
  redis-node-1:6379 \
  redis-node-2:6379 \
  redis-node-3:6379 \
  redis-node-4:6379 \
  redis-node-5:6379 \
  redis-node-6:6379 \
  --cluster-replicas 1
```

Update the configuration to include all 6 nodes.

## Monitoring

### Cluster Health Check

```bash
# Cluster info
redis-cli -c cluster info

# Cluster nodes
redis-cli -c cluster nodes

# Slot distribution
redis-cli cluster slots
```

### Application Logs

With cluster mode enabled, you'll see Lettuce cluster topology logs:

```yaml
logging:
  level:
    io.lettuce.core.cluster: DEBUG
    org.redisson.cluster: DEBUG
```

### Metrics to Monitor

- Cluster node status (master/replica)
- Slot coverage (should be 16384/16384)
- MOVED/ASK redirect counts
- Topology refresh frequency
- Connection pool utilization

## Troubleshooting

### Issue: MOVED/CROSSSLOT Errors

**Cause**: Multi-key operations on keys in different slots

**Solution**: Use CacheKeyGenerator with hash tags

```java
// Bad - may cause CROSSSLOT error
redisTemplate.opsForValue().multiGet(
    Arrays.asList("user:1:profile", "user:2:profile")
);

// Good - but only for same user
String key1 = CacheKeyGenerator.userKey(1L, "profile");
String key2 = CacheKeyGenerator.userKey(1L, "courses");
redisTemplate.opsForValue().multiGet(Arrays.asList(key1, key2));
```

### Issue: Cluster Topology Not Refreshing

**Check**:
1. Adaptive refresh is enabled in `RedisClusterConfig`
2. Network connectivity to all cluster nodes
3. Cluster bus ports (16379, 16380, 16381) are accessible

### Issue: Cannot Connect to Cluster

**Check**:
1. `spring.data.redis.cluster.enabled=true` is set
2. Cluster nodes are running and healthy
3. Cluster is initialized (`redis-cli cluster info`)
4. Application can reach all cluster nodes

## Performance Considerations

### Read Distribution

The configuration uses `ReadFrom.REPLICA_PREFERRED` to distribute read load:

```java
.readFrom(ReadFrom.REPLICA_PREFERRED)  // Prefer replicas for reads
```

Change to:
- `ReadFrom.MASTER`: All reads from master (highest consistency)
- `ReadFrom.REPLICA`: All reads from replicas (highest read throughput)
- `ReadFrom.NEAREST`: Prefer lowest latency node

### Connection Pooling

Cluster mode requires more connections. Adjust in `application-cluster.yaml`:

```yaml
spring:
  data:
    redis:
      lettuce:
        pool:
          max-active: 32  # Higher for cluster
          max-idle: 16
          min-idle: 8
```

## Migration Checklist

- [ ] Review existing Redis usage for multi-key operations
- [ ] Update critical paths to use CacheKeyGenerator
- [ ] Test with local cluster (`docker-compose.cluster.yml`)
- [ ] Configure production cluster nodes
- [ ] Set up monitoring and alerting
- [ ] Plan cache warmup strategy
- [ ] Document runbook for cluster operations
- [ ] Train team on cluster management

## References

- [Redis Cluster Specification](https://redis.io/docs/reference/cluster-spec/)
- [Lettuce Cluster Documentation](https://lettuce.io/core/release/reference/index.html#redis-cluster)
- [Redisson Cluster Mode](https://github.com/redisson/redisson/wiki/2.-Configuration#26-cluster-mode)
- [Spring Data Redis Cluster](https://docs.spring.io/spring-data/redis/reference/redis/cluster.html)

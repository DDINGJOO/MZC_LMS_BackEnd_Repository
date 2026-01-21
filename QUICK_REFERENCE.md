# Multi-Database Quick Reference Card

## TL;DR - What You Need to Know

### For Developers

**Write operations use PRIMARY database:**
```java
@Transactional  // or @Transactional(readOnly = false)
public void saveUser(User user) {
    userRepository.save(user);
}
```

**Read operations use REPLICA database:**
```java
@Transactional(readOnly = true)  // Important!
public User getUser(Long id) {
    return userRepository.findById(id).orElse(null);
}
```

**That's it!** The routing happens automatically based on `@Transactional(readOnly = true/false)`.

### For DevOps

**Development (single database):**
```yaml
spring.datasource.routing.enabled: false  # Default
```

**Production (with replica):**
```yaml
spring.datasource.routing.enabled: true
```

**Environment Variables:**
```bash
DATABASE_URL=primary-host
DATABASE_PORT=3306
DATABASE_REPLICA_URL=replica-host
DATABASE_REPLICA_PORT=3306
DATABASE_ROUTING_ENABLED=true
```

## Local Development

### Without Replication (Default)
```bash
# Just run the app - no changes needed
./gradlew bootRun
```

### With Replication (Optional)
```bash
# 1. Start containers
docker-compose up -d

# 2. Setup replication (first time only)
# See docker/mysql/REPLICATION_SETUP.md

# 3. Enable routing in application-dev.yaml
spring.datasource.routing.enabled: true

# 4. Run app
./gradlew bootRun
```

## Production Deployment

### Standard Deployment (single database)
```bash
# Set routing to false or don't set it
DATABASE_ROUTING_ENABLED=false
docker-compose -f docker-compose.prod.yml up -d
```

### With Replica (recommended)
```bash
# 1. Setup database replication (once)
# Configure MySQL master-slave replication

# 2. Start with replica profile
docker-compose -f docker-compose.prod.yml --profile with-replica up -d

# 3. Set environment variables
DATABASE_URL=mysql-primary-host
DATABASE_REPLICA_URL=mysql-replica-host
DATABASE_ROUTING_ENABLED=true
```

## Troubleshooting

### Issue: "Connection refused" on port 3307
**Cause:** Replica not running
**Fix:** `docker-compose up -d mysql-replica`

### Issue: "Read-after-write shows old data"
**Cause:** Replication lag or using replica for critical reads
**Fix:** Use `@Transactional` (without readOnly) for critical reads after writes

### Issue: "Too many connections"
**Cause:** Connection pool exhausted
**Fix:** Increase pool size in application.yaml:
```yaml
spring.datasource.primary.hikari.maximum-pool-size: 30
spring.datasource.replica.hikari.maximum-pool-size: 30
```

### Issue: "Routing not working"
**Cause:** `routing.enabled` is false
**Fix:** Set `spring.datasource.routing.enabled: true`

## Commands Cheat Sheet

```bash
# Check if routing is enabled
grep "routing:" springboot/src/main/resources/application-*.yaml

# Start local environment
docker-compose up -d

# Check MySQL status
docker exec -it lms-mysql-primary mysql -uroot -prootpassword -e "SHOW MASTER STATUS"
docker exec -it lms-mysql-replica mysql -uroot -prootpassword -e "SHOW SLAVE STATUS\G"

# Check replication lag
docker exec -it lms-mysql-replica mysql -uroot -prootpassword -e "SHOW SLAVE STATUS\G" | grep Seconds_Behind_Master

# Restart with routing disabled
export SPRING_DATASOURCE_ROUTING_ENABLED=false
./gradlew bootRun

# Build without tests
./gradlew build -x test

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## Architecture Diagram

```
┌─────────────────────────────────────┐
│         Your Service Code           │
│                                     │
│  @Transactional(readOnly=true)     │
│         ↓                           │
│  userRepository.findById(id)        │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│  LazyConnectionDataSourceProxy     │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│       RoutingDataSource             │
│                                     │
│  if (readOnly) → REPLICA           │
│  else → PRIMARY                    │
└──────┬───────────────┬──────────────┘
       │               │
   ┌───▼────┐      ┌───▼────┐
   │PRIMARY │      │REPLICA │
   │ :3306  │      │ :3307  │
   │(Write) │      │ (Read) │
   └────────┘      └────────┘
```

## File Locations

**Configuration:**
- `springboot/src/main/resources/application-dev.yaml`
- `springboot/src/main/resources/application-prod.yaml`

**Java Classes:**
- `com.mzc.backend.lms.common.config.datasource.DataSourceType`
- `com.mzc.backend.lms.common.config.datasource.RoutingDataSource`
- `com.mzc.backend.lms.common.config.datasource.DataSourceConfig`

**Docker:**
- `docker-compose.yml` (dev)
- `docker-compose.prod.yml` (production)
- `docker/mysql/primary/conf.d/my.cnf`
- `docker/mysql/replica/conf.d/my.cnf`

**Documentation:**
- `MULTI_DATASOURCE_GUIDE.md` (complete guide)
- `docker/mysql/REPLICATION_SETUP.md` (replication setup)
- `IMPLEMENTATION_SUMMARY.md` (implementation details)
- `IMPLEMENTATION_CHECKLIST.md` (verification checklist)

## Best Practices

1. **Always use `@Transactional(readOnly = true)` for read-only methods**
   ```java
   @Transactional(readOnly = true)
   public List<User> findAll() { ... }
   ```

2. **Use `@Transactional` (without readOnly) for writes**
   ```java
   @Transactional
   public void saveUser(User user) { ... }
   ```

3. **For critical reads after writes, use PRIMARY**
   ```java
   @Transactional  // Not readOnly - uses PRIMARY
   public User getUserAfterUpdate(Long id) {
       return userRepository.findById(id).orElse(null);
   }
   ```

4. **Monitor replication lag in production**
   ```sql
   SHOW SLAVE STATUS\G  -- Check Seconds_Behind_Master
   ```

5. **Start with routing disabled in dev, enable when ready**
   ```yaml
   spring.datasource.routing.enabled: false  # Safe default
   ```

## Common Mistakes

❌ **Forgetting `readOnly = true`**
```java
@Transactional  // This goes to PRIMARY, not REPLICA!
public User getUser(Long id) { ... }
```

✅ **Correct:**
```java
@Transactional(readOnly = true)  // This goes to REPLICA
public User getUser(Long id) { ... }
```

---

❌ **Expecting immediate consistency**
```java
@Transactional
public void updateUser(User user) {
    userRepository.save(user);  // PRIMARY
}

@Transactional(readOnly = true)
public User getUser(Long id) {
    return userRepository.findById(id);  // REPLICA - may be stale!
}
```

✅ **Correct:**
```java
@Transactional  // Both use PRIMARY
public User updateAndGetUser(Long id, User updates) {
    userRepository.save(updates);
    return userRepository.findById(id);
}
```

---

❌ **Not checking routing configuration**
```bash
# Just running the app without checking config
./gradlew bootRun
```

✅ **Correct:**
```bash
# Verify configuration first
grep "routing.enabled" application-dev.yaml
# Then run
./gradlew bootRun
```

## Quick Test

Test if routing is working:

```bash
# 1. Start app with routing enabled
SPRING_DATASOURCE_ROUTING_ENABLED=true ./gradlew bootRun

# 2. In another terminal, monitor connections
watch -n 1 "docker exec lms-mysql-primary mysql -uroot -prootpassword -e 'SHOW PROCESSLIST' | wc -l"
watch -n 1 "docker exec lms-mysql-replica mysql -uroot -prootpassword -e 'SHOW PROCESSLIST' | wc -l"

# 3. Make read request (should increment replica connections)
curl http://localhost:8080/api/users

# 4. Make write request (should increment primary connections)
curl -X POST http://localhost:8080/api/users -d '{...}'
```

## Support

- **Full Documentation:** [MULTI_DATASOURCE_GUIDE.md](MULTI_DATASOURCE_GUIDE.md)
- **Setup Guide:** [docker/mysql/REPLICATION_SETUP.md](docker/mysql/REPLICATION_SETUP.md)
- **Implementation Details:** [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)

---
**Last Updated:** January 20, 2026
**Version:** 1.0
**Issue:** #18

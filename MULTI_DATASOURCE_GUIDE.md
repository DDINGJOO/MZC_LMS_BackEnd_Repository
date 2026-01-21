# Multi-DataSource Configuration Guide

This document explains the Multi-Database configuration with Read/Write replica separation implemented for Issue #18.

## Overview

The application now supports Read/Write database separation using Spring's `AbstractRoutingDataSource`. This configuration allows:
- **Write operations** (INSERT, UPDATE, DELETE) to route to the **Primary (Master)** database
- **Read operations** (SELECT) to route to the **Replica (Slave)** database

This improves performance by distributing the load between Primary and Replica databases.

## Architecture

```
┌─────────────────┐
│   Application   │
└────────┬────────┘
         │
    ┌────▼───────────────────┐
    │ LazyConnectionProxy    │
    └────┬───────────────────┘
         │
    ┌────▼──────────────────┐
    │  RoutingDataSource    │
    └────┬──────────────┬───┘
         │              │
    ┌────▼────┐    ┌───▼─────┐
    │ PRIMARY │    │ REPLICA │
    │ (Write) │    │ (Read)  │
    └─────────┘    └─────────┘
```

## Components

### 1. DataSourceType Enum
Location: `com.mzc.backend.lms.common.config.datasource.DataSourceType`

Defines the two types of datasources:
- `PRIMARY`: For write operations (Master DB)
- `REPLICA`: For read operations (Slave DB)

### 2. RoutingDataSource
Location: `com.mzc.backend.lms.common.config.datasource.RoutingDataSource`

Extends `AbstractRoutingDataSource` to route queries based on transaction readOnly flag:
- `@Transactional(readOnly = true)` → Routes to **REPLICA**
- `@Transactional` or `@Transactional(readOnly = false)` → Routes to **PRIMARY**

### 3. DataSourceConfig
Location: `com.mzc.backend.lms.common.config.datasource.DataSourceConfig`

Configures:
- Primary DataSource with HikariCP
- Replica DataSource with HikariCP
- RoutingDataSource combining both
- LazyConnectionDataSourceProxy for deferred connection determination

**Conditional Activation:** Only enabled when `spring.datasource.routing.enabled=true`

### 4. LazyConnectionDataSourceProxy

Wraps the RoutingDataSource to defer the actual connection until it's needed. This ensures:
- Transaction readOnly attribute is set before datasource selection
- Proper routing based on `@Transactional(readOnly = true/false)`

## Configuration

### Development Environment (default: routing disabled)

File: `application-dev.yaml`

```yaml
spring:
  datasource:
    # Fallback single datasource (default)
    url: jdbc:mysql://localhost:3306/lms_db?...

    # Routing disabled by default for local dev
    routing:
      enabled: false

    # Primary datasource config
    primary:
      jdbc-url: jdbc:mysql://localhost:3306/lms_db?...
      hikari:
        maximum-pool-size: 10
        minimum-idle: 5

    # Replica datasource config
    replica:
      jdbc-url: jdbc:mysql://localhost:3307/lms_db?...
      hikari:
        maximum-pool-size: 10
        minimum-idle: 5
        read-only: true
```

### Production Environment (default: routing enabled)

File: `application-prod.yaml`

```yaml
spring:
  datasource:
    routing:
      enabled: true  # Enabled by default in production

    primary:
      jdbc-url: jdbc:mysql://${DATABASE_URL}:${DATABASE_PORT}/${DATABASE_NAME}?...
      hikari:
        maximum-pool-size: 20
        minimum-idle: 10

    replica:
      jdbc-url: jdbc:mysql://${DATABASE_REPLICA_URL}:${DATABASE_REPLICA_PORT}/${DATABASE_NAME}?...
      hikari:
        maximum-pool-size: 20
        minimum-idle: 10
        read-only: true
```

## Usage in Code

The routing is **completely transparent** to the business logic. Just use the `@Transactional` annotation correctly:

### Write Operations (routes to PRIMARY)

```java
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Routes to PRIMARY (Master DB)
    @Transactional
    public User createUser(UserCreateRequest request) {
        User user = User.builder()
            .email(request.getEmail())
            .name(request.getName())
            .build();
        return userRepository.save(user);
    }

    // Routes to PRIMARY (Master DB)
    @Transactional
    public void updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.update(request);
    }
}
```

### Read Operations (routes to REPLICA)

```java
@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    // Routes to REPLICA (Slave DB)
    @Transactional(readOnly = true)
    public User findUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    // Routes to REPLICA (Slave DB)
    @Transactional(readOnly = true)
    public Page<User> findUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // Routes to REPLICA (Slave DB)
    @Transactional(readOnly = true)
    public List<User> searchUsers(String keyword) {
        return userRepository.findByNameContaining(keyword);
    }
}
```

## Local Development Setup

### Option 1: Single Database (Default)

No changes needed. The application uses single datasource by default.

```yaml
spring:
  datasource:
    routing:
      enabled: false  # Default
```

### Option 2: With MySQL Replication

#### Step 1: Start Docker Containers

```bash
docker-compose up -d
```

This starts:
- `mysql-primary` on port 3306 (Master)
- `mysql-replica` on port 3307 (Slave)
- `redis` on port 6379

#### Step 2: Configure Replication

See [docker/mysql/REPLICATION_SETUP.md](docker/mysql/REPLICATION_SETUP.md) for detailed instructions.

Quick setup:

```bash
# Create replication user on primary
docker exec -it lms-mysql-primary mysql -uroot -prootpassword -e "
CREATE USER 'repl'@'%' IDENTIFIED BY 'repl_password';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
FLUSH PRIVILEGES;
SHOW MASTER STATUS;
"

# Note the File and Position, then configure replica
docker exec -it lms-mysql-replica mysql -uroot -prootpassword -e "
CHANGE MASTER TO
  MASTER_HOST='mysql-primary',
  MASTER_USER='repl',
  MASTER_PASSWORD='repl_password',
  MASTER_LOG_FILE='mysql-bin.000001',
  MASTER_LOG_POS=123;
START SLAVE;
SHOW SLAVE STATUS\G
"
```

#### Step 3: Enable Routing

Edit `application-dev.yaml`:

```yaml
spring:
  datasource:
    routing:
      enabled: true  # Enable routing
```

Or set environment variable:

```bash
export SPRING_DATASOURCE_ROUTING_ENABLED=true
```

## Production Deployment

### Environment Variables

Set these variables in your production environment:

```bash
# Primary (Master) Database
DATABASE_URL=primary-db-host
DATABASE_PORT=3306
DATABASE_USER=app_user
DATABASE_PASSWORD=secure_password
DATABASE_NAME=lms_db

# Replica (Slave) Database
DATABASE_REPLICA_URL=replica-db-host
DATABASE_REPLICA_PORT=3306
DATABASE_REPLICA_USER=app_user  # Optional, defaults to DATABASE_USER
DATABASE_REPLICA_PASSWORD=secure_password  # Optional, defaults to DATABASE_PASSWORD

# Enable routing
DATABASE_ROUTING_ENABLED=true
```

### Docker Compose Production

#### Without Replica (single database)

```bash
docker-compose -f docker-compose.prod.yml up -d
```

#### With Replica

```bash
docker-compose -f docker-compose.prod.yml --profile with-replica up -d
```

This starts both `mysql` (primary) and `mysql-replica` services.

## Monitoring and Verification

### Check Current Routing

Add debug logging to see which datasource is being used:

```yaml
logging:
  level:
    com.mzc.backend.lms.common.config.datasource: DEBUG
```

### Verify Replication Status

```bash
# Check primary
docker exec -it lms-mysql-primary mysql -uroot -p${DATABASE_PASSWORD} -e "SHOW MASTER STATUS"

# Check replica
docker exec -it lms-mysql-replica mysql -uroot -p${DATABASE_PASSWORD} -e "SHOW SLAVE STATUS\G"
```

Key metrics to monitor:
- `Slave_IO_Running: Yes`
- `Slave_SQL_Running: Yes`
- `Seconds_Behind_Master: 0` (or small number)

### Test Routing

Create a test endpoint:

```java
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class DataSourceTestController {

    private final UserRepository userRepository;

    @GetMapping("/read")
    @Transactional(readOnly = true)
    public String testRead() {
        userRepository.findAll();
        return "Read from REPLICA";
    }

    @PostMapping("/write")
    @Transactional
    public String testWrite() {
        User user = new User();
        userRepository.save(user);
        return "Write to PRIMARY";
    }
}
```

## Troubleshooting

### Issue: Always routing to PRIMARY

**Cause:** LazyConnectionDataSourceProxy not properly configured

**Solution:** Ensure `dataSource()` bean returns `LazyConnectionDataSourceProxy`:

```java
@Primary
@Bean
public DataSource dataSource() {
    return new LazyConnectionDataSourceProxy(routingDataSource());
}
```

### Issue: Replication lag

**Cause:** High write volume on Primary

**Solutions:**
1. Monitor `Seconds_Behind_Master` on replica
2. Increase replica resources
3. Consider multiple replicas with load balancing

### Issue: Connection pool exhausted

**Cause:** Insufficient pool size configuration

**Solution:** Increase HikariCP pool size:

```yaml
spring:
  datasource:
    primary:
      hikari:
        maximum-pool-size: 30  # Increase from default
    replica:
      hikari:
        maximum-pool-size: 30
```

### Issue: Routing not working

**Cause:** `spring.datasource.routing.enabled` not set to `true`

**Solution:** Enable routing:

```yaml
spring:
  datasource:
    routing:
      enabled: true
```

Or via environment variable:

```bash
SPRING_DATASOURCE_ROUTING_ENABLED=true
```

## Fallback to Single DataSource

If routing causes issues, you can easily fall back to single datasource:

```yaml
spring:
  datasource:
    routing:
      enabled: false  # Disable routing
```

The application will use the single datasource configuration:

```yaml
spring:
  datasource:
    url: jdbc:mysql://...
    username: ...
    password: ...
```

## Best Practices

1. **Always use `@Transactional(readOnly = true)` for read operations**
   - This is essential for proper routing
   - Also provides performance benefits (no transaction overhead)

2. **Keep replication lag low**
   - Monitor `SHOW SLAVE STATUS` regularly
   - Alert if `Seconds_Behind_Master > 10`

3. **Use connection pooling wisely**
   - Primary pool: smaller (10-20 connections)
   - Replica pool: larger (20-50 connections) - more reads than writes

4. **Test routing in development**
   - Enable routing locally to test before production
   - Verify both read and write operations

5. **Monitor datasource metrics**
   - Active connections
   - Pool usage
   - Query latency per datasource

## Future Enhancements (Phase 2)

This configuration prepares for future domain-based database separation:

```
┌─────────────────┐
│   Application   │
└────────┬────────┘
         │
    ┌────▼────────────────────┐
    │ DomainRoutingDataSource │
    └─┬──────┬──────┬─────────┘
      │      │      │
   ┌──▼──┐ ┌▼───┐ ┌▼──────┐
   │User │ │Course│Content│
   │ DB  │ │ DB  │ │  DB  │
   └─┬─┬─┘ └┬──┬─┘ └┬────┬─┘
     │ │    │  │    │    │
    P  R   P  R   P    R
```

Each domain database will have its own Primary/Replica setup.

## References

- Spring Framework Documentation: [AbstractRoutingDataSource](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/datasource/lookup/AbstractRoutingDataSource.html)
- HikariCP Configuration: [HikariCP GitHub](https://github.com/brettwooldridge/HikariCP)
- MySQL Replication: [MySQL Documentation](https://dev.mysql.com/doc/refman/8.0/en/replication.html)

## Support

For issues or questions:
1. Check this guide first
2. Review [docker/mysql/REPLICATION_SETUP.md](docker/mysql/REPLICATION_SETUP.md)
3. Check application logs with DEBUG level
4. Verify replication status with `SHOW SLAVE STATUS`

# MySQL Replication Setup Guide

This guide explains how to set up MySQL Master-Slave replication for local development.

## Quick Start

### 1. Start Docker Containers

```bash
docker-compose up -d
```

### 2. Set Up Replication (First Time Only)

#### a. Create Replication User on Primary

```bash
docker exec -it lms-mysql-primary mysql -uroot -prootpassword
```

In MySQL prompt:

```sql
CREATE USER 'repl'@'%' IDENTIFIED BY 'repl_password';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
FLUSH PRIVILEGES;
SHOW MASTER STATUS;
```

Note the `File` and `Position` values from `SHOW MASTER STATUS` output.

#### b. Configure Replica

```bash
docker exec -it lms-mysql-replica mysql -uroot -prootpassword
```

In MySQL prompt (replace `File` and `Position` with values from step a):

```sql
CHANGE MASTER TO
  MASTER_HOST='mysql-primary',
  MASTER_USER='repl',
  MASTER_PASSWORD='repl_password',
  MASTER_LOG_FILE='mysql-bin.000001',  -- Replace with actual file
  MASTER_LOG_POS=123;                  -- Replace with actual position

START SLAVE;
SHOW SLAVE STATUS\G
```

Verify that:
- `Slave_IO_Running: Yes`
- `Slave_SQL_Running: Yes`
- `Seconds_Behind_Master: 0`

### 3. Enable Routing in Application

Edit `application-dev.yaml`:

```yaml
spring:
  datasource:
    routing:
      enabled: true  # Change from false to true
```

## Testing Replication

### Test Write on Primary

```bash
docker exec -it lms-mysql-primary mysql -uroot -prootpassword -Dlms_db
```

```sql
CREATE TABLE test_repl (id INT PRIMARY KEY, data VARCHAR(50));
INSERT INTO test_repl VALUES (1, 'test data');
SELECT * FROM test_repl;
```

### Verify Read from Replica

```bash
docker exec -it lms-mysql-replica mysql -uroot -prootpassword -Dlms_db
```

```sql
SELECT * FROM test_repl;  -- Should show the same data
```

## Connection Details

### Primary (Master)
- Host: localhost
- Port: 3306
- Database: lms_db
- User: root / lmsuser
- Password: rootpassword / lmspassword

### Replica (Slave)
- Host: localhost
- Port: 3307
- Database: lms_db
- User: root / lmsuser
- Password: rootpassword / lmspassword

## Application Configuration

### Development (routing disabled by default)

```yaml
spring:
  datasource:
    routing:
      enabled: false  # Use single datasource
```

### Development with Routing (after replication setup)

```yaml
spring:
  datasource:
    routing:
      enabled: true  # Enable Read/Write separation
```

Environment variables:
```bash
DATABASE_URL=localhost
DATABASE_PORT=3306
DATABASE_REPLICA_URL=localhost
DATABASE_REPLICA_PORT=3307
DATABASE_NAME=lms_db
DATABASE_USER=root
DATABASE_PASSWORD=rootpassword
```

### Production

```yaml
spring:
  datasource:
    routing:
      enabled: true  # Always enabled in production
```

Environment variables:
```bash
DATABASE_URL=primary-db-host
DATABASE_PORT=3306
DATABASE_REPLICA_URL=replica-db-host
DATABASE_REPLICA_PORT=3306
DATABASE_NAME=lms_db
DATABASE_USER=app_user
DATABASE_PASSWORD=secure_password
```

## How It Works

### Automatic Routing

The application automatically routes queries based on `@Transactional` annotation:

```java
// Routes to PRIMARY (Master)
@Transactional
public void createUser(User user) {
    userRepository.save(user);
}

// Routes to REPLICA (Slave)
@Transactional(readOnly = true)
public User findUser(Long id) {
    return userRepository.findById(id).orElse(null);
}
```

### Components

1. **DataSourceType**: Enum defining PRIMARY and REPLICA
2. **RoutingDataSource**: Routes based on transaction readOnly flag
3. **DataSourceConfig**: Configures both datasources with HikariCP
4. **LazyConnectionDataSourceProxy**: Defers connection until needed

## Troubleshooting

### Replication Not Working

```bash
# Check primary status
docker exec -it lms-mysql-primary mysql -uroot -prootpassword -e "SHOW MASTER STATUS"

# Check replica status
docker exec -it lms-mysql-replica mysql -uroot -prootpassword -e "SHOW SLAVE STATUS\G"
```

### Reset Replication

If replication breaks, reset on replica:

```sql
STOP SLAVE;
RESET SLAVE;
-- Then re-run CHANGE MASTER TO command
START SLAVE;
```

### Single DataSource Fallback

If routing causes issues, disable it:

```yaml
spring:
  datasource:
    routing:
      enabled: false
```

The application will fall back to the single datasource configuration.

## Best Practices

1. **Development**: Start with routing disabled, enable after testing
2. **Production**: Always enable routing for better performance
3. **Monitoring**: Monitor replication lag with `SHOW SLAVE STATUS`
4. **Testing**: Test both read and write operations after enabling routing
5. **Fallback**: Keep single datasource config as fallback option

## Phase 2: Multi-Database (Future)

This setup prepares for future domain-based database separation:
- User Domain DB
- Course Domain DB
- Content Domain DB
- Each with their own Primary/Replica setup

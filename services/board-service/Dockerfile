# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy gradle files
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew .

# Download dependencies
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# Copy source and build
COPY src src
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -g 1000 spring && adduser -u 1000 -G spring -s /bin/sh -D spring

# Copy jar from builder
COPY --from=builder /app/build/libs/board-service.jar app.jar

# Change ownership
RUN chown -R spring:spring /app

USER spring

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8092/actuator/health || exit 1

EXPOSE 8092

ENTRYPOINT ["java", "-jar", "app.jar"]

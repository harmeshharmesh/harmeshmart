# ==============================================================================
# HarmeshMart - Multi-Stage Production Dockerfile for Render.com Deployment
# Stack: Java 17, Apache Tomcat 9, Maven, H2 In-Memory / File Database
# ==============================================================================

# ------------------------------------------------------------------------------
# Stage 1: Build Application WAR with Maven & Eclipse Temurin JDK 17
# ------------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Cache Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source code and resources
COPY src ./src

# Build production WAR (skipping test execution during Docker image build)
RUN mvn clean package -DskipTests

# ------------------------------------------------------------------------------
# Stage 2: Production Runtime with Apache Tomcat 9 & Eclipse Temurin JRE 17
# ------------------------------------------------------------------------------
FROM tomcat:9.0.86-jre17-temurin-jammy

LABEL maintainer="HarmeshMart <admin@harmeshmart.com>"
LABEL description="HarmeshMart Amazon-like Multi-Vendor E-Commerce Platform"

# Clean up default Tomcat apps (examples, docs, ROOT, etc.) to free resources
RUN rm -rf /usr/local/tomcat/webapps/*

# Create directory for persistent file-based H2 database (if Render Disk is mounted)
RUN mkdir -p /var/data/harmeshmart && chmod -R 777 /var/data

# Copy built WAR file:
# 1. Deploy as ROOT.war so HarmeshMart serves directly from domain root (https://<app>.onrender.com/)
COPY --from=builder /build/target/HarmeshMart.war /usr/local/tomcat/webapps/ROOT.war

# 2. Deploy also as HarmeshMart.war for backward-compatible routing (/HarmeshMart/...)
COPY --from=builder /build/target/HarmeshMart.war /usr/local/tomcat/webapps/HarmeshMart.war

# Copy entrypoint script for dynamic Render $PORT binding & memory optimization
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN tr -d '\r' < /usr/local/bin/docker-entrypoint.sh > /usr/local/bin/entrypoint-fixed.sh && \
    mv /usr/local/bin/entrypoint-fixed.sh /usr/local/bin/docker-entrypoint.sh && \
    chmod +x /usr/local/bin/docker-entrypoint.sh

# Default environment variables
ENV PORT=8080 \
    DB_URL="jdbc:h2:mem:harmeshmart;DB_CLOSE_DELAY=-1" \
    DB_USERNAME="sa" \
    DB_PASSWORD="" \
    AI_CHATBOT_PROVIDER="mock"

# Render maps dynamic PORT; default container port is 8080
EXPOSE 8080

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]

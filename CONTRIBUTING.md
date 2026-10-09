# Contributing to HarmeshMart

Welcome to **HarmeshMart**! This document outlines the exact steps to clone, build, test, and run the project locally.

## Prerequisites
- **JDK 17** (Temurin 17 LTS recommended)
- **Apache Maven 3.8+**
- **Apache Tomcat 9.0.x**

## Local Setup & Execution Guide

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/HarmeshMart.git
cd HarmeshMart
```

### 2. Configure Environment
A default embedded H2 configuration is provided in `src/main/resources/config.properties`.
If you wish to override settings, copy `.env.example` to `.env`:
```bash
cp .env.example .env
```

### 3. Build the Application
Compile the code, run all JUnit 5 & Mockito test suites, and generate the deployable WAR:
```bash
mvn clean package
```
This produces `target/HarmeshMart.war`.

### 4. Deploy to Apache Tomcat 9.0.x
Copy `HarmeshMart.war` into your Tomcat `webapps/` directory:
- **Windows:**
  ```powershell
  Copy-Item target/HarmeshMart.war C:\Users\h\tools\apache-tomcat-9.0.96\webapps\
  C:\Users\h\tools\apache-tomcat-9.0.96\bin\startup.bat
  ```
- **Linux / macOS:**
  ```bash
  cp target/HarmeshMart.war /opt/tomcat/webapps/
  /opt/tomcat/bin/startup.sh
  ```

### 5. Access the Application
Open your browser and navigate to:
```
http://localhost:8080/HarmeshMart/
```

### 6. Verify Health Check
Confirm the application and H2 database are online:
```
http://localhost:8080/HarmeshMart/api/v1/health
```
Expected JSON output:
```json
{
  "status": "UP",
  "db": "UP",
  "timestamp": 1728475200000
}
```

## Branching & Commit Guidelines
- Use conventional commit messages: `feat:`, `fix:`, `test:`, `docs:`.
- All features developed in `feature/<name>` branches.
- PRs must pass `mvn clean verify` before merging to `main`.

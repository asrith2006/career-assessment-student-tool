# Career Assessment System - Environment Setup Guide

## Backend Environment Configuration

### 1. MySQL Configuration
```yaml
# File: application.yml

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/career_assessment_db
    username: root
    password: kundhan@183
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 2. JWT Configuration
```yaml
jwt:
  secret: your-super-secret-key-change-this-in-production-with-a-long-random-string
  expiration: 86400000  # 24 hours in milliseconds
```

### 3. Server Configuration
```yaml
server:
  port: 8080
  servlet:
    context-path: /api
```

### 4. Logging Configuration
```yaml
logging:
  level:
    root: INFO
    com.careers.assessment: DEBUG
```

### 5. JPA Configuration
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # Options: none, validate, update, create-drop
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect
```

## Frontend Environment Configuration

### 1. API Base URL
```javascript
// File: src/services/axiosInstance.js

const API_BASE_URL = 'http://localhost:8080/api';  // Development
// const API_BASE_URL = 'https://api.example.com/api';  // Production
```

### 2. CORS Configuration
The backend accepts requests from:
- http://localhost:3000 (React dev server)
- http://localhost:5173 (Alternative dev server)

Add more origins in `SecurityConfig.java` if needed.

## Production Environment Variables

### Backend (application-prod.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://db-server:3306/career_assessment_db
    username: db_user
    password: ${DB_PASSWORD}  # Set from environment
    
jwt:
  secret: ${JWT_SECRET}  # Must be long and random
  expiration: 86400000

server:
  port: 8080
  ssl:
    key-store: /path/to/keystore.jks
    key-store-password: ${KEYSTORE_PASSWORD}
    key-store-type: JKS

logging:
  level:
    root: WARN
    com.careers.assessment: INFO
```

### Frontend (.env.production)
```
VITE_API_URL=https://api.example.com/api
VITE_APP_NAME=Career Assessment System
```

## Docker Configuration (Optional)

### Dockerfile for Backend
```dockerfile
FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/assessment-system-1.0.0.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java","-jar","/application.jar"]
```

### Dockerfile for Frontend
```dockerfile
FROM node:16-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

## Environment Variables Reference

### Backend Environment Variables
```bash
# Database
DB_URL=jdbc:mysql://localhost:3306/career_assessment_db
DB_USERNAME=root
DB_PASSWORD=root

# JWT
JWT_SECRET=your-super-secret-key-change-this-in-production
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080
SERVER_SERVLET_CONTEXT_PATH=/api

# Logging
LOG_LEVEL=DEBUG
```

### Frontend Environment Variables
```bash
# API Configuration
VITE_API_URL=http://localhost:8080/api
VITE_API_TIMEOUT=30000

# App Configuration
VITE_APP_NAME=Career Assessment System
VITE_APP_VERSION=1.0.0
```

## Configuration Profiles

### Development Profile
```bash
# No special configuration needed
# Uses default settings from application.yml
```

### Production Profile
```bash
# Run with production profile:
java -jar assessment-system-1.0.0.jar --spring.profiles.active=prod

# Or set environment variable:
export SPRING_PROFILES_ACTIVE=prod
```

## Security Checklist for Production

- [ ] Change JWT secret to a long random string
- [ ] Update database credentials
- [ ] Enable HTTPS/SSL
- [ ] Restrict CORS to specific domains
- [ ] Set strong database password
- [ ] Enable database backups
- [ ] Configure firewall rules
- [ ] Enable logging and monitoring
- [ ] Set up automated security updates
- [ ] Use environment variables for sensitive data
- [ ] Implement rate limiting
- [ ] Setup email notifications

## Quick Environment Setup

### Development
```bash
# Backend
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/career_assessment_db
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=root
export JWT_SECRET=dev-secret-key

# Frontend
export VITE_API_URL=http://localhost:8080/api
```

### Production
```bash
# Backend
export SPRING_DATASOURCE_URL=jdbc:mysql://prod-db-server:3306/career_assessment_db
export SPRING_DATASOURCE_USERNAME=prod_user
export SPRING_DATASOURCE_PASSWORD=$PROD_DB_PASSWORD
export JWT_SECRET=$PROD_JWT_SECRET

# Frontend
export VITE_API_URL=https://api.yourdomain.com/api
```

## Configuration Priority

Spring Boot loads properties in this order (lowest to highest priority):
1. `application.properties`
2. `application.yml`
3. `application-{profile}.yml`
4. Environment variables
5. Command line arguments

Example override:
```bash
java -jar app.jar --spring.datasource.password=new-password
```

## Troubleshooting Configuration

**Issue**: Application fails to start
- Check `application.yml` syntax
- Verify database connection
- Check port availability
- Review error logs

**Issue**: JWT expires too quickly
- Increase `jwt.expiration` value
- Default is 86400000 ms (24 hours)

**Issue**: CORS errors
- Verify origin in `SecurityConfig.java`
- Check browser console for actual error
- Ensure credentials flag is set correctly

**Issue**: Database connection fails
- Verify MySQL is running
- Check credentials in `application.yml`
- Ensure database exists
- Check network connectivity

## Additional Resources

- Spring Boot Docs: https://spring.io/projects/spring-boot
- React Docs: https://react.dev
- MySQL Docs: https://dev.mysql.com/doc
- JWT Docs: https://jwt.io

---

**Last Updated**: April 6, 2024

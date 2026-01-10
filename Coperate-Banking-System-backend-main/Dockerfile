# Backend Dockerfile - For Pre-built JAR
# Location: corporate-banking-backend/Dockerfile

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the pre-built JAR file
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
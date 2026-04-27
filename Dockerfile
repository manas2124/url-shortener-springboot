# Use Java 17 (LTS)
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/urlshortener-0.0.1-SNAPSHOT.jar app.jar
# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-jar","/app.jar"]
# Use official Java image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/DAG2iLL-0.0.1-SNAPSHOT.jar app.jar




# Expose port (Render uses PORT env variable)
EXPOSE 8080

# Run Spring Boot app
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT"]

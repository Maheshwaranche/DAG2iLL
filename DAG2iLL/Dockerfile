FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build JAR
RUN ./mvnw clean package -DskipTests

# Use a smaller base image for running the jar
FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY --from=build /app/target/DAG2iLL-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT"]

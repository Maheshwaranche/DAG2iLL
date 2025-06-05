FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app
COPY Dag2Ill/Dag2iLL /app


# Build JAR
RUN mvn clean package -DskipTests


FROM maven:3.8.3-openjdk-17-slim

COPY --from=build /target/DAG2iLL-0.0.1-SNAPSHOT.jar DAG2iLL.jar

EXPOSE 8080

ENTRYPOINT [ "java","-jar","DAG2iLL.jar"]


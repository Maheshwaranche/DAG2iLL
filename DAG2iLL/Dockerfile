FROM maven:3.8.5-openjdk-17 AS build


COPY DAG2iLL/. . 


# Build JAR
RUN mvn clean package -DskipTests


FROM maven:3.8.3-openjdk-17-slim

COPY --from=build /target/DAG2iLL-0.0.1-SNAPSHOT.jar DAG2iLL.jar



ENTRYPOINT [ "java","-jar","DAG2iLL.jar"]


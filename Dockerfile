# # Use a Java base image
# FROM openjdk:17-alpine

# # Set the working directory to /app
# WORKDIR /app

# # Copy the Spring Boot application JAR file into the Docker image
# COPY target/pvh_pholsophak_spring-0.0.1-SNAPSHOT.jar /app/pvh_pholsophak_spring-0.0.1-SNAPSHOT.jar

# # Run the Spring Boot application when the container starts
# CMD ["java", "-jar", "pvh_pholsophak_spring-0.0.1-SNAPSHOT.jar"]

===========================================================
===========================================================

# Builder stage
FROM maven:3.8.4-openjdk-11 AS builder
WORKDIR /workspace/source
COPY . .
RUN mvn clean install

# Final stage
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /workspace/source/target/pvh_pholsophak_spring-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-jar", "pvh_pholsophak_spring-0.0.1-SNAPSHOT.jar"]

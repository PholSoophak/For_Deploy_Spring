# Use a Java base image
FROM openjdk:17-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the Spring Boot application JAR file into the Docker image
COPY target/spring_maven_customer_api-0.0.1-SNAPSHOT.jar /app/spring_maven_customer_api-0.0.1-SNAPSHOT.jar

# Run the Spring Boot application when the container starts
CMD ["java", "-jar", "spring_maven_customer_api-0.0.1-SNAPSHOT.jar"]


# Use a base image with Java 17 and Alpine Linux
FROM eclipse-temurin:17-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/*.jar /app/gateway.jar

# Expose the port that your Spring Cloud Gateway application will run on
EXPOSE 8080

# Specify the command to run on container startup
CMD ["java", "-jar", "gateway.jar"]
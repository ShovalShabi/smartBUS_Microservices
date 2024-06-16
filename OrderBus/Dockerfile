# Use a base image that includes OpenJDK
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY target/orderbus-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your application will run on
EXPOSE 10093

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

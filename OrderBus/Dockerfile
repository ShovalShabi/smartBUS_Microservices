# Use a base image that includes OpenJDK
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the build.gradle files
COPY build.gradle .
COPY gradlew.bat .
COPY gradle .

# Copy the source code
COPY src ./src

# Build the application
RUN gradle build

# Use a lightweight base image for the server
FROM openjdk:17

WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/orderbus-0.0.1-SNAPSHOT.jar /app/

# Expose the port your application will run on
EXPOSE 10093

# Run the application
ENTRYPOINT ["java", "-jar", "app/orderbus-0.0.1-SNAPSHOT.jar"]

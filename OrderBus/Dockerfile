# Stage 1: Build the application
FROM gradle:jdk17 as builder

# Set the working directory
WORKDIR /home/gradle/src

# Copy the project files
COPY --chown=gradle:gradle . .

# Build the project
RUN gradle build

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=builder /home/gradle/src/build/libs/orderbus-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your application will run on
EXPOSE 10093

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

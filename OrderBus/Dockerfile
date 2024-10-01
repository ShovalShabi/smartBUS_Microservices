# Stage 1: Build the application with a smaller image
FROM gradle:jdk17-alpine as builder

# Set the working directory
WORKDIR /home/gradle/src

# Copy the project files
COPY --chown=gradle:gradle . .

# Build the project, skipping tests, and clean Gradle caches to reduce image size
RUN gradle build -x test --no-daemon && \
    rm -rf /home/gradle/.gradle/caches/*

# Stage 2: Create the final image using a slimmed-down JRE
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

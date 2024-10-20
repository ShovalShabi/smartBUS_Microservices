
# Project Overview

This project is a microservice-based system designed to run several services, including **Auth**, **Feedback**, **GTFS**, **OrderBus**, and **Routes** services. These services communicate with PostgreSQL and Redis databases, and are designed to run in either **development** or **production** environments.

The services can be run using **IntelliJ IDEA** or **Docker Containers**, depending on your preference. This README will walk you through both options, as well as provide details on running tests.

---

## Project Summary

This project is part of a final project in a Software Engineering degree aimed at improving public transportation in Israel, specifically focusing on buses. The project focuses on saving passengers time, rating bus agencies, reducing driving time for drivers, and helping bus agencies manage their resources.

Each service has a specific purpose:
- **Auth Service**: Manages user authentication (Bus Drivers and Agency Administrators).
- **Feedback Service**: Collects user feedback and ratings to improve agency services.
- **Routes Service**: Interfaces with Google Routes API to generate driving routes for bus drivers.
- **OrderBus Service**: Acts as the intermediary between passengers and drivers, handling notifications and retrieving bus line and station data.

---

## Prerequisites

Before running the project, ensure you have the following tools installed:

### For running via IntelliJ IDEA:

1. [Java 17 JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
2. [Gradle](https://gradle.org/install/)
3. [PostgreSQL](https://www.postgresql.org/download/)
4. [Redis](https://redis.io/download)
5. [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)

### For running via Docker Containers:

1. [Docker](https://docs.docker.com/get-docker/)
2. [Docker Compose](https://docs.docker.com/compose/install/)

Ensure Docker is running on your machine before proceeding with the Docker setup.

---

## Running the Project via IntelliJ IDEA

### Step 1: Set up PostgreSQL and Redis Databases

1. Install PostgreSQL and Redis on your machine (see [Prerequisites](#prerequisites)).
2. Ensure PostgreSQL is running on `localhost:5432` with the credentials set in your service configuration (`POSTGRES_USER=postgres`, `POSTGRES_PASSWORD=12683579`).
3. Run Redis on the default port `6379`.

### Step 2: Clone the Project

```bash
git clone https://github.com/ShovalShabi/smartBUS_Microservices.git
cd smartBUS_Microservices
```

### Step 3: Open the Project in IntelliJ IDEA

1. Open IntelliJ IDEA and click on **Open**.
2. Select the root directory of the project.

### Step 4: Configure Run Configurations

For each service (**Auth**, **Feedback**, **GTFS**, **OrderBus**, and **Routes**):
1. Go to **Run** -> **Edit Configurations**.
2. Add a new **Application** configuration.
3. Set the **Main class** to the main entry point of each service (e.g., `club.smartbus.AuthApplication`).
4. Set the environment variable `SPRING_PROFILES_ACTIVE=dev` for the development environment or `SPRING_PROFILES_ACTIVE=prod` for the production environment.

### Step 5: Run the Services

Run each service individually from IntelliJ by selecting the run configuration for each service and clicking **Run**.

---

## Running the Project via Docker

### Step 1: Set up Docker and Docker Compose

Ensure Docker and Docker Compose are installed and running on your machine (see [Prerequisites](#prerequisites)).

### Step 2: Build and Run the Docker Containers

To run the services via Docker, use the `compose-*.yaml` files that provided in the root directory.

1. Navigate to the project root directory:
   ```bash
   cd smartBUS
   ```

2. Build and run the services in **development** mode with detached containers:
   ```bash
   docker-compose -f compose-dev.yaml up --build -d 
   ```

   For **production** mode, build and run the services with detached containers:
   ```bash
   docker-compose -f compose-prod.yaml up --build -d
   ```

### Step 3: Access the Services

Each service will be exposed on the respective port as defined in the `compose-dev.yaml` file:
- **Auth service (dev)**: `http://localhost:3804`
- **Feedback service (dev)**: `http://localhost:5003`
- **OrderBus service (dev)**: `http://localhost:6936`
- **Routes service (dev)**: `http://localhost:6924`
- **GTFS service**: `http://localhost:8000`

For production:
- **Auth service (prod)**: `http://localhost:10000`
- **Feedback service (prod)**: `http://localhost:10050`
- **OrderBus service (prod)**: `http://localhost:10093`
- **Routes service (prod)**: `http://localhost:10094`
---

## Running Tests

### Running Tests via IntelliJ IDEA

1. Right-click on the `src/test` directory in each service module.
2. Select **Run Tests** to run all the tests for that specific service.

This will run the tests for each service in an isolated environment.

---

## Environment Segregation

This project supports running in both **development** and **production** environments. You can switch between the environments by setting the `SPRING_PROFILES_ACTIVE` environment variable.

- **Development Environment**: Set `SPRING_PROFILES_ACTIVE=dev`.
- **Production Environment**: Set `SPRING_PROFILES_ACTIVE=prod`.

In **development** mode, the services use development configurations, and in **production** mode, they use production-grade configurations for performance and security.

---

#### © All rights reserved to Shoval Shabi and Tamir Spilberg
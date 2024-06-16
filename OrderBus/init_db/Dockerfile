# Stage 1: Clone the repository and copy files
FROM alpine:3.12 AS clone_stage

ARG GIT_REPO_URL
WORKDIR /repo

RUN apk add --no-cache git \
    && git clone ${GIT_REPO_URL} .

# Stage 2: Build the Python environment
FROM python:3.10-slim

# Set the working directory
WORKDIR /usr/src/app

# Install build tools and a C compiler (assuming Debian/Ubuntu-based system)
RUN apt-get update && apt-get install -y build-essential

# Copy the requirements file from the cloned repository
COPY --from=clone_stage /repo/requirements.txt ./

# Copy the configuration file from the cloned repository
COPY gtfs_configuration.yaml config.yaml

# Install any needed packages specified in requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Copy the entire repository from the clone stage
COPY --from=clone_stage /repo /usr/src/app

# Define the command to run your application
CMD ["python", "main.py", "--config", "config.yaml"]

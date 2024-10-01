# Use the official Python image as the base
FROM python:3.9-slim

# Set the working directory inside the container
WORKDIR /app

# Install the necessary system packages
RUN apt-get update && apt-get install -y \
    git \
    && rm -rf /var/lib/apt/lists/*

# Clone the GtfsScript repository
RUN git clone https://github.com/tamir303/GtfsScript.git

# Change to the GtfsScript directory
WORKDIR /app/GtfsScript

# Install the required Python dependencies
RUN pip install -r requirements.txt

# Copy the config.yaml file into the correct location inside the container
COPY config.yaml /app/GtfsScript/config/config.yaml

# Expose the GTFS public directory for external data
VOLUME /app/GtfsScript/public

# Command to run the script
CMD ["python", "main.py", "--config", "/app/GtfsScript/config/config.yaml"]

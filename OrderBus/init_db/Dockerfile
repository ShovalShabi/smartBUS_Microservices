FROM python:3.9

WORKDIR /usr/src/app

# Install git
RUN apt-get update && apt-get install -y git

# Clone the repository
ARG GIT_REPO_URL
RUN git clone ${GIT_REPO_URL} repo

WORKDIR /usr/src/app/repo

# Copy the configuration file
COPY gtfs_configuration.yaml .

# Copy the rest of the application code
COPY . .

# Install Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Set entrypoint to run the initialization script
ENTRYPOINT ["python", "main.py", "--config" ,"gtfs_configuration.yaml"]

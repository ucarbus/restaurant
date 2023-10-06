#!/bin/bash

# Check if Docker is installed
if ! [ -x "$(command -v docker)" ]; then
  echo "Error: Docker is not installed. Please install Docker and try again."
  exit 1
fi

# Build the Docker image
docker build -t wolt-app .

# Run the Docker container
docker run -p 8080:8080 wolt-app



#!/bin/bash
set -e

mvn clean install
mkdir -p plugin-volume
cp target/sonar-example-plugin-8.1.0.jar plugin-volume
docker-compose up 

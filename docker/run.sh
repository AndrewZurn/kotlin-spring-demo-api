#!/bin/bash

set -e

echo "Starting Kotlin Spring Boot Demo application."
java -server \
  -Xms4g \
  -Xmx4g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=100 \
  -XX:+UseStringDeduplication \
  -jar /deploy/*.jar

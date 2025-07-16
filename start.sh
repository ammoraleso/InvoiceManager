#!/bin/bash

echo "Checking Java version..."

# Get the java version string
JAVA_VERSION_OUTPUT=$(java -version 2>&1)
echo "$JAVA_VERSION_OUTPUT"

# Extract major version (assumes java version "23" or "23.0.1" format)
JAVA_MAJOR_VERSION=$(java -version 2>&1 | grep 'version' | sed -E 's/.*"([0-9]+).*/\1/')

echo "Detected Java major version: $JAVA_MAJOR_VERSION"

if [ "$JAVA_MAJOR_VERSION" -ne 23 ]; then
  echo "❌ ERROR: Java 23 is required to run this project. Detected Java $JAVA_MAJOR_VERSION."
  exit 1
fi

echo "✅ Java 23 detected. Proceeding..."

echo "Building project with Maven..."
mvn clean package -DskipTests

echo "Starting InvoiceManager..."
java -jar target/InvoiceManager-1.0-SNAPSHOT.jar

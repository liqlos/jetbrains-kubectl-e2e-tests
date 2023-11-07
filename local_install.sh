#!/bin/bash

KUBECTL_VERSION="v1.28.3"
KIND_VERSION="v0.20.0"
CLUSTER_NAME="test"

# Check if jre or jdk is installed
if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME is not set. Please install JDK or JRE and set the JAVA_HOME environment variable."
    exit 1
fi

# Check if Docker is running
if ! docker ps &> /dev/null; then
    echo "Docker is not installed or not running. Please install or start Docker."
    exit 1
fi

echo "Installing kind"
wget -qO kind "https://kind.sigs.k8s.io/dl/$KIND_VERSION/kind-linux-amd64"
chmod +x kind
sudo mv kind /usr/local/bin/

echo "Installing kubectl $KUBECTL_VERSION"
wget -qO kubectl "https://dl.k8s.io/release/$KUBECTL_VERSION/bin/linux/amd64/kubectl"
chmod +x kubectl
sudo mv kubectl /usr/local/bin/

echo "Creating k8s cluster with kind"
kind create cluster --name $CLUSTER_NAME --config src/test/resources/kind-config.yml --wait 300s

echo "Executing tests"
./gradlew test

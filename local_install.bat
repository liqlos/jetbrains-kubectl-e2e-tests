@echo off
set KUBECTL_VERSION=v1.28.3
set KIND_VERSION=v0.20.0
set CLUSTER_NAME=test

:: Check if JAVA_HOME is empty
if "%JAVA_HOME%" == "" (
    echo JAVA_HOME is not set. Please install JDK or JRE and set the JAVA_HOME environment variable.
    pause
    exit
)

:: Check if Docker is running
docker ps
:: if previous command return code != 0
if %errorlevel% NEQ 0 (
    echo Docker is not installed or not running. Please install or start Docker.
    pause
    exit
)

echo Installing kind
curl -Lo kind.exe https://kind.sigs.k8s.io/dl/v0.20.0/kind-windows-amd64

echo Installing kubectl %KUBECTL_VERSION%
curl -Lo kubectl.exe https://dl.k8s.io/release/%KUBECTL_VERSION%/bin/windows/amd64/kubectl.exe

Add current dir to PATH
setx PATH "%PATH%;%cd%"

echo Creating k8s cluster with kind
kind create cluster --name %CLUSTER_NAME% --config src\test\resources\kind-config.yml --wait 300s

echo Executing tests
gradlew.bat test

pause

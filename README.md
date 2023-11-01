# jetbrains-kubectl-tests

This project tests kubectl cmd tool and its functionality against a real kubernetes cluster



To run tests locally, docker, kubectl and kind should be installed.
Kotest plugin for intellij idea can be installed to work with tests from idea conveniently.

kind (kubernetes in docker) https://kind.sigs.k8s.io is used for easy instantiation of kubernetes cluster for testing
kind install manual for different operating systems: https://kind.sigs.k8s.io/docs/user/quick-start/#installation

Creation of cluster:
kind create cluster --name kind-2 

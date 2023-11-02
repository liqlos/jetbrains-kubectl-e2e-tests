# jetbrains-kubectl-tests

This project tests kubectl cmd tool and its functionality against a real kubernetes cluster


To run tests locally, docker, kubectl and kind should be installed.
Kotest plugin for intellij idea can be installed to work with tests from idea conveniently.

kind (kubernetes in docker) https://kind.sigs.k8s.io is used for easy instantiation of kubernetes cluster for testing
kind install manual for different operating systems: https://kind.sigs.k8s.io/docs/user/quick-start/#installation


The pipeline described in .gitlab-ci.yml is installing kind, kubectl, then creating a kubernetes cluster using kind
then tests are executing different commands with kubectl and are checking the outputs


//TODO: add manual for local running


Test results can be downloaded as artifacts from gitlab's pipelines page 
or can be viewed by the link: http://jetbrains-kubectl-e2e-tests-root-03b3a09fdbdc351023855f04d17e52.pages.bukhalov.com


Pipeline run time can be optimized by building a custom docker image where kind and jre are preinstalled
cluster creation should remain in the pipeline because it needs dind service, and kubectl installation should also be 
a part of the pipeline to be able to parametrize its version from gitlab when running with parameters

package com.bukhalov.kubectl

import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.framework.concurrency.eventually
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.File

@ExperimentalKotest
class KubernetesResourceTests : DescribeSpec({

    beforeEach() {
        executeCommand("kubectl delete all --all")
    }

    val deploymentDefinitionFile = File("src/test/resources/nginx/nginx-deployment.yaml")

    describe("Applying Kubernetes resources definition") {
        it("should be able to apply a deployment") {
            val applyDeploymentCommand = "kubectl apply -f ${deploymentDefinitionFile.absolutePath}"

            val applyDeploymentResult = executeCommand(applyDeploymentCommand)
            applyDeploymentResult.exitCode shouldBe 0
            applyDeploymentResult.output shouldContain "nginx-deployment created"
            executeCommand("kubectl get pods").output shouldContain "nginx-deployment"
        }
    }

    describe("Scaling a deployment") {
        it("should be able to scale a deployment") {
            val applyDeploymentCommand = "kubectl apply -o json -f ${deploymentDefinitionFile.absolutePath} "
            val scaleDeploymentCommand = "kubectl scale deployments/nginx-deployment --replicas=4"
            val getDeploymentCommand = "kubectl -o json get deployments/nginx-deployment"

            val initialReplicaCount = jsonMapper
                .readTree(executeCommand(applyDeploymentCommand).output)["spec"]["replicas"].intValue()

            initialReplicaCount shouldBe 2
            executeCommand(scaleDeploymentCommand).exitCode shouldBe 0

            eventually({
                duration = 5
            }) {
                val scaledReplicaCount = jsonMapper
                    .readTree(executeCommand(getDeploymentCommand).output)["spec"]["replicas"].intValue()
                scaledReplicaCount shouldBe 4
            }
        }
    }

    describe("Applying nginx Kubernetes pod definition") {
        it("should be able to apply service configuration") {
            val applyDeploymentCommand = "kubectl apply -f ${deploymentDefinitionFile.absolutePath}"

            val applyDeploymentResult = executeCommand(applyDeploymentCommand)
            applyDeploymentResult.exitCode shouldBe 0
            applyDeploymentResult.output shouldContain "nginx-deployment created"
            executeCommand("kubectl get pods").output shouldContain "nginx-deployment"
        }
    }
})
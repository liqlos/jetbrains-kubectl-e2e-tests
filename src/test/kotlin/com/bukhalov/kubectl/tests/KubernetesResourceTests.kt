package com.bukhalov.kubectl.tests

import com.bukhalov.kubectl.cleanCluster
import com.bukhalov.kubectl.jsonMapper
import com.bukhalov.kubectl.runKubectlCommand
import io.kotest.assertions.timing.eventually
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.File
import kotlin.time.Duration.Companion.seconds

@ExperimentalKotest
class KubernetesResourceTests : DescribeSpec({

    beforeEach {
        cleanCluster()
    }

    val deploymentDefinition = File("src/test/resources/nginx/deployment.yaml")
    val applyDeploymentCommand = "apply -f ${deploymentDefinition.absolutePath}"
    val deploymentName = "nginx-deployment"
    val serviceDefinition = File("src/test/resources/nginx/service.yaml")
    val podDefinition = File("src/test/resources/nginx/pod.yaml")

    describe("Applying deployment definition") {
        it("should be able to apply a deployment") {
            val applyDeploymentResult = runKubectlCommand(applyDeploymentCommand)
            applyDeploymentResult.exitCode shouldBe 0
            applyDeploymentResult.output shouldContain "$deploymentName created"
            runKubectlCommand("get deployments").output shouldContain deploymentName
        }
    }

    describe("Scaling a deployment") {
        it("should be able to scale a deployment") {
            val scaleDeploymentCommand = "scale deployments/$deploymentName --replicas=4"
            val getDeploymentCommand = "-o json get deployments/$deploymentName"

            val initialReplicaCount = jsonMapper
                .readTree(runKubectlCommand("$applyDeploymentCommand -o json").output)["spec"]["replicas"].intValue()

            initialReplicaCount shouldBe 2
            runKubectlCommand(scaleDeploymentCommand).exitCode shouldBe 0

            eventually(5.seconds) {
                val scaledReplicaCount = jsonMapper
                    .readTree(runKubectlCommand(getDeploymentCommand).output)["spec"]["replicas"].intValue()
                scaledReplicaCount shouldBe 4
            }
        }
    }

    describe("Applying nginx pod definition") {
        it("should be able to apply pod definition") {
            val applyPodCommand = "apply -f ${podDefinition.absolutePath}"
            val applyPodResult = runKubectlCommand(applyPodCommand)

            applyPodResult.exitCode shouldBe 0
            val getPodsNamesCommandResult = runKubectlCommand("get pods -o=jsonpath='{.items[*].metadata.name}'")
            getPodsNamesCommandResult.output shouldContain "nginx-pod"

            eventually(15.seconds) {
                val getPodsStatusesCommandResult =
                    runKubectlCommand("get pods -o=jsonpath='{.items[*].status.phase}'")
                getPodsStatusesCommandResult.output shouldBe "Running"
            }
        }
    }

    describe("Applying service pod definition") {
        it("should be able to apply service definition") {
            runKubectlCommand(applyDeploymentCommand)
            val applyServiceCommand = "apply -f ${serviceDefinition.absolutePath}"
            val applyServiceResult = runKubectlCommand(applyServiceCommand)
            applyServiceResult.exitCode shouldBe 0

            val getServicesCommandResult = runKubectlCommand("get services")
            getServicesCommandResult.exitCode shouldBe 0
            getServicesCommandResult.output shouldContain "nginx-service"
            getServicesCommandResult.output shouldContain "NodePort"
        }
    }
})
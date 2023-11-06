package com.bukhalov.kubectl

import com.bukhalov.kubectl.utils.executeCommand
import com.bukhalov.kubectl.utils.logger
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.File

class KubernetesResourceTests : DescribeSpec({
    val resourceDir = "src/test/resources/nginx"

    describe("Applying nginx Kubernetes deployment definition") {
        it("should apply Deployment for nginx") {
            val deploymentDefinitionFile = File("$resourceDir/nginx-deployment-2-replicas.yaml")

            val applyDeployment = "kubectl apply -f ${deploymentDefinitionFile.absolutePath}"

            val result = executeCommand(applyDeployment)
            result.exitCode shouldBe 0
            result.output shouldContain "created"

            logger.info { "get pods: " + executeCommand("kubectl get pods").output }
            logger.info { "get deployments: " + executeCommand("kubectl get deployments").output }
        }
    }

//    val servicesListColumns = listOf("NAME", "TYPE", "CLUSTER-IP", "EXTERNAL-IP", "PORT(S)", "AGE")
//    servicesListColumns.all { it in result.output } shouldBe true
//   describe("Monitoring the status of deployed resources") {
//      it("should monitor the status of the Pod, Service, and Deployment for nginx") {
//         // Monitor the status of the deployed resources using kubectl
//         val getPods = "kubectl get pod -l app=nginx"
//         val getServices = "kubectl get svc -l app=nginx"
//         val getDeployments = "kubectl get deployment -l app=nginx"
//
//         val podStatus = runCommand(getPods)
//         val serviceStatus = runCommand(getServices)
//         val deploymentStatus = runCommand(getDeployments)
//
//         // Verify the status of the deployed resources
//         podStatus shouldBe 0
//         serviceStatus shouldBe 0
//         deploymentStatus shouldBe 0
//      }
//   }
})
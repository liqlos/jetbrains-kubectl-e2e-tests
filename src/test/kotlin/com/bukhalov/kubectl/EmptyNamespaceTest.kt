package com.bukhalov.kubectl

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class EmptyNamespaceTest : FeatureSpec({
    val emptyNamespace = "empty-namespace"
    beforeSpec {
        executeCommand("kubectl create namespace $emptyNamespace")
    }

    feature("getting version info") {
        scenario("should allow user to check kubectl version") {
            val result = executeCommand("kubectl version")

            result.exitCode shouldBe 0
            result.output shouldContain "Client Version"
        }
    }

    feature("getting cluster resources info") {
        scenario("should allow user to list pods") {
            val result = executeCommand("kubectl get pods -n $emptyNamespace")

            result.exitCode shouldBe 0
            result.errorOutput shouldContain "No resources found in $emptyNamespace namespace."
        }

        scenario("should allow user to list services") {
            val result = executeCommand("kubectl get services -n $emptyNamespace")

            result.exitCode shouldBe 0
            result.errorOutput shouldContain "No resources found in $emptyNamespace namespace."
        }

        scenario("should allow user to list nodes") {
            val result = executeCommand("kubectl get nodes -o json")
            result.exitCode shouldBe 0

            val nodesArray = jsonMapper
                .readTree(result.output).path("items")
            nodesArray.size() shouldBe 1

            val nodeName = nodesArray[0].path("metadata").path("name").asText()
            nodeName shouldContain "control-plane"
        }
    }

    afterSpec {
        executeCommand("kubectl delete namespace $emptyNamespace")
    }
})

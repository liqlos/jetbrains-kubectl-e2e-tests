package com.bukhalov.kubectl.tests

import com.bukhalov.kubectl.jsonMapper
import com.bukhalov.kubectl.runKubectlCommand
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class EmptyNamespaceTests : FeatureSpec({
    val emptyNamespace = "empty-namespace"
    beforeSpec {
        runKubectlCommand("create namespace $emptyNamespace")
    }

    feature("getting version info") {
        scenario("should allow user to check kubectl version") {
            val result = runKubectlCommand("version")

            result.exitCode shouldBe 0
            result.output shouldContain "Client Version"
        }
    }

    feature("getting cluster resources info") {
        scenario("should allow user to list pods") {
            val result = runKubectlCommand("get pods -n $emptyNamespace")

            result.exitCode shouldBe 0
            result.errorOutput shouldContain "No resources found in $emptyNamespace namespace."
        }

        scenario("should allow user to list services") {
            val result = runKubectlCommand("get services -n $emptyNamespace")

            result.exitCode shouldBe 0
            result.errorOutput shouldContain "No resources found in $emptyNamespace namespace."
        }

        scenario("should allow user to list nodes") {
            val result = runKubectlCommand("get nodes -o json")
            result.exitCode shouldBe 0

            val nodesArray = jsonMapper
                .readTree(result.output).path("items")
            nodesArray.size() shouldBe 2

            val nodeName = nodesArray[0].path("metadata").path("name").asText()
            nodeName shouldContain "control-plane"
        }
    }

    afterSpec {
        runKubectlCommand("delete namespace $emptyNamespace")
    }
})

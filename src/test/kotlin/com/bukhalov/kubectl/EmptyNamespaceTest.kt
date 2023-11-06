package com.bukhalov.kubectl

import com.bukhalov.kubectl.utils.executeCommand
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
    }

    afterSpec { executeCommand("kubectl delete namespace $emptyNamespace") }
})

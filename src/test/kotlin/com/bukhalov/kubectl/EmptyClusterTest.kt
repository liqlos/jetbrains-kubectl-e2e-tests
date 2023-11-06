package com.bukhalov.kubectl

import com.bukhalov.kubectl.utils.executeCommand
import com.bukhalov.kubectl.utils.logger
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class EmptyClusterTest : FeatureSpec({
    feature("getting version info") {
        scenario("should allow user to check kubectl version") {
            val result = executeCommand("kubectl version")
            logger.info { "Result:" + result.output }
            logger.info { "Result.error:" + result.error }

            result.exitCode shouldBe 0
            result.output shouldContain "Client Version"
        }
    }

    feature("getting cluster resources info") {
        scenario("should allow user to list pods") {
            val result = executeCommand("kubectl get pods")
            logger.info { "Result:" + result.output }
            logger.info { "Result.error:" + result.error }

            result.exitCode shouldBe 0
            result.output shouldContain "No resources found in default namespace."
        }

        scenario("should allow user to list services") {
            val result = executeCommand("kubectl get services")
            logger.info { "Result:" + result.output }
            logger.info { "Result.error:" + result.error }

            result.exitCode shouldBe 0

            val servicesListColumns = listOf("NAME", "TYPE", "CLUSTER-IP", "EXTERNAL-IP", "PORT(S)", "AGE")
            servicesListColumns.all { it in result.output } shouldBe true
        }
    }
})

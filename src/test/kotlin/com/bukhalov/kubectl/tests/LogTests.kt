package com.bukhalov.kubectl.tests

import com.bukhalov.kubectl.cleanCluster
import com.bukhalov.kubectl.runKubectlCommand
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.framework.concurrency.eventually
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.File
import kotlin.time.Duration.Companion.seconds

@ExperimentalKotest
class LogTests : FeatureSpec({

    beforeEach {
        cleanCluster()
    }

    feature("seeing logs") {
        scenario("should allow user to read logs of a pod") {
            val podWithLogsDefinition = File("src/test/resources/nginx/pod.yaml")
            runKubectlCommand("apply -f ${podWithLogsDefinition.absolutePath}")

            eventually(5.seconds) {
                val result = runKubectlCommand("logs nginx-pod")
                result.exitCode shouldBe 0
                result.output shouldContain "This is my log"
            }
        }
    }
})
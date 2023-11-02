package com.bukhalov.kubectl

import com.bukhalov.kubectl.utils.executeCommand
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class KubectlTest : FeatureSpec({
    feature("version info") {
        scenario("should allow user to check kubectl version") {
            val result = executeCommand("kubectl version")


            result.exitCode shouldBe 0
            result.output shouldContain "Client Version"
            //kubectl --kubeconfig ./admin.conf get nodes
        }
    }
})

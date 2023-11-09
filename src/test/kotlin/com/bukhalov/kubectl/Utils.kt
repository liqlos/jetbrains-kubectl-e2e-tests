package com.bukhalov.kubectl

import mu.KotlinLogging


val logger = KotlinLogging.logger {}
fun executeCommand(command: String): CommandResult {
    try {
        val process = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", command))

        val output = process.inputStream.bufferedReader().readText()
        val errorOutput = process.errorReader().readText()
        val exitCode = process.waitFor()

        logger.info {
            "Executed command: $command\n" +
                    "Stdout: $output\n" +
                    "ExitCode: $exitCode\n" +
                    "Stderr: $errorOutput\n"
        }

        return CommandResult(exitCode, output, errorOutput)
    } catch (e: Exception) {
        logger.error { "An error occurred while executing the command: ${e.message}" }
        return CommandResult(-1, "", e.message ?: "Unknown error")
    }
}

data class CommandResult(val exitCode: Int, val output: String, val errorOutput: String)

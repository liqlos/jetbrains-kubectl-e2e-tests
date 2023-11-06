package com.bukhalov.kubectl

import mu.KotlinLogging
import java.io.BufferedReader
import java.io.InputStreamReader


val logger = KotlinLogging.logger {}
fun executeCommand(command: String): CommandResult {
    try {
        val process = ProcessBuilder("/bin/sh", "-c", command).start()
        //TODO:
        //val process = Runtime.getRuntime().exec(command)
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = reader.readText()
        val errorStream = process.errorStream.bufferedReader().readText()
        val exitCode = process.waitFor()

        return CommandResult(exitCode, output, errorStream)
    } catch (e: Exception) {
        // Log any exceptions that occur
        println("An error occurred while executing the command: ${e.message}")
        return CommandResult(-1, "", e.message ?: "Unknown error")
    }
}

data class CommandResult(val exitCode: Int, val output: String, val error: String)

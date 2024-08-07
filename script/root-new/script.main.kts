#!/bin/sh
///bin/echo >/dev/null <<EOC
/*
EOC
kotlinc -script -Xplugin="${KOTLIN_HOME}/lib/kotlinx-serialization-compiler-plugin.jar" -- "$0" "$@"
exit $?
*/
// NOTE: this script is for Kotlin 2.0 as kotlinx-serialization-json:1.7.0 require it
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:4.4.0")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException


fun fatal(
    exitCode: Int = 0,
    vararg messages: String,
) {
    for (message in messages) {
        println(message)
    }
    Runtime.getRuntime().exit(exitCode)
}

fun run(vararg cmd: String): String {
    try {
        val process = Runtime.getRuntime().exec(cmd)

        val result = process.waitFor()
        val stdout = process.inputStream.bufferedReader().readText()
        if (result != 0) {
            val stderr = process.errorStream.bufferedReader().readText()
            fatal(
                exitCode = 2,
                "fatal: failed command ${cmd.toList()}",
                "output: $stdout",
                "error output: $stderr",
                "tip: most common reason: incorrect git commit SHA",
            )
        }
        return stdout.trim()
    } catch (e: IOException) {
        println("fatal: failed command ${cmd.toList()}")
        throw e
    }
}

class CliConfig(
    val asJson: Boolean,
    val tagPrefix: String,
    val asTag: Boolean,
    val scope: String,
    val initialRevision: String,
    val lastRevision: String,
)

class MyScript :
    CliktCommand(
        name = "script",
        printHelpOnEmptyArgs = true,
        help = "Provides next release version and release notes from git commit messages.",
    ) {
    private val json by option("-j", "--json", help = "Output in json format").flag()
    private val tagPrefix by option(
        "-t",
        "--tag-prefix",
        help = "Prefix for tags (optional)",
    ).default("")
    private val tag by option(
        help = "Add tag prefix to versions (only if tag prefix is defined)",
    ).flag()
    private val scope by option(
        "-s",
        "--scope",
        help = "Scope to filter release note items",
    ).default("")
    private val initialRevision by option(
        "-i",
        "--initial-revision",
        help = "Start range from next revision",
    ).default("")
    private val lastRevision by option(
        "-l",
        "--last-revision",
        help = "Stop on this revision",
    ).default("HEAD")

    override fun run() {
        currentContext.obj = CliConfig(json, tagPrefix, tag, scope, initialRevision, lastRevision)
    }
}

class Version :
    CliktCommand(
        name = "version",
        help = "Prints version of this tool",
    ) {
    private val config by requireObject<CliConfig>()

    override fun run() {
        val version = "SNAPSHOT"
        val result =
            if (config.asJson) {
                """{"tool_version": "$version"}"""
            } else {
                version
            }
        println(result)
    }
}


MyScript()
    .subcommands(
        Version(),
    ).main(args)

package name.stepin

import com.github.ajalt.clikt.core.subcommands
import name.stepin.cli.Cli
import name.stepin.cli.VersionCmd

fun main(args: Array<String>) {
    Cli()
        .subcommands(
            VersionCmd(),
        ).main(args)
}

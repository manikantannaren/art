/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import java.io.PrintWriter;
import picocli.CommandLine.Command;

/**
 *
 * @author manikantans
 */
@Command(
        name = "readme",
        aliases = {"r", "rme"},
        mixinStandardHelpOptions = true,
        description = {
            "Provides a readme form of help"
        },
        footer = {
            "Hit Ctrl+d to exit"
        },
        helpCommand = false
)
class ReadmeCommand extends AbstractCommand {

    @Override
    public ExitCode call() throws Exception {
        PrintWriter out = commandContext.getWriter();
        out.println(readme());
        return ExitCode.SUCCESS;
    }

    private String readme() {
        return """
               # Ignite Java Shell
               ___
               
               A Apache ignite client built as a java shell. It is built on [Picocli](https://github.com/remkop/picocli) and [Jline3](https://github.com/jline/jline3)
               
               ## How to build
               ___
               
               1. Checkout code
               2. Ensure JAVA_HOME is set to minimum Java 17
               3. Run ./gradlew clean build
               
               ## How to use
               ___
               
               1. Ensure JAVA_HOME is set to minimum Java 17
               2. Extract distribution
               2. Navigate to ignite-command-0.1/bin
               3. Run shell script `./ignite-command`
               
               ## CLI Guide
               This is a terminal shell with built in commands. Normal Linux/Unix commands will not work.
               
               **To EXIT the shell press** `ctrl+z` **or** `ctrl+d`
               
               ### List of available commands
               To see list of commands type `help`
               
               #### `help`
               Help command is available as a top level command or sub command of other commands
               
               `help` commands shows list of available commands
               `\\[command\\] help`  shows options for provided command.
               """;
    }

}

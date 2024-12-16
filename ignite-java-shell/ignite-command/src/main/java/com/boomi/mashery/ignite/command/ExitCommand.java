/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import java.io.PrintWriter;
import picocli.CommandLine;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        name = "exit",
        mixinStandardHelpOptions = true,
        description = {
            "Exit the shell",
            "Or press ctrl-D anytime"
        }
)

public class ExitCommand extends AbstractCommand {

    @Override
    public ExitCode call() throws Exception {
        try {
            PrintWriter out = commandContext.getWriter();
            out.println("Chee you choon");
        } finally {
            System.exit(0);
        }
            return ExitCode.SUCCESS;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import java.io.PrintWriter;
import java.nio.file.Path;
import org.apache.commons.io.input.ReversedLinesFileReader;
import picocli.CommandLine;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        name = "logstail",
        aliases = {"logs", "tail"},
        mixinStandardHelpOptions = true,
        description = {
            "Show logs"
        },
        requiredOptionMarker = '*',
        subcommands = {CommandLine.HelpCommand.class}
)
public class TailLogsCommand extends AbstractCommand {

    @CommandLine.Option(
            names = {"--numlines", "-n"},
            defaultValue = "20",
            description = {
                "Display last lines from the log",
                "Default is all lines represented by 20"
            }
    )
    private Integer numLines = 20;

    @Override
    public ExitCode call() throws Exception {
        Path filePath = commandContext.getLogFilePath();
        PrintWriter out = commandContext.getWriter();
        ReversedLinesFileReader reader = ReversedLinesFileReader.builder()
                .setPath(filePath)
                .get();
        reader.readLines(numLines)
                .forEach(line -> out.println(line));
        return ExitCode.SUCCESS;
    }

}

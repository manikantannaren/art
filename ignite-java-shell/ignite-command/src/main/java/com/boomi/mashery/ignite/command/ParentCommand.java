/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import com.boomi.mashery.common.Status;
import com.boomi.mashery.ignite.client.ClientNode;
import com.boomi.mashery.ignite.client.LocalCache;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Callable;
import org.jline.reader.LineReader;
import picocli.CommandLine;
import picocli.shell.jline3.PicocliCommands;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        name = "",
        mixinStandardHelpOptions = true,
        description = {
            "Interactive shell with Apache Ignite",
            "HIT |Tab| for completions",
            ""
        },
        footer = {
            "type exit or ctrl+d to exit the shell"
        },
        subcommands = {
            PicocliCommands.ClearScreen.class,
            CommandLine.HelpCommand.class,
            ReadmeCommand.class,
            ConnectCommand.class,
            ExitCommand.class,
            TestCommand.class,
            StatusCommand.class,
            UseCommand.class,
            DisconnectCommand.class,
            TailLogsCommand.class,
            GetCommand.class,
            PutCommand.class,
            DumpKeysCommand.class,
            InfoCommand.class
        }
)
public class ParentCommand implements Callable<ExitCode> {

    private PrintWriter out;
    private LocalCache<String, Object> currentContext;
    private ClientNode cacheClient;
    private Path logFilePath;

    public final void setReader(LineReader reader) {
        out = reader.getTerminal().writer();
    }

    PrintWriter getWriter() {
        return out;
    }

    public LocalCache<String, Object> getCurrentContext() {
        return currentContext;
    }

    public void setCurrentContext(LocalCache<String, Object> currentContext) {
        this.currentContext = currentContext;
    }

    public ClientNode getCacheClient() {
        return cacheClient;
    }

    public void setCacheClient(ClientNode cacheClient) {
        this.cacheClient = cacheClient;
    }

    public Path getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(Path logFilePath) {
        this.logFilePath = logFilePath;
    }

    public Status status() {
        if (cacheClient == null) {
            return Status.NOT_CONNECTED;
        }
        Status currentStatus = cacheClient.status();
        if(Status.RUNNING.equals(currentStatus)){
            if(!hasContext()){
                return Status.NO_CONTEXT;
            }
        }
        return currentStatus;
    }

    public Boolean hasContext() {
        return Objects.nonNull(currentContext);
    }

    public void clear(){
        currentContext = null;
        cacheClient.close();
        cacheClient = null;
    }
    
    @Override
    public ExitCode call() throws Exception {
        out.println(new CommandLine(this).getUsageMessage());
        return ExitCode.SUCCESS;
    }

}

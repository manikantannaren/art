package com.boomi.mashery.ignite.main;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import com.boomi.mashery.common.Utils;
import com.boomi.mashery.ignite.command.ParentCommand;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.jline.builtins.ConfigurationPath;
import org.jline.console.SystemRegistry;
import org.jline.console.impl.Builtins;
import org.jline.console.impl.SystemRegistryImpl;
import org.jline.jansi.AnsiConsole;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.Parser;
import org.jline.reader.Reference;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.widget.TailTipWidgets;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.shell.jline3.PicocliCommands;
import picocli.shell.jline3.PicocliCommands.PicocliCommandsFactory;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author manikantans
 */
public class Main {

    private static final String LEFT_PROMPT = "ignite $ ";
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        Path workDir = setupWorkDir();
        Path logFilePath = setupLogger(workDir);
        startShell(workDir, logFilePath);
    }

    private static Path setupWorkDir() throws IOException {
        Path workDir = identifyWorkDir();
        if (!Files.exists(workDir)) {
            Files.createDirectories(workDir);
        }
        return workDir;
    }

    private static Path identifyWorkDir() {
        Path tmpDir = Utils.getTempDirectoryPath();
        return tmpDir.resolve("igniteclient");
    }

    private static Path setupLogger(Path workDir) throws IOException {
        //1. set up logger from ENV var 
        //"BOOMI_ML_IGNITE_CLIENT_LOG_LEVEL and IGINTE_INNER_CLIENT_LOG_LEVEL" and ROOT_LOG_LEVEL.
        //No using logback.xml
        //Default log levels are INFO and WARN and OFF respectively
        LoggerContext ctx = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder layout = new PatternLayoutEncoder();
        layout.setPattern("%d{dd-MM-YYYY HH:mm:ss.SSS} %-5level %logger{36} - %msg%n");
        layout.setContext(ctx);
        layout.start();

        FileAppender appender = new FileAppender();
        appender.setContext(ctx);
        appender.setAppend(false);
        final Path logFilePath = getLogFilePath(workDir);
        String logFileCanonicalPath = logFilePath.toFile().getCanonicalPath();
        appender.setFile(logFileCanonicalPath);
        appender.setEncoder(layout);
        appender.start();

        new LoggerHandler(Level.INFO, "com.boomi.mashery", "BOOMI_ML_IGNITE_CLIENT_LOG_LEVEL")
                .prepareLogger(ctx, appender);
        new LoggerHandler(Level.WARN, "org.apache.ignite", "IGINTE_INNER_CLIENT_LOG_LEVEL")
                .prepareLogger(ctx, appender);
        new LoggerHandler(Level.OFF, Logger.ROOT_LOGGER_NAME, "ROOT_LOG_LEVEL")
                .prepareLogger(ctx, appender);
        
        return logFilePath;
    }

    private static Path getLogFilePath(Path workDir) throws IOException {
        Path logDir = workDir.resolve("logs");
        if (!Files.exists(logDir)) {
            Files.createDirectories(logDir);
        }
        Path logFilePath = logDir.resolve("igniteclient.log");
        return logFilePath;
    }

    private static void startShell(Path workDir, Path logFilePath) {
        PicocliCommandsFactory factory = new PicocliCommandsFactory();

        ParentCommand command = new ParentCommand();
        command.setLogFilePath(logFilePath);
        CommandLine cmdLine = new CommandLine(command, factory);

        PicocliCommands commands = new PicocliCommands(cmdLine);

        Builtins builtins = new Builtins(workDir,
                new ConfigurationPath(workDir, workDir), null);
        builtins.rename(Builtins.Command.TTOP, "top");
        builtins.alias("zle", "widget");
        builtins.alias("bindkey", "keymap");

        Parser lineParser = new DefaultParser();

        try (Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build()) {
            LOGGER.info("Using terminal {} of type {}", terminal, terminal.getClass());
            SystemRegistry registry = new SystemRegistryImpl(lineParser, terminal, () -> workDir, null);
            registry.setCommandRegistries(builtins, commands);
            registry.register("help", commands);
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .parser(lineParser)
                    .variable(LineReader.LIST_MAX, 50)
                    .build();

            builtins.setLineReader(reader);
            command.setReader(reader);
            factory.setTerminal(terminal);
            TailTipWidgets widgets = new TailTipWidgets(reader, registry::commandDescription, 5, TailTipWidgets.TipType.COMPLETER);
            widgets.enable();
            KeyMap<Binding> keyMap = reader.getKeyMaps().get("main");
            keyMap.bind(new Reference("tailtip-toggle"), KeyMap.alt("s"));

            while (true) {
                try {
                    registry.cleanUp();
                    String line = reader.readLine(LEFT_PROMPT, "", (MaskingCallback) null, null);
                    registry.execute(line);
                } catch (UserInterruptException e) {
                    LOGGER.error("User interrupted command",e);
                } catch (EndOfFileException e) {
                    LOGGER.error("Encountered EOF",e);
                    return;
                } catch (Exception e) {
                    registry.trace(e);
                }
            }
        } catch (Throwable t) {
            LOGGER.warn("Error starting terminal", t);
        } finally {
            AnsiConsole.systemUninstall();
        }

    }

    private static class LoggerHandler {

        private final Level defaultLogLevel;
        private final String name;
        private final String envVarName;

        LoggerHandler(Level defaultLogLevel, String name, String envVarName) {
            this.defaultLogLevel = Objects.isNull(defaultLogLevel) ? Level.WARN : defaultLogLevel;
            this.name = name;
            this.envVarName = envVarName;
        }

        void prepareLogger(LoggerContext context, Appender appender) {
            Logger logger = context.getLogger(name);
            logger.setAdditive(false);
            logger.addAppender(appender);
            String level = Utils.getSystemProperty(envVarName, defaultLogLevel.levelStr);
            Level loggerLevel = Level.toLevel(level, defaultLogLevel);
            logger.setLevel(loggerLevel);
        }
    }
}

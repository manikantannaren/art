/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import com.boomi.mashery.common.CacheClientException;
import com.boomi.mashery.common.Status;
import com.boomi.mashery.ignite.client.ClientNode;
import com.boomi.mashery.ignite.client.LocalCache;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import picocli.CommandLine;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        name = "test",
        mixinStandardHelpOptions = true,
        description = {
            "Test connection to an ignite cluster",
            "Basic test with or full test -t"
        },
        customSynopsis = {
            "Usage:",
            "test [-t]"
        },
        footer = {
            "run disconnect to disconnect from the cluster"
        },
        requiredOptionMarker = '*',
        subcommands = {CommandLine.HelpCommand.class}
)
public class TestCommand extends AbstractCommand {

    @CommandLine.Option(
            names = {"-d", "--deep"},
            description = {
                "If flagged, test will create a cache called testcache if not already created",
                "Put some key-value pairs",
                "Fetch those values",
                "Delete the test cache",
                "NOTE: This command will not use current cache as context"
            }
    )
    private Optional<Boolean> deepTest =  Optional.of(Boolean.FALSE);

    @Override
    public ExitCode call() throws Exception {
        PrintWriter out = commandContext.getWriter();
        if (!Status.RUNNING.equals(commandContext.status())) {
            out.println(" Not connected. Connect to an ignite cluser. Use connect -h for options");
            return ExitCode.GENERIC_ERROR;
        }

        ClientNode node = commandContext.getCacheClient();
        out.println("Connected");
        out.println(node.connectionInfo());
        if (deepTest.get()) {
            try {
                doFullTest(node, out);
            } catch (CacheClientException e) {
                out.println("Test failed");
                out.println(e.getMessage());
            }
        }
        return ExitCode.SUCCESS;
    }

    private void doFullTest(ClientNode node, PrintWriter out) throws CacheClientException {
        LocalCache<String, Object> cache = node.getCache("testcache");
        Map<String, Object> data = Map.of("one", "1", "two", "22", "three", "333", "four", "4444", "five", "55555");
        cache.putAll(data);
        out.println("Put all entries into test cache");
        out.println(data);

        Set<String> allKeys = new TreeSet(cache.keys());
        out.println("Fetched all keys from cache (Lexicographically sorted");
        out.println(allKeys);
        for (String key : allKeys) {
            Object value = cache.get(key);
            out.println(key + ":::" + value);
        }
        
        node.destroyCache("testcache");
    }

}

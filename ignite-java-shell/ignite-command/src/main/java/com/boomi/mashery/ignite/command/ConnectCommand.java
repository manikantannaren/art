/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import com.boomi.mashery.common.CacheClientException;
import com.boomi.mashery.common.Status;
import com.boomi.mashery.ignite.client.ClientDiscoveryType;
import com.boomi.mashery.ignite.client.ClientNode;
import com.boomi.mashery.ignite.client.IgniteClientDiscoveryConfiguration;
import com.boomi.mashery.ignite.client.LocalCache;
import java.io.PrintWriter;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 *
 * @author manikantans
 */
@Command(
        name = "connect",
        aliases = {"c", "co"},
        mixinStandardHelpOptions = true,
        description = {
            "Connect to an ignite cluster",},
        footer = {
            "run disconnect to disconnect from the cluster"
        },
        requiredOptionMarker = '*',
        subcommands = {CommandLine.HelpCommand.class}
)
public class ConnectCommand extends AbstractCommand {

    @CommandLine.Option(
            names = {"--type", "-t"},
            description = "One of K8S or TCP",
            required = true
    )
    ClientDiscoveryType type;

    @CommandLine.Option(
            names = {"--location", "-l"},
            description = {
                "Location of Ignite cache.",
                "It is of the form k8sservicename:discoveryport or",
                "hostname:tcp_port or",
                "ipaddress:tcp_port"
            },
            required = true
    )
    private List<String> locations;

    @CommandLine.Option(
            names = {"--cache", "-c"},
            description = {
                "Optional: Name of Ignite cache.",
                "You can also set cache by using uc or use --cache <cache name> command"
            },
            required = false
    )
    private String cacheName;

    @Override
    public ExitCode call() throws Exception {
        PrintWriter out = commandContext.getWriter();
        out.println("Hello there - I will help you connect");
        //first check if already connected
        Status status = commandContext.status();
        if (Status.RUNNING.equals(status)) {
            out.println("Oh! Oh! You are already connected");
            out.println("Current Connection details:");
            ClientNode node = commandContext.getCacheClient();
            out.println(node.connectionInfo());
            return ExitCode.SUCCESS;
        }

        IgniteClientDiscoveryConfiguration config = new IgniteClientDiscoveryConfiguration.DiscoveryConfigurationBuilder()
                .withClientDiscoveryType(type)
                .withIPAddresses(locations)
                .build();
        LOGGER.debug("Configuration : {}",config);
        try {
            ClientNode node = ClientNode.instance(config);
            node.start();
            commandContext.setCacheClient(node);
            out.println("Connected");
            out.println(config);
            if(StringUtils.isNotEmpty(cacheName)){
                LocalCache<String, Object> cache = node.getCache(cacheName);
                commandContext.setCurrentContext(cache);
            }
            out.println("Connected with options "+config);
        } catch (CacheClientException e) {
            out.println("ERROR:");
            out.println(e.getMessage());
            LOGGER.error("Error connecting to ignite",e);
        }
        return ExitCode.SUCCESS;
    }

}

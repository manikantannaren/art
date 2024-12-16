/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import com.boomi.mashery.common.Status;
import com.boomi.mashery.ignite.client.ClusterInfo;
import com.boomi.mashery.ignite.client.IgniteClientDiscoveryConfiguration;
import java.io.PrintWriter;
import org.apache.commons.lang3.BooleanUtils;
import picocli.CommandLine;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        name = "info",
        mixinStandardHelpOptions = true,
        description = {
            "Provides cluster information"
        },
        requiredOptionMarker = '*',
        subcommands = {CommandLine.HelpCommand.class}
)

public class InfoCommand extends AbstractCommand{

    @Override
    public ExitCode call() throws Exception {
        Status status = commandContext.status();
        PrintWriter out = commandContext.getWriter();
        CommandUtils.printStatus(status, out);
        ClusterInfo info = commandContext.getCacheClient().clusterInfo();
        IgniteClientDiscoveryConfiguration config = info.getConfig();
        out.println("Connection configuration");
        out.println("------------------------");
        out.println("Discovery type : "+config.type());
        out.println("Provided nodes : "+config.getIpAddresses());
        out.println("------------------------");
        out.println("");
        out.println("Available caches");
        info.getCacheNames().forEach(name->out.println(name));
        out.println("----------------");
        out.println("");
        out.println("Nodes information");
        out.println("----------------");
        info.getNodes().forEach(node->{
            out.println("Node "+node.nodeId());
            out.println("   Client node: "+BooleanUtils.toStringYesNo(node.isClientNode()));
            out.println("   Local  node: "+BooleanUtils.toStringYesNo(node.isLocalNode()));
            out.println("   Host names : "+node.hostNames());
        });
        
        return ExitCode.SUCCESS;
    }
    
}

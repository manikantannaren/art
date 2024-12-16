/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import com.boomi.mashery.ignite.client.LocalCache;
import java.io.PrintWriter;
import org.apache.commons.lang3.ObjectUtils;
import picocli.CommandLine;

/**
 *
 * @author manikantans
 */
@CommandLine.Command(
        name = "get",
        aliases = {"g"},
        mixinStandardHelpOptions = true,
        description = {
            "Get a value for a key. NUll if it does not exist"
        },
        requiredOptionMarker = '*',
        subcommands = {CommandLine.HelpCommand.class}
)
public class GetCommand extends AbstractCommand{
    
    @CommandLine.Option(
            names={"-k","--key"},
            required=true
    )
    private String key;

    @Override
    public ExitCode call() throws Exception {
        
        LocalCache<String, Object> cache = commandContext.getCurrentContext();
        Object value = cache.get(key);
        PrintWriter out = commandContext.getWriter();
        if(ObjectUtils.isEmpty(value)){
            out.println("<KEY NOT FOUND>: "+key);
        }else{
            out.println("Key   :"+key);
            out.println("Value :"+value);
        }
        return ExitCode.SUCCESS;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

import com.boomi.mashery.common.Status;
import static com.boomi.mashery.common.Status.*;
import java.io.PrintWriter;

/**
 *
 * @author manikantans
 */
public abstract class CommandUtils {

    private CommandUtils() {
    }

    public static void printStatus(Status status, PrintWriter out) {
        switch (status) {
            case CONNECTING ->
                out.println("Connecting...");
            case NOT_CONNECTED ->
                out.println("Not connected!");
            case NO_CONTEXT ->
                out.println("Connected but no current context");
            case RUNNING ->
                out.println("Connected!");
        }

    }
}

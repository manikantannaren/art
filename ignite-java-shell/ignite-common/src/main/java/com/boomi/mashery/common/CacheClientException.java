/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.common;

/**
 *
 * @author manikantans
 */
public class CacheClientException extends Exception {

    public CacheClientException(MsgCode errCode, String message) {
        super(constructMessage(errCode, message));
    }

    public CacheClientException(MsgCode errCode, String message, Throwable cause) {
        super(constructMessage(errCode, message), cause);
    }

    private static String constructMessage(MsgCode errCode, String message) {
        StringBuilder msg = new StringBuilder(errCode.getMessage())
            .append(Utils.lineSeparator())
            .append(message);
        return msg.toString();
    }
}

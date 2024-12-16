/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.common;

/**
 *
 * @author manikantans
 */
public enum MsgCode {
    
    E0001_GENERIC_ERROR ("E0001","Generic Error. Check nested exception"),
    E0002_CONN_ERROR ("E0002", "Connection error"),
    E0003_INVALID_CONFIG ("E0003", "Invalid configuration"),
    E0004_FILE_CREATE_ERROR("E0004","File/Directory IO error"),
    E0005_NOT_INITIALIZED ("E0005","Not yet initialized"),
    E0006_ILLEGAL_ARGUMENT("E0006", "Illegal argument. Parameter expected"),
    E0007_UNSUCCESSFUL_OPERATION("E0007", "Could not perform operation");

    private final String code;
    private final String message;
    
    private MsgCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }
    
    public String getMessage(){
        return String.format("[%S]: %S",code, message);
    }
    
}

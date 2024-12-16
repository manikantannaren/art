/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.command;

/**
 *
 * @author manikantans
 */
public enum ExitCode {
    SUCCESS(0),
    GENERIC_ERROR(1),
    BAD_ARGUMENTS(2);

    private final Integer code;
    private ExitCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name() + "[" + code + ']';
    }
    
}

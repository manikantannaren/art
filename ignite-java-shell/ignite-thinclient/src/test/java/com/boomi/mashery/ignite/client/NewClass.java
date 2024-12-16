/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.ignite.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author manikantans
 */
public class NewClass {
    @Test
    public void test(){
        long millis = 24*60*60*1000;
        long micros = 24*60*60*1000*1000;
        
        long result = micros/millis;
        
        Assertions.assertEquals(1000,result);
    }
}

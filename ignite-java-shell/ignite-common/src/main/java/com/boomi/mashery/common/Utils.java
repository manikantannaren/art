/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boomi.mashery.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author manikantans
 */
public abstract class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    
    public static final String FOR_TEST = "forTest";

    public static Path getTempDirectoryPath() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        LOGGER.debug("Default temp directory = {}",tmpDir);
        Boolean underTest = getSystemBooleanProperty(FOR_TEST, Boolean.FALSE);
        if (underTest) {
            return Paths.get("build",tmpDir);
        }
        return Paths.get(tmpDir);
    }

    public static String lineSeparator() {
        return System.getProperty("line.separator");
    }

    public static Boolean getSystemBooleanProperty(String propName, Boolean defaultValue) {
        String prop = getSystemProperty(propName, BooleanUtils.toString(defaultValue, "true", "false", "false"));
        return BooleanUtils.toBoolean(prop);
    }

    public static String getSystemProperty(String propName, String defaultValue) {
        String retValue = System.getProperty(propName);
        if (StringUtils.isEmpty(retValue)) {
            retValue = System.getenv(propName);
        }
        return StringUtils.isNotEmpty(retValue) ? retValue : defaultValue;
    }

    private Utils() {
    }
}

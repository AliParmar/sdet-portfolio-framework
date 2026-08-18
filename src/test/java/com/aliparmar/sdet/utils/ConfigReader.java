package com.aliparmar.sdet.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads config.properties when class loads.
 * Provides simple getters so other framework classes can read config values.
*/
public class ConfigReader {

    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config/config.properties";

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Could not find " + CONFIG_FILE + " on the classpath.");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + CONFIG_FILE, e);
        }
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing config key: " + key + " in the config.properties file");
        }
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

}
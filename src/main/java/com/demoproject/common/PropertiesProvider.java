package com.demoproject.common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesProvider {

    private static final Logger LOGGER = LogManager.getLogger(PropertiesProvider.class);
    private static Properties properties;

    private PropertiesProvider() { }

    public static Properties loadProperties() throws Exception {
        if (PropertiesProvider.properties != null) {
            return PropertiesProvider.properties;
        }

        InputStream inputStream = null;

        try {
            inputStream = PropertiesProvider.class.getClassLoader().getResourceAsStream("application.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            // if properties is loaded successfully,
            // we'll assign that to our static variable...
            PropertiesProvider.properties = properties;

            return PropertiesProvider.properties;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception exception) {
                    LOGGER.log(Level.WARN, "An exception occurred while closing the input stream.", exception);
                }
            }
        }
    }

    public static Properties getProperties() {
        return properties;
    }
}

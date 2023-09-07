package com.demoproject;

import com.demoproject.application.ApplicationService;
import com.demoproject.application.ApplicationServiceImpl;
import com.demoproject.common.PropertiesProvider;
import com.demoproject.common.ServiceProvider;
import com.demoproject.common.SingletonServiceProvider;
import com.demoproject.database.ConnectionProvider;
import com.demoproject.net.SimpleHttpClient;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        ApplicationService applicationService = null;

        try {
            LOGGER.log(Level.INFO, "Application has booted up.");

            // it shall load properties from our application.properties file...
            PropertiesProvider.loadProperties();
            // creates a singleton instance of the HttpClient...
            SimpleHttpClient.createHttpClient();
            // establishes primary connection to our MySQL database...
            ConnectionProvider.createPrimaryConnection();

            // executes application service...
            ServiceProvider serviceProvider = SingletonServiceProvider.getInstance();
            applicationService = serviceProvider.get(ApplicationServiceImpl.class);
            applicationService.reset();
            applicationService.execute();
        } catch (Exception exception) {
            LOGGER.log(Level.ERROR, "An unexpected exception occurred.", exception);
        } finally {
            if (applicationService != null) {
                applicationService.dispose();
            }

            ConnectionProvider.closePrimaryConnection();
        }
    }
}

package com.demoproject.database;

import com.demoproject.common.PropertiesProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionProvider {

    private static Connection primaryConnection;

    private ConnectionProvider() { }

    /**
     * Creates new connection object.
     * @param shallAutoCommit If set to true, JDBC driver shall auto commit.
     * @return Returns the newly created connection object.
     * @throws Exception
     */
    public static Connection createConnection(boolean shallAutoCommit) throws Exception {
        // Class.forName("com.mysql.cj.jdbc.Driver");           // <-- this is no longer required...
        Properties properties = PropertiesProvider.getProperties();
        String host = properties.getProperty("DATABASE_HOST");
        int port = Integer.parseInt(properties.getProperty("DATABASE_PORT"));
        String databaseName = properties.getProperty("DATABASE_NAME");
        String username = properties.getProperty("DATABASE_USERNAME");
        String password = properties.getProperty("DATABASE_PASSWORD");
        boolean shallRewriteBatchedStatements = "true".equals(properties.getProperty("DATABASE_SHALL_REWRITE_BATCHED_STATEMENTS"));
        String connectionString = "jdbc:mysql://" + host + ":" + port + "/"
                + databaseName + "?user=" + username + "&password=" + password
                + "&rewriteBatchedStatements=" + shallRewriteBatchedStatements;

        // creating new connection to the database...
        Connection connection = DriverManager.getConnection(connectionString);
        connection.setAutoCommit(shallAutoCommit);

        return connection;
    }

    /**
     * Creates and stores the primary connection object for reuse.
     * @return Returns the primary connection object.
     * @throws Exception
     */
    public static Connection createPrimaryConnection() throws Exception {
        if (primaryConnection != null) { return primaryConnection; }

        // primary connection shall auto commit...
        primaryConnection = createConnection(true);

        return primaryConnection;
    }

    public static Connection getPrimaryConnection() {
        return primaryConnection;
    }

    public static void closePrimaryConnection() {
        DatabaseUtils.tryCloseConnection(primaryConnection);
    }
}

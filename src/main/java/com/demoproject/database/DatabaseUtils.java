package com.demoproject.database;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public final class DatabaseUtils {

    private static final Logger LOGGER = LogManager.getLogger(DatabaseUtils.class);
    private static final String STATEMENT_SQL_SEPARATOR = ": ";

    public static void tryCloseResultSet(ResultSet resultSet) {
        if (resultSet == null) { return; }

        try {
            resultSet.close();
        } catch (Exception exception) {
            LOGGER.log(Level.WARN, "An exception occurred while closing the database result set.", exception);
        }
    }

    public static String getSqlFromStatement(Statement statement) {
        if (statement == null) { return ""; }

        String statementAsString = statement.toString();
        int indexOfSqlSeparator = statementAsString.indexOf(STATEMENT_SQL_SEPARATOR);

        if (indexOfSqlSeparator == -1) { return ""; }

        return statementAsString.substring(indexOfSqlSeparator + STATEMENT_SQL_SEPARATOR.length());
    }

    public static void tryCloseStatement(Statement statement) {
        if (statement == null) { return; }

        try {
            statement.close();
        } catch (Exception exception) {
            LOGGER.log(Level.WARN, "An exception occurred while closing the database statement.", exception);
        }
    }

    public static void tryClearBatch(Statement statement) {
        if (statement == null) { return; }

        try {
            statement.clearBatch();
        } catch (Exception exception) {
            LOGGER.log(Level.WARN, "An exception occurred while clearing the batch of database statements/commands.", exception);
        }
    }

    public static int[] tryExecuteBatch(Statement statement) {
        int[] result = new int[0];

        if (statement == null) { return result; }

        try {
            result = statement.executeBatch();
            tryClearBatch(statement);
        } catch (Exception exception) {
            LOGGER.log(Level.WARN, "An exception occurred while executing the batch of database statements/commands.", exception);
        }

        return result;
    }

    public static void tryCommit(Connection connection) {
        if (connection == null) { return; }

        try {
            // getAutoCommit() method also throws SQLException...
            if (connection.getAutoCommit()) { return; }

            connection.commit();
        } catch (Exception exception) {
            LOGGER.log(Level.WARN, "An exception occurred while closing the database connection.", exception);
        }
    }

    /**
     * @implNote If auto commit is set to false for the connection
     * object, this method shall try to commit before closing the
     * connection.
     * @param connection Connection to close.
     */
    public static void tryCloseConnection(Connection connection) {
        if (connection == null) { return; }

        tryCommit(connection);

        try {
            connection.close();
        } catch (Exception exception) {
            LOGGER.log(Level.WARN, "An exception occurred while closing the database connection.", exception);
        }
    }
}

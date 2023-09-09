package com.demoproject.database;

import com.demoproject.common.Tuple;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Repository {

    Logger logger = LogManager.getLogger(Repository.class);

    String getTableName();

    default String convertColumnsToString(String ...columns) {
        // if no column specified, we shall select all...
        if (columns.length == 0) { return "*"; }

        StringBuilder columnsBuilder = new StringBuilder();

        for (String column : columns) {
            columnsBuilder.append(column);
            columnsBuilder.append(", ");
        }

        // removes the last comma...
        return columnsBuilder.substring(0, columnsBuilder.length() - 2);
    }

    /**
     * This method returns a tuple which contains ResultSet
     * as the first element and the PreparedStatement as the
     * second element.
     * @param id ID of the row.
     * @param columns Column names to select (optional).
     * @return A tuple containing ResultSet and PreparedStatement.
     * @throws SQLException
     */
    default Tuple findResultSetById(long id, String ...columns) throws SQLException {
        String columnsAsString = convertColumnsToString(columns);
        String sqlFormat = "SELECT " + columnsAsString + " FROM " + getTableName() + " WHERE id = " + id;
        PreparedStatement statement = ConnectionProvider
                .getPrimaryConnection().prepareStatement(sqlFormat);

        logger.log(Level.INFO, DatabaseUtils.getSqlFromStatement(statement));

        ResultSet resultSet = statement.executeQuery();

        return Tuple.of(resultSet, statement);
    }

    /**
     * This method returns a tuple which contains ResultSet
     * as the first element and the PreparedStatement as the
     * second element.
     * @param columns Column names to select (optional).
     * @return A tuple containing ResultSet and PreparedStatement.
     * @throws SQLException
     */
    default Tuple findResultSetForAll(String ...columns) throws SQLException {
        String columnsAsString = convertColumnsToString(columns);
        String sqlFormat = "SELECT " + columnsAsString + " FROM " + getTableName();
        PreparedStatement statement = ConnectionProvider
                .getPrimaryConnection().prepareStatement(sqlFormat);

        logger.log(Level.INFO, DatabaseUtils.getSqlFromStatement(statement));

        ResultSet resultSet = statement.executeQuery();

        return Tuple.of(resultSet, statement);
    }

    default void resetAutoIncrement() throws SQLException {
        PreparedStatement statement = null;

        try {
            String sqlFormat = "ALTER TABLE " + getTableName() + " AUTO_INCREMENT = 1";
            statement = ConnectionProvider.getPrimaryConnection()
                    .prepareStatement(sqlFormat);

            logger.log(Level.INFO, DatabaseUtils.getSqlFromStatement(statement));

            statement.execute();
        } finally {
            DatabaseUtils.tryCloseStatement(statement);
        }
    }

    default void deleteAll() throws SQLException {
        PreparedStatement statement = null;

        try {
            String sqlFormat = "DELETE FROM " + getTableName() + " " +
                    "WHERE " +
                    "id <> -1";
            statement = ConnectionProvider.getPrimaryConnection()
                    .prepareStatement(sqlFormat);

            logger.log(Level.INFO, DatabaseUtils.getSqlFromStatement(statement));

            statement.execute();
        } finally {
            DatabaseUtils.tryCloseStatement(statement);
        }
    }
}

package com.demoproject.appconfig;

import com.demoproject.common.Tuple;
import com.demoproject.database.ConnectionProvider;
import com.demoproject.database.Repository;
import com.demoproject.database.DatabaseUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppConfigRepository implements Repository {

    private final Logger logger = LogManager.getLogger(AppConfigRepository.class);

    public AppConfig findById(int appConfigId) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Tuple tuple = findResultSetById(appConfigId, "number_of_row", "status");
            resultSet = tuple.get(1, ResultSet.class);
            statement = tuple.get(2, PreparedStatement.class);

            if (!resultSet.next()) {
                return null;
            }

            int numberOfRows = resultSet.getInt(1);
            AppConfigStatus status = AppConfigStatus.from(resultSet.getInt(2));

            return new AppConfig(appConfigId, numberOfRows, status);
        } finally {
            DatabaseUtils.tryCloseResultSet(resultSet);
            DatabaseUtils.tryCloseStatement(statement);
        }
    }

    public void updateStatusById(int appConfigId, int status) throws SQLException {
        PreparedStatement statement = null;

        try {
            String sqlFormat = "UPDATE app_conf SET status = ? WHERE id = ?";
            statement = ConnectionProvider
                    .getPrimaryConnection().prepareStatement(sqlFormat);
            statement.setInt(1, status);
            statement.setInt(2, appConfigId);

            logger.log(Level.INFO, DatabaseUtils.getSqlFromStatement(statement));

            statement.execute();
        } finally {
            DatabaseUtils.tryCloseStatement(statement);
        }
    }

    @Override
    public String getTableName() {
        return "app_conf";
    }
}

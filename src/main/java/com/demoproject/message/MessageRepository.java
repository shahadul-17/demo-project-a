package com.demoproject.message;

import com.demoproject.database.BatchProcessableRepository;
import com.demoproject.database.ConnectionProvider;
import com.demoproject.database.DatabaseUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository implements BatchProcessableRepository {

    private final Logger logger = LogManager.getLogger(MessageRepository.class);
    private Connection connectionForBatchProcessing;
    private PreparedStatement statementForBatchProcessing;

    /**
     * @implNote As we use this method in a continuous manner,
     * this method can perform faster by utilizing the primary key
     * of the inbox table.
     */
    public List<Message> findByStatus(MessageStatus status, int limit, long lastMessageId) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sqlFormat = "SELECT id, msisdn, sms_text FROM " + getTableName() + " WHERE id > ? AND status = ? LIMIT ?";
            statement = ConnectionProvider
                    .getPrimaryConnection().prepareStatement(sqlFormat);
            statement.setLong(1, lastMessageId);
            statement.setString(2, "" + status.getValue());
            statement.setInt(3, limit);

            logger.log(Level.INFO, DatabaseUtils.getSqlFromStatement(statement));

            resultSet = statement.executeQuery();
            List<Message> inboxes = new ArrayList<>();

            while (resultSet.next()) {
                long messageId = resultSet.getLong(1);
                String mobileNumber = resultSet.getString(2);
                String content = resultSet.getString(3);
                inboxes.add(new Message(messageId, mobileNumber, content, status));
            }

            return inboxes;
        } finally {
            DatabaseUtils.tryCloseResultSet(resultSet);
            DatabaseUtils.tryCloseStatement(statement);
        }
    }

    public void updateStatusById(long messageId, MessageStatus status) throws Exception {
        if (connectionForBatchProcessing == null) {
            connectionForBatchProcessing = ConnectionProvider.createConnection(false);
        }

        if (statementForBatchProcessing == null) {
            String sqlFormat = "UPDATE " + getTableName() + " SET status = ? WHERE id = ?";
            statementForBatchProcessing = connectionForBatchProcessing.prepareStatement(sqlFormat);
        }

        statementForBatchProcessing.setString(1, "" + status.getValue());
        statementForBatchProcessing.setLong(2, messageId);
        String sql = statementForBatchProcessing.toString().split(": ")[1];

        logger.log(Level.INFO, sql);

        statementForBatchProcessing.addBatch();
    }

    @Override
    public void commitBatchedCommands(boolean shallDisposeResources) {
        logger.log(Level.DEBUG, "Changes to messages are being committed.");

        int[] updateCounts = DatabaseUtils.tryExecuteBatch(statementForBatchProcessing);
        DatabaseUtils.tryCommit(connectionForBatchProcessing);

        // we could've further checked the updateCounts array for success/failure status.
        // but we shall ignore that for now just to keep things simple...

        logger.log(Level.DEBUG, updateCounts.length + " commands committed.");

        if (!shallDisposeResources) { return; }

        DatabaseUtils.tryCloseStatement(statementForBatchProcessing);
        DatabaseUtils.tryCloseConnection(connectionForBatchProcessing);
    }

    public void updateStatusForAll(MessageStatus status) throws SQLException {
        PreparedStatement statement = null;

        try {
            String sqlFormat = "UPDATE " + getTableName() + " SET status = ? WHERE id <> -1";
            statement = ConnectionProvider
                    .getPrimaryConnection().prepareStatement(sqlFormat);
            statement.setString(1, "" + status.getValue());

            logger.log(Level.INFO, DatabaseUtils.getSqlFromStatement(statement));

            statement.execute();
        } finally {
            DatabaseUtils.tryCloseStatement(statement);
        }
    }

    @Override
    public String getTableName() {
        return "inbox";
    }
}

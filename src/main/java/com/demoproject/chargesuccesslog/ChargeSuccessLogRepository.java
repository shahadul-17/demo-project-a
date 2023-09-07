package com.demoproject.chargesuccesslog;

import com.demoproject.database.BatchProcessableRepository;
import com.demoproject.database.ConnectionProvider;
import com.demoproject.database.DatabaseUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ChargeSuccessLogRepository implements BatchProcessableRepository {

    private final Logger logger = LogManager.getLogger(ChargeSuccessLogRepository.class);
    private Connection connectionForBatchProcessing;
    private PreparedStatement statementForBatchProcessing;

    public void insert(ChargeSuccessLog log) throws Exception {
        if (connectionForBatchProcessing == null) {
            connectionForBatchProcessing = ConnectionProvider.createConnection(false);
        }

        if (statementForBatchProcessing == null) {
            String sqlFormat = "INSERT INTO " + getTableName() + " " +
                    "(sms_id, msisdn, keyword_id, amount, amount_with_vat, validity, charge_id, tid_ref, response) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statementForBatchProcessing = connectionForBatchProcessing.prepareStatement(sqlFormat);
        }

        statementForBatchProcessing.setLong(1, log.getMessageId());
        statementForBatchProcessing.setString(2, log.getMobileNumber());
        statementForBatchProcessing.setInt(3, log.getKeywordId());
        statementForBatchProcessing.setInt(4, log.getPrice());
        statementForBatchProcessing.setFloat(5, log.getPriceWithVat());
        statementForBatchProcessing.setInt(6, log.getValidity());
        statementForBatchProcessing.setInt(7, log.getChargeId());
        statementForBatchProcessing.setString(8, log.getTransactionId());
        statementForBatchProcessing.setString(9, log.getResponse());

        String sql = statementForBatchProcessing.toString().split(": ")[1];

        logger.log(Level.INFO, sql);

        statementForBatchProcessing.addBatch();
    }

    @Override
    public String getTableName() {
        return "charge_success_log";
    }

    @Override
    public void commitBatchedCommands(boolean shallDisposeResources) {
        logger.log(Level.DEBUG, "Changes to charge success logs are being committed.");

        int[] updateCounts = DatabaseUtils.tryExecuteBatch(statementForBatchProcessing);
        DatabaseUtils.tryCommit(connectionForBatchProcessing);

        // we could've further checked the updateCounts array for success/failure status.
        // but we shall ignore that for now just to keep things simple...

        logger.log(Level.DEBUG, updateCounts.length + " commands committed.");

        if (!shallDisposeResources) { return; }

        DatabaseUtils.tryCloseStatement(statementForBatchProcessing);
        DatabaseUtils.tryCloseConnection(connectionForBatchProcessing);
    }
}

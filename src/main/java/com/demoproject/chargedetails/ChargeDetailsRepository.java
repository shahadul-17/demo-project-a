package com.demoproject.chargedetails;

import com.demoproject.common.Tuple;
import com.demoproject.database.Repository;
import com.demoproject.database.DatabaseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChargeDetailsRepository implements Repository {

    private final Logger logger = LogManager.getLogger(ChargeDetailsRepository.class);

    public List<ChargeDetails> findAll() throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Tuple tuple = findResultSetForAll("id", "charge_code", "price", "price_with_vat", "validity");
            resultSet = tuple.get(1, ResultSet.class);
            statement = tuple.get(2, PreparedStatement.class);
            List<ChargeDetails> chargeDetailsList = new ArrayList<>();

            while (resultSet.next()) {
                int chargeDetailsId = resultSet.getInt(1);
                String chargeCode = resultSet.getString(2);
                int price = resultSet.getInt(3);
                float priceWithVat = resultSet.getFloat(4);
                int validity = resultSet.getInt(5);

                chargeDetailsList.add(new ChargeDetails(chargeDetailsId, chargeCode, price, priceWithVat, validity));
            }

            return chargeDetailsList;
        } finally {
            DatabaseUtils.tryCloseResultSet(resultSet);
            DatabaseUtils.tryCloseStatement(statement);
        }
    }

    @Override
    public String getTableName() {
        return "charge_conf";
    }
}

package com.demoproject.keyworddetails;

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

public class KeywordDetailsRepository implements Repository {

    private final Logger logger = LogManager.getLogger(KeywordDetailsRepository.class);

    public List<KeywordDetails> findAll() throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Tuple tuple = findResultSetForAll("id", "keyword", "unlockurl", "chargeurl");
            resultSet = tuple.get(1, ResultSet.class);
            statement = tuple.get(2, PreparedStatement.class);
            List<KeywordDetails> keywordDetailsList = new ArrayList<>();

            while (resultSet.next()) {
                int keywordDetailsId = resultSet.getInt(1);
                String keyword = resultSet.getString(2);
                String unlockUrl = resultSet.getString(3);
                String chargeUrl = resultSet.getString(4);

                keywordDetailsList.add(new KeywordDetails(keywordDetailsId, keyword, unlockUrl, chargeUrl));
            }

            return keywordDetailsList;
        } finally {
            DatabaseUtils.tryCloseResultSet(resultSet);
            DatabaseUtils.tryCloseStatement(statement);
        }
    }

    @Override
    public String getTableName() {
        return "keyword_details";
    }
}

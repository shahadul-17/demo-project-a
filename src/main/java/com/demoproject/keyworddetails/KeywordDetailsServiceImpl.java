package com.demoproject.keyworddetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordDetailsServiceImpl implements KeywordDetailsService {

    private final KeywordDetailsRepository repository = new KeywordDetailsRepository();
    private Map<String, KeywordDetails> keywordDetailsMap;

    private KeywordDetailsServiceImpl() { }

    @Override
    public void loadAllKeywordDetails() throws Exception {
        List<KeywordDetails> keywordDetailsList = repository.findAll();
        Map<String, KeywordDetails> keywordDetailsMap = toMap(keywordDetailsList);

        this.keywordDetailsMap = keywordDetailsMap;
    }

    public KeywordDetails getKeywordDetails(String keyword) {
        return keywordDetailsMap.get(keyword);
    }

    /**
     * From a list of keyword details, this method prepares a map
     * which contains keywords as keys and keyword details as value.
     * @param keywordDetailsList List containing keywords.
     * @return A map containing keywords as keys and details as values.
     */
    private static Map<String, KeywordDetails> toMap(List<KeywordDetails> keywordDetailsList) {
        Map<String, KeywordDetails> keywordDetailsMap = new HashMap<>(keywordDetailsList.size());

        for (KeywordDetails keywordDetails : keywordDetailsList) {
            keywordDetailsMap.put(keywordDetails.getKeyword(), keywordDetails);
        }

        return keywordDetailsMap;
    }
}

package com.demoproject.keyworddetails;

public interface KeywordDetailsService {
    void loadAllKeywordDetails() throws Exception;
    KeywordDetails getKeywordDetails(String keyword);
}

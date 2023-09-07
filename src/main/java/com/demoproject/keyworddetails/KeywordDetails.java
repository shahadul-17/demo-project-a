package com.demoproject.keyworddetails;

import com.demoproject.utils.JsonSerializer;

public class KeywordDetails {

    private int keywordDetailsId;                   // id
    private String keyword;                         // keyword
    private String unlockUrl;                       // unlockurl
    private String chargeUrl;                       // chargeurl

    public KeywordDetails() { }

    public KeywordDetails(int keywordDetailsId, String keyword, String unlockUrl,
                          String chargeUrl) {
        this.keywordDetailsId = keywordDetailsId;
        this.keyword = keyword;
        this.unlockUrl = unlockUrl;
        this.chargeUrl = chargeUrl;
    }

    public int getKeywordDetailsId() {
        return keywordDetailsId;
    }

    public void setKeywordDetailsId(int keywordDetailsId) {
        this.keywordDetailsId = keywordDetailsId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUnlockUrl() {
        return unlockUrl;
    }

    public void setUnlockUrl(String unlockUrl) {
        this.unlockUrl = unlockUrl;
    }

    public String getChargeUrl() {
        return chargeUrl;
    }

    public void setChargeUrl(String chargeUrl) {
        this.chargeUrl = chargeUrl;
    }

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}

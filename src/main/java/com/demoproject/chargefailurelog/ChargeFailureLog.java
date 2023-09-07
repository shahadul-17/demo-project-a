package com.demoproject.chargefailurelog;

import com.demoproject.utils.JsonSerializer;

public class ChargeFailureLog {

    private long chargeFailureLogId;     // id
    private long messageId;              // sms_id
    private String mobileNumber;         // msisdn
    private int keywordId;               // keyword_id
    private int price;                   // amount
    private int chargeId;                // charge_id
    private int statusCode;              // fail_code
    private String transactionId;        // tid_ref
    private String response;             // response

    public ChargeFailureLog() { }

    public ChargeFailureLog(long chargeFailureLogId, long messageId, String mobileNumber,
                            int keywordId, int price, int chargeId, int statusCode,
                            String transactionId, String response) {
        setChargeFailureLogId(chargeFailureLogId);
        setMessageId(messageId);
        setMobileNumber(mobileNumber);
        setKeywordId(keywordId);
        setPrice(price);
        setChargeId(chargeId);
        setStatusCode(statusCode);
        setTransactionId(transactionId);
        setResponse(response);
    }

    public long getChargeFailureLogId() {
        return chargeFailureLogId;
    }

    public void setChargeFailureLogId(long chargeFailureLogId) {
        this.chargeFailureLogId = chargeFailureLogId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(int keywordId) {
        this.keywordId = keywordId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getChargeId() {
        return chargeId;
    }

    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}

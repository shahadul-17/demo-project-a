package com.demoproject.chargesuccesslog;

import com.demoproject.utils.JsonSerializer;

public class ChargeSuccessLog {

    private long chargeSuccessLogId;     // id
    private long messageId;              // sms_id
    private String mobileNumber;         // msisdn
    private int keywordId;               // keyword_id
    private int price;                   // amount
    private float priceWithVat;          // amount_with_vat
    private int validity;                // validity
    private int chargeId;                // charge_id
    private String transactionId;        // tid_ref
    private String response;             // response

    public ChargeSuccessLog() { }

    public ChargeSuccessLog(long chargeSuccessLogId, long messageId, String mobileNumber, int keywordId,
                            int price, float priceWithVat, int validity, int chargeId, String transactionId,
                            String response) {
        setChargeSuccessLogId(chargeSuccessLogId);
        setMessageId(messageId);
        setMobileNumber(mobileNumber);
        setKeywordId(keywordId);
        setPrice(price);
        setPriceWithVat(priceWithVat);
        setValidity(validity);
        setChargeId(chargeId);
        setTransactionId(transactionId);
        setResponse(response);
    }

    public long getChargeSuccessLogId() {
        return chargeSuccessLogId;
    }

    public void setChargeSuccessLogId(long chargeSuccessLogId) {
        this.chargeSuccessLogId = chargeSuccessLogId;
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

    public float getPriceWithVat() {
        return priceWithVat;
    }

    public void setPriceWithVat(float priceWithVat) {
        this.priceWithVat = priceWithVat;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public int getChargeId() {
        return chargeId;
    }

    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
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

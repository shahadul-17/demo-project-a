package com.demoproject.external;

import com.demoproject.net.HttpResponse;
import com.demoproject.utils.JsonSerializer;

public class ChargeResponse extends HttpResponse {

    private String transactionId;
    private int statusCode;
    private String description;

    public ChargeResponse() { }

    public ChargeResponse(String transactionId, int statusCode, String description, HttpResponse response) {
        super(response.getStatusCode(), response.getMessage(), response.getContent());

        setTransactionId(transactionId);
        setStatusCode(statusCode);
        setDescription(description);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}

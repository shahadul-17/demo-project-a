package com.demoproject.external;

import com.demoproject.net.HttpResponse;
import com.demoproject.utils.JsonSerializer;

public class ChargeResponse extends HttpResponse {

    private String transactionId;
    private int chargeStatusCode;
    private String description;

    public ChargeResponse() { }

    public ChargeResponse(String transactionId, int chargeStatusCode, String description, HttpResponse response) {
        super(response.getStatusCode(), response.getMessage(), response.getContent());

        setTransactionId(transactionId);
        setChargeStatusCode(chargeStatusCode);
        setDescription(description);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Gets the status code that is returned
     * by the server on successful charge.
     * @return The status code that is returned
     * by the server when charged successfully.
     */
    public int getChargeStatusCode() {
        return chargeStatusCode;
    }

    /**
     * Sets the status code that is returned by the server
     * on successful charge.
     * @param chargeStatusCode The charge status code to set.
     */
    public void setChargeStatusCode(int chargeStatusCode) {
        this.chargeStatusCode = chargeStatusCode;
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

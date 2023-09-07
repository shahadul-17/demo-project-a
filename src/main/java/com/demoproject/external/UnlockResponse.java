package com.demoproject.external;

import com.demoproject.utils.JsonSerializer;
import com.demoproject.net.HttpResponse;

public class UnlockResponse extends HttpResponse {

    private int price;
    private String unlockCode;

    public UnlockResponse() { }

    public UnlockResponse(int price, String unlockCode, HttpResponse response) {
        super(response.getStatusCode(), response.getMessage(), response.getContent());

        setPrice(price);
        setUnlockCode(unlockCode);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUnlockCode() {
        return unlockCode;
    }

    public void setUnlockCode(String unlockCode) {
        this.unlockCode = unlockCode;
    }

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}

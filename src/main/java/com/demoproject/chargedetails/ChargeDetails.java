package com.demoproject.chargedetails;

import com.demoproject.utils.JsonSerializer;

public class ChargeDetails {

    private int chargeDetailsId;                    // id
    private String chargeCode;                      // charge_code
    private int price;                              // price
    private float priceWithVat;                     // price_with_vat
    private int validity;                           // validity

    public ChargeDetails() { }

    public ChargeDetails(int chargeDetailsId, String chargeCode, int price, float priceWithVat, int validity) {
        setChargeDetailsId(chargeDetailsId);
        setChargeCode(chargeCode);
        setPrice(price);
        setPriceWithVat(priceWithVat);
        setValidity(validity);
    }

    public int getChargeDetailsId() {
        return chargeDetailsId;
    }

    public void setChargeDetailsId(int chargeDetailsId) {
        this.chargeDetailsId = chargeDetailsId;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
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

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}

package com.demoproject.chargedetails;

public interface ChargeDetailsService {
    void loadAllChargeDetails() throws Exception;
    ChargeDetails getChargeDetails(Integer price);
}

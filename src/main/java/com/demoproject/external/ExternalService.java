package com.demoproject.external;

public interface ExternalService {
    UnlockResponse unlock(String keyword,
                          String message,
                          String unlockUrlFormat) throws Exception;
    ChargeResponse charge(String chargeCode,
                          String mobileNumber,
                          String transactionUrlFormat) throws Exception;
}

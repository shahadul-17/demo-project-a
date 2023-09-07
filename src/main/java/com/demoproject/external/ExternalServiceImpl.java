package com.demoproject.external;

import com.demoproject.net.SimpleHttpClient;

import java.net.URLEncoder;

public class ExternalServiceImpl implements ExternalService {

    private static final String DEFAULT_TEXT_ENCODING = "UTF-8";

    private ExternalServiceImpl() { }

    @Override
    public UnlockResponse unlock(String keyword,
                                 String message,
                                 String unlockUrlFormat) throws Exception {
        String encodedMessage = URLEncoder.encode(message, DEFAULT_TEXT_ENCODING);
        String unlockUrl = unlockUrlFormat.replaceAll("<sms_text>", encodedMessage);

        // utilizing Java 8 lambda expressions...
        return SimpleHttpClient.getHttpClient().get(unlockUrl, response -> {
            if (response.getStatusCode() != 200) {
                throw new Exception(response.getMessage());
            }

            String content = response.getContent();
            String[] splittedContent = content.split("::");

            if (splittedContent.length != 2) {
                throw new Exception("Unprocessable response received.");
            }

            String unlockCode = splittedContent[0];
            int price = Integer.parseInt(splittedContent[1]);

            return new UnlockResponse(price, unlockCode, response);
        });
    }

    @Override
    public ChargeResponse charge(String chargeCode,
                                 String mobileNumber,
                                 String chargeUrlFormat) throws Exception {
        String chargeUrl = chargeUrlFormat
                .replaceAll("<charge_code>", chargeCode)
                .replaceAll("<msisdn>", mobileNumber);

        // utilizing Java 8 lambda expressions...
        return SimpleHttpClient.getHttpClient().get(chargeUrl, response -> {
            if (response.getStatusCode() != 200) {
                throw new Exception(response.getMessage());
            }

            String content = response.getContent();
            String[] splittedContent = content.split("::");

            if (splittedContent.length != 3) {
                throw new Exception("Unprocessable response received.");
            }

            String transactionId = splittedContent[0];
            int statusCode = Integer.parseInt(splittedContent[1]);
            String description = splittedContent[2];

            return new ChargeResponse(transactionId, statusCode, description, response);
        });
    }
}

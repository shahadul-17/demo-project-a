package com.demoproject.chargedetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargeDetailsServiceImpl implements ChargeDetailsService {

    private final ChargeDetailsRepository repository = new ChargeDetailsRepository();
    private Map<Integer, ChargeDetails> chargeDetailsMap;

    private ChargeDetailsServiceImpl() { }

    @Override
    public void loadAllChargeDetails() throws Exception {
        List<ChargeDetails> chargeDetailsList = repository.findAll();
        Map<Integer, ChargeDetails> chargeDetailsMap = toMap(chargeDetailsList);

        this.chargeDetailsMap = chargeDetailsMap;
    }

    @Override
    public ChargeDetails getChargeDetails(Integer price) {
        return chargeDetailsMap.get(price);
    }

    /**
     * From a list of charge details, this method prepares a map
     * which contains prices as keys and charge details as value.
     * @param chargeDetailsList List containing charge details.
     * @return A map containing prices as keys and details as values.
     */
    private static Map<Integer, ChargeDetails> toMap(List<ChargeDetails> chargeDetailsList) {
        Map<Integer, ChargeDetails> chargeDetailsMap = new HashMap<>(chargeDetailsList.size());

        for (ChargeDetails chargeDetails : chargeDetailsList) {
            chargeDetailsMap.put(chargeDetails.getPrice(), chargeDetails);
        }

        return chargeDetailsMap;
    }
}

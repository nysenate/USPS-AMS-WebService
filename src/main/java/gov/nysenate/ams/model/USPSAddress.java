package gov.nysenate.ams.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents the data for an address inquiry.
 * The address key can be used for sorting and indexing purposes.
 */
public record USPSAddress(Address validatedAddress, ParsedAddress parsedInputAddress,
                          String postOfficeCity, String postOfficeState, String standardCityAbbr,
                          String deliveryBarCode, String carrierRoute, String addressKey, int fipsCounty) {

    // C constructor
    @SuppressWarnings("unused")
    public USPSAddress(Address validatedAddress, ParsedAddress parsedInputAddress, String postOfficeCity,
                       String postOfficeState, String standardCityAbbr, String deliveryBarCode, String carrierRoute,
                       String addressKey, String fipsCounty) {
        this(validatedAddress, parsedInputAddress, postOfficeCity, postOfficeState, standardCityAbbr, deliveryBarCode,
                carrierRoute, addressKey, StringUtils.isEmpty(fipsCounty) ? 0 : Integer.parseInt(fipsCounty));
    }
}
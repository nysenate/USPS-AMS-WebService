package gov.nysenate.ams.model;

/**
 * Represents the data for an address inquiry.
 * The address key can be used for sorting and indexing purposes.
 */
public record USPSAddress(Address validatedAddress, ParsedAddress parsedInputAddress,
                          String postOfficeCity, String postOfficeState, String standardCityAbbr,
                          String deliveryBarCode, String carrierRoute, String addressKey, String fipsCounty) {}
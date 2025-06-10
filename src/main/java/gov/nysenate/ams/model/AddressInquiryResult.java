package gov.nysenate.ams.model;

/**
 * Represents the data returned by AMS upon address inquiry.
 */
public record AddressInquiryResult(int responseCode, USPSAddress uspsAddress, int statusCode,
                                   String footnotes, AddressRecord[] records) {}

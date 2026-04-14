package gov.nysenate.ams.model;

/**
 * Represents the data returned by AMS upon address inquiry.
 */
public record AddressInquiryResult(int errorCode, USPSAddress uspsAddress, int responseCode,
                                   String footnotes, AddressRecord[] records) {}

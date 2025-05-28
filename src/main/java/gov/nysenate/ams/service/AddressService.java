package gov.nysenate.ams.service;

import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.CityStateResult;

/**
 * This interface defines the necessary address validation/lookup functions that need to be
 * supported by the address management system.
 */
public interface AddressService {
    /**
     * Performs a standardized address inquiry using an address and city/state/zip5 information.
     * @param address Address to addressInquiry.
     * @return AddressInquiryResult.
     */
    AddressInquiryResult addressInquiry(Address address);

    /**
     * Performs city/state lookup using a zip5 code as the search key.
     * @param zip5 5 digit zip code string.
     * @return CityStateResult.
     */
    CityStateResult cityStateLookup(String zip5);

    /**
     * Performs a standardized address inquiry using a 9 digit zip5.
     * @param zip9 9 digit zip5 code string. Any hyphens will be removed.
     * @return AddressInquiryResult.
     */
    AddressInquiryResult zip9Inquiry(String zip9);
}
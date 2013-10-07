package gov.nysenate.ams.service;

import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.CityStateResult;

/**
 * This interface defines the necessary address validation/lookup functions that need to be
 * supported by the address management system.
 */
public interface AddressService
{
    /**
     * Performs a standardized address inquiry using an address and city/state/zip information.
     * @param address Address to addressInquiry.
     * @return AddressInquiryResult.
     */
    public AddressInquiryResult addressInquiry(Address address);

    /**
     * Performs city/state lookup using a zip code as the search key.
     * @param zip Zip code string. Can be either a 5 or 9 digit zip. Any hyphens will be removed.
     * @return CityStateResult.
     */
    public CityStateResult cityStateLookup(String zip);

    /**
     * Performs a standardized address inquiry using a 9 digit zip.
     * @param zip9 9 digit zip code string. Any hyphens will be removed.
     * @return AddressInquiryResult.
     */
    public AddressInquiryResult zip9Inquiry(String zip9);
}

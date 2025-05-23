package gov.nysenate.ams.service;

import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.CityStateResult;

import java.util.List;

/**
 * This interface defines the necessary address validation/lookup functions that need to be
 * supported by the address management system.
 */
public interface AddressService
{
    /**
     * Performs a standardized address inquiry using an address and city/state/zip5 information.
     * @param address Address to addressInquiry.
     * @return AddressInquiryResult.
     */
    AddressInquiryResult addressInquiry(Address address);

    /**
     * Batch addressInquiry.
     * @param addresses List of input addresses.
     * @return  List of AddressInquiryResult.
     */
    default List<AddressInquiryResult> addressInquiry(List<Address> addresses) {
        return addresses.stream().map(this::addressInquiry).toList();
    }

    /**
     * Performs city/state lookup using a zip5 code as the search key.
     * @param zip5 5 digit zip code string.
     * @return CityStateResult.
     */
    CityStateResult cityStateLookup(String zip5);

    /**
     * Batch cityStateLookup.
     * @param zip5List List of input zip5s.
     * @return List of CityStateResult.
     */
    default List<CityStateResult> cityStateLookup(List<String> zip5List) {
        return zip5List.stream().map(this::cityStateLookup).toList();
    }

    /**
     * Performs a standardized address inquiry using a 9 digit zip5.
     * @param zip9 9 digit zip5 code string. Any hyphens will be removed.
     * @return AddressInquiryResult.
     */
    AddressInquiryResult zip9Inquiry(String zip9);

    /**
     * Batch zip9Inquiry.
     * @param zip9List List of input zip9s.
     * @return List of AddressInquiryResult.
     */
    default List<AddressInquiryResult> zip9Inquiry(List<String> zip9List) {
        return zip9List.stream().map(this::zip9Inquiry).toList();
    }
}
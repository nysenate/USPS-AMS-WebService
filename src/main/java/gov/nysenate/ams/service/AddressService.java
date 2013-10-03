package gov.nysenate.ams.service;

import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.USPSAddress;

/**
 * This interface defines the necessary address validation/lookup functions that need to be
 * supported by the address management system.
 */
public interface AddressService
{
    public USPSAddress validate(Address adress);
}

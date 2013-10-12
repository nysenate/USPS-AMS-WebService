package gov.nysenate.ams.client.response;

import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.StatusCode;

public class BaseAddressInquiryResponse
{
    protected Address address;
    protected StatusCode status;

    public BaseAddressInquiryResponse(AddressInquiryResult result)
    {

    }


}

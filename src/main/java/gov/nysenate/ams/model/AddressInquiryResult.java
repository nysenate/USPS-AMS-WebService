package gov.nysenate.ams.model;

import java.util.Set;

/**
 * Represents the data returned by AMS upon address inquiry.
 */
public class AddressInquiryResult
{
    protected final boolean success;
    protected final USPSAddress uspsAddress;
    protected final ReturnCode returnCode;
    protected final Set<Footnote> footnotes;

    public AddressInquiryResult(boolean success, USPSAddress uspsAddress, ReturnCode returnCode, Set<Footnote> footnotes)
    {
        this.success = success;
        this.uspsAddress = uspsAddress;
        this.returnCode = returnCode;
        this.footnotes = footnotes;
    }

    public AddressInquiryResult(int responseCode, USPSAddress uspsAddress, ReturnCode returnCode, String footnotes)
    {
        this.success = (responseCode == 0);
        this.uspsAddress = uspsAddress;
        this.returnCode = returnCode;
        this.footnotes = null; /* TODO: Parse footnote string */
    }

    public boolean isSuccess() {
        return success;
    }

    public USPSAddress getUspsAddress() {
        return uspsAddress;
    }

    public ReturnCode getReturnCode() {
        return returnCode;
    }

    public Set<Footnote> getFootnotes() {
        return footnotes;
    }
}
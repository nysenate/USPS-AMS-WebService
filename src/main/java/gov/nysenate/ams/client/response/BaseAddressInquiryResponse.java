package gov.nysenate.ams.client.response;

import gov.nysenate.ams.client.view.AddressView;
import gov.nysenate.ams.client.view.FootnoteView;
import gov.nysenate.ams.client.view.StatusCodeView;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.Footnote;
import gov.nysenate.ams.model.StatusCode;
import gov.nysenate.ams.model.USPSAddress;

import java.util.ArrayList;

public class BaseAddressInquiryResponse
{
    protected boolean validated;
    protected AddressView address;
    protected StatusCodeView status;
    protected ArrayList<FootnoteView> footnotes = new ArrayList<>();

    public BaseAddressInquiryResponse(AddressInquiryResult result)
    {
        if (result != null) {
            StatusCode statusCode = result.getStatusCode();
            USPSAddress uspsAddress = result.getUspsAddress();
            if (statusCode != null) {
                if (statusCode.equals(StatusCode.EXACT_MATCH) || statusCode.equals(StatusCode.DEFAULT_MATCH)) {
                    this.validated = true;
                }
                this.status = new StatusCodeView(result.getStatusCode());
            }
            if (uspsAddress != null) {
                this.address = new AddressView(uspsAddress.getValidatedAddress());
            }
            if (result.getFootnotes() != null && !result.getFootnotes().isEmpty()) {
                for (Footnote footnote : result.getFootnotes()) {
                    this.footnotes.add(new FootnoteView(footnote));
                }
            }
        }
    }

    public boolean isValidated() {
        return validated;
    }

    public AddressView getAddress() {
        return address;
    }

    public StatusCodeView getStatus() {
        return status;
    }

    public ArrayList<FootnoteView> getFootnotes() {
        return footnotes;
    }
}

package gov.nysenate.ams.client.response;

import gov.nysenate.ams.client.view.USPSDetailView;
import gov.nysenate.ams.client.view.AddressRecordView;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.AddressRecord;

import java.util.ArrayList;
import java.util.List;

public class DetailAddressInquiryResponse extends BaseAddressInquiryResponse
{
    protected USPSDetailView detail;
    protected List<AddressRecordView> records = new ArrayList<>();

    public DetailAddressInquiryResponse(AddressInquiryResult result) {
        super(result);
        if (result != null) {
            this.detail = new USPSDetailView(result.getUspsAddress());
            for (AddressRecord addressRecord : result.getRecords()) {
                this.records.add(new AddressRecordView(addressRecord));
            }
        }
    }

    public USPSDetailView getDetail() {
        return detail;
    }

    public List<AddressRecordView> getRecords() {
        return records;
    }
}

package gov.nysenate.ams.client.response;

import gov.nysenate.ams.client.view.AddressRecordView;
import gov.nysenate.ams.client.view.USPSDetailView;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.AddressRecord;

import java.util.ArrayList;
import java.util.List;

public class DetailAddressInquiryResponse extends BaseAddressInquiryResponse {
    protected USPSDetailView detail;
    protected List<AddressRecordView> records = new ArrayList<>();

    public DetailAddressInquiryResponse(AddressInquiryResult result, boolean initCaps) {
        super(result, initCaps);
        if (result != null) {
            this.detail = new USPSDetailView(result.uspsAddress());
            for (AddressRecord addressRecord : result.records()) {
                this.records.add(new AddressRecordView(addressRecord));
            }
        }
    }

    public USPSDetailView getDetail() {
        return detail;
    }

    public int getRecordCount() {
        return records.size();
    }

    public List<AddressRecordView> getRecords() {
        return records;
    }
}

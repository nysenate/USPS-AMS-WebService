package gov.nysenate.ams.client.response;

import gov.nysenate.ams.client.view.*;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.StatusCode;
import gov.nysenate.ams.model.USPSAddress;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public record AddressInquiryResponse(boolean validated, AddressView address, StatusCodeView status,
                                     List<FootnoteView> footnotes, USPSDetailView detail,
                                     List<AddressRecordView> records) {

    public static AddressInquiryResponse getResponse(AddressInquiryResult result) {
        StatusCode statusCode = result.statusCode();
        boolean validated = (statusCode == StatusCode.EXACT_MATCH || statusCode == StatusCode.DEFAULT_MATCH);
        USPSAddress uspsAddress = result.uspsAddress();
        AddressView addressView = null;
        if (uspsAddress != null) {
            addressView = new AddressView(uspsAddress.validatedAddress());
        }
        return new AddressInquiryResponse(validated, addressView, new StatusCodeView(statusCode),
                toViewList(result.footnotes(), FootnoteView::new), new USPSDetailView(result.uspsAddress()),
                toViewList(result.records(), AddressRecordView::from));
    }

    public Integer getRecordCount() {
        return records == null ? null : records.size();
    }

    private static <T, V> List<V> toViewList(Collection<T> list, Function<T, V> mapper) {
        if (list == null) {
            return null;
        }
        return list.stream().map(mapper).toList();
    }
}

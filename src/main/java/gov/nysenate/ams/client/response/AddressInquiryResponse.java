package gov.nysenate.ams.client.response;

import gov.nysenate.ams.client.view.*;
import gov.nysenate.ams.model.*;

import java.util.*;

public record AddressInquiryResponse(AddressView address, ResponseCodeView status,
                                     List<FootnoteView> footnotes, USPSDetailView detail,
                                     List<AddressRecordView> records, boolean success) implements BaseResponse {

    public static BaseResponse getResponse(AddressInquiryResult result) {
        ResponseCode responseCode = ResponseCode.getByCode(result.responseCode());
        var responseCodeView = new ResponseCodeView(responseCode);
        USPSAddress uspsAddress = result.uspsAddress();
        AddressView addressView = null;
        USPSDetailView detailView = null;
        if (uspsAddress != null) {
            addressView = new AddressView(uspsAddress.validatedAddress());
            detailView = new USPSDetailView(uspsAddress);
        }
        return new AddressInquiryResponse(addressView, responseCodeView,
                parseFootnotes(result.footnotes()), detailView,
                Arrays.stream(result.records()).map(AddressRecordView::from).toList(),
                uspsAddress != null && responseCode.isSuccess());
    }

    private static List<FootnoteView> parseFootnotes(String footnotes) {
        if (footnotes == null) {
            return null;
        }
        var footnoteViews = new ArrayList<FootnoteView>();
        for (char c : footnotes.toCharArray()) {
            try {
                Footnote footnote = Footnote.valueOf(String.valueOf(c).toUpperCase());
                footnoteViews.add(new FootnoteView(footnote));
            }
            catch (IllegalArgumentException ignored) {}
        }
        return footnoteViews;
    }
}

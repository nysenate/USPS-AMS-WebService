package gov.nysenate.ams.client.response;

import gov.nysenate.ams.client.view.*;
import gov.nysenate.ams.model.*;

import java.util.*;

public record AddressInquiryResponse(AddressView address, ResponseCodeView status,
                                     List<FootnoteView> footnotes, USPSDetailView detail,
                                     List<AddressRecordView> records) implements BaseResponse {

    public static BaseResponse getResponse(AddressInquiryResult result) {
        ResponseCode responseCode = ResponseCode.getByCode(result.responseCode());
        USPSAddress uspsAddress = result.uspsAddress();
        var responseCodeView = new ResponseCodeView(responseCode);
        if (uspsAddress == null) {
            return new BaseResponse() {
                @Override
                public boolean isSuccess() {
                    return false;
                }

                // Used in the frontend.
                @SuppressWarnings("unused")
                public ResponseCodeView status() {
                    return responseCodeView;
                }
            };
        }
        AddressView addressView = new AddressView(uspsAddress.validatedAddress());
        return new AddressInquiryResponse(addressView, responseCodeView,
                parseFootnotes(result.footnotes()), new USPSDetailView(uspsAddress),
                Arrays.stream(result.records()).map(AddressRecordView::from).toList());
    }

    @Override
    public boolean isSuccess() {
        return true;
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

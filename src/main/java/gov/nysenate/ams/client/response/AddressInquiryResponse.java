package gov.nysenate.ams.client.response;

import gov.nysenate.ams.client.view.*;
import gov.nysenate.ams.model.*;

import java.util.*;

public record AddressInquiryResponse(boolean validated, AddressView address, StatusCodeView status,
                                     List<FootnoteView> footnotes, USPSDetailView detail,
                                     List<AddressRecordView> records) {

    public static AddressInquiryResponse getResponse(AddressInquiryResult result) {
        StatusCode statusCode = StatusCode.getByCode(result.statusCode());
        boolean validated = (statusCode == StatusCode.EXACT_MATCH || statusCode == StatusCode.DEFAULT_MATCH);
        USPSAddress uspsAddress = result.uspsAddress();
        AddressView addressView = null;
        if (uspsAddress != null) {
            addressView = new AddressView(uspsAddress.validatedAddress());
        }
        return new AddressInquiryResponse(validated, addressView, new StatusCodeView(statusCode),
                parseFootnotes(result.footnotes()), uspsAddress == null ? null : new USPSDetailView(uspsAddress),
                parseRecords(result.records()));
    }

    // Used in the frontend.
    @SuppressWarnings("unused")
    public boolean isEmpty() {
        return records == null || records.isEmpty();
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

    private static List<AddressRecordView> parseRecords(AddressRecord[] records) {
        if (records == null) {
            return null;
        }
        return Arrays.stream(records).map(AddressRecordView::from).toList();
    }
}

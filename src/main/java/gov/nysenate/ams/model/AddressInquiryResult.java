package gov.nysenate.ams.model;

import java.util.*;

/**
 * Represents the data returned by AMS upon address inquiry.
 */
public record AddressInquiryResult(int responseCode, USPSAddress uspsAddress, StatusCode statusCode,
                                   Set<Footnote> footnotes, List<AddressRecord> records) {

    // C constructor
    @SuppressWarnings("unused")
    public AddressInquiryResult(int responseCode, USPSAddress uspsAddress, int statusCode,
                                String footnotes, AddressRecord[] records) {
        this(responseCode, uspsAddress, StatusCode.getByCode(statusCode), parseFootnotes(footnotes),
                records == null ? List.of() : List.of(records));
    }

    public AddressInquiryResult(StatusCode errorCode) {
        this(-1, null, errorCode, Set.of(), List.of());
    }

    private static Set<Footnote> parseFootnotes(String footnotes) {
        if (footnotes == null) {
            return Set.of();
        }
        var footnoteSet = new HashSet<Footnote>();
        for (char c : footnotes.toCharArray()) {
            try {
                footnoteSet.add(Footnote.valueOf(String.valueOf(c).toUpperCase()));
            }
            catch (IllegalArgumentException ignored) {}
        }
        return footnoteSet;
    }
}

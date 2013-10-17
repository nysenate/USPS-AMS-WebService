package gov.nysenate.ams.model;

import java.util.*;

/**
 * Represents the data returned by AMS upon address inquiry.
 */
public class AddressInquiryResult
{
    protected final int responseCode;
    protected final USPSAddress uspsAddress;
    protected final StatusCode statusCode;
    protected final Set<Footnote> footnotes;
    protected final List<AddressRecord> records;

    public AddressInquiryResult(int responseCode, USPSAddress uspsAddress, int statusCode, String footnotes,
                                AddressRecord[] records)
    {
        this.responseCode = responseCode;
        this.uspsAddress = uspsAddress;
        this.statusCode = StatusCode.getByCode(statusCode);
        this.footnotes = new HashSet<>();
        if (footnotes != null && !footnotes.isEmpty()) {
            footnotes = footnotes.replaceAll("(#| )", "");
            for (String s : footnotes.split("")) {
                try {
                    if (s != null && !s.isEmpty()) {
                        this.footnotes.add(Footnote.valueOf(s.toUpperCase()));
                    }
                }
                catch (IllegalArgumentException ex) {
                    /** Ignore bad footnote value. */
                }
            }
        }
        if (records != null && records.length > 0) {
            this.records = Arrays.asList(records);
        }
        else {
            this.records = new ArrayList<>();
        }

    }

    public boolean isSuccess() {
        return (this.responseCode == 0);
    }

    public USPSAddress getUspsAddress() {
        return uspsAddress;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public Set<Footnote> getFootnotes() {
        return footnotes;
    }

    public List<AddressRecord> getRecords() {
        return records;
    }
}
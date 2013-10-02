package gov.nysenate.ams.model;

public enum RecordType
{
    F ("Firm",
        "This is a match to a Firm Record, which is the finest level of match available for an address."),

    G ("General Delivery",
        "This is a match to a General Delivery record."),

    H ("Building / Apartment",
        "This is a match to a Building or Apartment record."),

    P ("Post Office Box",
        "This is a match to a Post Office Box."),

    R ("Rural Route or Highway Contract",
        "This is a match to either a Rural Route or a Highway Contract record, both of which may have " +
        "associated Box Number ranges."),

    S ("Street Record",
        "This is a match to a Street record containing a valid primary number range.");

    private String shortDesc;
    private String longDesc;

    RecordType(String shortDesc, String longDesc) {
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }
}

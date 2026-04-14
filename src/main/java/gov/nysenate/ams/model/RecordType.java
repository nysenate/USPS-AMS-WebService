package gov.nysenate.ams.model;

/**
 * Represents the type of record associated with an entry in an address record stack.
 */
public enum RecordType {
    F("Firm"), G("General Delivery"),
    H("Building / Apartment"), P("Post Office Box"),
    R("Rural Route or Highway Contract"), S("Street Record");

    private final String shortDesc;

    RecordType(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }
}

package gov.nysenate.ams.model;


/**
 * An immutable representation of a basic address object.
 * addr1 is the street address, and addr2 is the secondary address.
 */
public record Address(String firmName, String addr1, String addr2, String city, String state,
                      String zip5, String zip4) {

    public Address(String addr1, String addr2, String city, String state,
                   String zip5, String zip4) {
        this("", addr1, addr2, city, state, zip5, zip4);
    }

    // The C constructor
    public Address(String firmName, String addr1, String addr2, String city, String state,
                   String zip5, String zip4) {
        this.firmName = nonNull(firmName);
        this.addr1 = nonNull(addr1);
        this.addr2 = nonNull(addr2);
        this.zip5 = nonNull(zip5);
        this.zip4 = nonNull(zip4);
        city = nonNull(city);
        state = nonNull(state);

        if (state.length() > 2) {
            this.city = city + ", " + state + ", " + this.zip5;
            this.state = "";
        }
        else {
            this.city = city;
            this.state = state;
        }
    }

    // Used in C
    @SuppressWarnings("unused")
    public String zip9() {
        return zip5 + zip4;
    }

    private static String nonNull(String str) {
        return str == null ? "" : str;
    }
}

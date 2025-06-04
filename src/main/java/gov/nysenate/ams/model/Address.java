package gov.nysenate.ams.model;


/**
 * An immutable representation of a basic address object.
 * addr1 is the street address, and addr2 is the secondary address.
 */
public record Address(String firmName, String addr1, String addr2, String city, String state,
                      String zip5, String zip4) {

    // Also the C constructor
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

    /**
     * Indicates if the address object is empty.
     * @return true if all the address fields are empty.
     */
    public boolean isEmpty() {
        return addr1.isBlank() && addr2.isBlank() && city.isBlank() &&
                state.isBlank() && zip5.isBlank();
    }

    private static String nonNull(String str) {
        return str == null ? "" : str;
    }
}

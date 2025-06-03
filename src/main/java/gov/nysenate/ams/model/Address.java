package gov.nysenate.ams.model;


/**
 * A mostly immutable representation of a basic address object.
 */
public class Address {
    private final String firmName;
    private final String addr1;            // Street Address
    private final String addr2;            // Secondary Address
    private final String city;
    private final String state;
    private final String zip5;
    private final String zip4;

    protected Integer id;

    // The C constructor.
    public Address(String firmName, String addr1, String addr2, String city, String state, String zip5, String zip4) {
        this(firmName, addr1, addr2, city, state, zip5, zip4, false);
    }

    public Address(String firmName, String addr1, String addr2, String city, String state, String zip5, String zip4, Integer id) {
        this.firmName = firmName;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.city = city;
        this.state = state;
        this.zip5 = zip5;
        this.zip4 = zip4;
        this.id = id;
    }

    public Address(String firmName, String addr1, String addr2, String city, String state, String zip5, String zip4, boolean merge) {
        this.firmName = nonNull(firmName);
        this.addr1 = nonNull(addr1);
        this.addr2 = nonNull(addr2);
        this.zip5 = nonNull(zip5);
        this.zip4 = nonNull(zip4);
        city = nonNull(city);
        state = nonNull(state);

        if (state.length() > 2 || merge) {
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
        return addr1.trim().isEmpty() && addr2.trim().isEmpty() && city.trim().isEmpty() &&
                state.trim().isEmpty() && zip5.trim().isEmpty();
    }

    public String getFirmName() {
        return firmName;
    }

    public String getAddr1() {
        return addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip5() {
        return zip5;
    }

    public String getZip4() {
        return zip4;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private static String nonNull(String str) {
        return str == null ? "" : str;
    }
}

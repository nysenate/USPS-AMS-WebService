package gov.nysenate.ams.model;


/**
 * A mostly immutable representation of a basic address object.
 */
public class Address
{
    protected final String firmName;
    protected final String addr1;            // Street Address
    protected final String addr2;            // Secondary Address
    protected final String city;
    protected final String state;
    protected final String zip5;
    protected final String zip4;

    protected Integer id;

    public Address(String address, boolean merge)
    {
        this("", address, "", "", "", "", "", merge);
    }

    // The C constructor.
    public Address(String firmName, String addr1, String addr2, String city, String state, String zip5, String zip4)
    {
        this(firmName, addr1, addr2, city, state, zip5, zip4, false);
    }

    public Address(String firmName, String addr1, String addr2, String city, String state, String zip5, String zip4, Integer id)
    {
        this.firmName = firmName;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.city = city;
        this.state = state;
        this.zip5 = zip5;
        this.zip4 = zip4;
        this.id = id;
    }

    public Address(String firmName, String addr1, String addr2, String city, String state, String zip5, String zip4, boolean merge)
    {
        this.firmName = (firmName != null) ? firmName : "";
        this.addr1 = (addr1 != null) ? addr1 : "";
        this.addr2 = (addr2 != null) ? addr2 : "";
        this.zip5 = (zip5 != null) ? zip5 : "";
        this.zip4 = (zip4 != null) ? zip4 : "";

        if ((state != null && state.length() > 2) || merge) {
            city = (city != null) ? city : "";
            state = (state != null) ? state : "";
            zip5 = (zip5 != null) ? zip5 : "";
            this.city = city + ", " + state + ", " + zip5;
            this.state = "";
        }
        else {
            this.city = (city != null) ? city : "";
            this.state = (state != null) ? state : "";
        }
    }

    /**
     * Indicates if the address object is empty.
     * @return true if all the address fields are empty.
     */
    public boolean isEmpty()
    {
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
}

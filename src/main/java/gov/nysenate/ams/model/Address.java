package gov.nysenate.ams.model;


/**
 * A mostly immutable representation of a basic address object.
 */
public class Address
{
    protected final String firmName;         // Firm Name
    protected final String addr1;            // Street Address
    protected final String addr2;            // Secondary Address
    protected final String city;             // City
    protected final String state;            // State
    protected final String zip5;             // Zip 5
    protected final String zip4;             // Zip 4

    protected Integer id;
    protected boolean merge = false;

    public Address(String address)
    {
        this("", address, "", "", "", "", "", false);
    }

    public Address(String addr1, String addr2, String city, String state, String zip5)
    {
        this("", addr1, addr2, city, state, zip5, "", false);
    }

    public Address(String firmName, String addr1, String addr2, String city, String state, String zip5, String zip4)
    {
        this(firmName, addr1, addr2, city, state, zip5, zip4, false);
    }


    public Address(String addr1, String addr2, String city, String state, String zip5, Integer id)
    {
        this.firmName = "";
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.city = city;
        this.state = state;
        this.zip5 = zip5;
        this.zip4 = "";
        this.id = id;
        this.merge = false;
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
        this.merge = false;
    }

    public Address(String firmName, String addr1, String addr2, String city, String state, String zip5, String zip4, boolean merge)
    {
        this.firmName = (firmName != null) ? firmName : "";
        this.addr1 = (addr1 != null) ? addr1 : "";
        this.addr2 = (addr2 != null) ? addr2 : "";
        this.zip5 = (zip5 != null) ? zip5 : "";
        this.zip4 = (zip4 != null) ? zip4 : "";

        if (state.length() > 2 || merge) {
            city = (city != null) ? city : "";
            state = (state != null) ? state : "";
            zip5 = (zip5 != null) ? zip5 : "";
            this.city = city + ", " + state + ", " + zip5;
            this.state = "";
            this.merge = true;
        }
        else {
            this.city = (city != null) ? city : "";
            this.state = (state != null) ? state : "";
            this.merge = false;
        }
    }

    /**
     * Indicates if the address object is empty.
     * @return true if all of the address fields are empty.
     */
    public boolean isEmpty()
    {
        return (addr1.trim().isEmpty() && !isParsed());
    }

    /**
     * Indicates if the address object is in a semi-parsed format.
     * @return true if there are non-empty address fields other than addr1.
     */
    public boolean isParsed()
    {
        return !(addr2.trim().isEmpty() && city.trim().isEmpty() &&
                state.trim().isEmpty() && zip5.trim().isEmpty());
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

    public boolean isMerge() {
        return merge;
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

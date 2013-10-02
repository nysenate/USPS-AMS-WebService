package gov.nysenate.ams.model;

/**
 * An immutable representation of a basic address object.
 */
public class Address
{
    protected final String firmName;         // Firm Name
    protected final String addr1;            // Street Address
    protected final String addr2;            // Secondary Address
    protected final String city;             // City
    protected final String state;            // State
    protected final String zip;              // Zip

    public Address(String firmName, String addr1, String addr2, String city, String state, String zip)
    {
        this.firmName = firmName;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public Address(String addr1, String addr2, String city, String state, String zip)
    {
        this("", addr1, addr2, city, state, zip);
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
                state.trim().isEmpty() && zip.trim().isEmpty());
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

    public String getZip() {
        return zip;
    }
}
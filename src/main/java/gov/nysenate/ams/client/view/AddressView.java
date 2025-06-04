package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.Address;
import org.apache.commons.lang3.text.WordUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressView
{
    protected String firm = "";
    protected String addr1 = "";
    protected String addr2 = "";
    protected String city = "";
    protected String state = "";
    protected String zip5 = "";
    protected String zip4 = "";
    protected Integer id = null;

    public AddressView(Address address, boolean initCaps) {
        if (address != null) {
            this.firm = address.firmName();
            this.addr1 = address.addr1();
            this.addr2 = address.addr2();
            this.city = address.city();
            this.state = address.state();
            this.zip5 = address.zip5();
            this.zip4 = address.zip4();

            if (initCaps) {
                this.city = (this.city != null && !this.city.isEmpty()) ? WordUtils.capitalizeFully(this.city.toLowerCase()) : "";

                if (this.addr1 != null && !this.addr1.isEmpty()) {
                    this.addr1 = initCapStreetLine(this.addr1);
                }

                if (this.addr2 != null && !this.addr2.isEmpty()) {
                    this.addr2 = initCapStreetLine(this.addr2);
                }
            }
        }
    }

    /**
     * Makes the address line init capped as in:
     *    TYPICAL ST NW APT 1S -> W Typical St NW Apt 1S
     * Some exceptions include unit characters and directionals.
     * @param line String
     * @return String
     */
    private String initCapStreetLine(String line)
    {
        /** Perform init caps on the street address */
        line = WordUtils.capitalizeFully(line.toLowerCase());

        /** Ensure unit portion is fully uppercase e.g 2N */
        Pattern p = Pattern.compile("([0-9]+-?[a-z]+[0-9]*)$");
        Matcher m = p.matcher(line);
        if (m.find()) {
            line = m.replaceFirst(m.group().toUpperCase());
        }

        /** Ensure (SW|SE|NW|NE) are not init capped */
        p = Pattern.compile("(?i)\\b(SW|SE|NW|NE)\\b");
        m = p.matcher(line);
        if (m.find()) {
            line = m.replaceAll(m.group().toUpperCase());
        }

        /** Change Po Box to PO Box */
        line = line.replaceAll("Po Box", "PO Box");

        return line;
    }

    public String getFirm() {
        return firm;
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

    public Integer getId() { return id; }
}
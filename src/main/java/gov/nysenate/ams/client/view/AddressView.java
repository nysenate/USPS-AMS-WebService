package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.Address;

public class AddressView
{
    protected String firm = "";
    protected String addr1 = "";
    protected String addr2 = "";
    protected String city = "";
    protected String state = "";
    protected String zip5 = "";
    protected String zip4 = "";

    public AddressView(Address address)
    {
        if (address != null) {
            this.firm = address.getFirmName();
            this.addr1 = address.getAddr1();
            this.addr2 = address.getAddr2();
            this.city = address.getCity();
            this.state = address.getState();
            this.zip5 = address.getZip5();
            this.zip4 = address.getZip4();
        }
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
}
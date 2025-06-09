package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.Address;

public class AddressView {
    protected String addr1 = "";
    protected String addr2 = "";
    protected String city = "";
    protected String state = "";
    protected String zip5 = "";
    protected String zip4 = "";

    public AddressView(Address address) {
        if (address != null) {
            this.addr1 = address.addr1();
            this.addr2 = address.addr2();
            this.city = address.city();
            this.state = address.state();
            this.zip5 = address.zip5();
            this.zip4 = address.zip4();
        }
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

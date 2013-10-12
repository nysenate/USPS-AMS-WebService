package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.Address;

public class AddressView extends Address {
    public AddressView(String firmName, String addr1, String addr2, String city, String state, String zip5, String zip4) {
        super(firmName, addr1, addr2, city, state, zip5, zip4);
    }
}
package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.Address;

public record AddressView(String addr1, String addr2, String city, String state, String zip5, String zip4) {
    public AddressView(Address address) {
        this(address.addr1(), address.addr2(),  address.city(), address.state(), address.zip5(), address.zip4());
    }
}

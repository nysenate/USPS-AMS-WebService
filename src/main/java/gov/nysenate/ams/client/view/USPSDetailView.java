package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.USPSAddress;

public record USPSDetailView(String standardCityAbbr, String postOfficeCity, String postOfficeState,
                             String deliveryBarCode, String carrierRoute, String fipsCounty) {
    public USPSDetailView(USPSAddress uspsAddress) {
        this(uspsAddress.standardCityAbbr(), uspsAddress.postOfficeCity(), uspsAddress.postOfficeState(),
                uspsAddress.deliveryBarCode(), uspsAddress.carrierRoute(), uspsAddress.fipsCounty());
    }
}

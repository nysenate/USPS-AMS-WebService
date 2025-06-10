package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.ParsedAddress;
import gov.nysenate.ams.model.USPSAddress;

public record USPSDetailView(String standardCityAbbr, String postOfficeCity, String postOfficeState,
                             String deliveryBarCode, String carrierRoute, int fipsCounty,
                             ParsedAddress parsedAddress) {
    public USPSDetailView(USPSAddress uspsAddress) {
        this(uspsAddress.standardCityAbbr(), uspsAddress.postOfficeCity(), uspsAddress.postOfficeState(), uspsAddress.deliveryBarCode(), uspsAddress.carrierRoute(),
                uspsAddress.fipsCounty().isEmpty() ? 0 : Integer.parseInt(uspsAddress.fipsCounty()), uspsAddress.parsedInputAddress());
    }
}

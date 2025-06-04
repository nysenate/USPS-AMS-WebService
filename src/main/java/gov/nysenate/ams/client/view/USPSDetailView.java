package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.ParsedAddress;
import gov.nysenate.ams.model.USPSAddress;

public class USPSDetailView
{
    protected String standardCityAbbr;
    protected String postOfficeCity;
    protected String postOfficeState;
    protected String deliveryBarCode;
    protected String carrierRoute;
    protected int fipsCounty;
    protected ParsedAddress parsedAddress;

    public USPSDetailView(USPSAddress uspsAddress)
    {
        if (uspsAddress != null) {
            this.standardCityAbbr = uspsAddress.standardCityAbbr();
            this.postOfficeCity = uspsAddress.postOfficeCity();
            this.postOfficeState = uspsAddress.postOfficeState();
            this.deliveryBarCode = uspsAddress.deliveryBarCode();
            this.carrierRoute = uspsAddress.carrierRoute();
            this.fipsCounty = uspsAddress.fipsCounty();
            this.parsedAddress = uspsAddress.parsedInputAddress();
        }
    }

    public String getStandardCityAbbr() {
        return standardCityAbbr;
    }

    public String getPostOfficeCity() {
        return postOfficeCity;
    }

    public String getPostOfficeState() {
        return postOfficeState;
    }

    public String getDeliveryBarCode() {
        return deliveryBarCode;
    }

    public String getCarrierRoute() {
        return carrierRoute;
    }

    public int getFipsCounty() {
        return fipsCounty;
    }

    public ParsedAddress getParsedAddress() {
        return parsedAddress;
    }
}
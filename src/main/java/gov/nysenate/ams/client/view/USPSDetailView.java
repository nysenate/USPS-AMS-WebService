package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.USPSAddress;

public class USPSDetailView
{
    protected String standardCityAbbr;
    protected String postOfficeCity;
    protected String postOfficeState;
    protected String deliveryBarCode;
    protected String carrierRoute;
    protected int fipsCounty;

    public USPSDetailView(USPSAddress uspsAddress)
    {
        if (uspsAddress != null) {
            this.standardCityAbbr = uspsAddress.getStandardCityAbbr();
            this.postOfficeCity = uspsAddress.getPostOfficeCity();
            this.postOfficeState = uspsAddress.getPostOfficeState();
            this.deliveryBarCode = uspsAddress.getDeliveryBarCode();
            this.carrierRoute = uspsAddress.getCarrierRoute();
            this.fipsCounty = uspsAddress.getFipsCounty();
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
}
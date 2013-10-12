package gov.nysenate.ams.model;

/**
 * Represents the data for an address inquiry.
 */
public class USPSAddress
{
    protected final Address validatedAddress;          // The standardized USPS address
    protected final ParsedAddress parsedInputAddress;  // Parsed input address
    protected final String postOfficeCity;             // Main Post Office city
    protected final String postOfficeState;            // Main Post Office state
    protected final String standardCityAbbr;           // Standardized city abbreviation
    protected final String deliveryBarCode;            // Delivery point bar code
    protected final String carrierRoute;               // Carrier route
    protected final String addressKey;                 // Address key used for indexing purposes
    protected final int fipsCounty;                   // FIPS county code

    public USPSAddress(Address validatedAddress, ParsedAddress parsedInputAddress, String postOfficeCity,
                       String postOfficeState, String standardCityAbbr, String deliveryBarCode, String carrierRoute,
                       String addressKey, String fipsCounty)
    {
        this.validatedAddress = validatedAddress;
        this.parsedInputAddress = parsedInputAddress;
        this.postOfficeCity = postOfficeCity;
        this.postOfficeState = postOfficeState;
        this.standardCityAbbr = standardCityAbbr;
        this.deliveryBarCode = deliveryBarCode;
        this.carrierRoute = carrierRoute;
        this.addressKey = addressKey;
        if (fipsCounty != null && !fipsCounty.isEmpty()) {
            this.fipsCounty = Integer.parseInt(fipsCounty);
        }
        else {
            this.fipsCounty = 0;
        }
    }

    public Address getValidatedAddress() {
        return validatedAddress;
    }

    public ParsedAddress getParsedInputAddress() {
        return parsedInputAddress;
    }

    public String getPostOfficeCity() {
        return postOfficeCity;
    }

    public String getPostOfficeState() {
        return postOfficeState;
    }

    public String getStandardCityAbbr() {
        return standardCityAbbr;
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

    public String getAddressKey() {
        return addressKey;
    }
}
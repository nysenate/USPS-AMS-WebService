package gov.nysenate.ams.model;

/**
 * Represents an address record in the format of the USPS ZIP+4 file.
 */
public class AddressRecord
{
    protected final int recordID;
    protected final String zip;
    protected RecordType recordType;
    protected final String preDir;
    protected final String streetName;
    protected final String suffix;
    protected final String postDir;
    protected final String primaryLow;
    protected final String primaryHigh;
    protected final char primaryParity;
    protected final String bldgFirmName;
    protected final String unit;
    protected final String secLow;
    protected final String secHigh;
    protected final char secCode;
    protected final String addonLow;
    protected final String addonHigh;
    protected final String financeCode;
    protected final String stateAbbr;
    protected final String countyNum;
    protected final String congressionalDist;
    protected final String municipality;
    protected final String urbanization;
    protected final String lastLine;

    public AddressRecord(int recordID, String zip, String preDir, String streetName, String suffix, String postDir,
                         String primaryLow, String primaryHigh,  String bldgFirmName, String unit,
                         String secLow, String secHigh,  String addonLow, String addonHigh, String financeCode,
                         String stateAbbr, String countyNum, String congressionalDist, String municipality,
                         String urbanization, String lastLine,char primaryParity, char secCode, char recordType)
    {
        this.recordID = recordID;
        this.zip = zip;
        this.preDir = preDir;
        this.streetName = streetName;
        this.suffix = suffix;
        this.postDir = postDir;
        this.primaryLow = primaryLow;
        this.primaryHigh = primaryHigh;
        this.primaryParity = primaryParity;
        this.bldgFirmName = bldgFirmName;
        this.unit = unit;
        this.secLow = secLow;
        this.secHigh = secHigh;
        this.secCode = secCode;
        this.addonLow = addonLow;
        this.addonHigh = addonHigh;
        this.financeCode = financeCode;
        this.stateAbbr = stateAbbr;
        this.countyNum = countyNum;
        this.congressionalDist = congressionalDist;
        this.municipality = municipality;
        this.urbanization = urbanization;
        this.lastLine = lastLine;
        this.recordType = RecordType.valueOf(Character.toString(recordType).toUpperCase());
    }

    public int getRecordID() {
        return recordID;
    }

    public String getZip() {
        return zip;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public String getPreDir() {
        return preDir;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getPostDir() {
        return postDir;
    }

    public String getPrimaryLow() {
        return primaryLow;
    }

    public String getPrimaryHigh() {
        return primaryHigh;
    }

    public char getPrimaryParity() {
        return primaryParity;
    }

    public String getBldgFirmName() {
        return bldgFirmName;
    }

    public String getUnit() {
        return unit;
    }

    public String getSecLow() {
        return secLow;
    }

    public String getSecHigh() {
        return secHigh;
    }

    public char getSecCode() {
        return secCode;
    }

    public String getAddonLow() {
        return addonLow;
    }

    public String getAddonHigh() {
        return addonHigh;
    }

    public String getFinanceCode() {
        return financeCode;
    }

    public String getStateAbbr() {
        return stateAbbr;
    }

    public String getCountyNum() {
        return countyNum;
    }

    public String getCongressionalDist() {
        return congressionalDist;
    }

    public String getMunicipality() {
        return municipality;
    }

    public String getUrbanization() {
        return urbanization;
    }

    public String getLastLine() {
        return lastLine;
    }
}
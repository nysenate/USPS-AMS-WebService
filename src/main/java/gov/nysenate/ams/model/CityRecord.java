package gov.nysenate.ams.model;

/**
 * Represents a City/State record that is populated during a city/state request.
 */
public class CityRecord
{
    protected final char detailCode;           // Copyright Detail Code
    protected final String zipCode;            // Zip code
    protected final String cityKey;            // City/state key
    protected final ZipClassCode zipClassCode;
    protected final String cityName;           // City/state name
    protected final String cityAbbrev;         // City/state name abbrev
    protected final FacilityCode facilityCd;
    protected final char mailingNameInd;       // Mailing name indicator:
                                               // Y = Mailing name
                                               // N = Non-mailing name
    protected final String lastLineNum;        // Preferred last line key
    protected final String lastLineName;       // Preferred city name
    protected final char cityDelvInd;
    protected final char autoZoneInd;
    protected final char uniqueZipInd;
    protected final String stateAbbr;          // State abbreviation
    protected final String countyNum;          // County number
    protected final String countyName;         // County name

    public CityRecord(String countyName, String stateAbbr, String zipCode,  String lastLineName, String lastLineNum,
                      String cityAbbrev, String cityName,String cityKey, String countyNum, char zipClassCode,
                      char mailingNameInd,  char detailCode, char facilityCd, char cityDelvInd, char autoZoneind,
                      char uniqueZipInd)
    {
        if(countyName != null)
        {
            this.countyName = countyName.trim();
        }
        else {this.countyName = "";}
        this.countyNum = countyNum;
        this.stateAbbr = stateAbbr;
        this.lastLineName = lastLineName;
        this.lastLineNum = lastLineNum;
        this.mailingNameInd = mailingNameInd;
        this.facilityCd = FacilityCode.valueOf(Character.toString(facilityCd));
        this.cityAbbrev = cityAbbrev;
        this.cityName = cityName;
        this.zipClassCode =ZipClassCode.getValue(Character.toString(zipClassCode));
        this.cityKey = cityKey;
        this.zipCode = zipCode;
        this.detailCode = detailCode;
        this.cityDelvInd = cityDelvInd;
        this.autoZoneInd = autoZoneind;
        this.uniqueZipInd = uniqueZipInd;
    }

    public char getDetailCode() {
        return detailCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCityKey() {
        return cityKey;
    }

    public ZipClassCode getZipClassCode() {
        return zipClassCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityAbbrev() {
        return cityAbbrev;
    }

    public FacilityCode getFacilityCd() {
        return facilityCd;
    }

    public char getMailingNameInd() {
        return mailingNameInd;
    }

    public String getLastLineNum() {
        return lastLineNum;
    }

    public String getLastLineName() {
        return lastLineName;
    }

    public String getStateAbbr() {
        return stateAbbr;
    }

    public String getCountyNum() {
        return countyNum;
    }

    public String getCountyName() {
        return countyName;
    }

    public char getCityDelvInd() {
        return cityDelvInd;
    }

    public char getAutoZoneInd() {
        return autoZoneInd;
    }

    public char getUniqueZipInd() {
        return uniqueZipInd;
    }
}
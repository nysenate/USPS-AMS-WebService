package gov.nysenate.ams.model;

/**
 * Represents a City/State record that is populated during a city/state request.
 */
public class CityRecord
{
    protected final char detailCode;      // Copyright Detail Code
    protected final String zipCode;       // Zip code
    protected final String cityKey;       // City/state key
    protected final char zipClassCode;    // Zip classification code:
                                          //    blank = non-unique zip
                                          //    M = APO/FPO military zip
                                          //    P = PO BOX zip
                                          //    U = Unique zip

    protected final String cityName;      // City/state name
    protected final String cityAbbrev;    // City/state name abbrev
    protected final char facilityCd;      // Facility code:
                                          //    A = Airport mail facility
                                          //    B = Branch
                                          //    C = Community post office
                                          //    D = Area distrib. center
                                          //    E = Sect. center facility
                                          //    F = General distrib. center
                                          //    G = General mail facility
                                          //    K = Bulk mail center
                                          //    M = Money order unit
                                          //    N = Non-postal name
                                          //       community name,former postal facility,or place name
                                          //    P = Post office
                                          //    S = Station
                                          //    U = Urbanization

    protected final char mailingNameInd;  // Mailing name indicator:
                                          //    Y = Mailing name
                                          //    N = Non-mailing name

    protected final String lastLineNum;   // Preferred last line key
    protected final String lastLineName;  // Preferred city name
    protected final String stateAbbr;     // State abbreviation
    protected final int countyNum;        // County number
    protected final String countyName;    // County name

    public CityRecord(String countyName, int countyNum, String stateAbbr, String lastLineName, String lastLineNum,
                      char mailingNameInd, char facilityCd, String cityAbbrev, String cityName, char zipClassCode,
                      String cityKey, String zipCode, char detailCode)
    {
        this.countyName = countyName;
        this.countyNum = countyNum;
        this.stateAbbr = stateAbbr;
        this.lastLineName = lastLineName;
        this.lastLineNum = lastLineNum;
        this.mailingNameInd = mailingNameInd;
        this.facilityCd = facilityCd;
        this.cityAbbrev = cityAbbrev;
        this.cityName = cityName;
        this.zipClassCode = zipClassCode;
        this.cityKey = cityKey;
        this.zipCode = zipCode;
        this.detailCode = detailCode;
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

    public char getZipClassCode() {
        return zipClassCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityAbbrev() {
        return cityAbbrev;
    }

    public char getFacilityCd() {
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

    public int getCountyNum() {
        return countyNum;
    }

    public String getCountyName() {
        return countyName;
    }
}
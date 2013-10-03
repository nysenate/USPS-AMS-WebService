package gov.nysenate.ams.model;


public class CityRec

{
       protected final char detailCode;      // copyright detail code

    protected final String zipCode;               // zip code

    protected final String cityKey;               // city/state key

    protected final char zipClassCode;      // zip classification code
                                                   // blank = non-unique zip
                                                   // M=APO/FPO military zip
                                                   // P=PO BOX zip
                                                   // U=Unique zip

       protected final String cityName;           // city/state name

    protected final String cityAbbrev;            // city/state name abbrev

    protected final char facilityCd;            // facility code
                                                   // A=Airport mail facility
                                                   // B=Branch
                                                   // C=Community post office
                                                   // D=Area distrib. center
                                                   // E=Sect. center facility
                                                   // F=General distrib. center
                                                   // G=General mail facility
                                                   // K=Bulk mail center
                                                   // M=Money order unit
                                                   // N=Non-postal name
                                                   // community name,former postal facility,or place name
                                                   // P=Post office
                                                   // S=Station
                                                   // U=Urbanization

       protected final char mailingNameInd; // mailing name indicator
                                                   // Y=Mailing name
                                                   // N=Non-mailing name

    protected final String lastLineNum;          // preferred last line key
    protected final String lastLineName;         // preferred city name

    protected CityRec (char detailCode, String zipCode, String cityKey, char zipClassCode,
                       String cityName, String cityAbbrev, char facilityCd, char mailingNameInd,
                       String lastLineNum, String lastLineName)

        {
               this.detailCode = detailCode;
            this.zipCode = zipCode;
            this.cityKey = cityKey;
            this.zipClassCode = zipClassCode;
            this.cityName = cityName;
            this.cityAbbrev = cityAbbrev;
            this.facilityCd = facilityCd;
            this.mailingNameInd = mailingNameInd;
            this.lastLineNum = lastLineNum;
            this.lastLineName = lastLineName;

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
}

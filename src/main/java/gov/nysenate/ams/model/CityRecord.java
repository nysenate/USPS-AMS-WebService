package gov.nysenate.ams.model;

/**
 * Represents a City/State record that is populated during a city/state request.
 * "city" in these variable names actually refers to "city/state".
 * Some notes on the fields:
 * cityKey, lastLineName, uniqueZipInd: not currently populated
 * lastLineNum: preferred last line key
 */
public record CityRecord(char detailCode, String zipCode, String cityKey, ZipClassCode zipClassCode,
                         String cityName, String cityAbbrev, FacilityCode facilityCd,
                         MailingNameIndicator mailingNameInd, String lastLineNum, String lastLineName,
                         char cityDelvInd, char autoZoneInd, char uniqueZipInd, String stateAbbr,
                         String countyNum, String countyName) {

    // C constructor
    @SuppressWarnings("unused")
    public CityRecord(String countyName, String stateAbbr, String zipCode,  String lastLineName, String lastLineNum,
                      String cityAbbrev, String cityName, String cityKey, String countyNum, char zipClassCode,
                      char mailingNameInd,  char detailCode, char facilityCd, char cityDelvInd, char autoZoneind,
                      char uniqueZipInd) {
        this(detailCode, zipCode, cityKey, ZipClassCode.getValue(Character.toString(zipClassCode)),
                cityName, cityAbbrev, FacilityCode.valueOf(Character.toString(facilityCd)),
                MailingNameIndicator.valueOf(String.valueOf(mailingNameInd)),
                lastLineNum, lastLineName, cityDelvInd, autoZoneind, uniqueZipInd, stateAbbr, countyNum,
                countyName == null ? "" : countyName.trim());
    }
}

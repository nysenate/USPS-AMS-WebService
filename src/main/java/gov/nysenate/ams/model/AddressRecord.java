package gov.nysenate.ams.model;

/**
 * Represents an address record in the format of the USPS ZIP+4 file.
 */
public record AddressRecord(int recordID, String zip, RecordType recordType,
                            String preDir, String streetName, String suffix, String postDir,
                            String primaryLow, String primaryHigh, char primaryParity,
                            String bldgFirmName, String unit, String secLow, String secHigh, char secCode,
                            String addonLow, String addonHigh, String financeCode, String stateAbbr, String countyNum,
                            String congressionalDist, String municipality, String urbanization, String lastLine) {

    // C constructor
    public AddressRecord(int recordID, String zip, String preDir, String streetName, String suffix, String postDir,
                         String primaryLow, String primaryHigh,  String bldgFirmName, String unit,
                         String secLow, String secHigh,  String addonLow, String addonHigh, String financeCode,
                         String stateAbbr, String countyNum, String congressionalDist, String municipality,
                         String urbanization, String lastLine,char primaryParity, char secCode, char recordType) {
        this(recordID, zip, RecordType.valueOf(Character.toString(recordType).toUpperCase()),
                preDir, streetName, suffix, postDir, primaryLow, primaryHigh, primaryParity,
                bldgFirmName, unit, secLow, secHigh, secCode, addonLow, addonHigh, financeCode,
                stateAbbr, countyNum, congressionalDist, municipality, urbanization, lastLine);
    }
}

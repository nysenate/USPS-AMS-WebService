package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.AddressRecord;
import gov.nysenate.ams.model.RecordType;

public record AddressRecordView(int recordId, String recordType, String recordTypeDesc,
                                String primaryLow, String primaryHigh, String primaryParity,
                                String preDir, String streetName, String streetSuffix, String postDir,
                                String unit, String secondaryLow, String secondaryHigh, String secondaryParity,
                                String zip5, String zip4Low, String zip4High) {

    public static AddressRecordView from(AddressRecord addressRecord) {
        if (addressRecord == null) {
            return null;
        }
        RecordType rType =  addressRecord.recordType();
        return new AddressRecordView(addressRecord.recordID(), rType.name(), rType.getShortDesc(),
                trimLeadingZeroes(addressRecord.primaryLow()), trimLeadingZeroes(addressRecord.primaryHigh()),
                getParity(addressRecord.primaryParity()), addressRecord.preDir(), addressRecord.streetName(),
                addressRecord.suffix(), addressRecord.postDir(), addressRecord.unit(),
                trimLeadingZeroes(addressRecord.secLow()), trimLeadingZeroes(addressRecord.secHigh()),
                getParity(addressRecord.secCode()), addressRecord.zip(),
                addressRecord.addonLow(), addressRecord.addonHigh());
    }

    private static String trimLeadingZeroes(String s) {
        if (s != null) {
            return s.replaceFirst("^0+(?!$)", "");
        }
        return "";
    }

    private static String getParity(char parityCode) {
        return switch (parityCode) {
            case 'E' -> "EVEN";
            case 'O' -> "ODD";
            case 'B' -> "BOTH";
            default -> "";
        };
    }
}

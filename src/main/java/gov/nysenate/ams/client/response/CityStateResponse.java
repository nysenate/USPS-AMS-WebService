package gov.nysenate.ams.client.response;

import gov.nysenate.ams.model.CityRecord;
import gov.nysenate.ams.model.CityStateResult;

public record CityStateResponse(boolean success, String cityName, String cityAbbr, String zipCode,
                                String stateAbbr, char mailingNameInd, String preferredCity, String countyName,
                                String zipClassDesc, String facility) {

    public static CityStateResponse from(CityStateResult result) {
        CityRecord cityRecord = result.cityRecord();
        return new CityStateResponse(result.responseCode() == 0, cityRecord.cityName(), cityRecord.cityAbbrev(),
                cityRecord.zipCode(), cityRecord.stateAbbr(), cityRecord.mailingNameInd().name().charAt(0),
                cityRecord.lastLineName(), cityRecord.countyName(),
                cityRecord.zipClassCode().getDesc(), cityRecord.facilityCd().getDesc());
    }
}

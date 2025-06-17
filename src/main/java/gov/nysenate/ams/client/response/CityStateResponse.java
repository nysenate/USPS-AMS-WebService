package gov.nysenate.ams.client.response;

import gov.nysenate.ams.model.CityRecord;
import gov.nysenate.ams.model.CityStateResult;

public record CityStateResponse(String cityName, String cityAbbr, String zipCode,
                                String stateAbbr, char mailingNameInd, String preferredCity, String countyName,
                                String zipClassDesc, String facility) implements BaseResponse{

    public static BaseResponse from(CityStateResult result) {
        CityRecord cityRecord = result.cityRecord();
        if (cityRecord == null) {
            return () -> false;
        }
        return new CityStateResponse(cityRecord.cityName(), cityRecord.cityAbbrev(),
                cityRecord.zipCode(), cityRecord.stateAbbr(), cityRecord.mailingNameInd(),
                cityRecord.lastLineName(), cityRecord.countyName(),
                cityRecord.zipClassCode().getDesc(), cityRecord.facilityCd().getDesc());
    }

    @Override
    public boolean isSuccess() {
        return true;
    }
}

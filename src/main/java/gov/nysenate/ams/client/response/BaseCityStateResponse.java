package gov.nysenate.ams.client.response;

import gov.nysenate.ams.model.CityRecord;
import gov.nysenate.ams.model.CityStateResult;
import org.apache.commons.lang.WordUtils;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 10/17/13
 * Time: 2:47 PM
 */
public class BaseCityStateResponse {
    protected boolean success;
    protected String cityName;
    protected String cityAbbr;
    protected String zipCode;
    protected String stateAbbr;

    public BaseCityStateResponse(CityStateResult result, boolean initCaps) {
        if (result == null) {
            return;
        }
        this.success = result.isSuccess();
        CityRecord cityRecord = result.cityRecord();
        if (cityRecord == null) {
            return;
        }
        if (initCaps) {
            this.cityName = WordUtils.capitalizeFully(cityRecord.cityName());
            this.cityAbbr = WordUtils.capitalizeFully(cityRecord.cityAbbrev());
        } else {
            this.cityName = cityRecord.cityName();
            this.cityAbbr = cityRecord.cityAbbrev();
        }
        this.zipCode = cityRecord.zipCode();
        this.stateAbbr = cityRecord.stateAbbr();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityAbbr() {
        return cityAbbr;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getStateAbbr() {
        return stateAbbr;
    }
}

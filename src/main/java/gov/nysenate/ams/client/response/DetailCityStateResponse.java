package gov.nysenate.ams.client.response;

import gov.nysenate.ams.client.view.FacilityCodeView;
import gov.nysenate.ams.client.view.ZipClassCodeView;
import gov.nysenate.ams.model.CityRecord;
import gov.nysenate.ams.model.CityStateResult;

public class DetailCityStateResponse extends BaseCityStateResponse {
    protected ZipClassCodeView zipClass;
    protected FacilityCodeView facility;
    protected char mailingNameInd;
    protected String preferredCity;
    protected String countyNum;
    protected String countyName;

    public DetailCityStateResponse(CityStateResult result, boolean initCaps) {
        super(result, initCaps);
        if (result == null) {
            return;
        }
        CityRecord cityRecord = result.cityRecord();
        if (cityRecord == null) {
            return;
        }
        this.zipClass = new ZipClassCodeView(cityRecord.zipClassCode());
        this.facility = new FacilityCodeView(cityRecord.facilityCd());
        this.mailingNameInd = cityRecord.mailingNameInd().name().charAt(0);
        this.preferredCity = cityRecord.lastLineName();
        this.countyNum = cityRecord.countyNum();
        this.countyName = cityRecord.countyName();
    }

    public ZipClassCodeView getZipClass() {
        return zipClass;
    }

    public FacilityCodeView getFacility() {
        return facility;
    }

    public String getMailingNameInd() {
        return Character.toString(mailingNameInd);
    }

    public String getPreferredCity() {
        return preferredCity;
    }

    public String getCountyNum() {
        return countyNum;
    }

    public String getCountyName() {
        return countyName;
    }
}

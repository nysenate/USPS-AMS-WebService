package gov.nysenate.ams.client.response;

import gov.nysenate.ams.client.view.FacilityCodeView;
import gov.nysenate.ams.client.view.ZipClassCodeView;
import gov.nysenate.ams.model.CityRecord;
import gov.nysenate.ams.model.CityStateResult;
import org.apache.commons.lang.WordUtils;

public class DetailCityStateResponse {
    private boolean success;
    private String cityName;
    private String cityAbbr;
    private String zipCode;
    private String stateAbbr;

    private char mailingNameInd;
    private String preferredCity;
    private String countyName;
    // TODO: only need the descriptions for both of these. Change index.jsp to match.
    private ZipClassCodeView zipClass;
    private FacilityCodeView facility;

    public DetailCityStateResponse(CityStateResult result) {
        if (result == null) {
            return;
        }
        this.success = result.isSuccess();
        CityRecord cityRecord = result.cityRecord();
        if (cityRecord == null) {
            return;
        }
        this.cityName = WordUtils.capitalizeFully(cityRecord.cityName());
        this.cityAbbr = WordUtils.capitalizeFully(cityRecord.cityAbbrev());
        this.zipCode = cityRecord.zipCode();
        this.stateAbbr = cityRecord.stateAbbr();

        this.zipClass = new ZipClassCodeView(cityRecord.zipClassCode());
        this.facility = new FacilityCodeView(cityRecord.facilityCd());
        this.mailingNameInd = cityRecord.mailingNameInd().name().charAt(0);
        this.preferredCity = cityRecord.lastLineName();
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

    public String getCountyName() {
        return countyName;
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

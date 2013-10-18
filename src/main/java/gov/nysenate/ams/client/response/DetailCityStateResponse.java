package gov.nysenate.ams.client.response;

import gov.nysenate.ams.client.view.FacilityCodeView;
import gov.nysenate.ams.client.view.ZipClassCodeView;
import gov.nysenate.ams.model.CityStateResult;

public class DetailCityStateResponse extends BaseCityStateResponse {

    protected ZipClassCodeView zipClass;
    protected FacilityCodeView facility;
    protected char mailingNameInd;
    protected String preferredCity;
    protected String countyNum;
    protected String countyName;

    public DetailCityStateResponse(CityStateResult result)
    {
        super(result);
        if(result != null && result.getCityRecord() != null)
        {
            this.zipClass = new ZipClassCodeView(result.getCityRecord().getZipClassCode());
            this.facility = new FacilityCodeView(result.getCityRecord().getFacilityCd());
            this.mailingNameInd = result.getCityRecord().getMailingNameInd();
            this.preferredCity = result.getCityRecord().getPreferredCity();
            this.countyNum = result.getCityRecord().getCountyNum();
            this.countyName = result.getCityRecord().getCountyName();
        }
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

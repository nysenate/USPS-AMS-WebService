package gov.nysenate.ams.client.response;

import gov.nysenate.ams.client.view.FacilityCodeView;
import gov.nysenate.ams.client.view.ZipClassCodeView;
import gov.nysenate.ams.model.CityStateResult;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 10/17/13
 * Time: 3:09 PM
 */
public class DetailCityStateResponse extends BaseCityStateResponse {

    protected ZipClassCodeView zipClassCode;
    protected FacilityCodeView facilityCode;
    protected char mailingNameInd;
    protected String preferredCity;
    protected String countyNum;
    protected String countyName;

    public DetailCityStateResponse(CityStateResult result)
    {
        super(result);
        if(result != null)
        {
            this.zipClassCode = new ZipClassCodeView(result.getCityRecord().getZipClassCode());
            this.facilityCode = new FacilityCodeView(result.getCityRecord().getFacilityCd());
            this.mailingNameInd = result.getCityRecord().getMailingNameInd();
            this.preferredCity = result.getCityRecord().getPreferredCity();
            this.countyNum = result.getCityRecord().getCountyNum();
            this.countyName = result.getCityRecord().getCountyName();
        }
    }

    public ZipClassCodeView getZipClassCode() {
        return zipClassCode;
    }

    public FacilityCodeView getFacilityCode() {
        return facilityCode;
    }

    public char getMailingNameInd() {
        return mailingNameInd;
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

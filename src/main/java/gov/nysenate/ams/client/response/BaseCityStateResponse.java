package gov.nysenate.ams.client.response;

import gov.nysenate.ams.model.CityStateResult;
import org.apache.commons.lang.WordUtils;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 10/17/13
 * Time: 2:47 PM
 */
public class BaseCityStateResponse
{
    protected boolean success;
    protected String cityName;
    protected String cityAbbr;
    protected String zipCode;
    protected String stateAbbr;

    public BaseCityStateResponse(CityStateResult result, boolean initCaps)
    {
        if(result != null)
        {
            this.success = result.isSuccess();
            if (result.getCityRecord() != null) {
                if(initCaps)
                {
                    this.cityName = WordUtils.capitalizeFully(result.getCityRecord().getCityName());
                    this.cityAbbr = WordUtils.capitalizeFully(result.getCityRecord().getCityAbbrev());
                }
                else
                {
                    this.cityName = result.getCityRecord().getCityName();
                    this.cityAbbr = result.getCityRecord().getCityAbbrev();
                }
                this.zipCode = result.getCityRecord().getZipCode();
                this.stateAbbr = result.getCityRecord().getStateAbbr();

            }
        }
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

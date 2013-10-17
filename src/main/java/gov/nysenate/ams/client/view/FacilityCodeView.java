package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.FacilityCode;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 10/17/13
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class FacilityCodeView
{
    protected String code;
    protected String desc;

    public FacilityCodeView(FacilityCode code)
    {
        if (code != null) {
            this.code = code.name();
            this.desc = code.getDesc();
        }
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

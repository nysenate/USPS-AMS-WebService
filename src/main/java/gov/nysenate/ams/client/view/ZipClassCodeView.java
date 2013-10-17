package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.ZipClassCode;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 10/17/13
 * Time: 2:21 PM
 */
public class ZipClassCodeView
{
    protected String code;
    protected String desc;

    public ZipClassCodeView(ZipClassCode code)
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

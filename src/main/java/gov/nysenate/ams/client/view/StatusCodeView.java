package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.StatusCode;

public class StatusCodeView
{
    protected int code = 0;
    protected String name = "";
    protected String desc = "";

    public StatusCodeView(StatusCode statusCode)
    {
        if (statusCode != null) {
            this.code = statusCode.getCode();
            this.name = statusCode.name();
            this.desc = statusCode.getMessage();
        }
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}

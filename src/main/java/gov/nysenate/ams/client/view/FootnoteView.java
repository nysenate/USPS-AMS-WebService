package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.Footnote;

public class FootnoteView
{
    protected String code;
    protected String name;
    protected String desc;

    public FootnoteView(Footnote footnote)
    {
        if (footnote != null) {
            this.code = footnote.name();
            this.name = footnote.getShortDesc();
            this.desc = footnote.getLongDesc();
        }
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}

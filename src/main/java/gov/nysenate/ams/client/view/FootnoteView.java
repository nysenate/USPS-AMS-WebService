package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.Footnote;

public class FootnoteView {
    protected String name;
    protected String desc;

    public FootnoteView(Footnote footnote) {
        if (footnote != null) {
            this.name = footnote.getShortDesc();
            this.desc = footnote.getLongDesc();
        }
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}

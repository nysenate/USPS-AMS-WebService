package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.Footnote;

public record FootnoteView(String shortDesc, String longDesc) {
    public FootnoteView(Footnote footnote) {
        this(footnote.getShortDesc(), footnote.getLongDesc());
    }
}

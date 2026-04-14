package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.ResponseCode;

public record ResponseCodeView(String shortDesc, String longDesc) {
    public ResponseCodeView(ResponseCode responseCode) {
        this(responseCode.getShortDesc(), responseCode.getLongDesc());
    }
}

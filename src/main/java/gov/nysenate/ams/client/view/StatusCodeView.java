package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.StatusCode;

public record StatusCodeView(int code, String name, String desc) {
    public StatusCodeView(StatusCode statusCode) {
        this(statusCode.getCode(), statusCode.name(), statusCode.getMessage());
    }
}

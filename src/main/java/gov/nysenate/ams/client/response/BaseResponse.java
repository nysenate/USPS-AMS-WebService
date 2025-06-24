package gov.nysenate.ams.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface BaseResponse {
    @JsonProperty
    boolean success();
}

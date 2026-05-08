package gov.nysenate.ams.controller;

import com.fasterxml.jackson.databind.JsonNode;
import gov.nysenate.ams.client.response.BaseResponse;
import gov.nysenate.ams.client.response.CityStateResponse;
import gov.nysenate.ams.model.CityStateResult;

import jakarta.servlet.http.HttpServletRequest;

public class CityStateController extends BaseApiController<String> {
    /* TODO - Implement init caps functionality for city/state */
    @Override
    protected String getInputFromParams(HttpServletRequest request) {
        return getZip5FromParams(request);
    }

    @Override
    protected String getInputFromJson(JsonNode node) {
        return node.asText();
    }

    @Override
    protected BaseResponse getResponse(String input) {
        CityStateResult result = amsNativeProvider.cityStateLookup(input);
        return CityStateResponse.from(result);
    }
}

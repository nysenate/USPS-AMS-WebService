package gov.nysenate.ams.controller;

import com.fasterxml.jackson.databind.JsonNode;
import gov.nysenate.ams.client.response.CityStateResponse;
import gov.nysenate.ams.model.CityStateResult;

import javax.servlet.http.HttpServletRequest;

public class CityStateController extends BaseApiController<String, CityStateResult> {
    @Override
    protected String getInputFromParams(HttpServletRequest request) {
        return getZip5FromParams(request);
    }

    @Override
    protected String getInputFromJson(JsonNode node) {
        return node.asText();
    }

    @Override
    protected CityStateResult getResult(String input) {
        return amsNativeProvider.cityStateLookup(input);
    }

    @Override
    protected Object getResponse(CityStateResult result) {
        return CityStateResponse.from(result);
    }
}

package gov.nysenate.ams.controller;

import com.fasterxml.jackson.databind.JsonNode;
import gov.nysenate.ams.client.response.BaseCityStateResponse;
import gov.nysenate.ams.client.response.DetailCityStateResponse;
import gov.nysenate.ams.model.CityStateResult;

import javax.servlet.http.HttpServletRequest;

public class CityStateController extends BaseApiController<String, CityStateResult> {
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
    protected CityStateResult getResult(String input) {
        return amsNativeProvider.cityStateLookup(input);
    }

    @Override
    protected Object getResponse(boolean detail, boolean initCaps, CityStateResult result) {
        if (detail) {
            return new DetailCityStateResponse(result, initCaps);
        }
        return new BaseCityStateResponse(result, initCaps);
    }
}

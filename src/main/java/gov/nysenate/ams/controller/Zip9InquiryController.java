package gov.nysenate.ams.controller;

import gov.nysenate.ams.client.response.AddressInquiryResponse;
import gov.nysenate.ams.client.response.BaseResponse;
import gov.nysenate.ams.model.AddressInquiryResult;
import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.JsonNode;

public class Zip9InquiryController extends BaseApiController<String> {
    @Override
    protected String getInputFromParams(HttpServletRequest request) {
        return getZip5FromParams(request) + StringUtils.defaultIfEmpty(request.getParameter("zip4"), "");
    }

    @Override
    protected String getInputFromJson(JsonNode node) {
        String zip5 = node.hasNonNull("zip5") ? node.get("zip5").asString() : "";
        String zip4 = node.hasNonNull("zip4") ? node.get("zip4").asString() : "";
        return zip5 + zip4;
    }

    @Override
    protected BaseResponse getResponse(String input) {
        AddressInquiryResult result = amsNativeProvider.zip9Inquiry(input);
        return AddressInquiryResponse.getResponse(result);
    }
}

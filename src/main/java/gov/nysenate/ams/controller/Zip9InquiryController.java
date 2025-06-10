package gov.nysenate.ams.controller;

import com.fasterxml.jackson.databind.JsonNode;
import gov.nysenate.ams.client.response.AddressInquiryResponse;
import gov.nysenate.ams.model.AddressInquiryResult;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class Zip9InquiryController extends BaseApiController<String, AddressInquiryResult> {
    @Override
    protected String getInputFromParams(HttpServletRequest request) {
        return getZip5FromParams(request) + StringUtils.defaultIfEmpty(request.getParameter("zip4"), "");
    }

    @Override
    protected String getInputFromJson(JsonNode node) {
        String zip5 = node.hasNonNull("zip5") ? node.get("zip5").asText() : "";
        String zip4 = node.hasNonNull("zip4") ? node.get("zip4").asText() : "";
        return zip5 + zip4;
    }

    @Override
    protected AddressInquiryResult getResult(String input) {
        return amsNativeProvider.zip9Inquiry(input);
    }

    @Override
    protected Object getResponse(AddressInquiryResult result) {
        return AddressInquiryResponse.getResponse(result);
    }
}

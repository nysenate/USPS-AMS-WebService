package gov.nysenate.ams.controller;

import com.fasterxml.jackson.databind.JsonNode;
import gov.nysenate.ams.client.response.AddressInquiryResponse;
import gov.nysenate.ams.client.response.BaseResponse;
import gov.nysenate.ams.model.AddressInquiryResult;
import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zip9InquiryController extends BaseApiController<String> {
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
    protected BaseResponse getResponse(String input) {
        AddressInquiryResult result = amsNativeProvider.zip9Inquiry(input);
        return AddressInquiryResponse.getResponse(result);
    }
}

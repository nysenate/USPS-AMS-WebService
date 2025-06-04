package gov.nysenate.ams.controller;

import com.fasterxml.jackson.databind.JsonNode;
import gov.nysenate.ams.client.response.BaseAddressInquiryResponse;
import gov.nysenate.ams.client.response.DetailAddressInquiryResponse;
import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Servlet to handle address validation requests.
 */
public class AddressValidateController extends BaseApiController<Address, AddressInquiryResult> {
    /**
     * Constructs a new Address object using the query parameters of the supplied HttpServletRequest.
     * This method exists to provide consistency among the different controllers when retrieving an
     * address from the query string.
     * @param request HttpServletRequest object
     * @return new Address instance if request was valid
     *         null if request was null
     */
    @Override
    protected Address getInputFromParams(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        if (request.getParameter("addr") != null) {
            return new Address("", request.getParameter("addr"), "",
                    "", "", "", "");
        }
        else {
            return new Address(request.getParameter("firm"), request.getParameter("addr1"),
                    request.getParameter("addr2"), request.getParameter("city"),
                    request.getParameter("state"), request.getParameter("zip5"),
                    request.getParameter("zip4"));
        }
    }

    /**
     * Constructs an Address object using the given JSON, which must be an array containing a collection of
     * address component objects, e.g.
     * <code>
     *  [{"addr1":"", "addr2":"", "city":"", "state":"","zip5":"", "zip4":""} .. ]
     * </code>
     * @param node JSON payload
     * @return Address
     */
    @Override
    protected Address getInputFromJson(JsonNode node) {
        String firm = getOrEmpty(node, "firm");
        String addr1 = getOrEmpty(node, "addr1");
        String addr2 = getOrEmpty(node, "addr2");
        String city = getOrEmpty(node, "city");
        String state = getOrEmpty(node, "state");
        String zip5 = getOrEmpty(node, "zip5");
        String zip4 = getOrEmpty(node, "zip4");
        return new Address(firm, addr1, addr2, city, state, zip5, zip4);
    }

    @Override
    protected AddressInquiryResult getResult(Address input) {
        return amsNativeProvider.addressInquiry(input);
    }

    @Override
    protected Object getResponse(boolean detail, boolean initCaps, AddressInquiryResult result) {
        if (detail) {
            return new DetailAddressInquiryResponse(result, initCaps);
        }
        return new BaseAddressInquiryResponse(result, initCaps);
    }

    private static String getOrEmpty(JsonNode baseNode, String fieldName) {
        return baseNode.has(fieldName) ? baseNode.get(fieldName).asText() : "";
    }
}

package gov.nysenate.ams.controller;

import gov.nysenate.ams.client.response.BaseAddressInquiryResponse;
import gov.nysenate.ams.client.response.DetailAddressInquiryResponse;
import gov.nysenate.ams.dao.AmsNativeDao;
import gov.nysenate.ams.filter.ApiFilter;
import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.util.OutputUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to handle address validation requests.
 */
public class AddressValidateController extends BaseApiController
{
    @Override
    public void init(ServletConfig config) throws ServletException {}

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Object responseObj = null;

        /** Retrieve input address from query parameters. */
        Address inputAddress = getAddressFromParams(request);

        /** Query Parameter - detail : If true, output all info returned by AMS. */
        boolean detail = Boolean.parseBoolean(request.getParameter("detail"));

        /** Perform address validation if input address is valid. */
        if (inputAddress != null && !inputAddress.isEmpty()) {
            AmsNativeDao amsNativeDao = new AmsNativeDao();
            AddressInquiryResult result = amsNativeDao.addressInquiry(inputAddress);

            /** Create the response object based on the detail level. */
            if (!detail) {
                responseObj = new BaseAddressInquiryResponse(result);
            }
            else {
                responseObj = new DetailAddressInquiryResponse(result);
            }
        }

        /** Set the response. */
        ApiFilter.setApiResponse(responseObj, request);
    }
}

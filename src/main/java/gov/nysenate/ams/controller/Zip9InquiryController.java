package gov.nysenate.ams.controller;

import gov.nysenate.ams.client.response.BaseAddressInquiryResponse;
import gov.nysenate.ams.client.response.DetailAddressInquiryResponse;
import gov.nysenate.ams.dao.AmsNativeDao;
import gov.nysenate.ams.filter.ApiFilter;
import gov.nysenate.ams.model.AddressInquiryResult;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Zip9InquiryController extends BaseApiController
{
    @Override
    public void init(ServletConfig config) throws ServletException {}

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Object responseObj = null;

        /** Query Parameter - detail : If true, output all info returned by AMS. */
        boolean detail = Boolean.parseBoolean(request.getParameter("detail"));

        /** Query Parameter - zip5 : 5 digit zip code. */
        String zip5 = request.getParameter("zip5");

        /** Query Parameter - zip4 : 4 digit zip code extension. */
        String zip4 = request.getParameter("zip4");

        if (zip5 != null && !zip5.isEmpty() && zip4 != null && !zip4.isEmpty()) {
            String zip9 = zip5 + zip4;
            AmsNativeDao amsNativeDao = new AmsNativeDao();
            AddressInquiryResult result = amsNativeDao.zip9Inquiry(zip9);

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

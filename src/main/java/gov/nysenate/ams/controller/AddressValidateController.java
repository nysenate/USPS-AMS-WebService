package gov.nysenate.ams.controller;

import gov.nysenate.ams.client.response.BaseAddressInquiryResponse;
import gov.nysenate.ams.client.response.BatchResponse;
import gov.nysenate.ams.client.response.DetailAddressInquiryResponse;
import gov.nysenate.ams.dao.AmsNativeDao;
import gov.nysenate.ams.filter.ApiFilter;
import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.provider.AmsNativeProvider;
import gov.nysenate.ams.provider.AmsWebProvider;
import gov.nysenate.ams.util.Application;
import gov.nysenate.ams.util.OutputUtil;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet to handle address validation requests.
 */
public class AddressValidateController extends BaseApiController
{
    private Logger logger = Logger.getLogger(AddressValidateController.class);
    private AmsNativeProvider amsNativeProvider;
    private AmsWebProvider amsWebProvider;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        this.amsNativeProvider = Application.getAmsNativeProvider();
        //this.amsWebProvider = new AmsWebProvider();
        logger.debug("Initialized AddressValidateController.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Object responseObj = null;

        /** Query Parameter - batch : If true, process batch payload, otherwise delegate to GET. */
        boolean batch = Boolean.parseBoolean(request.getParameter("batch"));

        /** Query Parameter - detail : If true, output all info returned by AMS. */
        boolean detail = Boolean.parseBoolean(request.getParameter("detail"));

        /** Handle batch address validation request. */
        if (batch) {
            String json = IOUtils.toString(request.getInputStream(), "UTF-8");
            List<Address> inputAddresses = getAddressesFromJsonBody(json);
            List<AddressInquiryResult> results = new ArrayList<>();
            if (inputAddresses != null && inputAddresses.size() > 0) {
                results = amsNativeProvider.addressInquiry(inputAddresses);
            }
            List<BaseAddressInquiryResponse> baseResponses = new ArrayList<>();
            List<DetailAddressInquiryResponse> detailResponses = new ArrayList<>();
            for (AddressInquiryResult result : results) {
                if (!detail) {
                    baseResponses.add(new BaseAddressInquiryResponse(result));
                }
                else {
                    detailResponses.add(new DetailAddressInquiryResponse(result));
                }
            }
            if (!detail) {
                responseObj = new BatchResponse<>(baseResponses);
            }
            else {
                responseObj = new BatchResponse<>(detailResponses);
            }

            /** Set the response. */
            ApiFilter.setApiResponse(responseObj, request);
        }

        /** Delegate to default GET request otherwise. */
        else {
            doGet(request, response);
        }
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
            AddressInquiryResult result = amsNativeProvider.addressInquiry(inputAddress);
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

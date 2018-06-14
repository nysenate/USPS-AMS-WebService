package gov.nysenate.ams.controller;

import gov.nysenate.ams.client.response.BaseAddressInquiryResponse;
import gov.nysenate.ams.client.response.BatchResponse;
import gov.nysenate.ams.client.response.DetailAddressInquiryResponse;
import gov.nysenate.ams.filter.ApiFilter;
import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.provider.AmsNativeProvider;
import gov.nysenate.ams.util.Application;
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

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        this.amsNativeProvider = Application.getAmsNativeProvider();
        logger.debug("Initialized AddressValidateController.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Object responseObj;

        boolean batch = isBatch(request);
        boolean detail = isDetail(request);
        boolean initCaps = isInitCaps(request);

        if (batch) {
            String json = IOUtils.toString(request.getInputStream(), "UTF-8");
            List<Address> inputAddresses = getAddressesFromJson(json);
            List<AddressInquiryResult> results = new ArrayList<>();
            if (inputAddresses != null && inputAddresses.size() > 0) {
                results = amsNativeProvider.addressInquiry(inputAddresses);
            }
            List<BaseAddressInquiryResponse> baseResponses = new ArrayList<>();
            List<DetailAddressInquiryResponse> detailResponses = new ArrayList<>();
            for (AddressInquiryResult result : results) {
                if (!detail) {
                    baseResponses.add(new BaseAddressInquiryResponse(result, initCaps));
                }
                else {
                    detailResponses.add(new DetailAddressInquiryResponse(result, initCaps));
                }
            }
            if (!detail) {
                responseObj = new BatchResponse<>(baseResponses);
            }
            else {
                responseObj = new BatchResponse<>(detailResponses);
            }

            ApiFilter.setApiResponse(responseObj, request);
        }
        else {
            doGet(request, response);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Object responseObj;

        Address inputAddress = getAddressFromParams(request);
        inputAddress.setMerge(Boolean.parseBoolean(request.getParameter("merge")));
        boolean detail = isDetail(request);
        boolean initCaps = isInitCaps(request);

        AddressInquiryResult result = amsNativeProvider.addressInquiry(inputAddress);

        if (!detail) {
            responseObj = new BaseAddressInquiryResponse(result, initCaps);
        }
        else {
            responseObj = new DetailAddressInquiryResponse(result, initCaps);
        }

        ApiFilter.setApiResponse(responseObj, request);
    }
}
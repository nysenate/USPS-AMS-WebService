package gov.nysenate.ams.controller;

import gov.nysenate.ams.client.response.BaseAddressInquiryResponse;
import gov.nysenate.ams.client.response.BatchResponse;
import gov.nysenate.ams.client.response.DetailAddressInquiryResponse;
import gov.nysenate.ams.filter.ApiFilter;
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

public class Zip9InquiryController extends BaseApiController
{
    private Logger logger = Logger.getLogger(Zip9InquiryController.class);
    private AmsNativeProvider amsNativeProvider;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        this.amsNativeProvider = Application.getAmsNativeProvider();
        logger.debug("Initialized Zip9InquiryController.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Object responseObj;

        boolean batch = isBatch(request);
        boolean detail = isDetail(request);

        if (batch) {
            String json = IOUtils.toString(request.getInputStream(), "UTF-8");
            List<String> zip9List = getZip9ListFromJson(json);
            List<AddressInquiryResult> results = new ArrayList<>();
            if (zip9List != null && zip9List.size() > 0) {
                results = amsNativeProvider.zip9Inquiry(zip9List);
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

        boolean detail = isDetail(request);
        String zip9 = getZip9FromParams(request);

        AddressInquiryResult result = amsNativeProvider.zip9Inquiry(zip9);

        if (!detail) {
            responseObj = new BaseAddressInquiryResponse(result);
        }
        else {
            responseObj = new DetailAddressInquiryResponse(result);
        }

        ApiFilter.setApiResponse(responseObj, request);
    }
}

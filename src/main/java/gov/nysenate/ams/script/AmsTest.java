package gov.nysenate.ams.script;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nysenate.ams.dao.AmsNativeDao;
import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.AmsSettings;
import gov.nysenate.ams.util.Application;
import gov.nysenate.ams.util.OutputUtil;
import gov.nysenate.util.Config;
import org.apache.log4j.Logger;

public class AmsTest
{
    private static Logger logger = Logger.getLogger(AmsTest.class);

    public static void main(String args[])
    {
        Application.bootstrap();
        logger.info("Library Path: " + System.getProperty("java.library.path"));

        Config config = Application.getConfig();
        AmsNativeDao amsNativeDao = new AmsNativeDao();
        AmsSettings amsSettings = new AmsSettings(config);
        amsNativeDao.loadAmsLibrary("amsnative");
        if (amsNativeDao.setupAmsLibrary(amsSettings)) {
            logger.info("Setup AMS successfully!");
        }

        Address inputAddress = new Address("Fairlawn Ave", "", "Albany", "NY", "12203");
        AddressInquiryResult res = amsNativeDao.addressInquiry(inputAddress);
        OutputUtil.printObject(res);

        if (amsNativeDao.closeAmsLibrary()) {
            logger.info("Closed AMS successfully!");
        }

        System.out.println("AMSTest completed.");
    }
}
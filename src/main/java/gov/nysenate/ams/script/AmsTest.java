package gov.nysenate.ams.script;

import gov.nysenate.ams.dao.AmsNativeDao;
import gov.nysenate.ams.model.Address;
import gov.nysenate.ams.model.AddressInquiryResult;
import gov.nysenate.ams.model.AmsSettings;
import gov.nysenate.ams.util.Application;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import tools.jackson.databind.ObjectMapper;

public class AmsTest {
    private static final Logger logger = LoggerFactory.getLogger(AmsTest.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        var app = new Application();
        if (!app.bootstrap()) {
            logger.error("Application failed to start!");
            return;
        }
        logger.info("Library Path: {}", System.getProperty("java.library.path"));

        PropertiesConfiguration config = app.getConfig();
        var amsNativeDao = new AmsNativeDao();
        AmsSettings amsSettings = AmsSettings.fromConfig(config);
        amsNativeDao.loadAmsLibrary("amsnative");
        if (amsNativeDao.setupAmsLibrary(amsSettings)) {
            logger.info("Setup AMS successfully!");
        }

        Address inputAddress = new Address("Fairlawn Ave", "", "Albany", "NY", "12203", "");
        AddressInquiryResult res = amsNativeDao.addressInquiry(inputAddress);
        String str = "";
        try {
            str = mapper.writeValueAsString(res);
        }
        catch (Exception ex){
            logger.error("Object to JSON Error: ".concat(ex.getMessage()));
        }
        System.out.println(str);

        if (amsNativeDao.closeAmsLibrary()) {
            logger.info("Closed AMS successfully!");
        }

        System.out.println("AMSTest completed.");
    }
}

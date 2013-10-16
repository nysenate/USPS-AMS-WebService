package gov.nysenate.ams.util;

import gov.nysenate.ams.dao.AmsNativeDao;
import gov.nysenate.ams.model.AmsSettings;
import gov.nysenate.util.Config;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

public class Application
{
    private static Logger logger = Logger.getLogger(Application.class);

    private static String DEFAULT_PROPERTY_FILENAME = "app.properties";
    private static String TEST_PROPERTY_FILENAME = "test.app.properties";

    private Config config;
    private AmsSettings amsSettings;
    private AmsNativeDao amsNativeDao;

    /** Singleton instance */
    private static Application INSTANCE = new Application();
    private Application() {}

    public static boolean bootstrap()
    {
        try {
            INSTANCE.config = new Config(DEFAULT_PROPERTY_FILENAME);
            INSTANCE.amsNativeDao = new AmsNativeDao();
            INSTANCE.amsSettings = new AmsSettings(INSTANCE.config);
            String libraryName = INSTANCE.config.getValue("shared.library.name", "amsnative");
            INSTANCE.amsNativeDao.loadAmsLibrary(libraryName);
            INSTANCE.amsNativeDao.setupAmsLibrary(INSTANCE.amsSettings);
            return true;
        }
        catch (ConfigurationException ex) {
            logger.error("Failed to load configuration.", ex);
        }
        return false;
    }

    public static boolean shutdown()
    {
        logger.info("Shutting down AMS application");
        if (INSTANCE.amsNativeDao.closeAmsLibrary()) {
            logger.info("Closed the AMS instance.");
            return true;
        }
        return false;
    }

    public static Config getConfig()
    {
        return INSTANCE.config;
    }
}

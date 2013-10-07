package gov.nysenate.ams.util;

import gov.nysenate.util.Config;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

public class Application
{
    private static Logger logger = Logger.getLogger(Application.class);

    private static String DEFAULT_PROPERTY_FILENAME = "app.properties";
    private static String TEST_PROPERTY_FILENAME = "test.app.properties";

    private Config config;

    /** Singleton instance */
    private static Application INSTANCE = new Application();
    private Application() {}

    public static boolean bootstrap()
    {
        try {
            INSTANCE.config = new Config(DEFAULT_PROPERTY_FILENAME);
            return true;
        }
        catch (ConfigurationException ex) {
            logger.error("Failed to load configuration.", ex);
        }
        return false;
    }

    public static Config getConfig()
    {
        return INSTANCE.config;
    }
}

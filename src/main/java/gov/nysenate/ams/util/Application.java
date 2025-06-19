package gov.nysenate.ams.util;

import gov.nysenate.ams.model.AmsSettings;
import gov.nysenate.ams.provider.AmsNativeProvider;
import gov.nysenate.util.Config;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String DEFAULT_PROPERTY_FILENAME = "app.properties";

    private Config config;
    private AmsSettings amsSettings;
    private AmsNativeProvider amsNativeProvider;

    /** Singleton instance */
    private static final Application INSTANCE = new Application();
    private Application() {}

    public static boolean bootstrap() {
        try {
            INSTANCE.config = new Config(DEFAULT_PROPERTY_FILENAME);
            INSTANCE.amsSettings = AmsSettings.fromConfig(INSTANCE.config);

            /* Setup the native AMS provider. */
            INSTANCE.amsNativeProvider = new AmsNativeProvider(INSTANCE.config, INSTANCE.amsSettings);
            INSTANCE.amsNativeProvider.load();
            INSTANCE.amsNativeProvider.setup();

            return true;
        }
        catch (ConfigurationException ex) {
            logger.error("Failed to load configuration.", ex);
        }
        return false;
    }

    @SuppressWarnings("unused")
    // This method is somehow called during shutdown.
    public static boolean shutdown() {
        logger.info("Shutting down AMS application");
        if (INSTANCE.amsNativeProvider != null && INSTANCE.amsNativeProvider.shutDown()) {
            logger.info("Closed the AMS instance.");
            return true;
        }
        return false;
    }

    public static Config getConfig() {
        return INSTANCE.config;
    }

    public static AmsNativeProvider getAmsNativeProvider() {
        return INSTANCE.amsNativeProvider;
    }
}

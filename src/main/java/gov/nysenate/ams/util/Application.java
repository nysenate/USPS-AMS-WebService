package gov.nysenate.ams.util;

import gov.nysenate.ams.model.AmsSettings;
import gov.nysenate.ams.provider.AmsNativeProvider;
import gov.nysenate.util.Config;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public final class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String DEFAULT_PROPERTY_FILENAME = "app.properties";

    private static Config config;
    private static AmsNativeProvider amsNativeProvider;

    private Application() {}

    public static boolean bootstrap() {
        try {
            config = new Config(DEFAULT_PROPERTY_FILENAME);
            AmsSettings amsSettings = AmsSettings.fromConfig(config);
            if (!amsSettings.pathsSet()) {
                logger.error("Did not pull in all file paths from {}!", DEFAULT_PROPERTY_FILENAME);
                return false;
            }

            String libraryName = config.getValue("shared.library.name", "amsnative");
            /* Set up the native AMS provider. */
            amsNativeProvider = new AmsNativeProvider(libraryName, amsSettings);
            return amsNativeProvider.load();
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
        if (amsNativeProvider != null && amsNativeProvider.shutDown()) {
            logger.info("Closed the AMS instance.");
            return true;
        }
        return false;
    }

    public static Config getConfig() {
        return config;
    }

    public static AmsNativeProvider getAmsNativeProvider() {
        return amsNativeProvider;
    }
}

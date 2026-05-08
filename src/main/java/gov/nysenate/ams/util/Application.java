package gov.nysenate.ams.util;

import gov.nysenate.ams.model.AmsSettings;
import gov.nysenate.ams.provider.AmsNativeProvider;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public final class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String DEFAULT_PROPERTY_FILENAME = "app.properties";

    private static PropertiesConfiguration config;
    private static AmsNativeProvider amsNativeProvider;

    private Application() {}

    public static boolean bootstrap() {
        try {
            config = new Configurations().properties(DEFAULT_PROPERTY_FILENAME);
        } catch (ConfigurationException e) {
            logger.error("Failed to pull in properties!");
            return false;
        }
        AmsSettings amsSettings = AmsSettings.fromConfig(config);
        if (!amsSettings.pathsSet()) {
            logger.error("Did not pull in all file paths from {}!", DEFAULT_PROPERTY_FILENAME);
            return false;
        }

        String libraryName = config.getString("shared.library.name", "amsnative");
        /* Set up the native AMS provider. */
        amsNativeProvider = new AmsNativeProvider(libraryName, amsSettings);
        return amsNativeProvider.load();
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

    public static PropertiesConfiguration getConfig() {
        return config;
    }

    public static AmsNativeProvider getAmsNativeProvider() {
        return amsNativeProvider;
    }
}

package gov.nysenate.ams.util;

import gov.nysenate.ams.model.AmsSettings;
import gov.nysenate.ams.provider.AmsNativeProvider;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@WebListener
public final class Application implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String DEFAULT_PROPERTY_FILENAME = "app.properties";

    public static final String CONTEXT_ATTRIBUTE = "ams";

    private PropertiesConfiguration config;
    private AmsNativeProvider amsNativeProvider;

    public Application() {}

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        boolean ok = bootstrap();
        sce.getServletContext().setAttribute("init", ok);
        sce.getServletContext().setAttribute(CONTEXT_ATTRIBUTE, this);
        logger.info("AMS application bootstrap: {}", ok);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutting down AMS application");
        if (amsNativeProvider != null && amsNativeProvider.shutDown()) {
            logger.info("Closed the AMS instance.");
        }
    }

    public boolean bootstrap() {
        try {
            this.config = new Configurations().properties(DEFAULT_PROPERTY_FILENAME);
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
        this.amsNativeProvider = new AmsNativeProvider(libraryName, amsSettings);
        return amsNativeProvider.load();
    }

    public PropertiesConfiguration getConfig() {
        return config;
    }

    public AmsNativeProvider getAmsNativeProvider() {
        return amsNativeProvider;
    }
}

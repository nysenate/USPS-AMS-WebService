package gov.nysenate.ams.model;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;

public record AmsSettings(String systemPath, String address1Path, String addrIndexPath, String cityStatePath,
                          String crossRefPath, String elotPath, String elotIndexPath, String lacsLinkPath,
                          String dpvPath, String fnsPath, String suiteLinkPath, String abrstPath,
                          boolean elotEnabled, boolean ewsEnabled, boolean suiteLinkEnabled,
                          boolean abrstEnabled, boolean systemEnabled) {

    public static AmsSettings fromConfig(PropertiesConfiguration config) {
        String systemPath = config.getString("ams.cfg.system.path");
        String address1Path = config.getString("ams.cfg.address1.path");
        String addrIndexPath = config.getString("ams.cfg.addrIndex.path");
        String cityStatePath = config.getString("ams.cfg.cityState.path");
        String crossRefPath = config.getString("ams.cfg.crossRef.path");
        String elotPath = config.getString("ams.cfg.elot.path");
        String elotIndexPath = config.getString("ams.cfg.elotIndex.path");
        String lacsLinkPath = config.getString("ams.cfg.lacsLink.path");
        String dpvPath = config.getString("ams.cfg.dpv.path");
        String fnsPath = config.getString("ams.cfg.fns.path");
        String suiteLinkPath = config.getString("ams.cfg.suiteLink.path");
        String abrstPath = config.getString("ams.cfg.abrst.path");

        boolean elotEnabled = getFlag(config, "ams.cfg.elot.enabled");
        boolean ewsEnabled = getFlag(config, "ams.cfg.ews.enabled");
        boolean suiteLinkEnabled = getFlag(config, "ams.cfg.suiteLink.enabled");
        boolean abrstEnabled = getFlag(config, "ams.cfg.abrst.enabled");
        boolean systemEnabled = getFlag(config, "ams.cfg.system.enabled");
        return new AmsSettings(systemPath, address1Path, addrIndexPath, cityStatePath, crossRefPath, elotPath,
                elotIndexPath, lacsLinkPath, dpvPath, fnsPath, suiteLinkPath, abrstPath,
                elotEnabled, ewsEnabled, suiteLinkEnabled, abrstEnabled, systemEnabled);
    }

    private static boolean getFlag(PropertiesConfiguration config, String field) {
        return Boolean.parseBoolean(config.getString(field));
    }

    public boolean pathsSet() {
        return StringUtils.isNoneBlank(systemPath, address1Path, addrIndexPath, cityStatePath, crossRefPath, elotPath, elotIndexPath,
                lacsLinkPath, dpvPath, fnsPath, suiteLinkPath, abrstPath);
    }
}

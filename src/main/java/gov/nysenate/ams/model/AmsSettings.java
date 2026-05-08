package gov.nysenate.ams.model;

import gov.nysenate.util.Config;
import org.apache.commons.lang3.StringUtils;

public record AmsSettings(String systemPath, String address1Path, String addrIndexPath, String cityStatePath,
                          String crossRefPath, String elotPath, String elotIndexPath, String lacsLinkPath,
                          String dpvPath, String fnsPath, String suiteLinkPath, String abrstPath,
                          boolean elotEnabled, boolean ewsEnabled, boolean suiteLinkEnabled,
                          boolean abrstEnabled, boolean systemEnabled) {

    public static AmsSettings fromConfig(Config config) {
        String systemPath = config.getValue("ams.cfg.system.path");
        String address1Path = config.getValue("ams.cfg.address1.path");
        String addrIndexPath = config.getValue("ams.cfg.addrIndex.path");
        String cityStatePath = config.getValue("ams.cfg.cityState.path");
        String crossRefPath = config.getValue("ams.cfg.crossRef.path");
        String elotPath = config.getValue("ams.cfg.elot.path");
        String elotIndexPath = config.getValue("ams.cfg.elotIndex.path");
        String lacsLinkPath = config.getValue("ams.cfg.lacsLink.path");
        String dpvPath = config.getValue("ams.cfg.dpv.path");
        String fnsPath = config.getValue("ams.cfg.fns.path");
        String suiteLinkPath = config.getValue("ams.cfg.suiteLink.path");
        String abrstPath = config.getValue("ams.cfg.abrst.path");

        boolean elotEnabled = getFlag(config, "ams.cfg.elot.enabled");
        boolean ewsEnabled = getFlag(config, "ams.cfg.ews.enabled");
        boolean suiteLinkEnabled = getFlag(config, "ams.cfg.suiteLink.enabled");
        boolean abrstEnabled = getFlag(config, "ams.cfg.abrst.enabled");
        boolean systemEnabled = getFlag(config, "ams.cfg.system.enabled");
        return new AmsSettings(systemPath, address1Path, addrIndexPath, cityStatePath, crossRefPath, elotPath,
                elotIndexPath, lacsLinkPath, dpvPath, fnsPath, suiteLinkPath, abrstPath,
                elotEnabled, ewsEnabled, suiteLinkEnabled, abrstEnabled, systemEnabled);
    }

    private static boolean getFlag(Config config, String field) {
        return Boolean.parseBoolean(config.getValue(field));
    }

    public boolean pathsSet() {
        return StringUtils.isNoneBlank(systemPath, address1Path, addrIndexPath, cityStatePath, crossRefPath, elotPath, elotIndexPath,
                lacsLinkPath, dpvPath, fnsPath, suiteLinkPath, abrstPath);
    }
}

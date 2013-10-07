package gov.nysenate.ams.model;

import gov.nysenate.util.Config;

public class AmsSettings
{
    /** Data Paths */
    private final String systemPath;
    private final String address1Path;
    private final String addrIndexPath;
    private final String cityStatePath;
    private final String crossRefPath;
    private final String elotPath;
    private final String elotIndexPath;
    private final String lacslinkPath;
    private final String dpvPath;
    private final String fnsPath;
    private final String suitelinkPath;
    private final String abrstPath;

    /** Data Flags */
    private final boolean elotEnabled;
    private final boolean ewsEnabled;
    private final boolean dpvEnabled;
    private final boolean lacslinkEnabled;
    private final boolean suitelinkEnabled;
    private final boolean abrstEnabled;
    private final boolean systemEnabled;
    
    public AmsSettings(Config config)
    {
        this.systemPath = config.getValue("ams.cfg.system.path");
        this.address1Path = config.getValue("ams.cfg.address1.path");
        this.addrIndexPath = config.getValue("ams.cfg.addrindex.path");
        this.cityStatePath = config.getValue("ams.cfg.citystate.path");
        this.crossRefPath = config.getValue("ams.cfg.crossref.path");
        this.elotPath = config.getValue("ams.cfg.elot.path");
        this.elotIndexPath = config.getValue("ams.cfg.elotindex.path");
        this.lacslinkPath = config.getValue("ams.cfg.lacslink.path");
        this.dpvPath = config.getValue("ams.cfg.dpv.path");
        this.fnsPath = config.getValue("ams.cfg.fns.path");
        this.suitelinkPath = config.getValue("ams.cfg.suitelink.path");
        this.abrstPath = config.getValue("ams.cfg.abrst.path");

        this.elotEnabled = Boolean.parseBoolean(config.getValue("ams.cfg.elot.enabled"));
        this.ewsEnabled = Boolean.parseBoolean(config.getValue("ams.cfg.ews.enabled"));
        this.dpvEnabled = Boolean.parseBoolean(config.getValue("ams.cfg.dpv.enabled"));
        this.lacslinkEnabled = Boolean.parseBoolean(config.getValue("ams.cfg.lacslink.enabled"));
        this.suitelinkEnabled = Boolean.parseBoolean(config.getValue("ams.cfg.suitelink.enabled"));
        this.abrstEnabled = Boolean.parseBoolean(config.getValue("ams.cfg.abrst.enabled"));
        this.systemEnabled = Boolean.parseBoolean(config.getValue("ams.cfg.system.enabled"));
    }

    public String getSystemPath() {
        return systemPath;
    }

    public String getAddress1Path() {
        return address1Path;
    }

    public String getAddrIndexPath() {
        return addrIndexPath;
    }

    public String getCityStatePath() {
        return cityStatePath;
    }

    public String getCrossRefPath() {
        return crossRefPath;
    }

    public String getElotPath() {
        return elotPath;
    }

    public String getElotIndexPath() {
        return elotIndexPath;
    }

    public String getLacslinkPath() {
        return lacslinkPath;
    }

    public String getDpvPath() {
        return dpvPath;
    }

    public String getFnsPath() {
        return fnsPath;
    }

    public String getSuitelinkPath() {
        return suitelinkPath;
    }

    public String getAbrstPath() {
        return abrstPath;
    }

    public boolean isElotEnabled() {
        return elotEnabled;
    }

    public boolean isEwsEnabled() {
        return ewsEnabled;
    }

    public boolean isDpvEnabled() {
        return dpvEnabled;
    }

    public boolean isLacslinkEnabled() {
        return lacslinkEnabled;
    }

    public boolean isSuitelinkEnabled() {
        return suitelinkEnabled;
    }

    public boolean isAbrstEnabled() {
        return abrstEnabled;
    }

    public boolean isSystemEnabled() {
        return systemEnabled;
    }
}
package gov.nysenate.ams.model;

/**
 * Represents an address by its parsed components.
 */
public class ParsedAddress
{
    protected int primaryNum;
    protected int secondaryNum;
    protected int ruralRouteNum;
    protected String secondaryUnit;
    protected String firstPreDir;
    protected String secondPreDir;
    protected String firstSuffix;
    protected String secondSuffix;
    protected String firstPostDir;
    protected String secondPostDir;
    protected String primaryName;

    public ParsedAddress(String primaryNum, String secondaryNum, String ruralRouteNum, String secondaryUnit,
                         String firstPreDir, String secondPreDir, String firstSuffix, String secondSuffix,
                         String firstPostDir, String secondPostDir, String primaryName)
    {
        //this.primaryNum = primaryNum;
        //this.secondaryNum = secondaryNum;
        //this.ruralRouteNum = ruralRouteNum;
        this.secondaryUnit = secondaryUnit;
        this.firstPreDir = firstPreDir;
        this.secondPreDir = secondPreDir;
        this.firstSuffix = firstSuffix;
        this.secondSuffix = secondSuffix;
        this.firstPostDir = firstPostDir;
        this.secondPostDir = secondPostDir;
        this.primaryName = primaryName;
    }

    public int getPrimaryNum() {
        return primaryNum;
    }

    public int getSecondaryNum() {
        return secondaryNum;
    }

    public int getRuralRouteNum() {
        return ruralRouteNum;
    }

    public String getSecondaryUnit() {
        return secondaryUnit;
    }

    public String getFirstPreDir() {
        return firstPreDir;
    }

    public String getSecondPreDir() {
        return secondPreDir;
    }

    public String getFirstSuffix() {
        return firstSuffix;
    }

    public String getSecondSuffix() {
        return secondSuffix;
    }

    public String getFirstPostDir() {
        return firstPostDir;
    }

    public String getSecondPostDir() {
        return secondPostDir;
    }

    public String getPrimaryName() {
        return primaryName;
    }
}
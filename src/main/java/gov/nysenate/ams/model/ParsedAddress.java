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
    protected int suffix;
    protected String postDir;

    public ParsedAddress(int primaryNum, int secondaryNum, int ruralRouteNum, String secondaryUnit,
                         String firstPreDir, String secondPreDir, String firstSuffix, String secondSuffix,
                         String firstPostDir, String secondPostDir, String primaryName, int suffix, String postDir)
    {
        this.primaryNum = primaryNum;
        this.secondaryNum = secondaryNum;
        this.ruralRouteNum = ruralRouteNum;
        this.secondaryUnit = secondaryUnit;
        this.firstPreDir = firstPreDir;
        this.secondPreDir = secondPreDir;
        this.firstSuffix = firstSuffix;
        this.secondSuffix = secondSuffix;
        this.firstPostDir = firstPostDir;
        this.secondPostDir = secondPostDir;
        this.primaryName = primaryName;
        this.suffix = suffix;
        this.postDir = postDir;
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

    public int getSuffix() {
        return suffix;
    }

    public String getPostDir() {
        return postDir;
    }
}
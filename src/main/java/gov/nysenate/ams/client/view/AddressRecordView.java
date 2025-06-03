package gov.nysenate.ams.client.view;

import gov.nysenate.ams.model.AddressRecord;
import gov.nysenate.ams.model.RecordType;
import gov.nysenate.ams.util.OutputUtil;

public class AddressRecordView
{
    protected int recordId;
    protected String recordType;
    protected String recordTypeDesc;
    protected String primaryLow;
    protected String primaryHigh;
    protected String primaryParity;
    protected String preDir;
    protected String streetName;
    protected String streetSuffix;
    protected String postDir;
    protected String unit;
    protected String secondaryLow;
    protected String secondaryHigh;
    protected String secondaryParity;
    protected String zip5;
    protected String zip4Low;
    protected String zip4High;
    protected String financeCode;
    protected String fipsCounty;

    public AddressRecordView(AddressRecord addressRecord)
    {
        if (addressRecord != null) {
            this.recordId = addressRecord.recordID();
            RecordType rType =  addressRecord.recordType();
            if (rType != null) {
                this.recordType = rType.name();
                this.recordTypeDesc = rType.getShortDesc();
            }
            this.primaryLow = OutputUtil.trimLeadingZeroes(addressRecord.primaryLow());
            this.primaryHigh = OutputUtil.trimLeadingZeroes(addressRecord.primaryHigh());
            this.primaryParity = Character.toString(addressRecord.primaryParity());
            this.preDir = addressRecord.preDir();
            this.streetName = addressRecord.streetName();
            this.streetSuffix = addressRecord.suffix();
            this.postDir = addressRecord.postDir();
            this.unit = addressRecord.unit();
            this.secondaryLow = OutputUtil.trimLeadingZeroes(addressRecord.secLow());
            this.secondaryHigh = OutputUtil.trimLeadingZeroes(addressRecord.secHigh());
            this.secondaryParity = Character.toString(addressRecord.secCode());
            this.zip5 = addressRecord.zip();
            this.zip4Low = addressRecord.addonLow();
            this.zip4High = addressRecord.addonHigh();
            this.financeCode = addressRecord.financeCode();
            this.fipsCounty = addressRecord.countyNum();
        }
    }

    public int getRecordId() {
        return recordId;
    }

    public String getRecordType() {
        return recordType;
    }

    public String getRecordTypeDesc() {
        return recordTypeDesc;
    }

    public String getPrimaryLow() {
        return primaryLow;
    }

    public String getPrimaryHigh() {
        return primaryHigh;
    }

    public String getPrimaryParity() {
        return primaryParity;
    }

    public String getPreDir() {
        return preDir;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetSuffix() {
        return streetSuffix;
    }

    public String getPostDir() {
        return postDir;
    }

    public String getUnit() {
        return unit;
    }

    public String getSecondaryLow() {
        return secondaryLow;
    }

    public String getSecondaryHigh() {
        return secondaryHigh;
    }

    public String getSecondaryParity() {
        return secondaryParity;
    }

    public String getZip5() {
        return zip5;
    }

    public String getZip4Low() {
        return zip4Low;
    }

    public String getZip4High() {
        return zip4High;
    }

    public String getFinanceCode() {
        return financeCode;
    }

    public String getFipsCounty() {
        return fipsCounty;
    }
}
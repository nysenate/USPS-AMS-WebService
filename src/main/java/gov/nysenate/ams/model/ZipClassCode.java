package gov.nysenate.ams.model;

public enum ZipClassCode
{
    M ("APO/FPO Military Zip5"),
    P ("PO BOX Zip5"),
    U ("Unique Zip5"),
    B ("Non-Unique Zip5");

    private String desc;

    ZipClassCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static ZipClassCode getValue(String p)
    {
        if( p != null && !p.isEmpty() && !p.equals(" ") ) {
            try {
                return ZipClassCode.valueOf(p.toUpperCase());
            }
            catch (IllegalArgumentException ex) {
                return null;
            }
        }
        else return ZipClassCode.B;
    }

}

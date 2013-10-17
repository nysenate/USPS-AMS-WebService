package gov.nysenate.ams.model;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 10/17/13
 * Time: 11:27 AM
 */
public enum ZipClassCode
{
    M ("APO/FPO military zip5"),
    P ("PO BOX zip5"),
    U ("Unique zip5"),
    B ("Non-unique Zip");

    private String desc;

    ZipClassCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static ZipClassCode getValue (String p)
    {

        if(p != null && !p.isEmpty() && !p.equals(" "))
        {    System.out.println(p.length());
          try{return ZipClassCode.valueOf(p.toUpperCase());}

        catch (IllegalArgumentException ex)
            {
             return null;
             }
        }

        else return ZipClassCode.B;
    }

}

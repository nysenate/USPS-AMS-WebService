package gov.nysenate.ams.model;

public enum FacilityCode
{
    A ("Airport mail facility"),
    B ("Branch"),
    C ("Community post office"),
    D ("Area distrib. center"),
    E ("Sect. center facility"),
    F ("General distrib. center"),
    G ("General mail facility"),
    K ("Bulk mail center"),
    M ("Money order unit"),
    N ("Non-postal name, community name,former postal facility,or place name"),
    P ("Post office"),
    S ("Station"),
    U ("Urbanization");

    private String desc;

    FacilityCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}

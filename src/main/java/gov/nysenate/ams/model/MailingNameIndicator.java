package gov.nysenate.ams.model;

public enum MailingNameIndicator {
    Y("Mailing name"), N("Non-mailing name");

    private final String desc;

    MailingNameIndicator(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}

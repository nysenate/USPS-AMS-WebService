package gov.nysenate.ams.provider;

import gov.nysenate.ams.service.AddressService;
import gov.nysenate.ams.service.LicensingService;

public class AMS implements LicensingService, AddressService {


    @Override
    public String getApiVersion() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getDataExpireDays() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getLibraryExpireDays() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

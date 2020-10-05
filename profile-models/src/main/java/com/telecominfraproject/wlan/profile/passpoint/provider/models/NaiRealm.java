package com.telecominfraproject.wlan.profile.passpoint.provider.models;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class NaiRealm extends BaseJsonModel implements PushableConfiguration<NaiRealm> {

    /**
     * 
     */
    private static final long serialVersionUID = -6102005274671968193L;

    @Override
    public boolean needsToBeUpdatedOnDevice(NaiRealm previousVersion) {
        // TODO Auto-generated method stub
        return false;
    }


}

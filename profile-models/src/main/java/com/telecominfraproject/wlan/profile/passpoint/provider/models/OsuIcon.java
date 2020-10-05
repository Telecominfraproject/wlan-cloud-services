package com.telecominfraproject.wlan.profile.passpoint.provider.models;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class OsuIcon extends BaseJsonModel implements PushableConfiguration<OsuIcon>{

    private static final long serialVersionUID = 6779033020793748979L;

    @Override
    public boolean needsToBeUpdatedOnDevice(OsuIcon previousVersion) {
        // TODO Auto-generated method stub
        return false;
    }

}

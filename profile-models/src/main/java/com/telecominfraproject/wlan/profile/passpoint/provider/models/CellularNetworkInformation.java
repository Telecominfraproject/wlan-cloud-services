package com.telecominfraproject.wlan.profile.passpoint.provider.models;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class CellularNetworkInformation extends BaseJsonModel
        implements PushableConfiguration<CellularNetworkInformation> {

    private static final long serialVersionUID = -7725583150308531978L;

    @Override
    public boolean needsToBeUpdatedOnDevice(CellularNetworkInformation previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

}

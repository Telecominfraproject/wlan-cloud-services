package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.List;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public abstract class NeighbourEquipmentInfo extends BaseJsonModel {
    private static final long serialVersionUID = -3900688076906788530L;

    public abstract List<NeighbourRadioInfo> getRadios();
}

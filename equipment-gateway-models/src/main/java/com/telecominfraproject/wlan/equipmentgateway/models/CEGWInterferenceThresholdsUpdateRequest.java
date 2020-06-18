package com.telecominfraproject.wlan.equipmentgateway.models;

import com.telecominfraproject.wlan.core.model.equipment.ChannelHopSettings;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;

/**
 *
 * @author erikvilleneuve
 *
 */
public class CEGWInterferenceThresholdsUpdateRequest extends EquipmentCommand {

    /**
     *
     */
    private static final long serialVersionUID = 5489845866667403625L;
    private RadioType radioType;
    private ChannelHopSettings hopSettings;

    protected CEGWInterferenceThresholdsUpdateRequest() {
        // serial
    }

    public CEGWInterferenceThresholdsUpdateRequest(String qrCode, long equipmentId, RadioType radioType,
            ChannelHopSettings settings) {
        super(CEGWCommandType.InterferenceThresholdUpdateRequest, qrCode, equipmentId);
        this.radioType = radioType;
        this.hopSettings = settings;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public ChannelHopSettings getHopSettings() {
        return hopSettings;
    }

    public void setHopSettings(ChannelHopSettings hopSettings) {
        this.hopSettings = hopSettings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hopSettings == null) ? 0 : hopSettings.hashCode());
        result = prime * result + ((radioType == null) ? 0 : radioType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CEGWInterferenceThresholdsUpdateRequest other = (CEGWInterferenceThresholdsUpdateRequest) obj;
        if (hopSettings == null) {
            if (other.hopSettings != null) {
                return false;
            }
        } else if (!hopSettings.equals(other.hopSettings)) {
            return false;
        }
        if (radioType != other.radioType) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (RadioType.isUnsupported(radioType) || hasUnsupportedValue(hopSettings)) {
            return true;
        }
        return false;
    }

}

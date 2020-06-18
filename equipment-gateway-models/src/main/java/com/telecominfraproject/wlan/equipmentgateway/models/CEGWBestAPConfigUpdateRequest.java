package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.List;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioBestApSettingsPerRadio;

/**
 *
 * @author erikvilleneuve
 *
 */
public class CEGWBestAPConfigUpdateRequest extends EquipmentCommand {

    /**
     *
     */
    private static final long serialVersionUID = 5644302132642843913L;
    private List<RadioBestApSettingsPerRadio> bestApConfigs;

    protected CEGWBestAPConfigUpdateRequest() {
        // serial
    }

    public CEGWBestAPConfigUpdateRequest(String qrCode, long equipmentId,
            List<RadioBestApSettingsPerRadio> bestApConfigs) {
        super(CEGWCommandType.BestApConfigurationUpdateRequest, qrCode, equipmentId);
        this.bestApConfigs = bestApConfigs;
    }

    public List<RadioBestApSettingsPerRadio> getBestApConfigs() {
        return bestApConfigs;
    }

    public void setBestApConfigs(List<RadioBestApSettingsPerRadio> bestApConfigs) {
        this.bestApConfigs = bestApConfigs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(bestApConfigs);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof CEGWBestAPConfigUpdateRequest)) {
            return false;
        }
        CEGWBestAPConfigUpdateRequest other = (CEGWBestAPConfigUpdateRequest) obj;
        return Objects.equals(bestApConfigs, other.bestApConfigs);
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(bestApConfigs)) {
            return true;
        }
        return false;
    }

}

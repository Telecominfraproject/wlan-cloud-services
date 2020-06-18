package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.List;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacToMonitor;

/**
 *
 * @author erikvilleneuve
 *
 */
public class CEGWBSSIDtoMACMonitoringRequest extends EquipmentCommand {

    /**
     *
     */
    private static final long serialVersionUID = 5155639063452463359L;
    private List<MacToMonitor> macsToMonitor;

    protected CEGWBSSIDtoMACMonitoringRequest() {
        // serial
    }

    public CEGWBSSIDtoMACMonitoringRequest(String qrCode, long equipmentId, List<MacToMonitor> macsToMonitor) {
        super(CEGWCommandType.BSSIDToMacMonitoringRequest, qrCode, equipmentId);
        this.macsToMonitor = macsToMonitor;
    }

    public List<MacToMonitor> getMacsToMonitor() {
        return macsToMonitor;
    }

    public void setMacsToMonitor(List<MacToMonitor> macsToMonitor) {
        this.macsToMonitor = macsToMonitor;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(macsToMonitor)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(macsToMonitor);
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
        if (!(obj instanceof CEGWBSSIDtoMACMonitoringRequest)) {
            return false;
        }
        CEGWBSSIDtoMACMonitoringRequest other = (CEGWBSSIDtoMACMonitoringRequest) obj;
        return Objects.equals(macsToMonitor, other.macsToMonitor);
    }

}

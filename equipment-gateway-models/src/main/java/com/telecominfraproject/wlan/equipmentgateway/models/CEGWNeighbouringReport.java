package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.List;
import java.util.Objects;

import com.telecominfraproject.wlan.status.equipment.models.RoamingCandidate;

public class CEGWNeighbouringReport extends EquipmentCommand {
    /**
     *
     */
    private static final long serialVersionUID = 4637333255326356948L;
    /**
     *
     */
    private List<RoamingCandidate> acNeighbours;
    private List<RoamingCandidate> bgNeighbours;

    public CEGWNeighbouringReport(String equipmentQrCode, long equipmentId) {
        super(CEGWCommandType.NeighbourhoodReport, equipmentQrCode, equipmentId);
    }

    protected CEGWNeighbouringReport() {
        super(CEGWCommandType.NeighbourhoodReport, null, 0);
    }

    public List<RoamingCandidate> getAcNeighbours() {
        return acNeighbours;
    }

    public void setAcNeighbours(List<RoamingCandidate> acNeighbours) {
        this.acNeighbours = acNeighbours;
    }

    public List<RoamingCandidate> getBgNeighbours() {
        return bgNeighbours;
    }

    public void setBgNeighbours(List<RoamingCandidate> bgNeighbours) {
        this.bgNeighbours = bgNeighbours;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(bgNeighbours) || hasUnsupportedValue(acNeighbours)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(acNeighbours, bgNeighbours);
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
        if (!(obj instanceof CEGWNeighbouringReport)) {
            return false;
        }
        CEGWNeighbouringReport other = (CEGWNeighbouringReport) obj;
        return Objects.equals(acNeighbours, other.acNeighbours) && Objects.equals(bgNeighbours, other.bgNeighbours);
    }

}

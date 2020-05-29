package com.telecominfraproject.wlan.servicemetric.neighbourscan.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.NeighboreScanPacketType;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;

/**
 * @author ekeddy
 *
 */
public class NeighbourScanReports extends ServiceMetricDetails {
    private static final long serialVersionUID = 6091333623390501053L;

    public static final String TYPE_NAME = NeighbourScanReports.class.getSimpleName();

    private List<NeighbourReport> neighbourReports;

    @Override
    public ServiceMetricDataType getDataType() {
    	return ServiceMetricDataType.Neighbour;
    }
    
    @Override
    public NeighbourScanReports clone() {
        NeighbourScanReports ret = (NeighbourScanReports) super.clone();
        if (this.neighbourReports != null) {
            ret.neighbourReports = new ArrayList<>();
            for (NeighbourReport p : this.neighbourReports) {
                ret.neighbourReports.add(p.clone());
            }
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NeighbourScanReports)) {
            return false;
        }
        NeighbourScanReports other = (NeighbourScanReports) obj;
        if (neighbourReports == null) {
            if (other.neighbourReports != null) {
                return false;
            }
        } else if (!neighbourReports.equals(other.neighbourReports)) {
            return false;
        }
        return true;
    }

    public List<NeighbourReport> getNeighbourReports() {
        return neighbourReports;
    }

    @JsonIgnore
    public long getNeighbourReportCount() {
        return (neighbourReports == null) ? 0 : neighbourReports.size();
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((neighbourReports == null) ? 0 : neighbourReports.hashCode());
        return result;
    }

    public void setNeighbourReports(List<NeighbourReport> reports) {
        this.neighbourReports = reports;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(neighbourReports)) {
            return true;
        }
        return false;
    }
    
    
    @JsonIgnore
    public boolean containsBeaconsOnRadio(RadioType radio)
    {
        if(this.neighbourReports != null)
        {
            return this.neighbourReports.stream().anyMatch(c -> c.getPacketType() == NeighboreScanPacketType.BEACON && c.getRadioType() == radio);
        }
        
        return false;   
    }

}

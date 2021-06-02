/**
 * 
 */
package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasSourceTimestamp;

/**
 * @author ekeddy
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class RadioUtilization extends BaseJsonModel implements HasSourceTimestamp {
    
	private static final long serialVersionUID = -6295155025058221865L;

    private Integer assocClientTx;
    private Integer unassocClientTx;
    private Integer assocClientRx;
    private Integer unassocClientRx;
    private Integer nonWifi;
    private Integer timestampSeconds;
    private long sourceTimestampMs;
    //
    // If the two value below are null and the value above aren't, we'll 
    // calculate them on the spot. 
    //
    private Double ibss;
    private Double unAvailableCapacity;

    public Integer getAssocClientTx() {
        return assocClientTx;
    }

    public void setAssocClientTx(Integer assocClientTx) {
        this.assocClientTx = assocClientTx;
    }

    public Integer getUnassocClientTx() {
        return unassocClientTx;
    }

    public void setUnassocClientTx(Integer unassocClientTx) {
        this.unassocClientTx = unassocClientTx;
    }

    public Integer getAssocClientRx() {
        return assocClientRx;
    }

    public void setAssocClientRx(Integer assocClientRx) {
        this.assocClientRx = assocClientRx;
    }

    public Integer getUnassocClientRx() {
        return unassocClientRx;
    }

    public void setUnassocClientRx(Integer unassocClientRx) {
        this.unassocClientRx = unassocClientRx;
    }

    public Integer getNonWifi() {
        return nonWifi;
    }

    public void setNonWifi(Integer nonWifi) {
        this.nonWifi = nonWifi;
    }

    /**
     * @return the timestampSeconds
     */
    public Integer getTimestampSeconds() {
        return timestampSeconds;
    }

    /**
     * @param timestampSeconds the timestampSeconds to set
     */
    public void setTimestampSeconds(Integer timestampSeconds) {
        this.timestampSeconds = timestampSeconds;
    }

    public Double getIbss() 
    {
        if(ibss == null && assocClientRx != null && assocClientTx != null)
        {
            return (double) assocClientRx + assocClientTx;
        }
        
        return ibss;
    }

    public void setIbss(Double ibss) {
        this.ibss = ibss;
    }

    public Double getUnAvailableCapacity() 
    {
        if(unAvailableCapacity == null && unassocClientRx != null && unassocClientTx != null && nonWifi != null)
        {
            return (double) unassocClientRx + unassocClientTx + nonWifi;
        }
        
        return unAvailableCapacity;
    }

    public void setUnAvailableCapacity(Double unAvailableCapacity) {
        this.unAvailableCapacity = unAvailableCapacity;
    }

   

    @Override
    public int hashCode() {
        return Objects.hash(assocClientRx, assocClientTx, ibss, nonWifi, sourceTimestampMs, timestampSeconds, unAvailableCapacity, unassocClientRx,
                unassocClientTx);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RadioUtilization other = (RadioUtilization) obj;
        return Objects.equals(assocClientRx, other.assocClientRx) && Objects.equals(assocClientTx, other.assocClientTx) && Objects.equals(ibss, other.ibss)
                && Objects.equals(nonWifi, other.nonWifi) && sourceTimestampMs == other.sourceTimestampMs
                && Objects.equals(timestampSeconds, other.timestampSeconds) && Objects.equals(unAvailableCapacity, other.unAvailableCapacity)
                && Objects.equals(unassocClientRx, other.unassocClientRx) && Objects.equals(unassocClientTx, other.unassocClientTx);
    }

    @Override
    public RadioUtilization clone() {
        return (RadioUtilization) super.clone();
    }

    public void setSourceTimestampMs(long sourceTimestamp) {
        this.sourceTimestampMs = sourceTimestamp;
    }
    
    @Override
    public long getSourceTimestampMs() {
        return this.sourceTimestampMs;
    }
}

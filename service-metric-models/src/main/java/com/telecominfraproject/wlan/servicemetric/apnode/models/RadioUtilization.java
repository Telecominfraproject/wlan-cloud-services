/**
 * 
 */
package com.telecominfraproject.wlan.servicemetric.apnode.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class RadioUtilization extends BaseJsonModel {
    
	private static final long serialVersionUID = -6295155025058221865L;

    private Integer assocClientTx;
    private Integer unassocClientTx;
    private Integer assocClientRx;
    private Integer unassocClientRx;
    private Integer nonWifi;
    private Integer timestampSeconds;
    
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
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((assocClientTx == null) ? 0 : assocClientTx.hashCode());
        result = prime * result
                + ((unassocClientTx == null) ? 0 : unassocClientTx.hashCode());
        result = prime * result
                + ((assocClientRx == null) ? 0 : assocClientRx.hashCode());
        result = prime * result
                + ((unassocClientRx == null) ? 0 : unassocClientRx.hashCode());
        result = prime * result
                + ((nonWifi == null) ? 0 : nonWifi.hashCode());
        result = prime * result
                + ((timestampSeconds == null) ? 0 : timestampSeconds.hashCode());
        result = prime * result 
                + ((ibss == null) ? 0 : ibss.hashCode());
        result = prime * result
                + ((unAvailableCapacity == null) ? 0 : unAvailableCapacity.hashCode());

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

        RadioUtilization other = (RadioUtilization) obj;
        if (assocClientTx == null) {
            if (other.assocClientTx != null) {
                return false;
            }
        } else if (!assocClientTx.equals(other.assocClientTx)) {
            return false;
        }
        if (unassocClientTx == null) {
            if (other.unassocClientTx != null) {
                return false;
            }
        } else if (!unassocClientTx.equals(other.unassocClientTx)) {
            return false;
        }
        if (assocClientRx == null) {
            if (other.assocClientRx != null) {
                return false;
            }
        } else if (!assocClientRx.equals(other.assocClientRx)) {
            return false;
        }
        if (unassocClientRx == null) {
            if (other.unassocClientRx != null) {
                return false;
            }
        } else if (!unassocClientRx.equals(other.unassocClientRx)) {
            return false;
        }
        if (nonWifi == null) {
            if (other.nonWifi != null) {
                return false;
            }
        } else if (!nonWifi.equals(other.nonWifi)) {
            return false;
        }
        if (timestampSeconds == null) {
            if (other.timestampSeconds != null) {
                return false;
            }
        } else if (!timestampSeconds.equals(other.timestampSeconds)) {
            return false;
        }
        if (ibss == null) {
            if (other.ibss != null) {
                return false;
            }
        } else if (!ibss.equals(other.ibss)) {
            return false;
        }
        if (unAvailableCapacity == null) {
            if (other.unAvailableCapacity != null) {
                return false;
            }
        } else if (!unAvailableCapacity.equals(other.unAvailableCapacity)) {
            return false;
        }
        return true;
    }

    @Override
    public RadioUtilization clone() {
        return (RadioUtilization) super.clone();
    }
}

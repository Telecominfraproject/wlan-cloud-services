package com.telecominfraproject.wlan.systemevent.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasProducedTimestamp;

/**
 * @author dtoptygin
 *
 */
public abstract class SystemEvent extends BaseJsonModel implements HasCustomerId, HasEquipmentId, HasProducedTimestamp {
    
    private static final long serialVersionUID = -2030250050038922237L;

    private long eventTimestamp;
    private String deploymentId;

    protected SystemEvent() {
    }

    protected SystemEvent(long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }
    
    public long getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    @Override
    public SystemEvent clone() {
        SystemEvent ret = (SystemEvent) super.clone();
        
        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName(), this.eventTimestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        // make sure we compare by name in case they are from different class loader
        if (!obj.getClass().getName().equals(this.getClass().getName())) {
            return false;
        }
        SystemEvent other = (SystemEvent) obj;
        if (eventTimestamp != other.eventTimestamp) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        return false;
    }
    
    @Override
    @JsonIgnore
    public long getProducedTimestampMs() {
        Long eTs = getEventTimestamp();
        return eTs==null?0L:eTs;
    }


    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    @Override
    public int getCustomerId() {
        //subclasses will override this as needed
        return 0;
    }
    
    @Override
    public long getEquipmentId() {
        //subclasses will override this as needed
        return 0;
    }
    
    public String calculateUniqueId() {
        //subclasses will override this as needed
        return null;        
    }
}

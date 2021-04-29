package com.telecominfraproject.wlan.systemevent.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;

/**
 * @author dtoptygin
 * @param <T> 
 *
 */
public abstract class CustomerEvent extends SystemEvent implements HasCustomerId {

    private static final long serialVersionUID = -6866253868015569466L;
    
    private int customerId;

    /**
     * Creates a CustomerEvent with the current time stamp.
     * @param customerId
     */
    protected CustomerEvent(int customerId) {
        super(System.currentTimeMillis());
        this.customerId = customerId;
    }

    protected CustomerEvent(int customerId, long eventTimestamp) {
        super(eventTimestamp);
        this.customerId = customerId;
    }
    
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    

    @Override
    public CustomerEvent clone() {
		CustomerEvent ret = (CustomerEvent) super.clone();
        
        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof CustomerEvent)) {
            return false;
        }
		CustomerEvent other = (CustomerEvent) obj;
        if (customerId != other.customerId) {
            return false;
        }
        return true;
    }

    
}

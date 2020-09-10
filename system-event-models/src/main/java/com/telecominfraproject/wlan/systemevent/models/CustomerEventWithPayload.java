package com.telecominfraproject.wlan.systemevent.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;

/**
 * @author dtoptygin
 * @param <T> 
 *
 */
public abstract class CustomerEventWithPayload<T> extends SystemEvent implements HasCustomerId {

    private static final long serialVersionUID = -6866253868015569466L;
    
    private int customerId;
    private T payload;

    protected CustomerEventWithPayload(int customerId, long eventTimestamp, T payload) {
        super(eventTimestamp);
        this.customerId = customerId;
        this.payload = payload;
    }
    
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public T getPayload() {
    	return payload;
    }

    public void setPayload(T payload) {
    	this.payload = payload;
    }

    @Override
    public CustomerEventWithPayload<T> clone() {
        @SuppressWarnings("unchecked")
		CustomerEventWithPayload<T> ret = (CustomerEventWithPayload<T>) super.clone();
        
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
        if (!(obj instanceof CustomerEventWithPayload)) {
            return false;
        }
        @SuppressWarnings("unchecked")
		CustomerEventWithPayload<T> other = (CustomerEventWithPayload<T>) obj;
        if (customerId != other.customerId) {
            return false;
        }
        return true;
    }

    
}

package com.telecominfraproject.wlan.systemevent.models;


/**
 * @author dtoptygin
 * @param <T> 
 *
 */
public abstract class CustomerEvent<T> extends SystemEvent {

    private static final long serialVersionUID = -6866253868015569466L;
    
    private int customerId;
    /**
     * Rule agent queue name into which this event is to be delivered
     */
    private String queueName;

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
    
    /**
     * @return Rule agent queue name into which this event is to be delivered
     */
    public String getQueueName() {
        return queueName;
    }

    /**
     * Set Rule agent queue name into which this event is to be delivered
     * @param queueName
     */
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public CustomerEvent<T> clone() {
        CustomerEvent<T> ret = (CustomerEvent<T>) super.clone();
        
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + customerId;
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
        if (!(obj instanceof CustomerEvent)) {
            return false;
        }
        CustomerEvent<T> other = (CustomerEvent<T>) obj;
        if (customerId != other.customerId) {
            return false;
        }
        return true;
    }

    
}

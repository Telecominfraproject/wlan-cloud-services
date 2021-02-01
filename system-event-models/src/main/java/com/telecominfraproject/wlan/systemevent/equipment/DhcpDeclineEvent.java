/**
 * 
 */
package com.telecominfraproject.wlan.systemevent.equipment;

/**
 * @author ekeddy
 *
 */
public class DhcpDeclineEvent extends BaseDhcpEvent {
    private static final long serialVersionUID = -7745659083975485467L;

    public DhcpDeclineEvent(int customerId, long locationId, long equipmentId, long eventTimestamp, long sessionId){
    	super(customerId, locationId, equipmentId,eventTimestamp, sessionId);
    }
    
    public DhcpDeclineEvent() {
        super(0,0L, 0L,0L,0L);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

    @Override
    public DhcpDeclineEvent clone() {
        DhcpDeclineEvent ret = (DhcpDeclineEvent) super.clone();

        return ret;
    }

}

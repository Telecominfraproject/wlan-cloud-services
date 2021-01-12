/**
 * 
 */
package com.telecominfraproject.wlan.systemevent.equipment;

/**
 * @author ekeddy
 *
 */
public class DhcpInformEvent extends BaseDhcpEvent {
    private static final long serialVersionUID = 7053813308222200205L;

    public DhcpInformEvent(int customerId, long equipmentId, long eventTimestamp, long sessionId){
    	super(customerId,equipmentId,eventTimestamp, sessionId);
    }
    
    public DhcpInformEvent() {
        super(0,0L,0L,0L);
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
    public DhcpInformEvent clone() {
        DhcpInformEvent ret = (DhcpInformEvent) super.clone();

        return ret;
    }
}

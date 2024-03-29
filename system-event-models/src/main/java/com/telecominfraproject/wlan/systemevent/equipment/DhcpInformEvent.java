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

    public DhcpInformEvent(int customerId, long locationId, long equipmentId, long eventTimestamp, String sessionId){
    	super(customerId, locationId,equipmentId,eventTimestamp, sessionId);
    }
    
    public DhcpInformEvent() {
        super(0, 0L,0L,0L,"0");
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

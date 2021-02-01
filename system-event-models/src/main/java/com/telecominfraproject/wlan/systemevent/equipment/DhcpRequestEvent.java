/**
 * 
 */
package com.telecominfraproject.wlan.systemevent.equipment;

import java.util.Objects;

public class DhcpRequestEvent extends BaseDhcpEvent {
    private static final long serialVersionUID = 906425685437156761L;
    private String hostName;
    
    public DhcpRequestEvent(int customerId, long locationId, long equipmentId, long eventTimestamp, long sessionId){
    	super(customerId, locationId,equipmentId,eventTimestamp, sessionId);
    }
    
    public DhcpRequestEvent() {
        super(0, 0L,0L,0L,0L);
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(hostName);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DhcpRequestEvent other = (DhcpRequestEvent) obj;
        return Objects.equals(hostName, other.hostName);
    }

    @Override
    public DhcpRequestEvent clone() {
        DhcpRequestEvent ret = (DhcpRequestEvent) super.clone();

        return ret;
    }


}

/**
 * 
 */
package com.telecominfraproject.wlan.systemevent.equipment;

import java.util.Objects;

public class DhcpDiscoverEvent extends BaseDhcpEvent {
    private static final long serialVersionUID = -8290687227649478971L;
    private String hostName;

    public DhcpDiscoverEvent(int customerId, long equipmentId, long eventTimestamp, Long sessionId){
    	super(customerId,equipmentId,eventTimestamp,sessionId);
    }

	public DhcpDiscoverEvent() {
        super(0,0,0, null);
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
        DhcpDiscoverEvent other = (DhcpDiscoverEvent) obj;
        return Objects.equals(hostName, other.hostName);
    }

    @Override
    public DhcpDiscoverEvent clone() {
        DhcpDiscoverEvent ret = (DhcpDiscoverEvent) super.clone();

        return ret;
    }

}

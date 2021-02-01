/**
 * 
 */
package com.telecominfraproject.wlan.systemevent.equipment;

import java.util.Objects;

public class DhcpNakEvent extends BaseDhcpEvent {
    private static final long serialVersionUID = -8648265834227002667L;
    private boolean fromInternal = false;

    public DhcpNakEvent(int customerId, long locationId, long equipmentId, long eventTimestamp, long sessionId) {
        super(customerId, locationId, equipmentId, eventTimestamp, sessionId);
    }

    public DhcpNakEvent() {
        super(0, 0L,0L,0L,0L);
    }

    /**
     * @return the fromInternal
     */
    public boolean isFromInternal() {
        return fromInternal;
    }

    /**
     * @param fromInternal the fromInternal to set
     */
    public void setFromInternal(boolean fromInternal) {
        this.fromInternal = fromInternal;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(fromInternal);
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
        DhcpNakEvent other = (DhcpNakEvent) obj;
        return fromInternal == other.fromInternal;
    }

    @Override
    public DhcpNakEvent clone() {
        DhcpNakEvent ret = (DhcpNakEvent) super.clone();

        return ret;
    }
}

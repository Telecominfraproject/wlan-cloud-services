/**
 * 
 */
package com.telecominfraproject.wlan.systemevent.equipment;

public class DhcpOfferEvent extends BaseDhcpEvent {
    private static final long serialVersionUID = -7646915589780956503L;
    /**
     * Offer provided by internal server
     */
    private boolean fromInternal = false;

    public DhcpOfferEvent(int customerId, long locationId, long equipmentId, long eventTimestamp, String sessionId) {
        super(customerId, locationId, equipmentId, eventTimestamp, sessionId);
    }

    public DhcpOfferEvent() {
        super(0, 0L,0L,0L,"0");
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
    public DhcpOfferEvent clone() {
        DhcpOfferEvent ret = (DhcpOfferEvent) super.clone();

        return ret;
    }

}

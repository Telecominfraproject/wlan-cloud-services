package com.telecominfraproject.wlan.systemevent.models;

public class UnserializableSystemEvent extends EquipmentEvent<UnserializableSystemEvent> {
    /**
     * 
     */
    private static final long serialVersionUID = -4671657885329062531L;

    public UnserializableSystemEvent() {
        super(0, 0, 0);
    }

    private String payloadStr;

    public String getPayloadStr() {
        return payloadStr;
    }

    public void setPayloadStr(String payloadStr) {
        this.payloadStr = payloadStr;
    }

    // WARNING: this method is final because we want to guarantee that events
    // that make its
    // way into rule engine do not try to create equals methods that work off
    // mutable fields
    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    // WARNING: this method is final because we want to guarantee that events
    // that make its
    // way into rule engine do not try to create equals methods that work off
    // mutable fields
    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }
}

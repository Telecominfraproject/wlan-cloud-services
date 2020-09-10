package com.telecominfraproject.wlan.systemevent.models;

public class UnserializableSystemEvent extends EquipmentEventWithPayload<String> {

	private static final long serialVersionUID = -4671657885329062531L;

    public UnserializableSystemEvent(int customerId, long equipmentId, long eventTimestamp, String payload) {
        super(customerId, equipmentId, eventTimestamp, payload);
    }

    public UnserializableSystemEvent() {
        super(0, 0, 0, null);
    }

}

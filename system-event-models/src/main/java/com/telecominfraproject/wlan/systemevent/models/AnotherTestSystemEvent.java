package com.telecominfraproject.wlan.systemevent.models;

import java.util.Objects;

public class AnotherTestSystemEvent extends EquipmentEventWithPayload<String> {
    private static final long serialVersionUID = -2430769929028051157L;

    public AnotherTestSystemEvent(int customerId, long equipmentId, long eventTimestamp, String payload) {
        super(customerId, equipmentId, eventTimestamp, payload);
    }

    public AnotherTestSystemEvent() {
        super(0, 0, 0, null);
    }

    
	
	@Override
	public int hashCode() {
        return Objects.hash(super.hashCode(), getPayload());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof AnotherTestSystemEvent)) {
			return false;
		}
		
		AnotherTestSystemEvent other = (AnotherTestSystemEvent) obj;
		
		return Objects.equals(getPayload(), other.getPayload());

	}

	@Override
	public AnotherTestSystemEvent clone() {
		return (AnotherTestSystemEvent) super.clone();
	}
}

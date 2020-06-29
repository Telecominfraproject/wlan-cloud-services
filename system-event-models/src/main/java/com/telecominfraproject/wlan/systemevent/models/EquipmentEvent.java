package com.telecominfraproject.wlan.systemevent.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;

/**
 * @author dtoptygin
 * @param <T>
 *
 */
public abstract class EquipmentEvent<T> extends CustomerEvent<T> implements HasEquipmentId {

    private static final long serialVersionUID = -3139345444294000637L;

    private long equipmentId;

    protected EquipmentEvent(int customerId, long equipmentId, long eventTimestamp, T payload) {
        super(customerId, eventTimestamp, payload);
        this.equipmentId = equipmentId;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    @Override
    public EquipmentEvent<T> clone() {
        EquipmentEvent<T> ret = (EquipmentEvent<T>) super.clone();
        return ret;
    }

    // WARNING: subclass must override and make it final to guarantee that
    // events that make its way into rule engine do not try to create equals
    // methods that work off mutable fields
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), equipmentId);
    }

    // WARNING: subclass must override and make it final to guarantee that
    // events that make its way into rule engine do not try to create equals
    // methods that work off mutable fields
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj.getClass().equals(this.getClass()))) {
            return false;
        }

        @SuppressWarnings("unchecked")
		EquipmentEvent<T> other = (EquipmentEvent<T>) obj;
        if (equipmentId != other.equipmentId) {
            return false;
        }
        return true;
    }

}

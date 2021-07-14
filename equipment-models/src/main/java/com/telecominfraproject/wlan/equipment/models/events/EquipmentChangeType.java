package com.telecominfraproject.wlan.equipment.models.events;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;


public enum EquipmentChangeType {

    All(0), ChannelsOnly(1), CellSizeAttributesOnly(2), ApImpacting(3), blinkLEDs(4), UNSUPPORTED(-1);

    private final int id;
    private static final Map<Integer, EquipmentChangeType> ELEMENTS = new HashMap<>();
    private static final EquipmentChangeType validValues[] = { All, ChannelsOnly, CellSizeAttributesOnly, ApImpacting,blinkLEDs};

    private EquipmentChangeType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static EquipmentChangeType getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    // initialize elements map
                    for (EquipmentChangeType met : EquipmentChangeType.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }

    public static EquipmentChangeType[] validValues() {
        return validValues;
    }

    @JsonCreator
    public static EquipmentChangeType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, EquipmentChangeType.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(EquipmentChangeType radioType) {
        return (UNSUPPORTED.equals(radioType));
    }

    public static boolean hasUnsupportedValue(Collection<EquipmentChangeType> radioTypes) {
        if (radioTypes != null) {
            for (EquipmentChangeType type : radioTypes) {
                if (isUnsupported(type)) {
                    return true;
                }
            }
        }

        return false;
    }
}

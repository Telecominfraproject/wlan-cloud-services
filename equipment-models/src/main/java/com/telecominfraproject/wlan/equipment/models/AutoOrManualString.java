package com.telecominfraproject.wlan.equipment.models;

public class AutoOrManualString extends AbstractAutoOrManual<String> {

    private static final long serialVersionUID = -9061816241015020567L;

    public AutoOrManualString(boolean isAuto, String manualValue) {
        super(isAuto, manualValue);
    }

    protected AutoOrManualString() {
        super();
    }
}

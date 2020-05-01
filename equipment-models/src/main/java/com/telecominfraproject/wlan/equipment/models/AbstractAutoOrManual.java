package com.telecominfraproject.wlan.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public abstract class AbstractAutoOrManual<T> extends BaseJsonModel {
    private static final long serialVersionUID = 2761981826629575941L;
    private boolean auto;
    private T value;

    public AbstractAutoOrManual(boolean isAuto, T manualValue) {
        this.auto = isAuto;
        this.value = manualValue;
    }

    protected AbstractAutoOrManual() {
        // json construct
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(auto, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AbstractAutoOrManual)) {
            return false;
        }
        AbstractAutoOrManual<T> other = (AbstractAutoOrManual<T>) obj;
        return this.auto == other.auto && Objects.equals(value, other.value);
    }

}

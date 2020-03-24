package com.telecominfraproject.wlan.systemevent.models;

public class TestSystemEvent extends EquipmentEvent<TestSystemEvent> {
    private static final long serialVersionUID = -2430769929028051157L;
    private String testValue;

    public TestSystemEvent() {
        super(0, 0, 0);
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
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

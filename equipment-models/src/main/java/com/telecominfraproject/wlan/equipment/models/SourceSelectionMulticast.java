package com.telecominfraproject.wlan.equipment.models;

import com.telecominfraproject.wlan.core.model.equipment.AbstractSource;
import com.telecominfraproject.wlan.core.model.equipment.SourceType;

public class SourceSelectionMulticast extends AbstractSource<MulticastRate>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4631172351117490997L;
	
	private SourceSelectionMulticast() {

	}

    private SourceSelectionMulticast(SourceType source, MulticastRate value) {
        setSource(source);
        setValue(value);
    }

    public static SourceSelectionMulticast createAutomaticInstance(MulticastRate value) {
        return new SourceSelectionMulticast(SourceType.auto, value);
    }

    public static SourceSelectionMulticast createManualInstance(MulticastRate value) {
        return new SourceSelectionMulticast(SourceType.manual, value);
    }
    
    public static SourceSelectionMulticast createProfileInstance(MulticastRate value) {
        return new SourceSelectionMulticast(SourceType.profile, value);
    }
    
    @Override
    public SourceSelectionMulticast clone() {
        return (SourceSelectionMulticast) super.clone();
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (SourceType.isUnsupported(source) || MulticastRate.isUnsupported(value)) {
            return true;
        }
        return false;
    }
    
}

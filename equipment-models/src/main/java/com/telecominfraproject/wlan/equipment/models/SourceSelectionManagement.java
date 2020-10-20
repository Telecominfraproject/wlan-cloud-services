package com.telecominfraproject.wlan.equipment.models;

import com.telecominfraproject.wlan.core.model.equipment.AbstractSource;
import com.telecominfraproject.wlan.core.model.equipment.SourceType;

public class SourceSelectionManagement extends AbstractSource<ManagementRate>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4631172351117490997L;
	
	private SourceSelectionManagement() {

	}

    private SourceSelectionManagement(SourceType source, ManagementRate value) {
        setSource(source);
        setValue(value);
    }

    public static SourceSelectionManagement createAutomaticInstance(ManagementRate value) {
        return new SourceSelectionManagement(SourceType.auto, value);
    }

    public static SourceSelectionManagement createManualInstance(ManagementRate value) {
        return new SourceSelectionManagement(SourceType.manual, value);
    }
    
    public static SourceSelectionManagement createProfileInstance(ManagementRate value) {
        return new SourceSelectionManagement(SourceType.profile, value);
    }
    
    @Override
    public SourceSelectionManagement clone() {
        return (SourceSelectionManagement) super.clone();
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (SourceType.isUnsupported(source) || ManagementRate.isUnsupported(value)) {
            return true;
        }
        return false;
    }
    
}

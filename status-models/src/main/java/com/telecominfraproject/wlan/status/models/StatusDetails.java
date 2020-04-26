package com.telecominfraproject.wlan.status.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtoptygin
 *
 */
public class StatusDetails extends BaseJsonModel {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
    //TODO: put more fields here, generate getters/setters for them
    private String sampleDetailsStr;

	public String getSampleDetailsStr() {
		return sampleDetailsStr;
	}

	public void setSampleDetailsStr(String sampleDetailsStr) {
		this.sampleDetailsStr = sampleDetailsStr;
	}

	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		return false;
	}
	
    @Override
    public StatusDetails clone() {
        return (StatusDetails) super.clone();
    }

	@Override
	public int hashCode() {
		return Objects.hash(sampleDetailsStr);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof StatusDetails)) {
			return false;
		}
		StatusDetails other = (StatusDetails) obj;
		return Objects.equals(sampleDetailsStr, other.sampleDetailsStr);
	}
    
}

package com.telecominfraproject.wlan.manufacturer.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author mpreston
 *
 * Properties of Manufacturer OUI mappings.
 */
public class ManufacturerOuiDetails extends BaseJsonModel {

    private static final long serialVersionUID = 6097322123679241676L;
    
    private String oui;
    private String manufacturerName;
    private String manufacturerAlias;
    
    public ManufacturerOuiDetails() {}

    public String getOui() {
        return oui;
    }

    public void setOui(String oui) {
        this.oui = oui;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerAlias() {
        return manufacturerAlias;
    }

    public void setManufacturerAlias(String manufacturerAlias) {
        this.manufacturerAlias = manufacturerAlias;
    }
    
    public String calculateDisplayName() {
     return (this.getManufacturerAlias() != null && this.getManufacturerAlias().length() != 0) ? 
             this.getManufacturerAlias() : this.getManufacturerName();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((manufacturerAlias == null) ? 0 : manufacturerAlias.hashCode());
        result = prime * result + ((manufacturerName == null) ? 0 : manufacturerName.hashCode());
        result = prime * result + ((oui == null) ? 0 : oui.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ManufacturerOuiDetails other = (ManufacturerOuiDetails) obj;
        if (oui == null) {
            if (other.oui != null)
                return false;
        } else if (!oui.equals(other.oui))
            return false;
        if (manufacturerName == null) {
            if (other.manufacturerName != null)
                return false;
        } else if (!manufacturerName.equals(other.manufacturerName))
            return false;
        if (manufacturerAlias == null) {
            if (other.manufacturerAlias != null)
                return false;
        } else if (!manufacturerAlias.equals(other.manufacturerAlias))
            return false;
        return true;
    }

    @Override
    public ManufacturerOuiDetails clone() {
        ManufacturerOuiDetails ret = (ManufacturerOuiDetails) super.clone();
        return ret;
    }
    
}

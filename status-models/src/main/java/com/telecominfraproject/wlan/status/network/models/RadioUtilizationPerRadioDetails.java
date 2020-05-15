package com.telecominfraproject.wlan.status.network.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class RadioUtilizationPerRadioDetails extends BaseJsonModel {

    private static final long serialVersionUID = -4773437330014768087L;
    
    private int avgAssocClientTx;
    private int avgUnassocClientTx;
    private int avgAssocClientRx;
    private int avgUnassocClientRx;
    private int avgNonWifi;

    public int getAvgAssocClientTx() {
        return avgAssocClientTx;
    }

    public void setAvgAssocClientTx(int avgAssocClientTx) {
        this.avgAssocClientTx = avgAssocClientTx;
    }

    public int getAvgUnassocClientTx() {
        return avgUnassocClientTx;
    }

    public void setAvgUnassocClientTx(int avgUnassocClientTx) {
        this.avgUnassocClientTx = avgUnassocClientTx;
    }

    public int getAvgAssocClientRx() {
        return avgAssocClientRx;
    }

    public void setAvgAssocClientRx(int avgAssocClientRx) {
        this.avgAssocClientRx = avgAssocClientRx;
    }

    public int getAvgUnassocClientRx() {
        return avgUnassocClientRx;
    }

    public void setAvgUnassocClientRx(int avgUnassocClientRx) {
        this.avgUnassocClientRx = avgUnassocClientRx;
    }

    public int getAvgNonWifi() {
        return avgNonWifi;
    }

    public void setAvgNonWifi(int avgNonWifi) {
        this.avgNonWifi = avgNonWifi;
    }

    @Override
    public RadioUtilizationPerRadioDetails clone() {
        RadioUtilizationPerRadioDetails ret = (RadioUtilizationPerRadioDetails) super.clone();
        
        return ret;
    }
    
    /**
     * @param other
     * @return this object, merged with other
     */
    public RadioUtilizationPerRadioDetails combineWith(RadioUtilizationPerRadioDetails other) {
        
        if(other == null){
            return this;
        }
        
        if(this.avgAssocClientTx == 0){
            this.avgAssocClientTx = other.avgAssocClientTx;
        }

        if(this.avgUnassocClientTx == 0){
            this.avgUnassocClientTx = other.avgUnassocClientTx;
        }
        
        if(this.avgAssocClientRx == 0){
            this.avgAssocClientRx = other.avgAssocClientRx;
        }
        
        if(this.avgUnassocClientRx == 0){
            this.avgUnassocClientRx = other.avgUnassocClientRx;
        }
        
        if(this.avgNonWifi == 0){
            this.avgNonWifi = other.avgNonWifi;
        }

        return this;
    }

}

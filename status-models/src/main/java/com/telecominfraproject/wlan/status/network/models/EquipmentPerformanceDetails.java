package com.telecominfraproject.wlan.status.network.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class EquipmentPerformanceDetails extends BaseJsonModel {

    private static final long serialVersionUID = -9029108689918137602L;
    
    private Integer avgFreeMemory;
    private Integer avgCpuUtilCore1;
    private Integer avgCpuUtilCore2;
    private Integer avgCpuTemperature;

    public Integer getAvgFreeMemory() {
        return avgFreeMemory;
    }

    public void setAvgFreeMemory(Integer avgFreeMemory) {
        this.avgFreeMemory = avgFreeMemory;
    }

    public Integer getAvgCpuUtilCore1() {
        return avgCpuUtilCore1;
    }

    public void setAvgCpuUtilCore1(Integer avgCpuUtilCore1) {
        this.avgCpuUtilCore1 = avgCpuUtilCore1;
    }

    public Integer getAvgCpuUtilCore2() {
        return avgCpuUtilCore2;
    }

    public void setAvgCpuUtilCore2(Integer avgCpuUtilCore2) {
        this.avgCpuUtilCore2 = avgCpuUtilCore2;
    }

    public Integer getAvgCpuTemperature() {
        return avgCpuTemperature;
    }

    public void setAvgCpuTemperature(Integer avgCpuTemperature) {
        this.avgCpuTemperature = avgCpuTemperature;
    }

    @Override
    public EquipmentPerformanceDetails clone() {
        EquipmentPerformanceDetails ret = (EquipmentPerformanceDetails) super.clone();
        
        return ret;
    }
    
    /**
     * @param other
     * @return this object, merged with other
     */
    public EquipmentPerformanceDetails combineWith(EquipmentPerformanceDetails other) {
        
        if(other == null){
            return this;
        }
        
        if(this.avgFreeMemory==null){
            this.avgFreeMemory = other.avgFreeMemory;
        }

        if(this.avgCpuUtilCore1==null){
            this.avgCpuUtilCore1 = other.avgCpuUtilCore1;
        }

        if(this.avgCpuUtilCore2==null){
            this.avgCpuUtilCore2 = other.avgCpuUtilCore2;
        }

        if(this.avgCpuTemperature==null){
            this.avgCpuTemperature = other.avgCpuTemperature;
        }

        
        return this;
    }

}

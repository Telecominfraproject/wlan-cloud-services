package com.telecominfraproject.wlan.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class CellSizeAttributes extends BaseJsonModel {
    private static final long serialVersionUID = -6799381201211331251L;
    private Integer rxCellSizeDb;
    private Integer probeResponseThresholdDb;
    private Integer clientDisconnectThresholdDb;
    private Integer eirpTxPowerDb;
    private MulticastRate multicastRate;
    private ManagementRate managementRate;
    
    public CellSizeAttributes() {
        // serialize
    }

    public CellSizeAttributes(Integer rxCellSizeDb, Integer probeResponseThresholdDb, ManagementRate managementRate,
            Integer eirpTxPowerDb, MulticastRate multicastRate,  Integer clientDisconnectThresholdDb) {
        super();
        this.rxCellSizeDb = rxCellSizeDb;
        this.probeResponseThresholdDb = probeResponseThresholdDb;
        this.clientDisconnectThresholdDb = clientDisconnectThresholdDb;
        this.eirpTxPowerDb = eirpTxPowerDb;
        this.multicastRate = multicastRate;
        this.managementRate = managementRate;
    }
    
    public Integer getRxCellSizeDb() {
        return rxCellSizeDb;
    }

    public void setRxCellSizeDb(Integer rxCellSizeDb) {
        this.rxCellSizeDb = rxCellSizeDb;
    }

    public Integer getProbeResponseThresholdDb() {
        return probeResponseThresholdDb;
    }

    public void setProbeResponseThresholdDb(Integer probeResponseThresholdDb) {
        this.probeResponseThresholdDb = probeResponseThresholdDb;
    }

    public Integer getClientDisconnectThresholdDb() {
        return clientDisconnectThresholdDb;
    }

    public void setClientDisconnectThresholdDb(Integer clientDisconnectThresholdDb) {
        this.clientDisconnectThresholdDb = clientDisconnectThresholdDb;
    }

    public Integer getEirpTxPowerDb() {
        return eirpTxPowerDb;
    }

    public void setEirpTxPowerDb(Integer eirpTxPowerDb) {
        this.eirpTxPowerDb = eirpTxPowerDb;
    }

    public MulticastRate getMulticastRate() {
        return multicastRate;
    }

    public void setMulticastRate(MulticastRate multicastRate) {
        this.multicastRate = multicastRate;
    }

    public ManagementRate getManagementRate() {
        return managementRate;
    }

    public void setManagementRate(ManagementRate managementRate) {
        this.managementRate = managementRate;
    }

    @Override
    public boolean hasUnsupportedValue() 
    {
        return MulticastRate.isUnsupported(multicastRate) || 
                ManagementRate.isUnsupported(managementRate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(rxCellSizeDb, probeResponseThresholdDb, clientDisconnectThresholdDb,
                eirpTxPowerDb, multicastRate, managementRate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof CellSizeAttributes)) {
            return false;
        }
        CellSizeAttributes other = (CellSizeAttributes) obj;
        return Objects.equals(rxCellSizeDb, other.rxCellSizeDb)
        		&& Objects.equals(probeResponseThresholdDb, other.probeResponseThresholdDb)
        		&& Objects.equals(clientDisconnectThresholdDb, other.clientDisconnectThresholdDb)
        		&& Objects.equals(eirpTxPowerDb, other.eirpTxPowerDb)
        		&& Objects.equals(multicastRate, other.multicastRate)
        		&& Objects.equals(managementRate, other.managementRate);
    } 
}

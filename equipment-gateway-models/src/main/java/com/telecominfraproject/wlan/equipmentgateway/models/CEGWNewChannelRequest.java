package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.Objects;

public class CEGWNewChannelRequest extends EquipmentCommand {
    /**
     *
     */
    private static final long serialVersionUID = -8251272945594285119L;
    /**
     *
     */
    private Integer newBackupChannel2g;
    private Integer newBackupChannel5g;

    protected CEGWNewChannelRequest() {
        // serial
    }

    public CEGWNewChannelRequest(String qrCode, long equipmentId, Integer newBackupChannel2g,
            Integer newBackupChannel5g) {
        super(CEGWCommandType.NewChannelRequest, qrCode, equipmentId);
        this.newBackupChannel2g = newBackupChannel2g;
        this.newBackupChannel5g = newBackupChannel5g;
    }

    public Integer getNewBackupChannel2g() {
        return newBackupChannel2g;
    }

    public Integer getNewBackupChannel5g() {
        return newBackupChannel5g;
    }

    public void setNewBackupChannel2g(Integer newBackupChannel2g) {
        this.newBackupChannel2g = newBackupChannel2g;
    }

    public void setNewBackupChannel5g(Integer newBackupChannel5g) {
        this.newBackupChannel5g = newBackupChannel5g;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(newBackupChannel2g, newBackupChannel5g);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof CEGWNewChannelRequest)) {
            return false;
        }
        CEGWNewChannelRequest other = (CEGWNewChannelRequest) obj;
        return Objects.equals(newBackupChannel2g, other.newBackupChannel2g)
                && Objects.equals(newBackupChannel5g, other.newBackupChannel5g);
    }

}

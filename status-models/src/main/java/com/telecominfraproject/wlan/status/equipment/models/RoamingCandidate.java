package com.telecominfraproject.wlan.status.equipment.models;

import java.net.InetAddress;
import java.util.List;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class RoamingCandidate extends BaseJsonModel {
    private static final long serialVersionUID = -4887457522962137028L;
    private RadioType radioType;
    private String qrCode;
    private MacAddress mac;
    private InetAddress ip;
    private InetAddress subnetMask;
    private InetAddress gateway;
    private Integer signalStrength;
    private Integer numConnectedClients;
    private Integer activeChannel;
    private Integer channelUtilizationPercentage;
    private Integer availableCapacityPercentage;
    private List<BSSIDDetails> bssids;

    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public MacAddress getMac() {
        return mac;
    }

    public void setMac(MacAddress mac) {
        this.mac = mac;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public InetAddress getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(InetAddress subnetMask) {
        this.subnetMask = subnetMask;
    }

    public InetAddress getGateway() {
        return gateway;
    }

    public void setGateway(InetAddress gateway) {
        this.gateway = gateway;
    }

    public Integer getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Integer signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Integer getNumConnectedClients() {
        return numConnectedClients;
    }

    public void setNumConnectedClients(Integer numConnectedClients) {
        this.numConnectedClients = numConnectedClients;
    }

    public Integer getActiveChannel() {
        return activeChannel;
    }

    public void setActiveChannel(Integer activeChannel) {
        this.activeChannel = activeChannel;
    }

    public Integer getChannelUtilizationPercentage() {
        return channelUtilizationPercentage;
    }

    public void setChannelUtilizationPercentage(Integer channelUtilizationPercentage) {
        this.channelUtilizationPercentage = channelUtilizationPercentage;
    }

    public Integer getAvailableCapacityPercentage() {
        return availableCapacityPercentage;
    }

    public void setAvailableCapacityPercentage(Integer availableCapacityPercentage) {
        this.availableCapacityPercentage = availableCapacityPercentage;
    }

    public List<BSSIDDetails> getBssids() {
        return bssids;
    }

    public void setBssids(List<BSSIDDetails> bssids) {
        this.bssids = bssids;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((activeChannel == null) ? 0 : activeChannel.hashCode());
        result = prime * result + ((availableCapacityPercentage == null) ? 0 : availableCapacityPercentage.hashCode());
        result = prime * result + ((bssids == null) ? 0 : bssids.hashCode());
        result = prime * result
                + ((channelUtilizationPercentage == null) ? 0 : channelUtilizationPercentage.hashCode());
        result = prime * result + ((gateway == null) ? 0 : gateway.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + ((mac == null) ? 0 : mac.hashCode());
        result = prime * result + ((numConnectedClients == null) ? 0 : numConnectedClients.hashCode());
        result = prime * result + ((qrCode == null) ? 0 : qrCode.hashCode());
        result = prime * result + ((radioType == null) ? 0 : radioType.hashCode());
        result = prime * result + ((signalStrength == null) ? 0 : signalStrength.hashCode());
        result = prime * result + ((subnetMask == null) ? 0 : subnetMask.hashCode());
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
        RoamingCandidate other = (RoamingCandidate) obj;
        if (activeChannel == null) {
            if (other.activeChannel != null)
                return false;
        } else if (!activeChannel.equals(other.activeChannel))
            return false;
        if (availableCapacityPercentage == null) {
            if (other.availableCapacityPercentage != null)
                return false;
        } else if (!availableCapacityPercentage.equals(other.availableCapacityPercentage))
            return false;
        if (bssids == null) {
            if (other.bssids != null)
                return false;
        } else if (!bssids.equals(other.bssids))
            return false;
        if (channelUtilizationPercentage == null) {
            if (other.channelUtilizationPercentage != null)
                return false;
        } else if (!channelUtilizationPercentage.equals(other.channelUtilizationPercentage))
            return false;
        if (gateway == null) {
            if (other.gateway != null)
                return false;
        } else if (!gateway.equals(other.gateway))
            return false;
        if (ip == null) {
            if (other.ip != null)
                return false;
        } else if (!ip.equals(other.ip))
            return false;
        if (mac == null) {
            if (other.mac != null)
                return false;
        } else if (!mac.equals(other.mac))
            return false;
        if (numConnectedClients == null) {
            if (other.numConnectedClients != null)
                return false;
        } else if (!numConnectedClients.equals(other.numConnectedClients))
            return false;
        if (qrCode == null) {
            if (other.qrCode != null)
                return false;
        } else if (!qrCode.equals(other.qrCode))
            return false;
        if (radioType != other.radioType)
            return false;
        if (signalStrength == null) {
            if (other.signalStrength != null)
                return false;
        } else if (!signalStrength.equals(other.signalStrength))
            return false;
        if (subnetMask == null) {
            if (other.subnetMask != null)
                return false;
        } else if (!subnetMask.equals(other.subnetMask))
            return false;
        return true;
    }

    public boolean needToBeUpdated(RoamingCandidate other) {
        if (other != null) {
            boolean otherSame = Objects.equals(this.activeChannel, other.activeChannel)
                    && Objects.equals(this.bssids, other.bssids) && Objects.equals(this.gateway, other.gateway)
                    && Objects.equals(this.ip, other.ip) && Objects.equals(this.mac, other.mac)
                    && Objects.equals(this.numConnectedClients, other.numConnectedClients)
                    && Objects.equals(this.radioType, other.radioType)
                    && Objects.equals(this.subnetMask, other.subnetMask) && Objects.equals(this.qrCode, other.qrCode)
                    && closeEnough(this.availableCapacityPercentage, other.availableCapacityPercentage, 5)
                    && closeEnough(this.channelUtilizationPercentage, other.channelUtilizationPercentage, 5)
                    && closeEnough(this.signalStrength, other.signalStrength, 2);

            return !otherSame;
        }

        return true;
    }

    /**
     * Will make sure that the first and second don't have a bigger difference
     * than "maxDiff"
     * 
     * @param first
     * @param second
     * @param maxDiff
     * @return
     */
    static boolean closeEnough(Integer first, Integer second, int maxDiff) {
        if (first != null && second != null) {
            return Math.abs(first - second) < maxDiff;
        } else {
            // At lease one of these it null
            return first == second;
        }
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (RadioType.isUnsupported(radioType) || hasUnsupportedValue(mac) || hasUnsupportedValue(bssids)) {
            return true;
        }
        return false;
    }

}

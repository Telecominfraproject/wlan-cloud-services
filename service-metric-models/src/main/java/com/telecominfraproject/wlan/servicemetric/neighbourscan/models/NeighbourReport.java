package com.telecominfraproject.wlan.servicemetric.neighbourscan.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.equipment.DetectedAuthMode;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.NeighboreScanPacketType;
import com.telecominfraproject.wlan.core.model.equipment.NetworkType;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.equipment.Toggle;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class NeighbourReport extends BaseJsonModel {
    private static final long serialVersionUID = 4978041583453690066L;

    private MacAddress macAddress;

    private String ssid;
    private int beaconInterval;
    private NetworkType networkType;
    private Toggle privacy;
    private RadioType radioType;
    private int channel;
    private int rate;
    private int rssi;
    private int signal;
    private long scanTimeInSeconds;
    private Toggle nMode;
    private Toggle acMode;
    private Toggle bMode;
    private NeighboreScanPacketType packetType;
    private DetectedAuthMode secureMode;

    public Toggle getAcMode() {
        return acMode;
    }

    public int getBeaconInterval() {
        return beaconInterval;
    }

    public Toggle getbMode() {
        return bMode;
    }

    public int getChannel() {
        return channel;
    }

    public MacAddress getMacAddress() {
        return macAddress;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public Toggle getnMode() {
        return nMode;
    }

    public NeighboreScanPacketType getPacketType() {
        return packetType;
    }

    public Toggle getPrivacy() {
        return privacy;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public int getRate() {
        return rate;
    }

    public int getRssi() {
        return rssi;
    }

    public long getScanTimeInSeconds() {
        return scanTimeInSeconds;
    }

    public int getSignal() {
        return signal;
    }

    public String getSsid() {
        return ssid;
    }


    public void setAcMode(Toggle acMode) {
        this.acMode = acMode;
    }

    public void setBeaconInterval(int beaconInterval) {
        this.beaconInterval = beaconInterval;
    }

    public void setbMode(Toggle bMode) {
        this.bMode = bMode;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setMacAddress(MacAddress macAddress) {
        this.macAddress = macAddress;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public void setnMode(Toggle nMode) {
        this.nMode = nMode;
    }

    public void setPacketType(NeighboreScanPacketType packetType) {
        this.packetType = packetType;
    }

    public void setPrivacy(Toggle privacy) {
        this.privacy = privacy;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setScanTimeInSeconds(long scanTimeInSeconds) {
        this.scanTimeInSeconds = scanTimeInSeconds;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
    
    public DetectedAuthMode getSecureMode() {
        return secureMode;
    }

    public void setSecureMode(DetectedAuthMode secureMode) {
        this.secureMode = secureMode;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (NetworkType.isUnsupported(networkType) || Toggle.isUnsupported(privacy)
                || RadioType.isUnsupported(radioType) || Toggle.isUnsupported(nMode) || Toggle.isUnsupported(acMode)
                || Toggle.isUnsupported(bMode) || NeighboreScanPacketType.isUnsupported(packetType) 
                || DetectedAuthMode.isUnsupported(secureMode)) { 
            return true;
        }
         return false;
    }

    @Override
    public NeighbourReport clone() {
    	NeighbourReport ret =  (NeighbourReport) super.clone();
    	
    	if(macAddress!=null) {
    		ret.macAddress = macAddress.clone();
    	}
    	
    	return ret;
    }

	@Override
	public int hashCode() {
		return Objects.hash(acMode, bMode, beaconInterval, channel, macAddress, nMode, networkType, packetType, privacy,
				radioType, rate, rssi, scanTimeInSeconds, secureMode, signal, ssid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NeighbourReport)) {
			return false;
		}
		NeighbourReport other = (NeighbourReport) obj;
		return acMode == other.acMode && bMode == other.bMode && beaconInterval == other.beaconInterval
				&& channel == other.channel && Objects.equals(macAddress, other.macAddress) && nMode == other.nMode
				&& networkType == other.networkType && packetType == other.packetType && privacy == other.privacy
				&& radioType == other.radioType && rate == other.rate && rssi == other.rssi
				&& scanTimeInSeconds == other.scanTimeInSeconds && secureMode == other.secureMode
				&& signal == other.signal && Objects.equals(ssid, other.ssid);
	}
    
}

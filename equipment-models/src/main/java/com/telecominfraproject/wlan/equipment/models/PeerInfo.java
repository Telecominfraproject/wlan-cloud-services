package com.telecominfraproject.wlan.equipment.models;

import java.net.InetAddress;
import java.util.Arrays;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class PeerInfo extends BaseJsonModel {
    /**
     * 
     */
    private static final long serialVersionUID = -4895554157361116882L;
    
    private byte[] peerMAC;
    private InetAddress peerIP;
    private TunnelIndicator tunnel;
    private byte[] vlans;
    private String radiusSecret;

    public byte[] getPeerMAC() {
        return peerMAC;
    }

    public void setPeerMAC(byte[] peerMAC) {
        this.peerMAC = peerMAC;
    }

    public InetAddress getPeerIP() {
        return peerIP;
    }

    public void setPeerIP(InetAddress peerIP) {
        this.peerIP = peerIP;
    }

    public TunnelIndicator getTunnel() {
        return tunnel;
    }

    public void setTunnel(TunnelIndicator tunnel) {
        this.tunnel = tunnel;
    }

    public byte[] getVlans() {
        return vlans;
    }

    public void setVlans(byte[] vlans) {
        this.vlans = vlans;
    }

    public String getRadiusSecret() {
        return radiusSecret;
    }

    public void setRadiusSecret(String radiusSecret) {
        this.radiusSecret = radiusSecret;
    }

    @Override
    public PeerInfo clone() {
        PeerInfo ret = (PeerInfo) super.clone();
        if (peerMAC != null) {
            ret.peerMAC = new byte[peerMAC.length];
            System.arraycopy(peerMAC, 0, ret.peerMAC, 0, peerMAC.length);
        }
        if (vlans != null) {
            ret.vlans = new byte[vlans.length];
            System.arraycopy(vlans, 0, ret.vlans, 0, vlans.length);
        }

        return ret;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((peerIP == null) ? 0 : peerIP.hashCode());
        result = prime * result + Arrays.hashCode(peerMAC);
        result = prime * result + ((radiusSecret == null) ? 0 : radiusSecret.hashCode());
        result = prime * result + ((tunnel == null) ? 0 : tunnel.hashCode());
        result = prime * result + Arrays.hashCode(vlans);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PeerInfo)) {
            return false;
        }
        PeerInfo other = (PeerInfo) obj;
        if (peerIP == null) {
            if (other.peerIP != null) {
                return false;
            }
        } else if (!peerIP.equals(other.peerIP)) {
            return false;
        }
        if (!Arrays.equals(peerMAC, other.peerMAC)) {
            return false;
        }
        if (radiusSecret == null) {
            if (other.radiusSecret != null) {
                return false;
            }
        } else if (!radiusSecret.equals(other.radiusSecret)) {
            return false;
        }
        if (tunnel != other.tunnel) {
            return false;
        }
        if (!Arrays.equals(vlans, other.vlans)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (TunnelIndicator.isUnsupported(tunnel)) {
            return true;
        }
        return false;
    }
}

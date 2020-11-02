package com.telecominfraproject.wlan.profile.gre.tunnels;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;


public class GreTunnelProfile extends ProfileDetails implements PushableConfiguration<GreTunnelProfile> {

    private static final long serialVersionUID = 1981388698007511601L;

    private String gre_ifname;
    private InetAddress gre_local_inet_addr;
    private InetAddress gre_remote_inet_addr;
    private MacAddress gre_remote_mac_addr;

    private GreTunnelProfile() {

    }

    public static GreTunnelProfile createWithDefaults() {
        return new GreTunnelProfile();
    }

    public String getGre_ifname() {
        return gre_ifname;
    }


    public void setGre_ifname(String gre_ifname) {
        this.gre_ifname = gre_ifname;
    }


    public InetAddress getGre_local_inet_addr() {
        return gre_local_inet_addr;
    }


    public void setGre_local_inet_addr(InetAddress gre_local_inet_addr) {
        this.gre_local_inet_addr = gre_local_inet_addr;
    }


    public InetAddress getGre_remote_inet_addr() {
        return gre_remote_inet_addr;
    }


    public void setGre_remote_inet_addr(InetAddress gre_remote_inet_addr) {
        this.gre_remote_inet_addr = gre_remote_inet_addr;
    }


    public MacAddress getGre_remote_mac_addr() {
        return gre_remote_mac_addr;
    }


    public void setGre_remote_mac_addr(MacAddress gre_remote_mac_addr) {
        this.gre_remote_mac_addr = gre_remote_mac_addr;
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.gre_tunnel;
    }
    
    @Override
    public GreTunnelProfile clone() {
        return (GreTunnelProfile) super.clone();
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(GreTunnelProfile previousVersion) {
        if (equals(previousVersion))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gre_ifname, gre_local_inet_addr, gre_remote_inet_addr, gre_remote_mac_addr);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GreTunnelProfile)) {
            return false;
        }
        GreTunnelProfile other = (GreTunnelProfile) obj;
        return Objects.equals(gre_ifname, other.gre_ifname)
                && Objects.equals(gre_local_inet_addr, other.gre_local_inet_addr)
                && Objects.equals(gre_remote_inet_addr, other.gre_remote_inet_addr)
                && Objects.equals(gre_remote_mac_addr, other.gre_remote_mac_addr);
    }


}

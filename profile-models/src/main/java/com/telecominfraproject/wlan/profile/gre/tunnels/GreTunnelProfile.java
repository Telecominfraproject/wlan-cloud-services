package com.telecominfraproject.wlan.profile.gre.tunnels;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;


public class GreTunnelProfile extends ProfileDetails implements PushableConfiguration<GreTunnelProfile> {

    private static final long serialVersionUID = 1981388698007511601L;

    private String greTunnelName;
    private String greParentIfName;
    private InetAddress greLocalInetAddr;
    private InetAddress greRemoteInetAddr;
    private MacAddress greRemoteMacAddr;

    private GreTunnelProfile() {

    }

    public static GreTunnelProfile createWithDefaults() {
        return new GreTunnelProfile();
    }

    
    public String getGreTunnelName() {
        return greTunnelName;
    }

    
    public void setGreTunnelName(String greTunnelName) {
        this.greTunnelName = greTunnelName;
    }

    public String getGreParentIfName() {
        return greParentIfName;
    }


    public void setGreParentIfName(String gre_ifname) {
        this.greParentIfName = gre_ifname;
    }


    public InetAddress getGreLocalInetAddr() {
        return greLocalInetAddr;
    }


    public void setGreLocalInetAddr(InetAddress gre_local_inet_addr) {
        this.greLocalInetAddr = gre_local_inet_addr;
    }


    public InetAddress getGreRemoteInetAddr() {
        return greRemoteInetAddr;
    }


    public void setGreRemoteInetAddr(InetAddress gre_remote_inet_addr) {
        this.greRemoteInetAddr = gre_remote_inet_addr;
    }


    public MacAddress getGreRemoteMacAddr() {
        return greRemoteMacAddr;
    }


    public void setGreRemoteMacAddr(MacAddress gre_remote_mac_addr) {
        this.greRemoteMacAddr = gre_remote_mac_addr;
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
        return Objects.hash(greParentIfName, greLocalInetAddr, greRemoteInetAddr, greRemoteMacAddr);
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
        return Objects.equals(greParentIfName, other.greParentIfName)
                && Objects.equals(greLocalInetAddr, other.greLocalInetAddr)
                && Objects.equals(greRemoteInetAddr, other.greRemoteInetAddr)
                && Objects.equals(greRemoteMacAddr, other.greRemoteMacAddr);
    }


}

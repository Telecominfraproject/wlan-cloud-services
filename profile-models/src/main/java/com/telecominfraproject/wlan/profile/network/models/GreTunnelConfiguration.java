package com.telecominfraproject.wlan.profile.network.models;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class GreTunnelConfiguration extends BaseJsonModel implements PushableConfiguration<GreTunnelConfiguration> {

    private static final long serialVersionUID = 4403093289593783049L;
    private String greTunnelName;
    private String greParentIfName;
    private InetAddress greLocalInetAddr;
    private InetAddress greRemoteInetAddr;
    private MacAddress greRemoteMacAddr;
    private Set<Integer> vlanIdsInGreTunnel;

    private GreTunnelConfiguration() {
        vlanIdsInGreTunnel = new HashSet<>();
    }

    public static GreTunnelConfiguration createWithDefaults() {
        return new GreTunnelConfiguration();
    }

    public GreTunnelConfiguration(String greTunnelName, String greParentIfName, InetAddress greLocalInetAddr,
            InetAddress greRemoteInetAddr, MacAddress greRemoteMacAddr, Set<Integer> vlanIdsInGreTunnel) {

        this.greTunnelName = greTunnelName;
        this.greParentIfName = greParentIfName;
        this.greLocalInetAddr = greLocalInetAddr;
        this.greRemoteInetAddr = greRemoteInetAddr;
        this.greRemoteMacAddr = greRemoteMacAddr;
        if (vlanIdsInGreTunnel != null && vlanIdsInGreTunnel.size() > 0) {
            this.vlanIdsInGreTunnel = new HashSet<>(vlanIdsInGreTunnel);
        } else {
            vlanIdsInGreTunnel = new HashSet<>();
        }

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

    public void setGreParentIfName(String greParentIfName) {
        this.greParentIfName = greParentIfName;
    }

    public InetAddress getGreLocalInetAddr() {
        return greLocalInetAddr;
    }

    public void setGreLocalInetAddr(InetAddress greLocalInetAddr) {
        this.greLocalInetAddr = greLocalInetAddr;
    }

    public InetAddress getGreRemoteInetAddr() {
        return greRemoteInetAddr;
    }

    public void setGreRemoteInetAddr(InetAddress greRemoteInetAddr) {
        this.greRemoteInetAddr = greRemoteInetAddr;
    }

    public MacAddress getGreRemoteMacAddr() {
        return greRemoteMacAddr;
    }

    public void setGreRemoteMacAddr(MacAddress greRemoteMacAddr) {
        this.greRemoteMacAddr = greRemoteMacAddr;
    }

    public Set<Integer> getVlanIdsInGreTunnel() {
        return vlanIdsInGreTunnel;
    }

    public void setVlanIdsInGreTunnel(Set<Integer> vlanIdsInGreTunnel) {
        this.vlanIdsInGreTunnel = vlanIdsInGreTunnel;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(GreTunnelConfiguration previousVersion) {
        return !Objects.equals(this, previousVersion);
    }

    @Override
    public GreTunnelConfiguration clone() {
        GreTunnelConfiguration ret = (GreTunnelConfiguration) super.clone();

        if (this.vlanIdsInGreTunnel != null) {
            ret.vlanIdsInGreTunnel = new HashSet<>(this.vlanIdsInGreTunnel);
        }

        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(greLocalInetAddr, greParentIfName, greRemoteInetAddr, greRemoteMacAddr, greTunnelName,
                vlanIdsInGreTunnel);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GreTunnelConfiguration other = (GreTunnelConfiguration) obj;
        return Objects.equals(greLocalInetAddr, other.greLocalInetAddr)
                && Objects.equals(greParentIfName, other.greParentIfName)
                && Objects.equals(greRemoteInetAddr, other.greRemoteInetAddr)
                && Objects.equals(greRemoteMacAddr, other.greRemoteMacAddr)
                && Objects.equals(greTunnelName, other.greTunnelName)
                && Objects.equals(vlanIdsInGreTunnel, other.vlanIdsInGreTunnel);
    }

}

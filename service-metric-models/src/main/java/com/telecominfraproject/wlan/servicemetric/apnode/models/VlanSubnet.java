package com.telecominfraproject.wlan.servicemetrics.models;

import java.net.InetAddress;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VlanSubnet extends BaseJsonModel {
	
    private static final long serialVersionUID = 181985919456427954L;

    private int subnetVlan;
    private InetAddress subnetBase;
    private InetAddress subnetMask;
    private InetAddress subnetGateway;
    private InetAddress subnetDhcpServer;
    private InetAddress subnetDns1;
    private InetAddress subnetDns2;
    private InetAddress subnetDns3;

    public VlanSubnet() {
        // serializing
    }

    public VlanSubnet(int subnetVlan, InetAddress subnetBase, InetAddress subnetMask, InetAddress subnetGateway,
            InetAddress subnetDhcpServer, InetAddress subnetDns1, InetAddress subnetDns2, InetAddress subnetDns3) {
        super();
        this.subnetVlan = subnetVlan;
        this.subnetBase = subnetBase;
        this.subnetMask = subnetMask;
        this.subnetGateway = subnetGateway;
        this.subnetDhcpServer = subnetDhcpServer;
        this.subnetDns1 = subnetDns1;
        this.subnetDns2 = subnetDns2;
        this.subnetDns3 = subnetDns3;
    }

    public int getSubnetVlan() {
        return subnetVlan;
    }

    public void setSubnetVlan(int subnetVlan) {
        this.subnetVlan = subnetVlan;
    }

    public InetAddress getSubnetBase() {
        return subnetBase;
    }

    public void setSubnetBase(InetAddress subnetBase) {
        this.subnetBase = subnetBase;
    }

    public InetAddress getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(InetAddress subnetMask) {
        this.subnetMask = subnetMask;
    }

    public InetAddress getSubnetGateway() {
        return subnetGateway;
    }

    public void setSubnetGateway(InetAddress subnetGateway) {
        this.subnetGateway = subnetGateway;
    }

    public InetAddress getSubnetDhcpServer() {
        return subnetDhcpServer;
    }

    public void setSubnetDhcpServer(InetAddress subnetDhcpServer) {
        this.subnetDhcpServer = subnetDhcpServer;
    }

    public InetAddress getSubnetDns1() {
        return subnetDns1;
    }

    public void setSubnetDns1(InetAddress subnetDns1) {
        this.subnetDns1 = subnetDns1;
    }

    public InetAddress getSubnetDns2() {
        return subnetDns2;
    }

    public void setSubnetDns2(InetAddress subnetDns2) {
        this.subnetDns2 = subnetDns2;
    }

    public InetAddress getSubnetDns3() {
        return subnetDns3;
    }

    public void setSubnetDns3(InetAddress subnetDns3) {
        this.subnetDns3 = subnetDns3;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((subnetBase == null) ? 0 : subnetBase.hashCode());
        result = prime * result + ((subnetDhcpServer == null) ? 0 : subnetDhcpServer.hashCode());
        result = prime * result + ((subnetDns1 == null) ? 0 : subnetDns1.hashCode());
        result = prime * result + ((subnetDns2 == null) ? 0 : subnetDns2.hashCode());
        result = prime * result + ((subnetDns3 == null) ? 0 : subnetDns3.hashCode());
        result = prime * result + ((subnetGateway == null) ? 0 : subnetGateway.hashCode());
        result = prime * result + ((subnetMask == null) ? 0 : subnetMask.hashCode());
        result = prime * result + subnetVlan;
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
        VlanSubnet other = (VlanSubnet) obj;
        if (subnetBase == null) {
            if (other.subnetBase != null)
                return false;
        } else if (!subnetBase.equals(other.subnetBase))
            return false;
        if (subnetDhcpServer == null) {
            if (other.subnetDhcpServer != null)
                return false;
        } else if (!subnetDhcpServer.equals(other.subnetDhcpServer))
            return false;
        if (subnetDns1 == null) {
            if (other.subnetDns1 != null)
                return false;
        } else if (!subnetDns1.equals(other.subnetDns1))
            return false;
        if (subnetDns2 == null) {
            if (other.subnetDns2 != null)
                return false;
        } else if (!subnetDns2.equals(other.subnetDns2))
            return false;
        if (subnetDns3 == null) {
            if (other.subnetDns3 != null)
                return false;
        } else if (!subnetDns3.equals(other.subnetDns3))
            return false;
        if (subnetGateway == null) {
            if (other.subnetGateway != null)
                return false;
        } else if (!subnetGateway.equals(other.subnetGateway))
            return false;
        if (subnetMask == null) {
            if (other.subnetMask != null)
                return false;
        } else if (!subnetMask.equals(other.subnetMask))
            return false;
        if (subnetVlan != other.subnetVlan)
            return false;
        return true;
    }

    @Override
    public VlanSubnet clone() {
        return (VlanSubnet) super.clone();
    }
}

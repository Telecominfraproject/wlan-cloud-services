package com.telecominfraproject.wlan.profile.network.models;

import java.net.InetAddress;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.profile.models.common.ManagedFileInfo;

public class RadiusProxyConfiguration extends BaseJsonModel implements PushableConfiguration<RadiusProxyConfiguration> {

    private static final long serialVersionUID = 5859624395353373172L;
    
    private ManagedFileInfo caCert;
    private ManagedFileInfo clientCert;
    private ManagedFileInfo clientKey;
    
    private InetAddress server;
    private Integer port;
    private Set<String> realm;
    private String name;
    private String passphrase;
    private Boolean useRadSec;

    private RadiusProxyConfiguration() {

    }

    public static RadiusProxyConfiguration createWithDefaults() {
        return new RadiusProxyConfiguration();
    }

    public ManagedFileInfo getCaCert() {
        return caCert;
    }

    public void setCaCert(ManagedFileInfo caCert) {
        this.caCert = caCert;
    }

    public ManagedFileInfo getClientCert() {
        return clientCert;
    }

    public void setClientCert(ManagedFileInfo clientCert) {
        this.clientCert = clientCert;
    }

    public ManagedFileInfo getClientKey() {
        return clientKey;
    }

    public void setClientKey(ManagedFileInfo clientKey) {
        this.clientKey = clientKey;
    }

    public InetAddress getServer() {
        return server;
    }

    public void setServer(InetAddress server) {
        this.server = server;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Set<String> getRealm() {
        return realm;
    }

    public void setRealm(Set<String> realm) {
        this.realm = realm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public Boolean getUseRadSec() {
        return useRadSec;
    }

    public void setUseRadSec(Boolean useRadSec) {
        this.useRadSec = useRadSec;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(RadiusProxyConfiguration previousVersion) {       
        return !Objects.deepEquals(this, previousVersion);
    }

    @Override
    public RadiusProxyConfiguration clone() {
        return (RadiusProxyConfiguration) super.clone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(caCert, clientCert, clientKey, name, passphrase, port, realm, server, useRadSec);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RadiusProxyConfiguration other = (RadiusProxyConfiguration) obj;
        return Objects.equals(caCert, other.caCert) && Objects.equals(clientCert, other.clientCert)
                && Objects.equals(clientKey, other.clientKey) && Objects.equals(name, other.name)
                && Objects.equals(passphrase, other.passphrase) && Objects.equals(port, other.port)
                && Objects.equals(realm, other.realm) && Objects.equals(server, other.server)
                && Objects.equals(useRadSec, other.useRadSec);
    }

 
}

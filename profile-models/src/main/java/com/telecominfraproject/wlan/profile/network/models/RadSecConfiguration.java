package com.telecominfraproject.wlan.profile.network.models;

import java.net.InetAddress;

import com.google.common.base.Objects;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.profile.models.common.ManagedFileInfo;

public class RadSecConfiguration extends BaseJsonModel implements PushableConfiguration<RadSecConfiguration> {

    private static final long serialVersionUID = 5859624395353373172L;
    
    private ManagedFileInfo caCert;
    private ManagedFileInfo clientCert;
    private ManagedFileInfo clientKey;
    
    private InetAddress server;
    private Integer port;
    private String realm;
    private String name;
    private String passphrase;

    private RadSecConfiguration() {

    }

    public static RadSecConfiguration createWithDefaults() {
        return new RadSecConfiguration();
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

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
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

    @Override
    public boolean needsToBeUpdatedOnDevice(RadSecConfiguration previousVersion) {       
        return !Objects.equal(this, previousVersion);
    }

    @Override
    public RadSecConfiguration clone() {
        return (RadSecConfiguration) super.clone();
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(caCert, clientCert, clientKey, port, realm, server);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RadSecConfiguration other = (RadSecConfiguration) obj;
        return java.util.Objects.equals(caCert, other.caCert) && java.util.Objects.equals(clientCert, other.clientCert)
                && java.util.Objects.equals(clientKey, other.clientKey) && java.util.Objects.equals(port, other.port)
                && java.util.Objects.equals(realm, other.realm) && java.util.Objects.equals(server, other.server);
    }
    
    
}

package com.telecominfraproject.wlan.profile.network.models;

import java.net.InetAddress;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.profile.models.common.ManagedFileInfo;

public class RadiusProxyConfiguration extends BaseJsonModel implements PushableConfiguration<RadiusProxyConfiguration> {

    // TODO: This should be incorporated into the RadiusProfile
    
    private static final long serialVersionUID = 5859624395353373172L;
    
    private ManagedFileInfo caCert;
    private ManagedFileInfo clientCert;
    private ManagedFileInfo clientKey;
    
    private InetAddress server;
    private Integer port;
    private InetAddress acctServer;
    private Integer acctPort;
    private Set<String> realm;
    private String name; // must be unique, similar to profile name
    private String passphrase; // for decryption of keys
    private Boolean useRadSec;
    private String sharedSecret; // if useRadSec is false
    private String acctSharedSecret; // if useRadSec is false

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

    public InetAddress getAcctServer() {
        return acctServer;
    }

    public void setAcctServer(InetAddress acctServer) {
        this.acctServer = acctServer;
    }

    public Integer getAcctPort() {
        return acctPort;
    }

    public void setAcctPort(Integer acctPort) {
        this.acctPort = acctPort;
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

    public String getSharedSecret() {
        return sharedSecret;
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public String getAcctSharedSecret() {
        return acctSharedSecret;
    }

    public void setAcctSharedSecret(String acctSharedSecret) {
        this.acctSharedSecret = acctSharedSecret;
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
        return Objects.hash(acctPort, acctServer, acctSharedSecret, caCert, clientCert, clientKey, name, passphrase, port, realm, server, sharedSecret,
                useRadSec);
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
        return Objects.equals(acctPort, other.acctPort) && Objects.equals(acctServer, other.acctServer)
                && Objects.equals(acctSharedSecret, other.acctSharedSecret) && Objects.equals(caCert, other.caCert)
                && Objects.equals(clientCert, other.clientCert) && Objects.equals(clientKey, other.clientKey) && Objects.equals(name, other.name)
                && Objects.equals(passphrase, other.passphrase) && Objects.equals(port, other.port) && Objects.equals(realm, other.realm)
                && Objects.equals(server, other.server) && Objects.equals(sharedSecret, other.sharedSecret) && Objects.equals(useRadSec, other.useRadSec);
    }


 
}

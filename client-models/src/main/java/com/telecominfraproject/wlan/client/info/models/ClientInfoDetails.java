package com.telecominfraproject.wlan.client.info.models;

import java.util.Objects;

import com.telecominfraproject.wlan.client.models.ClientDetails;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 * Various settings and attributes provisioned for a client.
 */
public class ClientInfoDetails extends ClientDetails {
    /**
     * String to use when {@link #getApFingerprint()} returns null
     */
    public static final String UNKNOWN_AP_FP_STRING = "Unknown";
    
    private static final long serialVersionUID = -8202572160220431160L;
    private String alias;
    private int clientType;
    private String apFingerprint;
    private String userName;
    private String hostName;
    private String lastUsedCpUsername;
    private String lastUserAgent;
    private boolean doNotSteer;
    private BlocklistDetails blocklistDetails;
    
    public ClientInfoDetails() {
        super();
    }
    
    public ClientInfoDetails(String alias, int clientType, String apFingerprint, String userName, String hostName) {
        super();
        this.alias = alias;
        this.clientType = clientType;
        this.apFingerprint = apFingerprint;
        this.userName = userName;
        this.hostName = hostName;
    }
    
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public int getClientType() {
        return clientType;
    }
    public void setClientType(int clientType) {
        this.clientType = clientType;
    }
    public String getApFingerprint() {
        return apFingerprint;
    }
    public void setApFingerprint(String apFingerprint) {
        this.apFingerprint = apFingerprint;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getHostName() {
        return hostName;
    }
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    
    public BlocklistDetails getBlocklistDetails() {
        return blocklistDetails;
    }

    public void setBlocklistDetails(BlocklistDetails blocklistDetails) {
        this.blocklistDetails = blocklistDetails;
    }


    public String getLastUsedCpUsername() {
        return lastUsedCpUsername;
    }

    public void setLastUsedCpUsername(String lastUsedCpUsername) {
        this.lastUsedCpUsername = lastUsedCpUsername;
    }

    public String getLastUserAgent() {
        return lastUserAgent;
    }

    public void setLastUserAgent(String lastUserAgent) {
        this.lastUserAgent = lastUserAgent;
    }

    public boolean isDoNotSteer() {
		return doNotSteer;
	}

	public void setDoNotSteer(boolean doNotSteer) {
		this.doNotSteer = doNotSteer;
	}

	public boolean mergeDetails(ClientInfoDetails other) {
        boolean changed = false;
        if(other != null) {
            if(other.getAlias() != null && (alias == null || !alias.equals(other.getAlias()))) {
                alias = other.getAlias();
                changed = true;
            }
            if(other.getApFingerprint() != null && (apFingerprint == null || !apFingerprint.equals(other.getApFingerprint()))) {
                apFingerprint = other.getApFingerprint();
                changed = true;
            }
            if(other.getHostName() != null && (hostName == null || !hostName.equals(other.getHostName()))) {
                hostName = other.getHostName();
                changed = true;
            }
            if(other.getUserName() != null && (userName == null || !userName.equals(other.getUserName()))) {
                userName = other.getUserName();
                changed = true;
            }
            if(other.getLastUsedCpUsername() != null && (lastUsedCpUsername == null || !lastUsedCpUsername.equals(other.getLastUsedCpUsername()))) {
                lastUsedCpUsername = other.getLastUsedCpUsername();
                changed = true;
            }
            if(other.getClientType() > 0 &&  clientType != other.getClientType()) {
                clientType = other.clientType;
                changed = true;
            }
            if(other.getBlocklistDetails() != null && (blocklistDetails == null || !blocklistDetails.equals(other.getBlocklistDetails()))) {
                this.blocklistDetails = other.blocklistDetails;
                changed = true;
            }
            if(other.getLastUserAgent() != null && (lastUserAgent == null || !lastUserAgent.equals(other.getLastUserAgent()))) {
                this.lastUserAgent = other.lastUserAgent;
                changed = true;
            }
            if(other.doNotSteer != doNotSteer) {
            	this.doNotSteer = other.doNotSteer;
            	changed = true;
            }
            
                
        }
        return changed;
    }
    
    @Override
	public int hashCode() {
		return Objects.hash(alias, apFingerprint, blocklistDetails, clientType, doNotSteer, hostName,
				lastUsedCpUsername, lastUserAgent, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ClientInfoDetails)) {
			return false;
		}
		ClientInfoDetails other = (ClientInfoDetails) obj;
		return Objects.equals(alias, other.alias) && Objects.equals(apFingerprint, other.apFingerprint)
				&& Objects.equals(blocklistDetails, other.blocklistDetails) && clientType == other.clientType
				&& doNotSteer == other.doNotSteer && Objects.equals(hostName, other.hostName)
				&& Objects.equals(lastUsedCpUsername, other.lastUsedCpUsername)
				&& Objects.equals(lastUserAgent, other.lastUserAgent) && Objects.equals(userName, other.userName);
	}

	@Override
    public ClientInfoDetails clone() {
        ClientInfoDetails ret = (ClientInfoDetails) super.clone();
        if(this.blocklistDetails != null) {
            ret.setBlocklistDetails(blocklistDetails.clone());
        }
        return ret;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (BaseJsonModel.hasUnsupportedValue(blocklistDetails)) {
            return true;
        }
        return false;
    }

}

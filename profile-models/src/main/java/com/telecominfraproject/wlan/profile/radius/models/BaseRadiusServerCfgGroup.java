package com.telecominfraproject.wlan.profile.radius.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;


/**
 * @author yongli
 *
 */
public class BaseRadiusServerCfgGroup extends BaseJsonModel {

    /**
     * 
     */
    private static final long serialVersionUID = -4191681151189055541L;
    /**
     * Maximum number of RADIUS server supported per RADIUS service
     */
    public static final int MAX_SERVER_PER_SERVICE = 3;
    /**
     * Maximum number of RADIUS services per Configuration Group
     */
    public static final int MAX_SERVICE_PER_GROUP = 16;
    /**
     * Map of the name to server list
     */
    private Map<String, List<RadiusServer>> serverMap;

    /**
     * Add a server to existing configuration for a serverName
     * 
     * @param radiusName
     * @param radiusServer
     */
    public void addRadiusServer(String radiusName, RadiusServer radiusServer) {
        List<RadiusServer> serverList = getRadiusServerList(radiusName, true);
        serverList.add(radiusServer);
    }

    /**
     * Add a list of server to existing configuration for a serverName
     * 
     * @param radiusName
     * @param radiusServer
     */
    public void addRadiusServers(String radiusName, List<RadiusServer> radiusServer) {
        List<RadiusServer> serverList = getRadiusServerList(radiusName, true);
        serverList.addAll(radiusServer);
    }

    public BaseRadiusServerCfgGroup clone() {
        BaseRadiusServerCfgGroup result = (BaseRadiusServerCfgGroup) super.clone();
        if (null != serverMap) {
            result.serverMap = new HashMap<>(serverMap.size());
            for (Entry<String, List<RadiusServer>> entry : serverMap.entrySet()) {
                List<RadiusServer> serverList = entry.getValue();
                if (null == serverList) {
                    result.serverMap.put(entry.getKey(), serverList);
                    continue;
                }
                // clone the server list
                List<RadiusServer> newList = new ArrayList<>();
                for (RadiusServer server : serverList) {
                    newList.add(server.clone());
                }
                result.serverMap.put(entry.getKey(), newList);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
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
        if (!(obj instanceof BaseRadiusServerCfgGroup)) {
            return false;
        }
        BaseRadiusServerCfgGroup other = (BaseRadiusServerCfgGroup) obj;
        if (serverMap == null) {
            if (other.serverMap != null) {
                return false;
            }
        } else if (!serverMap.equals(other.serverMap)) {
            return false;
        }
        return true;
    }

    /**
     * Find the server configuration for a RADIUS name
     * 
     * @param radiusName
     * @return empty list if not configured
     */

    public List<RadiusServer> findServerConfiguration(String radiusName) {
        return getRadiusServerList(radiusName, false);
    }

    /**
     * Get the List of Radius Server configuration for a given radiusName. If
     * createIfNotFound is true, then a new list is created if it's exiting
     * mapping is not found.
     * 
     * @param radiusName
     * @param createIfNotFound
     * @return the list found; empty or created list it not found.
     */
    private List<RadiusServer> getRadiusServerList(String radiusName, boolean createIfNotFound) {
        List<RadiusServer> result = null;
        if (null == this.serverMap) {
            if (!createIfNotFound) {
                return Collections.emptyList();
            }
            this.serverMap = new HashMap<>();
        } else {
            result = this.serverMap.get(radiusName);
        }

        if (null == result) {
            if (createIfNotFound) {
                result = new ArrayList<>();
                serverMap.put(radiusName, result);
            } else {
                result = Collections.emptyList();
            }
        }
        return result;
    }

    /**
     * Use by JSON only
     * 
     * @return map
     */
    @Deprecated
    public Map<String, List<RadiusServer>> getServerMap() {
        return serverMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((serverMap == null) ? 0 : serverMap.hashCode());
        return result;
    }

    /**
     * Return the all entry in the map
     * 
     * @return entrySet
     */
    public Set<Entry<String, List<RadiusServer>>> MapEntrySet() {
        if (null != serverMap) {
            return serverMap.entrySet();
        }
        return Collections.emptySet();
    }

    /**
     * Number of the entry in the map
     * 
     * @return size
     */
    public int mapSize() {
        if (null != serverMap) {
            return serverMap.size();
        }
        return 0;
    }

    /**
     * Used by Json only
     * 
     * @param serverMap
     */
    @Deprecated
    public void setServerMap(Map<String, List<RadiusServer>> serverMap) {
        this.serverMap = serverMap;
    }

    /**
     * Test if the configuration is valid.
     * 
     * @throws ConfigurationException
     *             if it's invalid.
     */
    public void validateConfig() {
        if ((null == serverMap) || serverMap.isEmpty()) {
            // empty map
            throw new ConfigurationException("missing RADIUS service");
        }

        if (serverMap.size() > MAX_SERVICE_PER_GROUP) {
            throw new ConfigurationException("too many (" + serverMap.size()
                    + " services configured and maximum allowed is " + MAX_SERVICE_PER_GROUP);
        }
        Set<String> usedServersForRegion = new HashSet<>();
        for (Entry<String, List<RadiusServer>> entry : serverMap.entrySet()) {
            // verify each radius service
            List<RadiusServer> serverList = entry.getValue();
            if ((null == serverList) || serverList.isEmpty()) {
                throw new ConfigurationException("no RADIUS server configured for service " + entry.getKey());
            }
            if (serverList.size() > MAX_SERVER_PER_SERVICE) {
                throw new ConfigurationException(
                        "too many (" + serverList.size() + ") RADIUS servers configured for service " + entry.getKey()
                                + " and maximum allowed is " + MAX_SERVER_PER_SERVICE);
            }
            try {
                for (RadiusServer server : serverList) {
                    server.validateConfig();
                    if (usedServersForRegion.contains(server.getIpAddress().getHostAddress())) {
                        throw new ConfigurationException("Duplicate RADIUS Server address " + server.getIpAddress().getHostAddress());
                    }
                    usedServersForRegion.add(server.getIpAddress().getHostAddress());
                }
            } catch (ConfigurationException e) {
                throw new ConfigurationException("invalid RADIUS server configure for service " + entry.getKey() + ", "
                        + e.getLocalizedMessage(), e);
            }
        }
    }
}

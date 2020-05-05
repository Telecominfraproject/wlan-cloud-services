package com.telecominfraproject.wlan.profile.radius.models;

import java.net.InetAddress;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

/**
 * Base subnet configuration with address and CIDR prefix
 * 
 * @author yongli
 *
 */
public class BaseSubnetConfiguration extends BaseJsonModel {

    private static final long serialVersionUID = 5767615077868666696L;

    public static final Integer MAX_CIDR_PREFIX_VALUE = 32;

    public static final Integer MIN_CIDR_PREFIX_VALUE = 1;

    /**
     * Subnet Address.
     */
    private InetAddress subnetAddress;

    /**
     * Subnet CIDR prefix
     */
    private Integer subnetCidrPrefix;

    /**
     * Hide constructor
     */
    protected BaseSubnetConfiguration() {

    }

    public BaseSubnetConfiguration(BaseSubnetConfiguration other) {
        this.subnetAddress = other.subnetAddress;
        this.subnetCidrPrefix = other.subnetCidrPrefix;
    }

    public BaseSubnetConfiguration clone() {
        return (BaseSubnetConfiguration) super.clone();
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
        if (!(obj instanceof BaseSubnetConfiguration)) {
            return false;
        }
        BaseSubnetConfiguration other = (BaseSubnetConfiguration) obj;
        if (subnetAddress == null) {
            if (other.subnetAddress != null) {
                return false;
            }
        } else if (!subnetAddress.equals(other.subnetAddress)) {
            return false;
        }
        if (subnetCidrPrefix == null) {
            if (other.subnetCidrPrefix != null) {
                return false;
            }
        } else if (!subnetCidrPrefix.equals(other.subnetCidrPrefix)) {
            return false;
        }
        return true;
    }

    /**
     * @return the subnetAddress
     */
    public InetAddress getSubnetAddress() {
        return subnetAddress;
    }

    /**
     * @return the subnetCidrPrefix
     */
    public Integer getSubnetCidrPrefix() {
        return subnetCidrPrefix;
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
        result = prime * result + ((subnetAddress == null) ? 0 : subnetAddress.hashCode());
        result = prime * result + ((subnetCidrPrefix == null) ? 0 : subnetCidrPrefix.hashCode());
        return result;
    }

    /**
     * @param subnetAddress
     *            the subnetAddress to set
     */
    public void setSubnetAddress(InetAddress subnetAddress) {
        this.subnetAddress = subnetAddress;
    }

    /**
     * @param subnetCidrPrefix
     *            the subnetCidrPrefix to set
     */
    public void setSubnetCidrPrefix(Integer subnetCidrPrefix) {
        this.subnetCidrPrefix = subnetCidrPrefix;
    }

    /**
     * Invalidate the configuration.
     * 
     * @throws ConfigurationException
     */
    public void validateConfig() {
        if ((null == subnetAddress) || subnetAddress.isAnyLocalAddress() || subnetAddress.isLoopbackAddress()) {
            throw new ConfigurationException("invalid subnet address " + subnetAddress);
        }
        if ((null == subnetCidrPrefix) || (subnetCidrPrefix < MIN_CIDR_PREFIX_VALUE)
                || (subnetCidrPrefix > MAX_CIDR_PREFIX_VALUE)) {
            throw new ConfigurationException("invalid subnet cidr prefix value " + subnetCidrPrefix);
        }
    }

    /**
     * Check if the two subnet configuration overlap. Both must be validated
     * before.
     * 
     * @param other
     * @return true if they overlap
     */
    public boolean overlapSubnet(BaseSubnetConfiguration other) {
        if (equals(other)) {
            return true;
        }
        byte[] min = getAddressRange(this.subnetAddress, this.subnetCidrPrefix, true);
        byte[] max = getAddressRange(this.subnetAddress, this.subnetCidrPrefix, false);
        byte[] otherMin = getAddressRange(other.subnetAddress, other.subnetCidrPrefix, true);
        byte[] otherMax = getAddressRange(other.subnetAddress, other.subnetCidrPrefix, false);

        if (compareAddress(min, otherMax) > 0) {
            return false;
        }

        if (compareAddress(max, otherMin) < 0) {
            return false;
        }
        return true;
    }

    /**
     * Compare two IP address in byte[] format
     * 
     * @param left
     * @param right
     * @return
     */
    private static int compareAddress(byte[] left, byte[] right) {
        int i = 0;
        for (; i < Math.min(left.length, right.length); ++i) {
            int l = (0x00ff & left[i]);
            int r = (0x00ff & right[i]);
            int result = l - r;
            if (0 != result) {
                return result;
            }
        }
        if (i < left.length) {
            // left has more bytes
            return 1;
        }
        if (i < right.length) {
            // right has more bytes
            return -1;
        }

        return 0;
    }

    /**
     * Get the byte[] for a subnet address and prefix.
     * 
     * @param address
     * @param prefix
     * @param isStart
     * @return starting address if isStart is set to true, otherwise return the
     *         ending address
     */
    private static byte[] getAddressRange(InetAddress address, Integer prefix, boolean isStart) {
        if ((null == address) || (null == prefix)) {
            return null;
        }
        byte[] result = address.getAddress();
        for (int i = result.length - 1; (i >= 0) && (i >= (prefix / 8)); --i) {
            if (8 * i >= prefix) {
                result[i] = isStart ? 0 : (byte) 0xff;
            } else {
                byte mask = 0;
                for (int j = 0; j < prefix % 8; ++j) {
                    mask |= 0b1 << j;
                }
                if (isStart) {
                    result[i] &= (~mask);
                } else {
                    result[i] |= mask;
                }
            }
        }
        return result;
    }
}

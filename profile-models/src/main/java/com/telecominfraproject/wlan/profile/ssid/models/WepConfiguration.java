package com.telecominfraproject.wlan.profile.ssid.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;
import com.telecominfraproject.wlan.profile.ssid.models.WepKey.WepType;

/**
 * @author mpreston
 *
 */
public class WepConfiguration extends BaseJsonModel {
    private static final long serialVersionUID = 3242080974554729643L;
    public static final int DEFAULT_PRIMARY_TX_KEY_ID = 1;
    public static final int NUM_TX_KEYS = 4;

    private int primaryTxKeyId;
    private WepKey[] wepKeys;

    private WepAuthType wepAuthType;

    public static WepConfiguration createWithDefaults() {
        WepConfiguration result = new WepConfiguration();
        result.primaryTxKeyId = DEFAULT_PRIMARY_TX_KEY_ID;
        result.wepAuthType = WepAuthType.open;
        result.wepKeys = new WepKey[4];
        for (int i = 0; i < result.wepKeys.length; i++) {
            result.wepKeys[i] = WepKey.createWithDefaults();
        }

        return result;
    }

    public static enum WepAuthType {

        open(0L), shared(1L),

        UNSUPPORTED(-1L);

        private final long id;
        private static final Map<Long, WepAuthType> ELEMENTS = new HashMap<>();

        private WepAuthType(long id) {
            this.id = id;
        }

        public long getId() {
            return this.id;
        }

        public static WepAuthType getById(long enumId) {
            if (ELEMENTS.isEmpty()) {
                synchronized (ELEMENTS) {
                    if (ELEMENTS.isEmpty()) {
                        // initialize elements map
                        for (WepAuthType wat : WepAuthType.values()) {
                            ELEMENTS.put(wat.getId(), wat);
                        }
                    }
                }
            }
            return ELEMENTS.get(enumId);
        }

        @JsonCreator
        public static WepAuthType getByName(String value) {
            return JsonDeserializationUtils.deserializEnum(value, WepAuthType.class, UNSUPPORTED);
        }

        public static boolean isUnsupported(WepAuthType value) {
            return UNSUPPORTED.equals(value);
        }
    }

    public WepConfiguration() {

    }

    @Override
    public WepConfiguration clone() {
        WepConfiguration result = (WepConfiguration) super.clone();

        if (wepKeys != null) {
            result.wepKeys = new WepKey[wepKeys.length];
            for (int i = 0; i < wepKeys.length; i++) {
                result.setWepKeyAtIndex(wepKeys[i].clone(), i);
            }
        }

        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (wepKeys != null) {
            for (int i = 0; i < NUM_TX_KEYS; i++) {
                if (WepType.UNSUPPORTED.equals(wepKeys[i].getTxKeyType())) {
                    return true;
                }
            }
        }

        if (WepAuthType.isUnsupported(wepAuthType)) {
            return true;
        }

        return false;
    }

    public int getPrimaryTxKeyId() {
        return primaryTxKeyId;
    }

    public void setPrimaryTxKeyId(int primaryTxKeyId) {
        this.primaryTxKeyId = primaryTxKeyId;
    }

    public WepAuthType getWepAuthType() {
        return wepAuthType;
    }

    public void setWepAuthType(WepAuthType wepAuthType) {
        this.wepAuthType = wepAuthType;
    }

    public WepKey[] getWepKeys() {
        return wepKeys;
    }

    public void setWepKeys(WepKey[] wepKeys) {
        this.wepKeys = wepKeys;
    }

    public void setWepKeyAtIndex(WepKey key, int index) {
        if (key == null) {
            throw new IllegalArgumentException("Unable to store a null Wep Key.");
        }

        if (wepKeys == null) {
            wepKeys = new WepKey[4];
        }

        key.validateWepKey();
        if (index < 0 || index >= wepKeys.length) {
            throw new IllegalArgumentException("Unable to store a Wep Key at index " + index + " Index must be 0 - 3.");
        }

        // Now we know the key is valid, we set it at the proper index. For the
        // current
        // implementation, we actually copy the key to all 4 slots. This may
        // change in
        // the future. If it does, reomve this loop and instead just use the
        // single index assignment
        // code below the loop.
        for (int i = 0; i < wepKeys.length; i++) {
            wepKeys[i] = key;
        }

        /**
         * TODO - Uncomment this if we ever move to a wep model where we do not
         * just copy the same key to all indices.
         * 
         * wepKeys[index] = key;
         */
    }

    public void setWepKeyAtIndex(String keyValue, WepType keyType, int index) {
        setWepKeyAtIndex(new WepKey(keyValue, keyType), index);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + primaryTxKeyId;
        result = prime * result + ((wepAuthType == null) ? 0 : wepAuthType.hashCode());
        result = prime * result + Arrays.hashCode(wepKeys);
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
        WepConfiguration other = (WepConfiguration) obj;
        if (primaryTxKeyId != other.primaryTxKeyId)
            return false;
        if (wepAuthType != other.wepAuthType)
            return false;
        if (!Arrays.equals(wepKeys, other.wepKeys))
            return false;
        return true;
    }

}

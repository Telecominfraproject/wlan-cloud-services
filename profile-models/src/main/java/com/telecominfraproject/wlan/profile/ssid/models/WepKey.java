package com.telecominfraproject.wlan.profile.ssid.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * @author mpreston
 *
 */
public class WepKey extends BaseJsonModel {
    private static final long serialVersionUID = -8652320382914257445L;
    private static final List<Character> hexCharList = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F');
    private static final List<Character> asciiCharList = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
    
    private static final String DEFAULT_KEY_STR = "hello";
    
    public static WepKey createWithDefaults() {
        WepKey result = new WepKey();
        result.setTxKeyType(WepType.wep64);
        result.setTxKey(DEFAULT_KEY_STR);
        result.setTxKeyConverted(convertAsciiKeyToHex(DEFAULT_KEY_STR));
        return result;
    }
    
    private String txKey;
    private String txKeyConverted;
    private WepType txKeyType;
    
    public static enum WepType {

        wep64(0L),
        wep128(1L),
        
        UNSUPPORTED (-1L);

        private final long id;
        private static final Map<Long, WepType> ELEMENTS = new HashMap<>();

        private WepType(long id) {
            this.id = id;
        }

        public long getId() {
            return this.id;
        }

        public static WepType getById(long enumId) {
            if (ELEMENTS.isEmpty()) {
                synchronized (ELEMENTS) {
                    if (ELEMENTS.isEmpty()) {
                        //initialize elements map
                        for(WepType wt : WepType.values()) {
                            ELEMENTS.put(wt.getId(), wt);
                        }
                    }
                }
            }
            return ELEMENTS.get(enumId);
        }
        
        @JsonCreator
        public static WepType getByName(String value) {
            return JsonDeserializationUtils.deserializEnum(value, WepType.class, UNSUPPORTED);
        }
        
        public static boolean isUnsupported(WepType value) {
            return UNSUPPORTED.equals(value);
        }
    }
    
    private WepKey() {
        
    }
    
    public WepKey(String key, WepType type) {
        txKeyType = type;
        txKey = key;
        validateWepKey();
    }
    
    @Override
    public WepKey clone() {
        return (WepKey) super.clone();
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (WepType.isUnsupported(txKeyType)) {
            return true;
        }
        return false;
    }

    public String getTxKey() {
        return txKey;
    }

    public void setTxKey(String txKey) {
        this.txKey = txKey;
    }
    
    public WepType getTxKeyType() {
        return txKeyType;
    }

    public void setTxKeyType(WepType txKeyType) {
        this.txKeyType = txKeyType;
    }

    public String getTxKeyConverted() {
        return txKeyConverted;
    }

    public void setTxKeyConverted(String txKeyConverted) {
        this.txKeyConverted = txKeyConverted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((txKey == null) ? 0 : txKey.hashCode());
        result = prime * result + ((txKeyType == null) ? 0 : txKeyType.hashCode());
        result = prime * result + ((txKeyConverted == null) ? 0 : txKeyConverted.hashCode());
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
        WepKey other = (WepKey) obj;
        if (txKey == null) {
            if (other.txKey != null)
                return false;
        } else if (!txKey.equals(other.txKey))
            return false;
        if (txKeyType != other.txKeyType)
            return false;
        if (txKeyConverted == null) {
            if (other.txKeyConverted != null)
                return false;
        } else if (!txKeyConverted.equals(other.txKeyConverted))
            return false;
        return true;
    }

    public void validateWepKey() {
        
        if (getTxKey() == null) {
            throw new InvalidWepKeyFormatException("Unable to store a null WEP Tx Key value.");
        }
        
        int keyLength = txKey.length();
        
        if (keyLength <=0) {
            throw new InvalidWepKeyFormatException("Unable to store an empty WEP Tx Key value.");
        } else if (keyLength == 5) {
            if (WepType.wep64.equals(txKeyType)) {
                validateAsciiKey(txKey);
                this.txKeyConverted = convertAsciiKeyToHex(this.txKey);
            } else {
                throw new InvalidWepKeyFormatException("Invalid WEP Tx Key. Tx key length and Wep Type do not match");
            }
        } else if (keyLength == 10) {
            if (WepType.wep64.equals(txKeyType)) {
                validateHexKey(txKey);
                this.txKeyConverted = this.txKey;
            } else {
                throw new InvalidWepKeyFormatException("Invalid WEP Tx Key. Tx key length and Wep Type do not match");
            }
        } else if (keyLength == 13) {
            if (WepType.wep128.equals(txKeyType)) {
                validateAsciiKey(txKey);
                this.txKeyConverted = convertAsciiKeyToHex(this.txKey);
            } else {
                throw new InvalidWepKeyFormatException("Invalid WEP Tx Key. Tx key length and Wep Type do not match");
            }
        } else if (keyLength == 26) {
            if (WepType.wep128.equals(txKeyType)) {
                validateHexKey(txKey);
                this.txKeyConverted = this.txKey;
            } else {
                throw new InvalidWepKeyFormatException("Invalid WEP Tx Key. Tx key length and Wep Type do not match");
            }
        } else {
            throw new InvalidWepKeyFormatException("Passed in Wep Tx key " + txKey + " is invalid." +
                    " Please provide an ASCII key with length of 5 or 13 chars, or a HEX key with length 10 or 26 chars.");
        }
    }
    
    public static String convertAsciiKeyToHex(String ascii)
    {
        char[] chars = ascii.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }
    
    private void validateAsciiKey(String key) {
        for (Character symbol : key.toCharArray()) {
            if (!asciiCharList.contains(symbol)) {
                throw new InvalidWepKeyFormatException("Invalid WEP Tx Key. Invalid character for ASCII based key: " + symbol);
            }
        }
    }
    
    private void validateHexKey(String key) {
        for (Character symbol : key.toCharArray()) {
            if (!hexCharList.contains(symbol)) {
                throw new InvalidWepKeyFormatException("Invalid WEP Tx Key. Invalid character for HEX based key: " + symbol);
            }
        }
    }
    
    private class InvalidWepKeyFormatException extends RuntimeException {
        private static final long serialVersionUID = 8655780666924810260L;

        public InvalidWepKeyFormatException(String msg) {
            super(msg);
        }
    }
    
   
}

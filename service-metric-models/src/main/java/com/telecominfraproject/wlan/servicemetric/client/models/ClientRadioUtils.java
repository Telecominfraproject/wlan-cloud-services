package com.telecominfraproject.wlan.servicemetric.client.models;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.equipment.ChannelBandwidth;
import com.telecominfraproject.wlan.core.model.equipment.GuardInterval;

/**
 * Utility Routines for Client Radio
 * 
 * @author yongli
 *
 */
public final class ClientRadioUtils {

    private static final long KBITS_IN_MBITS = 1000L;

    static class PhyRateEntry {

        enum ValueType {
            kbs20MhzLGI, kbs20MhzSGI, kbs40MhzLGI, kbs40MhzSGI, kbs80MhzLGI, kbs80MhzSGI, kbs160MhzLGI, kbs160MhzSGI,
            // always keep last
            Unknown;

            public static ValueType getValueType(ChannelBandwidth bandwidth, GuardInterval guardInterval) {
                ValueType result = Unknown;
                switch (guardInterval) {
                case SGI:
                    switch (bandwidth) {
                    case is20MHz:
                        result = kbs20MhzSGI;
                        break;
                    case is40MHz:
                        result = kbs40MhzSGI;
                        break;
                    case is80MHz:
                        result = kbs80MhzSGI;
                        break;
                    case is160MHz:
                        result = ValueType.kbs160MhzSGI;
                        break;
                    default:
                        break;
                    }
                    break;
                case LGI:
                    switch (bandwidth) {
                    case is20MHz:
                        result = kbs20MhzLGI;
                        break;
                    case is40MHz:
                        result = kbs40MhzLGI;
                        break;
                    case is80MHz:
                        result = kbs80MhzLGI;
                        break;
                    case is160MHz:
                        result = ValueType.kbs160MhzLGI;
                        break;
                    default:
                        break;
                    }
                    break;
                default:
                    break;
                }
                return result;
            }
        }

        /**
         * physical rate in kilobits per second, indexed by ValueType. Unknown
         * or unsupported should map to null.
         */
        private Long[] phyRate;

        public PhyRateEntry(Long[] phyRate) {
            this.phyRate = phyRate;
        }

        /**
         * Get the physical rate in kilobits per second
         * 
         * @param bandwidth
         * @param guardInterval
         * @return null if not supported or unknown
         */
        public Long getPhyRate(ChannelBandwidth bandwidth, GuardInterval guardInterval) {
            ValueType vType = ValueType.getValueType(bandwidth, guardInterval);
            if (null != phyRate && phyRate.length > vType.ordinal()) {
                return phyRate[vType.ordinal()];
            }
            return null;
        }
    }

    /**
     * Hide constructor
     */
    private ClientRadioUtils() {

    }
}

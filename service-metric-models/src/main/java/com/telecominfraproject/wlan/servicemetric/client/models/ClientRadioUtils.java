package com.telecominfraproject.wlan.servicemetric.client.models;

import java.util.EnumMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.models.McsType;
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

    static final Map<McsType, PhyRateEntry> phyRateEntryMap;
    
    //some vendors report truncated rates, so in order to be able to process them we'll maintain truncated rates as well
    static final Map<McsType, PhyRateEntry> truncatedPhyRateEntryMap;
    //some vendors report rounded-up rates, so in order to be able to process them we'll maintain those rates as well
    static final Map<McsType, PhyRateEntry> roundedPhyRateEntryMap;

    static {
        phyRateEntryMap = new EnumMap<>(McsType.class);

        // Populate values as 20Mzh LGI, 20Mhz SGI, 40Mhz LGI, 40Mhz LGI, 80 Mzh
        // LGI, 80 Mhz SIG, 160Mhz LGI, 160Mhz SGI

        // legacy
        phyRateEntryMap.put(McsType.MCS_1, new PhyRateEntry(new Long[] { M2Kbps(1), M2Kbps(1) })); // 2.4
        phyRateEntryMap.put(McsType.MCS_2, new PhyRateEntry(new Long[] { M2Kbps(2), M2Kbps(2) })); // 2.4
        phyRateEntryMap.put(McsType.MCS_5dot5, new PhyRateEntry(new Long[] { M2Kbps(55, 10), M2Kbps(55, 10) }));// 2.4
        phyRateEntryMap.put(McsType.MCS_6, new PhyRateEntry(new Long[] { M2Kbps(6), M2Kbps(6), M2Kbps(6), M2Kbps(6) }));
        phyRateEntryMap.put(McsType.MCS_9, new PhyRateEntry(new Long[] { M2Kbps(9), M2Kbps(9), M2Kbps(9), M2Kbps(9) }));
        phyRateEntryMap.put(McsType.MCS_11, new PhyRateEntry(new Long[] { M2Kbps(11), M2Kbps(11) })); // 2.4
        phyRateEntryMap.put(McsType.MCS_12,
                new PhyRateEntry(new Long[] { M2Kbps(12), M2Kbps(12), M2Kbps(12), M2Kbps(12) }));
        phyRateEntryMap.put(McsType.MCS_18,
                new PhyRateEntry(new Long[] { M2Kbps(18), M2Kbps(18), M2Kbps(18), M2Kbps(18) }));
        phyRateEntryMap.put(McsType.MCS_24,
                new PhyRateEntry(new Long[] { M2Kbps(24), M2Kbps(24), M2Kbps(24), M2Kbps(24) }));
        phyRateEntryMap.put(McsType.MCS_36,
                new PhyRateEntry(new Long[] { M2Kbps(36), M2Kbps(36), M2Kbps(36), M2Kbps(36) }));
        phyRateEntryMap.put(McsType.MCS_48,
                new PhyRateEntry(new Long[] { M2Kbps(48), M2Kbps(48), M2Kbps(48), M2Kbps(48) }));
        phyRateEntryMap.put(McsType.MCS_54,
                new PhyRateEntry(new Long[] { M2Kbps(54), M2Kbps(54), M2Kbps(54), M2Kbps(54) }));

        // see
        // http://www.wlanpros.com/wp-content/uploads/2015/06/Screenshot-2014-09-28-10.11.56-1024x837.png
        // HT MCS 0: 6.5, 7.2, 13.5, 15
        phyRateEntryMap.put(McsType.MCS_N_0,
                new PhyRateEntry(new Long[] { M2Kbps(65, 10), M2Kbps(72, 10), M2Kbps(135, 10), M2Kbps(15) }));
        // HT MCS 1: 13, 14.4 27, 30
        phyRateEntryMap.put(McsType.MCS_N_1,
                new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(144, 10), M2Kbps(27), M2Kbps(30) }));
        // HT MCS 2: 19.5, 21.7, 40.5, 45
        phyRateEntryMap.put(McsType.MCS_N_2,
                new PhyRateEntry(new Long[] { M2Kbps(195, 10), M2Kbps(217, 10), M2Kbps(405, 10), M2Kbps(45) }));
        // HT MCS 3: 26, 28.9, 54, 60
        phyRateEntryMap.put(McsType.MCS_N_3,
                new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(289, 10), M2Kbps(54), M2Kbps(60) }));
        // HT MCS 4: 39, 43.3, 81, 90
        phyRateEntryMap.put(McsType.MCS_N_4,
                new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(433, 10), M2Kbps(81), M2Kbps(90) }));
        // HT MCS 5: 52, 57.8, 108, 120
        phyRateEntryMap.put(McsType.MCS_N_5,
                new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(578, 10), M2Kbps(108), M2Kbps(120) }));
        // HT MCS 6: 58.5, 65, 121.5, 135
        phyRateEntryMap.put(McsType.MCS_N_6,
                new PhyRateEntry(new Long[] { M2Kbps(585, 10), M2Kbps(65), M2Kbps(1215, 10), M2Kbps(135) }));
        // HT MCS 7: 65, 72.2, 135, 150
        phyRateEntryMap.put(McsType.MCS_N_7,
                new PhyRateEntry(new Long[] { M2Kbps(65), M2Kbps(722, 10), M2Kbps(135), M2Kbps(150) }));
        // HT MCS 8: 13, 14.4, 27, 30
        phyRateEntryMap.put(McsType.MCS_N_8,
                new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(144, 10), M2Kbps(27), M2Kbps(30) }));
        // HT MCS 9: 26, 28.9, 54, 60
        phyRateEntryMap.put(McsType.MCS_N_9,
                new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(289, 10), M2Kbps(54), M2Kbps(60) }));
        // HT MCS 10: 39, 43.3, 81, 90
        phyRateEntryMap.put(McsType.MCS_N_10,
                new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(433, 10), M2Kbps(81), M2Kbps(90) }));
        // HT MCS 11: 52, 57.8, 108, 120
        phyRateEntryMap.put(McsType.MCS_N_11,
                new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(578, 10), M2Kbps(108), M2Kbps(120) }));
        // HT MCS 12: 78, 86.7, 162, 180
        phyRateEntryMap.put(McsType.MCS_N_12,
                new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(867, 10), M2Kbps(162), M2Kbps(180) }));
        // HT MCS 13: 104, 115.6, 216, 240
        phyRateEntryMap.put(McsType.MCS_N_13,
                new PhyRateEntry(new Long[] { M2Kbps(104), M2Kbps(1156, 10), M2Kbps(216), M2Kbps(240) }));
        // HT MCS 14: 117, 130.3, 243, 270
        phyRateEntryMap.put(McsType.MCS_N_14,
                new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(1303, 10), M2Kbps(243), M2Kbps(270) }));
        // HT MCS 15: 130, 144.4, 270, 300
        phyRateEntryMap.put(McsType.MCS_N_15,
                new PhyRateEntry(new Long[] { M2Kbps(130), M2Kbps(1444, 10), M2Kbps(270), M2Kbps(300) }));
        
        // HT MCS 16-23 with 3 Spatial Streams is unsupported by our AP right now,
        // HT MCS 16: 19.5  21.7    40.5    45
        phyRateEntryMap.put(McsType.MCS_N_16,
                new PhyRateEntry(new Long[] { M2Kbps(195,10), M2Kbps(217, 10), M2Kbps(405, 10), M2Kbps(45) }));
        
        //https://en.wikipedia.org/wiki/IEEE_802.11n-2009#Data_rates
        // HT MCS 17  3   QPSK    1/2 39  43.3    81  90
        phyRateEntryMap.put(McsType.MCS_N_17,
                new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(433, 10), M2Kbps(81), M2Kbps(90) }));
        
        // HT MCS 18  3   QPSK    3/4 58.5    65  121.5   135
        phyRateEntryMap.put(McsType.MCS_N_18,
                new PhyRateEntry(new Long[] { M2Kbps(585, 10), M2Kbps(65), M2Kbps(1215, 10), M2Kbps(135) }));
        
        // HT MCS 19  3   16-QAM  1/2 78  86.7    162 180
        phyRateEntryMap.put(McsType.MCS_N_19,
                new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(867, 10), M2Kbps(162), M2Kbps(180) }));
        
        // HT MCS 20  3   16-QAM  3/4 117 130 243 270
        phyRateEntryMap.put(McsType.MCS_N_20,
                new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(130), M2Kbps(243), M2Kbps(270) }));
        
        // HT MCS 21  3   64-QAM  2/3 156 173.3   324 360
        phyRateEntryMap.put(McsType.MCS_N_21,
                new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(1733, 10), M2Kbps(324), M2Kbps(360) }));

        // HT MCS 22  3   64-QAM  3/4 175.5   195 364.5   405
        phyRateEntryMap.put(McsType.MCS_N_22,
                new PhyRateEntry(new Long[] { M2Kbps(1755, 10), M2Kbps(195), M2Kbps(3645, 10), M2Kbps(405) }));

        // HT MCS 23  3   64-QAM  5/6 195 216.7   405 450
        phyRateEntryMap.put(McsType.MCS_N_23,
                new PhyRateEntry(new Long[] { M2Kbps(195), M2Kbps(2167, 10), M2Kbps(405), M2Kbps(450) }));

        // HT MCS 24  4   BPSK    1/2 26  28.8    54  60
        phyRateEntryMap.put(McsType.MCS_N_24,
                new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(288, 10), M2Kbps(54), M2Kbps(60) }));

        // HT MCS 25  4   QPSK    1/2 52  57.6    108 120
        phyRateEntryMap.put(McsType.MCS_N_25,
                new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(576, 10), M2Kbps(108), M2Kbps(120) }));

        // HT MCS 26  4   QPSK    3/4 78  86.8    162 180
        phyRateEntryMap.put(McsType.MCS_N_26,
                new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(868, 10), M2Kbps(162), M2Kbps(180) }));

        // HT MCS 27  4   16-QAM  1/2 104 115.6   216 240
        phyRateEntryMap.put(McsType.MCS_N_27,
                new PhyRateEntry(new Long[] { M2Kbps(104), M2Kbps(1156, 10), M2Kbps(216), M2Kbps(240) }));

        // HT MCS 28  4   16-QAM  3/4 156 173.2   324 360
        phyRateEntryMap.put(McsType.MCS_N_28,
                new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(1732, 10), M2Kbps(324), M2Kbps(360) }));

        // HT MCS 29  4   64-QAM  2/3 208 231.2   432 480
        phyRateEntryMap.put(McsType.MCS_N_29,
                new PhyRateEntry(new Long[] { M2Kbps(208), M2Kbps(2312, 10), M2Kbps(432), M2Kbps(480) }));

        // HT MCS 30  4   64-QAM  3/4 234 260 486 540
        phyRateEntryMap.put(McsType.MCS_N_30,
                new PhyRateEntry(new Long[] { M2Kbps(234), M2Kbps(260), M2Kbps(486), M2Kbps(540) }));

        // HT MCS 31  4   64-QAM  5/6 260 288.8   540 600
        phyRateEntryMap.put(McsType.MCS_N_31,
                new PhyRateEntry(new Long[] { M2Kbps(260), M2Kbps(2888, 10), M2Kbps(540), M2Kbps(600) }));



        // VHT MCS 0 (SS=1): 6.5, 7.2, 13.5, 15, 29.3, 32.5, 58.5, 65
        phyRateEntryMap.put(McsType.MCS_AC_1x1_0, new PhyRateEntry(new Long[] { M2Kbps(65, 10), M2Kbps(72, 10),
                M2Kbps(135, 10), M2Kbps(15), M2Kbps(293, 10), M2Kbps(325, 10), M2Kbps(585, 10), M2Kbps(65) }));
        // VHT MCS 1 (SS=1): 13, 14.4, 27, 30, 58.5, 65, 117, 130
        phyRateEntryMap.put(McsType.MCS_AC_1x1_1, new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(144, 10), M2Kbps(27),
                M2Kbps(30), M2Kbps(585, 10), M2Kbps(65), M2Kbps(117), M2Kbps(130) }));
        // VHT MCS 2 (SS=1): 19.5, 21.7, 40.5, 45, 87.8, 97.5, 175.5, 195
        phyRateEntryMap.put(McsType.MCS_AC_1x1_2, new PhyRateEntry(new Long[] { M2Kbps(195, 10), M2Kbps(217, 10),
                M2Kbps(405, 10), M2Kbps(45), M2Kbps(878, 10), M2Kbps(975, 10), M2Kbps(1755, 10), M2Kbps(195) }));
        // VHT MCS 3 (SS=1): 26, 28.9 54, 60, 117, 130, 234, 260
        phyRateEntryMap.put(McsType.MCS_AC_1x1_3, new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(289, 10), M2Kbps(54),
                M2Kbps(60), M2Kbps(117), M2Kbps(130), M2Kbps(234), M2Kbps(260) }));
        // VHT MCS 4 (SS=1): 39, 43.3, 81, 90, 175.5 195, 351, 390
        phyRateEntryMap.put(McsType.MCS_AC_1x1_4, new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(433, 10), M2Kbps(81),
                M2Kbps(90), M2Kbps(1755, 10), M2Kbps(195), M2Kbps(351), M2Kbps(390) }));
        // VHT MCS 5 (SS=1): 52, 57.8, 108, 120, 234, 260, 468, 520
        phyRateEntryMap.put(McsType.MCS_AC_1x1_5, new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(578, 10),
                M2Kbps(108), M2Kbps(120), M2Kbps(234), M2Kbps(260), M2Kbps(468), M2Kbps(520) }));
        // VHT MCS 6 (SS=1): 58.5, 65, 121.5, 135, 263.3, 292.5, 526.5, 585
        phyRateEntryMap.put(McsType.MCS_AC_1x1_6, new PhyRateEntry(new Long[] { M2Kbps(585, 10), M2Kbps(65),
                M2Kbps(1215, 10), M2Kbps(135), M2Kbps(2633, 10), M2Kbps(2925, 10), M2Kbps(5265, 10), M2Kbps(585) }));
        // VHT MCS 7 (SS=1): 65, 72.2, 135, 150, 292.5, 325, 585, 650
        phyRateEntryMap.put(McsType.MCS_AC_1x1_7, new PhyRateEntry(new Long[] { M2Kbps(65), M2Kbps(722, 10),
                M2Kbps(135), M2Kbps(150), M2Kbps(2925, 10), M2Kbps(325), M2Kbps(585), M2Kbps(650) }));
        // VHT MCS 8 (SS=1): 78, 86.7, 162, 180, 351, 390, 702, 780
        phyRateEntryMap.put(McsType.MCS_AC_1x1_8, new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(867, 10),
                M2Kbps(162), M2Kbps(180), M2Kbps(351), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        // VHT MCS 9 (SS=1): n/a, n/a, 180, 200, 390, 433.3, 780, 866.7
        phyRateEntryMap.put(McsType.MCS_AC_1x1_9, new PhyRateEntry(new Long[] { null, null, M2Kbps(180), M2Kbps(200),
                M2Kbps(390), M2Kbps(4333, 10), M2Kbps(780), M2Kbps(8667, 10) }));

        // VHT MCS 0 (SS=2): 13, 14.4, 27, 30, 58.5, 65, 117, 130
        phyRateEntryMap.put(McsType.MCS_AC_2x2_0, new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(144, 10), M2Kbps(27),
                M2Kbps(30), M2Kbps(585, 10), M2Kbps(65), M2Kbps(117), M2Kbps(130) }));
        // VHT MCS 1 (SS=2): 26, 28.9, 54, 60, 117, 130, 234, 260
        phyRateEntryMap.put(McsType.MCS_AC_2x2_1, new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(289, 10), M2Kbps(54),
                M2Kbps(60), M2Kbps(117), M2Kbps(130), M2Kbps(234), M2Kbps(260) }));
        // VHT MCS 2 (SS=2): 39, 43.3, 81, 90, 175.5, 195, 351, 390
        phyRateEntryMap.put(McsType.MCS_AC_2x2_2, new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(433, 10), M2Kbps(81),
                M2Kbps(90), M2Kbps(1755, 10), M2Kbps(195), M2Kbps(351), M2Kbps(390) }));
        // VHT MCS 3 (SS=2): 52, 57.8, 108, 120, 234, 260, 468, 520
        phyRateEntryMap.put(McsType.MCS_AC_2x2_3, new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(578, 10),
                M2Kbps(108), M2Kbps(120), M2Kbps(234), M2Kbps(260), M2Kbps(468), M2Kbps(520) }));
        // VHT MCS 4 (SS=2): 78, 86.7, 162, 180, 351, 390, 702, 780
        phyRateEntryMap.put(McsType.MCS_AC_2x2_4, new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(867, 10),
                M2Kbps(162), M2Kbps(180), M2Kbps(351), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        // VHT MCS 5 (SS=2): 104, 115.6, 216, 240, 468, 520, 936, 1040
        phyRateEntryMap.put(McsType.MCS_AC_2x2_5, new PhyRateEntry(new Long[] { M2Kbps(104), M2Kbps(1156, 10),
                M2Kbps(216), M2Kbps(240), M2Kbps(468), M2Kbps(520), M2Kbps(936), M2Kbps(1040) }));
        // VHT MCS 6 (SS=2): 117, 130.3, 243, 270, 526.5, 585, 1053, 1170
        phyRateEntryMap.put(McsType.MCS_AC_2x2_6, new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(1303, 10),
                M2Kbps(243), M2Kbps(270), M2Kbps(5265, 10), M2Kbps(585), M2Kbps(1053), M2Kbps(1170) }));
        // VHT MCS 7 (SS=2): 130, 144.4, 270, 300, 585, 650, 1170, 1300
        phyRateEntryMap.put(McsType.MCS_AC_2x2_7, new PhyRateEntry(new Long[] { M2Kbps(130), M2Kbps(1444, 10),
                M2Kbps(270), M2Kbps(300), M2Kbps(585), M2Kbps(650), M2Kbps(1170), M2Kbps(1300) }));
        // VHT MCS 8 (SS=2): 156, 173.3, 324, 360, 702 780, 1404, 1560
        phyRateEntryMap.put(McsType.MCS_AC_2x2_8, new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(1733, 10),
                M2Kbps(324), M2Kbps(360), M2Kbps(702), M2Kbps(780), M2Kbps(1404), M2Kbps(1560) }));
        // VHT MCS 9 (SS=2): n/a, n/a, 360, 400, 780, 866.7, 1560, 1733.3
        phyRateEntryMap.put(McsType.MCS_AC_2x2_9, new PhyRateEntry(new Long[] { null, null, M2Kbps(360), M2Kbps(400),
                M2Kbps(780), M2Kbps(8667, 10), M2Kbps(1560), M2Kbps(17333, 10) }));

        // VHT MCS 0 (SS=3): 19.5, 21.7, 40.5, 45, 87.8, 97.5, 175.5, 195
        phyRateEntryMap.put(McsType.MCS_AC_3x3_0, new PhyRateEntry(new Long[] { M2Kbps(195,10), M2Kbps(217,10),
                M2Kbps(405,10), M2Kbps(45), M2Kbps(878,10), M2Kbps(975, 10), M2Kbps(1755, 10), M2Kbps(195) }));
        // VHT MCS 1 (SS=3): 39,43.3,81,90,175.5,195,351,390
        phyRateEntryMap.put(McsType.MCS_AC_3x3_1, new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(433,10),
                M2Kbps(81), M2Kbps(90), M2Kbps(1755,10), M2Kbps(195), M2Kbps(351), M2Kbps(390) }));
        // VHT MCS 2 (SS=3): 58.5,65,121.5,135,263.3,292.5,526.5,585
        phyRateEntryMap.put(McsType.MCS_AC_3x3_2, new PhyRateEntry(new Long[] { M2Kbps(585,10), M2Kbps(65),
                M2Kbps(1215,10), M2Kbps(135), M2Kbps(2633,10), M2Kbps(2925, 10), M2Kbps(5265, 10), M2Kbps(585) }));
        // VHT MCS 3 (SS=3): 78,86.7,162,180,351,390,702,780
        phyRateEntryMap.put(McsType.MCS_AC_3x3_3, new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(867,10),
                M2Kbps(162), M2Kbps(180), M2Kbps(351), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        // VHT MCS 4 (SS=3): 117,130,243,270,526.5,585,1053,1170
        phyRateEntryMap.put(McsType.MCS_AC_3x3_4, new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(130),
                M2Kbps(243), M2Kbps(270), M2Kbps(5265,10), M2Kbps(585), M2Kbps(1053), M2Kbps(1170) }));
        // VHT MCS 5 (SS=3): 156,173.3,324,360,702,780,1404,1560
        phyRateEntryMap.put(McsType.MCS_AC_3x3_5, new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(1733,10),
                M2Kbps(324), M2Kbps(360), M2Kbps(702), M2Kbps(780), M2Kbps(1404), M2Kbps(1560) }));
        // VHT MCS 6 (SS=3): 175.5,195,364.5,405,n/a,n/a,1579.5,1755
        phyRateEntryMap.put(McsType.MCS_AC_3x3_6, new PhyRateEntry(new Long[] { M2Kbps(1755,10), M2Kbps(195),
                M2Kbps(3645,10), M2Kbps(405), null, null, M2Kbps(15795,10), M2Kbps(1755) }));
        // VHT MCS 7 (SS=3): 195,216.7,405,450,877.5,975,1755,1950
        phyRateEntryMap.put(McsType.MCS_AC_3x3_7, new PhyRateEntry(new Long[] { M2Kbps(195), M2Kbps(2167,10),
                M2Kbps(405), M2Kbps(450), M2Kbps(8775,10), M2Kbps(975), M2Kbps(1755), M2Kbps(1950) }));
        // VHT MCS 8 (SS=3): 234,260,486,540,1053,1170,2106,2340
        phyRateEntryMap.put(McsType.MCS_AC_3x3_8, new PhyRateEntry(new Long[] { M2Kbps(234), M2Kbps(260),
                M2Kbps(486), M2Kbps(540), M2Kbps(1053), M2Kbps(1170), M2Kbps(2106), M2Kbps(2340) }));
        // VHT MCS 9 (SS=3): 260,288.9,540,600,1170,1300,n/a,n/a
        phyRateEntryMap.put(McsType.MCS_AC_3x3_9, new PhyRateEntry(new Long[] { M2Kbps(260), M2Kbps(2889,10),
                M2Kbps(540), M2Kbps(600), M2Kbps(1170), M2Kbps(1300) }));


        //  VHT MCS (SS=4) 0   4   BPSK    1/2 26  28.8    54  60  117.2   130 234 260
        phyRateEntryMap.put(McsType.MCS_AC_4x4_0, new PhyRateEntry(new Long[] { 
                M2Kbps(26), M2Kbps(288,10), M2Kbps(54), M2Kbps(60), M2Kbps(1172, 10), M2Kbps(130), M2Kbps(234), M2Kbps(260) }));
        
        //  VHT MCS (SS=4) 1   4   QPSK    1/2 52  57.6    108 120 234 260 468 520
        phyRateEntryMap.put(McsType.MCS_AC_4x4_1, new PhyRateEntry(new Long[] { 
                M2Kbps(52), M2Kbps(576,10), M2Kbps(108), M2Kbps(120), M2Kbps(234), M2Kbps(260), M2Kbps(468), M2Kbps(520) }));
        
        //  VHT MCS (SS=4) 2   4   QPSK    3/4 78  86.8    162 180 351.2   390 702 780
        phyRateEntryMap.put(McsType.MCS_AC_4x4_2, new PhyRateEntry(new Long[] { 
                M2Kbps(78), M2Kbps(868,10), M2Kbps(162), M2Kbps(180), M2Kbps(3512, 10), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        
        //  VHT MCS (SS=4) 3   4   16-QAM  1/2 104 115.6   216 240 468 520 936 1040
        phyRateEntryMap.put(McsType.MCS_AC_4x4_3, new PhyRateEntry(new Long[] { 
                M2Kbps(104), M2Kbps(1156,10), M2Kbps(216), M2Kbps(240), M2Kbps(468), M2Kbps(520), M2Kbps(936), M2Kbps(1040) }));
        
        //  VHT MCS (SS=4) 4   4   16-QAM  3/4 156 173.2   324 360 702 780 1404    1560
        phyRateEntryMap.put(McsType.MCS_AC_4x4_4, new PhyRateEntry(new Long[] { 
                M2Kbps(156), M2Kbps(1732,10), M2Kbps(324), M2Kbps(360), M2Kbps(702), M2Kbps(780), M2Kbps(1404), M2Kbps(1560) }));
        
        //  VHT MCS (SS=4) 5   4   64-QAM  2/3 208 231.2   432 480 936 1040    1872    2080
        phyRateEntryMap.put(McsType.MCS_AC_4x4_5, new PhyRateEntry(new Long[] { 
                M2Kbps(208), M2Kbps(2312,10), M2Kbps(432), M2Kbps(480), M2Kbps(936), M2Kbps(1040), M2Kbps(1872), M2Kbps(2080) }));
        
        //  VHT MCS (SS=4) 6   4   64-QAM  3/4 234 260 486 540 1053.2  1170    2106    2340
        phyRateEntryMap.put(McsType.MCS_AC_4x4_6, new PhyRateEntry(new Long[] { 
                M2Kbps(234), M2Kbps(260), M2Kbps(486), M2Kbps(540), M2Kbps(10532, 10), M2Kbps(1170), M2Kbps(2106), M2Kbps(2340) }));
        
        //  VHT MCS (SS=4) 7   4   64-QAM  5/6 260 288.8   540 600 1170    1300    2340    2600
        phyRateEntryMap.put(McsType.MCS_AC_4x4_7, new PhyRateEntry(new Long[] { 
                M2Kbps(260), M2Kbps(2888, 10), M2Kbps(540), M2Kbps(600), M2Kbps(1170), M2Kbps(1300), M2Kbps(2340), M2Kbps(2600) }));
        
        //  VHT MCS (SS=4) 8   4   256-QAM 3/4 312 346.8   648 720 1404    1560    2808    3120
        phyRateEntryMap.put(McsType.MCS_AC_4x4_8, new PhyRateEntry(new Long[] { 
                M2Kbps(312), M2Kbps(3468, 10), M2Kbps(648), M2Kbps(720), M2Kbps(1404), M2Kbps(1560), M2Kbps(2808), M2Kbps(3120) }));
        
        //  VHT MCS (SS=4) 9   4   256-QAM 5/6 N/A N/A 720 800 1560    1733.2  3120    3466.8
        phyRateEntryMap.put(McsType.MCS_AC_4x4_9, new PhyRateEntry(new Long[] { 
                null, null, M2Kbps(720), M2Kbps(800), M2Kbps(1560), M2Kbps(17332, 10), M2Kbps(3120), M2Kbps(34668, 10) }));

        
        ///////////////////////////////////
        // truncated rate map goes below
        ///////////////////////////////////
        truncatedPhyRateEntryMap = new EnumMap<>(McsType.class);

        // Populate values as 20Mzh LGI, 20Mhz SGI, 40Mhz LGI, 40Mhz LGI, 80 Mzh
        // LGI, 80 Mhz SIG, 160Mhz LGI, 160Mhz SGI

        // legacy
        truncatedPhyRateEntryMap.put(McsType.MCS_1, new PhyRateEntry(new Long[] { M2Kbps(1), M2Kbps(1) })); // 2.4
        truncatedPhyRateEntryMap.put(McsType.MCS_2, new PhyRateEntry(new Long[] { M2Kbps(2), M2Kbps(2) })); // 2.4
        truncatedPhyRateEntryMap.put(McsType.MCS_5dot5, new PhyRateEntry(new Long[] { M2Kbps(55, 10), M2Kbps(55, 10) }));// 2.4
        truncatedPhyRateEntryMap.put(McsType.MCS_6, new PhyRateEntry(new Long[] { M2Kbps(6), M2Kbps(6), M2Kbps(6), M2Kbps(6) }));
        truncatedPhyRateEntryMap.put(McsType.MCS_9, new PhyRateEntry(new Long[] { M2Kbps(9), M2Kbps(9), M2Kbps(9), M2Kbps(9) }));
        truncatedPhyRateEntryMap.put(McsType.MCS_11, new PhyRateEntry(new Long[] { M2Kbps(11), M2Kbps(11) })); // 2.4
        truncatedPhyRateEntryMap.put(McsType.MCS_12,
                new PhyRateEntry(new Long[] { M2Kbps(12), M2Kbps(12), M2Kbps(12), M2Kbps(12) }));
        truncatedPhyRateEntryMap.put(McsType.MCS_18,
                new PhyRateEntry(new Long[] { M2Kbps(18), M2Kbps(18), M2Kbps(18), M2Kbps(18) }));
        truncatedPhyRateEntryMap.put(McsType.MCS_24,
                new PhyRateEntry(new Long[] { M2Kbps(24), M2Kbps(24), M2Kbps(24), M2Kbps(24) }));
        truncatedPhyRateEntryMap.put(McsType.MCS_36,
                new PhyRateEntry(new Long[] { M2Kbps(36), M2Kbps(36), M2Kbps(36), M2Kbps(36) }));
        truncatedPhyRateEntryMap.put(McsType.MCS_48,
                new PhyRateEntry(new Long[] { M2Kbps(48), M2Kbps(48), M2Kbps(48), M2Kbps(48) }));
        truncatedPhyRateEntryMap.put(McsType.MCS_54,
                new PhyRateEntry(new Long[] { M2Kbps(54), M2Kbps(54), M2Kbps(54), M2Kbps(54) }));

        // see
        // http://www.wlanpros.com/wp-content/uploads/2015/06/Screenshot-2014-09-28-10.11.56-1024x837.png
        // HT MCS 0: 6.5, 7.2, 13.5, 15
        truncatedPhyRateEntryMap.put(McsType.MCS_N_0,
                new PhyRateEntry(new Long[] { M2Kbps(6), M2Kbps(7), M2Kbps(13), M2Kbps(15) }));
        // HT MCS 1: 13, 14.4 27, 30
        truncatedPhyRateEntryMap.put(McsType.MCS_N_1,
                new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(14), M2Kbps(27), M2Kbps(30) }));
        // HT MCS 2: 19.5, 21.7, 40.5, 45
        truncatedPhyRateEntryMap.put(McsType.MCS_N_2,
                new PhyRateEntry(new Long[] { M2Kbps(19), M2Kbps(21), M2Kbps(40), M2Kbps(45) }));
        // HT MCS 3: 26, 28.9, 54, 60
        truncatedPhyRateEntryMap.put(McsType.MCS_N_3,
                new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(28), M2Kbps(54), M2Kbps(60) }));
        // HT MCS 4: 39, 43.3, 81, 90
        truncatedPhyRateEntryMap.put(McsType.MCS_N_4,
                new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(43), M2Kbps(81), M2Kbps(90) }));
        // HT MCS 5: 52, 57.8, 108, 120
        truncatedPhyRateEntryMap.put(McsType.MCS_N_5,
                new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(57), M2Kbps(108), M2Kbps(120) }));
        // HT MCS 6: 58.5, 65, 121.5, 135
        truncatedPhyRateEntryMap.put(McsType.MCS_N_6,
                new PhyRateEntry(new Long[] { M2Kbps(58), M2Kbps(65), M2Kbps(121), M2Kbps(135) }));
        // HT MCS 7: 65, 72.2, 135, 150
        truncatedPhyRateEntryMap.put(McsType.MCS_N_7,
                new PhyRateEntry(new Long[] { M2Kbps(65), M2Kbps(72), M2Kbps(135), M2Kbps(150) }));
        // HT MCS 8: 13, 14.4, 27, 30
        truncatedPhyRateEntryMap.put(McsType.MCS_N_8,
                new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(14), M2Kbps(27), M2Kbps(30) }));
        // HT MCS 9: 26, 28.9, 54, 60
        truncatedPhyRateEntryMap.put(McsType.MCS_N_9,
                new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(28), M2Kbps(54), M2Kbps(60) }));
        // HT MCS 10: 39, 43.3, 81, 90
        truncatedPhyRateEntryMap.put(McsType.MCS_N_10,
                new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(43), M2Kbps(81), M2Kbps(90) }));
        // HT MCS 11: 52, 57.8, 108, 120
        truncatedPhyRateEntryMap.put(McsType.MCS_N_11,
                new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(57), M2Kbps(108), M2Kbps(120) }));
        // HT MCS 12: 78, 86.7, 162, 180
        truncatedPhyRateEntryMap.put(McsType.MCS_N_12,
                new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(86), M2Kbps(162), M2Kbps(180) }));
        // HT MCS 13: 104, 115.6, 216, 240
        truncatedPhyRateEntryMap.put(McsType.MCS_N_13,
                new PhyRateEntry(new Long[] { M2Kbps(104), M2Kbps(115), M2Kbps(216), M2Kbps(240) }));
        // HT MCS 14: 117, 130.3, 243, 270
        truncatedPhyRateEntryMap.put(McsType.MCS_N_14,
                new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(130), M2Kbps(243), M2Kbps(270) }));
        // HT MCS 15: 130, 144.4, 270, 300
        truncatedPhyRateEntryMap.put(McsType.MCS_N_15,
                new PhyRateEntry(new Long[] { M2Kbps(130), M2Kbps(144), M2Kbps(270), M2Kbps(300) }));
        
        // HT MCS 16-23 with 3 Spatial Streams is unsupported by our AP right now,
        // HT MCS 16: 19.5  21.7    40.5    45
        truncatedPhyRateEntryMap.put(McsType.MCS_N_16,
                new PhyRateEntry(new Long[] { M2Kbps(19), M2Kbps(21), M2Kbps(40), M2Kbps(45) }));
        
        //https://en.wikipedia.org/wiki/IEEE_802.11n-2009#Data_rates
        // HT MCS 17  3   QPSK    1/2 39  43.3    81  90
        truncatedPhyRateEntryMap.put(McsType.MCS_N_17,
                new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(43), M2Kbps(81), M2Kbps(90) }));
        
        // HT MCS 18  3   QPSK    3/4 58.5    65  121.5   135
        truncatedPhyRateEntryMap.put(McsType.MCS_N_18,
                new PhyRateEntry(new Long[] { M2Kbps(58), M2Kbps(65), M2Kbps(121), M2Kbps(135) }));
        
        // HT MCS 19  3   16-QAM  1/2 78  86.7    162 180
        truncatedPhyRateEntryMap.put(McsType.MCS_N_19,
                new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(86), M2Kbps(162), M2Kbps(180) }));
        
        // HT MCS 20  3   16-QAM  3/4 117 130 243 270
        truncatedPhyRateEntryMap.put(McsType.MCS_N_20,
                new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(130), M2Kbps(243), M2Kbps(270) }));
        
        // HT MCS 21  3   64-QAM  2/3 156 173.3   324 360
        truncatedPhyRateEntryMap.put(McsType.MCS_N_21,
                new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(173), M2Kbps(324), M2Kbps(360) }));

        // HT MCS 22  3   64-QAM  3/4 175.5   195 364.5   405
        truncatedPhyRateEntryMap.put(McsType.MCS_N_22,
                new PhyRateEntry(new Long[] { M2Kbps(175), M2Kbps(195), M2Kbps(364), M2Kbps(405) }));

        // HT MCS 23  3   64-QAM  5/6 195 216.7   405 450
        truncatedPhyRateEntryMap.put(McsType.MCS_N_23,
                new PhyRateEntry(new Long[] { M2Kbps(195), M2Kbps(216), M2Kbps(405), M2Kbps(450) }));

        // HT MCS 24  4   BPSK    1/2 26  28.8    54  60
        truncatedPhyRateEntryMap.put(McsType.MCS_N_24,
                new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(28), M2Kbps(54), M2Kbps(60) }));

        // HT MCS 25  4   QPSK    1/2 52  57.6    108 120
        truncatedPhyRateEntryMap.put(McsType.MCS_N_25,
                new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(57), M2Kbps(108), M2Kbps(120) }));

        // HT MCS 26  4   QPSK    3/4 78  86.8    162 180
        truncatedPhyRateEntryMap.put(McsType.MCS_N_26,
                new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(86), M2Kbps(162), M2Kbps(180) }));

        // HT MCS 27  4   16-QAM  1/2 104 115.6   216 240
        truncatedPhyRateEntryMap.put(McsType.MCS_N_27,
                new PhyRateEntry(new Long[] { M2Kbps(104), M2Kbps(115), M2Kbps(216), M2Kbps(240) }));

        // HT MCS 28  4   16-QAM  3/4 156 173.2   324 360
        truncatedPhyRateEntryMap.put(McsType.MCS_N_28,
                new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(173), M2Kbps(324), M2Kbps(360) }));

        // HT MCS 29  4   64-QAM  2/3 208 231.2   432 480
        truncatedPhyRateEntryMap.put(McsType.MCS_N_29,
                new PhyRateEntry(new Long[] { M2Kbps(208), M2Kbps(231), M2Kbps(432), M2Kbps(480) }));

        // HT MCS 30  4   64-QAM  3/4 234 260 486 540
        truncatedPhyRateEntryMap.put(McsType.MCS_N_30,
                new PhyRateEntry(new Long[] { M2Kbps(234), M2Kbps(260), M2Kbps(486), M2Kbps(540) }));

        // HT MCS 31  4   64-QAM  5/6 260 288.8   540 600
        truncatedPhyRateEntryMap.put(McsType.MCS_N_31,
                new PhyRateEntry(new Long[] { M2Kbps(260), M2Kbps(288), M2Kbps(540), M2Kbps(600) }));



        // VHT MCS 0 (SS=1): 6.5, 7.2, 13.5, 15, 29.3, 32.5, 58.5, 65
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_1x1_0, new PhyRateEntry(new Long[] { M2Kbps(6), M2Kbps(7),
                M2Kbps(13), M2Kbps(15), M2Kbps(29), M2Kbps(32), M2Kbps(58), M2Kbps(65) }));
        // VHT MCS 1 (SS=1): 13, 14.4, 27, 30, 58.5, 65, 117, 130
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_1x1_1, new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(14), M2Kbps(27),
                M2Kbps(30), M2Kbps(58), M2Kbps(65), M2Kbps(117), M2Kbps(130) }));
        // VHT MCS 2 (SS=1): 19.5, 21.7, 40.5, 45, 87.8, 97.5, 175.5, 195
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_1x1_2, new PhyRateEntry(new Long[] { M2Kbps(19), M2Kbps(21),
                M2Kbps(40), M2Kbps(45), M2Kbps(87), M2Kbps(97), M2Kbps(175), M2Kbps(195) }));
        // VHT MCS 3 (SS=1): 26, 28.9 54, 60, 117, 130, 234, 260
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_1x1_3, new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(28), M2Kbps(54),
                M2Kbps(60), M2Kbps(117), M2Kbps(130), M2Kbps(234), M2Kbps(260) }));
        // VHT MCS 4 (SS=1): 39, 43.3, 81, 90, 175.5 195, 351, 390
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_1x1_4, new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(43), M2Kbps(81),
                M2Kbps(90), M2Kbps(175), M2Kbps(195), M2Kbps(351), M2Kbps(390) }));
        // VHT MCS 5 (SS=1): 52, 57.8, 108, 120, 234, 260, 468, 520
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_1x1_5, new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(57),
                M2Kbps(108), M2Kbps(120), M2Kbps(234), M2Kbps(260), M2Kbps(468), M2Kbps(520) }));
        // VHT MCS 6 (SS=1): 58.5, 65, 121.5, 135, 263.3, 292.5, 526.5, 585
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_1x1_6, new PhyRateEntry(new Long[] { M2Kbps(58), M2Kbps(65),
                M2Kbps(121), M2Kbps(135), M2Kbps(263), M2Kbps(292), M2Kbps(526), M2Kbps(585) }));
        // VHT MCS 7 (SS=1): 65, 72.2, 135, 150, 292.5, 325, 585, 650
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_1x1_7, new PhyRateEntry(new Long[] { M2Kbps(65), M2Kbps(72),
                M2Kbps(135), M2Kbps(150), M2Kbps(292), M2Kbps(325), M2Kbps(585), M2Kbps(650) }));
        // VHT MCS 8 (SS=1): 78, 86.7, 162, 180, 351, 390, 702, 780
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_1x1_8, new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(86),
                M2Kbps(162), M2Kbps(180), M2Kbps(351), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        // VHT MCS 9 (SS=1): n/a, n/a, 180, 200, 390, 433.3, 780, 866.7
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_1x1_9, new PhyRateEntry(new Long[] { null, null, M2Kbps(180), M2Kbps(200),
                M2Kbps(390), M2Kbps(433), M2Kbps(780), M2Kbps(866) }));

        // VHT MCS 0 (SS=2): 13, 14.4, 27, 30, 58.5, 65, 117, 130
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_2x2_0, new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(14), M2Kbps(27),
                M2Kbps(30), M2Kbps(58), M2Kbps(65), M2Kbps(117), M2Kbps(130) }));
        // VHT MCS 1 (SS=2): 26, 28.9, 54, 60, 117, 130, 234, 260
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_2x2_1, new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(28), M2Kbps(54),
                M2Kbps(60), M2Kbps(117), M2Kbps(130), M2Kbps(234), M2Kbps(260) }));
        // VHT MCS 2 (SS=2): 39, 43.3, 81, 90, 175.5, 195, 351, 390
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_2x2_2, new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(43), M2Kbps(81),
                M2Kbps(90), M2Kbps(175), M2Kbps(195), M2Kbps(351), M2Kbps(390) }));
        // VHT MCS 3 (SS=2): 52, 57.8, 108, 120, 234, 260, 468, 520
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_2x2_3, new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(57),
                M2Kbps(108), M2Kbps(120), M2Kbps(234), M2Kbps(260), M2Kbps(468), M2Kbps(520) }));
        // VHT MCS 4 (SS=2): 78, 86.7, 162, 180, 351, 390, 702, 780
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_2x2_4, new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(86),
                M2Kbps(162), M2Kbps(180), M2Kbps(351), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        // VHT MCS 5 (SS=2): 104, 115.6, 216, 240, 468, 520, 936, 1040
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_2x2_5, new PhyRateEntry(new Long[] { M2Kbps(104), M2Kbps(115),
                M2Kbps(216), M2Kbps(240), M2Kbps(468), M2Kbps(520), M2Kbps(936), M2Kbps(1040) }));
        // VHT MCS 6 (SS=2): 117, 130.3, 243, 270, 526.5, 585, 1053, 1170
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_2x2_6, new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(130),
                M2Kbps(243), M2Kbps(270), M2Kbps(526), M2Kbps(585), M2Kbps(1053), M2Kbps(1170) }));
        // VHT MCS 7 (SS=2): 130, 144.4, 270, 300, 585, 650, 1170, 1300
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_2x2_7, new PhyRateEntry(new Long[] { M2Kbps(130), M2Kbps(144),
                M2Kbps(270), M2Kbps(300), M2Kbps(585), M2Kbps(650), M2Kbps(1170), M2Kbps(1300) }));
        // VHT MCS 8 (SS=2): 156, 173.3, 324, 360, 702 780, 1404, 1560
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_2x2_8, new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(173),
                M2Kbps(324), M2Kbps(360), M2Kbps(702), M2Kbps(780), M2Kbps(1404), M2Kbps(1560) }));
        // VHT MCS 9 (SS=2): n/a, n/a, 360, 400, 780, 866.7, 1560, 1733.3
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_2x2_9, new PhyRateEntry(new Long[] { null, null, M2Kbps(360), M2Kbps(400),
                M2Kbps(780), M2Kbps(866), M2Kbps(1560), M2Kbps(1733) }));

        // VHT MCS 0 (SS=3): 19.5, 21.7, 40.5, 45, 87.8, 97.5, 175.5, 195
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_3x3_0, new PhyRateEntry(new Long[] { M2Kbps(19), M2Kbps(21),
                M2Kbps(40), M2Kbps(45), M2Kbps(87), M2Kbps(97), M2Kbps(175), M2Kbps(195) }));
        // VHT MCS 1 (SS=3): 39,43.3,81,90,175.5,195,351,390
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_3x3_1, new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(43),
                M2Kbps(81), M2Kbps(90), M2Kbps(175), M2Kbps(195), M2Kbps(351), M2Kbps(390) }));
        // VHT MCS 2 (SS=3): 58.5,65,121.5,135,263.3,292.5,526.5,585
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_3x3_2, new PhyRateEntry(new Long[] { M2Kbps(58), M2Kbps(65),
                M2Kbps(121), M2Kbps(135), M2Kbps(263), M2Kbps(292), M2Kbps(526), M2Kbps(585) }));
        // VHT MCS 3 (SS=3): 78,86.7,162,180,351,390,702,780
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_3x3_3, new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(86),
                M2Kbps(162), M2Kbps(180), M2Kbps(351), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        // VHT MCS 4 (SS=3): 117,130,243,270,526.5,585,1053,1170
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_3x3_4, new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(130),
                M2Kbps(243), M2Kbps(270), M2Kbps(526), M2Kbps(585), M2Kbps(1053), M2Kbps(1170) }));
        // VHT MCS 5 (SS=3): 156,173.3,324,360,702,780,1404,1560
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_3x3_5, new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(173),
                M2Kbps(324), M2Kbps(360), M2Kbps(702), M2Kbps(780), M2Kbps(1404), M2Kbps(1560) }));
        // VHT MCS 6 (SS=3): 175.5,195,364.5,405,n/a,n/a,1579.5,1755
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_3x3_6, new PhyRateEntry(new Long[] { M2Kbps(175), M2Kbps(195),
                M2Kbps(364), M2Kbps(405), null, null, M2Kbps(1579), M2Kbps(1755) }));
        // VHT MCS 7 (SS=3): 195,216.7,405,450,877.5,975,1755,1950
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_3x3_7, new PhyRateEntry(new Long[] { M2Kbps(195), M2Kbps(216),
                M2Kbps(405), M2Kbps(450), M2Kbps(877), M2Kbps(975), M2Kbps(1755), M2Kbps(1950) }));
        // VHT MCS 8 (SS=3): 234,260,486,540,1053,1170,2106,2340
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_3x3_8, new PhyRateEntry(new Long[] { M2Kbps(234), M2Kbps(260),
                M2Kbps(486), M2Kbps(540), M2Kbps(1053), M2Kbps(1170), M2Kbps(2106), M2Kbps(2340) }));
        // VHT MCS 9 (SS=3): 260,288.9,540,600,1170,1300,n/a,n/a
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_3x3_9, new PhyRateEntry(new Long[] { M2Kbps(260), M2Kbps(288),
                M2Kbps(540), M2Kbps(600), M2Kbps(1170), M2Kbps(1300) }));


        //  VHT MCS (SS=4) 0   4   BPSK    1/2 26  28.8    54  60  117.2   130 234 260
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_4x4_0, new PhyRateEntry(new Long[] { 
                M2Kbps(26), M2Kbps(28), M2Kbps(54), M2Kbps(60), M2Kbps(117), M2Kbps(130), M2Kbps(234), M2Kbps(260) }));
        
        //  VHT MCS (SS=4) 1   4   QPSK    1/2 52  57.6    108 120 234 260 468 520
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_4x4_1, new PhyRateEntry(new Long[] { 
                M2Kbps(52), M2Kbps(57), M2Kbps(108), M2Kbps(120), M2Kbps(234), M2Kbps(260), M2Kbps(468), M2Kbps(520) }));
        
        //  VHT MCS (SS=4) 2   4   QPSK    3/4 78  86.8    162 180 351.2   390 702 780
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_4x4_2, new PhyRateEntry(new Long[] { 
                M2Kbps(78), M2Kbps(86), M2Kbps(162), M2Kbps(180), M2Kbps(351), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        
        //  VHT MCS (SS=4) 3   4   16-QAM  1/2 104 115.6   216 240 468 520 936 1040
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_4x4_3, new PhyRateEntry(new Long[] { 
                M2Kbps(104), M2Kbps(115), M2Kbps(216), M2Kbps(240), M2Kbps(468), M2Kbps(520), M2Kbps(936), M2Kbps(1040) }));
        
        //  VHT MCS (SS=4) 4   4   16-QAM  3/4 156 173.2   324 360 702 780 1404    1560
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_4x4_4, new PhyRateEntry(new Long[] { 
                M2Kbps(156), M2Kbps(173), M2Kbps(324), M2Kbps(360), M2Kbps(702), M2Kbps(780), M2Kbps(1404), M2Kbps(1560) }));
        
        //  VHT MCS (SS=4) 5   4   64-QAM  2/3 208 231.2   432 480 936 1040    1872    2080
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_4x4_5, new PhyRateEntry(new Long[] { 
                M2Kbps(208), M2Kbps(231), M2Kbps(432), M2Kbps(480), M2Kbps(936), M2Kbps(1040), M2Kbps(1872), M2Kbps(2080) }));
        
        //  VHT MCS (SS=4) 6   4   64-QAM  3/4 234 260 486 540 1053.2  1170    2106    2340
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_4x4_6, new PhyRateEntry(new Long[] { 
                M2Kbps(234), M2Kbps(260), M2Kbps(486), M2Kbps(540), M2Kbps(1053), M2Kbps(1170), M2Kbps(2106), M2Kbps(2340) }));
        
        //  VHT MCS (SS=4) 7   4   64-QAM  5/6 260 288.8   540 600 1170    1300    2340    2600
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_4x4_7, new PhyRateEntry(new Long[] { 
                M2Kbps(260), M2Kbps(288), M2Kbps(540), M2Kbps(600), M2Kbps(1170), M2Kbps(1300), M2Kbps(2340), M2Kbps(2600) }));
        
        //  VHT MCS (SS=4) 8   4   256-QAM 3/4 312 346.8   648 720 1404    1560    2808    3120
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_4x4_8, new PhyRateEntry(new Long[] { 
                M2Kbps(312), M2Kbps(346), M2Kbps(648), M2Kbps(720), M2Kbps(1404), M2Kbps(1560), M2Kbps(2808), M2Kbps(3120) }));
        
        //  VHT MCS (SS=4) 9   4   256-QAM 5/6 N/A N/A 720 800 1560    1733.2  3120    3466.8
        truncatedPhyRateEntryMap.put(McsType.MCS_AC_4x4_9, new PhyRateEntry(new Long[] { 
                null, null, M2Kbps(720), M2Kbps(800), M2Kbps(1560), M2Kbps(1733), M2Kbps(3120), M2Kbps(3466) }));
        

        
        ///////////////////////////////////
        // rounded rate map goes below
        ///////////////////////////////////

        
        roundedPhyRateEntryMap = new EnumMap<>(McsType.class);

        // Populate values as 20Mzh LGI, 20Mhz SGI, 40Mhz LGI, 40Mhz LGI, 80 Mzh
        // LGI, 80 Mhz SIG, 160Mhz LGI, 160Mhz SGI

        // legacy
        roundedPhyRateEntryMap.put(McsType.MCS_1, new PhyRateEntry(new Long[] { M2Kbps(1), M2Kbps(1) })); // 2.4
        roundedPhyRateEntryMap.put(McsType.MCS_2, new PhyRateEntry(new Long[] { M2Kbps(2), M2Kbps(2) })); // 2.4
        roundedPhyRateEntryMap.put(McsType.MCS_5dot5, new PhyRateEntry(new Long[] { M2Kbps(6), M2Kbps(6) }));// 2.4
        roundedPhyRateEntryMap.put(McsType.MCS_6, new PhyRateEntry(new Long[] { M2Kbps(6), M2Kbps(6), M2Kbps(6), M2Kbps(6) }));
        roundedPhyRateEntryMap.put(McsType.MCS_9, new PhyRateEntry(new Long[] { M2Kbps(9), M2Kbps(9), M2Kbps(9), M2Kbps(9) }));
        roundedPhyRateEntryMap.put(McsType.MCS_11, new PhyRateEntry(new Long[] { M2Kbps(11), M2Kbps(11) })); // 2.4
        roundedPhyRateEntryMap.put(McsType.MCS_12,
                new PhyRateEntry(new Long[] { M2Kbps(12), M2Kbps(12), M2Kbps(12), M2Kbps(12) }));
        roundedPhyRateEntryMap.put(McsType.MCS_18,
                new PhyRateEntry(new Long[] { M2Kbps(18), M2Kbps(18), M2Kbps(18), M2Kbps(18) }));
        roundedPhyRateEntryMap.put(McsType.MCS_24,
                new PhyRateEntry(new Long[] { M2Kbps(24), M2Kbps(24), M2Kbps(24), M2Kbps(24) }));
        roundedPhyRateEntryMap.put(McsType.MCS_36,
                new PhyRateEntry(new Long[] { M2Kbps(36), M2Kbps(36), M2Kbps(36), M2Kbps(36) }));
        roundedPhyRateEntryMap.put(McsType.MCS_48,
                new PhyRateEntry(new Long[] { M2Kbps(48), M2Kbps(48), M2Kbps(48), M2Kbps(48) }));
        roundedPhyRateEntryMap.put(McsType.MCS_54,
                new PhyRateEntry(new Long[] { M2Kbps(54), M2Kbps(54), M2Kbps(54), M2Kbps(54) }));

        // see
        // http://www.wlanpros.com/wp-content/uploads/2015/06/Screenshot-2014-09-28-10.11.56-1024x837.png
        // HT MCS 0: 6.5, 7.2, 13.5, 15
        roundedPhyRateEntryMap.put(McsType.MCS_N_0,
                new PhyRateEntry(new Long[] { M2Kbps(7), M2Kbps(8), M2Kbps(14), M2Kbps(15) }));
        // HT MCS 1: 13, 14.4 27, 30
        roundedPhyRateEntryMap.put(McsType.MCS_N_1,
                new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(15), M2Kbps(27), M2Kbps(30) }));
        // HT MCS 2: 19.5, 21.7, 40.5, 45
        roundedPhyRateEntryMap.put(McsType.MCS_N_2,
                new PhyRateEntry(new Long[] { M2Kbps(20), M2Kbps(22), M2Kbps(41), M2Kbps(45) }));
        // HT MCS 3: 26, 28.9, 54, 60
        roundedPhyRateEntryMap.put(McsType.MCS_N_3,
                new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(29), M2Kbps(54), M2Kbps(60) }));
        // HT MCS 4: 39, 43.3, 81, 90
        roundedPhyRateEntryMap.put(McsType.MCS_N_4,
                new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(44), M2Kbps(81), M2Kbps(90) }));
        // HT MCS 5: 52, 57.8, 108, 120
        roundedPhyRateEntryMap.put(McsType.MCS_N_5,
                new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(58), M2Kbps(108), M2Kbps(120) }));
        // HT MCS 6: 58.5, 65, 121.5, 135
        roundedPhyRateEntryMap.put(McsType.MCS_N_6,
                new PhyRateEntry(new Long[] { M2Kbps(59), M2Kbps(65), M2Kbps(122), M2Kbps(135) }));
        // HT MCS 7: 65, 72.2, 135, 150
        roundedPhyRateEntryMap.put(McsType.MCS_N_7,
                new PhyRateEntry(new Long[] { M2Kbps(65), M2Kbps(73), M2Kbps(135), M2Kbps(150) }));
        // HT MCS 8: 13, 14.4, 27, 30
        roundedPhyRateEntryMap.put(McsType.MCS_N_8,
                new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(15), M2Kbps(27), M2Kbps(30) }));
        // HT MCS 9: 26, 28.9, 54, 60
        roundedPhyRateEntryMap.put(McsType.MCS_N_9,
                new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(29), M2Kbps(54), M2Kbps(60) }));
        // HT MCS 10: 39, 43.3, 81, 90
        roundedPhyRateEntryMap.put(McsType.MCS_N_10,
                new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(44), M2Kbps(81), M2Kbps(90) }));
        // HT MCS 11: 52, 57.8, 108, 120
        roundedPhyRateEntryMap.put(McsType.MCS_N_11,
                new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(58), M2Kbps(108), M2Kbps(120) }));
        // HT MCS 12: 78, 86.7, 162, 180
        roundedPhyRateEntryMap.put(McsType.MCS_N_12,
                new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(87), M2Kbps(162), M2Kbps(180) }));
        // HT MCS 13: 104, 115.6, 216, 240
        roundedPhyRateEntryMap.put(McsType.MCS_N_13,
                new PhyRateEntry(new Long[] { M2Kbps(104), M2Kbps(116), M2Kbps(216), M2Kbps(240) }));
        // HT MCS 14: 117, 130.3, 243, 270
        roundedPhyRateEntryMap.put(McsType.MCS_N_14,
                new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(131), M2Kbps(243), M2Kbps(270) }));
        // HT MCS 15: 130, 144.4, 270, 300
        roundedPhyRateEntryMap.put(McsType.MCS_N_15,
                new PhyRateEntry(new Long[] { M2Kbps(130), M2Kbps(145), M2Kbps(270), M2Kbps(300) }));
        
        // HT MCS 16-23 with 3 Spatial Streams is unsupported by our AP right now,
        // HT MCS 16: 19.5  21.7    40.5    45
        roundedPhyRateEntryMap.put(McsType.MCS_N_16,
                new PhyRateEntry(new Long[] { M2Kbps(20), M2Kbps(22), M2Kbps(41), M2Kbps(45) }));
        
        //https://en.wikipedia.org/wiki/IEEE_802.11n-2009#Data_rates
        // HT MCS 17  3   QPSK    1/2 39  43.3    81  90
        roundedPhyRateEntryMap.put(McsType.MCS_N_17,
                new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(44), M2Kbps(81), M2Kbps(90) }));
        
        // HT MCS 18  3   QPSK    3/4 58.5    65  121.5   135
        roundedPhyRateEntryMap.put(McsType.MCS_N_18,
                new PhyRateEntry(new Long[] { M2Kbps(59), M2Kbps(65), M2Kbps(122), M2Kbps(135) }));
        
        // HT MCS 19  3   16-QAM  1/2 78  86.7    162 180
        roundedPhyRateEntryMap.put(McsType.MCS_N_19,
                new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(87), M2Kbps(162), M2Kbps(180) }));
        
        // HT MCS 20  3   16-QAM  3/4 117 130 243 270
        roundedPhyRateEntryMap.put(McsType.MCS_N_20,
                new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(130), M2Kbps(243), M2Kbps(270) }));
        
        // HT MCS 21  3   64-QAM  2/3 156 173.3   324 360
        roundedPhyRateEntryMap.put(McsType.MCS_N_21,
                new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(174), M2Kbps(324), M2Kbps(360) }));

        // HT MCS 22  3   64-QAM  3/4 175.5   195 364.5   405
        roundedPhyRateEntryMap.put(McsType.MCS_N_22,
                new PhyRateEntry(new Long[] { M2Kbps(176), M2Kbps(195), M2Kbps(365), M2Kbps(405) }));

        // HT MCS 23  3   64-QAM  5/6 195 216.7   405 450
        roundedPhyRateEntryMap.put(McsType.MCS_N_23,
                new PhyRateEntry(new Long[] { M2Kbps(195), M2Kbps(217), M2Kbps(405), M2Kbps(450) }));

        // HT MCS 24  4   BPSK    1/2 26  28.8    54  60
        roundedPhyRateEntryMap.put(McsType.MCS_N_24,
                new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(29), M2Kbps(54), M2Kbps(60) }));

        // HT MCS 25  4   QPSK    1/2 52  57.6    108 120
        roundedPhyRateEntryMap.put(McsType.MCS_N_25,
                new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(58), M2Kbps(108), M2Kbps(120) }));

        // HT MCS 26  4   QPSK    3/4 78  86.8    162 180
        roundedPhyRateEntryMap.put(McsType.MCS_N_26,
                new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(87), M2Kbps(162), M2Kbps(180) }));

        // HT MCS 27  4   16-QAM  1/2 104 115.6   216 240
        roundedPhyRateEntryMap.put(McsType.MCS_N_27,
                new PhyRateEntry(new Long[] { M2Kbps(104), M2Kbps(116), M2Kbps(216), M2Kbps(240) }));

        // HT MCS 28  4   16-QAM  3/4 156 173.2   324 360
        roundedPhyRateEntryMap.put(McsType.MCS_N_28,
                new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(1732, 10), M2Kbps(324), M2Kbps(360) }));

        // HT MCS 29  4   64-QAM  2/3 208 231.2   432 480
        roundedPhyRateEntryMap.put(McsType.MCS_N_29,
                new PhyRateEntry(new Long[] { M2Kbps(208), M2Kbps(232), M2Kbps(432), M2Kbps(480) }));

        // HT MCS 30  4   64-QAM  3/4 234 260 486 540
        roundedPhyRateEntryMap.put(McsType.MCS_N_30,
                new PhyRateEntry(new Long[] { M2Kbps(234), M2Kbps(260), M2Kbps(486), M2Kbps(540) }));

        // HT MCS 31  4   64-QAM  5/6 260 288.8   540 600
        roundedPhyRateEntryMap.put(McsType.MCS_N_31,
                new PhyRateEntry(new Long[] { M2Kbps(260), M2Kbps(289), M2Kbps(540), M2Kbps(600) }));



        // VHT MCS 0 (SS=1): 6.5, 7.2, 13.5, 15, 29.3, 32.5, 58.5, 65
        roundedPhyRateEntryMap.put(McsType.MCS_AC_1x1_0, new PhyRateEntry(new Long[] { M2Kbps(7), M2Kbps(8),
                M2Kbps(14), M2Kbps(15), M2Kbps(30), M2Kbps(33), M2Kbps(59), M2Kbps(65) }));
        // VHT MCS 1 (SS=1): 13, 14.4, 27, 30, 58.5, 65, 117, 130
        roundedPhyRateEntryMap.put(McsType.MCS_AC_1x1_1, new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(15), M2Kbps(27),
                M2Kbps(30), M2Kbps(59), M2Kbps(65), M2Kbps(117), M2Kbps(130) }));
        // VHT MCS 2 (SS=1): 19.5, 21.7, 40.5, 45, 87.8, 97.5, 175.5, 195
        roundedPhyRateEntryMap.put(McsType.MCS_AC_1x1_2, new PhyRateEntry(new Long[] { M2Kbps(20), M2Kbps(22),
                M2Kbps(41), M2Kbps(45), M2Kbps(88), M2Kbps(98), M2Kbps(176), M2Kbps(195) }));
        // VHT MCS 3 (SS=1): 26, 28.9 54, 60, 117, 130, 234, 260
        roundedPhyRateEntryMap.put(McsType.MCS_AC_1x1_3, new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(29), M2Kbps(54),
                M2Kbps(60), M2Kbps(117), M2Kbps(130), M2Kbps(234), M2Kbps(260) }));
        // VHT MCS 4 (SS=1): 39, 43.3, 81, 90, 175.5 195, 351, 390
        roundedPhyRateEntryMap.put(McsType.MCS_AC_1x1_4, new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(44), M2Kbps(81),
                M2Kbps(90), M2Kbps(176), M2Kbps(195), M2Kbps(351), M2Kbps(390) }));
        // VHT MCS 5 (SS=1): 52, 57.8, 108, 120, 234, 260, 468, 520
        roundedPhyRateEntryMap.put(McsType.MCS_AC_1x1_5, new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(58),
                M2Kbps(108), M2Kbps(120), M2Kbps(234), M2Kbps(260), M2Kbps(468), M2Kbps(520) }));
        // VHT MCS 6 (SS=1): 58.5, 65, 121.5, 135, 263.3, 292.5, 526.5, 585
        roundedPhyRateEntryMap.put(McsType.MCS_AC_1x1_6, new PhyRateEntry(new Long[] { M2Kbps(59), M2Kbps(65),
                M2Kbps(122), M2Kbps(135), M2Kbps(264), M2Kbps(293), M2Kbps(527), M2Kbps(585) }));
        // VHT MCS 7 (SS=1): 65, 72.2, 135, 150, 292.5, 325, 585, 650
        roundedPhyRateEntryMap.put(McsType.MCS_AC_1x1_7, new PhyRateEntry(new Long[] { M2Kbps(65), M2Kbps(73),
                M2Kbps(135), M2Kbps(150), M2Kbps(293), M2Kbps(325), M2Kbps(585), M2Kbps(650) }));
        // VHT MCS 8 (SS=1): 78, 86.7, 162, 180, 351, 390, 702, 780
        roundedPhyRateEntryMap.put(McsType.MCS_AC_1x1_8, new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(87),
                M2Kbps(162), M2Kbps(180), M2Kbps(351), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        // VHT MCS 9 (SS=1): n/a, n/a, 180, 200, 390, 433.3, 780, 866.7
        roundedPhyRateEntryMap.put(McsType.MCS_AC_1x1_9, new PhyRateEntry(new Long[] { null, null, M2Kbps(180), M2Kbps(200),
                M2Kbps(390), M2Kbps(434), M2Kbps(780), M2Kbps(867) }));

        // VHT MCS 0 (SS=2): 13, 14.4, 27, 30, 58.5, 65, 117, 130
        roundedPhyRateEntryMap.put(McsType.MCS_AC_2x2_0, new PhyRateEntry(new Long[] { M2Kbps(13), M2Kbps(15), M2Kbps(27),
                M2Kbps(30), M2Kbps(59), M2Kbps(65), M2Kbps(117), M2Kbps(130) }));
        // VHT MCS 1 (SS=2): 26, 28.9, 54, 60, 117, 130, 234, 260
        roundedPhyRateEntryMap.put(McsType.MCS_AC_2x2_1, new PhyRateEntry(new Long[] { M2Kbps(26), M2Kbps(29), M2Kbps(54),
                M2Kbps(60), M2Kbps(117), M2Kbps(130), M2Kbps(234), M2Kbps(260) }));
        // VHT MCS 2 (SS=2): 39, 43.3, 81, 90, 175.5, 195, 351, 390
        roundedPhyRateEntryMap.put(McsType.MCS_AC_2x2_2, new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(44), M2Kbps(81),
                M2Kbps(90), M2Kbps(176), M2Kbps(195), M2Kbps(351), M2Kbps(390) }));
        // VHT MCS 3 (SS=2): 52, 57.8, 108, 120, 234, 260, 468, 520
        roundedPhyRateEntryMap.put(McsType.MCS_AC_2x2_3, new PhyRateEntry(new Long[] { M2Kbps(52), M2Kbps(58),
                M2Kbps(108), M2Kbps(120), M2Kbps(234), M2Kbps(260), M2Kbps(468), M2Kbps(520) }));
        // VHT MCS 4 (SS=2): 78, 86.7, 162, 180, 351, 390, 702, 780
        roundedPhyRateEntryMap.put(McsType.MCS_AC_2x2_4, new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(87),
                M2Kbps(162), M2Kbps(180), M2Kbps(351), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        // VHT MCS 5 (SS=2): 104, 115.6, 216, 240, 468, 520, 936, 1040
        roundedPhyRateEntryMap.put(McsType.MCS_AC_2x2_5, new PhyRateEntry(new Long[] { M2Kbps(104), M2Kbps(116),
                M2Kbps(216), M2Kbps(240), M2Kbps(468), M2Kbps(520), M2Kbps(936), M2Kbps(1040) }));
        // VHT MCS 6 (SS=2): 117, 130.3, 243, 270, 526.5, 585, 1053, 1170
        roundedPhyRateEntryMap.put(McsType.MCS_AC_2x2_6, new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(131),
                M2Kbps(243), M2Kbps(270), M2Kbps(527), M2Kbps(585), M2Kbps(1053), M2Kbps(1170) }));
        // VHT MCS 7 (SS=2): 130, 144.4, 270, 300, 585, 650, 1170, 1300
        roundedPhyRateEntryMap.put(McsType.MCS_AC_2x2_7, new PhyRateEntry(new Long[] { M2Kbps(130), M2Kbps(145),
                M2Kbps(270), M2Kbps(300), M2Kbps(585), M2Kbps(650), M2Kbps(1170), M2Kbps(1300) }));
        // VHT MCS 8 (SS=2): 156, 173.3, 324, 360, 702 780, 1404, 1560
        roundedPhyRateEntryMap.put(McsType.MCS_AC_2x2_8, new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(174),
                M2Kbps(324), M2Kbps(360), M2Kbps(702), M2Kbps(780), M2Kbps(1404), M2Kbps(1560) }));
        // VHT MCS 9 (SS=2): n/a, n/a, 360, 400, 780, 866.7, 1560, 1733.3
        roundedPhyRateEntryMap.put(McsType.MCS_AC_2x2_9, new PhyRateEntry(new Long[] { null, null, M2Kbps(360), M2Kbps(400),
                M2Kbps(780), M2Kbps(867), M2Kbps(1560), M2Kbps(1734) }));

        // VHT MCS 0 (SS=3): 19.5, 21.7, 40.5, 45, 87.8, 97.5, 175.5, 195
        roundedPhyRateEntryMap.put(McsType.MCS_AC_3x3_0, new PhyRateEntry(new Long[] { M2Kbps(20), M2Kbps(22),
                M2Kbps(41), M2Kbps(45), M2Kbps(88), M2Kbps(98), M2Kbps(176), M2Kbps(195) }));
        // VHT MCS 1 (SS=3): 39,43.3,81,90,175.5,195,351,390
        roundedPhyRateEntryMap.put(McsType.MCS_AC_3x3_1, new PhyRateEntry(new Long[] { M2Kbps(39), M2Kbps(44),
                M2Kbps(81), M2Kbps(90), M2Kbps(176), M2Kbps(195), M2Kbps(351), M2Kbps(390) }));
        // VHT MCS 2 (SS=3): 58.5,65,121.5,135,263.3,292.5,526.5,585
        roundedPhyRateEntryMap.put(McsType.MCS_AC_3x3_2, new PhyRateEntry(new Long[] { M2Kbps(59), M2Kbps(65),
                M2Kbps(122), M2Kbps(135), M2Kbps(264), M2Kbps(293), M2Kbps(527), M2Kbps(585) }));
        // VHT MCS 3 (SS=3): 78,86.7,162,180,351,390,702,780
        roundedPhyRateEntryMap.put(McsType.MCS_AC_3x3_3, new PhyRateEntry(new Long[] { M2Kbps(78), M2Kbps(87),
                M2Kbps(162), M2Kbps(180), M2Kbps(351), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        // VHT MCS 4 (SS=3): 117,130,243,270,526.5,585,1053,1170
        roundedPhyRateEntryMap.put(McsType.MCS_AC_3x3_4, new PhyRateEntry(new Long[] { M2Kbps(117), M2Kbps(130),
                M2Kbps(243), M2Kbps(270), M2Kbps(527), M2Kbps(585), M2Kbps(1053), M2Kbps(1170) }));
        // VHT MCS 5 (SS=3): 156,173.3,324,360,702,780,1404,1560
        roundedPhyRateEntryMap.put(McsType.MCS_AC_3x3_5, new PhyRateEntry(new Long[] { M2Kbps(156), M2Kbps(174),
                M2Kbps(324), M2Kbps(360), M2Kbps(702), M2Kbps(780), M2Kbps(1404), M2Kbps(1560) }));
        // VHT MCS 6 (SS=3): 175.5,195,364.5,405,n/a,n/a,1579.5,1755
        roundedPhyRateEntryMap.put(McsType.MCS_AC_3x3_6, new PhyRateEntry(new Long[] { M2Kbps(176), M2Kbps(195),
                M2Kbps(365), M2Kbps(405), null, null, M2Kbps(1580), M2Kbps(1755) }));
        // VHT MCS 7 (SS=3): 195,216.7,405,450,877.5,975,1755,1950
        roundedPhyRateEntryMap.put(McsType.MCS_AC_3x3_7, new PhyRateEntry(new Long[] { M2Kbps(195), M2Kbps(217),
                M2Kbps(405), M2Kbps(450), M2Kbps(878), M2Kbps(975), M2Kbps(1755), M2Kbps(1950) }));
        // VHT MCS 8 (SS=3): 234,260,486,540,1053,1170,2106,2340
        roundedPhyRateEntryMap.put(McsType.MCS_AC_3x3_8, new PhyRateEntry(new Long[] { M2Kbps(234), M2Kbps(260),
                M2Kbps(486), M2Kbps(540), M2Kbps(1053), M2Kbps(1170), M2Kbps(2106), M2Kbps(2340) }));
        // VHT MCS 9 (SS=3): 260,288.9,540,600,1170,1300,n/a,n/a
        roundedPhyRateEntryMap.put(McsType.MCS_AC_3x3_9, new PhyRateEntry(new Long[] { M2Kbps(260), M2Kbps(289),
                M2Kbps(540), M2Kbps(600), M2Kbps(1170), M2Kbps(1300) }));


        //  VHT MCS (SS=4) 0   4   BPSK    1/2 26  28.8    54  60  117.2   130 234 260
        roundedPhyRateEntryMap.put(McsType.MCS_AC_4x4_0, new PhyRateEntry(new Long[] { 
                M2Kbps(26), M2Kbps(29), M2Kbps(54), M2Kbps(60), M2Kbps(118), M2Kbps(130), M2Kbps(234), M2Kbps(260) }));
        
        //  VHT MCS (SS=4) 1   4   QPSK    1/2 52  57.6    108 120 234 260 468 520
        roundedPhyRateEntryMap.put(McsType.MCS_AC_4x4_1, new PhyRateEntry(new Long[] { 
                M2Kbps(52), M2Kbps(58), M2Kbps(108), M2Kbps(120), M2Kbps(234), M2Kbps(260), M2Kbps(468), M2Kbps(520) }));
        
        //  VHT MCS (SS=4) 2   4   QPSK    3/4 78  86.8    162 180 351.2   390 702 780
        roundedPhyRateEntryMap.put(McsType.MCS_AC_4x4_2, new PhyRateEntry(new Long[] { 
                M2Kbps(78), M2Kbps(87), M2Kbps(162), M2Kbps(180), M2Kbps(352), M2Kbps(390), M2Kbps(702), M2Kbps(780) }));
        
        //  VHT MCS (SS=4) 3   4   16-QAM  1/2 104 115.6   216 240 468 520 936 1040
        roundedPhyRateEntryMap.put(McsType.MCS_AC_4x4_3, new PhyRateEntry(new Long[] { 
                M2Kbps(104), M2Kbps(116), M2Kbps(216), M2Kbps(240), M2Kbps(468), M2Kbps(520), M2Kbps(936), M2Kbps(1040) }));
        
        //  VHT MCS (SS=4) 4   4   16-QAM  3/4 156 173.2   324 360 702 780 1404    1560
        roundedPhyRateEntryMap.put(McsType.MCS_AC_4x4_4, new PhyRateEntry(new Long[] { 
                M2Kbps(156), M2Kbps(174), M2Kbps(324), M2Kbps(360), M2Kbps(702), M2Kbps(780), M2Kbps(1404), M2Kbps(1560) }));
        
        //  VHT MCS (SS=4) 5   4   64-QAM  2/3 208 231.2   432 480 936 1040    1872    2080
        roundedPhyRateEntryMap.put(McsType.MCS_AC_4x4_5, new PhyRateEntry(new Long[] { 
                M2Kbps(208), M2Kbps(232), M2Kbps(432), M2Kbps(480), M2Kbps(936), M2Kbps(1040), M2Kbps(1872), M2Kbps(2080) }));
        
        //  VHT MCS (SS=4) 6   4   64-QAM  3/4 234 260 486 540 1053.2  1170    2106    2340
        roundedPhyRateEntryMap.put(McsType.MCS_AC_4x4_6, new PhyRateEntry(new Long[] { 
                M2Kbps(234), M2Kbps(260), M2Kbps(486), M2Kbps(540), M2Kbps(1054), M2Kbps(1170), M2Kbps(2106), M2Kbps(2340) }));
        
        //  VHT MCS (SS=4) 7   4   64-QAM  5/6 260 288.8   540 600 1170    1300    2340    2600
        roundedPhyRateEntryMap.put(McsType.MCS_AC_4x4_7, new PhyRateEntry(new Long[] { 
                M2Kbps(260), M2Kbps(289), M2Kbps(540), M2Kbps(600), M2Kbps(1170), M2Kbps(1300), M2Kbps(2340), M2Kbps(2600) }));
        
        //  VHT MCS (SS=4) 8   4   256-QAM 3/4 312 346.8   648 720 1404    1560    2808    3120
        roundedPhyRateEntryMap.put(McsType.MCS_AC_4x4_8, new PhyRateEntry(new Long[] { 
                M2Kbps(312), M2Kbps(347), M2Kbps(648), M2Kbps(720), M2Kbps(1404), M2Kbps(1560), M2Kbps(2808), M2Kbps(3120) }));
        
        //  VHT MCS (SS=4) 9   4   256-QAM 5/6 N/A N/A 720 800 1560    1733.2  3120    3466.8
        roundedPhyRateEntryMap.put(McsType.MCS_AC_4x4_9, new PhyRateEntry(new Long[] { 
                null, null, M2Kbps(720), M2Kbps(800), M2Kbps(1560), M2Kbps(1734), M2Kbps(3120), M2Kbps(3467) }));

        
    }

    /**
     * Hide constructor
     */
    private ClientRadioUtils() {

    }

    /**
     * Convert mbps to kbps
     * 
     * @param mbps
     * @return kbps
     */
    private static Long M2Kbps(int mbps) {
        return KBITS_IN_MBITS * mbps;
    }

    /**
     * Avoid decimal math
     * 
     * @param mbps
     * @param div
     * @return kbps
     */
    private static Long M2Kbps(int mbps, int div) {
        return KBITS_IN_MBITS * mbps / div;
    }

    /**
     * Get the physical rate based on the McsType, Channel Bandwidth and
     * GuardInterval. Result is in kilobit per second.
     * 
     * <b>1 megabit per second = 1000 kilobit per second.</b>
     * 
     * @param mcsNum
     * @param bandwidth
     * @param guardInterval
     * @return physical rate in Kilobit per Second. Return null if required
     *         field are missing.
     */
    public static Long getPhyRateKb(McsType mcsNum, ChannelBandwidth bandwidth, GuardInterval guardInterval) {
        if ((null == mcsNum) || (null == bandwidth) || (null == guardInterval)) {
            return null;
        }
        PhyRateEntry entry = phyRateEntryMap.get(mcsNum);
        if (null == entry) {
            // unsupported mcsNum
            return null;
        }
        return entry.getPhyRate(bandwidth, guardInterval);
    }

    public static McsType getMcs(ChannelBandwidth channelBandwidth, GuardInterval guardInterval, int numSpatialStreams, int dataRate, boolean isVht, boolean isHt){
        // DataRate Values are populated as 20Mzh LGI, 20Mhz SGI, 40Mhz LGI, 40Mhz LGI, 80Mhz LGI, 80 Mhz SIG, 160Mhz LGI, 160Mhz SGI
        int idx = -1;
        switch(channelBandwidth){
        case is20MHz:
            if(guardInterval==GuardInterval.LGI){
                idx = 0;
            }else{
                idx = 1;
            }
            break;
        case is40MHz:
            if(guardInterval==GuardInterval.LGI){
                idx = 2;
            }else{
                idx = 3;
            }
            break;
        case is80MHz:
            if(guardInterval==GuardInterval.LGI){
                idx = 4;
            }else{
                idx = 5;
            }
            break;
        case is160MHz:
            if(guardInterval==GuardInterval.LGI){
                idx = 6;
            }else{
                idx = 7;
            }            
            break;
        case UNSUPPORTED:
            break;
        case auto:
            break;
        default:
            break;
        }
        
        if(idx==-1){
            idx=0;
        }
        
        
        McsType mcsType;
        mcsType = getMcs(phyRateEntryMap, idx, channelBandwidth, guardInterval, numSpatialStreams, dataRate, isVht, isHt);
        
        //if we did not find a mapping, it could be because the datarate was truncated, let's look again in a truncatedDatarate map
        if(mcsType == McsType.UNSUPPORTED){
            mcsType = getMcs(truncatedPhyRateEntryMap, idx, channelBandwidth, guardInterval, numSpatialStreams, dataRate, isVht, isHt);
        }
        
        if(mcsType == McsType.UNSUPPORTED){
            //if we did not find a mapping, it could be because the datarate was rounded, let's look again in a truncatedDatarate map
            mcsType = getMcs(roundedPhyRateEntryMap, idx, channelBandwidth, guardInterval, numSpatialStreams, dataRate, isVht, isHt);
        }

        return mcsType;
    }
    
    public static void main(String[] args) {
        boolean isVht = true;
        boolean isHt = false;
        System.out.println("Calculated LastTxMcsIdx="+ getMcs(ChannelBandwidth.is80MHz, GuardInterval.SGI, 1, 433, isVht, isHt)+" from a-VHT-80sgi-1ss with datarate 433");
    }
    
    
    private static McsType getMcs(Map<McsType, PhyRateEntry> phyRatesMap, int idx, ChannelBandwidth channelBandwidth, GuardInterval guardInterval, int numSpatialStreams, int dataRate, boolean isVht, boolean isHt){
        Long[] phyRate;
        McsType mcsType;

        for(Map.Entry<McsType, PhyRateEntry> entry: phyRatesMap.entrySet()){
            phyRate = entry.getValue().phyRate;
            
            if(idx>=phyRate.length){
                continue;
            }
            
            mcsType = entry.getKey();
            
            if(mcsType.isHt()==isHt && mcsType.isVht()==isVht && mcsType.getNumSpacialStreams()==numSpatialStreams){
                if(phyRate[idx]!=null && phyRate[idx].equals(M2Kbps(dataRate))){
                    return mcsType;
                }
            }
        }


        //if we did not find a mapping, it could be because the datarate was truncated/rounded, let's look again in an alternative map
        for(Map.Entry<McsType, PhyRateEntry> entry: phyRatesMap.entrySet()){
            phyRate = entry.getValue().phyRate;
            
            if(idx>=phyRate.length){
                continue;
            }
            
            mcsType = entry.getKey();
            
            if(mcsType.isHt()==isHt && mcsType.isVht()==isVht && mcsType.getNumSpacialStreams()==numSpatialStreams){
                if(phyRate[idx]!=null && phyRate[idx].equals(M2Kbps(dataRate))){
                    return mcsType;
                }
            }
        }

        //if we did not find a mapping, it could be because the SGI/LGI was incorrectly supplied, let's look again without GI filter
        for(Map.Entry<McsType, PhyRateEntry> entry: phyRatesMap.entrySet()){
            phyRate = entry.getValue().phyRate;
            mcsType = entry.getKey();
            
            if(mcsType.isHt()==isHt && mcsType.isVht()==isVht && mcsType.getNumSpacialStreams()==numSpatialStreams){
                for(int i = 0; i<phyRate.length; i++){
                    if(phyRate[i]!=null && phyRate[i].equals(M2Kbps(dataRate))){
                        return mcsType;
                    }
                }
            }
        }

        //if we did not find a mapping, let's look again without any filters
        for(Map.Entry<McsType, PhyRateEntry> entry: phyRatesMap.entrySet()){
            phyRate = entry.getValue().phyRate;
            mcsType = entry.getKey();
            
            for(int i = 0; i<phyRate.length; i++){
                if(phyRate[i]!=null && phyRate[i].equals(M2Kbps(dataRate))){
                    return mcsType;
                }
            }
        }

        return McsType.UNSUPPORTED;
        
    }

    /**
     * Get the default channel bandwidth based on radio type.
     * 
     * @param radioType
     * @return default channel bandwidth
     */
    public static ChannelBandwidth getDefaultChannelBandwidth(RadioType radioType) {
        if (null == radioType) {
            return null;
        }
        switch (radioType) {
        case is2dot4GHz:
            return ChannelBandwidth.is20MHz;
        case is5GHz:
            return ChannelBandwidth.is40MHz;
        default:
            return null;
        }
    }
}

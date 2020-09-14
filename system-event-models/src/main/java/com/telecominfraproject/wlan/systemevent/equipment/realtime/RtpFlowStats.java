package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class RtpFlowStats extends BaseJsonModel {
    /**
     * Raw value contains 1 bit flag and 7 bit block PT, only use when Primary
     * CODEC is redundant (RFC2198)
     * 
     *
     */
    public static class RTPBlockCodec extends BaseJsonModel {
        /**
         * 
         */
        private static final long serialVersionUID = 6404053681806088215L;
        private static final int firstBitMask = 0b10000000;
        private static final int ptValueMask = 0b1111111;
        /**
         * 
         */
        private int rawValue;

        public RTPBlockCodec() {
        }

        public RTPBlockCodec(int raw) {
            this.rawValue = raw;
        }

        public RTPBlockCodec(boolean isLast, int codec) {
            this.rawValue = (ptValueMask & codec);
            if (!isLast) {
                this.rawValue |= firstBitMask;
            }
        }

        /**
         * Decode the 7 bit payload type
         * 
         * @return decoded playload type.
         */
        @JsonIgnore
        public int getPT() {
            return (rawValue & ptValueMask);
        }

        public int getRawValue() {
            return rawValue;
        }

        /**
         * Test if this the last Codec based on the Fist bit value in the raw
         * value.
         * 
         * @return true,
         */
        @JsonIgnore
        public boolean isLast() {
            return (0 == (firstBitMask & rawValue));
        }

        public void setRawValue(int rawValue) {
            this.rawValue = rawValue;
        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = 3460624975215785417L;

    private RtpFlowDirection direction;
    private RtpFlowType flowType;
    private Integer latency;
    private Integer jitter;
    @Deprecated
    private Integer packetLossPercentage;
    private Integer packetLossConsecutive;
    private Integer codec;
    private Integer mosMultipliedBy100;
    private byte[] blockCodecs;
    /**
     * Total number of RTP packet 
     */
    private Integer totalPacket;
    /**
     * Total number of RTP packet lost
     */
    private Integer totalPacketLost;
    /**
     * First sequence number for RTP packet, used to calculate consecutive packet lost during Roaming.
     */
    private Integer firstRTPSeq;
    /**
     * Last sequence number for RTP packet, used to calculate consecutive packet lost during Roaming.
     */
    private Integer lastRTPSeq;
    
    private Integer statsIndex;

    public RtpFlowStats() {
        // nothing
    }

    public RtpFlowStats clone() {
        RtpFlowStats result = (RtpFlowStats) super.clone();
        if (null != result.blockCodecs) {
            result.blockCodecs = Arrays.copyOf(this.blockCodecs, this.blockCodecs.length);
        }
        return result;
    }

    /* (non-Javadoc)
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
        if (!(obj instanceof RtpFlowStats)) {
            return false;
        }
        RtpFlowStats other = (RtpFlowStats) obj;
        if (!Arrays.equals(blockCodecs, other.blockCodecs)) {
            return false;
        }
        if (codec == null) {
            if (other.codec != null) {
                return false;
            }
        } else if (!codec.equals(other.codec)) {
            return false;
        }
        if (direction != other.direction) {
            return false;
        }
        if (firstRTPSeq == null) {
            if (other.firstRTPSeq != null) {
                return false;
            }
        } else if (!firstRTPSeq.equals(other.firstRTPSeq)) {
            return false;
        }
        if (flowType != other.flowType) {
            return false;
        }
        if (jitter == null) {
            if (other.jitter != null) {
                return false;
            }
        } else if (!jitter.equals(other.jitter)) {
            return false;
        }
        if (lastRTPSeq == null) {
            if (other.lastRTPSeq != null) {
                return false;
            }
        } else if (!lastRTPSeq.equals(other.lastRTPSeq)) {
            return false;
        }
        if (latency == null) {
            if (other.latency != null) {
                return false;
            }
        } else if (!latency.equals(other.latency)) {
            return false;
        }
        if (mosMultipliedBy100 == null) {
            if (other.mosMultipliedBy100 != null) {
                return false;
            }
        } else if (!mosMultipliedBy100.equals(other.mosMultipliedBy100)) {
            return false;
        }
        if (packetLossConsecutive == null) {
            if (other.packetLossConsecutive != null) {
                return false;
            }
        } else if (!packetLossConsecutive.equals(other.packetLossConsecutive)) {
            return false;
        }
        if (packetLossPercentage == null) {
            if (other.packetLossPercentage != null) {
                return false;
            }
        } else if (!packetLossPercentage.equals(other.packetLossPercentage)) {
            return false;
        }
        if (statsIndex == null) {
            if (other.statsIndex != null) {
                return false;
            }
        } else if (!statsIndex.equals(other.statsIndex)) {
            return false;
        }
        if (totalPacket == null) {
            if (other.totalPacket != null) {
                return false;
            }
        } else if (!totalPacket.equals(other.totalPacket)) {
            return false;
        }
        if (totalPacketLost == null) {
            if (other.totalPacketLost != null) {
                return false;
            }
        } else if (!totalPacketLost.equals(other.totalPacketLost)) {
            return false;
        }
        return true;
    }

    /**
     * Used for JSON. use {@link #getDecodedBlockCodecs()}
     * 
     * @return raw value
     */
    public byte[] getBlockCodecs() {
        return this.blockCodecs;
    }

    @JsonIgnore
    public int getBlockCodecCount() {
        if (null != blockCodecs) {
            return blockCodecs.length;
        }
        return 0;
    }

    public Integer getCodec() {
        return codec;
    }

    @JsonIgnore
    public List<RTPBlockCodec> getDecodedBlockCodecs() {
        if (null == this.blockCodecs) {
            return Collections.emptyList();
        }
        List<RTPBlockCodec> result = new ArrayList<>(this.blockCodecs.length);
        for (byte raw : this.blockCodecs) {
            result.add(new RTPBlockCodec(raw));
        }
        return result;
    }

    public RtpFlowDirection getDirection() {
        return direction;
    }

    public RtpFlowType getFlowType() {
        return flowType;
    }

    public Integer getJitter() {
        return jitter;
    }

    public Integer getLatency() {
        return latency;
    }

    public Integer getMosMultipliedBy100() {
        return mosMultipliedBy100;
    }

    public Integer getPacketLossConsecutive() {
        return packetLossConsecutive;
    }

    public Integer getPacketLossPercentage() {
        return packetLossPercentage;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(blockCodecs);
        result = prime * result + ((codec == null) ? 0 : codec.hashCode());
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + ((firstRTPSeq == null) ? 0 : firstRTPSeq.hashCode());
        result = prime * result + ((flowType == null) ? 0 : flowType.hashCode());
        result = prime * result + ((jitter == null) ? 0 : jitter.hashCode());
        result = prime * result + ((lastRTPSeq == null) ? 0 : lastRTPSeq.hashCode());
        result = prime * result + ((latency == null) ? 0 : latency.hashCode());
        result = prime * result + ((mosMultipliedBy100 == null) ? 0 : mosMultipliedBy100.hashCode());
        result = prime * result + ((packetLossConsecutive == null) ? 0 : packetLossConsecutive.hashCode());
        result = prime * result + ((packetLossPercentage == null) ? 0 : packetLossPercentage.hashCode());
        result = prime * result + ((statsIndex == null) ? 0 : statsIndex.hashCode());
        result = prime * result + ((totalPacket == null) ? 0 : totalPacket.hashCode());
        result = prime * result + ((totalPacketLost == null) ? 0 : totalPacketLost.hashCode());
        return result;
    }

    public void setBlockCodecs(byte[] blockCodecs) {
        this.blockCodecs = blockCodecs;
    }

    public void setCodec(Integer codec) {
        this.codec = codec;
    }

    public void setDirection(RtpFlowDirection direction) {
        this.direction = direction;
    }

    public void setFlowType(RtpFlowType flowType) {
        this.flowType = flowType;
    }

    public void setJitter(Integer jitter) {
        this.jitter = jitter;
    }

    public void setLatency(Integer latency) {
        this.latency = latency;
    }

    public void setMosMultipliedBy100(Integer mosMultipliedBy100) {
        this.mosMultipliedBy100 = mosMultipliedBy100;
    }

    public void setPacketLossConsecutive(Integer packetLossConsecutive) {
        this.packetLossConsecutive = packetLossConsecutive;
    }

    public void setPacketLossPercentage(Integer packetLossPercentage) {
        this.packetLossPercentage = packetLossPercentage;
    }

    public Integer getTotalPacket() {
        return totalPacket;
    }

    public void setTotalPacket(Integer totalPacket) {
        this.totalPacket = totalPacket;
    }

    public Integer getTotalPacketLost() {
        return totalPacketLost;
    }

    public void setTotalPacketLost(Integer totalPacketLost) {
        this.totalPacketLost = totalPacketLost;
    }

    public Integer getFirstRTPSeq() {
        return firstRTPSeq;
    }

    public void setFirstRTPSeq(Integer firstRTPSeq) {
        this.firstRTPSeq = firstRTPSeq;
    }

    public Integer getLastRTPSeq() {
        return lastRTPSeq;
    }

    public void setLastRTPSeq(Integer lastRTPSeq) {
        this.lastRTPSeq = lastRTPSeq;
    }

    public Integer getStatsIndex() {
        return statsIndex;
    }

    public void setStatsIndex(Integer statsIndex) {
        this.statsIndex = statsIndex;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (RtpFlowDirection.isUnsupported(direction) || RtpFlowType.isUnsupported(flowType)) {
            return true;
        }
        return false;
    }
    
}

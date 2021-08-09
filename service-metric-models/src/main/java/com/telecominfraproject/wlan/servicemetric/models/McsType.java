package com.telecominfraproject.wlan.servicemetric.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * MCS index values
 * @author ekeddy
 *
 */
public enum McsType {
    
    //McsType(int id, boolean isHt, boolean isVht, int numSpacialStreams, int mcsIndex)
    MCS_1  (0, false, false, 1, 1),  // 2.4GHz only
    MCS_2  (1, false, false, 1, 2),  // 2.4GHz only
    MCS_5dot5(2, false, false, 1, 5),  // 2.4GHz only
    MCS_11 (3, false, false, 1, 11),  // 2.4GHz only
    MCS_6  (4, false, false, 1, 13),
    MCS_9  (5, false, false, 1, 16),
    MCS_12 (6, false, false, 1, 5),
    MCS_18 (7, false, false, 1, 7),
    MCS_24 (8, false, false, 1, 9),
    MCS_36 (9, false, false, 1, 11),
    MCS_48 (10, false, false, 1, 1),
    MCS_54 (11, false, false, 1, 3),
    MCS_N_0 (12, true, false, 1, 0),
    MCS_N_1 (13, true, false, 1, 1),
    MCS_N_2 (14, true, false, 1, 2),
    MCS_N_3 (15, true, false, 1, 3),
    MCS_N_4 (16, true, false, 1, 4),
    MCS_N_5 (17, true, false, 1, 5),
    MCS_N_6 (18, true, false, 1, 6),
    MCS_N_7 (19, true, false, 1, 7),
    MCS_N_8 (20, true, false, 2, 8),
    MCS_N_9 (21, true, false, 2, 9),
    MCS_N_10(22, true, false, 2, 10),
    MCS_N_11(23, true, false, 2, 11),
    MCS_N_12(24, true, false, 2, 12),
    MCS_N_13(25, true, false, 2, 13),
    MCS_N_14(26, true, false, 2, 14),
    MCS_N_15(27, true, false, 2, 15),
    MCS_AC_1x1_0(28, false, true, 1, 0),
    MCS_AC_1x1_1(29, false, true, 1, 1),
    MCS_AC_1x1_2(30, false, true, 1, 2),
    MCS_AC_1x1_3(31, false, true, 1, 3),
    MCS_AC_1x1_4(32, false, true, 1, 4),
    MCS_AC_1x1_5(33, false, true, 1, 5),
    MCS_AC_1x1_6(34, false, true, 1, 6),
    MCS_AC_1x1_7(35, false, true, 1, 7),
    MCS_AC_1x1_8(36, false, true, 1, 8),
    MCS_AC_1x1_9(37, false, true, 1, 9),
    MCS_AC_2x2_0(38, false, true, 2, 0),
    MCS_AC_2x2_1(39, false, true, 2, 1),
    MCS_AC_2x2_2(40, false, true, 2, 2),
    MCS_AC_2x2_3(41, false, true, 2, 3),
    MCS_AC_2x2_4(42, false, true, 2, 4),
    MCS_AC_2x2_5(43, false, true, 2, 5),
    MCS_AC_2x2_6(44, false, true, 2, 6),
    MCS_AC_2x2_7(45, false, true, 2, 7),
    MCS_AC_2x2_8(46, false, true, 2, 8),
    MCS_AC_2x2_9(47, false, true, 2, 9),
    MCS_AC_3x3_0(48, false, true, 3, 0),
    MCS_AC_3x3_1(49, false, true, 3, 1),
    MCS_AC_3x3_2(50, false, true, 3, 2),
    MCS_AC_3x3_3(51, false, true, 3, 3),
    MCS_AC_3x3_4(52, false, true, 3, 4),
    MCS_AC_3x3_5(53, false, true, 3, 5),
    MCS_AC_3x3_6(54, false, true, 3, 6),
    MCS_AC_3x3_7(55, false, true, 3, 7),
    MCS_AC_3x3_8(56, false, true, 3, 8),
    MCS_AC_3x3_9(57, false, true, 3, 9),
    MCS_N_16(58, true, false, 3, 16),
    MCS_N_17(59, true, false, 3, 17),
    MCS_N_18(60, true, false, 3, 18),
    MCS_N_19(61, true, false, 3, 19),
    MCS_N_20(62, true, false, 3, 20),
    MCS_N_21(63, true, false, 3, 21),
    MCS_N_22(64, true, false, 3, 22),
    MCS_N_23(65, true, false, 3, 23),
    MCS_N_24(66, true, false, 4, 24),
    MCS_N_25(67, true, false, 4, 25),
    MCS_N_26(68, true, false, 4, 26),
    MCS_N_27(69, true, false, 4, 27),
    MCS_N_28(70, true, false, 4, 28),
    MCS_N_29(71, true, false, 4, 29),
    MCS_N_30(72, true, false, 4, 30),
    MCS_N_31(73, true, false, 4, 31),    
    MCS_AC_4x4_0(74, false, true, 4, 0),
    MCS_AC_4x4_1(75, false, true, 4, 1),
    MCS_AC_4x4_2(76, false, true, 4, 2),
    MCS_AC_4x4_3(77, false, true, 4, 3),
    MCS_AC_4x4_4(78, false, true, 4, 4),
    MCS_AC_4x4_5(79, false, true, 4, 5),
    MCS_AC_4x4_6(80, false, true, 4, 6),
    MCS_AC_4x4_7(81, false, true, 4, 7),
    MCS_AC_4x4_8(82, false, true, 4, 8),
    MCS_AC_4x4_9(83, false, true, 4, 9),

    //last used index 83

    UNSUPPORTED(-1, false, false, 0, -1);

    private final int id;
    private final boolean isHt;
    private final boolean isVht;
    private final int numSpacialStreams;
    private final int mcsIndex; 
    
    private static final Map<Integer, McsType> ELEMENTS = new HashMap<>();
    
    private McsType(int id, boolean isHt, boolean isVht, int numSpacialStreams, int mcsIndex){
        this.id = id;
        this.isHt = isHt;
        this.isVht = isVht;
        this.numSpacialStreams = numSpacialStreams;
        this.mcsIndex = mcsIndex;
    }
    
    public int getId(){
        return this.id;
    }
    
    public int getMcsIndex() {
        return mcsIndex;
    }
    
    public int getNumSpacialStreams() {
        return numSpacialStreams;
    }
    
    public boolean isHt() {
        return isHt;
    }
    
    public boolean isVht() {
        return isVht;
    }
    
    public static McsType getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(McsType met : McsType.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static McsType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, McsType.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(McsType value) {
        return UNSUPPORTED.equals(value);
    }
}

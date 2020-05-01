package com.telecominfraproject.wlan.equipment.models;
/**
 * We started seeing a ton of "either use 'auto' or a numeric value".
 * 
 * In that spirit, I wrote this class to encapsulate (and reduce the number of
 * classes with two members for any parameters that can be "auto" like "bool
 * channelAuto; int channelNuber")
 * 
 * @author erikvilleneuve
 *
 */
public class AutoOrManualValue extends AbstractAutoOrManual<Integer> {
    /**
     * 
     */
    private static final long serialVersionUID = -2074132169454513108L;

    // Defaults to "auto" by default
    private AutoOrManualValue() {
        // for serializing
    }

    private AutoOrManualValue(boolean auto, int value) {
        super(auto, value);
    }

    public static AutoOrManualValue createAutomaticInstance(int defaultValue) {
        return new AutoOrManualValue(true, defaultValue);
    }

    public static AutoOrManualValue createManualInstance(int manualValue) {
        return new AutoOrManualValue(false, manualValue);
    }

}

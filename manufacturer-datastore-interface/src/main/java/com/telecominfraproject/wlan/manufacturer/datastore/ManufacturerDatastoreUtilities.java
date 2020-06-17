package com.telecominfraproject.wlan.manufacturer.datastore;

/**
 * Utility Class
 * 
 * @author yongli
 *
 */
public class ManufacturerDatastoreUtilities {
    /**
     * We will construct the manufacture name for Private with Private (OUI)
     */
    public static final String PRIVATE_MANUFACTURER_NAME = "Private";

    private ManufacturerDatastoreUtilities() {

    }

    /**
     * Process Manufacturer name. It will generate a new one if the name is
     * {@value PRIVATE_MANUFACTURER_NAME}. In all other case it will simply
     * trim the string.
     * 
     * @param originOUI
     * @param originName
     * @return formated result
     */
    public static String getByManufacturerName(final String originOUI, final String originName) {
        String result = originName.trim();
        if (PRIVATE_MANUFACTURER_NAME.equalsIgnoreCase(result)) {
            result = String.format("%s (%s)", PRIVATE_MANUFACTURER_NAME, originOUI);
        }
        return result;
    }
}

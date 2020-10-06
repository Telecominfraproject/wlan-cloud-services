package com.telecominfraproject.wlan.profile.passpoint.models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


public class MobileCountryAndNetworkCodes {

    private static final Logger LOG = LoggerFactory.getLogger(MobileCountryAndNetworkCodes.class);

    @Value("${tip.wlan.mobileCountryAndNetworkCodesUrl:https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-cloud-schemas/MobileCountryAndNetworkCodes.txt}")
    private static String mobileCountryAndNetworkCodesUrl;

    @Value("${tip.wlan.mobileCountryAndNetworkCodesLocalPath:./MobileCountryAndNetworkCodes.txt}")
    private static String mobileCountryAndNetworkCodesLocalPath = "./src/main/resources/MobileCountryAndNetworkCodes.txt";

    public static File mccMncCodesFile = new File(mobileCountryAndNetworkCodesLocalPath);

    private static Set<MccMnc> mccMncCodeSet = new HashSet<>();

    static {
        if (mccMncCodesFile.canRead()) {
            try {
                List<String> lines = Files.readAllLines(Paths.get(mobileCountryAndNetworkCodesLocalPath));

                for (String line : lines) {

                    LOG.debug("Line {}", line);
                    String[] tokens = line.split("\t");
                    MccMnc mccMnc = MccMnc.createWithDefaults();
                    try {
                        mccMnc.setMcc(Integer.parseInt(tokens[0]));
                        mccMnc.setMnc(Integer.parseInt(tokens[1]));
                    } catch (NumberFormatException e) {
                        LOG.info("Couldn't parse: {}", e.getMessage());
                        continue;
                    }
                    mccMnc.setIso(tokens[2]);
                    mccMnc.setCountry(tokens[3]);
                    mccMnc.setCountryCode(Integer.parseInt(tokens[4]));
                    mccMnc.setNetwork(tokens[5]);
                    mccMncCodeSet.add(mccMnc);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }


    public static String getMobileCountryAndNetworkCodesUrl() {
        return mobileCountryAndNetworkCodesUrl;
    }


    public static void setMobileCountryAndNetworkCodesUrl(String mobileCountryAndNetworkCodesUrl) {
        MobileCountryAndNetworkCodes.mobileCountryAndNetworkCodesUrl = mobileCountryAndNetworkCodesUrl;
    }


    public static String getMobileCountryAndNetworkCodesLocalPath() {
        return mobileCountryAndNetworkCodesLocalPath;
    }


    public static void setMobileCountryAndNetworkCodesLocalPath(String mobileCountryAndNetworkCodesLocalPath) {
        MobileCountryAndNetworkCodes.mobileCountryAndNetworkCodesLocalPath = mobileCountryAndNetworkCodesLocalPath;
    }


    public static File getMccMncCodesFile() {
        return mccMncCodesFile;
    }


    public static void setMccMncCodesFile(File mccMncCodesFile) {
        MobileCountryAndNetworkCodes.mccMncCodesFile = mccMncCodesFile;
    }


    public static Set<MccMnc> getMccMncCodeSet() {
        return mccMncCodeSet;
    }


    public static void setMccMncCodeSet(Set<MccMnc> mccMncCodeSet) {
        MobileCountryAndNetworkCodes.mccMncCodeSet = mccMncCodeSet;
    }

    public static void main(String args[]) {
        getMccMncCodeSet().stream().forEach(m -> {
            LOG.debug("MCC_MNC {}", m.toPrettyString());
        });
    }

}

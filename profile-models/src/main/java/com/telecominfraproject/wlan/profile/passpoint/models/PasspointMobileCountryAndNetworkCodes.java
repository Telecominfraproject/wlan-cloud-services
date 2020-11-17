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


public class PasspointMobileCountryAndNetworkCodes {

    private static final Logger LOG = LoggerFactory.getLogger(PasspointMobileCountryAndNetworkCodes.class);

    @Value("${tip.wlan.mobileCountryAndNetworkCodesUrl:https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-cloud-schemas/PasspointMobileCountryAndNetworkCodes.txt}")
    private static String mobileCountryAndNetworkCodesUrl;

    @Value("${tip.wlan.mobileCountryAndNetworkCodesLocalPath:./PasspointMobileCountryAndNetworkCodes.txt}")
    private static String mobileCountryAndNetworkCodesLocalPath = "./src/main/resources/PasspointMobileCountryAndNetworkCodes.txt";

    public static File mccMncCodesFile = new File(mobileCountryAndNetworkCodesLocalPath);

    private static Set<PasspointMccMnc> mccMncCodeSet = new HashSet<>();

    static {
        if (mccMncCodesFile.canRead()) {
            try {
                List<String> lines = Files.readAllLines(Paths.get(mobileCountryAndNetworkCodesLocalPath));

                for (String line : lines) {

                    LOG.debug("Line {}", line);
                    String[] tokens = line.split("\t");
                    PasspointMccMnc passpointMccMnc = PasspointMccMnc.createWithDefaults();
                    try {
                        passpointMccMnc.setMcc(Integer.parseInt(tokens[0]));
                        passpointMccMnc.setMnc(Integer.parseInt(tokens[1]));
                    } catch (NumberFormatException e) {
                        LOG.info("Couldn't parse: {}", e.getMessage());
                        continue;
                    }
                    passpointMccMnc.setIso(tokens[2]);
                    passpointMccMnc.setCountry(tokens[3]);
                    passpointMccMnc.setCountryCode(Integer.parseInt(tokens[4]));
                    passpointMccMnc.setNetwork(tokens[5]);
                    mccMncCodeSet.add(passpointMccMnc);
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
        PasspointMobileCountryAndNetworkCodes.mobileCountryAndNetworkCodesUrl = mobileCountryAndNetworkCodesUrl;
    }


    public static String getMobileCountryAndNetworkCodesLocalPath() {
        return mobileCountryAndNetworkCodesLocalPath;
    }


    public static void setMobileCountryAndNetworkCodesLocalPath(String mobileCountryAndNetworkCodesLocalPath) {
        PasspointMobileCountryAndNetworkCodes.mobileCountryAndNetworkCodesLocalPath = mobileCountryAndNetworkCodesLocalPath;
    }


    public static File getMccMncCodesFile() {
        return mccMncCodesFile;
    }


    public static void setMccMncCodesFile(File mccMncCodesFile) {
        PasspointMobileCountryAndNetworkCodes.mccMncCodesFile = mccMncCodesFile;
    }


    public static Set<PasspointMccMnc> getMccMncCodeSet() {
        return mccMncCodeSet;
    }


    public static void setMccMncCodeSet(Set<PasspointMccMnc> mccMncCodeSet) {
        PasspointMobileCountryAndNetworkCodes.mccMncCodeSet = mccMncCodeSet;
    }

    public static void main(String args[]) {
        getMccMncCodeSet().stream().forEach(m -> {
            LOG.debug("MCC_MNC {}", m.toPrettyString());
        });
    }

}

package com.telecominfraproject.wlan.profile.passpoint.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class MccMnc extends BaseJsonModel {

    private static final long serialVersionUID = -4481708731385084502L;

    private int mcc;
    private int mnc;
    private String iso;
    private String country;
    private int countryCode;
    private String network;

    public MccMnc() {

    }

    public static MccMnc createWithDefaults() {
        return new MccMnc();
    }

    public MccMnc(int mcc, int mnc) {
        this();
        this.mcc = mcc;
        this.mnc = mnc;
    }

    public int getMcc() {
        return mcc;
    }


    public void setMcc(int mcc) {
        this.mcc = mcc;
    }


    public int getMnc() {
        return mnc;
    }


    public void setMnc(int mnc) {
        this.mnc = mnc;
    }


    public String getIso() {
        return iso;
    }


    public void setIso(String iso) {
        this.iso = iso;
    }


    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public String getMccMncPairing() {
        return getMcc() + "," + getMnc();
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    @Override
    public MccMnc clone() {
        MccMnc ret = (MccMnc) super.clone();
        ret.country = country;
        ret.countryCode = countryCode;
        ret.network = network;
        ret.iso = iso;
        ret.mcc = mcc;
        ret.mnc = mnc;
        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, countryCode, iso, mcc, mnc, network);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MccMnc)) {
            return false;
        }
        MccMnc other = (MccMnc) obj;
        return Objects.equals(country, other.country) && Objects.equals(countryCode, other.countryCode)
                && Objects.equals(iso, other.iso) && mcc == other.mcc && mnc == other.mnc
                && Objects.equals(network, other.network);
    }


}

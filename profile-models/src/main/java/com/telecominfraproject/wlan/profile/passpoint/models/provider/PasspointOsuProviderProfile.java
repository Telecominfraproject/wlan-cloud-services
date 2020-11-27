package com.telecominfraproject.wlan.profile.passpoint.models.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.passpoint.models.PasspointDuple;
import com.telecominfraproject.wlan.profile.passpoint.models.PasspointMccMnc;


public class PasspointOsuProviderProfile extends ProfileDetails
        implements PushableConfiguration<PasspointOsuProviderProfile> {

    private static final Logger LOG = LoggerFactory.getLogger(PasspointOsuProviderProfile.class);

    private String domainName;

    private List<PasspointMccMnc> mccMncList;
    private List<PasspointNaiRealmInformation> naiRealmList;
    private List<PasspointOsuIcon> osuIconList;
    private String osuServerUri;
    private List<PasspointDuple> osuFriendlyName;
    private String osuNaiStandalone; // needs to be unsigned, so do take byte
                                     // value, and make osuNaiStandalone & 0xff;
    private String osuNaiShared;
    private List<Integer> osuMethodList;
    private List<PasspointDuple> osuServiceDescription;
    private static final int MIN_ROAMING_OI_OCTETS = 3;
    private static final int MAX_ROAMING__OI_OCTETS = 15;
    private List<Byte> roamingOi;


    private static final long serialVersionUID = -6146454085334670280L;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    private PasspointOsuProviderProfile() {
        mccMncList = new ArrayList<>();
        naiRealmList = new ArrayList<>();
        osuIconList = new ArrayList<>();
        osuFriendlyName = new ArrayList<>();
        osuMethodList = new ArrayList<>();
        osuServiceDescription = new ArrayList<>();
        roamingOi = new ArrayList<>(MIN_ROAMING_OI_OCTETS);
    }

    public static PasspointOsuProviderProfile createWithDefaults() {

        return new PasspointOsuProviderProfile();
    }


    @Override
    public ProfileType getProfileType() {
        return ProfileType.passpoint_osu_id_provider;
    }


    public List<PasspointMccMnc> getMccMncList() {
        return mccMncList;
    }


    public void setMccMncList(List<PasspointMccMnc> mccMncList) {
        this.mccMncList = mccMncList;
    }


    public List<PasspointNaiRealmInformation> getNaiRealmList() {
        return naiRealmList;
    }


    public void setNaiRealmList(List<PasspointNaiRealmInformation> naiRealmList) {
        this.naiRealmList = naiRealmList;
    }


    public List<PasspointOsuIcon> getOsuIconList() {
        return osuIconList;
    }


    public void setOsuIconList(List<PasspointOsuIcon> osuIconList) {
        this.osuIconList = osuIconList;
    }

    public String getOsuServerUri() {
        return osuServerUri;
    }


    public void setOsuServerUri(String osuServerUri) {
        this.osuServerUri = osuServerUri;
    }


    public List<PasspointDuple> getOsuFriendlyName() {
        return osuFriendlyName;
    }

    public void setOsuFriendlyName(List<PasspointDuple> osuFriendlyName) {
        this.osuFriendlyName = osuFriendlyName;
    }


    public String getOsuNaiStandalone() {
        return osuNaiStandalone;
    }


    public void setOsuNaiStandalone(String osuNaiStandalone) {
        this.osuNaiStandalone = osuNaiStandalone;
    }


    public String getOsuNaiShared() {
        return osuNaiShared;
    }


    public void setOsuNaiShared(String osuNaiShared) {
        this.osuNaiShared = osuNaiShared;
    }


    public List<Integer> getOsuMethodList() {
        return osuMethodList;
    }


    public void setOsuMethodList(List<Integer> osuMethodList) {
        this.osuMethodList = osuMethodList;
    }


    public List<PasspointDuple> getOsuServiceDescription() {
        return osuServiceDescription;
    }


    public void setOsuServiceDescription(List<PasspointDuple> osuServiceDescription) {
        this.osuServiceDescription = osuServiceDescription;
    }

    public List<Byte> getRoamingOi() {
        return roamingOi;
    }


    public void setRoamingOi(List<Byte> roamingOi) {
        this.roamingOi = roamingOi;
    }


    public static int getMinRoamingOiOctets() {
        return MIN_ROAMING_OI_OCTETS;
    }


    public static int getMaxRoamingOiOctets() {
        return MAX_ROAMING__OI_OCTETS;
    }


    @Override
    public boolean needsToBeUpdatedOnDevice(PasspointOsuProviderProfile previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

    public PasspointOsuProviderProfile clone() {
        PasspointOsuProviderProfile ret = (PasspointOsuProviderProfile) super.clone();
        ret.domainName = getDomainName();
        if (mccMncList != null)
            ret.mccMncList = getMccMncList();
        if (naiRealmList != null)
            ret.naiRealmList = getNaiRealmList();
        if (osuIconList != null)
            ret.osuIconList = getOsuIconList();
        if (osuMethodList != null)
            ret.osuMethodList = getOsuMethodList();
        if (osuServiceDescription != null)
            ret.osuServiceDescription = getOsuServiceDescription();
        if (roamingOi != null)
            ret.roamingOi = getRoamingOi();
        ret.osuServerUri = getOsuServerUri();
        ret.osuFriendlyName = getOsuFriendlyName();
        ret.osuNaiShared = getOsuNaiShared();
        ret.osuNaiStandalone = getOsuNaiStandalone();
        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(domainName, mccMncList, naiRealmList, osuFriendlyName, osuIconList, osuMethodList,
                osuNaiShared, osuNaiStandalone, osuServerUri, osuServiceDescription, roamingOi);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PasspointOsuProviderProfile other = (PasspointOsuProviderProfile) obj;
        return Objects.equals(domainName, other.domainName) && Objects.equals(mccMncList, other.mccMncList)
                && Objects.equals(naiRealmList, other.naiRealmList)
                && Objects.equals(osuFriendlyName, other.osuFriendlyName)
                && Objects.equals(osuIconList, other.osuIconList) && Objects.equals(osuMethodList, other.osuMethodList)
                && Objects.equals(osuNaiShared, other.osuNaiShared)
                && Objects.equals(osuNaiStandalone, other.osuNaiStandalone)
                && Objects.equals(osuServerUri, other.osuServerUri)
                && Objects.equals(osuServiceDescription, other.osuServiceDescription)
                && Objects.equals(roamingOi, other.roamingOi);
    }

  


}

package com.telecominfraproject.wlan.profile.passpoint.provider.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.passpoint.models.Hotspot20Duple;
import com.telecominfraproject.wlan.profile.passpoint.models.MccMnc;


public class Hotspot20IdProviderProfile extends ProfileDetails
        implements PushableConfiguration<Hotspot20IdProviderProfile> {

    private static final Logger LOG = LoggerFactory.getLogger(Hotspot20IdProviderProfile.class);

    private String domainName;

    private List<MccMnc> mccMncList;
    private List<NaiRealmInformation> naiRealmList;
    private List<OsuIcon> osuIconList;
    private String radiusProfileAuth;
    private String radiusProfileAccounting;
    private String osuSsid;
    private String osuServerUri;
    private List<Hotspot20Duple> osuFriendlyName;
    private String osuNaiStandalone; // needs to be unsigned, so do take byte
                                     // value, and make osuNaiStandalone & 0xff;
    private String osuNaiShared;
    private List<Integer> osuMethodList;
    private List<Hotspot20Duple> osuServiceDescription;
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

    private Hotspot20IdProviderProfile() {
        mccMncList = new ArrayList<>();
        naiRealmList = new ArrayList<>();
        osuIconList = new ArrayList<>();
        osuFriendlyName = new ArrayList<>();
        osuMethodList = new ArrayList<>();
        osuServiceDescription = new ArrayList<>();
        roamingOi = new ArrayList<>(MIN_ROAMING_OI_OCTETS);
    }

    public static Hotspot20IdProviderProfile createWithDefaults() {

        return new Hotspot20IdProviderProfile();
    }


    @Override
    public ProfileType getProfileType() {
        return ProfileType.id_provider;
    }


    public List<MccMnc> getMccMncList() {
        return mccMncList;
    }


    public void setMccMncList(List<MccMnc> mccMncList) {
        this.mccMncList = mccMncList;
    }


    public List<NaiRealmInformation> getNaiRealmList() {
        return naiRealmList;
    }


    public void setNaiRealmList(List<NaiRealmInformation> naiRealmList) {
        this.naiRealmList = naiRealmList;
    }


    public List<OsuIcon> getOsuIconList() {
        return osuIconList;
    }


    public void setOsuIconList(List<OsuIcon> osuIconList) {
        this.osuIconList = osuIconList;
    }


    public String getRadiusProfileAuth() {
        return radiusProfileAuth;
    }


    public void setRadiusProfileAuth(String radiusProfileAuth) {
        this.radiusProfileAuth = radiusProfileAuth;
    }


    public String getRadiusProfileAccounting() {
        return radiusProfileAccounting;
    }


    public void setRadiusProfileAccounting(String radiusProfileAccounting) {
        this.radiusProfileAccounting = radiusProfileAccounting;
    }


    public String getOsuSsid() {
        return osuSsid;
    }


    public void setOsuSsid(String osuSsid) {
        this.osuSsid = osuSsid;
    }


    public String getOsuServerUri() {
        return osuServerUri;
    }


    public void setOsuServerUri(String osuServerUri) {
        this.osuServerUri = osuServerUri;
    }


    public List<Hotspot20Duple> getOsuFriendlyName() {
        return osuFriendlyName;
    }

    public void setOsuFriendlyName(List<Hotspot20Duple> osuFriendlyName) {
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


    public List<Hotspot20Duple> getOsuServiceDescription() {
        return osuServiceDescription;
    }


    public void setOsuServiceDescription(List<Hotspot20Duple> osuServiceDescription) {
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
    public boolean needsToBeUpdatedOnDevice(Hotspot20IdProviderProfile previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

    public Hotspot20IdProviderProfile clone() {
        Hotspot20IdProviderProfile ret = (Hotspot20IdProviderProfile) super.clone();
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
        ret.radiusProfileAccounting = getRadiusProfileAccounting();
        ret.radiusProfileAuth = getRadiusProfileAuth();
        ret.osuSsid = getOsuSsid();
        ret.osuServerUri = getOsuServerUri();
        ret.osuFriendlyName = getOsuFriendlyName();
        ret.osuNaiShared = getOsuNaiShared();
        ret.osuNaiStandalone = getOsuNaiStandalone();
        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(domainName, mccMncList, naiRealmList, osuFriendlyName, osuIconList, osuMethodList,
                osuNaiShared, osuNaiStandalone, osuServerUri, osuServiceDescription, osuSsid, radiusProfileAccounting,
                radiusProfileAuth, roamingOi);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Hotspot20IdProviderProfile)) {
            return false;
        }
        Hotspot20IdProviderProfile other = (Hotspot20IdProviderProfile) obj;
        return Objects.equals(domainName, other.domainName) && Objects.equals(mccMncList, other.mccMncList)
                && Objects.equals(naiRealmList, other.naiRealmList)
                && Objects.equals(osuFriendlyName, other.osuFriendlyName)
                && Objects.equals(osuIconList, other.osuIconList) && Objects.equals(osuMethodList, other.osuMethodList)
                && Objects.equals(osuNaiShared, other.osuNaiShared)
                && Objects.equals(osuNaiStandalone, other.osuNaiStandalone)
                && Objects.equals(osuServerUri, other.osuServerUri)
                && Objects.equals(osuServiceDescription, other.osuServiceDescription)
                && Objects.equals(osuSsid, other.osuSsid)
                && Objects.equals(radiusProfileAccounting, other.radiusProfileAccounting)
                && Objects.equals(radiusProfileAuth, other.radiusProfileAuth)
                && Objects.equals(roamingOi, other.roamingOi);
    }

   

}

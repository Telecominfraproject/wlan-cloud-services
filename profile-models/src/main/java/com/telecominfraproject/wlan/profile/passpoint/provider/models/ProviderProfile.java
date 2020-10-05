package com.telecominfraproject.wlan.profile.passpoint.provider.models;

import java.util.List;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.passpoint.models.Hotspot20Duple;


public class ProviderProfile extends ProfileDetails implements PushableConfiguration<ProviderProfile> {


    private String domainName;

    byte[] mcc;
    byte[] mnc;


    List<CellularNetworkInformation> mccMncList;
    
    List<NaiRealm> naiRealmList;
    
    List<OsuIcon> osuIconList;

    String radiusProfileAuth;
    String radiusProfileAccounting;
    String osuSsid;
    String osuServerUri;
    String osuFriendlyName;
    int osuNaiStandalone; // needs to be unsigned, so do take byte value, and make osuNaiStandalone & 0xff;
    int osuNaiShared;
    List<Integer> osuMethodList;
    List<Hotspot20Duple> osuServiceDescription;
  
    private static final long serialVersionUID = -6146454085334670280L;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    private ProviderProfile() {
    }

    public static ProviderProfile createWithDefaults() {
        return new ProviderProfile();
    }


    @Override
    public ProfileType getProfileType() {
        return ProfileType.id_provider;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(ProviderProfile previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

}

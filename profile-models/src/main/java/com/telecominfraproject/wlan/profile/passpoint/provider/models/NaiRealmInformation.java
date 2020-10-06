package com.telecominfraproject.wlan.profile.passpoint.provider.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class NaiRealmInformation extends BaseJsonModel implements PushableConfiguration<NaiRealmInformation> {

    private static final long serialVersionUID = -6102005274671968193L;

    private Set<String> naiRealms;
    private int encoding;
    private Set<String> eapMethods;
    private Map<String, Set<String>> eapMap;

    public NaiRealmInformation() {

    }

    public static NaiRealmInformation createWithDefaults() {
        NaiRealmInformation ret = new NaiRealmInformation();
        ret.setEncoding(NaiRealmEncoding.ietf_rfc_4282_encoding.getId());
        ret.naiRealms = new HashSet<>();
        ret.naiRealms.add("example.com");
        ret.naiRealms.add("example.net");
        ret.eapMethods = new HashSet<>();
        ret.eapMethods.add(EapMethods.eap_tls.getName());
        ret.eapMap = new HashMap<>();

        Set<String> credentialsSet = new HashSet<>();
        credentialsSet.add(CredentialType.softoken.getName());
        credentialsSet.add(CredentialType.certificate.getName());
        ret.eapMap.put(EapMethods.eap_tls.getName(), credentialsSet);

        return ret;
    }

    public int getEncoding() {
        return encoding;
    }

    public void setEncoding(int i) {
        this.encoding = i;
    }


    public Set<String> getNaiRealms() {
        return naiRealms;
    }


    public void setNaiRealms(Set<String> naiRealms) {
        this.naiRealms = naiRealms;
    }


    public Set<String> getEapMethods() {
        return eapMethods;
    }


    public void setEapMethods(Set<String> eapMethods) {
        this.eapMethods = eapMethods;
    }


    public Map<String, Set<String>> getEapMap() {
        return eapMap;
    }


    public void setEapMap(Map<String, Set<String>> eapMap) {
        this.eapMap = eapMap;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(NaiRealmInformation previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

    @Override
    public NaiRealmInformation clone() {
        NaiRealmInformation ret = (NaiRealmInformation) super.clone();
        if (eapMap != null)
            ret.eapMap = eapMap;
        if (eapMethods != null)
            ret.eapMethods = eapMethods;
        if (naiRealms != null)
            ret.naiRealms = naiRealms;
        ret.encoding = encoding;
        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eapMap, eapMethods, encoding, naiRealms);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NaiRealmInformation)) {
            return false;
        }
        NaiRealmInformation other = (NaiRealmInformation) obj;
        return Objects.equals(eapMap, other.eapMap) && Objects.equals(eapMethods, other.eapMethods)
                && encoding == other.encoding && Objects.equals(naiRealms, other.naiRealms);
    }


}

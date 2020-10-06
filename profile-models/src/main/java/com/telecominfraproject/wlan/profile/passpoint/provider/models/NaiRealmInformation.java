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

    private Set<String> naiRealms = new HashSet<>();
    private int encoding;
    private Set<String> eapMethods = new HashSet<>();
    private Map<String, Set<String>> eapMap = new HashMap<>();;

    public NaiRealmInformation() {

    }

    public static NaiRealmInformation createWithDefaults() {
        NaiRealmInformation ret = new NaiRealmInformation();
        ret.setEncoding(NaiRealmEncoding.ietf_rfc_4282_encoding.getId());
        ret.naiRealms.add("example.com");
        ret.naiRealms.add("example.net");

        ret.eapMethods.add(EapMethods.eap_tls.getName());
        ret.eapMethods.add(EapMethods.eap_ttls.getName());

        Set<String> tlsCredentialsSet = new HashSet<>();

        String tlsCredential = AuthenticationParameterTypes.credential_type.getName() + ":"
                + CredentialType.certificate.getName(); 
        tlsCredentialsSet.add(tlsCredential);
        ret.eapMap.put(EapMethods.eap_tls.getName(), tlsCredentialsSet);
        
        Set<String> ttsCredentialsSet = new HashSet<>();
        String ttsEapCredential = AuthenticationParameterTypes.credential_type.getName() + ":"
                + CredentialType.username_password.getName();
        ttsCredentialsSet.add(ttsEapCredential);    
        String nonEapcredential = AuthenticationParameterTypes.non_eap_inner_authentication_type.getName() + ":"
                + NonEapInnerAuthenticationTypes.mschap_v2.getName();
        ttsCredentialsSet.add(nonEapcredential);
        ret.eapMap.put(EapMethods.eap_ttls.getName(), ttsCredentialsSet);
      
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

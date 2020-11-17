package com.telecominfraproject.wlan.profile.passpoint.models.provider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class PasspointNaiRealmInformation extends BaseJsonModel implements PushableConfiguration<PasspointNaiRealmInformation> {

    private static final long serialVersionUID = -6102005274671968193L;

    private Set<String> naiRealms = new HashSet<>();
    private int encoding;
    private Set<String> eapMethods = new HashSet<>();
    private Map<String, Set<String>> eapMap = new HashMap<>();;

    public PasspointNaiRealmInformation() {

    }

    public static PasspointNaiRealmInformation createWithDefaults() {
        PasspointNaiRealmInformation ret = new PasspointNaiRealmInformation();
        ret.setEncoding(PasspointNaiRealmEncoding.ietf_rfc_4282_encoding.getId());
        ret.naiRealms.add("example.com");
        ret.naiRealms.add("example.net");

        ret.eapMethods.add(PasspointEapMethods.eap_tls.getName());
        ret.eapMethods.add(PasspointEapMethods.eap_ttls.getName());

        Set<String> tlsCredentialsSet = new HashSet<>();

        String tlsCredential = PasspointNaiRealmEapAuthParam.NAI_REALM_EAP_AUTH_CRED_TYPE.getName() + ":"
                + PasspointNaiRealmEapCredType.NAI_REALM_CRED_TYPE_CERTIFICATE.getName();
        tlsCredentialsSet.add(tlsCredential);
        ret.eapMap.put(PasspointEapMethods.eap_tls.getName(), tlsCredentialsSet);

        Set<String> ttsCredentialsSet = new HashSet<>();
        String ttsEapCredential = PasspointNaiRealmEapAuthParam.NAI_REALM_EAP_AUTH_CRED_TYPE.getName() + ":"
                + PasspointNaiRealmEapCredType.NAI_REALM_CRED_TYPE_USERNAME_PASSWORD.getName();
        ttsCredentialsSet.add(ttsEapCredential);
        String nonEapcredential = PasspointNaiRealmEapAuthParam.NAI_REALM_EAP_AUTH_NON_EAP_INNER_AUTH.getName() + ":"
                + PasspointNaiRealmEapAuthInnerNonEap.NAI_REALM_INNER_NON_EAP_MSCHAPV2.getName();
        ttsCredentialsSet.add(nonEapcredential);
        ret.eapMap.put(PasspointEapMethods.eap_ttls.getName(), ttsCredentialsSet);

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
    public boolean needsToBeUpdatedOnDevice(PasspointNaiRealmInformation previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

    @Override
    public PasspointNaiRealmInformation clone() {
        PasspointNaiRealmInformation ret = (PasspointNaiRealmInformation) super.clone();
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
        if (!(obj instanceof PasspointNaiRealmInformation)) {
            return false;
        }
        PasspointNaiRealmInformation other = (PasspointNaiRealmInformation) obj;
        return Objects.equals(eapMap, other.eapMap) && Objects.equals(eapMethods, other.eapMethods)
                && encoding == other.encoding && Objects.equals(naiRealms, other.naiRealms);
    }


}

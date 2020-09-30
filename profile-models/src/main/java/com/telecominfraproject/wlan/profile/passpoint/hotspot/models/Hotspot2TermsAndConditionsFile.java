package com.telecominfraproject.wlan.profile.passpoint.hotspot.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class Hotspot2TermsAndConditionsFile extends BaseJsonModel {

    /**
     * 
     */
    private static final long serialVersionUID = 6252716511395666075L;

    private String filename;
    private String filetype;
    private long fileLastModifiedTimestamp;
    private String fileUrl;


    private Hotspot2TermsAndConditionsFile() {

        filename = "DefaultHotspot2TermsAndConditions";
        fileLastModifiedTimestamp = System.currentTimeMillis();
        filetype = ".txt";
        fileUrl = "https://localhost:9091/filestore/" + filename + "-" + fileLastModifiedTimestamp + filetype;

    }


    public String getFilename() {
        return filename;
    }


    public void setFilename(String filename) {
        this.filename = filename;
    }


    public long getFileLastModifiedTimestamp() {
        return fileLastModifiedTimestamp;
    }


    public void setFileLastModifiedTimestamp(long fileLastModifiedTimestamp) {
        this.fileLastModifiedTimestamp = fileLastModifiedTimestamp;
    }


    public String getFileUrl() {
        return fileUrl;
    }


    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFiletype() {
        return filetype;
    }


    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    @Override
    public Hotspot2TermsAndConditionsFile clone() {
        Hotspot2TermsAndConditionsFile returnValue = (Hotspot2TermsAndConditionsFile) super.clone();
        returnValue.setFileLastModifiedTimestamp(fileLastModifiedTimestamp);
        returnValue.setFilename(filename);
        returnValue.setFiletype(filetype);
        returnValue.setFileUrl(fileUrl);
        return returnValue;
    }


    @Override
    public int hashCode() {
        return Objects.hash(fileLastModifiedTimestamp, fileUrl, filename, filetype);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Hotspot2TermsAndConditionsFile)) {
            return false;
        }
        Hotspot2TermsAndConditionsFile other = (Hotspot2TermsAndConditionsFile) obj;
        return fileLastModifiedTimestamp == other.fileLastModifiedTimestamp && Objects.equals(fileUrl, other.fileUrl)
                && Objects.equals(filename, other.filename) && Objects.equals(filetype, other.filetype);
    }


    public static Hotspot2TermsAndConditionsFile createWithDefaults() {
        // TODO Auto-generated method stub
        return new Hotspot2TermsAndConditionsFile();
    }


}

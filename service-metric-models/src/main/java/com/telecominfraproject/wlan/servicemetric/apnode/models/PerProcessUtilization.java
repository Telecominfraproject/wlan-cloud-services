package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class PerProcessUtilization extends BaseJsonModel {

    private static final long serialVersionUID = 167357514689103313L;
    private int pid;
    private String cmd;
    private int util;
    
    public PerProcessUtilization () {
        // for serialization
    }
    
    public PerProcessUtilization (int pid, String cmd, int util) {
        this.pid = pid;
        this.cmd = cmd;
        this.util = util;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getUtil() {
        return util;
    }

    public void setUtil(int util) {
        this.util = util;
    }
    
    @Override
    public PerProcessUtilization clone() {
        return (PerProcessUtilization)super.clone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(cmd, pid, util);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PerProcessUtilization other = (PerProcessUtilization) obj;
        return Objects.equals(cmd, other.cmd) && pid == other.pid && util == other.util;
    }
    
    
    

}

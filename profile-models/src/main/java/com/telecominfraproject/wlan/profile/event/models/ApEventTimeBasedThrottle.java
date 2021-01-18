package com.telecominfraproject.wlan.profile.event.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class ApEventTimeBasedThrottle extends BaseJsonModel {

    /**
     * 
     */
    private static final long serialVersionUID = 740842237614470967L;

    public static final ApEventTimeBasedThrottle DO_NOT_FORWARD = new ApEventTimeBasedThrottle(true);

    /**
     * Disable forwarding completely
     */
    private Boolean doNotForward;

    /**
     * Window (seconds)
     */
    private Integer windowInSecond;
    /**
     * Maximum count per window
     */
    private Integer countPerWindow;

    /**
     * Default constructor
     */
    public ApEventTimeBasedThrottle() {
        // do nothing
    }

    /**
     * Used for do not forward only
     * 
     * @param doNotForward
     */
    protected ApEventTimeBasedThrottle(boolean doNotForward) {
        // do nothing
        this.doNotForward = doNotForward;
    }

    /**
     * Constructor
     * 
     * @param doNotForward
     * @param windowInSecond
     * @param countPerWindow
     */
    public ApEventTimeBasedThrottle(boolean doNotForward, int windowInSecond, int countPerWindow) {
        this.doNotForward = doNotForward;
        this.windowInSecond = windowInSecond;
        this.countPerWindow = countPerWindow;
    }

    @Override
    public ApEventTimeBasedThrottle clone() {
        return (ApEventTimeBasedThrottle) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ApEventTimeBasedThrottle)) {
            return false;
        }
        ApEventTimeBasedThrottle other = (ApEventTimeBasedThrottle) obj;
        if (this.countPerWindow == null) {
            if (other.countPerWindow != null) {
                return false;
            }
        } else if (!this.countPerWindow.equals(other.countPerWindow)) {
            return false;
        }
        if (this.doNotForward == null) {
            if (other.doNotForward != null) {
                return false;
            }
        } else if (!this.doNotForward.equals(other.doNotForward)) {
            return false;
        }
        if (this.windowInSecond == null) {
            if (other.windowInSecond != null) {
                return false;
            }
        } else if (!this.windowInSecond.equals(other.windowInSecond)) {
            return false;
        }
        return true;
    }

    /**
     * @return the countPerWindow
     */
    public Integer getCountPerWindow() {
        return this.countPerWindow;
    }

    public Boolean getDoNotForward() {
        return doNotForward;
    }

    /**
     * @return the windowInSecond
     */
    public Integer getWindowInSecond() {
        return this.windowInSecond;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.countPerWindow == null) ? 0 : this.countPerWindow.hashCode());
        result = prime * result + ((this.doNotForward == null) ? 0 : this.doNotForward.hashCode());
        result = prime * result + ((this.windowInSecond == null) ? 0 : this.windowInSecond.hashCode());
        return result;
    }

    /**
     * @param countPerWindow
     *            the countPerWindow to set
     */
    public void setCountPerWindow(Integer countPerWindow) {
        this.countPerWindow = countPerWindow;
    }

    public void setDoNotForward(Boolean doNotForward) {
        this.doNotForward = doNotForward;
    }

    /**
     * @param windowInSecond
     *            the windowInSecond to set
     */
    public void setWindowInSecond(Integer windowInSecond) {
        this.windowInSecond = windowInSecond;
    }

}

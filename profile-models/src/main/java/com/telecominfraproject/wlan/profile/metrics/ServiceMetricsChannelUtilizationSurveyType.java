package com.telecominfraproject.wlan.profile.metrics;

/**
 * @author mikehansen
 *
 */
public enum ServiceMetricsChannelUtilizationSurveyType {
    ON_CHANNEL, // Measurements on the home channel. These can be done frequently and periodically without user impact.
    OFF_CHANNEL, // The channel measurements on the listed foreign channels​ ​are periodically chosen using the round robin fashion (“1,6,11” -> t0=1, t1=6, t2=11, t3=1) to minimize user impact.
    FULL // ​the measurements are done on all allowed channels at once, this is very intrusive
}
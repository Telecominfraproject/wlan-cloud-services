package com.telecominfraproject.wlan.profile.metrics;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

public class ServiceMetricSurveyConfigParameters extends ServiceMetricRadioConfigParameters {

    /**
     * 
     */
    private static final long serialVersionUID = -4922288870505360355L;
    private ChannelUtilizationSurveyType channelSurveyType;
    private int scanIntervalMillis;
    private int percentUtilizationThreshold;
    private int delayMillisecondsThreshold;
    private StatsReportFormat statsReportFormat;


    public ServiceMetricSurveyConfigParameters(RadioType radioType, ServiceMetricDataType dataType,
            ChannelUtilizationSurveyType channelSurveyType, StatsReportFormat statsReportFormat, int scanIntervalMillis,
            int sampleIntervalMillis, int reportingIntervalSeconds, int percentUtilizationThreshold,
            int delayMillisecondsThreshold) {
        super(radioType, dataType, sampleIntervalMillis, reportingIntervalSeconds);
        this.setStatsReportFormat(statsReportFormat);
        this.setChannelSurveyType(channelSurveyType);
        this.setScanIntervalMillis(scanIntervalMillis);
        this.setPercentUtilizationThreshold(percentUtilizationThreshold);
        this.setDelayMillisecondsThreshold(delayMillisecondsThreshold);


    }


    public ChannelUtilizationSurveyType getChannelSurveyType() {
        return channelSurveyType;
    }


    public void setChannelSurveyType(ChannelUtilizationSurveyType channelSurveyType) {
        this.channelSurveyType = channelSurveyType;
    }


    public int getScanIntervalMillis() {
        return scanIntervalMillis;
    }


    public void setScanIntervalMillis(int scanIntervalMillis) {
        this.scanIntervalMillis = scanIntervalMillis;
    }


    public int getPercentUtilizationThreshold() {
        return percentUtilizationThreshold;
    }


    public void setPercentUtilizationThreshold(int percentUtilizationThreshold) {
        this.percentUtilizationThreshold = percentUtilizationThreshold;
    }


    public int getDelayMillisecondsThreshold() {
        return delayMillisecondsThreshold;
    }


    public void setDelayMillisecondsThreshold(int delayMillisecondsThreshold) {
        this.delayMillisecondsThreshold = delayMillisecondsThreshold;
    }

    public StatsReportFormat getStatsReportFormat() {
        return statsReportFormat;
    }

    public void setStatsReportFormat(StatsReportFormat statsReportFormat) {
        this.statsReportFormat = statsReportFormat;
    }

}

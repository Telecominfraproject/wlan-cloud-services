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
        this.radioType = radioType;
        this.serviceMetricDataType = dataType;
        this.reportingIntervalSeconds = ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS;
        this.samplingInterval = ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS;
        this.statsReportFormat = StatsReportFormat.RAW;
        this.channelSurveyType = ChannelUtilizationSurveyType.ON_CHANNEL;
        this.scanIntervalMillis = ServiceMetricConfigParameterDefaults.DEFAULT_DWELL_TIME_MILLIS;
        this.percentUtilizationThreshold = ServiceMetricConfigParameterDefaults.DEFAULT_MAX_PERCENT_UTILIZATION_THRESHOLD;
        this.delayMillisecondsThreshold = ServiceMetricConfigParameterDefaults.DEFAULT_MAX_MEASUREMENT_DELAY_MILLIS;

    }

    ServiceMetricSurveyConfigParameters() {
        
        reportingIntervalSeconds = ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS;
        samplingInterval = ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS;
        statsReportFormat = StatsReportFormat.RAW;
        channelSurveyType = ChannelUtilizationSurveyType.ON_CHANNEL;
        scanIntervalMillis = ServiceMetricConfigParameterDefaults.DEFAULT_DWELL_TIME_MILLIS;
        percentUtilizationThreshold = ServiceMetricConfigParameterDefaults.DEFAULT_MAX_PERCENT_UTILIZATION_THRESHOLD;
        delayMillisecondsThreshold = ServiceMetricConfigParameterDefaults.DEFAULT_MAX_MEASUREMENT_DELAY_MILLIS;

    }

    public static ServiceMetricSurveyConfigParameters generateDefault(ServiceMetricDataType serviceMetricDataType,
            RadioType radioType) {
        ServiceMetricSurveyConfigParameters ret = new ServiceMetricSurveyConfigParameters();
        ret.serviceMetricDataType = serviceMetricDataType;
        ret.radioType = radioType;
        return ret;
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

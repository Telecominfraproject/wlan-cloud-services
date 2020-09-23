package com.telecominfraproject.wlan.profile.metrics;

import java.util.EnumMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

public class MetricConfigParameters extends BaseJsonModel implements PushableConfiguration<MetricConfigParameters> {

    private static final long serialVersionUID = 1469611396924992464L;

    private static final int DEFAULT_DWELL_TIME_MILLIS = 30;
    private static final int DEFAULT_SAMPLE_INTERVAL_MILLIS = 30;
    private static final int DEFAULT_REPORT_INTERVAL_SECONDS = 60;


    /**
     * Type of cloud service metric being configured
     */
    private ServiceMetricDataType serviceMetricDataType;

    /**
     * Time between sample collections.
     */
    private Integer samplingInterval;
    /**
     * Reporting interval for Metric
     */
    private Integer reportingIntervalSeconds;
    /**
     * DWEL time when surveying for a sample
     */
    private Integer scanIntervalMillis;
    /**
     * Intrusiveness behavior of the scan
     * 
     * @see com.telecominfraproject.wlan.profile.metrics.ScanIntrusivenessThresholdAttributes.MAX_DELAY​
     * @see com.telecominfraproject.wlan.profile.metrics.ScanIntrusivenessThresholdAttributes.PERCENT_UTILIZATION
     */
    private Map<ScanIntrusivenessThresholdAttributes, Integer> threshold = new EnumMap<>(
            ScanIntrusivenessThresholdAttributes.class);

    private ChannelUtilizationType surveyType;

    private StatsReportFormat statsReportFormat;

    /**
     * @param dataType
     * @param channelSurveyType
     * @param scanIntervalMillis
     * @param sampleIntervalMillis
     * @param reportingIntervalSeconds
     * @param percentUtilizationThreshold
     * @param delayMilliscondsThreshold
     */
    public MetricConfigParameters(ServiceMetricDataType dataType, ChannelUtilizationType channelSurveyType,
            StatsReportFormat statsReportFormat, int scanIntervalMillis, int sampleIntervalMillis,
            int reportingIntervalSeconds, int percentUtilizationThreshold, int delayMilliscondsThreshold) {
        this.serviceMetricDataType = dataType;
        this.scanIntervalMillis = scanIntervalMillis;
        this.samplingInterval = sampleIntervalMillis;

        this.reportingIntervalSeconds = reportingIntervalSeconds;

        if (dataType.equals(ServiceMetricDataType.Channel) || dataType.equals(ServiceMetricDataType.Neighbour)) {
            if (channelSurveyType.equals(ChannelUtilizationType.OFF_CHANNEL)
                    || channelSurveyType.equals(ChannelUtilizationType.FULL)) {
                this.threshold.put(ScanIntrusivenessThresholdAttributes.MAX_DELAY, delayMilliscondsThreshold);
                this.threshold.put(ScanIntrusivenessThresholdAttributes.PERCENT_UTILIZATION,
                        percentUtilizationThreshold);
            } else {
                this.threshold = null;
            }
            this.surveyType = channelSurveyType;
        } else {
            this.surveyType = null;
        }
        this.statsReportFormat = statsReportFormat;
    }

    /**
     * @param dataType
     * @return default parameter set for on-channel stats collection on the AP
     *         for the given ServiceMetricDataType
     */
    public static MetricConfigParameters getDefaultConfigForMetricType(ServiceMetricDataType dataType) {
        MetricConfigParameters ret = new MetricConfigParameters(dataType, ChannelUtilizationType.ON_CHANNEL,
                StatsReportFormat.RAW, DEFAULT_DWELL_TIME_MILLIS, DEFAULT_SAMPLE_INTERVAL_MILLIS,
                DEFAULT_REPORT_INTERVAL_SECONDS, 0, 0);
        return ret;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(MetricConfigParameters previousVersion) {
        // TODO Auto-generated method stub
        return false;
    }


    public Integer getSamplingInterval() {
        return samplingInterval;
    }


    public void setSamplingInterval(Integer samplingInterval) {
        this.samplingInterval = samplingInterval;
    }

    public Integer getReportingInterval() {
        return reportingIntervalSeconds;
    }


    public void setReportingInterval(Integer reportingInterval) {
        this.reportingIntervalSeconds = reportingInterval;
    }


    public Integer getDwellTime() {
        return scanIntervalMillis;
    }


    public void setDwellTime(Integer dwellTime) {
        this.scanIntervalMillis = dwellTime;
    }


    public Map<ScanIntrusivenessThresholdAttributes, Integer> getThreshold() {
        return threshold;
    }


    public void setThreshold(Map<ScanIntrusivenessThresholdAttributes, Integer> threshold) {
        this.threshold = threshold;
    }


    public ChannelUtilizationType getSurveyType() {
        return surveyType;
    }


    public void setSurveyType(ChannelUtilizationType surveyType) {
        this.surveyType = surveyType;
    }

    public StatsReportFormat getStatsReportFormat() {
        return statsReportFormat;
    }

    public void setStatsReportFormat(StatsReportFormat statsReportFormat) {
        this.statsReportFormat = statsReportFormat;
    }


    public Integer getDwellTimeMilliseconds() {
        return scanIntervalMillis;
    }


    public void setDwellTimeMilliseconds(Integer dwellTimeMilliseconds) {
        this.scanIntervalMillis = dwellTimeMilliseconds;
    }


    public Integer getReportingIntervalSeconds() {
        return reportingIntervalSeconds;
    }


    public void setReportingIntervalSeconds(Integer reportingIntervalSeconds) {
        this.reportingIntervalSeconds = reportingIntervalSeconds;
    }


    public Integer getScanIntervalMillis() {
        return scanIntervalMillis;
    }


    public void setScanIntervalMillis(Integer scanIntervalMillis) {
        this.scanIntervalMillis = scanIntervalMillis;
    }


    public ServiceMetricDataType getServiceMetricDataType() {
        return serviceMetricDataType;
    }


    public void setServiceMetricDataType(ServiceMetricDataType serviceMetricDataType) {
        this.serviceMetricDataType = serviceMetricDataType;
    }

    @Override
    public MetricConfigParameters clone() {
        MetricConfigParameters ret = (MetricConfigParameters) super.clone();

        if (threshold != null) {
            ret.threshold = new EnumMap<>(ScanIntrusivenessThresholdAttributes.class);

            for (Map.Entry<ScanIntrusivenessThresholdAttributes, Integer> entry : threshold.entrySet()) {
                ret.threshold.put(entry.getKey(), entry.getValue());
            }

        }


        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MetricConfigParameters other = (MetricConfigParameters) obj;

        if (serviceMetricDataType == null) {
            if (other.serviceMetricDataType != null)
                return false;
        } else if (!serviceMetricDataType.equals(other.serviceMetricDataType))
            return false;

        if (samplingInterval == null) {
            if (other.samplingInterval != null)
                return false;
        } else if (!samplingInterval.equals(other.samplingInterval))
            return false;

        if (reportingIntervalSeconds == null) {
            if (other.reportingIntervalSeconds != null)
                return false;
        } else if (!reportingIntervalSeconds.equals(other.reportingIntervalSeconds))
            return false;

        if (threshold == null) {
            if (other.threshold != null)
                return false;
        } else if (!threshold.equals(other.threshold))
            return false;

        if (surveyType == null) {
            if (other.surveyType != null)
                return false;
        } else if (!surveyType.equals(other.surveyType))
            return false;

        if (statsReportFormat == null) {
            if (other.statsReportFormat != null)
                return false;
        } else if (!statsReportFormat.equals(other.statsReportFormat))
            return false;

        return true;
    }

}

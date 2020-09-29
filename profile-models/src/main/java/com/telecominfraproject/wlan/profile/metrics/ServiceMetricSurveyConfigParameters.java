package com.telecominfraproject.wlan.profile.metrics;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

public class ServiceMetricSurveyConfigParameters extends CommonServiceMetricConfigParameters {

    /**
     * 
     */
    private static final long serialVersionUID = -4922288870505360355L;
    private ChannelUtilizationSurveyType channelSurveyType;
    private int scanIntervalMillis;
    private int percentUtilizationThreshold;
    private int delayMillisecondsThreshold;
    private StatsReportFormat statsReportFormat;
    private RadioType radioType;
    private ServiceMetricDataType serviceMetricDataType;


    private ServiceMetricSurveyConfigParameters() {
        super();
    }

    public static ServiceMetricSurveyConfigParameters createWithDefaults() {
        return new ServiceMetricSurveyConfigParameters();

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

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (RadioType.isUnsupported(getRadioType())) {
            return true;
        }
        
        if (ServiceMetricDataType.isUnsupported(getServiceMetricDataType())) {
            return true;
        }

        return false;
    }


    @Override
    public ServiceMetricSurveyConfigParameters clone() {
        ServiceMetricSurveyConfigParameters ret = (ServiceMetricSurveyConfigParameters) super.clone();
        ret.setServiceMetricDataType(getServiceMetricDataType());
        ret.setChannelSurveyType(channelSurveyType);
        ret.setDelayMillisecondsThreshold(delayMillisecondsThreshold);
        ret.setPercentUtilizationThreshold(percentUtilizationThreshold);
        ret.setRadioType(radioType);
        ret.setScanIntervalMillis(getScanIntervalMillis());
        ret.setStatsReportFormat(getStatsReportFormat());
        return ret;
    }

   

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(channelSurveyType, delayMillisecondsThreshold,
                percentUtilizationThreshold, radioType, scanIntervalMillis, serviceMetricDataType, statsReportFormat);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof ServiceMetricSurveyConfigParameters)) {
            return false;
        }
        ServiceMetricSurveyConfigParameters other = (ServiceMetricSurveyConfigParameters) obj;
        return channelSurveyType == other.channelSurveyType
                && delayMillisecondsThreshold == other.delayMillisecondsThreshold
                && percentUtilizationThreshold == other.percentUtilizationThreshold && radioType == other.radioType
                && scanIntervalMillis == other.scanIntervalMillis
                && Objects.equals(serviceMetricDataType, other.serviceMetricDataType)
                && statsReportFormat == other.statsReportFormat;
    }

    @Override
    public ServiceMetricDataType getServiceMetricDataType() {
        return serviceMetricDataType ;
    }

    @Override
    public void setServiceMetricDataType(ServiceMetricDataType serviceMetricDataType) {
       this.serviceMetricDataType = serviceMetricDataType;
        
    }

}


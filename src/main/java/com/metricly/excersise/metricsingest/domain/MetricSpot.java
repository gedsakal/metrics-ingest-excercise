package com.metricly.excersise.metricsingest.domain;

import java.io.Serializable;

public class MetricSpot implements Serializable{

    private String metric;
    private Double value;
    private Long timestamp;

    public MetricSpot() {};

    public MetricSpot(String metricId, Double value, Long timestamp) {
        this.metric = metricId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s, \t %f, \t %d ", getMetric(), getValue(), getTimestamp());
    }
}

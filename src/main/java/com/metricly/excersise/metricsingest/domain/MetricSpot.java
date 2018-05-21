package com.metricly.excersise.metricsingest.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="metrics")
public class MetricSpot implements Serializable{

    @Id
    private UUID uuid;

    private String metric;
    private Double value;
    private Long timestamp;

    public MetricSpot() {};

    public MetricSpot(String metricId, Double value, Long timestamp) {
        this.metric = metricId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
    public boolean equals(Object o) {
        if (this == o) return  true;
        if (!(o instanceof MetricSpot)) return false;
        MetricSpot ms = (MetricSpot) o;
        return Objects.equals(metric, ms.metric)
                && Objects.equals(value, ms.value)
                && Objects.equals(timestamp, ms.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metric, value, timestamp);
    }

    @Override
    public String toString() {
        return String.format("%s, \t %f, \t %d ", getMetric(), getValue(), getTimestamp());
    }
}

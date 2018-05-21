package com.metricly.excersise.metricsingest.service;

import com.metricly.excersise.metricsingest.domain.MetricSpot;
import com.metricly.excersise.metricsingest.jpa.MetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MetricsService {

    @Autowired
    MetricsRepository metricsRepo;

    public List<MetricSpot> extractAllMetrics() {
        return metricsRepo.findAll();
    }


    public void persistMetricsStream(Stream<MetricSpot> stream) {
        for (MetricSpot spot: stream.collect(Collectors.toList())) {
            spot.setUuid(UUID.randomUUID());
            metricsRepo.save(spot);
        }
    }
}

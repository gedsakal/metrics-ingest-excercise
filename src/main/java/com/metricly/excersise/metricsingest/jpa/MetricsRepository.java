package com.metricly.excersise.metricsingest.jpa;

import com.metricly.excersise.metricsingest.domain.MetricSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface MetricsRepository extends JpaRepository<MetricSpot, Long> {}

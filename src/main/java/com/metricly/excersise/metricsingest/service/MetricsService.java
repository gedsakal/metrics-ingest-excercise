package com.metricly.excersise.metricsingest.service;

import com.metricly.excersise.metricsingest.domain.MetricSpot;
import org.apache.spark.sql.*;
import org.springframework.stereotype.Service;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.spark.sql.functions.col;

@Service
public class MetricsService {

    private static final String DATA_METRICS_OUTPUT_DIR = "data/metrics/";

    private static Encoder<MetricSpot> metricSpotEncoder = Encoders.bean(MetricSpot.class);

    private SparkSession spark = SparkSession
            .builder()
            .appName("Metrics persister app")
            .master("local")
            .getOrCreate();

    /**
     * Uses Spark to read datastore
     * @return list of items or empty list on error
     */
    public List<MetricSpot> extractAllMetrics() {
        try {
            Dataset<MetricSpot> ds = spark
                    .read()
                    .json(DATA_METRICS_OUTPUT_DIR + "/*.json")
                    .as(metricSpotEncoder);

            return ds.distinct().sort(col( "timestamp").desc(), col("metric")).collectAsList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Outputs stream of metrics into local dir (could be any persister)
     * @param stream
     */
    public void persistMetricsStream(Stream<MetricSpot> stream) {
        Dataset<MetricSpot> ds = spark.createDataset(stream.collect(Collectors.toList()), metricSpotEncoder);
        ds.write().mode(SaveMode.Append).json(DATA_METRICS_OUTPUT_DIR);
    }
}

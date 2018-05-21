package com.metricly.excersise.metricsingest.controller;

import com.metricly.excersise.metricsingest.domain.MetricSpot;
import com.metricly.excersise.metricsingest.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

@RestController
public class MetricsController {

    @Autowired
    private MetricsService service;

    @GetMapping("/get")
    public String getAllMetrics() {

        List<MetricSpot> metricList = service.extractAllMetrics();

        String result = "Found: " + metricList.size() + "<br/>" + (metricList.size() == 0 ? " POST something..." : "" );

        for (MetricSpot m : metricList) {
            result+=m.toString()+"<br/>";
        }

        return result;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingestMetrics(@RequestBody Collection<MetricSpot> metrics) {
        try {
            service.persistMetricsStream(metrics.stream());
        } catch (Exception e) {
            return new ResponseEntity<> (MessageFormat.format("Error occurred:  {0}", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<> (
                MessageFormat.format("Successfully Ingested {0} entities", metrics.size()),
                HttpStatus.OK);
    }
}

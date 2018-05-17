package com.metricly.excersise.metricsingest.controller;

import com.metricly.excersise.metricsingest.domain.MetricSpot;
import com.metricly.excersise.metricsingest.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

@Controller
public class MetricsController {

    @Autowired
    private MetricsService service;

    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getAllMetrics() {

        List<MetricSpot> metricList = service.extractAllMetrics();

        String result = "Found: " + metricList.size() + "\n" + (metricList.size() == 0 ? " POST something..." : "" );

        for (MetricSpot m : metricList) {
            result+=m.toString()+"\n";
        }

        return result;
    }

    @RequestMapping(value = "/ingest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> ingestMetrics(@RequestBody Collection<MetricSpot> metrics) {
        try {
            service.persistMetricsStream(metrics.stream());
        } catch (Exception e) {
            return new ResponseEntity<String> (MessageFormat.format("Error occurred:  {0}", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String> (
                MessageFormat.format("Successfully Ingested {0} entities", metrics.size()),
                HttpStatus.OK);
    }
}

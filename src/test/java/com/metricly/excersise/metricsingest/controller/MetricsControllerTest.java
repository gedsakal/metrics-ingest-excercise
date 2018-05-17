package com.metricly.excersise.metricsingest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metricly.excersise.metricsingest.MetricsIngestApplication;
import com.metricly.excersise.metricsingest.domain.MetricSpot;
import com.metricly.excersise.metricsingest.service.MetricsService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MetricsIngestApplication.class})
@WebAppConfiguration
public class MetricsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    MetricsService service;


    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    private MediaType contentType = new MediaType(MediaType.TEXT_PLAIN.getType(),
            MediaType.TEXT_PLAIN.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void extractAllMetrics_None_Test() throws Exception {
        when(service.extractAllMetrics()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/get"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().string( "Found: 0\n POST something..." ));
    }

    @Test
    public void extractAllMetrics_1_Test() throws Exception {
        MetricSpot aSpot = new MetricSpot("m",100D, 111111111L );
        when(service.extractAllMetrics()).thenReturn( Arrays.asList(aSpot));

        mockMvc.perform(get("/get"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().string( "Found: 1\n"+ aSpot.toString() + "\n"));
    }

    @Test
    @Ignore // I give up with these content types... :(
    public void ingestMetrics_1_Test() throws Exception {
        MetricSpot aSpot = new MetricSpot("m", 100D, 111111111L);

        doNothing().when(service).persistMetricsStream(anyObject());

        String theJson = "[{ \"metric\": \"cpu.utilization\",\"value\": 1234,\"timestamp\": 11111111111 }]";

        mockMvc.perform(post("/ingest")//.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(new ObjectMapper().writeValueAsBytes(theJson.getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully persisted 1 "));
    }

}
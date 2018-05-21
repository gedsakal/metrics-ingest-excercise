package com.metricly.excersise.metricsingest.controller;

import com.metricly.excersise.metricsingest.MetricsIngestApplication;
import com.metricly.excersise.metricsingest.domain.MetricSpot;
import com.metricly.excersise.metricsingest.service.MetricsService;
import org.junit.Before;
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

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static java.util.Arrays.asList;
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
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    private MediaType contentType = new MediaType(MediaType.TEXT_PLAIN.getType(),
            MediaType.TEXT_PLAIN.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void extractAllMetrics_None_Test() throws Exception {
        when(service.extractAllMetrics()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/get").accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string( "Found: 0<br/> POST something..." ));
    }

    @Test
    public void extractAllMetrics_1_Test() throws Exception {
        MetricSpot aSpot = new MetricSpot("m",100D, 111111111L );
        when(service.extractAllMetrics()).thenReturn( asList(aSpot));

        mockMvc.perform(get("/get").accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().string( "Found: 1<br/>"+ aSpot.toString() + "<br/>"));
    }

    @Test
    public void ingestMetrics_1_Test() throws Exception {
        MetricSpot aSpot = new MetricSpot("m", 100D, 111111111L);
        Jsonb jsonb = JsonbBuilder.create();
        doNothing().when(service).persistMetricsStream(anyObject());

        mockMvc.perform(post("/ingest")
                    .contentType("application/json")
                    .content("[" + jsonb.toJson(aSpot) + " ]"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully Ingested 1 entities"));
    }

}
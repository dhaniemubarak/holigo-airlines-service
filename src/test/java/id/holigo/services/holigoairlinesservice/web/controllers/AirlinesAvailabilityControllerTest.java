package id.holigo.services.holigoairlinesservice.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.web.model.InquiryDto;
import id.holigo.services.holigoairlinesservice.web.model.ListAvailabilityDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AirlinesAvailabilityController.class)
class AirlinesAvailabilityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RestTemplate restTemplate;

    @MockBean
    AirlinesService airlinesService;

    private static final String URL = "http://localhost:8109/api/v1/airlines/availabilities";

    private MockRestServiceServer mockRestServiceServer;

    InquiryDto inquiryDto;


//    public void setAirlinesService(AirlinesService airlinesService) {
//        this.airlinesService = airlinesService;
//    }

    @BeforeEach
    void setUp() {

        inquiryDto = new InquiryDto();
        inquiryDto.setOriginAirportId("CGK");
        inquiryDto.setDestinationAirportId("DPS");
        inquiryDto.setDepartureDate(Date.valueOf("2022-06-13"));
        inquiryDto.setAirlinesCode("JT");
        inquiryDto.setAdultAmount(1);
        inquiryDto.setChildAmount(0);
        inquiryDto.setInfantAmount(0);
        inquiryDto.setSeatClass("E");
        inquiryDto.setTripType("O");
    }

    @Test
    void getAvailabilities() {

        try {
            mockMvc.perform(MockMvcRequestBuilders.get(URL, inquiryDto)).andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
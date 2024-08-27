package com.ipinformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipinformation.model.objects.Location;
import com.ipinformation.utils.DistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.redis.connection.ReactiveStreamCommands.AddStreamRecord.body;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebAppConfiguration
class ApplicationTests {

//	@LocalServerPort
//	private int port;

//	private final RestTemplate restTemplate;
//
//	private final TestRestTemplate testRestTemplate;

//	private final ObjectMapper mapper;

	private final WebApplicationContext wac;

	private WebTestClient testRestClient;

	public ApplicationTests(WebApplicationContext wac) {
//		this.mapper = mapper;
        this.wac = wac;
    }


	private MockRestServiceServer mockServer;



	@BeforeEach
	void setUp() {
		RestClient.Builder builder = RestClient.builder();
		testRestClient = MockMvcWebTestClient.bindToApplicationContext(this.wac).build();
		mockServer = MockRestServiceServer.bindTo(builder).ignoreExpectOrder(true).build();
	}


//	@Test
	void WebServiceTest() throws JsonProcessingException {

		Location emp = Mockito.mock(Location.class);

//		mockServer.expect(ExpectedCount.once(), requestTo(matchesPattern(".*ipapi.*")))
//				.andExpect(method(HttpMethod.GET))
//				.andRespond(withStatus(HttpStatus.OK)
//						.contentType(MediaType.APPLICATION_JSON)
//						.body(mapper.writeValueAsString(emp)));
//
//		mockServer.expect(ExpectedCount.once(), requestTo(matchesPattern(".*get-country-details.*")))
//				.andExpect(method(HttpMethod.GET))
//				.andRespond(withStatus(HttpStatus.OK)
//						.contentType(MediaType.APPLICATION_JSON)
//						.body(mapper.writeValueAsString(emp)));
//
//		mockServer.expect(ExpectedCount.once(), requestTo(matchesPattern(".*get-currency-exchange-rate.*")))
//				.andExpect(method(HttpMethod.GET))
//				.andRespond(withStatus(HttpStatus.OK)
//						.contentType(MediaType.APPLICATION_JSON)
//						.body(mapper.writeValueAsString(emp)));


		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("ipAdress", "10.0.0.1");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

//		ipApiUrl = "https://run.mocky.io/v3/b040d502-e758-47a6-9923-a61ce6b4d062";


//		String result = testRestClient.post().uri("http://localhost:" + port + "/lookup").body(request, MultiValueMap.class).exchange().expectStatus().isOk().returnResult(String.class).getResponseBody().blockFirst();

		mockServer.verify();
		assertEquals(emp, new Location());

	}

//	@Test
	void testDistanceCalculation() {
		double lat1 = 40.714268; // New York
		double lon1 = -74.005974;
		double lat2 = 34.0522; // Los Angeles
		double lon2 = -118.2437;

		double vincentyDistance = new DistanceCalculator().calculateDistance(lat1, lon1, lat2, lon2);


		double expectedDistance = 3944;
		assertTrue(Math.abs(vincentyDistance - expectedDistance) < 0.5);


	}

}

package com.terence.santorini;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.terence.santorini.testutil.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class SantoriniApplicationTests extends BaseIntegrationTest {
  @Test
  void testContextLoadsWithActuatorEndpoint() {
    ResponseEntity<String> response =
        testRestTemplate.getForEntity(createUrl("/management/health"), String.class);

    assertEquals("{\"status\":\"UP\"}", response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testContextLoadsWithInfoEndpoint() {
    ResponseEntity<String> response =
        testRestTemplate.getForEntity(createUrl("/management/info"), String.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}

package com.terence.santorini.testutil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = BaseIntegrationTest.DockerPostgreDataSourceInitializer.class)
@Testcontainers
public abstract class BaseIntegrationTest {
  @LocalServerPort private int port;

  @Autowired public TestRestTemplate testRestTemplate;

  public String createUrl(String path) {
    return "http://localhost:" + port + path;
  }

  public static PostgreSQLContainer<?> postgreDBContainer =
      new PostgreSQLContainer<>("postgres:13.2-alpine").withUsername("postgres");

  static {
    postgreDBContainer.start();
  }

  public static class DockerPostgreDataSourceInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
          applicationContext,
          "spring.datasource.url=" + postgreDBContainer.getJdbcUrl(),
          "spring.datasource.username=" + postgreDBContainer.getUsername(),
          "spring.datasource.password=" + postgreDBContainer.getPassword());
    }
  }
}

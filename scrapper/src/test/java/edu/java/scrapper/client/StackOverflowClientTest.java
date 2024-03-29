package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import edu.java.scrapper.IntegrationTest;
import edu.java.stackOverflow.ItemResponse;
import edu.java.stackOverflow.StackOverflowClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.OffsetDateTime;

@SpringBootTest
public class StackOverflowClientTest extends IntegrationTest {
    private static WireMockServer wireMockServer;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public StackOverflowClientTest(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @BeforeAll
    public static void beforeAll() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    @AfterAll
    public static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    void fetchQuestion() {
        StackOverflowClient stackOverflowClient =
            new StackOverflowClient(webClientBuilder, "http://localhost:8080");
        String id = "21007680";
        stubFor(get(urlPathMatching(String.format("/questions/%s", id)))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withBody(
                    """
                        {   "items" :
                            [
                                {
                                    "title": "Semaphore in Java",
                                    "last_activity_date": "2014-01-08T22:32:49Z"
                                }
                            ]
                        }
                        """
                )));
        ItemResponse itemResponse = stackOverflowClient.fetchQuestion(id);
        Assertions.assertEquals("Semaphore in Java", itemResponse.title());
        Assertions.assertEquals(OffsetDateTime.parse("2014-01-08T22:32:49Z"), itemResponse.lastActivityDate());
    }
}

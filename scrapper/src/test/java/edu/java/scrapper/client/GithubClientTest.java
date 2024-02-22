package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import edu.java.github.GithubClient;
import edu.java.github.RepositoryResponse;
import edu.java.github.impl.GithubClientImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

@SpringBootTest
public class GithubClientTest {

    private static WireMockServer wireMockServer;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public GithubClientTest(WebClient.Builder webClientBuilder) {
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
    void fetchRepoTest() {
        final GithubClient underTest = new GithubClientImpl(webClientBuilder, "http://localhost:8080");
        final String owner = "Markelych32";
        final String repo = "Book_REST_API";
        stubFor(get(urlPathMatching(String.format("/repos/%s/%s", owner, repo)))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withBody(
                    """
                        {
                            "id": 759504210,
                            "full_name": "Markelych32/Book_REST_API",
                            "updated_at": "2024-02-18T19:11:30Z"
                        }
                        """
                )));
        RepositoryResponse response = underTest.fetchRepository(owner, repo);
        Assertions.assertEquals("759504210", response.id());
        Assertions.assertEquals("Markelych32/Book_REST_API", response.fullName());
        Assertions.assertEquals(OffsetDateTime.parse("2024-02-18T19:11:30Z"), response.lastUpdate());
    }
}

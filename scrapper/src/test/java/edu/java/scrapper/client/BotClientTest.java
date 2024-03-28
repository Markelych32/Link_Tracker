package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.BotClient;
import edu.java.configuration.ClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

@WireMockTest(httpPort = 8080)
public class BotClientTest {

}

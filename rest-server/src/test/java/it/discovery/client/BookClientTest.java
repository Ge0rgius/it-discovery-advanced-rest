package it.discovery.client;

import it.discovery.dto.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureWireMock(port = 9000)
class BookClientTest {

    BookClient bookClient;

    @BeforeEach
    void setup() {
        bookClient = new BookClient("http://localhost:9000");
    }

    @Test
    void findAll_singleBook_success() {
        stubFor(get(urlEqualTo("/api/books"))
                .willReturn(aResponse().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                [{"id": 1, "title":"WireMock", "author": "John Smith"}]
                                """
                        )));

        List<BookDTO> dtos = bookClient.findAll();
        assertEquals(1, dtos.size());
        assertEquals("WireMock", dtos.get(0).getTitle());

        verify(1, getRequestedFor(urlMatching("/api/books")));
    }
}
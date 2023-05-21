package it.discovery.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.discovery.dto.BookDTO;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;

public class BookClient {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public BookClient(String bookServiceUrl) {
        restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(bookServiceUrl));
        objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    public List<BookDTO> findAll() {
        List<Map<?, ?>> books = restTemplate.getForObject("/api/books", List.class);
        return books.stream().map(item -> objectMapper.convertValue(item, BookDTO.class)).toList();
    }
}

package it.discovery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.discovery.RestApplication;
import it.discovery.dto.BookDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    MockMvc mockMvc;

    static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    @Test
    @DisplayName("GET /api/books Returns single book at startup")
    void findAll_singleBook_bookReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("POST /api/books Creates new book without required fields")
    void save_titleIsEmpty_badRequest() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAmount(3);
        bookDTO.setAuthor("Jones");
        bookDTO.setYear(2022);

        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsBytes(bookDTO))).andExpect(status().isBadRequest());
    }

}


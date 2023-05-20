package it.discovery.controller.unit;

import it.discovery.RestApplication;
import it.discovery.dto.BookDTO;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class BookControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    JacksonTester<BookDTO> jacksonTester;

    @MockBean
    BookRepository bookRepository;

    @MockBean
    ApplicationRunner applicationRunner;

    @Test
    @DisplayName("GET /api/books Returns single book at startup")
    void findAll_singleBook_bookReturned() throws Exception {
        Book book = new Book();
        book.setAmount(3);
        book.setAuthor("Jones");
        book.setYear(2022);

        BDDMockito.given(bookRepository.findAll()).willReturn(List.of(book));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].amount", equalTo(3)));

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("POST /api/books Creates new book without required fields")
    void save_titleIsEmpty_badRequest() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAmount(3);
        bookDTO.setAuthor("Jones");
        bookDTO.setYear(2022);

        mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jacksonTester.write(bookDTO).getJson())).andExpect(status().isBadRequest());

        //TODO fix (another invocation in RestApplication)
        verify(bookRepository, never()).save(any(Book.class));
    }

}


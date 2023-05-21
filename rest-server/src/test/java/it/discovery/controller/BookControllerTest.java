package it.discovery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.discovery.RestApplication;
import it.discovery.dto.BookDTO;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import it.discovery.version.v1.BookV1DTO;
import it.discovery.version.v2.BookV2DTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = RestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class BookControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BookRepository bookRepository;

    static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    @Test
    @DisplayName("GET /api/books Returns single book at startup with default(v1) version")
    void findAll_singleBook_bookReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", equalTo("REST API")));
    }

    @Test
    @DisplayName("GET /api/books/1 Returns single book with ETag header")
    void findById_bookExists_eTagReturned() throws Exception {
        Book book = bookRepository.findById(1).orElseThrow();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo("REST API")))
                .andExpect(header().string(HttpHeaders.ETAG, "\"" + book.getVersion().toString() + "\""));
    }

    @Test
    @DisplayName("GET /api/books Returns single book at startup with v2 version")
    void findAll_v2version_returnsSingleBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books?version=v2")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", equalTo("REST API")));
    }

    @Test
    //FIX validation
    @DisplayName("POST /api/books Creates new book without required fields")
    void save_titleIsEmpty_badRequest() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAmount(3);
        bookDTO.setAuthor("Jones");
        bookDTO.setYear(2022);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(MAPPER.writeValueAsBytes(bookDTO))).andExpect(status().isBadRequest())
                .andDo(document("{class-name}/{method-name}",
                        requestFields(fieldWithPath("id").description("Book identifier"),
                                fieldWithPath("title").description("Book title"),
                                fieldWithPath("author").description("Book author"),
                                fieldWithPath("year").description("Publishing year"),
                                fieldWithPath("orders").description("Book orders"),
                                fieldWithPath("amount").description("Number of the books in the store"))));
    }

    @Test
    @DisplayName("POST /api/books Creates new book with all required fields and without API version")
    void save_noVersion_v1Used() throws Exception {
        BookV1DTO bookDTO = new BookV1DTO();
        bookDTO.setAmount(3);
        bookDTO.setAuthor("Jones");
        bookDTO.setYear(2022);
        bookDTO.setTitle("N/A");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsBytes(bookDTO))).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/books Creates new book with all required fields and with v2 API version")
    void save_v2version_success() throws Exception {
        BookV2DTO bookDTO = new BookV2DTO();
        bookDTO.setAmount(3);
        bookDTO.setAuthor("Jones");
        bookDTO.setYear(2022);
        bookDTO.setName("N/A");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/books?version=v2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsBytes(bookDTO))).andExpect(status().isCreated());
    }

}


package it.discovery.controller.restassured;

import io.restassured.http.ContentType;
import it.discovery.RestApplication;
import it.discovery.dto.BookDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/books Returns single book at startup")
    void findAll_singleBook_bookReturned() {
        Integer bookCount = given().mockMvc(mockMvc).when().get("/api/books").then().status(HttpStatus.OK)
                .extract().response().jsonPath().getObject("size()", Integer.class);
        assertEquals(1, bookCount);
    }

    @Test
    @DisplayName("POST /api/books Creates new book without required fields")
    void save_titleIsEmpty_badRequest() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAmount(3);
        bookDTO.setAuthor("Jones");
        bookDTO.setYear(2022);

        given().mockMvc(mockMvc).body(bookDTO).contentType(ContentType.JSON).when().post("/api/books")
                .then().status(HttpStatus.BAD_REQUEST);
    }

}


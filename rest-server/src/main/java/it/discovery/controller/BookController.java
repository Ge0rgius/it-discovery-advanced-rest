package it.discovery.controller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.discovery.dto.BookDTO;
import it.discovery.error.handling.BookNotFoundException;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import it.discovery.version.ApiVersion;
import it.discovery.version.BookFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookFactory bookFactory;

    @GetMapping
    @Timed("books.findAll")
    @CacheResult(cacheName = "books")
    @Operation(summary = "Returns all the existing books in the store", responses = {
            @ApiResponse(responseCode = "200", description = "Success")
    })
    public List<?> findAll(@RequestParam(name = "version", required = false)
                           ApiVersion version) {
        return bookRepository.findAll().stream()
                .map(book -> bookFactory.transform(book, version)).toList();
    }

    @GetMapping("{id}")
    //TODO How to support ResponseEntity<Book> and BookDTO values for the same cache?
    @CacheResult(cacheName = "book")
    public ResponseEntity<Book> findById(@PathVariable int id) {
        if (id <= 0) {
            //return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
//        return bookRepository.findById(id).map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
        return bookRepository.findById(id).map(ResponseEntity::ok)
                .orElseThrow(() -> new BookNotFoundException(id));
//        return bookRepository.findById(id).map(ResponseEntity::ok)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adds new book", responses = {
            @ApiResponse(description = "Success", responseCode = "201"),
            @ApiResponse(description = "Validation failed", responseCode = "400")
    })
    public void save(/*@Valid @RequestBody BookDTO book*/HttpServletRequest request,
                                                         @RequestParam(name = "version", required = false)
                                                         ApiVersion version) throws IOException {
        byte[] bytes = request.getInputStream().readAllBytes();
        Book book = bookFactory.untransform(bytes, version);
        bookRepository.save(modelMapper.map(book, Book.class));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CachePut(cacheName = "books")
    public void update(@PathVariable int id, @CacheValue @Valid @RequestBody BookDTO book) {
        bookRepository.save(modelMapper.map(book, Book.class));
    }

    @DeleteMapping
    @CacheRemove(cacheName = "book")
    public void delete(@PathVariable int id) {
        bookRepository.delete(id);
    }

//    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public Book sampleBook() {
//        return new Book();
//    }


}

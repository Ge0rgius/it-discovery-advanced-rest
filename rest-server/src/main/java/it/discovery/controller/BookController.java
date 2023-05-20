package it.discovery.controller;

import io.micrometer.core.annotation.Timed;
import it.discovery.dto.BookDTO;
import it.discovery.error.handling.BookNotFoundException;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @Timed("books.findAll")
    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDTO.class)).toList();
    }

    @GetMapping("{id}")
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
    public void save(@Valid @RequestBody BookDTO book) {
        bookRepository.save(modelMapper.map(book, Book.class));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody BookDTO book) {
        bookRepository.save(modelMapper.map(book, Book.class));
    }

    @DeleteMapping
    public void delete(@PathVariable int id) {
        bookRepository.delete(id);
    }

//    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public Book sampleBook() {
//        return new Book();
//    }


}

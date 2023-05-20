package it.discovery.error.handling;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(int bookId) {
        super("Book not found: " + bookId);
    }
}

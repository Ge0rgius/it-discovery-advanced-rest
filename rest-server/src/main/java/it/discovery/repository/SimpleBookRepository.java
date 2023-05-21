package it.discovery.repository;

import it.discovery.event.BookAddedEvent;
import it.discovery.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequiredArgsConstructor
public class SimpleBookRepository implements BookRepository {
    private final Map<Integer, Book> books = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger();

    private final ApplicationEventPublisher publisher;

    @Override
    public Optional<Book> findById(int id) {
        return Optional.ofNullable(books.get(id));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    @Override
    public void save(Book book) {
        if (book.getId() == 0) {
            int id = counter.incrementAndGet();
            book.setId(id);
            books.put(id, book);
            System.out.println("*** Book with id=" + book.getId() + " was created");

            publisher.publishEvent(new BookAddedEvent(book));
        } else {
            books.put(book.getId(), book);
            System.out.println("*** Book with id=" + book.getId() + " was updated");
        }
        book.incrementVersion();
    }

    @Override
    public boolean delete(int id) {
        if (!books.containsKey(id)) {
            return false;
        }

        books.remove(id);
        System.out.println("*** Book with id=" + id + " was deleted");
        return true;
    }

}

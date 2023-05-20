package it.discovery.repository;

import it.discovery.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(int id);

	List<Book> findAll();

	void save(Book book);

	boolean delete(int id);

}

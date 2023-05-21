package it.discovery.version;

import it.discovery.model.Book;

public interface BookFactory {

    Book untransform(byte[] data, ApiVersion apiVersion);

    Object transform(Book book, ApiVersion apiVersion);
}

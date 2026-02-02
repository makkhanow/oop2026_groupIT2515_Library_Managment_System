package edu.aitu.oop3.Repositories;

import edu.aitu.oop3.Entities.Book;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean deleteById(ID id);

    List<Book> listAvailableBooks();
}

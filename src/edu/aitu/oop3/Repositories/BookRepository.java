package edu.aitu.oop3.Repositories;

import edu.aitu.oop3.Entities.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(Connection con, long id) throws SQLException;
    List<Book> findAvailable(Connection con) throws SQLException;

    // Делает available_copies = available_copies - 1 если > 0
    boolean decreaseAvailable(Connection con, long bookId) throws SQLException;

    // Делает available_copies = available_copies + 1
    void increaseAvailable(Connection con, long bookId) throws SQLException;
}

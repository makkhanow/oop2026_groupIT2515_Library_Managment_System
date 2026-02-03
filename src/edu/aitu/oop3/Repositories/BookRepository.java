package edu.aitu.oop3.Repositories;

import edu.aitu.oop3.Entities.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findAvailable(Connection con) throws SQLException;
    boolean decreaseAvailable(Connection con, long bookId) throws SQLException;
    void increaseAvailable(Connection con, long bookId) throws SQLException;
}

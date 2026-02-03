package edu.aitu.oop3.Repositories;

import edu.aitu.oop3.Entities.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findById(Connection con, long id) throws SQLException;
    List<Book> findAvailable(Connection con) throws SQLException;


    boolean decreaseAvailable(Connection con, long bookId) throws SQLException;


    void increaseAvailable(Connection con, long bookId) throws SQLException;

    Optional<Book> findById(Connection con, Long aLong) throws SQLException;
}




package edu.aitu.oop3.repositoriesimpl;

import edu.aitu.oop3.Entities.Book;
import edu.aitu.oop3.Repositories.BookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryJdbc implements BookRepository {

    @Override
    public Optional<Book> findById(Connection con, long id) throws SQLException {
        String sql = "SELECT id, title, author, isbn, total_copies, available_copies FROM books WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        }
    }

    @Override
    public List<Book> findAvailable(Connection con) throws SQLException {
        String sql = "SELECT id, title, author, isbn, total_copies, available_copies FROM books WHERE available_copies > 0 ORDER BY id";
        List<Book> list = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    @Override
    public boolean decreaseAvailable(Connection con, long bookId) throws SQLException {
        String sql = "UPDATE books SET available_copies = available_copies - 1 WHERE id = ? AND available_copies > 0";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, bookId);
            int updated = st.executeUpdate();
            return updated == 1;
        }
    }

    @Override
    public void increaseAvailable(Connection con, long bookId) throws SQLException {
        String sql = "UPDATE books SET available_copies = available_copies + 1 WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, bookId);
            st.executeUpdate();
        }
    }

    private Book map(ResultSet rs) throws SQLException {
        return new Book(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getInt("total_copies"),
                rs.getInt("available_copies")
        );
    }

    @Override
    public Optional<Book> findById(Connection con, Long aLong) throws SQLException {
        return Optional.empty();
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Book save(Book entity) {
        return null;
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public Optional<Book> findById(Long aLong) {
        return Optional.empty();
    }

    /**
     * @return
     */
    @Override
    public List<Book> findAll() {
        return List.of();
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }

    /**
     * @return
     */
    @Override
    public List<Book> listAvailableBooks() {
        return List.of();
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }

    /**
     * @return
     */
    @Override
    public List<Book> listAvailableBooks() {
        return List.of();
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public boolean deleteById(long aLong) {
        return false;
    }
}

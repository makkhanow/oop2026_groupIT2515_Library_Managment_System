package edu.aitu.oop3.repositoriesimpl;

import edu.aitu.oop3.Entities.Book;
import edu.aitu.oop3.Entities.Loan;
import edu.aitu.oop3.Repositories.LoanRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanRepositoryJdbc implements LoanRepository {

    @Override
    public Optional<Loan> findActiveLoanByBook(Connection con, long bookId) throws SQLException {
        String sql = """
                SELECT id, book_id, member_id, loan_date, due_date, return_date
                FROM loans
                WHERE book_id = ? AND return_date IS NULL
                """;
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, bookId);
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        }
    }

    @Override
    public List<Loan> findActiveLoansByMember(Connection con, long memberId) throws SQLException {
        String sql = """
                SELECT id, book_id, member_id, loan_date, due_date, return_date
                FROM loans
                WHERE member_id = ? AND return_date IS NULL
                ORDER BY due_date
                """;
        List<Loan> list = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, memberId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    @Override
    public Loan createLoan(Connection con, long bookId, long memberId, LocalDate loanDate, LocalDate dueDate) throws SQLException {
        String sql = """
                INSERT INTO loans(book_id, member_id, loan_date, due_date, return_date)
                VALUES (?, ?, ?, ?, NULL)
                RETURNING id, book_id, member_id, loan_date, due_date, return_date
                """;
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, bookId);
            st.setLong(2, memberId);
            st.setDate(3, Date.valueOf(loanDate));
            st.setDate(4, Date.valueOf(dueDate));

            try (ResultSet rs = st.executeQuery()) {
                rs.next();
                return map(rs);
            }
        }
    }

    @Override
    public void closeLoan(Connection con, long loanId, LocalDate returnDate) throws SQLException {
        String sql = "UPDATE loans SET return_date = ? WHERE id = ? AND return_date IS NULL";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setDate(1, Date.valueOf(returnDate));
            st.setLong(2, loanId);
            st.executeUpdate();
        }
    }

    private Loan map(ResultSet rs) throws SQLException {
        Date ret = rs.getDate("return_date");
        LocalDate returnDate = (ret == null) ? null : ret.toLocalDate();

        return new Loan(
                rs.getLong("id"),
                rs.getLong("book_id"),
                rs.getLong("member_id"),
                rs.getDate("loan_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                returnDate
        );
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public Loan save(Loan entity) {
        return null;
    }

    /**
     * @param aLong
     * @return
     */
    @Override
    public Optional<Loan> findById(Long aLong) {
        return Optional.empty();
    }

    /**
     * @return
     */
    @Override
    public List<Loan> findAll() {
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
}

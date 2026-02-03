package edu.aitu.oop3.Repositories;

import edu.aitu.oop3.Entities.Loan;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends CrudRepository<Loan, Long> {
    Optional<Loan> findActiveLoanByBook(Connection con, long bookId) throws SQLException;

    List<Loan> findActiveLoansByMember(Connection con, long memberId) throws SQLException;

    Loan createLoan(Connection con, long bookId, long memberId, LocalDate loanDate, LocalDate dueDate) throws SQLException;

    void closeLoan(Connection con, long loanId, LocalDate returnDate) throws SQLException;

    void update(Optional<Loan> loan);
}

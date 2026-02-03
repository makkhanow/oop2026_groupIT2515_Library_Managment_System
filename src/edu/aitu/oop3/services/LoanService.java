package edu.aitu.oop3.services;

import edu.aitu.oop3.controllers.MemberSummary;
import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.Entities.Loan;
import edu.aitu.oop3.Exceptions.BookAlreadyOnLoanException;
import edu.aitu.oop3.Exceptions.LoanOverdueException;
import edu.aitu.oop3.Exceptions.MemberNotFoundException;
import edu.aitu.oop3.Repositories.BookRepository;
import edu.aitu.oop3.Repositories.LoanRepository;
import edu.aitu.oop3.Repositories.MemberRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private final IDB db;
    private final BookRepository bookRepo;
    private final MemberRepository memberRepo;
    private final LoanRepository loanRepo;
    private final FineCalculator fineCalculator;

    private final int defaultLoanDays;

    public LoanService(IDB db,
                       BookRepository bookRepo,
                       MemberRepository memberRepo,
                       LoanRepository loanRepo,
                       FineCalculator fineCalculator,
                       int defaultLoanDays) {
        this.db = db;
        this.bookRepo = bookRepo;
        this.memberRepo = memberRepo;
        this.loanRepo = loanRepo;
        this.fineCalculator = fineCalculator;
        this.defaultLoanDays = defaultLoanDays;
    }

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, MemberRepository memberRepository, FineCalculator fineCalculator) {

        this.db = db;
        this.bookRepo = bookRepo;
        this.memberRepo = memberRepo;
        this.loanRepo = loanRepo;
        this.fineCalculator = fineCalculator1;
        this.defaultLoanDays = defaultLoanDays;
    }




    // User story: list available books
    public List<edu.aitu.oop3.Entities.Book> listAvailableBooks() {
        try (Connection con = db.getConnection()) {
            return bookRepo.findAvailable(con);
        } catch (SQLException e) {
            throw new RuntimeException("DB error: listAvailableBooks", e);
        }
    }

    // User story: view current loans per member
    public List<Loan> viewCurrentLoansByMember(long memberId) {
        try (Connection con = db.getConnection()) {
            // member must exist
            if (memberRepo.findById(con, memberId).isEmpty()) {
                throw new MemberNotFoundException(memberId);
            }
            return loanRepo.findActiveLoansByMember(con, memberId);
        } catch (SQLException e) {
            throw new RuntimeException("DB error: viewCurrentLoansByMember", e);
        }
    }

    // User story: borrow book
    public Loan borrowBook(long memberId, long bookId) {
        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);

            // 1) member exists?
            if (memberRepo.findById(con, memberId).isEmpty()) {
                con.rollback();
                throw new MemberNotFoundException(memberId);
            }

            // 2) book exists + decrease available
            boolean decreased = bookRepo.decreaseAvailable(con, bookId);
            if (!decreased) {
                con.rollback();
                throw new BookAlreadyOnLoanException(bookId);
            }

            // 3) ensure no active loan for this book (unique index should protect too)
            if (loanRepo.findActiveLoanByBook(con, bookId).isPresent()) {
                con.rollback();
                // вернуть обратно available, чтобы не терять копию
                bookRepo.increaseAvailable(con, bookId);
                con.commit();
                throw new BookAlreadyOnLoanException(bookId);
            }

            // 4) create loan
            LocalDate today = LocalDate.now();
            LocalDate due = today.plusDays(defaultLoanDays);
            Loan created = loanRepo.createLoan(con, bookId, memberId, today, due);

            con.commit();
            return created;

        } catch (BookAlreadyOnLoanException | MemberNotFoundException ex) {
            throw ex;
        } catch (SQLException e) {
            throw new RuntimeException("DB error: borrowBook", e);
        }
    }

    // User story: return book
    public double returnBook(long bookId) {
        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);

            Loan active = loanRepo.findActiveLoanByBook(con, bookId)
                    .orElseThrow(() -> new RuntimeException("No active loan found for book id=" + bookId));

            LocalDate today = LocalDate.now();

            // Если просрочено — кидаем exception (по ТЗ), но при этом всё равно вернём книгу правильно:
            boolean overdue = today.isAfter(active.getDueDate());

            loanRepo.closeLoan(con, active.getId(), today);
            bookRepo.increaseAvailable(con, bookId);

            con.commit();

            double fine = fineCalculator.calculate(active.getDueDate(), today);

            if (overdue) {
                // по заданию нужна ошибка "loan overdue"
                throw new LoanOverdueException(active.getId(), active.getDueDate());
            }

            return fine;

        } catch (LoanOverdueException ex) {
            // Штраф уже посчитан/можно посчитать отдельно, но по ТЗ кидаем ошибку.
            throw ex;
        } catch (SQLException e) {
            throw new RuntimeException("DB error: returnBook", e);
        }
    }

    public MemberSummary generateMemberSummary(int memberId) {


        return null;
    }

    public <LoanReport> LoanReport generateLoanReport(int loanId) {
        return null;
    }

    public void listOverdueLoans() {

    }
}

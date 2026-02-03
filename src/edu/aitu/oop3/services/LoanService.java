package edu.aitu.oop3.services;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.dto.MemberSummary;

import edu.aitu.oop3.Entities.Book;
import edu.aitu.oop3.Entities.Loan;
import edu.aitu.oop3.Entities.Member;

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

    public List<Book> listAvailableBooks() {
        try (Connection con = db.getConnection()) {
            return bookRepo.findAvailable(con);
        } catch (SQLException e) {
            throw new RuntimeException("DB error: listAvailableBooks", e);
        }
    }

    public List<Loan> viewCurrentLoansByMember(long memberId) {
        try (Connection con = db.getConnection()) {
            if (memberRepo.findById( memberId).isEmpty()) {
                throw new MemberNotFoundException(memberId);
            }
            return loanRepo.findActiveLoansByMember(con, memberId);
        } catch (SQLException e) {
            throw new RuntimeException("DB error: viewCurrentLoansByMember", e);
        }
    }

    public MemberSummary buildMemberSummary(long memberId) {
        try (Connection con = db.getConnection()) {
            Member member = memberRepo.findById( memberId)
                    .orElseThrow(() -> new MemberNotFoundException(memberId));

            List<Loan> loans = loanRepo.findActiveLoansByMember(con, memberId);

            LocalDate today = LocalDate.now();
            double totalFine = 0;
            for (Loan l : loans) {
                totalFine += fineCalculator.calculate(l.getDueDate(), today);
            }

            return new MemberSummary.Builder()
                    .member(member)
                    .activeLoans(loans)
                    .totalFine(totalFine)
                    .build();

        } catch (SQLException e) {
            throw new RuntimeException("DB error: buildMemberSummary", e);
        }
    }

    public Loan borrowBook(long memberId, long bookId) {
        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);

            if (memberRepo.findById( memberId).isEmpty()) {
                con.rollback();
                throw new MemberNotFoundException(memberId);
            }

            boolean decreased = bookRepo.decreaseAvailable(con, bookId);
            if (!decreased) {
                con.rollback();
                throw new BookAlreadyOnLoanException(bookId);
            }

            if (loanRepo.findActiveLoanByBook(con, bookId).isPresent()) {
                bookRepo.increaseAvailable(con, bookId);
                con.commit();
                throw new BookAlreadyOnLoanException(bookId);
            }

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

    public double returnBook(long bookId) {
        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);

            Loan active = loanRepo.findActiveLoanByBook(con, bookId)
                    .orElseThrow(() -> new RuntimeException("No active loan for bookId=" + bookId));

            LocalDate today = LocalDate.now();
            boolean overdue = today.isAfter(active.getDueDate());

            loanRepo.closeLoan(con, active.getId(), today);
            bookRepo.increaseAvailable(con, bookId);

            con.commit();

            double fine = fineCalculator.calculate(active.getDueDate(), today);

            if (overdue) {
                throw new LoanOverdueException(active.getId(), active.getDueDate());
            }

            return fine;

        } catch (LoanOverdueException ex) {
            throw ex;
        } catch (SQLException e) {
            throw new RuntimeException("DB error: returnBook", e);
        }
    }
}

package edu.aitu.oop3.app;

import edu.aitu.oop3.Entities.Loan;
import edu.aitu.oop3.config.LibraryConfig;
import edu.aitu.oop3.controllers.LibraryController;
import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.db.SupabaseDB;

import edu.aitu.oop3.Repositories.BookRepository;
import edu.aitu.oop3.Repositories.MemberRepository;
import edu.aitu.oop3.Repositories.LoanRepository;

import edu.aitu.oop3.repositoriesimpl.BookRepositoryJdbc;
import edu.aitu.oop3.repositoriesimpl.MemberRepositoryJdbc;
import edu.aitu.oop3.repositoriesimpl.LoanRepositoryJdbc;

import edu.aitu.oop3.services.FineCalculator;
import edu.aitu.oop3.services.SimpleFineCalculator;
import edu.aitu.oop3.services.LoanService;

import java.util.Optional;

public class Main {

    public static void main(String[] args) {


        String url = "jdbc:postgresql://YOUR_HOST:5432/postgres?sslmode=require";
        String user = "YOUR_DB_USER";
        String password = "YOUR_DB_PASSWORD";

        IDB db = new SupabaseDB(url, user, password);


        BookRepository bookRepo = new BookRepositoryJdbc();
        MemberRepository memberRepo = new MemberRepositoryJdbc();
        LoanRepository loanRepo = new LoanRepositoryJdbc() {
            /**
             * @param loan
             */
            @Override
            public void update(Optional<Loan> loan) {

            }
        };
        LibraryConfig cfg = LibraryConfig.getInstance();


        FineCalculator fineCalculator = new SimpleFineCalculator(cfg.getFinePerDay());

        LoanService loanService = new LoanService(
                db,
                bookRepo,
                memberRepo,
                loanRepo,
                fineCalculator,
                cfg.getDefaultLoanDays()
        );

        LibraryController controller = new LibraryController(loanService);
        controller.start();
    }
}

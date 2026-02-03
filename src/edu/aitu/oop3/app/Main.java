package edu.aitu.oop3.app;

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

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://aws-1-ap-southeast-2.pooler.supabase.com:5432/postgres?sslmode=require";
        String user = "postgres.yoylqddvtcevkdhyktlp";
        String password = "";

        IDB db = new SupabaseDB(url, user, password);

        // 🔹 2) Repositories
        BookRepository bookRepo = new BookRepositoryJdbc();
        MemberRepository memberRepo = new MemberRepositoryJdbc();
        LoanRepository loanRepo = new LoanRepositoryJdbc();

        // 🔹 3) Singleton config
        LibraryConfig config = LibraryConfig.getInstance();

        // 🔹 4) Services
        FineCalculator fineCalculator =
                new SimpleFineCalculator(config.getFinePerDay());

        LoanService loanService = new LoanService(
                db,
                bookRepo,
                memberRepo,
                loanRepo,
                fineCalculator,
                config.getDefaultLoanDays()
        );

        // 🔹 5) Controller (UI)
        LibraryController controller = new LibraryController(loanService);
        controller.start();
    }
}

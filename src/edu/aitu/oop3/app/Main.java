package edu.aitu.oop3.app;

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
        String password = "DB_PASWORD";

        IDB db = new SupabaseDB(url, user, password);

        BookRepository bookRepo = new BookRepositoryJdbc();
        MemberRepository memberRepo = new MemberRepositoryJdbc();
        LoanRepository loanRepo = new LoanRepositoryJdbc();

        FineCalculator fineCalculator = new SimpleFineCalculator(500.0);

        LoanService loanService = new LoanService(
                db, bookRepo, memberRepo, loanRepo, fineCalculator, 14
        );

        LibraryController controller = new LibraryController(loanService);
        controller.start();
    }
}

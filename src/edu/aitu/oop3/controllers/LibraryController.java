package edu.aitu.oop3.controllers;

import edu.aitu.oop3.Entities.Book;
import edu.aitu.oop3.Entities.Loan;
import edu.aitu.oop3.Exceptions.BookAlreadyOnLoanException;
import edu.aitu.oop3.Exceptions.LoanOverdueException;
import edu.aitu.oop3.Exceptions.MemberNotFoundException;
import edu.aitu.oop3.services.LoanService;

import java.util.List;
import java.util.Scanner;

public class LibraryController {
    private final LoanService loanService;

    public LibraryController(LoanService loanService) {
        this.loanService = loanService;
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> listAvailableBooks();
                    case "2" -> borrowBook(sc);
                    case "3" -> returnBook(sc);
                    case "4" -> viewLoansByMember(sc);
                    case "0" -> {
                        System.out.println("Bye!");
                        return;
                    }
                    default -> System.out.println("Unknown option. Try again.");
                }
            } catch (MemberNotFoundException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (BookAlreadyOnLoanException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (LoanOverdueException e) {
                System.out.println("OVERDUE: " + e.getMessage());
                System.out.println("Book was returned, but it was overdue. (You can calculate fine if needed.)");
            } catch (RuntimeException e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("=== Library Management System ===");
        System.out.println("1) List available books");
        System.out.println("2) Borrow book");
        System.out.println("3) Return book");
        System.out.println("4) View current loans per member");
        System.out.println("0) Exit");
        System.out.print("Choose option: ");
    }

    private void listAvailableBooks() {
        List<Book> books = loanService.listAvailableBooks();
        if (books.isEmpty()) {
            System.out.println("No available books.");
            return;
        }
        for (Book b : books) {
            System.out.println(b);
        }
    }

    private void borrowBook(Scanner sc) {
        System.out.print("Enter memberId: ");
        long memberId = Long.parseLong(sc.nextLine().trim());

        System.out.print("Enter bookId: ");
        long bookId = Long.parseLong(sc.nextLine().trim());

        Loan loan = loanService.borrowBook(memberId, bookId);
        System.out.println("SUCCESS: Book borrowed!");
        System.out.println("Loan: " + loan);
    }

    private void returnBook(Scanner sc) {
        System.out.print("Enter bookId: ");
        long bookId = Long.parseLong(sc.nextLine().trim());

        double fine = loanService.returnBook(bookId);
        System.out.println("SUCCESS: Book returned!");
        System.out.println("Fine: " + fine);
    }

    private void viewLoansByMember(Scanner sc) {
        System.out.print("Enter memberId: ");
        long memberId = Long.parseLong(sc.nextLine().trim());

        List<Loan> loans = loanService.viewCurrentLoansByMember(memberId);
        if (loans.isEmpty()) {
            System.out.println("No active loans for this member.");
            return;
        }
        for (Loan l : loans) {
            System.out.println(l);
        }
    }
}

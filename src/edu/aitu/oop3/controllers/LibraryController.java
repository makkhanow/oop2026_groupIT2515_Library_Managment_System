package edu.aitu.oop3.controllers;

import edu.aitu.oop3.services.LoanService;

import java.util.Scanner;

public class LibraryController {

    private final LoanService loanService;

    public LibraryController(LoanService loanService) {
        this.loanService = loanService;
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1) List available books");
            System.out.println("2) Borrow book");
            System.out.println("3) Return book");
            System.out.println("4) View current loans per member");
            System.out.println("5) Member summary");
            System.out.println("0) Exit");
            System.out.print("Choose option: ");

            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> listAvailableBooks();
                    case "2" -> borrowBook(sc);
                    case "3" -> returnBook(sc);
                    case "4" -> viewLoansByMember(sc);
                    case "5" -> memberSummary(sc);
                    case "0" -> {
                        System.out.println("Bye!");
                        return;
                    }
                    default -> System.out.println("Wrong option!");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void listAvailableBooks() {
        var books = loanService.listAvailableBooks();
        if (books.isEmpty()) {
            System.out.println("No available books.");
            return;
        }
        for (var b : books) System.out.println(b);
    }

    private void borrowBook(Scanner sc) {
        System.out.print("Enter memberId: ");
        long memberId = Long.parseLong(sc.nextLine().trim());

        System.out.print("Enter bookId: ");
        long bookId = Long.parseLong(sc.nextLine().trim());

        var loan = loanService.borrowBook(memberId, bookId);
        System.out.println("Borrowed successfully. Loan: " + loan);
    }

    private void returnBook(Scanner sc) {
        System.out.print("Enter bookId: ");
        long bookId = Long.parseLong(sc.nextLine().trim());

        double fine = loanService.returnBook(bookId);
        System.out.println("Returned successfully. Fine: " + fine);
    }

    private void viewLoansByMember(Scanner sc) {
        System.out.print("Enter memberId: ");
        long memberId = Long.parseLong(sc.nextLine().trim());

        var loans = loanService.viewCurrentLoansByMember(memberId);
        if (loans.isEmpty()) {
            System.out.println("No active loans.");
            return;
        }
        for (var l : loans) System.out.println(l);
    }

    private void memberSummary(Scanner sc) {
        System.out.print("Enter memberId: ");
        long memberId = Long.parseLong(sc.nextLine().trim());

        var summary = loanService.buildMemberSummary(memberId);

        System.out.println("Member: " + summary.getMember());
        System.out.println("Active loans:");
        for (var l : summary.getActiveLoans()) System.out.println(l);
        System.out.println("Total fine (if returned today): " + summary.getTotalFine());
    }
}

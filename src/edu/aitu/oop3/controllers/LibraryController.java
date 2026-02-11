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
                    case "1":
                        listAvailableBooks();
                        break;
                    case "2":
                        borrowBook(sc);
                        break;
                    case "3":
                        returnBook(sc);
                        break;
                    case "4":
                        viewLoansByMember(sc);
                        break;
                    case "5":
                        memberSummary(sc);
                        break;
                    case "0":
                        System.out.println("Bye!");
                        return;
                    default:
                        System.out.println("Wrong option!");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void listAvailableBooks() {
        var books = loanService.listAvailableBooks();
        if (books == null || books.isEmpty()) {
            System.out.println("No available books.");
            return;
        }
        for (var b : books) {
            System.out.println(b);
        }
    }

    private void borrowBook(Scanner sc) {
        long memberId = readLong(sc, "Enter memberId: ");
        long bookId = readLong(sc, "Enter bookId: ");

        var loan = loanService.borrowBook(memberId, bookId);
        System.out.println("Borrowed successfully. Loan: " + loan);
    }

    private void returnBook(Scanner sc) {
        long bookId = readLong(sc, "Enter bookId: ");

        double fine = loanService.returnBook(bookId);
        System.out.println("Returned successfully. Fine: " + fine);
    }

    private void viewLoansByMember(Scanner sc) {
        long memberId = readLong(sc, "Enter memberId: ");

        var loans = loanService.viewCurrentLoansByMember(memberId);
        if (loans == null || loans.isEmpty()) {
            System.out.println("No active loans.");
            return;
        }
        for (var l : loans) {
            System.out.println(l);
        }
    }

    private void memberSummary(Scanner sc) {
        long memberId = readLong(sc, "Enter memberId: ");

        System.out.println("Member ID: " + memberId);
        System.out.println("Active loans:");

        var loans = loanService.viewCurrentLoansByMember(memberId);

        if (loans == null || loans.isEmpty()) {
            System.out.println("No active loans.");
            System.out.println("Total fine (if returned today): 0");
            return;
        }

        for (var l : loans) {
            System.out.println(l);
        }


        System.out.println("Total fine (if returned today): 0");
    }

    private long readLong(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Long.parseLong(s);} catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
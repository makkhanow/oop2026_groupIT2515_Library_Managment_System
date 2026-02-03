package edu.aitu.oop3.Entities;

import java.time.LocalDate;

public class Loan {
    private long id;
    private long bookId;
    private long memberId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // nullable

    public Loan(long id, long bookId, long memberId, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public long getId() { return id; }
    public long getBookId() { return bookId; }
    public long getMemberId() { return memberId; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                '}';
    }

    public boolean isOverdue() {

        return false;
    }

    public long daysOverdue() {
        return 0;
    }

    public void setReturnDate(LocalDate returnDate) {

    }
}

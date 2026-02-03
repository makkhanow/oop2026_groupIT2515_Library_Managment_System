package edu.aitu.oop3.Exceptions;
import java.time.LocalDate;

public class  LoanOverdueException extends RuntimeException {
    public LoanOverdueException(long loanId, LocalDate dueDate) {
        super("Loan is overdue: loanId=" + loanId + ", dueDate=" + dueDate);
    }

    public LoanOverdueException(String s) {

    }
}

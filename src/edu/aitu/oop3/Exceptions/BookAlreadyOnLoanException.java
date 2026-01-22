package edu.aitu.oop3.Exceptions;

public class BookAlreadyOnLoanException extends RuntimeException {
    public BookAlreadyOnLoanException(long bookId) {
        super("Book is already on loan OR not available: id=" + bookId);
    }
}

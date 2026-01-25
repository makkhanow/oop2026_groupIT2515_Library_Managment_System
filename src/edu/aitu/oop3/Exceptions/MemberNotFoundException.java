package edu.aitu.oop3.Exceptions;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(long memberId) {
        super("Member not found: id=" + memberId);
    }
}

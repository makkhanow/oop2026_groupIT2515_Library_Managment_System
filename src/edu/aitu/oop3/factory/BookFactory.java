package edu.aitu.oop3.factory;

import edu.aitu.oop3.Entities.*;

public class BookFactory {
    public static Book create(String type, long id, String title, String author, String isbn, int total, int available) {
        if (type == null) type = "PRINTED";
        return switch (type.toUpperCase()) {
            case "EBOOK" -> new EBook(id, title, author, isbn, total, available);
            case "REFERENCE" -> new ReferenceBook(id, title, author, isbn, total, available);
            default -> new PrintedBook(id, title, author, isbn, total, available);
        };
    }
}

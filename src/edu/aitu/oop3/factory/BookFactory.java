package edu.aitu.oop3.factory;

import edu.aitu.oop3.Entities.Book;
import edu.aitu.oop3.Entities.EBook;
import edu.aitu.oop3.Entities.PrintedBook;
import edu.aitu.oop3.Entities.ReferenceBook;

public class BookFactory {
    private BookFactory() {}

    public  static Book createBook(int id, String title, String author, boolean available, String type, int pages, String fileFormat, String subjectArea) {
        switch (type.toLowerCase()) {
            case "printed":
                return new PrintedBook(id, title, author, available, pages);
            case "ebook":
                return new EBook(id, title, author, available, fileFormat);
            case "reference":
                return new ReferenceBook(id, title, author, available, subjectArea);
            default:
                throw new IllegalArgumentException("Unknown book type: " + type);
        }
    }
}
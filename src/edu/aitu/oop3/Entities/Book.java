package edu.aitu.oop3.entities;

public class Book {
    private long id;
    private String title;
    private String author;
    private String type;
    private boolean available;

    public Book() {}

    public Book(long id, String title, String author, String type, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.type = type;
        this.available = available;
    }

    public Book(String title, String author, String type) {
        this(0, title, author, type, true);
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getType() { return type; }
    public boolean isAvailable() { return available; }

    public void setId(long id) { this.id = id; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return id + " | " + title + " | " + author + " | " + type + " | available=" + available;
    }
}

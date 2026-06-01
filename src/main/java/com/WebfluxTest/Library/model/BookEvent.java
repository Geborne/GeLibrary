package com.WebfluxTest.Library.model;

public class BookEvent {
    private Book book;
    private String eventType;

    public BookEvent(Book book, String eventType) {
        this.book = book;
        this.eventType = eventType;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}

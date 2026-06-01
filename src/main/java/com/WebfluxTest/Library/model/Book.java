package com.WebfluxTest.Library.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "books")
public class Book {

    @Id
    private String ID;
    private String title;
    private String author;
    private Double rating;

    // Construtor.
    public Book (String ID, String title, String author, Double rating) {
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.rating = rating;
    }

    // Sobrescreve equals e hashCode para comparação de objetos.
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Book book = (Book) object;
        return Objects.equals(ID, book.ID) && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(rating, book.rating);
    }

    // Sobrescreve hashCode para garantir que objetos iguais tenham o mesmo hash.
    @Override
    public int hashCode() {
        return Objects.hash(ID, title, author, rating);
    }

//Getters e Setters para os campos da classe.
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    // Sobrescreve toString para exibir informações do livro de forma legível caso já não sejam.
    @Override
    public String toString() {
        return "Book{" +
                "ID='" + ID + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", rating=" + rating +
                '}';
    }

}

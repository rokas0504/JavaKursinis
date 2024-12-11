package com.example.lab2weblayer.model;

import com.example.lab2weblayer.model.enums.Format;
import com.example.lab2weblayer.model.enums.Genre;
import com.example.lab2weblayer.model.enums.Language;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//You can generate getters and setters, here Lombok lib is used. They are generated for you
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book extends Publication {

    //Variables should be private and their values retrieved and changed using getters and setters respectively

    private String publisher;
    private String isbn;
    @Enumerated
    private Genre genre;
    private int pageCount;
    @Enumerated
    private Language language;
    private int publicationYear;
    @Enumerated
    private Format format;
    private String summary;
    @JsonIgnore //Do not return this in JSON structure
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Chat> chatList;

    public Book(String title, String author, String publisher, String isbn, Genre genre, int pageCount, Language language, int publicationYear, Format format, String summary) {
        super(title, author);
        this.publisher = publisher;
        this.isbn = isbn;
        this.genre = genre;
        this.pageCount = pageCount;
        this.language = language;
        this.publicationYear = publicationYear;
        this.format = format;
        this.summary = summary;
    }

    @Override
    public String toString() {
        return title + "(" + publicationYear + ")";
    }
}

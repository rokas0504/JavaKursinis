package com.example.lab2weblayer.model;

import com.example.lab2weblayer.model.enums.PublicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Publication implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected String title;
    protected String author;
    @ManyToOne
    protected Client owner;
    @ManyToOne
    protected Client client;
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL)
    protected List<PeriodicRecord> records;
    @Enumerated
    protected PublicationStatus publicationStatus;

    public Publication(String title, String author) {
        this.title = title;
        this.author = author;
    }
}

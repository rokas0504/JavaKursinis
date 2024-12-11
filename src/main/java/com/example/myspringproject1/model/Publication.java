package com.example.myspringproject1.model;

import com.example.myspringproject1.model.enums.PublicationStatus;
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

    @Enumerated(EnumType.STRING)
    protected PublicationStatus publicationStatus = PublicationStatus.AVAILABLE;

    protected boolean hidden;

    public Publication(String title, String author) {
        this.title = title;
        this.author = author;
        this.hidden = false;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}

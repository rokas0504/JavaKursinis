package com.example.myspringproject1.model;

import com.example.myspringproject1.model.enums.Demographic;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Manga extends Publication{

    private String illustrator;
    private int volumeNumber;
    @Enumerated
    private Demographic demographic;
    private boolean isColor;


    public Manga(String title, String author, String illustrator, int volumeNumber, Demographic demographic, boolean isColor) {
        super(title, author);
        this.illustrator = illustrator;
        this.volumeNumber = volumeNumber;
        this.demographic = demographic;
        this.isColor = isColor;
    }
}

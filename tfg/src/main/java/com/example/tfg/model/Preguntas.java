package com.example.tfg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Preguntas {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="idpreguntas")
    private Long id;

    @Column(nullable = false, length = 64)
    @NotBlank
    @Size(max = 64)
    private String name;

    @Column(nullable = false, length = 64)
    private Double nota;

    @Column
    private Double media_preg;   
    
    @ManyToOne(optional = false)
    private Quiz quiz;


    public Double getMedia_preg() {
        return media_preg;
    }

    public void setMedia_preg(Double media_preg) {
        this.media_preg = media_preg;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    } 

}

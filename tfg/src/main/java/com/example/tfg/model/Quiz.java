package com.example.tfg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


import java.util.Date;


@Entity
@Table(name = "quizes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idquizes")
    private Long id;

    @Column
    private String nombre_quiz;

    public String getNombre_quiz() {
        return nombre_quiz;
    }

    public void setNombre_quiz(String nombre_quiz) {
        this.nombre_quiz = nombre_quiz;
    }

    @Column
    private String estado;

    @Column
    private Date comenzado;

    @Column
    private Date finalizado;

    @Column
    private Long tiempo_requerido;

    @Column
    private Double calificacion;

    @Column
    private Double media;

    @Column
    private String comenzadoDate;

    public String getDateString(Date comenzado){
        String fullDate = comenzado.toString();
        String[] date = fullDate.split(" ");
        return date[0];
    }

    public String getComenzadoDate() {
        return comenzadoDate;
    }

    public void setComenzadoDate(Date comenzado) {
        this.comenzadoDate = comenzado.toString();
    }

    @ManyToOne(optional = false)
    private User student;

    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getComenzado() {
        return comenzado;
    }

    public void setComenzado(Date comenzado) {
        this.comenzado = comenzado;
    }

    public Date getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Date finalizado) {
        this.finalizado = finalizado;
    }

    public Long getTiempo_requerido() {
        return tiempo_requerido;
    }

    public void setTiempo_requerido(Long tiempo_requerido) {
        this.tiempo_requerido = tiempo_requerido;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Long getId() {
        return id;
    }

}

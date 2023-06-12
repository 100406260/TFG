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
@Table(name="logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="idlogs")
    private Long id;

    @Column
    private String eventname;

    @Column
    private String componente;

    @Column
    private String contexto;

    @Column
    private String descripcion;

    @Column
    private Date datestring;

    @Column 
    private String date;

    @Column
    private String time;   

    @ManyToOne(optional = false)
    private User student;

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getDatestring() {
        return datestring;
    }

    public void setDatestring(Date datestring) {
        this.datestring = datestring;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getComponente() {
        return componente;
    }

    public void setComponente(String componente) {
        this.componente = componente;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

}

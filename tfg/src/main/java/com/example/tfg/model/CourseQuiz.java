package com.example.tfg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="course_tasks")
public class CourseQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="idcourse_tasks")
    private Long id;

    @Column
    private String taskname;

    @Column
    private Double taskmark;

    @ManyToOne(optional = false)
    private User student;

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public String getTask_name() {
        return taskname;
    }

    public void setTask_name(String task_name) {
        this.taskname = task_name;
    }

    public Double getTask_mark() {
        return taskmark;
    }

    public void setTask_mark(Double task_mark) {
        this.taskmark = task_mark;
    }
    
}


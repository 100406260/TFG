package com.example.tfg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "students")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idstudents")
    private Long id;    
    
    public Long getId() {
        return id;
    }

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name="password_en")
    @NotNull
    private String password_en;

    @Column(nullable = false, length = 64, name = "student_name")
    @NotBlank
    @Size(max = 64)
    private String name;

    @Column(nullable = false, length = 64, name = "surnames")
    @NotBlank
    @Size(max = 64)
    private String surnames;

    @Column(unique = true, nullable = false, length = 64, name = "email")
    @Email
    @NotBlank
    @Size(max = 64)
    private String email;

    @Column(name = "fullname")
    private String fullname;

    @Column(name ="role")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_en() {
        return password_en;
    }

    public void setPassword_en(String password_en) {
        this.password_en = password_en;
    }

    public String getfullName() {
        return fullname;
    }

    public void setfullName() {
        String fullname_ = name.toUpperCase() + " " + surnames.toUpperCase();
        if (fullname_.contains("Á")) {
            fullname_.replace("Á", "A");
        }
        if (fullname_.contains("É")) {
            fullname_.replace("É", "E");
        }
        if (fullname_.contains("Í")) {
            fullname_.replace("Í", "I");
        }
        if (fullname_.contains("Ó")) {
            fullname_.replace("Ó", "O");
        }
        if (fullname_.contains("Ú")) {
            fullname_.replace("Ú", "U");
        }
        this.fullname = fullname_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

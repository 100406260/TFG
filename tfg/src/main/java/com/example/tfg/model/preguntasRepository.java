package com.example.tfg.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.example.tfg.model.Preguntas;
import com.example.tfg.model.Quiz;

public interface preguntasRepository extends CrudRepository<Preguntas, Long>{
    List<Preguntas> findByQuiz(Quiz quiz);
    //List<Preguntas> findByStudent(Student student);
}

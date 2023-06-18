package com.example.tfg.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.example.tfg.model.Quiz;
import com.example.tfg.model.User;

public interface quizRepository extends CrudRepository<Quiz, Long>{
    List<Quiz> findByStudent(User student);
    Optional<Quiz> findByStudentAndComenzadoDateContains(User student, String comenzado);
}

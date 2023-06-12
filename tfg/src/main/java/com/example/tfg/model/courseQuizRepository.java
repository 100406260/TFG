package com.example.tfg.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.example.tfg.model.CourseQuiz;
import com.example.tfg.model.User;


public interface courseQuizRepository extends CrudRepository<CourseQuiz, Long>{
    Optional<CourseQuiz> findByStudent(User student);
    List<CourseQuiz> findAllByStudent(User student);
    List<CourseQuiz> findByTasknameContains(String task_name);
}

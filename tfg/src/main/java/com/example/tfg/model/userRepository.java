package com.example.tfg.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.example.tfg.model.User;

public interface userRepository extends CrudRepository<User, Long>{
    User findAllByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByName(String name);
    Optional<User> findBySurnames(String surnames);
    Optional<User> findByEmail(String email);
    Optional<User> findByFullname(String fullname);
    Optional<User> findByEmailAndPassword(String email, String password);
    List<User> findAll();
}

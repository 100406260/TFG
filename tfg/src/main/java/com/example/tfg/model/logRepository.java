package com.example.tfg.model;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.data.repository.CrudRepository;
import com.example.tfg.model.Log;
import com.example.tfg.model.User;

public interface logRepository extends CrudRepository<Log, Long>{
    Optional<Log> findByStudentAndDatestring(User student, Date datestring);
    List<Log> findAllByStudent(User student);
    List<Log> findAllByStudentAndComponenteOrderByDatestring(User student, String componente);
    List<Log> findAllByStudentAndContextoAndComponenteAndEventnameOrderByDatestring(User student,String contexto,  String componente, String eventname);
    int countByStudentAndDescripcion(User student, String descripcion);
    List<Log> findAllByContextoContainsAndComponenteAndEventname(String contexto,  String componente, String eventname);
    List<Log> findAllByStudentAndContextoContainsAndComponenteAndEventname(User student, String contexto, String componente, String eventname);
    List<Log> findAllByContextoAndComponenteAndEventname(String contexto, String componente, String eventname);
    
}

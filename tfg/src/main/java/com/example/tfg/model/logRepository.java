package com.example.tfg.model;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.data.repository.CrudRepository;
import com.example.tfg.model.Log;
import com.example.tfg.model.User;

public interface logRepository extends CrudRepository<Log, Long>{
    Optional<Log> findByStudentAndDatestring(User User, Date datestring);
    Optional<Log> findByStudentAndComponenteAndDateContainsAndEventname(User user, String componente, String date, String eventname);
    Optional<Log> findByStudentAndContextoAndEventname(User student, String contexto, String eventname);
    List<Log> findAllByStudent(User User);
    List<Log> findAllByStudentAndComponenteOrderByDatestring(User User, String componente);
    List<Log> findAllByStudentAndComponenteAndEventname(User Student, String componente, String eventname);
    List<Log> findAllByStudentAndContextoAndComponenteAndEventnameOrderByDatestring(User User,String contexto,  String componente, String eventname);
    int countByStudentAndDescripcion(User User, String descripcion);
    List<Log> findAllByContextoContainsAndComponenteAndEventname(String contexto,  String componente, String eventname);
    List<Log> findAllByStudentAndContextoContainsAndComponenteAndEventname(User User, String contexto, String componente, String eventname);
    List<Log> findAllByContextoAndComponenteAndEventname(String contexto, String componente, String eventname);
    
}

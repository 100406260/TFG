package com.example.tfg.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.example.tfg.model.User;
import com.example.tfg.model.preguntasRepository;
import com.example.tfg.model.Preguntas;
import com.example.tfg.model.quizRepository;
import com.example.tfg.model.userRepository;
import com.example.tfg.model.Quiz;
import com.example.tfg.model.courseQuizRepository;
import com.example.tfg.model.logRepository;
import com.example.tfg.model.CourseQuiz;
import com.example.tfg.model.Log;
import com.example.tfg.services.courseService;
import com.example.tfg.services.quizReportService;
import com.example.tfg.services.logsService;


import org.springframework.ui.Model;

import com.google.gson.Gson;

@Controller
@RequestMapping("/")
public class UploadController {

    // atributos

    @Autowired
    private quizReportService quizReportService;

    @Autowired
    private courseService courseService;

    @Autowired
    private logsService logsService;

    @Autowired
    private logRepository logRepository;

    @Autowired
    private courseQuizRepository courseQuizRepository;

    @Autowired
    private quizRepository quizRepository;

    @Autowired
    private preguntasRepository preguntasRepository;

    @Autowired
    private userRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // metodos

    @GetMapping("/login")
    public String login() {
        Optional<User> teacherOpt = studentRepository.findByEmail("teacher@teacher.com");
        if(!teacherOpt.isPresent()){
            User teacher = new User();
            teacher.setEmail("teacher@teacher.com");
            teacher.setName("Teacher");
            teacher.setSurnames("Teacher");
            teacher.setRole("ADMIN");
            teacher.setPassword("Uploadfiles");
            teacher.setPassword_en(passwordEncoder.encode(teacher.getPassword()));
            studentRepository.save(teacher);
        }
        return "login";
    }

    @GetMapping(path = "/")
    public String mainView(Principal principal, Model model) {

        User student = studentRepository.findAllByEmail(principal.getName());

        model.addAttribute("student", student);

        return "main_view";
    }

    @GetMapping("/uploadfiles123")
    public String teacherPage() {
        return "teacher_view";
    }

    @PostMapping("/uploadQuiz")
    public String uploadQuizFile(Model model, @RequestParam("file") File file) throws IOException {

        ArrayList<User> student_list = quizReportService.saveStudent(file);
        ArrayList<Quiz> quiz_list = quizReportService.saveQuiz(file);
        ArrayList<Preguntas> preguntas_list = quizReportService.savePregunta(file);
        int pregunta = 0;

        // save the students and quizes
        for (int i = 0; i < student_list.size(); i++) {
            Optional<User> studentOpt = studentRepository.findByEmail(student_list.get(i).getEmail());
            if (!studentOpt.isPresent()) { // if student no present
                studentRepository.save(student_list.get(i)); // save
                quiz_list.get(i).setStudent(student_list.get(i));// set studnet in quiz
                quizRepository.save(quiz_list.get(i));// save quiz
                System.out.println((preguntas_list.size() / quiz_list.size()));
                for (int j = 0; j < (preguntas_list.size() / quiz_list.size()); j++) {
                    preguntas_list.get(pregunta).setQuiz(quiz_list.get(i));// set quiz in pregunta
                    preguntasRepository.save(preguntas_list.get(pregunta));// save pregunta
                    if (pregunta < preguntas_list.size()) {
                        pregunta++;
                    }
                }
            }
            if (studentOpt.isPresent()) {
                Optional<Quiz> noQuiz = quizRepository.findByStudentAndComenzado(studentOpt.get(),
                        quiz_list.get(i).getComenzado());
                if (!noQuiz.isPresent()) {
                    quiz_list.get(i).setStudent(studentOpt.get());
                    quizRepository.save(quiz_list.get(i));
                    for (int j = 0; j < preguntas_list.size() / quiz_list.size(); j++) {
                        preguntas_list.get(pregunta).setQuiz(quiz_list.get(i));
                        preguntasRepository.save(preguntas_list.get(pregunta));
                        if (pregunta < preguntas_list.size()) {
                            pregunta++;
                        }
                    }
                }
            }
        }
        return "redirect:/uploadfiles123";
    }

    @PostMapping("/uploadCourse")
    public String uploadCourseFile(Model model, @RequestParam("file") File file) throws IOException {

        ArrayList<CourseQuiz> course_list = courseService.saveCourse(file);

        for (int i = 0; i < course_list.size(); i++) {
            course_list.get(i).getTask_mark();
            System.out.println(course_list.get(i).getTask_name() + ": " + course_list.get(i).getTask_mark());
            Optional<User> studOpt = studentRepository.findByEmail(course_list.get(i).getStudent().getEmail());
            if (studOpt.isPresent()) {
                course_list.get(i).setStudent(studOpt.get());
                courseQuizRepository.save(course_list.get(i));
            }
        }
        List<CourseQuiz> cour = courseQuizRepository.findByTasknameContains("Tarea");
        System.out.println(cour.size());
        return "redirect:/uploadfiles123";
    }

    @PostMapping("/uploadLogs")
    public String uploadLogs(Model model, @RequestParam("file") File file) throws IOException {

        ArrayList<Log> log_list = logsService.saveLogs(file);

        for (int i = 0; i < log_list.size(); i++) {
            logRepository.save(log_list.get(i));
        }

        return "redirect:/uploadfiles123";
    }

    @PostMapping("/showDataQuiz")
    public String showPreguntas(Principal principal, Model model, @RequestParam("date") String date)
            throws IOException, ParseException {
        DateTimeFormatter dtfInput = DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("es", "ES"));
        LocalDate date1 = LocalDate.parse(date, dtfInput);

        Date date_ = java.sql.Date.valueOf(date1);
        User student = studentRepository.findAllByEmail(principal.getName());
        Optional<Quiz> quiz_opt = quizRepository.findByStudentAndComenzado(student,
                date_);

        List<Quiz> quizOpt = quizRepository.findByStudent(student);

        int quiz_index = quizOpt.indexOf(quiz_opt.get());

        List<Preguntas> preguntas = preguntasRepository.findByQuiz(quizOpt.get(quiz_index));

        Gson gson = new Gson();
        String preguntas_json = gson.toJson(preguntas);
        String quiz_json = gson.toJson(quizOpt);

        model.addAttribute("student", student);
        model.addAttribute("preguntas_json", preguntas_json);
        model.addAttribute("quiz_json", quiz_json);
        model.addAttribute("quiz", quiz_opt.get());
        model.addAttribute("preguntas", preguntas);

        return "quizRep_view";
    }

    @PostMapping("/showStats")
    public String showLogs(Principal principal, Model model) {

        User student = studentRepository.findAllByEmail(principal.getName());

        List<Log> log_list = logRepository.findAllByStudent(student);
        List<Log> log_list_last = logRepository.findAllByStudentAndContextoAndComponenteAndEventnameOrderByDatestring(
                student, "Curso: Aplicaciones Web 21/22-2C", "Sistema", "Curso visto");

        Date lastLog = log_list_last.get(log_list_last.size() - 1).getDatestring();
        logsService.lastOnline(lastLog);

        List<Log> log_archivos_all = logRepository.findAllByContextoContainsAndComponenteAndEventname("Archivo",
                "Recurso", "Módulo de curso visto");
        List<Log> log_archivos_student = logRepository.findAllByStudentAndContextoContainsAndComponenteAndEventname(
                student, "Archivo", "Recurso", "Módulo de curso visto");

        List<Log> need_to_see = logsService.needToSee(log_archivos_student, log_archivos_all);

        for (int i = 0; i < need_to_see.size(); i++) {
            for (int j = 0; j < need_to_see.size(); j++) {
                if (need_to_see.get(i).getContexto().equalsIgnoreCase(need_to_see.get(j).getContexto())) {
                    need_to_see.remove(need_to_see.get(i));
                }
            }
        }

        Gson gson = new Gson();
        String log_json = gson.toJson(log_list);
        model.addAttribute("student", student);
        model.addAttribute("need_to_see", need_to_see);
        model.addAttribute("log_json", log_json);
        model.addAttribute("lastOnline", logsService.lastOnline(lastLog));
        return "log_view";
    }
}

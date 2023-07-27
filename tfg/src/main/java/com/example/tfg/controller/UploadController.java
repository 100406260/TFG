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
import org.springframework.web.bind.annotation.PathVariable;
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
        if (!teacherOpt.isPresent()) {
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
        boolean teacher = false;

        if(principal.getName().equals("teacher@teacher.com")){
            teacher = true;
            List<User> students = studentRepository.findAll();
            students.remove(0);
            model.addAttribute("students", students);
        }

        User student = studentRepository.findAllByEmail(principal.getName());

        List<Log> quiz_done = logRepository.findAllByStudentAndComponenteAndEventname(student, "Cuestionario",
                "Intento de cuestionario visualizado");
        for (int i = 0; i < quiz_done.size(); i++) {
            Optional<Quiz> quizOpt = quizRepository.findByStudentAndComenzadoDateContains(student,
                    quiz_done.get(i).getDate().substring(0, 9));
            if (quizOpt.isPresent()) {
                quizOpt.get().setNombre_quiz(quiz_done.get(i).getContexto());
                quizReportService.update(quizOpt.get());
            }
        }
        List<Quiz> quizes = quizRepository.findByStudent(student);

        model.addAttribute("teacher", teacher);
        model.addAttribute("quizes", quizes);
        model.addAttribute("student", student);

        return "main_view";
    }

    @GetMapping("/uploadfiles123")
    public String teacherPage() {
        return "teacher_view";
    }

    @PostMapping("/uploadStudents")
    public String uploadStudentsFile(Model model, @RequestParam("file") File file) throws IOException {

        ArrayList<User> student_list = quizReportService.saveStudent(file);

        for (int i = 0; i < student_list.size(); i++) {
            Optional<User> studentOpt = studentRepository.findByEmail(student_list.get(i).getEmail());
            if (!studentOpt.isPresent()) { // if student no present
                student_list.get(i).setRole("USER");
                studentRepository.save(student_list.get(i)); // save
            }
        }

        return "redirect:/uploadfiles123";
    }

    @PostMapping("/uploadQuiz")
    public String uploadQuizFile(Model model, @RequestParam("file") File file) throws IOException {

        ArrayList<Quiz> quiz_list = quizReportService.saveQuiz(file);
        ArrayList<Preguntas> preguntas_list = quizReportService.savePregunta(file);
        int pregunta = 0;

        // save the students and quizes
        for (int i = 0; i < quiz_list.size(); i++) {
            Optional<Quiz> noQuiz = quizRepository.findByStudentAndComenzadoDateContains(quiz_list.get(i).getStudent(),
                    quiz_list.get(i).getComenzadoDate());
            if (!noQuiz.isPresent()) {
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
            Optional<Log> logOpt = logRepository.findByStudentAndDatestring(log_list.get(i).getStudent(),
                    log_list.get(i).getDatestring());
            if (!logOpt.isPresent()) {
                logRepository.save(log_list.get(i));
            }
        }

        return "redirect:/uploadfiles123";
    }

    @GetMapping("/showQuiz/{studentId}/{quizId}")
    public String showQuiz(Principal principal, Model model, @PathVariable(name = "quizId") Long quizId) {

        User student = studentRepository.findAllByEmail(principal.getName());
        Optional<Quiz> quizOpt = quizRepository.findById(quizId);

        List<Preguntas> preguntas = preguntasRepository.findByQuiz(quizOpt.get());

        Gson gson = new Gson();
        String preguntas_json = gson.toJson(preguntas);
        String quiz_json = gson.toJson(quizOpt.get());

        Double media_preg = (double) ((quizOpt.get().getTiempo_requerido().doubleValue()) / (preguntas.size()));
        int t = (int) (100 * media_preg);
        int min = t / 100;
        int sec = 60 * (t % 100);

        model.addAttribute("student", student);
        model.addAttribute("preguntas_json", preguntas_json);
        model.addAttribute("quiz_json", quiz_json);
        model.addAttribute("media_preg", media_preg);
        model.addAttribute("min", min);
        model.addAttribute("sec", sec);
        model.addAttribute("quiz", quizOpt.get());
        model.addAttribute("preguntas", preguntas);

        return "quizRep_view";
    }

    @PostMapping("/showStats/{studentId}")
    public String showLogs(Principal principal, Model model) {

        User student = studentRepository.findAllByEmail(principal.getName());
        List<Quiz> quizes = quizRepository.findByStudent(student);

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

        List<Quiz> quizes_sin_revisar = new ArrayList<>();
        for(int i=0; i< quizes.size(); i++){
            Optional<Log> quiz_visto = logRepository.findByStudentAndContextoAndEventname(
                student, quizes.get(i).getNombre_quiz(), "Intento del cuestionario revisado");
            if(!quiz_visto.isPresent()){
                quizes_sin_revisar.add(quizes.get(i));
            }
        }

        for (int i = 0; i < need_to_see.size(); i++) {
            for (int j = 0; j < need_to_see.size(); j++) {
                if (need_to_see.get(i).getContexto().equalsIgnoreCase(need_to_see.get(j).getContexto())) {
                    need_to_see.remove(need_to_see.get(i));
                }
            }
        }

        Gson gson = new Gson();
        String log_json = gson.toJson(log_list);
        String log_visto_json = gson.toJson(log_list_last);
        model.addAttribute("student", student);
        model.addAttribute("need_to_see", need_to_see);
        model.addAttribute("log_json", log_json);
        model.addAttribute("log_visto_json", log_visto_json);
        model.addAttribute("no_vistos", quizes_sin_revisar);
        model.addAttribute("lastOnline", logsService.lastOnline(lastLog));
        // model.addAttribute("quizes", quizes);
        return "log_view";
    }
}

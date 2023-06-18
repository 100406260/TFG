package com.example.tfg.services;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tfg.model.Preguntas;
import com.example.tfg.model.Quiz;
import com.example.tfg.model.User;
import com.example.tfg.model.quizRepository;
import com.example.tfg.model.userRepository;

@Service
public class quizReportService {

    @Autowired
    private quizRepository quizRepository;

    @Autowired
    private userRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Quiz update(Quiz quiz) {
		return quizRepository.save(quiz);
	}

    public ArrayList<User> saveStudent(File file) throws IOException {

        ArrayList<User> student_list = new ArrayList<>();

        try {

            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            System.out.println(file.getAbsolutePath());

            int num_cols = 0;
            int num_rows = 0;
            for (Row rows : sheet) {
                for (Cell cells : rows) {
                    if (cells.getRowIndex() == 1) {
                        num_cols = rows.getLastCellNum();
                    }
                }
                if (rows.getFirstCellNum() >= 0) {
                    num_rows++;
                }
            }

            for (Row row : sheet) {
                User student_aux = new User();
                for (Cell cell : row) {
                    if ((cell.getRowIndex() >= 1 && cell.getRowIndex() <= (num_rows - 1))) {
                        switch (cell.getColumnIndex()) {
                            case 1:
                                String surname_aux = cell.getStringCellValue().toLowerCase();
                                String surname = "";
                                String[] words = surname_aux.split(" ");

                                for (int i = 0; i < words.length; i++) {
                                    surname += words[i].replaceFirst(words[i].charAt(0) + "",
                                            Character.toUpperCase(words[i].charAt(0)) + "") + " ";
                                }
                                surname = surname.substring(0, surname.length()-1);
                                student_aux.setSurnames(surname);
                                break;
                            case 2:
                                String name_aux = cell.getStringCellValue().toLowerCase();
                                String name = "";
                                String[] words_ = name_aux.split(" ");

                                for (int i = 0; i < words_.length; i++) {
                                    name += words_[i].replaceFirst(words_[i].charAt(0) + "",
                                            Character.toUpperCase(words_[i].charAt(0)) + "") + " ";
                                }
                                name = name.substring(0, name.length()-1);
                                student_aux.setName(name);
                                break;
                            case 3:
                                student_aux.setEmail(cell.getStringCellValue());
                                student_aux.setfullName();
                                Random rand = new Random();
                                Long pass = rand.nextLong(1000000);
                                student_aux.setPassword(pass.toString());
                                student_aux.setPassword_en(passwordEncoder.encode(pass.toString()));
                                student_list.add(student_aux);
                                break;
                        }
                    }
                }
            }
            wb.close();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return student_list;
    }

    public ArrayList<Quiz> saveQuiz(File file) throws IOException {

        ArrayList<Quiz> quiz_list = new ArrayList<>();
        Double media = 0.0;

        try {

            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            System.out.println(file.getAbsolutePath());

            int num_cols = 0;
            int num_rows = 0;
            for (Row rows : sheet) {
                for (Cell cells : rows) {
                    if (cells.getRowIndex() == 1) {
                        num_cols = rows.getLastCellNum();
                    }
                }
                if (rows.getFirstCellNum() >= 0) {
                    num_rows++;
                }
            }

            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getRowIndex() == num_rows - 1) {
                        if (cell.getColumnIndex() == 7) {
                            cell.getCachedFormulaResultType();
                            media = cell.getNumericCellValue();
                        }
                    }
                }
            }

            for (Row row : sheet) {
                Quiz quiz_aux = new Quiz();
                Optional<User> student_aux;
                for (Cell cell : row) {
                    if ((cell.getRowIndex() >= 1 && cell.getRowIndex() <= (num_rows - 2))) {
                        switch (cell.getColumnIndex()) {
                            case 2:
                                student_aux = studentRepository.findByEmail(cell.getStringCellValue());
                                if (student_aux.isPresent()) {
                                    quiz_aux.setStudent(student_aux.get());
                                    System.out.println(quiz_aux.getStudent().getEmail());
                                }
                                break;
                            case 3:
                                quiz_aux.setEstado(cell.getStringCellValue());
                                break;
                            case 4:
                                System.out.println("entra case 4");
                                String date = cell.getStringCellValue();
                                DateTimeFormatter dtfInput_ = DateTimeFormatter.ofPattern(
                                        "dd 'de' MMMM 'de' yyy  HH:mm",
                                        new Locale("es", "ES"));
                                LocalDateTime date_aux_ = LocalDateTime.parse(date, dtfInput_);
                                Date date_ = java.sql.Timestamp.valueOf(date_aux_);
                                quiz_aux.setComenzado(date_);
                                quiz_aux.setComenzadoDate(date_);
                                System.out.println(quiz_aux.getComenzado().toString());
                                break;
                            case 5:
                                String date_f = cell.getStringCellValue();
                                System.out.println(date_f);
                                DateTimeFormatter dtfInput_f = DateTimeFormatter.ofPattern(
                                        "dd 'de' MMMM 'de' yyy  HH:mm",
                                        new Locale("es", "ES"));
                                LocalDateTime date_aux_f = LocalDateTime.parse(date_f, dtfInput_f);
                                Date date_f_ = java.sql.Timestamp.valueOf(date_aux_f);
                                quiz_aux.setFinalizado(date_f_);
                                break;
                            case 6:
                                String dur_aux = cell.getStringCellValue();
                                String[] values = dur_aux.split(" ");
                                Duration t_req = Duration.ofMinutes(Long.valueOf(values[0]));
                                if (values.length > 2) {
                                    t_req.plusSeconds(Long.valueOf(values[2]));
                                }
                                quiz_aux.setTiempo_requerido(t_req.toMinutes());
                                break;
                            case 7:
                                cell.getCachedFormulaResultType();
                                quiz_aux.setCalificacion(cell.getNumericCellValue());
                                quiz_aux.setMedia(media);
                                quiz_list.add(quiz_aux);
                                break;
                        }
                    }
                }
            }
            wb.close();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return quiz_list;
    }

    public ArrayList<Preguntas> savePregunta(File file) throws IOException {

        ArrayList<Preguntas> preguntas_list = new ArrayList<>();
        ArrayList<Double> medias_preg = new ArrayList<Double>();

        try {

            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            System.out.println(file.getAbsolutePath());

            int num_cols = 0;
            int num_rows = 0;
            for (Row rows : sheet) {
                for (Cell cells : rows) {
                    if (cells.getRowIndex() == 1) {
                        num_cols = rows.getLastCellNum();
                    }
                }
                if (rows.getFirstCellNum() >= 0) {
                    num_rows++;
                }
            }

            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getRowIndex() == num_rows - 1) {
                        if (cell.getColumnIndex() > 7) {
                            cell.getCachedFormulaResultType();
                            medias_preg.add(cell.getNumericCellValue());
                        }
                    }
                }
            }

            for (Row row : sheet) {
                int num = 1;
                for (Cell cell : row) {
                    Preguntas preguntas_aux = new Preguntas();
                    if ((cell.getRowIndex() >= 1 && cell.getRowIndex() <= (num_rows - 2))) {
                        if (cell.getColumnIndex() > 7) {
                            preguntas_aux.setName("P." + num);
                            preguntas_aux.setNota(cell.getNumericCellValue());
                            preguntas_aux.setMedia_preg(medias_preg.get(cell.getColumnIndex() - 8));
                            preguntas_list.add(preguntas_aux);
                            num++;
                        }
                    }
                }
            }
            wb.close();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return preguntas_list;
    }
}

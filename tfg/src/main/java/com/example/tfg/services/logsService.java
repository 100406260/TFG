package com.example.tfg.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tfg.model.Log;
import com.example.tfg.model.User;
import com.example.tfg.model.userRepository;

@Service
public class logsService {

    @Autowired
    private userRepository studentRepository;

    public ArrayList<Log> saveLogs(File file) throws IOException {

        ArrayList<Log> log_list = new ArrayList<>();

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
                Log log_aux = new Log();
                for (Cell cell : row) {
                    if ((cell.getRowIndex() >= 1  && cell.getRowIndex() <= (num_rows - 2))) {
                        switch (cell.getColumnIndex()) {
                            case 0:
                                String date = cell.getStringCellValue();
                                if(date.length()==15){
                                    date = "0".concat(date);
                                }
                                DateTimeFormatter dtfInput = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm", new Locale("es", "ES"));
                                LocalDateTime date_aux = LocalDateTime.parse(date, dtfInput);
                                Date date_ = java.sql.Timestamp.valueOf(date_aux);
                                log_aux.setDatestring(date_);
                                log_aux.setDate(date_.toString());
                                break;
                            case 1:
                                Optional<User> student_aux = studentRepository.findByFullname("PAULA MADRID ALONSO");
                                if(student_aux.isPresent()){
                                    log_aux.setStudent(student_aux.get());
                                }
                                break;
                            case 3:
                                log_aux.setContexto(cell.getStringCellValue());
                                break;
                            case 4:
                                log_aux.setComponente(cell.getStringCellValue());
                                break;
                            case 5:
                                log_aux.setEventname(cell.getStringCellValue());
                                break;
                            case 6:
                                log_aux.setDescripcion(cell.getStringCellValue());
                                log_list.add(log_aux);
                                break;
                        }

                    }
                }
            }
            wb.close();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return log_list;
    }

    public int lastOnline(Date lastDate){

        Calendar calendar = Calendar.getInstance();

        Long timePassed = calendar.getTime().getTime()- lastDate.getTime();

        int days = (int) (timePassed/ (1000*60*60*24));

        return days;
        
    }

    public List<Log> needToSee(List<Log> log_student, List<Log> log_all){
        List<Log> need = new ArrayList<Log>();

        if(log_all.containsAll(log_student)){
            log_all.removeAll(log_student);
        }

        System.out.println("size all despues de borrar student: "+ log_all.size());


        for(int i=0; i<log_all.size();i++){
            for(int j =0; j<log_student.size();j++){
                if(log_all.get(i).getContexto().equalsIgnoreCase(log_student.get(j).getContexto())){
                    log_all.remove(log_all.get(i));
                }
            }
        }
        need = log_all;
        //System.out.println(need.size());
        return need;
    }
}

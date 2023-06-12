package com.example.tfg.services;

import java.io.File;
import java.io.IOException;

import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import com.example.tfg.model.CourseQuiz;
import com.example.tfg.model.User;

@Service
public class courseService {

    public ArrayList<CourseQuiz> saveCourse(File file) throws IOException {

        ArrayList<CourseQuiz> course_list = new ArrayList<>();

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
            ArrayList<String> names = new ArrayList<String>();
            for (Row row : sheet) {
                User student_aux = new User();
                Double media = 0.0;
                for (Cell cell : row) {
                    if (cell.getRowIndex() == 0 && cell.getColumnIndex() > 2 && cell.getColumnIndex() < num_cols - 2) {
                        names.add(cell.getStringCellValue());
                    }
                    if ((cell.getRowIndex() >= 1 /* && cell.getRowIndex() <= (num_rows - 1) */)) {
                        switch (cell.getColumnIndex()) {
                            case 0:
                                student_aux.setSurnames(cell.getStringCellValue());
                                System.out.println(student_aux.getSurnames());
                                break;
                            case 1:
                                student_aux.setName(cell.getStringCellValue());
                                System.out.println(student_aux.getName());
                                student_aux.setfullName();
                                break;
                            case 2:
                                student_aux.setEmail(cell.getStringCellValue());
                                System.out.println(student_aux.getEmail());
                                break;
                        }
                        if (cell.getColumnIndex() > 2) {
                            CourseQuiz course_aux = new CourseQuiz();
                            if (cell.getColumnIndex() == num_cols - 1) {
                                cell.getCachedFormulaResultType();
                                media = cell.getNumericCellValue();
                            }
                            course_aux.setStudent(student_aux);
                            course_aux.setTask_name(names.get(cell.getColumnIndex() - 3));
                            course_aux.setTask_mark(cell.getNumericCellValue());
                            course_list.add(course_aux);
                        }
                    }

                }
            }
            wb.close();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return course_list;
    }
}

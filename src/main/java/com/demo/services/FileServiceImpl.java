package com.demo.services;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.Gender;
import com.demo.common.utilities.Constant;
import com.demo.common.utilities.RestAPIStatus;
import com.demo.common.utilities.UniqueID;
import com.demo.common.utilities.Validator;
import com.demo.controllers.helper.DateHelper;
import com.demo.controllers.helper.StudentHelper;
import com.demo.controllers.model.response.FileStudentResponse;
import com.demo.controllers.model.response.StudentResponse;
import com.demo.entities.Student;
import com.demo.entities.Subject;
import org.apache.commons.csv.*;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    StudentService studentService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    StudentHelper studentHelper;
    @Autowired
    DateHelper dateHelper;

    @Override
    public ByteArrayInputStream createTemplateStudent() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT);
        csvPrinter.printRecord("Email", "First Name", "Last name", "Dob", "Phone Number", "Gender", "Math", "Literature", "Medium Score");
        csvPrinter.flush();
        return new ByteArrayInputStream(out.toByteArray());

    }

    @Override
    public ByteArrayInputStream readFile(MultipartFile file) throws IOException, ParseException {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
                CSVFormat.DEFAULT.withHeader(Constant.HEADERS_EXCEL_TEMPLATE).withIgnoreHeaderCase().withTrim().withSkipHeaderRecord());
        List<StudentResponse> students = new ArrayList<>();
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        List<String> existsErr = new ArrayList<>();


        for (CSVRecord csvRecord : csvRecords) {
            if (!csvRecord.get("Email").trim().isEmpty() && !csvRecord.get("Phone Number").trim().isEmpty() &&
                    !csvRecord.get("First Name").trim().isEmpty() && !csvRecord.get("Last Name").trim().isEmpty()
                    && !csvRecord.get("Dob").trim().isEmpty()
            ) {
                Validator.validateEmail(csvRecord.get("Email"));
                Validator.validatePhone(csvRecord.get("Phone Number"));
                Validator.validateStringNull(csvRecord.get("First Name"));
                Validator.validateStringNull(csvRecord.get("Last Name"));
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                dateHelper.checkDate(csvRecord.get("Dob"));
                Date dob = formatter.parse(csvRecord.get("Dob"));

                Student studentByEmail = studentService.getByEmail(csvRecord.get("Email"));

                if (studentByEmail != null) {
                    existsErr.add("this Email " + csvRecord.get("Email") + " existed");
                }

                Validator.isGenderEnum(csvRecord.get("Gender"), RestAPIStatus.BAD_REQUEST, "Invalid name of gender");
                Student studentByPhone = studentService.getByPhoneNumber(csvRecord.get("Phone Number"));
                if (studentByPhone != null) {
                    existsErr.add("this Phone " + csvRecord.get("Phone Number") + " existed");
                }

                Validator.validateScore(csvRecord.get("Math"));
                Validator.validateScore(csvRecord.get("Literature"));
                Validator.validateMediumScore(csvRecord.get("Math"), csvRecord.get("Literature"), csvRecord.get("Medium Score"));
                Student student = new Student();
                student.setId(UniqueID.getUUID());
                student.setEmail(csvRecord.get("Email"));
                student.setFirstName(csvRecord.get("First Name"));
                student.setLastName(csvRecord.get("Last Name"));
                student.setBob(dob);
                student.setStatus(AppStatus.ACTIVE);
                student.setPhoneNumber(csvRecord.get("Phone Number"));
                student.setGender(Gender.valueOf(csvRecord.get("Gender")));

                List<Subject> subjects = new ArrayList<>();
                if (csvRecord.get("Math") != null) {
                    Subject subject = new Subject();
                    subject.setId(UniqueID.getUUID());
                    subject.setStudentId(student.getId());
                    subject.setStatus(AppStatus.ACTIVE);
                    subject.setName("Math");
                    subject.setScore(Double.valueOf(csvRecord.get("Math")));
                    subjects.add(subject);
                }
                if (csvRecord.get("Literature") != null) {
                    Subject subject = new Subject();
                    subject.setId(UniqueID.getUUID());
                    subject.setStatus(AppStatus.ACTIVE);
                    subject.setName("Literature");
                    subject.setStudentId(student.getId());
                    subject.setScore(Double.valueOf(csvRecord.get("Literature")));
                    subjects.add(subject);
                }
                studentService.save(student);
                subjectService.saveAll(subjects);
                students.add(new StudentResponse(student, subjects));
            }
        }
        if (!existsErr.isEmpty()) {
            return this.createFileError(existsErr);
        } else {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] array = "Import file successful".getBytes();
            output.write(array);
            output.close();
            return new ByteArrayInputStream(output.toByteArray());

        }
    }

    @Override
    public ByteArrayInputStream exportExcelAllStudent() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT);
        List<Student> students = studentService.findAll();
        List<FileStudentResponse> listStudent = new ArrayList<>();
        csvPrinter.printRecord("Email", "First Name", "Last name", "Dob", "Phone Number", "Gender", "Math", "Literature", "Medium Score");
        List<String> ids = students.stream().map(Student::getId).collect(Collectors.toList());
        List<Subject> subjects = subjectService.findAllByListStudentId(ids);
        students.forEach(e -> {
            listStudent.add(new FileStudentResponse(e, subjects.stream().filter(subject -> subject.getStudentId().equals(e.getId())).collect(Collectors.toList())));
        });

        listStudent.forEach(e -> {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = parser.parse(e.getDob());
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String dobParse = formatter.format(date);
            String scoreMath = "";
            String scoreMedium = "";
            String scoreLiterature = "";
            if (e.getMath() != null) {
                scoreMath = e.getMath().toString();
            }
            if (e.getLiterature() != null) {
                scoreLiterature = e.getLiterature().toString();
            }
            if (e.getMediumScore() != null) {
                scoreMedium = e.getMediumScore().toString();
            }
            List<String> data = Arrays.asList(
                    e.getEmail(),
                    e.getFirstName(),
                    e.getLastName(),
                    dobParse,
                    e.getPhoneNumber(),
                    e.getGender(),
                    scoreMath,
                    scoreLiterature,
                    scoreMedium
            );
            try {
                csvPrinter.printRecord(data);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        csvPrinter.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream createFileError(List<String> errors) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT);
        csvPrinter.printRecord("Error exist");
        if (errors != null) {
            errors.forEach(e -> {

                try {
                    csvPrinter.printRecord(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            });
        }


        csvPrinter.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }
}

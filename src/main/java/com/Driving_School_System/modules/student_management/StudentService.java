package com.Driving_School_System.modules.student_management;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.Driving_School_System.utils.CustomBubbleSort;
import com.Driving_School_System.utils.CustomQueue;

public class StudentService {
    private static final String FILE_PATH = "students.txt";
    private static final String DIRECTORY_PATH = "./";


    public boolean save(StudentModel student) {

        createDirectoryIfNotExists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            if (student.getCreatedAt() == null) {
                student.setCreatedAt(LocalDateTime.now());
            }
            if (student.getId() == null) {
                student.setId(getNextId());
            }

            bw.write(formatStudent(student));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Optional<StudentModel> getById(Long id) {
        List<StudentModel> students = getAll();
        return students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst();
    }


    public List<StudentModel> getAll() {
        List<StudentModel> students = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return students;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                students.add(parseStudent(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return students;
    }
    public boolean update(StudentModel student) {
        if (student.getId() == null) {
            return false;
        }

        List<StudentModel> students = getAll();
        boolean found = false;

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(student.getId())) {
                students.set(i, student);
                found = true;
                break;
            }
        }

        if (found) {
            return saveAll(students);
        }

        return false;
    }


    public boolean delete(Long id) {
        List<StudentModel> students = getAll();
        boolean removed = students.removeIf(student -> student.getId().equals(id));

        if (removed) {
            return saveAll(students);
        }

        return false;
    }


    private boolean saveAll(List<StudentModel> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (StudentModel student : students) {
                bw.write(formatStudent(student));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private String formatStudent(StudentModel student) {
        return String.join("|",
                String.valueOf(student.getId()),
                student.getName(),
                student.getAddress(),
                student.getStudentNumber(),
                student.getContactNumber(),
                student.getEmail(),
                String.valueOf(student.getAge()),
                student.getCreatedAt().toString()
        );
    }


    private StudentModel parseStudent(String line) {
        String[] parts;


        if (line.contains("|")) {
            parts = line.split("\\|");
        } else {

            parts = new String[8]; 


            int commaIndex = line.indexOf(",");
            parts[0] = line.substring(0, commaIndex);


            String[] tempParts = line.substring(commaIndex + 1).split(",");


            parts[1] = tempParts[0];


            parts[2] = tempParts[1] + "," + tempParts[2];

            parts[3] = tempParts[3]; // studentNumber
            parts[4] = tempParts[4]; // contactNumber
            parts[5] = tempParts[5]; // email
            parts[6] = tempParts[6].trim(); // age


            if (tempParts.length > 7) {
                StringBuilder dateTimeBuilder = new StringBuilder();
                for (int i = 7; i < tempParts.length; i++) {
                    if (i > 7) dateTimeBuilder.append(",");
                    dateTimeBuilder.append(tempParts[i]);
                }
                parts[7] = dateTimeBuilder.toString().trim();
            } else {
                parts[7] = LocalDateTime.now().toString();
            }
        }

        StudentModel student = new StudentModel();
        student.setId(Long.parseLong(parts[0]));
        student.setName(parts[1]);
        student.setAddress(parts[2]);
        student.setStudentNumber(parts[3]);
        student.setContactNumber(parts[4]);
        student.setEmail(parts[5]);
        student.setAge(Integer.parseInt(parts[6]));


        if (parts.length > 7 && parts[7] != null && !parts[7].isEmpty()) {
            try {
                student.setCreatedAt(java.time.LocalDateTime.parse(parts[7]));
            } catch (Exception e) {
                // Handle parsing error for createdAt
                student.setCreatedAt(java.time.LocalDateTime.now());
            }
        } else {
            student.setCreatedAt(java.time.LocalDateTime.now());
        }

        return student;
    }


    private Long getNextId() {
        List<StudentModel> students = getAll();

        if (students.isEmpty()) {
            return 1L;
        }

        return students.stream()
                .mapToLong(StudentModel::getId)
                .max()
                .orElse(0) + 1;
    }

    
    private void createDirectoryIfNotExists() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }


    public CustomQueue<StudentModel> getStudentsInQueue() {
        List<StudentModel> students = getAll();
        CustomQueue<StudentModel> studentQueue = new CustomQueue<>();


        for (StudentModel student : students) {
            studentQueue.enqueue(student);
        }

        return studentQueue;
    }


    public List<StudentModel> getStudentsSortedByName() {
        List<StudentModel> students = getAll();


        Comparator<StudentModel> nameComparator = Comparator.comparing(StudentModel::getName);


        CustomBubbleSort.sortList(students, nameComparator);

        return students;
    }


    public List<StudentModel> getStudentsSortedByAge() {
        List<StudentModel> students = getAll();


        Comparator<StudentModel> ageComparator = Comparator.comparing(StudentModel::getAge);


        CustomBubbleSort.sortList(students, ageComparator);

        return students;
    }


    public List<StudentModel> processStudentsFromQueue() {
        CustomQueue<StudentModel> studentQueue = getStudentsInQueue();
        List<StudentModel> processedStudents = new ArrayList<>();


        while (!studentQueue.isEmpty()) {
            StudentModel student = studentQueue.dequeue();

            processedStudents.add(student);
        }

        return processedStudents;
    }
}

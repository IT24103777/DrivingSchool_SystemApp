package com.Driving_School_System.modules.instructor_management;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com .Driving_School_System.utils.CustomBubbleSort;
import com.Driving_School_System.utils.CustomQueue;

public class InstructorService {
    private static final String FILE_PATH = "instructors.txt";
    private static final String DIRECTORY_PATH = "./";

   
    public boolean save(InstructorModel instructor) {
        createDirectoryIfNotExists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (instructor.getCreatedAt() == null) {
                instructor.setCreatedAt(LocalDateTime.now());
            }
            if (instructor.getId() == null) {
                instructor.setId(getNextId());
            }

            bw.write(formatInstructor(instructor));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public Optional<InstructorModel> getById(Long id) {
        List<InstructorModel> instructors = getAll();
        return instructors.stream()
                .filter(instructor -> instructor.getId().equals(id))
                .findFirst();
    }

    
    public List<InstructorModel> getAll() {
        List<InstructorModel> instructors = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return instructors;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                instructors.add(parseInstructor(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instructors;
    }

  
    public boolean update(InstructorModel instructor) {
        if (instructor.getId() == null) {
            return false;
        }

        List<InstructorModel> instructors = getAll();
        boolean found = false;

        for (int i = 0; i < instructors.size(); i++) {
            if (instructors.get(i).getId().equals(instructor.getId())) {
                instructors.set(i, instructor);
                found = true;
                break;
            }
        }

        if (found) {
            return saveAll(instructors);
        }

        return false;
    }

    
    public boolean delete(Long id) {
        List<InstructorModel> instructors = getAll();
        boolean removed = instructors.removeIf(instructor -> instructor.getId().equals(id));

        if (removed) {
            return saveAll(instructors);
        }

        return false;
    }

    
    private boolean saveAll(List<InstructorModel> instructors) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (InstructorModel instructor : instructors) {
                bw.write(formatInstructor(instructor));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    private String formatInstructor(InstructorModel instructor) {
        return String.join("|",
                String.valueOf(instructor.getId()),
                instructor.getName(),
                instructor.getInstructorNumber(),
                instructor.getEmail(),
                instructor.getContactNumber(),
                instructor.getAddress(),
                instructor.getNicNumber(),
                String.valueOf(instructor.getAge()),
                instructor.getCreatedAt().toString()
        );
    }

    
    private InstructorModel parseInstructor(String line) {
        String[] parts = line.split("\\|");
        InstructorModel instructor = new InstructorModel();
        instructor.setId(Long.parseLong(parts[0]));
        instructor.setName(parts[1]);
        instructor.setInstructorNumber(parts[2]);
        instructor.setEmail(parts[3]);
        instructor.setContactNumber(parts[4]);
        instructor.setAddress(parts[5]);
        instructor.setNicNumber(parts[6]);
        instructor.setAge(Integer.parseInt(parts[7]));

        
        if (parts.length > 8 && parts[8] != null && !parts[8].isEmpty()) {
            try {
                instructor.setCreatedAt(java.time.LocalDateTime.parse(parts[8]));
            } catch (Exception e) {
                instructor.setCreatedAt(java.time.LocalDateTime.now());
            }
        } else {
            instructor.setCreatedAt(java.time.LocalDateTime.now());
        }

        return instructor;
    }

   
    private Long getNextId() {
        List<InstructorModel> instructors = getAll();

        if (instructors.isEmpty()) {
            return 1L;
        }

        return instructors.stream()
                .mapToLong(InstructorModel::getId)
                .max()
                .orElse(0) + 1;
    }

    
    private void createDirectoryIfNotExists() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

   
    public CustomQueue<InstructorModel> getInstructorsInQueue() {
        List<InstructorModel> instructors = getAll();
        CustomQueue<InstructorModel> instructorQueue = new CustomQueue<>();

       
        for (InstructorModel instructor : instructors) {
            instructorQueue.enqueue(instructor);
        }

        return instructorQueue;
    }

   
    public List<InstructorModel> getInstructorsSortedByName() {
        List<InstructorModel> instructors = getAll();

        
        Comparator<InstructorModel> nameComparator = Comparator.comparing(InstructorModel::getName);

       
        CustomBubbleSort.sortList(instructors, nameComparator);

        return instructors;
    }

   
    public List<InstructorModel> getInstructorsSortedByAge() {
        List<InstructorModel> instructors = getAll();

        
        Comparator<InstructorModel> ageComparator = Comparator.comparing(InstructorModel::getAge);

        
        CustomBubbleSort.sortList(instructors, ageComparator);

        return instructors;
    }

   
    public List<InstructorModel> processInstructorsFromQueue() {
        CustomQueue<InstructorModel> instructorQueue = getInstructorsInQueue();
        List<InstructorModel> processedInstructors = new ArrayList<>();

        
        while (!instructorQueue.isEmpty()) {
            InstructorModel instructor = instructorQueue.dequeue();
            
            processedInstructors.add(instructor);
        }

        return processedInstructors;
    }
}

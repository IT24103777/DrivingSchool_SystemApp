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

    // Create or update an instructor
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

    // Get an instructor by ID
    public Optional<InstructorModel> getById(Long id) {
        List<InstructorModel> instructors = getAll();
        return instructors.stream()
                .filter(instructor -> instructor.getId().equals(id))
                .findFirst();
    }

    // Get all instructors
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

    // Update an instructor
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

    // Delete an instructor by ID
    public boolean delete(Long id) {
        List<InstructorModel> instructors = getAll();
        boolean removed = instructors.removeIf(instructor -> instructor.getId().equals(id));

        if (removed) {
            return saveAll(instructors);
        }

        return false;
    }

    // Save all instructors (overwrite the file)
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

    // Helper method to format an instructor as a string for file storage
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

    // Helper method to parse an instructor from a string
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

        // Set createdAt if available
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

    // Get the next available ID
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

    // Create directory if it doesn't exist
    private void createDirectoryIfNotExists() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Stores all instructor data in a custom queue and returns it
     * @return CustomQueue containing all instructor records
     */
    public CustomQueue<InstructorModel> getInstructorsInQueue() {
        List<InstructorModel> instructors = getAll();
        CustomQueue<InstructorModel> instructorQueue = new CustomQueue<>();

        // Add all instructors to the queue
        for (InstructorModel instructor : instructors) {
            instructorQueue.enqueue(instructor);
        }

        return instructorQueue;
    }

    /**
     * Sorts the list of instructors by their name using bubble sort
     * @return List of instructors sorted by name
     */
    public List<InstructorModel> getInstructorsSortedByName() {
        List<InstructorModel> instructors = getAll();

        // Create a comparator to sort instructors by name
        Comparator<InstructorModel> nameComparator = Comparator.comparing(InstructorModel::getName);

        // Use the CustomBubbleSort utility to sort the list
        CustomBubbleSort.sortList(instructors, nameComparator);

        return instructors;
    }

    /**
     * Sorts the list of instructors by their age using bubble sort
     * @return List of instructors sorted by age (youngest first)
     */
    public List<InstructorModel> getInstructorsSortedByAge() {
        List<InstructorModel> instructors = getAll();

        // Create a comparator to sort instructors by age
        Comparator<InstructorModel> ageComparator = Comparator.comparing(InstructorModel::getAge);

        // Use the CustomBubbleSort utility to sort the list
        CustomBubbleSort.sortList(instructors, ageComparator);

        return instructors;
    }

    /**
     * Processes instructors in queue order and returns them as a list
     * This demonstrates using the queue for processing
     * @return List of instructors processed from the queue
     */
    public List<InstructorModel> processInstructorsFromQueue() {
        CustomQueue<InstructorModel> instructorQueue = getInstructorsInQueue();
        List<InstructorModel> processedInstructors = new ArrayList<>();

        // Process each instructor from the queue
        while (!instructorQueue.isEmpty()) {
            InstructorModel instructor = instructorQueue.dequeue();
            // In a real application, you might do additional processing here
            processedInstructors.add(instructor);
        }

        return processedInstructors;
    }
}

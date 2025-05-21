package com.Driving_School_System.modules.schedule_management;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.Driving_School_System.utils.CustomBubbleSort;
import com.Driving_School_System.utils.CustomQueue;

public class ScheduleService {
    private static final String FILE_PATH = "schedules.txt";
    private static final String DIRECTORY_PATH = "./";

    public boolean save(ScheduleModel schedule) {
        createDirectoryIfNotExists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (schedule.getCreatedAt() == null) {
                schedule.setCreatedAt(LocalDateTime.now());
            }
            if (schedule.getId() == null) {
                schedule.setId(getNextId());
            }

            bw.write(formatSchedule(schedule));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Optional<ScheduleModel> getById(Long id) {
        List<ScheduleModel> schedules = getAll();
        return schedules.stream()
                .filter(schedule -> schedule.getId().equals(id))
                .findFirst();
    }


    public List<ScheduleModel> getAll() {
        List<ScheduleModel> schedules = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return schedules;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                schedules.add(parseSchedule(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return schedules;
    }


    public boolean update(ScheduleModel schedule) {
        if (schedule.getId() == null) {
            return false;
        }

        List<ScheduleModel> schedules = getAll();
        boolean found = false;

        for (int i = 0; i < schedules.size(); i++) {
            if (schedules.get(i).getId().equals(schedule.getId())) {
                schedules.set(i, schedule);
                found = true;
                break;
            }
        }

        if (found) {
            return saveAll(schedules);
        }

        return false;
    }


    public boolean delete(Long id) {
        List<ScheduleModel> schedules = getAll();
        boolean removed = schedules.removeIf(schedule -> schedule.getId().equals(id));

        if (removed) {
            return saveAll(schedules);
        }

        return false;
    }


    private boolean saveAll(List<ScheduleModel> schedules) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (ScheduleModel schedule : schedules) {
                bw.write(formatSchedule(schedule));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private String formatSchedule(ScheduleModel schedule) {
        return String.join("|",
                String.valueOf(schedule.getId()),
                String.valueOf(schedule.getInstructorId()),
                schedule.getDate(),
                schedule.getTime(),
                schedule.getVehicle(),
                schedule.getCreatedAt().toString()
        );
    }

    private ScheduleModel parseSchedule(String line) {
        String[] parts = line.split("\\|");
        ScheduleModel schedule = new ScheduleModel();
        schedule.setId(Long.parseLong(parts[0]));
        schedule.setInstructorId(Long.parseLong(parts[1]));
        schedule.setDate(parts[2]);
        schedule.setTime(parts[3]);
        schedule.setVehicle(parts[4]);


        if (parts.length > 5 && parts[5] != null && !parts[5].isEmpty()) {
            try {
                schedule.setCreatedAt(java.time.LocalDateTime.parse(parts[5]));
            } catch (Exception e) {
                schedule.setCreatedAt(java.time.LocalDateTime.now());
            }
        } else {
            schedule.setCreatedAt(java.time.LocalDateTime.now());
        }

        return schedule;
    }


    private Long getNextId() {
        List<ScheduleModel> schedules = getAll();

        if (schedules.isEmpty()) {
            return 1L;
        }

        return schedules.stream()
                .mapToLong(ScheduleModel::getId)
                .max()
                .orElse(0) + 1;
    }

    private void createDirectoryIfNotExists() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }


    public CustomQueue<ScheduleModel> getSchedulesInQueue() {
        List<ScheduleModel> schedules = getAll();
        CustomQueue<ScheduleModel> scheduleQueue = new CustomQueue<>();

        for (ScheduleModel schedule : schedules) {
            scheduleQueue.enqueue(schedule);
        }

        return scheduleQueue;
    }


    public List<ScheduleModel> getSortedSchedulesByDate() {
        List<ScheduleModel> schedules = getAll();


        Comparator<ScheduleModel> dateComparator = (s1, s2) -> s1.getDate().compareTo(s2.getDate());


        ScheduleModel[] scheduleArray = schedules.toArray(new ScheduleModel[0]);
        CustomBubbleSort sorter = new CustomBubbleSort();
        sorter.sort(scheduleArray, dateComparator);

        return Arrays.asList(scheduleArray);
    }




}
package com.Driving_School_System.modules.admin_management;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.Driving_School_System.utils.CustomBubbleSort;
import com.Driving_School_System.utils.CustomQueue;

public class AdminService {
    private static final String FILE_PATH = "admins.txt";
    private static final String DIRECTORY_PATH = "./";

    public boolean save(AdminModel admin) {
        createDirectoryIfNotExists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (admin.getCreatedAt() == null) {
                admin.setCreatedAt(LocalDateTime.now());
            }
            if (admin.getId() == null) {
                admin.setId(getNextId());
            }

            bw.write(formatAdmin(admin));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<AdminModel> getById(Long id) {
        List<AdminModel> admins = getAll();
        return admins.stream()
                .filter(admin -> admin.getId().equals(id))
                .findFirst();
    }

    public List<AdminModel> getAll() {
        List<AdminModel> admins = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return admins;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                admins.add(parseAdmin(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return admins;
    }

    public boolean update(AdminModel admin) {
        if (admin.getId() == null) {
            return false;
        }

        List<AdminModel> admins = getAll();
        boolean found = false;

        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getId().equals(admin.getId())) {
                admins.set(i, admin);
                found = true;
                break;
            }
        }

        if (found) {
            return saveAll(admins);
        }

        return false;
    }

    public boolean delete(Long id) {
        List<AdminModel> admins = getAll();
        boolean removed = admins.removeIf(admin -> admin.getId().equals(id));

        if (removed) {
            return saveAll(admins);
        }

        return false;
    }

    private boolean saveAll(List<AdminModel> admins) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (AdminModel admin : admins) {
                bw.write(formatAdmin(admin));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatAdmin(AdminModel admin) {
        return String.join("|",
                String.valueOf(admin.getId()),
                admin.getName(),
                admin.getAdminNumber(),
                admin.getContactNumber(),
                admin.getEmail(),
                admin.getNic(),
                admin.getCreatedAt().toString()
        );
    }

    private AdminModel parseAdmin(String line) {
        String[] parts = line.split("\\|");
        AdminModel admin = new AdminModel();
        admin.setId(Long.parseLong(parts[0]));
        admin.setName(parts[1]);
        admin.setAdminNumber(parts[2]);
        admin.setContactNumber(parts[3]);
        admin.setEmail(parts[4]);
        admin.setNic(parts[5]);

        if (parts.length > 6 && parts[6] != null && !parts[6].isEmpty()) {
            try {
                admin.setCreatedAt(java.time.LocalDateTime.parse(parts[6]));
            } catch (Exception e) {
                admin.setCreatedAt(java.time.LocalDateTime.now());
            }
        } else {
            admin.setCreatedAt(java.time.LocalDateTime.now());
        }

        return admin;
    }

    private Long getNextId() {
        List<AdminModel> admins = getAll();

        if (admins.isEmpty()) {
            return 1L;
        }

        return admins.stream()
                .mapToLong(AdminModel::getId)
                .max()
                .orElse(0) + 1;
    }

    private void createDirectoryIfNotExists() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Stores all admin data in a custom queue and returns it
     * @return CustomQueue containing all admin records
     */
    public CustomQueue<AdminModel> getAdminsInQueue() {
        List<AdminModel> admins = getAll();
        CustomQueue<AdminModel> adminQueue = new CustomQueue<>();


        for (AdminModel admin : admins) {
            adminQueue.enqueue(admin);
        }

        return adminQueue;
    }

    /**
     * Sorts the list of admins by their name using bubble sort
     * @return List of admins sorted by name
     */
    public List<AdminModel> getAdminsSortedByName() {
        List<AdminModel> admins = getAll();


        Comparator<AdminModel> nameComparator = Comparator.comparing(AdminModel::getName);


        CustomBubbleSort.sortList(admins, nameComparator);

        return admins;
    }
}

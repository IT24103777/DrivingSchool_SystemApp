package com.Driving_School_System.modules.vehicle_management;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.Driving_School_System.utils.CustomBubbleSort;
import com.Driving_School_System.utils.CustomQueue;

public class VehicleService {
    private static final String FILE_PATH = "vehicles.txt";
    private static final String DIRECTORY_PATH = "./";

    public boolean save(VehicleModel vehicle) {
        createDirectoryIfNotExists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (vehicle.getCreatedAt() == null) {
                vehicle.setCreatedAt(LocalDateTime.now());
            }
            if (vehicle.getId() == null) {
                vehicle.setId(getNextId());
            }

            bw.write(formatVehicle(vehicle));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<VehicleModel> getById(Long id) {
        List<VehicleModel> vehicles = getAll();
        return vehicles.stream()
                .filter(vehicle -> vehicle.getId().equals(id))
                .findFirst();
    }

    public List<VehicleModel> getAll() {
        List<VehicleModel> vehicles = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return vehicles;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                vehicles.add(parseVehicle(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    public boolean update(VehicleModel vehicle) {
        if (vehicle.getId() == null) {
            return false;
        }

        List<VehicleModel> vehicles = getAll();
        boolean found = false;

        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId().equals(vehicle.getId())) {
                vehicles.set(i, vehicle);
                found = true;
                break;
            }
        }

        if (found) {
            return saveAll(vehicles);
        }

        return false;
    }

    public boolean delete(Long id) {
        List<VehicleModel> vehicles = getAll();
        boolean removed = vehicles.removeIf(vehicle -> vehicle.getId().equals(id));

        if (removed) {
            return saveAll(vehicles);
        }

        return false;
    }

    private boolean saveAll(List<VehicleModel> vehicles) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (VehicleModel vehicle : vehicles) {
                bw.write(formatVehicle(vehicle));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatVehicle(VehicleModel vehicle) {
        return String.join("|",
                String.valueOf(vehicle.getId()),
                vehicle.getNumber(),
                vehicle.getType(),
                vehicle.getStatus(),
                vehicle.getCreatedAt().toString()
        );
    }

    private VehicleModel parseVehicle(String line) {
        String[] parts = line.split("\\|");
        VehicleModel vehicle = new VehicleModel();
        vehicle.setId(Long.parseLong(parts[0]));
        vehicle.setNumber(parts[1]);
        vehicle.setType(parts[2]);
        vehicle.setStatus(parts[3]);

        if (parts.length > 4 && parts[4] != null && !parts[4].isEmpty()) {
            try {
                vehicle.setCreatedAt(java.time.LocalDateTime.parse(parts[4]));
            } catch (Exception e) {
                vehicle.setCreatedAt(java.time.LocalDateTime.now());
            }
        } else {
            vehicle.setCreatedAt(java.time.LocalDateTime.now());
        }

        return vehicle;
    }

    private Long getNextId() {
        List<VehicleModel> vehicles = getAll();

        if (vehicles.isEmpty()) {
            return 1L;
        }

        return vehicles.stream()
                .mapToLong(VehicleModel::getId)
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
     * Stores all vehicle data in a custom queue and returns it
     * @return CustomQueue containing all vehicle records
     */
    public CustomQueue<VehicleModel> getVehiclesInQueue() {
        List<VehicleModel> vehicles = getAll();
        CustomQueue<VehicleModel> vehicleQueue = new CustomQueue<>();

        // Add all vehicles to the queue
        for (VehicleModel vehicle : vehicles) {
            vehicleQueue.enqueue(vehicle);
        }

        return vehicleQueue;
    }

    /**
     * Sorts the list of vehicles by their type using bubble sort
     * @return List of vehicles sorted by type
     */
    public List<VehicleModel> getVehiclesSortedByType() {
        List<VehicleModel> vehicles = getAll();

        // Create a comparator to sort vehicles by type
        Comparator<VehicleModel> typeComparator = Comparator.comparing(VehicleModel::getType);

        // Use the CustomBubbleSort utility to sort the list
        CustomBubbleSort.sortList(vehicles, typeComparator);

        return vehicles;
    }

    /**
     * Sorts the list of vehicles by their status using bubble sort
     * @return List of vehicles sorted by status
     */
    public List<VehicleModel> getVehiclesSortedByStatus() {
        List<VehicleModel> vehicles = getAll();

        // Create a comparator to sort vehicles by status
        Comparator<VehicleModel> statusComparator = Comparator.comparing(VehicleModel::getStatus);

        // Use the CustomBubbleSort utility to sort the list
        CustomBubbleSort.sortList(vehicles, statusComparator);

        return vehicles;
    }

    /**
     * Processes vehicles in queue order and returns them as a list
     * This demonstrates using the queue for processing
     * @return List of vehicles processed from the queue
     */
    public List<VehicleModel> processVehiclesFromQueue() {
        CustomQueue<VehicleModel> vehicleQueue = getVehiclesInQueue();
        List<VehicleModel> processedVehicles = new ArrayList<>();

        // Process each vehicle from the queue
        while (!vehicleQueue.isEmpty()) {
            VehicleModel vehicle = vehicleQueue.dequeue();
            // In a real application, you might do additional processing here
            processedVehicles.add(vehicle);
        }

        return processedVehicles;
    }
}
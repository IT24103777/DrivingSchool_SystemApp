package com.Driving_School_System.modules.feedback_management;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.Driving_School_System.utils.CustomBubbleSort;
import com.Driving_School_System.utils.CustomQueue;

public class FeedbackService {
    private static final String FILE_PATH = "feedbacks.txt";
    private static final String DIRECTORY_PATH = "./";

    public boolean save(FeedbackModel feedback) {
        createDirectoryIfNotExists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (feedback.getCreatedAt() == null) {
                feedback.setCreatedAt(LocalDateTime.now());
            }
            if (feedback.getId() == null) {
                feedback.setId(getNextId());
            }

            bw.write(formatFeedback(feedback));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<FeedbackModel> getById(Long id) {
        List<FeedbackModel> feedbacks = getAll();
        return feedbacks.stream()
                .filter(feedback -> feedback.getId().equals(id))
                .findFirst();
    }

    public List<FeedbackModel> getAll() {
        List<FeedbackModel> feedbacks = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return feedbacks;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                feedbacks.add(parseFeedback(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return feedbacks;
    }

    public boolean update(FeedbackModel feedback) {
        if (feedback.getId() == null) {
            return false;
        }

        List<FeedbackModel> feedbacks = getAll();
        boolean found = false;

        for (int i = 0; i < feedbacks.size(); i++) {
            if (feedbacks.get(i).getId().equals(feedback.getId())) {
                feedbacks.set(i, feedback);
                found = true;
                break;
            }
        }

        if (found) {
            return saveAll(feedbacks);
        }

        return false;
    }

    public boolean delete(Long id) {
        List<FeedbackModel> feedbacks = getAll();
        boolean removed = feedbacks.removeIf(feedback -> feedback.getId().equals(id));

        if (removed) {
            return saveAll(feedbacks);
        }

        return false;
    }

    private boolean saveAll(List<FeedbackModel> feedbacks) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (FeedbackModel feedback : feedbacks) {
                bw.write(formatFeedback(feedback));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatFeedback(FeedbackModel feedback) {
        return String.join("|",
                String.valueOf(feedback.getId()),
                feedback.getFeedback(),
                String.valueOf(feedback.getNumOfStars()),
                feedback.getAdditionalComments(),
                feedback.getRelatedTo(),
                feedback.getCreatedAt().toString()
        );
    }

    private FeedbackModel parseFeedback(String line) {
        String[] parts = line.split("\\|");
        FeedbackModel feedback = new FeedbackModel();
        feedback.setId(Long.parseLong(parts[0]));
        feedback.setFeedback(parts[1]);
        feedback.setNumOfStars(Integer.parseInt(parts[2]));
        feedback.setAdditionalComments(parts[3]);
        feedback.setRelatedTo(parts[4]);

        if (parts.length > 5 && parts[5] != null && !parts[5].isEmpty()) {
            try {
                feedback.setCreatedAt(java.time.LocalDateTime.parse(parts[5]));
            } catch (Exception e) {
                feedback.setCreatedAt(java.time.LocalDateTime.now());
            }
        } else {
            feedback.setCreatedAt(java.time.LocalDateTime.now());
        }

        return feedback;
    }

    private Long getNextId() {
        List<FeedbackModel> feedbacks = getAll();

        if (feedbacks.isEmpty()) {
            return 1L;
        }

        return feedbacks.stream()
                .mapToLong(FeedbackModel::getId)
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
     * Stores all feedback data in a custom queue and returns it
     * @return CustomQueue containing all feedback records
     */
    public CustomQueue<FeedbackModel> getFeedbacksInQueue() {
        List<FeedbackModel> feedbacks = getAll();
        CustomQueue<FeedbackModel> feedbackQueue = new CustomQueue<>();

        // Add all feedbacks to the queue
        for (FeedbackModel feedback : feedbacks) {
            feedbackQueue.enqueue(feedback);
        }

        return feedbackQueue;
    }

    /**
     * Sorts the list of feedbacks by their star rating using bubble sort
     * @return List of feedbacks sorted by star rating (highest first)
     */
    public List<FeedbackModel> getFeedbacksSortedByRating() {
        List<FeedbackModel> feedbacks = getAll();

        // Create a comparator to sort feedbacks by star rating (highest first)
        Comparator<FeedbackModel> ratingComparator = (f1, f2) ->
                Integer.compare(f2.getNumOfStars(), f1.getNumOfStars());

        // Use the CustomBubbleSort utility to sort the list
        CustomBubbleSort.sortList(feedbacks, ratingComparator);

        return feedbacks;
    }

    /**
     * Sorts the list of feedbacks by their creation date using bubble sort
     * @return List of feedbacks sorted by creation date (newest first)
     */
    public List<FeedbackModel> getFeedbacksSortedByDate() {
        List<FeedbackModel> feedbacks = getAll();

        // Create a comparator to sort feedbacks by creation date (newest first)
        Comparator<FeedbackModel> dateComparator = (f1, f2) ->
                f2.getCreatedAt().compareTo(f1.getCreatedAt());

        // Use the CustomBubbleSort utility to sort the list
        CustomBubbleSort.sortList(feedbacks, dateComparator);

        return feedbacks;
    }

    /**
     * Processes feedbacks in queue order and returns them as a list
     * This demonstrates using the queue for processing
     * @return List of feedbacks processed from the queue
     */
    public List<FeedbackModel> processFeedbacksFromQueue() {
        CustomQueue<FeedbackModel> feedbackQueue = getFeedbacksInQueue();
        List<FeedbackModel> processedFeedbacks = new ArrayList<>();

        // Process each feedback from the queue
        while (!feedbackQueue.isEmpty()) {
            FeedbackModel feedback = feedbackQueue.dequeue();
            // In a real application, you might do additional processing here
            processedFeedbacks.add(feedback);
        }

        return processedFeedbacks;
    }
}
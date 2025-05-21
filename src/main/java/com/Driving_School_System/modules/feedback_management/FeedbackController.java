package com.Driving_School_System.modules.feedback_management;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.Driving_School_System.utils.CustomQueue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebServlet("/api/feedbacks/*")
public class FeedbackController extends HttpServlet {
    private final FeedbackService feedbackService = new FeedbackService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();
        PrintWriter out = response.getWriter();

        try {
            // Check for special endpoints
            if (pathInfo != null) {
                // Handle sorted endpoints
                if (pathInfo.equals("/sorted/rating")) {
                    List<FeedbackModel> sortedFeedbacks = feedbackService.getFeedbacksSortedByRating();
                    out.print(objectMapper.writeValueAsString(sortedFeedbacks));
                    return;
                } else if (pathInfo.equals("/sorted/date")) {
                    List<FeedbackModel> sortedFeedbacks = feedbackService.getFeedbacksSortedByDate();
                    out.print(objectMapper.writeValueAsString(sortedFeedbacks));
                    return;
                } else if (pathInfo.equals("/queue")) {
                    // Convert queue to list for serialization
                    CustomQueue<FeedbackModel> queue = feedbackService.getFeedbacksInQueue();
                    out.print(objectMapper.writeValueAsString(queue.toList()));
                    return;
                } else if (pathInfo.equals("/process-queue")) {
                    List<FeedbackModel> processedFeedbacks = feedbackService.processFeedbacksFromQueue();
                    out.print(objectMapper.writeValueAsString(processedFeedbacks));
                    return;
                }

                // Handle ID lookup (existing functionality)
                if (!pathInfo.equals("/")) {
                    String[] pathParts = pathInfo.split("/");
                    if (pathParts.length > 1) {
                        Long id = Long.parseLong(pathParts[1]);
                        Optional<FeedbackModel> feedback = feedbackService.getById(id);

                        if (feedback.isPresent()) {
                            out.print(objectMapper.writeValueAsString(feedback.get()));
                        } else {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\":\"Feedback not found\"}");
                        }
                        return;
                    }
                }
            }

            // Default: return all feedbacks
            List<FeedbackModel> feedbacks = feedbackService.getAll();
            out.print(objectMapper.writeValueAsString(feedbacks));

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid ID format\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            FeedbackModel feedback = objectMapper.readValue(requestBody, FeedbackModel.class);

            boolean success = feedbackService.save(feedback);

            if (success) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                PrintWriter out = response.getWriter();
                out.print(objectMapper.writeValueAsString(feedback));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                PrintWriter out = response.getWriter();
                out.print("{\"error\":\"Failed to create feedback\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && !pathInfo.equals("/")) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length > 1) {
                    Long id = Long.parseLong(pathParts[1]);

                    String requestBody = request.getReader().lines().collect(Collectors.joining());
                    FeedbackModel feedback = objectMapper.readValue(requestBody, FeedbackModel.class);
                    feedback.setId(id);

                    boolean success = feedbackService.update(feedback);

                    if (success) {
                        out.print(objectMapper.writeValueAsString(feedback));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Feedback not found\"}");
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Feedback ID is required\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid ID format\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && !pathInfo.equals("/")) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length > 1) {
                    Long id = Long.parseLong(pathParts[1]);

                    boolean success = feedbackService.delete(id);

                    if (success) {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Feedback not found\"}");
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Feedback ID is required\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid ID format\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}

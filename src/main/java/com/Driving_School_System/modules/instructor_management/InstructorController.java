package com.Driving_School_System.modules.instructor_management;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebServlet("/api/instructors/*")
public class InstructorController extends HttpServlet {
    private final InstructorService instructorService = new InstructorService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // Handle GET requests - Get all instructors or get by ID
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();
        PrintWriter out = response.getWriter();

        try {
            // Check for special sorting or queue operations
            if (pathInfo != null) {
                // Sort instructors by name
                if (pathInfo.equals("/sortByName")) {
                    List<InstructorModel> sortedInstructors = instructorService.getInstructorsSortedByName();
                    out.print(objectMapper.writeValueAsString(sortedInstructors));
                    return;
                }
                // Sort instructors by age
                else if (pathInfo.equals("/sortByAge")) {
                    List<InstructorModel> sortedInstructors = instructorService.getInstructorsSortedByAge();
                    out.print(objectMapper.writeValueAsString(sortedInstructors));
                    return;
                }
                // Process instructors using queue
                else if (pathInfo.equals("/queue")) {
                    List<InstructorModel> queuedInstructors = instructorService.processInstructorsFromQueue();
                    out.print(objectMapper.writeValueAsString(queuedInstructors));
                    return;
                }
                // Get instructor by ID
                else if (!pathInfo.equals("/")) {
                    String[] pathParts = pathInfo.split("/");
                    if (pathParts.length > 1) {
                        Long id = Long.parseLong(pathParts[1]);
                        Optional<InstructorModel> instructor = instructorService.getById(id);

                        if (instructor.isPresent()) {
                            out.print(objectMapper.writeValueAsString(instructor.get()));
                        } else {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\":\"Instructor not found\"}");
                        }
                        return;
                    }
                }
            }

            // Default: Get all instructors
            List<InstructorModel> instructors = instructorService.getAll();
            out.print(objectMapper.writeValueAsString(instructors));

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid ID format\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // Handle POST requests - Create a new instructor
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Read request body
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            InstructorModel instructor = objectMapper.readValue(requestBody, InstructorModel.class);

            // Save the instructor
            boolean success = instructorService.save(instructor);

            if (success) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                PrintWriter out = response.getWriter();
                out.print(objectMapper.writeValueAsString(instructor));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                PrintWriter out = response.getWriter();
                out.print("{\"error\":\"Failed to create instructor\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // Handle PUT requests - Update an existing instructor
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();

        try {
            // Check if ID is provided
            if (pathInfo != null && !pathInfo.equals("/")) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length > 1) {
                    Long id = Long.parseLong(pathParts[1]);

                    // Read request body
                    String requestBody = request.getReader().lines().collect(Collectors.joining());
                    InstructorModel instructor = objectMapper.readValue(requestBody, InstructorModel.class);
                    instructor.setId(id);

                    // Update the instructor
                    boolean success = instructorService.update(instructor);

                    if (success) {
                        out.print(objectMapper.writeValueAsString(instructor));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Instructor not found\"}");
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Instructor ID is required\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid ID format\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // Handle DELETE requests - Delete an instructor
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();

        try {
            // Check if ID is provided
            if (pathInfo != null && !pathInfo.equals("/")) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length > 1) {
                    Long id = Long.parseLong(pathParts[1]);

                    // Delete the instructor
                    boolean success = instructorService.delete(id);

                    if (success) {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Instructor not found\"}");
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Instructor ID is required\"}");
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

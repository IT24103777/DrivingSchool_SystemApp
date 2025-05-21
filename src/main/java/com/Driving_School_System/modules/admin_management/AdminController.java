package com.Driving_School_System.modules.admin_management;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.Driving_School_System.utils.CustomQueue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebServlet("/api/admins/*")
public class AdminController extends HttpServlet {
    private final AdminService adminService = new AdminService();
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
            if (pathInfo != null && !pathInfo.equals("/")) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length > 1) {
                    if (pathParts[1].equals("queue")) {
                        // Endpoint for getting admins in a queue
                        CustomQueue<AdminModel> adminQueue = adminService.getAdminsInQueue();
                        List<AdminModel> adminList = new ArrayList<>();

                        // Convert queue to list for serialization
                        while (!adminQueue.isEmpty()) {
                            adminList.add(adminQueue.dequeue());
                        }

                        out.print(objectMapper.writeValueAsString(adminList));
                        return;
                    } else if (pathParts[1].equals("sorted")) {
                        // Endpoint for getting admins sorted by name
                        List<AdminModel> sortedAdmins = adminService.getAdminsSortedByName();
                        out.print(objectMapper.writeValueAsString(sortedAdmins));
                        return;
                    }

                    // Original get by ID logic
                    Long id = Long.parseLong(pathParts[1]);
                    Optional<AdminModel> admin = adminService.getById(id);

                    if (admin.isPresent()) {
                        out.print(objectMapper.writeValueAsString(admin.get()));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Admin not found\"}");
                    }
                }
            } else {
                List<AdminModel> admins = adminService.getAll();
                out.print(objectMapper.writeValueAsString(admins));
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            AdminModel admin = objectMapper.readValue(requestBody, AdminModel.class);

            boolean success = adminService.save(admin);

            if (success) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                PrintWriter out = response.getWriter();
                out.print(objectMapper.writeValueAsString(admin));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                PrintWriter out = response.getWriter();
                out.print("{\"error\":\"Failed to create admin\"}");
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
                    AdminModel admin = objectMapper.readValue(requestBody, AdminModel.class);
                    admin.setId(id);

                    boolean success = adminService.update(admin);

                    if (success) {
                        out.print(objectMapper.writeValueAsString(admin));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Admin not found\"}");
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Admin ID is required\"}");
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

                    boolean success = adminService.delete(id);

                    if (success) {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Admin not found\"}");
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Admin ID is required\"}");
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
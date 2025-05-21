package com.Driving_School_System.modules.schedule_management;

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

@WebServlet("/api/schedules/*")
public class ScheduleController extends HttpServlet {
    private final ScheduleService scheduleService = new ScheduleService();
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
            if (pathInfo != null) {
                if (pathInfo.equals("/queue")) {
                    CustomQueue<ScheduleModel> scheduleQueue = scheduleService.getSchedulesInQueue();
                    List<ScheduleModel> scheduleList = new ArrayList<>();

                    while (!scheduleQueue.isEmpty()) {
                        scheduleList.add(scheduleQueue.dequeue());
                    }

                    out.print(objectMapper.writeValueAsString(scheduleList));
                    return;
                } else if (pathInfo.equals("/sortByDate")) {
                    List<ScheduleModel> sortedSchedules = scheduleService.getSortedSchedulesByDate();
                    out.print(objectMapper.writeValueAsString(sortedSchedules));
                    return;
                }

                if (!pathInfo.equals("/")) {
                    String[] pathParts = pathInfo.split("/");
                    if (pathParts.length > 1) {
                        Long id = Long.parseLong(pathParts[1]);
                        Optional<ScheduleModel> schedule = scheduleService.getById(id);

                        if (schedule.isPresent()) {
                            out.print(objectMapper.writeValueAsString(schedule.get()));
                        } else {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print("{\"error\":\"Schedule not found\"}");
                        }
                    }
                }
            }
            else {
                List<ScheduleModel> schedules = scheduleService.getAll();
                out.print(objectMapper.writeValueAsString(schedules));
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
            ScheduleModel schedule = objectMapper.readValue(requestBody, ScheduleModel.class);

            boolean success = scheduleService.save(schedule);

            if (success) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                PrintWriter out = response.getWriter();
                out.print(objectMapper.writeValueAsString(schedule));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                PrintWriter out = response.getWriter();
                out.print("{\"error\":\"Failed to create schedule\"}");
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
                    ScheduleModel schedule = objectMapper.readValue(requestBody, ScheduleModel.class);
                    schedule.setId(id);

                    boolean success = scheduleService.update(schedule);

                    if (success) {
                        out.print(objectMapper.writeValueAsString(schedule));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Schedule not found\"}");
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Schedule ID is required\"}");
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


                    boolean success = scheduleService.delete(id);

                    if (success) {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Schedule not found\"}");
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Schedule ID is required\"}");
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

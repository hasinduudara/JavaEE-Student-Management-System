package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/studentRegister")
public class StudentRegisterServlet extends HttpServlet {

    public void fixCROS(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setContentType("application/json");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fixCROS(req, resp);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_registration",
                    "root",
                    "hasindu12345");

            String query = "select * from students";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Map<String, String>> students = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, String> student = new HashMap<>();
                student.put("studentId", resultSet.getString("student_id"));
                student.put("studentName", resultSet.getString("name"));
                student.put("studentAge", resultSet.getString("age"));
                student.put("studentAddress", resultSet.getString("address"));
                student.put("studentPhone", resultSet.getString("phone"));
                student.put("studentCourse", resultSet.getString("course"));
                students.add(student);
            }

            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(students);
            resp.getWriter().write(jsonResponse);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fixCROS(req, resp);

        try {
            String studentId = req.getParameter("studentId");
            String name = req.getParameter("name");
            String age = req.getParameter("age");
            String address = req.getParameter("address");
            String phone = req.getParameter("phone");
            String course = req.getParameter("course");

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_registration",
                    "root",
                    "hasindu12345");

            String query = "INSERT INTO students (student_id, name, age, address, phone, course) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, studentId);
            statement.setString(2, name);
            statement.setString(3, age);
            statement.setString(4, address);
            statement.setString(5, phone);
            statement.setString(6, course);

            int result = statement.executeUpdate();

            if (result > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Student Registered Successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Student Registration Failed");
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fixCROS(req, resp);

        try {
            String studentId = req.getParameter("studentId");
            String name = req.getParameter("name");
            String age = req.getParameter("age");
            String address = req.getParameter("address");
            String phone = req.getParameter("phone");
            String course = req.getParameter("course");

            if (studentId == null || studentId.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Student ID is required");
                return;
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_registration",
                    "root",
                    "hasindu12345");

            String query = "UPDATE students SET name = ?, age = ?, address = ?, phone = ?, course = ? WHERE student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, age);
            statement.setString(3, address);
            statement.setString(4, phone);
            statement.setString(5, course);
            statement.setString(6, studentId);

            int result = statement.executeUpdate();

            if (result > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Student Updated Successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Student Update Failed");
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fixCROS(req, resp);

        try {
            String studentId = req.getParameter("studentId");

            if (studentId == null || studentId.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Student ID is required");
                return;
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_registration",
                    "root",
                    "hasindu12345");

            String query = "DELETE FROM students WHERE student_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, studentId);

            int result = statement.executeUpdate();

            if (result > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Student Deleted Successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Student Delete Failed");
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> getParametersFromRequest(HttpServletRequest req) throws IOException {
        Map<String, String> paramMap = new HashMap<>();

        Map<String, String[]> params = req.getParameterMap();
        if (!params.isEmpty()) {
            for (Map.Entry<String, String[]> entry : params.entrySet()) {
                if (entry.getValue() != null && entry.getValue().length > 0) {
                    paramMap.put(entry.getKey(), entry.getValue()[0]);
                }
            }
            return paramMap;
        }

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        String body = sb.toString();
        if (body != null && !body.isEmpty()) {
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0) {
                    String key = java.net.URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                    String value = java.net.URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                    paramMap.put(key, value);
                }
            }
        }
        return paramMap;
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fixCROS(req, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
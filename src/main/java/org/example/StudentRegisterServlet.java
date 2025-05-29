package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/studentRegister")
public class StudentRegisterServlet extends HttpServlet {

    public void fixCROS(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
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
}

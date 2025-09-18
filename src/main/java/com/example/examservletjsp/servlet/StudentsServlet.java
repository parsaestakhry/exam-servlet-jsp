package com.example.examservletjsp.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.examservletjsp.db.DbUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/students")
public class StudentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        try (Connection conn = DbUtil.getConnection()) {
            switch (action) {
                case "new":
                    req.getRequestDispatcher("/studentForm.jsp").forward(req, resp);
                    break;

                case "edit":
                    long studentCode = Long.parseLong(req.getParameter("student_code"));
                    PreparedStatement ps = conn.prepareStatement(
                            "SELECT * FROM students WHERE student_code=?");
                    ps.setLong(1, studentCode);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        req.setAttribute("studentCode", rs.getLong("student_code"));
                        req.setAttribute("firstName", rs.getString("first_name"));
                        req.setAttribute("lastName", rs.getString("last_name"));
                        req.setAttribute("fieldName", rs.getString("field_name"));
                    }
                    req.getRequestDispatcher("/studentForm.jsp").forward(req, resp);
                    break;

                case "delete":
                    long delCode = Long.parseLong(req.getParameter("student_code"));
                    PreparedStatement delPs = conn.prepareStatement(
                            "DELETE FROM students WHERE student_code=?");
                    delPs.setLong(1, delCode);
                    delPs.executeUpdate();
                    resp.sendRedirect("students");
                    break;

                default: 
                    Statement stmt = conn.createStatement();
                    ResultSet rsAll = stmt.executeQuery("SELECT * FROM students ORDER BY student_code");
                    List<Map<String, Object>> students = new ArrayList<>();
                    while (rsAll.next()) {
                        Map<String, Object> s = new HashMap<>();
                        s.put("studentCode", rsAll.getLong("student_code"));
                        s.put("firstName", rsAll.getString("first_name"));
                        s.put("lastName", rsAll.getString("last_name"));
                        s.put("fieldName", rsAll.getString("field_name"));
                        students.add(s);
                    }
                    req.setAttribute("students", students);
                    req.getRequestDispatcher("/students.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String studentCode = req.getParameter("student_code");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        String fieldName = req.getParameter("field_name");

        try (Connection conn = DbUtil.getConnection()) {
            if (studentCode == null || studentCode.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO students (student_code, first_name, last_name, field_name) VALUES (?, ?, ?, ?)");
                ps.setLong(1, System.currentTimeMillis() % 1000000); 
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, fieldName);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE students SET first_name=?, last_name=?, field_name=? WHERE student_code=?");
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, fieldName);
                ps.setLong(4, Long.parseLong(studentCode));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        resp.sendRedirect("students");
    }
}

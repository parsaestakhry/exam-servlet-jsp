package com.example.examservletjsp.servlet;

import com.example.examservletjsp.db.DbUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/courses")
public class CoursesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        try (Connection conn = DbUtil.getConnection()) {
            switch (action) {
                case "new":
                    req.getRequestDispatcher("/courseForm.jsp").forward(req, resp);
                    break;

                case "edit": {
                    int courseId = Integer.parseInt(req.getParameter("course_id"));
                    PreparedStatement ps = conn.prepareStatement(
                            "SELECT * FROM courses WHERE course_id=?");
                    ps.setInt(1, courseId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        req.setAttribute("courseId", rs.getInt("course_id"));
                        req.setAttribute("courseTitle", rs.getString("course_title"));
                        req.setAttribute("unitNo", rs.getInt("unit_no"));
                    }
                    req.getRequestDispatcher("/courseForm.jsp").forward(req, resp);
                    break;
                }

                case "delete": {
                    int delId = Integer.parseInt(req.getParameter("course_id"));
                    PreparedStatement delPs = conn.prepareStatement(
                            "DELETE FROM courses WHERE course_id=?");
                    delPs.setInt(1, delId);
                    delPs.executeUpdate();
                    resp.sendRedirect("courses");
                    break;
                }

                default: { // list all courses
                    Statement stmt = conn.createStatement();
                    ResultSet rsAll = stmt.executeQuery(
                            "SELECT * FROM courses ORDER BY course_id");
                    List<Map<String, Object>> courses = new ArrayList<>();
                    while (rsAll.next()) {
                        Map<String, Object> c = new HashMap<>();
                        c.put("courseId", rsAll.getInt("course_id"));
                        c.put("courseTitle", rsAll.getString("course_title"));
                        c.put("unitNo", rsAll.getInt("unit_no"));
                        courses.add(c);
                    }
                    req.setAttribute("courses", courses);
                    req.getRequestDispatcher("/courses.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in CoursesServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String courseId = req.getParameter("course_id");
        String title = req.getParameter("course_title");
        String unit = req.getParameter("unit_no");

        try (Connection conn = DbUtil.getConnection()) {
            if (courseId == null || courseId.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO courses (course_title, unit_no) VALUES (?, ?)");
                ps.setString(1, title);
                ps.setInt(2, (unit != null && !unit.isEmpty()) ? Integer.parseInt(unit) : 0);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE courses SET course_title=?, unit_no=? WHERE course_id=?");
                ps.setString(1, title);
                ps.setInt(2, (unit != null && !unit.isEmpty()) ? Integer.parseInt(unit) : 0);
                ps.setInt(3, Integer.parseInt(courseId));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in CoursesServlet", e);
        }

        resp.sendRedirect("courses");
    }
}

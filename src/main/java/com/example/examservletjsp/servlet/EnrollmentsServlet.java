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

@WebServlet("/enrollments")
public class EnrollmentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        try (Connection conn = DbUtil.getConnection()) {
            switch (action) {
                case "new":
                    req.setAttribute("students", getStudents(conn));
                    req.setAttribute("courses", getCourses(conn));
                    req.getRequestDispatcher("/enrollmentForm.jsp").forward(req, resp);
                    break;

                case "edit": {
                    long studentCode = Long.parseLong(req.getParameter("student_code"));
                    int courseId = Integer.parseInt(req.getParameter("course_id"));

                    req.setAttribute("studentCode", studentCode);
                    req.setAttribute("courseId", courseId);
                    req.setAttribute("students", getStudents(conn));
                    req.setAttribute("courses", getCourses(conn));

                    req.getRequestDispatcher("/enrollmentForm.jsp").forward(req, resp);
                    break;
                }

                case "delete": {
                    long studentCode = Long.parseLong(req.getParameter("student_code"));
                    int courseId = Integer.parseInt(req.getParameter("course_id"));

                    PreparedStatement delPs = conn.prepareStatement(
                            "DELETE FROM enrollments WHERE student_code=? AND course_id=?");
                    delPs.setLong(1, studentCode);
                    delPs.setInt(2, courseId);
                    delPs.executeUpdate();
                    resp.sendRedirect("enrollments");
                    break;
                }

                default: {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(
                            "SELECT e.student_code, e.course_id, " +
                                    "s.first_name, s.last_name, c.course_title " +
                                    "FROM enrollments e " +
                                    "JOIN students s ON e.student_code = s.student_code " +
                                    "JOIN courses c ON e.course_id = c.course_id " +
                                    "ORDER BY e.student_code, e.course_id");

                    List<Map<String, Object>> enrollments = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> e = new HashMap<>();
                        e.put("studentCode", rs.getLong("student_code"));
                        e.put("courseId", rs.getInt("course_id"));
                        e.put("studentName", rs.getString("first_name") + " " + rs.getString("last_name"));
                        e.put("courseTitle", rs.getString("course_title"));
                        enrollments.add(e);
                    }

                    req.setAttribute("enrollments", enrollments);
                    req.getRequestDispatcher("/enrollments.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in EnrollmentsServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String studentCode = req.getParameter("student_code");
        String courseId = req.getParameter("course_id");
        String originalStudent = req.getParameter("original_student");
        String originalCourse = req.getParameter("original_course");

        try (Connection conn = DbUtil.getConnection()) {
            if (originalStudent == null || originalCourse == null) {
                
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO enrollments (student_code, course_id) VALUES (?, ?)");
                ps.setLong(1, Long.parseLong(studentCode));
                ps.setInt(2, Integer.parseInt(courseId));
                ps.executeUpdate();
            } else {
                PreparedStatement del = conn.prepareStatement(
                        "DELETE FROM enrollments WHERE student_code=? AND course_id=?");
                del.setLong(1, Long.parseLong(originalStudent));
                del.setInt(2, Integer.parseInt(originalCourse));
                del.executeUpdate();

                PreparedStatement ins = conn.prepareStatement(
                        "INSERT INTO enrollments (student_code, course_id) VALUES (?, ?)");
                ins.setLong(1, Long.parseLong(studentCode));
                ins.setInt(2, Integer.parseInt(courseId));
                ins.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in EnrollmentsServlet", e);
        }

        resp.sendRedirect("enrollments");
    }

    private List<Map<String, Object>> getStudents(Connection conn) throws SQLException {
        List<Map<String, Object>> students = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT student_code, first_name, last_name FROM students");
        while (rs.next()) {
            Map<String, Object> s = new HashMap<>();
            s.put("studentCode", rs.getLong("student_code"));
            s.put("name", rs.getString("first_name") + " " + rs.getString("last_name"));
            students.add(s);
        }
        return students;
    }

    private List<Map<String, Object>> getCourses(Connection conn) throws SQLException {
        List<Map<String, Object>> courses = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT course_id, course_title FROM courses");
        while (rs.next()) {
            Map<String, Object> c = new HashMap<>();
            c.put("courseId", rs.getInt("course_id"));
            c.put("title", rs.getString("course_title"));
            courses.add(c);
        }
        return courses;
    }
}

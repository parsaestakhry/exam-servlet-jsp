package com.example.examservletjsp.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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

@WebServlet("/exams")
public class ExamsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        try (Connection conn = DbUtil.getConnection()) {
            switch (action) {
                case "new":
                    req.setAttribute("courses", getCourses(conn));
                    req.getRequestDispatcher("/examForm.jsp").forward(req, resp);
                    break;

                case "edit": {
                    int examId = Integer.parseInt(req.getParameter("exam_id"));
                    PreparedStatement ps = conn.prepareStatement(
                            "SELECT * FROM exams WHERE exam_id=?");
                    ps.setInt(1, examId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        req.setAttribute("examId", rs.getInt("exam_id"));
                        req.setAttribute("courseId", rs.getInt("course_id"));
                        req.setAttribute("examTitle", rs.getString("exam_title"));
                        req.setAttribute("examDate", rs.getDate("exam_date"));
                        req.setAttribute("percentile", rs.getBigDecimal("percentile"));
                    }
                    req.setAttribute("courses", getCourses(conn));
                    req.getRequestDispatcher("/examForm.jsp").forward(req, resp);
                    break;
                }

                case "delete": {
                    int delId = Integer.parseInt(req.getParameter("exam_id"));
                    PreparedStatement delPs = conn.prepareStatement(
                            "DELETE FROM exams WHERE exam_id=?");
                    delPs.setInt(1, delId);
                    delPs.executeUpdate();
                    resp.sendRedirect("exams");
                    break;
                }

                default: { 
                    Statement stmt = conn.createStatement();

                    ResultSet rs = stmt.executeQuery(
                            "SELECT e.exam_id, e.exam_title, e.exam_date, e.percentile, " +
                                    "e.course_id, c.course_title " +
                                    "FROM exams e " +
                                    "JOIN courses c ON e.course_id = c.course_id " +
                                    "ORDER BY e.exam_id");

                    List<Map<String, Object>> exams = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> e = new HashMap<>();
                        e.put("examId", rs.getInt("exam_id"));
                        e.put("examDate", rs.getDate("exam_date"));
                        e.put("examTitle", rs.getString("exam_title"));
                        e.put("percentile", rs.getBigDecimal("percentile"));
                        e.put("courseId", rs.getInt("course_id"));        
                        e.put("courseTitle", rs.getString("course_title")); 
                        exams.add(e);
                    }



                    req.setAttribute("exams", exams);
                    req.getRequestDispatcher("/exams.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in ExamsServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String examId = req.getParameter("exam_id");
        String courseId = req.getParameter("course_id");
        String title = req.getParameter("exam_title");
        String date = req.getParameter("exam_date");
        String percentile = req.getParameter("percentile");

        try (Connection conn = DbUtil.getConnection()) {
            if (examId == null || examId.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO exams (course_id, exam_title, exam_date, percentile) VALUES (?, ?, ?, ?)");
                ps.setInt(1, Integer.parseInt(courseId));
                ps.setString(2, title);
                ps.setDate(3, (date != null && !date.isEmpty()) ? Date.valueOf(date) : null);
                if (percentile != null && !percentile.isEmpty()) {
                    ps.setBigDecimal(4, new java.math.BigDecimal(percentile));
                } else {
                    ps.setNull(4, Types.DECIMAL);
                }
                ps.executeUpdate();
            } else {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE exams SET course_id=?, exam_title=?, exam_date=?, percentile=? WHERE exam_id=?");
                ps.setInt(1, Integer.parseInt(courseId));
                ps.setString(2, title);
                ps.setDate(3, (date != null && !date.isEmpty()) ? Date.valueOf(date) : null);
                if (percentile != null && !percentile.isEmpty()) {
                    ps.setBigDecimal(4, new java.math.BigDecimal(percentile));
                } else {
                    ps.setNull(4, Types.DECIMAL);
                }
                ps.setInt(5, Integer.parseInt(examId));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in ExamsServlet", e);
        }

        resp.sendRedirect("exams");
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

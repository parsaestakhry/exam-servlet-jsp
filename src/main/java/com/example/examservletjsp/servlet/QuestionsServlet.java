package com.example.examservletjsp.servlet;

import com.example.examservletjsp.db.DbUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/questions")
public class QuestionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        try (Connection conn = DbUtil.getConnection()) {
            switch (action) {
                case "new":
                    req.setAttribute("exams", getExams(conn));
                    req.getRequestDispatcher("/questionForm.jsp").forward(req, resp);
                    break;

                case "edit": {
                    int questionId = Integer.parseInt(req.getParameter("question_id"));
                    PreparedStatement ps = conn.prepareStatement(
                            "SELECT * FROM questions WHERE question_id=?");
                    ps.setInt(1, questionId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        req.setAttribute("questionId", rs.getInt("question_id"));
                        req.setAttribute("examId", rs.getInt("exam_id"));
                        req.setAttribute("questionNo", rs.getInt("question_no"));
                        req.setAttribute("questionDescription", rs.getString("question_description"));
                        req.setAttribute("score", rs.getInt("score"));
                    }
                    req.setAttribute("exams", getExams(conn));
                    req.getRequestDispatcher("/questionForm.jsp").forward(req, resp);
                    break;
                }

                case "delete": {
                    int delId = Integer.parseInt(req.getParameter("question_id"));
                    PreparedStatement delPs = conn.prepareStatement(
                            "DELETE FROM questions WHERE question_id=?");
                    delPs.setInt(1, delId);
                    delPs.executeUpdate();
                    resp.sendRedirect("questions");
                    break;
                }

                default: { // list
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(
                            "SELECT q.question_id, q.question_no, q.question_description, q.score, " +
                                    "e.exam_title " +
                                    "FROM questions q " +
                                    "JOIN exams e ON q.exam_id = e.exam_id " +
                                    "ORDER BY q.exam_id, q.question_no");

                    List<Map<String, Object>> questions = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> q = new HashMap<>();
                        q.put("questionId", rs.getInt("question_id"));
                        q.put("questionNo", rs.getInt("question_no"));
                        q.put("questionDescription", rs.getString("question_description"));
                        q.put("score", rs.getInt("score"));
                        q.put("examTitle", rs.getString("exam_title"));
                        questions.add(q);
                    }

                    req.setAttribute("questions", questions);
                    req.getRequestDispatcher("/questions.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in QuestionsServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String questionId = req.getParameter("question_id");
        String examId = req.getParameter("exam_id");
        String questionNo = req.getParameter("question_no");
        String description = req.getParameter("question_description");
        String score = req.getParameter("score");

        try (Connection conn = DbUtil.getConnection()) {
            if (questionId == null || questionId.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO questions (exam_id, question_no, question_description, score) VALUES (?, ?, ?, ?)");
                ps.setInt(1, Integer.parseInt(examId));
                ps.setInt(2, Integer.parseInt(questionNo));
                ps.setString(3, description);
                ps.setInt(4, (score != null && !score.isEmpty()) ? Integer.parseInt(score) : 1);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE questions SET exam_id=?, question_no=?, question_description=?, score=? WHERE question_id=?");
                ps.setInt(1, Integer.parseInt(examId));
                ps.setInt(2, Integer.parseInt(questionNo));
                ps.setString(3, description);
                ps.setInt(4, (score != null && !score.isEmpty()) ? Integer.parseInt(score) : 1);
                ps.setInt(5, Integer.parseInt(questionId));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in QuestionsServlet", e);
        }

        resp.sendRedirect("questions");
    }

    // Utility: fetch exams for dropdown
    private List<Map<String, Object>> getExams(Connection conn) throws SQLException {
        List<Map<String, Object>> exams = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT exam_id, exam_title FROM exams");
        while (rs.next()) {
            Map<String, Object> e = new HashMap<>();
            e.put("examId", rs.getInt("exam_id"));
            e.put("title", rs.getString("exam_title"));
            exams.add(e);
        }
        return exams;
    }
}

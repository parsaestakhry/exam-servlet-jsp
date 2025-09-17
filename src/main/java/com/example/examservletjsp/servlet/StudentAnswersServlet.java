package com.example.examservletjsp.servlet;

import java.io.IOException;
import java.sql.Connection;
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

@WebServlet(urlPatterns = {"/studentAnswers", "/studentanswers"})
public class StudentAnswersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        try (Connection conn = DbUtil.getConnection()) {
            switch (action) {
                case "new":
                    req.setAttribute("studentExams", getStudentExams(conn));
                    req.setAttribute("questions", getQuestions(conn));
                    req.setAttribute("options", getOptions(conn));
                    req.getRequestDispatcher("/studentAnswerForm.jsp").forward(req, resp);
                    break;

                case "edit": {
                    int answerId = Integer.parseInt(req.getParameter("student_answer_id"));
                    PreparedStatement ps = conn.prepareStatement(
                            "SELECT * FROM student_answers WHERE student_answer_id=?");
                    ps.setInt(1, answerId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        req.setAttribute("studentAnswerId", rs.getInt("student_answer_id"));
                        req.setAttribute("studentExamId", rs.getInt("student_exam_id"));
                        req.setAttribute("questionId", rs.getInt("question_id"));
                        req.setAttribute("chosenOptionId", rs.getInt("chosenoptionid"));
                        req.setAttribute("scoreGiven", rs.getBigDecimal("scoregiven"));
                    }
                    req.setAttribute("studentExams", getStudentExams(conn));
                    req.setAttribute("questions", getQuestions(conn));
                    req.setAttribute("options", getOptions(conn));
                    req.getRequestDispatcher("/studentAnswerForm.jsp").forward(req, resp);
                    break;
                }

                case "delete": {
                    int delId = Integer.parseInt(req.getParameter("student_answer_id"));
                    PreparedStatement delPs = conn.prepareStatement(
                            "DELETE FROM student_answers WHERE student_answer_id=?");
                    delPs.setInt(1, delId);
                    delPs.executeUpdate();
                    resp.sendRedirect("studentAnswers");
                    break;
                }

                default: { // list
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(
                            "SELECT sa.student_answer_id, sa.score_given, " +
                                    "se.student_exam_id, sa.question_id, sa.chosen_option_id " +
                                    "FROM student_answers sa " +
                                    "JOIN questions q ON sa.question_id = q.question_id " +
                                    "LEFT JOIN options o ON sa.chosen_option_id = o.option_id " +
                                    "JOIN student_exams se ON sa.student_exam_id = se.student_exam_id " +
                                    "ORDER BY sa.student_exam_id, sa.student_answer_id");

                    List<Map<String, Object>> answers = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> a = new HashMap<>();
                        a.put("studentAnswerId", rs.getInt("student_answer_id"));
                        a.put("studentExamId", rs.getInt("student_exam_id"));
                        a.put("scoreGiven", rs.getBigDecimal("score_given"));
                        a.put("questionId", rs.getInt("question_id"));
                        a.put("chosenOptionId", rs.getInt("chosen_option_id"));
                        answers.add(a);
                    }

                    req.setAttribute("studentAnswers", answers);
                    req.getRequestDispatcher("/studentanswers.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in StudentAnswersServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String studentAnswerId = req.getParameter("student_answer_id");
        String studentExamId = req.getParameter("student_exam_id");
        String questionId = req.getParameter("question_id");
        String chosenOptionId = req.getParameter("chosen_option_id");
        String scoreGiven = req.getParameter("score_given");

        try (Connection conn = DbUtil.getConnection()) {
            if (studentAnswerId == null || studentAnswerId.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO student_answers (student_exam_id, question_id, chosenoptionid, scoregiven) VALUES (?, ?, ?, ?)");
                ps.setInt(1, Integer.parseInt(studentExamId));
                ps.setInt(2, Integer.parseInt(questionId));
                if (chosenOptionId != null && !chosenOptionId.isEmpty()) {
                    ps.setInt(3, Integer.parseInt(chosenOptionId));
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                ps.setBigDecimal(4, (scoreGiven != null && !scoreGiven.isEmpty())
                        ? new java.math.BigDecimal(scoreGiven) : new java.math.BigDecimal("0.00"));
                ps.executeUpdate();
            } else {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE student_answers SET student_exam_id=?, question_id=?, chosenoptionid=?, scoregiven=? WHERE student_answer_id=?");
                ps.setInt(1, Integer.parseInt(studentExamId));
                ps.setInt(2, Integer.parseInt(questionId));
                if (chosenOptionId != null && !chosenOptionId.isEmpty()) {
                    ps.setInt(3, Integer.parseInt(chosenOptionId));
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                ps.setBigDecimal(4, (scoreGiven != null && !scoreGiven.isEmpty())
                        ? new java.math.BigDecimal(scoreGiven) : new java.math.BigDecimal("0.00"));
                ps.setInt(5, Integer.parseInt(studentAnswerId));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in StudentAnswersServlet", e);
        }

        resp.sendRedirect("studentAnswers");
    }

    // Utility: fetch student exams
    private List<Map<String, Object>> getStudentExams(Connection conn) throws SQLException {
        List<Map<String, Object>> exams = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT student_exam_id FROM studentexams");
        while (rs.next()) {
            Map<String, Object> e = new HashMap<>();
            e.put("studentExamId", rs.getInt("student_exam_id"));
            exams.add(e);
        }
        return exams;
    }

    // Utility: fetch questions
    private List<Map<String, Object>> getQuestions(Connection conn) throws SQLException {
        List<Map<String, Object>> questions = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT question_id, question_description FROM questions");
        while (rs.next()) {
            Map<String, Object> q = new HashMap<>();
            q.put("questionId", rs.getInt("question_id"));
            q.put("description", rs.getString("question_description"));
            questions.add(q);
        }
        return questions;
    }

    // Utility: fetch options
    private List<Map<String, Object>> getOptions(Connection conn) throws SQLException {
        List<Map<String, Object>> options = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT option_id, option_title FROM options");
        while (rs.next()) {
            Map<String, Object> o = new HashMap<>();
            o.put("optionId", rs.getInt("option_id"));
            o.put("title", rs.getString("option_title"));
            options.add(o);
        }
        return options;
    }
}

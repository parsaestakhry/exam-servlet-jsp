package com.example.examservletjsp.servlet;

import com.example.examservletjsp.db.DbUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/options")
public class OptionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        try (Connection conn = DbUtil.getConnection()) {
            switch (action) {
                case "new":
                    req.setAttribute("questions", getQuestions(conn));
                    req.getRequestDispatcher("/optionForm.jsp").forward(req, resp);
                    break;

                case "edit": {
                    int optionId = Integer.parseInt(req.getParameter("option_id"));
                    PreparedStatement ps = conn.prepareStatement(
                            "SELECT * FROM options WHERE option_id=?");
                    ps.setInt(1, optionId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        req.setAttribute("optionId", rs.getInt("option_id"));
                        req.setAttribute("questionId", rs.getInt("question_id"));
                        req.setAttribute("optionNo", rs.getInt("option_no"));
                        req.setAttribute("optionTitle", rs.getString("option_title"));
                        req.setAttribute("isCorrect", rs.getBoolean("is_correct"));
                    }
                    req.setAttribute("questions", getQuestions(conn));
                    req.getRequestDispatcher("/optionForm.jsp").forward(req, resp);
                    break;
                }

                case "delete": {
                    int delId = Integer.parseInt(req.getParameter("option_id"));
                    PreparedStatement delPs = conn.prepareStatement(
                            "DELETE FROM options WHERE option_id=?");
                    delPs.setInt(1, delId);
                    delPs.executeUpdate();
                    resp.sendRedirect("options");
                    break;
                }

                default: { // list
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(
                            "SELECT o.option_id, o.option_no, o.option_title, o.is_correct, " +
                                    "q.question_description " +
                                    "FROM options o " +
                                    "JOIN questions q ON o.question_id = q.question_id " +
                                    "ORDER BY o.question_id, o.option_no");

                    List<Map<String, Object>> options = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> opt = new HashMap<>();
                        opt.put("optionId", rs.getInt("option_id"));
                        opt.put("optionNo", rs.getInt("option_no"));
                        opt.put("optionTitle", rs.getString("option_title"));
                        opt.put("isCorrect", rs.getBoolean("is_correct"));
                        opt.put("questionDescription", rs.getString("question_description"));
                        options.add(opt);
                    }

                    req.setAttribute("options", options);
                    req.getRequestDispatcher("/options.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in OptionsServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String optionId = req.getParameter("option_id");
        String questionId = req.getParameter("question_id");
        String optionNo = req.getParameter("option_no");
        String optionTitle = req.getParameter("option_title");
        boolean isCorrect = req.getParameter("is_correct") != null;

        try (Connection conn = DbUtil.getConnection()) {
            if (optionId == null || optionId.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO options (question_id, option_no, option_title, is_correct) VALUES (?, ?, ?, ?)");
                ps.setInt(1, Integer.parseInt(questionId));
                ps.setInt(2, Integer.parseInt(optionNo));
                ps.setString(3, optionTitle);
                ps.setBoolean(4, isCorrect);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE options SET question_id=?, option_no=?, option_title=?, is_correct=? WHERE option_id=?");
                ps.setInt(1, Integer.parseInt(questionId));
                ps.setInt(2, Integer.parseInt(optionNo));
                ps.setString(3, optionTitle);
                ps.setBoolean(4, isCorrect);
                ps.setInt(5, Integer.parseInt(optionId));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in OptionsServlet", e);
        }

        resp.sendRedirect("options");
    }

    // Utility: fetch list of questions for dropdown
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
}

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

@WebServlet(urlPatterns = {"/studentExams" })
public class StudentExamsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = req.getParameter("action");
		if (action == null) action = "list";

		try (Connection conn = DbUtil.getConnection()) {
			switch (action) {
				case "new":
					req.getRequestDispatcher("/studentExamForm.jsp").forward(req, resp);
					break;

				case "edit": {
					int studentExamId = Integer.parseInt(req.getParameter("student_exam_id"));
					PreparedStatement ps = conn.prepareStatement(
							"SELECT * FROM student_exams WHERE student_exam_id=?");
					ps.setInt(1, studentExamId);
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						req.setAttribute("studentExamId", rs.getInt("student_exam_id"));
						req.setAttribute("studentCode", rs.getLong("student_code"));
						req.setAttribute("examId", rs.getInt("exam_id"));
						req.setAttribute("finalResult", rs.getString("final_result"));
					}
					req.getRequestDispatcher("/studentExamForm.jsp").forward(req, resp);
					break;
				}

				case "delete": {
					int delId = Integer.parseInt(req.getParameter("student_exam_id"));
					PreparedStatement delPs = conn.prepareStatement(
							"DELETE FROM student_exams WHERE student_exam_id=?");
					delPs.setInt(1, delId);
					delPs.executeUpdate();
					resp.sendRedirect("studentexams");
					break;
				}

				default: { // list
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(
							"SELECT se.student_exam_id, se.student_code, se.exam_id, se.final_result " +
							"FROM student_exams se ORDER BY se.student_exam_id");

					List<Map<String, Object>> studentExams = new ArrayList<>();
					while (rs.next()) {
						Map<String, Object> se = new HashMap<>();
						se.put("studentExamId", rs.getInt("student_exam_id"));
						se.put("studentCode", rs.getLong("student_code"));
						se.put("examId", rs.getInt("exam_id"));
						se.put("finalResult", rs.getString("final_result"));
						studentExams.add(se);
					}

					req.setAttribute("studentExams", studentExams);
					req.getRequestDispatcher("/studentexams.jsp").forward(req, resp);
				}
			}
		} catch (SQLException e) {
			throw new ServletException("Database error in StudentExamsServlet", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String studentExamId = req.getParameter("student_exam_id");
		String studentCode = req.getParameter("student_code");
		String examId = req.getParameter("exam_id");
		String finalResult = req.getParameter("final_result");

		try (Connection conn = DbUtil.getConnection()) {
			if (studentExamId == null || studentExamId.isEmpty()) {
				PreparedStatement ps = conn.prepareStatement(
						"INSERT INTO student_exams (student_code, exam_id, final_result) VALUES (?, ?, ?)");
				ps.setLong(1, Long.parseLong(studentCode));
				ps.setInt(2, Integer.parseInt(examId));
				ps.setString(3, finalResult);
				ps.executeUpdate();
			} else {
				PreparedStatement ps = conn.prepareStatement(
						"UPDATE student_exams SET student_code=?, exam_id=?, final_result=? WHERE student_exam_id=?");
				ps.setLong(1, Long.parseLong(studentCode));
				ps.setInt(2, Integer.parseInt(examId));
				ps.setString(3, finalResult);
				ps.setInt(4, Integer.parseInt(studentExamId));
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw new ServletException("Database error in StudentExamsServlet", e);
		}

		resp.sendRedirect("studentexams");
	}
}



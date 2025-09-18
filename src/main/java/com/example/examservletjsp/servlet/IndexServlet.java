package com.example.examservletjsp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "IndexServlet", urlPatterns = {"/", "/index"})
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>ExamDB - Home</title></head><body>");
            out.println("<h1>Exam Management System</h1>");
            out.println("<ul>");
            out.println("<li><a href='students'>Manage Students</a></li>");
            out.println("<li><a href='courses'>Manage Courses</a></li>");
            out.println("<li><a href='enrollments'>Manage Enrollments</a></li>");
            out.println("<li><a href='exams'>Manage Exams</a></li>");
            out.println("<li><a href='questions'>Manage Questions</a></li>");
            out.println("<li><a href='options'>Manage Options</a></li>");
            out.println("<li><a href='studentexams'>Manage Student Exams</a></li>");
            out.println("<li><a href='studentAnswers'>Manage Student Answers</a></li>");
            out.println("</ul>");
            out.println("</body></html>");
        }
    }
}

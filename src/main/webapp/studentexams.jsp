<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Student Exams</title>
</head>
<body>
<h1>Student Exams</h1>
<a href="studentexams?action=new">Add New Student Exam</a><br><br>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Student ID</th>
        <th>Exam ID </th>
        <th>Final Result</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="se" items="${studentExams}">
        <tr>
            <td>${se.studentExamId}</td>
            <td>${se.studentCode}
            <td>${se.examId} </td>
            <td>${se.finalResult}</td>
            <td>
                <a href="studentexams?action=edit&student_exam_id=${se.studentExamId}">Edit</a> |
                <a href="studentexams?action=delete&student_exam_id=${se.studentExamId}"
                   onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

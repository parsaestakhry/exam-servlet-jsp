<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Exams</title></head>
<body>
<h1>Exams</h1>
<a href="exams?action=new">Add New Exam</a><br><br>
<table border="1">
    <tr><th>ID</th><th>Course ID</th><th>Title</th><th>Date</th><th>Percentile</th><th>Actions</th></tr>
    <c:forEach var="exam" items="${exams}">
        <tr>
            <td>${exam.examId}</td>
            <td>${exam.courseId}</td>
            <td>${exam.examTitle}</td>
            <td>${exam.examDate}</td>
            <td>${exam.percentile}</td>
            <td>
                <a href="exams?action=edit&exam_id=${exam.examId}">Edit</a> |
                <a href="exams?action=delete&exam_id=${exam.examId}" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Questions</title></head>
<body>
<h1>Questions</h1>
<a href="questions?action=new">Add New Question</a><br><br>
<table border="1">
    <tr><th>ID</th><th>Exam ID</th><th>No</th><th>Description</th><th>Score</th><th>Actions</th></tr>
    <c:forEach var="q" items="${questions}">
        <tr>
            <td>${q.questionId}</td>
            <td>${q.examId}</td>
            <td>${q.questionNo}</td>
            <td>${q.questionDescription}</td>
            <td>${q.score}</td>
            <td>
                <a href="questions?action=edit&question_id=${q.questionId}">Edit</a> |
                <a href="questions?action=delete&question_id=${q.questionId}" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

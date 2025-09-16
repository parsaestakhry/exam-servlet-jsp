<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Student Answers</title></head>
<body>
<h1>Student Answers</h1>
<a href="studentanswers?action=new">Add New</a><br><br>
<table border="1">
    <tr><th>ID</th><th>Student Exam ID</th><th>Question ID</th><th>Chosen Option ID</th><th>Score</th><th>Actions</th></tr>
    <c:forEach var="sa" items="${studentAnswers}">
        <tr>
            <td>${sa.studentAnswerId}</td>
            <td>${sa.studentExamId}</td>
            <td>${sa.questionId}</td>
            <td>${sa.chosenOptionId}</td>
            <td>${sa.scoreGiven}</td>
            <td>
                <a href="studentanswers?action=edit&student_answer_id=${sa.studentAnswerId}">Edit</a> |
                <a href="studentanswers?action=delete&student_answer_id=${sa.studentAnswerId}" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

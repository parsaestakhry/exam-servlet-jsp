<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Options</title></head>
<body>
<h1>Options</h1>
<a href="options?action=new">Add New Option</a><br><br>
<table border="1">
    <tr><th>ID</th><th>Question ID</th><th>Question Description</th><th>No</th><th>Title</th><th>Correct</th><th>Actions</th></tr>
    <c:forEach var="opt" items="${options}">
        <tr>
            <td>${opt.optionId}</td>
            <td>${opt.questionId}</td>
            <td>${opt.questionDescription}</td>
            <td>${opt.optionNo}</td>
            <td>${opt.optionTitle}</td>
            <td>${opt.isCorrect}</td>
            <td>
                <a href="options?action=edit&option_id=${opt.optionId}">Edit</a> |
                <a href="options?action=delete&option_id=${opt.optionId}" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

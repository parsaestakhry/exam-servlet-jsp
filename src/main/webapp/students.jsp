<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Students List</title>
</head>
<body>
<h1>Students</h1>
<a href="students?action=new">Add New Student</a><br><br>
<table border="1">
    <tr>
        <th>Code</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Field</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="student" items="${students}">
        <tr>
            <td>${student.studentCode}</td>
            <td>${student.firstName}</td>
            <td>${student.lastName}</td>
            <td>${student.fieldName}</td>
            <td>
                <a href="students?action=edit&student_code=${student.studentCode}">Edit</a> |
                <a href="students?action=delete&student_code=${student.studentCode}" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

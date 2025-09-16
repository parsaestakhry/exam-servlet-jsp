<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Courses</title></head>
<body>
<h1>Courses</h1>
<a href="courses?action=new">Add New Course</a><br><br>
<table border="1">
    <tr><th>ID</th><th>Title</th><th>Unit No</th><th>Actions</th></tr>
    <c:forEach var="course" items="${courses}">
        <tr>
            <td>${course.courseId}</td>
            <td>${course.courseTitle}</td>
            <td>${course.unitNo}</td>
            <td>
                <a href="courses?action=edit&course_id=${course.courseId}">Edit</a> |
                <a href="courses?action=delete&course_id=${course.courseId}" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

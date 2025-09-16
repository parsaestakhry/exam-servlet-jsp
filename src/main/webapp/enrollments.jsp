<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Enrollments</title></head>
<body>
<h1>Enrollments</h1>
<a href="enrollments?action=new">Add New Enrollment</a><br><br>
<table border="1">
    <tr><th>Student Code</th><th>Course ID</th><th>Actions</th></tr>
    <c:forEach var="enrollment" items="${enrollments}">
        <tr>
            <td>${enrollment.studentCode}</td>
            <td>${enrollment.courseId}</td>
            <td>
                <a href="enrollments?action=delete&student_code=${enrollment.studentCode}&course_id=${enrollment.courseId}" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

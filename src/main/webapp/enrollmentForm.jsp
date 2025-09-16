<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Enrollment Form</title></head>
<body>
<h1>Enrollment Form</h1>
<form method="post" action="enrollments">
    Student Code: <input type="number" name="student_code" value="${studentCode}" required/><br/>
    Course ID: <input type="number" name="course_id" value="${courseId}" required/><br/>
    <input type="submit" value="Save"/>
</form>
<a href="enrollments">Cancel</a>
</body>
</html>

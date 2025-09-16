<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Course Form</title></head>
<body>
<h1>Course Form</h1>
<form method="post" action="courses">
    <input type="hidden" name="course_id" value="${courseId}" />
    Title: <input type="text" name="course_title" value="${courseTitle}" required/><br/>
    Unit No: <input type="number" name="unit_no" value="${unitNo}" /><br/>
    <input type="submit" value="Save"/>
</form>
<a href="courses">Cancel</a>
</body>
</html>

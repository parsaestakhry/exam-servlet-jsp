<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Exam Form</title></head>
<body>
<h1>Exam Form</h1>
<form method="post" action="exams">
    <input type="hidden" name="exam_id" value="${examId}" />
    Course ID: <input type="number" name="course_id" value="${courseId}" required/><br/>
    Title: <input type="text" name="exam_title" value="${examTitle}" required/><br/>
    Date: <input type="date" name="exam_date" value="${examDate}"/><br/>
    Percentile: <input type="number" step="0.01" name="percentile" value="${percentile}"/><br/>
    <input type="submit" value="Save"/>
</form>
<a href="exams">Cancel</a>
</body>
</html>

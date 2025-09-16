<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Student Exam Form</title></head>
<body>
<h1>Student Exam Form</h1>
<form method="post" action="studentexams">
    <input type="hidden" name="student_exam_id" value="${studentExamId}" />
    Student Code: <input type="number" name="student_code" value="${studentCode}" required/><br/>
    Exam ID: <input type="number" name="exam_id" value="${examId}" required/><br/>
    Result: <input type="text" name="final_result" value="${finalResult}"/><br/>
    <input type="submit" value="Save"/>
</form>
<a href="studentexams">Cancel</a>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Question Form</title></head>
<body>
<h1>Question Form</h1>
<form method="post" action="questions">
    <input type="hidden" name="question_id" value="${questionId}" />
    Exam ID: <input type="number" name="exam_id" value="${examId}" required/><br/>
    Question No: <input type="number" name="question_no" value="${questionNo}" required/><br/>
    Description: <input type="text" name="question_description" value="${questionDescription}" required/><br/>
    Score: <input type="number" name="score" value="${score}" required/><br/>
    <input type="submit" value="Save"/>
</form>
<a href="questions">Cancel</a>
</body>
</html>

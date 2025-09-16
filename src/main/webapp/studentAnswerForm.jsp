<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Student Answer Form</title></head>
<body>
<h1>Student Answer Form</h1>
<form method="post" action="studentanswers">
    <input type="hidden" name="student_answer_id" value="${studentAnswerId}" />
    Student Exam ID: <input type="number" name="student_exam_id" value="${studentExamId}" required/><br/>
    Question ID: <input type="number" name="question_id" value="${questionId}" required/><br/>
    Chosen Option ID: <input type="number" name="chosen_option_id" value="${chosenOptionId}"/><br/>
    Score: <input type="number" step="0.01" name="score_given" value="${scoreGiven}"/><br/>
    <input type="submit" value="Save"/>
</form>
<a href="studentanswers">Cancel</a>
</body>
</html>

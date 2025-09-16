<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Option Form</title></head>
<body>
<h1>Option Form</h1>
<form method="post" action="options">
    <input type="hidden" name="option_id" value="${optionId}" />
    Question ID: <input type="number" name="question_id" value="${questionId}" required/><br/>
    Option No: <input type="number" name="option_no" value="${optionNo}" required/><br/>
    Title: <input type="text" name="option_title" value="${optionTitle}" required/><br/>
    Correct: <input type="checkbox" name="is_correct" ${isCorrect == 'true' ? 'checked' : ''}/><br/>
    <input type="submit" value="Save"/>
</form>
<a href="options">Cancel</a>
</body>
</html>

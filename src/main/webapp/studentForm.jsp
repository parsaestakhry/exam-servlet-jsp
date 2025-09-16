<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Student Form</title>
</head>
<body>
<h1>Student Form</h1>
<form method="post" action="students">
    <input type="hidden" name="student_code" value="${studentCode}" />
    First Name: <input type="text" name="first_name" value="${firstName}" required/><br/>
    Last Name: <input type="text" name="last_name" value="${lastName}" required/><br/>
    Field Name: <input type="text" name="field_name" value="${fieldName}"/><br/>
    <input type="submit" value="Save"/>
</form>
<a href="students">Cancel</a>
</body>
</html>

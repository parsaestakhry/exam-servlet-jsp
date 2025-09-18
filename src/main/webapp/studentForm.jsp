<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Student Form</title>
</head>
<body>
<h1>
    <c:choose>
        <c:when test="${not empty studentCode}">Edit Student</c:when>
        <c:otherwise>Add New Student</c:otherwise>
    </c:choose>
</h1>

<form action="students" method="post">
    <!-- Hidden input for edit mode -->
    <c:if test="${not empty studentCode}">
        <input type="hidden" name="student_code" value="${studentCode}" />
    </c:if>

    <table>
        <tr>
            <td>First Name:</td>
            <td><input type="text" name="first_name" value="${firstName}" required /></td>
        </tr>
        <tr>
            <td>Last Name:</td>
            <td><input type="text" name="last_name" value="${lastName}" required /></td>
        </tr>
        <tr>
            <td>Field Name:</td>
            <td><input type="text" name="field_name" value="${fieldName}" required /></td>
        </tr>
    </table>

    <br>
    <input type="submit" value="Save" />
    <a href="students">Cancel</a>
</form>

</body>
</html>

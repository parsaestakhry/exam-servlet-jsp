<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Student Exam Form</title>
</head>
<body>
<h1>${studentExamId == null ? "Add New" : "Edit"} Student Exam</h1>

<form action="studentExams" method="post">
    <input type="hidden" name="student_exam_id" value="${studentExamId}" />

    <label>Student:</label>
    <select name="student_code" required>
        <c:forEach var="s" items="${students}">
            <option value="${s.studentCode}"
                    <c:if test="${s.studentCode == studentCode}">selected</c:if>>
                    ${s.studentCode} - ${s.firstName} ${s.lastName}
            </option>
        </c:forEach>
    </select>
    <br><br>

    <label>Exam:</label>
    <select name="exam_id" required>
        <c:forEach var="e" items="${exams}">
            <option value="${e.examId}"
                    <c:if test="${e.examId == examId}">selected</c:if>>
                    ${e.examId} - ${e.examTitle}
            </option>
        </c:forEach>
    </select>
    <br><br>

    <label>Final Result:</label>
    <input type="text" name="final_result" value="${finalResult}" />
    <br><br>

    <button type="submit">Save</button>
    <a href="studentExams">Cancel</a>
</form>
</body>
</html>

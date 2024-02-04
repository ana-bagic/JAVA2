<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Polls</title>
</head>
<body>
  <h1>Pick a poll:</h1>
  <ol>
  <c:forEach items="${POLLS}" var="poll">
    <li><a href="glasanje?pollID=${poll.getId()}">${poll.getTitle()}</a></li>
  </c:forEach>
  </ol>
</body>
</html>
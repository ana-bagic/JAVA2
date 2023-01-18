<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Voting</title>
</head>
<body>
  <h1>${POLL.getTitle()}</h1>
  <p>${POLL.getMessage()} Click on the link to vote!</p>
  <ol>
  <c:forEach items="${OPTIONS}" var="option">
    <li><a href="glasanje-glasaj?id=${option.getId()}">${option.getTitle()}</a></li>
  </c:forEach>
  </ol>
</body>
</html>
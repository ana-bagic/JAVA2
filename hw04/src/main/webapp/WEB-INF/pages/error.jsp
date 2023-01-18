<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Error</title>
</head>
<body style="background-color:#<%= session.getAttribute("pickedBgCol") %>">
  <h1>Error!</h1>
  <p><c:out value="${MSG}"/></p>
  <p><a href="<c:url value="index.jsp"/>">Return to home page.</a></p>
</body>
</html>
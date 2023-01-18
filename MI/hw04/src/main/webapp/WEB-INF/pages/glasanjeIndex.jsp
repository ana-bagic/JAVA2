<%@page import="java.util.List"%>
<%@page import="hr.fer.oprpp2.hw04.Band"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Voting</title>
</head>
<body style="background-color:#<%= session.getAttribute("pickedBgCol") %>">
  <h1>Vote for your favourite band:</h1>
  <p>Which band, from the ones that are given, is your favourite? Click on the link to vote!</p>
  <ol>
  <c:forEach items="${BANDS}" var="band">
    <li><a href="glasanje-glasaj?id=${band.getId()}">${band.getName()}</a></li>
  </c:forEach>
  </ol>
</body>
</html>
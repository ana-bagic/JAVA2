<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Color chooser</title>
</head>
<body style="background-color:#<%= session.getAttribute("pickedBgCol") %>">
  <p>Pick one of the colors to set the background:</p>
  <p><a href="<c:url value="/setColor/FFFFFF"/>">WHITE</a></p>
  <p><a href="<c:url value="/setColor/ff6060"/>">RED</a></p>
  <p><a href="<c:url value="/setColor/62bd5f"/>">GREEN</a></p>
  <p><a href="<c:url value="/setColor/8cfff4"/>">CYAN</a></p>
</body>
</html>
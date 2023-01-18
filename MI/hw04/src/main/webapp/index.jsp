<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Home</title>
</head>
<body style="background-color:#<%= session.getAttribute("pickedBgCol") %>">
  <p><a href="<c:url value="colors"/>">Background color chooser</a></p>
  
  <p><a href="<c:url value="trigonometric?a=0&b=90"/>">Trigonometric values 0-90 degrees</a></p>
  <form action="trigonometric" method="GET">
	 Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
	 Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
    <input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
  </form>
  
  <p><a href="<c:url value="stories/funny.jsp"/>">Funny story</a></p>
  
  <p><a href="<c:url value="powers?a=1&b=100&n=3"/>">Excel table of powers of numbers</a></p>
  
  <p><a href="<c:url value="appInfo"/>">Application info</a></p>
  
  <p><a href="<c:url value="glasanje"/>">Vote for your favourite band</a></p>
</body>
</html>
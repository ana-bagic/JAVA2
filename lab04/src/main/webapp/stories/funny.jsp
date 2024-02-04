<%@ page import="hr.fer.oprpp2.hw04.util.ColorsUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Funny story</title>
</head>
<body style="background-color:#<%=session.getAttribute("pickedBgCol")%>">
  <h2 style="color:#<%=ColorsUtil.getRandColor()%>">A programmer was walking out of door for work
  and his wife said "while you're out, buy some milk". He never came back home.</h2>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Application info</title>
</head>
<body style="background-color:#<%= session.getAttribute("pickedBgCol") %>">
  <p>This application is running for: ${TIME}</p>
</body>
</html>
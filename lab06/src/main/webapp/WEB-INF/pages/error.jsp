<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Error</title>
</head>
<body>
	<h1>Error!</h1>
	<p>
		<c:out value="${MSG}" />
	</p>
	<p>
		<a href="<c:url value="/"/>">Return to home page.</a>
	</p>
</body>
</html>
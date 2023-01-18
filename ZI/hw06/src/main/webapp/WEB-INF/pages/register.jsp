<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Register</title>
<style type="text/css">
.error {
	font-family: fantasy;
	font-weight: bold;
	font-size: 0.9em;
	color: #FF0000;
	padding-left: 110px;
}
</style>
</head>
<body>
	<h1>Register here:</h1>
	<form action="register" method="post">
		<div>
			<label for="firstName">First name:</label> <input type="text"
				id="firstName" name="firstName" value="${USER.getFirstName()}">
			<c:if test="${USER.hasError('firstName')}">
				<div class="error">
					<c:out value="${USER.getError('firstName')}" />
				</div>
			</c:if>
		</div>

		<div>
			<label for="lastName">Last name:</label> <input type="text"
				id="lastName" name="lastName" value="${USER.getLastName()}">
			<c:if test="${USER.hasError('lastName')}">
				<div class="error">
					<c:out value="${USER.getError('lastName')}" />
				</div>
			</c:if>
		</div>

		<div>
			<label for="email">E-mail:</label> <input type="text" id="email"
				name="email" value="${USER.getEmail()}">
			<c:if test="${USER.hasError('email')}">
				<div class="error">
					<c:out value="${USER.getError('email')}" />
				</div>
			</c:if>
		</div>

		<div>
			<label for="nick">Nickname:</label> <input type="text" id="nick"
				name="nick" value="${USER.getNick()}">
			<c:if test="${USER.hasError('nick')}">
				<div class="error">
					<c:out value="${USER.getError('nick')}" />
				</div>
			</c:if>
		</div>

		<div>
			<label for="password">Password:</label> <input type="password"
				id="password" name="password" value="${USER.password}">
			<c:if test="${USER.hasError('password')}">
				<div class="error">
					<c:out value="${USER.getError('password')}" />
				</div>
			</c:if>
		</div>

		<input type="submit" value="Submit">
	</form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<style type="text/css">
.error {
	font-weight: bold;
	font-size: 0.9em;
	color: #FF0000;
	padding-left: 20px;
}
</style>
</head>
<body>
	<c:choose>
		<c:when test="<%=session.getAttribute(\"current.user.id\") != null%>">
			<div>
				<h2>
					Welcome
					<%=session.getAttribute("current.user.firstName")%>
					<%=session.getAttribute("current.user.lastName")%>
					<a href="<c:url value="/servleti/logout"></c:url>">Logout</a>
				</h2>
			</div>
		</c:when>
		<c:otherwise>
			<div>
				<h1>Login here:</h1>
				<form action="main" method="post">
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
							id="password" name="password">
						<c:if test="${USER.hasError('password')}">
							<div class="error">
								<c:out value="${USER.getError('password')}" />
							</div>
						</c:if>
					</div>
					<c:if test="${ERROR != null}">
						<div class="error">
							<c:out value="${ERROR}" />
						</div>
					</c:if>
					<input type="submit" value="Submit">
				</form>
				<div>
					<a href="register">Register</a>
				</div>
			</div>
		</c:otherwise>
	</c:choose>

	<div>
		<ol>
			<c:forEach items="${AUTHORS}" var="author">
				<li><a href="author/${author.getNick()}">${author.getNick()}</a></li>
			</c:forEach>
		</ol>
	</div>

</body>
</html>
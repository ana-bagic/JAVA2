<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>
	<c:choose>
		<c:when test="${NEW}">Create new entry</c:when>
		<c:otherwise>Edit entry</c:otherwise>
	</c:choose>
</title>
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
				<h2>Not logged in</h2>
			</div>
		</c:otherwise>
	</c:choose>
	
	<c:choose>
		<c:when test="<%= request.getAttribute(\"NICK\").equals(session.getAttribute(\"current.user.nick\")) %>">
			<c:choose>
				<c:when test="${NEW}">Create new entry:</c:when>
				<c:otherwise>Edit entry:</c:otherwise>
			</c:choose>
			<form action="<c:choose>
				<c:when test="${NEW}"><c:url value="/servleti/author/${NICK}/new"></c:url></c:when>
				<c:otherwise><c:url value="/servleti/author/${NICK}/edit/${ENTRY.id}"></c:url></c:otherwise>
				</c:choose>" method="post">
				<div>
					<label for="title">Title:</label> <input type="text"
						id="title" name="title" value="${ENTRY.title}">
					<c:if test="${ENTRY.hasError('title')}">
						<div class="error">
							<c:out value="${ENTRY.getError('title')}" />
						</div>
					</c:if>
				</div>
		
				<div>
					<label for="text">Text:</label>
						<textarea id="text" name="text" rows="4" cols="50">${ENTRY.text}</textarea>
					<c:if test="${ENTRY.hasError('text')}">
						<div class="error">
							<c:out value="${ENTRY.getError('text')}" />
						</div>
					</c:if>
				</div>
				<input type="submit" value="Submit">
			</form>
		</c:when>
		<c:otherwise>
			<div>You are not logged in as user ${NICK}</div>
		</c:otherwise>
	</c:choose>
</body>
</html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Entry</title>
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

	<h1>${ENTRY.title}</h1>
	<p>${ENTRY.text}</p>

	<c:if
		test="<%=request.getAttribute(\"NICK\").equals(session.getAttribute(\"current.user.nick\"))%>">
		<div>
			<a
				href="<c:url value="/servleti/author/${NICK}/edit/${ENTRY.id}"></c:url>">Edit
				entry</a>
		</div>
	</c:if>

	<c:if test="${!ENTRY.comments.isEmpty()}">
		<ul>
			<c:forEach var="comment" items="${ENTRY.comments}">
				<li><div style="font-weight: bold">
						[USER =
						<c:out value="${comment.usersEMail}" />
						]
						<c:out value="${comment.postedOn}" />
					</div>
					<div style="padding-left: 10px;">
						<c:out value="${comment.message}" />
					</div></li>
			</c:forEach>
		</ul>
	</c:if>

	<h3>Add new comment:</h3>
	<form action="<c:url value="/servleti/author/${NICK}/${ENTRY.id}"></c:url>" method="post">
		<div>
			<label for="usersEMail">Your email:</label> <input type="text" id="usersEMail"
				name="usersEMail" value="${COMMENT.usersEMail}">
			<c:if test="${COMMENT.hasError('usersEMail')}">
				<div class="error">
					<c:out value="${COMMENT.getError('usersEMail')}" />
				</div>
			</c:if>
		</div>

		<div>
			<label for="message">Message:</label>
			<textarea id="message" name="message" rows="4" cols="50">${COMMENT.message}</textarea>
			<c:if test="${COMMENT.hasError('message')}">
				<div class="error">
					<c:out value="${COMMENT.getError('message')}" />
				</div>
			</c:if>
		</div>
		<input type="submit" value="Submit">
	</form>
</body>
</html>

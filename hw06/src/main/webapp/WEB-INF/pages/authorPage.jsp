<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
  <title>Author</title>
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
		<c:when test="${ENTRIES.isEmpty()}">
			<div>No entries for this user.</div>
    	</c:when>
    	
		<c:otherwise>
			<ol>
				<c:forEach items="${ENTRIES}" var="entry">
					<li><a href="${NICK}/${entry.id}">${entry.title}</a></li>
				</c:forEach>
			</ol>
		</c:otherwise>
	</c:choose>
	<c:if test="<%= request.getAttribute(\"NICK\").equals(session.getAttribute(\"current.user.nick\")) %>">
		<div><a href="${NICK}/new">Create new entry</a></div>
	</c:if>
</body>
</html>
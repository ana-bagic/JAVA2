<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Results</title>
  <style type="text/css">
 	table.rez td {text-align: center;}
  </style>
</head>
<body>
  <h1>Results of the voting:</h1>
  <p>These are the results of voting!</p>
  <table border="1" class="rez">
  <thead>
    <tr>
      <th>Option</th>
      <th>Number of votes</th>
    </tr>
  </thead>
  <tbody>
  	<c:forEach items="${OPTIONS}" var="option">
      <tr>
        <td>${option.getTitle()}</td>
        <td>${option.getVotesCount()}</td>
      </tr>
    </c:forEach>
  </tbody>
  </table>

  <h2>Graphic display of results</h2>
  <img alt="Pie-chart" src="glasanje-grafika?pollID=${POLLID}">

  <h2>Results in XLS format</h2>
  <p>Results in XLS format are available <a href="glasanje-xls?pollID=${POLLID}">here.</a></p>

  <h2>Other</h2>
  <p>Examples of the songs of the winning option(s):</p>
  <ul>
    <c:forEach items="${WINOPTIONS}" var="option">
      <li>
        <a href="${option.getLink()}" target="_blank">${option.getTitle()}</a>
      </li>
    </c:forEach>
  </ul>

</body>
</html>
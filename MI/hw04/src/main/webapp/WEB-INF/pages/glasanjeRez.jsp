<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Results</title>
  <style type="text/css">
 	table.rez td {text-align: center;}
  </style>
</head>
<body style="background-color:#<%= session.getAttribute("pickedBgCol") %>">
  <h1>Results of the voting:</h1>
  <p>These are the results of voting!</p>
  <table border="1" class="rez">
  <thead>
    <tr>
      <th>Band</th>
      <th>Number of votes</th>
    </tr>
  </thead>
  <tbody>
  	<c:forEach items="${BANDS}" var="band">
      <tr>
        <td>${band.getName()}</td>
        <td>${band.getVotes()}</td>
      </tr>
    </c:forEach>
  </tbody>
  </table>

  <h2>Graphic display of results</h2>
  <img alt="Pie-chart" src="glasanje-grafika">
  
  <h2>Results in XLS format</h2>
  <p>Results in XLS format are available <a href="glasanje-xls">here.</a></p>
  
  <h2>Other</h2>
  <p>Examples of the songs of the winning band(s):</p>
  <ul>
    <c:forEach items="${WINBANDS}" var="band">
      <li>
        <a href="${band.getSongLink()}" target="_blank">${band.getName()}</a>
      </li>
    </c:forEach>
  </ul>

</body>
</html>
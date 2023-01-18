<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Results 2</title>
  <style type="text/css">
 	table.rez td {text-align: center;}
  </style>
</head>
<body style="background-color:#<%= session.getAttribute("pickedBgCol") %>">
  <h1>Results of the voting (2):</h1>
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
  <br>
  <form action="glasanje-rezultati2">
  <label for="sort">Choose result sorting:</label>
  <select id="sort" name="sort">
    <option value="abcAsc">Alphabetically ascending</option>
    <option value="abcDesc">Alphabetically descending</option>
    <option value="voteAsc">Votes ascending</option>
    <option value="voteDesc" selected="selected">Votes descending</option>
  </select>
  <input type="submit" value="Submit">
</form>
</body>
</html>
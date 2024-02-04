<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <style type="text/css">
    td { padding:0 15px; }
  </style>
  <title>Trigonometric</title>
</head>
<body style="background-color:#<%= session.getAttribute("pickedBgCol") %>">
  <p>Trigonometric values:</p>
  <table>
  <thead>
    <tr>
      <th>x</th>
      <th>sin(x)</th>
      <th>cos(x)</th>
    </tr>
  </thead>
  <tbody>
    <% for(int i = (Integer)request.getAttribute("a"); i <= (Integer)request.getAttribute("b") ; i++) { %>
      <tr>
        <td><%= i %></td>
        <td><%= Math.sin(Math.toRadians(i)) %></td>
        <td><%= Math.cos(Math.toRadians(i)) %></td>
      </tr>
    <% } %>
  </tbody>
  </table>
</body>
</html>
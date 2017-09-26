<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Change Character Name</title>
  <script>
  $("#characterDetailsSummaryFrame").empty();
  clearCompares();
  charId=-1;
  </script>
<style>
</style>
</head>
<body>
	<%@ include file="../checkError.jsp" %>
	<h1>Change Character Name</h1>	
    <form method="POST" action="${pageContext.request.contextPath}/play/character/${character.id}/rename/${boostId}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="id" value="${character.id}" />
    <input type="hidden" name="accountId" value="${account.id}" />
    <input type="hidden" name="hardcore" value="${hardcore}" />
    <input type="hidden" name="ironborn" value="${ironborn}" />
	<table>
        <tr>
        	<td><label for="name">Name</label></td>
        	<td><input id="name" name="name" type="text" maxlength="14" value="${character.name}" /></td>
        </tr>
        <tr>
        	<td>&nbsp;</td>
        	<td><input name="submit" type="submit" value="Change Name" /></td>
        </tr>
    </table>
    </form>
</body>
</html>

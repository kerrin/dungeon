<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Account</title>
</head>
<body onload="javascript: initSummary('${pageContext.request.contextPath}')">
	<%@ include file="common.jsp" %>
	<span class="accountSpan">
	<h1>Add Dungeon Tokens <img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /></h1>
	You cannot currently purchase additional dungeon tokens.
	</span>
</body>
</html>

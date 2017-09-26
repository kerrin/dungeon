<!doctype html>
<!-- 
 This page can be viewed in 2 modes:
  * My account hiscores (logged in users)
  * View an accounts hiscores (logged in or logged out users) 
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Account</title>
<script>
function setOffsetAndSubmit(offset) {
	document.getElementById('hiscoresForm').offset.value=offset;
	document.getElementById('hiscoresForm').submit();
}
</script>
</head>
<body <c:if test="${!empty account}">onload="javascript: initSummary('${pageContext.request.contextPath}','?hardcore='+(hardcore?'1':'0')+'&ironborn='+(ironborn?'1':'0'))"</c:if> >
	<%@ include file="play/common.jsp" %>
	<span class="accountSpan">
	<span class="hiscoreTitle">
	<h1>Hiscores</h1>
	</span>
	<span class="hiscoreDetails">
	<form:form method="GET" modelAttribute="hiscoresForm" action="${pageContext.request.contextPath}/hiscores">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <form:input type="hidden" path="offset" />
    Score Type:
	<form:select path="type">
 		<form:options items="${hiscoreTypes}" itemLabel="niceName" />
	</form:select>
	<br />
	Hardcore: <form:checkbox path="viewHardcore" value="true" id="viewHardcore" /><br />
	Ironborn: <form:checkbox path="viewIronborn" value="true" id="viewIronborn" /><br />
	<input name="search" type="submit" value="Search" />
	<c:if test="${!empty hiscoresForm.type}">
	<h2 data-qtiptitle="${hiscoresForm.type.description}">Hiscores - ${hiscoresForm.type.niceName}</h2>
	</c:if>
	<c:if test="${empty hiscoresForm.type}">
	<h2>Hiscores</h2>
	</c:if>
	<table>
	<tr><th>Name</th><th>Rank</th><th>Score</th><th>Earnt Date</th></tr>
	<c:forEach items="${hiscores}" var="hiscore">
	<tr>
		<td style="text-align: center;"><a href="${pageContext.request.contextPath}/profile/${hiscore.account.id}/?hardcore=${hardcore}&ironborn=${ironborn}">${hiscore.account.displayName}</a></td>
		<td style="text-align: center;">${hiscore.rank<1?'Unranked':hiscore.rank}</td>
		<td style="text-align: right;">${hiscore.displayScore}</td>
		<fmt:formatDate value="${hiscore.timestamp}" var="formattedDate" type="date" pattern="dd MMM" timeZone="GMT" />
		<fmt:formatDate value="${hiscore.timestamp}" var="formattedTime" type="date" pattern="HH:mm:ss" timeZone="GMT" />
		<td style="text-align: center;" data-qtiptitle="${formattedTime}">		
	   	${formattedDate}
		</td>
	</tr>
	</c:forEach>
	</table>
	<br />
	<c:if test="${hiscoresForm.offset > 0}">
	<a href="#" onclick="javascript: setOffsetAndSubmit(${hiscoresForm.offset-hiscoresForm.pageSize});">Previous</a>
	</c:if>
	&nbsp;Page Size: 
	<select name="pageSize" onchange="javascript: setOffsetAndSubmit(${hiscoresForm.offset});")>
		<c:if test="${hiscoresForm.pageSize == 10}">
		<option value="10" selected="selected">10</option>
		</c:if>
		<c:if test="${hiscoresForm.pageSize != 10}">
		<option value="10">10</option>
		</c:if>
		<c:if test="${hiscoresForm.pageSize == 25}">
		<option value="25" selected="selected">25</option>
		</c:if>
		<c:if test="${hiscoresForm.pageSize != 25}">
		<option value="25">25</option>
		</c:if>
		<c:if test="${hiscoresForm.pageSize == 50}">
		<option value="50" selected="selected">50</option>
		</c:if>
		<c:if test="${hiscoresForm.pageSize != 50}">
		<option value="50">50</option>
		</c:if>
	</select>
	&nbsp;
	<c:if test="${hasMore}">
	<a href="#" onclick="javascript: setOffsetAndSubmit(${hiscoresForm.offset+hiscoresForm.pageSize});">Next</a>
	</c:if>
	</form:form>
	</span>
	</span>
</body>
</html>

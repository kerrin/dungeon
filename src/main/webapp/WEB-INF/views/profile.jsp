<!doctype html>
<!-- 
 This page can be viewed in 2 modes:
  * My account profile (logged in users)
  * View an accounts profile (logged in and logged out users) 
 --><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Account</title>
</head>
<body <c:if test="${!empty account}">onload="javascript: initSummary('${pageContext.request.contextPath}','?hardcore='+(hardcore?'1':'0')+'&ironborn='+(ironborn?'1':'0'))"</c:if> >
	<%@ include file="play/common.jsp" %>
	<span class="profileSpan">
	<span class="profileTitle">
	<h1>${viewAccount.displayName}</h1>
	</span>
	<span class="profileHiscores">
	<a href="${pageContext.request.contextPath}/achievements/${viewAccount.id}?hardcore=${hardcore}&ironborn=${ironborn}">Compare Achievements</a>
	<h2>Hiscores</h2>
	<table>
	<tr><th>Score Type</th><th>Mode</th><th>Rank</th><th>Score</th><th>Earnt Date</th></tr>
	<c:forEach items="${hiscores}" var="hiscore">
	<tr>
		<td style="text-align: right;">${hiscore.type.niceName}</td>
		<td style="text-align: center;">
			<font color="red">
			${hiscore.hardcore?(hiscore.ironborn?'Extreme':'Hardcore'):hiscore.ironborn?'Ironborn':'<font color="Green">Normal</font>'}
			</font>
		</td>
		<td style="text-align: center;">
			<a href="${pageContext.request.contextPath}/hiscores?viewHardcore=${hiscore.hardcore}&viewIronborn=${hiscore.ironborn}&type=${hiscore.type}&offset=${hiscore.rank}">
				${hiscore.rank<1?'Unranked':hiscore.rank}
			</a>
		</td>
		<td style="text-align: right;">${hiscore.displayScore}</td>
		<fmt:formatDate value="${hiscore.timestamp}" var="formattedDate" type="date" pattern="dd MMM" timeZone="GMT" />
		<fmt:formatDate value="${hiscore.timestamp}" var="formattedTime" type="date" pattern="HH:mm:ss" timeZone="GMT" />
		<td style="text-align: center;" data-qtiptitle="${formattedTime}">		
	   	${formattedDate}
		</td>
	</tr>
	</c:forEach>
	</table>
	</span>
	</span>
</body>
</html>

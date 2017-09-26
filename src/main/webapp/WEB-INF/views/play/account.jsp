<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Account</title>
</head>
<body onload="javascript: initSummary('${pageContext.request.contextPath}','?hardcore='+(hardcore?'1':'0')+'&ironborn='+(ironborn?'1':'0'))">
	<%@ include file="common.jsp" %>
	<span class="accountSpan">
	<span class="accountTitle">
	<h1>Account</h1>
	</span>
	<span class="accountDetails">
	<a href="${pageContext.request.contextPath}/play/account/history?hardcore=${hardcore}&ironborn=${ironborn}">Account Token History</a>
	<br />
	<a href="${pageContext.request.contextPath}/play/account/boostitem?hardcore=${hardcore}&ironborn=${ironborn}">Boost Items</a>
	<br />
	<a href="${pageContext.request.contextPath}/play/account/achievements?hardcore=${hardcore}&ironborn=${ironborn}">Achievements</a>
	<br />
	<img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" />:
	<ul> 
	<li>Normal: ${accountCurrencyStandard.currency} <a href="${pageContext.request.contextPath}/play/account/addTokens?hardcore=0&ironborn=0">Add Dungeon Tokens</a></li>
	<li>Hardcore: ${accountCurrencyHardcore.currency} <a href="${pageContext.request.contextPath}/play/account/addTokens?hardcore=1&ironborn=0">Add Dungeon Tokens</a></li>
	<li>Ironborn: ${accountCurrencyIronborn.currency} <a href="${pageContext.request.contextPath}/play/account/addTokens?hardcore=0&ironborn=1">Add Dungeon Tokens</a></li>
	<li>Extreme: ${accountCurrencyExtreme.currency} <a href="${pageContext.request.contextPath}/play/account/addTokens?hardcore=1&ironborn=1">Add Dungeon Tokens</a></li>
	</ul>
	Daily Dungeon Tokens: ${account.loginTokens}<br/>
	<c:if test="${!account.onHoliday}"><a href="${pageContext.request.contextPath}/play/account/onHoliday?hardcore=${hardcore}&ironborn=${ironborn}">Take A Holiday</a> <span style="color: red; font-weight: bold; cursor: help;" data-qtiptitle="You will receive no daily dungeon tokens for a day after you return, but your daily login dungeon tokens will not reduce each day while you are on holiday.">Note</span></c:if>
	<c:if test="${account.onHoliday}"><a href="${pageContext.request.contextPath}/play/account/offHoliday?hardcore=${hardcore}&ironborn=${ironborn}">Stop Holiday</a></c:if>
	<br />
	<br />
	<a href="${pageContext.request.contextPath}/play/account/modify">Modify Details</a>
	</span>
	<span class="accountHiscore">
	Your Hiscores:
	<table>
	<tr><th>Score Type</th><th>Mode</th><th>Rank</th><th>Score</th><th>Earnt Date</th></tr>
	<c:forEach items="${hiscores}" var="hiscore">
	<tr data-qtiptitle="${hiscore.type.description}">
		<td style="text-align: right;">${hiscore.type.niceName}</td>
		<td style="text-align: center;">
			<c:if test="${empty hiscore.hardcore || empty hiscore.ironborn}">
			Account
			</c:if>
			<c:if test="${not empty hiscore.hardcore && not empty hiscore.ironborn}">
			<font color="red">
			${hiscore.hardcore?(hiscore.ironborn?'Extreme':'Hardcore'):hiscore.ironborn?'Ironborn':'<font color="Green">Normal</font>'}
			</font>
			</c:if>
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

<c:if test="${accountConfigToolTips.value > 0}">	
<script>
$('[title]').qtip();
$('[data-qtiptitle]').each(function( index) {
	var thisTitle = $(this).attr('data-qtiptitle');
	$(this).attr('title', thisTitle);
	$(this).qtip({show: { <c:if test="${account.touchScreen}">event: 'touchend', </c:if>solo: true }, hide: { event: 'unfocus', distance: 100 }});
});
</script>
</c:if>
</body>
</html>

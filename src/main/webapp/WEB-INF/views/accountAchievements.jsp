<!doctype html>
<!-- 
 This page can be viewed in 3 modes:
  * My account achievements (logged in users)
  * Compare my achievements to another account (logged in users)
  * View an accounts achievements (logged out users) 
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Account Achievements</title>
<script>
function setDetailsDay(day) {
	$(".accountAchievementsDetailsDays").hide();
	$(".accountAchievementsDetails"+day).show();
}
</script>
</head>
<body onload="javascript: initSummary('${pageContext.request.contextPath}','?hardcore='+(hardcore?'1':'0')+'&ironborn='+(ironborn?'1':'0'))">
	<%@ include file="play/common.jsp" %>
	<span class="accountSpan">
	<span class="accountTitle">
	<h1>Account Achievements<c:if test="${!empty compareAccount}"> - <a href="${pageContext.request.contextPath}/profile/${compareAccount.id}/?hardcore=${hardcore}&ironborn=${ironborn}">${compareAccount.displayName}</a></c:if></h1>
	</span>
	<span class="accountAchievements">
		<span style="float: left;">Points:</span>
		<span style="float: left;">
		<table class="borderLessTable center">
		<c:if test="${!empty myAchievementsThisMode}">
		<tr><th>&nbsp;</th><th>Theirs</th><th>Yours</th><th>&nbsp;</th><th>Theirs</th><th>Yours</th><th>&nbsp;</th><th>Theirs</th><th>Yours</th></tr>
		</c:if>
		<tr>
		<td>Account Wide:</td><td>${achievementPointsGeneral}</td><td>${myAchievementPointsGeneral}</td>
		<td>This Mode:</td><td>${achievementPointsThisMode}</td><td>${myAchievementPointsThisMode}</td>
		<td>All:</td><td>${achievementPointsAll}</td><td>${myAchievementPointsAll}</td></tr>
		</table>
		</span>
		<br style="clear: both;"/>
		<span>
		<c:set var="index" value="0" />
		<c:forEach var="achievement" items="${achievementsThisMode}">
		<c:if test="${!empty achievement}">
			<c:if test="${index%20 == 0}">
				<c:if test="${index > 0}">
		</table>
				</c:if>
		<table style="float: left;">
			<c:if test="${empty myAchievementsThisMode}">
			<tr>
				<th>Achievement</th>
				<th>Points</th>
				<th>Got</th>
			</tr>
			</c:if>
			<c:if test="${!empty myAchievementsThisMode}">
			<tr>
				<th rowspan="2">Achievement</th>
				<th rowspan="2">Points</th>
				<th colspan="2">Got</th>
			</tr>
			<tr><th>Theirs</th><th>Yours</th></tr>
			</c:if>
			</c:if>
			<tr data-qtiptitle="${achievement.type.description}">
				<td>${achievement.type.niceName}</td>
				<td>${achievement.type.points}</td>
			   	<td
			   	<c:if test="${empty myAchievementsThisMode}">colspan="2"</c:if>
			   	>
				   	<c:if test="${achievement.id < 1}">
				   	<font color="red">Pending</font>
				   	</c:if>
				   	<c:if test="${achievement.id >= 1}">
				   	<fmt:formatDate value="${achievement.timestamp}" var="formattedDate" type="date" pattern="dd MMM yyyy" timeZone="GMT" />
				   	<font color="green">${formattedDate}</font>
				   	</c:if>
				</td>
				<c:if test="${!empty myAchievementsThisMode}">
				<td>
				   	<c:if test="${empty myAchievementsThisMode[achievement.type.order]}">
				   	<font color="red">Pending</font>
				   	</c:if>
				   	<c:if test="${!empty myAchievementsThisMode[achievement.type.order]}">
				   	<fmt:formatDate value="${myAchievementsThisMode[achievement.type.order].timestamp}" var="formattedDate" type="date" pattern="dd MMM yyyy" timeZone="GMT" />
				   	<font color="green">${formattedDate}</font>
				   	</c:if>
				   	</td>
				</c:if>
			</tr>
			<c:set var="index" value="${index+1}" />
		</c:if>
		</c:forEach>
		</table>
		</span>
		<span>
		<span style="display: inline-block; width: 280px;">Account Wide:</span><br />
		<span style="display: inline-block; min-width: 280px;">
		<c:set var="index" value="0" />
		<c:forEach var="achievement" items="${achievementsGeneral}">
		<c:if test="${!empty achievement}">
			<c:if test="${index%20 == 0}">
				<c:if test="${index > 0}">
		</table>
				</c:if>
		<table style="float: left;">
			<c:if test="${empty myAchievementsGeneral}">
			<tr>
				<th>Achievement</th>
				<th>Points</th>
				<th>Got</th>
			</tr>
			</c:if>
			<c:if test="${!empty myAchievementsGeneral}">
			<tr>
				<th rowspan="2">Achievement</th>
				<th rowspan="2">Points</th>
				<th colspan="2">Got</th>
			</tr>
			<tr><th>Theirs</th><th>Yours</th></tr>
			</c:if>
			</c:if>
			<tr data-qtiptitle="${achievement.type.description}">
				<td>${achievement.type.niceName}</td>
				<td>${achievement.type.points}</td>
			   	<td
			   	<c:if test="${empty myAchievementsGeneral}">colspan="2"</c:if>
			   	>
				   	<c:if test="${achievement.id < 1}">
				   	<font color="red">Pending</font>
				   	</c:if>
				   	<c:if test="${achievement.id >= 1}">
				   	<fmt:formatDate value="${achievement.timestamp}" var="formattedDate" type="date" pattern="dd MMM yyyy" timeZone="GMT" />
				   	<font color="green">${formattedDate}</font>
				   	</c:if>
				</td>
				<c:if test="${!empty myAchievementsGeneral}">
				<td>
				   	<c:if test="${empty myAchievementsGeneral[achievement.type.order]}">
				   	<font color="red">Pending</font>
				   	</c:if>
				   	<c:if test="${!empty myAchievementsGeneral[achievement.type.order]}">
				   	<fmt:formatDate value="${myAchievementsGeneral[achievement.type.order].timestamp}" var="formattedDate" type="date" pattern="dd MMM yyyy" timeZone="GMT" />
				   	<font color="green">${formattedDate}</font>
				   	</c:if>
			   	</td>
				</c:if>
			</tr>			
			<c:set var="index" value="${index+1}" />
		</c:if>
		</c:forEach>
		</table>
		</span>
		</span>
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

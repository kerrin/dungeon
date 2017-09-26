<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="equipmentTagLib" prefix="equip"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Character Summary</title>
</head>
<body>
	<%@ include file="../checkError.jsp" %>
	<span data-qtiptitle="A rough guide to the characters attack power in dungeons of the same level">
		Atk: 
		<c:if test="${attackValue >= 0}"><font style="color: green"></c:if>
		<c:if test="${attackValue < 0}"><font style="color: red"></c:if>
		${attackValue}
		</font>
	</span>
	<span data-qtiptitle="A rough guide to the characters defence power in dungeons of the same level">
		Def: 
		<c:if test="${defenceValue >= 0}"><font style="color: green"></c:if>
		<c:if test="${defenceValue < 0}"><font style="color: red"></c:if>
		${defenceValue}
		</font>
	</span>
	<span data-qtiptitle="A rough guide to the characters recovery rate">
		Rec: 
		<c:if test="${recoveryValue >= 0}"><font style="color: green"></c:if>
		<c:if test="${recoveryValue < 0}"><font style="color: red"></c:if>
		${recoveryValue}
		</font>
	</span>
	<br />
	<table>
	<c:forEach var="characterAttribute" items="${characterSummary}" varStatus="loopStatus" >
		<c:if test="${loopStatus.index % 2 == 0}">
		<tr>
		</c:if>
			<td><span data-qtiptitle="${characterAttribute.key.description}">${characterAttribute.key.niceName}</span></td>
			<td>
				<span data-qtiptitle="${characterAttribute.key.description}">
				<c:if test="${characterAttribute.value >= 0}"><font style="color: green"></c:if>
				<c:if test="${characterAttribute.value < 0}"><font style="color: red"></c:if>
				${characterAttribute.value}
				</font>
				</span>
			</td>
		<c:if test="${loopStatus.index % 2 != 0}">
		</tr>
		</c:if>
		</c:forEach>
		<c:if test="${fn:length(characterSummary) % 2 != 0}">
		</tr>
		</c:if>
	</table>

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

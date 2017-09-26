<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Account History</title>
</head>
<body onload="javascript: initSummary('${pageContext.request.contextPath}','?hardcore='+(hardcore?'1':'0')+'&ironborn='+(ironborn?'1':'0'))">
	<%@ include file="common.jsp" %>
	<span class="accountSpan">
	<span class="accountTitle">
	<h1>
	Account Boost Items
	<c:if test="${hardcore}">- Hardcore</c:if>
	<c:if test="${ironborn}">- Ironborn</c:if>
	<c:if test="${!hardcore && !ironborn}">- Standard</c:if>
	</h1>
	</span>
	<span class="accountBoostItemSummary">
		<table>
			<tr>
				<th>Boost Item</th>
				<th>Level</th>
				<th>Description</th>
				<c:if test="${account.touchScreen}"><th>Instructions</th></c:if>
			</tr>
			<c:forEach items="${boostItems}" var="boostItem">
		   		<tr>
		   			<td data-qtiptitle="${boostItem.boostItemType.getUsageInstructions(boostItem.level)}">${boostItem.boostItemType.niceName}</td>
		   			<td>${boostItem.level}</td>
		   			<td>${boostItem.boostItemType.getDescription(boostItem.level)}</td>
		   			<c:if test="${account.touchScreen}"><td>${boostItem.boostItemType.getUsageInstructions(boostItem.level)}</td></c:if>
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

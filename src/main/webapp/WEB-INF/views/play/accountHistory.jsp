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
<script>
function setDetailsDay(day) {
	$(".accountHistoryDetailsDays").hide();
	$(".accountHistoryDetails"+day).show();
}
</script>
</head>
<body onload="javascript: initSummary('${pageContext.request.contextPath}','?hardcore='+(hardcore?'1':'0')+'&ironborn='+(ironborn?'1':'0'))">
	<%@ include file="common.jsp" %>
	<span class="accountSpan">
	<span class="accountTitle">
	<h1>Account Token History</h1>
	</span>
	<span class="accountHistoryDetailsSummary">
		<c:set var="startDay" value="${history.startDay}" />
	    <c:forEach items="${history.days}" var="day">
	    	<c:if test="${day == startDay}"><span class="accountHistoryMonth">${history.endMonth}</span><br style="clear: both;"/></c:if>
			<c:set var="dayHistorySummary" value="${history.getDaySummary(day)}" />
			<c:set var="dayAccountAudits" value="${dayHistorySummary.accountAudits}" />    		
    		<c:set var="summaries" value="${dayHistorySummary.getSummaries(hardcore, ironborn)}" />
    		<c:if test="${!empty summaries}">
    			<span class="accountHistorySummary">
	    			<a href="javascript: setDetailsDay(${day});">
				    	<span class="accountHistorySummaryDay">
				    		${day}<c:if test="${day % 10 == 0}">th</c:if><c:if test="${day % 10 == 1}">st</c:if><c:if test="${day % 10 == 2}">nd</c:if><c:if test="${day % 10 == 3}">rd</c:if><c:if test="${day % 10 > 3}">th</c:if>
			    		</span>
			    		<br style="clear: both;" />
				    	<span class="accountHistorySummaries">
				    		<c:set var="dayTotal" value="0" />
			    			<c:forEach items="${summaries.modificationTypes}" var="modificationType">
				    			<c:set var="thisValue" value="${summaries.getValue(modificationType)}" />
				    			<span class="accountHistorySummaryLabel">${modificationType.niceName}:&nbsp;</span>
				    			<span class="accountHistorySummaryValue">
				    				<c:if test="${thisValue < 0}">
				    				<font color="red">
				    				</c:if>
				    				<c:if test="${thisValue >= 0}">
				    				<font color="green">
				    				</c:if>
				    				${thisValue}
				    				</font>
				    			</span>
				    			<c:set var="dayTotal" value="${dayTotal + thisValue}" />
			    			</c:forEach>
				    		<span class="accountHistorySummaryLabel"><strong>Day Total</strong>:&nbsp;</span>
				    		<span class="accountHistorySummaryValue">
				    			<c:if test="${dayTotal < 0}">
				    			<font color="red">
			    				</c:if>
			    				<c:if test="${dayTotal >= 0}">
			    				<font color="green">
			    				</c:if>
				    			${dayTotal}
				    			</font>
				    		</span>
					    </span>
			    	</a>
			    </span>
		    	<br style="clear: both;"/>
	        </c:if>
			<c:if test="${day != startDay}">
	    		<c:if test="${day == 1}"><span class="accountHistoryMonth">${history.startMonth}</span><br style="clear: both;"/></c:if>
	    	</c:if>	    	
	    </c:forEach>
	</span>
	<span class="accountHistoryDetails">
	<c:forEach items="${history.days}" var="day">
		<c:set var="dayHistorySummary" value="${history.getDaySummary(day)}" />
		<c:set var="dayAccountAudits" value="${dayHistorySummary.accountAudits}" />    		
    	<span class="accountHistoryDetailsDays accountHistoryDetails${day} accountHistoryDetailsDayHeader">
    		${day}<c:if test="${day % 10 == 0}">th</c:if><c:if test="${day % 10 == 1}">st</c:if><c:if test="${day % 10 == 2}">nd</c:if><c:if test="${day % 10 == 3}">rd</c:if><c:if test="${day % 10 > 3}">th</c:if>
    		of
    		<c:if test="${day <= startDay}">${history.endMonth}</c:if>
    		<c:if test="${day > startDay}">${history.startMonth}</c:if>
    	</span>
		<c:forEach items="${dayAccountAudits}" var="audit">
			<c:if test="${audit.hardcore == hardcore && audit.ironborn == ironborn}">
			<span class="accountHistoryDetailsDays accountHistoryDetails${day}">
				<span class="accountHistoryDetailsDayTime"><fmt:formatDate type="time" value="${audit.timestamp}" /></span> 
				<span class="accountHistoryDetailsDayType">${audit.modificationType.niceName}</span>
				<span class="accountHistoryDetailsDayValue">${audit.currency}</span>
			</span>
			</c:if>
		</c:forEach>
	</c:forEach>
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

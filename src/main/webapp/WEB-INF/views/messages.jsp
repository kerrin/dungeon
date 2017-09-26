<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="equipmentTagLib" prefix="equip"%>
<%@ taglib uri="boostItemTagLib" prefix="boost"%>
<span><h1>Messages</h1></span>
<c:forEach items="${messages}" var="thisMessage">
<span class="messageSpan<c:if test="${!empty thisMessage.displayItem}"> messageItemSpan</c:if>">
<c:if test="${!empty thisMessage.linkUrl && !empty thisMessage.linkPanel}">
<a href="#" onclick="loadSpan('${thisMessage.linkPanel}','${thisMessage.linkUrl}');">
</c:if>
<c:if test="${!empty thisMessage.displayItem}">
	<c:if test="${thisMessage.displayItem.stashSlotType == 'EQUIPMENT'}">
		<equip:equipmentDisplay 
				equipment="${thisMessage.displayItem}"
				compare="false" 
       			dragable="true" 
       			dragableSrcFrame="messagesContentFrame"
       			dropable="false" 
       			hoverAbove="false"
       			accountApiKey="${account.apiKey}"
       			isTouchScreen="${account.touchScreen}"
       			/>
     </c:if>
     <c:if test="${thisMessage.displayItem.stashSlotType == 'BOOST_ITEM'}">
     	<boost:boostItemDisplay 
      			item="${thisMessage.displayItem}" 
      			dragable="true" 
      			dragableSrcFrame="messagesContentFrame"
      			dropable="false"
      			accountApiKey="${account.apiKey}"
       			viewForm="hardcore=${hardcore}&ironborn=${ironborn}&noBreakout=false"
      			/>
     </c:if>
</c:if>
${thisMessage.message}
<c:if test="${!empty thisMessage.linkUrl && !empty thisMessage.linkPanel}">
</a>
</c:if>
</span>
</c:forEach>
<span id="messagesCloseFrame" class="messagesCloseFrame">
<a href="javascript: closeMessages('${pageContext.request.contextPath}','?hardcore=${hardcore}&ironborn=${ironborn}');" 
	style="color: red;">X</a>
</span>

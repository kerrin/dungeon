<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${!empty error}">
<div id="errorPopUp" class="error">
<span class="errorText">${error}</span>
<span id="errorCloseFrame" class="errorCloseFrame">
<a href="javascript: closeError();" style="color: red;">X</a>
</span>
</div>
</c:if>
<!-- Only use message if the user is logged out -->
<c:if test="${!empty message}">
<script>
$("#outerSpan_messageFrame").append("<div class='messageContentFrame'></div>");
$(".messageContentFrame").load("${pageContext.request.contextPath}/message/${message}");
$("#outerSpan_messagesFrame").show();
</script>
<span id="messageCloseFrame" class="messageCloseFrame">
<a href="javascript: closeMessage();" style="color: blue;">X</a>
</span>
</c:if>
<c:if test="${!empty gotMessages && gotMessages}">
<script>
	var typeParam = "?hardcore="+(hardcore?"1":"0")+"&ironborn="+(ironborn?"1":"0");
	showMessages('${pageContext.request.contextPath}', typeParam);
</script>
</c:if>

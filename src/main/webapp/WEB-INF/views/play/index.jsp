<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Dungeon</title>
</head>
<body onLoad="initPlay('${pageContext.request.contextPath}',${dungeonId},${charId})">
	<%@ include file="common.jsp" %>
	<span id="outerSpan_dungeonsFrame" class="hiddenFullPage"></span>
	<span id="outerSpan_enchantFrame" class="enchantFrame"></span>
	<span id="dungeonsFrame" class="dungeonsSpan"><h1>Dungeons</h1></span>
	<span id="charactersFrame" class="charactersSpan"><h1>Characters</h1></span>
	<span id="characterDetailsFrame" class="characterDetailsSpan"><h1>Character Details</h1></span>
	<span id="characterDetailsSummaryFrame" class="characterDetailsSummarySpan"><h1>Summary</h1></span>
	<br style="clear both;" />
	<span id="enchantFrame" class="enchantSpan"><h3>Enchanting</h3></span>
	<span id="stashFrame" class="stashSpan"><h3>Stash</h3></span>
	<span id="salvageFrame" class="salvageSpan"><h3>Salvage</h3></span>
	<c:if test="${account.debugMode}">
	<span id="debugFrame" class="debugSpan">
	<h3>Debug Mode</h3>
	Pending Loads: <span id="debugPendingLoadsSpan" class="debugPendingLoadsSpan">0</span><br style="clear: both;" />
	Logs: <br style="clear: both;" />
	<span id="debugLogsSpan" class="debugLogsSpan">Debug on<br/></span>
	</c:if>
<script type="text/javascript">
// Make the Characters frame fill the remaining width
var windowWidth = $(window).width();
$('#charactersFrame').width(windowWidth - 4 - 302 - Math.min(((windowWidth/2) - 150), 506));
$('#charactersFrame').css('left', Math.min(((windowWidth/2) - 150), 506) + 2);

$(window).resize(function(event) {
	var windowWidth = $(window).width();
	$('#charactersFrame').width(windowWidth - 4 - 302 - Math.min(((windowWidth/2) - 150), 506));
	$('#charactersFrame').css('left', Math.min(((windowWidth/2) - 150), 506) + 2);
});

$( ".enchantFrame" ).draggable();
</script>
</body>
</html>

<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Dungeon - Dungeons</title>
<script>
$("#outerSpan_dungeonsFrame").empty();
dungeonId=-1;
</script>
</head>
<body>
	<%@ include file="../checkError.jsp" %>
	<h1 style="float: left;">Dungeons</h1>
	<span style="float:right">
	<c:if test="${!empty magicFindBoostExpires}">
		<fmt:formatDate value="${magicFindBoostExpires}" var="formattedDateMagicFindBoostExpires" type="date" pattern="EEE MMM dd HH:mm:ss zzz yyyy" timeZone="GMT" />
        <script lang="javascript">initializeClock('magicFindExpiredDiv', '${formattedDateMagicFindBoostExpires}');</script>
 		<div id="magicFindExpiredDiv" style="color: green; float: left;" data-qtiptitle="Magic find on closed dungeons is doubled until this runs out.">
 			<span class="magicFindBoostIcon">&nbsp;</span>
			<span class="hours"></span>h
			<span class="minutes"></span>m
			<span class="seconds"></span>s
 		</div>
	</c:if>
	<c:if test="${!empty xpBoostExpires}">
		<fmt:formatDate value="${xpBoostExpires}" var="formattedDateXpBoostExpires" type="date" pattern="EEE MMM dd HH:mm:ss zzz yyyy" timeZone="GMT" />
        <script lang="javascript">initializeClock('xpBoostExpiredDiv', '${formattedDateXpBoostExpires}');</script>
 		<div id="xpBoostExpiredDiv" style="color: green; float: left;" data-qtiptitle="Experience is doubled on finishing dungeons until this runs out.">
 			<span class="xpBoostIcon">&nbsp;</span>
			<span class="hours"></span>h
			<span class="minutes"></span>m
			<span class="seconds"></span>s
 		</div>
	</c:if>
	</span>
	<br style="clear: both;" />
	<span style="float: left;" title="This will allow you to manually add characters to an Adventure dungeon">
		<a href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon/prepareAdventure?hardcore=${hardcore}&ironborn=${ironborn}');">
			Run Adventure
		</a>
	</span>
	<c:if test="${refreshCost > 4}">
	<span style="float: right;" title="This will remove all dungeons that are not currently started. If that means you will have less pending dungeons than the level of your highest level character, new pending dungeons will be created.">
		<a href="javascript: if(confirm('Are you sure you want refresh all the pending dungeons for dungeon tokens?')) { loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon/refresh?hardcore=${hardcore}&ironborn=${ironborn}');}">
			Refresh All Pending Dungeons (Costs ${refreshCost}
			<img src="${pageContext.request.contextPath}/images/token.png" alt="Tokens" title="Dungeon Tokens" class="dungeonToken" />)
		</a>
	</span>
	</c:if>
	<br style="clear: both;" />
	<c:if test="${fn:length(dungeons) > 0}">
	<table border="1" class="center black">
	<tr>
		<th>&nbsp;</th>
        <th>Type</th>  
        <th>Level</th>
        <th>XP</th>
        <th><font color="yellow">Finish</font>/<font color="red">Expire</font></th>
        <th>Party Size</th>        
    </tr>
	<c:forEach items="${dungeons}" var="dungeon">
		<c:if test="${(!empty dungeon.expires && dungeon.expires.time > now.time) || empty dungeon.expires}">
        <tr>
            <td><a style="font-weight: normal;" href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon/${dungeon.id}');">&#x1f441;</a></td>
            <td style="text-align: right;">
            	<c:if test="${!account.touchScreen}"><a style="font-weight: normal;" href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon/${dungeon.id}');"></c:if>
            		<font color="${dungeon.type.htmlColour}">${dungeon.type.niceName}</font>
            	<c:if test="${!account.touchScreen}"></a></c:if>
            </td>
            <td>
            	<c:if test="${!account.touchScreen}"><a style="font-weight: normal;" href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon/${dungeon.id}');"></c:if>
            		${dungeon.level}
            	<c:if test="${!account.touchScreen}"></a></c:if>
            </td>
            <td>
            	<c:if test="${!account.touchScreen}"><a style="font-weight: normal;" href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon/${dungeon.id}');"></c:if>
            		${dungeon.xpReward}
            	<c:if test="${!account.touchScreen}"></a></c:if>
            </td>
            <td>
            <c:if test="${!account.touchScreen}"><a style="font-weight: normal;" href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon/${dungeon.id}');"></c:if>
            <c:if test="${!empty dungeon.doneDate}">
            <fmt:formatDate value="${dungeon.doneDate}" var="formattedDate" type="date" pattern="EEE MMM dd HH:mm:ss zzz yyyy" timeZone="GMT" />
            <script lang="javascript">initializeClock('finishDiv${dungeon.id}', '${formattedDate}', null, null, '${dungeon.id}');</script>
   			<div id="finishDiv${dungeon.id}" style="color: yellow">
	   			<span class="hours"></span>h
				<span class="minutes"></span>m
				<span class="seconds"></span>s
   			</div>
            </c:if>
            <c:if test="${!empty dungeon.expires}">
            <fmt:formatDate value="${dungeon.expires}" var="formattedDate" type="date" pattern="EEE MMM dd HH:mm:ss zzz yyyy" timeZone="GMT" />
            <script lang="javascript">initializeClock('expiredDiv${dungeon.id}', '${formattedDate}', null, null, '${dungeon.id}');</script>
   			<div id="expiredDiv${dungeon.id}" style="color: red">
   				<span class="hours"></span>h
				<span class="minutes"></span>m
				<span class="seconds"></span>s
   			</div>
            </c:if>
            <c:if test="${!account.touchScreen}"></a></c:if>
            </td>
            <td>
            <span id="finished_${dungeon.id}" class="dungeonFinished">
            <c:if test="${!dungeon.failed}">
            <a href="javascript: reloadPage('${pageContext.request.contextPath}/play/dungeon/${dungeon.id}/stashAll?hardcore=${hardcore}&ironborn=${ironborn}');">Stash &amp; Close</a>
            <br />
            <c:set var="deadCount" scope="session" value="${dungeon.deadCount}" />
            <c:if test="${dungeon.partySize > 0}">
            <c:forEach begin="0" end="${dungeon.partySize - deadCount - 1}" varStatus="loop">
            <c:forEach begin="1" end="5" varStatus="breakPeopleAtNumberLoop">
            <c:if test="${loop.index > 0 && loop.index % breakPeopleAtNumberLoop.index == 0}"><br class="br${breakPeopleAtNumberLoop.index}" /></c:if>
            </c:forEach>
            <span class="peopleGreen" data-qtiptitle="Alive">&nbsp;</span>
			</c:forEach>
			</c:if>
            <c:if test="${deadCount > 0}">
            <c:forEach begin="${dungeon.partySize - deadCount}" end="${dungeon.partySize - 1}" varStatus="loop">
            <c:forEach begin="1" end="5" varStatus="breakPeopleAtNumberLoop">
            <c:if test="${loop.index > 0 && loop.index % breakPeopleAtNumberLoop.index == 0}"><br class="br${breakPeopleAtNumberLoop.index}" /></c:if>
            </c:forEach>
            <span class="peopleRed" data-qtiptitle="Dead">&nbsp;</span>
            </c:forEach>
            </c:if>
            </c:if>
            <c:if test="${dungeon.failed}">            
            <a href="javascript: reloadPage('${pageContext.request.contextPath}/play/dungeon/${dungeon.id}/close?hardcore=${hardcore}&ironborn=${ironborn}');">Close Failed</a>
            <br />
            <c:forEach begin="0" end="${dungeon.partySize - 1}" varStatus="loop">
            <c:forEach begin="1" end="5" varStatus="breakPeopleAtNumberLoop">
            <c:if test="${loop.index > 0 && loop.index % breakPeopleAtNumberLoop.index == 0}"><br class="br${breakPeopleAtNumberLoop.index}" /></c:if>
            </c:forEach>
            <span class="peopleRed" data-qtiptitle="Dead">&nbsp;</span>
            </c:forEach>
            </c:if>
            </span>
            <span id="pending_${dungeon.id}" class="dungeonPending">
            <a style="font-weight: normal;" href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon/${dungeon.id}');">
            <c:if test="${!empty dungeon.started}">
            <c:if test="${!dungeon.finished}">
            <c:forEach begin="0" end="${dungeon.partySize - 1}" varStatus="loop">
            <c:forEach begin="1" end="5" varStatus="breakPeopleAtNumberLoop">
            <c:if test="${loop.index > 0 && loop.index % breakPeopleAtNumberLoop.index == 0}"><br class="br${breakPeopleAtNumberLoop.index}" /></c:if>
            </c:forEach>
            <span class="peopleYellow" data-qtiptitle="Running Dungeon">&nbsp;</span>
            </c:forEach>
            </c:if>
            </c:if>
            <c:if test="${empty dungeon.started}">
            <c:forEach begin="0" end="${dungeon.type.minCharacters - 1}" varStatus="loop">
            <c:forEach begin="1" end="5" varStatus="breakPeopleAtNumberLoop">
            <c:if test="${loop.index > 0 && loop.index % breakPeopleAtNumberLoop.index == 0}"><br class="br${breakPeopleAtNumberLoop.index}" /></c:if>
            </c:forEach>
            <c:if test="${localMode}"><span class="peopleBlueLocalMode" data-qtiptitle="Required">&nbsp;</span></c:if>
            <c:if test="${! localMode}"><span class="peopleBlue" data-qtiptitle="Required">&nbsp;</span></c:if>
            </c:forEach>
            <c:forEach begin="${dungeon.type.minCharacters}" end="${dungeon.type.maxCharacters - 1}" varStatus="loop">
            <c:forEach begin="1" end="5" varStatus="breakPeopleAtNumberLoop">
            <c:if test="${loop.index > 0 && loop.index % breakPeopleAtNumberLoop.index == 0}"><br class="br${breakPeopleAtNumberLoop.index}" /></c:if>
            </c:forEach>
            <c:if test="${localMode}"><span class="peopleOrangeLocalMode" data-qtiptitle="Optional">&nbsp;</span></c:if>
            <c:if test="${! localMode}"><span class="peopleOrange" data-qtiptitle="Optional">&nbsp;</span></c:if>
            </c:forEach>
            </c:if>
            </a>
            </span>
            <c:if test="${!empty dungeon.started && dungeon.finished}">  
            <script>
            hideDivById("pending_${dungeon.id}");
            showDivById("finished_${dungeon.id}");
            </script>          
            </c:if>
            </td>
        </tr>
        </c:if>
    </c:forEach>
    </table>
    </c:if>
<script>
<c:if test="${accountConfigToolTips.value > 0}">
$('[title]').qtip();
$('[data-qtiptitle]').each(function( index) {
	var thisTitle = $(this).attr('data-qtiptitle');
	$(this).attr('title', thisTitle);
	$(this).qtip({show: { <c:if test="${account.touchScreen}">event: 'touchend', </c:if>solo: true }, hide: { event: 'unfocus', distance: 100 }});
});
</c:if>

// Break the people at a sensible place for the space there is. No more than 5 people per line
var dungeonFrameWidth = $('#dungeonsFrame').width();
var showIndex = Math.round((dungeonFrameWidth - 300) / 25);
if(showIndex < 1) { showIndex = 1; }
if(showIndex > 5) { showIndex = 5; }
showOnlyDivByClass('br', 1, 5, showIndex);

$(window).resize(function(event) {
	var dungeonFrameWidth = $('#dungeonsFrame').width();
	var showIndex = Math.round((dungeonFrameWidth - 300) / 25);
	if(showIndex < 1) { showIndex = 1; }
	if(showIndex > 5) { showIndex = 5; }
	showOnlyDivByClass('br', 1, 5, showIndex);
});
</script>
</body>
</html>
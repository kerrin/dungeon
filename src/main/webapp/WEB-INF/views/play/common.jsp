<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/shared.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/play.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dungeonpedia.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.qtip.min.css">
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.qtip.min.js"></script>
<script src="${pageContext.request.contextPath}/js/imagesloaded.pkg.min.js"></script>
<script src="${pageContext.request.contextPath}/js/ios-drag-drop.js"></script>
<script src="${pageContext.request.contextPath}/js/shared.js"></script>
<script src="${pageContext.request.contextPath}/js/play-drag-and-drop.js"></script>
<script src="${pageContext.request.contextPath}/js/play-manipulate-dom.js"></script>
<script lang="javascript">
var charId=${charId};
var dungeonId=${dungeonId};
var hardcore=${hardcore};
var ironborn=${ironborn};
function submitPlay() {
	document.getElementById('playSubmitForm').charId.value=charId;
	document.getElementById('playSubmitForm').dungeonId.value=dungeonId;
	document.getElementById('playSubmitForm').submit();
	return true;
}
</script>
<script>
  window.fbAsyncInit = function() {
    FB.init({
      appId      : '${facebook.apiId}',
      xfbml      : true,
      version    : 'v2.5'
    });
  };
  
  (function(d, s, id){
     var js, fjs = d.getElementsByTagName(s)[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement(s); js.id = id;
     js.src = "//connect.facebook.net/en_US/sdk.js";
     fjs.parentNode.insertBefore(js, fjs);
   }(document, 'script', 'facebook-jssdk'));
</script>

<div class="navMenu">
<div class="icon"><h1>Dungeon</h1></div>
<c:if test="${!empty account}">
<ul class="menu">
<li>
<form id="playSubmitForm" action="${pageContext.request.contextPath}/play" method="GET">
	<input type="hidden" name="hardcore" value="${hardcore}" />
	<input type="hidden" name="ironborn" value="${ironborn}" />
	<input type="hidden" name="noBreakout" value="true" />
	<input type="hidden" name="charId" value="${charId}" />
	<input type="hidden" name="dungeonId" value="${dungeonId}" />
</form>
</li>
<li id="playMenuPlay" class="navMenuOption"><a href="javascript: submitPlay();" class="menuLink">Play</a></li>
<li id="playMenuAccount" class="navMenuOption"><a href="${pageContext.request.contextPath}/play/account?hardcore=${hardcore}&ironborn=${ironborn}&noBreakout=true" class="menuLink">Account</a></li>
<li id="playMenuNewPlayer" class="navMenuOption"><a href="${pageContext.request.contextPath}/newPlayers?noBreakout=true&hardcore=${hardcore}&ironborn=${ironborn}" class="menuLink">New Player Info</a></li>
<li id="playMenuDungeonpedia" class="navMenuOption"><a href="javascript: createDungeonPedia('${pageContext.request.contextPath}');" class="menuLink">Dungeon-Pedia</a></li>
<sec:authorize access="hasRole('View') || hasRole('Modify')">
<li id="playMenuDungeonpedia" class="navMenuOption"><a href="${pageContext.request.contextPath}/admin" class="menuLink">Admin</a></li>
</sec:authorize>
</ul>

<c:if test="${account.level > 7}">
  <span id="viewTypeFrame" class="viewTypeFrame">
    <span title="In this mode, you cannot resurrect dead characters!">
      <a href="${pageContext.request.contextPath}/play/viewTypeChange?hardcore=${!hardcore}&ironborn=${ironborn}&noBreakout=true" class="menuLink">Hardcore&nbsp;
        <c:if test="${hardcore}"><font color="green">&#10004;</font></c:if><c:if test="${!hardcore}"><font color="red">&#10006;</font></c:if>
      </a>
    </span>
    &nbsp;
    <span title="In this mode, once an item is equiped, it cannot be put back in the stash or on another character!">
      <a href="${pageContext.request.contextPath}/play/viewTypeChange?hardcore=${hardcore}&ironborn=${!ironborn}&noBreakout=true" class="menuLink">Ironborn&nbsp;
        <c:if test="${ironborn}"><font color="green">&#10004;</font></c:if><c:if test="${!ironborn}"><font color="red">&#10006;</font></c:if>
      </a>
    </span>
  </span>
</c:if>
<c:if test="${account.level <= 7}">
<div id="viewTypeFrame" class="viewTypeFrame" data-qtiptitle="Level a character to 7 to unlock more modes. Current highest level character is ${account.level}">
Unlock at level 7
</div>
</c:if>
</c:if>
<c:if test="${empty account}">
	<ul class="menuLoggedOut">
		<li id="playMenuLogin" class="navMenuOption"><a href="${pageContext.request.contextPath}/play?noBreakout=true&hardcore=${hardcore}&ironborn=${ironborn}" class="menuLink">Play</a></li>
		<li id="playMenuNewPlayer" class="navMenuOption"><a href="${pageContext.request.contextPath}/newPlayers?noBreakout=true&hardcore=${hardcore}&ironborn=${ironborn}" class="menuLink">New Player Info</a></li>
		<li id="playMenuDungeonpedia" class="navMenuOption"><a href="javascript: createDungeonPedia('${pageContext.request.contextPath}');" class="menuLink">Dungeon-Pedia</a></li>
	</ul>

</c:if>
<div id="summaryFrame" class="summaryFrame"></div>
<div class="endBar">
<hr style="margin: 0px;" />
</div>
<div>
<div
  class="fb-like"
  data-share="true"
  data-width="140"
  data-show-faces="false"
  data-layout="button">
</div>
</div>
</div>
<div id="outerSpan_dungeonpediaFrame" class="dungeonpediaFrame"></div>
<div id="outerSpan_messagesFrame" class="messagesFrame"></div>
<%@ include file="../checkError.jsp" %>

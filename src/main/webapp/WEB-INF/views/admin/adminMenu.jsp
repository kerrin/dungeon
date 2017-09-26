<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/shared.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.qtip.min.css">
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.qtip.min.js"></script>
<script src="${pageContext.request.contextPath}/js/imagesloaded.pkg.min.js"></script>
<script src="${pageContext.request.contextPath}/js/shared.js"></script>
<script src="${pageContext.request.contextPath}/js/admin.js"></script>
<span id="outerSpan_adminFrame" class="hiddenFullPage"></span>
<div class="navMenu">
<span class="adminTitle"><h1>Admin</h1></span>
<ul class="menuAdmin">
<li id="adminMenuAccount" class="navMenuOption"><a href="${pageContext.request.contextPath}/admin/account" class="menuLink">Accounts</a></li>
<li id="adminMenuCharacters" class="navMenuOption"><a href="${pageContext.request.contextPath}/admin/character" class="menuLink">Characters</a></li>
<li id="adminMenuEquipment" class="navMenuOption"><a href="${pageContext.request.contextPath}/admin/equipment" class="menuLink">Equipment</a></li>
<li id="adminMenuBoostItem" class="navMenuOption"><a href="${pageContext.request.contextPath}/admin/boostitem" class="menuLink">Boost Items</a></li>
<li id="adminMenuDungeons" class="navMenuOption"><a href="${pageContext.request.contextPath}/admin/dungeon" class="menuLink">Dungeons</a></li>
<li id="adminMenuCurrencyAudit" class="navMenuOption"><a href="${pageContext.request.contextPath}/admin/account/currencyaudit" class="menuLink">Currency Audit</a></li>
<li id="adminMenuTestDungeon" class="navMenuOption"><a href="${pageContext.request.contextPath}/admin/dungeon/test" class="menuLink">Test Dungeon</a></li>
<li id="adminMenuMessageAll" class="navMenuOption"><a href="${pageContext.request.contextPath}/admin/messageAll" class="menuLink">Message</a></li>
<li id="adminMenuLogout" class="navMenuOption"><a href="${pageContext.request.contextPath}/logout" class="menuLink">Logout</a></li>
</ul>
<ul>
<li id="adminMyAccount" class="navMenuOption"><a href="${pageContext.request.contextPath}/play" class="menuLink">My Account</a></li>
</ul>
</div>
<c:if test="${!empty error}">
<div class="error">${error}</div>
</c:if>
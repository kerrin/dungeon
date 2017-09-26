<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<span class="dungeonpediaTitle"><h1>Dungeon-Pedia</h1></span>
<span class="dungeonpediaMenu">
<span id="menuAccount" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/account');">Account</a></span>
<span id="menuCharacters" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/character');">Characters</a></span>
<span id="menuDungeons" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/dungeon');">Dungeons</a></span>
<span id="menuEquipment" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/equipment');">Items</a></span>
<span id="menuSpells" class="menuOption"><a href="javascript: loadSpan('dungeonpediaContentInner','${pageContext.request.contextPath}/dungeonpedia/spells');">Spells</a></span>
</span>
<span id="dungeonpediaCloseFrame" class="dungeonpediaCloseFrame">
<a href="javascript: closeDungeonPedia();" style="color: red;">X</a>
</span>
<span class="dungeonpediaEndBar">
<hr style="margin: 0px;" />
</span>
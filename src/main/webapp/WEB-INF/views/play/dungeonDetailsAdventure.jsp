<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="equipmentTagLib" prefix="equip"%>
<%@ taglib uri="characterTagLib" prefix="character"%>
<%@ taglib uri="monsterTagLib" prefix="monster"%>
<%@ taglib uri="dungeonEventsTagLib" prefix="dungeonEvents"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Adventure</title>
<style>
</style>
<script>$("#outerSpan_dungeonsFrame").empty();</script>
</head>
<body>
	<%@ include file="../checkError.jsp" %>
	<h1 style="float: left;"><a href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon?hardcore=${hardcore}&ironborn=${ironborn}');">Dungeons</a>: Run Adventure</h1>
	<span style="float:right">
	<c:if test="${!empty magicFindBoostExpires}">
		<span class="magicFindBoostIcon" data-qtiptitle="Magic find on closed dungeons is doubled until this runs out.">&nbsp;</span>
	</c:if>
	<c:if test="${!empty xpBoostExpires}">
 		<span class="xpBoostIcon" data-qtiptitle="Experience is doubled on finishing dungeons until this runs out.">&nbsp;</span>
	</c:if>
	</span>
	<br style="clear: both;" />
	<form:form name="dungeonAdventureStartForm" method="POST" 
			action="${pageContext.request.contextPath}/play/dungeon/createAdventure?hardcore=${hardcore}&ironborn=${ironborn}" 
			modelAttribute="dungeonAdventureStartForm"  
	        onsubmit="document.dungeonAdventureStartForm.panelCharacterId.value=charId; return true;">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	<input type="hidden" name="panelCharacterId" value="-1"/>
	<table style="float: left">
			<tr>
	        	<td colspan="2">
	        	Level: Lowest character level in party 	        	
	        	</td>
	        </tr>
	   		<tr>
	        	<td colspan="2">
	        		XP Reward: 1/2 a level of Xp for level of dungeon for each party member,<br />
	        		Plus health left from any characters that manage to kill all monsters
	        	</td>	        	
	        </tr>
	        <tr>
	        	<td colspan="2">
	        		Items: At least 1 random item for each party member
	        	</td>	        	
	        </tr>	        
	   		<tr>
	        	<td colspan="2"><b>Party</b></td>
	        </tr>
	        <tr>
	        	<td colspan="2">
	        	<c:forEach begin="1" end="${dungeonType.minCharacters}" varStatus="loop">
	        	<span id="partyChar${loop.index}" ondrop="dropCharacter(event,'dungeonsFrame')" ondragover="allowDrop(event)">
	        		<span class="charDrop requiredParty" id="name${loop.index}">Party Member ${loop.index}</span>
	        		<input name="characterIds" type="hidden" value="-1">
	        	</span>
	        	</c:forEach>
	        	<c:forEach begin="${dungeonType.minCharacters + 1}" end="${dungeonType.maxCharacters}" varStatus="loop">
	        	<span id="partyChar${loop.index}" ondrop="dropCharacter(event,'dungeonsFrame')" ondragover="allowDrop(event)">
	        		<span class="charDrop optionalParty" id="name${loop.index}">Party Member ${loop.index}</span>
	        		<input name="characterIds" type="hidden" value="-1">
	        	</span>
	        	</c:forEach>
	        	</td>
	        </tr>
	        <tr>
	        	<td colspan="2">Monsters: Randomly created when started</td>
	        </tr>
	        <tr>
	        	<td colspan="2">
	        		<input type="submit" name="Start" value="Start" />
	        	</td>
	        </tr>
	</table>
	</form:form>
    <br style="clear:both;" />
    <br />
    <a href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon?hardcore=${hardcore}&ironborn=${ironborn}');">List Dungeons</a>
</body>
</html>

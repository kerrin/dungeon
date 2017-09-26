<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Monsters</title>
  <script src="${pageContext.request.contextPath}/js/shared.js"></script>
  <script>
	$(function() {
		$( "#dungeonSubMenuDungeons" ).removeClass("menuOptionSelected");
		$( "#dungeonSubMenuMonsters" ).addClass("menuOptionSelected");
		loadSpan("monsterDetails", "${pageContext.request.contextPath}/dungeonpedia/dungeon/monsters/1/monsterDetails/1");
	});
	
	function updateSelectedMonster(selectedId) {
		var e = document.getElementById("level");
		var level = e.options[e.selectedIndex].value;
		loadSpan("monsterDetails", "${pageContext.request.contextPath}/dungeonpedia/dungeon/monsters/"+selectedId+"/monsterDetails/"+level);
	}
	
	function updateSelectedLevel(selectedId) {
		var e = document.getElementById("monster");
		var monsterId = e.options[e.selectedIndex].value;
		loadSpan("monsterDetails", "${pageContext.request.contextPath}/dungeonpedia/dungeon/monsters/"+monsterId+"/monsterDetails/"+selectedId);
	}
	</script>
</head>
<body>
	<%@ include file="../../checkError.jsp" %>
	<h1>Monsters</h1>
	<p>
	Monsters and characters have different attributes:
	<ul>
	<li>Health - Damaging them reduces this, they get to 0, they die.</li>
	<li>Mana (resource used to cast spells and summon minions)</li>
	<li>Damage they do when they hit you</li>
	<li>Spells they can cast (higher level monsters have more powerful spells they can cast)</li>
	<li>Other minions they can summon (only a few can do this)</li>
	</ul>
	The health, damage and damage from spells are all multiplied by the dungeon level. Health increases at 120% of the level.<br />
	<br />
	A level 1 character should be just as strong against a level 1 monster as a level 50 character is against a level 50 monster of the same type (ignoring the extra spells the monster could cast.)<br />
	<br />
	Monsters also have another modifier:
	<ul>
	<li>Trash - Standard Monster</li>
	<li>Elite - Double Health</li>
	<li>Boss - Triple Health</li>
	</ul>
	Note: All monster health increases by 75% for each party member you add after the first one, so monsters in a dungeon with 2 characters in will have 175% of the health they would have against 1 character.<br />
	<br />
	The level of the dungeon determines what level monsters it has in, and what level rewards you get.<br />
	Monsters also have strengths and weaknesses. A strength means they take less damage or do more, and a weakness means they do less damage and take more.<br />
	Below you can look at specific monsters and specific levels to see all the attributes they have.
	</p>
	<span id="monsterSelect">
	<form:form name="monsterForm" action="#" method="POST" modelAttribute="monsterForm">
		<label for="monster">Monster</label>
		<form:select path="monster" onChange="javascript: updateSelectedMonster(this.value);">
 			<form:options items="${monsters}" itemLabel="niceName" itemValue="id" />
		</form:select>
		<label for="level">Level</label>
		<form:select path="level" onChange="javascript: updateSelectedLevel(this.value);">
 			<c:forEach var='level' begin="1" end="${maxLevel}">
		        <form:option value='${level}' label='${level}' />
		    </c:forEach>
		</form:select>
	</form:form>	
	</span>
	<span id="monsterDetails">	
	</span>
</body>
</html>

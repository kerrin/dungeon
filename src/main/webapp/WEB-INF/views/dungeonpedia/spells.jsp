<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Spells</title>
  <script>
	$(function() {
		$( "#menuAccount" ).removeClass("menuOptionSelected");
		$( "#menuCharacters" ).removeClass("menuOptionSelected");
		$( "#menuDungeons" ).removeClass("menuOptionSelected");
		$( "#menuEquipment" ).removeClass("menuOptionSelected");
		$( "#menuSpells" ).addClass("menuOptionSelected");
		loadSpan("spellDetails", "${pageContext.request.contextPath}/dungeonpedia/spells/1");
	});
	
	function updateSelectedSpell(selectedId) {
		loadSpan("spellDetails", "${pageContext.request.contextPath}/dungeonpedia/spells/"+selectedId);
	}
	</script>
</head>
<body>
	<%@ include file="../checkError.jsp" %>
	<h1>Spells</h1>
	<p>
	Spells have the following attributes:
	<ul>
	<li>Mana - How much mana is used to cast the spell</li>
	<li>Cooldown - How long before the spell can be recast</li>
	<li>Minimum Level - The level a character or monster must be to be able to cast the spell</li>
	<li>Damage Type - The type of damage the spell does (see below)</li>
	<li>Effect Types - Any additional effects the spell does (see below)</li>
	</ul>
	<br />
	Damage types:
	<ul>
	<li>Melee - Standard damage - Armour reduces damage taken</li>
	<li>Piercing - Not reduced by armour</li>
	<li>Cold - Reduces the targets attack speed for a short while</li>
	<li>Fire</li>
	<li>Electric - Damage increased by armour</li>
	<li>Poison - Damage occurs over time, rather than all at once. Poison damage stacks.</li>
	<li>Water</li>
	<li>Acid - Temporally reduces armour</li>
	<li>Holy - Mostly used by healing spells</li>
	<li>Stun - Target has a chance to not be able to attack back for a short while</li>
	</ul>
	All damage types have equipment attributes that can resist the damage. Monsters may also be strong or weak against specific types of damage.
	<br />
	Effect Types
	<ul>
	<li>Range - Reduces chance of being hit back</li>
	<li>Splash Damage - Damages all other enemies to caster</li>
	<li>Bleed Damage - Takes damage over time</li>
	<li>Damage Over Time - Takes damage over time</li>
	<li>Stun Chance - Has a chance to make target stop attacking for a while</li>
	<li>Heal - Increases the targets health (can never increase above maximum health)</li>
	<li>Run Speed - Increases the speed of the characters attacks</li>
	<li>Resist All - Reduces all incoming damage</li>
	<li>Health Regen - Increases health over time</li>
	<li>Mana Regen - Increases mana over time</li>
	</ul>
	<br />
	Below you can look at specific spells attributes.
	</p>
	<span id="spellSelect">
	<form:form name="spellForm" action="#" method="POST" modelAttribute="spellForm">
		<label for="spell">Spell</label>
		<form:select path="spell" onChange="javascript: updateSelectedSpell(this.value);">
 			<form:options items="${spells}" itemLabel="niceName" itemValue="id" />
		</form:select>
	</form:form>	
	</span>
	<span id="spellDetails" class="dungeonpediaSpellDetails">	
	</span>
</body>
</html>

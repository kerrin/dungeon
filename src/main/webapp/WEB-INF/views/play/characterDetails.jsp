<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="equipmentTagLib" prefix="equip"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Character Equipment</title>
<script>
loadSpan('characterDetailsSummaryFrame','${pageContext.request.contextPath}/play/character/${character.id}/summary');
charId=${character.id};
</script>
</head>
<body>
	<%@ include file="../checkError.jsp" %>
	<h1 style="float: left;"><img class="classIcon${character.charClass.name}25" data-qtiptitle="Main Attribute: ${character.charClass.mainAttribute.niceName}. Defensive Attribute: ${character.charClass.defenceAttribute.niceName}" alt="${character.charClass.niceName}" src="${pageContext.request.contextPath}/images/1x1.png"/> ${character.name}</h1>
	<span style="float: right;">
	<c:if test="${!empty usableRenameBoost}">
	<a href="javascript: loadSpan('characterDetailsFrame','${pageContext.request.contextPath}/play/character/${character.id}/rename/${usableRenameBoost.id}');" title="Rename this character">(${usableRenameBoost.level} <span class="changeNameBoostIcon">&nbsp;</span>)</a>
	</c:if>
	</span>
	<br style="clear: both;"/>
	<span>
		<label>Level: </label>${character.level}
	</span>
	<c:if test="${character.level < 60}">
	<span class="chararacterSummaryXpBar"><img src='${pageContext.request.contextPath}/images/1x1.png' alt='Level' data-qtiptitle='Level' class='xpBar${character.xpClass}DoubleWidth' /></span>
	<span class="chararacterSummaryXpText">${character.xp} / ${character.xpNextLevel}</span>
	<c:if test="${empty character.dungeon && !character.currentlyDead && !character.ironborn}">
		<span class="afterLevel">
			Level Up 
			<a href="${pageContext.request.contextPath}/play/character/${character.id}/levelUp" title="Use Dungeon Tokens to level up this character">(${character.level+1} <img src="${pageContext.request.contextPath}/images/token.png" alt="Tokens" title="Dungeon Tokens" class="dungeonToken" />)</a>
			<c:if test="${!empty usableLevelBoost}">
			<a href="${pageContext.request.contextPath}/play/character/${character.id}/levelUp/${usableLevelBoost.id}" title="Use Level Up boost item to level up this character">(${usableLevelBoost.level} <span class="levelUpBoostIcon">&nbsp;</span>)</a>
			</c:if>
		</span>
	</c:if>
	</c:if>
	<c:if test="${empty character.dungeon && character.level >= 10 && !character.currentlyDead}">
	<span class="prestigeReset">
		<a href="${pageContext.request.contextPath}/play/character/${character.id}/prestige" 
			onclick="return confirm('Are you sure you want to reset ${character.name} to level 1 for prestige, all their equipment will be set to level 1 too?');" 
			title="Reset Character and Equipment to Level 1 for ${character.resetPrestige} Prestige Levels">
				Reset Level for ${character.resetPrestige} Prestige
		</a>
	</span>
	<c:if test="${character.prestigeLevel > 0}">
	<br />
	</c:if>
	</c:if>
	<c:if test="${character.prestigeLevel > 0}">
	<span class="prestigeLevel">
		<label>Prestige Level: </label>${character.prestigeLevel}
	</span>
	</c:if>
	<c:if test="${!empty character.dungeon}">
		<c:if test="${character.prestigeLevel > 0}">
		<br />
		</c:if>
		<span class="afterLevel">
			<label>Running: </label><a href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon/${character.dungeon.id}');">Dungeon</a>
		</span>
		<br />
	</c:if>
	<c:if test="${character.currentlyDead}">
		<span class="dead">
			<label data-qtiptitle="Died To ${character.diedTo}">Dead: </label>
			<c:if test="${character.hardcore}">
			<a href="${pageContext.request.contextPath}/play/character/${character.id}/delete" 
				title="This will permanently delete this character and any items they are wearing!"
				onclick="return confirm('Are you sure you want to delete ${character.name}, all their equipment will be deleted too?');" 
				>Delete</a>
			</c:if>
			<c:if test="${!character.hardcore}">
			<c:if test="${!empty character.dungeon}">
			Close Dungeon To Resurrect
			</c:if>
			<c:if test="${empty character.dungeon}">
			Resurrect
			<a href="${pageContext.request.contextPath}/play/character/${character.id}/resurrect" title="Use Dungeon Tokens to resurrect this character">(${character.level} <img src="${pageContext.request.contextPath}/images/token.png" alt="Tokens" title="Dungeon Tokens" class="dungeonToken" />)</a>
			<c:if test="${!empty usableResurrectBoost}">
			<a href="${pageContext.request.contextPath}/play/character/${character.id}/resurrect/${usableResurrectBoost.id}" title="Use Resurrect boost item to resurrect this character">(${usableResurrectBoost.level} <span class="resurrectBoostIcon">&nbsp;</span>)</a>
			</c:if>
			 
			</c:if>
			</c:if>
		</span>
		<br />
	</c:if>
	<table>
	    <tr>
	    	<td>
	    		<equip:equipmentDisplay 
	    			equipment="${characterEquipment['SHOULDERS']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}" 
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}" 
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="2"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
		    		/>
		    	<span class="charSlotIconLargeSHOULDERS"></span>	    		
	    	</td>
	    	<td>
	    		<equip:equipmentDisplay 
	    			equipment="${characterEquipment['HEAD']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}" 
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="1"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
		    		/>
		    	<span class="charSlotIconLargeHEAD"></span>
	    	</td>
	    	<td>&nbsp;</td>
	    </tr>
	    <tr>
	    	<td>&nbsp;</td>
	    	<td>
	    		<equip:equipmentDisplay 
	    			equipment="${characterEquipment['AMULET']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}"  
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="11"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
		    		/>
	    		<span class="charSlotIconLargeAMULET"></span>
	    	</td>
	    	<td>
	    		<equip:equipmentDisplay 
	    			equipment="${characterEquipment['BROACH']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}"  
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="13"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
			        leftOffset="30"
		    		/>
	    		<span class="charSlotIconLargeBROACH"></span>
	    	</td>
	    </tr>
	    <tr>
	    	<td>
	    		<equip:equipmentDisplay 
	    			equipment="${characterEquipment['RING_LEFT']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}"  
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="9"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
		    		/>
	    		<span class="charSlotIconLargeRING_LEFT"></span>
	    	</td>
	    	<td>
	    		<equip:equipmentDisplay 
	    			equipment="${characterEquipment['CHEST']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}"  
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="3"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
		    		/>
	    		<span class="charSlotIconLargeCHEST"></span>
	    	</td>
	    	<td>
	    		<equip:equipmentDisplay 
	    			equipment="${characterEquipment['RING_RIGHT']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}"  
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="10"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
			        leftOffset="30"
		    		/>
	    		<span class="charSlotIconLargeRING_RIGHT"></span>
	    	</td>
	    </tr>	    
	    <tr>
	    	<td>
	    		<equip:equipmentDisplay 
	    			equipment="${characterEquipment['BRACERS']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}"  
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="12"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
		    		/>
	    		<span class="charSlotIconLargeBRACERS"></span>
	    	</td>
	    	<td>
	    		<equip:equipmentDisplay equipment="${characterEquipment['LEGS']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}"  
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="4"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
		    		/>
	    		<span class="charSlotIconLargeLEGS"></span>
	    	</td>
	    	<td>
	    		<equip:equipmentDisplay equipment="${characterEquipment['HANDS']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}"  
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="6"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
			        leftOffset="30"
		    		/>
	    		<span class="charSlotIconLargeHANDS"></span>
	    	</td>
	    </tr>    
	    <tr>
	    	<td>
	    		<equip:equipmentDisplay 
	    			equipment="${characterEquipment['MAIN_WEAPON']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}"  
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="7"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
		    		/>
	    		<span class="charSlotIconLargeMAIN_WEAPON"></span>
	    	</td>
	    	<td>
	    		<equip:equipmentDisplay 
	    			equipment="${characterEquipment['FEET']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}"  
			        dragableSrcFrame="characterDetailsFrame"
			        dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="5"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
		    		/>
	    		<span class="charSlotIconLargeFEET"></span>
	    	</td>
	    	<td>
	    		<equip:equipmentDisplay 
	    			equipment="${characterEquipment['OFF_HAND']}" 
	    			dragable="${empty character.dungeon || character.dungeon.finished}"  
			        dragableSrcFrame="characterDetailsFrame"
	    			dropable="${empty character.dungeon || character.dungeon.finished}"  
			        comparable="${accountConfigEquipmentCompare.value > 0}"
			        charSlotId="8"
        			accountApiKey="${account.apiKey}"
        			characterId="${character.id}"
			        isTouchScreen="${account.touchScreen}"
			        character="${character}"
			        leftOffset="30"
		    		/>
	    		<span class="charSlotIconLargeOFF_HAND"></span>
	    	</td>
	    </tr>	        
    </table>
</body>
<script>
removeZombiedQtips();
</script>
</html>

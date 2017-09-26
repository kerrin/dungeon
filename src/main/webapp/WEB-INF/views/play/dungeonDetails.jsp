<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="equipmentTagLib" prefix="equip"%>
<%@ taglib uri="boostItemTagLib" prefix="boost"%>
<%@ taglib uri="characterTagLib" prefix="character"%>
<%@ taglib uri="monsterTagLib" prefix="monster"%>
<%@ taglib uri="dungeonEventsTagLib" prefix="dungeonEvents"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Dungeon Details</title>
<style>
</style>
<script>
$("#outerSpan_dungeonsFrame").empty();
dungeonId=${dungeon.id};
if(isTouchDevice()) {
	// Move away from edge to stop iOS from thinking we are trying to go back when we drag stuff
	var dd = document.getElementById("dungeonDetails");
	dd.style.left = 50 + "px";
	dd.style.position = "absolute";
}
reloadedCharacter = false;
</script>
</head>
<body>
	<%@ include file="../checkError.jsp" %>
	<span id="dungeonDetails" class="dungeonDetails">
	<h1 style="float: left;"><a href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon?hardcore=${hardcore}&ironborn=${ironborn}');">Dungeons</a>: ${dungeon.type.niceName} Details</h1>
	<span style="float:right">
	<c:if test="${!empty magicFindBoostExpires}">
		<span class="magicFindBoostIcon" data-qtiptitle="Magic find on closed dungeons is doubled until this runs out.">&nbsp;</span>
	</c:if>
	<c:if test="${!empty xpBoostExpires}">
 		<span class="xpBoostIcon" data-qtiptitle="Experience is doubled on finishing dungeons until this runs out.">&nbsp;</span>
	</c:if>
	</span>
	<br style="clear: both;" />
	<form:form name="dungeonForm" method="POST" 
			action="${pageContext.request.contextPath}/play/dungeon/${dungeon.id}/start" 
			modelAttribute="dungeon"  
	        onsubmit="document.dungeonForm.panelCharacterId.value=charId; return true;">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<input type="hidden" name="panelCharacterId" value="-1"/>
		<form:input path="id" type="hidden" />	        
		<table style="float: left; max-width: 99%;">
			<tr>
	        	<td colspan="2">
		        	<c:if test="${dungeon.finished}">
		        	<div class="link" id="lb_link_logs">View Action</div>
		        	<script lang="javascript">registerShowDiv('lb_link_logs', 'lb_logs',{});</script>
		        	<dungeonEvents:dungeonEventsDisplay 
		        			dungeon="${dungeon}" 
		        			dungeonEvents="${dungeonEvents}"
		        			parentSpan="dungeonsFrame"
		        			/>
		        	</c:if>
	        	</td>
	        	<td colspan="2"> 
		        	<span style="float: right;" data-qtiptitle="An estimate of how difficult this dungeon is for a party of characters at the shown level or level 60 if shown level is higher">
		        		Difficulty:
		        		<c:if test="${empty dungeon.started}">
		        		${dungeon.getDifficulty(0).difficultyScore} Level:
		        		<select name="difficultyModifier" data-qtiptitle="Changing the level of the dungeon will affect the Xp and level of rewarded items too">
		        		<!-- Loop from 0 to 8 then reduce the loop counter by 4 to get -4 to +4 -->
		        		<c:forEach begin="0" end="8" varStatus="loop">	        		 
		        			<c:set var="difficulty" value="${dungeon.getDifficulty(loop.index-4)}" />
		        			<c:if test="${!empty difficulty}">
		        			<option value="${difficulty.difficultyModifier}"
		        			<c:if test="${loop.index == 4}">selected="selected"</c:if>
		        			>
			        			${dungeon.level + difficulty.levelAdjustment}
			        			<c:if test="${difficulty.cost > 0}"> Cost: ${difficulty.cost}D</c:if>
		        			</option>
		        			</c:if>
		        		</c:forEach>
		        		</select>
		        		</c:if>
		        		<c:if test="${!empty dungeon.started}">
		        		<c:set var="difficulty" value="${dungeon.getDifficulty(0)}" />
		        			<c:if test="${!empty difficulty}">
		        				${difficulty.difficultyScore} Level ${dungeon.level}
			        		</c:if>
		        		</c:if>
		        	</span>
	        	</td>
	        </tr>
	   		<tr>
	        	<td style="text-align: right">XP Reward&nbsp;</td>
	        	<td>
	        		<c:if test="${!dungeon.failed || !dungeon.finished}">
	        		${dungeon.xpReward}
	        		<c:if test="${dungeon.finished && dungeon.killedXp > 0}">
	        		plus ${dungeon.killedXp} from monster kills
	        		</c:if>
	        		</c:if>
	        		<c:if test="${dungeon.finished && dungeon.failed}">
	        		<font style="text-decoration: line-through; color: red;">${dungeon.xpReward}</font>
	        		<c:if test="${dungeon.killedXp > 0}">
	        		plus ${dungeon.killedXp} bonus XP
	        		</c:if>
	        		</c:if>
	        	</td>
	        	<td style="text-align: right">Tokens Reward&nbsp;</td>
	        	<td>
	        		<c:if test="${!dungeon.failed || !dungeon.finished}">
	        		${dungeon.rewardTokens}
	        		</c:if>
	        		<c:if test="${dungeon.finished && dungeon.failed}">
	        		<font style="text-decoration: line-through; color: red;">${dungeon.rewardTokens}</font>
	        		</c:if>
	        	</td>
	        </tr>	        
	        <c:if test="${!empty dungeon.doneDate && !dungeon.finished}">
	   		<tr>
	   			<td style="text-align: right">Finishes&nbsp;</td>
	   			<td colspan="3">
	   			<fmt:formatDate value="${dungeon.doneDate}" var="formattedDate" type="date" pattern="EEE MMM dd HH:mm:ss zzz yyyy" timeZone="GMT" />
	   			<script lang="javascript">initializeClock('finishDiv', '${formattedDate}', 'dungeonsFrame', '${pageContext.request.contextPath}/play/dungeon/${dungeon.id}');</script>
	   			<div id="finishDiv">
	   				<span class="hours"></span>h
					<span class="minutes"></span>m
					<span class="seconds"></span>s
					&nbsp;
					Rush 
					<a href="javascript: if(confirm('Are you sure you want to rush this dungeon for dungeon tokens?')) { loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon/${dungeon.id}/rush'); loadSpan('summaryFrame','${pageContext.request.contextPath}/play/summary');}" title="Use Dungeon Tokens to finish the dungeon">(<span class="minutesPlusOne">${dungeon.rushCost}</span> <img src="${pageContext.request.contextPath}/images/token.png" alt="Tokens" title="Dungeon Tokens" class="dungeonToken" />)</a>
					<c:if test="${!empty usableRushBoost}">
						<a href="javascript: if(confirm('Are you sure you want to rush this dungeon for a boost item?')) { loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon/${dungeon.id}/rush/boostItem'); loadSpan('summaryFrame','${pageContext.request.contextPath}/play/summary');}" title="Use a Dungeon Speed Boost Item to reduce length of time left on dungeon.">(${usableRushBoost.level} <span class="dungeonSpeedBoostIcon">&nbsp;</span>)</a>
					</c:if>
	   			</div>
	   			</td>
            </tr>
            </c:if>
            <c:if test="${!empty dungeon.expires}">
	   		<tr>
	   			<td>Expires</td>
	   			<td>
	   			<fmt:formatDate value="${dungeon.expires}" var="formattedDate" type="date" pattern="EEE MMM dd HH:mm:ss zzz yyyy" timeZone="GMT" />
	   			<script lang="javascript">initializeClock('expiredDiv', '${formattedDate}', 'dungeonsFrame', '${pageContext.request.contextPath}/play/dungeon');</script>
	   			<div id="expiredDiv">
	   				<span class="hours"></span>h
					<span class="minutes"></span>m
					<span class="seconds"></span>s
	   			</div>
	   			</td>
	   			<td colspan="2">&nbsp;</td>
	   		</tr>
	   		</c:if>
	        <c:if test="${closable}">
	        <tr>
	        	<td colspan="4">
	        		<a href="javascript: reloadPage('${pageContext.request.contextPath}/play/dungeon/${dungeon.id}/close?hardcore=${hardcore}&ironborn=${ironborn}');">
	        		<c:if test="${!dungeon.failed}">
	        		Close Dungeon and Claim ${dungeon.xpReward + dungeon.killedXp} XP<br />Plus ${dungeon.level} <img src="${pageContext.request.contextPath}/images/token.png" alt="Tokens" title="Dungeon Tokens" class="dungeonToken" />
	        		</c:if>
	        		<c:if test="${dungeon.failed}">
	        		Close Failed Dungeon and Claim ${dungeon.killedXp} XP
	        		</c:if>
	        		</a>
	        	</td>
	        </tr>
	        </c:if>
	   		<tr>
	        	<td colspan="4"><b>Party</b></td>
	        </tr>
	        <tr>
	        	<td colspan="4">
	        	<c:if test="${!empty dungeon.started}">
	        	<c:forEach items="${dungeon.characters}" var="character">
	        		<character:characterDisplay 
			          	character="${character}"
				        dragable="false" 
				        linkDestFrame="characterDetailsFrame" 
	        			linkDestUrl="${pageContext.request.contextPath}/play/character/${character.id}"
			        	isTouchScreen="${account.touchScreen}"
	        			/>
	        		<c:if test="${!fullPage}">
	        		<script>
	        		if(charId == '${character.id}') {
	        			// Make sure the character is up to date, incase they just got added to this dungeon,
	        			// or this dungeon just finished so now equipment is changable
	        			reloadCharacterDetailsPanel('${pageContext.request.contextPath}');
	        			reloadedCharacter = true;
	        		}
	        		</script>
	        		</c:if>
	        	</c:forEach>
				</c:if>
				<c:if test="${!empty dungeon.expires}">
	        	<c:forEach begin="1" end="${dungeon.type.minCharacters}" varStatus="loop">
	        	<span id="partyChar${loop.index}" ondrop="dropCharacter(event,'dungeonsFrame')" ondragover="allowDrop(event)">
	        		<span class="charDrop requiredParty" id="name${loop.index}">Party Member ${loop.index}</span>
	        		<input name="characterIds" type="hidden" value="-1">
	        	</span>
	        	</c:forEach>
	        	<c:forEach begin="${dungeon.type.minCharacters + 1}" end="${dungeon.type.maxCharacters}" varStatus="loop">
	        	<span id="partyChar${loop.index}" ondrop="dropCharacter(event,'dungeonsFrame')" ondragover="allowDrop(event)">
	        		<span class="charDrop optionalParty" id="name${loop.index}">Party Member ${loop.index}</span>
	        		<input name="characterIds" type="hidden" value="-1">
	        	</span>
	        	</c:forEach>
	        	</c:if>
	        	</td>
	        </tr>
        	<c:if test="${empty dungeon.started}">
	        <tr>
	        	<td colspan="4"><input name="start" type="submit" value="Start"/></td>
	        </tr>
	        </c:if>
	        <tr>
	        	<td colspan="4"><b>Monsters</b></td>
	        </tr>
	        <tr>
	        	<td colspan="4">
		        <c:forEach items="${dungeon.monstersAsList}" var="monster" varStatus="loop">
	        		<monster:monsterDisplay 
	        			spanId="${monster.name}"
	        			monster="${monster}" 
	        			monsterType="${dungeon.monsters[monster]}" 
	        			level="${dungeon.level}"
	        			hoverAbove="true"
	        			/>
		        </c:forEach>
		        <c:forEach items="${dungeon.deadMonstersAsList}" var="monster" varStatus="loop">
	        		<monster:monsterDisplay 
	        			spanId="${monster.name}"
	        			monster="${monster}" 
	        			monsterType="${dungeon.deadMonsters[monster]}" 
	        			level="${dungeon.level}"
	        			dead="${dungeon.finished}"
	        			hoverAbove="true"
	        			/>
		        </c:forEach>
	        	</td>
        	</tr>
        	<c:if test="${fn:length(dungeon.itemRewardsAsSet) > 0 || fn:length(dungeon.boostItemRewards) > 0}">
	        <!-- Item Rewards -->
	        <tr>
	        	<td colspan="4"><b>Rewards</b></td>
	        </tr>
		    <tr>
	        	<td colspan="4">
		        	<c:forEach items="${dungeon.itemRewardsAsSet}" var="itemReward" varStatus="loop">
		        		<span style="float: left;">		        
			        		<equip:equipmentDisplay 
			        			found="${dungeon.itemRewards[itemReward]}"
								compare="${accountConfigEquipmentCompare.value > 0}" 
			        			equipment="${itemReward}" 
			        			dragable="${dungeon.finished && !dungeon.failed}" 
			        			dragableSrcFrame="dungeonsFrame"
			        			dropable="false"
			        			isTouchScreen="${account.touchScreen}"
	        					hoverAbove="${loop.index <= 1}"
			        			/>
			        		<c:if test="${accountConfigEquipmentCompare.value > 0}">
			        		<script>
				        		if(!reloadedCharacter && charId > 0) {
				        			setUpCompare('${itemReward.equipmentType.validSlotIds}','lb_charslot_', 'equipmentdungeonsFrame${itemReward.id}');
				        		}
			        		</script>
			        		</c:if>
			        	</span>
		        	</c:forEach>
		        	<c:forEach items="${dungeon.boostItemRewards}" var="boostItemReward" varStatus="loop">
		        		<span style="float: left;">		        
			        		<boost:boostItemDisplay 
			        			item="${boostItemReward}" 
			        			dragable="${dungeon.finished && !dungeon.failed}" 
			        			dragableSrcFrame="dungeonsFrame"
			        			dropable="false"
       							viewForm="hardcore=${hardcore}&ironborn=${ironborn}&noBreakout=false"
			        			/>
			        	</span>
		        	</c:forEach>
		        	<c:if test="${dungeon.finished && !dungeon.failed}">
		        	<br style="clear: both;"/>
		        	<span style="float: left;">
		        		<a href="javascript: reloadPage('${pageContext.request.contextPath}/play/dungeon/${dungeonId}/stashAll?hardcore=${hardcore}&ironborn=${ironborn}');">Stash All and Close</a>
		        	</span>
		        	<span style="float: left;">&nbsp;</span>
		        	<span style="float: right;">
		        		<a href="javascript: if(confirm('Are you sure you want to salvage all the remaining items?')){ reloadPage('${pageContext.request.contextPath}/play/dungeon/${dungeonId}/salvageAll?hardcore=${hardcore}&ironborn=${ironborn}', 'summaryFrame','${pageContext.request.contextPath}/play/summary?hardcore=${hardcore}&ironborn=${ironborn}'); }">Salvage All and Close</a>
		        	</span>
		        	<br style="clear: both;"/>
		        	</c:if>
	        	</td>
	        </tr>
	        </c:if>
    	</table>
	</form:form>
    <br style="clear:both;" />
    <br />
    <a href="javascript: loadSpan('dungeonsFrame','${pageContext.request.contextPath}/play/dungeon?hardcore=${hardcore}&ironborn=${ironborn}');">List Dungeons</a>
    </span>
    <c:if test="${!fullPage}">
    <script>
		if(!reloadedCharacter) {
			removeZombiedQtips();
		}
	</script>
	</c:if>
</body>
</html>

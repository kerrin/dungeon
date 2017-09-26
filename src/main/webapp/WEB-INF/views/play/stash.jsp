<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="equipmentTagLib" prefix="equip"%>
<%@ taglib uri="boostItemTagLib" prefix="boost"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Stash</title>
<style>
</style>
<script>
var setUpCompareParams = [];
var setUpCompareCount = 0;
</script>
</head>
<body> 
	<%@ include file="../checkError.jsp" %>
	<span>
		<span style="float: left;">
			<h3>Stash</h3>
		</span>
		<span style="float: left; width: 20px;">
			&nbsp;
		</span>
		<span style="float: left;">
			<a href="${pageContext.request.contextPath}/play/config/toggleEquipmentCompare?hardcore=${hardcore}&ironborn=${ironborn}"
				onclick="location.href=this.href+'&characterId='+charId+'&dungeonId='+dungeonId">
				<c:if test="${accountConfigEquipmentCompare.value > 0}"><font style="color: green;">Equipment Compare</font></c:if>
				<c:if test="${accountConfigEquipmentCompare.value <= 0}"><font style="text-decoration: line-through; color: red;">Equipment Compare</font></c:if>
			</a>
		</span>
		<span style="float: left; width: 20px;">
			&nbsp;
		</span>
		<span style="float: left;">
			<a href="${pageContext.request.contextPath}/play/config/toggleToolTips?hardcore=${hardcore}&ironborn=${ironborn}"
				onclick="location.href=this.href+'&characterId='+charId+'&dungeonId='+dungeonId">
				<c:if test="${accountConfigToolTips.value > 0}"><font style="color: green;">Tool Tips</font></c:if>
				<c:if test="${accountConfigToolTips.value <= 0}"><font style="text-decoration: line-through; color: red;">Tool Tips</font></c:if>
			</a>
		</span>
		<span style="float: right;">
			<a href="javascript: removeQtips('lb_equipmentstashFrame'); loadSpan('stashFrame', '${pageContext.request.contextPath}/play/stash/sort?hardcore=${hardcore}&ironborn=${ironborn}"
				onclick="location.href=this.href+'&characterId='+charId+'&dungeonId='+dungeonId+'\');';return false;">Sort Stash</a>
		</span>
	</span>
	<br style="clear: both;"/>
	<table border="1">
	<c:forEach begin="0" end="${inventory.size-1}" varStatus="loop">
	<span id="stashSlot${loop.index}">
	<c:if test="${inventory.inventorySlots.containsKey(loop.index)}">
		<c:if test="${inventory.inventorySlots[loop.index].equipment}">
		<!-- Note, reloading the stash panel will cause duplicate ids for this equipment -->
		<equip:equipmentDisplay equipment="${inventory.inventorySlots[loop.index]}"
								compare="${accountConfigEquipmentCompare.value > 0}" 
			        			dragable="true" 
			        			dragableSrcFrame="stashFrame"
			        			dropable="true" 
			        			stashId="${loop.index}"
			        			hoverAbove="true"
			        			accountApiKey="${account.apiKey}"
			        			isTouchScreen="${account.touchScreen}"
			        			/>
		<c:if test="${accountConfigEquipmentCompare.value > 0}">
		<script>
       		if(charId > 0) {
      			debug("Setting up compares for stash slot ${loop.index}");
       			setUpCompareParams[setUpCompareCount++] = ['${inventory.inventorySlots[loop.index].equipmentType.validSlotIds}','lb_charslot_', 'equipmentstashFrame${inventory.inventorySlots[loop.index].id}'];       			
       		}
      	</script>
      	</c:if>
      	</c:if>
      	<c:if test="${!inventory.inventorySlots[loop.index].equipment}">
      	<boost:boostItemDisplay 
       			item="${inventory.inventorySlots[loop.index]}" 
       			dragable="true" 
       			dragableSrcFrame="stashFrame"
       			dropable="true"
       			stashId="${loop.index}"
       			accountApiKey="${account.apiKey}"
       			viewForm="hardcore=${hardcore}&ironborn=${ironborn}&noBreakout=false"
       			/>
      	</c:if>
	</c:if>
	<c:if test="${!inventory.inventorySlots.containsKey(loop.index)}">
		<span id="emptyStash${loop.index}" class="itemDrop" 
			ondrop="dropItemStash(event,'${pageContext.request.contextPath}/api/account/${account.apiKey}/inventory/${loop.index}', '<c:if test="${accountConfigEquipmentCompare.value > 0}">true</c:if><c:if test="${accountConfigEquipmentCompare.value <= 0}">false</c:if>')" 
				ondragover="allowDrop(event)" ></span>
		<script>
			$('#emptyStash${loop.index}').qtip({
					show: { <c:if test="${account.touchScreen}">event: 'touchend', </c:if>solo: true }, 
					hide: { event: 'unfocus', distance: 100 },
					position: { adjust: { x: -90, y: -210 } },
					<c:if test="${accountConfigEquipmentCompare.value > 0}">prerender: true,</c:if>
					content: {
						text: "<span style='pointer-events: all; float: left;' class='charEquipment_charslot_none' id='lb_emptyStash${loop.index}'></span><span class='charslot_none${loop.index}' id='lb_charslot_emptyStash${loop.index}' style='float: left;'></span>"
					}
				}, event);
		</script>
	</c:if>
	</span>
	</c:forEach>
	<span class="itemLarge">
		<a href="javascript: if(confirm('Are you sure you want to purchase another stash slot for ${inventory.size * inventory.size} dungeon tokens?')) { reloadPage('${pageContext.request.contextPath}/play/stash/purchase?hardcore=${hardcore}&ironborn=${ironborn}&noBreakout=false'); }">
			Purchase another slot for ${inventory.size * inventory.size} <img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" title="Dungeon Tokens" class="dungeonToken" />
		</a>
	</span>
    </table>
    <c:if test="${accountConfigEquipmentCompare.value > 0}">
	<script>
		setTimeout(function() {
  			for(i=0; i < setUpCompareCount; i++) {
      			setUpCompare(setUpCompareParams[i][0],setUpCompareParams[i][1],setUpCompareParams[i][2]);
      		}
      	}, 1000); // wait 1000 ms
     </script>
     </c:if>
</body>
</html>

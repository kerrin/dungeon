<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="characterTagLib" prefix="character"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Characters</title>
  <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.qtip.min.js"></script>
  <script type="text/javascript">
  function changeClassFilter(selected) {
	  if(selected.checked) {
		  showDivByClass("CharClass"+selected.value);
	  } else {
		  hideDivByClass("CharClass"+selected.value);
	  }
  }
  function showEquipment() {
	  $(".characterNeedEquipment").css("display", "inline-block");
	  $("#toggleEquipmentWarnings").html("<a href=\"javascript: hideEquipment()\">Hide Equipment</a>");
  }

  function hideEquipment() {
	  $(".characterNeedEquipment").hide();
	  $("#toggleEquipmentWarnings").html("<a href=\"javascript: showEquipment()\">Show Equipment</a>");
  }
  </script>
</head>
<body>
	<%@ include file="../checkError.jsp" %>
	<span class="runAdventures">
		<a href="${pageContext.request.contextPath}/play/dungeon/allAdventure?hardcore=${hardcore}&ironborn=${ironborn}"
			onclick="location.href=this.href+'&charId='+charId+'&dungeonId='+dungeonId; return false;">
			Run Adventures
		</a>
	</span>
	<span style="float: left; width: 5px; height: 20px; margin: 0px;">&nbsp;</span>
	<span id="toggleEquipmentWarnings" class="toggleEquipmentWarnings">
		<a href="javascript: showEquipment()">Show Equipment</a>
	</span>
	<span class="charactersClassFilter">
		<span class="charClassSelector25">
       		<input id="charClassSelectorId1" class="hidden" type="checkbox" name="charClassId" value="MELEE" checked="checked" onchange="javascript: changeClassFilter(this);" />
	      	<label for="charClassSelectorId1" style="text-align: center; display: block; width: 25px;">
		    	<img id="charClassSelectorId1Img" src="${pageContext.request.contextPath}/images/1x1.png" 
		    		alt="Barbarian" data-qtiptitle="Barbarian - Strength and Armour" class="classIconMELEE25" />
			    <script>touchMe('charClassSelectorId1Img', 'charClassSelectorId1', toggleCheckboxById, changeClassFilter);</script>
			</label>
		</span>
		<span class="charClassSelector25">
       		<input id="charClassSelectorId2" class="hidden" type="checkbox" name="charClassId" value="MAGIC" checked="checked" onchange="javascript: changeClassFilter(this);" />
        	<label for="charClassSelectorId2" style="text-align: center; display: block; width: 25px;">
		    	<img id="charClassSelectorId2Img" src="${pageContext.request.contextPath}/images/1x1.png" 
		    		alt="Wizard" data-qtiptitle="Wizard - Intelligence and Range" class="classIconMAGIC25" />
			    <script>touchMe('charClassSelectorId2Img', 'charClassSelectorId2', toggleCheckboxById, changeClassFilter);</script>
			</label>
		</span>
		<span class="charClassSelector25">
       		<input id="charClassSelectorId3" class="hidden" type="checkbox" name="charClassId" value="HEALER" checked="checked" onchange="javascript: changeClassFilter(this);" />
        	<label for="charClassSelectorId3" style="text-align: center; display: block; width: 25px;">
		    	<img id="charClassSelectorId3Img" src="${pageContext.request.contextPath}/images/1x1.png" 
		    		alt="Cleric" data-qtiptitle="Cleric - Intelligence and Armour" class="classIconHEALER25" />
			    <script>touchMe('charClassSelectorId3Img', 'charClassSelectorId3', toggleCheckboxById, changeClassFilter);</script>
			</label>
		</span>
        <span class="charClassSelector25">
       		<input id="charClassSelectorId4" class="hidden" type="checkbox" name="charClassId" value="RANGE" checked="checked" onchange="javascript: changeClassFilter(this);" />
        	<label for="charClassSelectorId4" style="text-align: center; display: block; width: 25px;">
		    	<img id="charClassSelectorId4Img" src="${pageContext.request.contextPath}/images/1x1.png" 
		    		alt="Archer" data-qtiptitle="Archer - Dexerity and Range" class="classIconRANGE25"  />
			    <script>touchMe('charClassSelectorId4Img', 'charClassSelectorId4', toggleCheckboxById, changeClassFilter);</script>
			</label>
		</span>
		<span class="charClassSelector25">
       		<input id="charClassSelectorId5" class="hidden" type="checkbox" name="charClassId" value="SNEAKY" checked="checked" onchange="javascript: changeClassFilter(this);" />
       		<label for="charClassSelectorId5" style="text-align: center; display: block; width: 25px;">
		    	<img id="charClassSelectorId5Img" src="${pageContext.request.contextPath}/images/1x1.png" 
		    		alt="Rogue" data-qtiptitle="Rogue - Dexterity and Guile" class="classIconSNEAKY25" />
			    <script>touchMe('charClassSelectorId5Img', 'charClassSelectorId5', toggleCheckboxById, changeClassFilter);</script>
			</label>
		</span>
		<span class="charClassSelector25">
       		<input id="charClassSelectorId6" class="hidden" type="checkbox" name="charClassId" value="BUFF" checked="checked" onchange="javascript: changeClassFilter(this);" />
       		<label for="charClassSelectorId6" style="text-align: center; display: block; width: 25px;">
		    	<img id="charClassSelectorId6Img" src="${pageContext.request.contextPath}/images/1x1.png" 
		    		alt="Paladin" data-qtiptitle="Paladin - Strength and Guile" class="classIconBUFF25" />
			    <script>touchMe('charClassSelectorId6Img', 'charClassSelectorId6', toggleCheckboxById, changeClassFilter);</script>
			</label>
		</span>
  		<span class="charClassSelector25">
       		<input id="charClassSelectorId7" class="hidden" type="checkbox" name="charClassId" value="PETS" checked="checked" onchange="javascript: changeClassFilter(this);" />
       		<label for="charClassSelectorId7" style="text-align: center; display: block; width: 25px;">
		    	<img id="charClassSelectorId7Img" src="${pageContext.request.contextPath}/images/1x1.png" 
		    		alt="Necromancer" data-qtiptitle="Necromancer - Intelligence and Guile" class="classIconPETS25" />
			    <script>touchMe('charClassSelectorId7Img', 'charClassSelectorId7', toggleCheckboxById, changeClassFilter);</script>
			</label>
		</span>
		<span class="charClassSelector25">
       		<input id="charClassSelectorId8" class="hidden" type="checkbox" name="charClassId" value="MOBILE" checked="checked" onchange="javascript: changeClassFilter(this);" />
       		<label for="charClassSelectorId8" style="text-align: center; display: block; width: 25px;">
		    	<img id="charClassSelectorId8Img" src="${pageContext.request.contextPath}/images/1x1.png" 
		    		alt="Monk" data-qtiptitle="Monk - Dexterity and Armour" class="classIconMOBILE25" />
			    <script>touchMe('charClassSelectorId8Img', 'charClassSelectorId8', toggleCheckboxById, changeClassFilter);</script>
			</label>
		</span>
		<span class="charClassSelector25">
       		<input id="charClassSelectorId9" class="hidden" type="checkbox" name="charClassId" value="BARD" checked="checked" onchange="javascript: changeClassFilter(this);" />
       		<label for="charClassSelectorId9" style="text-align: center; display: block; width: 25px;">
		    	<img id="charClassSelectorId9Img" src="${pageContext.request.contextPath}/images/1x1.png" 
		    		alt="Hard Bard" data-qtiptitle="Hard Bard - Strength and Range" class="classIconBARD25" />
			    <script>touchMe('charClassSelectorId9Img', 'charClassSelectorId9', toggleCheckboxById, changeClassFilter);</script>
			</label>
		</span>
	</span>
	<br style="clear: both;" />
	<c:forEach items="${characters}" var="character">
    	<character:characterDisplay 
          	character="${character}"
          	linkDestFrame="characterDetailsFrame" 
	        linkDestUrl="${pageContext.request.contextPath}/play/character/${character.id}" 
	        dragable="${empty character.dungeon}" 
	        dragableSrcFrame="charactersFrame" 
	        dragableSrcUrl="${pageContext.request.contextPath}/play/character" 
			isTouchScreen="${account.touchScreen}"
			isFilterable="true"
			characterEquipment="${characterEquipment[character.id]}"
			localMode="${localMode}"
     		/>
    </c:forEach>
    <br style="clear: both" />
    <br />
    <c:if test="${resurrectionCost > 0}">
    <a href="${pageContext.request.contextPath}/play/character/resurrectAll?hardcore=${hardcore}&ironborn=${ironborn}"
    		onclick="if(confirm('Are you sure you want to pay ${resurrectionCost} dungeon tokens to resurrect all your characters?')){location.href=this.href+'&charId='+charId+'&dungeonId='+dungeonId;}; return false;">
    	Resurrect All Dead Characters not in a dungeon for ${resurrectionCost} <img src="${pageContext.request.contextPath}/images/token.png" alt="Tokens" title="Dungeon Tokens" class="dungeonToken" />
    </a>
    <br />
    <br />
    </c:if>
    <a href="javascript: loadSpan('characterDetailsFrame','${pageContext.request.contextPath}/play/character/create?hardcore=${hardcore}&ironborn=${ironborn}');">Create New Level 1 Character</a>
</body>
</html>

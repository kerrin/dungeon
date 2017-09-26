<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Create Character</title>
  <script>
  $("#characterDetailsSummaryFrame").empty();
  clearCompares();
  charId=-1;
  </script>
<style>
</style>
</head>
<body>
	<%@ include file="../checkError.jsp" %>
	<h1>Create Character</h1>
	<h2><label>Cost: </label>${numberOfCharacters} <img src="${pageContext.request.contextPath}/images/token.png" alt="Dungeon Tokens" data-qtiptitle="Dungeon Tokens" class="dungeonToken" /></h2>
    <form:form method="POST" modelAttribute="characterForm" action="${pageContext.request.contextPath}/play/character">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="hardcore" value="${hardcore}" />
    <input type="hidden" name="ironborn" value="${ironborn}" />
	<table>
        <tr>
        	<td><label for="name">Name</label></td>
        	<td><form:input path="name" type="text" id="name" maxlength="14" placeholder="Character Name" /></td>
        </tr>
        <tr>
        	<td colspan="2">
        		<span class="charClassSelector">
	        	<input id="charClassId1" class="hidden" type="radio" name="charClassId" value="1" />
        		<label for="charClassId1" class="charClassSelectorInner">
			    	<img id="charClassId1Img" src="${pageContext.request.contextPath}/images/1x1.png" alt="Barbarian" 
			    		data-qtiptitle="Barbarian - Strength and Armour" class="classIconMELEE50" />
				    <script>touchMe('charClassId1Img', 'charClassId1', selectRadioButtonById);</script>
				    <br />
			    	Barbarian
				</label>
				</span>
				<span class="charClassSelector">
        		<input id="charClassId2" class="hidden" type="radio" name="charClassId" value="2" />
        		<label for="charClassId2" class="charClassSelectorInner">
			    	<img id="charClassId2Img" src="${pageContext.request.contextPath}/images/1x1.png" alt="Wizard" 
			    		data-qtiptitle="Wizard - Intelligence and Range" class="classIconMAGIC50" />
				    <script>touchMe('charClassId2Img', 'charClassId2', selectRadioButtonById);</script>
				    <br />
			    	Wizard
				</label>
				</span>
				<span class="charClassSelector">
				<input id="charClassId3" class="hidden" type="radio" name="charClassId" value="3" />
        		<label for="charClassId3" class="charClassSelectorInner">
			    	<img id="charClassId3Img" src="${pageContext.request.contextPath}/images/1x1.png" alt="Cleric" 
			    		data-qtiptitle="Cleric - Intelligence and Armour" class="classIconHEALER50" />
				    <script>touchMe('charClassId3Img', 'charClassId3', selectRadioButtonById);</script>
				    <br />
			    	Cleric
				</label>
				</span>
   				<br style="clear: both;"/>
        		<span class="charClassSelector">
        		<input id="charClassId4" class="hidden" type="radio" name="charClassId" value="4" />
        		<label for="charClassId4" class="charClassSelectorInner">
			    	<img id="charClassId4Img" src="${pageContext.request.contextPath}/images/1x1.png" alt="Archer" 
			    		data-qtiptitle="Archer - Dexerity and Range" class="classIconRANGE50" />
				    <script>touchMe('charClassId4Img', 'charClassId4', selectRadioButtonById);</script>
				    <br />
			    	Archer
				</label>
				</span>
				<span class="charClassSelector">
        		<input id="charClassId5" class="hidden" type="radio" name="charClassId" value="5" />
        		<label for="charClassId5" class="charClassSelectorInner">
			    	<img id="charClassId5Img" src="${pageContext.request.contextPath}/images/1x1.png" alt="Rogue" 
			    		data-qtiptitle="Rogue - Dexterity and Guile" class="classIconSNEAKY50" />
				    <script>touchMe('charClassId5Img', 'charClassId5', selectRadioButtonById);</script>
				    <br />
			    	Rogue
				</label>
				</span>
				<span class="charClassSelector">
        		<input id="charClassId6" class="hidden" type="radio" name="charClassId" value="6" />
        		<label for="charClassId6" class="charClassSelectorInner">
			    	<img id="charClassId6Img" src="${pageContext.request.contextPath}/images/1x1.png" alt="Paladin" 
			    		data-qtiptitle="Paladin - Strength and Guile" class="classIconBUFF50" />
				    <script>touchMe('charClassId6Img', 'charClassId6', selectRadioButtonById);</script>
				    <br />
			    	Paladin
				</label>
				</span>
   				<br style="clear: both;"/>
        		<span class="charClassSelector">
        		<input id="charClassId7" class="hidden" type="radio" name="charClassId" value="7" />
        		<label for="charClassId7" class="charClassSelectorInner">
			    	<img id="charClassId7Img" src="${pageContext.request.contextPath}/images/1x1.png" alt="Necromancer" 
			    		data-qtiptitle="Necromancer - Intelligence and Guile" class="classIconPETS50" />
				    <script>touchMe('charClassId7Img', 'charClassId7', selectRadioButtonById);</script>
				    <br />
			    	Necromancer
				</label>
				</span>
				<span class="charClassSelector">
        		<input id="charClassId8" class="hidden" type="radio" name="charClassId" value="8" />
        		<label for="charClassId8" class="charClassSelectorInner">
			    	<img id="charClassId8Img" src="${pageContext.request.contextPath}/images/1x1.png" alt="Monk" 
			    		data-qtiptitle="Monk - Dexterity and Armour" class="classIconMOBILE50" />
				    <script>touchMe('charClassId8Img', 'charClassId8', selectRadioButtonById);</script>
				    <br />
			    	Monk
				</label>
				</span>
				<span class="charClassSelector">
        		<input id="charClassId9" class="hidden" type="radio" name="charClassId" value="9" />
        		<label for="charClassId9" class="charClassSelectorInner">
			    	<img id="charClassId9Img" src="${pageContext.request.contextPath}/images/1x1.png" alt="Hard Bard" 
			    		data-qtiptitle="Hard Bard - Strength and Range" class="classIconBARD50" />
				    <script>touchMe('charClassId9Img', 'charClassId9', selectRadioButtonById);</script>
				    <br />
			    	Hard Bard
				</label>
				</span>
   			</td>
   		</tr>
        <tr>
        	<td>&nbsp;</td>
        	<td><input name="add" type="submit" value="Create" /></td>
        </tr>
    </table>
    </form:form>
</body>
</html>

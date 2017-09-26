<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Character Details</title>
  <script>
	$(function() {
		$( "#characterSubMenuCharacters" ).removeClass("menuOptionSelected");
		$( "#characterSubMenuCreateCharacter" ).removeClass("menuOptionSelected");
		$( "#characterSubMenuCharacterDetails" ).removeClass("menuOptionSelected");
		$( "#characterSubMenuCharacterClasses" ).addClass("menuOptionSelected");
		loadSpan("charClassDetails", "${pageContext.request.contextPath}/dungeonpedia/character/characterClasses/1");
	});
	
	function updateSelectedCharClass(selectedId) {
		loadSpan("charClassDetails", "${pageContext.request.contextPath}/dungeonpedia/character/characterClasses/"+selectedId);
	}
	</script>
</head>
<body>
	<span id="dungeonpediaContentInnerTab" class="dungeonpediaContentInner">
	<h1>Character Classes</h1>
	<p>
	A characters class is very important for a character, it determines the following:
	<ul>
	<li>Health - The base health of the character</li>
	<li>Damage - The amount of damage they do with no spells or skills</li>
	<li>Strong Attributes - Each class uses different attributes to increase attack and defence, pick equipment with the strong attributes to be more efficient</li>	
	<li>Spells/Skills - Each class has access to different spells and skills</li>
	</ul>
	<form:form name="charClassForm" action="#" method="POST" modelAttribute="charClassForm">
	<form:select path="charClass" onChange="javascript: updateSelectedCharClass(this.value);">
		<form:options items="${charClasses}" itemLabel="niceName" itemValue="id" />
	</form:select>
	</form:form>
	<span id="charClassDetails">	
	</span>
	</p>
	</span>
</body>
</html>

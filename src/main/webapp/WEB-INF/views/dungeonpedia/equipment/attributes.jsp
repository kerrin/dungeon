<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Attributes</title>
  <script>
	$(function() {
		$( "#equipmentSubMenuEquipment" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuAttributes" ).addClass("menuOptionSelected");
		$( "#equipmentSubMenuEnchanting" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuStash" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuSalvaging" ).removeClass("menuOptionSelected");
		$( "#equipmentSubMenuBoostItem" ).removeClass("menuOptionSelected");
		loadSpan("attributeDetails", "${pageContext.request.contextPath}/dungeonpedia/equipment/attributes/1");
	});
	
	function updateSelectedAttribute(selectedId) {
		loadSpan("attributeDetails", "${pageContext.request.contextPath}/dungeonpedia/equipment/attributes/"+selectedId);
	}
	</script>
</head>
<body>
	<%@ include file="../../checkError.jsp" %>
	<h1>Attributes</h1>
	<span id="attributeSelect">
	<form:form name="attributeForm" action="#" method="POST" modelAttribute="attributeForm">
		<label for="attribute">Attribute</label>
		<form:select path="attribute" onChange="javascript: updateSelectedAttribute(this.value);">
 			<form:options items="${attributes}" itemLabel="niceName" itemValue="id" />
		</form:select>
	</form:form>	
	</span>
	<span id="attributeDetails" class="dungeonpediaAttributeDetails">	
	</span>
</body>
</html>

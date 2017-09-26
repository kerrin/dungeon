<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="equipmentTagLib" prefix="equip"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Salvage</title>
<style>
</style>
</head>
<body>
	<%@ include file="../checkError.jsp" %>
	<h3>Salvage Item</h3>
	<span class="itemDrop" style="height: 80px; border: 1px solid green; background-color: red;" data-qtiptitle="Converts equipment to dungeon tokens."
		ondrop="dropEquipmentSalvage(event,'${pageContext.request.contextPath}/api/account/${account.apiKey}/equipment/')" 
			ondragover="allowDrop(event)" >
			<input name="equipmentId" type="hidden" value="-1">
		&nbsp;
	</span>
</body>
</html>

<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Spells Details</title>
</head>
<body>
	<span id="dungeonpediaContentSpells">
	Mana Cost: ${spell.castCost}<br />
	Cooldown: ${spell.castCoolDown}<br /><!-- Fix me -->
	Minimum Level: ${spell.minimumLevel}<br />
	Damage Type: ${spell.damageType.niceName}<br />
	<c:if test="${fn:length(spell.effectTypes) > 0}">
	Effect Types:
	<c:forEach var='effectType' items="${spell.effectTypes}" varStatus="loop">
	<c:if test="${loop.index > 0}">,</c:if>
	 ${effectType.niceName}
	</c:forEach>
	</c:if>
	</span>
</body>
</html>

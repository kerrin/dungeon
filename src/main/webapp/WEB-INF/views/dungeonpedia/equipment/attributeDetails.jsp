<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Attribute Details</title>
</head>
<body>
	<span id="dungeonpediaContentAttributes">
	Type: ${attribute.type.niceName}<br />
	Description: ${attribute.description}<br />
	Roll Chance: ${attribute.rollChance}<br />
	<span data-qtiptitle="Cursed attributes reduce the value instead of increasing it. Cursed attributes can not have their range rerolled.">Curse Chance: ${attribute.cursedChance}</span><br />
	<span data-qtiptitle="Range at level 1">Possible Range: ${attribute.attributeValueMaxType.asString}</span><br />
	<span data-qtiptitle="How the item level affects the rolled value">Level Modifier: ${attribute.attributeValueEquationType.asString}</span><br />
	<c:if test="${!empty attribute.resistAttribute}">
	Resist: ${attribute.resistAttribute.niceName}<br />
	</c:if>
	<c:if test="${!empty attribute.powerType && attribute.powerType.niceName != 'None'}">
	Power Type: ${attribute.powerType.niceName}
	</c:if>
	</span>
</body>
</html>

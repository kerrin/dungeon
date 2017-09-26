<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="monsterTagLib" prefix="monster"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Character Classes Details</title>  
</head>
<body>
	<span class="dungeonpediaCharClassDetails scroll">
	<span class="scroll">
	Attack Attribute: ${charClass.mainAttribute.niceName}<br />
	Defence Attribute: ${charClass.defenceAttribute.niceName}<br />
	</span>
	<span class="scroll">
	<monster:monsterDisplay monster="${monster}" spanId="monsterDetails" fullDetails="true"/>
	</span>
	</span>
</body>
</html>

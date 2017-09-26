<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Dungeon</title>
<style>
.quizSpan {
	position: absolute;
	left: 50px;
}

table {
    margin: 0px;
    padding: 0px;
	border-spacing: 0px;
	border-collapse: collapse; 
}
td {
	height: 155px;
	width: 155px;
}
.box, .redBox, .greenBox, .yellowBox, .blueBox, .dropBox {
	height: 	150px;
	width: 		150px;
	padding-top:50px;
	float: 		left;
}
.RockBox {
	background-color: red;
}
.IdiotBox {
	background-color: green;
}
.ColdplayBox {
	background-color: yellow;
}
.ELOBox {
	background-color: blue;
}

.blankBox {
	background-color: white;
}
</style>
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
<script src="${pageContext.request.contextPath}/js/ios-drag-drop.js"></script>

<script type="text/javascript">
function allowDrop(ev) {
    ev.preventDefault();
}

function checkValidSubmit() {
	if($("#box11").val() != "ELO") return false;
	if($("#box12").val() != "Rock") return false;
	if($("#box21").val() != "Coldplay") return false;
	if($("#box22").val() != "Idiot") return false;
	
	window.location.replace("result");
	return true;
}

function dragBox(event, colour) {
	event.dataTransfer.setData("text", colour);
}

function dropBox(event, location) {
	var colour = event.dataTransfer.getData("text")
	var target = event.target;
	while(target && !target.id) target = target.parentNode;
	if(!target) { return false }
	$("#"+target.id).siblings("input").val(colour);
	$("#"+target.id).removeClass("blankBox");
	$("#"+target.id).removeClass("RockBox");
	$("#"+target.id).removeClass("IdiotBox");
	$("#"+target.id).removeClass("ColdplayBox");
	$("#"+target.id).removeClass("ELOBox");
	$("#"+target.id).addClass(colour+"Box");
	$("#"+target.id).html(colour);
}
</script>
</head>
<body>
	<span class="quizSpan">
	<h1>Paper Anniversary Quiz - Question 21 - Step 2 of 2</h1>
	<span>
	<table border="2" style="border-spacing: 0px; text-align: center;">
		<tr>
			<td>
				<span id="redBox" class="box" draggable="true" ondragstart="dragBox(event, 'Rock')">Molten Rock</span>
			</td>
			<td>
				<span id="greenBox" class="box" draggable="true" ondragstart="dragBox(event, 'Idiot')">Don't want to be an American Idiot</span>
			</td>
			<td>
				<span id="yellowBox" class="box" draggable="true" ondragstart="dragBox(event, 'Coldplay')">Coldplay Song</span>				
			</td>
			<td>
				<span id="blueBox" class="box" draggable="true" ondragstart="dragBox(event, 'ELO')">Electric Light Orchestra</span>
			</td>
		</tr>
	</table>
	<table border="2" style="border-spacing: 0px; text-align: center;">
		<tr>
			<td>
				<span id="span11" class="dropBox blankBox" ondrop="dropBox(event,'box11')" ondragover="allowDrop(event)"></span>
				<input id="box11" name="box11" type="hidden" value="-1">
			</td>
			<td>
				<span id="span12" class="dropBox blankBox" ondrop="dropBox(event,'box12')" ondragover="allowDrop(event)"></span>
				<input id="box12" name="box12" type="hidden" value="-1">
			</td>
		</tr>
		<tr>
			<td>
				<span id="span21" class="dropBox blankBox" ondrop="dropBox(event,'box21')" ondragover="allowDrop(event)"></span>
				<input id="box21" name="box21" type="hidden" value="-1">			
			</td>
			<td>
				<span id="span22" class="dropBox blankBox" ondrop="dropBox(event,'box22')" ondragover="allowDrop(event)"></span>
				<input id="box22" name="box22" type="hidden" value="-1">
			</td>
		</tr>
	</table>
	<br />
	<input type="submit" value="Check" onclick="javascript: if(!checkValidSubmit()) { alert('Sorry, that is incorrect.'); }" />
    </span>
    <br style="clear: both;"/>
    <br />    
	Try again before following this link.<br />
    <a href="step2easiest">Nope, make it easier please.</a>
    </span>
</body>
</html>

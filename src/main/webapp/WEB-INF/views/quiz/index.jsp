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
table {
    margin: 0px;
    padding: 0px;
	border-spacing: 0px;
	border-collapse: collapse; 
}
td table {
	width: 150px;
}
td {
	width: 48px;
	height: 48px;
}  
</style>
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>

<script type="text/javascript">
function checkValidSubmit() {
	if($("#t12c22").val() != 2) return false;
	if($("#t13c22").val() != 3) return false;
	if($("#t23c22").val() != 9) return false;
	if($("#t31c22").val() != 6) return false;

	window.location.replace("quiz25/step2");
	return true;
}
</script>
</head>
<body>
	<span class="infoSpan">
	<h1>Paper Anniversary Quiz - Question 21 - Step 1 of 2</h1>
	<span>
	<audio controls>
		<source src="audio/Hero-Me-Full.mp3" type="audio/mpeg">
		Your browser does not support the audio element.
	</audio>
	<table border="2" style="border-spacing: 0px; text-align: center;">
		<tr>
			<td>
				<table border="1">
					<tr><td>M</td>		<td>X</td>		<td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td>	<td>&nbsp;</td>	<td>Y</td></tr>
					<tr><td>&nbsp;</td>	<td>&nbsp;</td>	<td>S</td></tr>
				</table>
			</td>
			<td>
				<table border="1">
					<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td><td><input id="t12c22" type="number" min="1" max="9" size="1" maxlength="1"/></td><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
				</table>
			</td>
			<td>
				<table border="1">
					<tr><td>R</td>	<td>&nbsp;</td>	<td>J</td></tr>
					<tr><td></td>	<td><input id="t13c22" type="number" min="1" max="9" size="1" maxlength="1"/></td>	<td>H</td></tr>
					<tr><td>P</td>	<td>T</td>		<td>&nbsp;</td></tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border="1">
					<tr><td>&nbsp;</td>	<td>&nbsp;</td>	<td>&nbsp;</td></tr>
					<tr><td>W</td>		<td>&nbsp;</td>	<td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td>	<td>L</td>		<td>N</td></tr>
				</table>
			</td>
			<td>
				<table border="1">
					<tr><td>A</td><td>B</td>		<td>C</td></tr>
					<tr><td>D</td><td>&nbsp;</td>	<td>&nbsp;</td></tr>
					<tr><td>E</td><td>F</td>		<td>G</td></tr>
				</table>
			</td>
			<td>
				<table border="1">
					<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td><td><input id="t23c22" type="number" min="1" max="9" size="1" maxlength="1"/></td><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>					
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border="1">
					<tr><td>&nbsp;</td><td>W</td>	<td>&nbsp;</td></tr>
					<tr><td>V</td><td><input id="t31c22" type="number" min="1" max="9" size="1" maxlength="1"/></td>	<td>&nbsp;</td></tr>
					<tr><td>Z</td><td>H</td>		<td>&nbsp;</td></tr>
				</table>
			</td>
			<td>
				<table border="1">
					<tr><td>&nbsp;</td><td>&nbsp;</td>	<td>J</td></tr>
					<tr><td>&nbsp;</td><td>K</td>		<td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td><td>W</td>		<td>T</td></tr>
				</table>
			</td>
			<td>
				<table border="1">
					<tr><td>&nbsp;</td><td>&nbsp;</td>	<td>T</td></tr>
					<tr><td>&nbsp;</td><td>H</td>		<td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td>	<td>&nbsp;</td>	<td>U</td></tr>
				</table>
			</td>
		</tr>
	</table>
	<input type="submit" value="Check" onclick="javascript: if(!checkValidSubmit()) { alert('Sorry, that is incorrect.'); }" />
    </span>
    </span>
</body>
</html>

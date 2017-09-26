<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Testing</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/shared.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.qtip.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.min.css">
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.qtip.min.js"></script>
<script src="${pageContext.request.contextPath}/js/imagesloaded.pkg.min.js"></script>

<script>
$(function() {
  $( ".draggable" ).draggable();
});
$(function() {
  $( ".resizable" ).resizable();
});
</script>
<svg class="defs-only">
<defs>
<filter id="monochrome-blue"
		color-interpolation-filters="sRGB"
        x="0" y="0" height="100%" width="100%">
 	<feColorMatrix type="matrix"
     	values="0.95 0 0 0 0.05 
              	0.85 0 0 0 0.15  
              	0.50 0 0 0 0.50 
              	0    0 0 1 0"/>
</filter>

<filter id="monochrome-yellow"
		color-interpolation-filters="sRGB"
        x="0" y="0" height="100%" width="100%">
 	<feColorMatrix type="matrix"
     	values="0.293 0.769 0.189 0 0
         		0.249 0.686 0.168 0 0
         		0.272 0.534 0.131 0 0
         		0   0   0   1 0"/>
</filter>

<filter id="monochrome-red"
		color-interpolation-filters="sRGB"
        x="0" y="0" height="100%" width="100%">
    <feColorMatrix type="matrix"
      	values="0.90 0 0 0   0.40 
              	0.95 0 0 0  -0.10  
             	-0.20 0 0 0   0.65 
                0  0 0 1   0" />
</filter>
</defs>
</svg>
</head>
<body>
    <div class="ui-widget-content draggable" style="display: inline-block;">
    <img class="classIconMELEE100" />
    </div>
    <img class="classIconMAGIC100" />
    <img class="classIconHEALER100" />
    <div class="ui-widget-content resizable" style="display: inline-block;">
    <img class="classIconRANGE100" />
    </div>
    <div class="ui-widget-content resizable draggable" style="display: inline-block;">
    <img class="classIconSNEAKY100" />
    </div>
    <img class="classIconBUFFS100" />
    <img class="classIconPETS100" />
    <img class="classIconMOBILE100" />
    <img class="classIconBARD100" />
    <br />
    <span style="background-color: #000000; width: 50px; height: 50px;" title="#000000">000</span>
    <span style="background-color: #007700; width: 50px; height: 50px;" title="#007700">010</span>
    <span style="background-color: #000077; width: 50px; height: 50px;" title="#000077">001</span>
    <span style="background-color: #007777; width: 50px; height: 50px;" title="#007777">011</span>
    <span style="background-color: #00FF00; width: 50px; height: 50px;" title="#00ff00">020</span>
    <span style="background-color: #0077FF; width: 50px; height: 50px;" title="#0077ff">012</span>
    <span style="background-color: #0000FF; width: 50px; height: 50px;" title="#0000ff">002</span>
    <span style="background-color: #00FFFF; width: 50px; height: 50px;" title="#00ffff">022</span>
    <br />
    <span style="background-color: #770000; width: 50px; height: 50px;" title="#770000">100</span>
    <span style="background-color: #777700; width: 50px; height: 50px;" title="#777700">110</span>
    <span style="background-color: #770077; width: 50px; height: 50px;" title="#770077">101</span>
    <span style="background-color: #777777; width: 50px; height: 50px;" title="#777777">111</span>
    <span style="background-color: #77FF00; width: 50px; height: 50px;" title="#77ff00">120</span>
    <span style="background-color: #7777FF; width: 50px; height: 50px;" title="#7777ff">112</span>
    <span style="background-color: #7700FF; width: 50px; height: 50px;" title="#7700ff">102</span>
    <span style="background-color: #77FFFF; width: 50px; height: 50px;" title="#77ffff">122</span>
    <br />
    <span style="background-color: #FF0000; width: 50px; height: 50px;" title="#ff0000">200</span>
    <span style="background-color: #FF7700; width: 50px; height: 50px;" title="#ff7700">210</span>
    <span style="background-color: #FF0077; width: 50px; height: 50px;" title="#ff0077">201</span>
    <span style="background-color: #FF7777; width: 50px; height: 50px;" title="#ff7777">211</span>
    <span style="background-color: #FFFF00; width: 50px; height: 50px;" title="#ffff00">220</span>
    <span style="background-color: #FF77FF; width: 50px; height: 50px;" title="#ff77ff">212</span>
    <span style="background-color: #FF00FF; width: 50px; height: 50px;" title="#ff00ff">202</span>
    <span style="background-color: #FFFFFF; width: 50px; height: 50px;" title="#ffffff">222</span>
    <br />
    <br />
    
    <img class="charSlotIconNeedMissingSHOULDERS" style="filter: grayscale(100%);" />
    <img class="charSlotIconNeedLowSHOULDERS" style="-webkit-filter: grayscale(100%);" />
    <br />
    <img class="charSlotIconNeedMissingHEAD" />
    <img class="charSlotIconNeedLowHEAD" />
    <br />
    <img class="charSlotIconNeedMissingBROACH" />
    <img class="charSlotIconNeedLowBROACH" />
    <br />
    <img class="charSlotIconNeedMissingRING_LEFT" />
    <img class="charSlotIconNeedLowRING_LEFT" />
    <br />
    <img class="charSlotIconNeedMissingRING_RIGHT" />
    <img class="charSlotIconNeedLowRING_RIGHT" />
    <br />
    <img class="charSlotIconNeedMissingAMULET" />
    <img class="charSlotIconNeedLowAMULET" />
    <br />
    <img class="charSlotIconNeedMissingCHEST" />
    <img class="charSlotIconNeedLowCHEST" />
    <br />
    <img class="charSlotIconNeedMissingBRACERS" />
    <img class="charSlotIconNeedLowBRACERS" />
    <br />
    <img class="charSlotIconNeedMissingLEGS" />
    <img class="charSlotIconNeedLowLEGS" />
    <br />
    <img class="charSlotIconNeedMissingHANDS" />
    <img class="charSlotIconNeedLowHANDS" />
    <br />
    <img class="charSlotIconNeedMissingMAIN_WEAPON" />
    <img class="charSlotIconNeedLowMAIN_WEAPON" />
    <br />
    <img class="charSlotIconNeedMissingOFF_HAND" />
    <img class="charSlotIconNeedLowOFF_HAND" />
    <br />
    <img class="charSlotIconNeedMissingFEET" />
    <img class="charSlotIconNeedLowFEET" />
    <br />
    
    <img src="../images/CharSlotIcons.png" style="-webkit-filter: url('#monochrome-red'); filter: url('#monochrome-red');" />
    <img src="../images/CharSlotIcons.png" style="-webkit-filter: url('../assets/filters.svg#monochrome-red'); filter: url('../assets/filters.svg#monochrome-red');" />
    <img src="../images/CharSlotIcons.png" style="-webkit-filter: url('#monochrome-yellow'); filter: url('#monochrome-yellow');" />
    <img src="../images/CharSlotIcons.png" style="-webkit-filter: grayscale(100%); filter: grayscale(100%);" />
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

        <title>Transaction Viewer</title>
        <script type="text/javascript">
            
		function showMessage(msgType) {
			var hdnVar = document.getElementById("hdn"+msgType+"Msg");
			//alert(hdnVar.value);
			var msgDiv = document.getElementById("msgDiv");
			//alert(msgDiv);
			msgDiv.value = hdnVar.value;
		}
        </script>

    </head>
    <body>
	<s:set name='_action' value='[0]'/>

	<div>
	<table>
	<tr > <td style="vertical-align: top; border-bottom-style: inset; border-top-style: inset; border-left-style: inset; border-right-style: inset">
	<table >
	<s:hidden name='hdnType' value="%{#_action.getTransType()}"/>

	<s:if test='#_action.getTransType() == "OrderPlacement"'>
	<s:set name='XMLInputMsg' value='#_action.getMessage("XMLInput")'/>
	<s:hidden name='hdnXMLInputMsg' value="%{#XMLInputMsg}"/>
	<s:set name='MapRequestOutMsg' value='#_action.getMessage("MapRequestOut")'/>
	<s:hidden name='hdnMapRequestOutMsg' value="%{#MapRequestOutMsg}"/>
	<s:set name='MQStringInMsg' value='#_action.getMessage("MQStringIn")'/>
	<s:hidden name='hdnMQStringInMsg' value="%{#MQStringInMsg}"/>
	<s:set name='MQStringOutMsg' value='#_action.getMessage("MQStringOut")'/>
	<s:hidden name='hdnMQStringOutMsg' value="%{#MQStringOutMsg}"/>
	<s:set name='MapResponseInMsg' value='#_action.getMessage("MapResponseIn")'/>
	<s:hidden name='hdnMapResponseInMsg' value="%{#MapResponseInMsg}"/>
	<s:set name='XMLOutputMsg' value='#_action.getMessage("XMLOutput")'/>
	<s:hidden name='hdnXMLOutputMsg' value="%{#XMLOutputMsg}"/>
	
	<tr>
	<td> <a href="#" onclick="javascript:showMessage('XMLInput');"> XMLInput</a>
				</td> </tr> <tr>
<td> <a href="#" onclick="javascript:showMessage('MapRequestOut');"> MapRequestOut</a>
				</td>
				
	</tr><tr>
	<td> <a href="#" onclick="javascript:showMessage('MQStringIn');"> MQStringIn</a>
				</td></tr>
				<tr>
	<td> <a href="#" onclick="javascript:showMessage('MQStringOut');"> MQStringOut</a>
				</td></tr>
				<tr>
	<td> <a href="#" onclick="javascript:showMessage('MapResponseIn');"> MapResponseIn</a>
				</td></tr>
				<tr>
	<td> <a href="#" onclick="javascript:showMessage('XMLOutput');"> XMLOutput</a>
				</td></tr>
	</s:if>
     <s:elseif test='#_action.getTransType() == "OrderUpdate"'>
	<s:set name='MapInMsg' value='#_action.getMessage("MapIn")'/>
	<s:hidden name='hdnMapInMsg' value="%{#MapInMsg}"/>
	<s:set name='MapOutMsg' value='#_action.getMessage("MapOut")'/>
	<s:hidden name='hdnMapOutMsg' value="%{#MapOutMsg}"/>
	 
	<tr>
	<td> <a href="#" onclick="javascript:showMessage('MapIn');"> MapIn</a>
				</td> </tr> <tr>
	<td> <a href="#" onclick="javascript:showMessage('MapOut');"> MapOut</a>
				</td>
				
	</tr>
     </s:elseif>	
	</table>
	</td> 
	<td colspan=4> <textarea id="msgDiv" name="msgDiv" style="height: 581px; width: 612px; "> </textarea> </td>
	</table>
	</div>
	
</body>
</html>
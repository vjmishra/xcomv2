<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html;  import=java.util.*  charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
  <%@page import="java.util.Date"%>

  <%@page import="java.text.SimpleDateFormat"%>
    <%@page import="java.text.DateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; import=java.util.*    charset=ISO-8859-1">

        <title>Transaction Viewer</title>
		<script src="js/jquery-1.7.1.js"></script>
		<script src="js/jquery.ui.core.min.js"></script>
		<script src="js/jquery.ui.widget.min.js"></script>
		<script src="js/jquery.ui.datepicker.min.js"></script>

		<link rel="stylesheet" href="jquery/themes/base/jquery.ui.all.css">        
        <script type="text/javascript">
            function notEmpty(ctl00$MainContent$TextBox, helperMsg){
	if(WebconfTextboxName.value.length == 0){
		alert("Please Enter a Web Configuration Number");
		WebconfTextboxName.focus();
		return false;
	}
	return true;
	
}
            
           
            
			function openMessageWindow(timestamp, transType) {
				
				var msgIds = "";
				var hdnVar = document.getElementById(timestamp);
				msgIds = hdnVar.value;
				//alert( msgIds);
				//alert( msgTypes);
				
				message_window=window.open('viewMessage.action?msgIds='+ msgIds+'&transType='+transType,'message_window');
				message_window.focus();
			}
			$(function() {
				$( "#DateTextBoxId" ).datepicker();
			});
			
			

</script>
        
      

    </head>
    <body>
    		
    	
		 
		 <%!
		 DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		 String formattedDate = df.format(new Date());
		%>
		 
    
        <s:form name="SubmitButtonName" method="post" action="submit"  >

			<!-- <table style="width: 50px; ">
                    <tr>
                            <td style="width: 142px; ">Environment: </td>
                            <td style="width: 85px; ">
                                    <select id="Environment">
                                            <option value="Production" selected="selected"></option>
                                            <option value="Staging"></option>
                                            <option value="Development"></option>
                                    </select>
                            </td>
                            <td style="width: 168px; " width="168px">Web Confirmation No.: </td>
                            <td>
                                    <input>
                            </td>
                            
                    </tr>
            </table> -->

            <table cellspacing="0" cellpadding="2" border="0" bgcolor="#CFECEC" width="100%" style="border-right: #000000 1px solid; border-top: #000000 1px solid;
                   border-left: #000000 1px solid; border-bottom: #000000 1px solid" id="table1">
                <tbody><tr>

                        <td>
                            <b><font face="Verdana" size="1">Environment</font></b></td>
                        <td>
                            <b><font face="Verdana" size="1">Date(mm/dd/yyyy)</font></b></td> 
                        <td>
                            <b><font face="Verdana" size="1">Transaction Type</font></b></td>

                        <td>
                            <b><font face="Verdana" size="1">Web Confirmation Number</font></b></td>

                        <td>
                            <b><font face="Verdana" size="1">Time Range (Central Time)</font></b></td>





                        <td>
                            <b><font face="Verdana" size="1"></font></b>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <select style="font-family:Verdana;font-size:XX-Small;" id="EnvironmentTypeDropdownId" name="EnvironmentTypeDropdownName">                               
                                <option value="Xpdx_Test">Production</option>
                                <option value="Xpdx_Dev">Non Production</option>
                            </select>
                        
                        </td>
                        
                        <td>
                        <input type="text" style="font-family:Verdana;font-size:XX-Small;width:72px;" id="DateTextBoxId" maxlength="10" value=<%=formattedDate%>
                            name="DateTextBoxName">
                            </td>
                         <td>
                            <select style="font-family:Verdana;font-size:XX-Small;" id="TransactionTypeDropdownId" name="TransactionTypeDropdownName">
                                <option value="--All Transactions--">--All Transactions--</option>
                                <option value="OrderPlacement">Order Placement</option>
                                <option value="OrderUpdate">Order Update</option>
                                <option value="PriceAndAvailability">Price and Availability</option>
                                
                            </select></td>
                        


                        <td>
                            <input type="text" style="font-family:Verdana;font-size:XX-Small;" id="WebconfTextboxId" maxlength="14" value="" name="WebconfTextboxName" size="30">
                        </td>

                        <td>
                            <select style="font-family:Verdana;font-size:XX-Small;" id="TimeRangeDropDownId " name="TimeRangeDropDownName">
                                <option value="--All Times--">--All Times--</option>
                                <option value="00">00:00 AM - 00:59 AM</option>
                                <option value="01">01:00 AM - 01:59 AM</option>
                                <option value="02">02:00 AM - 02:59 AM</option>
                                <option value="03">03:00 AM - 03:59 AM</option>
                                <option value="04">04:00 AM - 04:59 AM</option>
                                <option value="05">05:00 AM - 05:59 AM</option>
                                <option value="06">06:00 AM - 06:59 AM</option>
                                <option value="07">07:00 AM - 07:59 AM</option>
                                <option value="08">08:00 AM - 08:59 AM</option>
                                <option value="09">09:00 AM - 09:59 AM</option>
                                <option value="10">10:00 AM - 10:59 AM</option>
                                <option value="11">11:00 AM - 11:59 AM</option>
                                <option value="12">12:00 PM - 12:59 PM</option>
                                <option value="13">01:00 PM - 01:59 PM</option>
                                <option value="14">02:00 PM - 02:59 PM</option>
                                <option value="15">03:00 PM - 03:59 PM</option>
                                <option value="16">04:00 PM - 04:59 PM</option>
                                <option value="17">05:00 PM - 05:59 PM</option>
                                <option value="18">06:00 PM - 06:59 PM</option>
                                <option value="19">07:00 PM - 07:59 PM</option>
                                <option value="20">08:00 PM - 08:59 PM</option>
                                <option value="21">09:00 PM - 09:59 PM</option>
                                <option value="22">10:00 PM - 10:59 PM</option>
                                <option value="23">11:00 PM - 11:59 PM</option>
                                
                                
                            </select>
                        </td>
                        <td>
                            <input type="submit" style="font-family:Verdana;font-size:XX-Small;" id="SubmitButtonId" value="Submit Query" name="SubmitButtonName" onclick="notEmpty(WebconfTextboxName)" >
                        </td>
                    </tr>
                    <tr>
                        <td>
                        </td>
                        <td>
                        </td>
                        <td>
                            
                        <td>
                        </td>
                    </tr>
                </tbody></table>
            <div> 
                <table cellspacing="0" cellpadding="4" border="0" style="color:#333333;font-family:Verdana;font-size:XX-Small;width: 1100px;border-collapse:collapse;z-index: 101" id="ReportDataGrid">
                    <tbody><tr style="color:White;background-color:#5D7B9D;font-weight:bold;white-space:nowrap;">
                            <td style="width: 260px; height: 16px"><a style="color:White;" href="javascript:__doPostBack('ReportDataGridl00','')">Time Stamp</a></td>
                            <td><a style="color:White;" href="javascript:__doPostBack('ReportDataGridl00','')">Web Confirmation No.</a></td>
                            <td><a style="color:White;" href="javascript:__doPostBack('ReportDataGridl00','')">Transaction Type</a></td>
                            <td><a style="color:White;" href="javascript:__doPostBack('ReportDataGridl00','')">Transaction Messages</a></td>
                        </tr>
 				<s:if test="xcomMessageIdMap != null">
				
				<s:iterator value='xcomMessageIdMap'>
							<s:set name='businessTimestamp' value='key' />
							<s:set name="transObj" value="value" />
							<s:set name='webConfNumber' value="#transObj.webConfNumber"/>
							<s:set name='transType' value="#transObj.transactionType"/>
							<s:hidden id="selectedBT" name='selectedBT' value='%{#businessTimestamp}' />
							<s:hidden id="selectedWebConf" name='selectedWebConf' value='%{#webConfNumber}' />
							<s:hidden id="selectedTransType" name='selectedTransType' value='%{#transType}' />
							<tr style="color:#333333;background-color:#F7F6F3;">
								<td><s:property value='#businessTimestamp' /></td>
								<td><s:property value='#webConfNumber' /></td>
								<td><s:property value='#transType' /></td>
								<td> 
								<s:hidden id="%{#businessTimestamp}" name="%{#businessTimestamp}"
								value="%{#transObj.getAllMsgIds()}"/>
								<input type="button" value="View Message" style="font-family:Verdana;font-size:XX-Small;" 
								onClick="javascript:openMessageWindow('<s:property value='#businessTimestamp'/>', '<s:property value='#transType'/>');">
								</td>
							</tr>
				</s:iterator>
				</s:if>
                    </tbody>
                </table>
            </div>
        </s:form>
    </body>

</html>
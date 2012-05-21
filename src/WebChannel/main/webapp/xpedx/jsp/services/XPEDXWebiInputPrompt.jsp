<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<swc:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES.css" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />
<![endif]-->
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" /> 

<!-- styles -->
<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/banner.css"/>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jQuery.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.datepicker.js"></script>


<!-- carousel scripts css  -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/skin.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/demo.css"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.css"/>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>

<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom.css" media="screen" />

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shopping-cart.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/om2.css" />


<!--  Change Location Light box -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/org/xpedxCustomerLocations.js"></script>


<title><s:property value="wCContext.storefrontId" /> - Webi Input Prompt</title>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>


<s:set name='_action' value='[0]' />
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />
<s:set name='sdoc' value="outputDoc" />
<s:set name='extnElem' value='#xmlUtil.getChildElement(#sdoc, "Extn")'/>



<script type="text/javascript"><!--

<s:if test="getRenderReport() == 'true'">

	var strEntry = '<s:property value="storageToken"/>';
	var docId = '<s:property value="id"/>';
	var strUrl = "<s:property value='#wcUtil.staticFileLocation' />/xpedx/jsp/reporting/viewers/cdz_adv/viewDocument.jsp?sEntry="+ strEntry +"&lang=en&id="+ docId +"&ViewType=H&sPageMode=QuickDisplay&sRefresh=yes&kind=Webi";
	//strUrl += "&timeOutx=100&zoom=100&sNewDoc=false&sApplyFormat=&iDPIndex=&bValidateSQL=false&nAction=&advPrompts=yes&bCreateDefaultReportBody=false&defaultRepTitle=Report%2BTitle";
	//Added For Jira 2810
	//window.open(strUrl,'','toolbar=0');
	window.open(strUrl);
</s:if>



	function submitSapForm() {
		$.fancybox.close();
		var arr = new Array();
		arr = document.getElementsByName('customerId');

        for(var i = 0; i < arr.length; i++)        
        {            
            var obj = arr.item(i);     
            if(obj.checked == true)  {     
        
            	var textElement = document.getElementById('txtLocation');
            	if(textElement != null){

            		var token = obj.value;
            		var currentTagTokens = token.split( ";" );
            		  
            		textElement.value = currentTagTokens[0];
            		var hdnLocType = document.getElementById('selectedLocationType');
            		hdnLocType.value="Account";
            		var hdnCustId = document.getElementById('selectedCustId');
					if(currentTagTokens[1]!=null)
            			hdnCustId.value= currentTagTokens[1];
            		//alert("hdnCustId" + hdnCustId);
           			
            	}                 	
            }   
        }
        
		
		
	}

	

	function submitShipToForm() {
		$.fancybox.close();
		var arr = new Array();
		var selectedvalue = null;
		
		arr = document.getElementsByName('selectedShipTo');
		for(var i = 0; i < arr.length; i++)        
        {            
            var obj = arr.item(i);     
            if(obj.checked == true)  {     
            	selectedvalue= arr[i].value;
            	var hdnOrganization = document.getElementById('selectedorganization');
            	var hdnCompany = document.getElementById('selectedcompany');
        		var hdnAddress = document.getElementById('selectedaddressList');
        		var hdnLocid = document.getElementById('selectedlocationID');
        		var hdnCity = document.getElementById('selectedcity');
        		var hdnState = document.getElementById('selectedstate');
        		var hdnZcode = document.getElementById('selectedzipCode');
        		var hdncustomerID= document.getElementById('selectedcustomerid');
        		var hdncountry= document.getElementById('selectedcountry');
        		
            	var textElement = document.getElementById('txtLocation');
            	if(textElement != null){
                	var displaycustID=hdncustomerID.value;
                	var mytool_array=displaycustID.split("-");
                	var selCustId = mytool_array[0]+"-"+mytool_array[1]+"-"+mytool_array[2];
                	var displayString = selCustId + "\n" + hdnOrganization.value + "\n"  ;
                	var adrs = hdnAddress.value;
            		var currentTagTokens = adrs.split( "," );
            		for(var i = 0; i < currentTagTokens.length; i++)        
                    {
                    	displayString = displayString + currentTagTokens[i] + "\n";
                        	
                    }
            		displayString =  displayString + hdnCity.value + ", " + hdnState.value + " " + hdnZcode.value + " " + hdncountry.value;
                    var customizeString1= displayString.replace("[","");
            		var onScreenString= customizeString1.replace("]","");
            		//alert("onScreenString=="+ onScreenString);
            		textElement.value = onScreenString;
            		var hdnLocType = document.getElementById('selectedLocationType');
            		hdnLocType.value="Ship To";
            		var hdnCustId = document.getElementById('selectedCustId');
            	    hdnCustId.value=obj.value;
            	   
            	}
            	break;                 	
            }   
        }
       
	}

	function updateSelectedAddress(customerID, organizationName,company,address,locid,city,state,zcode,country)
	{
		var hdnOrganization = document.getElementById('selectedorganization');
		var hdnCompany = document.getElementById('selectedcompany');
		var hdnAddress = document.getElementById('selectedaddressList');
		var hdnLocid = document.getElementById('selectedlocationID');
		var hdnCity = document.getElementById('selectedcity');
		var hdnState = document.getElementById('selectedstate');
		var hdnZcode= document.getElementById('selectedzipCode');
		var hdncustomerID= document.getElementById('selectedcustomerid');
		var hdncountry= document.getElementById('selectedcountry');

		hdnOrganization.value = organizationName;
		hdncustomerID.value = customerID;
		hdnCompany.value = company;
		hdnAddress.value = address;
		hdnLocid.value = locid;
		hdnCity.value = city;
		hdnState.value = state;
		hdnZcode.value = zcode;
		hdncountry.value = country;
	}
	
	function submitBillToForm() {
		
			$.fancybox.close();
			var arr = new Array();
			arr = document.getElementsByName('customerId');

	        for(var i = 0; i < arr.length; i++)        
	        {            
	            var obj = arr.item(i);     
	            if(obj.checked == true)  {     
	        
	            	var textElement = document.getElementById('txtLocation');
	            	if(textElement != null){
	            		var token = obj.value;
	            		var currentTagTokens = token.split( ";" );
	            		var textedit = currentTagTokens[0];
	            		//Editing billtoname start
	            		var editbillto = textedit.split(",");
	            		var editbilltoone = editbillto[0];
	            		var editbilltotwo = editbilltoone.split(" ");
	            		var display_BillToname = "";
						for (var i = 1; i< editbilltotwo.length;i++){
							display_BillToname = display_BillToname + " " + editbilltotwo[i];

							}
						//Editing billtoname end
						//Editing Address Start
	            		var editaddress= "";
	            		for (var i = 1; i< editbillto.length-3;i++){
	    						
	    	            			var editaddress= editaddress + editbillto[i] + "\n" ;
	
	    		            }
						//Editing City, zipcode, country
	            		var editaddress1="";
	            		for (var i = editbillto.length-3; i< editbillto.length;i++){
    						if(editaddress1 =="")
    							editaddress1= editbillto[i];
    						else
	            			editaddress1= editaddress1 +"," +editbillto[i] ;
	            			
		           			 }
	            		//Editing Address End
			            var stringone =  textedit.split(" ");
						var displayid = stringone[0];
						var displaycustid_with_BillToname = displayid + "\n" +  display_BillToname;
						textElement.value = displaycustid_with_BillToname + "\n" + 	editaddress  + editaddress1  ;            		
	            		var hdnLocType = document.getElementById('selectedLocationType');
	            		hdnLocType.value="Bill To";
	            		var hdnCustId = document.getElementById('selectedCustId');
						if(currentTagTokens[1]!=null)
	            			hdnCustId.value= currentTagTokens[1];
            			
	            	}                 	
	            }   
	        }		
	}	

	function submitReportForm() {
		//alert("Submit called");
		document.webiPromptForm.submit();

	}	

	function showLightBox() {
		
		var opt = document.getElementById('optsLocations');

		
		if(opt.value == "sap") {
			$("#varSAP").trigger('click');
			
		} else if(opt.value =="shipTo") {
			$('#varShipTo').trigger('click');			
		}else if(opt.value =="billTo") {
			$('#varBillTo').trigger('click');			
		}else {
			var textElement = document.getElementById('txtLocation');
    		textElement.value = "";
    		var hdnLocType = document.getElementById('selectedLocationType');
    		hdnLocType.value="All";
		}
		
				
	}
		 
--></script>

<!--  REporting lightbox changes end -->
<script type="text/javascript">

$(function() {
	$(".datepicker").datepicker({
		showOn: 'button',
					numberOfMonths: 1,
		buttonImage: '<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/calendar-icon.png',
		buttonImageOnly: true
		
	});
});
		$(document).ready(function(){
	$(document).pngFix();
	$('#split-btn').click(function(){       
			$('.split-rows').toggle();
			return false;
	});
	$('#popup-window-close').click(function(){
			$('#split-order-overlay').toggle();
			return false;
	});
});

					
			function showAllAccounts() {
				
				var url = document.getElementById("showLocationsUrl").value;
				var customerId = document.getElementById("customerId").value;
				var organizationCode = document.getElementById("organizationCode").value;

				

				if(url!=null) {

					//Added For Jira 2903
					//Commented for 3475
					//Ext.Msg.wait("Processing..."); 
					//Ext.Msg.wait("Getting the Accounts.... Please Wait..."); 


					Ext.Ajax.request({
					    url: url,
					    params: {
							//organizationCode: organizationCode
							userId : customerId,
							processBillTo : 'false'
							},
					    method: 'POST',
						success: function (response, request){
							var responseText = response.responseText;
							if(responseText.indexOf("Error")>-1)
							{
								alert("Error Getting the Locations. Please try again later");
								Ext.MessageBox.hide();
								$.fancybox.close();
							}
							else
							{
								Ext.MessageBox.hide(); 
								
									document.getElementById('showLocationsDiv').innerHTML = responseText;
									
									 var x = document.getElementById('showLocationsDiv').getElementsByTagName("script");   
									   for( var i=0; i < x.length; i++) {  
										 eval(x[i].text);  
									   }
								
							}	
						},
						failure: function (response, request){
							alert("Error Getting the Locations. Please try again later");
							Ext.MessageBox.hide();
							$.fancybox.close();
						},
					});
		
				}
			}

			
			function showAllBillTo() {
				
				var url = document.getElementById("showLocationsUrl").value;
				var customerId = document.getElementById("customerId").value;
				var organizationCode = document.getElementById("organizationCode").value;
				

				if(url!=null) {

					//Added For Jira 2903
					//Commented for 3475
					//Ext.Msg.wait("Processing..."); 
					//Ext.Msg.wait("Getting the Bill-To Locations.... Please Wait..."); 


					Ext.Ajax.request({
					    url: url,
					    params: {
							//organizationCode: organizationCode
							userId : customerId,
							processBillTo : 'true'
							},
					    method: 'POST',
						success: function (response, request){
							var responseText = response.responseText;
							if(responseText.indexOf("Error")>-1)
							{
								alert("Error Getting the Locations. Please try again later");
								Ext.MessageBox.hide();
								$.fancybox.close();
							}
							else
							{
								
								Ext.MessageBox.hide(); 
									document.getElementById('showBillToDiv').innerHTML = responseText;
									 var x = document.getElementById('showBillToDiv').getElementsByTagName("script");   
									   for( var i=0; i < x.length; i++) {  
										 eval(x[i].text);  
									   }
								   

								   
							}	
						},
						failure: function (response, request){
							alert("Error Getting the Locations. Please try again later");
							Ext.MessageBox.hide();
							$.fancybox.close();
						},
					});
		
				}
			}			

			function showAllShipToLocations() {
				var url = document.getElementById("showShipToLocationUrl").value;
				var customerId = document.getElementById("customerId").value;
				

				
				if(url!=null) {
					//Added For Jira 2903
					//Commented for 3475
					//Ext.Msg.wait("Processing..."); 
					//Ext.Msg.wait("Getting the Ship-To Locations.... Please Wait..."); 


					Ext.Ajax.request({
					    url: url,
					    params: {
							//organizationCode: organizationCode
							userId : customerId,
							orderByAttribute:'ShipToCustomerID'
							},
					    method: 'POST',
						success: function (response, request){
							var responseText = response.responseText;
							if(responseText.indexOf("Error")>-1)
							{
								alert("Error Getting the Locations. Please try again later");
								Ext.MessageBox.hide();
								$.fancybox.close();
							}
							else
							{
								Ext.MessageBox.hide(); 
														
								document.getElementById('showShipToLocationsDiv').innerHTML = responseText;
								 var x = document.getElementById('showShipToLocationsDiv').getElementsByTagName("script");   
								   for( var i=0; i < x.length; i++) {  
									 eval(x[i].text);  
								   }  
							}	
						},
						failure: function (response, request){
							alert("Error Getting the Locations. Please try again later");
							Ext.MessageBox.hide();
							$.fancybox.close();
						},
					});
		
				}
			}

				
	$(document).ready(function() {
		$(document).pngFix();
 
		$("#varSAP").fancybox({
			'onStart'		:	function(){
			//Show location fancybox
			if(document.getElementById("showLocationsUrl").value == '')	
					{	
					 $.fancybox.close();
				}
			else
				{
					//showLocations();
					showAllAccounts();
				}
				},
				'autoDimensions'	: false,
				'width' 			: 800,
				'height' 			: 310,
				//XNGTP - JIRA- 489 
				'onClosed' : function(){				
			    	document.getElementById("showLocationsDiv").innerHTML = '';
			    	//alert("Closed .. " );
			    },		
								
		});	

		$("#varShipTo").fancybox({
			'onStart'		:	function(){
			//Show location fancybox
			if(document.getElementById("showLocationsUrl").value == '')	
					{	
					
					$.fancybox.close();
				}
			else
				{
					//showLocations();
					showAllShipToLocations();
				}
				},
				'autoDimensions'	: false,
				'width' 			: 800,
				'height' 			: 410,
				//XNGTP - JIRA- 489 
				'onClosed' : function(){				
			    	document.getElementById("showShipToLocationsDiv").innerHTML = '';
			    	//alert("Closed .. " );
			    },		
								
		});

		$("#varBillTo").fancybox({
			'onStart'		:	function(){
			//Show location fancybox
			if(document.getElementById("showLocationsUrl").value == '')	
					{	
					
					$.fancybox.close();
				}
			else
				{
					//showLocations();
					showAllBillTo();
				}
				},
				'autoDimensions'	: false,
				'width' 			: 800,
				'height' 			: 400,
				//XNGTP - JIRA- 489 
				'onClosed' : function(){				
			    	document.getElementById("showBillToDiv").innerHTML = '';
			    	//alert("Closed .. " );
			    },		
								
		});		

	});

</script>
</head>


<body class="ext-gecko ext-gecko3">
<div id="main-container">
  <div id="main" class="min-height-fix">
     <s:action name="xpedxHeader" executeResult="true"
		namespace="/common" />
    <div class="container">
      <!-- breadcrumb -->
      <div id="mid-col-mil"> 
      <div class="clearview"> &nbsp;</div>
   <div class="padding-bottom3"><p><span class="page-title"><s:property value="%{name}" /> </span></p>
       </div>
    
          <s:if test="getErrorNames() != null && getErrorNames().size() > 0">
             	<s:iterator value="getErrorNames()" status="errorListStatus" id="listId">
          		<font color = "red"><s:property/></font>
          	</s:iterator>          	          	
          </s:if>
          
          <!--  Hidden hrefs for lightbox to popup from drop down menu select -->
          
          
			<div id="hidden_clicker" style="display:none">
				<a id="varSAP" href="#showLocationsDlg" >Hidden Clicker 1</a>
				<a id="varShipTo" href="#showShipToLocationsDlg" >Hidden Clicker 2</a>
				<a id="varBillTo" href="#showBillToDlg" >Hidden Clicker 3</a>
			</div>
          
          <s:form name="webiPromptForm" id="webiPromptForm" action="processWebiInputPrompt"
                    namespace="/xpedx/services" method="post">
                    <s:hidden name="id" value="%{#_action.getId()}"/>
                    <s:hidden name="cuid" value="%{#_action.getCuid()}"/>
                    <s:hidden name="kind" value="%{#_action.getKind()}"/>
                    <s:hidden name="name" value="%{#_action.getName()}"/>
					<s:hidden name="customerId" id="customerId" value='%{wCContext.customerId}' />
	 				<s:hidden name='organizationCode' id='organizationCode' value='%{wCContext.storefrontId}' />
	 				
                     
         
          <!-- LB CODE Start -->
      
      <div>
          
            <s:url id='showLocationUrlId' action='xpedxShowLocationsForReporting' ></s:url>
		    <s:hidden id="showLocationsUrl" name='showLocationsUrl' value='%{#showLocationUrlId}' />
      			
            <s:url id='showShipToLocationUrlId' action='xpedxGetAssignedCustomersForReporting' >
            </s:url>
		    <s:hidden id="showShipToLocationUrl" name='showShipToLocationUrl' value='%{#showShipToLocationUrlId}' />
		    <s:hidden id="selectedLocationType" name='selectedLocationType' value="All"/>
			<s:hidden id="selectedCustId" name='selectedCustId' value="All"/>

          	<br/>
	
          	
			<s:set name="displayCustomerMap" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@custFullAddresses(wCContext.customerId,wCContext.storefrontId)" />
			 
      	 </div> 
    
		
        <!-- LB CODE End -->
           <table  cellpadding="0" cellspacing="0" width="65%" class="form"  id="order-filter">
           <tr>
           <td> Location:</td>
           
           <td> 
          	<select theme="simple" id="optsLocations" onchange="javascript:showLightBox();">
				<option value="select" selected="yes">All Authorized Locations</option>
				<option value="sap">Accounts </option>
				<option value="billTo">Bill-To</option>
				<option value="shipTo">Ship-To</option>
			</select> 
			</td> 
			<td valign="top" rowspan="6"  style ="padding-left: 5px;" >
		   <s:textarea id="txtLocation" name="txtLocation" cssStyle="width:300px;border:none;overflow:auto;" readonly="true" cols="200" rows="6" value=""/>
		   </td>	
          	
           </tr>
           <tr> <td>&nbsp;</td></tr>   
           <s:iterator value="webiPromptsBean" status="webiPromptsBeanStatus" id="beanId">
           		<s:set name='promptList' value='#beanId.promptValues' />
           		 <s:set name='sufxList' value='#beanId.suffixList' /> 
           		<!-- <s:set name='promptName' value='#beanId.prmtName'/> -->
           		  <s:if test="#beanId.prefix == 'hdn'">
           		  	<s:iterator value='#promptList' status='promptStatus' id='promptId'>
	           			<s:hidden name="%{#beanId.prmtName}" value="%{#promptList.get(0)}"/>	           		
	           		</s:iterator>           		  	
           		  </s:if>
           		  <!-- <s:iterator value='#promptList' id='promptId'>
	           			<s:property value='promptId'/> <br/>           		
	           		</s:iterator> -->
           		  <!--<s:if test="#beanId.prefix == 'ddlb' || #beanId.prefix == 'ddls'">    
	           		  <tr>
	           		  	<td> <s:property value="suffix"/> </td>
	           		  	<td valign="top" ><div class="demo">
	           		  		<s:autocompleter dropdownWidth="500" cssStyle="width:500px" name="%{#beanId.prmtName}" list="sufxList" listKey="suffixValue" listValue="strToDisplay"  	           	
	           		  		id="%{#beanId.prmtName}" theme="simple" headerKey="All Authorised" headerValue="All Authorised" />
	           		  	</div></td> 
	           		  </tr>           		  	        
	           	 </s:if>
	           	 
	           	 -->
	           	 <s:if test="#beanId.prefix == 'mslb' || #beanId.prefix == 'msls'">    
	           		  <tr>
	           		  	<td valign="top"> <s:property value="suffix"/> </td>
	           		  	
	           		  	<td>
	           		  		<s:select name="%{#beanId.prmtName}" id="%{#beanId.prmtName}" theme="simple"  cssClass="x-input"  multiple="true" list="promptList"></s:select>
	           		  	</td>
	           		  </tr>           		  	        
	           	 </s:if>
	           	 
	           	 <s:if test="#beanId.prefix == 'caln'"> 
	           	 
	           	  <tr>
	           		  		<td valign="top" style="padding-right: 5px"><s:property value="suffix"/> </td>
	           		  	
	           		 <td>												   				
	           		  		<s:textfield  theme="simple" size="15" name="%{#beanId.prmtName}" cssClass='calendar-input-fields datepicker' />      		  	 
	           		     		  	
	           		  		&nbsp;<span style="padding-left: 1.5em;"></span>
	           		  	</td> 
	           		  	
	           		  </tr> 
	           	 </s:if>
	           	 
	           	 <s:if test="#beanId.prefix == 'txt'">    
	           		  <tr>
	           		  	<td> <s:property value="suffix"/> </td>
	           		  	
	           		  	<td valign="top">
	           		  		<s:textfield theme="simple" name="%{#beanId.prmtName}" cssStyle="width:150px;" cssClass="search-by-input-field"/>
	           		  	
	           		  	</td>
	           		  </tr>  
	           		  <tr> <td>&nbsp;</td></tr>         		  	        
	           	 </s:if> 
           </s:iterator>
		
          </table>
          <s:url id='backLink' namespace='/xpedx/services' action='XPEDXReports'>
		</s:url>


				
          <div class="clearview">&nbsp;</div>
          <div class="form-service"> 
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>&nbsp;</td>
                <td>  <div id="cart-actions" >
            <ul id="cart-actions" class="float-right">
              <li class="float-left"><s:a href="%{backLink}" cssClass="grey-ui-btn"><span>Cancel</span></s:a></li>
              <li><a href="#" onclick="return submitReportForm();" class="orange-ui-btn"><span>Submit Report</span></a></li>
            </ul>
          </div></td>
              </tr>
              </table>
            </div>
          
         </s:form>
<div class=" bot-margin">&nbsp;</div>  
      
</div>
      <!-- End Pricing -->
      <br />
    </div>
  </div>
</div>
    <!-- LB CODE Start -->
<div style="display: none;">
	<div title="Showing the Locations" id="showLocationsDlg">
		<div id="showLocationsDiv">
			
		</div>
	
	</div>
</div>
<div style="display: none;">
	<div title="Showing the Bill-To Locations" id="showBillToDlg">
		<div id="showBillToDiv">
			
		</div>
	
	</div>
</div>
<div style="display: none;">	
	
	<div title="Showing the Ship-To Locations" id="showShipToLocationsDlg">
		<div id="showShipToLocationsDiv">
			
		</div>
	
	</div>

</div>
<!-- LB CODE End -->
    <!-- end main  -->
    <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
<!-- end container  -->
</body>


</swc:html>
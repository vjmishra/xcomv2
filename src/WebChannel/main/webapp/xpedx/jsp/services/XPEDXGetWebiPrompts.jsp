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

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" /> 

<!-- styles -->
<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/banner<s:property value='#wcUtil.xpedxBuildKey' />.css"/>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jQuery<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/jquery-1.4.2<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.datepicker<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


<!-- carousel scripts css  -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/skin<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/demo<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shopping-cart<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/om2<s:property value='#wcUtil.xpedxBuildKey' />.css" />


<!--  Change Location Light box -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/org/xpedxCustomerLocations<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


<title><s:property value="wCContext.storefrontId" /> - Webi Input Prompt</title>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


<s:set name='_action' value='[0]' />
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />
<s:set name='sdoc' value="outputDoc" />
<s:set name='extnElem' value='#xmlUtil.getChildElement(#sdoc, "Extn")'/>



<script type="text/javascript">

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
	
		
		
		var i=1;
		var retVal=true;
		for(;;i++)
		{
			var dateTextList=document.getElementById(i);
			if(dateTextList == null || dateTextList == undefined)
			{
				break;
			}
			else
			{
				var dateText=dateTextList.value;
				var divid="error_"+dateTextList.id;
				var divObje=document.getElementById(divid);
				divObje.style.display = "none";
				if(dateText != "" && !validateDate(dateText))
				{
					divObje.style.display = "block";
					retVal=false;
				}
			}
		}
		if(retVal ==false)
		{
			return false;
		}
		else
		{	
			document.webiPromptForm.submit();
		}

	}	
	function validateDate(date)
{
    var matches = /^(\d{2})[-\/](\d{2})[-\/](\d{4})$/.exec(date);
    if (matches == null) return false;
    var d = matches[2];
    var m = matches[1] - 1;
    var y = matches[3];
    var composedDate = new Date(y, m, d);
    return composedDate.getDate() == d &&
            composedDate.getMonth() == m &&
            composedDate.getFullYear() == y;
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
				'width' 			: 740,
				'height' 			: 370,
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
          
          <s:form name="webiPromptForm" id="webiPromptForm" action="processinputprompt"
                    namespace="/services" method="post">
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
      			
            <s:url id='showShipToLocationUrlId' action='xpedxGetAssignedCustomersForReporting' namespace='/services' >
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
            <s:set name="dateFieldCount" value="%{1}" />   
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
	           		  		<s:textfield  id="%{#dateFieldCount}" theme="simple" size="15" cssClass='calendar-input-fields datepicker' name="%{#beanId.prmtName}"/>    
	           		  		<div id="error_<s:property value='%{#dateFieldCount}' />" style="display:none;">
	           		  			<h5><font color="red">Please enter date in MM/DD/YYYY format</font></h5>
	           		  		</div>  		  	 
	           		     	<s:set name="dateFieldCount" value="%{#dateFieldCount + 1}" /> 	  	
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
          <s:url id='backLink' namespace='/services' action='myreports'>
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

<script type="text/javascript">
<s:if test="getRenderReport() == 'true'">

var strUrl = '<s:property value="finalURL" escape="false"/>';

//var viewAs = '<s:property value="viewReportAs"/>'

//var strUrl = "/swc/xpedx/jsp/services/XPEDXDisplayReport.jsp?viewReportAs="+viewAs;
//strUrl += "&timeOutx=100&zoom=100&sNewDoc=false&sApplyFormat=&iDPIndex=&bValidateSQL=false&nAction=&advPrompts=yes&bCreateDefaultReportBody=false&defaultRepTitle=Report%2BTitle";
//Added For Jira 2810
//window.open(strUrl,'','toolbar=0');
//XBT-346 - Added  ReplaceAll() so that the report is not rendered on refresh
var reportUrl= window.location.href;
reportUrl = ReplaceAll(reportUrl,"renderReport=true", 'renderReport=false');
window.open(strUrl);	
window.location.href = reportUrl;
</s:if>

function ReplaceAll(Source,stringToFind,stringToReplace){
  var temp = Source;
    var index = temp.indexOf(stringToFind);
        while(index != -1){
            temp = temp.replace(stringToFind,stringToReplace);
            index = temp.indexOf(stringToFind);
		}
        return temp;
}
</script>


</swc:html>
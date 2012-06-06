<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<% request.setAttribute("isMergedCSSJS","true"); %>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:set name='ctxt' value="getWCContext()" />
<s:set name="CurrentCustomerId"
	value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />

<s:set name='userListResId' value='"/swc/profile/ManageUserList"' />

<s:set name='manageOrgResId'
	value='"/swc/profile/ManageOrganizationProfile"' />
<s:bean
	name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils"
	id="wcUtil" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header.js"></script>		
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder.js"></script>
<!-- styles -->

<!-- begin styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/ADMIN.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />
<![endif]-->
<!-- end styles -->

<!-- 
<script type="text/javascript"
	src="../../xpedx/js/global/ext-base.js"></script>
<script type="text/javascript"
	src="../../xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="../../xpedx/js/swc.min.js"></script>
<script type="text/javascript"
	src="../../xpedx/js/global/validation.js"></script>
<script type="text/javascript" 
	src="../../xpedx/js/common/ajaxValidation.js"></script>
<script type="text/javascript"
	src="<s:url value='/xpedx/js/global/dojo.js'/>"></script>
<script type="text/javascript"
	src="../../xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript"
	src="../../xpedx/js/theme/theme-1/theme.js"></script>

<script type="text/javascript"
	src="../../xpedx/js/catalog/catalogExt.js"></script>
<script type="text/javascript" src="../../xpedx/js/swc.js"></script>
<script type="text/javascript"
	src="../../xpedx/js/jquery-ui-1/development-bundle/jquery-1.4.2.js"></script>

<script type="text/javascript"
	src="../../xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript"
	src="../../xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
	javascript -->
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/profile.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/address/editableAddress.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/verifyAddress.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/user/user.js"></script>
<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag.js"></script>
<!-- Web Trends tag end  -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs.js"></script>

<!-- Facy Box (Lightbox/Modal Window
<script type="text/javascript"
	src="<s:url value='/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js'/>"></script>
<script type="text/javascript"
	src="<s:url value='/xpedx/js/fancybox/jquery.fancybox-1.3.4.js'/>"></script>
	-->
<script type="text/javascript" 
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric.js"></script>
	 
<script type="text/javascript" 
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.maskedinput-1.3.js"></script>
<!--  to restrict numeric values in spending limit  -->
	
<!-- Facy Box (Lightbox/Modal Window -->

<!-- JIRA 1878 -->
<s:url id='assignedShipToURL' namespace='/common'
	action='xpedxGetAssignedCustomersForDefaultShipTo' />
<s:url id='assignedShipToURLforUserProfile' namespace='/common'
	action='xpedxGetAssignedCustomersForUserProfile' />
<s:url id="xpedxGetCustomerLocations" action="xpedxGetCustomerLocations"
	namespace="/profile/user" />

<s:set name="selectedTab" value="#request.selectedTab" />
<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sorttable.js"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/SpryTabbedPanels.js"
	type="text/javascript"></script>
	<!-- 
<script type="text/javascript" src="../../xpedx/js/jquery.numeric.js"></script>
<!--  to restrict numeric values in spending limit  
<script type="text/javascript" src="../../xpedx/js/jquery-tool-tip/jquery-ui.min.js"></script><!-- jira 1833 -->
<script type="text/javascript">
    	
function getNewContactInfo1(url){
 	document.userListaddForm.action = url;//JIRA 3917
	document.userListaddForm.submit();//JIRA 3917
	} 

function loadLocations(userId) {
	if(userId != "")
	{
		param={loggedInUserId : userId };
	}
	else{
		param={customerID : '<s:property value="wCContext.loggedInUserId" escape='false'/>'}
	}
	if(document.getElementById('LocationsLoaded')!=null && document.getElementById('LocationsLoaded').value == 'false') {
		Ext.Msg.wait("Loading Locations.... Please Wait!!!");
		Ext.Ajax.request({
	        url: '<s:property value="#xpedxGetCustomerLocations" escape='false'/>',
	        params: param,
	        method: 'POST',
	        success: function (response, request){
		       		Ext.MessageBox.hide();
	        		document.getElementById('Locations').innerHTML = "";
			        document.getElementById('Locations').innerHTML = response.responseText;
	            },
	        failure: function (response, request){
	            	Ext.MessageBox.hide();
	            	document.getElementById('Locations').innerHTML =
		            			"Failed to Load Locations for this particular User Try again Later";		            	 
	             }
	    });
	}
	
}
function showAssignedShipTos(billToID){
	var shipToDiv = document.getElementById(billToID);
	switch(shipToDiv.style.display){
	case 'none':
		shipToDiv.style.display = 'block';
		break;
	case 'block':
		shipToDiv.style.display = 'none';
		break;
	}
}
function showShipTo(url)
{
//JIRA 1878
 var selected_customerContactId=document.getElementById("userName").value;
 
	Ext.Ajax.request({
        url :url,
        params: {
                    customerContactId: selected_customerContactId
                 },
        method: 'POST',
        success: function (response, request){
		document.getElementById('ajax-assignedShipToCustomers').innerHTML = response.responseText;
   	 	},
    	failure: function (response, request){
   	 		document.getElementById('ajax-assignedShipToCustomers').innerHTML = response.responseText;
        }
    });       
}
//Added for JIRA 2737
function showShipToForUserProfile(url)
{
//JIRA 1878
 var selected_customerContactId=document.getElementById("userName").value;
 
	Ext.Ajax.request({
        url :url,
        params: {
                    customerContactId: selected_customerContactId
                 },
        method: 'POST',
        success: function (response, request){
		document.getElementById('shipToUserProfile').innerHTML = response.responseText;
   	 	},
    	failure: function (response, request){
   	 		document.getElementById('shipToUserProfile').innerHTML = response.responseText;
        }
    });       
}
var selectedTabPanel = '<s:property value="#selectedTab" />';
$(document).ready(function() 
		{$(document).pngFix();
		
		var TabbedPanels2 = new Spry.Widget.TabbedPanels("TabbedPanels1");
		var showPanelVal = 0;
		if(selectedTabPanel!=null && selectedTabPanel.length>0){
			showPanelVal = Number(selectedTabPanel);
		}
		TabbedPanels2.showPanel(showPanelVal);
		$("#varous1").fancybox();
		$("#various2").fancybox();
		$("#various3").fancybox();
		$("#various4").fancybox();
		$("#various5").fancybox();
		$("#various6").fancybox();
		$("#various7").fancybox();
		$("#various8").fancybox();
		$("#various9").fancybox();
		$("#various10").fancybox();
		$("#various11").fancybox();
		$("#various12").fancybox();
		$("#various13").fancybox();
		$("#various14").fancybox();
		$("#various15").fancybox();
		$("#various16").fancybox();
		$("#various17").fancybox();
		$("#various18").fancybox();
		$("#various19").fancybox();
		$("#various20").fancybox();
		$("#various21").fancybox();
		$("#various22").fancybox();
		$("#selectusertomodifyProfile").fancybox({
			'onStart' 	: function(){
			showUsers('<s:property value="#CurrentCustomerId"/>');		
			
			},
			'autoDimensions'	: false,
			'width' 			: 800,
			'height' 			: 600  
		});
		$("#changeShipTo").fancybox({
			'onStart'	:	function(){
			showShipTo('<s:property value="#assignedShipToURL"/>');
			},
			'autoDimensions'	: false,
			'width' 			: 800,
			'height' 			: 600  
			});
		$("#changeShipToForUserProfile").fancybox({
			'onStart'	:	function(){			
			showShipToForUserProfile('<s:property value="#assignedShipToURLforUserProfile"/>');
			},
			'autoDimensions'	: false,
			'width' 			: 751,
			'height' 			: 350  
			});
		$("#addNewQL").fancybox({
			'onStart'		:	function(){
			document.getElementById("operation").value = "addoperation";
			document.getElementById("linkName").value = "";
			document.getElementById("url").value = "";
			},
			'onComplete':	function(){
				document.getElementById("linkName").focus();
			},
			'titleShow'			: false,
			'transitionIn'		: 'fade',
			'transitionOut'		: 'fade',
			'titlePosition' : 'inside',
			'transitionIn' : 'none',
			'transitionOut' : 'none'
								
		});
		$('#spendingLt').numeric(".",false);
		$('#spendingLt').numeric(false); 
		$('.phone-numeric').numeric(false); 
		$("#dayPhone_new").mask("999 999-9999");
		$("#dayFaxNo_new").mask("999 999-9999");
});

</script>
<!-- 
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
	 -->
<script type="text/javascript">
xyz = function(){
	expPhnPnl = '<s:property value="#expandPhonePanel" />';
	expAddPnl = '<s:property value="#expandAddressPanel" />';
}
</script>
<!-- Facy Box (expand and collpse Modal Window -->

<script type="text/javascript">

var isPasswordChanged = false;


 $(document).ready(function() {
     $('#tree').checkboxTree();
     $('#collapseAllButtonsTree').checkboxTree({
         collapseAllButton: 'Collapse All',
         expandAllButton: 'Expand All'
     });
 });
			
function funDivOpenClose(val1)
{
	for(i=1;i<=2;i++)
	{
		document.getElementById("strdiv"+i).style.display="none";
	}
	document.getElementById("strdiv"+val1).style.display="block";
}



		function blockRow(tableID) {	
		    
			var table = document.getElementById(tableID);

			var rowCount = table.rows.length;
			rowCount=rowCount-2;
			//alert(rowCount);
			var element = document.getElementById(rowCount);
			element.onclick = new Function(""); 
			element.value="";
			
		}
		
		function deleteQuickLinkRow(rowId) {
			try {
				var table = document.getElementById("tb1").getElementsByTagName("tbody")[0];
				var rowCount = table.rows.length;
				if(rowId > 0 && rowId < rowCount){
					var deletedSeqIndex = 0;
                    deletedSeqIndex = table.rows[rowId].cells[3].childNodes[0].selectedIndex;
					document.getElementById("tb1").deleteRow(rowId);
					rowCount = table.rows.length;
					for(var i=rowId; i<rowCount; i++) {
		                var row = table.rows[i];
	                    row.cells[0].childNodes[0].setAttribute('href', 'javascript:deleteQuickLinkRow('+(i)+');');
					}
					for(var i=1; i<rowCount;i++){
						var row = table.rows[i];
						var selectSeq = row.cells[3].childNodes[0];
	                    var selectSeqIndex = selectSeq.selectedIndex;
	                    var options = selectSeq.options;
	                    if(selectSeqIndex > deletedSeqIndex){
		                    selectSeq.selectedIndex = selectSeq.selectedIndex - 1;
		                    //selectSeq.setAttribute('name', 'QuickLinksSeq' + i);
		                    selectSeq.setAttribute('id', 'QuickLinksSeq' + i);
	                    }
						else{
									//options[options.length-1] = null; //Jira 3630
								 options[options.length] = null
						}
					}
				}
			}catch(e) {
				alert(e);
			}
		}

		function addPOToList(){
			var newPO = document.getElementById("addToPOListName").value;
			var pol = document.getElementById("POList");
			document.getElementById("POListText").value = document.getElementById("POListText").value+newPO+",";
		}

		function addEmailToList(){
			var newEmail = document.getElementById("confEmailText").value;
			document.getElementById("AddnlEmailAddrText").value = document.getElementById("AddnlEmailAddrText").value+newEmail+",";
		}
		
		function confirmEmail() {
			var tableele = document.getElementById('userDataTable');
			var tabletr = tableele.rows[6];//Gets the the tr element with email input element
			var emailAddr = tabletr.cells[1].firstChild;
			var emailConfAddr = tabletr.cells[3].firstChild;
			var emailval = emailAddr.value;
			var confval = emailConfAddr.value;
			if(emailval != confval){
				alert('The Emails do not match. Please check if the entered Emails are the same.');
				emailConfAddr.focus();
			}
			return true;
		}

		function verifyURLEntry()
		{
			var link = document.getElementById("urlValue").value;
			var urlN = 	document.getElementById("urlValue").value;

			if(urlN.indexOf("htt",0) < 0)
			{
				urlN = "http://" + urlN;
				document.getElementById("urlValue").value = urlN;
			}
		}
		
		<%-- Save Quick Link into table tb1--%>
		function saveClick() {
			var link = document.getElementById("theLinkName").value;
			var urlN = 	document.getElementById("theUrl").value;

			if(urlN.indexOf("htt",0) < 0)
			{
				urlN = "http://" + urlN;
			}

				
		
			//$.fancybox.close();
			var tbody = document.getElementById("tb1").getElementsByTagName("tbody")[0];
			var rowCount = tbody.rows.length;
			
			var theCreateRow = document.getElementById("inLineChangeRow");
			tbody.removeChild(theCreateRow);
			
			var row = null;
			
			if (rowCount%2 == 0) {
				row = document.createElement("tr")
				row.setAttribute('class', 'odd');
			} else {
				row = document.createElement("tr")
			}
			
			var data1 = document.createElement("td");		
			data1.setAttribute('class', ' noBorders padding-left1');

			var deleteLink = document.createElement('a');
			var imageIcon = document.createElement('img');
			imageIcon.setAttribute('src','<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_red_x.png');
			imageIcon.setAttribute('alt','delete');
			imageIcon.setAttribute('title','Remove');
			imageIcon.setAttribute('width','16');
			imageIcon.setAttribute('height','16');
			imageIcon.setAttribute('border','0');
			imageIcon.setAttribute('style','float:left');
			deleteLink.appendChild (imageIcon);
			var tablelen = document.getElementById("tb1");
			var table = document.getElementById("tb1").getElementsByTagName("tbody")[0];
			var rowCount = table.rows.length;
			deleteLink.setAttribute('href', 'javascript:deleteQuickLinkRow('+rowCount+');');
			data1.appendChild(deleteLink);
			var element2 = document.createElement("input");
			element2.type = "text";
			element2.name = "urlName";
			element2.value = link;
			element2.setAttribute('class','x-input user-profile-input');	
			element2.setAttribute('style','width:220px;');
			element2.setAttribute('tabindex','12');
			element2.setAttribute('maxlength','35');
			data1.appendChild(element2);

			var data2 = document.createElement("td");
			data2.setAttribute('valign', 'top');
			var element3 = document.createElement("input");
			element3.type = "text";
			element3.name = "urlValue";
			element3.id = "urlValue";	
			element3.value = urlN;
			element3.setAttribute('class','x-input user-profile-input');	
			element3.setAttribute('style','width: 330px;');
			element3.setAttribute('tabindex','12');		
			element3.setAttribute('maxlength','150');
			element3.setAttribute('onblur','verifyURLEntry()');
			data2.appendChild(element3);

			var data3 = document.createElement("td");
			data3.setAttribute('valign', 'top');
			var element4 = document.createElement("input");  
			element4.type = "checkbox";
			element4.name = "showURL";  	
			element4.setAttribute('class', ' margin-15');
			element4.checked = true;
			element4.setAttribute('tabindex','12');	
			data3.appendChild(element4);
			

			var data4 = document.createElement("td");
			data4.setAttribute('valign', 'top');

			var combo = document.createElement("select");
			combo.setAttribute('name','combo');
			combo.setAttribute('class', 'x-input');
			combo.setAttribute('tabindex','12');
			combo.setAttribute('onfocus','this.oldvalue = this.value;');
			combo.setAttribute('onchange', 'javascript:onChangeItemOrder(this, this.oldvalue, this.value);');
			combo.id = 'QuickLinksSeq'+rowCount;

			for(var i=1; i<=rowCount; i++) {
				var option = document.createElement("option");  
			    option.text = i;  
			    option.value = i;  
			    if(i == rowCount) {
				    option.selected = true;
			    }
			    try {  
			        combo.add(option, null); //Standard  
			    }catch(error) {  
			        combo.add(option); // IE only  
			    }
			}

			//Adding one more option to the previous rows select seq options 
			for(var i=1; i<rowCount; i++){
				var selectSeq = tbody.rows[i].cells[3].childNodes[0];
				var option = document.createElement("option");
				option.text = rowCount;  
			    option.value = rowCount;
			    try {  
			    	selectSeq.add(option, null); //Standard  
			    }catch(error) {  
			    	//selectSeq.add(option); // IE only  
			    }
			}
		     			
			data4.appendChild (combo)
			        		
			row.appendChild(data1);
			row.appendChild(data2);
			row.appendChild(data3);
			row.appendChild(data4);
			tbody.appendChild(row);		
		}

		function callSave(docDivId, ignoreDivIds) {
			resetCallSaveDiv();
			
			try{
				if (mandatoryFieldValidation(docDivId, ignoreDivIds) != "")
				{
					new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
					//All mandatory fields are in first panel , it has to be customized if the mandatory fields are spread across tabs
					return false;
				}
			}catch(err){
			}

			var isSelfAdmin = document.getElementById("selfAdminId").value;
			if(isSelfAdmin == "true"){
			
				var errorDiv = document.getElementById("pwdErrorDiv");
	            if(errorDiv){
	                return false;   
	            } 
	            
				if(document.getElementById("userpassword")!=null && document.getElementById("userpassword").value!=document.getElementById("confirmpassword").value)
			    {
					new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
					document.getElementById("errorMsgFor_userpassword").style.display = "inline";
					document.getElementById("successMsgFor_save").innerHTML = "";
					document.getElementById("successMsgFor_save").style.display = "none";
					return false;
			         
			    }
				if(document.getElementById("secretQuestion")!=null && (document.getElementById("secretQuestion").value==""
					|| document.getElementById("secretQuestion").value=="-1"))
			    {
					new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
			        alert('Please select Your Security Question');
			        document.getElementById("secretQuestion").focus();
			        return false;    
			    }
				if(document.getElementById("secretAnswer")!=null && document.getElementById("secretAnswer").value!=document.getElementById("confirmAnswer").value)
			    {
					new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
					document.getElementById("errorMsgFor_secretAnswer").style.display = "inline";
					return false;    
			    }
			}
			
		if(document.getElementById("emailId")!=null){
			var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[(2([0-4]\d|5[0-5])|1?\d{1,2})(\.(2([0-4]\d|5[0-5])|1?\d{1,2})){3} \])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			var email=document.getElementById("emailId").value;
			if(!re.test(email)){
				new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
		    	document.getElementById("errorMsgFor_validemailId").style.display = "inline";
		        return false;
			}
			
		    len=email.length;
		    var no=0;
		    for(var i=0;i<len;i++)
		    {
		        if(email.charAt(i)=="@" || email.charAt(i)==",")
		        {
		            //alert(i);
		            no=no+1;
		            //alert(no);
		        }
		    }
		    var dat=Date();
		    //alert(dat);
		    if (email.indexOf("@") < 0)
		    {
		    	new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
		        alert("Please enter valid Email address!");
		        document.getElementById("emailId").focus();
		        return false;
		    }
		    // Email Checker
		    else if (email.indexOf(".") < 0)
		    {
		    	new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
		        alert("Incorrect email address. Please re-enter!");
		        document.getElementById("emailId").focus();
		        return false;
		    }
		    // Email Checker
		    else if (email.indexOf(" ") >= 0)
		    {
		    	new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
		        alert("Incorrect email address. Please re-enter!");
		        document.getElementById("emailId").focus();
		        return false;
		    }
		    else if (len > 50)
		    {
		        //length1=email.length;
		        //alert(length1);
		        new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
		        alert("Email should not exceed more than 50 characters!");
		        document.getElementById("emailId").focus();
		        return false;
		    }
		    else if (no >= 2)
		    {
		        //length1=email.length;
		        //alert(length1);
		        new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
		        alert("Please do not enter more than one email address!");
		        document.getElementById("emailId").focus();
		        return false;
		    }            	

		    var confirmemail=document.getElementById("confirmEmailId").value;
		    if(email!=confirmemail){
		    	new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(0);
		    	document.getElementById("errorMsgFor_emailId").style.display = "inline";
		        return false;
		    }
		}

		var spendingLimit="";
		if(document.getElementById("spendingLt") != null){
			spendingLimit = document.getElementById("spendingLt").value;
		}
		
		// Added AND condition for jira 2055
		if(spendingLimit!="" && spendingLimit != null && (document.getElementById("primaryApprover") !=null)){

			var primaryApprover = document.getElementById("primaryApprover").value;
			var alternateApprover = '';
			if(document.getElementById("alternateApprover") != undefined || document.getElementById("alternateApprover") != null){
				alternateApprover = document.getElementById("alternateApprover").value;	
			}
			var currency = document.getElementById("spendingLtCurrency").value;
			if(primaryApprover==null || primaryApprover==""){
				if (alternateApprover==null || alternateApprover==""){
					new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(3);
			    	document.getElementById("errorMsgFor_approvers").style.display = "inline";
					return false;
				}
			}
			if(currency==null || currency==""){
				new Spry.Widget.TabbedPanels("TabbedPanels1").showPanel(3);
				if(document.getElementById("errorMsgFor_approvers")!=null){
					document.getElementById("errorMsgFor_approvers").style.display = "none";
				}
		    	document.getElementById("errorMsgFor_currency").style.display = "inline";
				return false;
			}
		}
		    /*var tbody = document.getElementById("tb1").getElementsByTagName("tbody")[0];
			var rowCount = tbody.rows.length;
			var invalidOrdering = false;
		  //Checking if two quick links have the same order
			for(var i=1; i<rowCount-1; i++){
				var selectSeq = tbody.rows[i].cells[3].childNodes[0];
				var selectedOption = selectSeq.selectedIndex;
				for(var j=i+1; j<rowCount;j++){
					var selectSeqJ = tbody.rows[j].cells[3].childNodes[0];
					var selectedOptionJ = selectSeqJ.selectedIndex;
					if(selectedOption == selectedOptionJ)
						invalidOrdering = true;
				}
			}

			if(invalidOrdering)
			{
				alert("Please enter a valid ordering of Quick Links in 'Manage Site Settings' tab.");
				document.getElementById("tb1").getElementsByTagName("tbody")[0].rows[1].cells[3].childNodes[0].focus();
		        return false;
			}*/

		    var lboTo=document.myAccount.customers2;
			for ( var i=0; lboTo!=null && i < lboTo.options.length; i++ )
			{
				lboTo.options[i].selected = true;
			}
			lboTo=document.myAccount.customers1;
			for ( var i=0; lboTo!=null && i < lboTo.options.length; i++ )
			{
				lboTo.options[i].selected = true;
			}

			var tbody = document.getElementById("tb1");
			var rowCount = tbody.rows.length;
			var comleteData = "";

			for(var i=1; i<=rowCount; i++) {
				var row = tbody.rows[i];
				if (null != row) {
	    			var cellsCount = row.cells.length;    			
	    			for(var j=0; j<cellsCount; j++) {
	    				var temp = "";        				 
	        			if (j==3) {
	        				temp = row.cells[j].childNodes[0].value;
	        			} else {
	        				if(j==2) {
	            				var checkbox = row.cells[j].childNodes[0];
	            				if(null != checkbox && true == checkbox.checked){
	            					temp = "Y" ;
	            				}
	            				else
	            					temp = "N" ;
	            				
	            			}
	        				else if(j==0){
	        					temp = row.cells[j].childNodes[1].value;
	        				}
	            			else {
	            				temp = row.cells[j].childNodes[0].value;
	            			}        				
	        			}               		
	        			
	        			if(temp == "") {
	            			temp = "*#?";
	        			}        
				
	        			comleteData = comleteData + "||" + temp;                			                			
	    			}
				}
				            		
			} 
			      
			document.getElementById('bodyData').value = comleteData;
			
			document.getElementById("myAccount").submit();
		
		    return true;
			  	            	                        	            	
		}

		function resetCallSaveDiv()
		{
			if(document.getElementById("errorMsgFor_emailId") != null)
			{
				document.getElementById("errorMsgFor_emailId").style.display = "none";
			}
			if(document.getElementById("errorMsgFor_validemailId") != null)
			{
				document.getElementById("errorMsgFor_validemailId").style.display = "none";
			}
			if(document.getElementById("errorMsgFor_validFaxNo") != null)
			{
				document.getElementById("errorMsgFor_validFaxNo").style.display = "none";
			}
			if(document.getElementById("errorMsgFor_validPhoneNo") != null)
			{
				document.getElementById("errorMsgFor_validPhoneNo").style.display = "none";
			}
			if(document.getElementById("errorMsgFor_secretAnswer") != null)
			{
				document.getElementById("errorMsgFor_secretAnswer").style.display = "none";
			}
			if(document.getElementById("errorMsgFor_userpassword") != null)
			{
				document.getElementById("errorMsgFor_userpassword").style.display = "none";
			}
			if(document.getElementById("errorMsgFor_approvers") != null)
			{
				document.getElementById("errorMsgFor_approvers").style.display = "none";
			}
			if(document.getElementById("errorMsgFor_currency") != null)
			{
				document.getElementById("errorMsgFor_currency").style.display = "none";
			}
			if(document.getElementById("errorNote") != null)
			{
				document.getElementById("errorNote").style.display = "none";
			}
			if(document.getElementById("successMsgFor_save") != null)
			{
				document.getElementById("successMsgFor_save").style.display = "none";
			}
		}
		
		function testFieldValueCheck(component, docDivId){
			var mandatoryFieldCheckFlag = document.getElementById("mandatoryFieldCheckFlag_"+docDivId).value;
			if(mandatoryFieldCheckFlag=="true"){
				if(component.value==""){
	            	component.style.borderColor="#FF0000";
	        	}
	        	else{
	            	component.style.borderColor="";
	    	    }
	    	}
	    }

		function confirmFieldValidation(component, compareWith){
			if(document.getElementById(compareWith).value!=""){
				if(component.value!=document.getElementById(compareWith).value){
	            	component.style.borderColor="#FF0000";
	            	document.getElementById("errorMsgFor_"+compareWith).style.display = "inline";
	        	}
	        	else{
	            	component.style.borderColor="";
	            	document.getElementById("errorMsgFor_"+compareWith).style.display = "none";
	    	    }
	    	}
	    }
	
		function addToPOListfn(){
			var poTextElement = document.getElementById('addToPOListName');
			var poText = poTextElement.value;
			var selectElement = document.getElementById('selectPOName');
			var invalidPO = false;
			
			for(var i=0;i<selectElement.options.length;i++)
			{
				if ((selectElement.options[i].value == poText ) )
				{
					alert('Please enter a unique Purchase order.');
					poTextElement.focus();
					invalidPO = true;
				}
			}
			
			if(poText == null || poText == ''){
				alert('Please give a name for the Purchase order.');
				poTextElement.focus();
				invalidPO = true;
			}

			if(!invalidPO){
				addPOToList();
				
				var elOptNew = document.createElement('option');
				elOptNew.text = poText;
				elOptNew.value = poText;
				
				try {
					selectElement.add(elOptNew, null); // standards compliant; doesn't work in IE
					poTextElement.value = '';
					poTextElement.focus();
				}
				catch(ex) {
					selectElement.add(elOptNew); // IE only
					poTextElement.value = '';
					poTextElement.focus();
				}
			}			
			return false;
		}

		/*function removeFrmPOList(){
			var elSel = document.getElementById('selectPOName');
			var selectedPO = '';
			var i;
			for (i = elSel.length - 1; i>=0; i--) {
			  if (elSel.options[i].selected) {
				selectedPO = elSel.options[i].value;
			    elSel.remove(i);
			  }
			}
			var POlistText = document.getElementById("POListText").value;
			var POListArray = POlistText.split(",");
			var POListNewText = null;
			for(var i=0; i<POListArray.length; i++) {
				if(POListArray[i] != selectedPO && POListArray[i].trim().length>0)
				{
					if(POListNewText == null)
						POListNewText = POListArray[i]+",";
					else
						POListNewText = POListNewText+","+POListArray[i]+",";
				}
			}
			document.getElementById("POListText").value = POListNewText;
			return false;
		}*/

		function removeFrmPOList(){
			var poSelectElement = document.getElementById('selectPOName');
			var removedPOElems = new Array();
			var j =0;
			if(poSelectElement!=null && poSelectElement.options!=null){
				for ( var i=0; i < poSelectElement.options.length; i++ )
				{
					if ((poSelectElement.options[i].selected == true ) )
					{
						strItemToRemoveVal = poSelectElement.options[i].value;
						poSelectElement.options[i] = null;
						removedPOElems[j++] = strItemToRemoveVal;
						i--;
					}
				}
			}

			if(removedPOElems.length>0){
				var POlistText = document.getElementById("POListText").value;
				var POListArray = POlistText.split(",");
				var POListNewText = null;
				for(var i=0; i<POListArray.length; i++) {
					var currentPO = POListArray[i];
					if(currentPO=="")
						continue;
					var isCurrentPORemoved = false;
					for(var k=0;k<removedPOElems.length;k++)
					{
						var removedPO = removedPOElems[k];
						if(currentPO==removedPO)
						{
							isCurrentPORemoved = true;
						}
					}
					if(!isCurrentPORemoved)
					{
						if(POListNewText == null || POListNewText == "")
							POListNewText = POListArray[i]+",";
						else
							POListNewText = POListNewText+POListArray[i]+",";
					}
					
				}
				document.getElementById("POListText").value = POListNewText;
			}
			return false;
		}

		function addOrderConfEmailToList(){
			var emailTextElement = document.getElementById('confEmailText');
			if(emailTextElement!=null && emailTextElement.value.length>499){
				alert('Please enter a email address of length not exceeding 500.');
				emailTextElement.focus();
				invalidEmail = true;
			}
			var emailText = emailTextElement.value;
			var selectElement = document.getElementById('selectOrderConfEmails');
			var invalidEmail = false;
			
			var totallength = 0;
			for(var i=0;i<selectElement.options.length;i++)
			{
				totallength = totallength+selectElement.options[i].value.length;
				if ((selectElement.options[i].value == emailText ) )
				{
					alert('Please enter a unique email address.');
					emailTextElement.focus();
					invalidEmail = true;
				}
			}
			totallength = totallength+emailText.length;
			if(totallength > 500){
				alert('Length exceeds the maximum allowed value of 500.');
				emailTextElement.focus();
				invalidEmail = true;
			}
			if(emailText == null || emailText == ''){
				alert('Please enter a valid email address.');
				emailTextElement.focus();
				invalidEmail = true;
			}
			var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[(2([0-4]\d|5[0-5])|1?\d{1,2})(\.(2([0-4]\d|5[0-5])|1?\d{1,2})){3} \])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			if(!re.test(emailText)){
				alert('Please enter a valid email address.');
				emailTextElement.focus();
				invalidEmail = true;
			}
			if(!invalidEmail)
			{
				addEmailToList();
				var elOptNew = document.createElement('option');
			  	elOptNew.text = emailText;
			  	elOptNew.value = emailText;

			  	try {
					selectElement.add(elOptNew, null); // standards compliant; doesn't work in IE
					emailTextElement.value = '';
					emailTextElement.focus();
			  	}
			  	catch(ex) {
					selectElement.add(elOptNew); // IE only
					emailTextElement.value = '';
					emailTextElement.focus();
			  }
			}
			return false;
		}

		/*function removeOrderConfEmailFrmList(){
			var elSel = document.getElementById('selectOrderConfEmails');
			var selectedEmail = '';
			
			for (var i = 0; i< elSel.length; i++) {
			  if (elSel.options[i].selected) {
				  selectedEmail = elSel.options[i].value;
			  	  elSel.remove(i);
			  }
			}
			var AddEmailListText = document.getElementById("AddnlEmailAddrText").value;
			var AddEmailListArray = AddEmailListText.split(",");
			var AddEmailListNewText = null;
			for(var i=0; i<AddEmailListArray.length; i++) {
				if(AddEmailListArray[i] != selectedEmail && AddEmailListArray[i].trim().length>0)
				{
					if(AddEmailListNewText == null)
						AddEmailListNewText = AddEmailListArray[i]+",";
					else
						AddEmailListNewText = AddEmailListNewText+","+AddEmailListArray[i]+",";
				}
			}
			document.getElementById("AddnlEmailAddrText").value = AddEmailListNewText;
			return false;
		}*/

		function removeOrderConfEmailFrmList()
		{
			var selectElement = document.getElementById('selectOrderConfEmails');
			var removedElems = new Array();
			var j =0;
			if(selectElement!=null && selectElement.options!=null){
				for ( var i=0; i < selectElement.options.length; i++ )
				{
					if ((selectElement.options[i].selected == true ) )
					{
						strItemToRemoveVal = selectElement.options[i].value;
						selectElement.options[i] = null;
						removedElems[j++] = strItemToRemoveVal;
						i--;
					}
				}
			}
			if(removedElems.length>0){
				var AddEmailListText = document.getElementById("AddnlEmailAddrText").value;
				var AddEmailListArray = AddEmailListText.split(",");
				var AddEmailListNewText = null;
				for(var i=0; i<AddEmailListArray.length; i++) {
					var currentEmailAddr = AddEmailListArray[i];
					if(currentEmailAddr=="")
						continue;
					var isCurrentElemRemoved = false;
					for(var k=0;k<removedElems.length;k++)
					{
						var removedEmailAddr = removedElems[k];
						if(currentEmailAddr==removedEmailAddr)
						{
							isCurrentElemRemoved = true;
						}
					}
					if(!isCurrentElemRemoved)
					{
						if(AddEmailListNewText == null || AddEmailListNewText == "")
							AddEmailListNewText = AddEmailListArray[i]+",";
						else
							AddEmailListNewText = AddEmailListNewText+AddEmailListArray[i]+",";
					}
					
				}
				document.getElementById("AddnlEmailAddrText").value = AddEmailListNewText;
			}
			return false;
		}
		
		function formatSpendingLt(component) {
	    	var quantity = component.value.trim();
	        var qtyLen = quantity.length;
	        var validVals = "0123456789";
	        var isValid=true;
	        var char;
	        for (i = 0; i < qtyLen && isValid == true; i++) {
	           char = quantity.charAt(i); 
	           if (validVals.indexOf(char) == -1) 
	           {
	              isValid = false;
	           }
	     	}
	     	if (!isValid){
	            component.value = "";
	            return false;
	     	}

	        if (quantity.length > 6){
	            var val = quantity.substr(0,6);
	    		quantity = val;
	            component.value = quantity;
	        }
	        return true;
		}
		</script>

<style type="text/css">
.checkboxTree input[type=checkbox] {
	visibility: hidden;
	display: none;
}

table thead {cursor: pointer;} 
. {} 
.sc{ height:100px; overflow:auto; overflow-x: hidden; overflow-y: scroll; margin-right:15px;} 
. input[type=radio]{ margin-right:15px; margin-left:5px;} 
.no-padding { padding:0px; } 
div table#myTable td { padding:5px; word-wrap:break-word; border-bottom: 1px solid #CCCCCC;} 
div table#myTable td.left-cell { border-left: 0px; } 
div table#myTable td.right-cell { border-right: 0px; } 
div table#myTable td.bottom-cell { border-bottom:0px; } 
div table#myTable td div { width:179px; } 
div table#myTable td input { margin-right:5px; } 
div table#user-header th { padding:5px; width:179px; } 
div#border-div { border-left: 1px solid #CCCCCC; border-right: 1px solid #CCCCCC; } 
a.underlink:hover { text-decoration: underline !important; } 


</style>
<link href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/SpryTabbedPanels.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
</head>
<!-- END swc:head -->
<body class="ext-gecko ext-gecko3">
<s:set name='_action' value='[0]' />
<s:set name="isSalesRep" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute('IS_SALES_REP')}"/>
<s:set name="AddressInformationTitle" scope="page"
	value="getText('Address_Information_Title')" />
<s:set name="confirmAddressDeleteMessage" scope="page"
	value="getText('confirmAddressDelete')" />
<s:set name="confirmAddressDuplicateMessage" scope="page"
	value="getText('confirmAddressDuplicate')" />
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />
<s:set name='sdoc' value="#_action.getCustomer().getOwnerDocument()" />
<s:set name='customerelement' value='#_action.getCustomer()' />

<!-- STARTS - Customer-User Profile Changes - adsouza -->
<s:set name='extnElem' value='%{#util.getElement(#sdoc, "//Extn")}' />
<!-- ENDS - Customer-User Profile Changes - adsouza -->

<s:set name='viewInvoices' value='#_action.getViewInvoices()' />
<s:set name='punchoutUser' value='#_action.getPunchoutUsers()' />
<s:set name='stockCheckWebservice'
	value='#_action.getStockCheckWebservice()' />
<s:set name='estimator' value='#_action.getEstimator()' />
<s:set name='customer' value='customerelement' />

<!-- Customer Additional Address -->

<s:set name="additionalAddressList"
	value='#util.getElement(#customer,"CustomerAdditionalAddressList")' />
<s:set name='addladdress'
	value='#util.getElements(#additionalAddressList,"CustomerAdditionalAddress")' />
<s:hidden name='selfAdminId' id="selfAdminId" value="%{isSelfAdmin()}" />
<!-- s:set name='customercontact' value='#util.getElement(#sdoc,"//Customer/CustomerContactList/CustomerContact")' /-->
<s:set name='customercontact' value='getContact()' />
<!-- Customer Contact Additional Address -->
<s:set name="additionalAddressList"
	value='#util.getElement(#customer,"CustomerAdditionalAddressList")' />
<s:set name='addladdress'
	value='#util.getElements(#additionalAddressList,"CustomerAdditionalAddress")' />
<title><s:property value="wCContext.storefrontId" /> / User Profile</title>
<!-- Web Trends tag start -->
 <meta name="WT.ti" Content="xpedx / User Profile">
<!-- Web Trends tag stop -->
<s:set name='userelement' value="getUser()" />
<s:set name='user' value='#userelement' />
<s:set name='userExists'
	value='%{#xmlUtil.hasAttribute(#user,"Loginid")}' />
<s:if test='%{#userExists}'>
	<s:set name='loginid' value='%{#user.getAttribute("Loginid")}' />
	<s:set name='displayUserID'
		value='%{#user.getAttribute("DisplayUserID")}' />
	<s:set name='orgQuestionList' value='%{getQuestionListForOrg()}' />
	<s:set name='userSecretQuestion' value='%{getSecretQuestionForUser()}' />
	<s:set name='optedCurrency' value='%{getSpendingLtCurrency()}' />
	<s:set name='primaryApprover' value='%{getPrimaryApprover()}' />
	<s:set name='alternateApprover' value='%{getAlternateApprover()}' />
	<s:set name='maskedPasswordString' value='%{getMaskedPasswordString()}' />
	<s:set name='maskedAnswerString'
		value='%{getMaskedSecretAnswerString()}' />
</s:if>
<s:set name='buyerOrgCode' value='%{getBuyerOrgCode()}' />
<s:set name='buyerOrgName' value='%{getBuyerOrgName()}' />
<s:set name='selfAdmin' value="isSelfAdmin()" />
<s:set name='isCustomerNotAdmin' value="isAdminListEnabled()" />

<s:set name='customerId' value='#customer.getAttribute("CustomerID")' />
<s:set name='customerContactId'
	value='#customercontact.getAttribute("CustomerContactID")' />
<s:set name='defShipToAddId' value='%{getDefShipToAddId()}' />
<s:set name='defBillToAddId' value='%{getDefBillToAddId()}' />
<s:set name='defSoldToAddId' value='%{getDefSoldToAddId()}' />
<s:set name='roles' value="getUserGroups()" />
<s:set name='hasAccessToEdit' value="%{getIsChildCustomer()}" />
<s:if test='%{#hasAccessToEdit == "TRUE"}'>
	<s:set name='isDisabled' value="%{'false'}" />
</s:if>
<s:else>
	<s:set name='isDisabled' value="%{'true'}" />
</s:else>

<div class='x-hidden dialog-body ' id="shipToUserProfileDiv">
	<div id="shipToUserProfile"></div>
</div>
	
<div id="main-container">
<div id="main" class="userProfile"><s:action name="xpedxHeader" executeResult="true"
	namespace="/common" />

<div class="container">
<div id="mid-col-mil"><br />
<div id="requestform">
<table class="form" width="100%" border="0" cellspacing="0"
	cellpadding="0">
	<s:if test="%{wCContext.loggedInUserId == #displayUserID}">
		<s:set name="disableSinceSelfApprover" value="%{true}"></s:set>
	</s:if>

	<tr>
	
	<s:set name='SalesRepUserId' value="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute('loggedInUserId')}" />
		<td colspan="2" class="no-border-right-user">
		<s:if test="%{#isSalesRep && #SalesRepUserId != null && #SalesRepUserId.isEmpty()==false}"><span class="page-title">Username:&nbsp;</span><s:property value="%{#SalesRepUserId}" /></s:if>
		<s:else><span class="page-title">Username:</span><s:if
			test='%{#userExists}'>
			<s:property value="%{#displayUserID}" />
			<s:hidden name="userName" value="%{#displayUserID}" />
			<s:hidden name="validateUserId" value="%{false}" />
		</s:if> <s:else>
			<s:textfield tabindex="5" disabled="%{#isDisabled}" name="userName"
				cssClass="x-input" cssStyle="width: 130px;" />
			<s:hidden name="validateUserId" value="%{true}" />
		</s:else></s:else></td>
		
		<%-- <s:if test="%{#session.errorNote!= null}">
		<td><h5 align="center"><b><font color="red"><s:property value='%{#session.errorNote}'/></font></b></h5></td>
		<s:set name="errorNote" value="<s:property value=null />" scope="session"/>
		</s:if> --%>
	</tr>
	<s:if test='%{#isCustomerNotAdmin == false && !#isSalesRep}'>
	<tr>
	     <%--JIRA 3917 Start --%>
		<td width="5%"  class="padding-left0 no-border-right-user">

		<div  class="question">
		<ul>
		 <s:form name="userList" id="userList">
			<li><a id="selectusertomodifyProfile" href="#viewUsersDlg" class="underlink">Show My Users&nbsp;</a></li>
			<li><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"		border="0" width="12" height="12"
				alt="Displays all users set up for this account."
				title="Displays all users set up for this account." align="right" /></a>
			</li>
			</s:form>
			</ul>
	        </div>	   
		    </td>	
			
			<td width="36%" align="left" class="padding-left0 no-border-right-user">
			<div class="question">
			<ul>
			<s:set name='custID' value='#customerId'/>
			<s:set name='storeFrontID' value="wCContext.storefrontId"/>
			  <li>
				    <s:form name="userListaddForm" id="userListaddForm"><%--JIRA 3917 Finish --%>
					    <s:hidden name="customerID" value="%{#_action.getCustomerID()}"/>
					    <s:hidden name="buyerOrgCode" value="%{#buyerOrgCode}"/>
					    <s:url id="showAllUsersURL" action='getUserList' namespace='/profile/user'>
						    <s:param name="customerID" value="%{#_action.getCustomerID()}"/>
						    <s:param name="buyerOrgCode" value="%{#buyerOrgCode}"/>
					    </s:url>
					    <s:set name='custDoc' value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getCustomerDetails(#custID,#storeFrontID)"/>
					    <s:set name='userCustElement' value='#custDoc.getDocumentElement()'/>
					    <s:set name='extnSuffixType' value='%{#SCXmlUtils.getXpathAttribute(#userCustElement, "//Extn/@ExtnSuffixType")}' />
					    <div id="searchTable" >
					    	<ul class="tool-bar-bottom" style="float:left">
					            
					          	<a href="javascript:getNewContactInfo1('<s:url value="/profile/user/xpedxNewUserCreate.action"/>');" class="grey-ui-btn">
					          		<span><s:text name='Add_User'/></span>
					          	</a>
					        </ul>
					    </div>
				    </s:form>
			    </li> 
		 </ul>
		</div>
		</td>
	</tr>
	</s:if>
</table>
<span class="smallfont">* - Required fields</span>
</div>

<s:form id="myAccount" name="myAccount" method="post" validate="true"
	action="saveUserProfileInfo">
	<s:hidden id="bodyData" name="bodyData" />
	<s:hidden id="selectedTab" name="selectedTab" />
	<s:set name="poListVal" value="%{addnlPOList}" />
	<s:set name="addnlEmailListVal" value="%{addnlEmailAddrs}" />
	<s:hidden name="POListText" id="POListText"
		value='%{#poListVal}' />
	<s:hidden name="AddnlEmailAddrText" id="AddnlEmailAddrText"
		value='%{#addnlEmailListVal}' />
	<s:hidden name="customerId" value="%{customerId}" id="customerId"/>
	<s:hidden name="customerContactId"
		value="%{#customercontact.getAttribute('CustomerContactID')}" id="customerContactId"/>
	<s:hidden name="buyerOrgCode" value="%{#buyerOrgCode}" />
	<s:hidden name="#action.namespace" value="/profile/user" />
	<s:hidden id="actionName" name="#action.name" value="saveUserInfo" />
	<s:hidden name='userExistsId' value='%{#userExists}' id='userExistsId' />
	<s:hidden name='selfAdminId' id="selfAdminId" value="%{isSelfAdmin()}" />
	<s:hidden name="preferredLocale" id="saveNewUserInfo_preferredLocale" value="%{'en_US_EST'}"/>

	<s:if test='#punchoutUser == "Y"'>
		<s:hidden name='punchoutUsers' value='%{true}' />
	</s:if>
	<s:else>
		<s:hidden name='punchoutUsers' value='%{false}' />
	</s:else>
	<s:if test='#stockCheckWebservice == "Y"'>
		<s:hidden name='stockCheckWebservice' value='%{true}' />
	</s:if>
	<s:else>
		<s:hidden name='stockCheckWebservice' value='%{false}' />
	</s:else>
	<s:set name='buyerApproverValue'
		value='%{isInUserGroup("BUYER-APPROVER")}' />
	<s:hidden name='defaultShipTo'
		value='%{#extnElem.getAttribute("ExtnDefaultShipTo")}' />
	<s:hidden name="userName" value="%{#displayUserID}" />
	<s:hidden id="mandatoryFieldCheckFlag_myAccount" name="mandatoryFieldCheckFlag_myAccount" value="%{false}"/>
	<div id="TabbedPanels1" class="TabbedPanels">
	<s:if test="%{#isSalesRep}">
		<ul class="TabbedPanelsTabGroup" style="margin-left: 5px;">
			<li class="TabbedPanelsTab" tabindex="0">Site Preferences</li>
		</ul>
		<div id="TabbedPanels1" class="TabbedPanelsContentGroup">
	<!-- ------------------------------------------------------------------------------------------------------------------------ -->
	<!-- ----------------------------------         Site Preference Div to be added here     ----------------------------------- -->
	<!-- ------------------------------------------------------------------------------------------------------------------------ -->

	<div class="TabbedPanelsContent">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" colspan="2" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li><span class="subtitle">Catalog Preferences</span></li>
			</ul>
			</div>
			</td>
		</tr>
		<tr>
			<td width="27%" valign="top" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li>Preferred Catalog View:&nbsp;</li>
				<li><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"
					width="12" height="12" border="0"
					alt="Default view setting for Catalog display."
					title="Default view setting for Catalog display." /></a></li>
			</ul>
			</div>
			</td>
			<td width="73%" valign="top" class="no-border-right-user padding00">
			<div class="divs">
			<div><s:radio list="#{'0': ''}" name="b2bCatalogView"
				id="b2bCatalogView" value='defaultB2bCatalogView' /></div>
			<div class="view1"></div>
			<div>Full View - 1 item per row</div>
			</div>
			<div class="divs">
			<div><s:radio list="#{'2': ''}" name="b2bCatalogView"
				id="b2bCatalogView" value='defaultB2bCatalogView' /></div>
<!-- 			<div class="view3"></div> -->
			<div><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/mini-view.png" style="margin: 0 6px;" class="mini-view" title="Mini View" alt="mini-view"/></div>
			<div>Mini View - 4 items per row</div>
			</div>
			<div class="divs">
			<div><s:radio list="#{'1': ''}" name="b2bCatalogView"
				id="b2bCatalogView" value='defaultB2bCatalogView' /></div>
			<div class="view2"></div>
			<div>Condensed View - 2 items per row</div>
			</div>
			<div class="divs">
			<div><s:radio list="#{'3': ''}" name="b2bCatalogView"
				id="b2bCatalogView" value='defaultB2bCatalogView' /></div>
<!-- 			<div class="view4"></div> -->
			<div><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/grid-view.png" style="margin: 0 6px;" class="grid-view" title="Grid View" alt="grid-view"/></div>
			<div>Grid View - No images</div>
			</div>
			</td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li>Preferred Product Category:&nbsp;</li>
				<li><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"
					alt="Defaults your preferred catalog on the homepage."
					title="Defaults your preferred catalog on the homepage." width="12"
					height="12" border="0" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user user-profile padding0 ">
			<s:select headerKey="" headerValue="- Select Category -" name="PrefCatalog" value='prefCategory' 
				list="getMainCategories()" cssClass="x-input" /></td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding0"
				style="padding-top: 20px;">
			<div class="question">
			<ul>
				<li><strong>PO List&nbsp;</strong></li>
				<li><a href="#"><img
					alt="List of PO numbers made available at order submission."
					title="List of PO numbers made available at order submission."
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" width="12" height="12"
					border="0" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding0"
				style="padding-top: 20px;">
			<div class="table-top-bar" style="width: 80%; clear: both;">
			<div class="table-top-bar-L"></div>
			<div class="table-top-bar-R"></div>
			</div>
			<table border="0" align="left" cellpadding="0" cellspacing="0"
				id="mil-list-new"
				style="background: none repeat scroll 0% 0% transparent; width: 80%;">
				<tbody>
					<tr>
						<td width="55%" valign="top" class="noBorder noBorders-FAFAFA">
						<select style="width: 250px;" id="selectPOName" name="select"
							multiple="multiple">
							<s:iterator value="POList" id="poListTbl" status="poListStatus">
								<s:set name="poListValue" value='value' />
								<option><s:property value="poListValue" /></option>
							</s:iterator>
						</select></td>
						<td valign="top" class="noBorder">
						<div class="add-list-label">Enter value to add:</div><input
							name="addToPOListName" id="addToPOListName"
							class="input-details-cart  x-input" style="width: 150px;"
							title="POname" tabindex="12" maxlength="22"/>
						<div class="clearview">&nbsp;</div>
						<div class="clearview">
						<ul class="float-left">
							<li class="float-left margin-10"><a href="#"
								class="grey-ui-btn"
								onclick="javascript: return addToPOListfn();"><span>Add
							to List</span></a></li>
						</ul>
						</div>
						<div class="clearview">&nbsp;</div>
						<div class="clearview">
						<ul class="float-left">
							<li class="float-left margin-10"><a href="#"
								class="grey-ui-btn"
								onclick="javascript: return removeFrmPOList();"><span>Remove
							from List</span></a></li>
						</ul>
						</div>
						</td>
					</tr>
				</tbody>
			</table>
			<div id="table-bottom-bar" style="width: 80%; clear: both;">
			<div id="table-bottom-bar-L"></div>
			<div id="table-bottom-bar-R"></div>
			</div>
			</td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding0">
			<div class="question">
			<ul>
				<li><span class="subtitle">Email Preferences&nbsp;</span></li>
				<li><a href="#"><img
					title="Types of email notifications regarding order status."
					alt="Types of email notifications regarding order status."
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" alt="help" width="12"
					height="12" border="0" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding0">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding0">
			<div class="question">
			<ul>
				<li><s:checkbox name="orderConfirmationEmailFlag"
					fieldValue="true" value='isSendOrderConfEmail()' /></li>
				<li>Order Confirmation&nbsp;</li>
				<li><a href="#"><img
					title="Email notification that your order has been placed."
					title="Email notification that your order has been placed."
					height="12" border="0" width="12"
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding0"><span
				class="clearview"> Additional Order Confirmation Email
			Address(es):</span>
			<div class="table-top-bar"
				style="width: 80%; clear: both; margin-top: 10px;">
			<div class="table-top-bar-L"></div>
			<div class="table-top-bar-R"></div>
			</div>
			<table border="0" align="left" cellpadding="0" cellspacing="0"
				id="mil-list-new"
				style="background: none repeat scroll 0% 0% transparent; width: 80%;">
				<tbody>
					<tr>
						<td width="55%" valign="top" class="noBorder noBorders-FAFAFA">
						<select style="width: 250px;" name="selectOrderConfEmails"
							id="selectOrderConfEmails" multiple="multiple">
							<s:iterator value="AddnlEmailAddrList" id="AddnlEmailAddrListTbl"
								status="AddnlEmailAddrListStatus">
								<s:set name="AddnlEmailAddr" value='value' />
								<option><s:property value="#AddnlEmailAddr" /></option>
							</s:iterator>
						</select></td>
						<td valign="top" class="noBorder">
						<div class="add-list-label">Enter value to add:</div><input name="confEmailText"
							id="confEmailText" class="input-details-cart  x-input" id="Text4"
							style="width: 150px;" tabindex="12" maxlength="150" />
						<div class="clearview">&nbsp;</div>
						<div class="clearview">
						<ul class="float-left">
							<li class="float-left margin-10"><a href="#"
								class="grey-ui-btn"
								onclick="javascript: return addOrderConfEmailToList();"><span>Add
							to List</span></a></li>
						</ul>
						</div>
						<div class="clearview">&nbsp;</div>
						<div class="clearview">
						<ul class="float-left">
							<li class="float-left margin-10"><a href="#"
								class="grey-ui-btn"
								onclick="javascript: return removeOrderConfEmailFrmList();"><span>Remove
							from List</span></a></li>
						</ul>
						</div>
						</td>
					</tr>
				</tbody>
			</table>
			<div id="table-bottom-bar" style="width: 80%; clear: both;">
			<div id="table-bottom-bar-L"></div>
			<div id="table-bottom-bar-R"></div>
			</div>
			</td>
		</tr>
<%-- 		<tr>
			<td valign="top" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li><s:checkbox name="orderShipmentEmailFlag" fieldValue="true"
					value='isSendOrderShipEmail()' /></li>
				<li>Order Shipped&nbsp;</li>
				<li><a href="#"><img width="12" height="12" border="0"
					alt="Email notification that your order has been shipped."
					title="Email notification that your order has been shipped."
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding00">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li><s:checkbox name="backorderEmailFlag" fieldValue="true"
					value='isSendBackOrderEmail()' /></li>
				<li>Order Backorder&nbsp;</li>
				<li><a href="#"><img border="0"
					alt="Email notification for backorders."
					title="Email notification for backorders." width="12" height="12"
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding00">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li><s:checkbox name="orderCancellationEmailFlag"
					fieldValue="true" value='isSendOrderCancelEmail()' /></li>
				<li>Order Cancelled&nbsp;</li>

				<li><a href="#"><img width="12" height="12" border="0"
					title="Email notification that your order has been cancelled."
					alt="Email notification that your order has been cancelled."
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding00">&nbsp;</td>
		</tr> --%>
		<tr>
			<td valign="top" colspan="2" class="no-border-right-user padding00">
			<div class="question">

			<ul class="padding-top3">
				<li><strong>Quick Links&nbsp;</strong></li>
				<li><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"
					alt="User defined bookmarked links which display on the homepage."
					title="User defined bookmarked links which display on the homepage."
					width="12" height="12" border="0" /></a></li>
				 <li><a class="underlink" onclick="setInLineChange();" id="NewQL">[Add New]</a></li> 				
			</ul>
			</div>

			<div class="txt-small clearview"></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="tb1" class="standard-table">
			<tbody>
				<tr class="table-header-bar">
					<td width="35%" class="no-border-left table-header-bar-left"><span
						class="white"> Name</span></td>
					<td width="48%" align="left" class="  "><span
						class="white">URL</span></td>
					<td width="8%" align="left" ><span
						class="white">Show</span></td>
					<td width="9%" align="left"
						class="no-border-right table-header-bar-right"><span
						class="white">Sequence</span></td>
				</tr>
				</tbody>			
			</table>
			</td>
		</tr>
		<s:set name='custContactAddtnlAddress'
			value="#_action.getCustContactAddtnlAddress()" />
		<s:set name='personInfo'
			value='#xmlUtil.getChildElement(#custContactAddtnlAddress,"PersonInfo")' />
		<s:hidden name='addtnlAddressID'
			value='%{#custContactAddtnlAddress.getAttribute("CustomerAdditionalAddressID")}' />
		<s:hidden name='personfInfoAddressID'
			value='%{#personInfo.getAttribute("AddressID")}' />
		<s:hidden name='personfInfoKey'
			value='%{#personInfo.getAttribute("PersonInfoKey")}' />
		<s:hidden name='isCommercialAddress'
			value="%{#personInfo.getAttribute('IsCommercialAddress')}" />
	</table>
	</div>
	
		
		</div>
	</s:if>
	<s:else>
	<ul class="TabbedPanelsTabGroup" style="margin-left: 5px;">
		<li class="TabbedPanelsTab" onclick="javascript: writeMetaTag('WT.ti', 'xpedx / User Profile /User information');" tabindex="0">User Information</li>
		<li class="TabbedPanelsTab" onclick="javascript: writeMetaTag('WT.ti', 'xpedx / User Profile /Authorized Locations');" tabindex="0">Authorized Locations</li>
		<li class="TabbedPanelsTab"  onclick="javascript: writeMetaTag('WT.ti', 'xpedx / User Profile');" tabindex="0">Site Preferences</li>
		 
		<%-- Added for Jira 3048 issue item 3 --%>
		 <s:set name="spendingLimitValue" value="%{getSpendingLimit()}" />		 	
		 <s:if test="%{(#optedCurrency != null && #optedCurrency!='') || ( #primaryApprover != null && #primaryApprover !='' ) || ( #spendingLimitValue != null && #spendingLimitValue !='') || (#alternateApprover != null && #spendingLimitValue !='')}">		 	 
			<li class="TabbedPanelsTab" tabindex="0">Spending Limit &
			Approvers</li>
		 </s:if>
		 <s:elseif test="%{!#disableSinceSelfApprover}">
		 	<li class="TabbedPanelsTab" tabindex="0">Spending Limit &
			Approvers</li>
		 </s:elseif> 
	   <%-- End Jira 3048 issue item 3 --%>
	</ul>

	<!-- ------------------------------------------------------------------------------------------------------------- -->
													<!-- user Information Div --> 
	<!-- ------------------------------------------------------------------------------------------------------------- -->
	<%--Adding Proper Tabindex for Jira 3927 --%>
	<div id="TabbedPanels1" class="TabbedPanelsContentGroup">
	<div id="UserInformationsTab" class="TabbedPanelsContent">
	<div>

	
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="tabs line-spacing-tr">
		
		<tr style="display: none;">
			<td class="boldText textAlignLeft">&nbsp;&nbsp;<s:text
				name="RB_title" />:</td>
			<td><s:select tabindex="7" name="title"
				disabled="%{#isDisabled}" list="%{getTitle()}"
				value="%{#customercontact.getAttribute('Title')}" /></td>
		</tr>
		
		<s:if test='%{#isCustomerNotAdmin == false}'>		
		<tr id="userNameRow">
			<td width="13%" valign="top" class="no-border-right-user">User Status:
			</td>
			<s:set  name="tabidx" value ="%{8}" />
			<td colspan="3" class="no-border-right-user">
			<s:iterator value="getStatusList().keySet()" id="statusVal" >
				<s:set name="statusValue" value="value" />
				<label>
				 <%-- Fix for Jira 3048 --%>
				    <s:if test="%{#disableSinceSelfApprover}">		
					   <input tabindex="<s:property value="#tabidx"/>"  type="radio" name="status" id='status' disabled = 'true' value='<s:property value="statusVal"/>' <s:if test ="%{#statusVal == getContactStatus()}" > checked="true"</s:if>>
					   </input>	
					   	<s:if test ="%{#statusVal == getContactStatus()}" ><s:hidden name="status" id='status' value='%{getContactStatus()}' ></s:hidden> </s:if>	        
					</s:if>
					<s:else>
					  <input tabindex="<s:property value="#tabidx"/>"  type="radio" name="status" id='status' value='<s:property value="statusVal"/>' <s:if test ="%{#statusVal == getContactStatus()}" > checked="true"</s:if>>
					  </input>
					</s:else>
				 <%-- END Fix for Jira 3048 --%>
					<s:if test ="%{#statusVal == 30}" >
					  Suspended
					</s:if>
					<s:else>
					  <s:property value="%{getStatusList().get(#statusVal)}" />
					</s:else>
					<s:set  name="tabidx" value ="%{#tabidx +1}" /> 
				</label>
        	</s:iterator>
			<div class="float-right"><span>Last Login: <s:property value="%{getExtnLastLoginDate()}"/> </span><span
			class="padding-left2">User Created: <s:property value="%{getUserCreatedDate()}"/> </span></div>
			</td>
		</tr>
		
		</s:if>
		<s:else>
			<s:hidden name="status" id='status' value='%{getContactStatus()}' ></s:hidden>						
			<s:set name='userStatus' value="%{getContactStatus()}" />
			<tr id="userNameRow">
			<td width="13%" valign="top" class="no-border-right-user">User
			Status:
			</td>
			<td colspan="3" class="no-border-right-user">                   		
			<s:if test="#userStatus == '10'" >
                   Active
            </s:if>
            <s:if test="#userStatus == '20'" >
                 On Hold
            </s:if>
            <s:if test="#userStatus == '30'">
                 Inactive
             </s:if>
            <div class="float-right"><span>Last Login: <s:property value="%{getExtnLastLoginDate()}"/> </span><span
			class="padding-left2">User Created: <s:property value="%{getUserCreatedDate()}"/> </span></div>
             
            </td>
            </tr>
		</s:else>
		<tr>
			<s:if test='%{isCustomerNotAdmin}'>
				<s:set name="checkBoxDisable" value='%{true}' />
			</s:if>
			<s:else>
				<s:set name="checkBoxDisable" value='%{false}' />
			</s:else>
			<td valign="top" class="no-border-right-user">
			<div class="question">
			<ul>
				<li>User Type:&nbsp;</li>
				<li><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"
					width="12" height="12"
					alt="One or more roles may be assigned to each user. Hover over each role to see more details."
					title="One or more roles may be assigned to each user. Hover over each role to see more details."
					border="0" /></a></li>
			</ul>
			</div>
			</td>
			
		<s:if test='%{#isCustomerNotAdmin == false}'>
			<%-- Fix for Jira 3048 item issue 1 
			 <s:if test="%{#disableSinceSelfApprover}">
			   <s:set name="checkBoxDisable" value='%{true}' />
			 </s:if> 
			 End Fix for Jira 3048 item issue 1 --%> 
			<td colspan="3" class="no-border-right-user">
			<s:if test="%{!#disableSinceSelfApprover}">
				<label
				title="Responsible for overall administration of, and access to, accounts on the web site. Creates user profiles, assigns roles, assigns locations.">
				<s:checkbox tabindex="11" name='buyerAdmin' id='buyerAdmin'
				fieldValue="true" value='%{isInUserGroup("BUYER-ADMIN")}'/> Admin</label> 
				<label
				title="Authorizes submission of orders."> 
				<s:checkbox	tabindex="13" name='buyerApprover' id='buyerApprover'
				value='%{isInUserGroup("BUYER-APPROVER")}' fieldValue="true"/> Approver</label> 
				<label
				title="Buyer has the ability to submit orders."> 
				<s:checkbox	tabindex="14" name='test' id='test' fieldValue="test123"
				value="%{isInUserGroup('BUYER-USER')}" />Buyer</label>
			</s:if>	
			<s:else> 
				<label
				title="Responsible for overall administration of, and access to, accounts on the web site. Creates user profiles, assigns roles, assigns locations.">
				<s:checkbox tabindex="11" name='buyerAdmin' id='buyerAdmin'
				fieldValue="true" value='%{isInUserGroup("BUYER-ADMIN")}' disabled="true"/> Admin</label> 
				<s:hidden name="buyerAdmin" id='buyerAdmin' value='%{isInUserGroup("BUYER-ADMIN")}' ></s:hidden>
				<label
				title="Authorizes submission of orders."> 
				<s:checkbox	tabindex="13" name='buyerApprover' id='buyerApprover'
				value='%{isInUserGroup("BUYER-APPROVER")}' fieldValue="true" disabled="true"/> Approver</label> 
				<s:hidden name="buyerApprover" id='buyerApprover' value='%{isInUserGroup("BUYER-APPROVER")}' ></s:hidden>
				<label
				title="Buyer has the ability to submit orders."> 
				<s:checkbox	tabindex="14" name='test' id='test' fieldValue="test123"
				value="%{isInUserGroup('BUYER-USER')}" disabled="true"/>Buyer</label>							
			</s:else>
			<s:if test='%{#estimator=="T"}'>
		        	<label title="Estimator views available inventory and pricing.">
					<s:checkbox tabindex="15" name='estimator' id='estimator' fieldValue="true" value="true" disabled='%{#checkBoxDisable || #isDisabled}' /> Estimator</label> 
			</s:if>
			<s:else>
			 		<label title="Estimator views available inventory and pricing.">
					<s:checkbox tabindex="15" name='estimator' id='estimator' fieldValue="true" value="false" disabled='%{#checkBoxDisable || #isDisabled}' /> Estimator</label> 
			</s:else>
			<s:if test='%{#viewInvoices=="Y"}'>
				<label
				title="Permitted to view invoices online."> 
				<s:checkbox tabindex="16" name="viewInvoices" fieldValue="true" id="viewInvoices"
				value="true" disabled='%{#checkBoxDisable || #isDisabled}'/> <p class="float-right">View Invoices</p></label> 
			</s:if>
			<s:else>
			 	<label
				title="Permitted to view invoices online."> 
				<s:checkbox tabindex="16" name="viewInvoices" fieldValue="true" id="viewInvoices"
				value="false" disabled='%{#checkBoxDisable || #isDisabled}'/> <p class="float-right">View Invoices</p></label> 
			</s:else>
			 	<label	title="Permitted to view prices."> 
				<s:checkbox tabindex="17" fieldValue="true" 
				name="viewPrices" id="viewPrices"
				value="%{isViewPrices()}" disabled='%{#checkBoxDisable || #isDisabled}'/> View Prices</label> 
				<label
				title="Permitted to view reports."> <s:checkbox tabindex="18"
				name="viewReports" id="viewReports" fieldValue="true" 
				value='%{isViewReports()}' disabled='%{#checkBoxDisable || #isDisabled}'/><p class="float-right"> View Reports</p></label></td>
		</s:if>
		<s:else><td colspan="3" class="no-border-right-user">			
			<s:if test='%{isInUserGroup("BUYER-USER")}'>Buyer </s:if>
			<s:if test='%{#estimator=="T"}'>Estimator </s:if>
			<s:if test='%{#viewInvoices=="Y"}'>View Invoices </s:if>
			<s:if test='%{isViewPrices()}'> View Prices </s:if>
			<s:if test='%{isViewReports()}'>View Reports </s:if>		
		 </td> </s:else>
		</tr>
		
		
		<tr style="display: none;">
			<td class="boldText textAlignLeft"><s:text name="RB_jobTitle" />:</td>
			<td><s:textfield tabindex="19" name="jobTitle"
				disabled="%{#isDisabled}"
				value='%{#customercontact.getAttribute("JobTitle")}' /></td>
		</tr>
		<tr>
			<td class="no-border-right-user">
			<div class="mandatory float-left">*</div>
			First Name:</td>
			<td width="31%" class="no-border-right-user"><s:textfield
				tabindex="20" name="firstName" id="firstName" onkeyup="javascript:testFieldValueCheck(this, 'myAccount');" 
			    onmouseover="javascript:testFieldValueCheck(this, 'myAccount');" maxlength="20"
				cssClass="x-input" cssStyle="width: 185px;"
				disabled="%{#isDisabled}"
				value='%{#customercontact.getAttribute("FirstName")}' /></td>
			<td width="19%" class="no-border-right-user">
			<div class="mandatory float-left">*</div>
			Last Name:</td>
			<td width="37%" class="no-border-right-user"><s:textfield
				tabindex="21" name="lastName" id="lastName"  onkeyup="javascript:testFieldValueCheck(this, 'myAccount');" 
			    onmouseover="javascript:testFieldValueCheck(this, 'myAccount');" cssClass="x-input"
				cssStyle="width: 185px;" maxlength="20" disabled="%{#isDisabled}"
				value='%{#customercontact.getAttribute("LastName")}' /></td>
		</tr>

	<s:if test="%{selfAdmin}">
		<!-- <tr><td valign="top" colspan="4" width="100%" class="no-border-right-user"><div id="pwdValidationDiv" style="display : none"/></td></tr> -->
		<tr>
			<td valign="top" class="no-border-right-user">
			<div class="question">
			<ul>
				<li>
				<div class="mandatory float-left">*</div>
				Password:&nbsp;</li>
				<li><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"
					width="12" height="12" border="0"
					title="Min 8, Max of 14 characters; at least 2 alpha; 1 uppercase; 1 numeric; Max repeated consecutive characters = 2. Following characters cannot be used: $, ?, !, Example:  &quot;Password1&quot;."
					alt="Min 8, Max of 14 characters; at least 2 alpha; 1 uppercase; 1 numeric; Max repeated consecutive characters = 2. Following characters cannot be used: $, ?, !, Example: &quot;Password1&quot;." /></a></li>
			</ul>
			</div>
			</td>
			<td width="31%" class="no-border-right-user">
			<s:url id="ValidatePasswordURL" action="validatePassword"/>
			<s:password
				tabindex="22" name="userpassword" id="userpassword" onkeyup="javascript:testFieldValueCheck(this, 'myAccount');" 
			    onmouseover="javascript:testFieldValueCheck(this, 'myAccount');" 
				cssClass="x-input" cssStyle="width: 185px;"
				value="%{#maskedPasswordString}" showPassword="true" size="8"
				onchange="javaScript:validatePassword();"
				maxlength="14" 
			/>
			</td>
			<td width="19%" class="no-border-right-user">
			<div class="mandatory float-left">*</div>
			Confirm Password:</td>
			<td width="37%" class="no-border-right-user"><s:password
				tabindex="23" name="confirmpassword" id="confirmpassword" onkeyup="" 
			    cssClass="x-input" cssStyle="width: 185px;"
				value="%{#maskedPasswordString}" showPassword="true" size="8"
				maxlength="14" /></td>
		</tr>
		
		<s:if test="#orgQuestionList!=null">
				<tr>
			<td class="no-border-right-user">
			<div class="mandatory float-left">*</div>
			Security Question:</td>
			<td colspan="3" class="no-border-right-user"><s:select headerKey="" headerValue="- Security Question -"
				tabindex="24" name="secretQuestion" id="secretQuestion"
				value="%{#userSecretQuestion}"
				list="#orgQuestionList" cssClass="x-input" cssStyle="width: auto;"
				onchange="javascript:document.myAccount.secretAnswer.value='';javascript:document.myAccount.confirmAnswer.value='';" />
			</td>
		</tr>
		</s:if>
		<tr>
			<td class="no-border-right-user">
			<div class="mandatory float-left">*</div>
			Security Answer:</td>
			<td class="no-border-right-user"><s:password tabindex="25"
				name='secretAnswer' id="secretAnswer" onkeyup="javascript:testFieldValueCheck(this, 'myAccount');" 
			    onmouseover="javascript:testFieldValueCheck(this, 'myAccount');" cssClass="x-input"
				cssStyle="width: 185px;" value="%{#maskedAnswerString}"
				showPassword="true" /></td>
			<td class="no-border-right-user">
			<div class="mandatory float-left">*</div>
			Confirm Answer:</td>
			<td class="no-border-right-user"><s:password tabindex="26"
				name='confirmAnswer' id="confirmAnswer" onkeyup="" 
			    cssClass="x-input"
				cssStyle="width: 185px;" value="%{#maskedAnswerString}"
				showPassword="true" /></td>
		</tr>
		<!-- <tr><td valign="top" colspan="4" width="100%" class="no-border-right-user"><div class="error" id="errorMsgFor_secretAnswer" style="display : none"/>Please enter the same answers in both answer and confirm answer fields.</div></td></tr> -->
	</s:if>	
	<s:if test='#selfAdmin == false'>
	<s:if test='%{#hasAccessToEdit == "TRUE"}'>	
		<tr>
			<td class="no-border-right-user">
			<div class="question">
			<ul>
				<li>Password:&nbsp;</li>
				<li><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"
					width="12" 
					title="System will automatically generate a new password and send a message to the user's email address as shown in this record."
					alt="System will automatically generate a new password and send a message to the user's email address as shown in this record."
					height="12" border="0" /></a></li>
			</ul>
			</div>
			</td>
			<td colspan="3" class="no-border-right-user">
			<ul class="float-left">
				<s:url id="ResetPasswordURL" action="resetPassword"/>
				<li><a href="javascript:resetPassword();" class="grey-ui-btn"><span><s:text name="reset.Password" /></span></a></li>
			</ul>
			</td>
		</tr>
		</s:if>
		</s:if>
		
		<tr>
			<td valign="top" class="no-border-right-user">
			<div class="question">
			<ul>
				<li>
				<div class="mandatory float-left">*</div>
				Email Address:&nbsp;</li>
				<li><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"
					width="12" height="12" border="0"
					alt="Used for all user specific communications from this website, including new password and password resets."
					title="Used for all user specific communications from this website, including new password and password resets." /></a></li>
			</ul>
			</div>
			</td>
			<td class="no-border-right-user"><s:textfield tabindex="27" maxlength="500"
				name="emailId" cssClass="x-input" id="emailId" onkeyup="javascript:testFieldValueCheck(this, 'myAccount');" 
			    onmouseover="javascript:testFieldValueCheck(this, 'myAccount');" 
				cssStyle="width: 185px;" disabled="%{#isDisabled}"
				value='%{#customercontact.getAttribute("EmailID")}' /></td>
			<td class="no-border-right-user">
			<div class="mandatory float-left">*</div>
			Confirm Email Address:</td>
			<td class="no-border-right-user"><s:textfield tabindex="28" maxlength="500"
				id="confirmEmailId" name="confirmEmailId" onkeyup="" 
			    cssClass="x-input"
				cssStyle="width: 185px;" disabled="%{#isDisabled}"
				value='%{#customercontact.getAttribute("EmailID")}'
				onchange="javascript:return confirmEmail();" />
				</td>
				
		</tr>
		<!-- <tr><td valign="top" colspan="4" width="100%" class="no-border-right-user"><div class="error" id="errorMsgFor_emailId" style="display : none"/>Please enter the same email in both Email and Confirm email fields.</div></td></tr>
		<tr><td valign="top" colspan="4" width="100%" class="no-border-right-user"><div class="error" id="errorMsgFor_validemailId" style="display : none"/>Please enter a valid email address.</div></td></tr> -->
		
		<s:set name="defaultAddressList"
			value="getDefaultContactAddressList()" />
		<s:set name="nonDefaultAddressList"
			value="getOtherContactAddressList()" />
		<s:if
			test='%{(#defaultAddressList !=null && #defaultAddressList.size() >0) || (#nonDefaultAddressList != null && #nonDefaultAddressList.size() >0)}'>
			<s:set name='listEmty' value="%{'TRUE'}" />
		</s:if>
		<s:if test='%{#hasAccessToEdit != "TRUE" || #listEmty == null}'>
			<s:set name='isButtonDisabled' value="%{'true'}" />
		</s:if>
		<s:else>
			<s:set name='isButtonDisabled' value="%{'false'}" />
		</s:else>
		<s:set name='custContactAddtnlAddress'
			value="#_action.getCustContactAddtnlAddress()" />
		<s:set name='personInfo'
			value='#xmlUtil.getChildElement(#custContactAddtnlAddress,"PersonInfo")' />
		<s:hidden name='addtnlAddressID'
			value='%{#custContactAddtnlAddress.getAttribute("CustomerAdditionalAddressID")}' />
		<s:hidden name='personfInfoAddressID'
			value='%{#personInfo.getAttribute("AddressID")}' />
		<s:hidden name='personfInfoKey'
			value='%{#personInfo.getAttribute("PersonInfoKey")}' />
		<s:hidden name='isCommercialAddress'
			value="%{#personInfo.getAttribute('IsCommercialAddress')}" />
		<tr>
			<td class="no-border-right-user">Address Line 1:</td>
			<td class="no-border-right-user" colspan="3"><s:textfield
				tabindex="29" name='addressLine1_new' id='addressLine1_new'
				value='%{#personInfo.getAttribute("AddressLine1")}'
				id='addressLine1_new' cssClass="x-input" cssStyle="width:270px;"
				maxlength="35" /></td>
		</tr>
		<tr>
			<td class="no-border-right-user">Address Line 2:</td>
			<td class="no-border-right-user" colspan="3"><s:textfield
				tabindex="30" name='addressLine2_new' id='addressLine2_new'
				value='%{#personInfo.getAttribute("AddressLine2")}'
				id='addressLine2_new' cssClass="x-input" cssStyle="width:270px;"
				maxlength="35" /></td>
		</tr>
		<tr>
			<td class="no-border-right-user">Address Line 3:</td>
			<td colspan="3" class="no-border-right-user"><s:textfield
				tabindex="31" name='addressLine3_new' id='addressLine3_new'
				value='%{#personInfo.getAttribute("AddressLine3")}'
				id='addressLine3_new' cssClass="x-input" cssStyle="width:270px;"
				maxlength="35" /></td>
		</tr>
		<tr>
			<td class="no-border-right-user">City:</td>
			<td class="no-border-right-user"><s:textfield name='city_new'
				tabindex="32" value='%{#personInfo.getAttribute("City")}' id='city_new'
				cssClass="x-input" cssStyle="width:190px;" maxlength="20" /></td>
			<td class="no-border-right-user">State / Province:</td>
			<td class="no-border-right-user"><s:textfield name="state_new"
				tabindex="33" value="%{#personInfo.getAttribute('State')}" id="state_new"
				cssClass="x-input" cssStyle="width: 190px;" maxlength="2" /></td>
		</tr>
		<tr>
			<td class="no-border-right-user">Postal Code:</td>
			<td class="no-border-right-user"><s:textfield name='zipCode_new'
				tabindex="34" value='%{#personInfo.getAttribute("ZipCode")}' id='zipCode_new'
				cssClass="x-input" cssStyle="width: 190px;" maxlength="10" /></td>
			<td class="no-border-right-user">Country:</td>
			<td class="no-border-right-user">
			<s:select
				tabindex="35" name="country_new" id="country_new"
				emptyOption="true" value='%{#personInfo.getAttribute("Country")}'
				list="%{getCountriesMap()}" headerKey="" headerValue="-Select Country-" cssClass="x-input" cssStyle="width: 190px;"/>
			</td>
		</tr>
		<tr>
			<td class="no-border-right-user">Phone:</td>
			<td class="no-border-right-user"><s:textfield
				tabindex="36" name='dayPhone_new' value='%{#personInfo.getAttribute("DayPhone")}'
				id='dayPhone_new' cssClass="x-input phone-numeric" cssStyle="width: 190px;"
				maxlength="10"/>
				<div class="error" id="errorMsgFor_validPhoneNo" style="display : none">Please enter a valid 10 digit number.</div>
				</td>
			<td class="no-border-right-user">Fax:</td>
			<td class="no-border-right-user"><s:textfield
				tabindex="37" name='dayFaxNo_new' value='%{#personInfo.getAttribute("DayFaxNo")}'
				id='dayFaxNo_new' cssClass="x-input phone-numeric" cssStyle="width: 190px;"
				maxlength="10"/>
			<div class="error" id="errorMsgFor_validFaxNo" style="display : none">Please enter a valid 10 digit number.</div>	
			</td>
		</tr>
	</table>

	</div>
	</div>

	<!-- ------------------------------------------------------------------------------------------------------------- -->
	<!-- Authorised Locations Div --> 
	<!-- ------------------------------------------------------------------------------------------------------------- -->
	<s:set name='defaultShipToId'
		value='%{#extnElem.getAttribute("ExtnDefaultShipTo")}' /> <s:set
		name='shipTo'
		value='#wcUtil.getShipToAdress(#defaultShipToId,wCContext.storefrontId)' />

	<div class="TabbedPanelsContent" id="AuthorizedLocations">
	<div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="tabs">
		<s:if test="#shipTo != null">
			<tr>
				<td valign="top" class="no-border-right-user paddingtop0">
				<div class="question">
				<ul>
					<li>Preferred Ship-To:&nbsp;</li>
					<li><span class="float-left"><a href="#"><img
						src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"
						alt="Preferred Authorized Location."
						title="Preferred Authorized Location." width="12" height="12"
						border="0" /></a></span></li>
				</ul>
				</div>
				<div class="clearview txt-small"  style="margin-left:2px;"><a
					id='changeShipToForUserProfile' href='#shipToUserProfile' onclick="javascript: writeMetaTag('WT.ti', 'xpedx / User Profile');" class=" underlines">[Change]</a></div>
				</td>
				<td width="81%" valign="top" class="no-border-right-user paddingtop0">
						<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#defaultShipToId)" /><br/>
						<s:if test="%{#shipTo.getOrganizationName()!=''}">
							<s:property	value='#shipTo.getOrganizationName()'/><br/>
						</s:if>
						<s:iterator value="#shipTo.getAddressList()" id="adressLine">
							<s:property value="adressLine"/> <br>	<!-- break for user address line 2 and  jira 3244--->		
						</s:iterator>
						<s:property value ="#shipTo.getCity()" />,
						<s:property value ="#shipTo.getState()" />
						<s:property value="%{@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(#shipTo.getZipCode())}" />&nbsp;
						<s:property value ="#shipTo.getCountry()" /><br/>
						<!-- JIRA 1878 -->
						<s:url id='targetURL' namespace='/common' action='xpedxGetAssignedCustomersForDefaultShipTo' />
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr>	<td valign="top" class="no-border-right-user padding0"><div class="question">
                        <ul>
                          <li>Preferred Ship-To:&nbsp;</li>
                          <li><span class="float-right  padding-right4 margin-right"><a id="purposeofmails-al2" href="#"><img width="12" height="12" border="0" title="Preffered Authorized Location" alt="Preffered Authorized Location" style="margin-top:2px;" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"></a></span>
                          </li>
                        </ul>
                        </br><a id='changeShipTo' href='#ajax-assignedShipToCustomers' class="txt-lnk-sml-1">[Change]</a>
                      </div>                      
                    </td>
						<td valign="top" class="no-borders paddingtop0"><div class="float-right">
						<img height="12" border="0" width="12" alt="help" src="<s:url value='/xpedx/images/icons/12x12_grey_help.png'/>"/></div>
							<%-- <s:text name="No Preferred Ship-To is selected"></s:text><br/> --%>
							<s:text name="MSG.SWC.SHIPTO.NOSHIPTO.INFO.NOPREFERREDSHIPTO"></s:text><br/>
							<s:url id='targetURL' namespace='/common' action='xpedxGetAssignedCustomers' />
							
						</td>
			</tr>
		</s:else>
		<s:action name="paginatedAuthorizeCustomerAssignment" namespace="/profile/user" executeResult="true" />
		<tr>
			<td valign="top" class="no-border-right-user ">&nbsp;</td>
			<td valign="top" class="no-border-right-user ">&nbsp;</td>
		</tr>
	</table>
	</div>
	</div>

	<!-- ------------------------------------------------------------------------------------------------------------------------ -->
	<!-- ----------------------------------         Site Preference Div to be added here     ----------------------------------- -->
	<!-- ------------------------------------------------------------------------------------------------------------------------ -->

	<div class="TabbedPanelsContent">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" colspan="2" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li><span class="subtitle">Catalog Preferences</span></li>
			</ul>
			</div>
			</td>
		</tr>
		<tr>
			<td width="27%" valign="top" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li>Preferred Catalog View:&nbsp;</li>
				<li><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"
					width="12" height="12" border="0"
					alt="Default view setting for Catalog display."
					title="Default view setting for Catalog display." /></a></li>
			</ul>
			</div>
			</td>
			<td width="73%" valign="top" class="no-border-right-user padding00">
			<div class="divs">
			<div><s:radio list="#{'0': ''}" name="b2bCatalogView"
				id="b2bCatalogView" value='defaultB2bCatalogView' /></div>
			<div class="view1"></div>
			<div>Full View - 1 item per row</div>
			</div>
			<div class="divs">
			<div><s:radio list="#{'2': ''}" name="b2bCatalogView"
				id="b2bCatalogView" value='defaultB2bCatalogView' /></div>
<!-- 			<div class="view3"></div> -->
			<div><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/mini-view.png" style="margin: 0 6px;" class="mini-view" title="Mini View" alt="mini-view"/></div>
			<div>Mini View - 4 items per row</div>
			</div>
			<div class="divs">
			<div><s:radio list="#{'1': ''}" name="b2bCatalogView"
				id="b2bCatalogView" value='defaultB2bCatalogView' /></div>
			<div class="view2"></div>
			<div>Condensed View - 2 items per row</div>
			</div>
			<div class="divs">
			<div><s:radio list="#{'3': ''}" name="b2bCatalogView"
				id="b2bCatalogView" value='defaultB2bCatalogView' /></div>
<!-- 			<div class="view4"></div> -->
			<div><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/grid-view.png" style="margin: 0 6px;" class="grid-view" title="Grid View" alt="grid-view"/></div>
			<div>Grid View - No images</div>
			</div>
			</td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li>Preferred Product Category:&nbsp;</li>
				<li><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"
					alt="Defaults your preferred catalog on the homepage."
					title="Defaults your preferred catalog on the homepage." width="12"
					height="12" border="0" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user user-profile padding0 ">
			<s:select headerKey="" headerValue="- Select Category -" name="PrefCatalog" value='prefCategory' 
				list="getMainCategories()" cssClass="x-input" /></td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding0"
				style="padding-top: 20px;">
			<div class="question">
			<ul>
				<li><strong>PO List&nbsp;</strong></li>
				<li><a href="#"><img
					alt="List of PO numbers made available at order submission."
					title="List of PO numbers made available at order submission."
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" width="12" height="12"
					border="0" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding0"
				style="padding-top: 20px;">
			<div class="table-top-bar" style="width: 80%; clear: both;">
			<div class="table-top-bar-L"></div>
			<div class="table-top-bar-R"></div>
			</div>
			<table border="0" align="left" cellpadding="0" cellspacing="0"
				id="mil-list-new"
				style="background: none repeat scroll 0% 0% transparent; width: 80%;">
				<tbody>
					<tr>
						<td width="55%" valign="top" class="noBorder noBorders-FAFAFA">
						<select style="width: 250px;" id="selectPOName" name="select"
							multiple="multiple">
							<s:iterator value="POList" id="poListTbl" status="poListStatus">
								<s:set name="poListValue" value='value' />
								<option><s:property value="poListValue" /></option>
							</s:iterator>
						</select></td>
						<td valign="top" class="noBorder">
						<div class="add-list-label">Enter value to add:</div><input
							name="addToPOListName" id="addToPOListName"
							class="input-details-cart  x-input" style="width: 150px;"
							title="POname" tabindex="12" maxlength="22"/>
						<div class="clearview">&nbsp;</div>
						<div class="clearview">
						<ul class="float-left">
							<li class="float-left margin-10"><a href="#"
								class="grey-ui-btn"
								onclick="javascript: return addToPOListfn();"><span>Add
							to List</span></a></li>
						</ul>
						</div>
						<div class="clearview">&nbsp;</div>
						<div class="clearview">
						<ul class="float-left">
							<li class="float-left margin-10"><a href="#"
								class="grey-ui-btn"
								onclick="javascript: return removeFrmPOList();"><span>Remove
							from List</span></a></li>
						</ul>
						</div>
						</td>
					</tr>
				</tbody>
			</table>
			<div id="table-bottom-bar" style="width: 80%; clear: both;">
			<div id="table-bottom-bar-L"></div>
			<div id="table-bottom-bar-R"></div>
			</div>
			</td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding0">
			<div class="question">
			<ul>
				<li><span class="subtitle">Email Preferences&nbsp;</span></li>
				<li><a href="#"><img
					title="Types of email notifications regarding order status."
					alt="Types of email notifications regarding order status."
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" alt="help" width="12"
					height="12" border="0" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding0">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding0">
			<div class="question">
			<ul>
				<li><s:checkbox name="orderConfirmationEmailFlag"
					fieldValue="true" value='isSendOrderConfEmail()' /></li>
				<li>Order Confirmation&nbsp;</li>
				<li><a href="#"><img
					title="Email notification that your order has been placed."
					title="Email notification that your order has been placed."
					height="12" border="0" width="12"
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding0"><span
				class="clearview"> Additional Order Confirmation Email
			Address(es):</span>
			<div class="table-top-bar"
				style="width: 80%; clear: both; margin-top: 10px;">
			<div class="table-top-bar-L"></div>
			<div class="table-top-bar-R"></div>
			</div>
			<table border="0" align="left" cellpadding="0" cellspacing="0"
				id="mil-list-new"
				style="background: none repeat scroll 0% 0% transparent; width: 80%;">
				<tbody>
					<tr>
						<td width="55%" valign="top" class="noBorder noBorders-FAFAFA">
						<select style="width: 250px;" name="selectOrderConfEmails"
							id="selectOrderConfEmails" multiple="multiple">
							<s:iterator value="AddnlEmailAddrList" id="AddnlEmailAddrListTbl"
								status="AddnlEmailAddrListStatus">
								<s:set name="AddnlEmailAddr" value='value' />
								<option><s:property value="#AddnlEmailAddr" /></option>
							</s:iterator>
						</select></td>
						<td valign="top" class="noBorder">
						<div class="add-list-label">Enter value to add:</div><input name="confEmailText"
							id="confEmailText" class="input-details-cart  x-input" id="Text4"
							style="width: 150px;" tabindex="12" maxlength="150" />
						<div class="clearview">&nbsp;</div>
						<div class="clearview">
						<ul class="float-left">
							<li class="float-left margin-10"><a href="#"
								class="grey-ui-btn"
								onclick="javascript: return addOrderConfEmailToList();"><span>Add
							to List</span></a></li>
						</ul>
						</div>
						<div class="clearview">&nbsp;</div>
						<div class="clearview">
						<ul class="float-left">
							<li class="float-left margin-10"><a href="#"
								class="grey-ui-btn"
								onclick="javascript: return removeOrderConfEmailFrmList();"><span>Remove
							from List</span></a></li>
						</ul>
						</div>
						</td>
					</tr>
				</tbody>
			</table>
			<div id="table-bottom-bar" style="width: 80%; clear: both;">
			<div id="table-bottom-bar-L"></div>
			<div id="table-bottom-bar-R"></div>
			</div>
			</td>
		</tr>
		<%-- <tr>
			<td valign="top" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li><s:checkbox name="orderShipmentEmailFlag" fieldValue="true"
					value='isSendOrderShipEmail()' /></li>
				<li>Order Shipped&nbsp;</li>
				<li><a href="#"><img width="12" height="12" border="0"
					alt="Email notification that your order has been shipped."
					title="Email notification that your order has been shipped."
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding00">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li><s:checkbox name="backorderEmailFlag" fieldValue="true"
					value='isSendBackOrderEmail()' /></li>
				<li>Order Backorder&nbsp;</li>
				<li><a href="#"><img border="0"
					alt="Email notification for backorders."
					title="Email notification for backorders." width="12" height="12"
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding00">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top" class="no-border-right-user padding00">
			<div class="question">
			<ul>
				<li><s:checkbox name="orderCancellationEmailFlag"
					fieldValue="true" value='isSendOrderCancelEmail()' /></li>
				<li>Order Cancelled&nbsp;</li>

				<li><a href="#"><img width="12" height="12" border="0"
					title="Email notification that your order has been cancelled."
					alt="Email notification that your order has been cancelled."
					src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;" /></a></li>
			</ul>
			</div>
			</td>
			<td valign="top" class="no-border-right-user padding00">&nbsp;</td>
		</tr> --%>
		<tr>
			<td valign="top" colspan="2" class="no-border-right-user padding00">
			<div class="question">

			<ul class="padding-top3">
				<li><strong>Quick Links&nbsp;</strong></li>
				<li><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px;"
					alt="User defined bookmarked links which display on the homepage."
					title="User defined bookmarked links which display on the homepage."
					width="12" height="12" border="0" /></a></li>
				 <li><a class="underlink" onclick="setInLineChange();" id="NewQL">[Add New]</a></li> 				
			</ul>
			</div>

			<div class="txt-small clearview"></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="tb1" class="standard-table">
			<tbody>
				<tr class="table-header-bar">
					<td width="35%" class="no-border-left table-header-bar-left"><span
						class="white"> Name</span></td>
					<td width="48%" align="left" class="  "><span
						class="white">URL</span></td>
					<td width="8%" align="left" ><span
						class="white">Show</span></td>
					<td width="9%" align="left"
						class="no-border-right table-header-bar-right"><span
						class="white">Sequence</span></td>
				</tr>
				</tbody>			
			</table>
			</td>
		</tr>
	</table>
	</div>

	<!-- ------------------------------------------------------------------------------------------------------------- -->
												<!-- Spending limit Div --> 
	<!-- ------------------------------------------------------------------------------------------------------------- -->
	<div class="TabbedPanelsContent" id="SpendingLimitApprovers">
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		style="padding: 0px;">
		<tr><td valign="top" colspan="4" width="100%" class="no-border-right-user"><div class="error" id="errorMsgFor_approvers" style="display : none"/>Please select an approver.</div></td></tr>
		<tr><td valign="top" colspan="4" width="100%" class="no-border-right-user"><div class="error" id="errorMsgFor_currency" style="display : none"/>Please select a currency.</div></td></tr>
		<tr>
			<td width="100%" colspan="2" valign="top"
				class="no-border-right-user padding00">
			<div style="width: 120px;"><span class="subtitle">Spending Limit&nbsp;</span>
			<a href="#"><img
				src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  style="margin-top:2px; display: inline;"
				alt="Manage who can approve orders and set order spending limits."
				title="Manage who can approve orders and set order spending limits."
				width="12" height="12" border="0" /></a>
			</div>
			</td>
		</tr>
		
		<tr>
			<td width="16%" valign="top" class="no-border-right-user padding00">Currency
			Type:</td>
			<td valign="top" width="84%" class="no-border-right-user padding00">
				<s:if test="#optedCurrency != null && #optedCurrency !=''"  >
					<s:if test="#disableSinceSelfApprover">
						<s:textfield id="spendingLtCurrency" name="spendingLtCurrency" value="%{#optedCurrency}" readonly="%{true}" cssStyle="width: 148px;"/>
					</s:if>
					<s:else>
					<s:select headerKey="" headerValue="- Select Currency -" tabindex="20" name="spendingLtCurrency"
						id="spendingLtCurrency" value="%{#optedCurrency}"
						list="currencyMap" cssClass="x-input" cssStyle="width: 150px;">
					</s:select>
				</s:else>
				</s:if>
				<s:else>
					<s:if test="!#disableSinceSelfApprover">
						<s:select headerKey="" headerValue="- Select Currency -" tabindex="20" name="spendingLtCurrency"
						id="spendingLtCurrency" value="- Select Currency -"
						list="currencyMap" cssClass="x-input" cssStyle="width: 150px;">
						</s:select>
					</s:if>	
				</s:else>
				
			</td>						
		</tr>
		<tr>
			<td width="16%" valign="top" class="no-border-right-user padding00">Spending
			Limit:</td>
			<td valign="top" width="84%" class="no-border-right-user padding00">
							 
				  <s:set name='spendingLimit' value="%{showSpendingLimit()}"/>
				 <s:if test="#spendingLimit != null && #spendingLimit != ''">
				   <s:if test="#disableSinceSelfApprover">
				     <s:textfield cssClass="x-input" cssStyle="width:148px;" id="spendingLt" readonly="%{true}" name="spendingLt" maxlength="6" value="%{showSpendingLimit()}" ></s:textfield>
				   </s:if>
				   <s:else>
				 	<s:textfield cssClass="x-input" cssStyle="width:148px;" id="spendingLt" name="spendingLt" maxlength="6" value="%{getUnformattedSpendingLimit()}"></s:textfield>
				  </s:else>	
				 </s:if>
				 <s:else>
				 	<s:if test="!#disableSinceSelfApprover">
				 		<s:textfield cssClass="x-input" cssStyle="width:148px;" id="spendingLt" name="spendingLt" maxlength="6" value=""></s:textfield>
				 	</s:if>	
				  </s:else>		 
			</td>
		</tr>
		
		<tr> <td style="border:0px; height:10px;"> &nbsp; </td></tr>
			<tr>
				<td width="100%" colspan="2" valign="top"
					class="no-border-right-user padding00"><span class="subtitle">Approvers</span></td>
			</tr>
			<tr>
				<td width="16%" valign="top" class="no-border-right-user padding00">Primary
				Approver:</td>
				<td valign="top" width="84%" class="no-border-right-user padding00 ">
					<s:if test="#primaryApprover != null && #primaryApprover != ''">
					 <s:if test="#disableSinceSelfApprover">
					 	<s:textfield cssClass="x-input" cssStyle="width:148px;" id="primaryApprover"  readonly="%{true}" name="primaryApprover" maxlength="6" value="%{#primaryApprover}"></s:textfield>						 
					 </s:if>
					  <s:else> 
					  	<s:select  headerKey="" headerValue="- Select Approver -" tabindex="20" name="primaryApprover" id="primaryApprover" 
							value="%{#primaryApprover}" list="availableApproversList"
							cssClass="x-input" cssStyle="width: 150px;">
						</s:select>		
					  </s:else>
					</s:if>
					<s:else> 
						<s:if test="!#disableSinceSelfApprover">
							<s:select headerKey="" headerValue="- Select Approver -" tabindex="20" name="primaryApprover" id="primaryApprover" 
							value="- Select Approver -" list="availableApproversList"
							cssClass="x-input" cssStyle="width: 150px;">
							</s:select>	
						</s:if>	
					</s:else>
				</td>
			</tr>
			<tr>
				<td valign="top" width="16%" class="no-border-right-user padding00">Alternate
				Approver:</td>
				<td valign="top" width="84%" class="no-border-right-user padding00">
				<s:if test="#alternateApprover != null && #alternateApprover != ''">
				 <s:if test="#disableSinceSelfApprover">
					<span class="no-border-right-user padding0 "> 
					  <s:textfield cssClass="x-input" cssStyle="width:148px;" id="alternateApprover"  readonly="%{true}" name="alternateApprover" maxlength="6" value="%{#alternateApprover}"></s:textfield>					  
					</span>				 
				 </s:if>
				 <s:else> 
				 	<span class="no-border-right-user padding0 "> 
					  <s:select headerKey="" headerValue="- Select Approver -" 
						tabindex="20" name="alternateApprover" id="alternateApprover"
						value="%{#alternateApprover}" list="availableApproversList" 
						cssClass="x-input" cssStyle="width:150px;">
					</s:select> 
					</span>				 
				 </s:else>
				</s:if>
				<s:else> 
					<s:if test="!#disableSinceSelfApprover">
						<span class="no-border-right-user padding0 "> 
					 	 <s:select headerKey="" headerValue="- Select Approver -" 
						tabindex="20" name="alternateApprover" id="alternateApprover"
						value="- Select Approver -" list="availableApproversList" 
						cssClass="x-input" cssStyle="width:150px;">
						</s:select> 
						</span>	
					</s:if>
				</s:else>
              </td>
			</tr>
		


	</table>
	</div>
	</div>
	</s:else>
	<div class="clearview">&nbsp;</div>
	<div>
	<ul class="float-right">
		<li class="float-left margin-10"><a href="#" onclick="javascript:window.location.reload();" class="grey-ui-btn"><span>Cancel</span></a></li>
		<li class="float-right"><a class="green-ui-btn" href="javascript:void(0);"
			onclick="javascript:callSave('myAccount', []);"  tabindex="38"><span>Save</span></a></li>
	</ul>
	</div>
	<div class="clearview">&nbsp;</div>
<%--Code Added For XNGTP-3196 --%>

<div class="success" id="msgFor_resetpassword" style="display : none; float: right"/>Instructions to reset this password have been sent to the registered user's email address.</div> 

<div class="error" id="errorMsgFor_userpassword" style="display : none; float: right"/>Please enter the same password in both Password and Confirm Password fields.</div>

<div class="error" id="errorMsgForMandatoryFields_myAccount" style="display : none; float: right"></div>

<s:if test="%{#session.errorNote!= null}">
	<div id="errorNote" class="error" style="display : inline; float: right"><s:property value='%{#session.errorNote}'/>
		<s:set name="errorNote" value="<s:property value=null />" scope="session"/>
		</div>
</s:if>

<div class="error" id="errorMsgFor_emailId" style="display : none; float: right"/>Please enter the same email address in both Email Address and Confirm Email Address fields.</div>

<div class="error" id="errorMsgFor_secretAnswer" style="display : none; float: right"/>Please enter the same Answer in both the Security Answer and the Confirm Answer fields.</div>

<s:hidden name='test_success' id="test_success" value="%{#_action.isSuccess()}" />
	<s:if test="%{#_action.isSuccess() && #disableSinceSelfApprover }">
		<div class="success" id="successMsgFor_save" style="display : inline; float: right"/>Profile has been updated successfully.</div> 	
	</s:if>
	<s:elseif test="%{#_action.isSuccess() && !#disableSinceSelfApprover}">
		<div class="success" id="successMsgFor_save" style="display : inline; float: right"/>Profile for <s:property value="%{#displayUserID}" /> has been updated successfully.</div>
	</s:elseif> 
	<s:elseif test="%{#_action.isSaveAddUser()}">
		<div class="success" id="successMsgFor_save" style="display : inline; float: right"/><s:property value="%{#displayUserID}" /> has been added successfully.</div>
	</s:elseif> 
	
<%-- End fix for XNGTP-3196 --%>


	<div class="clearview textAlignCenter">Last modified by
<s:property value="%{getContactFirstName()}"/> <s:property value="%{getContactLastName()}"/> on <s:property value="%{getLastModifiedDate()}"/></div>
	<div class="clearview">&nbsp;</div>
	</div>

</s:form></div>
</div>



<!-- Loading Quick Link Dialog -->
<script>
function enterPressed(evn) 
{
	//alert("FIRED!!");
if (window.event && window.event.keyCode == 13) {
  saveClick();
} else if (evn && evn.keyCode == 13) {
  saveClick();
}
}
document.onkeypress = enterPressed;

  function setInLineChange()
  {
	  //alert("FIRED!!");
	        var tbody = document.getElementById("tb1").getElementsByTagName("tbody")[0];
	        var row = document.createElement("TR");
		    row.id='inLineChangeRow';	
		    var data1 = document.createElement("td");		
			data1.setAttribute('class', ' noBorders padding-left1');
			var deleteLink = document.createElement('a');
			var imageIcon = document.createElement('img');
			imageIcon.setAttribute('src','<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_red_x.png');
			imageIcon.setAttribute('alt','delete');
			imageIcon.setAttribute('title','Remove');
			imageIcon.setAttribute('width','16');
			imageIcon.setAttribute('height','16');
			imageIcon.setAttribute('border','0');
			imageIcon.setAttribute('style','float:left');
			deleteLink.appendChild (imageIcon);
			var tablelen = document.getElementById("tb1");
			var table = document.getElementById("tb1").getElementsByTagName("tbody")[0];
			var rowCount = table.rows.length;
			deleteLink.setAttribute('href', 'javascript:deleteQuickLinkRow('+rowCount+');');
			data1.appendChild(deleteLink);
			var element2 = document.createElement("input");
			element2.type = "text";
			element2.name = "urlName";
			//element2.value = link;
			element2.setAttribute('id','theLinkName');
			element2.setAttribute('class','x-input user-profile-input');	
			element2.setAttribute('style','width:220px;');
			element2.setAttribute('tabindex','12');
			element2.setAttribute('maxlength','35');
			data1.appendChild(element2);

			var data2 = document.createElement("td");
			data2.setAttribute('valign', 'top');
			var element3 = document.createElement("input");
			element3.type = "text";
			element3.name = "urlValue";	
			//element3.value = urlN;
			element3.setAttribute('id','theUrl');
			element3.setAttribute('class','x-input user-profile-input');	
			element3.setAttribute('style','width: 330px;');
			element3.setAttribute('tabindex','12');		
			element3.setAttribute('maxlength','150');
			element3.setAttribute('onblur','saveClick();');
			//added for checkbox and sequence 3630
			var data3 = document.createElement("td");
			data3.setAttribute('valign', 'top');
			var element4 = document.createElement("input");  
			element4.type = "checkbox";
			element4.name = "showURL";  
			element4.setAttribute('tabindex','12');			
			element4.setAttribute('class', ' margin-15');
    		        element4.checked = true;
			data3.appendChild(element4);
			var data4 = document.createElement("td");
			data4.setAttribute('valign', 'top');

			var combo = document.createElement("select");
			combo.setAttribute('name','combo');
			combo.setAttribute('class', 'x-input');
			combo.setAttribute('tabindex','12');
			combo.setAttribute('onfocus','this.oldvalue = this.value;');
			combo.setAttribute('onchange', 'javascript:onChangeItemOrder(this, this.oldvalue, this.value);');
			combo.id = 'QuickLinksSeq'+rowCount;
				for(var i=1; i<=rowCount; i++) {
				var option = document.createElement("option");  
			    option.text = i;  
			    option.value = i;  
			    if(i == rowCount) {
				    option.selected = true;
			    }
			    try {  
			        combo.add(option, null); //Standard  
			    }catch(error) {  
			        combo.add(option); // IE only  
			    }
			}
			data4.appendChild(combo);
			//added for checkbox and sequence 3630
			data2.appendChild(element3);
			row.appendChild(data1);
			row.appendChild(data2);
			row.appendChild(data3);//added for 3630
			row.appendChild(data4);//added for 3630
			tbody.appendChild(row);		  
	  /*
	  var tbody = document.getElementById("tb1").getElementsByTagName("tbody")[0];
	  var row = document.createElement("TR");
	  row.id='inLineChangeRow';
      var cell1 = document.createElement("TD");
      cell1.innerHTML = "<input id=theLinkName type=text> ";
      cell1.valign="top";
      var cell2 = document.createElement("TD");
      cell2.innerHTML = "<input id=theUrl type=text>" + "<br><br>" + "<a class=green-ui-btn onclick=javascript:saveClick(); href=#addNewQL><span>Add</span></a>";
      var cell3 = document.createElement("TD");
      cell3.innerHTML = "&nbsp;";
      var cell4 = document.createElement("TD");
      cell4.innerHTML = "&nbsp;";	  
      row.appendChild(cell1);
	  row.appendChild(cell2);
	  row.appendChild(cell3);
	  row.appendChild(cell4);
	  tbody.appendChild(row); 
	  */                 
  }
  
  function insertInLineFields()
  {
	  document.getElementById('inLineChange').innerHTML("");
  }
	function loadDataOnStart() {
		var tbody = document.getElementById("tb1").getElementsByTagName("tbody")[0];
		
		<s:iterator value="quickLinkBeanArray" status="arrStatus">
			var row = null;
			
			<s:if test='#arrStatus.modulus(2) == 0'>
				row = document.createElement("tr")
		    </s:if>
		    <s:else>
				row = document.createElement("tr")
				row.setAttribute('class', 'odd');		    	
		    </s:else>
	
	//		var cT = new Date();
	//		var uniqueId = ""+cT.getDate() + cT.getDay() + cT.getFullYear() + cT.getHours() + cT.getMinutes()+ cT.getSeconds() ;
	//		var uniqueId = Math.floor(Math.random()*5000000000);	
	//		row.setAttribute('id',uniqueId);
			 			
			var data1 = document.createElement("td");		
			data1.setAttribute('class', ' noBorders padding-left1');
			var deleteLink = document.createElement('a');
			var imageIcon = document.createElement('img');
			imageIcon.setAttribute('src','<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_red_x.png');
			imageIcon.setAttribute('alt','delete');
			imageIcon.setAttribute('width','16');
			imageIcon.setAttribute('height','16');
			imageIcon.setAttribute('border','0');
			imageIcon.setAttribute('style','float:left');
			deleteLink.appendChild (imageIcon);
			deleteLink.setAttribute('href', 'javascript:deleteQuickLinkRow("<s:property value="%{#arrStatus.index+1}" escape='false'/>");');
			data1.appendChild(deleteLink);
			var element2 = document.createElement("input");
			element2.type = "text";
			element2.name = "urlName";			
			var urlName='<s:property value="urlName"/>';
			urlName=urlName.replace(/&amp;/g,"&");
			element2.value =urlName;	
			element2.setAttribute('class','x-input user-profile-input');	
			element2.setAttribute('style','width:220px;');
			element2.setAttribute('tabindex','12');
			element2.setAttribute('maxlength','35');
			data1.appendChild(element2);
	
			var data2 = document.createElement("td");
			data2.setAttribute('valign', 'top');
			var element3 = document.createElement("input");
			element3.type = "text";
			element3.name = "urlValue";
			var quickLinkURL='<s:property value="quickLinkURL"/>';
			quickLinkURL=quickLinkURL.replace(/&amp;/g,"&");
			element3.value =quickLinkURL;	
			element3.setAttribute('class','x-input user-profile-input');	
			element3.setAttribute('style','width: 330px;');
			element3.setAttribute('tabindex','12');	
			element3.setAttribute('maxlength','150');	
			data2.appendChild(element3);
	
			var data3 = document.createElement("td");
			data3.setAttribute('valign', 'top');
			var element4 = document.createElement("input");  
			element4.type = "checkbox";
			element4.name = "showURL";  
			element4.setAttribute('tabindex','12');		//Jira 3630	
			element4.setAttribute('class', ' margin-15');
			var showLink = '<s:property value="showQuickLink"/>';
		    if (showLink == 'Y')
				 element4.checked = true;
			data3.appendChild(element4);
	
			var data4 = document.createElement("td");
			data4.setAttribute('valign', 'top');
			var orderValue = '<s:property value="urlOrder"/>';  
			var linkSize = '<s:property value="quickLinkBeanArray.length" />';
	
			var combo = document.createElement("select");
			combo.setAttribute('name','combo');
			combo.setAttribute('class', 'x-input');
			combo.setAttribute('tabindex','12');//Jira 3630
			combo.setAttribute('onfocus','this.oldvalue = this.value;');
			combo.setAttribute('onchange', 'javascript:onChangeItemOrder(this, this.oldvalue, this.value);');
			combo.id = 'QuickLinksSeq'+"<s:property value="%{#arrStatus.index+1}" escape='false'/>";
	
			for(var i=1; i<=linkSize; i++) {
				var option = document.createElement("option");  
			    option.text = i;  
			    option.value = i;  
			    if(i == orderValue) {
				    option.selected = true;
			    }
			    try {  
			        combo.add(option, null); //Standard  
			    }catch(error) {  
			        combo.add(option); // IE only  
			    }
			}
	         			
			data4.appendChild (combo)
			        		
			row.appendChild(data1);
			row.appendChild(data2);
			row.appendChild(data3);
			row.appendChild(data4);
			tbody.appendChild(row);
		</s:iterator>      
		
	}

	loadDataOnStart();
	
</script>

<div style="display: none;" class='x-hidden dialog-body '>
<div id="viewShipToDlg" style="width: 745px; height: 397px; overflow: -moz-scrollbars-vertical;">
</div>
</div>
<script type="text/javascript">

var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");

function resetPassword()
{
	if(confirm('Do you really want to reset this password?')) {
		var url = '<s:property value="#ResetPasswordURL" />';
		url = ReplaceAll(url,"&amp;",'&');
		Ext.Ajax.request({
	        url :url,
	        params:{userId :'<s:property value="#loginid" />',
			customerId : '<s:property value="#customerId"/>',
			customerContactId:'<s:property value="#customercontact.getAttribute(\'CustomerContactID\')" />'},
	        method: 'POST',
	        success: function (response, request){
	        	//alert('User password will be sent to your registered email address!');
	        	//Added for Jira XNGTP-3196
			document.getElementById("msgFor_resetpassword").style.display = "inline";
	   		},
	   		failure: function (response, request){
	   			alert("Error sending reset password notification email!");
	         }
	    });
	}     
}
function validatePassword(){
	var url = '<s:property value="#ValidatePasswordURL" />';
		Ext.Ajax.request({
	        url :url,
	        params:{loginId :'<s:property value="#loginid" />',
			firstName : document.getElementById("firstName").value,
			lastName : document.getElementById("lastName").value,
			userPwdToValidate : document.getElementById("userpassword").value},
	        method: 'POST',
	        success: function (response, request){
	           var responseText = response.responseText;
	           errorDiv = document.getElementById("pwdValidationDiv");
               if(errorDiv){
               		if(responseText.indexOf("error")>-1){
	                    errorDiv.innerHTML = response.responseText;
	                    errorDiv.style.display = 'block';
	                }else{
	                	errorDiv.removeChild(document.getElementById('pwdErrorDiv'));
	                	//errorDiv.style.display = 'none';
	                }
               }
	   		},
	   		failure: function (response, request){
	   		   errorDiv = document.getElementById("pwdValidationDiv");
               if(errorDiv){
                errorDiv.style.display = 'none';
               }
	    	   alert("Error in service.");
	        }
	    });
}
</script></div>
<!-- Footer Start --> <s:action name="xpedxFooter" executeResult="true"
	namespace="/common" /> <!-- Footer End --> <!-- end main  --></div>
</body>
</html>
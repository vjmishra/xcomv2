<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<%--This is to setup reference to the action object so we can make calls to 
    action methods explicitly in JSP's.
    This is to avoid a defect in Struts that's creating contention under load.
    The explicit call style will also help the performance in evaluating Struts'
    OGNL statements. --%>
<s:bean
	name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils"
	id="wcUtil" />
<s:set name='_action' value='[0]' />
<s:set name="loggedInUser"
	value="%{#_action.getWCContext().getLoggedInUserId()}" />
<s:set name="logUser"
	value="%{#_action.getWCContext().getSCUIContext().getSecurityContext().getLoginId()}" />
<s:set name="assgnCustomers"
	value="#wcUtil.getAssignedCustomers(#loggedInUser)" />
<s:url id='targetURL' namespace='/common'
	action='xpedxGetAssignedCustomers' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='hUtil' />
<s:url id='setStockedCheckboxURL' action="setStockedCheckbox" namespace="/catalog"/>

<script type="text/javascript">
    var ajaxValidationURL = "<s:url value='/common/validators/ajaxValidateJson.action'/>";
 </script> 	
<script type="text/javascript">
	function setNormallyStockedCheckbox() {
		var isSelected = document.getElementById('stockedItemChk').value;
		alert(isSelected);
		Ext.Ajax.request({
      		url: '<s:property value="#setStockedCheckboxURL"/>',
      		params: {
          		isStockedCheckeboxSelected: isSelected
          		},
      		method: 'POST',
      		success: function (response, request){
          		alert('Stocked Check box set to '+isSelected);
      		},
      		failure: function (response, request){
          		alert("Can not set the Attribute to session");
      		}
   		});
	}
</script>
<script type="text/javascript">
    function setCatShortDesc(path,displayName){
        path = document.getElementById(path);
        for(var i=0;i<path.options.length;i++){
            if((path.options[i].selected==true) ){
                var  catShortName= path.options[i].text;
                var  selectedCatPath= path.options[i].value;
                document.getElementById(displayName).value=catShortName;
            }
        }
    }
    function showAssignedShipTo(url)
    {
    	Ext.get('ajax-assignedShipToCustomers').load({
           url :url,
           method: 'POST',
           callback:function(el, success, response){
	       		if(success)
	       		{
	   		        DialogPanel.show('assignedShipToCustomers');
	   		        svg_classhandlers_decoratePage();
	       		}
	       		else
	       		{
	   				DialogPanel.show('assignedShipToCustomers');
	                el.dom.innerHTML = response.responseText;
	       		}
            }
        });     
    }
    function editDetails()
    {   	
      document.getElementById('EditSelectedAddress').style.display = 'block'
    }
    function updateCurrentCustomer(url,selectedRadioButton){
    	var selectedCustomer = selectedRadioButton.value;
    	document.FormToPost.selectedCustomerId.value=selectedCustomer; 
    }
    function isValidString(stringValue)
    {
    	var numaric = stringValue;
    	for(var j=0; j<numaric.length; j++)
    	{
    	  var value1 = numaric.charAt(j);
    	  var hh = value1.charCodeAt(0);
    	  if((hh > 47 && hh<58) || (hh > 64 && hh<91) || (hh > 96 && hh<123))
    	  {
    		return true;
    	  }
    	  else{
    		 return false;
    	  }
    	}
    	return false;
    }
    String.prototype.trim = function () {
        return this.replace(/^\s*/, "").replace(/\s*$/, "");
    }
    function isValidQuantity(component){
        //valid size if 10,3(7 whole and 3 after digits)
        var quantity = component.value.trim();
        var qtyLen = quantity.length;
        var validVals = "0123456789.";
        var isValid=true;
        var char;
        for (i = 0; i < qtyLen && isValid == true; i++) {
           char = quantity.charAt(i); 
           if (validVals.indexOf(char) == -1) 
           {
        	   alert("Enter Valid Numeric Value"); 
              isValid = false;
           }
     	}
     	if (!isValid){
            component.value = "";
            return false;
     	}
  
        var qtyDecPointAt = quantity.indexOf(".");
        var wholenum = quantity.substr(0,qtyDecPointAt);
		if (qtyDecPointAt == -1){
			wholenum = quantity;
		}
        if (wholenum.length > 7){
            var val = quantity.substr(0,7);
			if (qtyDecPointAt != -1){
				val = val + quantity.substr(qtyDecPointAt,qtyLen);
			}
			quantity = val;
            component.value = quantity;
        }
        if (qtyDecPointAt != -1 && qtyDecPointAt < qtyLen - 4) {
            quantity = quantity.substr(0,qtyDecPointAt + 4);
            component.value = quantity;
            return false;
        }
        return true;
    }
    function saveShipToChanges(url)
    {
    	var selectedCustomer=document.FormToPost.selectedCustomerId.value; 
    	  
    	var xpedxSTName=document.FormToPost.xpedxSTName.value;
    	var xpedxSTStreet=document.FormToPost.xpedxSTStreet.value;
    	var xpedxSTAddressLine2=document.FormToPost.xpedxSTAddressLine2.value;
    	var xpedxSTAddressLine3=document.FormToPost.xpedxSTAddressLine3.value;
    	var xpedxSTCity=document.FormToPost.xpedxSTCity.value;
    	var xpedxSTState=document.FormToPost.xpedxSTState.value;
    	var xpedxSTZip=document.FormToPost.xpedxSTZip.value;
    	
    	if(selectedCustomer != null){
    		document.body.style.cursor = 'wait';
    		Ext.Ajax.request({
    	        url: url,
    	        params: {
    				selectedCurrentCustomer: selectedCustomer,
    				'xOverriddenShipToAddress.xpedxSTName':xpedxSTName,
    				'xOverriddenShipToAddress.xpedxSTStreet':xpedxSTStreet,
    				'xOverriddenShipToAddress.xpedxSTAddressLine2':xpedxSTAddressLine2,
    				'xOverriddenShipToAddress.xpedxSTAddressLine3':xpedxSTAddressLine3,
    				'xOverriddenShipToAddress.xpedxSTCity':xpedxSTCity,
    				'xOverriddenShipToAddress.xpedxSTState':xpedxSTState,			
    				'xOverriddenShipToAddress.xpedxSTZip':xpedxSTZip
    	        },
    	        method: 'POST',
    	        success: function (response, request){
    	            document.body.style.cursor = 'default';
    	            window.location.reload( true );
    	        },
    	        failure: function (response, request){
    	            document.body.style.cursor = 'default';	
    	        }
    	    });	
      	}
        document.body.style.cursor = 'default';
        	
    }
    function setSelectedAddressAsDefault(url)
    {
   		document.body.style.cursor = 'wait';
   		Ext.Ajax.request({
   	        url: url,    	        
   	        method: 'POST',
   	        success: function (response, request){
   	            alert('Successfully set as default customer');
   	        },
   	        failure: function (response, request){
   	   	        alert("request"+request+"response"+response);
   	            alert('Failed to set');
   	         	document.body.style.cursor = 'default';	            
   	            return;
   	        }
   	    });	
        document.body.style.cursor = 'default';
    }

	
	  function loadDialog(){
		 
		isguestuser = "<s:property value='%{wCContext.guestUser}'/>";
		assgnCustomerSize ='<s:property value="#assgnCustomers.size()"/>';
		defaultShipTo = '<s:property value="#wcUtil.getDefaultShipTo()"/>';
		customerSelected = '<s:property value="#wcUtil.isCustomerSelectedIntoConext(#_action.getWCContext())"/>';
		
	  	if(isguestuser!="true"){
			  	if(defaultShipTo=="" && assgnCustomerSize>0 && customerSelected!="true"){
	  				showAssignedShipTo('<s:property value="#targetURL"/>');
	  				svg_classhandlers_decoratePage();
	  			     alert("Please Do Select DefaultShipTo"); 
	  	  	}else if(assgnCustomerSize==0){
		  		alert("There are no shipTo locations assigned for this customer");
		  	}
	  	}
	  
	  }
	  
</script>
<s:set name='isProcurementUser' value='wCContext.procurementUser' />
<s:set name='isProcurementInspectMode'
	value='#hUtil.isProcurementInspectMode(wCContext)' />
<s:set name='isGuestUser' value="wCContext.guestUser" />

<noscript>
<div class="noScript"><s:text name='NoScriptWarning' /></div>
</noscript>

<s:set name='catDoc' value='#attr["RootCatalogDoc"].documentElement' />
<s:if test="%{catDoc == null}">
	<s:action name="catalogNavBar" executeResult="false"
		namespace="/catalog" />
	<s:set name='catDoc' value='#attr["RootCatalogDoc"].documentElement' />
</s:if>

<strong class="logo"><img
	src="<s:property value='%{logoURL}'/>"
	alt="<s:text name='header.logo.alttext.phrase'><s:param value='wCContext.storefrontId'/></s:text>" /></strong>

<s:if test='%{custLogoURL != null && custLogoURL != "" }'>
	<div><strong class="logo"><img
		src="<s:property value='%{custLogoURL}'/>"
		alt="<s:text name='header.logo.alttext.phrase'><s:param value='wCContext.customerId'/></s:text>" /></strong>
	</div>
</s:if>
<s:if test="#isGuestUser == true">
	<div id="headerSignUpNowDiv" style="position: absolute; left: 250px;"><s:text
		name="Get Your Personalized Account" /> <br />
	<s:form name='newUserRegistration' namespace='/profile/user'
		action='XPEDXRegisterUser'>
		<s:submit type="button" value="SignUp Now" align="right" />
	</s:form></div>
	<div id="headerTourImageDiv"
		style="position: absolute; left: 450px; size: 50px;"><img src=""
		height="50" width="50" /></div>
	<div id="headerTourDiv" style="position: absolute; left: 500px;">
	<s:text name="Tour our e-commerce site and services"></s:text><br />
	<s:submit type="button" value="Take the Tour" align="right"></s:submit>
	</div>
</s:if>
<swc:dialogPanel id="assignedShipToCustomers" isModal="true"
	title="Select ship to:">
	<s:div id="ajax-assignedShipToCustomers"
		cssStyle="width : 600px; height : 600px;overflow: -moz-scrollbars-vertical;">
	</s:div>
</swc:dialogPanel>





<s:if test='!#isProcurementInspectMode'>
	<div class="searchbox-1"><s:form name='newSearch'
		action='newSearch' namespace='/catalog'>
		<div class="searchbox-form">
		<div class="searchbox-form-bg"><label><s:text
			name="searchTitle" /></label> <select name='path' id='path' tabindex="2011"
			style='vertical-align: middle;'>
			<option value="/"><s:text name="searchDropdownTitle" /></option>
			<s:iterator
				value='XMLUtils.getElements(#catDoc, "//CategoryList/Category")'
				id='cat'>
				<s:if test='#cat.getAttribute("CategoryPath") == path'>
					<option
						value="<s:property value='#cat.getAttribute("CategoryPath")'/>"
						selected><s:property
						value='#cat.getAttribute("ShortDescription")' /></option>
				</s:if>
				<s:else>
					<option
						value="<s:property value='#cat.getAttribute("CategoryPath")'/>"><s:property
						value='#cat.getAttribute("ShortDescription")' /></option>
				</s:else>
			</s:iterator>
		</select> <label><s:text name="searchDropdownTitle_1" /></label>
		<div class='inlineWrapper'><s:textfield name="searchTerm"
			tabindex="2012" cssClass="searchTermBox"></s:textfield> <s:submit
			type='button' label='' cssClass='searchButton' tabindex='2013' /></div>
		</div>
		</div>
		<div class="searchbox-links"><s:url id='advancedSearch'
			action='advancedSearch.action' namespace='/catalog'>
			<s:param name='isNewOP' value='%{true}' />
		</s:url> <s:a href='%{advancedSearch}' tabindex="2014">
			<s:text name="advancedSearch" />
		</s:a> <!-- <a href="#">Product Advisor</a> --></div>
	</s:form></div>

</s:if>
<s:set name='isThereAUser' value="wCContext.thereAUser" />

<ul class="header-subnav commonHeader-subnav">
	<s:if test='#isGuestUser != true'>
		<li><s:include value="../common/address/XPEDXReadOnlyAddress.jsp" />
		</li>
		<li><s:a href="javascript:showAssignedShipTo('%{#targetURL}')">Select ShipTo</s:a>
		</li>
		<li><s:action name="xpedxDraftOrderDropDown" executeResult="true"
			namespace="/order" /></li>
		<li><s:action name="xpedxPendingApprovalCount"
			executeResult="true" namespace="/order" /></li>
	</s:if>
	<s:if test='!#isProcurementInspectMode'>
		<li><a
			href="<s:url action="home" namespace="/home" includeParams='none'/>"
			tabindex="2001"><s:text name="link.home" /></a></li>
	</s:if>


	<s:if test='#isGuestUser != true'>

		<!-- CODE_START -  Temp placeholder for 'My List' -PN -->
		<s:url id='myListsLink' namespace='/xpedx/myItems' action='XPEDXMyItemsList.action'>
			<s:param name="filterByAllChk" value="%{true}" />
			<s:param name="filterByMyListChk" value="%{true}" />
		</s:url>
		<li>
			<s:a href='%{myListsLink}'>
				<b>My Lists</b>
			</s:a>
		</li>
		<!-- CODE_END -  Temp placeholder for 'My List' -PN -->

		<s:url id='emailSampleLink' namespace='/xpedx/services'
			action='XPEDXServicesHome'>
		</s:url>
		<li><s:a href='%{emailSampleLink}'>Services</s:a></li>
	</s:if>

	<s:if test='!#isProcurementUser'>
		<s:if test='(isThereAUser == false) || (#isGuestUser == true)'>
			<s:if test='(#hUtil.sSLSwitchingEnabled == true)'>
				<li><a href='<s:url value="%{secureLoginURL}" />'
					tabindex="2002"><s:text name="link.login" /></a></li>
			</s:if>
			<s:else>
				<li><a
					href="<s:url action="loginFullPage" namespace="/home" includeParams='none' />"
					tabindex="2002"><s:text name="link.login" /></a></li>
			</s:else>
		</s:if>
		<s:else>
			<li><a
				href="<s:url action="getUserInfo" namespace="/profile/user" includeParams='none'/>"
				tabindex="2003"><s:text name="link.account" /></a></li>
			<li><a
				href="<s:url action="portalHome" namespace="/home" includeParams='none'/>"
				tabindex="2004"><s:text name="link.activity" /></a></li>
			<li><a
				href="<s:url action="logout" namespace="/home" includeParams='none'><s:param name='sfId' value='wCContext.storefrontId'/></s:url>"
				tabindex="2005"><s:text name="link.logout" /></a></li>
		</s:else>
	</s:if>
	<s:else>
		<s:url id='procurementPunchOutURL' action='procurementPunchOut'
			namespace="/order" escapeAmp="false">
			<s:param name='mode' value='"cancel"' />
			<s:param name='draft' value='"Y"' />
		</s:url>

		<li><a href="<s:property value='#procurementPunchOutURL'/>"
			tabindex="2003"><s:text name="link.procurementCancelAndReturn" /></a></li>
	</s:else>


	<li><!-- Begin: anonymouse user: remember me --> <s:set
		name="username" value="wCContext.getWCAttribute('WM_UserName')" /> <s:if
		test='wCContext.rememberedUserId != ""'>
		<s:if test='#isGuestUser == true'>
			<s:url id="clickifnotyouURL" action="ifThisIsNotYou"
				namespace="/home" includeParams='none' />
			<div class="message-1"><s:property value='%{username}' />
			<div class="message-2"><s:a href="%{clickifnotyouURL}"
				cssClass="message-4" tabindex="2006">
				<s:text name="Click.here" />
			</s:a></div>
			</div>
		</s:if>
	</s:if> <!-- End: anonymouse user: remember me --></li>

</ul>

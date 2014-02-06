<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Punchout Landing Page</title>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/punchout/po-xpedx<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</head>

<body class="bodyclass">
<div class="ui-po-wrap">
  <div class="ui-po-brand"></div>
  <div class="ui-po-content">
    <div class="ui-po-search"> <span>Search for Ship To Location</span>
     <s:form  id="punchoutShipToSearchForm" name="punchoutShipToSearchForm" action="punchoutShipToSearchAction" namespace="/punchout" method="post" >
        <div class="ui-po-searchinput">
          <input type="text" class="ui-searchbox" id="searchString" name="searchString"/>
        </div>
        <div class="ui-po-floatbtn">
          <input name="search" type="button" class="po-search-btn" onclick="searchPunchoutShipTo()"/>
        </div>
    </s:form>
    </div>
    <div class="ui-po-errormsg"></div>
    <div class="clearfix" ></div>
    <s:set name='searchedString' value='searchString'/>
    <div class="ul-po-results">Search Results for: <span class="blackText"><s:property value="#searchedString" /></span></div>
   							<div>
						  		<s:form  id="ChangeShipToForm" name="ChangeShipToForm" action="setCurrentCustomerIntoContext-punchout" namespace="/common" method="post" >
									<s:hidden id="setSelectedAsDefault" value="true" name="setSelectedAsDefault"/>
									<s:hidden id="initPrefs" value="true" name="initPrefs"/>
									<s:hidden id="selectedCurrentCustomer" value="" name="selectedCurrentCustomer"/>									
								</s:form>
							</div>
					      
					     <div id="wrapper" class="location-list">    
   			 				<div class="expand-links"><a href="javascript:void(0)" class="expand">Expand All</a> | <a href="javascript:void(0)" class="collapse">Collapse All</a></div>
   			 				 <s:iterator id="divisionBean" value="divisionBeanList">					     
						        <s:set name='divisionName' value='#divisionBean.getDivisionName()'/>
						        <s:set name='shipToCustomers' value='#divisionBean.getShipToCustomrs()'/>
	   			 				<div class="question"><s:property value="#divisionName" /></div>
	    					    <div style="display: none;" class="answer">
							  		<ul>	   	  
							    	  	<s:iterator status="status" id="shipToCustomer" value='#shipToCustomers'>					    	  	
								    	  <s:set name='id' value='#shipToCustomer.getShipToCustomerID()'/>
										  <s:set name='ShipToCustomerID' value='#shipToCustomer.getShipToCustomerID()'/>
										  <s:set name='shipToDisplayString' value='#shipToCustomer.getShipToDisplayString()'/>
										  <s:url id ='homeLink' action='setCurrentCustomerIntoContext-punchout' namespace='/common'>
					    						<s:param name="selectedCurrentCustomer" value="#ShipToCustomerID"/>
   						 						<s:param name="setSelectedAsDefault" value="true"/>
   						 						<s:param name="initPrefs" value="true"/>					    
											</s:url>								 
										   <li><s:a href="javascript:changeShipTo('%{#ShipToCustomerID}');"><s:property value="#shipToDisplayString" /></s:a></li>						 
							    	   </s:iterator>					    	   
							   	  </ul>
						   	   </div> 
					  		</s:iterator>   
				  </div>
  </div>
   <div class="clearfix" ></div>
   <div style="height:100px"></div>
</div>



<script>

	function changeShipTo(customerId){
		document.getElementById("selectedCurrentCustomer").value=customerId; 
		document.ChangeShipToForm.submit();
	}
	
	function searchPunchoutShipTo(){
		document.punchoutShipToSearchForm.submit();
	}
	
$(document).ready(function() { 

  $('.question').click(function() {
 
  if($(this).next().is(':hidden') != true) {
                $(this).removeClass('active'); 
    $(this).next().slideUp("normal");
  } else {
    $('.question').removeClass('active');  
     $('.answer').slideUp('normal');
    if($(this).next().is(':hidden') == true) {
    $(this).addClass('active');
    $(this).next().slideDown('normal');
     }   
  }
   });
 
  
  $('.question').next().slideDown('normal');
  {$('.question').addClass('active');}
 
  $('.expand').click(function(event)
    {$('.question').next().slideDown('normal');
        {$('.question').addClass('active');}
    }
  );
 
  $('.collapse').click(function(event)
    {$('.question').next().slideUp('normal');
        {$('.question').removeClass('active');}
    }
  );
});
</script>  

</body>
</html>

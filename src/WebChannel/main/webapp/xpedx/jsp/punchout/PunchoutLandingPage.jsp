<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Punchout Landing Page</title>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/punchout/po-xpedx.css"/>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</head>

<body>
<div class="ui-po-wrap">
  <div class="ui-po-brand"></div>
  <div class="ui-po-content">
    <div class="ui-po-search"> <span>Search for Ship To Location</span>
      <form action="" method="get" >
        <div class="ui-po-searchinput">
          <input type="text" class="ui-searchbox" />
        </div>
        <div class="ui-po-floatbtn">
          <div class="magnify-icon"><img width="16" height="15" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/punchout/ui-po-magnifying.png"/></div>
          <input name="search" type="button" class="po-search-btn" />
        </div>
      </form>
    </div>
    <div class="ui-po-errormsg">Error message can go here.</div>
    <div class="clearfix" ></div>
    <div class="ul-po-results">Search Results for ""</div>
   							<div>
						  		<s:form  id="ChangeShipToForm" name="ChangeShipToForm" action="setCurrentCustomerIntoContext-punchout" namespace="/common" method="post" >
									<s:hidden id="setSelectedAsDefault" value="true" name="setSelectedAsDefault"/>
									<s:hidden id="initPrefs" value="true" name="initPrefs"/>
									<s:hidden id="selectedCurrentCustomer" value="" name="selectedCurrentCustomer"/>									
								</s:form>
							</div>
					      
					     <div id="wrapper" class="location-list">    
   			 				<div class="expand-links"><a href="javascript:void(0)" class="expand">Expand All</a> | <a href="javascript:void(0)" class="collapse">Collapse All</a></div>
   			 				 <s:iterator id="entry" value="divisionWithShipTo.entrySet()">					     
						        <s:set name='divisionName' value='%{divisonDetails.get(#entry.getKey())}'/>
	   			 				<div class="question"><s:property value="#divisionName" /></div>
	    					    <div style="display: none;" class="answer">
							  		<ul>	   	  
							    	  	<s:iterator status="status" id="shipToCustomer" value='#entry.getValue()'>					    	  	
								    	  <s:set name='id' value='#shipToCustomer.getAttribute("ShipToCustomerID")' />
										  <s:set name='ShipToCustomerID' value='#shipToCustomer.getAttribute("ShipToCustomerID")' />
										  <s:set name='shipToDisplayString' value='#shipToCustomer.getAttribute("ShipToDisplayString")' />
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
</div>



<script>

	function changeShipTo(customerId){
		document.getElementById("selectedCurrentCustomer").value=customerId; 
		document.ChangeShipToForm.submit();
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

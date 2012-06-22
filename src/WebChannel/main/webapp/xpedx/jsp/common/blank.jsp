<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta content='IE=9' http-equiv='X-UA-Compatible' />

<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />
<s:set name="SelectedCustomerId" value="wCContext.customerId" />
<!-- styles -->


<!-- Hemantha -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-1<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/home<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/portalhome<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!-- <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/narrowBy<s:property value='#wcUtil.xpedxBuildKey' />.css" /> -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/styles<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-forms<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-quick-add<s:property value='#wcUtil.xpedxBuildKey' />.css"/>

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


<!-- carousel scripts css  -->
<!-- <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme<s:property value='#wcUtil.xpedxBuildKey' />.css" /> -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/skin<s:property value='#wcUtil.xpedxBuildKey' />.css" /><!--  fixed path -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- carousel scripts js   -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/demo<s:property value='#wcUtil.xpedxBuildKey' />.css"/><!--  fixed path -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree<s:property value='#wcUtil.xpedxBuildKey' />.css"/><!--  fixed path -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1.js"></script>
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
 -->

<!-- Page Calls -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/my-items<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/narrowBy<s:property value='#wcUtil.xpedxBuildKey' />.css" />


<!-- link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc.min.css" / -->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/ster/js/common/ajaxValidation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.accordion<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all<s:property value='#wcUtil.xpedxBuildKey' />.css" rel="stylesheet" />

<script type="text/javascript" src="jquery.mousewheel-3.0.2.pack.js/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="jquery.mousewheel-3.0.2.pack.js/js/fancybox/jquery.fancybox-1.3.4.js"></script>
<script type="text/javascript" src="jquery.mousewheel-3.0.2.pack.js/js/sorttable.js"></script>
<link rel="stylesheet" type="text/css" href="jquery.mousewheel-3.0.2.pack.js/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme<s:property value='#wcUtil.xpedxBuildKey' />.css" /><!--  fixed path -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog<s:property value='#wcUtil.xpedxBuildKey' />.css" />


<!-- Hemantha -->


<!-- Arun uses value Current customer id for this javascript  -->
<script type="text/javascript">
			$(document).ready(function() {
				$("#various1").fancybox({
					'titleShow'			: false,
					'transitionIn'		: 'fade',
					'transitionOut'		: 'fade'
				});

				$("#various5").fancybox({
					'onStart' 	: function(){
						showSelectedList('<s:property value="#CurrentCustomerId"/>');
						Ext.get("smilTitle_2").dom.innerHTML = "Select My Items for Location(s)";
					
					},
					'onClosed' : function(){
						var div = document.getElementById("dynamiccontent");
						div.style.display = "block";
						shareSelectAll(false);
					},
					'autoDimensions'	: false,
					'width' 			: 750,
					'height' 			: 235  
				});

				$("#dlgImportItemsLink").fancybox({
					'onClosed' : function(){
					document.getElementById("errorMsgForBrowsePath").style.display = "none";
					document.getElementById("errorMsgForRequiredField").innerHTML = "";
					document.getElementById("errorMsgForRequiredField").style.display = "none";
					if(document.getElementById("File"))
						document.getElementById("File").value = "";
					},
					'autoDimensions'	:false,
	    			'width'				: 620,
	    			'height'			: 460
				});
				
				$("#various3,#various4").fancybox({
					'onStart' 	: function(){
						/*if (callShareList){
							showShareList('<s:property value="#CurrentCustomerId"/>');
						}
						if (isUserAdmin) {
							Ext.get("rbPermissionShared").dom.checked = true;
						} else {
							Ext.get("rbPermissionPrivate").dom.checked = true;
						}*/
						if (isUserAdmin){
							showShareList('<s:property value="#CurrentCustomerId"/>');
						}
						Ext.get("smilTitle").dom.innerHTML = "New My Items List";
					},
					'onClosed' : function(){
						document.getElementById("mandatoryFieldCheckFlag_dlgShareList").value = "false";
						document.XPEDXMyItemsDetailsChangeShareList.listName.style.borderColor="";
			            document.getElementById("errorMsgForMandatoryFields_dlgShareList").style.display = "none";
			            document.XPEDXMyItemsDetailsChangeShareList.listName.value = "";
						document.XPEDXMyItemsDetailsChangeShareList.listDesc.value = "";
						document.XPEDXMyItemsDetailsChangeShareList.shareAdminOnly.checked=false;
						var radioBtns = document.XPEDXMyItemsDetailsChangeShareList.sharePermissionLevel;
						var div = document.getElementById("dynamiccontent");
						if(!isUserAdmin)
						{
							//Check Private radio button
							radioBtns[0].checked = true;
							//Hide Ship To Locations
							div.style.display = "none";
						}
						else
						{		
							//Check Shared radio button
							radioBtns[1].checked = true;
							//Display Ship To Locations
							div.style.display = "block";
						}
						shareSelectAll(false);
						document.getElementById("errorMsgForAddressFieldsHL").style.display = "none";
						
					},
					'autoDimensions'	: false,
					'width' 			: 780,
					'height' 			: 450  
				});
				
			});
			
			function updateShareListChild(){
			}
		
			function hideSharedListForm(){
				document.getElementById("dynamiccontent").style.display = "none";
			}

			function hideSelectedListForm(){
				document.getElementById("dynamicContentInSelectedList").style.display = "none";
			}
			
			function showSharedListForm(){
			var dlgForm 		= document.getElementById("dynamiccontent");
			if (dlgForm){
				dlgForm.style.display = "block";
			}
			}

			function showSelectedListForm(){
				var dlgForm 		= document.getElementById("dynamicContentInSelectedList");
				if (dlgForm){
					dlgForm.style.display = "block";
				}
			}
			
			function deleteItemList(){
				doAction_delete_item_list.dom.submit();
			}
			
	</script>
<%-- <title><s:text name='myitemslists.title' /></title> --%>
<title><s:property value="wCContext.storefrontId" /> - <s:text name='MSG.SWC.MIL.MYITEMLISTS.GENERIC.TABTITLE' /></title>

</head>
<!-- END swc:head -->


<!-- CODE_START - Global Vars -PN -->
<s:set name='wcContext' value="wCContext" />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
   	This is to avoid a defect in Struts that?s creating contention under load. 
   	The explicit call style will also help the performance in evaluating Struts? OGNL statements. 
   	--%>
<s:set name='_action' value='[0]' />

<s:set name="xutil" value="XMLUtils" />
<s:set name='categoryListElem' value="categoryListElement" />
<s:set name='childCategoryListElem' value="childCategoryListElement" />
<s:set name='fieldListElem' value="searchableIndexFieldListOutPutElement" />
<s:set name='outDoc2' value='%{outDoc.documentElement}' />

<!-- CODE_END - Global Vars -PN -->

<body class="  ext-gecko ext-gecko3">

</body>
</html>

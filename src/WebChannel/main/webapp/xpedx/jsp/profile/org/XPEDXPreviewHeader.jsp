<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<swc:html isXhtml="true">
<head>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that�s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
<s:set name='_action' value='[0]'/>
 <title>
   <s:property value="wCContext.storefrontId" /> - <s:text name="CustomerProfile"/>
 </title>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/profile<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/org/org<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/verifyAddress<s:property value='#wcUtil.xpedxBuildKey' />.js" ></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/address/editableAddress<s:property value='#wcUtil.xpedxBuildKey' />.js" ></script>

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='hUtil' />

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

	function closePreview()
	{
		window.close();
		document.previewCustLogo.submit();
	}

</script>
<noscript><div class="noScript"><s:text name='NoScriptWarning'/></div></noscript>

</head>
			 <s:set name='userListResId' value='"/swc/profile/ManageUserList"'/>
			<s:set name="AddressInformationTitle" scope="page" value="#_action.getText('Address_Information_Title')"/>
			 <s:set name='xmlUtil' value="#_action.getXMLUtils()" />
			 <s:set name='sdoc' value="#_action.getCustomerOrganizationEle()" />
			 <s:set name='sBuyerOrg' value='#xmlUtil.getChildElement(#sdoc, "BuyerOrganization")'/>
 			 <s:set name='CustomerCurrencyList' value='#xmlUtil.getChildElement(#sdoc, "CustomerCurrencyList")'/>
			 <s:set name='CustomerCurrency' value='#xmlUtil.getChildElement(#CustomerCurrencyList, "CustomerCurrency")'/>
			 <s:set name='parentCustomer' value='#xmlUtil.getChildElement(#sdoc, "ParentCustomer")'/>
             <s:set name='effectiveStatus' value='#xmlUtil.getAttribute(#sdoc, "AggregateStatus")' />

            <!-- Common codes -->
             <s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
             <s:set name='wccontext' value='#_action.getWCContext()'/>
             <s:set name='currency' value='#util.getCustomerCurrency(#wccontext)' />
             <s:set name='defaultCurrency' value="#_action.getCustomerDefaultCurrency()" />
             <s:set name='hasAccessToEdit' value="#_action.getIsChildCustomer()" />
             <s:set name='statusCodes' value="#util.getCommonCodes('CUSTOMER_STATUS', @com.sterlingcommerce.webchannel.core.CommonCodeDescriptionType@SHORT, #wccontext)" />

             <!--ENDS Common codes -->
             <s:set name='loginUserId' value="#wccontext.getCustomerId()" />
             <s:set name='cId' value='#xmlUtil.getAttribute(#sdoc, "CustomerID")'/>

             <s:if test='%{#cId != null && #loginUserId != null && (#cId == #loginUserId) }'>
               <s:set name='selfUser' value="%{'TRUE'}"/>
             </s:if>
             <s:if test='%{#hasAccessToEdit == "TRUE"}'>
	             <s:set name='isDisabled' value="%{'false'}"/>
             </s:if>
             <s:else>
             	 <s:set name='isDisabled' value="%{'true'}"/>
             </s:else>
             
           
<body>
<div id="navigate">
</div>
<div id="main">


<!-- begin Common header -->
<div class="t2-header commonHeader" id="headerContainer">
      <!-- add content here for header information -->
     
		<s:set name='catDoc' value='#attr["RootCatalogDoc"].documentElement' />

<s:if test="%{catDoc == null}">
    <s:action name="catalogNavBar" executeResult="false" namespace="/catalog" />
    <s:set name='catDoc' value='#attr["RootCatalogDoc"].documentElement' />
</s:if>
<strong class="logo"><img src="<s:property value='%{logoURL}'/>" alt="<s:text name='header.logo.alttext.phrase'><s:param value='wCContext.storefrontId'/></s:text>"/></strong>
<strong class="logo"><img src="<s:property value='%{custLogoUrl}'/>" alt="customer logo"/></strong>

<s:set name='isProcurementUser' value='wCContext.procurementUser'/>
<s:set name='isProcurementInspectMode' value='#hUtil.isProcurementInspectMode(wCContext)'/>

<s:if test='!#isProcurementInspectMode'>  
    <div class="searchbox-1">
        <s:form name='newSearch' action='newSearch' namespace='/catalog'>
            <div class="searchbox-form">
                <div class="searchbox-form-bg">
                    <label><s:text name="searchTitle"/></label>
                    <select name='path' id='path' tabindex="2011" style='vertical-align:middle;'>
                        <option value="/"><s:text name="searchDropdownTitle"/></option>
                        <s:iterator value='XMLUtils.getElements(#catDoc, "//CategoryList/Category")' id='cat'>
                            <s:if test='#cat.getAttribute("CategoryPath") == path'>
                                <option value="<s:property value='#cat.getAttribute("CategoryPath")'/>" selected><s:property value='#cat.getAttribute("ShortDescription")'/></option>
                            </s:if>
                            <s:else>
                                <option value="<s:property value='#cat.getAttribute("CategoryPath")'/>"><s:property value='#cat.getAttribute("ShortDescription")'/></option>
                            </s:else>
                        </s:iterator>
                    </select>
                    <label><s:text name="searchDropdownTitle_1"/></label>
                    <div class='inlineWrapper'>
                        <s:textfield name="searchTerm" tabindex="2012" cssClass="searchTermBox"></s:textfield>
                        <s:submit type='button' label='' cssClass='searchButton' tabindex='2013'/>
                    </div>
                </div>
            </div>
            <div class="searchbox-links">
                <s:url id='advancedSearch'action='advancedSearch.action' namespace='/catalog' >
                    <s:param name='isNewOP' value='%{true}'/>
                </s:url>
                <s:a href='%{advancedSearch}'  tabindex="2014">
                    <s:text name="advancedSearch"/>
                </s:a>
                <!-- <a href="#">Product Advisor</a> -->
            </div>
        </s:form>
    </div>
</s:if>
<s:set name='isThereAUser' value="wCContext.thereAUser" />
<s:set name='isGuestUser' value="wCContext.guestUser" />
<ul class="header-subnav commonHeader-subnav">
    <s:if test='!#isProcurementInspectMode'>
        <li><a href="<s:url action="home" namespace="/home" includeParams='none'/>"  tabindex="2001"><s:text name="link.home"/></a></li>
    </s:if>
    <s:if test='!#isProcurementUser'>
        <s:if test='(isThereAUser == false) || (#isGuestUser == true)'>
            <s:if test='(#hUtil.sSLSwitchingEnabled == true)'>
                <li><a href='<s:url value="%{secureLoginURL}" />' tabindex="2002"><s:text name="link.login"/></a></li>
            </s:if>
            <s:else>
                <li><a href="<s:url action="loginFullPage" namespace="/home" includeParams='none' />" tabindex="2002"><s:text name="link.login"/></a></li>
            </s:else>
        </s:if>
        <s:else>
            <li><a href="<s:url action="getUserInfo" namespace="/profile/user" includeParams='none'/>" tabindex="2003"><s:text name="link.account"/></a></li>
            <li><a href="<s:url action="portalHome" namespace="/home" includeParams='none'/>" tabindex="2004"><s:text name="link.activity"/></a></li>
            <li><a href="<s:url action="logout" namespace="/home" includeParams='none'><s:param name='sfId' value='wCContext.storefrontId'/></s:url>" tabindex="2005"><s:text name="link.logout"/></a></li>
        </s:else>
    </s:if>
    <s:else>
        <s:url id='procurementPunchOutURL' action='procurementPunchOut' namespace="/order" escapeAmp="false">
            <s:param name='mode' value='"cancel"'/>
            <s:param name='draft' value='"Y"'/>
        </s:url>

        <li><a href="<s:property value='#procurementPunchOutURL'/>" tabindex="2003"><s:text name="link.procurementCancelAndReturn"/></a></li>
    </s:else>
    <li>
        <!-- Begin: anonymouse user: remember me -->
        <s:set name="username" value="wCContext.getWCAttribute('WM_UserName')"/>
        <s:if test='wCContext.rememberedUserId != ""'>
            <s:if test='#isGuestUser == true'>
                <s:url id="clickifnotyouURL" action="ifThisIsNotYou" namespace="/home" includeParams='none'/>
                <div class="message-1"><s:property value='%{username}'/>
                    <div class="message-2"><s:a href="%{clickifnotyouURL}" cssClass="message-4" tabindex="2006"><s:text name="Click.here"/></s:a></div>
                </div>
            </s:if>
        </s:if>
        <!-- End: anonymouse user: remember me -->
    </li>
</ul>
<%-- Minicart --%>
<s:set name='shoppingCartResId' value='"/swc/order/guestShopping"'/>
<s:if test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#shoppingCartResId,wCContext)">
    <div id="miniCartLinkDiv" class="shopping-cart-1">
        <s:action name="miniCartLinkDisplay" namespace="/order" executeResult="true"/>
    </div>
    <div id="miniCartDiv" class="expanded-mini-cart">
    </div>
    <div  style='display:none;'>
        <s:form name='miniCartForm' id='miniCartForm'>
            <s:if test='(#parameters.minimalMiniCart == null) || (#yfcCommon.isVoid(#parameters.minimalMiniCart))'>
                <s:hidden id='minimalMiniCart' value='%{"false"}'/>
            </s:if>
            <s:else>
                <s:hidden id='minimalMiniCart' value='%{#parameters.minimalMiniCart}'/>
            </s:else>
            <s:hidden id='minimalMiniCartMessage' value='%{#parameters.minimalMiniCartMessage}'/>
            <s:if test='(#parameters.canExpandMiniCart == null) || (#yfcCommon.isVoid(#parameters.canExpandMiniCart))'>
                <s:hidden id='canExpandMiniCart' value='%{"true"}'/>
            </s:if>
            <s:else>
                <s:hidden id='canExpandMiniCart' value='%{#parameters.canExpandMiniCart}'/>
            </s:else>
            <s:hidden id='isGuestUser' value='%{#isGuestUser}'/>
            <s:hidden id='miniCartGeneralAJAXError' value='%{#_action.getText("miniCartGeneralAJAXError")}'/>
            <s:hidden id='miniCartDisplayError' value='%{#_action.getText("miniCartDisplayError")}'/>
            <s:hidden id='miniCartTitle' value='%{#_action.getText("ShoppingCart")}'/>
        </s:form>
        <s:url id='miniCartLinkDisplayURL' namespace='/order' action='XPEDXMiniCartLinkDisplay'/>
        <s:if test='(#parameters.miniCartListMaxElements == null) || (#yfcCommon.isVoid(#parameters.miniCartListMaxElements))'>
            <s:set name='miniCartListMaxElements' value='%{"5"}'/>
        </s:if>
        <s:else>
            <s:set name='miniCartListMaxElements' value='#parameters.miniCartListMaxElements'/>
        </s:else>
        <s:url id='miniCartDisplayURL' namespace='/order' action='XPEDXMiniCartDisplay'>
            <s:param name='miniCartListMaxElements' value='%{#miniCartListMaxElements}'/>
        </s:url>

        <s:url id='miniCartDeleteURL' namespace='/order' action='miniCartDelete'/>
        <s:url id='miniCartUpdateURL' namespace='/order' action='miniCartUpdate'/>
        <s:a id='miniCartLinkDisplayURL' href='%{#miniCartLinkDisplayURL}'/>
        <s:a id='miniCartDisplayURL' href='%{#miniCartDisplayURL}'/>
        <s:a id='miniCartDeleteURL' href='%{#miniCartDeleteURL}'/>
        <s:a id='miniCartUpdateURL' href='%{#miniCartUpdateURL}'/>
    </div>
</s:if>
		


</div><!-- //Common header end -->


<div class="container"> <!-- // begin t2-product-list -->
<div class="t2-mainContent" id="t2-mainContent"><!-- add content here for main content -->
<!-- main content header -->

<s:form action="delPreviewLogoHeader" name="previewCustLogo" method="post">
 <s:hidden name="fileName" id="fileName" value='%{previewFileName}'  />
 <input type="button" name="close" value="<s:text name="Close" />" class="submitBtnBg1" onclick = "javascript:closePreview()" />
</s:form>

<!-- // t2-main-content end -->
</div>
</div>
<!-- // container end -->
<div class="t2-footer commonFooter" id="t2-footer">
	<!-- add content here for footer -->
	<s:action name="footer" executeResult="true" namespace="/common" />
</div><!-- // footer end -->

</div><!-- // main end -->



       </body>
</swc:html>















































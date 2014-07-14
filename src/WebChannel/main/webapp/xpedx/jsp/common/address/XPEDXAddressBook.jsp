<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<%
/*
This JSP is intended to be used as part of the Address Book component.  For more information on
how to use this component, see the class level Javadoc comment in
com.sterlingcommerce.webchannel.common.address.GetAddressBookAction.java.
*/
%>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/address/addressBook<s:property value='#wcUtil.xpedxBuildKey' />.js" ></script>
<script type="text/javascript" language="JavaScript">

var <s:property value="addressArrayName"/> = new Array();

function callFunc(e){
	    if (e.keyCode == 13) {
		
		var searchText = document.getElementById("addrSearchTerm").value;
		var actionURL = '<s:url value="/common/displayAddressBook.action"><s:param name="addressType" value="%{'ShipTo'}"/><s:param name="targetForm" value="%{'saveOrderShipToAddress'}"/><s:param name="targetDiv" value="%{'readOnlyShipToAddressContentDiv'}"/><s:param name="addressArrayName" value="%{'addressBookData'}"/></s:url>';
		actionURL = actionURL + "&searchTextTerm=" + searchText;
		//actionURL = actionURL + "&addressDocString=" + <s:property value="addressArrayName"/>.toString();
		//var ele = <s:property value="addressArrayName"/>[0].toString();
		//alert(ele);
		actionURL=ReplaceAll(actionURL,"&amp;",'&');
		//alert(actionURL);
		Ext.Ajax.request({
	        url :actionURL,  
	        method: 'POST',
	        scripts: true,
	        success: function (response, request){
    	       theDiv = document.getElementById("addr-list-body");
               if(theDiv)
                    theDiv.innerHTML = response.responseText;

                 
	    	},
	    	failure: function ( response, request ) {
	    	   theDiv = document.getElementById("addr-list-body");
               if(theDiv){
                    theDiv.innerHTML = response.responseText;
               }
    
	    	}
	    });
		
        return false;
    }

}
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

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/common/address/addressBook<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />

<s:if test="!addressBook.isEmpty()">
<s:set name="counter" value="0"/>
<s:set name="needsShowMore" value="(numberOfColumns * numberOfInitialRows) < addressBook.size()"/>
<s:set name="columnWidthPercentage" value="100 / numberOfColumns"/>

<div align="right">
<s:textfield name="addrSearchTerm" id="addrSearchTerm" cssClass="searchTermBox" onkeypress="return callFunc(event)"></s:textfield>
</div>


<div class="panelBox" id="abc">
<table id="addressBookTable" border="1.5"  class="listTableBody">
  <s:iterator value="addressBook" id="renderAddress" status="iterStatus">
    <s:if test="#iterStatus.getIndex() % numberOfColumns == 0">
      <s:if test="!(#iterStatus.isFirst())">
        <s:if test="((#iterStatus.getIndex() / numberOfColumns) == numberOfInitialRows) && #needsShowMore">
          <tr>
            <td colspan="<s:property value="numberOfColumns"/>">
              <s:a id="ShowMoreAddresses" href="javascript:showMoreAddresses('addressBookTable', %{numberOfInitialRows});" tabindex='%{addressBooktabStartIndex+50}'>
                <s:text name="ShowMoreAddresses"/>
              </s:a>
            </td>
          </tr>
        </s:if>
      </s:if>

      <s:if test="#iterStatus.getIndex() >= (numberOfColumns * numberOfInitialRows)">
        <tr style="display:none">
      </s:if>
      <s:else>
        <tr>
      </s:else>
    </s:if>

	
    <td class="addressBook" valign="top" width="<s:property value="#columnWidthPercentage"/>%" >
      <s:set name="renderPersonInfo" value="#util.getElement(#renderAddress, 'PersonInfo')"/>
     <div id='addressText<s:property value="#counter"/>' >
        <s:include value="readOnlyAddress.jsp"/>
      </div>
      <s:if test='!(targetForm == "")'>
        <s:include value="PersonInfoData.jsp"/>
      </s:if>
      <s:if test='!((targetForm == "") && (targetDiv =="") && (callBackFunctionForUseAddress ==""))'>
        <s:a href="javascript:useAddress(%{#counter}, '%{addressArrayName}', '%{targetForm}', '%{targetDiv}', '%{callBackFunctionForUseAddress}', %{skipChangeEventsOnAddressSelection});"tabindex='%{addressBooktabStartIndex+#counter}'>
          <s:text name="UseThisAddress"/>
        </s:a>
      </s:if>
    </td>
	

    <s:set name="counter" value="#counter + 1"/>
  </s:iterator>
  <s:if test="addressBook.size() % numberOfColumns != 0">
    <s:set name="columnsToPad" value="numberOfColumns - (addressBook.size() % numberOfColumns)"/>
    <s:set name="paddingPercentage" value="(#columnWidthPercentage * #columnsToPad)"/>
        <td width='<s:property value="#paddingPercentage"/>%' colspan='<s:property value="#columnsToPad"/>'>&nbsp;</td>
  </s:if>
    </tr>
</table>
</div>
</s:if>
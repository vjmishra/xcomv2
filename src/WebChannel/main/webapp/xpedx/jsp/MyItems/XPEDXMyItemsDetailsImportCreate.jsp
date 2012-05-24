<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<swc:html>
    <head>
        <swc:head />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:property value="wCContext.storefrontId" /> - <s:text name='Items' /></title>
        <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all.css" />
        <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/advancedSearch.css"/>
        <script type="text/javascript">
  				 var attrPickerActions  = new Array(3);
				 attrPickerActions[0] = '<s:url action="attributemanagement-getattributeConfiguration" namespace="/catalog" escapeAmp="false" includeParams="none" />';
				 attrPickerActions[1] = '<s:url action="attributemanagement-getAttributeGroupListForDomain" namespace="/catalog" escapeAmp="false" includeParams="none" />';
				 attrPickerActions[2] = '<s:url action="attributemanagement-getAttributeGroupListForGroup" namespace="/catalog" escapeAmp="false" includeParams="none" />';
               
       </script>
        <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/advancedSearch.js"></script>
        <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/swcxmltreeloader.js"></script>
        <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sbc/picker/sbctreeloader.js"></script>
        <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sbc/picker/itemattrtreeloader.js"></script>
        <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sbc/picker/attrtreepanel.js"></script>
        <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sbc/picker/attributeutils.js"></script>
        <%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
		    This is to avoid a defect in Struts that�s creating contention under load. 
		    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
			<s:set name='_action' value='[0]'/>

        
        <s:set name="xutil" value="XMLUtils" />
        <s:set name='categoryListElem' value="categoryListElement" />
        <s:set name='childCategoryListElem' value="childCategoryListElement" />
        <s:set name='fieldListElem' value="searchableIndexFieldListOutPutElement" />
  </head>
<body>
<s:set name='outDoc2' value='%{outDoc.documentElement}' />
 
<s:url id='attrPicker' action='openattributelookup.action' namespace='/catalog' escapeAmp="false" includeParams='none' />
                     

    <s:url action='home.action' namespace='/home' id='myUrl' includeParams='none' />
    <s:set name='advancedSearchParameters'
        value='{"path", "", "indexField_1","","searchTerm_1","indexField_2","","searchTerm_2","indexField_3","","searchTerm_3","","priceRange_From","","priceRange_To","","productFeature_3",
            														"","productFeature_1","","productFeature_2","","isSupersededIncluded","","productFeature_1","","productFeature_2","","productFeature_3"}' />
    <swc:breadcrumb rootURL='#myUrl' group='catalog' displayGroup='advancedSearch' displayParam='#advancedSearchParameters' />
    <s:set name="diname" value="%{'advancedSearch'}" />

    <div id="main">
        <s:property value="#categoryListElem.getAttribute('CallingOrganizationCode')" /> <!-- begin t2-header -->

        <!-- // t2-header Start -->
        <div class="t2-header commonHeader" id="advancedSearchHeader">
            <s:action name="header" executeResult="true" namespace="/common" />
        </div>
        <!-- // t2-header end -->

        <!--  start catalog navigation tab -->
        <div class="t2-navigate commonNavigate">
            <s:action name="catalogNavBar" executeResult="true" namespace="/catalog" />
        </div>
        <!--  end catalog navigation tab --> <swc:breadcrumbScope>

        <div class="container">
        <div class="t2-mainContent" id="t2-mainContent">

            <!-- Breadcrumb -->
            <div class="breadcrumb" id="searchBreadcrumb">
            <s:set name="Back" value='%{#_action.getText("Back")}'/>
            	<s:if test="isNewOP">
            	<swc:breadcrumbDisplay displayRootName='%{#_action.getText("Breadcrumb_Home")}' breadcrumbSeparator=' > ' removable='false' startTabIndex='0'/>
            	</s:if>
            	<s:else>
            	<swc:breadcrumbDisplay displayRootName='%{#Back}' breadcrumbSeparator=' > ' removable='false' />
            	</s:else>

            </div>
<!-- START of MAIN PAGE -PN -->
			<style type="text/css">
				.dlgForm {
					display: none; 
					border-color:black; 
					border-width:10px; 
					padding: 20px; 
					background-color:white; 
					position: absolute; 
					left: 25%; 
					top: 40%; 
					z-index: 9999; 
					width:auto; 
					height:auto;
				}			
			</style>
			<SCRIPT type="text/javascript">
				function showForm(formId){
					var dlgForm 		= document.getElementById("dlg" + formId);
					
					if (dlgForm){
						dlgForm.style.display = "block";
					}
				}
				
				function hideForm(formId){
					var dlgForm 		= document.getElementById("dlg" + formId);
					
					if (dlgForm){
						dlgForm.style.display = "none";
					}
				}
				
				function deleteItems(){
					var formItemIds 	= document.getElementById("formItemIds");
					
			        <s:url id='deleteListLink' action='myItemsDetailsDelete.action' >
						<s:param name="listKey" value="%{listKey}" />
						<s:param name="listName" value="%{listName}" />
			        </s:url>
			        
					if (formItemIds){
						formItemIds.action = "<s:property value='%{deleteListLink}' escape='false'/>";
						formItemIds.submit();
					} else {
						alert("There is a problem in this page. Form formItemIds is missing.");
					}
				}
				
				function deleteRow(id) {
					var i = document.getElementById(id).rowIndex;
					document.getElementById('itemsTable').deleteRow(i);
				}				
			</SCRIPT>
            <s:set name="theListName" value="listName"></s:set>
            
            <div class="pageHeader">
                <span class="headerText"><s:label name="Items for List 1" key="Field" value='Import items for %{listName}' /></span>
            </div>
            
            <div class="listTableContainer">
				<table id="narrowByHeader" class="listTableHeader">
                    <tbody>
	                    <tr>
	                        <td>
	                        	<span class="listTableHeaderText">Items</span>
	                        </td>
	                    </tr>
                	</tbody>
                </table>
				<table id="narrowByTable" class="listTableBody">
					<tbody>
						<tr>
							<td colspan="2" class="tableContentLeft">
<!-- START of TABLE CONTENT -->
							      <s:form id="formItemIds" action="myItemsDetailsImportCreate" method="post">
									<s:hidden name="listKey" value="%{#parameters.listKey}" />
									<s:hidden name="listName" value="%{#parameters.listName}" />
									
							      <table id="itemsTable" border="0" bordercolor="black" width="100%">
							        <tr style="font-weight: bold;">
							        	<td>Name</td>
							        	<td>Description</td>
							        	<td>Quantity</td>
							        	<td>UOM</td>
							        	<td>Job Id</td>
							        	<td>Actions</td>
							        </tr>
							      <s:iterator id="item" value='dataList'>
							        <s:set name='id' value='#item.customerPartNumber'/>
							        <s:set name='qty' value='#item.qty'/>
							        <s:set name='desc' value='#item.description'/>
							        <s:set name='uom' value='#item.UOM'/>
							        <s:set name='rowId' value="'row_' + uniqueId"/>
							        
							        <tr id="<s:property value='%{#rowId}'/>">
							        	<td>
							        		<s:textfield name="itemsName" value="%{#id}"/>
							        	</td>
							        	<td> 
							        		<s:textfield name="itemsDesc" value="%{#desc}"/> 
							        	</td>
							        	<td> 
							        		<s:textfield name="itemsQty" value="%{#qty}"/> 
							        	</td>
							        	<td>
							        		<s:textfield name="itemsUOM" value="%{#uom}"/>
							        	</td>
							        	<td>
							        		<s:textfield name="itemsJobId" value="%{' '}"/>
							        	</td>
							        	<td>
							        		 <a href="javascript: deleteRow('<s:property value='%{#rowId}'/>');">Delete Row</a>
							        	</td>
							        </tr>
							      </s:iterator>
							      </table>
							      <s:submit label="Import items"></s:submit>
								  </s:form>							
<!-- END of TABLE CONTENT -->
							</td>
						</tr>
						</tbody>
					</tbody>
				</table>
                
            </div>
<!-- END of MAIN PAGE -PN -->
    </div>
    <!-- end t2-main-content -->
    </div>
    <!-- end container -->

    <div class="t2-footer commonFooter" id="t2-footer">
        <!-- add content here for footer -->
        <s:action name="footer" executeResult="true" namespace="/common" />
    </div>
</swc:breadcrumbScope> 
    <div class='hidden bubble' id='extDescBubble'></div> 
    <div style="visibility: hidden; display: none"> 
    <div id="doesChildCategoryExist"><s:property value="childCategoryExist" /></div>
    <div id="catPath"><s:property value="categoryPath" /></div>
    <div id="selectedPath"><s:property value="selectedPath" /></div>
    <!--  <div id="priceRangeFrom"/>
    					<s:property value="%{#priceRange_From}"/>
    				</div>--></div>
    <swc:dialogPanel title="Attribute Picker" isModal="true" id="attribute_Picker">
        <div id="ajax-attributePicker"></div>
    </swc:dialogPanel>

    <s:set name="Search_Tips_Link" scope="page" value="#_action.getText('Search_Tips_Link')"/>
	<swc:dialogPanel title='${Search_Tips_Link}' isModal="true" id="search_Tips">
		<div id="Search_Tips_Help"></div>
	</swc:dialogPanel>
    </div>
</body>
</swc:html>

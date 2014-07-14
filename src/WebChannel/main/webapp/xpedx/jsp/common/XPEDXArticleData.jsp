<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>

<swc:extDateFieldComponentSetup/>
<script type="text/javascript">
    function swc_validateForm_addArticle() {
    var errors = false;
        
        form = document.getElementById("addArticle");

        
        // validator name: swcajax
        var isAjaxValidated = swc_validateForm("addArticle", false); //calling global js-function for ajax-validation defined in ajaxValidation.js
        if(!isAjaxValidated)
           errors = true;
    if(errors){
    	try{
            svg_classhandlers_decoratePage();
        }catch (err){
            alert("There is an error in Svg-apply: "+err.message);
        }
    }
	    return !errors;
    }
</script>
<div class="simpleDataContent">
    <!-- Input New Article -->
    <s:if test='#addNoteFlag == "Y"'>
        <div>
            <p>&nbsp;</p>
            <s:form action="%{#formAction}" namespace="%{#formNamespace}"  validate="true" method="POST">
            <s:hidden name='#action.name' value='xpedxAddArticle'/>
                <s:hidden name='#action.namespace' value='/profile/org'/>
                <table width="50%">
                <tr>
                    <td width="5%"/>
                    <td align="left" width="80%">
                        <s:textfield name="articleName" label="Article Name" size="50" maxlength="50"/>
                    </td>
                    <td width="15%" />
                </tr>
                <tr>
                    <td width="5%"/>
                    <td align="left" width="80%">
                    	<s:textarea key="Article" tabindex="5" name="articleBody" cols="60" rows="3"/>
                    </td>
                    <td width="15%" />
                </tr>
                <tr>
                    <td align="left" width="5%"/>
                    <td  class="tableContentRight" width="80%">
                        <table>
                            <tr>
                                <td class="textAlignLeft" colspan="1"></td>
                                <td>
                                	<s:textfield name='submittedTSFrom' label="Effective date" size="20" cssClass='ExtDateField' value="%{#parameters.submittedTSFrom}"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="textAlignLeft" colspan="1"></td>
                                <td>
                                	<s:textfield name='submittedTSTo' label="Expiry date" size="20" cssClass='ExtDateField'  value="%{#parameters.submittedTSTo}"/>
                                </td>
                            </tr>
                        </table>
                        </td>
                    <td align="right" width="15%" />
                </tr>
                <tr>
                    <td width="5%" />
                    <td width="80%" >
                        <s:submit tabindex="10" align="left" name="submit" type="button" key="Add Article" cssClass="submitBtnBg2"/>
                    </td>
                    <td width="15%" />
                </tr>
                </table>
                <s:if test='#userNoteFlag == "Y"'>
                    <s:hidden name="customerId" value="%{#customerId}"  />
                    <s:hidden name="customerContactId" value="%{#customerContactID}"  /> 
                    <s:hidden name="organizationCode" value="%{WCContext.customerMstrOrg}"  />                 
                </s:if>                                
            </s:form>
    
        </div>
        <p>&nbsp;</p>
    </s:if>

    <!-- 
    Pattern needed for formatting the date.  We need to figure out how to pass this into the table component below.
    %{#util.formatDate('ContactTime', WCContext)} 
    -->
    
    <!-- Display Note List -->
    <div class="padding-left3">
        <s:set name="iter" value="articleLines"/>
            <s:action name="buildSimpleTable" executeResult="true" namespace="/common" >
                <s:param name="id" value="articleTable"/>
                <s:param name="summary" value="'Article Table'"/>
                <s:param name="cssClass" value="'orderNotesListTable'"/>
                <s:param name="iterable" value="#iter"/>
                
                <s:param name="columnSpecs[0].label" value="'Name'"/>
                <s:param name="columnSpecs[0].dataField" value="'ArticleName'"/>
                <s:param name="columnSpecs[0].fieldCssClass" value="'textCell noteTextColumnData'"/>
                <s:param name="columnSpecs[0].labelCssClass" value="'noteTextColumnHeader'"/>
                <s:param name="columnSpecs[0].columnId" value="'noteTextHeader'"/> 
                
                <s:param name="columnSpecs[1].label" value="'Article'"/>
                <s:param name="columnSpecs[1].dataField" value="'Article'"/>
                <s:param name="columnSpecs[1].fieldCssClass" value="'nameCell noteCreatedByColumnData'"/>
                <s:param name="columnSpecs[1].labelCssClass" value="'noteCreatedByColumnHeader'"/>
                <s:param name="columnSpecs[1].columnId" value="'noteCreatedByHeader'"/>
                
                <s:param name="columnSpecs[2].label" value="'Start Date'"/>
                <s:param name="columnSpecs[2].dataField" value="'StartDate'"/>
                <s:param name="columnSpecs[2].labelCssClass" value="'noteDateColumnHeader'"/>
                <s:param name="columnSpecs[2].fieldCssClass" value="'dateCell noteDateColumnData'"/>
                <s:param name="columnSpecs[2].columnId" value="'noteDateHeader'"/>
                <s:param name="columnSpecs[2].dataCellBuilder" value="'simpleTableDate'"/>
                
                <s:param name="columnSpecs[3].label" value="'End Date'"/>
                <s:param name="columnSpecs[3].dataField" value="'EndDate'"/>
                <s:param name="columnSpecs[3].labelCssClass" value="'noteDateColumnHeader'"/>
                <s:param name="columnSpecs[3].fieldCssClass" value="'dateCell noteDateColumnData'"/>
                <s:param name="columnSpecs[3].columnId" value="'noteDateHeader'"/>
                <s:param name="columnSpecs[3].dataCellBuilder" value="'simpleTableDate'"/>
                             
               
                
          </s:action>
     </div>
     <p>&nbsp;</p>
</div>


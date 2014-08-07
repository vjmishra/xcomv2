<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>
<%--This is to setup reference to the action object so we can make calls to
    action methods explicitly in JSP's.
    This is to avoid a defect in Struts that's creating contention under load.
    The explicit call style will also help the performance in evaluating Struts'
    OGNL statements. --%>
<s:set name='_action' value='[0]'/>
	   
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='util'/> 
         
<%--
Class Model for this component:
  * This component creates a single table. The table is, by default
    assigned the class "simpleTable".
  * Users can designate additional classes to be assigned to the table.
  * The table contains on header row. The columns within this row are implemented
    with <th> elements and are assigned the "simpleTableHeader" class.
  * Users can assign an additional class for table header cells.
  * Each row of the table body is derived from exactly one row of input. Columns
    are implemented with <td> elements. Odd rows are assigned the
    "simpleTableOddRow" class and even rows are assigned the "simpleTableEvenRow"
    class.
  * Users can override the odd and even row class assignments.
  * Users can designate data cell level classes on a column by column basis.
--%>
<%--<swc:sortctl sortField="'sortField'" sortDirection="'sortDirection'"
    down="'sortDown'" up="'sortUp'" urlSpec="sortURL'">--%>
<table class="<s:property value='cssClass'/> standard-table"
       id="<s:property value='id'/>" summary="<s:property value='summary'/>">
<!--        <tr id="none" class="table-header-bar">-->
<!-- Arun Changing for carts listing page - have to customize this if there is a conflict with other ones -->
		 <tr id="top-bar">
		  <s:iterator value="columnSpecs" id="columnSpec" status="headerStatus">
             	
                <th  align="<s:if test="#headerStatus.first"></s:if><s:else>center</s:else>" class="<s:property value='#columnSpec.labelCssClass'/>"
                    id="<s:property value='#columnSpec.columnId'/>" <s:if test="#headerStatus.last">colspan="2"</s:if>>
                    <s:if test="#columnSpec.sortable">
                        <swc:sortable fieldname="#columnSpec.dataField">
                            <span class="cm-sort underlink"><s:property value="getText(#columnSpec.label)"/></span>
<%--                             <a class="cm-sort underlink" href="#"><s:property value="getText(#columnSpec.label)"/></a> --%>
                        </swc:sortable>
                    </s:if>
                    <s:else>
                       <span class="cm-sort underlink"><s:property value="getText(#columnSpec.label)"/></span>
<%--                        <a class="cm-sort underlink" href="#"><s:property value="getText(#columnSpec.label)"/></a> --%>
                    </s:else>
                </th>
            </s:iterator>
        </tr>
                            <%-- bb4 --%>
        <s:iterator value="iterable" id="row" status="rowStatus">
            <tr class=" <s:if test="#rowStatus.odd">odd </s:if>
            	<s:if test="#rowStatus.last"> last </s:if>
           	">
                <!-- Row number: <s:property value="#rowStatus.count"/> -->
                <s:iterator value="columnSpecs" id="columnSpec1" status="columnStatus">
                    <s:if test="#row instanceof org.w3c.dom.Element">
                        <s:set name="value" value="getAttributeFromNode(#row, #columnSpec1.dataField)"/>
                    </s:if>
                    <s:else>
                        <s:set name="value" value="#row[#columnSpec1.dataField]"/>
                    </s:else>
                    <%-- bb5 --%>
                    <td class="<s:property value="%{#columnSpec1.fieldCssClass}"/> <s:if test="#columnStatus.first">left-cell </s:if><s:if test="#columnStatus.last">right-cell </s:if>" >
                        <s:if test="#columnSpec1.dataCellBuilder!=null">
                            <s:if test="#columnSpec1.dataCellBuilder.endsWith('.jsp')">
                                <s:set name="nmspace" value="%{#columnSpec1.dataCellBuilderProperties['namespace']}" />
                                <s:if test='%{#nmspace == null || #nmspace == "" }' >
                                    <s:set name="nmspace" value="%{'/common'}" />
                                </s:if>
                                <s:include
                                    value="%{#nmspace+#columnSpec1.dataCellBuilder}">
                                    <s:param name="name" value="#columnSpec1.dataField"/>
                                    <s:param name="value" value="#value"/>
                                    <s:param name="rowNumber" value="#rowStatus.count"/>
                                    <s:param name="columnIndex" value="#columnStatus.count"/>
                                    <s:param name="row" value="#row"/>
                                    <s:param name="properties" value="#columnSpec1.dataCellBuilderProperties"/>
                                </s:include>
                            </s:if>
                            <s:else>
                                <s:set name="nmspace" value="%{#columnSpec1.dataCellBuilderProperties['namespace']}" />
                                
                                <s:if test='%{#nmspace == null || #nmspace == "" }' >
                                    <s:set name="nmspace" value="%{'/common'}" />
                                </s:if>
                                <s:action name="%{#columnSpec1.dataCellBuilder}"  namespace="%{#nmspace}"
                                          executeResult="true">
                                    <s:param name="name" value="#columnSpec1.dataField"/>
                                    <s:param name="value" value="#value"/>
                                    <s:param name="rowNumber" value="#rowStatus.count"/>
                                    <s:param name="columnIndex" value="#columnStatus.count"/>
                                    <s:param name="row" value="#row"/>
                                    <s:param name="properties" value="#columnSpec1.dataCellBuilderProperties"/>
                                </s:action>
                            </s:else>
                        </s:if>
                        <s:else>
                        <s:set name="date" value="%{#columnSpec1.dataCellBuilderProperties['namespace']}" />
                        	<s:if test='!#value.equals("") && !#date.equals("")'>
                                <s:property value="#util.formatDate(#value,'MM/dd/yyyy','MM/dd/yyyy')" />
                            </s:if>
                            <s:elseif test='!#value.equals("")'>
                                <s:property value="#value" />
                            </s:elseif>
                            <s:else>
                                &nbsp;
                            </s:else>
                        </s:else>
                    </td>
                </s:iterator>
            </tr>
        </s:iterator>
        <s:if test="rowCount==0">
            <tr class="<s:property value='oddLineClass'/>">
                <td colspan="<s:property value='columnCount'/>">
                    <s:text name="NoResults"/>
                </td>
            </tr>
            <tr class="whiteout">
            	<td> &nbsp; </td>
            	<td> &nbsp; </td>
            	<td> &nbsp; </td>
            	<td> &nbsp; </td>
            </tr>
        </s:if>
</table>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

                    <s:if test='#_action.isDraftOrder() || !#canAddLine'>
                        <s:url id='detailURL' namespace='/catalog' action='itemDetails.action' escapeAmp="false">
                            <s:param name='itemID'><s:property value='#catalogItem.getAttribute("ItemID")'/></s:param>
                            <s:param name='unitOfMeasure'><s:property value='#catalogItem.getAttribute("UnitOfMeasure")'/></s:param>
                            <s:param name='_r_url_' value='%{returnURL}'/>
                        </s:url>
                    </s:if>
                    <s:else>
                        <s:url id='detailURL' namespace='/catalog' action='itemDetails.action' escapeAmp="false">
                            <s:param name='itemID'><s:property value='#catalogItem.getAttribute("ItemID")'/></s:param>
                            <s:param name='unitOfMeasure'><s:property value='#catalogItem.getAttribute("UnitOfMeasure")'/></s:param>
                            <s:param name='orderHeaderKey'><s:property value='#orderHeaderKey'/></s:param>
                            <s:param name='currency'><s:property value='#currencyCode'/></s:param>
                            <s:param name='flowID'><s:property value='flowType'/></s:param>
                            <s:param name='_r_url_' value='%{returnURL}'/>
                        </s:url>
                    </s:else>

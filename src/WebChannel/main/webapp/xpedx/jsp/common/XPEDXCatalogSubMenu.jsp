<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set name='_action' value='[0]' />
<s:set name='prodCategoryDocument' value="#_action.getProdCategoryOutputDoc()" />
<s:set name='prodCategoryElement' value="#prodCategoryDocument.getDocumentElement()" />
<s:set name="xutil" value="#_action.getXMLUtils()"/>

			        <s:set name='categoryList' value="#xutil.getChildElement(#prodCategoryElement, 'CategoryList')" />
						<s:iterator value='#xutil.getChildren(#categoryList, "Category")' id='category' status="iterCount">
							<s:url id='catURL' namespace='/catalog' action='navigate.action'>
								<s:param name='path' value='#xutil.getAttribute(#category,"CategoryPath")'/>
								<s:param name='cname' value='#xutil.getAttribute(#category,"ShortDescription")'/>
								<s:param name='newOP' value='%{true}'/>
							</s:url>
							<li>
								<s:a href='%{catURL}'>
									<s:property value='#xutil.getAttribute(#category,"ShortDescription")'/>
								</s:a>
							</li>
				        </s:iterator>
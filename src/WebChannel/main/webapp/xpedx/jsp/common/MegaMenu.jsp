<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<s:set name="megaMenu" value="%{#wcUtil.getMegaMenu(wCContext)}" />

<s:url id='baseBrandsURL' namespace='/catalog' action='brands' />
<s:hidden id='baseBrandsURL' value='%{#baseBrandsURL}' />

<ul>
	<s:iterator value="megaMenu" id="cat1" status="cat1Status">
		<li>
			<a id='category-<s:property value="%{#cat1.id}"/>' style='cursor:pointer;'>
				<s:property value="#cat1.name" />
			</a>
			
			<ul>
				<li>
					<s:div cssClass="panel%{#cat1Status.count}">
						<div class="border-container">
							<h2><s:property value="#cat1.name" /></h2>
							
							<div class="cat-2-wrap">
								<s:iterator value="#cat1.subcategories" id="cat2" status="cat2Status">
									<s:if test="#cat2Status.first">
										<s:set name="cssClass" value="first" />
									</s:if>
									<s:elseif test="#cat2Status.last">
										<s:set name="cssClass" value="last" />
									</s:elseif>
									<s:else>
										<s:set name="cssClass" value="" />
									</s:else>
									
									<a id='category-<s:property value="%{#cat2.id}"/>' class='mega-cat-2 <s:property value="%{#cssClass}"/>' style='cursor:pointer;'
											data-cat1Id='<s:property value="%{#cat1.id}"/>'>
										<s:property value="#cat2.name" /><div class="cat-2-arrow">&raquo;</div>
									</a>
								</s:iterator> <%-- cat2 --%>
							</div> <%-- / cat-2-wrap --%>
						</div> <%-- / border-container --%>
						
						<div class="mainContent">
							<div id='megacat3-for-cat1-<s:property value="%{#cat1.id}"/>' class='mega-cat-3'>
								<s:set name="cat2" value="#cat1.subcategories[0]" />
									<%-- developer note: note this cat3-content tag is identical to cat3-content tag later in page --%>
									<div class='cat3-content'>
										<h3>
											<s:url id='catURL' namespace='/catalog' action='navigate.action'>
												<s:param name='path' value='#cat2.path' />
												<s:param name='cname' value='#cat2.name' />
												<s:param name='newOP' value='true' />
												<s:param name='CategoryC3' value='true' />
											</s:url>
											<a href='<s:property value="#catURL" escape="false"/>'>
												<span>
													<s:property value="#cat2.name"/>
												</span>(<s:property value="#cat2.count"/>)
											</a>
										</h3>
										<s:iterator value="#cat2.subcategories" id="cat3" status="cat3Status">
											<s:url id='catURL' namespace='/catalog' action='navigate.action'>
												<s:param name='path' value='#cat3.path' />
												<s:param name='cname' value='#cat3.name' />
												<s:param name='newOP' value='true' />
												<s:param name='CategoryC3' value='true' />
											</s:url>
											<a href='<s:property value="#catURL" escape="false"/>'>
												<span>
													<s:property value="#cat3.name"/>
												</span>(<s:property value="#cat3.count"/>)
											</a>
										</s:iterator> <%-- / cat3 --%>
										<div class='button-nav-wrap'>
											<input name='button' type='button' class='btn-nav-list' value='View All Brands for <s:property value="#cat2.name"/>  &rsaquo;'
													onclick='openBrandPage(this); return false;' data-path='<s:property value="#cat2.path"/>'
													data-cat1name="<s:property value="#cat1.name"/>" data-cat2name="<s:property value="#cat2.name"/>" />
										</div>
									</div>
							</div> <%-- / mega-cat-3 --%>
							
							<div class='far-right-photo-<s:property value="%{#cat1.id}"/>'></div>
							
						</div> <%-- / mainContent --%>
					</s:div> <%-- / panel 1/2/3/4 --%>
				</li>
			</ul>
		</li>
	</s:iterator> <%-- / cat1 --%>
</ul>

<%-- ******************************************************************************************************* --%>
<%-- ********* We provide all the cat3 panels inline (hidden) so we have content for jquery plugin ********* --%>
<%-- ******************************************************************************************************* --%>

<s:iterator value="megaMenu" id="cat1" status="cat1Status">
	<s:iterator value="#cat1.subcategories" id="cat2" status="cat2Status">
		<div id='category-<s:property value="#cat2.id"/>-content' style='display:none;'>
			<%-- developer note: note this cat3-content tag is identical to cat3-content tag earlier in page --%>
			<div class='cat3-content'>
				<h3>
					<s:url id='catURL' namespace='/catalog' action='navigate.action'>
						<s:param name='path' value='#cat2.path' />
						<s:param name='cname' value='#cat2.name' />
						<s:param name='newOP' value='true' />
						<s:param name='CategoryC3' value='true' /> <%-- TODO: existing code does not have CategoryC3 param here, but page is broken without it. is this where _bcs_ triggers results? --%>
					</s:url>
					<a href='<s:property value="#catURL" escape="false"/>'>
						<span>
							<s:property value="#cat2.name"/>
						</span>(<s:property value="#cat2.count"/>)
					</a>
				</h3>
				<s:iterator value="#cat2.subcategories" id="cat3" status="cat3Status">
					<s:url id='catURL' namespace='/catalog' action='navigate.action'>
						<s:param name='path' value='#cat3.path' />
						<s:param name='cname' value='#cat3.name' />
						<s:param name='newOP' value='true' />
						<s:param name='CategoryC3' value='true' />
					</s:url>
					<a href='<s:property value="#catURL" escape="false"/>'>
						<span>
							<s:property value="#cat3.name"/>
						</span>(<s:property value="#cat3.count"/>)
					</a>
				</s:iterator> <%-- / cat3 --%>
				<div class='button-nav-wrap'>
					<input name='button' type='button' class='btn-nav-list' value='View All Brands for <s:property value="#cat2.name"/>  &rsaquo;'
							onclick='openBrandPage(this); return false;' data-path='<s:property value="#cat2.path"/>'
							data-cat1name="<s:property value="#cat1.name"/>" data-cat2name="<s:property value="#cat2.name"/>" />
				</div>
			</div>
		</div>
	</s:iterator> <%-- / cat2 --%>
</s:iterator> <%-- / cat1 --%>

<%--
	This is included within XPEDXPrepareHeader.jsp to encapsulate the lazy-loading mega menu.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

			<li class="<s:property value='%{#selectedHeaderTab == "CatalogTab" ? "active" : ""}'/>">
				<s:a id="megamenu-top-catalog" cssStyle='cursor:pointer;' cssClass='%{#selectedHeaderTab == "CatalogTab" ? "active" : ""}'>
					${param.tabName}
				</s:a>
				
				<s:if test="#wcUtil.isMegaMenuCached(wCContext)">
					<s:include value="MegaMenu.jsp" />
				</s:if>
				<s:else>
					<%--
						Developer note: Since the mega menu data is user-specific (entitlements, etc) the data is cached in the session.
										However, the API call to fetch the data is slow enough that we don't want to block the page load,
										 so if the data is not cached then we render the page without mega menu and immediately fetch it
										 via an ajax call (see MegaMenu.js/getMegaMenu).
										The response of the ajax call is the mega menu dom, which we inject into the page.
					--%>
					<s:url id="megaMenuURL" namespace="/common" action="megaMenu" />
					<input type="hidden" id="megaMenuAjaxURL" value="<s:property value='#megaMenuURL' escape='false'/>" />
					<ul id="megaMenuLoadingMessage">
						<li>
							<a class="spinner">
								Loading...
							</a>
						</li>
					</ul>
				</s:else>
			</li>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="right-col-int">
<s:set name='associationTypeList' value='#xutil.getChildElement(#itemElem, "AssociationTypeList")' />
<s:set name='numOfItemsPerPromotion' value='numOfItemsPerPromotion' />

    <s:if test='#associationTypeList != null'>
    <s:set name='ipTabIndex' value='500'/>
	<s:if
		test='(upSellAssociatedItems != null && upSellAssociatedItems.size() > 0) || (upgradeAssociatedItems != null && upgradeAssociatedItems.size() > 0) || (alternateAssociatedItems != null && alternateAssociatedItems.size() > 0)'>
		<p class="promotxt"><s:text name="item_promotion_msg" /></p>
		<s:if test='upgradeAssociatedItems.size() > 0'>
			<s:iterator value='upgradeAssociatedItems' id='upgradeItem'
						status="upgradeCount">
				<s:set name="promoItemPrimInfoElem"
					value='#xutil.getChildElement(#upgradeItem,"PrimaryInformation")' />
				<s:set name="promoItemComputedPrice"
					value='#xutil.getChildElement(#upgradeItem,"ComputedPrice")' />
				<s:set name="itemAssetList"
					value='#xutil.getElementsByAttribute(#upgradeItem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />
					
				<div class="right-promo-cat">
				<div class="table-top-bar">
		            <div class="table-top-bar-L"></div>
		            <div class="table-top-bar-R"></div>
		        </div>
		        <div class="promo-bg-rpt">
		        <s:url id='detailURLFromPromoProd' namespace='/catalog'	action='itemDetails.action'>
					<s:param name='itemID'>
						<s:property value='#xutil.getAttribute(#upgradeItem,"ItemID")' />
					</s:param>
					<s:param name='unitOfMeasure'>
						<s:property
							value='#xutil.getAttribute(#upgradeItem,"UnitOfMeasure")' />
					</s:param>
					<!--  WebTrends tag start -- -->
					<s:param name='prItemtype'>
					<s:property value="%{'R'}" />
					</s:param>
					<!--  WebTrends tag end -- -->
				</s:url>
		        <a href="<s:property value='%{detailURLFromPromoProd}' escape="false"/>" class="short-description">
		        	<s:if test='#itemAssetList != null && #itemAssetList.size() > 0 '>
		        		<s:set name="itemAsset" value='#itemAssetList[0]' />
						<s:set name='imageLocation'
							value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
						<s:set name='imageId'
							value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
						<s:set name='imageLabel'
							value="#xutil.getAttribute(#itemAsset, 'Label')" />
						<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
						<s:if test='%{#imageURL=="/"}'>
						 <s:set name='imageURL' value='%{"/xpedx/images/INF_150x150<s:property value='#wcUtil.xpedxBuildKey' />.jpg"}' />
					   </s:if>	
					   <s:else>
						<img src="<s:url value='%{#imageURL}' includeParams='none' />" alt="<s:text name='%{#imageLabel}'/>" width="52" height="50" align="left" />
						</s:else>
		        	</s:if>
		        	<s:else>
		        		<s:set name='imageIdBlank' value='%{"/xpedx/images/INF_150x150<s:property value='#wcUtil.xpedxBuildKey' />.jpg"}' />
		        		<img src="<s:url value='%{#imageIdBlank}'/>" width="52" height="50" align="left" />
		        	</s:else>

		        	<div class="promo-txt">
						<!-- #<s:property value='#xutil.getAttribute(#upgradeItem,"ItemID")' /> -->
						<s:set name="description" value='#xutil.getAttribute(#promoItemPrimInfoElem,"ShortDescription")'></s:set>
						<s:if test='#description.trim().equals("")'>
							<s:set name="description" value='#xutil.getAttribute(#promoItemPrimInfoElem,"ExtendedDescription")' />	
						</s:if>
						<s:set name='displayDescription' value='%{#description}'/>
						<s:if test="%{(#description.length()) > 50}">
							<s:set name='displayDescription' value='%{#description.substring(0,50) + "..."}'/>
						</s:if>
						
		        		<p title='<s:property value="%{#description}" />' class="short-description">
		        			<s:property	value='%{#displayDescription}' />
						</p>		        		
						<!-- 
						<s:a href="%{detailURLFromPromoProd}" cssClass="grey-ui-btn" tabindex="%{#ipTabIndex}" theme="simple">
							<span>View Details</span>
						</s:a>
						
						<s:set name='promoItemprice'
							value='#xutil.getAttribute(#promoItemComputedPrice,"UnitPrice")' />
						<s:set name='showCurrencySymbol' value='true' />
	
						<s:text name="your_price" /> <s:property
							value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#promoItemprice,#showCurrencySymbol)' />
						-->
								
						
		        	</div>
		        	</a>
		        </div>
		        <div class="table-bottom-bar">
			         <div class="table-bottom-bar-L"></div>
			         <div class="table-bottom-bar-R"></div>
			    </div>
			    </div>
	    	</s:iterator>
	    </s:if>
			<s:if test='upSellAssociatedItems!= null && upSellAssociatedItems.size() > 0'>
			
			<!-- Web Trends tag start -->	
			<META Name="DCSext.w_x_item_upsell_p" Content="1" />
			<!-- Web Trends tag end -->
			    		    
			<s:iterator value='upSellAssociatedItems' id='upItemElem' status="promoUpSellCount">		
			<s:set name="upItemPrimInfoElem"	value='#xutil.getChildElement(#upItemElem,"PrimaryInformation")' />
			<s:set name="upItemComputedPrice" value='#xutil.getChildElement(#upItemElem,"ComputedPrice")' />
			<s:set name="itemAssetList" value='#xutil.getElementsByAttribute(#upItemElem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />
			
			<div class="right-promo-cat">
				<div class="table-top-bar">
		            <div class="table-top-bar-L"></div>
		            <div class="table-top-bar-R"></div>
		        </div>
	        <div class="promo-bg-rpt">
	        	<s:url id='detailURLFromPromoProd' namespace='/catalog'
						action='itemDetails.action'>
						<s:param name='itemID'>
							<s:property
								value='#xutil.getAttribute(#upItemElem,"ItemID")' />
						</s:param>
						<s:param name='unitOfMeasure'>
							<s:property
								value='#xutil.getAttribute(#upItemElem,"UnitOfMeasure")' />
						</s:param>
						<!--  WebTrends tag start -- -->
						<s:param name='prItemtype'>
							<s:property
								value="%{'U'}" />
						</s:param>
						<!--  WebTrends tag end -- -->
				</s:url>
	        	<a href="<s:property value='%{detailURLFromPromoProd}' escape="false"/>" class="short-description">
	        	<s:if test='#itemAssetList != null && #itemAssetList.size() > 0 '>
					<s:set name="itemAsset" value='#itemAssetList[0]' />
					<s:set name='imageLocation'
						value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
					<s:set name='imageId'
						value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
					<s:set name='imageLabel'
						value="#xutil.getAttribute(#itemAsset, 'Label')" />
					<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
					<s:if test='%{#imageURL=="/"}'>
						 <s:set name='imageURL' value='%{"/xpedx/images/INF_150x150<s:property value='#wcUtil.xpedxBuildKey' />.jpg"}' />
					</s:if>	
					<s:else>
					<img src="<s:url value='%{#imageURL}' includeParams='none' />" 	alt="<s:text name='%{#imageLabel}'/>" width="52" height="50" align="left" />
					</s:else>
				</s:if>
				<s:else>
					<s:set name='imageIdBlank' value='%{"/xpedx/images/INF_150x150<s:property value='#wcUtil.xpedxBuildKey' />.jpg"}' /> <img src="<s:url value='%{#imageIdBlank}'/>" width="52" height="50" align="left" />
				</s:else>
				<div class="clearall"></div>
				<div class="promo-txt">
					<!-- #<s:property value='#xutil.getAttribute(#upItemElem,"ItemID")' /> -->
	        			<s:set name="description" value='#xutil.getAttribute(#upItemPrimInfoElem,"ShortDescription")'></s:set>
						<s:if test='#description.trim().equals("")'>
							<s:property	value='#xutil.getAttribute(#upItemPrimInfoElem,"ExtendedDescription")' />	
						</s:if>
						<s:set name='displayDescription' value='%{#description}'/>
						<s:if test="%{(#description.length()) > 50}">
							<s:set name='displayDescription' value='%{#description.substring(0,50) + "..."}'/>
						</s:if>
		        		<p title='<s:property value="%{#description}" />'>
		        			<s:property	value='%{#displayDescription}' />
						</p>
	        		<!-- 
					<s:a href="%{detailURLFromPromoProd}" cssClass="grey-ui-btn" tabindex="%{#ipTabIndex}" theme="simple">
						<span>View Details</span>
					</s:a>
					
					<s:set name='promoItemprice'
						value='#xutil.getAttribute(#upItemComputedPrice,"UnitPrice")' />
					<s:set name='showCurrencySymbol' value='true' />

					<s:text name="your_price" /> <s:property
						value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#promoItemprice,#showCurrencySymbol)' />
					-->
				
				</div>
				</a>
			</div>
			<div class="table-bottom-bar">
		         <div class="table-bottom-bar-L"></div>
		         <div class="table-bottom-bar-R"></div>
		    </div>
		    </div>
		    </s:iterator>
		    </s:if>
		    
		     <s:if test='alternateAssociatedItems.size() > 0'>
		     
		     <!-- Web Trends tag start -->
		     <META Name="DCSext.w_x_item_alt_p" Content="1" />
		     <!-- Web Trends tag end -->
		     
		 	<s:iterator value='alternateAssociatedItems' id='alternateItem' status="alternateCount">
		 	<s:set name="promoItemPrimInfoElem"	value='#xutil.getChildElement(#alternateItem,"PrimaryInformation")' />
			<s:set name="promoItemComputedPrice" value='#xutil.getChildElement(#alternateItem,"ComputedPrice")' />
			<s:set name="itemAssetList"	value='#xutil.getElementsByAttribute(#alternateItem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />
			<div class="right-promo-cat">
				<div class="table-top-bar">
		            <div class="table-top-bar-L"></div>
		            <div class="table-top-bar-R"></div>
		        </div>
	        <div class="promo-bg-rpt">
	        <s:url id='detailURLFromPromoProd' namespace='/catalog'	action='itemDetails.action'>
				<s:param name='itemID'>
					<s:property
						value='#xutil.getAttribute(#alternateItem,"ItemID")' />
				</s:param>
				<s:param name='unitOfMeasure'>
					<s:property
						value='#xutil.getAttribute(#alternateItem,"UnitOfMeasure")' />
				</s:param>
				<!--  WebTrends tag start -- -->
				<s:param name='prItemtype'>
					<s:property
						value="%{'A'}" />
				</s:param>
				<!--  WebTrends tag end -- -->
			</s:url>
	        <a href="<s:property value='%{detailURLFromPromoProd}' escape="false"/>" class="short-description">
	        	<s:if test='#itemAssetList != null && #itemAssetList.size() > 0 '>
					<s:set name="itemAsset" value='#itemAssetList[0]' />
					<s:set name='imageLocation'
						value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
					<s:set name='imageId'
						value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
					<s:set name='imageLabel'
						value="#xutil.getAttribute(#itemAsset, 'Label')" />
					<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
					<s:if test='%{#imageURL=="/"}'>
								<s:set name='imageURL' value='%{"/xpedx/images/INF_150x150<s:property value='#wcUtil.xpedxBuildKey' />.jpg"}' />
							</s:if>	
					<s:else>
					     <img src="<s:url value='%{#imageURL}' includeParams='none' />"	alt="<s:text name='%{#imageLabel}'/>" width="52" height="50" align="left" />
					</s:else>
				</s:if>
				<s:else>
					<s:set name='imageIdBlank' value='%{"/xpedx/images/INF_150x150<s:property value='#wcUtil.xpedxBuildKey' />.jpg"}' />
					<img src="<s:url value='%{#imageIdBlank}'/>"
						width="52" height="50" align="left" />
				</s:else>
				<div class="promo-txt">
					<!-- #<s:property value='#xutil.getAttribute(#alternateItem,"ItemID")' /> -->
						<s:set name="description" value='#xutil.getAttribute(#promoItemPrimInfoElem,"ShortDescription")'></s:set>
						<s:if test='#description.trim().equals("")'>
							<s:property	value='#xutil.getAttribute(#promoItemPrimInfoElem,"ExtendedDescription")' />	
						</s:if>
						<s:set name='displayDescription' value='%{#description}'/>
						<s:if test="%{(#description.length()) > 50}">
							<s:set name='displayDescription' value='%{#description.substring(0,50) + "..."}'/>
						</s:if>
		        		<p title='<s:property value="%{#description}" />'>
		        			<s:property	value='%{#displayDescription}' />
						</p>
					<!-- 
					<s:set name='ipTabIndex' value='%{#ipTabIndex+1}' />
					<s:a href="%{detailURLFromPromoProd}" cssClass="grey-ui-btn" tabindex="%{#ipTabIndex}" theme="simple">
						<span>View Details</span>
					</s:a>
					
					
					
					<tr>

						<s:set name='promoItemprice'
							value='#xutil.getAttribute(#promoItemComputedPrice,"UnitPrice")' />
						<s:set name='showCurrencySymbol' value='true' />

						<td class="itemOfferPrice"><s:text name="your_price" /> <s:property
							value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#promoItemprice,#showCurrencySymbol)' />

						</td>
					</tr>
					
					 -->
					 </div>
					 </a>
				</div>
				<div class="table-bottom-bar">
			         <div class="table-bottom-bar-L"></div>
			         <div class="table-bottom-bar-R"></div>
			    </div>
		   	</div>
		   	</s:iterator>
		</s:if>
	</s:if>
	
	<s:if
			test='(crossSellAssociatedItems != null && crossSellAssociatedItems.size() > 0) || (complimentAssociatedItems != null && complimentAssociatedItems.size > 0)'>        
		 <p class="promotxt"><s:text name="cross_Sell" /></p>
		
		<s:if test='complimentAssociatedItems.size() > 0'>
			<s:iterator value='complimentAssociatedItems' id='complementItem'
				status="complementCount">
				<s:set name="promoItemPrimInfoElem"	value='#xutil.getChildElement(#complementItem,"PrimaryInformation")' />
				<s:set name="promoItemComputedPrice" value='#xutil.getChildElement(#complementItem,"ComputedPrice")' />
				<s:set name="itemAssetList"	value='#xutil.getElementsByAttribute(#complementItem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />
				<div class="right-promo-cat">
					<div class="table-top-bar">
			            <div class="table-top-bar-L"></div>
			            <div class="table-top-bar-R"></div>
			        </div>
		        	<div class="promo-bg-rpt">
		        	<s:url id='detailURLFromPromoProd' namespace='/catalog'	action='itemDetails.action'>
						<s:param name='itemID'>
							<s:property
								value='#xutil.getAttribute(#complementItem,"ItemID")' />
						</s:param>
						<s:param name='unitOfMeasure'>
							<s:property
								value='#xutil.getAttribute(#complementItem,"UnitOfMeasure")' />
						</s:param>
						<!--  WebTrends tag start -- -->
						<s:param name='prItemtype'>
							<s:property value="%{'C'}" />
						</s:param>
						<!--  WebTrends tag end -- -->
					</s:url>
	        		<a href="<s:property value='%{detailURLFromPromoProd}' escape="false"/>" class="short-description">
		        		<s:if test='#itemAssetList != null && #itemAssetList.size() > 0 '>
							<s:set name="itemAsset" value='#itemAssetList[0]' />
							<s:set name='imageLocation'
								value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
							<s:set name='imageId'
								value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
							<s:set name='imageLabel'
								value="#xutil.getAttribute(#itemAsset, 'Label')" />
							<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
							<img src="<s:url value='%{#imageURL}' includeParams='none' />"
								alt="<s:text name='%{#imageLabel}'/>" width="52" height="50" align="left" />
						</s:if>
						<s:else>
							<s:set name='imageIdBlank' value='%{"/xpedx/images/INF_150x150<s:property value='#wcUtil.xpedxBuildKey' />.jpg"}' />
							<img src="<s:url value='%{#imageIdBlank}'/>"
								width="52" height="50" align="left" />
						</s:else>

						<div class="promo-txt">
							<!--  #<s:property value='#xutil.getAttribute(#complementItem,"ItemID")' /> -->
							<s:set name="description" value='#xutil.getAttribute(#promoItemPrimInfoElem,"ShortDescription")'></s:set>
							<s:if test='#description.trim().equals("")'>
								<s:property	value='#xutil.getAttribute(#promoItemPrimInfoElem,"ExtendedDescription")' />	
							</s:if>
							<s:set name='displayDescription' value='%{#description}'/>
							<s:if test="%{(#description.length()) > 50}">
								<s:set name='displayDescription' value='%{#description.substring(0,50) + "..."}'/>
							</s:if>
			        		<p title='<s:property value="%{#description}" />' class="short-description">
			        			<s:property	value='%{#displayDescription}' />
							</p>		        			
							<s:set name='ipTabIndex' value='%{#ipTabIndex+1}' />
								<!-- 
							<s:a href="%{detailURLFromPromoProd}" cssClass="grey-ui-btn" tabindex="%{#ipTabIndex}" theme="simple">
								<span>View Details</span>
							</s:a>
							
						
					
							<tr>
		
								<s:set name='promoItemprice'
									value='#xutil.getAttribute(#promoItemComputedPrice,"UnitPrice")' />
								<s:set name='showCurrencySymbol' value='true' />
		
								<td class="itemOfferPrice"><s:text name="your_price" /> <s:property
									value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#promoItemprice,#showCurrencySymbol)' />
		
								</td>
							</tr>
							
							 -->
							 
							 </div>
	 						</a>
						</div>
						<div class="table-bottom-bar">
					         <div class="table-bottom-bar-L"></div>
					         <div class="table-bottom-bar-R"></div>
					    </div>
				   	</div>
				</s:iterator>
			</s:if>
				<s:if test='crossSellAssociatedItems != null && crossSellAssociatedItems.size() > 0'>
				
				<!-- Web Trends tag start -->
				<META Name="DCSext.w_x_item_crosssell_p" Content="1" />
				<!-- Web Trends tag end -->
				 
					<s:iterator value='crossSellAssociatedItems' id='crossItemElem'	status="promoCrossSellCount">
						<!--<s:set name="promoItemElem"	value='#xutil.getChildElement(#association,"Item")' />
						-->
						<s:set name="crossItemPrimInfoElem"	value='#xutil.getChildElement(#crossItemElem,"PrimaryInformation")' />
						<s:set name="crossItemComputedPrice" value='#xutil.getChildElement(#crossItemElem,"ComputedPrice")' />
						<s:set name="itemAssetList" value='#xutil.getElementsByAttribute(#crossItemElem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />
							<div class="right-promo-cat">
								<div class="table-top-bar">
					            <div class="table-top-bar-L"></div>
						            <div class="table-top-bar-R"></div>
						        </div>
					        	<div class="promo-bg-rpt">
					        	<s:url id='detailURLFromPromoProd' namespace='/catalog'	action='itemDetails.action'>
									<s:param name='itemID'>
										<s:property
											value='#xutil.getAttribute(#crossItemElem,"ItemID")' />
									</s:param>
									<s:param name='unitOfMeasure'>
										<s:property
											value='#xutil.getAttribute(#crossItemElem,"UnitOfMeasure")' />
									</s:param>
									<!--  WebTrends tag start -- -->
									<s:param name='prItemtype'>
										<s:property value="%{'Cr'}" />
									</s:param>
									<!--  WebTrends tag end -- -->
								</s:url>
								<a href="<s:property value='%{detailURLFromPromoProd}' escape="false"/>" class="short-description">
					        		<s:if test='#itemAssetList != null && #itemAssetList.size() > 0 '>
										<s:set name="itemAsset" value='#itemAssetList[0]' />
										<s:set name='imageLocation'
											value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
										<s:set name='imageId'
											value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
										<s:set name='imageLabel'
											value="#xutil.getAttribute(#itemAsset, 'Label')" />
										<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
										<img src="<s:url value='%{#imageURL}' includeParams='none' />"
											alt="<s:text name='%{#imageLabel}'/>" width="52" height="50" align="left" />
									</s:if>
									<s:else>
										<s:set name='imageIdBlank' value='%{"/xpedx/images/INF_150x150<s:property value='#wcUtil.xpedxBuildKey' />.jpg"}' />
										<img src="<s:url value='%{#imageIdBlank}'/>"
											width="52" height="50" align="left" />
									</s:else>
									<div class="promo-txt">
										<!-- #<s:property value='#xutil.getAttribute(#crossItemElem,"ItemID")' />   -->
										<s:set name="description" value='#xutil.getAttribute(#crossItemPrimInfoElem,"ShortDescription")'></s:set>
										<s:if test='#description.trim().equals("")'>
											<s:property	value='#xutil.getAttribute(#crossItemPrimInfoElem,"ExtendedDescription")' />	
										</s:if>
										<s:set name='displayDescription' value='%{#description}'/>
										<s:if test="%{(#description.length()) > 50}">
											<s:set name='displayDescription' value='%{#description.substring(0,50) + "..."}'/>
										</s:if>
						        		<p title='<s:property value="%{#description}" />'>
			        						<s:property	value='%{#displayDescription}' />
										</p>
										
										<!-- 
										<s:set name='ipTabIndex' value='%{#ipTabIndex+1}' />
										<s:a href="%{detailURLFromPromoProd}" cssClass="grey-ui-btn" tabindex="%{#ipTabIndex}" theme="simple">
											<span>View Details</span>
										</s:a>
										
										
								
										<tr>
					
											<s:set name='promoItemprice'
												value='#xutil.getAttribute(#crossItemComputedPrice,"UnitPrice")' />
											<s:set name='showCurrencySymbol' value='true' />
					
											<td class="itemOfferPrice"><s:text name="your_price" /> <s:property
												value='#util.formatPriceWithCurrencySymbol(#scuicontext,#currency,#promoItemprice,#showCurrencySymbol)' />
					
											</td>
										</tr>
										
										 -->
										 
										 </div>
										 </a>
									</div>
									<div class="table-bottom-bar">
								         <div class="table-bottom-bar-L"></div>
								         <div class="table-bottom-bar-R"></div>
								    </div>
							</div>
						</s:iterator>
						</s:if>
	 			</s:if>
 			</s:if>
		</div> 
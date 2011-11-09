<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="st" uri="/struts-tags"%>
<st:set name="selectedTab" value="selectedTab"/>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that�s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
<st:set name='_action' value='[0]'/>
<st:set name="loggedInUser" value="%{#_action.getWCContext().getLoggedInUserId()}"/>
<st:bean name='com.sterlingcommerce.framework.utils.SCXmlUtils' id='util' />
<div class="tab-1">
<ul>

	<st:if test='#selectedTab=="GeneralInfo"'>
		<li class="selected"><a><st:text name="GeneralInformation"/></a></li>
	</st:if>
	<st:else>
		<li class="selectable"><a tabindex="951" href="<st:url value='/profile/user/getUserInfo.action'>
			 <st:param name="customerId" value="%{customerId}" />
 			 <st:param name="customerContactId" value="%{customerContactId}"  />
			</st:url>"> <st:text name="GeneralInformation"/>
			</a>
		</li>
	</st:else>

	<st:if test='#selectedTab=="PaymentInfo"'>
		<li class="selected"><a><st:text name="PaymentInformation"/></a></li>
	</st:if>
	<st:else>
		<li class="selectable"><a tabindex="952" href="<st:url value='/profile/user/getPaymentInfo.action'>
			 <st:param name="customerId" value="%{customerId}" />
 			 <st:param name="customerContactId" value="%{customerContactId}"  />
			</st:url>"> <st:text name="PaymentInformation"/></a></li>
	</st:else>

	<st:set name='userPrefRule' value='%{#_action.getUserPrefAllwdRuleValue()}'/>
	<st:if test='%{#userPrefRule == "Y"}'>
	<st:if test='#selectedTab=="Preferences"'>
		<li class="selected"><a><st:text name="Preferences"/></a></li>
	</st:if>
	<st:else>
		<li class="selectable"><a tabindex="953" href="<st:url value='/profile/user/getUserPreferences.action'>
			 <st:param name="customerId" value="%{customerId}" />
 			 <st:param name="customerContactId" value="%{customerContactId}"  />
			</st:url>"> <st:text name="Preferences"/>
		</a></li>
	</st:else>
	</st:if>

	<st:if test='#selectedTab=="Notes"'>
		<li class="selected"><a><st:text name="Notes"/></a></li>
	</st:if>
	<st:else>
		<li class="selectable"><a tabindex="954" href="<st:url value='/profile/user/getUserNotes.action'>
			 <st:param name="customerId" value="%{customerId}" />
 			 <st:param name="customerContactId" value="%{customerContactId}"  />
			</st:url>"> <st:text name="Notes"/> </a></li>
	</st:else>
	
	<st:set name='loggedInUserInfo' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUserInfo(#loggedInUser)' />
	<st:set name='UserGroupLists' value="#util.getXpathElement(#loggedInUserInfo, '//CustomerContactList/CustomerContact/User/UserGroupLists')"/>
												
		<st:iterator value='#util.getElements(#UserGroupLists, "UserGroupList")' id='userGroupList'>
			<st:set name='userGroupKey' value='#util.getAttribute(#userGroupList,"UsergroupKey")'/>
			<st:if test='#userGroupKey=="BUYER-ADMIN"'>
				<st:set name='isUserAdmin' value="%{'true'}"/>
			</st:if>   							
		</st:iterator>
	
	<st:if test="#isUserAdmin=='true'">
		<st:if test='#selectedTab=="CustomerAssignment"'>
			<li class="selected"><a><st:text name="CustomerAssignment" /></a></li>
		</st:if>
		<st:else>
			<li class="selectable"><a tabindex="954"
				href="<st:url value='/profile/org/CustomerAssignment.action'>
				<st:param name='customerId' value='%{customerId}'/>
	 			<st:param name="customerContactId" value="%{customerContactId}"  />
				</st:url>">
				<st:text name="CustomerAssignment" /> </a></li>
		</st:else>
	</st:if>	
</ul>
</div>




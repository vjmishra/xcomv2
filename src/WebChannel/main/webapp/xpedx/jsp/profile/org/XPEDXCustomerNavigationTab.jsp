<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="st" uri="/struts-tags"%>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that�s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
<st:set name='_action' value='[0]'/>
<st:set name="selectedTab" value="#_action.getSelectedTab()"/>

<div class="tab-1">
<ul>
	<st:if test='#selectedTab=="GeneralInfo"'>
		<li class="selected"><a><st:text name="GeneralInformation"/></a></li>
	</st:if>
	<st:else>
		<li class="selectable"><a tabindex="951" href="<st:url value='/profile/org/getCustomerGeneralInfo.action'>
			<st:param name='customerId' value='%{customerId}'/>
			<st:param name='organizationCode' value='%{organizationCode}'/>
			</st:url>"> <st:text name="GeneralInformation"/>
			</a>
		</li>
	</st:else>

	<st:if test='#selectedTab=="CorporateInfo"'>
		<li class="selected"><a><st:text name="CorporateInformation"/></a></li>
	</st:if>
	<st:else>
		<li class="selectable"><a tabindex="952" href="<st:url value='/profile/org/getCustomerCorporateInfo.action'>
			<st:param name='customerId' value='%{customerId}'/>
			<st:param name='organizationCode' value='%{organizationCode}'/>
			</st:url>"> <st:text name="CorporateInformation"/>
			</a>
		</li>
	</st:else>

	<st:if test='#selectedTab=="PaymentInfo"'>
		<li class="selected"><a><st:text name="PaymentInformation"/></a></li>
	</st:if>
	<st:else>
		<li class="selectable"><a tabindex="953" href="<st:url value='/profile/org/getCustomerPaymentInfo.action'>
			<st:param name='customerId' value='%{customerId}'/>
			<st:param name='organizationCode' value='%{organizationCode}'/>
			</st:url>"><st:text name="PaymentInformation"/>
			</a>
		</li>
	</st:else>


	<st:if test='#selectedTab=="Notes"'>
		<li class="selected"><a><st:text name="Notes"/></a></li>
	</st:if>
	<st:else>
		<li class="selectable"><a tabindex="954" href="<st:url value='/profile/org/getCustomerNotes.action'>
			<st:param name='customerId' value='%{customerId}'/>
			<st:param name='organizationCode' value='%{organizationCode}'/>
			</st:url>"><st:text name="Notes"/>
			</a>
		</li>
	</st:else>
	
	<st:if test='#selectedTab=="PublishArticle"'>
		<li class="selected"><a><st:text name="Publish Article"/></a></li>
	</st:if>
	<st:else>
		<li class="selectable"><a tabindex="954" href="<st:url value='/profile/org/xpedxPublishArticle.action'>
			 <st:param name="customerId" value="%{customerId}" />
 			 <st:param name="customerContactId" value="%{customerContactId}"  />
			</st:url>"> <st:text name="Publish Article"/> </a></li>
	</st:else>	
</ul>
</div>




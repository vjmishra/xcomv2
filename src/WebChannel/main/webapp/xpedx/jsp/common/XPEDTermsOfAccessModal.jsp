<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/swc.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/theme-xpedx_v1.2.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" />.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/xpedx-mil-new.css" />
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/xpedx-quick-add.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/prod-details.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/SpryTabbedPanels.css" />

<!-- jQuery -->
<script type="text/javascript" src="/swc/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
<link rel="stylesheet" type="text/css" href="../js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

<script type="text/javascript" src="/swc/xpedx/js/swc.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	$(document).pngFix();
	$("#video-tour").fancybox({
		'titleShow'			: false,
    	'transitionIn'		: 'fade',
		'transitionOut'		: 'fade'
	});
	$("#video-tour2").fancybox({
		'titleShow'			: false,
		'transitionIn'		: 'fade',
		'transitionOut'		: 'fade'
	});
});
</script>

<%-- <title><s:property value="wCContext.storefrontId" /> /  Terms of Access</title> --%>
<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.MISC.TERMS.GENERIC.TABTITLE"/> </title>
	
</head>
    <s:set name='_action' value='[0]' />
    
    <s:form name="toaform" namespace="/common" action="XPEDXRegisterUserTOA" onsubmit="return false;">
    <s:hidden name="toaChecked" id="toaChecked" value="Y" />
    <body class="ext-gecko ext-gecko3">
		<div id="main-container">
			<div id="main">
			<div class="container">
				<!-- breadcrumb -->
				<div id="mid-col-mil">
					<div>
						<div class="padding-top3 black bold">
							<s:text name="MSG.SWC.MISC.TERMS.GENERIC.PGTITLE"/>
						</div>
					</div>
					<%-- <table class="full-widths">
						<tbody>
							<tr>
								<td colspan="2" class="no-border-right-user"></td>
							</tr>
							<tr>
								<td colspan="2" valign="top"
									class=" no-border-right-user access"><div class="bold">REVIEW
										THESE TERMS OF ACCESS. </div><br /> All access to and use of this
									site, the information and applications provided herein is
									subject to these terms. Entering into or accessing this site
									indicates that you have reviewed these terms and agree to be
									bound by them. Access to this site is limited to the provision
									and sale of products and services to our customers within the
									United States of America. Any access or attempt to access any
									other system or information is strictly prohibited. The use of
									this site and the information provided herein is limited to a
									legitimate business purpose.<br /> <br /> <div class="bold">Copyright</div><br />
									All content included on this site, such as text, graphics,
									logos, button icons, images, audio clips and software, is the
									property of International Paper Company or its content
									suppliers and protected by U.S. and international copyright
									laws. Similarly, the compilation of all content on this site is
									the exclusive property of International Paper Company and its
									divisions (collectively, &quot;International Paper&quot;) and
									protected by such laws. All software used on this site is the
									property of International Paper or its software supplier and
									protected by the same laws.<br /> <br /> <div class="bold">How
										You May Use Our Materials</div><br /> You may download, use, and
									copy the materials found on the International Paper web site
									for your personal, noncommercial use only, provided that all
									copies that you make of the materials must bear any copyright,
									trademark or other proprietary notice located on the site which
									pertain to the material being copied. Except as authorized in
									this paragraph, you are not being granted a license under any
									copyright, trademark, patent, or other intellectual property
									right in the material or the products, services, processes or
									technology described therein. All such rights are retained by
									International Paper, its subsidiaries, and/or third party owner
									of such rights. You may not create framed or deep links to
									International Paper's web site without express written
									permission from International Paper. You agree that any copy of
									any page or screen print will include the copyright notice. You
									do not have permission to further reproduce, modify,
									distribute, transmit, republish, display or perform the content
									on this site.<br /> <br /> <div class="bold">Trademarks</div><br />
									This site contains many trade and service marks of
									International Paper and our suppliers. All marks are the
									property of their respective owners and all rights in the
									intellectual property contained in this site are reserved.<br />
									<br /> <div class="bold">How You May Use Our Marks</div><br /> You
									are not authorized to use any International Paper or supplier's
									name or mark in any advertising, publicity or in any other
									commercial manner without the prior written consent of
									International Paper. Requests for authorization should be made
									to International Paper's Trademark Manager. The trademarks
									included in this site may not be used in connection with any
									product or service that is not International Paper's or the
									trademark owners', in any manner that is likely to cause
									confusion among customers, or in any manner that disparages or
									discredits International Paper or the trademark owner.<br /> <br />
									<div class="bold">Disclaimer</div><br /> THIS SITE IS PROVIDED BY
									INTERNATIONAL PAPER COMPANY ON AN &quot;AS IS&quot; BASIS.
									INTERNATIONAL PAPER COMPANY MAKES NO REPRESENTATIONS OR
									WARRANTIES OF ANY KIND, EXPRESS OR IMPLIED, AS TO THE OPERATION
									OF THIS SITE, THE INFORMATION, CONTENT, MATERIALS OR PRODUCTS
									INCLUDED ON THIS SITE. TO THE FULL EXTENT PERMISSIBLE BY
									APPLICABLE LAW, INTERNATIONAL PAPER COMPANY DISCLAIMS ALL
									WARRANTIES OF MERCHANTABILITY, NONINFRINGEMENT AND FITNESS FOR
									A PARTICULAR PURPOSE. IN ADDITION, INTERNATIONAL PAPER COMPANY
									DOES NOT REPRESENT OR WARRANT THAT THE INFORMATION ACCESSIBLE
									VIA THIS SITE IS ACCURATE, COMPLETE OR CURRENT OR THAT THE
									FUNCTIONS PROVIDED WILL BE UNINTERRUPTED OR FREE FROM ERRORS OR
									VIRUSES. INTERNATIONAL PAPER COMPANY WILL NOT BE LIABLE FOR ANY
									DAMAGES OF ANY KIND ARISING FROM THE USE OF THIS SITE,
									INCLUDING BUT NOT LIMITED TO DIRECT, INDIRECT, INCIDENTAL,
									SPECIAL, PUNITIVE AND CONSEQUENTIAL DAMAGES.<br /> <br /> <div class="bold">Links
										to Other Sites</div><br /> International Paper Company has not
									reviewed all of the sites linked to this web site.
									International Paper Company is not responsible for any off-site
									pages or any other sites linked to this web site. The inclusion
									of any link to such a site does not imply endorsement by
									International Paper Company of the site and is provided merely
									as a convenience to our customers. Your linking to any other
									off-site pages or other sites is at your own risk.<br /> <br />
									<div class="bold">Disclosure and Use of Your Communications</div><br />
									Postings to this site and electronic mail delivered to
									International Paper Company are not confidential and
									International Paper Company shall not be liable for any use or
									disclosure thereof. Do not send us any confidential or
									proprietary information. Any communications, information, data
									or other materials (including, without limitation, unsolicited
									ideas, suggestions or materials) you send to this site or to
									International Paper Company by any means are and shall remain,
									the sole and exclusive property of International Paper Company
									and may be used by International Paper Company for any purpose
									whatsoever, commercial or otherwise, without compensation or
									restriction. Visitors to this site are not required to
									register. However, when requesting swatch books, promotional
									items, entry forms, placing orders, or requesting other
									information from International Paper Company, through our
									online forms, we require that you provide your first and last
									names, company, title, mailing address, phone number and e-mail
									address, as well as other information, including but not
									limited to detailed credit and financial information, in order
									to process your request. The information you provide will be
									added to our customer database. We rely on this database to
									facilitate our service to you. We do not sell, trade or rent
									your personal information to third parties. You may view your
									personal information at any time, request that it be modified,
									obtain printed copies of these Terms of Access, as well as the
									Terms of Sale, obtain printed copies of purchase requests or
									request that your name be removed by calling (859)-655-2000.
									You will also be charged $ .25 per page for any copies. You can
									also print copies of the Terms of Access and Terms of Sale by
									clicking on the &quot;Print&quot; function. We may update this
									policy from time to time so please review it periodically.<br />
									<br /> <div class="bold">Hardware and Software Requirements</div><br />
									This site may be accessed through any personal computer that
									runs Internet Explorer Release 5.0 or higher.<br /> <br /> <div class="bold">Cross
										Reference Information</div><br /> Product cross-references do not
									imply that all products are available, or have functional
									equivalency or that performance and other characteristics are
									perfectly comparable. Review all specifications prior to
									purchase.<br /> <br /> <div class="bold">Security</div><br />
									International Paper recognizes the importance or protecting
									information transmitted via the Site and will take reasonable
									security precautions to protect such information; however,
									International Paper cannot guarantee that any electronic
									commerce or exchange of information is totally secure.
									International Paper will take reasonable measures in the site
									to secure and protect all customer-specific information from
									loss, misuse, and alteration; however, the user takes all
									responsibility for, and we hold International Paper harmless
									for, administering and maintaining the security and
									confidentiality of user IDs and passwords. International Paper
									shall not be responsible for any consequential or incidental
									damages, loss, misuse or alteration of customer information.<br />
									<br /> <div class="bold">Terms of Sale</div><br /> The Terms of Sale
									contain International Paper's conditions of sale. Placement of
									an order shall be deemed acceptance of the Terms of Sale.<br />
									<br /> <div class="bold">Projections and Forecasts</div><br /> The
									content of the International Paper web site may contain
									projections and other forward-looking statements regarding
									future events or the future financial performance International
									Paper Company. Such statements are only predictions and actual
									events or results may differ materially. Please see
									International Paper Company's Form 10-K for actual information.<br />
									<br /> <div class="bold">Foreign Corrupt Practices Act</div><br />
									The user acknowledges that it is not the agent of International
									Paper or International Paper Company and represents and
									warrants that all purchases are for use within the United
									States of America and that purchaser has not and covenants that
									it will not pay anything of value to any government employee in
									connection with the resale of the products.<br /> <br /> <div class="bold">Governing
										Law; Limitations</div><br /> These Terms and Conditions shall be
									construed, interpreted and performed exclusively according to
									the, excluding conflict of law rules, of the State of New York,
									United States of America. Any legal action with respect to any
									transaction must be commenced within one year after the cause
									of action has arisen, unless otherwise specified in these Terms
									of Access or Terms of Sale, the provisions of the Uniform
									Commercial Code as adopted by the State of New York shall
									apply. <br /> <br />
								<br />
								</td>
							</tr>

							<tr>
								<td colspan="2" valign="top" style="text-align:center"
									class=" no-border-right-user access"><div class="bold"><span
										class="access">TERMS OF SALE - (E-COMMERCE SITE) </span>
								</div>
								</td>
							</tr>
							<tr>
								<td width="50%" valign="top" class=" no-border-right-user "><div class="access padding-right3">
										1. <span class="bold">BUSINESS CUSTOMERS ONLY</span> - Buyer hereby
										warrants that it is engaged in a legitimate business activity
										and acknowledges that International Paper sells only to
										businesses in the United States. <br /> <br /> 2. <span class="bold">ACCEPTANCE
											OF ORDERS </span>- Any acceptance herein of Buyer's Order by
										International Paper is expressly made conditional on Buyer's
										assent to the terms contained herein. No additional or
										different terms or conditions shall be inferred or implied
										without the express written consent of International Paper.<br />
										<br /> 3. <span class="bold">PRICE</span> - The price for the material
										covered hereby shall be adjusted to International Paper's
										published price or contract pricing in effect at time of
										shipment. <br /> <br /> 4. <span class="bold">DELIVERY</span> -
										F.O.B. points, freight allowances and other transportation
										arrangements applicable to this sale shall be as set forth in
										International Paper's published sales policy statements.
										Contact your local branch for further information.
										International Paper will not consider claims for errors,
										damages or shortages in shipments unless transmitted to
										International Paper within thirty (30) days of the date of
										shipment and accompanied by documents sufficient to
										substantiate the claim. <br /> <br /> 5. <span class="bold">TERMS
											OF PAYMENT</span> - Only if Buyer has previously made credit
										arrangements shall the terms be in accordance with the terms
										provided by the delivering International Paper branch.
										Otherwise, terms of payment shall be cash in advance. <br />
										<br /> 6. <span class="bold">SECURITY INTEREST</span> - On any sales
										on open account, Buyer hereby grants to International Paper a
										priority lien, purchase money security interest and/or chattel
										mortgage in the products and any accounts receivable or cash
										from resale thereof until full payment is made to
										International Paper. Buyer agrees to cooperate in filing any
										financing statements or other appropriate document to assure
										the validity, priority, and enforceability of the lien. Buyer
										agrees to inform International Paper immediately if it intends
										to use any financing or has or will be granting a lien or
										security interest on its inventory to any third party.<br />
										<br /> 7. <span class="bold">CANCELLATION</span> - Orders cannot be
										canceled except with International Paper's consent and upon
										terms that will indemnify International Paper against loss and
										Buyer may be subject to restocking or other charges. <br /> <br />
										8. <span class="bold">WARRANTY</span> - International Paper warrants to
										Buyer that the material to be delivered hereunder will be free
										from defects in material, workmanship and title and will be of
										the kind and quality described in the product description. THE
										FOREGOING WARRANTY IS EXCLUSIVE AND IN LIEU OF ALL OTHER
										WARRANTIES, WHETHER WRITTEN, ORAL OR IMPLIED INCLUDING ANY
										WARRANTY OF MERCHANTABILITY OR FITNESS FOR PURPOSE. Claims for
										defects in materials will not be considered unless made in
										writing within ninety (90) days of receipt of material, and
										unless Buyer promptly discontinues use of such material.
									</div>
								</td>

								<td valign="top" class=" no-border-right-user access">8. -
									CONT'D - Material proving defective in the hands of Buyer, when
									used for purposes for which such material is intended, will be
									replaced or credit will be allowed for the price thereof at
									International Paper's option. International Paper makes no
									Consumer warranties as defined in the Magnuson-Moss Warranty -
									Federal Trade Commission Improvement Act.<br /> <br /> 9. <span class="bold">LIMITATION
										OF LIABILITY</span> - International Paper's liability on any claim of
									any kind, whether based on negligence, warranty or otherwise,
									for any loss or damage arising out of, connected with, or
									resulting from this contract, or from the performance or breach
									thereof, or from the manufacture, sale, delivery, resale or use
									of any material or provision of any services covered by or
									furnished pursuant to this order shall in no case exceed the
									price allocatable to the material which gives rise to the
									claim. IN NO EVENT SHALL International Paper BE LIABLE FOR
									SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES. <br /> <br />
									10. <span class="bold">EXCUSABLE DELAYS</span> - International Paper
									shall not be liable for delay or failure to perform due to
									causes beyond International Paper's control such as, but not
									limited to, the unavailability of goods, law or governmental
									regulation, judgment of a competent court, labor difficulties,
									accidents, transportation delays, Acts of God, war or civil
									commotion. <br /> <br /> 11. <span class="bold">COMPLIANCE WITH
										LAWS</span> - International Paper hereby certifies that the products
									sold hereunder shall comply with the provisions of the Fair
									Labor Standards Act of 1938, as amended, and Executive Order
									No. 11246, Section 202 of the Equal Employment Opportunity Laws
									and Regulations and, where applicable, ASPR No. 12-802, the
									applicable provisions of the Occupational Safety and Health Act
									of 1970 and all other applicable state, Federal and local laws,
									regulations, rules and ordinances.<br /> <br /> 12. <span class="bold">TAXES</span>
									- Any tax, duty, or other governmental charge now or hereafter
									levied upon the production, sale, use or shipment of materials
									ordered or sold hereunder shall be for the Buyer's account.
									Such governmental levies are not covered in the International
									Paper's price unless expressly so stated. If the Buyer is tax
									exempt, a tax certificate must be on file for each jurisdiction
									affected.<br /> <br /> 13. <span class="bold">NON-WAIVER</span> -
									International Paper's waiver of any of these Terms of Sale in
									any instance shall be limited to that instance and shall not
									imply that International Paper will waive such Term of Sale on
									any future occasion.<br /> <br /> 14. <span class="bold">COMPLETE
										AGREEMENT</span>. The terms and conditions in International Paper's
									site Terms of Access, acknowledgments, quotations and invoices
									are incorporated herein by reference and with these terms of
									sale constitute the entire and exclusive agreement between
									Buyer and International Paper and its divisions.</td>
							</tr>
							<tr>
								<td colspan="2" valign="top" class=" no-border-right-user">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="2" valign="top" class="no-border-right-user">
									<ul class="float-right ">
										<li class="float-right margin-10"><a href="javascript:toaSubmit('Y')"
											class="grey-ui-btn"><span>Accept</span>
										</a>
										</li>
										<li class="float-right margin-10 checkaddavail"><a href="<s:url action='logout' namespace='/home' includeParams='none'/>"
											class="grey-ui-btn"><span>Cancel</span>
										</a>
										</li>
										<li class="float-right margin-10"><a href="<s:url action="logout" namespace="/home" includeParams='none'/>"
											class="grey-ui-btn"><span>Do Not Accept</span>
										</a>
										</li>
									</ul>
								</td>
							</tr>
							<tr>
								<td colspan="2" valign="top" class=" no-border-right-user">&nbsp;</td>
							</tr>
						</tbody>
					</table> --%>
						<table class="full-widths">
				<tbody>
					<tr>
						<td align='center'>
							<%-- <center> --%>
							<%--	<span class="bold">TERMS OF ACCESS</span>  --%>
								<br/>
								(Effective & last revised November 22, 2011)<br/><br/>
							<%-- </center> --%>
						</td>
						
					</tr>
					<tr>
						<td>
							1.	INTRODUCTION AND ACCEPTANCE<br/><br/>
							Welcome to www.xpedx.com (&quot;Website&quot;), an interactive online service operated by xpedx (&quot;Company,&quot; &quot;us,&quot; &quot;we,&quot; or &quot;our&quot;), a division of International Paper Company. 
							<br/><br/>
						</td>
						
					</tr>
					<tr>
						<td>
							PLEASE READ THESE TERMS OF ACCESS CAREFULLY BEFORE USING THE WEBSITE.  BY ACCESSING AND/OR USING THE WEBSITE (OTHER THAN TO READ THESE TERMS OF ACCESS FOR THE FIRST TIME) YOU ARE AGREEING TO COMPLY WITH THESE TERMS OF ACCESS, WHICH MAY CHANGE FROM TIME TO TIME AS SET FORTH IN SECTION 15 BELOW.  IF YOU DO NOT AGREE TO BE BOUND BY THESE TERMS OF ACCESS, DO NOT ACCESS OR USE THE WEBSITE.  
							<br/><br/>
						</td>
						
					</tr>
					<tr>
						<td>
							In addition to these Terms of Access, we have established a Privacy Policy to explain how we collect and use information about you.  A copy of this Privacy Policy can be found here: <a href="/swc/common/xpedxPrivacyPolicy.action?sfId=xpedx&scFlag=Y" target="_blank" class="underlink">Privacy Policy</a> and is incorporated by reference into these Terms of Access.  By accessing or using the Website, you are also signifying your acknowledgement and agreement to our Privacy Policy. <br/><br/>
							2.	INTELLECTUAL PROPERTY<br/><br/>
							The Website and included content (and any derivative works or enhancements of the same) including, but not limited to, all text, illustrations, files, images, software, scripts, graphics, photos, sounds, music, videos, information, content, materials, products, services, URLs, technology, documentation, and interactive features (collectively, the &quot;Website Content&quot;) and all intellectual property rights to the same are owned by us, our licensors, or both.  Additionally, all trademarks, service marks, trade names and trade dress that may appear on the Website are owned by us, our licensors, or both.  Except for the limited use rights granted to you in these Terms of Access, you shall not acquire any right, title or interest in the Website or any Website Content.  Any rights not expressly granted in these Terms of Access are expressly reserved.<br/><br/>
							3.	WEBSITE ACCESS AND USE<br/><br/>
							(A)	When using the Website, you agree to comply with all applicable federal, state, and local laws including, without limitation copyright law.  Except as expressly permitted in these Terms of Access, you may not use, reproduce, distribute, create derivative works based upon, publicly display, publicly perform, publish, transmit, or otherwise exploit Website Content for any purpose whatsoever without obtaining prior written consent from us or, in the case third-party content, its respective owner.  In certain instances, we may permit you to download or print Website Content or both.  In such a case, you may download or print (as applicable) one copy of Website Content for your personal, non-commercial use only. You acknowledge that you do not acquire any ownership rights by downloading or printing Website Content.<br/><br/>
							(B)	Furthermore, except as expressly permitted in these Terms of Access, you may not:<br/><br/>  
							
							<ul style="margin-left: 20px;">
								<li>(i)	remove, alter, cover, or distort any copyright, trademark, or other proprietary rights notice on the Website or Website Content;<br/><br/>
								<li>(ii)	circumvent, disable or otherwise interfere with security-related features of the Website including, without limitation, any features that prevent or restrict use or copying of any content or enforce limitations on the use of the Website or Website Content;  <br/><br/></li>
								<li>(iii)	use an automatic device (such as a robot or spider) or manual process to copy or &quot;scrape&quot; the Website or Website Content for any purpose without our express written permission.  Notwithstanding the foregoing, we grant public search engine operators permission to use automatic devices (such as robots or spiders) to copy Website Content from the Website for the sole purpose of creating (and only to the extent necessary to create) a searchable index of Website Content that is available to the public.  We reserve the right to revoke this permission (generally or specifically) at any time;<br/><br/>  </li>
								<li>(iv)	collect or harvest any personally identifiable information from the Website including, without limitation, user names, passwords, email addresses; <br/><br/>  </li>
								<li>(v)	solicit other users to join or become members of any commercial online service or other organization without our prior written approval; <br/><br/></li>
								<li>(vi)	attempt to or interfere with the proper working of the Website or impair, overburden, or disable the same;<br/><br/></li>
								<li>(vii)	decompile, reverse engineer, or disassemble any portion of any the Website; <br/><br/></li>
								<li>(viii)	use network-monitoring software to determine architecture of or extract usage data from the Website;<br/><br/></li>
								<li>(ix)	encourage conduct that violates any local, state or federal law, either civil or criminal, or impersonate another user, person, or entity (e.g., using another person’s Membership (as defined in Section 5(B)) without permission, etc.); <br/><br/></li>
								<li>(x)	violate U.S. export laws, including, without limitation, violations of the Export Administration Act and the Export Administration Regulations administered by the Department of Commerce; or<br/><br/></li>
								<li>(xi)	engage in any conduct that restricts or inhibits any other user, person or entity from using or enjoying the Website. <br/><br/></li>
							</ul>
							(C)	You agree to fully cooperate with us to investigate any suspected or actual activity that is in breach of these Terms of Access.<br/><br/></li>
						</td>
					</tr>
					<tr>
						<td>
							4.	FORWARD LOOKING STATEMENTS<br/><br/>
							This Website may contain forward-looking statements. These statements are often identified by the words, &quot;will,&quot; &quot;may,&quot; &quot;should,&quot; &quot;continue,&quot; &quot;anticipate,&quot; &quot;believe,&quot; &quot;expect,&quot; &quot;plan,&quot; &quot;appear,&quot; &quot;project,&quot; &quot;estimate,&quot; &quot;intend,&quot; and words of a similar nature. These statements reflect management's current views and are subject to risks and uncertainties that could cause actual results to differ materially from those expressed or implied in these statements. Factors which could cause actual results to differ relate to: (1) industry conditions, including but not limited to changes in the cost or availability of raw materials, energy and transportation, the cyclical nature of our business, competition we face and demand and pricing for our products; (2) global economic conditions and political changes, including but not limited to changes in currency exchange rates, credit availability and the company's credit ratings issued by recognized credit rating organizations; and (3) unanticipated expenditures related to the cost of compliance with environmental and other governmental regulations and to actual or potential litigation. We undertake no obligation to publicly update any forward-looking statements, whether as a result of new information, future events or otherwise. Other factors that could cause or contribute to actual results differing materially from such forward looking statements are discussed in greater detail in the company's Securities and Exchange Commission filings. <br/><br/>
							5.	USER REGISTRATION<br/><br/>
							(A)	In order to access or use some features of the Website, you may have to become a registered user.  If you are under the age of thirteen, then you are not permitted to register as a user or otherwise submit personal information.<br/><br/>  
							(B)	 If you become a registered user, you will provide true, accurate and complete registration information and, if such information changes, you will promptly update the relevant registration information.  During registration, you will create a user name and password (a &quot;Membership&quot;), which may permit you access to certain areas of the Website not available to non-registered users.  You are responsible for safeguarding and maintaining the confidentiality of your Membership.  You are solely responsible for the activity that occurs under your Membership, whether or not you have authorized the activity.  You agree to notify us immediately at distribution.webmaster@ipaper.com of any breach of security or unauthorized use of your Membership.  <br/><br/>
							6.	USER CONTENT <br/><br/>
							(A)	We may now or in the future permit users to post, upload, transmit through, or otherwise make available on the Website (collectively, &quot;submit&quot;) messages, text, illustrations, files, images, graphics, photos, comments, sounds, music, videos, information, content, and/or other materials (&quot;User Content&quot;).  Subject to the rights and license you grant herein, you retain all right, title and interest in your User Content.  We do not guarantee any confidentiality with respect to User Content even if it is not published on the Website.  It is solely your responsibility to monitor and protect any intellectual property rights that you may have in your User Content, and we do not accept any responsibility for the same.<br/><br/>  
							(B)	You shall not submit any User Content protected by copyright, trademark, patent, trade secret, moral right, or other intellectual property or proprietary right without the express permission of the owner of the respective right. You are solely liable for any damage resulting from your failure to obtain such permission or from any other harm resulting from User Content that you submit.<br/><br/> 
							(C)	By submitting User Content to us, simultaneously with such posting you automatically grant, or warrant that the owner has expressly granted, to us a worldwide, royalty-free, perpetual, irrevocable, non-exclusive, fully sublicensable, and transferable right and license to use, reproduce, distribute, create derivative works based upon (including, without limitation, translations), publicly display, publicly perform, transmit, and publish the User Content (in whole or in part) as we, in our sole discretion, deem appropriate including, without limitation, (1) in connection with our business; and (2) in connection with the businesses of our successors, parents, subsidiaries, and their related companies.  We may exercise this grant in any format, media or technology now known or later developed for the full term of any copyright that may exist in such User Content. Furthermore, you also grant other users permission to access your User Content and to use, reproduce, distribute, create derivative works based upon, publicly display, publicly perform, transmit, and publish your User Content for personal, non-commercial use as permitted by the functionality of the Website and these Terms of Access.<br/><br/>  
							(D)	We have the right, but not the obligation, to monitor User Content.  We have the right in our sole discretion and for any reason whatsoever to edit, refuse to post, remove, or disable access to any User Content.  <br/><br/>
							7.	UNSOLICITED IDEAS AND CONCEPTS POLICY<br/><br/>
							(A)	We do not consider, accept or request any outside unsolicited ideas for new products, process or product improvements, business strategies, product names or other technical or business concepts. If, despite this non-negotiable policy, you still elect to provide unsolicited information to us, you do so at your own risk and without recourse to us.<br/><br/> 
							(B)	To avoid any misunderstandings, by providing such unsolicited ideas and concepts, regardless of any terms you may attempt to impose as a condition of your submission to us, you agree that: (1) any submissions and their contents, including the ideas contained therein, automatically become our property; (2) we have no obligation to review such submissions; (3) we are under no obligation to keep such submissions confidential; and (4) we have the unrestricted right to use and redistribute such submissions without any obligation, accounting, payment or other recourse to you, your assignees, representatives, heirs or others affiliated with you; and (5) all such information shall be deemed to be non-confidential..<br/><br/> 
							8.	WEBSITE CONTENT & THIRD PARTY LINKS<br/><br/>
							(A)	We provide the Website including, without limitation, Website Content for educational, informational, entertainment and promotional purposes only. You may not rely on any information and opinions expressed on any of our Website for any other purpose.  In all instances, it is your responsibility to evaluate the accuracy, timeliness, completeness, or usefulness of Website Content.  Under no circumstances will we be liable for any loss or damage caused by your reliance on any Website Content.<br/><br/>
							(B)	In many instances, Website Content will include content posted by a third-party or will represent the opinions and judgments of a third-party.  We do not endorse, warrant and are not responsible for the accuracy, timeliness, completeness, or reliability of any opinion, advice, or statement made on the Website by anyone other than authorized employees or spokespersons while acting in their official capacities. <br/><br/>
							(C)	The Website may contain links to other websites maintained by third parties.  We do not operate or control, in any respect, or necessarily endorse the content found on these third-party websites.  You assume sole responsibility for your use of third-party links.  We are not responsible for any content posted on third-party websites or liable to you for any loss or damage of any sort incurred as a result of your dealings with any third-party or their website.  <br/><br/>
							9.	ACCESSIBILITY<br/><br/>
							In the event that you experience difficulty accessing this site, please contact us at <a href="mailto:comm@ipaper.com" class="underlink">comm@ipaper.com</a>. Qualified individuals with a disability or disabled veterans may request a reasonable accommodation if they cannot use or access the careers section of our Website. To request a reasonable accommodation, please contact us at 877-244-8713 or send an e-mail to <a href="mailto:comm@ipaper.com" class="underlink">comm@ipaper.com</a>, and we will provide assistance to you.<br/><br/> 
							10.	INDEMNIFICATION <br/><br/>
							You agree to indemnify and hold harmless Company and its officers, directors, employees, parents, partners, successors, agents, distribution partners, affiliates, subsidiaries, and their related companies from and against any and all claims, liabilities, losses, damages, obligations, costs and expenses (including reasonable attorneys’ fees and costs) arising out of, related to, or that may arise in connection with:  (i) your access to or use of the Website; (ii) User Content provided by you or through use of your Membership; (iii) any actual or alleged violation or breach by you of these Terms of Access; (iv) any actual or alleged breach of any representation, warranty, or covenant that you have made to us; or (v) your acts or omissions.  You agree to cooperate fully with us in the defense of any claim that is the subject of your obligations hereunder.<br/><br/>   
							
							11.	DISCLAIMERS<br/><br/>
							YOU EXPRESSLY AGREE THAT USE OF THE WEBSITE IS AT YOUR SOLE RISK.  THE WEBSITE AND WEBSITE CONTENT ARE PROVIDED ON AN &quot;AS IS&quot; AND &quot;AS AVAILABLE&quot; BASIS WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED.  WITHOUT LIMITING THE FOREGOING AND TO THE FULLEST EXTENT PERMITTED BY LAW, COMPANY AND ITS OFFICERS, DIRECTORS, EMPLOYEES, PARENTS, PARTNERS, SUCCESSORS, AGENTS, DISTRIBUTION PARTNERS, AFFILIATES, SUBSIDIARIES, SUPPLIERS AND THEIR RELATED COMPANIES DISCLAIM ANY AND ALL WARRANTIES INCLUDING ANY:  (1) WARRANTIES THAT THE WEBSITE WILL MEET YOUR REQUIREMENTS; (2) WARRANTIES CONCERNING THE AVAILABILITY, ACCURACY, SECURITY, USEFULNESS, TIMELINESS, OR INFORMATIONAL CONTENT OF THE WEBSITE OR WEBSITE CONTENT; (3) WARRANTIES OF TITLE, NON-INFRINGEMENT, MERCHANTABILITY, OR FITNESS FOR A PARTICULAR PURPOSE; (4) WARRANTIES FOR SERVICES OR GOODS RECEIVED THROUGH OR ADVERTISED ON OUR WEBSITE OR ACCESSED THROUGH THE WEBSITE; (5) WARRANTIES CONCERNING THE ACCURACY OR RELIABILITY OF THE RESULTS THAT MAY BE OBTAINED FROM THE USE OF THE WEBSITE; (6) WARRANTIES THAT YOUR USE OF THE WEBSITE WILL BE SECURE OR UNINTERRUPTED; AND (7) WARRANTIES THAT ERRORS IN THE SOFTWARE WILL BE CORRECTED.<br/><br/> 
							12.	LIMITATION ON LIABILITY<br/><br/>
							UNDER NO CIRCUMSTANCES SHALL COMPANY OR ITS OFFICERS, DIRECTORS, EMPLOYEES, PARENTS, PARTNERS, SUCCESSORS, AGENTS, DISTRIBUTION PARTNERS, AFFILIATES, SUBSIDIARIES, SUPPLIERS OR THEIR RELATED COMPANIES BE LIABLE FOR INDIRECT, INCIDENTAL, SPECIAL, CONSEQUENTIAL OR EXEMPLARY DAMAGES (EVEN IF COMPANY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES), ARISING OUT OF, RELATING TO, OR IN ANY WAY CONNECTED WITH THE WEBSITE OR THESE TERMS OF ACCESS.  YOUR SOLE REMEDY FOR DISSATISFACTION WITH THE WEBSITE INCLUDING, WITHOUT LIMITATION, THE WEBSITE CONTENT IS TO STOP USING THE WEBSITE.   SUCH LIMITATION SHALL ALSO APPLY WITH RESPECT TO DAMAGES INCURRED BY REASON OF SERVICES OR PRODUCTS RECEIVED THROUGH OR ADVERTISED IN CONNECTION WITH ANY OF THE WEBSITE OR ANY LINKS ON THE WEBSITE, AS WELL AS BY REASON OF ANY INFORMATION OR ADVICE RECEIVED THROUGH OR ADVERTISED IN CONNECTION WITH ANY OF THE WEBSITE OR ANY LINKS ON THE WEBSITE.  SUCH LIMITATION SHALL ALSO APPLY WITH RESPECT TO DAMAGES INCURRED BY REASON OF ANY CONTENT POSTED BY A THIRD-PARTY OR CONDUCT OF A THIRD-PARTY ON THE WEBSITE. In some jurisdictions limitations of liability are not permitted. In such jurisdictions, some of the foregoing limitations may not apply to you. These limitations shall apply to the fullest extent permitted by law.<br/><br/>  
							13.	TERMINATION<br/><br/>
							(A)	We reserve the right in our sole discretion and at any time to terminate or suspend your Membership and/or block your access to the Website for any reason including, without limitation if you have failed to comply with the letter and spirit of these Terms of Access.  You agree that we are not liable to you or any third party for any termination or suspension of your Membership or for blocking your access to the Website.<br/><br/>
							(B)	Any suspension or termination shall not affect your obligations to us under these Terms of Access.   The provisions of these Terms of Access which by their nature should survive the suspension or termination of your Membership or these Terms of Access shall survive including, but not limited to the rights and licenses that you have granted hereunder, indemnities, releases, disclaimers, limitations on liability, provisions related to choice of law, dispute resolution, no class action, no trial by jury and all of the miscellaneous provisions in Section 16.<br/><br/>
							14.	CHOICE OF LAW; JURISDICTION AND VENUE<br/><br/>
							These Terms of Access shall be construed in accordance with the laws of the State of Ohio without regard to its conflict of laws rules.  Any legal proceedings against Company that may arise out of, relate to, or be in any way connected with our Website or these Terms of Access shall be brought exclusively in the state and federal courts of Ohio and you waive any jurisdictional, venue, or inconvenient forum objections to such courts.<br/><br/>
							15.	AMENDMENT; ADDITIONAL TERMS<br/><br/>
							(A)	We reserve the right in our sole discretion and at any time and for any reason, to modify or discontinue any aspect or feature of the Website or to modify these Terms of Access.  In addition, we reserve the right to provide you with operating rules or additional terms that may govern your use of the Website generally, unique parts of the Website, or both  (&quot;Additional Terms&quot;).  Any Additional Terms that we may provide to you will be incorporated by reference into these Terms of Access.  To the extent any Additional Terms conflict with these Terms of Access, the Additional Terms will control.<br/><br/>  
							(B)	Modifications to these Terms of Access or Additional Terms will be effective immediately upon notice, either by posting on the Website or by notification by email or conventional mail.  It is your responsibility to review the Terms of Access and the Website from time to time for any changes or Additional Terms.  Your access and use of any the Website following any modification of these Terms of Access or the provision of Additional Terms will signify your assent to and acceptance of the same.  If you object to any subsequent revision to the Terms of Access or to any Additional Terms, immediately discontinue use of the Website and, if applicable, terminate your Membership.<br/><br/>
							16.	MISCELLANEOUS<br/><br/> 
							(A)	No waiver by either party of any breach or default hereunder shall be deemed to be a waiver of any preceding or subsequent breach or default. The section headings used herein are for convenience only and shall not be given any legal import.<br/><br/> 
							(B)	Except where specifically stated otherwise, if any part of these Terms of Access is unlawful or unenforceable for any reason, we both agree that only that part of the Terms of Access shall be stricken and that the remaining terms in the Terms of Access shall not be affected.<br/><br/>
							(C)	These Terms of Access (including the Privacy Policy and any Additional Terms incorporated by reference) constitute the entire agreement of the parties with respect to the subject matter hereof, and supersede all previous written or oral agreements between us with respect to such subject matter.<br/><br/> 
							(D)	You may not assign these Terms of Access or assign any rights or delegate any obligations hereunder, in whole or in part, without our prior written consent.  Any such purported assignment or delegation by you without the appropriate prior written consent will be null and void and of no force and effect.  We may assign these Terms of Access or any rights hereunder without your consent and without notice.<br/><br/>
					</td></tr>
					
						<tr>
							<td colspan="2" valign="top" class=" no-border-right-user">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2" valign="top" class="no-border-right-user">
								<ul class="float-right ">
									<li class="float-right margin-10"><a href="javascript:toaSubmit('Y')"
										class="grey-ui-btn"><span>Accept</span>
										</a>
									</li>
									<li class="float-right margin-10 checkaddavail"><a href="<s:url action='logout' namespace='/home' includeParams='none'/>"
										class="grey-ui-btn"><span>Cancel</span>
										</a>
									</li>								</ul>
							</td>
						</tr>
						<tr>
							<td colspan="2" valign="top" class=" no-border-right-user">&nbsp;</td>
						</tr>
					</tbody>
				     </table>
					
				</div>
				<!-- End Pricing -->
				<br />
			</div>
			<!-- end main  --> 
			</div>
		</div>
		<!-- end container  -->  
	</body>
	</s:form>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<s:set name='isGuestUser' value="wCContext.guestUser" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!-- styles -->
<s:if test="#isGuestUser == false">

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
</s:if>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MISC<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/IE.css" />
	<![endif]-->
	
<!-- javascript -->


					
<s:if test="#isGuestUser == true">
	
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
</s:if>
	
	
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- jQuery -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
    
    <link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
     
      <script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
 
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


	

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.maskedinput-1.3<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- Lightbox/Modal Window -->


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
<title><s:property value="wCContext.storefrontId" /> /  <s:text name="MSG.SWC.MISC.TERMS.GENERIC.TABTITLE"/> </title>
	<!--WebTrands Start -->
	<meta name="WT.ti" content="<s:property value="wCContext.storefrontId" /> / Terms of Access" /> 
	<!--WebTrands End -->
</head>
    <s:set name='isGuestUser' value="wCContext.guestUser" />
    <s:set name='_action' value='[0]' />
    
    <body class="ext-gecko ext-gecko3">
		<div id="main-container">
			<div id="main">
				
			<s:action name="xpedxHeader" executeResult="true" namespace="/common" />

			<div class="container">
				<!-- breadcrumb -->

				<div id="mid-col-mil">

					<div>
						<div class="padding-top2 float-right">
							<a href="javascript:window.print()" class="underlink"><span class="print-ico-xpedx underlink"><img
									height="15" width="16" alt="Print This Page"
									src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif">Print Page
							</span>
							</a>
						</div>
						<div class="padding-top3 black">
							<strong class="black"><s:text name="MSG.SWC.MISC.TERMS.GENERIC.PGTITLE"/></strong>
						</div>

					</div>
				<%-- 	<table class="full-widths">
						<tbody>
							<tr>
								<td colspan="2" class="no-border-right-user"></td>
							</tr>
							<tr>
								<td colspan="2" valign="top"
									class=" no-border-right-user access"><strong>REVIEW
										THESE TERMS OF ACCESS. </strong><br /> All access to and use of this
									site, the information and applications provided herein is
									subject to these terms. Entering into or accessing this site
									indicates that you have reviewed these terms and agree to be
									bound by them. Access to this site is limited to the provision
									and sale of products and services to our customers within the
									United States of America. Any access or attempt to access any
									other system or information is strictly prohibited. The use of
									this site and the information provided herein is limited to a
									legitimate business purpose.<br /> <br /> <strong>Copyright</strong><br />
									All content included on this site, such as text, graphics,
									logos, button icons, images, audio clips and software, is the
									property of International Paper Company or its content
									suppliers and protected by U.S. and international copyright
									laws. Similarly, the compilation of all content on this site is
									the exclusive property of International Paper Company and its
									divisions (collectively, &quot;International Paper&quot;) and
									protected by such laws. All software used on this site is the
									property of International Paper or its software supplier and
									protected by the same laws.<br /> <br /> <strong>How
										You May Use Our Materials</strong><br /> You may download, use, and
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
									on this site.<br /> <br /> <strong>Trademarks</strong><br />
									This site contains many trade and service marks of
									International Paper and our suppliers. All marks are the
									property of their respective owners and all rights in the
									intellectual property contained in this site are reserved.<br />
									<br /> <strong>How You May Use Our Marks</strong><br /> You
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
									<strong>Disclaimer</strong><br /> THIS SITE IS PROVIDED BY
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
									SPECIAL, PUNITIVE AND CONSEQUENTIAL DAMAGES.<br /> <br /> <strong>Links
										to Other Sites</strong><br /> International Paper Company has not
									reviewed all of the sites linked to this web site.
									International Paper Company is not responsible for any off-site
									pages or any other sites linked to this web site. The inclusion
									of any link to such a site does not imply endorsement by
									International Paper Company of the site and is provided merely
									as a convenience to our customers. Your linking to any other
									off-site pages or other sites is at your own risk.<br /> <br />
									<strong>Disclosure and Use of Your Communications</strong><br />
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
									<br /> <strong>Hardware and Software Requirements</strong><br />
									This site may be accessed through any personal computer that
									runs Internet Explorer Release 5.0 or higher.<br /> <br /> <strong>Cross
										Reference Information</strong><br /> Product cross-references do not
									imply that all products are available, or have functional
									equivalency or that performance and other characteristics are
									perfectly comparable. Review all specifications prior to
									purchase.<br /> <br /> <strong>Security</strong><br />
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
									<br /> <strong>Terms of Sale</strong><br /> The Terms of Sale
									contain International Paper's conditions of sale. Placement of
									an order shall be deemed acceptance of the Terms of Sale.<br />
									<br /> <strong>Projections and Forecasts</strong><br /> The
									content of the International Paper web site may contain
									projections and other forward-looking statements regarding
									future events or the future financial performance International
									Paper Company. Such statements are only predictions and actual
									events or results may differ materially. Please see
									International Paper Company's Form 10-K for actual information.<br />
									<br /> <strong>Foreign Corrupt Practices Act</strong><br />
									The user acknowledges that it is not the agent of International
									Paper or International Paper Company and represents and
									warrants that all purchases are for use within the United
									States of America and that purchaser has not and covenants that
									it will not pay anything of value to any government employee in
									connection with the resale of the products.<br /> <br /> <strong>Governing
										Law; Limitations</strong><br /> These Terms and Conditions shall be
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
									class=" no-border-right-user access"><strong><span
										class="access">TERMS OF SALE - (E-COMMERCE SITE) </span>
								</strong>
								</td>
							</tr>
							<tr>
								<td width="50%" valign="top" class=" no-border-right-user "><div
										class="access padding-right3">
										1. <strong>BUSINESS CUSTOMERS ONLY</strong> - Buyer hereby
										warrants that it is engaged in a legitimate business activity
										and acknowledges that International Paper sells only to
										businesses in the United States. <br /> <br /> 2. <strong>ACCEPTANCE
											OF ORDERS </strong>- Any acceptance herein of Buyer's Order by
										International Paper is expressly made conditional on Buyer's
										assent to the terms contained herein. No additional or
										different terms or conditions shall be inferred or implied
										without the express written consent of International Paper.<br />
										<br /> 3. <strong>PRICE</strong> - The price for the material
										covered hereby shall be adjusted to International Paper's
										published price or contract pricing in effect at time of
										shipment. <br /> <br /> 4. <strong>DELIVERY</strong> -
										F.O.B. points, freight allowances and other transportation
										arrangements applicable to this sale shall be as set forth in
										International Paper's published sales policy statements.
										Contact your local branch for further information.
										International Paper will not consider claims for errors,
										damages or shortages in shipments unless transmitted to
										International Paper within thirty (30) days of the date of
										shipment and accompanied by documents sufficient to
										substantiate the claim. <br /> <br /> 5. <strong>TERMS
											OF PAYMENT</strong> - Only if Buyer has previously made credit
										arrangements shall the terms be in accordance with the terms
										provided by the delivering International Paper branch.
										Otherwise, terms of payment shall be cash in advance. <br />
										<br /> 6. <strong>SECURITY INTEREST</strong> - On any sales
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
										<br /> 7. <strong>CANCELLATION</strong> - Orders cannot be
										canceled except with International Paper's consent and upon
										terms that will indemnify International Paper against loss and
										Buyer may be subject to restocking or other charges. <br /> <br />
										8. <strong>WARRANTY</strong> - International Paper warrants to
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
									Federal Trade Commission Improvement Act.<br /> <br /> 9. <strong>LIMITATION
										OF LIABILITY</strong> - International Paper's liability on any claim of
									any kind, whether based on negligence, warranty or otherwise,
									for any loss or damage arising out of, connected with, or
									resulting from this contract, or from the performance or breach
									thereof, or from the manufacture, sale, delivery, resale or use
									of any material or provision of any services covered by or
									furnished pursuant to this order shall in no case exceed the
									price allocatable to the material which gives rise to the
									claim. IN NO EVENT SHALL International Paper BE LIABLE FOR
									SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES. <br /> <br />
									10. <strong>EXCUSABLE DELAYS</strong> - International Paper
									shall not be liable for delay or failure to perform due to
									causes beyond International Paper's control such as, but not
									limited to, the unavailability of goods, law or governmental
									regulation, judgment of a competent court, labor difficulties,
									accidents, transportation delays, Acts of God, war or civil
									commotion. <br /> <br /> 11. <strong>COMPLIANCE WITH
										LAWS</strong> - International Paper hereby certifies that the products
									sold hereunder shall comply with the provisions of the Fair
									Labor Standards Act of 1938, as amended, and Executive Order
									No. 11246, Section 202 of the Equal Employment Opportunity Laws
									and Regulations and, where applicable, ASPR No. 12-802, the
									applicable provisions of the Occupational Safety and Health Act
									of 1970 and all other applicable state, Federal and local laws,
									regulations, rules and ordinances.<br /> <br /> 12. <strong>TAXES</strong>
									- Any tax, duty, or other governmental charge now or hereafter
									levied upon the production, sale, use or shipment of materials
									ordered or sold hereunder shall be for the Buyer's account.
									Such governmental levies are not covered in the International
									Paper's price unless expressly so stated. If the Buyer is tax
									exempt, a tax certificate must be on file for each jurisdiction
									affected.<br /> <br /> 13. <strong>NON-WAIVER</strong> -
									International Paper's waiver of any of these Terms of Sale in
									any instance shall be limited to that instance and shall not
									imply that International Paper will waive such Term of Sale on
									any future occasion.<br /> <br /> 14. <strong>COMPLETE
										AGREEMENT</strong>. The terms and conditions in International Paper's
									site Terms of Access, acknowledgments, quotations and invoices
									are incorporated herein by reference and with these terms of
									sale constitute the entire and exclusive agreement between
									Buyer and International Paper and its divisions.</td>
							</tr>
							<tr>
								<td colspan="2" valign="top" class=" no-border-right-user">&nbsp;</td>
							</tr>
						</tbody>
					</table>--%>
					
			<table class="full-widths">
				<tbody>
					<tr>
						<td align='center'>
							<%-- <center> --%>
								<span class="bold">TERMS OF ACCESS</span>
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
								<li>(ix)	encourage conduct that violates any local, state or federal law, either civil or criminal, or impersonate another user, person, or entity (e.g., using another person&#39;s Membership (as defined in Section 5(B)) without permission, etc.); <br/><br/></li>
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
							(B)	 If you become a registered user, you will provide true, accurate and complete registration information and, if such information changes, you will promptly update the relevant registration information.  During registration, you will create a user name and password (a &quot;Membership&quot;), which may permit you access to certain areas of the Website not available to non-registered users.  You are responsible for safeguarding and maintaining the confidentiality of your Membership.  You are solely responsible for the activity that occurs under your Membership, whether or not you have authorized the activity.  You agree to notify us immediately at <a href="mailto:distribution.webmaster@ipaper.com" class="underlink">distribution.webmaster@ipaper.com</a> of any breach of security or unauthorized use of your Membership.  <br/><br/>
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
							You agree to indemnify and hold harmless Company and its officers, directors, employees, parents, partners, successors, agents, distribution partners, affiliates, subsidiaries, and their related companies from and against any and all claims, liabilities, losses, damages, obligations, costs and expenses (including reasonable attorneys&#39; fees and costs) arising out of, related to, or that may arise in connection with:  (i) your access to or use of the Website; (ii) User Content provided by you or through use of your Membership; (iii) any actual or alleged violation or breach by you of these Terms of Access; (iv) any actual or alleged breach of any representation, warranty, or covenant that you have made to us; or (v) your acts or omissions.  You agree to cooperate fully with us in the defense of any claim that is the subject of your obligations hereunder.<br/><br/>   
							
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
						<td>
							<b>PLEASE NOTE:</b> Our Privacy Policy has changed effective November 22, 2011. If you submitted personally identifiable information to us prior to the above effective date, and desire to opt out of having that previously submitted personally identifiable information from being treated under the new policy, please contact us at <a href="mailto:distribution.webmaster@ipaper.com" class="underlink">distribution.webmaster@ipaper.com</a>.


							<br/><br/><br/>
						</td>
						
					</tr>
					<tr>
						<td align='center'>
							<%-- <center> --%>
								<span class="bold">PRIVACY POLICY</span>
								<br/>
								(Effective and last revised November 22, 2011)<br/><br/><br/>
							<%-- </center> --%>
						</td>
						
					</tr>
					<tr><td>
					<p>Welcome to <a href="http://www.xpedx.com">www.xpedx.com</a> ("Website"), an interactive online service operated by xpedx (“us”, “we”, or “our”), a division of International Paper Company. Please read the following to learn more about our Privacy Policy, which includes compliance with California Privacy Rights (see also <a href="http://www.privacyprotection.ca.gov">http://www.privacyprotection.ca.gov</a>). </p><br/>
					<p>We respect your privacy and are committed to protecting personally identifiable information you may provide us through our Website. We have adopted this Privacy Policy ("Privacy Policy") to explain what information may be collected on our Website, how we use this information, and under what circumstances we may disclose the information to third parties. This Privacy Policy applies only to information we collect through our Website and does not apply to our collection of information from other sources. </p><br/>
					<p>This Privacy Policy, together with the Terms of Access posted on our Website, sets forth the general rules and policies governing your use of our Website.  Depending on your activities when visiting our Website, you may be required to agree to additional terms and conditions.  </p><br/>
					<p>We generally keep this Privacy Policy posted on our Website and you should review it frequently, as it may change from time to time without notice. Any changes will be effective immediately upon the posting of the revised Privacy Policy.  WHEN YOU ACCESS OUR WEBSITE, YOU AGREE TO THIS PRIVACY POLICY.  IF YOU DO NOT AGREE TO THIS PRIVACY POLICY, OR TO ANY CHANGES WE MAY SUBSEQUENTLY MAKE, IMMEDIATELY STOP ACCESSING OUR WEBSITE.  If, at any point, we decide to use your personally identifiable information in a manner materially different than what was stated at the time it was collected, we will notify you of this change by e-mail to the last e-mail address provided to us. You will have a choice (by means of an “opt out” opportunity) as to whether or not we use their information in this different manner. We will use information in accordance with the Privacy Policy under which your information was collected if you opt out of the new policy. </p><br/>
					</td>
					
					<tr>
						<td><b>
							A.	INFORMATION WE COLLECT </b><br/><br/>
							Our Website typically collects two kinds of information about you: (a) information that you provide that personally identifies you; and (b) information that does not personally identify you, that we automatically collect when you visit our Website or that you provide us.<br/><br/>
							
							<b>(1)  Personally Identifiable Information: </b> Our definition of personally identifiable information includes any information that may be used to specifically identify or contact you, such as your name, mail address, phone number, etc.  As a general policy, we do not automatically collect your personally identifiable information when you visit our Website.  In certain circumstances, we may request, allow or otherwise provide you an opportunity to submit your personally identifiable information in connection with a feature, program, promotion or some other aspect of our Website.  For instance, you may: (a) provide your name, mail/shipping address, email address, credit card number and phone number when registering with our Website or using our online store; (b) provide certain demographic information about you (e.g., age, gender, purchase preference, usage frequency, etc.) when participating in a survey or poll; or (c) post a general comment and/or recommendation on our Website.  Whether or not you provide this information is your choice; however, in many instances this type of information is required to participate in the particular activity, realize a benefit we may offer, or gain access to certain content on our Website. <br/><br/>
							<b>(2)  Non-Personally Indentifiable Information:</b> Our definition of non-personally identifiable information is any information that does not personally identify you.  Non-personally identifiable information can include certain personally identifiable information that has been de-identified; that is, information that has been rendered anonymous.  We obtain non-personally identifiable information about you from information that you provide us, either separately or together with your personally identifiable information.  We also automatically collect certain non-personally identifiable information from you when you access our Website.  This information can include, among other things, IP addresses, the type of browser you are using (e.g., Internet Explorer, Firefox, Safari, etc.), the third party website from which your visit originated, the operating system you are using (e.g., Vista, Windows XP, Mac OS, etc.), the domain name of your Internet service provider (e.g., America Online, NetZero, etc.), the search terms you use on our Website, the specific web pages you visit, and the duration of your visits. <br/><br/> 
							<b>B.	HOW WE USE & SHARE THE INFORMATION COLLECTED </b><br/><br/>
							<b>(1)  Personally Identifiable Information:</b> The personally identifiable information you submit to us is generally used to carry out your requests, respond to your inquiries, better serve you, or in other ways naturally associated with the circumstances in which you provided the information. We may also use this information to later contact you for a variety of reasons, including for customer service purposes and/or to provide you promotional information for our products.  We may use third parties to carry out the foregoing and other business functions and, in such instances, these third parties may use your information for these limited purposes.In some instances we may share your personally identifiable information with our co-promotional partners and others with whom we have marketing relationships. Except as provided in this Privacy Policy, our Terms of Access, or as disclosed when you submit the information, your personally identifiable information will not be shared or sold to any third parties without your prior approval. Further, you may opt-out from receiving future promotional information from us as set forth below.<br/><br/>
							<b>(2)  Non-Personally Identifiable Information:</b> We use non-personally identifiable information in a variety of ways, including to help analyze site traffic, understand customer needs and trends, carry out targeted promotional activities, and to improve our services. We may use your non-personally identifiable information by itself or aggregate it with information we have obtained from others. We may share your non-personally identifiable information with our affiliated companies and third parties to achieve these objectives and others, but remember that aggregate information is anonymous information that does not personally identify you.<br/><br/>
							<b>C.	COOKIES AND PREFERENCE BASED ADVERTISING</b></br></br>
							<b>(1) Cookies and Web Beacons: </b>We automatically receive and store certain types of non-personally identifiable information whenever you interact with us. For example, like many websites, we use "cookies" and "web beacons" (also called “clear gifs” or “pixel tags”) to obtain certain types of information when your web browser accesses our Website.  "Cookies" are small files that we transfer to your computer's hard drive or your Web browser memory to enable our systems to recognize your browser and to provide convenience and other features to you, such as recognizing you as a frequent user of our Website. "Web beacons" are tiny graphics with a unique identifier, similar in function to cookies, and may be used to track the online movements of users, when an email has been opened, and to provide other information. </br></br>
							 Examples of the information we collect and analyze in this manner include the Internet protocol (IP) address used to connect your computer to the Internet; computer and connection information such as browser type and version, operating system, and platform; your behavior on our Website, including the URL you come from and go to next (whether this URL is on our site or not); and cookie number. <b> It is important to note that the cookies and Web beacons that we use do not contain and are not tied to personally identifiable information. </b><br/><br/>
                             If you are concerned about the storage and use of cookies, you may be able to direct your internet browser to notify you and seek approval whenever a cookie is being sent to your Web browser or hard drive. You may also delete a cookie manually from your hard drive through your internet browser or other programs. Please note, however, that some parts of our Website will not function properly or be available to you if you refuse to accept a cookie or choose to disable the acceptance of cookies. </br></br>
							 <b>(2)  Preference Based Advertising:</b> We may now or in the future work with third parties, including third party advertising networks and website analysis firms, who use cookies and web beacons (also called “clear gifs” and “pixel tags”) to collect non-personally identifiable information when you visit our Website and third party sites. This non-personally identifiable information, collected through cookies and web beacons, is often used by these advertising networks to serve you with advertisements, while on third party sites, tailored to meet your preferences and needs.  For more information on how this type of advertising works, go to <a href="http://www.aboutads.info/consumers">www.aboutads.info/consumers</a>.<br/><br/>  
							If you do not wish to be served third party advertising via these third party advertising networks, please go to <a href="http://www.aboutads.info/choices">www.aboutads.info/choices</a> and follow the simple opt-out process. A couple of important notes about this opt-out tool: (1) it includes all the advertising networks that we may work with, but also many that we do not work with; and (2) it may rely on cookies to ensure that a given advertising network does not collect information about you (“Opt-out Cookies”).  Therefore, if you buy a new computer, change web browsers or delete these Opt-out Cookies from your computer, you will need to perform the opt-out task again.<br/><br/>
							 <b>D.	OTHER USES & INFORMATION </b><br/><br/>
							 <b>(1)  IP Addresses:</b> An IP address is a number that is automatically assigned to your computer whenever you are surfing the Internet. Web servers (computers that "serve up" web pages) automatically identify your computer by its IP address. When visitors request pages from our Website, our servers typically log their IP addresses. We collect IP addresses for purposes of system administration, to report non-personal aggregate information to others, and to track the use of our Website.  IP addresses are considered non-personally identifiable information and may also be shared as provided above.  It is not our practice to link IP addresses to anything personally identifiable; that is, the visitor's session will be logged, but the visitor remains anonymous to us. However, we reserve the right to use IP addresses to identify a visitor when we feel it is necessary to enforce compliance with our Website rules or to: (a) fulfill a government request; (b) conform with the requirements of the law or legal process; (c) protect or defend our legal rights or property, our Website, or other users; or (d) in an emergency to protect the health and safety of our Website’s users or the general public. <br/><br/>
							 <b>(2) Email Communications: </b> If you send us an email with questions or comments, we may use your personally identifiable information to respond to your questions or comments, and we may save your questions or comments for future reference.  For security reasons, we do not recommend that you send non-public personal information, such as passwords, social security numbers, or bank account information, to us by email.  However, aside from our reply to such an email, it is not our standard practice to send you email unless you request a particular service or sign up for a feature that involves email communications, it relates to purchases you have made with us (e.g., product updates, customer support, etc.), we are sending you information about our other products and services, or you consented to being contacted by email for a particular purpose. In certain instances, we may provide you with the option to set your preferences for receiving email communications from us; that is, agree to some communications but not others. You may "opt out" of receiving future commercial email communications from us by clicking the "unsubscribe" link included at the bottom of most emails we send, or as provided below; provided, however, we reserve the right to send you transactional emails such as customer service communications. <br/><br/>
							 <b>(3) Transfer of Assets: </b> As we continue to develop our business, we may sell or purchase assets. If another entity acquires us or all (or substantially all) of our assets, the personally identifiable information and non-personally identifiable information we have about you will be transferred to and used by this acquiring entity, though we will take reasonable steps to ensure that your preferences are followed. Also, if any bankruptcy or reorganization proceeding is brought by or against us, all such information may be considered an asset of ours and as such may be sold or transferred to third parties. <br/><br/>
							
							<b>(4) Other:</b> Notwithstanding anything herein to the contrary, we reserve the right to disclose any personally identifiable or non-personally identifiable information about you if we are required to do so by law, with respect to copyright and other intellectual property infringement claims, or if we believe that such action is necessary to: (a) fulfill a government request; (b) conform with the requirements of the law or legal process; (c) protect or defend our legal rights or property, our Website, or other users; or (d) in an emergency to protect the health and safety of our Website's users or the general public. <br/><br/> 
							<b>(5) Your California Privacy Rights:</b> Residents of the State of California, under certain provisions of the California Civil Code, have the right to request from companies conducting business in California a list of all third parties to which the company has disclosed certain personally identifiable information as defined under California law during the preceding year for third party direct marketing purposes. You are limited to one request per calendar year. In your request, please attest to the fact that you are a California resident and provide a current California address for our response. You may request the information in writing as provided in Section I below.<br/><br/>
							<b>E.	PUBLIC FORUMS</b> <br/><br/>

							We may offer chat rooms, blogs, message boards, bulletin boards, or similar public forums where you and other users of our Website can communicate. The protections described in this Privacy Policy do not apply when you provide information (including personal information) in connection with your use of these public forums.  We may use personally identifiable and non-personally identifiable information about you to identify you with a posting in a public forum.  Any information you share in a public forum is public information and may be seen or collected by anyone, including third parties that do not adhere to our Privacy Policy. We are not responsible for events arising from the distribution of any information you choose to publicly post or share through our Website. <br/><br/>
							
							<b>F.	CHILDREN </b><br/><br/>
							The features, programs, promotions and other aspects of our Website requiring the submission of personally identifiable information are not intended for children.  We do not knowingly collect personally identifiable information from children under the age of 13.  If you are a parent or guardian of a child under the age of 13 and believe he or she has disclosed personally identifiable information to us please contact as provided in Section I below. A parent or guardian of a child under the age of 13 may review and request deletion of such child's personally identifiable information as well as prohibit the use thereof. <br/><br/>
							<b>G.	KEEPING YOUR INFORMATION SECURE </b><br/><br/>

							We have implemented security measures we consider reasonable and appropriate to protect against the loss, misuse and alteration of the information under our control.  Please be advised, however, that while we strive to protect your personally identifiable information and privacy, we cannot guarantee or warrant the security of any information you disclose or transmit to us online and are not responsible for the theft, destruction, or inadvertent disclosure of your personally identifiable information. In the unfortunate event that your "personally identifiable information" (as the term or similar terms are defined by any applicable law requiring notice upon a security breach) is compromised, we may notify you by e-mail (at our sole and absolute discretion) to the last e-mail address you have provided us in the most expedient time reasonable under the circumstances; provided, however, delays in notification may occur while we take necessary measures to determine the scope of the breach and restore reasonable integrity to the system as well as for the legitimate needs of law enforcement if notification would impede a criminal investigation. From time to time we evaluate new technology for protecting information, and when appropriate, we upgrade our information security systems. <br/><br/>
							
							<b>H.	OTHER SITES/LINKS </b>	

							Our Website may link to or contain links to other third party websites that we do not control or maintain, such as in connection with purchasing products referenced on our Website and banner advertisements. We are not responsible for the privacy practices employed by any third party website. We encourage you to note when you leave our Website and to read the privacy statements of all third party websites before submitting any personally identifiable information. <br/><br/>
							<b>I.	CONTACT & OPT-OUT INFORMATION </b>

								You may contact us as at <a href="mailto:distribution.webmaster@ipaper.com" class="underlink">distribution.webmaster@ipaper.com</a> if: (a) you have questions or comments about our Privacy Policy; (b) wish to make corrections to any personally identifiable information you have provided us; (c) want to opt-out from receiving future commercial correspondence from us; or (d) wish to withdraw your consent to our sharing of your personally identifiable information with relevant co-promotional partners.  Please note that using the above process to opt-out of receiving commercial correspondence from us will not necessarily opt you out of receiving correspondence from International Paper Company or its other divisions/affiliated entities (“Related Entities”) to the extent you have given your information (e.g., email address) to any such Related Entity.  For information on how to opt-out of communications from Related Entities, please see the Privacy Policy/Statement at <a href= "http://www.internationalpaper.com"> www.internationalpaper.com</a>. <br/><br/>

								We will respond to your request and, if applicable and appropriate, make the requested change in our active databases as soon as reasonably practicable.  Please note that we may not be able to fulfill certain requests while allowing you access to certain benefits and features of our Website.  <br/><br/>
							<b>J.	SOLE STATEMENT </b>

							This Privacy Policy as posted on this Website is the sole statement of our privacy policy with respect to this Website, and no summary, modification, restatement or other version thereof, or other privacy statement or policy, in any form, is valid unless we post a new or revised policy to the Website.<br/><br/><br/>
							</td>
							</tr>
						<tr>
						<td align='center'>
							<%-- <center> --%>
								<span class="bold">xpedx Conditions of Sale – xpedx.com</span>
								<br/>
								<br/>
							<%-- </center> --%>
						</td>
						
					</tr>
					<tr><td>
								
					<b>1.	ACCEPTANCE OF ORDERS -</b> Any acceptance herein of Buyer's Order by Seller is expressly made conditional on Buyer's assent to the additional or different terms contained herein.  Orders are accepted only at Seller's Offices and are final and cannot be changed or canceled upon acceptance.<br/><br/>
				    <b>2.	PRICE -</b>The price for the material covered hereby shall be adjusted to Seller's published price or contract pricing in effect at time of shipment.<br/><br/> 
					<b>3.	DELIVERY -</b> F.O.B. points, freight allowances and other transportation arrangements applicable to this sale shall be as set forth in Seller's published sales policy statements.  Seller will not consider claims for errors, damages or shortages in shipments unless transmitted to Seller within thirty (30) days of the date of shipment and accompanied by documents sufficient to substantiate the claim.<br/><br/>
					<b>4.	TERMS OF PAYMENT - </b>Terms of payment shall be in accordance with Seller's invoice or applicable published sales policy statements.  If Seller’s invoice contains a prompt payment discount, Buyer must deduct such discount amount from its timely payment of the invoice or be forever barred from claiming such discount.	<br/><br/>
					<b>5.	CANCELLATION -</b> The order hereby acknowledged cannot be canceled except with Seller's consent and upon terms that will indemnify Seller against loss.<br/><br/>	
					<b>6.	WARRANTY -</b> Seller warrants to Buyer that the material to be delivered hereunder will be free from defects in material, workmanship and title and will be of the kind and quality described in the contract.  <u>THE FOREGOING WARRANTY IS EXCLUSIVE AND IN LIEU OF ALL OTHER WARRANTIES, WHETHER WRITTEN, ORAL OR IMPLIED (INCLUDING ANY WARRANTY OF MERCHANTABILITY OR FITNESS FOR PURPOSE).</u>  Claims for defects in materials will not be considered unless made in writing within ninety (90) days of receipt of material, and unless Buyer promptly discontinues use of such material.  Material proving defective in the hands of Buyer, when used for purposes for which such material is intended, will be replaced or credit will be allowed for the price thereof at Seller's option.	<br/><br/>
					<b>7.	LIMITATION OF LIABILITY -</b> Seller's liability on any claim of any kind, whether based on negligence, warranty or otherwise, for any loss or damage arising out of, connected with, or resulting from this contract, or from the performance or breach thereof, or from the manufacture, sale, delivery, resale or use of any material or provision of any services covered by or furnished pursuant to this document shall, in no case, exceed the price allocable to the material which gives rise to the claim.  In no event shall Seller be liable for special, incidental or consequential damages. Seller shall not be liable for delay or failure to perform due to causes beyond Seller's control.	<br/><br/>
					<b>8.	COMPLIANCE WITH LAWS - </b>  The parties agree to comply with all applicable laws and regulations regarding employment and discrimination, including Executive Order 11246. The OFCCP affirmative action clauses set forth in 41 CFR §60-1.4, 60-250.4, 60-741.5(a) are hereby incorporated by reference.<br/><br/>	
					<b>9.	TAXES -</b> Any tax, duty, or other governmental charge now or hereafter levied upon the production, sale, use or shipment of materials ordered or sold hereunder shall be for the Buyer's account.  Such governmental levies are not covered in the Seller's price, unless expressly so stated.<br/><br/>
						<%-- </>You may enter the site only if you acknowledge acceptance of the Terms of Access, Privacy Policy and xpedx Conditions of Sale, by clicking “Accept” below:<br/><br/>--%>
						
						</td>
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
          
		<s:action name="xpedxFooter" executeResult="true" namespace="/common" />            
	</body>
</html>
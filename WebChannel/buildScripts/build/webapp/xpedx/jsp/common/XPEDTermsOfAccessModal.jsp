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

<title><s:property value="wCContext.storefrontId" /> /  Terms of Access</title>
	
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
							TERMS OF ACCESS
						</div>
					</div>
					<table class="full-widths">
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
										<%-- <li class="float-right margin-10"><a href="<s:url action="logout" namespace="/home" includeParams='none'/>"
											class="grey-ui-btn"><span>Do Not Accept</span>
										</a>
										</li> --%>
									</ul>
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
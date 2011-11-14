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
  
<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.MISC.PRPY.GENERIC.TABTITLE" /></title>

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
							<a href="javascript:window.print()"><span class="print-ico-xpedx underlink"><img
									height="15" width="16" alt="Print This Page"
									src="/swc/xpedx/images/common/print-icon.gif">Print Page
							</span>
							</a>
						</div>
						<div class="padding-top3 black">
							<div class="page-title"><s:text name="MSG.SWC.MISC.PRPY.GENERIC.PGTITLE" /></div>
						</div>

					</div>
<%-- 					<table class="full-widths">
						<tbody>
							<tr>
								<td colspan="2" class="no-border-right-user"></td>
							</tr>
							<tr>
								<td colspan="2" valign="top"
									class=" no-border-right-user access"><strong>Privacy Policy page. </strong><br /> 
									All access policy subjected to change.these terms.  <br /> <br />
								<br />
								</td>
							</tr>


							<tr>
								<td colspan="2" valign="top" class=" no-border-right-user">&nbsp;</td>
							</tr>
						</tbody>
					</table> 
--%>
					<br> &nbsp;</br>
					<br> &nbsp;</br>
					<div class=WordSection1>
					
					<p class=MsoListParagraphCxSpFirst align=center style='margin-left:0in;
					text-align:center'><b><span style='font-size:10.0pt;line-height:115%'>Legal
					Recommendations for Links and Updates to Terms of Access/Privacy Policy/Sales
					Terms </span></b></p>
					
					<p class=MsoListParagraphCxSpMiddle align=center style='margin-left:0in;
					text-align:center'><b><span style='font-size:10.0pt;line-height:115%'>on
					xpedx.com Website (NextGen)</span></b></p>
					
					<p class=MsoListParagraphCxSpLast align=center style='margin-left:0in;
					text-align:center'><b><span style='font-size:10.0pt;line-height:115%'>5-13-11</span></b></p>
					
					<p class=MsoNormal><span style='font-size:10.0pt;line-height:115%'>1.            xpedx.com
					(General Website access, prior to log-in to B2B site)</span></p>
					
					<p class=MsoNormal style='margin-left:.75in'><span style='font-size:10.0pt;
					line-height:115%'>a.     Links need to be available at the bottom of each page for
					the Terms of Access and the Privacy Policy for anyone accessing the xpedx.com
					site.  This functionality is currently available on xpedx.com.  No
					functionality requiring acknowledgment/acceptance of these documents is
					required at this “browser” (pre-login) level except as provided in the 1.c.
					below.</span></p>
					
					<p class=MsoNormal style='margin-left:.75in'><span style='font-size:10.0pt;
					line-height:115%'>b.    In the event of an update to the Terms of Access or the
					Privacy Policy under the direction and with the approval of the Legal
					Department, it would be helpful to have the functionality to enable the links to
					the documents to be notated for a six-month period with a “revised __/__/__” to
					provide notice to the user that these documents have been updated.  This
					notation would be removed after six months following each update.  A new
					notation would be inserted, as appropriate, for subsequent updates.  </span></p>
					
					<p class=MsoNormal style='margin-left:.75in'><span style='font-size:10.0pt;
					line-height:115%'>c.     For any location in the pre-login portion of the site in
					which personally identifiable information (name, address, email, etc.) is provided,
					such as on a “Contact Us” page, we’d recommend that there be a statement prior
					to the “submit” button as follows:</span></p>
					
					<p class=MsoListParagraph style='margin-top:0in;margin-right:.5in;margin-bottom:
					10.0pt;margin-left:1.5in'><span style='font-size:10.0pt;line-height:115%'>“By
					clicking the <b>submit</b> button below, the user accepts the terms of the
					xpedx Privacy Policy available at [insert link to Privacy Policy] and agrees
					that information submitted is subject to the Privacy Policy.” </span></p>
					
					<p class=MsoNormal style='margin-left:.75in'><span style='font-size:10.0pt;
					line-height:115%'>We’d need the ability to electronically retain “acceptance”
					data (date, time, person accepting) for future reference.</span></p>
					
					<p class=MsoNormal><span style='font-size:10.0pt;line-height:115%'>2.            xpedx.com
					B2B site (User login required) </span></p>
					
					<p class=MsoNormal style='margin-left:.75in'><span style='font-size:10.0pt;
					line-height:115%'>a.     On the first time a user logs in to the new B2B site,
					the user should be automatically directed to the page containing the Terms of
					Access, the Privacy Policy and the Conditions of Sale.   We understand that an
					“accept” button will appear at the bottom of this document.   The user should not
					be able to access the site without clicking the “accept” button.   The
					“acceptance” data (date, time, person accepting) needs to be retained for
					future reference.</span></p>
					
					<p class=MsoNormal style='margin-left:.75in'><span style='font-size:10.0pt;
					line-height:115%'>b.    In the event of a material change in the Terms of
					Access, the Privacy Policy or the Conditions of Sale, functionality should be
					available to trigger a “new” acceptance in accordance with 2.a.  The Legal
					Department would be approving any such changes and will notify the NextGen
					managers of the need to activate a “new” acceptance.</span></p>
					
					<p class=MsoNormal style='margin-left:.75in'><span style='font-size:10.0pt;
					line-height:115%'>c.     On the order submission page, we’d recommend that
					functionality be available to enable a link to the Sales Terms above the order
					submission button.  Language above the submission button would read as follows:</span></p>
					
					<p class=MsoListParagraph style='margin-top:0in;margin-right:.5in;margin-bottom:
					10.0pt;margin-left:1.5in'><span style='font-size:10.0pt;line-height:115%'>“The
					order being submitting is subject to the Sales Terms set forth at [insert
					link].”</span> </p>
					
					</div>

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
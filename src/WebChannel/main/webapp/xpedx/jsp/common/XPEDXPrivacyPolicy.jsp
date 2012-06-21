<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<s:set name='isGuestUser' value="wCContext.guestUser" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
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

<s:if test="#isGuestUser == true">
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
</s:if>


<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

<!-- jQuery -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

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
									src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif">Print Page
							</span>
							</a>
						</div>
						<div class="padding-top3 black">
							<div class="page-title"><s:text name="MSG.SWC.MISC.PRPY.GENERIC.PGTITLE" /></div>
						</div>

					</div>
					<%-- Old Privacy Policy deleted per xpedx's request --%>
					<br/>
					<br/>
					<div>
						<span class="bold">PLEASE NOTE</span>: Our Privacy Policy has changed effective November 22, 2011. If you submitted personally identifiable information to us prior to the above effective date, and desire to opt out of having that previously submitted personally identifiable information from being treated under the new policy, please contact us at <a href="mailto:distribution.webmaster@ipaper.com">distribution.webmaster@ipaper.com</a>.<br/><br/>

						<div align="center">
						<span class="bold">PRIVACY POLICY</span><br/>
						(<span style="font-style:italic">Effective and last revised November 22, 2011</span>)
						</div><br/><br/>
						
						Welcome to <a href="http://www.xpedx.com" class="underlink">www.xpedx.com</a> ("Website"), an interactive online service operated by xpedx (“us”, “we”, or “our”), a division of International Paper Company. Please read the following to learn more about our Privacy Policy, which includes compliance with California Privacy Rights (see also <a href="http://www.privacyprotection.ca.gov" class="underlink">http://www.privacyprotection.ca.gov</a>). <br/><br/>
						We respect your privacy and are committed to protecting personally identifiable information you may provide us through our Website. We have adopted this Privacy Policy ("Privacy Policy") to explain what information may be collected on our Website, how we use this information, and under what circumstances we may disclose the information to third parties. This Privacy Policy applies only to information we collect through our Website and does not apply to our collection of information from other sources.<br/><br/> 
						This Privacy Policy, together with the Terms of Access posted on our Website, sets forth the general rules and policies governing your use of our Website.  Depending on your activities when visiting our Website, you may be required to agree to additional terms and conditions.<br/><br/>  
						We generally keep this Privacy Policy posted on our Website and you should review it frequently, as it may change from time to time without notice. Any changes will be effective immediately upon the posting of the revised Privacy Policy.  WHEN YOU ACCESS OUR WEBSITE, YOU AGREE TO THIS PRIVACY POLICY.  IF YOU DO NOT AGREE TO THIS PRIVACY POLICY, OR TO ANY CHANGES WE MAY SUBSEQUENTLY MAKE, IMMEDIATELY STOP ACCESSING OUR WEBSITE.  If, at any point, we decide to use your personally identifiable information in a manner materially different than what was stated at the time it was collected, we will notify you of this change by e-mail to the last e-mail address provided to us. You will have a choice (by means of an “opt out” opportunity) as to whether or not we use their information in this different manner. We will use information in accordance with the Privacy Policy under which your information was collected if you opt out of the new policy.<br/><br/> 
						<span class="bold">A.	INFORMATION WE COLLECT </span><br/><br/>
						Our Website typically collects two kinds of information about you: (a) information that you provide that personally identifies you; and (b) information that does not personally identify you, that we automatically collect when you visit our Website or that you provide us. <br/><br/>
						<span class="bold">(1)  Personally Identifiable Information</span>:  Our definition of personally identifiable information includes any information that may be used to specifically identify or contact you, such as your name, mail address, phone number, etc.  As a general policy, we do not automatically collect your personally identifiable information when you visit our Website.  In certain circumstances, we may request, allow or otherwise provide you an opportunity to submit your personally identifiable information in connection with a feature, program, promotion or some other aspect of our Website.  For instance, you may: (a) provide your name, mail/shipping address, email address, credit card number and phone number when registering with our Website or using our online store; (b) provide certain demographic information about you (e.g., age, gender, purchase preference, usage frequency, etc.) when participating in a survey or poll; or (c) post a general comment and/or recommendation on our Website.  Whether or not you provide this information is your choice; however, in many instances this type of information is required to participate in the particular activity, realize a benefit we may offer, or gain access to certain content on our Website.<br/><br/>  
						<span class="bold">(2)  Non-Personally Indentifiable Information</span>:  Our definition of non-personally identifiable information is any information that does not personally identify you.  Non-personally identifiable information can include certain personally identifiable information that has been de-identified; that is, information that has been rendered anonymous.  We obtain non-personally identifiable information about you from information that you provide us, either separately or together with your personally identifiable information.  We also automatically collect certain non-personally identifiable information from you when you access our Website.  This information can include, among other things, IP addresses, the type of browser you are using (e.g., Internet Explorer, Firefox, Safari, etc.), the third party website from which your visit originated, the operating system you are using (e.g., Vista, Windows XP, Mac OS, etc.), the domain name of your Internet service provider (e.g., America Online, NetZero, etc.), the search terms you use on our Website, the specific web pages you visit, and the duration of your visits.<br/><br/> 
						<span class="bold">B.	HOW WE USE & SHARE THE INFORMATION COLLECTED</span> <br/><br/>
						<span class="bold">(1)  Personally Identifiable Information</span>: The personally identifiable information you submit to us is generally used to carry out your requests, respond to your inquiries, better serve you, or in other ways naturally associated with the circumstances in which you provided the information. We may also use this information to later contact you for a variety of reasons, including for customer service purposes and/or to provide you promotional information for our products.  We may use third parties to carry out the foregoing and other business functions and, in such instances, these third parties may use your information for these limited purposes.<br/><br/>  
						In some instances we may share your personally identifiable information with our co-promotional partners and others with whom we have marketing relationships. Except as provided in this Privacy Policy, our Terms of Access, or as disclosed when you submit the information, your personally identifiable information will not be shared or sold to any third parties without your prior approval. Further, you may opt-out from receiving future promotional information from us as set forth below.  <br/><br/>
						<span class="bold">(2)  Non-Personally Identifiable Information</span>: We use non-personally identifiable information in a variety of ways, including to help analyze site traffic, understand customer needs and trends, carry out targeted promotional activities, and to improve our services. We may use your non-personally identifiable information by itself or aggregate it with information we have obtained from others. We may share your non-personally identifiable information with our affiliated companies and third parties to achieve these objectives and others, but remember that aggregate information is anonymous information that does not personally identify you.<br/><br/> 
						<span class="bold">C.	COOKIES AND PREFERENCE BASED ADVERTISING </span><br/><br/>
						<span class="bold">(1) Cookies and Web Beacons </span>: We automatically receive and store certain types of non-personally identifiable information whenever you interact with us. For example, like many websites, we use "cookies" and "web beacons" (also called “clear gifs” or “pixel tags”) to obtain certain types of information when your web browser accesses our Website.  "Cookies" are small files that we transfer to your computer's hard drive or your Web browser memory to enable our systems to recognize your browser and to provide convenience and other features to you, such as recognizing you as a frequent user of our Website. "Web beacons" are tiny graphics with a unique identifier, similar in function to cookies, and may be used to track the online movements of users, when an email has been opened, and to provide other information.<br/><br/> 
						Examples of the information we collect and analyze in this manner include the Internet protocol (IP) address used to connect your computer to the Internet; computer and connection information such as browser type and version, operating system, and platform; your behavior on our Website, including the URL you come from and go to next (whether this URL is on our site or not); and cookie number.  It is important to note that the cookies and Web beacons that we use do not contain and are not tied to personally identifiable information.<br/><br/> 
						If you are concerned about the storage and use of cookies, you may be able to direct your internet browser to notify you and seek approval whenever a cookie is being sent to your Web browser or hard drive. You may also delete a cookie manually from your hard drive through your internet browser or other programs. Please note, however, that some parts of our Website will not function properly or be available to you if you refuse to accept a cookie or choose to disable the acceptance of cookies. <br/><br/>
						<span class="bold">(2)  Preference Based Advertising </span>: We may now or in the future work with third parties, including third party advertising networks and website analysis firms, who use cookies and web beacons (also called “clear gifs” and “pixel tags”) to collect non-personally identifiable information when you visit our Website and third party sites. This non-personally identifiable information, collected through cookies and web beacons, is often used by these advertising networks to serve you with advertisements, while on third party sites, tailored to meet your preferences and needs.  For more information on how this type of advertising works, go to <a href="http://www.aboutads.info/consumers" class="underlink">www.aboutads.info/consumers</a>.<br/><br/>  
						If you do not wish to be served third party advertising via these third party advertising networks, please to <a href="http://www.aboutads.info/choices" class="underlink">www.aboutads.info/choices</a> and follow the simple opt-out process. A couple of important notes about this opt-out tool: (1) it includes all the advertising networks that we may work with, but also many that we do not work with; and (2) it may rely on cookies to ensure that a given advertising network does not collect information about you (“Opt-out Cookies”).  Therefore, if you buy a new computer, change web browsers or delete these Opt-out Cookies from your computer, you will need to perform the opt-out task again.<br/><br/>
						<span class="bold">D.	OTHER USES & INFORMATION  </span><br/><br/>
						<span class="bold">(1)  IP Addresses </span>: An IP address is a number that is automatically assigned to your computer whenever you are surfing the Internet. Web servers (computers that "serve up" web pages) automatically identify your computer by its IP address. When visitors request pages from our Website, our servers typically log their IP addresses. We collect IP addresses for purposes of system administration, to report non-personal aggregate information to others, and to track the use of our Website.  IP addresses are considered non-personally identifiable information and may also be shared as provided above.  It is not our practice to link IP addresses to anything personally identifiable; that is, the visitor's session will be logged, but the visitor remains anonymous to us. However, we reserve the right to use IP addresses to identify a visitor when we feel it is necessary to enforce compliance with our Website rules or to: (a) fulfill a government request; (b) conform with the requirements of the law or legal process; (c) protect or defend our legal rights or property, our Website, or other users; or (d) in an emergency to protect the health and safety of our Website’s users or the general public. <br/><br/>
						<span class="bold">(2) Email Communications </span>:  If you send us an email with questions or comments, we may use your personally identifiable information to respond to your questions or comments, and we may save your questions or comments for future reference.  For security reasons, we do not recommend that you send non-public personal information, such as passwords, social security numbers, or bank account information, to us by email.  However, aside from our reply to such an email, it is not our standard practice to send you email unless you request a particular service or sign up for a feature that involves email communications, it relates to purchases you have made with us (e.g., product updates, customer support, etc.), we are sending you information about our other products and services, or you consented to being contacted by email for a particular purpose. In certain instances, we may provide you with the option to set your preferences for receiving email communications from us; that is, agree to some communications but not others. You may "opt out" of receiving future commercial email communications from us by clicking the "unsubscribe" link included at the bottom of most emails we send, or as provided below; provided, however, we reserve the right to send you transactional emails such as customer service communications.<br/><br/> 
						<span class="bold">(3) Transfer of Assets </span>:  As we continue to develop our business, we may sell or purchase assets. If another entity acquires us or all (or substantially all) of our assets, the personally identifiable information and non-personally identifiable information we have about you will be transferred to and used by this acquiring entity, though we will take reasonable steps to ensure that your preferences are followed. Also, if any bankruptcy or reorganization proceeding is brought by or against us, all such information may be considered an asset of ours and as such may be sold or transferred to third parties. <br/><br/>
						<span class="bold">(4) Other </span>: Notwithstanding anything herein to the contrary, we reserve the right to disclose any personally identifiable or non-personally identifiable information about you if we are required to do so by law, with respect to copyright and other intellectual property infringement claims, or if we believe that such action is necessary to: (a) fulfill a government request; (b) conform with the requirements of the law or legal process; (c) protect or defend our legal rights or property, our Website, or other users; or (d) in an emergency to protect the health and safety of our Website's users or the general public. <br/><br/>
						<span class="bold">(5) Your California Privacy Rights </span>:  Residents of the State of California, under certain provisions of the California Civil Code, have the right to request from companies conducting business in California a list of all third parties to which the company has disclosed certain personally identifiable information as defined under California law during the preceding year for third party direct marketing purposes. You are limited to one request per calendar year. In your request, please attest to the fact that you are a California resident and provide a current California address for our response. You may request the information in writing as provided in Section I below.<br/><br/>
						<span class="bold">E.	PUBLIC FORUMS  </span><br/><br/>
						We may offer chat rooms, blogs, message boards, bulletin boards, or similar public forums where you and other users of our Website can communicate. The protections described in this Privacy Policy do not apply when you provide information (including personal information) in connection with your use of these public forums.  We may use personally identifiable and non-personally identifiable information about you to identify you with a posting in a public forum.  Any information you share in a public forum is public information and may be seen or collected by anyone, including third parties that do not adhere to our Privacy Policy. We are not responsible for events arising from the distribution of any information you choose to publicly post or share through our Website.<br/><br/> 
						<span class="bold">F.	CHILDREN  </span><br/><br/>
						The features, programs, promotions and other aspects of our Website requiring the submission of personally identifiable information are not intended for children.  We do not knowingly collect personally identifiable information from children under the age of 13.  If you are a parent or guardian of a child under the age of 13 and believe he or she has disclosed personally identifiable information to us please contact as provided in Section I below. A parent or guardian of a child under the age of 13 may review and request deletion of such child's personally identifiable information as well as prohibit the use thereof.<br/><br/> 
						<span class="bold">G.	KEEPING YOUR INFORMATION SECURE  </span><br/><br/>
						We have implemented security measures we consider reasonable and appropriate to protect against the loss, misuse and alteration of the information under our control.  Please be advised, however, that while we strive to protect your personally identifiable information and privacy, we cannot guarantee or warrant the security of any information you disclose or transmit to us online and are not responsible for the theft, destruction, or inadvertent disclosure of your personally identifiable information. In the unfortunate event that your "personally identifiable information" (as the term or similar terms are defined by any applicable law requiring notice upon a security breach) is compromised, we may notify you by e-mail (at our sole and absolute discretion) to the last e-mail address you have provided us in the most expedient time reasonable under the circumstances; provided, however, delays in notification may occur while we take necessary measures to determine the scope of the breach and restore reasonable integrity to the system as well as for the legitimate needs of law enforcement if notification would impede a criminal investigation. From time to time we evaluate new technology for protecting information, and when appropriate, we upgrade our information security systems.<br/><br/> 
						<span class="bold">H.	OTHER SITES/LINKS  </span><br/><br/>
						Our Website may link to or contain links to other third party websites that we do not control or maintain, such as in connection with purchasing products referenced on our Website and banner advertisements. We are not responsible for the privacy practices employed by any third party website. We encourage you to note when you leave our Website and to read the privacy statements of all third party websites before submitting any personally identifiable information.<br/><br/> 
						<span class="bold">I.	CONTACT & OPT-OUT INFORMATION  </span><br/><br/>
						You may contact us as at <a href="mailto:distribution.webmaster@ipaper.com" class="underlink">distribution.webmaster@ipaper.com</a> if: (a) you have questions or comments about our Privacy Policy; (b) wish to make corrections to any personally identifiable information you have provided us; (c) want to opt-out from receiving future commercial correspondence from us; or (d) wish to withdraw your consent to our sharing of your personally identifiable information with relevant co-promotional partners.  Please note that using the above process to opt-out of receiving commercial correspondence from us will not necessarily opt you out of receiving correspondence from International Paper Company or its other divisions/affiliated entities (“Related Entities”) to the extent you have given your information (e.g., email address) to any such Related Entity.  For information on how to opt-out of communications from Related Entities, please see the Privacy Policy/Statement at <a href="http://www.internationalpaper.com" class="underlink">www.internationalpaper.com</a>.<br/><br/> 
						We will respond to your request and, if applicable and appropriate, make the requested change in our active databases as soon as reasonably practicable.  Please note that we may not be able to fulfill certain requests while allowing you access to certain benefits and features of our Website.  <br/><br/>
						<span class="bold">J.	SOLE STATEMENT </span><br/><br/>
						This Privacy Policy as posted on this Website is the sole statement of our privacy policy with respect to this Website, and no summary, modification, restatement or other version thereof, or other privacy statement or policy, in any form, is valid unless we post a new or revised policy to the Website.<br/><br/>
						
											
					
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
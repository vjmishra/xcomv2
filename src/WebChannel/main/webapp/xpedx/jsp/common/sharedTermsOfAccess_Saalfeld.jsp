<%--
	DRY TOA and privacy policy. This page contains this brand's TOA for re-use across multiple pages (XPEDXTermsOfAccess.jsp and XPEDTermsOfAccessModal.jsp).
	This allows it to be shared by both modal and non-modal pages. Note the inclusion of pp_brand.jsp in middle of content so this brand's privacy policy is re-used.
--%>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>

<c:choose>
	<c:when test="${param.modal == 'true'}">
		<s:url id='privacyPolicyLink' action='MyPrivacyPolicyModal' namespace='/common' />
	</c:when>
	<c:otherwise>
		<s:url id='privacyPolicyLink' action='MyPrivacyPolicy' namespace='/common' />
	</c:otherwise>
</c:choose>

					<table class="full-widths">
						<tbody>
							<tr>
								<td style='text-align:center'>
									<span class="bold">TERMS OF ACCESS</span>
									<br/>
									(Effective & last revised November 22, 2011)<br/><br/>
								</td>
							</tr>
							<tr>
								<td>
									1.	INTRODUCTION AND ACCEPTANCE<br/><br/>
									Welcome to www.saalfeldredistribution.com (&quot;Website&quot;), an interactive online service operated by Saalfeld (&quot;Company,&quot; &quot;us,&quot; &quot;we,&quot; or &quot;our&quot;), a division of International Paper Company.
									<br/><br/>
									PLEASE READ THESE TERMS OF ACCESS CAREFULLY BEFORE USING THE WEBSITE.  BY ACCESSING AND/OR USING THE WEBSITE (OTHER THAN TO READ THESE TERMS OF ACCESS FOR THE FIRST TIME) YOU ARE AGREEING TO COMPLY WITH THESE TERMS 
									OF ACCESS, WHICH MAY CHANGE FROM TIME TO TIME AS SET FORTH IN SECTION 15 BELOW.  IF YOU DO NOT AGREE TO BE BOUND BY THESE TERMS OF ACCESS, DO NOT ACCESS OR USE THE WEBSITE.  
									<br/><br/>
								</td>
							</tr>
							<tr>
								<td>
									In addition to these Terms of Access, we have established a Privacy Policy to explain how we collect and use information about you.  A copy of this Privacy Policy can be found here: <a href="<s:property value='#privacyPolicyLink' />" target="_blank" class="underlink">Privacy Policy</a> and is incorporated by reference into these Terms of Access.  By accessing or using the Website, you are also signifying your acknowledgement and agreement to our Privacy Policy.<br/><br/>
									Finally, the Saalfeld Conditions of Sale, as in effect from time to time, are available on the Website.  Placement of an order for products on our Website shall be deemed acceptance of the Conditions of Sale.<br/><br/>
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
								</td>
							</tr>
							
							<jsp:include page="../common/sharedPrivacyPolicy_Saalfeld.jsp" />
							
							<tr>
								<td style='text-align:center'>
									<span class="bold">Saalfeld Conditions of Sale &ndash; www.saalfeldredistribution.com</span>
									<br/>
									<br/>
								</td>
							</tr>
							<tr>
								<td>
									<b>1.	ACCEPTANCE OF ORDERS -</b> Any acceptance herein of Buyer's Order by Seller is expressly made conditional on Buyer's assent to the additional or different terms contained herein.  Orders are accepted only at Seller's Offices and are final and cannot be changed or canceled upon acceptance.<br/><br/>
									<b>2.	PRICE -</b>The price for the material covered hereby shall be adjusted to Seller's published price or contract pricing in effect at time of shipment.<br/><br/> 
									<b>3.	DELIVERY -</b> F.O.B. points, freight allowances and other transportation arrangements applicable to this sale shall be as set forth in Seller's published sales policy statements.  Seller will not consider claims for errors, damages or shortages in shipments unless transmitted to Seller within thirty (30) days of the date of shipment and accompanied by documents sufficient to substantiate the claim.<br/><br/>
									<b>4.	TERMS OF PAYMENT - </b>Terms of payment shall be in accordance with Seller's invoice or applicable published sales policy statements.  If Seller's invoice contains a prompt payment discount, Buyer must deduct such discount amount from its timely payment of the invoice or be forever barred from claiming such discount.	<br/><br/>
									<b>5.	CANCELLATION -</b> The order hereby acknowledged cannot be canceled except with Seller's consent and upon terms that will indemnify Seller against loss.<br/><br/>	
									<b>6.	WARRANTY -</b> Seller warrants to Buyer that the material to be delivered hereunder will be free from defects in material, workmanship and title and will be of the kind and quality described in the contract.  <u>THE FOREGOING WARRANTY IS EXCLUSIVE AND IN LIEU OF ALL OTHER WARRANTIES, WHETHER WRITTEN, ORAL OR IMPLIED (INCLUDING ANY WARRANTY OF MERCHANTABILITY OR FITNESS FOR PURPOSE).</u>  Claims for defects in materials will not be considered unless made in writing within ninety (90) days of receipt of material, and unless Buyer promptly discontinues use of such material.  Material proving defective in the hands of Buyer, when used for purposes for which such material is intended, will be replaced or credit will be allowed for the price thereof at Seller's option.	<br/><br/>
									<b>7.	LIMITATION OF LIABILITY -</b> Seller's liability on any claim of any kind, whether based on negligence, warranty or otherwise, for any loss or damage arising out of, connected with, or resulting from this contract, or from the performance or breach thereof, or from the manufacture, sale, delivery, resale or use of any material or provision of any services covered by or furnished pursuant to this document shall, in no case, exceed the price allocable to the material which gives rise to the claim.  In no event shall Seller be liable for special, incidental or consequential damages. Seller shall not be liable for delay or failure to perform due to causes beyond Seller's control.	<br/><br/>
									<b>8.	COMPLIANCE WITH LAWS - </b>  The parties agree to comply with all applicable laws and regulations regarding employment and discrimination, including Executive Order 11246. The OFCCP affirmative action clauses set forth in 41 CFR &sect;60-1.4, 60-250.4, 60-741.5(a) are hereby incorporated by reference.<br/><br/>	
									<b>9.	TAXES -</b> Any tax, duty, or other governmental charge now or hereafter levied upon the production, sale, use or shipment of materials ordered or sold hereunder shall be for the Buyer's account.  Such governmental levies are not covered in the Seller's price, unless expressly so stated.<br/><br/>	
									<b>10.	NON-WAIVER -</b> Seller's waiver of any of these Conditions of Sale in any instance shall be limited to that instance and shall not imply that Seller will waive such Condition of Sale on any future occasion.<br/><br/><br/>
									
									<c:if test="${param.modal == 'true'}">
										You may enter the site only if you acknowledge acceptance of the Terms of Access, Privacy Policy and Saalfeld Conditions of Sale, by clicking &quot;Accept&quot; below:<br/><br/>
									</c:if>
								</td>
							</tr>
							<c:if test="${param.modal == 'true'}">
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
										</ul>
									</td>
								</tr>
								<tr>
									<td colspan="2" valign="top" class=" no-border-right-user">&nbsp;</td>
								</tr>
							</c:if>
						</tbody>
					</table>

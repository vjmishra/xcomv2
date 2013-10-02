<%@ taglib prefix="s" uri="/struts-tags" %>

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
							</div>

						</div>
						<br/>
						<br/>
						
						<table class="full-widths">
							<tbody>
								<jsp:include page="../common/pp_xpedx.jsp" />
							</tbody>
						</table>

					</div>
					<!-- End Pricing -->
					<br />
				</div>

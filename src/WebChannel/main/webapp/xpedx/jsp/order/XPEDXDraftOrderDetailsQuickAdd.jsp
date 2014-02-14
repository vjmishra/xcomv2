<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<s:bean
	name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils'
	id='wcUtil' />

<!-- NEW Page for Quick Add EB 4367 -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />

<%
	request.setAttribute("isMergedCSSJS", "true");
%>
<s:url id='uomDescriptionURL' namespace="/common"
	action='getUOMDescription' />
<s:bean
	name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils'
	id='xpedxSCXmlUtil' />
<s:bean
	name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean"
	id="xpedxUtilBean" />

<!-- begin styles. These should be the only three styles. -->
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/sfskin-xpedx<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css"
	media="screen" />
<!--  End Styles -->

<!-- javascript -->
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts js   -->
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<s:include value="../order/XPEDXRefreshMiniCart.jsp" />
<!-- STUFF YOU NEED FOR BEAUTYTIPS -->

<s:hidden name="uomDescriptionURL" value='%{#uomDescriptionURL}' />
<!-- /STUFF -->
<body>

	<div id="main-container">
		<div id="main">

			<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
			<hr color="#dbe2f1" />
			<hr color="#dbe2f1" />
			<!----close_mid-col-mil------>
			<div class="container">
				<div id="mid-col-mil">
					<!--*************SELECT ITEM TYPE***************-->
					<div class="page-title addmarginbottom15">Quick Add</div>
					<div class="qa-rightcol">
						<p>Select Item Type</p>
					</div>
					<form>
						<select name="itemtype">
							<option value="">xpedx Item #</option>
							<option value="">My Item #</option>
						</select>
					</form>
					<div class="clearfix"></div>
					<hr size="1" color="#cdcdcd" class=" addmargintop20" />

					<!--*************ADD ITEMS LIST***************-->
					<h3 class="addmargintop10 qa-subhead">Add Item List</h3>

					<form class="pushleft addpadtop10">
						<div class=" float-right clearboth">
							<input name="input" type="button" class="btn-gradient"
								value="Add to Cart" />
						</div>
						<div class="qa-row-wrap">
							<div class="qa-listrow">
								<label>
									<div>Item Number</div> <input type="text" size="20"
									class="inputfloat error" />
								</label> <label>
									<div>Qty.</div> <input type="text" size="8" class="inputfloat" />
								</label> <label>
									<div>Number</div> <input type="text" size="20"
									class="inputfloat" />
								</label> <label>
									<div>Number</div> <input type="text" size="20"
									class="inputfloat" />
									<div class="error">Error: Invalid item number. Please try
										again.</div>
								</label>
							</div>
							<!-----close_row------>
							<div class="qa-listrow">
								<label><input type="text" size="20" class="inputfloat" /></label>
								<label><input type="text" size="8" class="inputfloat" /></label>
								<label><input type="text" size="20" class="inputfloat" /></label>
								<label><input type="text" size="20" class="inputfloat" /></label>
							</div>
							<!-----close_row------>

							<div class="qa-listrow">
								<label><input type="text" size="20" class="inputfloat" /></label>
								<label><input type="text" size="8" class="inputfloat" /></label>
								<label><input type="text" size="20" class="inputfloat" /></label>
								<label><input type="text" size="20" class="inputfloat" /></label>
							</div>
							<!-----close_row------>

							<div class="qa-listrow">
								<label><input type="text" size="20" class="inputfloat" /></label>
								<label><input type="text" size="8" class="inputfloat" /></label>
								<label><input type="text" size="20" class="inputfloat" /></label>
								<label><input type="text" size="20" class="inputfloat" /></label>
							</div>
							<!-----close_row------>

							<div class="qa-listrow">
								<label><input type="text" size="20" class="inputfloat" /></label>
								<label><input type="text" size="8" class="inputfloat" /></label>
								<label><input type="text" size="20" class="inputfloat" /></label>
								<label><input type="text" size="20" class="inputfloat" /></label>
							</div>
							<!-----close_row------>

							<div class="qa-listrow">
								<label><input type="text" size="20" class="inputfloat" /></label>
								<label><input type="text" size="8" class="inputfloat" /></label>
								<label><input type="text" size="20" class="inputfloat" /></label>
								<label><input type="text" size="20" class="inputfloat" /></label>
							</div>
							<!-----close_row------>

						</div>
						<!----close row-wrap----->

						<div class=" float-right clearboth">
							<input name="input" type="button" class="btn-gradient"
								value="Add to Cart" />
						</div>
					</form>
					<div class="clearfix"></div>
					<hr size="1" color="#cdcdcd" class=" addmargintop20" />

					<!--*************COPY PASTE***************-->
					<h3 class="addmargintop10 qa-subhead">Copy and Paste</h3>
					<div class="qa-copy-instructions">
						<p class="qa-psmall">
							Paste or type the
							<xpedx> item numbers or customer item numbers in the
							following format. 
						</p>
						<p class="qa-plarge addmargintop12">
							<strong>Item Number,Quantity</strong> (no spaces)
						</p>
						<p class="qa-psmall addmargintop20">Examples:</p>
						<p class="qa-plarge">
							<strong>50052121,12</strong> (item with quantity)
						</p>
						<p class="qa-plarge">
							<strong>50052121</strong> (item without quantity)
						</p>
					</div>

					<form class="pushleft">
						<div class="qa-copywrap">
							<textarea name="textarea" rows="10" class="qa-copypaste"></textarea>
							<div class="qa-itemlimit">Item Limit 200</div>


							<div class="floatright">
								<input name="button" type="button" class="btn-neutral"
									value="Clear" />
							</div>
						</div>
						<div class="clearfix"></div>

						<input name="input" type="button" class="btn-gradient float-right"
							value="Add to List" />
					</form>

				</div>
				<!-----close mil------>
			</div>
		</div>
		<!----close-container----->

	</div>

	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />

</body>
</html>

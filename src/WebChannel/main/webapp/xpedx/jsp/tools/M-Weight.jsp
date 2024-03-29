<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!-- Webtrend tag starts -->
<meta name="WT.ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.mweight.title" />'>
<meta name="DCSext.w_x_tools_ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.mweight.title" />'>

<!-- Webtrend tag stops --> 
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES<s:property value='#wcUtil.xpedxBuildKey' />.css" />

 <!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->

     
    
<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/m-Weights<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

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

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript">
	
</script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/backlink<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.mweight.title" /></title>
</head>
<body class="ext-gecko ext-gecko3">
    <div id="main-container">
        <div id="main">

        	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
			<div class="container content-container">
			<p class="addmarginbottom15"><a class="back-resources">‹ Back</a></p>
				<h1>M Weight Calculation</h1>
					<div style="width: 600px;">


						<!-- Add comment for jira 1538 -->
						<p style="width: 600px;">To calculate the M weight of a
							particular size, enter the length, width and basis weight.Then
							Enter the basic size for the table provided.The tool will then
							provide the total weight per 1,000 shts of the particular size.</p>
						<br />
						<p style="width: 600px;">The ream weight is the weight of 500
							sheets; the M weight is the weight of 1,000 sheets. The M weight
							can be obtained by first finding the ream weight, rounding it by
							the rounding rules, then multiplying by 2.</p>
						<br />
						<!--  <h2>  M Weight Calculation</h2> Changes done for jira 1538-->



						<div id="requestform">
							<div class="clearview">&nbsp;</div>


							<form name="eform" method="post" action="sc_PapCalcmweights.aspx"
								id="eform" class="formborder">

								<table style="width: 600px;" class="form">
									<tbody>

										<tr>
											<td>Given Size (Length):</td>
											<td colspan="3"><input name="hSize" type="text"
												style="width: 80px;"
												onkeyup="calcPhone('Value',eform.hSize);"
												class="x-input right-align" id="hSize" /></td>
										</tr>
										<tr>
											<td>Given Size (Width):</td>
											<td colspan="3"><input name="wSize" type="text"
												style="width: 80px;" class="x-input right-align" id="wSize"
												onkeyup="calcPhone('Value',eform.wSize);" /></td>
										</tr>
										<tr>
											<td>Basis Weight:</td>
											<td colspan="3"><span class="no-border-left"> <input
													type="hidden" id="txtstran" name="txtstran" />
											</span> <input name="bWeight" type="text" style="width: 80px;"
												class="x-input right-align" id="bWeight"
												onkeyup="calcPhone('Value',eform.bWeight);" /></td>
										</tr>
										<tr>
											<td>Basic Size (Length X Width):&nbsp;</td>
											<td width="98" class="padding8"><input name="hArea"
												type="text" style="width: 80px;" class="x-input right-align"
												id="hArea" onkeyup="calcPhone('Value',eform.hArea);" /></td>
											<td width="17" class="padding8">X</td>
											<td width="260" class="padding8"><input name="wArea"
												type="text" style="width: 80px;" class="x-input right-align"
												id="wArea" onkeyup="calcPhone('Value',eform.wArea);" /></td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td colspan="3">
												<!-- <ul id="cart-actions"><li><a href="#"onClick="javascript:validateForm()" class="green-ui-btn"><span>Calculate</span></a></li><li><a class="grey-ui-btn" href="#"><span>Clear</span></a></li> -->
												<!-- Changes done for jira 1538 -->
												<ul id="cart-actions" style="height: auto;">
													<li><input class="btn-neutral" type="button" href="#"
														onClick="javascript:document.eform.reset()" value="Clear" /></li>
													<li><input href="#"
														onClick="javascript:validateForm()" class="btn-gradient"
														type="button" value="Calculate" /></li>
												</ul>
											</td>
										</tr>
										<tr>
											<td width="205"><strong>M Weight:</strong></td>
											<td colspan="3"><input readonly="readonly" name="Answer"
												style="border: none; color: #F00; font-weight: bold;"
												size="5"/> lbs.</td>
										</tr>

									</tbody>
								</table>
							</form>

							<div class="clearview">&nbsp;</div>

						</div>
						<div class="clearview">
							<h2>Specifications</h2>
						</div>
						<div class="clearview">&nbsp;</div>
						<table style="width: 100%;" class="standard-table">
								<tr>
									<th width="195">Rounding Rules</th>
									<th width="393"> </th>
								</tr>
								<tbody>
								<tr class="odd2">
									<td>Sheet Size</td>
									<td valign="top">Ream Weights</td>
								</tr>
								<tr class="odd">
									<td>864 inches&sup2; or larger</td>
									<td valign="top">Ream Weight calculated to nearest pound.
										(166 lbs.)</td>
								</tr>
								<tr>
									<td>336 inches&sup2; to 864 inches&sup2;</td>
									<td valign="top">Ream Weight calculated to nearest half
										pound. (166.5 lbs.)</td>
								</tr>
								<tr class="odd">
									<td valign="top">Less than 336 inches&sup2;</td>
									<td valign="top">Ream Weight calculated to nearest one
										hundredth of a pound. (166.55 lbs.)</td>
								</tr>
							</tbody>
						</table>
						<div style="width: 100%;" id="table-bottom-bar">
							<div id="table-bottom-bar-L"></div>
							<div id="table-bottom-bar-R"></div>
						</div>
						<div class="clearview">&nbsp;</div>
						<table style="width: 100%;" class="standard-table">
							
								<tr>
									<th width="199">Basic Sizes</th>
									<th colspan="2"></th>
								</tr>
								<tbody>
								<tr class="odd">
									<td>Bond</td>
									<td width="116" valign="top">17 x 22</td>
									<td width="269" valign="top">= 374 Square Inches</td>
								</tr>
								<tr>
									<td>Book and Offset</td>
									<td valign="top">25 x 38</td>
									<td width="269" valign="top">= 950 Square Inches</td>
								</tr>
								<tr class="odd">
									<td>Tag</td>
									<td valign="top">24 x 36</td>
									<td width="269" valign="top">= 864 Square Inches</td>
								</tr>
								<tr>
									<td>Index</td>
									<td valign="top">25.5 x 30.5</td>
									<td width="269" valign="top">= 778 Square Inches</td>
								</tr>
								<tr class="odd">
									<td>Vellum Bristol</td>
									<td valign="top">22.5 x 30.5</td>
									<td width="269" valign="top">= 686 Square Inches</td>
								</tr>
								<tr>
									<td>Coated Cover 1S/C2S</td>
									<td valign="top">24 x 36</td>
									<td width="269" valign="top">= 864 Square Inches</td>
								</tr>
								<tr class="odd">
									<td>Coated Cover C2S</td>
									<td valign="top">20 x 26</td>
									<td width="269" valign="top">= 520 Square Inches</td>
								</tr>
								<tr>
									<td>C1S Label</td>
									<td valign="top">25 x 38</td>
									<td width="269" valign="top">= 950 Square Inches</td>
								</tr>
								<tr class="odd">
									<td>Opaque Cover</td>
									<td valign="top">20 x 26</td>
									<td width="269" valign="top">= 520 Square Inches</td>
								</tr>
							</tbody>
						</table>
						<div style="width: 100%;" id="table-bottom-bar">
							<div id="table-bottom-bar-L"></div>
							<div id="table-bottom-bar-R"></div>
						</div>
						<div class="clearview">&nbsp;</div>



						<p class="addpadtop20">
								<strong>Note:</strong>: The results of the
								Interactive Calculations System are estimates and are not
								guaranteed by xpedx, LLC.
							</p>
					</div>
			</div>
			<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	<!-- end main  -->
		<!-- end container  -->
    </div></div>
</body>
</html>
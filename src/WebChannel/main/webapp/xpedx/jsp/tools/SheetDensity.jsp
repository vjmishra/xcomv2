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
<meta name="WT.ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.sheetdensity.title" />'>
<meta name="DCSext.w_x_tools_ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.sheetdensity.title" />'>
<!-- Webtrend tag stops --> 
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->


<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
     
    


<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

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
<!-- /STUFF -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"> </script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sheetdensity<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"> </script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/backlink<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.sheetdensity.title" /></title>
</head>
<body class="ext-gecko ext-gecko3">
    <div id="main-container">
        <div id="main">

        	 <s:action name="xpedxHeader" executeResult="true" namespace="/common" />
			<div class="container content-container">
			<p class="addmarginbottom15"><a class="back-resources">‹ Back</a></p>
				<h1>Sheet Density</h1>
					<div style="width: 600px;">

						<p>To determine the density of any given sheet, divide the
							basis weight by the caliper.</p>
						<!-- Changes done for jira 1538 -->
						<p class="addpadtop10"><strong>Note</strong>: <br /> Caliper is expressed in the formula
						as a whole number.</p>

						<div id="requestform addpadtop20">


							<form name="eform" method="post"
								action="sc_papcalcoddnumshee.aspx" id="eform">

								<table style="width: 600px;" class="form sheet-density">
									<tbody>

										<tr>
											<td>Basis Weight:</td>
											<td width="438"><input name="bWeight" type="text"
												style="width: 80px;"
												onkeyup="calcPhone('Value',eform.bWeight);" class="x-input"
												id="bWeight" /></td>
										</tr>
										<tr>
											<td>Caliper:</td>
											<td><input name="caliper" type="text"
												style="width: 80px;"
												onkeyup="calcPhone('Value',eform.caliper);" class="x-input"
												id="caliper" /></td>
										</tr>
										<tr>
											<td colspan="2">
												<ul id="cart-actions" style="height: auto;">
													<li><input class="btn-neutral" type="button" href="#"
														onClick="javascript:document.eform.reset()" value="Clear" /></li>
													<li><input href="#"
														onclick="javascript:validateForm();" class="btn-gradient"
														type="button" value="Calculate" /></li>
												</ul>
											</td>
										</tr>
										<tr>
											<td width="150"><strong>Density :</strong></td>
											<td><input readonly="readonly" name="Answer"
												style="border: none; color: #F00; font-weight: bold;"
												size="15">lbs. per point</td>
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

									<th width="318">Formula</th>
									<th colspan="2"></th>
								</tr>
								<tbody>
								<tr>
									<td align="right" class="noBorders-fff" style="border-right: none;"><table
											cellspacing="0" cellpadding="0" class="noBorders-fff">
											<tbody>
												<tr>
													<td valign="middle" align="center"
														style="border: none;" class="noBorders-fff">Basis
														Weight</td>
												</tr>
												<tr>
													<td valign="middle" align="center"
														style="border-left: medium none; border-top: 1px solid #999; border-right: none; border-bottom: none;"
														class="noBorders-fff">Caliper</td>
												</tr>
											</tbody>
										</table></td>
									<td width="18" valign="middle" class="noBorders-fff" style="border-right: none;">=</td>
									<td width="228" valign="middle">Density</td>
								</tr>
							</tbody>
						</table>
						<div style="width: 100%;" id="table-bottom-bar">
							<div id="table-bottom-bar-L"></div>
							<div id="table-bottom-bar-R"></div>
						</div>
						<div class="clearview">&nbsp;</div>
						<table style="width: 100%;" class="standard-table">
							<tbody>
								<tr>

									<th width="318">Example</th>
									<th colspan="2"> </th>
								</tr>
								<tr>
									<td colspan="3" style="border-bottom: none;">Find the density of a sheet of 118lb.
										Carolina C2S Cover paper with caliper of .008</td>
								</tr>

								<tr>
									<td align="right" class="noBorders-fff" style="border-right: none;"><table
											cellspacing="0" cellpadding="0" class="noBorders-fff">
											<tbody>
												<tr>
													<td valign="middle" align="center"
														style="border: none;" class="noBorders-fff"><span
														class="noBorders-fff" style="border-left: medium none;">118</span></td>
												</tr>
												<tr>
													<td valign="middle" align="center"
														style="border-left: medium none; border-top: 1px solid #999; border-right: none; border-bottom: none;"
														class="noBorders-fff">8</td>
												</tr>
											</tbody>
										</table></td>
									<td width="18" valign="middle" class="noBorders-fff" style="border-right: none;">=</td>
									<td width="228" valign="middle">14.75 lbs. per point</td>
								</tr>
							</tbody>
						</table>

						<div style="width: 100%;" id="table-bottom-bar">
							<div id="table-bottom-bar-L"></div>
							<div id="table-bottom-bar-R"></div>
						</div>
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
    </div> </div>
</body>
</html>
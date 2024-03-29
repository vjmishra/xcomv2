<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<s:bean
	name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils"
	id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en"
	xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!-- Webtrend tag starts -->
<meta name="WT.ti"
	content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.eqbasiswtchart.title" />'>
<meta name="DCSext.w_x_tools_ti"
	content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.eqbasiswtchart.title" />'>
<!-- Webtrend tag stops -->
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!-- [endif]-->

<!-- javascript -->
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- jQuery -->
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css"
	media="screen" />

<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts js   -->

<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js"
	type="text/javascript" charset="utf-8"></script>

<script
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js"
	type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js"
	language="javascript">
	
</script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/backlink<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text
		name="tools.eqbasiswtchart.title" /></title>
</head>
<body class="ext-gecko ext-gecko3">
	<div id="main-container">
		<div id="main">

			<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
			<div class="equivalent container content-container">
				<p class="addmarginbottom15"><a class="back-resources">‹ Back</a></p>
				<h1>Equivalent Basis Wts Chart</h1>
					<div style="width: 600px;">

						<p>In reams (500 sheets); basis weights in bold type:</p>

						<table style="width: 100%;" class="standard-table addpadtop20">
								<tr>
									<th width="318" colspan="6">Book</th>
									<th align="left"></th>
								</tr>
								<tbody>
								<tr class="odd2">
									<td style="border-right: none;"><div align="center">
											Book<br /> 25x38
										</div></td>
									<td style="border-right: none;"><div align="center">
											Bond<br /> 17x22
										</div></td>
									<td style="border-right: none;"><div align="center">
											Cover<br /> 20x26
										</div></td>
									<td style="border-right: none;"><div align="center">
											Bristol<br /> 22&frac12;x28&frac12;
										</div></td>
									<td style="border-right: none;"><div align="center">
											Index<br /> 25&frac12;x30&frac12;
										</div></td>
									<td style="border-right: none;"><div align="center">
											Tag<br /> 24x36
										</div></td>
									<td style="border-right: none;"><div align="center">
											Metric<br /> g/m&sup2;
										</div></td>
								</tr>
								<tr class="odd" style="text-align:center;">
									<td style="border-right: none; border-bottom: none;"><div align="center">30</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">12</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">16</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">20</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">25</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">27</div></td>
									<td style="border-bottom: none;"><div align="center">44</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">40</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">16</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">22</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">27</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">33</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">36</div></td>
									<td style="border-bottom: none;"><div align="center">59</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">45</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">18</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">25</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">30</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">37</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">41</div></td>
									<td style="border-bottom: none;"><div align="center">67</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">50</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">20</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">27</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">34</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">41</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">45</div></td>
									<td style="border-bottom: none;"><div align="center">74</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">60</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">24</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">33</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">40</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">49</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">55</div></td>
									<td style="border-bottom: none;"><div align="center">89</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">70</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">28</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">38</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">47</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">57</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">64</div></td>
									<td style="border-bottom: none;"><div align="center">104</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">80</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">31</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">44</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">54</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">65</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">73</div></td>
									<td style="border-bottom: none;"><div align="center">118</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">90</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">35</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">49</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">60</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">74</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">82</div></td>
									<td style="border-bottom: none;"><div align="center">133</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">100</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">39</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">55</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">67</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">82</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">91</div></td>
									<td style="border-bottom: none;"><div align="center">148</div></td>
								</tr>
								<tr>
									<td style="border-right: none;"><div align="center">120</div></td>
									<td style="border-right: none;"><div align="center">47</div></td>
									<td style="border-right: none;"><div align="center">66</div></td>
									<td style="border-right: none;"><div align="center">80</div></td>
									<td style="border-right: none;"><div align="center">98</div></td>
									<td style="border-right: none;"><div align="center">109</div></td>
									<td><div align="center">178</div></td>
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

									<th width="318" colspan="6">Bond</th>
									<th align="left"></th>
								</tr>
								<tbody>
								<tr class="odd2">
									<td style="border-right: none;"><div align="center">
											Book<br /> 25x38
										</div></td>
									<td style="border-right: none;"><div align="center">
											Bond<br /> 17x22
										</div></td>
									<td style="border-right: none;"><div align="center">
											Cover<br /> 20x26
										</div></td>
									<td style="border-right: none;"><div align="center">
											Bristol<br /> 22&frac12;x28&frac12;
										</div></td>
									<td style="border-right: none;"><div align="center">
											Index<br /> 25&frac12;x30&frac12;
										</div></td>
									<td style="border-right: none;"><div align="center">
											Tag<br /> 24x36
										</div></td>
									<td style="border-right: none;"><div align="center">
											Metric<br /> g/m&sup2;
										</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">33</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">13</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">18</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">22</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">27</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">30</div></td>
									<td style="border-bottom: none;"><div align="center">49</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">41</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">16</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">22</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">27</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">33</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">37</div></td>
									<td style="border-bottom: none;"><div align="center">60</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">51</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">20</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">28</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">34</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">42</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">46</div></td>
									<td style="border-bottom: none;"><div align="center">75</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">61</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">24</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">33</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">41</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">50</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">56</div></td>
									<td style="border-bottom: none;"><div align="center">90</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">71</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">28</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">39</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">48</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">58</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">64</div></td>
									<td style="border-bottom: none;"><div align="center">105</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">81</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">32</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">45</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">55</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">67</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">74</div></td>
									<td style="border-bottom: none;"><div align="center">120</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">91</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">36</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">50</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">62</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">75</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">83</div></td>
									<td style="border-bottom: none;"><div align="center">135</div></td>
								</tr>
								<tr>
									<td style="border-right: none;"><div align="center">102</div></td>
									<td style="border-right: none;"><div align="center">40</div></td>
									<td style="border-right: none;"><div align="center">66</div></td>
									<td style="border-right: none;"><div align="center">69</div></td>
									<td style="border-right: none;"><div align="center">83</div></td>
									<td style="border-right: none;"><div align="center">93</div></td>
									<td><div align="center">150</div></td>
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

									<th width="318" colspan="6">Cover</th>
									<th align="left" ></th>
								</tr>
								<tbody>
								<tr class="odd2">
									<td style="border-right: none;"><div align="center">
											Book<br /> 25x38
										</div></td>
									<td style="border-right: none;"><div align="center">
											Bond<br /> 17x22
										</div></td>
									<td style="border-right: none;"><div align="center">
											Cover<br /> 20x26
										</div></td>
									<td style="border-right: none;"><div align="center">
											Bristol<br /> 22&frac12;x28&frac12;
										</div></td>
									<td style="border-right: none;"><div align="center">
											Index<br /> 25&frac12;x30&frac12;
										</div></td>
									<td style="border-right: none;"><div align="center">
											Tag<br /> 24x36
										</div></td>
									<td style="border-right: none;"><div align="center">
											Metric<br /> g/m&sup2;
										</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">91</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">36</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">50</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">62</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">75</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">82</div></td>
									<td style="border-bottom: none;"><div align="center">135</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">110</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">43</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">60</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">74</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">90</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">100</div></td>
									<td style="border-bottom: none;"><div align="center">162</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">119</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">47</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">65</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">80</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">97</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">100</div></td>
									<td style="border-bottom: none;"><div align="center">176</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">146</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">58</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">80</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">99</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">120</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">134</div></td>
									<td style="border-bottom: none;"><div align="center">216</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">164</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">66</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">90</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">111</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">135</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">149</div></td>
									<td style="border-bottom: none;"><div align="center">243</div></td>
								</tr>
								<tr>
									<td style="border-right: none;"><div align="center">183</div></td>
									<td style="border-right: none;"><div align="center">72</div></td>
									<td style="border-right: none;"><div align="center">100</div></td>
									<td style="border-right: none;"><div align="center">124</div></td>
									<td style="border-right: none;"><div align="center">150</div></td>
									<td style="border-right: none;"><div align="center">166</div></td>
									<td><div align="center">271</div></td>
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
									<th width="318" colspan="6">Vellum Bristol</th>
									<th align="left"></th>
								</tr>
								<tbody>
								<tr class="odd2">
									<td style="border-right: none;">Book<br /> 25x38
									</td>
									<td style="border-right: none;">Bond<br /> 17x22
									</td>
									<td style="border-right: none;">Cover<br /> 20x26
									</td>
									<td style="border-right: none;">Bristol<br /> 22&frac12;x28&frac12;
									</td>
									<td style="border-right: none;">Index<br /> 25&frac12;x30&frac12;
									</td>
									<td style="border-right: none;">Tag<br /> 24x36
									</td>
									<td style="border-right: none;">Metric<br /> g/m&sup2;
									</td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">110</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">39</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">54</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">67</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">81</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">91</div></td>
									<td style="border-bottom: none;"><div align="center">148</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">120</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">47</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">65</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">80</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">98</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">109</div></td>
									<td style="border-bottom: none;"><div align="center">178</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">148</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">58</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">81</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">100</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">121</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">135</div></td>
									<td style="border-bottom: none;"><div align="center">219</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">176</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">70</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">97</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">120</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">146</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">162</div></td>
									<td style="border-bottom: none;"><div align="center">261</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">207</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">82</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">114</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">140</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">170</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">189</div></td>
									<td style="border-bottom: none;"><div align="center">306</div></td>
								</tr>
								<tr>
									<td style="border-right: none;"><div align="center">237</div></td>
									<td style="border-right: none;"><div align="center">93</div></td>
									<td style="border-right: none;"><div align="center">130</div></td>
									<td style="border-right: none;"><div align="center">160</div></td>
									<td style="border-right: none;"><div align="center">194</div></td>
									<td style="border-right: none;"><div align="center">216</div></td>
									<td><div align="center">351</div></td>
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

									<th width="318" colspan="6">Index</th>
									<th align="left"></th>
								</tr>
								<tbody>
								<tr class="odd2">
									<td style="border-right: none;">Book<br /> 25x38
									</td>
									<td style="border-right: none;">Bond<br /> 17x22
									</td>
									<td style="border-right: none;">Cover<br /> 20x26
									</td>
									<td style="border-right: none;">Bristol<br /> 22&frac12;x28&frac12;
									</td>
									<td style="border-right: none;">Index<br /> 25&frac12;x30&frac12;
									</td>
									<td style="border-right: none;">Tag<br /> 24x36
									</td>
									<td style="border-right: none;">Metric<br /> g/m&sup2;
									</td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">110</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">43</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">60</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">74</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">90</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">100</div></td>
									<td style="border-bottom: none;"><div align="center">163</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">135</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">53</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">74</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">91</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">110</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">122</div></td>
									<td style="border-bottom: none;"><div align="center">203</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">170</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">67</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">93</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">115</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">140</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">156</div></td>
									<td style="border-bottom: none;"><div align="center">252</div></td>
								</tr>
								<tr>
									<td style="border-right: none;"><div align="center">208</div></td>
									<td style="border-right: none;"><div align="center">82</div></td>
									<td style="border-right: none;"><div align="center">114</div></td>
									<td style="border-right: none;"><div align="center">140</div></td>
									<td style="border-right: none;"><div align="center">170</div></td>
									<td style="border-right: none;"><div align="center">189</div></td>
									<td><div align="center">328</div></td>
								</tr>
							</tbody>
						</table>
						<div style="width: 100%;" id="table-bottom-bar">
							<div id="table-bottom-bar-L"></div>
							<div id="table-bottom-bar-R"></div>
						</div>
						<div class="clearview">&nbsp;</div>
						<table style="width: 100%;" class="standard-table" >
							
								<tr>

									<th width="318" colspan="6">Tag</th>
									<th align="left"></th>
								</tr>
								<tbody>
								<tr class="odd2">
									<td style="border-right: none;">Book<br /> 25x38
									</td>
									<td style="border-right: none;">Bond<br /> 17x22
									</td>
									<td style="border-right: none;">Cover<br /> 20x26
									</td>
									<td style="border-right: none;">Bristol<br /> 22&frac12;x28&frac12;
									</td>
									<td style="border-right: none;">Index<br /> 25&frac12;x30&frac12;
									</td>
									<td style="border-right: none;">Tag<br /> 24x36
									</td>
									<td style="border-right: none;">Metric<br /> g/m&sup2;
									</td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">110</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">43</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">60</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">74</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">90</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">100</div></td>
									<td style="border-bottom: none;"><div align="center">163</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">137</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">54</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">75</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">93</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">113</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">125</div></td>
									<td style="border-bottom: none;"><div align="center">203</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">165</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">65</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">90</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">111</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">135</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">150</div></td>
									<td style="border-bottom: none;"><div align="center">244</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none; border-bottom: none;"><div align="center">192</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">76</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">105</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">130</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">158</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">175</div></td>
									<td style="border-bottom: none;"><div align="center">284</div></td>
								</tr>
								<tr>
									<td style="border-right: none; border-bottom: none;"><div align="center">220</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">87</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">120</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">148</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">180</div></td>
									<td style="border-right: none; border-bottom: none;"><div align="center">200</div></td>
									<td style="border-bottom: none;"><div align="center">326</div></td>
								</tr>
								<tr class="odd">
									<td style="border-right: none;"><div align="center">275</div></td>
									<td style="border-right: none;"><div align="center">109</div></td>
									<td style="border-right: none;"><div align="center">151</div></td>
									<td style="border-right: none;"><div align="center">186</div></td>
									<td style="border-right: none;"><div align="center">225</div></td>
									<td style="border-right: none;"><div align="center">250</div></td>
									<td><div align="center">407</div></td>
								</tr>
							</tbody>
						</table>
						<div style="width: 100%;" id="table-bottom-bar">
							<div id="table-bottom-bar-L"></div>
							<div id="table-bottom-bar-R"></div>
						</div>

							
								<p class="addpadtop20"><strong>Note:</strong>: The results of the
								Interactive Calculations System are estimates and are not
								guaranteed by xpedx, LLC.<br /> <br /> </p>

					</div>
				
			</div>
			<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
			<!-- end main  -->
			<!-- end container  -->
		</div>
	</div>
</body>
</html>
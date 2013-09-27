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
<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
     
    

<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
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

<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript">
	
</script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.basiswtconvchart.title" /></title>
<!-- Web trend tag start -->
<meta name="DCSext.w_x_tools_ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.basiswtconvchart.title" />' />

<!-- Web trend tag end -->
</head>
<!-- Web trend tag start -->
<!-- commenting out WT temporarily-->
<!--s:include value="../../htmls/webtrends/webtrends.html"/-->
<!-- Web trend tag end -->

<body class="ext-gecko ext-gecko3">
    <div id="main-container">
        <div id="main">

        	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
            <div class="container"> 
                <!-- breadcrumb -->
                <div id="searchBreadcrumb">
                	<s:url id='toolsLink' namespace='/xpedx/tools' action='XPEDXTools'>
						<s:param name="selectedHeaderTab">ToolsTab</s:param>
		</s:url>
          <!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>"><s:text name="home.title" /></a> / <s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a>  / <span class="breadcrumb-inactive"><s:text name="tools.basiswtconvchart.title" /></span> Commented for jira 1538-->
                </div>
                <div id="mid-col-mil"><div style=" width: 600px;">  
                <div class="clearview">&nbsp;</div>
                 <h2>Basis Weight Conversion Chart</h2>
 <p>&nbsp; </p>
<br />

<table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="199" colspan="2" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Lbs./ 25 x 38" 500 g/m2 </span></td>
                                <td width="348" colspan="3" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr>
                                <td width="30%" valign="top" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff" width="100%">
                                  <tbody>
                                    <tr>
                                      <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff">1</td>
                                      <td width="50%" align="center" valign="middle" class="noBorders-fff" style="border-left: medium none;">1.48</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">27</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">40.0</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">28</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">41.4</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">30</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">44.4</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">31</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">45.9</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">33</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">48.8</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">34</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">50.3</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">35</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">51.8</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">36</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">53.3</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">37</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">54.8</td>
                                    </tr>
                                  </tbody>
                        </table></td>
                                <td width="5%" class="noBorders-fff">&nbsp;</td>
                              <td width="30%" valign="top" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff" width="100%">
                                  <tbody><tr>
                                    <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff"> 38</td>
                                    <td width="50%" align="center" valign="middle" class="noBorders-fff" style="border-left: medium none;">56.2</td>
                                  </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff " style="border-left: medium none; border-top: 1px solid #999;"">39</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">57.7</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">40</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">59.2</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">41</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">60.7</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">44</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">65.1</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">45</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">66.6</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">47</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">69.6</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">50</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">74.0</td>
                                    </tr>
                                    <tr>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">51</td>
                                      <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">75.5</td>
                                    </tr>
                                  <tr>
                                    <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">54</td>
                                    <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">79.9</td>
                                  </tr>
                              </tbody></table> </td>
                              <td width="5%" valign="top" class="noBorders-fff">&nbsp;</td>
                    <td width="30%" valign="top" ><table cellspacing="0" cellpadding="0" class="noBorders-fff" width="100%">
                      <tbody>
                        <tr>
                          <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff"> 55</td>
                          <td width="50%" align="center" valign="middle" class="noBorders-fff" style="border-left: medium none;">81.4</td>
                        </tr>
                        <tr>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">57</td>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">84.4</td>
                        </tr>
                        <tr>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">58</td>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">85.8</td>
                        </tr>
                        <tr>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">60</td>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">88.8</td>
                        </tr>
                        <tr>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">61</td>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">90.3</td>
                        </tr>
                        <tr>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">68</td>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">100.6</td>
                        </tr>
                        <tr>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">74</td>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">109.5</td>
                        </tr>
                        <tr>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">78</td>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">115.4</td>
                        </tr>
                        <tr>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">90</td>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">133.2</td>
                        </tr>
                        <tr>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">100</td>
                          <td align="center" valign="middle" class="noBorders-fff" style="border-left: medium none; border-top: 1px solid #999;"">148.0</td>
                        </tr>
                      </tbody>
                    </table></td>
                            </tr>
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
<div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>
                    <div class="clearview">&nbsp;</div> 
                    <div class="clearview"><strong>Conversion Factors </strong></div> 
                              
                       <div class="clearview"><br />
                         GSM/1.48 = 25 x 38 Book Weight<br />
                         GSM/2.7042 = 20 x 26 Cover Weight <br />

</div>
                     
                   
                 
                    <strong> </strong>

                </div><div class="bot-margin"></div>
			</div>
        </div>

	<!-- end main  -->
	<!--added for jira 3124 -->
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	<!-- end container  -->
    </div> </div>
</body>
</html>
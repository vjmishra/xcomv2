<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<%request.setAttribute("isMergedCSSJS","true");%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<!-- Webtrend tag starts -->
<meta name="WT.ti" content="Roll Weight">
<!-- Webtrend tag stops --> 
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES.css" />

<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script>

<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>

 <script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
 
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
     
     <!--[if IE]> -->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />

<!-- [endif]-->


<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<%-- <script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script> --%>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui.js" language="javascript"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/rool-weight.js" language="javascript"></script>



<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.rollweight.title" /></title>
</head>
<body class="ext-gecko ext-gecko3">
    <div id="main-container">
        <div id="main">

        	 <s:action name="xpedxHeader" executeResult="true" namespace="/common" />
            <div class="container"> 
                <!-- breadcrumb -->
                <div id="searchBreadcrumb">
                <s:url id='toolsLink' namespace='/xpedx/tools' action='XPEDXTools'>
						<s:param name="xpedxSelectedHeaderTab">ToolsTab</s:param>
				</s:url>
                	<!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>"><s:text name="home.title" /></a> / <s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a>  / <span class="breadcrumb-inactive"><s:text name="tools.rollweight.title" /></span> Commented for jira 1538-->
                </div>
                <div id="mid-col-mil"><div style=" width: 600px;">  
                 <div class="clearview">&nbsp;</div>
                 <h2> Roll Weight</h2>
 <p> 	
To find the approximate weight of a roll multiply roll diameter squared minus core diameter squared by roll width and appropriate factor.
</p> 

                        
                  <div id="requestform">
<div class="clearview">&nbsp;</div> 
 
            
           <form name="eform" method="post" action="sc_papcalcrollwt.aspx" id="eform" class="formborder">
            
               <table style="width:600px;" class="form roll-weight">
                    <tbody>
                      
                        <tr>
                          <td>Roll Diameter (Inches):</td>
                          <td> 
                          
                          <input name="rDiameter" type="text" style="width:80px;" onkeyup="calcPhone('Value',eform.rDiameter);" class="x-input right-align" id="rDiameter" /></td>
                        </tr>
                        <tr>
                          <td>Core Diameter (Inches):</td>
                          <td><input name="cDiameter" type="text" style="width:80px;" class="x-input right-align" id="cDiameter" onkeyup="calcPhone('Value',eform.cDiameter);"/></td>
                        </tr>
                        <tr>
                          <td>&nbsp;Roll Width (Inches):</td>
                          <td><span class="no-border-left">
                            <input type="hidden" id="txtstran" name="txtstran" />
                          </span>                            <input name="rWidth" type="text" style="width:80px;" class="x-input right-align" id="rWidth" onkeyup="calcPhone('Value',eform.rWidth);"/></td>
                        </tr>
                        <tr>
                          <td>Factor:&nbsp;</td>
                          <td class="padding8"><select name="factor" size="1" id="factor" class="TextField"> <option selected="selected" value="">Select factor :</option> <option value=".018">Antique - 
                          .018</option> <option value=".020">Vellum Offset - .020</option> 
                          <option value=".021">Bond - 
                          .021</option> <option value=".022">Smooth Offset - .022</option> 
                          <option value=".022">Vellum Bristol 
                          Cover - .022</option> <option value=".026">Tag and Index - .026</option> 
                          <option value=".032">C1S Label - 
                          .032</option> <option value=".033">C2S Cover - .033</option> <option value=".030">C1S Cover &amp; Blanks 
                          - .030</option> <option value=".037">C2S Coated Freesheet - 
                          .037</option> <option value=".032">Coated Groundwood &amp; SC - 
                          .032</option></select></td>
                        </tr>
                        <tr>
                          <td colspan="2">
                            <!-- <ul id="cart-actions"><li><a href="#" onclick="javascript:validateForm();" class="green-ui-btn"><span>Calculate</span></a></li><li><a class="grey-ui-btn" href="#" onclick="javascript:document.eform.reset()"><span>Clear</span></a></li> Changes done for jira 1538-->
                           <ul id="cart-actions"><li><a class="grey-ui-btn" href="#" onclick="javascript:document.eform.reset()"><span>Clear</span></a></li><li><a href="#" onclick="javascript:validateForm();" class="green-ui-btn"><span>Calculate</span></a></li>    
                            </ul>
                         </td>
                        </tr>
                        <tr >
                         <td width="195"><strong>Estimated Roll Weight:</strong></td>
                         <td> <strong class="red"> <input readonly="readonly"     name="Answer" style="border:none; color:#F00; font-weight:bold;" size="8"  >  </strong>lbs.</td>
                        </tr>
                         
                    </tbody>
                </table></form>
            
    <div class="clearview">&nbsp;</div>
                        
                    </div>
                     <div class="clearview"><h2>Specifications</h2></div><div class="clearview">&nbsp;</div>
<table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="200" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Factor Chart</span></td>
                                <td colspan="3" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr class="odd2">
                                <td>Type</td>
                               <td width="71" valign="top">Dia</td>
                               <td width="222" valign="top">Type</td>
                               <td width="87" valign="top">Dia</td>
                            </tr>
                            <tr class="odd">
                                <td>Antique</td>
                              <td valign="top">.018</td>
                              <td valign="top">Tag                     and Index</td>
                              <td valign="top">.026</td>
                            </tr>
                            <tr>
                              <td>Vellum Offset</td>
                              <td valign="top">.020</td>
                              <td valign="top">C1S                     Label</td>
                              <td valign="top">.032</td>
                          </tr>
                            <tr class="odd">
                              <td>Bond</td>
                              <td valign="top">.021</td>
                              <td valign="top">C2S                     Cover</td>
                              <td valign="top">.033</td>
                            </tr>
                            <tr>
                              <td>Smooth Offset</td>
                              <td valign="top">.022</td>
                              <td valign="top">C1S                     Cover &amp; Blanks</td>
                              <td valign="top">.030</td>
                          </tr>
                            <tr class="odd">
                              <td>Vellum Bristol Cover</td>
                              <td valign="top">.022</td>
                              <td valign="top">C2S                     Coated Freesheet</td>
                              <td valign="top">.037</td>
                            </tr>
                            <tr>
                              <td>Coated Groundwood &amp; SC</td>
                              <td valign="top">.032</td>
                              <td valign="top">&nbsp; </td>
                              <td valign="top">&nbsp;</td>
                          </tr>
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
<div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>
                    <div class="clearview">&nbsp;</div>
                     <table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="318" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Formula</span></td>
                                <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr>
                                <td align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                                  <tbody><tr>
                                    <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff">[ (Roll Dia&sup2;) - (Core Dia&sup2;)                                 ]</td>
                                  </tr>
                                  <tr>
                                    <td valign="middle" align="center" style="border-left: medium none; border-top: 1px solid #999;" class="noBorders-fff">x Roll Width x Factor </td>
                                  </tr>
                              </tbody></table></td>
                              <td width="38" valign="middle" class="noBorders-fff"> =</td>
                              <td width="228" valign="middle">Roll Weight</td>
                            </tr>
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
                    <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>  <div class="clearview">&nbsp; </div>       <table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="318" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Example</span></td>
                                <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr>
                              <td colspan="3" align="left" >Find the weight of                           roll of forms bond paper with a roll diameter of 40                           inches, a core diameter of 3 inches, and a roll width of                           17.5 inches.</td>
                            </tr>
                            <tr>
                                <td align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                                  <tbody><tr>
                                    <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff">[ (40x40) - (3x3) ]</td>
                                  </tr>
                                  <tr>
                                    <td valign="middle" align="center" style="border-left: medium none; border-top: 1px solid #999;" class="noBorders-fff">x 17.5 x .021<br /></td>
                                  </tr>
                                </tbody></table></td>
                              <td width="38" valign="middle" class="noBorders-fff"> =</td>
                              <td width="228" valign="middle">585 lbs.</td>
                            </tr><tr>
                              <td colspan="3" align="left" ><strong>Note</strong>: .021 is the appropriate factor for forms bond paper.</td>
                            </tr>
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
                    <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
          </div>  <div class="clearview">&nbsp; </div>
                     
                   
                 
                    <div class="x-corners"> 
                            
    <div ><strong>Note</strong>:<br />
      The results of the Interactive               Calculations System are estimates and are not guaranteed by International Paper.</div>
         
                    </div>

                </div><div class="bot-margin"></div>
			</div>
        </div>
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	<!-- end main  -->
		<!-- end container  -->
    </div></div>
</body>
</html>
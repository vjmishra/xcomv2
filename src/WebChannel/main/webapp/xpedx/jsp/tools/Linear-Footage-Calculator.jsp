<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<!-- Webtrend tag starts -->
<meta name="WT.ti" content="Linear Footage">
<!-- Webtrend tag stops --> 
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/global/ext-all.css" />
<s:include value="../common/XPEDXStaticInclude.jsp"/>

<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-mil-new.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/xpedx-quick-add.css"/>

<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/css/theme/prod-details.css"/>
 

<!-- javascript -->

<script type="text/javascript" src="../../xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="../../xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="../../xpedx/js/catalog/catalogExt.js"></script>

<!-- carousel scripts css  -->

<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../xpedx/js/jcarousel/skins/xpedx/skin.css" />

<!-- carousel scripts js   -->

<script type="text/javascript" src="../../xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="../../xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../../xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="../../xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="../../xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>

<script type="text/javascript" src="../../xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="../../xpedx/js/quick-add/quick-add.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="../../xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<script src="../../xpedx/js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="../../xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="../../xpedx/js/xpedx-new-ui.js" language="javascript"> </script>
<script type="text/javascript" src="../../xpedx/js/Linear-Footage-Calculator.js" language="javascript"> </script>

<script type="text/javascript" src="/swc/xpedx/js/jquery-ui.min.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.linearfootagecalculator.title" /></title>
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
                	<!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>"><s:text name="home.title" /></a> / <s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a>   / <span class="breadcrumb-inactive"><s:text name="tools.linearfootagecalculator.title" /></span> Commented for jira 1538-->
                </div>
                <div id="mid-col-mil"><div style=" width: 600px;">   
                 <div class="clearview">&nbsp;</div>
                <h2>Linear Footage</h2>
 <p>To find the approximate linear                                     footage in a roll of paper, multiply the weight                                     of the roll by the square inches of basic size                                     by 500. Divide by the sum representing the width                                     of the roll, multiplied by the substance or                                     basis weight, times 12.</p>
 
                  <div id="requestform">
<div class="clearview">&nbsp;</div> 
 
            
           <form name="eform" method="post" action="sc_papcalclinfoot.aspx" id="eform" >
            
               <table style="width:600px;" class="form">
                    <tbody>
                      
                        <tr>
                          <td>Roll                                                 Weight:<br /></td>
                          <td colspan="3"> 
                          
                          <input name="rWeight" type="text" style="width:80px;" onkeyup="calcPhone('Value',eform.rWeight);" class="x-input right-align" id="rWeight" /></td>
                        </tr>
                        <tr>
                          <td>Basic Size                                                 (Length X Width):&nbsp;</td>
                          <td width="98" class="padding8"><input name="hArea" type="text" style="width:80px;" class="x-input right-align" id="hArea" onkeyup="calcPhone('Value',eform.hArea);"/></td>
                          <td width="17" class="padding8">X </td>
                          <td width="270" class="padding8"><input name="wArea" type="text" style="width:80px;" class="x-input right-align" id="wArea" onkeyup="calcPhone('Value',eform.wArea);"/></td>
                        </tr>
                        <tr>
                          <td>Basis                                                 Weight:<br /></td>
                          <td colspan="3"><span class="no-border-left">
                            <input type="hidden" id="txtstran" name="txtstran" />
                          </span>                            <input name="bWeight" type="text" style="width:80px;" class="x-input right-align" id="bWeight" onkeyup="calcPhone('Value',eform.bWeight);"/></td>
                        </tr>
                        <tr>
                          <td>Roll                                                 Width:<br /></td>
                          <td colspan="3" class="padding8"><input name="rWidth" type="text" style="width:80px;" class="x-input right-align" id="rWidth" onkeyup="calcPhone('Value',eform.calcpages);"/></td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td colspan="3"> 
                           <!--  <ul id="cart-actions"><li><a class="grey-ui-btn" href="#" onClick="javascript:document.eform.reset()"><span>Clear</span></a></li><li><a href="#" onclick="javascript:validateForm();" class="green-ui-btn"><span>Calculate</span></a></li> Commented for jira 1538-->
                            <ul id="cart-actions"><li><a class="grey-ui-btn" href="#" onClick="javascript:document.eform.reset()"><span>Clear</span></a></li><li><a href="#" onclick="javascript:validateForm();" class="green-ui-btn"><span>Calculate</span></a></li>
                            </ul>
                         </td>
                        </tr>
                        <tr >
                         <td width="195"> <strong>Linear                                                 Footage:</strong></td>
                         <td colspan="3"> <input readonly="readonly"  name="Answer" style="border:none; color:#F00; font-weight:bold;" size="5">linear feet</td>
                        </tr>
                         
                    </tbody>
                </table></form>
            
    <div class="clearview">&nbsp;</div>
                        
                    </div>
                     <div class="clearview"><h2>Specifications</h2></div><div class="clearview">&nbsp;</div>
                     <table style="width: 100%;" id="mil-list-new2">
                       <tbody>
                         <tr class="table-header-bar">
                           <td width="195" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Rounding Rules</span></td>
                           <td width="393" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
                         </tr>
                         <tr class="odd2">
                           <td> Sheet Size</td>
                           <td valign="top">Ream Weights</td>
                         </tr>
                         <tr class="odd">
                           <td>864 inches&sup2; or larger</td>
                           <td valign="top">Ream Weight calculated to nearest pound. (166 lbs.)</td>
                         </tr>
                         <tr>
                           <td>336 inches&sup2; to 864 inches&sup2; </td>
                           <td valign="top">Ream Weight calculated to nearest half pound. (166.5 lbs.)</td>
                         </tr>
                         <tr class="odd">
                           <td valign="top">Less than 336 inches&sup2;</td>
                           <td valign="top">Ream Weight calculated to nearest one hundredth of a pound. (166.55 lbs.)</td>
                         </tr>
                       </tbody>
                     </table>
                     <div style="width: 100%;" id="table-bottom-bar">
<div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>
                    <div class="clearview">&nbsp;</div>
                    <table style="width: 100%;" id="mil-list-new3">
                      <tbody>
                        <tr class="table-header-bar">
                          <td width="199" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Basic Sizes</span></td>
                          <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
                        </tr>
                        <tr class="odd">
                          <td>Bond</td>
                          <td width="116" valign="top"  >17 x 22</td>
                          <td width="269" valign="top"> = 374 Square Inches </td>
                        </tr>
                        <tr>
                          <td>Book and Offset</td>
                          <td valign="top" >25 x 38 </td>
                          <td width="269" valign="top">= 950 Square Inches </td>
                        </tr>
                        <tr class="odd">
                          <td>Tag</td>
                          <td valign="top">24 x 36 </td>
                          <td width="269" valign="top"> = 864 Square Inches </td>
                        </tr>
                        <tr>
                          <td>Index</td>
                          <td valign="top" >25.5 x 30.5 </td>
                          <td width="269" valign="top">= 778 Square Inches</td>
                        </tr>
                        <tr class="odd">
                          <td>Vellum Bristol</td>
                          <td valign="top">22.5 x 30.5 </td>
                          <td width="269" valign="top"> = 686 Square Inches </td>
                        </tr>
                        <tr>
                          <td> Coated Cover 1S/C2S</td>
                          <td valign="top" >24 x 36 </td>
                          <td width="269" valign="top"> = 864 Square Inches </td>
                        </tr>
                        <tr class="odd">
                          <td> Coated Cover C2S</td>
                          <td valign="top"> 20 x 26 </td>
                          <td width="269" valign="top"> = 520 Square Inches </td>
                        </tr>
                        <tr>
                          <td>C1S Label</td>
                          <td valign="top" >25 x 38 </td>
                          <td width="269" valign="top"> = 950 Square Inches </td>
                        </tr>
                        <tr class="odd">
                          <td> Opaque Cover</td>
                          <td valign="top">20 x 26 </td>
                          <td width="269" valign="top"> = 520 Square Inches </td>
                        </tr>
                      </tbody>
                    </table>
                    <div style="width: 100%;" id="table-bottom-bar">
<div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
           </div> <div class="clearview">&nbsp;</div>
                     <table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="318" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Formula</span></td>
                                <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr>
                                <td align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                                  <tbody><tr>
                                    <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff"> Roll   Weight                                                       x Area of Basic Size   x 500 </td>
                                  </tr>
                                  <tr>
                                    <td valign="middle" align="center" style="border-left: medium none; border-top: 1px solid #999;" class="noBorders-fff"> Basis Weight                                                       x Roll Width x 12 </td>
                                  </tr>
                              </tbody></table></td>
                              <td width="38" valign="middle" class="noBorders-fff"> =</td>
                              <td width="228" valign="middle">Approximate 
Linear Footage</td>
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
                              <td colspan="3" align="left" > Find the footage                                                 of a roll of basis 50lb. Offset                                                 paper, 26&quot; wide and weighing                                                 100lbs.</td>
                            </tr>
                            <tr>
                                <td align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                                  <tbody><tr>
                                    <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff">100 x (25x38)                                                       x 500</td>
                                  </tr>
                                  <tr>
                                    <td valign="middle" align="center" style="border-left: medium none; border-top: 1px solid #999;" class="noBorders-fff"> (50x26)                                                       x 12 </td>
                                  </tr>
                                </tbody></table></td>
                              <td width="38" valign="middle" class="noBorders-fff"> =</td>
                              <td width="228" valign="middle"> 3,044.87                                                 linear feet</td>
                            </tr> 
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
            <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
          </div>  <div class="clearview">&nbsp; </div>
                     
                   
                 
                    <div class="x-corners"> 
                            
    <div ><strong>Note</strong>:<br />
The results of the                                       Interactive Calculations System are estimates                                   and are not guaranteed by International Paper.</div>
         
                    </div>

                </div> 
			</div>
        </div>
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	<!-- end main  -->
		<!-- end container  -->
    </div>    </div>
</body>
</html>
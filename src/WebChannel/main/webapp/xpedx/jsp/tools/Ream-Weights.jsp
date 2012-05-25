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
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES.css" />
 <!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />

<!-- [endif]-->
 
 

<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/Ream-Weights.js"></script>

<!-- jQuery -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>

    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
    
    <link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
     
      <script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
 
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>

<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui.js" language="javascript">
	
</script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.reamweight.title" /></title>

<!-- Webtrend tag starts -->
<meta name="WT.ti" content="Ream Weight Calculator">
<!-- Webtrend tag stops --> 

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
                	<!-- a href="<s:url action="home" namespace="/home" includeParams='none'/>"><s:text name="home.title" /></a> / <s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a>  / <span class="breadcrumb-inactive"><s:text name="tools.reamweight.title" /></span> Commented for jira 1538-->
                </div>
                <div id="mid-col-mil"><div style=" width: 600px;">
 <div id="requestform">
 						 <div class="clearview">&nbsp;</div>
                        <h2>Ream Weight Calculator</h2>
                        <br><!-- Add comment for jira 1538 -->
                        <p style="width: 600px;">Enter all fields to calculate the weight of the given size.</p><p style="width: 600px;"><strong>Note:</strong> For this tool, a ream is assumed to be 500 shts. 
                        </p>
                        <br />
                        <br>
                        <p style="width: 600px;">To determine the ream weight of any given size sheet, multiply the square inches in the given size by the given basis weight; divide the result by the square inch area of the basic size.</p>
                        <br />
                        <!-- <h2>Ream Weight Calculator</h2> changes done for jira 1538->		
	<!-- dharish-->
		
 
		<form name="eform" method="post" action="sc_PapCalcReamwts.aspx" id="eform" class="formborder">
<input type="hidden" name="__VIEWSTATE" value="dDwtMTU3MTQ0MjQwMzs7Pg==" />

			 <table style="width:600px;" class="form">
                    <tbody>
                      
                        <tr>
                          <td>Given Size                                                 (Length):</td>
                          <td colspan="3"> 
                          
                          <input name="hSize" type="text" style="width:80px;" onkeyup="calcPhone('Value',eform.hSize);" class="x-input right-align"id="hSize" /></td>
                        </tr>
                        <tr>
                          <td>Given Size                                                 (Width):</td>
                          <td colspan="3"><input name="wSize" type="text" style="width:80px;" class="x-input right-align"id="wSize" onkeyup="calcPhone('Value',eform.wSize);"/></td>
                        </tr>
                        <tr>
                          <td>Basis Weight:</td>
                          <td colspan="3"><span class="no-border-left">
                            <input type="hidden" id="txtstran" name="txtstran" />
                          </span>                            <input name="bWeight" type="text" style="width:80px;" class="x-input right-align"id="bWeight" onkeyup="calcPhone('Value',eform.bWeight);"/></td>
                        </tr>
                        <tr>
                          <td>Basic Size                                                 (Length X Width):&nbsp;</td>
                          <td width="138" class="padding8"><input name="hArea" type="text" style="width:80px;" class="x-input right-align"id="hArea" onkeyup="calcPhone('Value',eform.hArea);"/></td>
                          <td width="17" class="padding8">X </td>
                          <td width="230" class="padding8"><input name="wArea" type="text" style="width:80px;" class="x-input right-align"id="wArea" onkeyup="calcPhone('Value',eform.wArea);"/></td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td colspan="3"> 
                            <!-- <ul id="cart-actions"><li><a href="#" onClick="javascript:validateForm()" class="green-ui-btn"><span>Calculate</span></a></li><li><a class="grey-ui-btn" href="#" onClick="javascript:document.eform.reset()"><span>Clear</span></a></li> -->
                             <!-- Changes done for jira 1538  -->
                             <ul id="cart-actions"><li><a class="grey-ui-btn" href="#" onClick="javascript:document.eform.reset()"><span>Clear</span></a></li><li><a href="#" onClick="javascript:validateForm()" class="green-ui-btn"><span>Calculate</span></a></li> 
                                
                            </ul>
                         </td>
                        </tr>
                        <tr >
                         <td width="195"><strong>Ream Weight:</strong></td>
                         <td colspan="3"> <strong class="red"> <input readonly="readonly" style="border:none; color:#F00; font-weight:bold;" class="TextField" size="6" value=" " name="Answer"></strong>lbs                                              per ream</td>
                        </tr>
                         
                    </tbody>
                </table>  
		</form></div>
 <!-- dharish ends-->
 
   <div class="clearview">&nbsp;</div>
                     <div class="clearview"><h2>Specifications</h2></div><div class="clearview">&nbsp;</div>
<table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="195" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Rounding Rules</span></td>
                                <td width="393" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr class="odd2">
                                <td>
                                    Sheet Size</td>
                               <td valign="top">Ream Weights</td>
                            </tr>
                            <tr class="odd">
                                <td>864 inches&sup2; or larger</td>
                              <td valign="top">Ream Weight calculated to nearest pound. (166 lbs.)</td>
                            </tr>
                            <tr>
                              <td>336 inches² to 864 inches&sup2; </td>
                              <td valign="top">Ream Weight calculated to nearest half pound. (166.5 lbs.)</td>
                          </tr>
 <tr class="odd">
                                <td valign="top">Less than 336 inches&sup2;</td>
              <td valign="top">Ream Weight calculated to nearest one hundredth of a pound. (166.55 lbs.)</td>
 </tr>
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
                        <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>
                    <div class="clearview">&nbsp;</div>
                    <table style="width: 100%;" id="mil-list-new">
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
                    </div>  <div class="clearview">&nbsp; </div><table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="318" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Formula</span></td>
                                <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr>
                                <td align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                                  <tr>
                                    <td align="center" valign="middle" class="noBorders-fff" style="border-left:none">Sheet Size                                                     x Basis Weight </td>
                                  </tr>
                                  <tr>
                                    <td align="center" valign="middle" class="noBorders-fff" style="border-left:none; border-top:solid 1px #999;"> Basic Size </td>
                                  </tr>
                                </table></td>
                              <td width="38" valign="middle" class="noBorders-fff" > =</td>
                              <td width="228" valign="middle">Ream Weight</td>
                            </tr>
               	  </tbody></table>
                  
                      <div style="width: 100%;" id="table-bottom-bar">
                    <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                </div>
                <div class="clearview">&nbsp; </div><table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td colspan="2" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Example</span></td>
                                <td colspan="3" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr>
                                <td width="30%" align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                                  <tr>
                                    <td align="center" valign="middle" class="noBorders-fff" style="border-left:none">(43x61) x 60</td>
                                  </tr>
                                  <tr>
                                    <td align="center" valign="middle" class="noBorders-fff" style="border-left:none; border-top:solid 1px #999;"> 25 x 38 </td>
                                  </tr>
                                </table></td>
                               <td width="5%" valign="middle" class="noBorders-fff" > =</td>  <td width="30%"  align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                                  <tr>
                                    <td align="center" valign="middle" class="noBorders-fff" style="border-left:none">157,280</td>
                                  </tr>
                                  <tr>
                                    <td align="center" valign="middle" class="noBorders-fff" style="border-left:none; border-top:solid 1px #999;">950</td>
                                  </tr>
                                </table></td>
                              <td width="25%" valign="middle"   ><span class="noBorders-fff">=</span> 166 lbs.                                                 Per Ream </td>
                            </tr>
               	  </tbody></table>
                  
                      <div style="width: 100%;" id="table-bottom-bar">
                    <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
          </div>
                    <div class="x-corners"> 
                            
    <div ><br /><strong>Note:</strong><br />
      The results of the Interactive                                     Calculations System are estimates and are not                                   guaranteed by International Paper.<br />
<br />
    </div>
         
                    </div>

                </div> 
			</div>
        </div>
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	<!-- end main  -->
	<!-- end container  -->
    </div></div>
</body>
</html>

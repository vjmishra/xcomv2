<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<!-- Webtrend tag starts -->
<meta name="WT.ti" content="M-Weight">
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
<script type="text/javascript" src="../../xpedx/js/m-Weights.js"></script>

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
<!-- /STUFF --><script type="text/javascript" src="../../xpedx/js/xpedx-new-ui.js" language="javascript">
	
</script>


<script type="text/javascript" src="/swc/xpedx/js/jquery-ui.min.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.mweight.title" /></title>
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
				
                	 <!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>"><s:text name="home.title" /></a> / <s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a>  / <span class="breadcrumb-inactive"><s:text name="tools.mweight.title" /></span> Commented for jira 1538 -->
                
                </div>
                <div id="mid-col-mil"><div style=" width: 600px;">
 <div class="clearview">&nbsp;</div>
						
						<h2>  M Weight Calculation</h2><!-- Add comment for jira 1538 -->
						<p style="width: 600px;">To calculate the M weight of a particular size, enter the length, width and basis weight.Then Enter the basic size for the table provided.The tool will then provide the total weight per 1,000 shts of the particular size.</p>
                        <br />
                        <p style="width: 600px;">The ream weight is the weight of 500 sheets; the M weight is the weight of 1,000 sheets. The M weight can be obtained by first finding the ream weight, rounding it by the rounding rules, then multiplying by 2.</p>
                        <br />
                        <!--  <h2>  M Weight Calculation</h2> Changes done for jira 1538-->
                        
                      
                          
                  <div id="requestform">
<div class="clearview">&nbsp;</div> 
 
            
         <form name="eform" method="post" action="sc_PapCalcmweights.aspx" id="eform" class="formborder">
            
               <table style="width:600px;" class="form">
                    <tbody>
                      
                        <tr>
                          <td>Given Size                                                 (Length):</td>
                          <td colspan="3"> 
                          
                          <input name="hSize" type="text" style="width:80px;" onkeyup="calcPhone('Value',eform.hSize);" class="x-input right-align" id="hSize" /></td>
                        </tr>
                        <tr>
                          <td>Given Size                                                 (Width):</td>
                          <td colspan="3"><input name="wSize" type="text" style="width:80px;" class="x-input right-align" id="wSize" onkeyup="calcPhone('Value',eform.wSize);"/></td>
                        </tr>
                        <tr>
                          <td>Basis Weight:</td>
                          <td colspan="3"><span class="no-border-left">
                            <input type="hidden" id="txtstran" name="txtstran" />
                          </span>                            <input name="bWeight" type="text" style="width:80px;" class="x-input right-align" id="bWeight" onkeyup="calcPhone('Value',eform.bWeight);"/></td>
                        </tr>
                        <tr>
                          <td>Basic Size                                                 (Length X Width):&nbsp;</td>
                          <td width="98" class="padding8"><input name="hArea" type="text" style="width:80px;" class="x-input right-align" id="hArea" onkeyup="calcPhone('Value',eform.hArea);"/></td>
                          <td width="17" class="padding8">X </td>
                          <td width="260" class="padding8"><input name="wArea" type="text" style="width:80px;" class="x-input right-align" id="wArea" onkeyup="calcPhone('Value',eform.wArea);"/></td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td colspan="3"> 
                            <!-- <ul id="cart-actions"><li><a href="#"onClick="javascript:validateForm()" class="green-ui-btn"><span>Calculate</span></a></li><li><a class="grey-ui-btn" href="#"><span>Clear</span></a></li> -->
                              <!-- Changes done for jira 1538 -->
                              <ul id="cart-actions"><li><a class="grey-ui-btn" href="#" onClick="javascript:document.eform.reset()"><span>Clear</span></a></li><li><a href="#"onClick="javascript:validateForm()" class="green-ui-btn"><span>Calculate</span></a></li>   
                            </ul>
                         </td>
                        </tr>
                        <tr >
                         <td width="205"><strong>M Weight:</strong></td>
                         <td colspan="3"><input readonly="readonly"  name="Answer" style="border:none; color:#F00; font-weight:bold;" size="5"> lbs.</td>
                        </tr>
                         
                    </tbody>
                </table></form>
            
    <div class="clearview">&nbsp;</div>
                        
                    </div>
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
                              <td>336 inches� to 864 inches&sup2; </td>
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
                    </div>  <div class="clearview">&nbsp; </div>
                     
                   
                 
                    <div class="x-corners"> 
                            
    <div ><br /><strong>Note:</strong> <br />

The results of the Interactive Calculations System are estimates   												and are not guaranteed by International Paper.<br />
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
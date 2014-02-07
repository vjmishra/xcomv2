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
<meta name="WT.ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.gsmcalculator.title" />'>
<meta name="DCSext.w_x_tools_ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.gsmcalculator.title" />'>

<!-- Webtrend tag stops --> 
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
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
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
 
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"> </script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/gsm<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"> </script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.gsmcalculator.title" /></title>
</head>
<body class="ext-gecko ext-gecko3">
    <div id="main-container">
        <div id="main">

        	 <s:action name="xpedxHeader" executeResult="true" namespace="/common" />
            <div class="container"> 
                <!-- breadcrumb -->
                <div id="searchBreadcrumb">
                	<s:url id='toolsLink' namespace='/tools' action='MyTools'>
						<s:param name="selectedHeaderTab">ToolsTab</s:param>
		</s:url>
          <!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>"><s:text name="home.title" /></a> / <s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a> / <span class="breadcrumb-inactive"><s:text name="tools.gsmcalculator.title" /></span> Commented for jira 1538-->
                </div>
                <div id="mid-col-mil"><div style=" width: 600px;">
 <br />

                        <h2>GSM Calculator</h2>
                        
                      <p>To convert basis weight of a given standard size of paper to grams per square meter, multiply its basis weight by 1406.5 and divide the results by the square area of its basic size.</p>
                          
                  <div id="requestform">
<div class="clearview">&nbsp;</div> 
 
            
          <form name="eform" method="post" action="GSMCalculator.aspx" id="eform" class="formborder">
               <table style="width:600px;" class="form">
                    <tbody>
                      
                        <tr>
                          <td> Basis Weight:</td>
                          <td width="385"> 
                          
                          <input name="bWeight" type="text" style="width:120px;" onkeyup="calcPhone('Value',eform.bWeight);" class="x-input" id="bWeight" /></td>
                        </tr>
                        <tr>
                          <td>Standard Size (Sq.Inch): </td>
                          <td><span class="no-border-left">
                            <input type="hidden" id="txtstran" name="txtstran" />
                          </span>
                            <select name="bSize" size="1" id="bSize">
							  <option selected="">Please Select an Option</option>
																	<option value="374">Bond 17 X 22-374</option>
																	<option value="950">Book Offset 25 X 38-950</option>
																	<option value="864">Tag 24 X 36 - 864</option>
																	<option value="778">Index 25.5 X 30.5-778</option>
																	<option value="686">Vellum B 22.5 X 30.5-686</option>
																	<option value="520">Coated C C2S 20 X 26-520</option>
																	<option value="950">C1S Label 25 X 38-950</option>
																	<option value="520">Opaque C 20 X 26-520</option>
																	<option value="864">Cover C-1S C2S24x36-864</option>
						  </select></td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td > 
                            <!-- <ul id="cart-actions"><li><a href="#" onclick="javascript:validateForm();" class="green-ui-btn"><span>Calculate</span></a></li><li><a class="grey-ui-btn" href="#" onClick="javascript:document.eform.reset()"><span>Clear</span></a></li> -->
                            <!--Changes done for jira 1538  -->
                            <ul id="cart-actions"><li><a class="grey-ui-btn" href="#" onClick="javascript:document.eform.reset()"><span>Clear</span></a></li><li><a href="#" onclick="javascript:validateForm();" class="green-ui-btn"><span>Calculate</span></a></li>     
                            </ul>
                         </td>
                        </tr>
                        <tr >
                         <td width="195"><strong>Basis Weight Conversion:</strong></td>
                         <td align="left"><input readonly="readonly"  name="Answer" style="border:none; color:#F00; font-weight:bold;" size="15">
                         GSM </td>
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
                                <td>864 inches� or larger</td>
                              <td valign="top"> Ream Weight calculated to nearest pound. (166 lbs.)</td>
                            </tr>
                            <tr>
                              <td>336 inches&sup2; to 864 inches&sup2;</td>
                              <td valign="top"> Ream Weight calculated to nearest half pound. (166.5 lbs.)</td>
                          </tr>
 <tr class="odd">
                                <td> Less than 336 inches&sup2;</td>
              <td valign="top"> Ream Weight calculated to nearest one hundredth of a pound.   														(166.55 lbs.)</td>
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
                    </table><div style="width: 100%;" id="table-bottom-bar">
                    <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>   <div class="clearview">&nbsp;</div>
                     <table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="318" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Formula</span></td>
                                <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr>
                                <td align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                                  <tbody><tr>
                                    <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff">Basis Weight x 1406.5</td>
                                  </tr>
                                  <tr>
                                    <td valign="middle" align="center" style="border-left: medium none; border-top: 1px solid #999;" class="noBorders-fff">Basic Size</td>
                                  </tr>
                              </tbody></table></td>
                              <td width="38" valign="middle" class="noBorders-fff"> =</td>
                              <td width="228" valign="middle">Grams Per Square Meter</td>
                            </tr>
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
                    <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>  <div class="clearview">&nbsp; </div>  
                    <table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="318" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Example</span></td>
                                <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr>
                              <td colspan="3"  >Convert 17x22, 28lb. Bond paper to grams per square meter.</td>
                            </tr>
                            <tr>
                                <td align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                                  <tbody><tr>
                                    <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff">28 x 1406.5</td>
                                  </tr>
                                  <tr>
                                    <td valign="middle" align="center" style="border-left: medium none; border-top: 1px solid #999;" class="noBorders-fff">374</td>
                                  </tr>
                              </tbody></table></td>
                              <td width="38" valign="middle" class="noBorders-fff"> =</td>
                              <td width="228" valign="middle">105.30 grams/meter&sup2;</td>
                            </tr>
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
                    <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>  <div class="clearview">&nbsp; </div>  
                     
                   
                 
                  <div class="x-corners"> 
                            
    <div ><br />
      <strong>Note</strong>:<br />
The results of the Interactive Calculations System are   											estimates and are not guaranteed by International Paper.<br />
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
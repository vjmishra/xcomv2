<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="xpedx" uri="/WEB-INF/xpedx.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->
	<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
	

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/home<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/portalhome<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/narrowBy<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/styles<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/theme-xpedx_v1.2<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil-new<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-forms<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-quick-add<s:property value='#wcUtil.xpedxBuildKey' />.css"/>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/prod-details<s:property value='#wcUtil.xpedxBuildKey' />.css"/>


     
   
 

<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts css  -->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/skin<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
 <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript">
	
</script>


<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>

<title><s:property value="wCContext.storefrontId" /> - Imposition Calculator</title>
<!-- Webtrend tag starts -->
<meta name="WT.ti" content="xpedx/Imposition Calculater">
<!-- Webtrend tag stops --> 
</head>
<!-- Web trend tag start -->
<s:include value="../../htmls/webtrends/webtrends.html"/>
<!-- Web trend tag end -->

<s:set name='_action' value='[0]' />
<body class="ext-gecko ext-gecko3">
    <div id="main-container">
        <div id="main">
        	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
        
			<div class="container"> 
                <!-- breadcrumb -->
                <div id="searchBreadcrumb">
                	<!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>">Home</a> / <a href="<s:url action="XPEDXTools" namespace="/xpedx/tools" includeParams='none'/>">Tools</a>  / <span class="breadcrumb-inactive">Imposition Calculater</span> Commented for jira 1538-->
                </div>                               
                
                <div id="mid-col-mil"><div style=" width: 600px;">  
                 <div class="clearview">&nbsp;</div>
                 <h2> Imposition Calculator</h2>
                  To calculate the possible options for cutting a smaller sheet size from a larger sheet size, enter as much information as possible for the most accurate calculations.
                  
                  <div id="requestform">
<div class="clearview">&nbsp;</div> 
 
            
                      
              <s:form id="impCalculatorForm" name="impCalculatorForm" namespace="/xpedx/tools"
              method="post" action="xpedxViewImpCalculation">
            
               <table style="width:600px;" class="form">
                    <tbody>
                        <tr>
                          <td>Sheet Size:</td>
                          <td width="12" valign="middle" class="padding8">W: 
                            </td>
                          <td width="80" valign="middle" class="padding8">                                               
                          	<s:textfield name="sheetSizeW" cssStyle="width:70px;" cssClass="x-input" id="sheetSizeW" onkeyup="calcPhone('Value',impCalculatorForm.sheetSizeW);"/>                          
                          </td>
                          <td width="12" class="padding8">X </td>
                          <td width="12" class="padding8">H:                            </td>
                          <td width="256" class="padding8">                          
                          	<s:textfield name="sheetSizeH" cssStyle="width:70px;" cssClass="x-input" id="sheetSizeH" onkeyup="calcPhone('Value',impCalculatorForm.sheetSizeH);"/>
                          </td>
                        </tr>
                        <tr>
                          <td>Trim Size:</td>
                          <td width="12" valign="middle" class="padding8">W:</td>
                          <td width="80" valign="middle" class="padding8">							
							<s:textfield name="trimSizeW" cssStyle="width:70px;" cssClass="x-input" id="trimSizeW" onkeyup="calcPhone('Value',impCalculatorForm.trimSizeW);"/>
						  </td>
                          <td width="12" class="padding8">X </td>
                          <td width="12" class="padding8">H:                            </td>
                          <td width="256" class="padding8">                          	
                          	<s:textfield name="trimSizeH" cssStyle="width:70px;" cssClass="x-input" id="trimSizeH" onkeyup="calcPhone('Value',impCalculatorForm.trimSizeH);"/>
                          </td>
                        </tr>
                        <tr>
                         <td>Gripper Width:</td>
                         <td colspan="5">                                                     
                          <s:textfield name="gripperWidth" cssStyle="width:120px;" onkeyup="calcPhone('Value',impCalculatorForm.gripperWidth);" cssClass="x-input" id="gripperWidth" value="0"/>
                         </td>
                        </tr>
                        <tr>
                         <td>Color Bar Width:</td>
                         <td colspan="5">
                          <s:textfield name="colorBarWidth" cssStyle="width:120px;" cssClass="x-input" id="colorBarWidth" onkeyup="calcPhone('Value',impCalculatorForm.colorBarWidth);" value="0"/>
                         </td>
                        </tr>
                        <tr>
                          <td>Side Guide: </td>
                          <td colspan="5"><span class="no-border-left">                            
                          </span>
                           <s:textfield name="sideGuide" cssStyle="width:120px;" cssClass="x-input" id="sideGuide" onkeyup="calcPhone('Value',impCalculatorForm.sideGuide);" value="0"/>
                          </td>
                        </tr>
                      <tr>
                          <td>Gutter: </td>
                          <td colspan="5">                          	
                          	<s:textfield name="gutter" cssStyle="width:120px;" cssClass="x-input" id="gutter" onkeyup="calcPhone('Value',impCalculatorForm.gutter);" value="0"/>
                          </td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td colspan="5"> 
                            <!-- <ul id="cart-actions"><li><a href="#" onclick="return checkCalculatorValues();" class="green-ui-btn"><span>Calculate</span></a></li><li><a class="grey-ui-btn" href="#" onclick="clearValues();"><span>Clear</span></a></li>-->
                              <!-- Changes done for jira 1538 -->
                              <ul id="cart-actions"><li><a class="grey-ui-btn" href="#" onClick="clearValues();"><span>Clear</span></a></li><li><a href="#" onclick="return checkCalculatorValues();" class="green-ui-btn"><span>Calculate</span></a></li>  
                            </ul>
                         </td>
                        </tr>                       
                         
                    </tbody>
                </table></s:form>
             
            
    <div class="clearview">&nbsp;</div>
                        
                    </div>                                                       

                </div>
                
                
                <div class="bot-margin"></div>
			</div>
        </div>
        
        </div>
	</div>
 
	<!-- end main  -->
    	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />



	<!-- end container  -->
</body>
</html>
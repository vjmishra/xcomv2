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
<meta name="WT.ti" content="Pages/Inch Calculator">
<meta content='IE=9' http-equiv='X-UA-Compatible' />
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

<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
     
     <!--[if IE]> -->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />

<!-- [endif]-->

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
	src="/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui.js" language="javascript"> </script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/page-inch.js" language="javascript"> </script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.pagesinchcalculator.title" /></title>
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
        <!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>"><s:text name="home.title" /></a> / <s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a>/ <span class="page-title"><s:text name="tools.pagesinchcalculator.title" /></span> Commented for jira 1538 -->
                </div>
                <div id="mid-col-mil"><div style=" width: 600px;">
 <br />

                        <h2>Pages/Inch Calculator</h2>
                       <br/> 
                      <p> To determine the pages per inch (PPI), divide 2 by the caliper of the given   											sheet.<br />
                      </p>
                          
                  <div id="requestform">
<div class="clearview">&nbsp;</div> 
 
            
          <form name="eform" method="post" action="sc_PapCalcppinch.aspx" id="eform" class="formborder">
            
               <table style="width:600px;" class="form pages-inch">
                    <tbody>
                      
                        <tr>
                          <td>&nbsp;Caliper:
                         	 <input name="caliper" type="text" onkeyup="calcPhone('Value',eform.caliper);" class="x-input nofloat width-120" id="caliper" /> 
                          </td>
                          <td>
                         	 <p class="caliper-text">(Must be entered as a decimal. Example: 0.010.)</p>
                          </td>
                        </tr>
                        <tr>
                          <td colspan="2">
			<!-- Changes done for jira 1538 -->
			 <ul id="cart-actions"><li><a class="grey-ui-btn" href="#" onClick="javascript:document.eform.reset()"><span>Clear</span></a></li><li><a href="#" onclick="javascript:validateForm();" class="green-ui-btn"><span>Calculate</span></a></li>
                            <!--<ul id="cart-actions"><li><a href="#" onclick="javascript:validateForm();" class="green-ui-btn"><span>Calculate</span></a></li><li><a class="grey-ui-btn" href="#" onClick="javascript:document.eform.reset()"><span>Clear</span></a></li> -->
                                
                            </ul>
                         </td>
                        </tr>
                        <tr >
                         <td width="195"><strong>&nbsp;Pages/Inches   																(PPI):</strong></td>
                         <td> <input readonly="readonly"  name="Answer" style="border:none; color:#F00; font-weight:bold;" size="15">pages per inch</td>
                        </tr>
                         
                    </tbody>
             </table></form>
            
    <div class="clearview">&nbsp;</div>
                        
            </div>
                     <div class="clearview"><h2>Specifications</h2></div><div class="clearview">&nbsp;</div>
            <table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="318" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Formula</span></td>
                                <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr>
                                <td align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                                  <tbody><tr>
                                    <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff">2</td>
                                  </tr>
                                  <tr>
                                    <td valign="middle" align="center" style="border-left: medium none; border-top: 1px solid #999;" class="noBorders-fff">Caliper</td>
                                  </tr>
                              </tbody></table></td>
                              <td width="38" valign="middle" class="noBorders-fff"> =</td>
                              <td width="228" valign="middle">Pages Per Inch</td>
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
                              <td colspan="3" >Find the pages per inch for the   																	Coated Cover Paper with a caliper of .010</td>
                            </tr>
                            <tr>
                                <td align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                                  <tbody><tr>
                                    <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff">2 </td>
                                  </tr>
                                  <tr>
                                    <td valign="middle" align="center" style="border-left: medium none; border-top: 1px solid #999;" class="noBorders-fff"><div align="center">.010 </div></td>
                                  </tr>
                              </tbody></table></td>
                              <td width="38" valign="middle" class="noBorders-fff"> =</td>
                              <td width="228" valign="middle">200 Pages Per Inch</td>
                            </tr>
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
                    <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>  <div class="clearview">&nbsp; </div>  
                     
                   
                 
                  <div class="x-corners"> 
                            
    <div ><br />
      <strong>Note</strong>:<br />
 The results of the Interactive Calculations System are estimates and are not   												guaranteed by International Paper.<br />
<br />
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
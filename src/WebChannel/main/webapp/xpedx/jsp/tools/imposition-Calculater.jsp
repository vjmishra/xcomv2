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
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES<s:property value='#wcUtil.xpedxBuildKey' />.css" />
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

<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript">
	
</script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.impositioncalculator.title" /></title>
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
          <a href="<s:url action="home" namespace="/home" includeParams='none'/>"><s:text name="home.title" /></a> / <s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a>  / <span class="page-title"><s:text name="tools.impositioncalculator.title" /></span>
                </div>
                <div id="mid-col-mil"><div style=" width: 600px;">   <h2> Imposition Calculator</h2>
                 <p style="width: 600px;">To calculate the possible options for cutting a smaller sheet size from a larger sheet size, enter as much information as possible for the most accurate calculations.</p>
                 <br />
                 
                  <div id="requestform">
<div class="clearview">&nbsp;</div> 
 
            
                      <form method="post" action="#" id="form_Ream" name="form_Ream">
            
               <table style="width:600px;" class="form">
                    <tbody>
                        <tr>
                          <td>Sheet Size:</td>
                          <td width="12" valign="middle" class="padding8">W: 
                            </td>
                          <td width="80" valign="middle" class="padding8"><input name="calcpieces2" type="text" style="width:70px;" class="x-input" id="calcpieces2" onkeyup="calcPhone('Value',form_Ream.calcpieces2);"/></td>
                          <td width="12" class="padding8">X </td>
                          <td width="12" class="padding8">H:                            </td>
                          <td width="256" class="padding8"><input name="calcWaste" type="text" style="width:70px;" class="x-input" id="calcWaste" onkeyup="calcPhone('Value',form_Ream.calcWaste);"/></td>
                        </tr>
                        <tr>
                          <td>Trim Size:</td>
                          <td width="12" valign="middle" class="padding8">W:</td>
                          <td width="80" valign="middle" class="padding8"><input name="calcpieces" type="text" style="width:70px;" class="x-input" id="calcpieces" onkeyup="calcPhone('Value',form_Ream.calcpieces);"/></td>
                          <td width="12" class="padding8">X </td>
                          <td width="12" class="padding8">H:                            </td>
                          <td width="256" class="padding8"><input name="calcWaste2" type="text" style="width:70px;" class="x-input" id="calcWaste2" onkeyup="calcPhone('Value',form_Ream.calcWaste2);"/></td>
                        </tr>
                        <tr>
                          <td>Gripper Width:</td>
                          <td colspan="5"> 
                          
                          <input name="calcwidth" type="text" style="width:120px;" onkeyup="calcPhone('Value',form_Ream.calcwidth);" class="x-input" id="calcwidth" value="0" /></td>
                        </tr>
                        <tr>
                          <td>Color Bar Width:</td>
                          <td colspan="5"><input name="calcheight" type="text" style="width:120px;" class="x-input" id="calcheight" onkeyup="calcPhone('Value',form_Ream.calcheight);" value="0"/></td>
                        </tr>
                        <tr>
                          <td>Side Guide: </td>
                          <td colspan="5"><span class="no-border-left">
                            <input type="hidden" id="txtstran" name="txtstran" />
                          </span>                            <input name="calcpages" type="text" style="width:120px;" class="x-input" id="calcpages" onkeyup="calcPhone('Value',form_Ream.calcpages);" value="0" /></td>
                        </tr>
                      <tr>
                          <td>Gutter: </td>
                          <td colspan="5"><input name="Gutter" type="text" style="width:120px;" class="x-input" id="Gutter" onkeyup="calcPhone('Value',form_Ream.Gutter);" value="0" /></td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td colspan="5"> 
                            <ul id="cart-actions"><li><a href="#" onclick="return check5();" class="green-ui-btn"><span>Calculate</span></a></li><li><a class="grey-ui-btn" href="#" onClick="javascript:document.form_Ream.reset()"><span>Clear</span></a></li>
                                
                            </ul>
                         </td>
                        </tr>
                        
                         
                    </tbody>
                </table></form>
             
            
    <div class="clearview">&nbsp;</div>
                        
                    </div>
                     <div class="clearview"><h2>Possible Layouts:</h2></div><div class="clearview">&nbsp;</div>
<table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="399" class="no-border table-header-bar-left "><span class="white txt-small"> Layout Method (click link to view layout)</span></td>
                                <td width="82" align="left" class="no-border"><span class="white txt-small">Number out </span></td>
                                <td align="left" class="no-border-right table-header-bar-right"><span class="white txt-small">Waste % </span> </td>
                      </tr>
                            <tr class="odd">
                                <td><a href="javascript:document.View1.submit()">1. Sheet Long Side / Trim Long Side</a></td>
                              <td valign="top">0</td>
                              <td width="103" valign="top">100 %</td>
                            </tr>
                            <tr>
                              <td><a href="javascript:document.View2.submit()">2. Sheet Long Side / Trim Short Side</a></td>
                              <td valign="top">0</td>
                              <td valign="top">100 %</td>
                          </tr>
                            <tr class="odd">
                              <td><a href="javascript:document.View3.submit()">3. Sheet Long Side / Trim Long Side use waste if possible</a></td>
                              <td valign="top">0</td>
                              <td valign="top">100 %</td>
                            </tr>
                            <tr>
                              <td><a href="javascript:document.View4.submit()">4. Sheet Long Side / Trim Short Side use waste if possible</a></td>
                              <td valign="top">0</td>
                              <td valign="top">100 %</td>
                            </tr>
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
<div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
</div>
                    <div class="clearview">&nbsp;</div> 
                     <table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                <td width="187" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Parameters</span></td>
                                <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Sheet Size:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8">2 x 22</td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Trim Size:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8">2 x 22</td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Gripper Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8">2 x 22</td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Color Bar Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8">2 x 22</td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Side Guide Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8">2 x 22</td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Gutter Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8">2 x 22</td>
                            </tr>
               	  </tbody></table><div style="width: 100%;" id="table-bottom-bar">
            <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>  <div class="clearview">&nbsp; </div>       
                     
                     
                   
                 
                    <strong> </strong>

                </div><div class="bot-margin"></div>
			</div>
        </div>

	<!-- end main  -->
	<div class="t1-footer commonFooter" id="t1-footer">
    <table>
        <tbody>
            <tr>

                <td class="first">
                	<a href="#" tabindex="2501">Home</a>
                </td>
                <td>
                	<a href="#" tabindex="2502">About Us</a>
                </td>
                <td>
                	<a href="#" tabindex="2503">Contact Us</a>

                </td>
                <td>
                	<a href="#" tabindex="2504">Customer Service</a>
                </td>
            </tr>
        </tbody>
    </table>
</div>

	<!-- end container  -->
    </div> </div>
</body>
</html>
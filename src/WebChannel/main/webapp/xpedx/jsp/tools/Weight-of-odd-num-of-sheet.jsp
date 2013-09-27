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
<meta name="WT.ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.wtofoddnoofsheets.title" />'>
<meta name="DCSext.w_x_tools_ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.wtofoddnoofsheets.title" />'>
<!-- Webtrend tag stops --> 
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

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"> </script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/weight-odd<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"> </script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.wtofoddnoofsheets.title" /></title>
</head>
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
          <!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>"><s:text name="home.title" /></a> / <s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a> / <span class="breadcrumb-inactive"><s:text name="tools.wtofoddnoofsheets.title" /></span> Commented for jira 1538--> </div>
          <div id="mid-col-mil">
            <div style=" width: 600px;">
            <div class="clearview">&nbsp;</div>
              <h2> Weight of Odd Number of Sheets</h2>
              <p>To determine the weight of an odd number of sheets, multiply the M Weight by the number of sheets; divide the result by 1,000.</p>
              <div id="requestform">
                <div class="clearview">&nbsp;</div>
               	<form name="eform" method="post" action="sc_papcalcoddnumshee.aspx" id="eform" >
                  <table style="width:600px;" class="form">
                    <tbody>
                      <tr>
                        <td>Weight Per M Sheet:<br /></td>
                        <td colspan="3"><input name="Msheets" type="text" style="width:60px;" onkeyup="calcPhone('Value',eform.Msheets);" class="x-input" id="Msheets" /></td>
                      </tr>
                      <tr>
                        <td>Number of Sheets:<br /></td>
                        <td colspan="3"><input name="NumSheets" type="text" style="width:60px;" onkeyup="calcPhone('Value',eform.NumSheets);" class="x-input" id="NumSheets" /></td>
                      </tr>
                      <tr>
                        <td>&nbsp;</td>
                        <!--<td colspan="3"><ul id="cart-actions"><li><a href="#" onclick="javascript:validateForm();" class="green-ui-btn"><span>Calculate</span></a></li><li><a class="grey-ui-btn" href="#" onClick="javascript:document.eform.reset()"><span>Clear</span></a></li> Commented for jira 1538-->
                       <td colspan="3"><ul id="cart-actions"><li><a class="grey-ui-btn" href="#" onClick="javascript:document.eform.reset()"><span>Clear</span></a></li><li><a href="#" onclick="javascript:validateForm();" class="green-ui-btn"><span>Calculate</span></a></li>
                                
                            </ul></td>
                      </tr>
                      <tr >
                        <td width="195"><strong>Total Weight:</strong></td>
                        <td colspan="3" width="228"><input readonly="readonly"  name="Answer" style="border:none; color:#F00; font-weight:bold;" size="15">lbs.</td>
                      </tr>
                    </tbody>
                  </table>
                </form>
                <div class="clearview">&nbsp;</div>
              </div>
              <div class="clearview">
                <h2>Specifications</h2>
              </div>
              <div class="clearview">&nbsp;</div>
              <table style="width: 100%;" id="mil-list-new">
                <tbody>
                  <tr class="table-header-bar">
                    <td width="318" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Formula</span></td>
                    <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
                  </tr>
                  <tr>
                    <td align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                      <tbody>
                        <tr>
                          <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff"> Weight per M Sheet x No. of Sheets</td>
                        </tr>
                        <tr>
                          <td valign="middle" align="center" style="border-left: medium none; border-top: 1px solid #999;" class="noBorders-fff">1000 </td>
                        </tr>
                      </tbody>
                    </table></td>
                    <td width="38" valign="middle" class="noBorders-fff"> =</td>
                    <td width="228" valign="middle">Total Weight</td>
                  </tr>
                </tbody>
              </table>
              <div style="width: 100%;" id="table-bottom-bar">
                <div id="table-bottom-bar-L"></div>
                <div id="table-bottom-bar-R"></div>
              </div>
              <div class="clearview">&nbsp; </div>
              <table style="width: 100%;" id="mil-list-new">
                <tbody>
                  <tr class="table-header-bar">
                    <td width="318" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Example</span></td>
                    <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
                  </tr>
                  <tr>
                    <td colspan="3" align="left" >Find the weight of 1,365 sheets, 25x38, 60lb. 120M Offset   																	paper.</td>
                  </tr>
                  <tr>
                    <td align="right" class="noBorders-fff"><table cellspacing="0" cellpadding="0" class="noBorders-fff">
                      <tbody>
                        <tr>
                          <td valign="middle" align="center" style="border-left: medium none;" class="noBorders-fff">120 x 1,365</td>
                        </tr>
                        <tr>
                          <td valign="middle" align="center" style="border-left: medium none; border-top: 1px solid #999;" class="noBorders-fff"> 1000 </td>
                        </tr>
                      </tbody>
                    </table></td>
                    <td width="38" valign="middle" class="noBorders-fff"> =</td>
                    <td width="228" valign="middle">163.8 lbs.</td>
                  </tr>
                </tbody>
              </table>
              <div style="width: 100%;" id="table-bottom-bar">
                <div id="table-bottom-bar-L"></div>
                <div id="table-bottom-bar-R"></div>
              </div>
              <div class="clearview">&nbsp; </div>
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
      </div>
    </div>
</body>
</html>
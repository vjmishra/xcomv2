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
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-1.<s:property value='#wcUtil.xpedxBuildKey' />css" />
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

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript">
	
</script>

<style>

#requestform2{ 
	clear:both; 
	margin:auto;
	font-family:Arial, Helvetica, sans-serif; 
	}
	
#requestform2 .form table td{ 
border:solid 0px #999;
padding:10px;
	}
	
#requestform2 .form td table td{ 
border:solid 0px #999;  
padding:10px;
	}
	
#requestform2 .form td table td table td  { 
border:solid 1px #999;  
padding: 0px;
	}
	
</style>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:property value="wCContext.storefrontId" /> / Imposition Calculator</title>
</head>

<s:set name='_action' value='[0]' />
<s:set name='shtSizeW' value='#_action.getSheetSizeW()' />
<s:set name='shtSizeH' value='#_action.getSheetSizeH()' />
<s:set name='trmSizeW' value='#_action.getTrimSizeW()' />
<s:set name='trmSizeH' value='#_action.getTrimSizeH()' />
<s:set name='grpWidth' value='#_action.getGripperWidth()' />
<s:set name='clrBarWidth' value='#_action.getColorBarWidth()' />
<s:set name='sideGde' value='#_action.getSideGuide()' />
<s:set name='gtr' value='#_action.getGutter()' />
<s:set name='optn' value='#_action.getOption()' />
<s:set name='selectNumout' value='#_action.getSelectedNumout()' />
<s:set name='selectWaste' value='#_action.getSelectedWaste()' />

<body class="ext-gecko ext-gecko3">
    <div id="main-container">
        <div id="main">
        	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
        
			<div class="container"> 
                <!-- breadcrumb -->
                <div id="searchBreadcrumb">
                	<!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>">Home</a> / <a href="<s:url action="XPEDXTools" namespace="/xpedx/tools" includeParams='none'/>">Tools</a>  / <span class="page-title">Imposition Calculater</span> Commented For Jira 1538 -->
                </div>                               
                
                <div id="mid-col-mil">
                <div class="clearview">&nbsp;</div>
                <s:if test="#optn == 1">
                	<div class="clearview"><h2>Sheet Long Side / Trim Long Side</h2></div><div class="clearview">&nbsp;</div>
                </s:if>
                <s:if test="#optn == 2">
                	<div class="clearview"><h2>Sheet Long Side / Trim Short Side</h2></div><div class="clearview">&nbsp;</div>
                </s:if>
                <s:if test="#optn == 3">
                	<div class="clearview"><h2>Sheet Long Side / Trim Long Side use waste if possible</h2></div><div class="clearview">&nbsp;</div>
                </s:if>
                <s:if test="#optn == 4">
                	<div class="clearview"><h2>Sheet Long Side / Trim Short Side use waste if possible</h2></div><div class="clearview">&nbsp;</div>
                </s:if>
                
                <%
                	String pageDesign = (String)request.getAttribute("pageDetail");
                	response.getOutputStream().println(pageDesign);
                %>                             
                            
                
                <br/>
                <br/>
                
                
                <div class="clearview">&nbsp;</div> 
                     <table style="width: 100%;" id="mil-list-new">
                      <tbody><tr class="table-header-bar">
                       
                                
                      <s:if test="#optn == 1">
	                	<td width="187" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Sheet Long Side / Trim Long Side</span></td>
	                </s:if>
	                <s:if test="#optn == 2">	                	
	                	<td width="187" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Sheet Long Side / Trim Short Side</span></td>
	                </s:if>
	                <s:if test="#optn == 3">	                	
	                	<td width="187" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Sheet Long Side / Trim Long Side use waste if possible</span></td>
	                </s:if>
	                <s:if test="#optn == 4">
	                	<td width="187" class="no-border table-header-bar-left noBorders-blue"><span class="white txt-small">Sheet Long Side / Trim Short Side use waste if possible</span></td>
	                </s:if>          
                                
                                
                                <td colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small"> </span></td>
          					</tr>
                            <tr>
                                <td class="noBorders-fff padding8">Sheet Size:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{shtSizeW}' /> x <s:property value='%{shtSizeH}' /></td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Trim Size:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{trmSizeW}' /> x <s:property value='%{trmSizeH}' /></td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Gripper Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{grpWidth}' /></td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Color Bar Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{clrBarWidth}' /></td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Side Guide Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{sideGde}' /></td>
                            </tr>
                            <tr>
                                <td class="noBorders-fff padding8">Gutter Width:</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8"> :</td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{gtr}' /></td>
                            </tr>
                            <tr>
                              <td class="noBorders-fff padding8">Waste </td>
                              <td width="12" valign="middle" class="noBorders-fff padding8">=</td>
                              <td width="376" valign="middle" class="padding8"> <s:property value='%{selectWaste}' />%</td>
                            </tr>
                            <tr>
                              <td class="noBorders-fff padding8">Number Out = 	</td>
                              <td width="12" valign="middle" class="noBorders-fff padding8">= </td>
                              <td width="376" valign="middle" class="padding8"><s:property value='%{selectNumout}' /></td>
                            </tr>
               	      </tbody>
                  </table><div style="width: 100%;" id="table-bottom-bar">
            <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>  <div class="clearview">&nbsp; </div>   
                
                                      
                
                 <br/>
                <br/>
                
                
                
                <table border="1" cellspacing="0" cellpadding="2">
					<tr>
						<td BGCOLOR="#FFFFD7">&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;Trim</td>
					 
						<td BGCOLOR="#FFFFFF">&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;Unused Sheet</td>
					 
						<td BGCOLOR="#00FF00">&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;Color Bar</td>
					 
						<td BGCOLOR="#FF0000">&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;Gripper</td>
					 
						<td bgcolor="Blue">&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;Side Guide</td>
					 
						<td bgcolor="Silver">&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;Gutter Factor (position may vary)</TD>
					</tr>
				</table>
				
				<br />
				<br />
				
				<s:url id="viewCalculation" namespace="/xpedx/tools" action="xpedxViewImpCalculation">           			
           			<s:param name="sheetSizeW" value="#shtSizeW"/>
           			<s:param name="sheetSizeH" value="#shtSizeH"/>
           			<s:param name="trimSizeW" value="#trmSizeW"/>
           			<s:param name="trimSizeH" value="#trmSizeH"/>
           			<s:param name="gripperWidth" value="#grpWidth"/>
           			<s:param name="colorBarWidth" value="#clrBarWidth"/>
           			<s:param name="sideGuide" value="#sideGde"/>
           			<s:param name="gutter" value="#gtr"/>
           		</s:url>
				
				<ul id="cart-actions"><li><s:a href="%{#viewCalculation}" cssClass="green-ui-btn"><span>Back</span></s:a></li>
                </ul>
                            
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
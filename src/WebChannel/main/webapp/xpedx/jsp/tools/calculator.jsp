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
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript">
	
</script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<title><s:property value="wCContext.storefrontId" /> - <s:text name="tools.papercalculator.title" /></title>
<!-- Webtrend tag starts -->
<meta name="WT.ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.papercalculator.title" />'>
<meta name="DCSext.w_x_tools_ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name="tools.papercalculator.title" />'>
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
          		<!-- <a href="<s:url action="home" namespace="/home" includeParams='none'/>"><s:text name="home.title" /></a> / <s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a> / <span class="breadcrumb-inactive"><s:text name="tools.papercalculator.title" /></span> Commented for jira 1538-->
          		 </div>
	        <div id="mid-col-mil">
            <div style=" width: 1000px;">
               <div class="clearview">&nbsp;</div>
              <h2>Paper Calculator</h2>
              <br>
                 <p>To estimate the total pounds of paper needed for a job based on the information entered, the tool will calculate</p>
                 <p>total pounds needed. In order to get the most accurate estimate, enter all fields (waste % is optional)</p>
                 <p><strong>Note:</strong> Sizes should be entered as decimals.</p>
              <br />
              <div id="requestform">
                <div class="clearview">&nbsp;</div>
                
                <form name="calculator">
                  <table style="width:600px;" class="form paper-calc">
                    <tbody>
                      <tr>
                        <td>Page width: </td>
                        <td><input name="pagew" type="text" onkeyup="calcPhone('Value',calculator.pagew);" class="x-input width-80" id="pagew" /></td>
                      </tr>
                      <tr>
                        <td>Page height: </td>
                        <td><input name="pageh" type="text" class="x-input width-80" id="pageh" onkeyup="calcPhone('Value',calculator.pageh);"/></td>
                      </tr>
                      <tr>
                        <td>Number of pages: </td>
                        <td><input name="pages" type="text" class="x-input width-80" id="pages" onkeyup="calcPhone('Value',calculator.pages);"/></td>
                      </tr>
                      <tr>
                        <td>Number of pieces: </td>
                        <td><input name="pieces" type="text" class="x-input width-80" id="pieces" onkeyup="calcPhone('Value',calculator.pieces);"/></td>
                      </tr>
                      <tr>
                        <td>Waste: </td>
                        <td><input name="waste" type="text" class="x-input width-80" onkeyup="calcPhone('Value',calculator.waste);" id="waste" />
                          <strong>% </strong></td>
                      </tr>
                      <tr>
                        <td>Basis weight: </td>
                        <td><input name="weight" type="text" class="x-input width-80" id="weight" onkeyup="calcPhone('Value',calculator.weight);" /></td>
                      </tr>
                      <tr>
                        <td>Basis size: </td>
                        <td><span class="no-border-left">
                          <select style="width:90px;" name="size" id="size">
																																	
                            <option  selected="selected" value="">Select size</option>
                            <option value="374">17x22</option>
                            <option value="520">20x26</option>
                            <option value="641.25">22.5x28.5</option>
                            <option value="864">24x36</option>
                            <option value="726.75">25.5x28.5</option>
                            <option value="777.75">25.5x30.5</option>
                            <option value="950" >25x38</option>
                          </select>
                          <input type="hidden" id="txtstran" name="txtstran" />
                        </span></td>
                      </tr>
                      <tr>
                        <td colspan="2"><ul id="cart-actions">
                          <li><a class="grey-ui-btn" href="#" onClick="javascript:document.calculator.reset()"><span>Clear</span></a></li>
                          <li><a href="#" onclick="javascript:calc();" class="green-ui-btn"><span>Calculate</span></a></li>
                        </ul></td>
                      </tr>
                      <tr >
                        <td width="268"><strong>Paper Estimate: </strong></td>
                        <td width="320"><div id="Layer1" style="FONT-WEIGHT: bold; WIDTH: 239px; COLOR: #ff0000; HEIGHT: 19px">Paper
                        <!--Start- Jira 3109  --> 
                          Estimate: 0 Pounds </div></td>
                          <!--End- Jira 3109  -->
                      
                      </tr>
                    </tbody>
                  </table>
                </form>
                <div class="clearview">&nbsp;</div>
              </div>
              <div class="clearview">&nbsp;</div>
              <div class="x-corners">
                <div ><strong>Note:</strong><br />
                  The results of the Interactive Calculations System are estimates and are not guaranteed by International Paper.<br />
                  <br />
                </div>
              </div>
            </div>
            <div class="bot-margin"></div>
          </div>
        </div>
        <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
        <!-- end main  -->
        
        <!-- end container  -->
      </div>
    </div>
    <script>  
//Added For Jira 3109
    function resetPromptForCalc () {
    	document.getElementById("pagew").style.borderColor="";	
    	document.getElementById("pageh").style.borderColor="";	
    	document.getElementById("weight").style.borderColor="";
    	document.getElementById("pages").style.borderColor="";
    	document.getElementById("pieces").style.borderColor="";
    	document.getElementById("waste").style.borderColor="";
    	document.getElementById("size").style.borderColor="";
    	
    }
    
    function validateblank(){
    	var errorflag= false;
    	/* if (document.calculator.pagew.value == "" || document.calculator.pageh.value == "" || document.calculator.weight.value == "" || document.calculator.pages.value == ""
			|| document.calculator.pieces.value ==""|| document.calculator.waste.value == "" || document.calculator.size.value =="")
	{
		
		
	
	} */
    	if (document.calculator.pagew.value == ""){
    		document.getElementById("pagew").style.borderColor="#FF0000";
    		document.calculator.pagew.focus();
    		errorflag= true;
    	}
    	if (document.calculator.pageh.value == ""){
    		document.getElementById("pageh").style.borderColor="#FF0000";
    		document.calculator.pageh.focus();
    		errorflag= true;
    	}	
    	if (document.calculator.weight.value == ""){
    		document.getElementById("weight").style.borderColor="#FF0000";
    		document.calculator.weight.focus();
    		errorflag= true;
    	}	
    	if (document.calculator.pages.value == ""){
    		document.getElementById("pages").style.borderColor="#FF0000";
    		document.calculator.pages.focus();
    		errorflag= true;
    	}	
    	if (document.calculator.pieces.value == ""){
    		document.getElementById("pieces").style.borderColor="#FF0000";
    		document.calculator.pieces.focus();
    		errorflag= true;
    	}	
    	if (document.calculator.waste.value == ""){
    		document.getElementById("waste").style.borderColor="#FF0000";
    		document.calculator.waste.focus();
    		errorflag= true;
    	}
    	if (document.calculator.size.value == ""){
    		document.getElementById("size").style.borderColor="#FF0000";
    		document.calculator.size.focus();
    		errorflag= true;
    	}
		if(errorflag)
			{
			alert("Required fields missing. Please review and try again.");
			return true;
			}
    	
    }
    function calc()


{  //Added For Jira 3109
    	resetPromptForCalc();
	if(validateblank())
	{
		return false;
	}
	
	if (isNaN(document.calculator.pagew.value))
		{
		width = 0;
			document.calculator.pagew.value = '';
		}
	else
		{	width = document.calculator.pagew.value;
			
		}
	if (isNaN(document.calculator.pageh.value))
		{
			height = 0;
			document.calculator.pageh.value = '';
		}
	else
		{
			height = document.calculator.pageh.value;
		}
	if (isNaN(document.calculator.weight.value))
		{
			weight = 0;
			document.calculator.weight.value = '';
		}
	else
		{
			weight = document.calculator.weight.value;
		}
	if (isNaN(document.calculator.pages.value))
		{
			pages = 0;
			document.calculator.pages.value = '';
		}
	else
		{
			pages = document.calculator.pages.value;
		}
	if (isNaN(document.calculator.pieces.value))
		{
			pieces = 0;
			document.calculator.pieces.value = '';
		}
	else
		{
			pieces = document.calculator.pieces.value;
		}
	if (isNaN(document.calculator.waste.value))
		{
			waste = 0;
			document.calculator.waste.value = '';
		}
	else
		{
			waste = 1 + (document.calculator.waste.value / 100);
		}
	basis_size = document.calculator.size.value
	result = ((width * height)/ basis_size ) * (weight / 1000) * pages * pieces * waste;
	result = Math.ceil(result * 100);
	result = result / 100
	//usd-3634239 Prashant Pandey/EDS
	//if ((obj=MM_findObj('Layer1'))!=null) with (obj)

	//innerHTML = unescape('Paper Estimate: ' + result + ' pounds');
	document.getElementById('Layer1').innerHTML='Paper Estimate: ' + result + ' pounds';
	

}



function MM_findObj(n, d) { //v3.0

  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {

    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}

  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];

  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document); return x;

}



function MM_setTextOfLayer(objName,x,newText) { //v3.0

  if ((obj=MM_findObj(objName))!=null) with (obj)

   // if (navigator.appName=='Netscape') {document.write(unescape(newText)); document.close();}

    //else 

	 innerHTML = unescape(newText);

}

</script>
</body>
</html>
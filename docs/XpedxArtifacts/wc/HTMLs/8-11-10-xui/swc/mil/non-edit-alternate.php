<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/swc.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/theme-xpedx.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/xpedx-quick-add.css"/>
<!-- javascript -->
<script type="text/javascript" src="../../js/global/ext-base.js"></script>
<script type="text/javascript" src="../../js/global/ext-all.js"></script>
<script type="text/javascript" src="../../js/global/validation.js"></script>
<script type="text/javascript" src="../../js/global/dojo.js"></script>
<script type="text/javascript" src="../../js/global/dojoRequire.js"></script>
<script type="text/javascript" src="../../js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="../../js/catalog/catalogExt.js"></script>
<script type="text/javascript" src="../../js/swc.js"></script>

<!-- carousel scripts css  -->
<link media="all" type="text/css" rel="stylesheet" href="../../js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../js/jcarousel/skins/xpedx/skin.css" />
<script type="text/javascript" src="../../js/jcarousel/xpedx-custom-carousel.js"></script>
<!-- carousel scripts js   -->


<script type="text/javascript" src="../../js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../../js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="../../js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="../../js/jquery.dropdownPlain.js"></script>

<link media="all" type="text/css" rel="stylesheet" href="../modals/checkboxtree/demo.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../modals/checkboxtree/jquery.checkboxtree.css"/>


<script type="text/javascript" src="../modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="../../js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="../../js/quick-add/quick-add.js"></script>


<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="../../js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="../../js/fancybox/jquery.fancybox-1.3.1.js"></script>

<link rel="stylesheet" type="text/css" href="../../js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../../js/fancybox/jquery.fancybox-1.3.1.css" media="screen" />

<!-- Page Calls -->
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
		$("#varous1").fancybox();

$("#various2").fancybox();
$("#various3").fancybox();
$("#various4").fancybox();


});
	
	
	
</script>
<script type="text/javascript">
        //<!--
            $(document).ready(function() {
                $('#tree').checkboxTree();
                $('#collapseAllButtonsTree').checkboxTree({
                    collapseAllButton: 'Collapse all',
                    expandAllButton: 'Expand all'
                });
            });
        //-->
        </script>
        
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>

<title>xpedx / Non-Editable View</title>
</head>
<!-- END swc:head -->
<body class="  ext-gecko ext-gecko3">
<div id="main-container">
    <div id="main">
        <?php include '../includes/header.php'; ?>
        <div class="container">
            
            
            <!-- breadcrumb -->
            <div id="breadcumbs-list-name">
            	<a href="#">Home</a> / <a href="#">My Items Lists</a> / <span class="breadcrumb-inactive"> Mike's Items</span>
                <a href="#"><span class="print-ico-xpedx"><img src="../../images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print This Page</span></a>
            </div>
            
            
            <div id="mid-col-mil">
                <div class="mil-edit">
                    <!--
                    <div class="ui-widget">
                        <div class="ui-state-highlight ui-corner-all"> 
                        	<p><span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
                        	<strong>Alert!</strong> Some items on this list are not currently available. Select alternate items below.</p>
                        </div>
                    </div>
                    -->
                    <div class="x-corners response-msg error">
                        <p class="msg" style="width: 100%;"><b>Alert!</b> Some items on this list are not currently available. Select alternate items below.</p>
                        <div class="clearall"></div>
                    </div>
                    <h2 class="mil-title">Mike's Items </h2>
                    <ul id="tool-bar" class="tool-bar-top">
                        <li><a href="#">Select All</a></li>
                        <li><a href="#">Export List</a></li>
                        <li><a class="grey-ui-btn" href="#"><span>Edit This List</span></a></li>
                        <li><a class="grey-ui-btn" href="#"><span>Update Price & Availability</span></a></li>
                        <li><a href="#"><img src="../../images/theme/theme-1/mil/add-items-to-cart.gif" width="124" height="22" alt="Add Items to Cart" title="Add Items to Cart" /></a></li>
                    </ul>
                </div>
                <!-- Close mil-edit -->
                <br />
                <table id="mil-list">
                    <tbody>
                        <tr id="none" style="background:none;">
                            <td class="no-border" width="319"><span class="white"> Product Description </span></td>
                            <td class="no-border" align="center" width="50"><span class="white"> Quantity</span></td>
                            <td class="no-border" align="center"><span class="white">UOM</span></td>
                            <td class="no-border" align="center"><span class="white"> Availability </span></td>
                            <td class="no-border" width="55" align="center" ><span class="white"> My Price (usd)</span></td>
                            <td id="etended-price" class="no-border-right" align="center" ><span class="white"> Extended Price (usd) </span></td>
                        </tr>
                        <tr>
                            <td><div class="prod_grid_img"> <img src="../../images/catalog/products/prod-img-small.jpg" />
                                    <input type="checkbox" name="option2" />
                                </div>
                                <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case)<br />
                                <br />
                                xpedx # 23453241</a></td>
                            <td class="createdby-lastmod"><input title="QTY" tabindex="12" id="QTY" value="2" class="input-label" name="QTY" /></td>
                            <td class="createdby-lastmod"><span>
                                <select name="select" class="xpedx_select_sm">
                                    <option selected="selected" value="5">Carton (1,200)</option>
                                    <option value="8"> Case (24)</option>
                                </select>
                                <br />
                                <br />
                                <label class="left35">Job #
                                    <input name="Text" class="input-details-cart"  id="Text3" tabindex="12" title="JobNumber" />
                                </label>
                                <br />
                                <br />
                                <label>Customer #</label>
                                <input name="CustyNum" class="input-details-cart"  id="Text2" tabindex="12" title="CustomerNumber" />
                                </span></td>
                            <td class="actions"><div>
                                    <div class="float-left">
                                        <p>Availability</p>
                                        <p> Charlotte NC:</p>
                                        <p>Next Day:</p>
                                        <p>2+ Days:</p>
                                        <p>Total</p>
                                    </div>
                                </div>
                                <div class="float-left">
                                    <p>Carton (500)</p>
                                    <p> 0</p>
                                    <p>15</p>
                                    <p>12</p>
                                    <p>27</p>
                                </div></td>
                            <td class="actions">$72.27</td>
                            <td class="actions">$72.27</td>
                        </tr>
                        <tr class="odd">
                            <td><div class="prod_grid_img"><img src="../../images/catalog/products/prod-img-small.jpg" />
                                    <input type="checkbox" name="option2" />
                                </div>
                                <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case)<br />
                                <br />
                                xpedx # 23453241</a></td>
                            <td class="createdby-lastmod"><input title="QTY" tabindex="12" id="QTY" value="2" class="input-label" name="QTY" /></td>
                            <td class="createdby-lastmod"><span>
                                <select name="select" class="xpedx_select_sm">
                                    <option selected="selected" value="5">Carton (1,200)</option>
                                    <option value="8"> Case (24)</option>
                                </select>
                                <br />
                                <br />
                                <label class="left35">Job #
                                    <input name="Text" class="input-details-cart"  id="Text3" tabindex="12" title="JobNumber" />
                                </label>
                                <br />
                                <br />
                                <label>Customer #</label>
                                <input name="CustyNum" class="input-details-cart"  id="Text2" tabindex="12" title="CustomerNumber" />
                                </span></td>
                            <td class="availability">
                            	<!--
                                <div class="ui-widget">
                                <div class="ui-state-highlight ui-corner-all"> 
                                    <p><span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
                                    This item is not currently available. <br />                 <a href="../modals/alternate-items-.html" id="various2"><span>View Alternate Items</span></a>
                                    </p>
                                </div>
                                </div>
                                -->
                                <div class="x-corners response-msg response-msg-small notice">
                                	<p class="msg" style="width: 100%;">This item is not currently available.<br /><a href="../modals/alternate-items-.html" id="various2"><span>View Alternate Items</span></a>
                                	<div class="clearall"></div>
                                </div>
                            </td>
                            <td class="actions">$72.27</td>
                            <td class="actions">$72.27</td>
                        </tr>
                        <tr>
                            <td><div class="prod_grid_img"><img src="../../images/catalog/products/prod-img-small.jpg" />
                                    <input type="checkbox" name="option2" />
                                </div>
                                <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case)<br />
                                <br />
                                xpedx # 23453241</a></td>
                            <td class="createdby-lastmod"><input title="QTY"  tabindex="12" id="QTY" value="2" class="input-label" name="QTY" /></td>
                            <td class="createdby-lastmod"><span>
                                <select name="select" class="xpedx_select_sm">
                                    <option selected="selected" value="5">Carton (1,200)</option>
                                    <option value="8"> Case (24)</option>
                                </select>
                                <br />
                                <br />
                                <label class="left35">Job #
                                    <input name="Text" class="input-details-cart"  id="Text3" tabindex="12" title="JobNumber" />
                                </label>
                                <br />
                                <br />
                                <label>Customer #</label>
                                <input name="CustyNum" class="input-details-cart"  id="Text2" tabindex="12" title="CustomerNumber" />
                                </span></td>
                            <td class="availability">
                                <!--
                                <div class="ui-widget">
                                    <div class="ui-state-highlight ui-corner-all"> 
                                        <p><span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
                                        This item is not currently available. <br />  
                                        <a href="../modals/alternate-items-.html" id="various3"><span>View Alternate Items</span></a>
                                        </p>
                                    </div>
                                </div>
                                -->
                                <div class="x-corners response-msg response-msg-small notice">
                                	<p class="msg" style="width: 100%;">This item is not currently available.<br /><a href="../modals/alternate-items-.html" id="various3"><span>View Alternate Items</span></a>
                                	<div class="clearall"></div>
                                </div>
                            </td>
                            <td class="availability">$72.27</td>
                            <td class="availability"> $72.27 </td>
                        </tr>
                    </tbody>
                </table>
                </tbody>
                <div class="mil-bot"></div>
                <ul id="tool-bar" class="tool-bar-bottom">
                    <li><a href="#">Select All</a></li>
                    <li><a href="#">Export List</a></li>
                    <li><a class="grey-ui-btn" href="#"><span>Edit This List</span></a></li>
                    <li><a class="grey-ui-btn" href="#"><span>Update Price & Availability</span></a></li>
                    <li><a href="#"><img src="../../images/theme/theme-1/mil/add-items-to-cart.gif" width="124" height="22" alt="Add Items to Cart" title="Add Items to Cart" /></a></li>
                </ul>
                <div class="clearview">&nbsp;</div>
            </div>
            <!-- START Carousel -->
            <div class="mil-cart-bg">
                <div id="cross-sell" class="float-left">
                    <h2>Related Items</h2>
                    <ul id="footer-carousel-left" class="jcarousel-skin-xpedx">
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-1.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-2.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-3.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-1.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-2.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-3.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-1.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-2.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-3.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                    </ul>
                </div>
                <div id="cross-sell" class="float-left">
                    <h2>Special Offers</h2>
                    <ul id="footer-carousel-right" class="jcarousel-skin-xpedx">
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-1.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-2.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-3.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-1.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-2.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-3.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-1.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-2.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                        <li> <a href="#"> <img src="../../images/catalog/carousel-demo-3.jpg" width="91" height="94" alt="" /> Product Details </a> </li>
                    </ul>
                </div>
            </div>
            <!-- END carousel -->
        </div>
    </div>
</div>
<!-- end main  -->
<?php include '../includes/footer.php'; ?>
</div>
<!-- end container  -->
</body>
</html>
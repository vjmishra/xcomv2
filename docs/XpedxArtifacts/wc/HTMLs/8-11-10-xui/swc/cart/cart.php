﻿<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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

<!-- carousel scripts css  -->
<link media="all" type="text/css" rel="stylesheet" href="../../js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../js/jcarousel/skins/xpedx/skin.css" />

<!-- carousel scripts js   -->
<script type="text/javascript" src="../../js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="../../js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../../js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="../../js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="../../js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="../../js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="../../js/quick-add/quick-add.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="../../js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<script src="../../js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="../../js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
		$('#Availability_Hover').bt({
			ajaxPath: '../tool-tips/cart-availability-hover.html div#tool-tip-content',
			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
			fill: '#ebebeb',
			cssStyles: {color: 'black'},
			padding: 20,
			spikeLength: 10,
  			spikeGirth: 15,
			cornerRadius: 6,
			shadow: true,
			shadowOffsetX: 0,
			shadowOffsetY: 3,
			shadowBlur: 3,
			shadowColor: 'rgba(0,0,0,.4)',
			shadowOverlap: false,
			strokeWidth: 1,
  			strokeStyle: '#FFFFFF',
			noShadowOpts:     {strokeStyle: '#969696'},
			positions: ['top']
		});

	});
</script>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>

<title>xpedx / Cart</title>
</head>
<!-- END swc:head -->
<body class="  ext-gecko ext-gecko3">
<div id="main-container">
    <div id="main">
        <?php include '../includes/header.php'; ?>
            <div class="container">
                <!-- breadcrumb -->
                
                <div id="breadcumbs-list-name"><a href="#">Orders</a> / <span class="breadcrumb-inactive">Pete's Cart</span> 
                
                <a href="#"><span class="print-ico-xpedx"><img src="../../images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print Page</span></a>
                </div>
                <div id="mid-col-mil">
                    <div class="mil-edit">
                    
                    	<div id="order-numbers">
                    		<ul>
                            	<li><b>Web Confirmation Number:</b> 630101</li>
                            	<li><b>Order Number:</b> 12345-00</li>
                            </ul>
                    	</div>
                        
                        
                    	<div class="ad-float">
                    		<a href="#"><img width="234" height="60" title="Windex Ad" alt="Windex Ad" src="../../images/catalog/products/windex-ad-mil.jpg"></a>
                		</div>
                        <div class="quick-add float-left">
                            <h2 style="float:left;">Quick Add</h2>
                            <p class="quick-add-aux-links"><a href="#" id="various2">Quick Import</a></p>
                            <div class="clear">&nbsp;</div>
                            <div class="quick-add-form quick-add-form-mil">
                                <div class="quick-add-form-top">&nbsp;</div>
                                <form id="quick-add-selector" class="form selector">
                                    <ul class="hvv">
                                        <li>
                                            <label>Item Type</label>
                                            <select name="type">
                                                <option value="xpedx">xpedx #</option>
                                                <option value="manufacturer">Manufacturer #</option>
                                            </select>
                                        </li>
                                        <li>
                                            <label>Item #</label>
                                            <input type="text" name="item">
                                        </li>
                                        <li>
                                            <label>Quantity</label>
                                            <input type="text" class="qty-field" name="qty">
                                        </li>
                                        <li>
                                            <label>Job #</label>
                                            <input type="text" name="job">
                                        </li>
                                        <li>
                                            <label>&nbsp;</label>
                                            <input type="image" src="../../images/theme/theme-1/quick-add/addtoquicklist.png">
                                        </li>
                                    </ul>
                                </form>
                                <form style="display: none;" class="form quick-add-to-cart-form">
                                    <table cellspacing="0" cellpadding="0">
                                        <thead>
                                            <tr>
                                                <th class="del-col">&nbsp;</th>
                                                <th class="first-col-header col-header type-col">Item Type</th>
                                                <th class="col-header item-col">Item #</th>
                                                <th class="col-header qty-col">Quantity</th>
                                                <th class="col-header uom-col">UOM</th>
                                                <th class="last-col-header col-header job-col">Job#</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                    <input type="image" class="add-to-cart-btn" src="../../images/theme/theme-1/quick-add/addtocart.png">
                                </form>
                                <div class="clear">&nbsp;</div>
                            </div>
                        </div>
                    </div>
                    <!-- Close mil-edit -->
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <a href="#" class="txt-small">Select All</a>
                    
                    
                    
                    <table id="mil-list">
                        <tbody>
                            <tr id="none" style="background:none; height: 32px;">
                                <td class="no-border" width="180"><span class="white txt-small">Product Description</span></td>
                                <td class="no-border" align="center"><span class="white txt-small">Quantity</span></td>
                                <td class="no-border" align="center"><span class="white txt-small">UOM</span></td>
                                <td class="no-border" align="center"><span class="white txt-small">Availability</span></td>
                                <td class="no-border" align="center" ><span class="white txt-small">My Price (usd)</span></td>
                                <td class="no-border" align="center" ><span class="white txt-small">Extended Price (usd)</span></td>
                                <td class="no-border" align="center" ><span class="white txt-small">Adjustments</span></td>
                                <td class="no-border-right" align="center"><span class="white txt-small">Line Total (usd)</span></td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="prod_grid_img-cart"> <img src="../../images/catalog/products/prod-img-small.jpg">
                                        <input type="checkbox" name="option2">
                                    </div>
                                    <a href="#">Jiffy Padded Mailers, 4" W x 8" L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case)<br /><br /> xpedx # 23453241</a>
                                    <div id="special-instructions">
                                    	<p>Special Instruction: <a href="#">Add</a></p>
                                    </div>
                                </td>
                                <td valign="top">
                                	<input title="QTY" tabindex="12" id="QTY" value="2" class="input-label input-numeric x-input" name="QTY" />
                                </td>
                                <td valign="top">
                                    <select name="select" class="xpedx_select_sm">
                                        <option selected="selected" value="5">Carton (500)</option>
                                        <option value="8">Case (24)</option>
                                    </select>
                                    <br />
                                    <br />
                                    <p class="p-black txt-left form-label-top">Job #</p>
                                    <input title="JobNumber" style="width:90px;" tabindex="12" id="Text3" class="input-details-cart input-numeric x-input" name="Text">
                                </td>
                                <td valign="top"><a href="#" id="Availability_Hover" bt-xtitle="" title="">Immediate</a></td>
                                <td valign="top" align="right" width="85">
                                	$72.27/TH<br />
                                    $50.15/CA
                                </td>
                                <td valign="top" align="right" width="120">$72.27</td>
                                <td valign="top"></td>
                                <td valign="top" align="right">$144.54</td>
                            </tr>
                            <tr class="odd">
                                <td>
                                    <div class="prod_grid_img-cart"> <img src="../../images/catalog/products/prod-img-small.jpg">
                                        <input type="checkbox" name="option2">
                                    </div>
                                    <a href="#">Jiffy Padded Mailers, 4" W x 8" L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case)<br /><br /> xpedx # 23453241</a>
                                    <div id="special-instructions">
                                    	<p>Special Instruction: <a href="#">Add</a></p>
                                    </div>
                                </td>
                                <td valign="top">
                                	<input title="QTY" tabindex="12" id="QTY" value="2" class="input-label input-numeric x-input" name="QTY" />
                                </td>
                                <td valign="top">
                                    <select name="select" class="xpedx_select_sm">
                                        <option selected="selected" value="5">Carton (500)</option>
                                        <option value="8">Case (24)</option>
                                    </select>
                                    <br />
                                    <br />
                                    <p class="p-black txt-left form-label-top">Job #</p>
                                    <input title="JobNumber" style="width:90px;" tabindex="12" id="Text3" class="input-details-cart input-numeric x-input" name="Text">
                                </td>
                                <td valign="top"><a href="#">Immediate</a></td>
                                <td valign="top" align="right" width="85">
                                	$72.27/TH<br />
                                    $50.15/CA
                                </td>
                                <td valign="top" align="right" width="120">$72.27</td>
                                <td valign="top"></td>
                                <td valign="top" align="right">$144.54</td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="prod_grid_img-cart"> <img src="../../images/catalog/products/prod-img-small.jpg">
                                        <input type="checkbox" name="option2">
                                    </div>
                                    <a href="#">Jiffy Padded Mailers, 4" W x 8" L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case)<br /><br /> xpedx # 23453241</a>
                                    <div id="special-instructions">
                                    	<p>Special Instruction: <a href="#">Add</a></p>
                                    </div>
                                </td>
                                <td valign="top">
                                	<input title="QTY" tabindex="12" id="QTY" value="2" class="input-label input-numeric x-input" name="QTY" />
                                </td>
                                <td valign="top">
                                    <select name="select" class="xpedx_select_sm">
                                        <option selected="selected" value="5">Carton (500)</option>
                                        <option value="8">Case (24)</option>
                                    </select>
                                    <br />
                                    <br />
                                    <p class="p-black txt-left form-label-top">Job #</p>
                                    <input title="JobNumber" tabindex="12" style="width:90px;" id="Text3" class="input-details-cart input-numeric x-input" name="Text">
                                </td>
                                <td valign="top"><a href="#">Immediate</a></td>
                                <td valign="top" align="right" width="85">
                                	$72.27/TH<br />
                                    $50.15/CA
                                </td>
                                <td valign="top" align="right" width="120">$72.27</td>
                                <td valign="top"></td>
                                <td valign="top" align="right">$144.54</td>
                            </tr>
                    	</table>
                    </tbody>
                    <div id="table-bottom-bar">
                        <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>
                    <div class="clearview">&nbsp;</div>
                    
                    
                    <div id="order-btm-left">
                    	<div id="selected-items-manager">
                        	<p class="for-selected">For Selected Items</p>
                        	<select class="xpedx_select_sm float-left" name="select" style="margin-right: 15px;">
                            	<option value="5" selected="selected">Add Items to List</option>
                            	<option value="8">Item #1</option>
                        	</select>
                        	<a href="#" class="grey-ui-btn"><span>Edit This Cart</span></a>
                    	</div>
                	</div>
                    
                    
                    <!-- Pricing -->
                    <div id="order-pricing">
                        <ul>
                            <li>Subtotal Items:</li>
                            <li>Order Total Adjustments:</li>
                            <li>Adjusted Subtotal:</li>
                            <li>Tax:</li>
                            <li>Shipping Costs:</li>
                        </ul>
                        <ul class="txt-right price">
                            <li>$344.46</li>
                            <li>$0.00</li>
                            <li>$344.46</li>
                            <li class="txt-grey-1">To be determined</li>
                            <li class="txt-grey-1">To be determined</li>
                        </ul>
                        <div class="clearview">&nbsp;</div>
                        <br />
                        
                        <div id="order-total-cost">
                        	<div class="order-total-left-bg"></div>
                        	<div class="float-left">
                            	<ul style="width: 150px;">
                                	<li><h2>Order Total:</h2></li>
                                </ul>
                                <ul>
                                	<li style="float: right; text-align: right;"><h2 style="color: #083188">$344.46</h2></li>
                                </ul>
                            </div>
                            <div class="order-total-right-bg"></div>
                        </div>
                        
                    </div>
                    <div class="clearview">&nbsp;</div>
                    
                        
                        
                        <div id="cart-actions" class="float-right">    
                            <ul id="cart-actions">
                                <li>
                                	<select class="xpedx_select_sm float-left" name="select" style="margin-right: 15px;">
                            			<option value="5" selected="selected">Add Items to List</option>
                            			<option value="8">Item #1</option>
                        			</select>
                                </li>
                                <li><a class="grey-ui-btn" href="#"><span>Update Cart</span></a></li>
                               	<li><a href="#" class="green-ui-btn"><span>Save</span></a></li>
                                <li><a class="orange-ui-btn" href="#"><span>Checkout</span></a></li>
                            </ul>
                        </div>
                        
                        
                        
                	</div>
                    <!-- End Pricing -->
                    
                    
                    
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    
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
                    
                    
                    <br />
                </div>
            </div>
        </div>
    </div>
    <!-- end main  -->
    <?php include '../includes/footer.php'; ?>
</div>
<!-- end container  -->
</body>
</html>
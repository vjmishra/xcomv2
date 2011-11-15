<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<!-- START swc:head -->
<!--cssMode=minified-->
<!--jsMode=minified-->
<!-- START wctheme.head.ftl -->
<!-- END head.ftl -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/swc.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/theme-xpedx.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/xpedx-mil.css" />

<!-- jQuery -->
<link type="text/css" href="http://jqueryui.com/themes/base/jquery.ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../../js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="../../js/pngFix/jquery.pngFix.pack.js"></script>


<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="../../js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="../../js/fancybox/jquery.fancybox-1.3.1.js"></script>
<link rel="stylesheet" type="text/css" href="../../js/fancybox/jquery.fancybox-1.3.1.css" media="screen" />


<script type="text/javascript" src="http://jqueryui.com/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="http://jqueryui.com/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="http://jqueryui.com/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
		$("#ship-to-address").fancybox({
			'titleShow'			: false,
			'transitionIn'		: 'fade',
			'transitionOut'		: 'fade'
		});
		$("#edit-cart").fancybox({
			'titleShow'			: false,
			'transitionIn'		: 'fade',
			'transitionOut'		: 'fade'
		});
		$(".datepicker").datepicker({
			showOn: 'button',
						numberOfMonths: 1,

			buttonImage: '../../images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
		});
	});
</script>


<title>xpedx / Checkout</title>
</head>
<!-- END swc:head -->
<body class="ext-gecko ext-gecko3">
    <div id="main-container">
        <div id="main">
        	<?php include '../includes/header.php'; ?>
            <div class="container"> 
                <!-- breadcrumb -->
                <div id="breadcumbs-list-name">
                	<a href="#">Home</a> / <span class="breadcrumb-inactive">Order Summary</span>
                    
                    <a href="#"><span class="print-ico-xpedx"><img src="../../images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print This Page</span></a>
                    <a href="#"><span class="print-ico-xpedx"><img width="16" height="15" alt="Print This Page" src="../../images/common/email-icon.gif">Email Page</span></a>
                </div>
                <div id="mid-col-mil">
                
                    <ul class="tool-bar-top" id="tool-bar">
                        <li><a href="#" class="grey-ui-btn"><span>Edit Cart Details</span></a></li>
                    	<li style="float: right;"><a href="#" class="orange-ui-btn"><span>Submit Order</span></a></li>
                    </ul>
                    <br />
                    
                    <p class="float-right"><b>Total Price: $259.97</b>  &nbsp;&nbsp;<a href="#" class="p-orange-lnk">View Details</a></p>
                	<h2 class="table-hdr">Shipping Information</h2>
                    <table id="x-tbl-cmmn">
                        <tbody>
                            <tr class="table-header-bar">
                                <td class="no-border table-header-bar-left" align="left" width="300"><span class="white">Ship To: &nbsp;&nbsp;<a href="../modals/choose-a-ship-to-address.html" id="ship-to-address">Change</a></span></td>
                                <td class="no-border-right table-header-bar-right" align="left"><span class="white">Shipping Options</span></td>
                            </tr>
                            <tr  style="margin-bottom:-15px;">
                                
                                <td valign="top">
                                    <b>Metro Graphics1123</b><br />
                                    Clement AveCharlotte, NC<br />
                                    28205United States<br />
                                    <br />
                                    <br />
                                    Attention:
                                    <input class="x-input" id="Sales" />
                                </td>
                                
                                <td valign="top" align="left">
                                   <p class="mil-edit-forms-label">You will receive your order on your normal delivery schedule unless you select a different shipping option below:</p>
                                    <br />
                                    <form class="shipping-options">
                                        <ul>
                                            <li style="margin-top:4px;">
                                                <input type="checkbox" class="checkbox">
                                            </li>
                                        <li>Place Order on Hold until:&nbsp; <input type="text" style="margin-left:83px;" class="x-input datepicker" value="3/23/15" size="14">

                                            </li>
                                        </ul>
                                        <ul>
                                            <li>
                                                <input type="checkbox" class="checkbox">
                                            </li>
                                            <li>Ship Order Complete (will not ship until all items are available)</li>
                                        </ul>
                                        <ul>
                                            <li>
                                                <input type="checkbox" class="checkbox">
                                            </li>
                                            <li>Will Call – Pick up at [ insert xpedx address]. (Allow X hours for processing. A Customer Service Representative will contact you.)</li>
                                        </ul>
                                        <ul>
                                            <li style="height: 30px; margin-top:4px;">
                                                <input type="checkbox" class="checkbox">
                                            </li>
                                            <li><b>Rush Order:</b> Requested Delivery Date: &nbsp;
                                                <input type="text" class="x-input datepicker" value="3/23/15" size="14">
                                               * Additional Charges May Apply
                                            </li>
                                            &nbsp;
                                        </ul>
                                    </form>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div id="table-bottom-bar">
                        <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>
                    <br />
    
                
                    <h2 class="table-hdr">Payment Information</h2>
                    <table id="x-tbl-cmmn">
                        <tbody>
                            <tr class="table-header-bar">
                                <td class="no-border table-header-bar-left" align="left" width="300"><span class="white">Customer PO:</span></td>
                                <td class="no-border" align="left" width="300"><span class="white">Bill to Address on FIle:</span></td>
                                <td class="no-border-right table-header-bar-right" align="left"><span class="white">Payment Type:</span></td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <p>Customer PO:</p>
                            		<input class="input-numeric x-input" title=" " /><br /><br />
                                    <input type="checkbox" class="checkbox" id="Sales">
                                    <label class="option-label txt-sml-gry" for="Sales">Check to save new addresses</label>
                                    <br />
                                    <br />
                                    <select class="xpedx_select_sm">
                                        <option>Select Existing PO#</option>
                                        <option>1234</option>
                                        <option>5678</option>
                                        <option>9012</option>
                                 	</select>
                                </td>
                                <td valign="top" align="left">
                                    <b>Metro Graphics1123</b><br />
                                    Clement AveCharlotte, NC<br />
                                    28205United States
                                </td>
                                <td valign="top" align="left">
                                	<form>
                                    <input type="radio" class="radio-rght" value="1" name="pay">
                                    <label class="option-label" for="Sales">Pay on Account</label>
                                    <br />
                                    <br />
                                    
                                    <input type="radio" class="radio-rght" value="2" name="pay">
                                    <label class="option-label" for="Sales">Pay with Credit Card</label>
                                    &nbsp;&nbsp;&nbsp;<a href="../modals/edit-cart-card.html" id="edit-cart" class="txt-lnk-sml-1">Edit Card</a>
                                    <br />
                                    <br />
                                    </form>
                                    
                                    <div id="table-avail-col">
                                        <ul class="txt-right">
                                            <li><b>Cardholder Name: </b></li>
                                            <li><b>Card Number: </b></li>
                                            <li><b>Card Type: </b></li>
                                            <li><b>Expiration Date: </b></li>
                                        </ul>
                                        <ul class="txt-grey-1">
                                            <li>Mike Jones</li>
                                            <li>**** **** **** 4567</li>
                                            <li>VISA</li>
                                            <li>01/2015</li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div id="table-bottom-bar">
                        <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>
                    <br />
         
         
                    
                    <h2 class="table-hdr">Additional Instructions</h2>
                    <table id="x-tbl-cmmn">
                        <tbody>
                            <tr class="table-header-bar">
                                <td class="no-border table-header-bar-left" align="left" width="400"><span class="white">Order Comments</span></td>
                                <td class="no-border-right table-header-bar-right" align="left"><span class="white">Email Confirmation & Status Updates</span></td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <p class="mil-edit-forms-label">Enter special ordering or delivery instructions here:</p>
                            		<textarea cols="50" rows="4" name="comment" class="x-input"></textarea>
                                </td>
                                <td valign="top" align="left">
                                    <ul id="chkout-3col">
                                    	<li style="width: 20%;"><b>New Addresses:</b></li>
                                        <li style="width: 260px;">
                                        	<textarea cols="35" rows="2" name="comment" class="x-input" style="margin-bottom: 5px;"></textarea>
                                            <br />
                                            <input type="checkbox" class="checkbox" id="Sales">
                                            <label class="option-label txt-sml-gry" for="Sales">Check to save new addresses</label>
                                        </li>
                                        <li style="width: 20%;" class="txt-sml-gry">Use commas between address</li>
                                    </ul>
                                    <div class="clearall"></div>
                                    <br />
                                    
                                    <ul id="chkout-3col">
                                    	<li class="float-left" style="width: 20%;"><b>Saved Addresses:</b></li>
                                        <li class="float-left" style="width: 260px;">
                                        	<select multiple="multiple" size="4" name="Morefruit" style="width: 255px;" class="x-input">
                                                <option>msalzman@industrialwisdom.com</option>
                                                <option>cshushan@industrialwisdom.com</option>
                                                <option>sdavis@industrialwisom.com</option>
                                                <option>jorsi@industrialwisdom.com</option>
                                                <option>dhart@industrialwisdom.com</option>
                                        	</select>
                                        </li>
                                        <li class="float-left txt-sml-gry" style="width: 20%;">To select more than one address, use the 'Ctrl' key for PCs and the "Command‟ key for Macs</li>
                                    </ul>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div id="table-bottom-bar">
                        <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>
                    <br />

                    
                    
                    <!-- Messages -->
                    <div class="x-corners response-msg error">
                    	<div style="margin-right: 15px;" class="float-left"><img src="../../images/theme/theme-1/icons/error.png"/></div>
                        <p class="msg"><b>Warning:</b> Prices have changed as a result of updating your Ship To address. Please review changes before submitting your order.</p>
                        <ul style="height: inherit;">
                        	<li><a href="#" class="grey-ui-btn"><span>Change Ship To</span></a></li>
                            <li><a href="#" class="green-ui-btn"><span>Confirm Changes</span></a></li>
                        </ul>
                        <div class="clearall"></div>
                    </div>
                    
                    <h2 class="table-hdr">Order Summary</h2>
                    <table id="mil-list">
                        <tbody>
                            <tr class="table-header-bar">
                                <td class="no-border table-header-bar-left"><span class="white">Product Description</span></td>
                                <td class="no-border" align="center"><span class="white">Quantity</span></td>
                                <td class="no-border" align="center"><span class="white">UOM</span></td>
                                <td class="no-border" align="center" width="90"><span class="white">My Price (usd)</span></td>
                                <td class="no-border" width="150" align="center" ><span class="white">Extended Price (usd)</span></td>
                                <td class="no-border" align="center"><span class="white">Adjustments</span></td>
                                <td class="no-border-right table-header-bar-right" align="center" width="100"><span class="white">Line Total (usd)</span></td>
                            </tr>
                            <tr class="tbl-hvr">
                                <td>
                                    <div class="prod_grid_img"><img src="../../images/catalog/products/prod-img-small.jpg"/></div>
                                    <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case) xpedx # 23453241</a>
                                    <div id="special-instructions">
                                    	<p>Special Instruction: <a href="#">Add</a></p>
                                    </div>
                                </td>
                                <td valign="top" align="center">
                                    <p class="p-black txt-left form-label-top"><b>1</b></p>
                                    <br />
                                    <p class="p-black txt-left form-label-top">Job #</p>
                                    <input name="CustyNum" class="input-details-cart input-numeric x-input"  id="Text2" tabindex="12" title="CustomerNumber" />
                                </td>
                                <td valign="top" align="center">
                                	Carton
                                </td>
                                <td valign="top" align="right">
                                	<img src="../../images/theme/theme-1/icons/error.png" class="icons"/><b>Original Price:</b><br />
                                    <br />
                                    $72.27/TH<br />
                                	$50.15/CA
                                </td>
                                <td valign="top" align="right">50.15</td>
                                <td valign="top" align="right"></td>
                                <td valign="top" align="right">144.54</td>
                            </tr>
                            <tr class="odd tbl-hvr">
                                <td>
                                    <div class="prod_grid_img"><img src="../../images/catalog/products/prod-img-small.jpg"/></div>
                                    <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case) xpedx # 23453241</a>
                                    <div id="special-instructions">
                                    	<p>Special Instruction: <a href="#">Edit</a></p>
                                    	<p class="user-text">User-entered instructional text...</p>
                                    </div>
                                </td>
                                <td valign="top" align="center">
                                    <p class="p-black txt-left form-label-top"><b>1</b></p>
                                    <br />
                                    <p class="p-black txt-left form-label-top">Job #</p>
                                    <input name="CustyNum" class="input-details-cart input-numeric x-input"  id="Text2" tabindex="12" title="CustomerNumber" />
                                </td>
                                <td valign="top" align="center">
                                	Carton
                                </td>
                                <td valign="top" align="right">
                                	$72.27/TH<br />
                                	$50.15/CA
                                </td>
                                <td valign="top" align="right">50.15</td>
                                <td valign="top" align="right"></td>
                                <td valign="top" align="right">144.54</td>
                            </tr>
                            <tr class="tbl-hvr">
                                <td>
                                    <div class="prod_grid_img"><img src="../../images/catalog/products/prod-img-small.jpg"/></div>
                                    <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case) xpedx # 23453241</a>
                                    <div id="special-instructions">
                                    	<p>Special Instruction: <a href="#">Edit</a></p>
                                    	<p class="user-text">User-entered instructional text...</p>
                                    </div>
                                </td>
                                <td valign="top" align="center">
                                    <p class="p-black txt-left form-label-top"><b>1</b></p>
                                    <br />
                                    <p class="p-black txt-left form-label-top">Job #</p>
                                    <input name="CustyNum" class="input-details-cart input-numeric x-input"  id="Text2" tabindex="12" title="CustomerNumber" />
                                </td>
                                <td valign="top" align="center">
                                	Carton
                                </td>
                                <td valign="top" align="right">
                                	$72.27/TH<br />
                                	$50.15/CA
                                </td>
                                <td valign="top" align="right">50.15</td>
                                <td valign="top" align="right"></td>
                                <td valign="top" align="right">144.54</td>
                            </tr>
                        </tbody>
                    </table>
                    <div id="table-bottom-bar">
                        <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>
                    <div class="clearall"></div>
                    
                    
                    <div id="order-btm-left">
                    	<div id="selected-items-manager">
                        <br /> 
                        	<p class="for-selected">Coupon or Promotion Code</p>
                            <ul class="coupon-code x-corners response-msg notice">
                            	<li class="cpn_lbl"><label style="color:#000;">Add Code:</label></li>
                                <li class="cpn_input"><input style="width:130px;" class="input-details-cart x-input" /></li>
                                <li class="cpn_btn"><a href="#" class="grey-ui-btn"><span>Apply</span></a></li>
                                <div class="clearall"></div>
                            </ul>
                    	</div>
                	</div>
                    
                    
                    <!-- Pricing -->
                    <div id="order-pricing">
                        <ul>
                            <li>Subtotal Items:</li>
                            <li>Adjustments:</li>
                            <li>Adjusted Subtotal:</li>
                            <li>Tax:</li>
                            <li>Shipping Costs:</li>
                        </ul>
                        <ul class="txt-right price">
                            <li>$344.46</li>
                            <li>$0.00</li>
                            <li>$344.46</li>
                            <li class="txt-grey-1">To Be Determined</li>
                            <li class="txt-grey-1">To Be Determined</li>
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
                        
                    <ul class="tool-bar-top" id="tool-bar">
                        <li><a href="#" class="grey-ui-btn"><span>Edit Cart Details</span></a></li>
                    	<li style="float: right;"><a href="#" class="orange-ui-btn"><span>Submit Order</span></a></li>
                    </ul>
                    
                    
                    
                    
                     
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
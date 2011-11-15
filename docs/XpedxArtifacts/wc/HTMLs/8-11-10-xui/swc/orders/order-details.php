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
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/swc.min.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/swc.css" />
<link type="text/css" href="http://jqueryui.com/themes/base/jquery.ui.all.css" rel="stylesheet" />
<link type="text/css" href="http://jqueryui.com/demos/demos.css" rel="stylesheet" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/theme-xpedx.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/xpedx-mil.css" />

<!-- javascript -->
<script type="text/javascript" src="../../js/global/ext-base.js"></script>
<script type="text/javascript" src="../../js/global/ext-all.js"></script>
<script type="text/javascript" src="../../js/global/validation.js"></script>
<script type="text/javascript" src="../../js/global/dojo.js"></script>
<script type="text/javascript" src="../../js/global/dojoRequire.js"></script>
<script type="text/javascript" src="../../js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="../../js/catalog/catalogExt.js"></script>
<script type="text/javascript" src="../../js/swc.js"></script>
<script type="text/javascript" src="../../js/jquery-1.4.2.min.js"></script>
<link type="text/css" href="../../themes/base/jquery.ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="http://jqueryui.com/jquery-1.4.2.js"></script>
<script type="text/javascript" src="../../js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="http://jqueryui.com/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="http://jqueryui.com/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="http://jqueryui.com/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript">
	$(function() {
		$(".datepicker").datepicker({
			showOn: 'button',
						numberOfMonths: 0,

			buttonImage: '../../images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
		});
	});
	</script>
<title>xpedx / Orders</title>
</head>
<!-- END swc:head -->
<body class="  ext-gecko ext-gecko3">
<div id="main-container">
    <div id="main">
        <?php include '../includes/header.php'; ?>
            <div class="container">
                <!-- breadcrumb -->
                <div id="breadcumbs-list-name"> <a href="#">Orders</a> / <span class="breadcrumb-inactive">Order Details for Web Order Y1001</span> <a href="#"> <span class="print-ico-xpedx"><img src="../../images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print Page</span></a>  </div>
                <div id="mid-col-mil"><br />
                    <h2> Web Confirmation Number: Y1001 </h2>
                    <p class="boldText"> Order Number: 12345-00 </p>
                    <p class="boldText">Order Status: Open </p>
                 
                    <div class="clearall"></div>
                    <p> <a href="#" class="grey-ui-btn"><span> Return to Orders</span></a></p>
                    <p class="float-right-order"> <a href="#" class="grey-ui-btn"><span>Return Items</span></a></p>
                    <p class="float-right-order"> <a href="#" class="grey-ui-btn"><span>Re-Order</span></a></p>
                    <p class="float-right-order"> <a href="#" class="grey-ui-btn"><span>Update Order</span></a></p>

                    <p class="float-right-order"> <a href="#" class="grey-ui-btn"><span>Cancel Order</span></a></p>

                    <br />
                    <br />
                    <h2 id="orders"> Shipping Information </h2>
                    <div class="total-price">Total Price:<strong> $259.87</strong> <a href="#">View Details</a></div>
                    <br />
                   <table id="x-tbl-cmmn">
                        <tbody>
                            <tr class="table-header-bar">
                                <td width="300" align="left" class="no-border table-header-bar-left"><span class="white"><span class="white"> Ship To:</td>
                                <td align="left" class="no-border-right table-header-bar-right"><span class="white">Shipping Options:</span></td>
                            </tr>
                            <tr>
                                <td valign="top"><p class="txt-grey-1">Customer PO:
                                    <p class="txt-grey-1">CA-002101135-00001<br />
                                        Metro Graphics <br />
                                        1119 Clement Ave<br />
                                        Charlotte, NC</p>
                                    <br />
                                    <p class="boldText">Attention</p>
                                    <input type="text" size="8" tabindex="1002" id="search_searchTerm" name="search_searchTerm"></td>
                              <td valign="top" align="left">
                                   <p class="mil-edit-forms-label txt-grey-1">You will receive your order on your normal delivery schedule unless you select a different shipping option below:</p>
                                    <br />
                                    <form class="shipping-options">
                                        <ul>
                                            <li style="margin-top:4px;">
                                                <input type="checkbox" class="checkbox">
                                            </li>
                                        <li class="txt-grey-1">Place Order on Hold until:&nbsp; <input type="text" style="margin-left:83px;" class="x-input datepicker" value="3/23/15" size="14">

                                            </li>
                                        </ul>
                                        <ul>
                                            <li>
                                                <input type="checkbox" class="checkbox">
                                            </li>
                                          <li class="txt-grey-1">Ship Order Complete (will not ship until all items are available)</li>
                                        </ul>
                                        <ul>
                                            <li>
                                                <input type="checkbox" class="checkbox">
                                            </li>
                                            <li class="txt-grey-1">Will Call – Pick up at [ insert xpedx address]. (Allow X hours for processing. A Customer Service Representative will contact you.)</li>
                                        </ul>
                                        <ul>
                                            <li style="height: 30px; margin-top:4px;">
                                                <input type="checkbox" class="checkbox">
                                            </li>
                                            <li class="txt-grey-1"><b>Rush Order:</b> Requested Delivery Date: &nbsp;
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
                    <div class="mil-bot"></div>
                    <div class="clearall"></div><br /> 
                    <h2>Payment Information </h2><br /> 
                    <table id="x-tbl-cmmn">
                        <tbody>
                            <tr class="table-header-bar">
                                <td width="300" align="left" class="no-border table-header-bar-left"><span class="white">Customer PO:</span></td>
                                <td width="300" align="left" class="no-border"><span class="white">Bill to Address on FIle:</span></td>
                                <td align="left" class="no-border-right table-header-bar-right"><span class="white">Payment Type:</span></td>
                            </tr>
                            <tr>
                                <td valign="top"><p>Customer PO:</p>
                                    <input title=" " class="input-numeric x-input">
                                    <br><br>
                                    <input type="checkbox" id="Sales" class="checkbox">
                                    <label for="Sales" class="option-label txt-sml-gry">Check to save new addresses</label>  <br><br>
                                    <select class="xpedx_select_sm">
                                        <option>Select Existing PO#</option>
                                        <option>1234</option>
                                        <option>5678</option>
                                        <option>9012</option>
                                    </select></td>
                                <td valign="top" align="left"><b>Metro Graphics1123</b><br>
                                    Clement AveCharlotte, NC<br>
                                   28205United States </td>
                                <td valign="top" align="left"><form>
                                        <input type="radio" name="pay" value="1" class="radio-rght" DISABLED>
                                        <label for="Sales" class="option-label txt-grey-1">Pay on Account</label>
                                        <br>
                                        <br>
                                        <input name="pay" type="radio" class="radio-rght" value="2" checked="checked" DISABLED>
                                        <label for="Sales" class="option-label txt-grey-1">Pay with Credit Card</label>
                                        &nbsp;&nbsp;&nbsp; <br>
                                        <br>
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
                                    </div></td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="mil-bot"></div>
                    <div class="clearall"></div><br /> 
                    <h2>Additional Comments </h2><br /> 
                    <table id="mil-list" width="951" style="background:none;" border="0">
                        <tr id="none" class="table-header-bar">
                            <td width="446" class="no-border table-header-bar-left"><span class="white">Order Comments</span></td>
                            <td width="164" colspan="2" align="left" class="no-border-right table-header-bar-right"><span class="white"> Email Confirmation and Status </span></td>
                        </tr>
                        <tr>
                            <td><p class="grey-mil">Order comments placed by user. </p></td>
                            <td><p> Sent to: <a href="mailto:#">userx@fsinc.com</a></p></td>
                        </tr>
                    </table>
                    <div class="mil-bot"></div>
                    <br />
                    
                                        <h2>Order Summary</h2>
                    <br />

                    
                    <table id="mil-list" style="background:none;">
                        <tbody>
                            <tr class="table-header-bar">
                                <td class="no-border table-header-bar-left"><span class="white">Product Description</span></td>
                                <td class="no-border"  width="60" align="center"><span class="white">Quantity</span></td>
                                <td class="no-border" align="center"><span class="white">UOM</span></td>
                                <td class="no-border" align="center" width="90"><span class="white">Status</span></td>
                                <td class="no-border" align="center" width="90"><span class="white">My Price (usd)</span></td>
                                <td class="no-border" width="120" align="center" ><span class="white">Extended Price (usd)</span></td>
                                <td class="no-border" align="center"><span class="white">Adjustments</span></td>
                                <td class="no-border-right table-header-bar-right" align="center" width="100"><span class="white">Line Total (usd)</span></td>
                            </tr>
                            <tr>
                                <td><div><img src="../../images/catalog/products/prod-img-small.jpg"/></div>
                                    <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case) <br />
                                    <br />
                                    xpedx # 23453241</a>
                                    <div id="special-instructions">
                                        <p>Special Instruction: <a href="#">Add</a></p>
                                    </div></td>
                                <td valign="top" align="center"><p class="p-black txt-left form-label-top"><b>1</b></p>
                                    <br />
                                    <p class="p-black txt-left form-label-top">Job #</p>
                                    <input name="CustyNum" class="input-details-cart input-numeric x-input" style="width:65px;"  id="Text2" tabindex="12" title="CustomerNumber" /></td>
                                <td valign="top" align="center"> Carton </td>
                                <td valign="top" align="center">Open</td>
                                <td valign="top" align="right"> $72.27/TH<br />
                                    $50.15/CA </td>
                                <td valign="top" align="right">50.15</td>
                                <td valign="top" align="right"></td>
                                <td valign="top" align="right">144.54</td>
                            </tr>
                            <tr class="odd">
                                <td><div><img src="../../images/catalog/products/prod-img-small-2.jpg"/></div>
                                    <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case)<br />
                                    <br />
                                    xpedx # 23453241</a>
                                    <div id="special-instructions">
                                        <p>Special Instruction: <a href="#">Edit</a></p>
                                        <p class="user-text">User-entered instructional text...</p>
                                    </div></td>
                                <td valign="top" align="center"><p class="p-black txt-left form-label-top"><b>1</b></p>
                                    <br />
                                    <p class="p-black txt-left form-label-top">Job #</p>
                                    <input name="CustyNum" class="input-details-cart input-numeric x-input" style="width:65px;" id="Text2" tabindex="12" title="CustomerNumber" /></td>
                                <td valign="top" align="center"> Carton </td>
                                <td valign="top" align="center">Open</td>
                                <td valign="top" align="right"> $72.27/TH<br />
                                    $50.15/CA </td>
                                <td valign="top" align="right">50.15</td>
                                <td valign="top" align="right"></td>
                                <td valign="top" align="right">144.54</td>
                            </tr>
                            <tr>
                                <td><div><img src="../../images/catalog/products/prod-img-small-3.jpg"/></div>
                                    <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case)<br />
                                    <br />
                                    xpedx # 23453241</a>
                                    <div id="special-instructions">
                                        <p>Special Instruction: <a href="#">Edit</a></p>
                                        <p class="user-text">User-entered instructional text...</p>
                                    </div></td>
                                <td valign="top" align="center"><p class="p-black txt-left form-label-top"><b>1</b></p>
                                    <br />
                                    <p class="p-black txt-left form-label-top">Job #</p>
                                    <input name="CustyNum" class="input-details-cart input-numeric x-input" style="width:65px;" id="Text2" tabindex="12" title="CustomerNumber" /></td>
                                <td valign="top" align="center"> Carton </td>
                                <td valign="top" align="center">Open</td>
                                <td valign="top" align="right"> $72.27/TH<br />
                                    $50.15/CA </td>
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
                   <div id="order-btm-left">
                    	<div id="selected-items-manager">
                        <br /> 
                        	<p class="for-selected">Coupon or Promotion Code</p>
                            <ul style="width:300px;" class="coupon-code x-corners response-msg notice">
                            	<li>Codes: 12345-00</li>

                                <div class="clearall"></div>
                            </ul>
                    	</div>


                	</div>
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
                            <li class="txt-grey-1">$18.53</li>
                            <li class="txt-grey-1">$42.32</li>
                        </ul>
                        <div class="clearview">&nbsp;</div>
                        <br>
                        <div id="order-total-cost">
                            <div class="order-total-left-bg"></div>
                            <div class="float-left">
                                <ul style="width: 150px;">
                                    <li>
                                        <h2>Order Total:</h2>
                                    </li>
                                </ul>
                                <ul>
                                    <li style="float: right; text-align: right;">
                                        <h2 style="color: rgb(8, 49, 136);">$344.46</h2>
                                    </li>
                                </ul>
                            </div>
                            <div class="order-total-right-bg"></div>
                        </div>
                    </div>
                    <div class="clearview">&nbsp;</div>
                    
                    
                    
                                        <div class="clearview">&nbsp;</div>
                    <ul class="tool-bar-top" id="tool-bar">
                        <li><a href="#" class="grey-ui-btn"><span>Return to Orders</span></a></li>
                        <li style="float: right;"><a href="#" class="grey-ui-btn"><span>Return Items</span></a></li>

                        <li style="float: right;"><a href="#" class="grey-ui-btn"><span>Re-Order</span></a></li>
                        <li style="float: right;"><a href="#" class="grey-ui-btn"><span>Update Order</span></a></li>
                        <li style="float: right;"><a href="#" class="grey-ui-btn"><span>Cancel Order</span></a></li>
                    </ul>
                    <!-- end totals -->
                </div>

                    
                    
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
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
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/theme-xpedx.css" />
<!-- javascript -->

<script type="text/javascript" src="../../js/global/ext-base.js"></script>
<script type="text/javascript" src="../../js/global/ext-all.js"></script>
<script type="text/javascript" src="../../js/global/validation.js"></script>

<script type="text/javascript" src="../../js/global/dojo.js"></script>
<script type="text/javascript" src="../../js/global/dojoRequire.js"></script>
<script type="text/javascript" src="../../js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="../../js/catalog/catalogExt.js"></script>
<script type="text/javascript" src="../../js/swc.js"></script>



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
			numberOfMonths: 2,
			buttonImage: '../../images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
		});
	});
</script>
<style>
#mid-col-mil p {
	margin:25px;
}
</style>
<title>xpedx / ui project pages</title>
</head>
<!-- END swc:head -->
<body class="ext-gecko ext-gecko3">
<div id="main-container">
    <div id="main">
        <?php include '../includes/header.php'; ?>
            <div class="container">
                <div id="mid-col-mil">
                <br />
                
                    <h2>Homepage </h2>
                    <p><a href="index.php">Homepage</a></p>
                    <h2> Catalog</h2>
                    <p><a href="../catalog/catalog-landing.php">Catalog Landing</a></p>
                    <p><a href="../catalog/5-column.php">Catalog 5 Column</a></p>
                    <p><a href="../catalog/index.php">Catalog Custom Paper 8 Column View</a></p>
                    <p><a href="../itemdetails/product-detail.php">Item Details</a></p>
                    <h2> My Items List</h2>
                    <p><a href="../mil/index.php">List of List</a></p>
                    <p><a href="../mil/non-edit.php">My Items List Non Edit View</a></p>
                    <p><a href="../mil/edit.php">My Items List  Edit View</a> <br />
                    Quick add, add special or non stocked modal, copy and paste modal, share list modal</p>
                    <p><a href="../mil/non-edit-alternate.php">My Items List Alternate Items</a> <br />
                    Alternative items modal window</p>
                    <h2>Checkout</h2>
                    <p><a href="../checkout/index.php">Checkout</a></p>
                    <p><a href="../checkout/message-prompts.php">Message Prompts </a></p>
                    <p><a href="../checkout/order-confirmation-approval.php">Order Confirmation Approval</a></p>
                    <p><a href="../checkout/order-confirmation-error.php">Order Confirmation Error</a></p>
                    <p><a href="../checkout/order-confirmation-success.php">Order Confirmation Success</a></p>
                    <h2>Cart</h2>
                    <p><a href="../cart/list-of-carts.php">List of Carts</a></p>
                    <p><a href="../cart/cart.php">Cart</a> <br />
                    Availablity hover - first row of availability column &quot;Immediate&quot; link</p>
                    <h2>Orders</h2>
                    <p><a href="../orders/orders-landing.php">Orders Landing</a></p>

                    <p><a href="../orders/legacy.php">Legacy</a></p>
                    <p><a href="../orders/my-approvals.php">My Approvals</a> <br />
                    Accept and reject modal windows</p>
                    <p><a href="../orders/order-details.php">Order Details</a></p>
                    <p><a href="../orders/order-details-approval.php">Order Details Approval</a></p>
                    <p><a href="../orders/recent-orders.php">Recent Orders</a></p>
                    <p><a href="../orders/recent-orders-split.php">Recent Orders Split</a></p>
                    <h2>Anonymous Experience                 </h2>
                    <p>   <a href="../anonymous/anon-home.php">Anonymous Homepage</a></p>
 <p><a href="../anonymous/anon-catalog-landing.php">Anonymous Catalog Landing Page</a></p>
 <p><a href="../anonymous/anon-product-detail.php">Anonymous Catalog Product Details Page</a></p>
 <p><a href="../anonymous/anon-registration.php">Registration Signup Page</a></p>
 <p><a href="../tools-service/tools.php">Tools Landing Page</a></p>
 <p><a href="../tools-service/services.php">Services Landing Page</a>                </p>
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
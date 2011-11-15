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
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/theme-xpedx.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/xpedx-dan.css"/>
<!-- javascript -->
<script type="text/javascript" src="../../js/global/ext-base.js"></script>
<script type="text/javascript" src="../../js/global/ext-all.js"></script>
<script type="text/javascript" src="../../js/global/validation.js"></script>
<script type="text/javascript" src="../../js/global/dojo.js"></script>
<script type="text/javascript" src="../../js/global/dojoRequire.js"></script>
<script type="text/javascript" src="../../js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="../../js/catalog/catalogExt.js"></script>
<script type="text/javascript" src="../../js/swc.js"></script>
<script type="text/javascript" src="../../js/jQuery.js"></script>
<script type="text/javascript" src="../../js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
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
                <div id="breadcumbs-list-name"> <a href="#">Home</a> / <span class="breadcrumb-inactive">Order Summary</span> <a href="#"> <span class="print-ico-xpedx"><img src="../../images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print This Page</span></a> </div>
                <div id="mid-col-mil"><br />
                    <h2 id="orders"> Recent Approvals Summary <span class="orange-order"> <a href="#">Show All of My Approvals</a></span> </h2>
                    <table id="mil-list" width="951" border="0">
                        <tr id="none" class="table-header-bar">
                            <td class="no-border table-header-bar-left"><span class="white"> Web Confirmation # </span></td>
                            <td class="no-border" align="center" ><span class="white">Order Number</span></td>
                            <td class="no-border" align="center"><span class="white">Customer Purchase Order</span></td>
                            <td class="no-border" align="center"><span class="white">Order Date</span></td>
                            <td class="no-border" align="center"><span class="white">Order Owner</span></td>
                            <td class="no-border" align="center"><span class="white"> Ship To</span></td>
                            <td class="no-border" align="center"><span class="white"> Amount</span></td>
                            <td class="no-border-right table-header-bar-right" align="center" colspan="2"><span class="white"> Status </span></td>
                        </tr>
                        <tr>
                            <td><a href="#">Y10001</a></td>
                            <td>11111-00</td>
                            <td>8674-99</td>
                            <td>03/05/10</td>
                            <td>Johnson,J</td>
                            <td><p class="boldText">CA-002101135-00001<br />
                                    Metro Graphics <br />
                                    1119 Clement Ave<br />
                                    Charlotte, NC</p></td>
                            <td>$257.99</td>
                            <td>Open</td>
                        </tr>
                        <tr class="odd">
                            <td><a href="#">Y10001</a></td>
                            <td><a href="#">Split Order</a></td>
                            <td>8674-98</td>
                            <td>03/05/10</td>
                            <td>Johnson,J</td>
                            <td><div>
                                    <p class="boldText">CA-002101135-00001<br />
                                        Metro Graphics <br />
                                        1119 Clement Ave<br />
                                        Charlotte, NC</p>
                                </div></td>
                            <td>$257.99</td>
                            <td>Open</td>
                        </tr>
                        <tr>
                            <td><a href="#">Y10001</a></td>
                            <td>11111-00</td>
                            <td>8674-97</td>
                            <td>03/05/10</td>
                            <td>Johnson,J</td>
                            <td><div>
                                    <p class="boldText">CA-002101135-00001<br />
                                        Metro Graphics <br />
                                        1119 Clement Ave<br />
                                        Charlotte, NC</p>
                                </div></td>
                            <td>$257.99</td>
                            <td>Approved</td>
                        </tr>
                    </table>
                    <div class="mil-bot"></div>
                    <div class="clearall"></div><br />
                    <h2 id="orders"> Recent Orders Summary <span class="orange-order"> <a href="#">Show All of My Recent Orders</a></span> </h2>
                    <table id="mil-list" width="951" border="0">
                        <tr id="none" class="table-header-bar">
                            <td class="no-border table-header-bar-left"><span class="white"> Web Confirmation # </span></td>
                            <td class="no-border" align="center" ><span class="white">Order Number</span></td>
                            <td class="no-border" align="center"><span class="white">Customer Purchase Order</span></td>
                            <td class="no-border" align="center"><span class="white">Order Date</span></td>
                            <td class="no-border" align="center"><span class="white">Order Owner</span></td>
                            <td class="no-border" align="center"><span class="white"> Ship To</span></td>
                            <td class="no-border" align="center"><span class="white"> Amount</span></td>
                            <td class="no-border-right table-header-bar-right" align="center" colspan="2"><span class="white"> Status </span></td>
                        </tr>
                        <tr>
                            <td><a href="#">Y10001</a></td>
                            <td>11111-00</td>
                            <td>8674-99</td>
                            <td>03/05/10</td>
                            <td>Johnson,J</td>
                            <td><div>
                                    <p class="boldText">CA-002101135-00001<br />
                                        Metro Graphics <br />
                                        1119 Clement Ave<br />
                                        Charlotte, NC</p>
                                </div></td>
                            <td>$257.99</td>
                            <td>Open</td>
                        </tr>
                        <tr class="odd">
                            <td><a href="#">Y10001</a></td>
                            <td><a href="#">Split Order</a></td>
                            <td>8674-98</td>
                            <td>03/05/10</td>
                            <td>Johnson,J</td>
                            <td><div>
                                    <p class="boldText">CA-002101135-00001<br />
                                        Metro Graphics <br />
                                        1119 Clement Ave<br />
                                        Charlotte, NC</p>
                                </div></td>
                            <td>$257.99</td>
                            <td>Open</td>
                        </tr>
                        <tr>
                            <td><a href="#">Y10001</a></td>
                            <td>11111-00</td>
                            <td>8674-97</td>
                            <td>03/05/10</td>
                            <td>Johnson,J</td>
                            <td><div>
                                    <p class="boldText">CA-002101135-00001<br />
                                        Metro Graphics <br />
                                        1119 Clement Ave<br />
                                        Charlotte, NC</p>
                                </div></td>
                            <td>$257.99</td>
                            <td>Approved</td>
                        </tr>
                    </table>
                    <div class="mil-bot"></div>
                    <div class="clearall"></div><br />
                    <h2 id="orders"> Recent Carts Summary <span class="orange-order"> <a href="#">Show All of My Carts</a></span> </h2>
                    <table id="mil-list" width="951" border="0">
                        <tr id="none" class="table-header-bar">
                            <td width="446" class="no-border table-header-bar-left"><span class="white"> Name and Description</span></td>
                            <td width="164" align="center" class="no-border"><span class="white"> Modified By</span></td>
                            <td width="158" align="center" class="no-border"><span class="white"> Last Modified</span></td>
                            <td width="164" colspan="2" align="center" class="no-border-right table-header-bar-right"><span class="white"> Cart Actions </span></td>
                        </tr>
                        <tr>
                            <td><img src="../../images/theme/theme-1/cart-icon.png" alt="Cart Icon" width="14" height="14" align="left" />&nbsp; <a class="boldText" href="#"> Miguel's Cart  (3) Items</a>
                                <p class="grey-mil">Use this cart for monthly restock of paper products and facilities supplies</p></td>
                            <td align="center">Johnson, J</td>
                            <td align="center">03/05/10</td>
                            <td><select name="select" class="xpedx_select_sm">
                                    <option selected="selected" value="5">Select Action</option>
                                    <option value="8">Copy</option>
                                    <option value="8">Edit</option>
                                    <option value="8">Delete</option>
                                </select></td>
                        </tr>
                    </table>
                    <div class="mil-bot"></div>
                    <br />
                    <br />
                    <br />
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
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
<link media="all" type="text/css" rel="stylesheet" href="../../css/theme/xpedx.css" />
<link media="all" type="text/css" rel="stylesheet" href="../../css/global/swc.css" />
<link type="text/css" href="http://jqueryui.com/themes/base/jquery.ui.all.css" rel="stylesheet" />
<link type="text/css" href="http://jqueryui.com/demos/demos.css" rel="stylesheet" />
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
<script type="text/javascript" src="../../js/jquery-1.4.2.min.js"></script>
<link type="text/css" href="../../themes/base/jquery.ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="http://jqueryui.com/jquery-1.4.2.js"></script>
<script type="text/javascript" src="../../js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="http://jqueryui.com/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="http://jqueryui.com/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="http://jqueryui.com/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript">
$(document).ready(function() {
		$(document).pngFix();
	});

	$(function() {
	$('tr.parent')
		.css("cursor","pointer")
		.attr("title","Click to expand/collapse")
		.click(function(){
			$(this).siblings('.child-'+this.id).toggle();
		});
	$('tr[@class^=child-]').hide().children('td');
});
	
	
	
	
	
	
	</script>
<script type="text/javascript">
	$(function() {
		$(".datepicker").datepicker({
			showOn: 'button',
						numberOfMonths: 1,

			buttonImage: '../../images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
		});
	});
	</script>
<title>xpedx / Orders</title>
<style>
#orders {
	color:#003366;
	font-family:Arial, Helvetica, sans-serif;
	font-size:15px;
	font-weight:bold;
	padding-bottom:15px;
	margin-left:0px;
}
.ui-datepicker-trigger {
	margin-left:115px;
	margin-top:-21px;
}
#filter {
	color:#003366;
	font-family:Arial, Helvetica, sans-serif;
	font-size:15px;
	font-weight:bold;
	margin-left:0px;
}
.orange-order {
	font-size:10px;
	color:#ef6b00;
}
.orange-order a {
	font-size:10px;
	color:#ef6b00;
}
#order-filter table {
	width:auto;
}
#order-filter tr {
	background:none;
	padding:0px;
	margin:0px;
	border-width:0px;
	border-style:none;
	border-left:0 solid #CCCCCC;
	border-right:0 solid #CCCCCC;
}
.white {
	font-size:11px;
}
#order-filter td {
	background:none;
	padding:0px;
	margin:0px;
	border-width:0px;
	border-style:none;
	border-left:0 solid #CCCCCC;
	border-right:0 solid #CCCCCC;
}
</style>
</head>
<!-- END swc:head -->
<body class="  ext-gecko ext-gecko3">
<div id="main-container">
    <div id="main">
        <?php include '../includes/header.php'; ?>
            <div class="container">
                <!-- breadcrumb -->
                <div id="breadcumbs-list-name"> <a href="#">Home</a> / <span class="breadcrumb-inactive">Recent Orders</span> <a href="#"> <span class="print-ico-xpedx"><img src="../../images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print This Page</span></a> </div>
                <div id="mid-col-mil"><br />
                    <table id="order-filter" width="97%" cellpadding="2" border="0">
                        <tr>
                            <td width="14%"><h2 id="filter"> Search Orders:</h2></td>
                            <td width="23%" align="right"><input name="search_searchTerm" type="text" id="search_searchTerm" tabindex="1002" value="Enter Search Terms" size="15" /></td>
                            <td width="14%">&nbsp;</td>
                            <td width="20%">&nbsp;</td>
                            <td width="29%">&nbsp;</td>
                            <td width="0%">&nbsp;</td>
                        </tr>
                        <tr>
                            <td height="38"><h2 id="filter"> Or Filter By:</h2></td>
                            <td align="right"><label for="order-accounts">Accounts</label>
                                <select style="width:141px;" name="order-accounts" id="order-accounts">
                                    <option>All</option>
                                    <option>Account A </option>
                                    <option>Account B </option>
                                    <option>Account C </option>
                                </select></td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td align="right"><label for="order-ship-to">Ship To </label>
                                &nbsp;
                                <select style="width:141px;" name="order-ship-to" id="order-ship-to">
                                    <option>All</option>
                                    <option>Account A </option>
                                    <option>Account B </option>
                                    <option>Account C </option>
                                </select></td>
                            <td><div class="demo">
                                    <input type="text" id="datepicker" value="2/23/15-3/23/15" size="14">
                                </div></td>
                            <td><label for="order-ship-to"><em>OR</em> For: </label>
                                &nbsp;
                                <select name="order-ship-to" id="order-ship-to">
                                    <option>Last 7 Days</option>
                                    <option>Last 14 Days</option>
                                    <option>Last 21 Days</option>
                                    <option>Last 6 Months </option>
                                    <option>All </option>
                                </select></td>
                            <td><label for="order-ship-to">Order Status </label>
                                &nbsp;
                                <select name="order-ship-to2" id="order-ship-to2">
                                    <option>All</option>
                                    <option>Account A </option>
                                    <option>Account B </option>
                                    <option>Account C </option>
                                </select>
                                <span class="orange-order"><a href="#">Clear Filters</a></span></td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                    <div class="clear"></div>
                    <br />
                    <table id="mil-list" style="background:none;" width="951" border="0">
                        <tr id="none" class="table-header-bar">
                            <td class="no-border table-header-bar-left"><span class="white"> Web Confirmation Number </span></td>
                            <td class="no-border" align="center" ><span class="white">Order Number</span></td>
                            <td class="no-border" align="center"><span class="white">Customer Purchase Order</span></td>
                            <td class="no-border" align="center"><span class="white">Order Date</span></td>
                            <td class="no-border" align="center"><span class="white">Order Owner</span></td>
                            <td class="no-border" align="center"><span class="white">Ship To</span></td>
                            <td class="no-border" align="center"><span class="white">Amount</span></td>
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
                    <div class="clearall"></div>
                    <table class="detail" id="detail_table">
                        <col style="width: 40px;">
                        <col style="width: 80px;">
                        <col style="width: 150px;">
                        <col style="width: 40px;">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th colspan="2">Name</th>
                                <th>Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr id="row123" class="parent" style="cursor: pointer;" title="Click to expand/collapse">
                                <td>123</td>
                                <td colspan="2">11111-00</td>
                                <td>100</td>
                            </tr>
                            <tr id="row456" class="parent" style="cursor: pointer;" title="Click to expand/collapse">
                                <td>456</td>
                                <td colspan="2">11111-01</td>
                                <td>50</td>
                            </tr>
                            <tr id="row789" class="parent" style="cursor: pointer;" title="Click to expand/collapse">
                                <td>789</td>
                                <td colspan="2">Split Order</td>
                                <td>75</td>
                            </tr>
                            <tr class="child-row789" style="display: none;">
                                <td>&nbsp;</td>
                                <td>2007-01-02</td>
                                <td>A short description</td>
                                <td>33</td>
                            </tr>
                            <tr class="child-row789" style="display: none;">
                                <td>&nbsp;</td>
                                <td>2007-02-03</td>
                                <td>Another description</td>
                                <td>22</td>
                            </tr>
                            <tr class="child-row789" style="display: none;">
                                <td>&nbsp;</td>
                                <td>2007-03-04</td>
                                <td>More Stuff</td>
                                <td>20</td>
                            </tr>
                            <!-- split order -->
                        </tbody>
                    </table>
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
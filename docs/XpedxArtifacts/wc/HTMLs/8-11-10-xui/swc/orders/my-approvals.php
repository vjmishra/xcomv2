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


<!-- jquery css  --> 
<link type="text/css" href="../../js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all.css" rel="stylesheet" />
<link type="text/css" href="../../js/jquery-ui-1/development-bundle/demos/demos.css" rel="stylesheet" />

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
<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="../../js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="../../js/fancybox/jquery.fancybox-1.3.1.js"></script>
<link rel="stylesheet" type="text/css" href="../../js/fancybox/jquery.fancybox-1.3.1.css" media="screen" />
<!-- Page Calls -->



<script type="text/javascript">
	$(document).ready(function() {
		$(".modal").fancybox({
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
<style>
.ui-datepicker-trigger {
	margin-left:160px;
	margin-top:-25px;
}
div.demo {
	padding:4px;
}
</style>
<title>xpedx / Orders</title>
</head>
<!-- END swc:head -->
<body class="  ext-gecko ext-gecko3">
<div id="main-container">
    <div id="main">
        <?php include '../includes/header.php'; ?>
            <div class="container">
                <!-- breadcrumb -->
                <div id="breadcumbs-list-name"> <a href="#">Orders</a> / <span class="breadcrumb-inactive">My Approvals</span> <a href="#"> <span class="print-ico-xpedx"><img src="../../images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print This Page</span></a> </div>
                <div id="mid-col-mil"><br />
                    <table id="order-filter" width="100%" cellpadding="2" border="0">
                        <tr>
                            <td width="14%" height="41"><h2 id="filter"> Search Orders:</h2></td>
                            <td width="24%" align="center"><div class="float-left">
                                    <input class="float-left" name="search_searchTerm" type="text" id="search_searchTerm" tabindex="1002" value="Enter Search Terms" size="15" />
                                </div>
                                <div class="clear"></div>
                                <div class="float-right"> <a href="#"><img style="margin-top:-17px; margin-right:32px;" src="../../images/theme/theme-1/search-orders-ico.png" width="13" height="12" alt="Search" /></a></div>
                            <td width="21%">&nbsp;</td>
                            <td width="21%">&nbsp;</td>
                            <td width="20%">&nbsp;</td>
                            <td width="0%">&nbsp;</td>
                        </tr>
                        <tr>
                            <td height="38"><h2 id="filter"> Or Filter By:</h2></td>
                            <td align="center"><label for="order-accounts">Accounts</label>
                                <select style="width:141px;" name="order-accounts" id="order-accounts">
                                    <option>All</option>
                                    <option>Account A </option>
                                    <option>Account B </option>
                                    <option>Account C </option>
                                </select></td>
                            <td><div class="demo">From:
                                    <input type="text" class="x-input datepicker" value="2/23/15" size="14">
                                </div></td>
                            <td>&nbsp;</td>
                            <td valign="bottom"><span class="orange-order float-right"><a href="#">Clear Filters</a></span></td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td align="center"><label for="order-ship-to">Ship To </label>
                                &nbsp;
                                <select style="width:141px;" name="order-ship-to" id="order-ship-to">
                                    <option>All</option>
                                    <option>Account A </option>
                                    <option>Account B </option>
                                    <option>Account C </option>
                                </select></td>
                            <td><div class="demo"> To: &nbsp; &nbsp;
                                    <input type="text" class="x-input  datepicker" value="3/23/15" size="14">
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
                            <td align="right"><label for="order-ship-to">Order Status </label>
                                &nbsp;
                                <select name="order-ship-to2" id="order-ship-to2">
                                    <option>All</option>
                                    <option>Account A </option>
                                    <option>Account B </option>
                                    <option>Account C </option>
                                </select></td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                    <div class="clear"></div>          <br />

                    <table id="mil-list" width="951" border="0">
                        <tr id="none" class="table-header-bar">
                            <td class="no-border table-header-bar-left"><span class="white"> Web Confirmation # </span></td>
                            <td class="no-border" align="center"><span class="white">Customer Purchase Order</span></td>
                            <td class="no-border" align="center"><span class="white">Order Date</span></td>
                            <td class="no-border" align="center"><span class="white">Ordered By</span></td>
                            <td class="no-border" align="center"><span class="white"> Ship To</span></td>
                            <td class="no-border" align="center"><span class="white"> Amount</span></td>
                            <td class="no-border-right table-header-bar-right" align="center" colspan="2"><span class="white"> Status </span></td>
                        </tr>
                        <tr>
                            <td><a href="#">Y10001</a></td>
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
                            <td><p style="margin-left:20px;">Pending Approval</p>
                                <a href="../modals/approval-comments.html" class="modal grey-ui-btn btn-pad "><span>Accept</span></a> <a href="../modals/rejection-comments.html"  class="grey-ui-btn modal"><span>Reject</span></a>
                                <!-- <p>
                <input type="submit" name="button" id="button" value="Reject" />

                <input type="submit" name="button" id="button" value="Approve" />
              </p>--></td>
                        </tr>
                        <tr class="odd">
                            <td><a href="#">Y10001</a></td>
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
                            <td><p style="margin-left:20px;">Pending Approval</p>
                                <a href="../modals/approval-comments.html" class="modal grey-ui-btn"><span>Accept</span></a> <a href="../modals/rejection-comments.html"  class="grey-ui-btn btn-pad  modal"><span>Reject</span></a>
                                </p></td>
                        </tr>
                        <tr>
                            <td><a href="#">Y10001</a></td>
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
                            <td align="center"><p>Approved</p></td>
                        </tr>
                    </table>
                    <div class="mil-bot"></div>
                    <div class="clearall"></div>
                    <div style="width:940px;"><br />
                        <p  class="float-right" >1-50 of 136 <span class="grey">|</span> <a href="#">Next</a></p>
                    </div>
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
                    <br />
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
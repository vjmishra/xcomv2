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
<!-- jQuery Base & jQuery UI -->
<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../../js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="../../js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="../../js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="../../js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<link type="text/css" href="../../js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all.css" rel="stylesheet" />
<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="../../js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<script src="../../js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="../../js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="../../js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="../../js/fancybox/jquery.fancybox-1.3.1.js"></script>
<link rel="stylesheet" type="text/css" href="../../js/fancybox/jquery.fancybox-1.3.1.css" media="screen" />
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
		$("#various1").fancybox({
			'titleShow'			: false,
			'transitionIn'		: 'fade',
			'transitionOut'		: 'fade'
		});
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
<title>xpedx / Cart / List of Carts</title>
</head>
<!-- END swc:head -->
<body class="  ext-gecko ext-gecko3">
<div id="main-container">
    <div id="main">
        <?php include '../includes/header.php'; ?>
            <div class="container">
                <!-- breadcrumb -->
                <div id="breadcumbs-list-name">
                	<a href="#">Orders</a> / <span class="breadcrumb-inactive">Available Carts</span>
                    <a href="#"><span class="print-ico-xpedx"><img src="../../images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print This Page</span></a>

                    <a href="#"><span class="print-ico-xpedx"><img width="16" height="15" alt="Print This Page" src="../../images/common/email-icon.gif">Email Page</span></a>                    
                </div>
                <div id="mid-col-mil">
                    <div class="clearview">&nbsp;</div>
                    <table id="mil-list">
                        <tbody>
                            <tr id="none" class="table-header-bar">
                                <td class="no-border table-header-bar-left" width="403"><span class="white">Name and  Description</span></td>
                                <td class="no-border" align="center"width="100"><span class="white">Modified By</span></td>
                                <td class="no-border" align="center" width="162"><span class="white">Last Modified</span></td>
                                <td class="no-border-right table-header-bar-right" align="center" colspan="2"><span class="white">Cart Actions</span></td>
                            </tr>
                            <tr>
                                <td width="538"><img width="14" height="14" align="left" alt="Cart Icon" src="../../images/theme/theme-1/cart-icon.png">&nbsp; <a class="boldText" href="cart.php">Miguel's Cart (3) Items</a>
                                    <p class="grey-mil">Use this cart for monthly restock of paper products and facilities supplies</p></td>
                                <td class="createdby-lastmod">Salzman, M</td>
                                <td class="createdby-lastmod">7/19/2012</td>
                                <td class="actions"><select class="xpedx_select_sm">
                                        <option>Select Action</option>
                                        <option>Edit</option>
                                        <option>Copy</option>
                                        <option>Delete</option>
                                    </select></td>
                            </tr>
                            <tr class="odd">
                                <td><img width="14" height="14" align="left" alt="Cart Icon" src="../../images/theme/theme-1/cart-icon.png">&nbsp;<a class="boldText" href="#">Peet's Cart (14) Items</a>
                                    <p class="grey-mil">Use this cart for monthly restock of paper products and facilities supplies</p></td>
                                <td class="createdby-lastmod">Salzman, M</td>
                                <td class="createdby-lastmod">7/19/2012</td>
                                <td class="actions"><select class="xpedx_select_sm">
                                        <option>Select Action</option>
                                        <option>Edit</option>
                                        <option>Copy</option>
                                        <option>Delete</option>
                                    </select></td>
                            </tr>
                            <tr>
                                <td><img width="14" height="14" align="left" alt="Cart Icon" src="../../images/theme/theme-1/cart-icon.png">&nbsp;<a class="boldText" href="#">Mike's Cart (3) Items</a>
                                    <p class="grey-mil">Use this cart for monthly restock of paper products and facilities supplies</p></td>
                                <td class="createdby-lastmod">Salzman, M</td>
                                <td class="createdby-lastmod">7/19/2012</td>
                                <td class="actions"><select class="xpedx_select_sm">
                                        <option>Select Action</option>
                                        <option>Edit</option>
                                        <option>Copy</option>
                                        <option>Delete</option>
                                    </select></td>
                            </tr>
                            <tr class="odd">
                                <td><img width="14" height="14" align="left" alt="Cart Icon" src="../../images/theme/theme-1/cart-icon.png">&nbsp; <a class="boldText" href="#">Monthly Facilities Cart (11) Items</a>
                                    <p class="grey-mil">Use this cart for monthly restock of paper products and facilities supplies</p></td>
                                <td class="createdby-lastmod">Salzman, M</td>
                                <td class="createdby-lastmod">7/19/2012</td>
                                <td class="actions"><select class="xpedx_select_sm">
                                        <option>Select Action</option>
                                        <option>Edit</option>
                                        <option>Copy</option>
                                        <option>Delete</option>
                                    </select></td>
                            </tr>
                            <tr>
                                <td><img width="14" height="14" align="left" alt="Cart Icon" src="../../images/theme/theme-1/cart-icon.png"> &nbsp;<a class="boldText" href="#">My Paper Cart (11) Items</a>
                                    <p class="grey-mil">Use this cart for monthly restock of paper products and facilities supplies</p></td>
                                <td class="createdby-lastmod">Salzman, M</td>
                                <td class="createdby-lastmod">7/19/2012</td>
                                <td class="actions"><select class="xpedx_select_sm">
                                        <option>Select Action</option>
                                        <option>Edit</option>
                                        <option>Copy</option>
                                        <option>Delete</option>
                                    </select></td>
                            </tr>
                            <tr class="odd">
                                <td><img width="14" height="14" align="left" alt="Cart Icon" src="../../images/theme/theme-1/cart-icon.png">&nbsp; <a class="boldText" href="#">Sophia's Cart (6) Items</a>
                                    <p class="grey-mil">Use this cart for monthly restock of paper products and facilities supplies</a></p></td>
                                <td class="createdby-lastmod">Salzman, M</td>
                                <td class="createdby-lastmod">7/19/2012</td>
                                <td class="actions"><select class="xpedx_select_sm">
                                        <option>Select Action</option>
                                        <option>Edit</option>
                                        <option>Copy</option>
                                        <option>Delete</option>
                                    </select></td>
                            </tr>
                        </tbody>
                    </table>
                    <div id="table-bottom-bar">
                        <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>
                    <div id="tool-bar-bottom" class="float-right"><a class="grey-ui-btn" href="../modals/new-copy-edit-cart-details.html" id="various1" ><span>Create New Cart</span></a> </div>
                    <br />
                    <br />
                    <br />
                    <!-- <div type="text" id="example10" class="target">hover</div> -->
                    <!-- <a href="tool-tip-content.html">Tool Tip</a> -->
                </div>
                <!-- End mid-col-mil -->
                
                <div style="height:200px;"></div>
                
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
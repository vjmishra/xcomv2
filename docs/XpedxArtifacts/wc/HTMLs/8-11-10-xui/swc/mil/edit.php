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


<link media="all" type="text/css" rel="stylesheet" href="../modals/checkboxtree/demo.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../modals/checkboxtree/jquery.checkboxtree.css"/>
<script type="text/javascript" src="../../js/jquery.dropdownPlain.js"></script>

<script type="text/javascript" src="../modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="../../js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="../../js/quick-add/quick-add.js"></script>


<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="../../js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="../../js/fancybox/jquery.fancybox-1.3.1.js"></script>
<link rel="stylesheet" type="text/css" href="../../js/fancybox/jquery.fancybox-1.3.1.css" media="screen" />


<!-- Page Calls -->
<script type="text/javascript">
	$(document).ready(function() {
		$("#varous1").fancybox();

$("#various2").fancybox();
$("#various3").fancybox();
$("#various4").fancybox();
$(document).pngFix();

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

<title>xpedx / Editable View</title>
</head>
<!-- END swc:head -->
<body class="  ext-gecko ext-gecko3">
<div id="main-container">
    <div id="main">
        <?php include '../includes/header.php'; ?>
            <div class="container">
                <!-- breadcrumb -->
                <div id="breadcumbs-list-name"><a href="#">Home</a> / <a href="#">My Items Lists</a> / <span class="breadcrumb-inactive">Mike's Items</span> <a href="#"><span class="print-ico-xpedx"><img src="../../images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print This Page</span></a> </div>
                <div id="mid-col-mil">
                    <div class="mil-edit">
                        <div class="quick-add float-right">
                            <h2 style="float:left;">Quick Add</h2>
                            <p class="quick-add-aux-links"> <a style="margin-right:15px;" href="../modals/add-special-non-stocked.html" id="various4">Add Special or  Non-Stocked Item</a>  
                            
                            
                            <a href="../modals/quickadd-copy-paste.html" id="various2">Copy and Paste</a></p>
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
                        <div class="mil-edit-forms">
                            <p class="mil-edit-forms-label">Name</p>
                            <input value="Mike's Items" name="QTY" width="100px" class=" x-input"/>
                            <br />
                            <br />
                            <p class="mil-edit-forms-label">Description</p>
                            <textarea cols="45" rows="2" name="comment" class="x-input">Enter description here...</textarea>
                        </div>
                        <div class="clear">&nbsp;</div>
                        <br />
                        <p class="for-selected">For Selected Items:</p>
                        <ul id="tool-bar" class="tool-bar-top">
                            <li><a class="grey-ui-btn" href="#"><span>Remove Items</span></a></li>
                            <li>&nbsp;&nbsp;&nbsp;&nbsp;</li>
                            <li><a class="grey-ui-btn" href="#"><span>Import Items</span></a></li>
                            <li><a id="various3" class="grey-ui-btn" href="../modals/checkboxtree/share.html"><span>Share List</span></a></li>
                            <li style="float: right;"><a class="orange-ui-btn" href="#"><span>Save Changes</span></a></li>
                        </ul>
                    </div>
                    <!-- Close mil-edit -->
                    <br />
                    <table id="mil-list">
                        <tbody>
                            <tr id="none" style="background:none;">
                                <td class="no-border" width="60"><span class="white"><a href="#" class="select-all" style="color: white;">Select All</a></span></td>
                                <td class="no-border" width="319"><span class="white">Product Description</span></td>
                                <td class="no-border" align="center"><span class="white">Quantity</span></td>
                                <td class="no-border" align="center"><span class="white">UOM</span></td>
                                <td class="no-border" align="center"><span class="white">Availability</span></td>
                                <td class="no-border" width="55" align="center" ><span class="white">My Price (usd)</span></td>
                                <td class="no-border" align="center" ><span class="white">Extended Price (usd)</span></td>
                                <td class="no-border-right" align="center" width="30" ><span class="white">Sort</span></td>
                            </tr>
                            <!-- Row -->
                            <tr>
                                <td valign="top" align="center"><input type="checkbox" name="option2"></td>
                                <td><div class="prod_grid_img"><img src="../../images/catalog/products/prod-img-small.jpg" /></div>
                                    <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case)<br /><br /> xpedx # 23453241</a></td>
                                <td valign="top"><input title="QTY" tabindex="12" id="QTY" value="2" class="input-label input-numeric x-input" name="QTY" /></td>
                                <td valign="top" class="createdby-lastmod"><span>
                                    <select name="select" class="xpedx_select_sm">
                                        <option selected="selected" value="5">Carton (500)</option>
                                        <option value="8">Case (24)</option>
                                    </select></td>
                                <td valign="top"></td>
                                <td valign="top" align="right" width="110">$72.27</td>
                                <td valign="top" align="right" width="160">$72.27</td>
                                <td valign="top"><select name="select" class="xpedx_select_sm">
                                        <option selected="selected" value="5">1</option>
                                        <option value="8">2</option>
                                        <option value="8">3</option>
                                        <option value="8">4</option>
                                        <option value="8">5</option>
                                    </select></td>
                            </tr>
                            <!-- Row -->
                            <tr class="odd">
                                <td valign="top" align="center"><input type="checkbox" name="option2"></td>
                                <td><div class="prod_grid_img"><img src="../../images/catalog/products/prod-img-small.jpg" /></div>
                                    <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case) <br /><br />xpedx # 23453241</a></td>
                                <td valign="top"><input title="QTY" tabindex="12" id="QTY" value="2" class="input-label input-numeric x-input" name="QTY" /></td>
                                <td valign="top" class="createdby-lastmod"><span>
                                    <select name="select" class="xpedx_select_sm">
                                        <option selected="selected" value="5">Carton (500)</option>
                                        <option value="8">Case (24)</option>
                                    </select></td>
                                <td valign="top"></td>
                                <td valign="top" align="right" width="110">$72.27</td>
                                <td valign="top" align="right" width="160">$72.27</td>
                                <td valign="top"><select name="select" class="xpedx_select_sm">
                                        <option selected="selected" value="5">1</option>
                                        <option value="8">2</option>
                                        <option value="8">3</option>
                                        <option value="8">4</option>
                                        <option value="8">5</option>
                                    </select></td>
                            </tr>
                            <!-- Row -->
                            <tr>
                                <td valign="top" align="center"><input type="checkbox" name="option2"></td>
                                <td><div class="prod_grid_img"><img src="../../images/catalog/products/prod-img-small.jpg" /></div>
                                    <a href="#">Jiffy Padded Mailers, 4&quot; W x 8&quot; L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner Batting, Padded (500 Each/Case) <br /><br />xpedx # 23453241</a></td>
                                <td valign="top"><input title="QTY" tabindex="12" id="QTY" value="2" class="input-label input-numeric x-input" name="QTY" /></td>
                                <td valign="top" class="createdby-lastmod"><span>
                                    <select name="select" class="xpedx_select_sm">
                                        <option selected="selected" value="5">Carton (500)</option>
                                        <option value="8">Case (24)</option>
                                    </select></td>
                                <td valign="top"></td>
                                <td valign="top" align="right" width="110">$72.27</td>
                                <td valign="top" align="right" width="160">$72.27</td>
                                <td valign="top"><select name="select" class="xpedx_select_sm">
                                        <option selected="selected" value="5">1</option>
                                        <option value="8">2</option>
                                        <option value="8">3</option>
                                        <option value="8">4</option>
                                        <option value="8">5</option>
                                    </select></td>
                            </tr>
                    </table>
                    </tbody>
                    <div id="table-bottom-bar">
                        <div id="table-bottom-bar-L"></div>
                        <div id="table-bottom-bar-R"></div>
                    </div>
                    <ul id="tool-bar" class="tool-bar-bottom">
                        <li><a class="grey-ui-btn" href="#"><span>Remove Items</span></a></li>
                        <li>&nbsp;&nbsp;&nbsp;&nbsp;</li>
                        <li><a class="grey-ui-btn" href="#"><span>Import Items</span></a></li>
                       <li><a href="#" class="grey-ui-btn"><span>Share List</span></a></li>
                        <li style="float: right;"><a class="orange-ui-btn" href="#"><span>Save Changes</span></a></li>
                    </ul>
                    <div class="clearview">&nbsp;</div>
                    <br />
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

// Moving the JS functions from XPEDXCatalogExt.jsp to here on 12/29/2011.

$(document).ready(function() {
	$(document).pngFix();
});
function highlightRows()
{
	//$("tr:odd").css("background-color", "#fafafa");
	$("#x-tbl-cmmn tr:odd").css("background-color", "#fafafa");
	var table=document.getElementById("x-tbl-cmmn");
	if(table !=null && table != undefined)
	{
		var rowCount = table.rows.length;	
		if(rowCount >0)
		{
			var row = table.rows[0];
			if (row != undefined)
			{
				row.setAttribute('style', 'background-color:none');
			}					
		
			for(var i=(rowCount-1); i>=1; i--) 
			{
				var row = table.rows[i];
				if (row != undefined)
				 {
						if (i%2 != 0)
						{
							row.setAttribute('style', 'background-color:#fafafa');
						}
						else
						{
							row.setAttribute('style', 'background-color:none');
						}	
					
				}         			
			}
		}
	}	
}
function hideSharedListForm(){
	document.getElementById("dynamiccontent").style.display = "none";
}
	
function showSharedListForm(){
	var dlgForm 		= document.getElementById("dynamiccontent");
	if (dlgForm){
		dlgForm.style.display = "block";
	}
}
function setFocus(component){
	component.focus();
}	
//taken from global-xpedx-functions.js function not in the included js 
function expandPriceAndAvailability (el)
{
	jQuery(el).parents("dd").find('.price-and-availability').toggle();
	return false;
}
function showCost (el)
{
	if(el.innerHTML == '[Show]')
	  el.innerHTML = '[Hide]';
	else
	  el.innerHTML = '[Show]';
	  
	jQuery('#cost').toggle();
	
	return false;
}
function toggleCost(id)
{
   var e = document.getElementById(id);
   if(e.innerHTML = 'N/A')
	  e.style.display = 'none';
   else
	  e.style.display = 'block';

}
//end of global-xped-functions.js 	
//Added for removing double quote from the search srting. Jira # 2415
function validateQuote(e){
  if (e.keyCode == 13) {  
	var searchVal = document.getElementById("search_searchTerm").value;
	
	Ext.fly('search_searchTerm').dom.value=searchVal;
  }
}

$(document).ready(function() {
	$('.slideshow').cycle({
			fx: 'fade', // choose your transition type, ex: fade, scrollUp, shuffle, etc...
			pager: '#catalog-image-rotation-nav-inner',
			timeout: 5000,
			prev:   '#catalog-image-rotation-nav .img-navi-left', 
			next:   '#catalog-image-rotation-nav .img-navi-right'
	});
});
function shareSelectAll(checked_status){
	//var checked_status = this.checked;
	var checkboxes = Ext.query('input[name*=customerPaths]');
	Ext.each(checkboxes, function(obj_item){
		obj_item.checked = !checked_status;
		obj_item.click();
		//obj_item.fireEvent('click');
	});
}
function clearCreateNewList()
{			
	document.getElementById("listName").value="";
	document.getElementById("listDesc").value="";
	document.getElementById("shareAdminOnly").checked=false;
	$.fancybox.close();
}

/*if ("<s:property value="rbPermissionPrivate"/>" != ""){
	hideForm('ShareListShared');
}*/

function submitSL(){
	submitNewlist();
} 
function prepareDiv(data, itemId, name, desc, qty, uom){						
	try{
		data = data.replace("[itemId]", itemId);
		data = data.replace("[name]", 	name); 
		data = data.replace("[desc]", 	desc);
		data = data.replace("[qty]",	qty);
		data = data.replace("[uom]", 	uom);
		try{ console.debug("data: " + data + ", itemId: " + itemId + ", name: " + name + ", desc: " + desc + ", qty: " + qty + ", UOM: " + uom) }catch(e){}
	}catch(e){
		data = "";
	}
	return data;
}

var currentAadd2ItemList = new Object();

function checkUpdateAvailability()
{
	var ItemId=document.getElementById("ITemId").value;
	var UOM=document.getElementById("UOM").value;	
	if(document.getElementById("Qty").value!=null)
	{
		var qtyEle = document.getElementById("Qty");
		ItemId=ItemId+'&Qty='+document.getElementById("Qty").value+'&UOM='+UOM;
	}
	openAddToCart(ItemId);
	
}
function openDataJS(data, url)
{	var ITemId=document.getElementById("ITemId").value;
	var Qty=document.getElementById("Qty").value;
	var UOM=document.getElementById("UOM").value;
	var Job=document.getElementById("Job").value;
	var customer=document.getElementById("customer").value;
	
	listAddToCartItem(url,ITemId,UOM,Qty,Job,customer,'');
}
function openDialog()
{
 	openAddToCart1(document.getElementById("ITemId").value)
	javascript:DialogPanel.show('bracketPricingDialog');
}
function handleAjaxAvailabilitySuccess1(responseObject){
	myMask.hide();	
	var responseText = responseObject.responseText;
	Ext.fly('Price_Grid').dom.innerHTML='';
	Ext.fly('Price_Grid').dom.innerHTML=responseText;		
	svg_classhandlers_decoratePage();
}
function handleAjaxAvailabilityFailure1(){	
	myMask.hide();
	alert('fail');
}
function populateDivGridJS(myData,p_sfID,p_url)
{	
	var obj = eval("(" + myData + ")");
	var storeFrontId = p_sfID;
	var url = p_url;
	url = ReplaceAll(url,"&amp;",'&');
	var availStore = new Ext.data.JsonStore({	
		proxy: new Ext.data.HttpProxy({  
		    method:'POST',  
		    url: url,
		    headers: {'Content-type': 'application/json'}
		}),		
		data   : obj,					
		root: 'availList',
		fields:['availability','availValue']		
    });
	var grid = new Ext.grid.GridPanel({
        width:550,
        height:200,
        layout:'fit',
        title:'',
        store: availStore,
        trackMouseOver:false,
        disableSelection:true,
        loadMask: true,

        // grid columns
        columns:[{
            id: 'topic', 
            header: '',
            dataIndex: 'availability',
            width: 130,
            height:80,
            sortable: false
        },{
            header: '',
            dataIndex: 'availValue',
            width: 130,
            height:80,
            sortable: false
        }]
    });//grid			
    Ext.fly('availibility-grid').dom.innerHTML='';
	availStore.loadData(obj);	
	grid.render('availibility-grid'); 	    		
}
function handleAjaxAvailabilitySuccess(responseObject){	
	myMask.hide();	
	var responseText = responseObject.responseText;
	if(responseText.indexOf("Unable to get Price and Availability.Please contact system admin")>-1)
	{
		Ext.fly('availibility-grid_error').dom.innerHTML='';
		Ext.fly('availibility-grid_error').dom.innerHTML=responseText;
		DialogPanel.show("addToCart1");	
		svg_classhandlers_decoratePage();
	}
	else
	{
		var variable1="{\"availList\":";
		var startIndexof=responseText.indexOf(variable1);
		if(startIndexof > 0){
			var jsonString=responseText.substring(startIndexof,responseText.length);
			variable1="}]}";
			startIndexof=jsonString.indexOf(variable1)+variable1.length;
			jsonString=jsonString.substring(0,startIndexof);
			populateDivGrid1(jsonString);
		}		
		Ext.fly('add-to-cart-grid').dom.innerHTML='';
		Ext.fly('add-to-cart-grid').dom.innerHTML=responseText;
		DialogPanel.show("addToCart");
		svg_classhandlers_decoratePage();	
	}	
}

function handleAjaxAvailabilityFailure(){
	myMask.hide();	
	alert('Failure');
}

function listAddToCartItem(url, productID, UOM, quantity,Job,customer) {
      
   	Ext.Ajax.request({
    	// for testing only
        url: url,
        params: {
			productID: productID,
	    	productUOM: UOM,
	    	quantity: quantity,
	    	reqJobId: Job,
	    	reqCustomer:customer
	     },
        // end testing
        method: 'GET',
        success: function (response, request){
              
            //DialogPanel.toggleDialogVisibility('addToCart');	            
            //var myDiv = document.getElementById("ajax-body-1");	            
           	//myDiv.innerHTML = 'The product has been successfully added to the cart';	            
           	//DialogPanel.show('modalDialogPanel1');	            
           	//svg_classhandlers_decoratePage();
        },
        failure: function (response, request){
            var myDiv = document.getElementById("ajax-body-1");
            myDiv.innerHTML = response.responseText;
            DialogPanel.show('modalDialogPanel1');
            svg_classhandlers_decoratePage();
             }
    });		
}	

function addItemToMyItemList(pulldown,itemID,baseUom)
{
	if(pulldown.value == 'AddToListSample'){
		return;
	}
	 var submitForm = getNewSubmitForm();
	 createNewFormElement(submitForm, "qty", "1");
	 createNewFormElement(submitForm, "uomId", baseUom);
	 createNewFormElement(submitForm, "listKey", pulldown.value);
	 createNewFormElement(submitForm, "listName", pulldown.options[pulldown.selectedIndex].text);
	 createNewFormElement(submitForm, "itemId", itemID);
	 submitForm.action= document.getElementById('addToMyItemListURL');
	 submitForm.submit();
}

//helper function to create the form
function getNewSubmitForm(){
	 var submitForm = document.createElement("FORM");
	 document.body.appendChild(submitForm);
	 submitForm.method = "POST";
	 return submitForm;
}

//helper function to add elements to the form
function createNewFormElement(inputForm, elementName, elementValue){
	 var newElement = document.createElement("<input name='"+elementName+"' type='hidden'>");
	 inputForm.appendChild(newElement);
	 newElement.value = elementValue;
	 return newElement;
}
 // IW Update the extJS view
// NOTE: This process slows down after switching between views several times!
 function initializeViewJS(p_url)
{	
	var ct = Ext.get('item-box-inner');
	ct.on('mouseover', function (e, t) {
			if (t = e.getTarget('dd')) {
					Ext.fly(t).addClass('over');
			}
	});
						
	ct.on('mouseout', function (e, t) {
			if ((t = e.getTarget('dd')) && !e.within(t, true)) {
					Ext.fly(t).removeClass('over');
			}
	});
					
	Ext.get('items-cb').on('click', function (e) {
		// Change view
		var img = e.getTarget('img', 2);
		if (img) {
				Ext.getDom('items').className = img.className;
		}

		document.getElementById("selectedView").value = Ext.getDom('items').className;
		/******* IW Update the extJS View ******/
		updateView();
		//shortenItemDescriptions();

		var url = p_url;//'/swc/catalog/setSelectedView.action?sfId=xpedx&amp;scFlag=Y&amp;scGuestUser=Y';
		url = ReplaceAll(url,"&amp;",'&');
		Ext.Ajax.request({
				// for testing only
				url: url,
				params: {
					 selectedView: document.getElementById("selectedView").value
				},
				// end testing
				method: 'GET',
				callback: function () {
						svg_classhandlers_decoratePage(); //TODO: not working for IE
				}
		});
	});
						
	setItemDragZone(Ext.get('item-box-inner'));
	setItemDropZone(Ext.get('items-combox'));
}
function loadViewJS(p_url)
{
	//document.getElementById("selectedView").value = Ext.getDom('items').className; JIRA 1459
	//updateView();
	//shortenItemDescriptions();
	//var url = '/swc/catalog/setSelectedView.action?sfId=xpedx&amp;scFlag=Y';
	 initializeView(); updateView(); // 
	 var url = p_url;
	 url = ReplaceAll(url,"&amp;",'&');
	Ext.Ajax.request({
			// for testing only
			url: url,
			params: {
				 selectedView: document.getElementById("selectedView").value
			},
			// end testing
			method: 'GET',
			callback: function () {
					svg_classhandlers_decoratePage(); //TODO: not working for IE
			}
	});
}
// IW Update the extJS view
// NOTE: This process slows down after switching between views several times!
function updateView() {
	 var ct = Ext.get('item-box-inner');
	
	var selectedClass = document.getElementById("selectedView").value;

	if (selectedClass == selectedExtTemplate)
	   return; // only change html layout when necessary
  

  
	var tpl = null;
	if(selectedClass == "normal-view") {
		tpl = getNormalView();tpl.compile();
	}
	if(selectedClass == "condensed-view") {
		tpl = getCondensedView();tpl.compile();
	}
	if(selectedClass == "mini-view") {
		tpl = getMiniView();tpl.compile();
	}
	if(selectedClass == "papergrid-view") {
		tpl = getGridView();tpl.compile();
	}
   
   selectedExtTemplate = selectedClass;
  
	tpl.overwrite(ct, catalog);
   
	itemwin = new Ext.Window({
		id: 'itemwin',
		layout: 'fit',
		width: 400,
		height: 500,
		shadow: 'drop',
		shadowOffset: 10,
		closeAction: 'hide',
		modal: true,
		resizable: false
	});
  
	// select default view. can be based on user's last selection or preference
	//Ext.getDom('items').className = 'condensed-view';
	Ext.getDom('items').className = document.getElementById("selectedView").value;
 
	Ext.get('items-combox').addClassOnOver('com-over', true);
	svg_classhandlers_decoratePage();
	$("#x-tbl-cmmn tbody tr:odd").css("background-color", "#fafafa");
	$("#x-tbl-cmmn tbody tr:even").css("background-color", "#fff");
	
	if("papergrid-view" == Ext.getDom('items').className){
		forEach(document.getElementsByTagName('table'), function(table) {
		  if (table.className.search(/\bsortable\b/) != -1) {
			sorttable.makeSortable(table);
			headrow = table.tHead.rows[0].cells;
			if(defaultSortColumn != undefined)
			{
				sorttable.auto_clicked(headrow[defaultSortColumn]);
			}
		  }
		});
	}	
	shortenItemDescriptions();
}
function shortenItemDescriptions()
{
	var selectedClass = document.getElementById("selectedView").value;

	if(selectedClass == "normal-view") {

		/* To ensure that the long/short desc. gets shortened each time the view changes.
		 * Added per Jira 3318. 
		 */
			$('.prodlist ul li, #prodlist ul li ').each(function() {
				var html = $(this).html();
				var shortHTML = html.substring(0, 65);
				if( html.length > shortHTML.length )
				{
					$(this).html(shortHTML);
					$(this).append('...');	
					$(this).attr('title', html );
				}
			});
		/* End Jira 3318 changes */
		
	}
	else if(selectedClass == "condensed-view") {
		
		/* To ensure that the long/short desc. gets shortened each time the view changes.
		 * Added per Jira 3318. (Looks like substring is ignoring the spaces.)
		 */
			/* Begin long desc. shortener */
			$('.prodlist ul li, #prodlist ul li ').each(function() {
				var html = $(this).html();
				var shortHTML = html.substring(0, 20);
				if( html.length > shortHTML.length )
				{
					$(this).html(shortHTML);
					$(this).append('...');	
					$(this).attr('title', html );
				}
			});
			/* End long desc. shortener */
			
			/* Begin short desc. shortener */
			$('a#item-detail-lnk p.ddesc').each(function() {
				var html = $(this).html();
				var shortHTML = html.substring(0, 45);
				if( html.length > shortHTML.length )
				{
					$(this).html(shortHTML);
					$(this).append('...');	
					$(this).attr('title', html );
				}
			});
			/* end short desc. shortener */
		/* End Jira 3318 changes */
	}
	else if(selectedClass == "mini-view") {
		
		/* To ensure that the long/short desc. gets shortened each time the view changes.
		 * Added per Jira 3318. 
		 */
			/* Begin long desc. shortener */
			$('.prodlist ul li, #prodlist ul li ').each(function() {
				var html = $(this).html();
				var shortHTML = html.substring(0, 20);
				if( html.length > shortHTML.length )
				{
					$(this).html(shortHTML);
					$(this).append('...');	
					$(this).attr('title', html );
				}
			});
			/* End long desc. shortener */

			/* Begin short desc. shortener */
			$('a#item-detail-lnk p.ddesc').each(function() {
				var html = $(this).html();
				var shortHTML = html.substring(0, 35);
				if( html.length > shortHTML.length )
				{
					$(this).html(shortHTML);
					$(this).append('...');	
					$(this).attr('title', html );
				}
	});
			/* end short desc. shortener */
		/* End Jira 3318 changes */
	}
	else if(selectedClass == "papergrid-view") {

		/* To ensure that the long/short desc. gets shortened each time the view changes.
		 * Added per Jira 3318. 
		 */
		 SetArrowForSorting();
			$('.prodlist ul li, #prodlist ul li ').each(function() {
				var html = $(this).html();
				var shortHTML = html.substring(0, 25);
				if( html.length > shortHTML.length )
	{
					$(this).html(shortHTML);
					$(this).append('...');	
					$(this).attr('title', html );
				}
		});
		/* End Jira 3318 changes */
	}
	else {
		return;
	}
}

function setItemDragZone(v) {
	v.dragZone = new Ext.dd.DragZone(v, {
		getDragData: function (e) {
			//, #x-tbl-cmmn tbody tr
			var sourceEl = e.getTarget('dd.itemdiv', 10) || e.getTarget('tr.itemrow', 10);
			if (sourceEl) {
				d = Ext.query('.ddesc', sourceEl)[0].cloneNode(true);
				d.className = 'draggeditem';
				d.id = Ext.id();
				return v.dragData = {
					sourceEl: sourceEl,
					repairXY: Ext.fly(sourceEl).getXY(),
					ddel: d,
					itemid: sourceEl.id
				}
			}
		},
		getRepairXY: function () {
			return this.dragData.repairXY;
		},
		onMouseUp: function (evt) {
			var fld = evt.getTarget("input", 5);
			if (fld) fld.focus();
		}
	});
}

function setItemDropZone(g) {
	g.dropZone = new Ext.dd.DropZone(g, {
		getTargetFromEvent: function (e) {
			var tEle = e.getTarget('#items-combox');
			return tEle;
		},
		onNodeEnter: function (target, dd, e, data) {
		},
		onNodeOut: function (target, dd, e, data) {
		},
		onNodeOver: function (target, dd, e, data) {
			return Ext.dd.DropZone.prototype.dropAllowed;
		},
		onNodeDrop: function (target, dd, e, data) {
			if(data.itemid == '') {
				alert('This item cannot be compared at the moment. Please try again later');
				return true;
			}
			else {
				addCompare(data.itemid);
				return true;
			}
		}
	});
}
function updateCompareBucket(increment, effect) {
	var c = Ext.get('comnum');
	if (increment)
		compCount += increment;
		c.dom.innerHTML = (compCount == 1) ? (compCount + " Item") : (compCount + " Items");

	if (effect) {
		Ext.get('items-combox').highlight("ffdd00", { duration: 3 });
		Ext.get('items-combox').frame("00ff00", 1, { duration: 1 });
	}
}
Ext.onReady(function ()
{
		return; // disabled
	var imgs = Ext.query('.prodImg');
	var qvlTitle = document.getElementById("quickViewLaunchTitle").innerHTML;
	for (var i = 0; i < imgs.length; i++) {
		var tmp = imgs[i].id.split("_");
		var idx = tmp[tmp.length - 1];
		bubbleURL = document.getElementById("quickViewURL_" + idx).href;
		qvTitle = document.getElementById("quickViewTitle_" + idx).innerHTML;
		new QuickView(imgs[i],
			{
				url: bubbleURL,
				callback: svg_classhandlers_decoratePage
			},
			{
				title: qvlTitle,
				quickViewWindowConf: {
					animateFromAnchor: false,
					windowConf: {
						title: qvTitle,
						width: 750,
						//height: 400
						autoHeight: true
					}
				}
			});
	}
});
Ext.onReady(function () {
	updateCompareBucket(0);
});
Ext.onReady(function () {
	loadAfterCompilation();
});

function toggleDescSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
 //var theImageSpan = eval("document.getElementById('" + "directionDescArrow" + "')" );
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
        //theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/12x12_white_down.png" class="sort-order sort-desc">';	  
		processSortByUpperTroy("SortableShortDescription--D","sortDown","directionDescArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	  	// theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/icons/12x12_white_up.png" class="sort-order sort-desc">';
		processSortByUpperTroy("SortableShortDescription--A","sortUp","directionDescArrow");
	  }
	  else
	  {
	  //	 theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/icons/12x12_white_up.png" class="sort-order sort-desc">';
	   processSortByUpperTroy("SortableShortDescription--A","sortUp","directionDescArrow");
	  }
  }
  else
  {
//theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/icons/12x12_white_up.png" class="sort-order sort-desc">';
   processSortByUpperTroy("SortableShortDescription--A","sortUp","directionDescArrow");
  }
}        

function toggleSizeSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ExtnSize--D","sortDown","directionSizeArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ExtnSize--A","sortUp","directionSizeArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ExtnSize--A","sortUp","directionSizeArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ExtnSize--A","sortUp","directionSizeArrow");
  }
}

function toggleItemSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ItemID--D","sortDown","directionItemArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ItemID--A","sortUp","directionItemArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ItemID--A","sortUp","directionItemArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ItemID--A","sortUp","directionItemArrow");
  }
}

function toggleCertSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ExtnCert--D","sortDown","directionCertArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
  }
}   

function toggleRelevancySort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("relevancy","sortDown","directionMArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("relevancy","sortUp","directionMArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("relevancy","sortUp","directionMArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("relevancy","sortUp","directionMArrow");
  }
}

function toggleColorSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ExtnColor--D","sortDown","directionColorArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ExtnColor--A","sortUp","directionColorArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ExtnColor--A","sortUp","directionColorArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ExtnColor--A","sortUp","directionColorArrow");
  }
}

function toggleGaugeSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnGauge--D","sortDown","directionGaugeArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnGauge--A","sortUp","directionGaugeArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnGauge--A","sortUp","directionGaugeArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnGauge--A","sortUp","directionGaugeArrow");
  }
}

function toggleBasisSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnBasis--D","sortDown","directionBasisArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnBasis--A","sortUp","directionBasisArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnBasis--A","sortUp","directionBasisArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnBasis--A","sortUp","directionBasisArrow");
  }
}


function toggleEnvironmentSort()
{
	
}

function toggleMwtSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnMwt--D","sortDown","directionMwtArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnMwt--A","sortUp","directionMwtArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnMwt--A","sortUp","directionMwtArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnMwt--A","sortUp","directionMwtArrow");
  }
}

function toggleLeafSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ExtnCert--D","sortDown","directionCertArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
  }
}  

function togglePlySort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnPly--D","sortDown","directionPlyArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnPly--A","sortUp","directionPlyArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnPly--A","sortUp","directionPlyArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnPly--A","sortUp","directionPlyArrow");
  }
}

function toggleModelSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnModel--D","sortDown","directionModelArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnModel--A","sortUp","directionModelArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnModel--A","sortUp","directionModelArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnModel--A","sortUp","directionModelArrow");
  }
}

function toggleFormSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnForm--D","sortDown","directionFormArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnForm--A","sortUp","directionFormArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnForm--A","sortUp","directionFormArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnForm--A","sortUp","directionFormArrow");
  }
}

function toggleMaterialSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnMaterial--D","sortDown","directionMaterialArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnMaterial--A","sortUp","directionMaterialArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnMaterial--A","sortUp","directionMaterialArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnMaterial--A","sortUp","directionMaterialArrow");
  }
}

function toggleCapacitySort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ExtnCapacity--D","sortDown","directionCapacityArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ExtnCapacity--A","sortUp","directionCapacityArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ExtnCapacity--A","sortUp","directionCapacityArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ExtnCapacity--A","sortUp","directionCapacityArrow");
  }
}
function toggleVendorSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnVendorNo--D","sortDown","directionVendorArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnVendorNo--A","sortUp","directionVendorArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnVendorNo--A","sortUp","directionVendorArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnVendorNo--A","sortUp","directionVendorArrow");
  }
}

function toggleStockSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("showNormallyStockedItems--D","sortDown","directionMArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("showNormallyStockedItems--A","sortUp","directionMArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("showNormallyStockedItems--A","sortUp","directionMArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("showNormallyStockedItems--A","sortUp","directionMArrow");
  }
}
function processSortByUpperTroyJS(theValue,directionValue,theSpanNameValue,pUrl)
{
	var sortFieldValue;
	theUrl = pUrl;

	if(theValue.indexOf("showNormallyStockedItems") > -1)
	{
	sortFieldValue = theValue;
	}
	else{
	sortFieldValue = "Item." + theValue;
	}
	var theImageSpan = eval("document.getElementById('" + theSpanNameValue + "')" );
	//alert(theImageSpan);

	if(directionValue == "sortUp")
	{
	// alert("up");
	theUrl = theUrl + "&sortField=" + sortFieldValue + "&sortDirection=" + "sortDown"+"&theSpanNameValue="+theSpanNameValue;
	theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/12x12_white_up.png" class="sort-order sort-desc">';
	// alert(theImageSpan);
	}
	else if(directionValue == "sortDown")
	{
	//alert("down");
	theUrl = theUrl + "&sortField=" + sortFieldValue + "&sortDirection=" + "sortUp"+"&theSpanNameValue="+theSpanNameValue;
	theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/12x12_white_down.png" class="sort-order sort-desc">';
	// alert(theImageSpan);
	}
	setTimeout("changeUrl()",1000);
}  

function SetArrowForSorting()
{
	
	/*var globalsortField='<%=request.getParameter("sortField")%>';
	var globalsortDirection='<%=request.getParameter("sortDirection")%>';
	var globaltheSpanNameValue='<%=request.getParameter("theSpanNameValue")%>';*/
	
	var theImageSpan = eval("document.getElementById('" + globaltheSpanNameValue + "')" );
		//alert(theImageSpan);
        if(theImageSpan==null || theImageSpan=='null')
        	{
        	 return;
        	}
		if(globalsortDirection == "sortDown")
		{	// alert("up");
		
		theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/icons/12x12_white_up.png" class="sort-order sort-desc">';
		// alert(theImageSpan);
		
		}
		else if(globalsortDirection == "sortUp")
		{
			//alert("down");		
		theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/icons/12x12_white_down.png" class="sort-order sort-desc">';
		// alert(theImageSpan);
		}
		
}

function changeUrl()
{
  window.location.href=theUrl
}
//<!-- begin swc:dialogPanel -->
Ext.onReady(function () {
	var w = new Ext.Window({
		autoScroll: true,
		closeAction: 'hide',
		cls: 'swc-ext',
		contentEl: 'modalDialogPanel1Content',
		hidden: true,
		id: 'modalDialogPanel1',
		modal: true,
		width: '600',
		height: 'auto',
		resizable: true,
		shadow: 'drop',
		shadowOffset: 10,
		title: 'Add To Cart Result'
	});
});
//<!-- end swc:dialogPanel -->
//<!-- begin swc:dialogPanel -->
Ext.onReady(function () {
	var w = new Ext.Window({
		autoScroll: true,
		closeAction: 'hide',
		cls: 'swc-ext',
		contentEl: 'modalDialogPanel2Content',
		hidden: true,
		id: 'modalDialogPanel2',
		modal: true,
		width: '600',
		height: 'auto',
		resizable: true,
		shadow: 'drop',
		shadowOffset: 10,
		title: 'Supersession'
	});
});
//<!-- end swc:dialogPanel -->
//<!-- begin swc:dialogPanel -->
Ext.onReady(function () {
	var w = new Ext.Window({
		autoScroll: true,
		closeAction: 'hide',
		cls: 'swc-ext',
		contentEl: 'product_availabilityContent',
		hidden: true,
		id: 'product_availability',
		modal: true,
		width: '600',
		height: 'auto',
		resizable: true,
		shadow: 'drop',
		shadowOffset: 10,
		title: 'Product Availability'
	});
});
//<!-- end swc:dialogPanel -->
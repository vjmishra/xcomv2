/**
 * Javascript file for the mini cart display page.
 *
 */

var minimalMiniCart = false;
var minimalMiniCartMessage = "";
var isGuestUser = false;
var canExpandMiniCart = false;
//var miniCartExpanded = false;
//var miniCartTimer = 0;
//var clickPending = false;

Ext.onReady(function() {
    if(document.miniCartForm.minimalMiniCart.value == "true")
    {
        minimalMiniCart = true;
    }
    if(document.miniCartForm.isGuestUser.value == "true")
    {
        isGuestUser = true;
    }
    minimalMiniCartMessage = document.miniCartForm.minimalMiniCartMessage.value;
    if(document.miniCartForm.canExpandMiniCart.value == "true")
    {
        canExpandMiniCart = true;
    }
//    showMiniCartLink();
    setCartEventHandlers("h");
})

function showMiniCartLink()
{
    Ext.Ajax.request({
        url : document.getElementById('miniCartLinkDisplayURL').href,
        params : {
        },
        method: 'GET',
        // if we get a successful response, parse the data then render the panel
        success: function ( response, request ) {
            var linkDiv = document.getElementById("miniCartLinkDiv");
            linkDiv.innerHTML = response.responseText;
            setCartEventHandlers("h");
        },
        failure: function ( response, request ) {
            alert(document.miniCartForm.miniCartDisplayError.value);
        }
    });
}

var qvw = null;
function setCartEventHandlers(initTriggerVal) {
    // We only want to display mini-cart if there will be something in it.
    // The case where there wouldn't be is if it is the minimalMiniCart, for
    // the guest user, AND there's no minimalMiniCartMessage to display.
    if(!(minimalMiniCart && isGuestUser && (minimalMiniCartMessage == "")) &&
       (canExpandMiniCart == true) )
    {
        /*
    	Ext.get('miniCartLinkDiv').on('mouseover',
            function(e,t)
            {
                if(!clickPending && !miniCartExpanded)
                {
                    miniCartTimer = setTimeout("showMiniCartWindow();", 2000);
                }
            }
        );
        Ext.get('miniCartLinkDiv').on('mouseout',
            function(e,t)
            {
                clearTimeout(miniCartTimer);
            }
        );
    	
        Ext.get('miniCartLinkDiv').on('click',
                function(e,t)
                {
                    clearTimeout(miniCartTimer);
                    clickPending = true;
                }
            );
        */
    	if(qvw === null)
    	{
	    	qvw = new QuickViewWindow('miniCartMouseoverArea', 
		    	{
		            url : document.getElementById('miniCartDisplayURL').href,
		            params: {
		                minimalMiniCart: minimalMiniCart,
		                minimalMiniCartMessage: minimalMiniCartMessage
		            },
		            method: 'POST',
	                callback:function(el, success, response){
		                if(success)
		                {
		                      svg_classhandlers_decoratePage();

		                }
		                else
		                {
		                    // el.dom.innerHTML = response.responseText;
                            // el.dom.innerHTML = document.miniCartForm.miniCartDisplayError.value;
                            alert(document.miniCartForm.miniCartDisplayError.value);
		                    qvw.closeWindow();
		                }
		             }
		        },
		        {
		        	initTrigger: initTriggerVal,
		        	offsetTop: 25,
		        	offsetLeft: -240,
	                animateFromAnchor:false,
                    windowConf:
                    {
                        width: 350,
                        autoHeight: true,
                        title: document.getElementById("miniCartTitle").value
                    }
		        }
	        );
    	}
    }
}
function refreshMiniCartLink(forceRefresh)
{
	if(typeof forceRefresh == "undefined")
    {
        forceRefresh = false;
    }

    Ext.Ajax.request({
        url : document.getElementById('miniCartLinkDisplayURL').href,
        params : {
            forceRefresh: forceRefresh
        },
        method: 'GET',
        // if we get a successful response, parse the data then render the panel
        success: function ( response, request ) {
            var linkDiv = document.getElementById("miniCartLinkDiv");
            linkDiv.innerHTML = response.responseText;
            $('.mini-cart-trigger').activate();
//            if(qvw !== null)
//            {
//                qvw.getWindowHandle().destroy();
//                qvw = null;
//                setCartEventHandlers("hi");
//            }
        },
        failure: function ( response, request ) {
            alert(document.miniCartForm.miniCartDisplayError.value);
        }
    });

}

function showMiniCartWindow() {
	if(qvw !== null)
	{
		qvw.createWindow();
	}
	/*
    Ext.Ajax.request({
        url : document.getElementById('miniCartDisplayURL').href,
        params: {
            minimalMiniCart: minimalMiniCart,
            minimalMiniCartMessage: minimalMiniCartMessage
        },
        method: 'GET',
        success: function (response, request){
            var cartDiv = document.getElementById("miniCartDiv");
            cartDiv.innerHTML = response.responseText;
            cartDiv.style.display = "inline";
            miniCartExpanded = true;
            svg_classhandlers_decoratePage();
        },
        failure: function (response, request){
            alert(document.miniCartForm.miniCartDisplayError.value);
        }
    });
    */
}

function hideMiniCartWindow(){
    var cartDiv = document.getElementById("miniCartDiv");
    cartDiv.style.display="none";
    miniCartExpanded = false;
}

function toggleVisibility(divID)
{
    var divElement = document.getElementById(divID);
    if(divElement.style.display != "none")
    {
        divElement.style.display = "none";
    }
    else
    {
        divElement.style.display = "inline";
    }
}

function deleteLine(orderHeaderKey, lineKey){
    //alert(lineKey);
    Ext.Ajax.request({
        url: document.getElementById('miniCartDeleteURL').href,
        params: {
            delLineKey:lineKey,
            orderHeaderKey: orderHeaderKey,
            operation:"DeleteLine"
        },
        method: 'GET',
        success: function (response, request){
        	var anchorToreplace = document.getElementById("XPEDXMiniCartLinkDisplayDiv");
        	anchorToreplace.innerHTML= Ext.util.Format.trim(response.responseText);
        },
        failure: function (response, request){
            alert(document.miniCartForm.miniCartGeneralAJAXError.value);
        }
    });

}

function updateLines() {
    if(swc_validateForm("miniCartData") == false)
    {
        return;
    }
    
    document.miniCartData.action = document.miniCartData.updateURL.value;
    Ext.Ajax.request({
        url: document.getElementById('miniCartUpdateURL').href,
        form: 'miniCartData',
        method: 'POST',
        success: function (response, request){
            refreshMiniCartLink();
        },
        failure: function (response, request){
            alert(document.miniCartForm.miniCartGeneralAJAXError.value);
        }
    });
}

function miniCartCheckout() {
    if(swc_validateForm("miniCartData") == false)
    {
        return;
    }
    
    document.miniCartData.action = document.miniCartData.checkoutURL.value;
    document.miniCartData.submit();
}

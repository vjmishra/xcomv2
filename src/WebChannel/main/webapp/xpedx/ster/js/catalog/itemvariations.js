
//Ext.onReady(function() {
//		javascript:SVGAnnotator.makeRoundedNoBorder(".submitBtnBg2");
//		javascript:SVGAnnotator.makeSimpleGradientBackground(".t5-container","white","aliceblue");
//		javascript:SVGAnnotator.makeSimpleGradientBackground(".listTableBody3","white","aliceblue");
//});

tabifyOnLoad("Content");
var selectionArray = new Array();
var attrArray = new Array();
var completeProductList = new Array();
var productList = new Array();
var newcompleteProductList = new Array();
var valueProdBitMapArray = new Array();
var idx = 0;


function findItem(attrValue,attrName)
    {
	   if(selectionArray[attrName] != attrName){
		  for (var m in attrArray[selectionArray[attrName]]) {
			  if(attrArray[selectionArray[attrName]].hasOwnProperty(m)){
				 //Not sure why # being used to read the resource ID
				 //image = Ext.DomQuery.selectNode("#"+attrArray[selectionArray[attrName]][m]);
				 image = document.getElementById(attrArray[selectionArray[attrName]][m]);
				 setOpacity(image,200);
			  }
	      }
	   }

	   for (var m in attrArray[attrValue]) {
		   if(attrArray[attrValue].hasOwnProperty(m)){
				//Not sure why # being used to read the resource ID
				//image = Ext.DomQuery.selectNode("#"+attrArray[attrValue][m]);
				image = document.getElementById(attrArray[attrValue][m]);
				setOpacity(image,50);
		   }
	   }

	   selectionArray[attrName] = attrValue;
	   selectionOfItem();
	   svg_classhandlers_decoratePage();
	}

	function selectionOfItem() {
		var productList = new Array();
		var i = 0;
		for (var t in selectionArray) {
			if(selectionArray.hasOwnProperty(t)){
				if(selectionArray[t] != t){
					var m = selectionArray[t];
					productList[i] = valueProdBitMapArray[m];
					i++;
				}
			}
		}
		if(productList.length > 1){
			var mergedList = merge(productList);
			checkAndDisplayResult(mergedList);
		}
	}

	function checkAndDisplayResult( mergedList ){
		var z = 0;
		for(i = 0 ; i < totalChildItem ; i++){
			z = z + mergedList[i];
		}
		if(z == 1){
			var index = getFinalProductIndex(mergedList);
			var product = completeProductList[index];
			lit = '<a href="'+product.childDetailURL +'"> <img src="' +product.childImageURL + '" width=200 height=180 /></a>';

			/*lit =  lit + '<p> <a href="#"> Item ID : ' +product.ID + '</a> <BR>   Unit Price :' + product.unitPrice +' <BR> ';

				for (var m in product.attrNameList) {
					if(product.attrNameList.hasOwnProperty(m)){
						lit = lit + product.attrNameList[m] + ' : ' + product.attrValueList[product.attrNameList[m]] + ' <BR> ';
					}
				}
			lit = lit + '</p>';*/
			lit =  lit + '<p> <B>Item ID: </B><a href="'+product.childDetailURL +'">' +product.ID + '</a><BR><B>Unit Price: </B>' + product.unitPrice +' <BR> ';
			for (var m in product.attrNameList) {
					if(product.attrNameList.hasOwnProperty(m)){
						//lit = lit + product.attrNameList[m] + ' : ' + product.attrValueList[product.attrNameList[m]] + ' <BR> ';
						lit = lit + '<B>'+product.attrDescList[m] + ' :</B> ' + product.attrValueList[product.attrNameList[m]] + ' <BR> ';
					}
				}
			lit = lit + '<BR><input id="addToCart" type="submit" value="Add To Cart" class="submitBtnBg2" onclick="javascript:variantAddToCart(\'' + product.ID + '\',\'' + product.UOM + '\');return false;"/>'+'</p>';

			Ext.DomQuery.selectNode("#childProduct").innerHTML=lit;
		}
		if(z == 0){
			Ext.DomQuery.selectNode("#childProduct").innerHTML= '<p> No Item is selected </p>';
		}
	}

	function getFinalProductIndex( mergedList ){
		for(i = 0 ; i < totalChildItem ; i++){
			if( mergedList[i] == 1){
			   return i;
			}
		}

	}

	function merge( productList ){
		var numberOfValuesSelected = productList.length;
		var mergedProdList = new Array();
		for (  i = 0 ; i < totalChildItem ; i++) {
			mergedProdList[i] = 1;
			for (  m = 0; m < numberOfValuesSelected ; m ++) {
				if(productList[m][i] == 0){
				   mergedProdList[i] = 0;
		   		}
			}
		}
		return mergedProdList;
	}

  function setOpacity(obj, opacity) {
       opacity = (opacity == 100)?99.999:opacity;

	   // IE/Win
	   obj.style.filter = "alpha(opacity:"+opacity+")";

	   // Safari<1.2, Konqueror
	   obj.style.KHTMLOpacity = opacity/100;

	   // Older Mozilla and Firefox
	   obj.style.MozOpacity = opacity/100;

	   // Safari 1.2, newer Firefox and Mozilla, CSS3
	   obj.style.opacity = opacity/100;
   }

  /*function product(ID,unitPrice,attrNameList,attrValueList,childImageURL,childDetailURL)
  {
		this.ID=ID;
		this.unitPrice=unitPrice;
		this.attrNameList = attrNameList;
		this.attrValueList = attrValueList;
		this.childImageURL = childImageURL;
		this.childDetailURL = childDetailURL;

  }  */

  function product(ID,UOM,unitPrice,attrNameList,attrValueList,attrDescList,childImageURL,childDetailURL)
  {
		this.ID=ID;
		this.UOM=UOM;
		this.unitPrice=unitPrice;
		this.attrNameList = attrNameList;
		this.attrValueList = attrValueList;
		this.attrDescList = attrDescList;
		this.childImageURL = childImageURL;
		this.childDetailURL = childDetailURL;
  }


  function addToCart(url, productID, UOM, quantity, headerKey)
  {
  	Ext.Ajax.request({
      	// for testing only
          url: url,
          params: {
  			productID: productID,
  	    	productUOM: UOM,
  	    	quantity: quantity,
  	    	orderHeaderKey: headerKey
          },
          // end testing
          method: 'GET',
          success: function (response, request){
              refreshMiniCartLink();
              //var myDiv = document.getElementById("ajax-body-1");
              //myDiv.innerHTML = response.responseText;
              //DialogPanel.toggleDialogVisibility('modalDialogPanel1');
          },
          failure: function (response, request){
              var myDiv = document.getElementById("ajax-body-1");
              myDiv.innerHTML = response.responseText;
              DialogPanel.show('modalDialogPanel1');
              svg_classhandlers_decoratePage();
          }
      });
  }

  function variantAddToCart(productID, UOM)
  {
      var myDiv = document.getElementById("ajax-body-1");
      var url = document.getElementById("addToCartURL").value;
      addToCart(url, productID, UOM);
  }

  function findPos(obj) {
  	var curleft = curtop = 0;
  	if (obj.offsetParent) {
  		do {
  			curleft += obj.offsetLeft;
  			curtop += obj.offsetTop;
  		} while (obj = obj.offsetParent);
  		return [curleft,curtop];
  	}
  }

  function blurObj(id)
  {
      var elem0 = document.getElementById(id);
      var pos = findPos(elem0);
      //alert("left: " + pos[0] + "; top: " + pos[1]);
      //alert("width: " + elem0.offsetWidth + "; height: " + elem0.offsetHeight);

      var newdiv = document.createElement('div');
      document.body.appendChild(newdiv);
      newdiv.id="_blur_"+id;
      newdiv.className = "gray_overlay";
      newdiv.style.left=pos[0]+"px";
      newdiv.style.top=pos[1]+"px";
      newdiv.style.width=elem0.offsetWidth+"px";
      newdiv.style.height=elem0.offsetHeight+"px";

      //newdiv.innerHTML = "New DIV";
      //var pos = findPos(newdiv);
      //alert("left: " + pos[0] + "; top: " + pos[1]);
      //alert("width: " + newdiv.offsetWidth + "; height: " + newdiv.offsetHeight);

      return newdiv;
  }

  function unblurObj(id)
  {
      var myId = "_blur_" + id;
      var elem0 = document.getElementById(myId);
      elem0.style.display="none";
  }
  

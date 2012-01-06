function writeMetaTag(tag,content) { 			
		var linkUrl="/swc/xpedx/jsp/common/XPEDXWebtrends.jsp?tag="+tag+"&content="+content;
		linkUrl=addCSRFToken(linkUrl, 'link');
		try {
	        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
	    } catch (e) {
	        try {
	            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	        } catch (E) {
	            xmlhttp = false;
	        }
	    }
	    if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
	        try {
	            xmlhttp = new XMLHttpRequest();
	        } catch (e) {
	            xmlhttp = false;
	        }
	    }
	    if (!xmlhttp && window.createRequest) {
	        try {
	            xmlhttp = window.createRequest();
	        } catch (e) {
	            xmlhttp = false;
	        }
	    }	    
	    xmlhttp.open("GET", linkUrl, true);
	    
	    xmlhttp.onreadystatechange = function() {
	        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
	             //alert(xmlhttp.responseText)
	            // OR write code which you want to execute after completing the async code.
	        }
	    }
	    xmlhttp.send(null)
}
function writeMetaTag(tagList,contentList,cnt){ 	
	//alert("hi111");
	
	var linkUrl= "/swc/xpedx/jsp/common/XPEDXWebtrends.jsp?tag="+tagList+"&content="+contentList+"&noOfTags="+cnt; 
	linkUrl=addCSRFToken(linkUrl, 'link');
	linkUrl=addCSRFToken(linkUrl, 'link');
	try {
        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
        try {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (E) {
            xmlhttp = false;
        }
    }
    if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
        try {
            xmlhttp = new XMLHttpRequest();
        } catch (e) {
            xmlhttp = false;
        }
    }
    if (!xmlhttp && window.createRequest) {
        try {
            xmlhttp = window.createRequest();
        } catch (e) {
            xmlhttp = false;
        }
    }	    
    xmlhttp.open("GET", linkUrl, true);
    
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
             //alert(xmlhttp.responseText)
            // OR write code which you want to execute after completing the async code.
        }
    }
    xmlhttp.send(null)}

function ReplaceAll(Source,stringToFind,stringToReplace){
	  var temp = Source;
	    var index = temp.indexOf(stringToFind);
	        while(index != -1){
	            temp = temp.replace(stringToFind,stringToReplace);
	            index = temp.indexOf(stringToFind);
	        }
	        return temp;
	}

function createDummyAsyncRequest() {  
	//alert("in createDummy");
    try {
        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
        try {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (E) {
            xmlhttp = false;
        }
    }
    if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
        try {
            xmlhttp = new XMLHttpRequest();
        } catch (e) {
            xmlhttp = false;
        }
    }
    if (!xmlhttp && window.createRequest) {
        try {
            xmlhttp = window.createRequest();
        } catch (e) {
            xmlhttp = false;
        }
    }
    var linkUrl="/swc/xpedx/jsp/common/XPEDXWebtrends.jsp?tag=tagname1"+"&content=content1";
    xmlhttp.open("GET", linkUrl, true);
    
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            // alert(xmlhttp.responseText)
            // OR write code which you want to execute after completing the async code.
        }
    }
    xmlhttp.send(null)
    
}


/* This file is sepcific to any methods related to General Information page in User Profile.
 * 
 */

/**
 * @author Ragini
 * Created: 03/18/2011 3:34:12 PM
 */
/**
 * writeMetaTag function. Does the following tasks:
 *  1. Takes Name of meta Tag and content as input parameters of type String 
 *  2. creates a iFrame and loads XPEDXWebtrends.jsp, after processing it, then delete the iFrame 
 * 
 */
function writeMetaTag(tag,content) { 
	
		var iframeElement = document.createElement("iframe"); 
		iframeElement.id = 'iframeElement'; 
		document.body.appendChild(iframeElement);

		iframeElement.onreadystatechange = function () { 

		if (iframeElement.readyState == 'complete') { 

		if (document.getElementById('iframeElement')) 
		document.body.removeChild(iframeElement);

		}		
		} 
		iframeElement.style.width = '0px' 
		iframeElement.style.height ='0px' 
		iframeElement.style.display = 'none'; 
		var linkUrl="/swc/xpedx/jsp/common/XPEDXWebtrends.jsp?tag="+tag+"&content="+content;
		linkUrl=addCSRFToken(linkUrl, 'link');
		iframeElement.src =  linkUrl;
}

function writeMetaTag(tagList,contentList,cnt){ 	
	
	var iframeElement = document.createElement("iframe"); 
	iframeElement.id = 'iframeElement'; 
	document.body.appendChild(iframeElement);

	iframeElement.onreadystatechange = function () { 

	if (iframeElement.readyState == 'complete') { 

	if (document.getElementById('iframeElement')) 
	document.body.removeChild(iframeElement);

	}		
	} 
	iframeElement.style.width = '0px' 
	iframeElement.style.height ='0px' 
	iframeElement.style.display = 'none'; 
	var linkUrl= "/swc/xpedx/jsp/common/XPEDXWebtrends.jsp?tag="+tagList+"&content="+contentList+"&noOfTags="+cnt; 
	linkUrl=addCSRFToken(linkUrl, 'link');
	iframeElement.src =linkUrl;
}

function ReplaceAll(Source,stringToFind,stringToReplace){
	  var temp = Source;
	    var index = temp.indexOf(stringToFind);
	        while(index != -1){
	            temp = temp.replace(stringToFind,stringToReplace);
	            index = temp.indexOf(stringToFind);
	        }
	        return temp;
	}


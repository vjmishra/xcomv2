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
 *  2. creates a iFrame and loads Webtrends.jsp, after processing it, then delete the iFrame 
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
		var linkUrl="/swc/webtrends/jsp/common/Webtrends.jsp?tag="+tag+"&content="+content;
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
	var linkUrl= "/swc/xpedx/jsp/common/Webtrends.jsp?tag="+tagList+"&content="+contentList+"&noOfTags="+cnt; 
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

//-- Web Trends tag start --
function writeWebtrendTag(responseText){
	var variable1 = 'meta name=\"DCSext.w_x_sc\"' ;
	var variable2 = 'meta name=\"DCSext.w_x_scr\"' ;
	var variable3 = "content=";
	var variable4 = "content=";
	var try21 = responseText.indexOf(variable1);
	if(try21=="-1")
			return;
	var try211 = responseText.indexOf(variable3);
	var try22 = responseText.indexOf(variable2);
	var try222 = responseText.lastIndexOf(variable4);
	var try3 = responseText.substring(try21+10,try211-1);
	
	
	var try4 = responseText.substring(try22+10,try222-1);
	
	var try5 = responseText.indexOf(try3);
	var try51 = responseText.substring(try5+try3.length);
	
	var test1 = try51.indexOf("content=");
	var test11 = try51.indexOf("></meta");
	var test111 = try51.substring(test1+8,test11);		
	
	var test2 = try51.lastIndexOf("content=");
	var test22 = try51.lastIndexOf("></meta");
	var test222 = try51.substring(test2+8,test22);		
	
	tag = "DCSext.w_x_sc,DCSext.w_x_scr";
	content = test111 + ","+test222;	
	
	content = ReplaceAll(content,"'","");
	content= ReplaceAll(content,'"','');		
			
	writeMetaTag(tag,content,2);
	
}
//-- Web Trends tag end --


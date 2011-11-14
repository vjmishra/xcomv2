var DL_bNS4=(document.layers);
var DL_bNS6 = (navigator.vendor == ("Netscape6") || navigator.product == ("Gecko"));
var DL_bDOM=(document.getElementById);
var DL_bIE=(document.all);
var DL_bIE4=(DL_bIE && !DL_bDOM);
var DL_bMac = (navigator.appVersion.indexOf("Mac") != -1);
var DL_bIEMac=(DL_bIE && DL_bMac);
var DL_bIE4Mac=(DL_bIE4 && DL_bMac);
var DL_bNS =(DL_bNS4 || DL_bNS6);
var DL_nCurrentX = 0;
var DL_nCurrentY = 0;
var currentZIndex=5001;
var DL_oLayer;
var windowWidth;
var leftLocation;
var r=0;
var t=0;
var rightLocation = 0;
var topLocation = 0;
var velocity = 1.01;
var accelFactor = 1.1;
var isNewNote=false;
var deleto = false;
var notesUpdated = false;

var groupnumber="";
var show_menubar_default=true;
var show_notes_default=true;
var private_database_key="";

function addOnloadEvent(fnc){
  if ( typeof window.addEventListener != "undefined" )
    window.addEventListener( "load", fnc, false );
  else if ( typeof window.attachEvent != "undefined" ) {
    window.attachEvent( "onload", fnc );
  }
  else {
    if ( window.onload != null ) {
      var oldOnload = window.onload;
      window.onload = function ( e ) {
        oldOnload( e );
        window[fnc]();
      };
    }
    else 
      window.onload = fnc;
  }
}

//window.onload = function() {
function loadProtonotes() {
	if (groupnumber=="") {
		alert("You do not have a group number set for Protonotes. See protonotes.com for details.");	
		return false;
	}
	//insert css & javascripts
	var oHead = document.getElementsByTagName('HEAD').item(0); 
	var cssNode = document.createElement("LINK"); 
	cssNode.type = "text/css"; 
	cssNode.rel = "stylesheet";
	cssNode.href = "http://www.protonotes.com/stylz/style.css";
	oHead.appendChild(cssNode); 
	var oScript1= document.createElement("script"); 
	oScript1.type = "text/javascript"; 
	oScript1.src="http://www.protonotes.com/js/ui.js"; 
	oHead.appendChild(oScript1); 
	//insert HTML
	//top menu
	var oBody = document.getElementsByTagName('BODY').item(0); 
	var oElement1= document.createElement("div"); 
	oElement1.id = "ProtonotesMenu"; 
	oBody.appendChild(oElement1);
	document.getElementById('ProtonotesMenu').innerHTML = "<div class='ProtonotesBackgrounder'><table width='100%' border='0' cellpadding='0' cellspacing='0' ><tr> <td nowrap='nowrap' valign='top'><a href='http://www.industrialwisdom.com/'><img src='http://xpedx.industrialwisdom.com/js/badge4.gif' alt='Protonotes - Annotate your prototypes.' border='0' style='margin-left:3px !important;'/></a></td><td width='100%' align='right' nowrap='nowrap' style='padding-right:5px'><img src='http://www.protonotes.com/images/spacer.gif' id='waiter' style='margin-right:25px;padding-bottom:18px !important' /><img src='http://www.protonotes.com/images/newNote.gif' onclick='isNewNote=true;showHideAllNotes(true);insertNote(\"yes\");saveNewNote();isNewNote=false;' title='create new note' /><img src='http://www.protonotes.com/images/showNotes.gif' style='margin-left:15px;margin-right:5px;' title='show all notes' onclick='showHideAllNotes(true)'/><img src='http://www.protonotes.com/images/hideNotes.gif' title='hide all notes' onclick='showHideAllNotes(false)'/><img src='http://www.protonotes.com/images/index.gif' title='show all group notes on all web pages' onclick='viewNotesIndex()' style='margin-left:7px;'/></td><td align='right'><img src='http://www.protonotes.com/images/collapse.gif' onclick='slideIt();' id='menuPusher' title='slide in'/></td></tr></table><iframe frameborder='0' id='menuIframe' scrolling='no'  style='height:40px;display:none;position:absolute;top:0px;left:0px;z-index:-100;filter:progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)'></iframe></div><!--[if lt IE 7]><link href='http://www.protonotes.com/stylz/styleIE.css' rel='stylesheet' type='text/css'><![endif]-->";
	oElement1.style.display="block";
	//note template
	var oElement2 = document.createElement("div"); 
	oElement2.id = "notesContainer"; 
	oBody.appendChild(oElement2); 
	document.getElementById('notesContainer').innerHTML = "<div class='notesLayer' id='noteTemplate'><iframe class='bufferIframe' src='http://www.protonotes.com/transparentIframe.html'  frameborder='0'></iframe><input name='left' type='hidden' value='100px' /><input name='top' type='hidden' value='100px' /><input name='width' type='hidden' value='200px' /><input name='height' type='hidden' value='200px' /><div class='dragBar'><img src='http://www.protonotes.com/images/x.gif' style='cursor:pointer;position:relative;top:3px;' onmousedown='opacity(event,this.parentNode.parentNode, 100, 0, 500);' />&nbsp;</div><fieldset style='border:none;padding:0px;margin:0px;padding-left:4px;padding-right:4px;margin-right:0px;'><div style='overflow:auto;'><textarea id='noteBody' class='protoText' onblur='DL_oLayer=this.parentNode.parentNode.parentNode;saveExistingNote();' ></textarea></div></fieldset><table style='float:left;'><tr><td><input name='' type='checkbox' value='reviewed' style='width:12px;' onclick='DL_oLayer=this.parentNode.parentNode.parentNode.parentNode.parentNode;saveExistingNote()'/></td><td><span style='font-size:9px;color:#666666;'>&nbsp;reviewed</span></td><td style='width:10px;'></td><td></td><td><input name='' type='checkbox' value='completed' style='width:12px;' onclick='DL_oLayer=this.parentNode.parentNode.parentNode.parentNode.parentNode;saveExistingNote()'/><input name='noteId' type='hidden' value='null' /></td><td><span style='font-size:9px;color:#666666;'>&nbsp;completed</span></td></tr></table><img src='http://www.protonotes.com/images/dragger.gif' style='cursor:se-resize;position:relative;bottom:-12px;padding-right:2px;'/></div><a id='bottomMarker' ></a>";
	//white mask while dragging
	var oElement3 = document.createElement("div"); 
	oElement3.id = "noteSeparatorLayer"; 
	oBody.appendChild(oElement3);
	//data iframe
	var oElement4 = document.createElement("iframe"); 
	oElement4.id = "dataFrame"; 
	oBody.appendChild(oElement4);
	document.getElementById('dataFrame').style.display="none";
	//
	var oElement5 = document.createElement("iframe"); 
	oElement5.id = "dataFrame1"; 
	oBody.appendChild(oElement5);
	document.getElementById('dataFrame1').style.display="none";
	//
	currentNote=document.getElementById('noteTemplate');
	d=currentNote.parentNode;
	noteTemplateNode = d.removeChild(document.getElementById('noteTemplate'));
	insertBeforeMarker = document.getElementById('bottomMarker');
	//windowWidth = (navigator.userAgent.indexOf("MSIE")!=-1) ? document.documentElement.clientWidth : document.getElementById('menu').scrollWidth;
	//leftLocation = -windowWidth + 11;
	//document.getElementById('menuIframe').style.width = windowWidth + "px";
	//
	//setTimeout("resetMenubarWidth()",100);
	initialResetMenuBarWidth();
	//
	updateNotes();
	//
	preImage = new Image();
	preImage.src = "http://www.protonotes.com/images/expand.gif";
}

function initialResetMenuBarWidth() {
	if(notesUpdated==true) {
		resetMenubarWidth();
		if (show_menubar_default != true) {//hide menu if default hide is set
			r = leftLocation;
			document.getElementById('ProtonotesMenu').style.left = leftLocation + "px";
			document.getElementById('menuPusher').src = "http://www.protonotes.com/images/expand.gif";
			document.getElementById('menuPusher').title="slide out";
		}
	} else {
		setTimeout("initialResetMenuBarWidth()",100);
		
	}
}


addOnloadEvent(loadProtonotes);

function initializeNotes() {
		currentNote=document.getElementById('noteTemplate');
		d=currentNote.parentNode;
		noteTemplateNode = d.removeChild(document.getElementById('noteTemplate'));
		insertBeforeMarker = document.getElementById('bottomMarker');
		var oHead = document.getElementsByTagName('HEAD').item(0); 
		var oScript1= document.createElement("script"); 
		oScript1.type = "text/javascript"; 
		oScript1.src="javascript/YellowFade.js"; 
		oHead.appendChild(oScript1); 
		var oScript2= document.createElement("script"); 
		oScript2.type = "text/javascript"; 
		oScript2.src="javascript/ui.js"; 
		oHead.appendChild(oScript2); 
		var oScript3= document.createElement("script"); 
		oScript3.type = "text/javascript"; 
		oScript3.src="javascript/jsr_class.js"; 
		oHead.appendChild(oScript3); 
		//
		setTimeout('updateNotes()',1000);
}

function saveExistingNote() {//save existing note
		//window.status=deleto;
		if (deleto==true) {
			deleto=false;
			return true;
		}
		var noteBody = DL_oLayer.getElementsByTagName('textarea')[0].value;
		var left = DL_oLayer.style.left;
		if (parseFloat(left)<0) {left="0px"}
		//tempyTop = Math.max(window.document.body.scrollTop,window.document.documentElement.scrollTop);
		//var top = parseFloat(DL_oLayer.style.top) * 1 + tempyTop*1;
		//var top = top + "px";
		var top = DL_oLayer.style.top;
		if (parseFloat(top)<0) {top="0px"}
		DL_oLayer.getElementsByTagName('input')[1].value=top;
		var width = DL_oLayer.style.width;
		var height = DL_oLayer.getElementsByTagName('textarea')[0].style.height;
		if (DL_oLayer.getElementsByTagName('input')[4].checked) {
			var reviewed = 1;
		} else {
			var reviewed = 0;
		}
		if (DL_oLayer.getElementsByTagName('input')[5].checked) {
			var completed = 1;
		} else {
			var completed = 0;
		}
		var noteId = DL_oLayer.getElementsByTagName('input')[6].value;
		var page = window.document.location.href;
		page = page.replace(/&/, "~");
		page = escape(page);
		var url = "http://www.protonotes.com/_addEditNote.php?noteBody=" + escape(noteBody) + "&left=" + escape(left) + "&top=" + escape(top) + "&width=" + escape(width) + "&height=" + escape(height) + "&reviewed=" + reviewed + "&completed=" + completed + "&noteId=" + escape(noteId) + "&page=" + page + "&groupnumber=" + groupnumber + "&privatedb=" + private_database_key;
		//document.getElementById('dataFrame').src=url;
		document.getElementById('dataFrame').contentWindow.location.replace(url);
		rssURL = "http://www.protonotes.com/_rssFileMaker.php?groupnumber="+groupnumber+"&privatedb="+private_database_key+"&cacheKiller="+randomPassword(5);//update RSS feed
		//document.getElementById('dataFrame1').src=rssURL;
		document.getElementById('dataFrame1').contentWindow.location.replace(rssURL);
}

function randomPassword(length) {
  chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
  pass = "";
  for(x=0;x<length;x++) {
    i = Math.floor(Math.random() * 62);
    pass += chars.charAt(i);
  }
  return pass;
}

var currwidth;//when resizing the window, we have to reset the menubar width so that is hides correctly.
window.onresize = function(){
	if(currwidth != document.documentElement.clientWidth)
	{
		resetMenubarWidth();	
	}
	currwidth = document.documentElement.clientWidth;
}

function resetMenubarWidth() {
		//windowWidth = (navigator.userAgent.indexOf("MSIE")!=-1) ? document.documentElement.clientWidth : window.innerWidth;
		document.getElementById('ProtonotesMenu').style.width="100%";
		iw = ( document.documentElement.clientWidth ? document.documentElement.clientWidth : document.body.clientWidth );
		windowWidth = (navigator.userAgent.indexOf("MSIE")!=-1) ? iw : document.getElementById('ProtonotesMenu').scrollWidth;
		document.getElementById('ProtonotesMenu').style.width = windowWidth + "px";
		leftLocation = -windowWidth + 21;
		if (document.getElementById('menuPusher').src == "http://www.protonotes.com/images/expand.gif") {
			document.getElementById('ProtonotesMenu').style.left = leftLocation + "px";
			r=leftLocation;
		}
		document.getElementById('menuIframe').style.width = windowWidth + "px";
}

function saveNewNote() {//save new notes
		var page = window.document.location.href;
		page = page.replace(/&/, "~");
		page = escape(page);
		noteIDGen=randomPassword(10);
		//DL_oLayer.getElementsByTagName('input')[6].value=noteIDGen;
		DL_oLayer.getElementsByTagName('input')[6].value=noteIDGen;
		//DL_oLayer.getElementsByTagName('input')[6].value=675;
		var width = DL_oLayer.style.width;
		var height = DL_oLayer.getElementsByTagName('textarea')[0].style.height;
		//add randomPassword to avoid iframe caching problems
		var url = "http://www.protonotes.com/_addEditNote.php?noteBody=&left=100px&top=100px&width=&height=&reviewed=0&completed=0&noteId=" + noteIDGen + "&page=" + page + "&groupnumber=" + groupnumber + "&privatedb=" + private_database_key+"&newNote=true";
		//document.getElementById('dataFrame').src=url;
		document.getElementById('dataFrame').contentWindow.location.replace(url);
		//document.location=url;
		rssURL = "http://www.protonotes.com/_rssFileMaker.php?groupnumber="+groupnumber+"&privatedb="+private_database_key+"&cacheKiller="+randomPassword(5);//update RSS feed
		//document.getElementById('dataFrame1').src=rssURL;
		document.getElementById('dataFrame1').contentWindow.location.replace(rssURL);
		newNote.getElementsByTagName('textarea')[0].focus();
}

function deleteNote(deletedNoteId) {
	deleto=true;
	//var page = window.document.location.href;
	var url = "http://www.protonotes.com/_deleteNote.php?noteId="+escape(deletedNoteId) + "&privatedb=" + private_database_key;
	//document.getElementById('dataFrame').src=url;
	document.getElementById('dataFrame').contentWindow.location.replace(url);
	setTimeout("resetDeleto()",100);
	//alert(deletedNoteId);
	//window.status=deletedNoteId;
}

function resetDeleto() {
	deleto=false;	
}

function updateNotes() {
	document.getElementById('waiter').src="http://www.protonotes.com/images/wait.gif";
	var page = document.location.href;
	page = page.replace(/&/, "~");
	page = escape(page);
	var req = 'http://protonotes.com/_getNotesJSON.php?groupnumber=' + groupnumber + '&page=' + page + "&privatedb=" + private_database_key;
	try {
		bObj = new JSONscriptRequest(req); // Create a new request object
	}
	catch(err) {
		setTimeout('updateNotes()',100);
		return false;
	}
	bObj.buildScriptTag(); // Build the dynamic script tag
	bObj.addScriptTag(); // Add the script tag to the page
}

function ws_results(jsonData) {
	document.getElementById('waiter').src="http://www.protonotes.com/images/spacer.gif";
	if (jsonData.fields.note[0].noteBody=="emptyproto") {notesUpdated=true;return false}
	numberOfNotes = jsonData.fields.note.length;
	//document.getElementById('fillIt').innerHTML=jsonData.fields.note[0].noteBody;
	for (var i = 0; i < numberOfNotes; i++) {
		insertNote("no");
		textContainer = newNote.getElementsByTagName('textarea')[0];
		dragBar=newNote.getElementsByTagName('div')[0];
		//noteContent=jsonData.fields.note[i].noteBody;
		noteContent = jsonData.fields.note[i].noteBody.replace(/~~/g, "\r");//put carriage return back in for viewing notes
		noteContent = noteContent.replace(/~/g, "\n");//put carriage return back in for viewing notes
		//textContainer.innerHTML=noteContent;
		noteContent = noteContent.replace(/&quot;/g, "\"");
		textContainer.value=noteContent;
		iframeBuffer=newNote.getElementsByTagName('iframe')[0];
		newNote.style.left=jsonData.fields.note[i].left;
		tempyTop = Math.max(window.document.body.scrollTop,window.document.documentElement.scrollTop);
		newNote.style.top=parseInt(jsonData.fields.note[i].top) - tempyTop*1 + "px";
		newNote.getElementsByTagName('input')[6].value=jsonData.fields.note[i].noteId;
		newNote.style.width=jsonData.fields.note[i].width;
		dragBar.style.width=jsonData.fields.note[i].width;
		iframeBuffer.style.width=jsonData.fields.note[i].width;
		if(jsonData.fields.note[i].width != ""){
			iframeBuffer.style.width=parseInt(jsonData.fields.note[i].width)-0+"px";
		} else {
			iframeBuffer.style.width="200px";
		}
		if(jsonData.fields.note[i].height != ""){
			fullNoteHeight=parseInt(jsonData.fields.note[i].height) + 51 + "px";
		} else {
			fullNoteHeight="250px";

		}
		iframeBuffer.style.height=fullNoteHeight;
		//textContainer.style.height=jsonData.fields.note[i].height;
		textContainer.style.height=jsonData.fields.note[i].height; 
        textContainer.parentNode.style.overflow="hidden"; 
		
		//newNote.style.height=fullNoteHeight;
		newNote.getElementsByTagName('input')[0].value=jsonData.fields.note[i].left;
		newNote.getElementsByTagName('input')[1].value=jsonData.fields.note[i].top;
		if (jsonData.fields.note[i].reviewed==1) newNote.getElementsByTagName('input')[4].checked=true;
		if (jsonData.fields.note[i].completed==1) newNote.getElementsByTagName('input')[5].checked=true;
		if(show_notes_default==true){
			newNote.style.display="block";
		}
	}
	notesUpdated=true;
}

var targetId;
function insertNote(fadeIn) {
	noteId=null;
	newNote = noteTemplateNode.cloneNode(true);
	targetId = newNote;
	targetOpacity=90;
	//ff on mac has screwy issues with opacity layers above flash. Here I detect if FF2 and below on Mac and if so set opacity to 1
	var userAgent = navigator.userAgent.toLowerCase();
	if (/firefox[\/\s](\d+\.\d+)/.test(userAgent)) {
		var ffversion = new Number(RegExp.$1);
	}
	if (ffversion < 3 && userAgent.indexOf('mac') != -1) {
		targetOpacity=100;
	}
	if (fadeIn=="yes") {
		opacity1(targetId, 0, targetOpacity, 500); 
		newNote.style.left="100px";
		newNote.style.top="100px";
	} else {
		newNote.style.opacity = (targetOpacity / 100); 
		newNote.style.MozOpacity = (targetOpacity / 100); 
		newNote.style.KhtmlOpacity = (targetOpacity / 100); 
		newNote.style.filter = "alpha(opacity="+targetOpacity+")"; 
		newNote.style.display="none";
	}
	infoContainer = document.getElementById('notesContainer');
	infoContainer.insertBefore(newNote,insertBeforeMarker);
	DL_oLayer=newNote;
	currentZIndex++;
	DL_oLayer.style.zIndex=currentZIndex;
	//DL_oLayer.getElementsByTagName('input')[6].value = randomPassword(20);
	DL_oLayer.getElementsByTagName('div')[0].onmousedown=DL_fGrabEl;  
	iframeBuffer=DL_oLayer.getElementsByTagName('iframe')[0];
	dragResizer=DL_oLayer.getElementsByTagName('img')[DL_oLayer.getElementsByTagName('img').length-1];
	dragResizer.onmousedown= expand;  
	DL_oLayer.getElementsByTagName('div')[0].onmousedown=DL_fGrabEl; 
	DL_oLayer.style.display="none";
	newNote.getElementsByTagName('iframe')[0].src=newNote.getElementsByTagName('iframe')[0].src;
	if (fadeIn=="yes") {
		DL_oLayer.style.display="block";
		//DL_oLayer.getElementsByTagName('textarea')[0].focus();
	}
	
}

function showNote() {
	DL_oLayer.style.display="block";	
}

function showHideAllNotes(hide) {
	allDivs =document.getElementsByTagName('div');
	numOfDivs = allDivs.length;
	notesExist=false;
	// get all notes DIVS
	for (var i = 0; i < numOfDivs; i++) {
		if (allDivs[i].className=="notesLayer") {
			notesExist=true;
			if (!hide) {
				allDivs[i].style.display="none";
			} else {
				allDivs[i].style.display="block";
			}
		}
	}
	if (!notesExist && !isNewNote) alert('There are no notes on this page.');
}
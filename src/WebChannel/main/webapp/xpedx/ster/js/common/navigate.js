function initOpenClose()
{
	var blocks = document.getElementsByTagName("li");
	for (var i=0; i<blocks.length; i++)
	{
		if (blocks[i].className=="roll")
		{
			if ( blocks[i].className.indexOf("close") == -1 ) blocks[i].className += " close";
			var links = blocks[i].getElementsByTagName("a");

				for (var k=0; k<links.length; k++) {

					if (links[k].className == "button") {

						links[k].onclick = function()
						{
							/*
							if ( this.parentNode.parentNode.className.indexOf("close") != -1 )
							{
								this.parentNode.parentNode.className = this.parentNode.parentNode.className.replace("close", "");
							}
							else
							{
								this.parentNode.parentNode.className += " close";
							}
							*/
							if(this.parentNode.className.indexOf("close") != -1 )
							{
								this.parentNode.className = this.parentNode.parentNode.className.replace("close", "");
							}
							else
							{
								this.parentNode.className += " close";
							}
                            svg_classhandlers_decoratePage();
                            return false;
						}
				}
			}
		}
	}
}
function initNav()
{
	if (typeof document.body.style.maxHeight == 'undefined')
	{
		var nav = document.getElementById("navigate");
		if(nav != null)
		{
		var lis = nav.getElementsByTagName("li");
		for (var i=0; i<lis.length; i++)
		{
			lis[i].onmouseover = function()
			{
				this.className += " hover";
			}
			lis[i].onmouseout = function()
			{
				this.className = this.className.replace(" hover", "");
			}
		}
		}
	}
}

if (window.addEventListener)
	window.addEventListener("load", initOpenClose, false);
else if (window.attachEvent)
{
	window.attachEvent("onload", initOpenClose);
	window.attachEvent("onload", initNav);
}

sfHover = function() {
	var temp=document.getElementById("navigate");
	if(temp != null) {
	var sfEls = document.getElementById("navigate").getElementsByTagName("LI");
	for (var i=0; i<sfEls.length; i++) {
		sfEls[i].onmouseover=function() {
			this.className+=" sfhover";
		}
		sfEls[i].onmouseout=function() {
			this.className=this.className.replace(new RegExp(" sfhover\\b"), "");
		}
	}
	}
}
if (window.attachEvent) window.attachEvent("onload", sfHover);

Ext.onReady(function() {
	var lnks = Ext.query('.showMoreInfo');
    for (var i = 0; i < lnks.length; i++) {
    	Ext.get(lnks[i]).on('click',function(){
		var src = Ext.get('MoreInfo');
		src.setDisplayed("inline")
	});
    }

	lnks = Ext.query('.hideMoreInfo');
    for (var i = 0; i < lnks.length; i++) {
    	Ext.get(lnks[i]).on('click',function(){
		var src = Ext.get('MoreInfo');
		src.setDisplayed("none")
	});
    }
});

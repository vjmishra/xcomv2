function expandCollapse(lnk, content, showImg, hideImg)
{
	var xlnk = Ext.get(lnk);
	var xcont = Ext.get(content);
	if(xlnk.hasClass("selected"))
	{
		xlnk.removeClass("selected");
		var inner = '<span class="showDetails"><img src="'+showImg+'" width="10" height="10" /></span>';
		xlnk.update(inner);
	}
	else
	{
		xlnk.addClass("selected");
		var inner = '<span class="hideDetails"><img src="'+hideImg+'" width="10" height="10" /></span>';
		xlnk.update(inner);
	}

	if(xcont.isDisplayed())
	{
		xcont.setDisplayed("none");
	}
	else
	{
		xcont.setDisplayed("inline");
	}
}

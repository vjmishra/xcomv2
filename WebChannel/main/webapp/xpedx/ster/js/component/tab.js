/*==================================================
  $Id: tab.js,v 1.1 2011/03/07 06:20:16 jkotha Exp $
  tabber.js by Patrick Fitzgerald pat@barelyfitz.com

  Documentation can be found at the following URL:
  http://www.barelyfitz.com/projects/tabber/

  License (http://www.opensource.org/licenses/mit-license.php)

  Copyright (c) 2006 Patrick Fitzgerald

  Permission is hereby granted, free of charge, to any person
  obtaining a copy of this software and associated documentation files
  (the "Software"), to deal in the Software without restriction,
  including without limitation the rights to use, copy, modify, merge,
  publish, distribute, sublicense, and/or sell copies of the Software,
  and to permit persons to whom the Software is furnished to do so,
  subject to the following conditions:

  The above copyright notice and this permission notice shall be
  included in all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
  BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
  ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
  ==================================================*/

function tabObj(argsObj)
{
  var arg; /* name of an argument to override */

  this.div = null;
  //"tabcontainer","tab_1_content_class","tabnavigation","hidetab","activetab"
  /* Rename classMain to classMainLive after tabifying (so a different style can be applied)*/
  this.newMainTabClass = "tab-1";
  /* Start tabindex for the tab links */
  this.startindex = 0;
  if(argsObj.startindex)
  {
	  this.startindex = argsObj.startindex;
  }
  /* Class of each DIV that contains a tab */
  this.classTab = "tabContent";
  /* Class for the navigation UL */
  //this.classNav = "tabnavigation";
  /* When a tab is to be hidden, we set the class of the div to classTabHide. */
  this.classTabHide = "hidetab";
  /* Class to set the navigation LI when the tab is active, so a different style can be used on the active tab. */
  this.classNavActive = "selected";
  this.classNavInactive = "selectable";

  /* Class to indicate which tab should be active on startup */
  this.defaultTabClass = "default_tab";

  for (arg in argsObj) {
	  this[arg] = argsObj[arg];
	  }

  this.REclassMain = new RegExp('\\b' + this.classMainTab + '\\b', 'gi');
  this.REclassTabHide = new RegExp('\\b' + this.classTabHide + '\\b', 'gi');
  this.REclassTabDefault = new RegExp('\\b' + this.defaultTabClass + '\\b', 'gi');
  this.REclassTab = new RegExp('\\b' + this.classTab + '\\b', 'gi');

  /* Array of objects holding info about each tab */
  this.tabs = new Array();

  /* Added showTab event based on Ext event mechansim */
  this.addEvents({
      "showTab" : true
  });

  /* If the main tabber div was specified, call init() now */
  if (this.div) {

    this.init(this.div);

    this.div = null;
  }
}

// Extends Observable so we can add new events
Ext.extend(tabObj, Ext.util.Observable);

/*--------------------------------------------------
  Methods for tabObj
  --------------------------------------------------*/


tabObj.prototype.init = function(e)
{
  var
  childNodes, /* child nodes of the tabber div */
  i,  /* loop indices */
  t,  /* object to store info about a single tab */
  defaultTab=0, /* which tab to select by default */
  TAB_ul, /* tabbernav list */
  TAB_li, /* tabbernav list item */
  TAB_a, /* tabbernav link */
  aId,   /* A unique id for TAB_a */
  d;     /* A temp variable to hold container div */


  /* Clear the tabs array (but it should normally be empty) */
  this.tabs.length = 0;

  /* Get the child node with matching class name*/
  d = Ext.DomQuery.selectNode("."+this.mainClass);

  childNodes = d.childNodes;//Ext.DomQuery.select("."+this.classTab,d);

  //childNodes = Ext.query("*[class="+this.mainClass+"] >*[class="+this.classTab+"]");  extjs is junk

  /* Loop through an array of all the child nodes within our tabber element. */
  for(i=0; i < childNodes.length; i++) {
	   /* Find the nodes where class="tabbertab" */
     if(childNodes[i].className &&
         childNodes[i].className.match(this.REclassTab)) {
		  /* Create a new object to save info about this tab */
		  t = new Object();

		  /* Save a pointer to the div for this tab */
		  t.div = childNodes[i];

		  /* Add the new object to the array of tabs */
		  this.tabs[this.tabs.length] = t;

		  /* If the class name contains classTabDefault,
			 then select this tab by default.
		  */
		  if (childNodes[i].className.match(this.REclassTabDefault)) {

				defaultTab = this.tabs.length-1;
		  }
	  }
  }

  /* Create a new UL list to hold the tab headings */
  TAB_ul = document.createElement("ul");
  TAB_ul.className = this.classNav;

  /* Loop through each tab we found */
  for (i=0; i < this.tabs.length; i++) {

    t = this.tabs[i];

    /* Get the label to use for this tab:
       From the title attribute on the DIV,
       Or from one of the this.titleElements[] elements,
       Or use an automatically generated number.
     */
    t.headingText = t.div.title;

    /* Remove the title attribute to prevent a tooltip from appearing */
	t.div.title = '';

	/* Create a list element for the tab */
    TAB_li = document.createElement("li");
    TAB_li.className = this.classNavInactive;

    /* Save a reference to this list item so we can later change it to
       the "active" class */
    t.li = TAB_li;

    /* Create a link to activate the tab */
    TAB_a = document.createElement("a");
    TAB_a.appendChild(document.createTextNode(t.headingText));
    TAB_a.href = "javascript:void(null);";
    TAB_a.title = t.headingText;


	if (typeof this.eventHandlers[i] == 'function') {
		Ext.EventManager.addListener(TAB_a,'click',this.eventHandlers[i]);

	}

	Ext.EventManager.addListener(TAB_a,'click',this.navClick);

    /* Add some properties to the link so we can identify which tab
       was clicked. Later the navClick method will need this.
    */
    TAB_a.tabber = this;
    TAB_a.tabIndex = i + this.startindex;
    //TAB_a.tabIndex = i;

    /* Add the link to the list element */
    TAB_li.appendChild(TAB_a);

    /* Add the list element to the list */
    TAB_ul.appendChild(TAB_li);
  }

  /* Add the UL list to the beginning of the tabber div */
  e.insertBefore(TAB_ul, e.firstChild);

  /* Make the tabber div "live" so different CSS can be applied */
  e.className = e.className.replace(this.REclassMain, this.newMainTabClass);

  /* Activate the default tab, and do not call the onclick handler */
  this.tabShow(defaultTab);

  /* If the user specified an onLoad function, call it now. */
  if (typeof this.onLoad == 'function') {
    this.onLoad({tabber:this});
  }

  return this;
};


tabObj.prototype.navClick = function(event)
{
  var
  a, /* element that triggered the onclick event */
  self,  /* the tabber object */
  onClickArgs;

  a = this;
  if (!a.tabber) { return false; }

  self = a.tabber;
  /* there is no need for this ...so removing it(inline javascript method call is not advisable)
  if (typeof self.onClick == 'function') {

    onClickArgs = {'tabber':self, 'index':tabberIndex, 'event':event};

	if (!event) { onClickArgs.event = window.event; }

    rVal = self.onClick(onClickArgs);
    if (rVal === false) { return false; }
  }*/

  self.tabShow(a.tabIndex-this.tabber.startindex);

  return false;
};


tabObj.prototype.tabHideAll = function()
{
  var i; /* counter */

  /* Hide all tabs and make all navigation links inactive */
  for (i = 0; i < this.tabs.length; i++) {
    this.tabHide(i);
  }
};


tabObj.prototype.tabHide = function(tabIndex)
{
  var div;

  if (!this.tabs[tabIndex]) { return false; }

  /* Hide a single tab and make its navigation link inactive */
  div = this.tabs[tabIndex].div;

  /* Hide the tab contents by adding classTabHide to the div */
  if (!div.className.match(this.REclassTabHide)) {
    div.className += ' ' + this.classTabHide;
  }
  this.navClearActive(tabIndex);

  return this;
};


tabObj.prototype.tabShow = function(tabIndex)
{
  /* Show the tabIndex tab and hide all the other tabs */

  var div;

  if (!this.tabs[tabIndex]) { return false; }

  /* Hide all the tabs first */
  this.tabHideAll();

  /* Get the div that holds this tab */
  div = this.tabs[tabIndex].div;

  /* Remove classTabHide from the div */
  div.className = div.className.replace(this.REclassTabHide, '');

  /* Mark this tab navigation link as "active" */
  this.navSetActive(tabIndex);

  this.fireEvent("showTab");

  return this;
};

tabObj.prototype.navSetActive = function(tabIndex)
{
  /* Set classNavActive for the navigation list item */
  this.tabs[tabIndex].li.className = this.classNavActive;

  return this;
};


tabObj.prototype.navClearActive = function(tabIndex)
{
  /* Remove classNavActive from the navigation list item */
  this.tabs[tabIndex].li.className = this.classNavInactive;

  return this;
};


/*==================================================*/


function tabify(mainClassName, startindex, customClasses, defaultTab, eventHandlers)
{
    var divs,tabberArgs ; /* Array of all divs on the page */

    tabberArgs = { };
	/* Class of the main tabber div */
	tabberArgs.classMainTab = mainClassName;
	if(startindex)
	{
		tabberArgs.startindex = startindex;
	}

	if(customClasses){
		/* Rename classMain to classMainLive after tabifying (so a different style can be applied)*/
		if(customClasses[0] != null && customClasses[0] != ""){
		   tabberArgs.newMainTabClass = customClasses[0];
		}
		/* Class of each DIV that contains a tab */
		if(customClasses[1] != null && customClasses[1] != ""){
		   tabberArgs.classTab = customClasses[1];
		}

		/* Class for the navigation UL */
		if(customClasses[2] != null && customClasses[2] != ""){
		   tabberArgs.classNav = customClasses[2];
		}

		/* When a tab is to be hidden, we set the class of the div to classTabHide. */
		if(customClasses[3] != null && customClasses[3] != ""){
			tabberArgs.classTabHide = customClasses[3];
		}

		/* Class to set the navigation LI when the tab is active, so a different style can be used on the active tab. */
		if(customClasses[4] != null && customClasses[4] != ""){
			tabberArgs.classNavActive = customClasses[4];
		}
	}

	if(eventHandlers){
		tabberArgs.eventHandlers = eventHandlers;
	}else{
		tabberArgs.eventHandlers = new Array();
	}

	if(defaultTab){
		tabberArgs.defaultTabClass = defaultTab;
	}

	divs = Ext.DomQuery.selectNode("."+mainClassName);

	var obj;
	if(divs){
		tabberArgs.div = divs;
		tabberArgs.mainClass = mainClassName;
		obj = new tabObj(tabberArgs);
	}


   return obj;
}


/*==================================================*/


function tabifyOnLoad(mainClassName, startindex, customClasses, defaultTab, eventHandlers)
{
  /* This function adds tabify to the window.onload event,
     so it will run after the document has finished loading.
  */

  var oldOnLoad;

  oldOnLoad = window.onload;
  if (typeof window.onload != 'function') {
    window.onload = function() {
      tabify(mainClassName, startindex, customClasses, defaultTab, eventHandlers);
    };
  } else {
    window.onload = function() {
      oldOnLoad();
      tabify(mainClassName, startindex, customClasses, defaultTab, eventHandlers);
    };
  }
}


/*==================================================*/
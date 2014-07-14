/*
 * QuickView is a component providing the following behaviors
 * 1. Hover over a target area, a small clickable tooltip is displayed.
 * 2. Click on the tooltip a window is launched to display designated content.
 * 3. Unless the user move their mouse over the launched window, the window is
 *    closed automatically within certain configurable time.
 * This component is used to avoid the hover over event invoking the window directly 
 * which is heavy weight and can be annoying sometimes.
 *
 * Parameter:
 *      anchor: Mixed - id/element/Elemet
 *      	this is the target area to trap the hover event. It is also used
 *              as anchor to the tool tip and window display
 *      link:   String/url configuration
 *      	link is the url string or object to access content for the quick view window;
 *      conf:   configuration object, if not set, the following default values
 *              are used
	{	
        title: 'QuickView',
        showTimeout: 1000,
        hideTimeout: 3000,
        quickViewLaunchConf:
        {
            border: false,		// not configurable
            header:false,		// not configurable
            hideBorders:true,	// not configurable
            closable:false,		// not configurable
            autoWidth:true,		// not configurable
            autoHeight:true,	// not configurable
            floating: true,		// not configurable
            shadow:false,		// not configurable
            shim:true,			// not configurable
            renderTo: null,		// not configurable
            html: null			// not configurable
        },
        quickViewWindowConf:
        {
    		initTrigger: 'i',	// not configurable
            alignToAnchor: false// not configurable

            // See QuickViewWindow for other configurable options
        }
    }
 */
QuickView.defaultConfiguration = function()
{
    var toReturn = {
        title: 'QuickView',
        showTimeout: 1000,
        hideTimeout: 3000,
        quickViewLaunchConf:
        {
            border: false,		// not configurable
            header:false,		// not configurable
            hideBorders:true,	// not configurable
            closable:false,		// not configurable
            autoWidth:true,		// not configurable
            autoHeight:true,	// not configurable
            floating: true,		// not configurable
            shadow:false,		// not configurable
            shim:true,			// not configurable
            renderTo: null,		// not configurable
            html: null			// not configurable
        },
        quickViewWindowConf:
        {
    		initTrigger: 'i',	// not configurable
            alignToAnchor: false// not configurable

            // See QuickViewWindow for other configurable options
        }
    }
    return toReturn;
}

QuickView.getConfiguration = function(conf)
{
	var toReturn = QuickView.defaultConfiguration();
	if(conf)
	{
        if(conf.title !== undefined)
        {
        	toReturn.title = conf.title;
        }
        if(conf.showTimeout !== undefined)
        {
        	toReturn.showTimeout = conf.showTimeout;
        }
        if(conf.hideTimeout !== undefined)
        {
        	toReturn.hideTimeout = conf.hideTimeout;
        }
        if(conf.quickViewWindowConf !== undefined)
        {
        	var def = QuickView.defaultConfiguration();
        	toReturn.quickViewWindowConf = conf.quickViewWindowConf;
            if(toReturn.quickViewWindowConf.alignToAnchor === undefined)
            {
            	toReturn.quickViewWindowConf.alignToAnchor = def.quickViewWindowConf.alignToAnchor;
            }
            if(toReturn.quickViewWindowConf.initTrigger === undefined)
            {
            	toReturn.quickViewWindowConf.initTrigger = def.quickViewWindowConf.initTrigger;
            }
        }
	}

	return toReturn;
}

function QuickView(anchor, link, conf)
{
    var tm1 = 0;
    var launch = null;
    var view = null;
    var me = this;
    this.myConf = QuickView.getConfiguration(conf);

    var qid = QuickView.currentIndex; 
    var te = Ext.get(anchor);

    this.createQuickView = function()
    {
        if(launch !== null)
        {
            launch.destroy();
            lanuch = null;
        }
        view = new QuickViewWindow(anchor, link, this.myConf.quickViewWindowConf);
    };

    this.createLaunch = function(a, l)
    {
        if(launch === null)
        {
        	this.myConf.quickViewLaunchConf.renderTo = document.body;
        	this.myConf.quickViewLaunchConf.html = '<div class="quickViewLaunch"><a href="javascript:eval(\'QuickView.get(' + qid + ').createQuickView();\');">' + this.myConf.title + '</a></div>';
            launch = new Ext.Panel(this.myConf.quickViewLaunchConf);
    
            launch.show();
            var pos = calculateCenter(te, launch);
            launch.setPagePosition(pos.left, pos.top);
            launch.on('destroy', function(){launch=null;});
            launch.destroy.defer(this.myConf.hideTimeout, launch);
        }
    };
    
    this.getQuickViewId = function()
    {
        return qid;
    };

    if((t = Ext.get(anchor)) !== undefined)
    {
        t.on('mouseover', showLaunch);
        t.on('mouseout', function(){clearTimeout(tm1);});
    }

    QuickView.register(this);

    function showLaunch()
    {
        if(view === null || view.getWindowHandle() === null)
        {
        	tm1 = me.createLaunch.defer(me.myConf.showTimeout, me, [anchor, link]);
        }
    }

    function calculateCenter(anchor, display)
    {
    	var ac = Ext.get(anchor);
    	var position = 
    	{
    		left: ac.getX(),
    		top: ac.getY()
    	};
    	var width = 0;
    	var height = 0;
    	if(ac.getWidth())
    	{
    		width += ac.getWidth();
    	}
    	if(display.width)
    	{
    		width -= display.width;
    	}
    	if(ac.getHeight())
    	{
    		height += ac.getHeight();
    	}
    	if(display.height)
    	{
    		height -= display.height;
    	}
    	position.left += width/2;
    	position.top += height/2;
    	return position;
    }
};

QuickView.registry=new Array();
QuickView.registrySize = 100;
QuickView.currentIndex = 0;
QuickView.register = function(obj)
{
	QuickView.registry[QuickView.currentIndex] = obj;
	QuickView.currentIndex++;
	QuickView.currentIndex %= QuickView.registrySize;
};
QuickView.get = function(idx)
{
	return QuickView.registry[idx];
};

/*
 * QuickViewWindow is a component wrap around Ext.Window with 
 * default settings. It will display the content specified
 * by the 'link' paarmeter and anchor the window to the 'anchor'
 * paarmeter. It can be configured with a timeout period
 * to automatically close the window with animation.
 *
 *      anchor: Mixed - id/element/Elemet
 *      	this is the target area to trap the hover event or align the window to.
 *      link:   String/url configuration
 *      	link is the url string or object to access content for the quick view window;
 *      conf:   configuration object, if not set, the following default values
 *              are used

		{
    		initTrigger: 'ch', 		//c:click; h: hover over anchor; i: instantiate;
    		showTimeout: 1000,
            closeTimeout: 5000,
            alignToAnchor: true,
            animateFromAnchor: true,
            offsetTop: 25,
            offsetLeft: 0,
            windowConf:
            {
        		title: 'Quick View',
                width: 300,
                height: 450,
                modal: false,
                autoScroll: true,
                animCollapse: true,
                animateTarget: null,
                // autoLoad.url will be overwritten
                autoLoad: {
                    url: null
                }
            }
        };

 */
QuickViewWindow.defaultConfiguration = function()
{
    var toReturn = {
    		initTrigger: 'ch', 		//c:click; h: hover over anchor; i: instantiate;
    		showTimeout: 1000,
            closeTimeout: 5000,
            alignToAnchor: true,
            animateFromAnchor: true,
            offsetTop: 25,
            offsetLeft: 0,
            windowConf:
            {
        		title: 'Quick View',
                width: 300,
                height: 450,
                modal: false,
                autoScroll: true,
                animCollapse: true,
                animateTarget: null,
                // autoLoad.url will be overwritten
                autoLoad: {
                    url: null
                }
            }
        };
    return toReturn;
};

QuickViewWindow.getConfiguration = function(conf)
{
	var toReturn = QuickViewWindow.defaultConfiguration();
	if(conf)
	{
        if(conf.initTrigger !== undefined)
        {
        	toReturn.initTrigger = conf.initTrigger;
        }
        if(conf.showTimeout !== undefined)
        {
        	toReturn.showTimeout = conf.showTimeout;
        }
        if(conf.closeTimeout !== undefined)
        {
        	toReturn.closeTimeout = conf.closeTimeout;
        }
        if(conf.alignToAnchor !== undefined)
        {
        	toReturn.alignToAnchor = conf.alignToAnchor;
        }
        if(conf.animateFromAnchor !== undefined)
        {
        	toReturn.animateFromAnchor = conf.animateFromAnchor;
        }
        if(conf.offsetTop !== undefined)
        {
        	toReturn.offsetTop = conf.offsetTop;
        }
        if(conf.offsetLeft !== undefined)
        {
        	toReturn.offsetLeft = conf.offsetLeft;
        }
        if(conf.windowConf !== undefined)
        {
        	var def = QuickViewWindow.defaultConfiguration();
        	toReturn.windowConf = conf.windowConf; // in case there are more configuration than default
            if(toReturn.windowConf.title === undefined)
            {
            	toReturn.windowConf.title = def.windowConf.title;
            }
            if(toReturn.windowConf.width === undefined)
            {
            	toReturn.windowConf.width = def.windowConf.width;
            }
            if(toReturn.windowConf.height === undefined)
            {
            	toReturn.windowConf.height = def.windowConf.height;
            }
            if(toReturn.windowConf.modal === undefined)
            {
            	toReturn.windowConf.modal = def.windowConf.modal;
            }
            if(toReturn.windowConf.autoScroll === undefined)
            {
            	toReturn.windowConf.autoScroll = def.windowConf.autoScroll;
            }
            if(toReturn.windowConf.animCollapse === undefined)
            {
            	toReturn.windowConf.animCollapse = def.windowConf.animCollapse;
            }
            if(toReturn.windowConf.animateTarget === undefined)
            {
            	toReturn.windowConf.animateTarget = def.windowConf.animateTarget;
            }
            if(toReturn.windowConf.autoLoad === undefined)
            {
            	toReturn.windowConf.autoLoad = def.windowConf.autoLoad;
            }
        }
	}

	return toReturn;
}

function QuickViewWindow(anchor, link, conf)
{
    var win = null;
    var tm1 = 0;
    var tm2 = 0;
    var me = this;
    anchor = Ext.get(anchor);
    
    // Prepare input configuration
    this.myConf = QuickViewWindow.getConfiguration(conf);
    if(this.myConf.animateFromAnchor)
    {
    	this.myConf.windowConf.animateTarget = anchor;
    }
    if(typeof(link) == "string")
    {	
    	this.myConf.windowConf.autoLoad.url = link;
    }
    else
    {
    	this.myConf.windowConf.autoLoad = link; // url configuration object
    }
	
	this.closeWindow = function()
    {
		if(win !== null)	
		{
			win.hide(anchor);
		}
    	if(win !== null)
    	{
    		win.close();
    	}
    	win = null;
    };
    
    this.refresh = function()
    {
    	if(win !== null)
    	{
    		clearTimeout(tm2);
    		win.load(me.myConf.windowConf.autoLoad);
    	}
    }
    
    this.createWindow = function()
    {
        if(win === null)
        {
            win = new Ext.Window(me.myConf.windowConf);

            if(anchor !== null && anchor !== undefined && me.myConf.alignToAnchor)
            {
                var pos = calulateDisplayPosition(anchor);
                win.setPagePosition(pos.left, pos.top);
            }
            win.on('close', function(){ win = null;});
            win.show();
            tm2 = me.closeWindow.defer(me.myConf.closeTimeout, me);
            win.getEl().on('mouseover', function(){ clearTimeout(tm2); });
        }
    };

    this.getWindowHandle = function()
    {
        return win;
    };

    var t = Ext.get(anchor);
    if(this.myConf.initTrigger.indexOf('c') >= 0)
    {
        if(t !== undefined)
        {
            t.on('click', this.createWindow);
        }
    }
    if(this.myConf.initTrigger.indexOf('h') >= 0)
    {
        if(t !== undefined)
        {
            t.on('mouseover', showWindow);
            t.on('mouseout', function(){clearTimeout(tm1);});
        }
    }
    if(this.myConf.initTrigger.indexOf('i') >= 0)
    {
    	this.createWindow();
    }
    
    function showWindow()
    {
    	tm1 = me.createWindow.defer(me.myConf.showTimeout, me);
    }

    function calulateDisplayPosition(achor)
    {
    	var ac = Ext.get(anchor);
    	var position = 
    	{
    		left: ac.getX(),
    		top: ac.getY()
    	};
    	position.left += me.myConf.offsetLeft;
    	position.top += me.myConf.offsetTop;
    	return position;
    }
}


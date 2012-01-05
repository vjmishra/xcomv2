// Adding order list jquery.pngFix.pack.js

/**
 * --------------------------------------------------------------------
 * jQuery-Plugin "pngFix"
 * Version: 1.1, 11.09.2007
 * by Andreas Eberhard, andreas.eberhard@gmail.com
 *                      http://jquery.andreaseberhard.de/
 *
 * Copyright (c) 2007 Andreas Eberhard
 * Licensed under GPL (http://www.opensource.org/licenses/gpl-license.php)
 */
eval(function(p,a,c,k,e,r){e=function(c){return(c<62?'':e(parseInt(c/62)))+((c=c%62)>35?String.fromCharCode(c+29):c.toString(36))};if('0'.replace(0,e)==0){while(c--)r[e(c)]=k[c];k=[function(e){return r[e]||e}];e=function(){return'([237-9n-zA-Z]|1\\w)'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('(s(m){3.fn.pngFix=s(c){c=3.extend({P:\'blank.gif\'},c);8 e=(o.Q=="t R S"&&T(o.u)==4&&o.u.A("U 5.5")!=-1);8 f=(o.Q=="t R S"&&T(o.u)==4&&o.u.A("U 6.0")!=-1);p(3.browser.msie&&(e||f)){3(2).B("img[n$=.C]").D(s(){3(2).7(\'q\',3(2).q());3(2).7(\'r\',3(2).r());8 a=\'\';8 b=\'\';8 g=(3(2).7(\'E\'))?\'E="\'+3(2).7(\'E\')+\'" \':\'\';8 h=(3(2).7(\'F\'))?\'F="\'+3(2).7(\'F\')+\'" \':\'\';8 i=(3(2).7(\'G\'))?\'G="\'+3(2).7(\'G\')+\'" \':\'\';8 j=(3(2).7(\'H\'))?\'H="\'+3(2).7(\'H\')+\'" \':\'\';8 k=(3(2).7(\'V\'))?\'float:\'+3(2).7(\'V\')+\';\':\'\';8 d=(3(2).parent().7(\'href\'))?\'cursor:hand;\':\'\';p(2.9.v){a+=\'v:\'+2.9.v+\';\';2.9.v=\'\'}p(2.9.w){a+=\'w:\'+2.9.w+\';\';2.9.w=\'\'}p(2.9.x){a+=\'x:\'+2.9.x+\';\';2.9.x=\'\'}8 l=(2.9.cssText);b+=\'<y \'+g+h+i+j;b+=\'9="W:X;white-space:pre-line;Y:Z-10;I:transparent;\'+k+d;b+=\'q:\'+3(2).q()+\'z;r:\'+3(2).r()+\'z;\';b+=\'J:K:L.t.M(n=\\\'\'+3(2).7(\'n\')+\'\\\', N=\\\'O\\\');\';b+=l+\'"></y>\';p(a!=\'\'){b=\'<y 9="W:X;Y:Z-10;\'+a+d+\'q:\'+3(2).q()+\'z;r:\'+3(2).r()+\'z;">\'+b+\'</y>\'}3(2).hide();3(2).after(b)});3(2).B("*").D(s(){8 a=3(2).11(\'I-12\');p(a.A(".C")!=-1){8 b=a.13(\'url("\')[1].13(\'")\')[0];3(2).11(\'I-12\',\'none\');3(2).14(0).15.J="K:L.t.M(n=\'"+b+"\',N=\'O\')"}});3(2).B("input[n$=.C]").D(s(){8 a=3(2).7(\'n\');3(2).14(0).15.J=\'K:L.t.M(n=\\\'\'+a+\'\\\', N=\\\'O\\\');\';3(2).7(\'n\',c.P)})}return 3}})(3);',[],68,'||this|jQuery||||attr|var|style||||||||||||||src|navigator|if|width|height|function|Microsoft|appVersion|border|padding|margin|span|px|indexOf|find|png|each|id|class|title|alt|background|filter|progid|DXImageTransform|AlphaImageLoader|sizingMethod|scale|blankgif|appName|Internet|Explorer|parseInt|MSIE|align|position|relative|display|inline|block|css|image|split|get|runtimeStyle'.split('|'),0,{}))


// End order list jquery.pngFix.pack.js


//Adding order list xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js

/*! Copyright (c) 2009 Brandon Aaron (http://brandonaaron.net)
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 * Thanks to: http://adomas.org/javascript-mouse-wheel/ for some pointers.
 * Thanks to: Mathias Bank(http://www.mathias-bank.de) for a scope bug fix.
 *
 * Version: 3.0.2
 * 
 * Requires: 1.2.2+
 */

(function(b){function d(a){var f=[].slice.call(arguments,1),e=0;a=b.event.fix(a||window.event);a.type="mousewheel";if(a.wheelDelta)e=a.wheelDelta/120;if(a.detail)e=-a.detail/3;f.unshift(a,e);return b.event.handle.apply(this,f)}var c=["DOMMouseScroll","mousewheel"];b.event.special.mousewheel={setup:function(){if(this.addEventListener)for(var a=c.length;a;)this.addEventListener(c[--a],d,false);else this.onmousewheel=d},teardown:function(){if(this.removeEventListener)for(var a=c.length;a;)this.removeEventListener(c[--a],
d,false);else this.onmousewheel=null}};b.fn.extend({mousewheel:function(a){return a?this.bind("mousewheel",a):this.trigger("mousewheel")},unmousewheel:function(a){return this.unbind("mousewheel",a)}})})(jQuery);


//End order list xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js


//Adding order list /xpedx/js/jQuery.js

/* prevent execution of jQuery if included more then once */
if(typeof window.jQuery == "undefined") {
/*
 * jQuery 1.0.4 - New Wave Javascript
 *
 * Copyright (c) 2006 John Resig (jquery.com)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * $Date: 2010/08/26 11:16:32 $
 * $Rev: 696 $
 */

// Global undefined variable
window.undefined = window.undefined;

var jQuery = function(a,c) {

	// Shortcut for document ready
	if ( a && typeof a == "function" && jQuery.fn.ready && !a.nodeType && a[0] == undefined ) // Safari reports typeof on DOM NodeLists as a function
		return jQuery(document).ready(a);

	// Make sure that a selection was provided
	a = a || document;

	// Watch for when a jQuery object is passed as the selector
	if ( a.jquery )
		return jQuery( jQuery.merge( a, [] ) );

	// Watch for when a jQuery object is passed at the context
	if ( c && c.jquery )
		return jQuery( c ).find(a);

	// If the context is global, return a new object
	if ( window == this )
		return new jQuery(a,c);

	// Handle HTML strings
	if ( typeof a  == "string" ) {
		var m = /^[^<]*(<.+>)[^>]*$/.exec(a);
		if ( m ) a = jQuery.clean( [ m[1] ] );
	}

	// Watch for when an array is passed in
	this.set( a.constructor == Array || a.length && a != window && !a.nodeType && a[0] != undefined && a[0].nodeType ?
		// Assume that it is an array of DOM Elements
		jQuery.merge( a, [] ) :

		// Find the matching elements and save them for later
		jQuery.find( a, c ) );

	// See if an extra function was provided
	var fn = arguments[ arguments.length - 1 ];

	// If so, execute it in context
	if ( fn && typeof fn == "function" )
		this.each(fn);

	return this;
};

// Map over the $ in case of overwrite
if ( typeof $ != "undefined" )
	jQuery._$ = $;
	
// Map the jQuery namespace to the '$' one
var $ = jQuery;



jQuery.fn = jQuery.prototype = {

	jquery: "1.0.4",


	size: function() {
		return this.length;
	},


	get: function( num ) {
		return num == undefined ?

			// Return a 'clean' array
			jQuery.merge( this, [] ) :

			// Return just the object
			this[num];
	},

	set: function( array ) {
		// Use a tricky hack to make the jQuery object
		// look and feel like an array
		this.length = 0;
		[].push.apply( this, array );
		return this;
	},

	each: function( fn, args ) {
		return jQuery.each( this, fn, args );
	},

	index: function( obj ) {
		var pos = -1;
		this.each(function(i){
			if ( this == obj ) pos = i;
		});
		return pos;
	},


	attr: function( key, value, type ) {
		// Check to see if we're setting style values
		return key.constructor != String || value != undefined ?
			this.each(function(){
				// See if we're setting a hash of styles
				if ( value == undefined )
					// Set all the styles
					for ( var prop in key )
						jQuery.attr(
							type ? this.style : this,
							prop, key[prop]
						);

				// See if we're setting a single key/value style
				else
					jQuery.attr(
						type ? this.style : this,
						key, value
					);
			}) :

			// Look for the case where we're accessing a style value
			jQuery[ type || "attr" ]( this[0], key );
	},


	css: function( key, value ) {
		return this.attr( key, value, "curCSS" );
	},

	text: function(e) {
		e = e || this;
		var t = "";
		for ( var j = 0; j < e.length; j++ ) {
			var r = e[j].childNodes;
			for ( var i = 0; i < r.length; i++ )
				if ( r[i].nodeType != 8 )
					t += r[i].nodeType != 1 ?
						r[i].nodeValue : jQuery.fn.text([ r[i] ]);
		}
		return t;
	},


	wrap: function() {
		// The elements to wrap the target around
		var a = jQuery.clean(arguments);

		// Wrap each of the matched elements individually
		return this.each(function(){
			// Clone the structure that we're using to wrap
			var b = a[0].cloneNode(true);

			// Insert it before the element to be wrapped
			this.parentNode.insertBefore( b, this );

			// Find the deepest point in the wrap structure
			while ( b.firstChild )
				b = b.firstChild;

			// Move the matched element to within the wrap structure
			b.appendChild( this );
		});
	},


	append: function() {
		return this.domManip(arguments, true, 1, function(a){
			this.appendChild( a );
		});
	},


	prepend: function() {
		return this.domManip(arguments, true, -1, function(a){
			this.insertBefore( a, this.firstChild );
		});
	},


	before: function() {
		return this.domManip(arguments, false, 1, function(a){
			this.parentNode.insertBefore( a, this );
		});
	},


	after: function() {
		return this.domManip(arguments, false, -1, function(a){
			this.parentNode.insertBefore( a, this.nextSibling );
		});
	},

	end: function() {
		if( !(this.stack && this.stack.length) )
			return this;
		return this.set( this.stack.pop() );
	},

	find: function(t) {
		return this.pushStack( jQuery.map( this, function(a){
			return jQuery.find(t,a);
		}), arguments );
	},

	clone: function(deep) {
		return this.pushStack( jQuery.map( this, function(a){
			return a.cloneNode( deep != undefined ? deep : true );
		}), arguments );
	},


	filter: function(t) {
		return this.pushStack(
			t.constructor == Array &&
			jQuery.map(this,function(a){
				for ( var i = 0; i < t.length; i++ )
					if ( jQuery.filter(t[i],[a]).r.length )
						return a;
				return null;
			}) ||

			t.constructor == Boolean &&
			( t ? this.get() : [] ) ||

			typeof t == "function" &&
			jQuery.grep( this, t ) ||

			jQuery.filter(t,this).r, arguments );
	},


	not: function(t) {
		return this.pushStack( typeof t == "string" ?
			jQuery.filter(t,this,false).r :
			jQuery.grep(this,function(a){ return a != t; }), arguments );
	},


	add: function(t) {
		return this.pushStack( jQuery.merge( this, typeof t == "string" ?
			jQuery.find(t) : t.constructor == Array ? t : [t] ), arguments );
	},

	is: function(expr) {
		return expr ? jQuery.filter(expr,this).r.length > 0 : false;
	},

	domManip: function(args, table, dir, fn){
		var clone = this.size() > 1;
		var a = jQuery.clean(args);

		return this.each(function(){
			var obj = this;

			if ( table && this.nodeName.toUpperCase() == "TABLE" && a[0].nodeName.toUpperCase() != "THEAD" ) {
				var tbody = this.getElementsByTagName("tbody");

				if ( !tbody.length ) {
					obj = document.createElement("tbody");
					this.appendChild( obj );
				} else
					obj = tbody[0];
			}

			for ( var i = ( dir < 0 ? a.length - 1 : 0 );
				i != ( dir < 0 ? dir : a.length ); i += dir ) {
					fn.apply( obj, [ clone ? a[i].cloneNode(true) : a[i] ] );
			}
		});
	},

	pushStack: function(a,args) {
		var fn = args && args[args.length-1];
		var fn2 = args && args[args.length-2];
		
		if ( fn && fn.constructor != Function ) fn = null;
		if ( fn2 && fn2.constructor != Function ) fn2 = null;

		if ( !fn ) {
			if ( !this.stack ) this.stack = [];
			this.stack.push( this.get() );
			this.set( a );
		} else {
			var old = this.get();
			this.set( a );

			if ( fn2 && a.length || !fn2 )
				this.each( fn2 || fn ).set( old );
			else
				this.set( old ).each( fn );
		}

		return this;
	}
};


jQuery.extend = jQuery.fn.extend = function() {
	// copy reference to target object
	var target = arguments[0],
		a = 1;

	// extend jQuery itself if only one argument is passed
	if ( arguments.length == 1 ) {
		target = this;
		a = 0;
	}
	var prop;
	while (prop = arguments[a++])
		// Extend the base object
		for ( var i in prop ) target[i] = prop[i];

	// Return the modified object
	return target;
};

jQuery.extend({

	init: function(){
		jQuery.initDone = true;

		jQuery.each( jQuery.macros.axis, function(i,n){
			jQuery.fn[ i ] = function(a) {
				var ret = jQuery.map(this,n);
				if ( a && typeof a == "string" )
					ret = jQuery.filter(a,ret).r;
				return this.pushStack( ret, arguments );
			};
		});

		jQuery.each( jQuery.macros.to, function(i,n){
			jQuery.fn[ i ] = function(){
				var a = arguments;
				return this.each(function(){
					for ( var j = 0; j < a.length; j++ )
						jQuery(a[j])[n]( this );
				});
			};
		});

		jQuery.each( jQuery.macros.each, function(i,n){
			jQuery.fn[ i ] = function() {
				return this.each( n, arguments );
			};
		});

		jQuery.each( jQuery.macros.filter, function(i,n){
			jQuery.fn[ n ] = function(num,fn) {
				return this.filter( ":" + n + "(" + num + ")", fn );
			};
		});

		jQuery.each( jQuery.macros.attr, function(i,n){
			n = n || i;
			jQuery.fn[ i ] = function(h) {
				return h == undefined ?
					this.length ? this[0][n] : null :
					this.attr( n, h );
			};
		});

		jQuery.each( jQuery.macros.css, function(i,n){
			jQuery.fn[ n ] = function(h) {
				return h == undefined ?
					( this.length ? jQuery.css( this[0], n ) : null ) :
					this.css( n, h );
			};
		});

	},

	// args is for internal usage only
	each: function( obj, fn, args ) {
		if ( obj.length == undefined )
			for ( var i in obj )
				fn.apply( obj[i], args || [i, obj[i]] );
		else
			for ( var i = 0; i < obj.length; i++ )
				if ( fn.apply( obj[i], args || [i, obj[i]] ) === false ) break;
		return obj;
	},

	className: {
		add: function(o,c){
			if (jQuery.className.has(o,c)) return;
			o.className += ( o.className ? " " : "" ) + c;
		},
		remove: function(o,c){
			if( !c ) {
				o.className = "";
			} else {
				var classes = o.className.split(" ");
				for(var i=0; i<classes.length; i++) {
					if(classes[i] == c) {
						classes.splice(i, 1);
						break;
					}
				}
				o.className = classes.join(' ');
			}
		},
		has: function(e,a) {
			if ( e.className != undefined )
				e = e.className;
			return new RegExp("(^|\\s)" + a + "(\\s|$)").test(e);
		}
	},

	swap: function(e,o,f) {
		for ( var i in o ) {
			e.style["old"+i] = e.style[i];
			e.style[i] = o[i];
		}
		f.apply( e, [] );
		for ( var i in o )
			e.style[i] = e.style["old"+i];
	},

	css: function(e,p) {
		if ( p == "height" || p == "width" ) {
			var old = {}, oHeight, oWidth, d = ["Top","Bottom","Right","Left"];

			for ( var i=0; i<d.length; i++ ) {
				old["padding" + d[i]] = 0;
				old["border" + d[i] + "Width"] = 0;
			}

			jQuery.swap( e, old, function() {
				if (jQuery.css(e,"display") != "none") {
					oHeight = e.offsetHeight;
					oWidth = e.offsetWidth;
				} else {
					e = jQuery(e.cloneNode(true))
						.find(":radio").removeAttr("checked").end()
						.css({
							visibility: "hidden", position: "absolute", display: "block", right: "0", left: "0"
						}).appendTo(e.parentNode)[0];

					var parPos = jQuery.css(e.parentNode,"position");
					if ( parPos == "" || parPos == "static" )
						e.parentNode.style.position = "relative";

					oHeight = e.clientHeight;
					oWidth = e.clientWidth;

					if ( parPos == "" || parPos == "static" )
						e.parentNode.style.position = "static";

					e.parentNode.removeChild(e);
				}
			});

			return p == "height" ? oHeight : oWidth;
		}

		return jQuery.curCSS( e, p );
	},

	curCSS: function(elem, prop, force) {
		var ret;
		
		if (prop == 'opacity' && jQuery.browser.msie)
			return jQuery.attr(elem.style, 'opacity');
			
		if (prop == "float" || prop == "cssFloat")
		    prop = jQuery.browser.msie ? "styleFloat" : "cssFloat";

		if (!force && elem.style[prop]) {

			ret = elem.style[prop];

		} else if (document.defaultView && document.defaultView.getComputedStyle) {

			if (prop == "cssFloat" || prop == "styleFloat")
				prop = "float";

			prop = prop.replace(/([A-Z])/g,"-$1").toLowerCase();
			var cur = document.defaultView.getComputedStyle(elem, null);

			if ( cur )
				ret = cur.getPropertyValue(prop);
			else if ( prop == 'display' )
				ret = 'none';
			else
				jQuery.swap(elem, { display: 'block' }, function() {
				    var c = document.defaultView.getComputedStyle(this, '');
				    ret = c && c.getPropertyValue(prop) || '';
				});

		} else if (elem.currentStyle) {

			var newProp = prop.replace(/\-(\w)/g,function(m,c){return c.toUpperCase();});
			ret = elem.currentStyle[prop] || elem.currentStyle[newProp];
			
		}

		return ret;
	},
	
	clean: function(a) {
		var r = [];
		for ( var i = 0; i < a.length; i++ ) {
			var arg = a[i];
			if ( typeof arg == "string" ) { // Convert html string into DOM nodes
				// Trim whitespace, otherwise indexOf won't work as expected
				var s = jQuery.trim(arg), div = document.createElement("div"), wrap = [0,"",""];

				if ( !s.indexOf("<opt") ) // option or optgroup
					wrap = [1, "<select>", "</select>"];
				else if ( !s.indexOf("<thead") || !s.indexOf("<tbody") )
					wrap = [1, "<table>", "</table>"];
				else if ( !s.indexOf("<tr") )
					wrap = [2, "<table>", "</table>"];	// tbody auto-inserted
				else if ( !s.indexOf("<td") || !s.indexOf("<th") )
					wrap = [3, "<table><tbody><tr>", "</tr></tbody></table>"];

				// Go to html and back, then peel off extra wrappers
				div.innerHTML = wrap[1] + s + wrap[2];
				while ( wrap[0]-- ) div = div.firstChild;
				arg = div.childNodes;
			} 
			
			
			if ( arg.length != undefined && ( (jQuery.browser.safari && typeof arg == 'function') || !arg.nodeType ) ) // Safari reports typeof on a DOM NodeList to be a function
				for ( var n = 0; n < arg.length; n++ ) // Handles Array, jQuery, DOM NodeList collections
					r.push(arg[n]);
			else
				r.push(	arg.nodeType ? arg : document.createTextNode(arg.toString()) );
		}

		return r;
	},

	expr: {
		"": "m[2]== '*'||a.nodeName.toUpperCase()==m[2].toUpperCase()",
		"#": "a.getAttribute('id')&&a.getAttribute('id')==m[2]",
		":": {
			// Position Checks
			lt: "i<m[3]-0",
			gt: "i>m[3]-0",
			nth: "m[3]-0==i",
			eq: "m[3]-0==i",
			first: "i==0",
			last: "i==r.length-1",
			even: "i%2==0",
			odd: "i%2",

			// Child Checks
			"nth-child": "jQuery.sibling(a,m[3]).cur",
			"first-child": "jQuery.sibling(a,0).cur",
			"last-child": "jQuery.sibling(a,0).last",
			"only-child": "jQuery.sibling(a).length==1",

			// Parent Checks
			parent: "a.childNodes.length",
			empty: "!a.childNodes.length",

			// Text Check
			contains: "jQuery.fn.text.apply([a]).indexOf(m[3])>=0",

			// Visibility
			visible: "a.type!='hidden'&&jQuery.css(a,'display')!='none'&&jQuery.css(a,'visibility')!='hidden'",
			hidden: "a.type=='hidden'||jQuery.css(a,'display')=='none'||jQuery.css(a,'visibility')=='hidden'",

			// Form attributes
			enabled: "!a.disabled",
			disabled: "a.disabled",
			checked: "a.checked",
			selected: "a.selected || jQuery.attr(a, 'selected')",

			// Form elements
			text: "a.type=='text'",
			radio: "a.type=='radio'",
			checkbox: "a.type=='checkbox'",
			file: "a.type=='file'",
			password: "a.type=='password'",
			submit: "a.type=='submit'",
			image: "a.type=='image'",
			reset: "a.type=='reset'",
			button: "a.type=='button'",
			input: "/input|select|textarea|button/i.test(a.nodeName)"
		},
		".": "jQuery.className.has(a,m[2])",
		"@": {
			"=": "z==m[4]",
			"!=": "z!=m[4]",
			"^=": "z && !z.indexOf(m[4])",
			"$=": "z && z.substr(z.length - m[4].length,m[4].length)==m[4]",
			"*=": "z && z.indexOf(m[4])>=0",
			"": "z"
		},
		"[": "jQuery.find(m[2],a).length"
	},

	token: [
		"\\.\\.|/\\.\\.", "a.parentNode",
		">|/", "jQuery.sibling(a.firstChild)",
		"\\+", "jQuery.sibling(a).next",
		"~", function(a){
			var s = jQuery.sibling(a);
			return s.n >= 0 ? s.slice(s.n+1) : [];
		}
	],

	find: function( t, context ) {
		// Make sure that the context is a DOM Element
		if ( context && context.nodeType == undefined )
			context = null;

		// Set the correct context (if none is provided)
		context = context || document;

		if ( t.constructor != String ) return [t];

		if ( !t.indexOf("//") ) {
			context = context.documentElement;
			t = t.substr(2,t.length);
		} else if ( !t.indexOf("/") ) {
			context = context.documentElement;
			t = t.substr(1,t.length);
			// FIX Assume the root element is right :(
			if ( t.indexOf("/") >= 1 )
				t = t.substr(t.indexOf("/"),t.length);
		}

		var ret = [context];
		var done = [];
		var last = null;

		while ( t.length > 0 && last != t ) {
			var r = [];
			last = t;

			t = jQuery.trim(t).replace( /^\/\//i, "" );

			var foundToken = false;

			for ( var i = 0; i < jQuery.token.length; i += 2 ) {
				if ( foundToken ) continue;

				var re = new RegExp("^(" + jQuery.token[i] + ")");
				var m = re.exec(t);

				if ( m ) {
					r = ret = jQuery.map( ret, jQuery.token[i+1] );
					t = jQuery.trim( t.replace( re, "" ) );
					foundToken = true;
				}
			}

			if ( !foundToken ) {
				if ( !t.indexOf(",") || !t.indexOf("|") ) {
					if ( ret[0] == context ) ret.shift();
					done = jQuery.merge( done, ret );
					r = ret = [context];
					t = " " + t.substr(1,t.length);
				} else {
					var re2 = /^([#.]?)([a-z0-9\\*_-]*)/i;
					var m = re2.exec(t);

					if ( m[1] == "#" ) {
						// Ummm, should make this work in all XML docs
						var oid = document.getElementById(m[2]);
						r = ret = oid ? [oid] : [];
						t = t.replace( re2, "" );
					} else {
						if ( !m[2] || m[1] == "." ) m[2] = "*";

						for ( var i = 0; i < ret.length; i++ )
							r = jQuery.merge( r,
								m[2] == "*" ?
									jQuery.getAll(ret[i]) :
									ret[i].getElementsByTagName(m[2])
							);
					}
				}

			}

			if ( t ) {
				var val = jQuery.filter(t,r);
				ret = r = val.r;
				t = jQuery.trim(val.t);
			}
		}

		if ( ret && ret[0] == context ) ret.shift();
		done = jQuery.merge( done, ret );

		return done;
	},

	getAll: function(o,r) {
		r = r || [];
		var s = o.childNodes;
		for ( var i = 0; i < s.length; i++ )
			if ( s[i].nodeType == 1 ) {
				r.push( s[i] );
				jQuery.getAll( s[i], r );
			}
		return r;
	},

	attr: function(elem, name, value){
		var fix = {
			"for": "htmlFor",
			"class": "className",
			"float": jQuery.browser.msie ? "styleFloat" : "cssFloat",
			cssFloat: jQuery.browser.msie ? "styleFloat" : "cssFloat",
			innerHTML: "innerHTML",
			className: "className",
			value: "value",
			disabled: "disabled",
			checked: "checked",
			readonly: "readOnly"
		};
		
		// IE actually uses filters for opacity ... elem is actually elem.style
		if (name == "opacity" && jQuery.browser.msie && value != undefined) {
			// IE has trouble with opacity if it does not have layout
			// Would prefer to check element.hasLayout first but don't have access to the element here
			elem['zoom'] = 1; 
			if (value == 1) // Remove filter to avoid more IE weirdness
				return elem["filter"] = elem["filter"].replace(/alpha\([^\)]*\)/gi,"");
			else
				return elem["filter"] = elem["filter"].replace(/alpha\([^\)]*\)/gi,"") + "alpha(opacity=" + value * 100 + ")";
		} else if (name == "opacity" && jQuery.browser.msie) {
			return elem["filter"] ? parseFloat( elem["filter"].match(/alpha\(opacity=(.*)\)/)[1] )/100 : 1;
		}
		
		// Mozilla doesn't play well with opacity 1
		if (name == "opacity" && jQuery.browser.mozilla && value == 1) value = 0.9999;

		if ( fix[name] ) {
			if ( value != undefined ) elem[fix[name]] = value;
			return elem[fix[name]];
		} else if( value == undefined && jQuery.browser.msie && elem.nodeName && elem.nodeName.toUpperCase() == 'FORM' && (name == 'action' || name == 'method') ) {
			return elem.getAttributeNode(name).nodeValue;
		} else if ( elem.tagName ) { // IE elem.getAttribute passes even for style
			if ( value != undefined ) elem.setAttribute( name, value );
			return elem.getAttribute( name );
		} else {
			name = name.replace(/-([a-z])/ig,function(z,b){return b.toUpperCase();});
			if ( value != undefined ) elem[name] = value;
			return elem[name];
		}
	},

	// The regular expressions that power the parsing engine
	parse: [
		// Match: [@value='test'], [@foo]
		"\\[ *(@)S *([!*$^=]*) *('?\"?)(.*?)\\4 *\\]",

		// Match: [div], [div p]
		"(\\[)\s*(.*?)\s*\\]",

		// Match: :contains('foo')
		"(:)S\\(\"?'?([^\\)]*?)\"?'?\\)",

		// Match: :even, :last-chlid
		"([:.#]*)S"
	],

	filter: function(t,r,not) {
		// Figure out if we're doing regular, or inverse, filtering
		var g = not !== false ? jQuery.grep :
			function(a,f) {return jQuery.grep(a,f,true);};

		while ( t && /^[a-z[({<*:.#]/i.test(t) ) {

			var p = jQuery.parse;

			for ( var i = 0; i < p.length; i++ ) {
		
				// Look for, and replace, string-like sequences
				// and finally build a regexp out of it
				var re = new RegExp(
					"^" + p[i].replace("S", "([a-z*_-][a-z0-9_-]*)"), "i" );

				var m = re.exec( t );

				if ( m ) {
					// Re-organize the first match
					if ( !i )
						m = ["",m[1], m[3], m[2], m[5]];

					// Remove what we just matched
					t = t.replace( re, "" );

					break;
				}
			}

			// :not() is a special case that can be optimized by
			// keeping it out of the expression list
			if ( m[1] == ":" && m[2] == "not" )
				r = jQuery.filter(m[3],r,false).r;

			// Otherwise, find the expression to execute
			else {
				var f = jQuery.expr[m[1]];
				if ( f.constructor != String )
					f = jQuery.expr[m[1]][m[2]];

				// Build a custom macro to enclose it
				eval("f = function(a,i){" +
					( m[1] == "@" ? "z=jQuery.attr(a,m[3]);" : "" ) +
					"return " + f + "}");

				// Execute it against the current filter
				r = g( r, f );
			}
		}

		// Return an array of filtered elements (r)
		// and the modified expression string (t)
		return { r: r, t: t };
	},

	trim: function(t){
		return t.replace(/^\s+|\s+$/g, "");
	},

	parents: function( elem ){
		var matched = [];
		var cur = elem.parentNode;
		while ( cur && cur != document ) {
			matched.push( cur );
			cur = cur.parentNode;
		}
		return matched;
	},

	sibling: function(elem, pos, not) {
		var elems = [];
		
		if(elem) {
			var siblings = elem.parentNode.childNodes;
			for ( var i = 0; i < siblings.length; i++ ) {
				if ( not === true && siblings[i] == elem ) continue;
	
				if ( siblings[i].nodeType == 1 )
					elems.push( siblings[i] );
				if ( siblings[i] == elem )
					elems.n = elems.length - 1;
			}
		}

		return jQuery.extend( elems, {
			last: elems.n == elems.length - 1,
			cur: pos == "even" && elems.n % 2 == 0 || pos == "odd" && elems.n % 2 || elems[pos] == elem,
			prev: elems[elems.n - 1],
			next: elems[elems.n + 1]
		});
	},

	merge: function(first, second) {
		var result = [];

		// Move b over to the new array (this helps to avoid
		// StaticNodeList instances)
		for ( var k = 0; k < first.length; k++ )
			result[k] = first[k];

		// Now check for duplicates between a and b and only
		// add the unique items
		for ( var i = 0; i < second.length; i++ ) {
			var noCollision = true;

			// The collision-checking process
			for ( var j = 0; j < first.length; j++ )
				if ( second[i] == first[j] )
					noCollision = false;

			// If the item is unique, add it
			if ( noCollision )
				result.push( second[i] );
		}

		return result;
	},

	grep: function(elems, fn, inv) {
		// If a string is passed in for the function, make a function
		// for it (a handy shortcut)
		if ( typeof fn == "string" )
			fn = new Function("a","i","return " + fn);

		var result = [];

		// Go through the array, only saving the items
		// that pass the validator function
		for ( var i = 0; i < elems.length; i++ )
			if ( !inv && fn(elems[i],i) || inv && !fn(elems[i],i) )
				result.push( elems[i] );

		return result;
	},

	map: function(elems, fn) {
		// If a string is passed in for the function, make a function
		// for it (a handy shortcut)
		if ( typeof fn == "string" )
			fn = new Function("a","return " + fn);

		var result = [];

		// Go through the array, translating each of the items to their
		// new value (or values).
		for ( var i = 0; i < elems.length; i++ ) {
			var val = fn(elems[i],i);

			if ( val !== null && val != undefined ) {
				if ( val.constructor != Array ) val = [val];
				result = jQuery.merge( result, val );
			}
		}

		return result;
	},

	/*
	 * A number of helper functions used for managing events.
	 * Many of the ideas behind this code orignated from Dean Edwards' addEvent library.
	 */
	event: {

		// Bind an event to an element
		// Original by Dean Edwards
		add: function(element, type, handler) {
			// For whatever reason, IE has trouble passing the window object
			// around, causing it to be cloned in the process
			if ( jQuery.browser.msie && element.setInterval != undefined )
				element = window;

			// Make sure that the function being executed has a unique ID
			if ( !handler.guid )
				handler.guid = this.guid++;

			// Init the element's event structure
			if (!element.events)
				element.events = {};

			// Get the current list of functions bound to this event
			var handlers = element.events[type];

			// If it hasn't been initialized yet
			if (!handlers) {
				// Init the event handler queue
				handlers = element.events[type] = {};

				// Remember an existing handler, if it's already there
				if (element["on" + type])
					handlers[0] = element["on" + type];
			}

			// Add the function to the element's handler list
			handlers[handler.guid] = handler;

			// And bind the global event handler to the element
			element["on" + type] = this.handle;

			// Remember the function in a global list (for triggering)
			if (!this.global[type])
				this.global[type] = [];
			this.global[type].push( element );
		},

		guid: 1,
		global: {},

		// Detach an event or set of events from an element
		remove: function(element, type, handler) {
			if (element.events)
				if (type && element.events[type])
					if ( handler )
						delete element.events[type][handler.guid];
					else
						for ( var i in element.events[type] )
							delete element.events[type][i];
				else
					for ( var j in element.events )
						this.remove( element, j );
		},

		trigger: function(type,data,element) {
			// Clone the incoming data, if any
			data = $.merge([], data || []);

			// Handle a global trigger
			if ( !element ) {
				var g = this.global[type];
				if ( g )
					for ( var i = 0; i < g.length; i++ )
						this.trigger( type, data, g[i] );

			// Handle triggering a single element
			} else if ( element["on" + type] ) {
				// Pass along a fake event
				data.unshift( this.fix({ type: type, target: element }) );

				// Trigger the event
				element["on" + type].apply( element, data );
			}
		},

		handle: function(event) {
			if ( typeof jQuery == "undefined" ) return false;

			event = jQuery.event.fix( event || window.event || {} ); // Empty object is for triggered events with no data

			// If no correct event was found, fail
			if ( !event ) return false;

			var returnValue = true;

			var c = this.events[event.type];

			var args = [].slice.call( arguments, 1 );
			args.unshift( event );

			for ( var j in c ) {
				if ( c[j].apply( this, args ) === false ) {
					event.preventDefault();
					event.stopPropagation();
					returnValue = false;
				}
			}

			// Clean up added properties in IE to prevent memory leak
			if (jQuery.browser.msie) event.target = event.preventDefault = event.stopPropagation = null;

			return returnValue;
		},

		fix: function(event) {
			// check IE
			if(jQuery.browser.msie) {
				// fix target property, if available
				// check prevents overwriting of fake target coming from trigger
				if(event.srcElement)
					event.target = event.srcElement;
					
				// calculate pageX/Y
				var e = document.documentElement, b = document.body;
				event.pageX = event.clientX + (e.scrollLeft || b.scrollLeft);
				event.pageY = event.clientY + (e.scrollTop || b.scrollTop);
					
			// check safari and if target is a textnode
			} else if(jQuery.browser.safari && event.target.nodeType == 3) {
				// target is readonly, clone the event object
				event = jQuery.extend({}, event);
				// get parentnode from textnode
				event.target = event.target.parentNode;
			}
			
			// fix preventDefault and stopPropagation
			if (!event.preventDefault)
				event.preventDefault = function() {
					this.returnValue = false;
				};
				
			if (!event.stopPropagation)
				event.stopPropagation = function() {
					this.cancelBubble = true;
				};
				
			return event;
		}
	}
});

 
/*
 * Wheather the W3C compliant box model is being used.
 *
 * @property
 * @name $.boxModel
 * @type Boolean
 * @cat Javascript
 */
new function() {
	var b = navigator.userAgent.toLowerCase();

	// Figure out what browser is being used
	jQuery.browser = {
		safari: /webkit/.test(b),
		opera: /opera/.test(b),
		msie: /msie/.test(b) && !/opera/.test(b),
		mozilla: /mozilla/.test(b) && !/(compatible|webkit)/.test(b)
	};

	// Check to see if the W3C box model is being used
	jQuery.boxModel = !jQuery.browser.msie || document.compatMode == "CSS1Compat";
};

jQuery.macros = {
	to: {

		appendTo: "append",

		prependTo: "prepend",

		insertBefore: "before",

		insertAfter: "after"
	},



	css: "width,height,top,left,position,float,overflow,color,background".split(","),



	filter: [ "eq", "lt", "gt", "contains" ],

	attr: {


		val: "value",


		html: "innerHTML",


		id: null,


		title: null,


		name: null,


		href: null,


		src: null,


		rel: null
	},

	axis: {


		parent: "a.parentNode",


		ancestors: jQuery.parents,


		parents: jQuery.parents,


		next: "jQuery.sibling(a).next",


		prev: "jQuery.sibling(a).prev",


		siblings: "jQuery.sibling(a, null, true)",


		children: "jQuery.sibling(a.firstChild)"
	},

	each: {

		removeAttr: function( key ) {
			jQuery.attr( this, key, "" );
			this.removeAttribute( key );
		},

		show: function(){
			this.style.display = this.oldblock ? this.oldblock : "";
			if ( jQuery.css(this,"display") == "none" )
				this.style.display = "block";
		},

		hide: function(){
			this.oldblock = this.oldblock || jQuery.css(this,"display");
			if ( this.oldblock == "none" )
				this.oldblock = "block";
			this.style.display = "none";
		},

		toggle: function(){
			jQuery(this)[ jQuery(this).is(":hidden") ? "show" : "hide" ].apply( jQuery(this), arguments );
		},

		addClass: function(c){
			jQuery.className.add(this,c);
		},

		removeClass: function(c){
			jQuery.className.remove(this,c);
		},

		toggleClass: function( c ){
			jQuery.className[ jQuery.className.has(this,c) ? "remove" : "add" ](this, c);
		},


		remove: function(a){
			if ( !a || jQuery.filter( a, [this] ).r )
				this.parentNode.removeChild( this );
		},

		empty: function(){
			while ( this.firstChild )
				this.removeChild( this.firstChild );
		},

		bind: function( type, fn ) {
			jQuery.event.add( this, type, fn );
		},


		unbind: function( type, fn ) {
			jQuery.event.remove( this, type, fn );
		},

		trigger: function( type, data ) {
			jQuery.event.trigger( type, data, this );
		}
	}
};

jQuery.init();
jQuery.fn.extend({

	// We're overriding the old toggle function, so
	// remember it for later
	_toggle: jQuery.fn.toggle,
	toggle: function(a,b) {
		// If two functions are passed in, we're
		// toggling on a click
		return a && b && a.constructor == Function && b.constructor == Function ? this.click(function(e){
			// Figure out which function to execute
			this.last = this.last == a ? b : a;
			
			// Make sure that clicks stop
			e.preventDefault();
			
			// and execute the function
			return this.last.apply( this, [e] ) || false;
		}) :
		
		// Otherwise, execute the old toggle function
		this._toggle.apply( this, arguments );
	},
	hover: function(f,g) {
		
		// A private function for haandling mouse 'hovering'
		function handleHover(e) {
			// Check if mouse(over|out) are still within the same parent element
			var p = (e.type == "mouseover" ? e.fromElement : e.toElement) || e.relatedTarget;
	
			// Traverse up the tree
			while ( p && p != this ) try { p = p.parentNode } catch(e) { p = this; };
			
			// If we actually just moused on to a sub-element, ignore it
			if ( p == this ) return false;
			
			// Execute the right function
			return (e.type == "mouseover" ? f : g).apply(this, [e]);
		}
		
		// Bind the function to the two event listeners
		return this.mouseover(handleHover).mouseout(handleHover);
	},
	ready: function(f) {
		// If the DOM is already ready
		if ( jQuery.isReady )
			// Execute the function immediately
			f.apply( document );
			
		// Otherwise, remember the function for later
		else {
			// Add the function to the wait list
			jQuery.readyList.push( f );
		}
	
		return this;
	}
});

jQuery.extend({
	/*
	 * All the code that makes DOM Ready work nicely.
	 */
	isReady: false,
	readyList: [],
	
	// Handle when the DOM is ready
	ready: function() {
		// Make sure that the DOM is not already loaded
		if ( !jQuery.isReady ) {
			// Remember that the DOM is ready
			jQuery.isReady = true;
			
			// If there are functions bound, to execute
			if ( jQuery.readyList ) {
				// Execute all of them
				for ( var i = 0; i < jQuery.readyList.length; i++ )
					jQuery.readyList[i].apply( document );
				
				// Reset the list of functions
				jQuery.readyList = null;
			}
			// Remove event lisenter to avoid memory leak
			if ( jQuery.browser.mozilla || jQuery.browser.opera )
				document.removeEventListener( "DOMContentLoaded", jQuery.ready, false );
		}
	}
});

new function(){

	var e = ("blur,focus,load,resize,scroll,unload,click,dblclick," +
		"mousedown,mouseup,mousemove,mouseover,mouseout,change,reset,select," + 
		"submit,keydown,keypress,keyup,error").split(",");

	// Go through all the event names, but make sure that
	// it is enclosed properly
	for ( var i = 0; i < e.length; i++ ) new function(){
			
		var o = e[i];
		
		// Handle event binding
		jQuery.fn[o] = function(f){
			return f ? this.bind(o, f) : this.trigger(o);
		};
		
		// Handle event unbinding
		jQuery.fn["un"+o] = function(f){ return this.unbind(o, f); };
		
		// Finally, handle events that only fire once
		jQuery.fn["one"+o] = function(f){
			// save cloned reference to this
			var element = jQuery(this);
			var handler = function() {
				// unbind itself when executed
				element.unbind(o, handler);
				element = null;
				// apply original handler with the same arguments
				return f.apply(this, arguments);
			};
			return this.bind(o, handler);
		};
			
	};
	
	// If Mozilla is used
	if ( jQuery.browser.mozilla || jQuery.browser.opera ) {
		// Use the handy event callback
		document.addEventListener( "DOMContentLoaded", jQuery.ready, false );
	
	// If IE is used, use the excellent hack by Matthias Miller
	// http://www.outofhanwell.com/blog/index.php?title=the_window_onload_problem_revisited
	} else if ( jQuery.browser.msie ) {
	
		// Only works if you document.write() it
		document.write("<scr" + "ipt id=__ie_init defer=true " + 
			"src=//:><\/script>");
	
		// Use the defer script hack
		var script = document.getElementById("__ie_init");
		if (script) // script does not exist if jQuery is loaded dynamically
			script.onreadystatechange = function() {
				if ( this.readyState != "complete" ) return;
				this.parentNode.removeChild( this );
				jQuery.ready();
			};
	
		// Clear from memory
		script = null;
	
	// If Safari  is used
	} else if ( jQuery.browser.safari ) {
		// Continually check to see if the document.readyState is valid
		jQuery.safariTimer = setInterval(function(){
			// loaded and complete are both valid states
			if ( document.readyState == "loaded" || 
				document.readyState == "complete" ) {
	
				// If either one are found, remove the timer
				clearInterval( jQuery.safariTimer );
				jQuery.safariTimer = null;
	
				// and execute any waiting functions
				jQuery.ready();
			}
		}, 10);
	} 

	// A fallback to window.onload, that will always work
	jQuery.event.add( window, "load", jQuery.ready );
	
};

// Clean up after IE to avoid memory leaks
if (jQuery.browser.msie) jQuery(window).unload(function() {
	var event = jQuery.event, global = event.global;
	for (var type in global) {
 		var els = global[type], i = els.length;
		if (i>0) do if (type != 'unload') event.remove(els[i-1], type); while (--i);
	}
});
jQuery.fn.extend({

	// overwrite the old show method
	_show: jQuery.fn.show,

	show: function(speed,callback){
		return speed ? this.animate({
			height: "show", width: "show", opacity: "show"
		}, speed, callback) : this._show();
	},
	
	// Overwrite the old hide method
	_hide: jQuery.fn.hide,

	hide: function(speed,callback){
		return speed ? this.animate({
			height: "hide", width: "hide", opacity: "hide"
		}, speed, callback) : this._hide();
	},

	slideDown: function(speed,callback){
		return this.animate({height: "show"}, speed, callback);
	},

	slideUp: function(speed,callback){
		return this.animate({height: "hide"}, speed, callback);
	},

	slideToggle: function(speed,callback){
		return this.each(function(){
			var state = jQuery(this).is(":hidden") ? "show" : "hide";
			jQuery(this).animate({height: state}, speed, callback);
		});
	},

	fadeIn: function(speed,callback){
		return this.animate({opacity: "show"}, speed, callback);
	},

	fadeOut: function(speed,callback){
		return this.animate({opacity: "hide"}, speed, callback);
	},

	fadeTo: function(speed,to,callback){
		return this.animate({opacity: to}, speed, callback);
	},
	animate: function(prop,speed,callback) {
		return this.queue(function(){
		
			this.curAnim = jQuery.extend({}, prop);
			
			for ( var p in prop ) {
				var e = new jQuery.fx( this, jQuery.speed(speed,callback), p );
				if ( prop[p].constructor == Number )
					e.custom( e.cur(), prop[p] );
				else
					e[ prop[p] ]( prop );
			}
			
		});
	},
	queue: function(type,fn){
		if ( !fn ) {
			fn = type;
			type = "fx";
		}
	
		return this.each(function(){
			if ( !this.queue )
				this.queue = {};
	
			if ( !this.queue[type] )
				this.queue[type] = [];
	
			this.queue[type].push( fn );
		
			if ( this.queue[type].length == 1 )
				fn.apply(this);
		});
	}

});

jQuery.extend({
	
	speed: function(s,o) {
		o = o || {};
		
		if ( o.constructor == Function )
			o = { complete: o };
		
		var ss = { slow: 600, fast: 200 };
		o.duration = (s && s.constructor == Number ? s : ss[s]) || 400;
	
		// Queueing
		o.oldComplete = o.complete;
		o.complete = function(){
			jQuery.dequeue(this, "fx");
			if ( o.oldComplete && o.oldComplete.constructor == Function )
				o.oldComplete.apply( this );
		};
	
		return o;
	},
	
	queue: {},
	
	dequeue: function(elem,type){
		type = type || "fx";
	
		if ( elem.queue && elem.queue[type] ) {
			// Remove self
			elem.queue[type].shift();
	
			// Get next function
			var f = elem.queue[type][0];
		
			if ( f ) f.apply( elem );
		}
	},

	/*
	 * I originally wrote fx() as a clone of moo.fx and in the process
	 * of making it small in size the code became illegible to sane
	 * people. You've been warned.
	 */
	
	fx: function( elem, options, prop ){

		var z = this;

		// The users options
		z.o = {
			duration: options.duration || 400,
			complete: options.complete,
			step: options.step
		};

		// The element
		z.el = elem;

		// The styles
		var y = z.el.style;
		
		// Store display property
		var oldDisplay = jQuery.css(z.el, 'display');
		// Set display property to block for animation
		y.display = "block";
		// Make sure that nothing sneaks out
		y.overflow = "hidden";

		// Simple function for setting a style value
		z.a = function(){
			if ( options.step )
				options.step.apply( elem, [ z.now ] );

			if ( prop == "opacity" )
				jQuery.attr(y, "opacity", z.now); // Let attr handle opacity
			else if ( parseInt(z.now) ) // My hate for IE will never die
				y[prop] = parseInt(z.now) + "px";
		};

		// Figure out the maximum number to run to
		z.max = function(){
			return parseFloat( jQuery.css(z.el,prop) );
		};

		// Get the current size
		z.cur = function(){
			var r = parseFloat( jQuery.curCSS(z.el, prop) );
			return r && r > -10000 ? r : z.max();
		};

		// Start an animation from one number to another
		z.custom = function(from,to){
			z.startTime = (new Date()).getTime();
			z.now = from;
			z.a();

			z.timer = setInterval(function(){
				z.step(from, to);
			}, 13);
		};

		// Simple 'show' function
		z.show = function(){
			if ( !z.el.orig ) z.el.orig = {};

			// Remember where we started, so that we can go back to it later
			z.el.orig[prop] = this.cur();

			z.o.show = true;

			// Begin the animation
			z.custom(0, z.el.orig[prop]);

			// Stupid IE, look what you made me do
			if ( prop != "opacity" )
				y[prop] = "1px";
		};

		// Simple 'hide' function
		z.hide = function(){
			if ( !z.el.orig ) z.el.orig = {};

			// Remember where we started, so that we can go back to it later
			z.el.orig[prop] = this.cur();

			z.o.hide = true;

			// Begin the animation
			z.custom(z.el.orig[prop], 0);
		};
		
		//Simple 'toggle' function
		z.toggle = function() {
			if ( !z.el.orig ) z.el.orig = {};

			// Remember where we started, so that we can go back to it later
			z.el.orig[prop] = this.cur();

			if(oldDisplay == 'none')  {
				z.o.show = true;
				
				// Stupid IE, look what you made me do
				if ( prop != "opacity" )
					y[prop] = "1px";

				// Begin the animation
				z.custom(0, z.el.orig[prop]);	
			} else {
				z.o.hide = true;

				// Begin the animation
				z.custom(z.el.orig[prop], 0);
			}		
		};

		// Each step of an animation
		z.step = function(firstNum, lastNum){
			var t = (new Date()).getTime();

			if (t > z.o.duration + z.startTime) {
				// Stop the timer
				clearInterval(z.timer);
				z.timer = null;

				z.now = lastNum;
				z.a();

				z.el.curAnim[ prop ] = true;

				var done = true;
				for ( var i in z.el.curAnim )
					if ( z.el.curAnim[i] !== true )
						done = false;

				if ( done ) {
					// Reset the overflow
					y.overflow = '';
					
					// Reset the display
					y.display = oldDisplay;
					if (jQuery.css(z.el, 'display') == 'none')
						y.display = 'block';

					// Hide the element if the "hide" operation was done
					if ( z.o.hide ) 
						y.display = 'none';

					// Reset the properties, if the item has been hidden or shown
					if ( z.o.hide || z.o.show )
						for ( var p in z.el.curAnim )
							if (p == "opacity")
								jQuery.attr(y, p, z.el.orig[p]);
							else
								y[p] = '';
				}

				// If a callback was provided, execute it
				if( done && z.o.complete && z.o.complete.constructor == Function )
					// Execute the complete function
					z.o.complete.apply( z.el );
			} else {
				// Figure out where in the animation we are and set the number
				var p = (t - this.startTime) / z.o.duration;
				z.now = ((-Math.cos(p*Math.PI)/2) + 0.5) * (lastNum-firstNum) + firstNum;

				// Perform the next step of the animation
				z.a();
			}
		};
	
	}

});
jQuery.fn.extend({
	loadIfModified: function( url, params, callback ) {
		this.load( url, params, callback, 1 );
	},
	load: function( url, params, callback, ifModified ) {
		if ( url.constructor == Function )
			return this.bind("load", url);

		callback = callback || function(){};

		// Default to a GET request
		var type = "GET";

		// If the second parameter was provided
		if ( params ) {
			// If it's a function
			if ( params.constructor == Function ) {
				// We assume that it's the callback
				callback = params;
				params = null;

			// Otherwise, build a param string
			} else {
				params = jQuery.param( params );
				type = "POST";
			}
		}

		var self = this;

		// Request the remote document
		jQuery.ajax({
			url: url,
			type: type,
			data: params,
			ifModified: ifModified,
			complete: function(res, status){
				if ( status == "success" || !ifModified && status == "notmodified" ) {
					// Inject the HTML into all the matched elements
					self.html(res.responseText)
					  // Execute all the scripts inside of the newly-injected HTML
					  .evalScripts()
					  // Execute callback


					  .each( callback, [res.responseText, status, res] );
				} else
					callback.apply( self, [res.responseText, status, res] );
			}
		});
		return this;
	},
	serialize: function() {
		return jQuery.param( this );
	},
	evalScripts: function() {
		return this.find('script').each(function(){
			if ( this.src )
				// for some weird reason, it doesn't work if the callback is ommited
				jQuery.getScript( this.src );
			else {
				jQuery.globalEval( this.text || this.textContent || this.innerHTML || "" );
			}
		}).end();
	}

});

// If IE is used, create a wrapper for the XMLHttpRequest object
if ( jQuery.browser.msie && typeof XMLHttpRequest == "undefined" )
	XMLHttpRequest = function(){
		return new ActiveXObject("Microsoft.XMLHTTP");
	};

// Attach a bunch of functions for handling common AJAX events

new function(){
	var e = "ajaxStart,ajaxStop,ajaxComplete,ajaxError,ajaxSuccess,ajaxSend".split(",");

	for ( var i = 0; i < e.length; i++ ) new function(){
		var o = e[i];
		jQuery.fn[o] = function(f){
			return this.bind(o, f);
		};
	};
};

jQuery.extend({
	get: function( url, data, callback, type, ifModified ) {
		// shift arguments if data argument was ommited
		if ( data && data.constructor == Function ) {
			callback = data;
			data = null;
		}

		// Delegate
		jQuery.ajax({
			url: url,
			data: data,
			success: callback,
			dataType: type,
			ifModified: ifModified
		});
	},
	getIfModified: function( url, data, callback, type ) {
		jQuery.get(url, data, callback, type, 1);
	},
	getScript: function( url, callback ) {
		if(callback)
			jQuery.get(url, null, callback, "script");
		else {
			jQuery.get(url, null, null, "script");
		}
	},
	getJSON: function( url, data, callback ) {
		jQuery.get(url, data, callback, "json");
	},
	post: function( url, data, callback, type ) {
		// Delegate
		jQuery.ajax({
			type: "POST",
			url: url,
			data: data,
			success: callback,
			dataType: type
		});
	},

	// timeout (ms)
	timeout: 0,
	ajaxTimeout: function(timeout) {
		jQuery.timeout = timeout;
	},

	// Last-Modified header cache for next request
	lastModified: {},
	ajax: function( s ) {
		// TODO introduce global settings, allowing the client to modify them for all requests, not only timeout
		s = jQuery.extend({
			global: true,
			ifModified: false,
			type: "GET",
			timeout: jQuery.timeout,
			complete: null,
			success: null,
			error: null,
			dataType: null,
			url: null,
			data: null,
			contentType: "application/x-www-form-urlencoded",
			processData: true,
			async: true,
			beforeSend: null
		}, s);

		// if data available
		if ( s.data ) {
			// convert data if not already a string
			if (s.processData && typeof s.data != 'string')
    			s.data = jQuery.param(s.data);
			// append data to url for get requests
			if( s.type.toLowerCase() == "get" )
				// "?" + data or "&" + data (in case there are already params)
				s.url += ((s.url.indexOf("?") > -1) ? "&" : "?") + s.data;
		}

		// Watch for a new set of requests
		if ( s.global && ! jQuery.active++ )
			jQuery.event.trigger( "ajaxStart" );

		var requestDone = false;

		// Create the request object
		var xml = new XMLHttpRequest();

		// Open the socket
		xml.open(s.type, s.url, s.async);

		// Set the correct header, if data is being sent
		if ( s.data )
			xml.setRequestHeader("Content-Type", s.contentType);

		// Set the If-Modified-Since header, if ifModified mode.
		if ( s.ifModified )
			xml.setRequestHeader("If-Modified-Since",
				jQuery.lastModified[s.url] || "Thu, 01 Jan 1970 00:00:00 GMT" );

		// Set header so the called script knows that it's an XMLHttpRequest
		xml.setRequestHeader("X-Requested-With", "XMLHttpRequest");

		// Make sure the browser sends the right content length
		if ( xml.overrideMimeType )
			xml.setRequestHeader("Connection", "close");
			
		// Allow custom headers/mimetypes
		if( s.beforeSend )
			s.beforeSend(xml);
		if (s.global)
		    jQuery.event.trigger("ajaxSend", [xml, s]);

		// Wait for a response to come back
		var onreadystatechange = function(isTimeout){
			// The transfer is complete and the data is available, or the request timed out
			if ( xml && (xml.readyState == 4 || isTimeout == "timeout") ) {
				requestDone = true;

				var status = jQuery.httpSuccess( xml ) && isTimeout != "timeout" ?
					s.ifModified && jQuery.httpNotModified( xml, s.url ) ? "notmodified" : "success" : "error";

				// Make sure that the request was successful or notmodified
				if ( status != "error" ) {
					// Cache Last-Modified header, if ifModified mode.
					var modRes;
					try {
						modRes = xml.getResponseHeader("Last-Modified");
					} catch(e) {} // swallow exception thrown by FF if header is not available

					if ( s.ifModified && modRes )
						jQuery.lastModified[s.url] = modRes;

					// process the data (runs the xml through httpData regardless of callback)
					var data = jQuery.httpData( xml, s.dataType );

					// If a local callback was specified, fire it and pass it the data
					if ( s.success )
						s.success( data, status );

					// Fire the global callback
					if( s.global )
						jQuery.event.trigger( "ajaxSuccess", [xml, s] );

				// Otherwise, the request was not successful
				} else {
					// If a local callback was specified, fire it
					if ( s.error ) s.error( xml, status );

					// Fire the global callback
					if( s.global )
						jQuery.event.trigger( "ajaxError", [xml, s] );
				}

				// The request was completed
				if( s.global )
					jQuery.event.trigger( "ajaxComplete", [xml, s] );

				// Handle the global AJAX counter
				if ( s.global && ! --jQuery.active )
					jQuery.event.trigger( "ajaxStop" );

				// Process result
				if ( s.complete ) s.complete(xml, status);

				// Stop memory leaks
				xml.onreadystatechange = function(){};
				xml = null;

			}
		};
		xml.onreadystatechange = onreadystatechange;

		// Timeout checker
		if(s.timeout > 0)
			setTimeout(function(){
				// Check to see if the request is still happening
				if (xml) {
					// Cancel the request
					xml.abort();

					if ( !requestDone ) onreadystatechange( "timeout" );

					// Clear from memory
					xml = null;
				}
			}, s.timeout);

		// Send the data
		xml.send(s.data);
		
		// return XMLHttpRequest to allow aborting the request etc.
		return xml;
	},

	// Counter for holding the number of active queries
	active: 0,

	// Determines if an XMLHttpRequest was successful or not
	httpSuccess: function(r) {
		try {
			return !r.status && location.protocol == "file:" ||
				( r.status >= 200 && r.status < 300 ) || r.status == 304 ||
				jQuery.browser.safari && r.status == undefined;
		} catch(e){}

		return false;
	},

	// Determines if an XMLHttpRequest returns NotModified
	httpNotModified: function(xml, url) {
		try {
			var xmlRes = xml.getResponseHeader("Last-Modified");

			// Firefox always returns 200. check Last-Modified date
			return xml.status == 304 || xmlRes == jQuery.lastModified[url] ||
				jQuery.browser.safari && xml.status == undefined;
		} catch(e){}

		return false;
	},

	/* Get the data out of an XMLHttpRequest.
	 * Return parsed XML if content-type header is "xml" and type is "xml" or omitted,
	 * otherwise return plain text.
	 * (String) data - The type of data that you're expecting back,
	 * (e.g. "xml", "html", "script")
	 */
	httpData: function(r,type) {
		var ct = r.getResponseHeader("content-type");
		var data = !type && ct && ct.indexOf("xml") >= 0;
		data = type == "xml" || data ? r.responseXML : r.responseText;

		// If the type is "script", eval it in global context
		if ( type == "script" ) {
			jQuery.globalEval( data );
		}

		// Get the JavaScript object, if JSON is used.
		if ( type == "json" ) eval( "data = " + data );

		// evaluate scripts within html
		if ( type == "html" ) jQuery("<div>").html(data).evalScripts();

		return data;
	},

	// Serialize an array of form elements or a set of
	// key/values into a query string
	param: function(a) {
		var s = [];

		// If an array was passed in, assume that it is an array
		// of form elements
		if ( a.constructor == Array || a.jquery ) {
			// Serialize the form elements
			for ( var i = 0; i < a.length; i++ )
				s.push( a[i].name + "=" + encodeURIComponent( a[i].value ) );

		// Otherwise, assume that it's an object of key/value pairs
		} else {
			// Serialize the key/values
			for ( var j in a ) {
				// If the value is an array then the key names need to be repeated
				if( a[j].constructor == Array ) {
					for (var k = 0; k < a[j].length; k++) {
						s.push( j + "=" + encodeURIComponent( a[j][k] ) );
					}
				} else {
					s.push( j + "=" + encodeURIComponent( a[j] ) );
				}
			}
		}

		// Return the resulting serialization
		return s.join("&");
	},
	
	// evalulates a script in global context
	// not reliable for safari
	globalEval: function(data) {
		if (window.execScript)
			window.execScript( data );
		else if(jQuery.browser.safari)
			// safari doesn't provide a synchronous global eval
			window.setTimeout( data, 0 );
		else
			eval.call( window, data );
	}

});
} // close: if(typeof window.jQuery == "undefined") {






	
	//Balloon Popup//
	
//Rich HTML Balloon Tooltip: http://www.dynamicdrive.com/dynamicindex5/balloontooltip.htm
//Created: September 10th, 2006

var disappeardelay=250  //tooltip disappear delay (in miliseconds)
var verticaloffset=-130 //added vertical offset of tooltip from anchor link, if any
var horizontaloffset=-150 //added horizontal offset of tooltip from anchor link, if any
var enablearrowhead=0 //0, 1, or 2 - to disable(0) or enable the top(1) or side(2) arrow image
var arrowheadimg=["/images/arrowdown.gif", "/images/arrowup.gif", "/images/arrowleft.gif", "/images/arrowright.gif"] //path to down and up left and right arrow images
var arrowheadsize=11 //size of arrow image (amount to reveal)

/////No further editting needed

function preloadarrow(arrow){
var graphic=new Image();
graphic.src=arrow;
return graphic.src;
}

for (var i_tem = arrowheadimg.length-1; i_tem >=0 ; i_tem--)
arrowheadimg[i_tem]=preloadarrow(arrowheadimg[i_tem])

var ie=document.all
var ns6=document.getElementById&&!document.all
verticaloffset=(enablearrowhead==1)? verticaloffset+arrowheadsize : verticaloffset
horizontaloffset=(enablearrowhead==2)? horizontaloffset+arrowheadsize : horizontaloffset

function getposOffset(what, offsettype){
var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
var parentEl=what.offsetParent;
while (parentEl!=null){
totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
parentEl=parentEl.offsetParent;
}
return totaloffset;
}

function showhide(obj, e){
dropmenuobj.style.left=dropmenuobj.style.top="-500px"
if (e.type=="mouseover")
obj.visibility="visible"
}

function iecompattest(){
return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

function clearbrowseredge(obj, whichedge){
if (whichedge=="rightedge"){
edgeoffsetx=0
var windowedge=ie && !window.opera? iecompattest().scrollLeft+iecompattest().clientWidth-15 : window.pageXOffset+window.innerWidth-15
dropmenuobj.contentmeasure=dropmenuobj.offsetWidth
if (windowedge-dropmenuobj.x < dropmenuobj.contentmeasure)
edgeoffsetx=dropmenuobj.contentmeasure-obj.offsetWidth
return edgeoffsetx
}
else{
edgeoffsety=0
var topedge=ie && !window.opera? iecompattest().scrollTop : window.pageYOffset
var windowedge=ie && !window.opera? iecompattest().scrollTop+iecompattest().clientHeight-15 : window.pageYOffset+window.innerHeight-18
dropmenuobj.contentmeasure=dropmenuobj.offsetHeight
if (windowedge-dropmenuobj.y < dropmenuobj.contentmeasure) //move up?
edgeoffsety=dropmenuobj.contentmeasure+obj.offsetHeight+(verticaloffset*2)
return edgeoffsety
}
}

function displayballoontip(obj, e){ //main ballooon tooltip function
if (window.event) event.cancelBubble=true
else if (e.stopPropagation) e.stopPropagation()
if (typeof dropmenuobj!="undefined") //hide previous tooltip?
dropmenuobj.style.visibility="hidden"
clearhidemenu()
//obj.onmouseout=delayhidemenu
dropmenuobj=document.getElementById(obj.getAttribute("rel"))
showhide(dropmenuobj.style, e)
dropmenuobj.x=getposOffset(obj, "left")+(enablearrowhead==2? obj.offsetWidth : 0)+horizontaloffset
dropmenuobj.y=getposOffset(obj, "top")+verticaloffset-(enablearrowhead==2? obj.offsetHeight : 0)
dropmenuobj.style.left=dropmenuobj.x-clearbrowseredge(obj, "rightedge")+"px"
dropmenuobj.style.top=dropmenuobj.y-clearbrowseredge(obj, "bottomedge")+obj.offsetHeight+"px"
dropmenuobj.style.height=''
if (enablearrowhead==1)
displaytiparrowt()
else if (enablearrowhead==2)
displaytiparrows()
}

function displaytiparrowt(){ //function to display optional arrow image associated with tooltip
tiparrow=document.getElementById("arrowhead")
tiparrow.src=(edgeoffsety!=0)? arrowheadimg[0] : arrowheadimg[1]
var ieshadowwidth=(dropmenuobj.filters && dropmenuobj.filters[0])? dropmenuobj.filters[0].Strength-1 : 0
//modify "left" value depending on whether there's no room on right edge of browser to display it, respectively
tiparrow.style.left=(edgeoffsetx!=0)? parseInt(dropmenuobj.style.left)+dropmenuobj.offsetWidth-tiparrow.offsetWidth-10+"px" : parseInt(dropmenuobj.style.left)+5+"px"
//modify "top" value depending on whether there's no room on right edge of browser to display it, respectively
tiparrow.style.top=(edgeoffsety!=0)? parseInt(dropmenuobj.style.top)+dropmenuobj.offsetHeight-tiparrow.offsetHeight-ieshadowwidth+arrowheadsize+"px" : parseInt(dropmenuobj.style.top)-arrowheadsize+"px"
tiparrow.style.visibility="visible"
}
function displaytiparrows(){ //function to display optional arrow image associated with tooltip
tiparrow=document.getElementById("arrowhead")
tiparrow.src=(edgeoffsetx!=0)? arrowheadimg[3] : arrowheadimg[2]
dropmenuobj.style.height=Math.max(tiparrow.height, dropmenuobj.offsetHeight)+'px'
var ieshadowwidth=(dropmenuobj.filters && dropmenuobj.filters[0])? dropmenuobj.filters[0].Strength-1 : 0
//modify "left" value depending on whether there's no room on right edge of browser to display it, respectively
tiparrow.style.top=(edgeoffsety!=0)? parseInt(dropmenuobj.style.top)+dropmenuobj.offsetHeight-tiparrow.offsetHeight-10+"px" : parseInt(dropmenuobj.style.top)+5+"px"
//modify "top" value depending on whether there's no room on right edge of browser to display it, respectively
tiparrow.style.left=(edgeoffsetx!=0)? parseInt(dropmenuobj.style.left)+dropmenuobj.offsetWidth-tiparrow.offsetWidth-ieshadowwidth+arrowheadsize+"px" : parseInt(dropmenuobj.style.left)-arrowheadsize+"px"
tiparrow.style.visibility="visible"
}

function delayhidemenu(){
delayhide=setTimeout("dropmenuobj.style.visibility='hidden'; dropmenuobj.style.left=0; if (enablearrowhead) tiparrow.style.visibility='hidden'",disappeardelay)
}

function clearhidemenu(){
if (typeof delayhide!="undefined")
clearTimeout(delayhide)
}

function reltoelement(linkobj){ //tests if a link has "rel" defined and it's the ID of an element on page
var relvalue=linkobj.getAttribute("rel")
return (relvalue!=null && relvalue!="" && document.getElementById(relvalue)!=null && document.getElementById(relvalue).className=="balloonstyle")? true : false
}

function initalizetooltip(){
var all_links=document.getElementsByTagName("a")
if (enablearrowhead){
tiparrow=document.createElement("img")
tiparrow.setAttribute("src", arrowheadimg[enablearrowhead])
tiparrow.setAttribute("id", "arrowhead")
document.body.appendChild(tiparrow)
}
for (var i=0; i<all_links.length; i++){
if (reltoelement(all_links[i])){ //if link has "rel" defined and it's the ID of an element on page
all_links[i].onmouseover=function(e){
var evtobj=window.event? window.event : e
displayballoontip(this, evtobj)
}
all_links[i].onmouseout=delayhidemenu
}
}
}

if (window.addEventListener)
window.addEventListener("load", initalizetooltip, false)
else if (window.attachEvent)
window.attachEvent("onload", initalizetooltip)
else if (document.getElementById)
window.onload=initalizetooltip

//End of order list /xpedx/js/jQuery.js

//Adding order list swc/xpedx/js/jquery.ui.core.js

/*!
 * jQuery UI 1.8.5
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI
 */
(function( $, undefined ) {

// prevent duplicate loading
// this is only a problem because we proxy existing functions
// and we don't want to double proxy them
$.ui = $.ui || {};
if ( $.ui.version ) {
	return;
}

$.extend( $.ui, {
	version: "1.8.5",

	keyCode: {
		ALT: 18,
		BACKSPACE: 8,
		CAPS_LOCK: 20,
		COMMA: 188,
		COMMAND: 91,
		COMMAND_LEFT: 91, // COMMAND
		COMMAND_RIGHT: 93,
		CONTROL: 17,
		DELETE: 46,
		DOWN: 40,
		END: 35,
		ENTER: 13,
		ESCAPE: 27,
		HOME: 36,
		INSERT: 45,
		LEFT: 37,
		MENU: 93, // COMMAND_RIGHT
		NUMPAD_ADD: 107,
		NUMPAD_DECIMAL: 110,
		NUMPAD_DIVIDE: 111,
		NUMPAD_ENTER: 108,
		NUMPAD_MULTIPLY: 106,
		NUMPAD_SUBTRACT: 109,
		PAGE_DOWN: 34,
		PAGE_UP: 33,
		PERIOD: 190,
		RIGHT: 39,
		SHIFT: 16,
		SPACE: 32,
		TAB: 9,
		UP: 38,
		WINDOWS: 91 // COMMAND
	}
});

// plugins
$.fn.extend({
	_focus: $.fn.focus,
	focus: function( delay, fn ) {
		return typeof delay === "number" ?
			this.each(function() {
				var elem = this;
				setTimeout(function() {
					$( elem ).focus();
					if ( fn ) {
						fn.call( elem );
					}
				}, delay );
			}) :
			this._focus.apply( this, arguments );
	},

	scrollParent: function() {
		var scrollParent;
		if (($.browser.msie && (/(static|relative)/).test(this.css('position'))) || (/absolute/).test(this.css('position'))) {
			scrollParent = this.parents().filter(function() {
				return (/(relative|absolute|fixed)/).test($.curCSS(this,'position',1)) && (/(auto|scroll)/).test($.curCSS(this,'overflow',1)+$.curCSS(this,'overflow-y',1)+$.curCSS(this,'overflow-x',1));
			}).eq(0);
		} else {
			scrollParent = this.parents().filter(function() {
				return (/(auto|scroll)/).test($.curCSS(this,'overflow',1)+$.curCSS(this,'overflow-y',1)+$.curCSS(this,'overflow-x',1));
			}).eq(0);
		}

		return (/fixed/).test(this.css('position')) || !scrollParent.length ? $(document) : scrollParent;
	},

	zIndex: function( zIndex ) {
		if ( zIndex !== undefined ) {
			return this.css( "zIndex", zIndex );
		}

		if ( this.length ) {
			var elem = $( this[ 0 ] ), position, value;
			while ( elem.length && elem[ 0 ] !== document ) {
				// Ignore z-index if position is set to a value where z-index is ignored by the browser
				// This makes behavior of this function consistent across browsers
				// WebKit always returns auto if the element is positioned
				position = elem.css( "position" );
				if ( position === "absolute" || position === "relative" || position === "fixed" ) {
					// IE returns 0 when zIndex is not specified
					// other browsers return a string
					// we ignore the case of nested elements with an explicit value of 0
					// <div style="z-index: -10;"><div style="z-index: 0;"></div></div>
					value = parseInt( elem.css( "zIndex" ) );
					if ( !isNaN( value ) && value != 0 ) {
						return value;
					}
				}
				elem = elem.parent();
			}
		}

		return 0;
	},
	
	disableSelection: function() {
		return this.bind(
			"mousedown.ui-disableSelection selectstart.ui-disableSelection",
			function( event ) {
				event.preventDefault();
			});
	},

	enableSelection: function() {
		return this.unbind( ".ui-disableSelection" );
	}
});

$.each( [ "Width", "Height" ], function( i, name ) {
	var side = name === "Width" ? [ "Left", "Right" ] : [ "Top", "Bottom" ],
		type = name.toLowerCase(),
		orig = {
			innerWidth: $.fn.innerWidth,
			innerHeight: $.fn.innerHeight,
			outerWidth: $.fn.outerWidth,
			outerHeight: $.fn.outerHeight
		};

	function reduce( elem, size, border, margin ) {
		$.each( side, function() {
			size -= parseFloat( $.curCSS( elem, "padding" + this, true) ) || 0;
			if ( border ) {
				size -= parseFloat( $.curCSS( elem, "border" + this + "Width", true) ) || 0;
			}
			if ( margin ) {
				size -= parseFloat( $.curCSS( elem, "margin" + this, true) ) || 0;
			}
		});
		return size;
	}

	$.fn[ "inner" + name ] = function( size ) {
		if ( size === undefined ) {
			return orig[ "inner" + name ].call( this );
		}

		return this.each(function() {
			$.style( this, type, reduce( this, size ) + "px" );
		});
	};

	$.fn[ "outer" + name] = function( size, margin ) {
		if ( typeof size !== "number" ) {
			return orig[ "outer" + name ].call( this, size );
		}

		return this.each(function() {
			$.style( this, type, reduce( this, size, true, margin ) + "px" );
		});
	};
});

// selectors
function visible( element ) {
	return !$( element ).parents().andSelf().filter(function() {
		return $.curCSS( this, "visibility" ) === "hidden" ||
			$.expr.filters.hidden( this );
	}).length;
}

$.extend( $.expr[ ":" ], {
	data: function( elem, i, match ) {
		return !!$.data( elem, match[ 3 ] );
	},

	focusable: function( element ) {
		var nodeName = element.nodeName.toLowerCase(),
			tabIndex = $.attr( element, "tabindex" );
		if ( "area" === nodeName ) {
			var map = element.parentNode,
				mapName = map.name,
				img;
			if ( !element.href || !mapName || map.nodeName.toLowerCase() !== "map" ) {
				return false;
			}
			img = $( "img[usemap=#" + mapName + "]" )[0];
			return !!img && visible( img );
		}
		return ( /input|select|textarea|button|object/.test( nodeName )
			? !element.disabled
			: "a" == nodeName
				? element.href || !isNaN( tabIndex )
				: !isNaN( tabIndex ))
			// the element and all of its ancestors must be visible
			&& visible( element );
	},

	tabbable: function( element ) {
		var tabIndex = $.attr( element, "tabindex" );
		return ( isNaN( tabIndex ) || tabIndex >= 0 ) && $( element ).is( ":focusable" );
	}
});

// support
$(function() {
	var div = document.createElement( "div" ),
		body = document.body;

	$.extend( div.style, {
		minHeight: "100px",
		height: "auto",
		padding: 0,
		borderWidth: 0
	});

	$.support.minHeight = body.appendChild( div ).offsetHeight === 100;
	// set display to none to avoid a layout bug in IE
	// http://dev.jquery.com/ticket/4014
	body.removeChild( div ).style.display = "none";
});





// deprecated
$.extend( $.ui, {
	// $.ui.plugin is deprecated.  Use the proxy pattern instead.
	plugin: {
		add: function( module, option, set ) {
			var proto = $.ui[ module ].prototype;
			for ( var i in set ) {
				proto.plugins[ i ] = proto.plugins[ i ] || [];
				proto.plugins[ i ].push( [ option, set[ i ] ] );
			}
		},
		call: function( instance, name, args ) {
			var set = instance.plugins[ name ];
			if ( !set || !instance.element[ 0 ].parentNode ) {
				return;
			}
	
			for ( var i = 0; i < set.length; i++ ) {
				if ( instance.options[ set[ i ][ 0 ] ] ) {
					set[ i ][ 1 ].apply( instance.element, args );
				}
			}
		}
	},
	
	// will be deprecated when we switch to jQuery 1.4 - use jQuery.contains()
	contains: function( a, b ) {
		return document.compareDocumentPosition ?
			a.compareDocumentPosition( b ) & 16 :
			a !== b && a.contains( b );
	},
	
	// only used by resizable
	hasScroll: function( el, a ) {
	
		//If overflow is hidden, the element might have extra content, but the user wants to hide it
		if ( $( el ).css( "overflow" ) === "hidden") {
			return false;
		}
	
		var scroll = ( a && a === "left" ) ? "scrollLeft" : "scrollTop",
			has = false;
	
		if ( el[ scroll ] > 0 ) {
			return true;
		}
	
		// TODO: determine which cases actually cause this to happen
		// if the element doesn't have the scroll set, see if it's possible to
		// set the scroll
		el[ scroll ] = 1;
		has = ( el[ scroll ] > 0 );
		el[ scroll ] = 0;
		return has;
	},
	
	// these are odd functions, fix the API or move into individual plugins
	isOverAxis: function( x, reference, size ) {
		//Determines when x coordinate is over "b" element axis
		return ( x > reference ) && ( x < ( reference + size ) );
	},
	isOver: function( y, x, top, left, height, width ) {
		//Determines when x, y coordinates is over "b" element
		return $.ui.isOverAxis( y, top, height ) && $.ui.isOverAxis( x, left, width );
	}
});

})( jQuery );


//End of order list swc/xpedx/js/jquery.ui.core.js

//Adding order list /swc/xpedx/js/jquery.ui.widget.js

/*!
 * jQuery UI Widget 1.8.5
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Widget
 */
(function( $, undefined ) {

// jQuery 1.4+
if ( $.cleanData ) {
	var _cleanData = $.cleanData;
	$.cleanData = function( elems ) {
		for ( var i = 0, elem; (elem = elems[i]) != null; i++ ) {
			$( elem ).triggerHandler( "remove" );
		}
		_cleanData( elems );
	};
} else {
	var _remove = $.fn.remove;
	$.fn.remove = function( selector, keepData ) {
		return this.each(function() {
			if ( !keepData ) {
				if ( !selector || $.filter( selector, [ this ] ).length ) {
					$( "*", this ).add( [ this ] ).each(function() {
						$( this ).triggerHandler( "remove" );
					});
				}
			}
			return _remove.call( $(this), selector, keepData );
		});
	};
}

$.widget = function( name, base, prototype ) {
	var namespace = name.split( "." )[ 0 ],
		fullName;
	name = name.split( "." )[ 1 ];
	fullName = namespace + "-" + name;

	if ( !prototype ) {
		prototype = base;
		base = $.Widget;
	}

	// create selector for plugin
	$.expr[ ":" ][ fullName ] = function( elem ) {
		return !!$.data( elem, name );
	};

	$[ namespace ] = $[ namespace ] || {};
	$[ namespace ][ name ] = function( options, element ) {
		// allow instantiation without initializing for simple inheritance
		if ( arguments.length ) {
			this._createWidget( options, element );
		}
	};

	var basePrototype = new base();
	// we need to make the options hash a property directly on the new instance
	// otherwise we'll modify the options hash on the prototype that we're
	// inheriting from
//	$.each( basePrototype, function( key, val ) {
//		if ( $.isPlainObject(val) ) {
//			basePrototype[ key ] = $.extend( {}, val );
//		}
//	});
	basePrototype.options = $.extend( true, {}, basePrototype.options );
	$[ namespace ][ name ].prototype = $.extend( true, basePrototype, {
		namespace: namespace,
		widgetName: name,
		widgetEventPrefix: $[ namespace ][ name ].prototype.widgetEventPrefix || name,
		widgetBaseClass: fullName
	}, prototype );

	$.widget.bridge( name, $[ namespace ][ name ] );
};

$.widget.bridge = function( name, object ) {
	$.fn[ name ] = function( options ) {
		var isMethodCall = typeof options === "string",
			args = Array.prototype.slice.call( arguments, 1 ),
			returnValue = this;

		// allow multiple hashes to be passed on init
		options = !isMethodCall && args.length ?
			$.extend.apply( null, [ true, options ].concat(args) ) :
			options;

		// prevent calls to internal methods
		if ( isMethodCall && options.substring( 0, 1 ) === "_" ) {
			return returnValue;
		}

		if ( isMethodCall ) {
			this.each(function() {
				var instance = $.data( this, name );
				if ( !instance ) {
					throw "cannot call methods on " + name + " prior to initialization; " +
						"attempted to call method '" + options + "'";
				}
				if ( !$.isFunction( instance[options] ) ) {
					throw "no such method '" + options + "' for " + name + " widget instance";
				}
				var methodValue = instance[ options ].apply( instance, args );
				if ( methodValue !== instance && methodValue !== undefined ) {
					returnValue = methodValue;
					return false;
				}
			});
		} else {
			this.each(function() {
				var instance = $.data( this, name );
				if ( instance ) {
					instance.option( options || {} )._init();
				} else {
					$.data( this, name, new object( options, this ) );
				}
			});
		}

		return returnValue;
	};
};

$.Widget = function( options, element ) {
	// allow instantiation without initializing for simple inheritance
	if ( arguments.length ) {
		this._createWidget( options, element );
	}
};

$.Widget.prototype = {
	widgetName: "widget",
	widgetEventPrefix: "",
	options: {
		disabled: false
	},
	_createWidget: function( options, element ) {
		// $.widget.bridge stores the plugin instance, but we do it anyway
		// so that it's stored even before the _create function runs
		$.data( element, this.widgetName, this );
		this.element = $( element );
		this.options = $.extend( true, {},
			this.options,
			$.metadata && $.metadata.get( element )[ this.widgetName ],
			options );

		var self = this;
		this.element.bind( "remove." + this.widgetName, function() {
			self.destroy();
		});

		this._create();
		this._init();
	},
	_create: function() {},
	_init: function() {},

	destroy: function() {
		this.element
			.unbind( "." + this.widgetName )
			.removeData( this.widgetName );
		this.widget()
			.unbind( "." + this.widgetName )
			.removeAttr( "aria-disabled" )
			.removeClass(
				this.widgetBaseClass + "-disabled " +
				"ui-state-disabled" );
	},

	widget: function() {
		return this.element;
	},

	option: function( key, value ) {
		var options = key,
			self = this;

		if ( arguments.length === 0 ) {
			// don't return a reference to the internal hash
			return $.extend( {}, self.options );
		}

		if  (typeof key === "string" ) {
			if ( value === undefined ) {
				return this.options[ key ];
			}
			options = {};
			options[ key ] = value;
		}

		$.each( options, function( key, value ) {
			self._setOption( key, value );
		});

		return self;
	},
	_setOption: function( key, value ) {
		this.options[ key ] = value;

		if ( key === "disabled" ) {
			this.widget()
				[ value ? "addClass" : "removeClass"](
					this.widgetBaseClass + "-disabled" + " " +
					"ui-state-disabled" )
				.attr( "aria-disabled", value );
		}

		return this;
	},

	enable: function() {
		return this._setOption( "disabled", false );
	},
	disable: function() {
		return this._setOption( "disabled", true );
	},

	_trigger: function( type, event, data ) {
		var callback = this.options[ type ];

		event = $.Event( event );
		event.type = ( type === this.widgetEventPrefix ?
			type :
			this.widgetEventPrefix + type ).toLowerCase();
		data = data || {};

		// copy original event properties over to the new event
		// this would happen if we could call $.event.fix instead of $.Event
		// but we don't have a way to force an event to be fixed multiple times
		if ( event.originalEvent ) {
			for ( var i = $.event.props.length, prop; i; ) {
				prop = $.event.props[ --i ];
				event[ prop ] = event.originalEvent[ prop ];
			}
		}

		this.element.trigger( event, data );

		return !( $.isFunction(callback) &&
			callback.call( this.element[0], event, data ) === false ||
			event.isDefaultPrevented() );
	}
};

})( jQuery );

//End of order list /swc/xpedx/js/jquery.ui.widget.js


//Adding order list /swc/xpedx/js/jquery.ui.datepicker.js

/*
 * jQuery UI Datepicker 1.8.5
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Datepicker
 *
 * Depends:
 *	jquery.ui.core.js
 */
(function( $, undefined ) {

$.extend($.ui, { datepicker: { version: "1.8.5" } });

var PROP_NAME = 'datepicker';
var dpuuid = new Date().getTime();

/* Date picker manager.
   Use the singleton instance of this class, $.datepicker, to interact with the date picker.
   Settings for (groups of) date pickers are maintained in an instance object,
   allowing multiple different settings on the same page. */

function Datepicker() {
	this.debug = false; // Change this to true to start debugging
	this._curInst = null; // The current instance in use
	this._keyEvent = false; // If the last event was a key event
	this._disabledInputs = []; // List of date picker inputs that have been disabled
	this._datepickerShowing = false; // True if the popup picker is showing , false if not
	this._inDialog = false; // True if showing within a "dialog", false if not
	this._mainDivId = 'ui-datepicker-div'; // The ID of the main datepicker division
	this._inlineClass = 'ui-datepicker-inline'; // The name of the inline marker class
	this._appendClass = 'ui-datepicker-append'; // The name of the append marker class
	this._triggerClass = 'ui-datepicker-trigger'; // The name of the trigger marker class
	this._dialogClass = 'ui-datepicker-dialog'; // The name of the dialog marker class
	this._disableClass = 'ui-datepicker-disabled'; // The name of the disabled covering marker class
	this._unselectableClass = 'ui-datepicker-unselectable'; // The name of the unselectable cell marker class
	this._currentClass = 'ui-datepicker-current-day'; // The name of the current day marker class
	this._dayOverClass = 'ui-datepicker-days-cell-over'; // The name of the day hover marker class
	this.regional = []; // Available regional settings, indexed by language code
	this.regional[''] = { // Default regional settings
		closeText: 'Done', // Display text for close link
		prevText: 'Prev', // Display text for previous month link
		nextText: 'Next', // Display text for next month link
		currentText: 'Today', // Display text for current month link
		monthNames: ['January','February','March','April','May','June',
			'July','August','September','October','November','December'], // Names of months for drop-down and formatting
		monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'], // For formatting
		dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'], // For formatting
		dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'], // For formatting
		dayNamesMin: ['Su','Mo','Tu','We','Th','Fr','Sa'], // Column headings for days starting at Sunday
		weekHeader: 'Wk', // Column header for week of the year
		dateFormat: 'mm/dd/yy', // See format options on parseDate
		firstDay: 0, // The first day of the week, Sun = 0, Mon = 1, ...
		isRTL: false, // True if right-to-left language, false if left-to-right
		showMonthAfterYear: false, // True if the year select precedes month, false for month then year
		yearSuffix: '' // Additional text to append to the year in the month headers
	};
	this._defaults = { // Global defaults for all the date picker instances
		showOn: 'focus', // 'focus' for popup on focus,
			// 'button' for trigger button, or 'both' for either
		showAnim: 'fadeIn', // Name of jQuery animation for popup
		showOptions: {}, // Options for enhanced animations
		defaultDate: null, // Used when field is blank: actual date,
			// +/-number for offset from today, null for today
		appendText: '', // Display text following the input box, e.g. showing the format
		buttonText: '', // Text for trigger button
		buttonImage: '', // URL for trigger button image
		buttonImageOnly: false, // True if the image appears alone, false if it appears on a button
		hideIfNoPrevNext: false, // True to hide next/previous month links
			// if not applicable, false to just disable them
		navigationAsDateFormat: false, // True if date formatting applied to prev/today/next links
		gotoCurrent: false, // True if today link goes back to current selection instead
		changeMonth: false, // True if month can be selected directly, false if only prev/next
		changeYear: false, // True if year can be selected directly, false if only prev/next
		yearRange: 'c-10:c+10', // Range of years to display in drop-down,
			// either relative to today's year (-nn:+nn), relative to currently displayed year
			// (c-nn:c+nn), absolute (nnnn:nnnn), or a combination of the above (nnnn:-n)
		showOtherMonths: false, // True to show dates in other months, false to leave blank
		selectOtherMonths: false, // True to allow selection of dates in other months, false for unselectable
		showWeek: false, // True to show week of the year, false to not show it
		calculateWeek: this.iso8601Week, // How to calculate the week of the year,
			// takes a Date and returns the number of the week for it
		shortYearCutoff: '+10', // Short year values < this are in the current century,
			// > this are in the previous century,
			// string value starting with '+' for current year + value
		minDate: null, // The earliest selectable date, or null for no limit
		maxDate: null, // The latest selectable date, or null for no limit
		duration: 'fast', // Duration of display/closure
		beforeShowDay: null, // Function that takes a date and returns an array with
			// [0] = true if selectable, false if not, [1] = custom CSS class name(s) or '',
			// [2] = cell title (optional), e.g. $.datepicker.noWeekends
		beforeShow: null, // Function that takes an input field and
			// returns a set of custom settings for the date picker
		onSelect: null, // Define a callback function when a date is selected
		onChangeMonthYear: null, // Define a callback function when the month or year is changed
		onClose: null, // Define a callback function when the datepicker is closed
		numberOfMonths: 1, // Number of months to show at a time
		showCurrentAtPos: 0, // The position in multipe months at which to show the current month (starting at 0)
		stepMonths: 1, // Number of months to step back/forward
		stepBigMonths: 12, // Number of months to step back/forward for the big links
		altField: '', // Selector for an alternate field to store selected dates into
		altFormat: '', // The date format to use for the alternate field
		constrainInput: true, // The input is constrained by the current date format
		showButtonPanel: false, // True to show button panel, false to not show it
		autoSize: false // True to size the input for the date format, false to leave as is
	};
	$.extend(this._defaults, this.regional['']);
	this.dpDiv = $('<div id="' + this._mainDivId + '" class="ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all ui-helper-hidden-accessible"></div>');
}

$.extend(Datepicker.prototype, {
	/* Class name added to elements to indicate already configured with a date picker. */
	markerClassName: 'hasDatepicker',

	/* Debug logging (if enabled). */
	log: function () {
		if (this.debug)
			console.log.apply('', arguments);
	},
	
	// TODO rename to "widget" when switching to widget factory
	_widgetDatepicker: function() {
		return this.dpDiv;
	},

	/* Override the default settings for all instances of the date picker.
	   @param  settings  object - the new settings to use as defaults (anonymous object)
	   @return the manager object */
	setDefaults: function(settings) {
		extendRemove(this._defaults, settings || {});
		return this;
	},

	/* Attach the date picker to a jQuery selection.
	   @param  target    element - the target input field or division or span
	   @param  settings  object - the new settings to use for this date picker instance (anonymous) */
	_attachDatepicker: function(target, settings) {
		// check for settings on the control itself - in namespace 'date:'
		var inlineSettings = null;
		for (var attrName in this._defaults) {
			var attrValue = target.getAttribute('date:' + attrName);
			if (attrValue) {
				inlineSettings = inlineSettings || {};
				try {
					inlineSettings[attrName] = eval(attrValue);
				} catch (err) {
					inlineSettings[attrName] = attrValue;
				}
			}
		}
		var nodeName = target.nodeName.toLowerCase();
		var inline = (nodeName == 'div' || nodeName == 'span');
		if (!target.id) {
			this.uuid += 1;
			target.id = 'dp' + this.uuid;
		}
		var inst = this._newInst($(target), inline);
		inst.settings = $.extend({}, settings || {}, inlineSettings || {});
		if (nodeName == 'input') {
			this._connectDatepicker(target, inst);
		} else if (inline) {
			this._inlineDatepicker(target, inst);
		}
	},

	/* Create a new instance object. */
	_newInst: function(target, inline) {
		var id = target[0].id.replace(/([^A-Za-z0-9_])/g, '\\\\$1'); // escape jQuery meta chars
		return {id: id, input: target, // associated target
			selectedDay: 0, selectedMonth: 0, selectedYear: 0, // current selection
			drawMonth: 0, drawYear: 0, // month being drawn
			inline: inline, // is datepicker inline or not
			dpDiv: (!inline ? this.dpDiv : // presentation div
			$('<div class="' + this._inlineClass + ' ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all"></div>'))};
	},

	/* Attach the date picker to an input field. */
	_connectDatepicker: function(target, inst) {
		var input = $(target);
		inst.append = $([]);
		inst.trigger = $([]);
		if (input.hasClass(this.markerClassName))
			return;
		this._attachments(input, inst);
		input.addClass(this.markerClassName).keydown(this._doKeyDown).
			keypress(this._doKeyPress).keyup(this._doKeyUp).
			bind("setData.datepicker", function(event, key, value) {
				inst.settings[key] = value;
			}).bind("getData.datepicker", function(event, key) {
				return this._get(inst, key);
			});
		this._autoSize(inst);
		$.data(target, PROP_NAME, inst);
	},

	/* Make attachments based on settings. */
	_attachments: function(input, inst) {
		var appendText = this._get(inst, 'appendText');
		var isRTL = this._get(inst, 'isRTL');
		if (inst.append)
			inst.append.remove();
		if (appendText) {
			inst.append = $('<span class="' + this._appendClass + '">' + appendText + '</span>');
			input[isRTL ? 'before' : 'after'](inst.append);
		}
		input.unbind('focus', this._showDatepicker);
		if (inst.trigger)
			inst.trigger.remove();
		var showOn = this._get(inst, 'showOn');
		if (showOn == 'focus' || showOn == 'both') // pop-up date picker when in the marked field
			input.focus(this._showDatepicker);
		if (showOn == 'button' || showOn == 'both') { // pop-up date picker when button clicked
			var buttonText = this._get(inst, 'buttonText');
			var buttonImage = this._get(inst, 'buttonImage');
			inst.trigger = $(this._get(inst, 'buttonImageOnly') ?
				$('<img/>').addClass(this._triggerClass).
					attr({ src: buttonImage, alt: buttonText, title: buttonText }) :
				$('<button type="button"></button>').addClass(this._triggerClass).
					html(buttonImage == '' ? buttonText : $('<img/>').attr(
					{ src:buttonImage, alt:buttonText, title:buttonText })));
			input[isRTL ? 'before' : 'after'](inst.trigger);
			inst.trigger.click(function() {
				if ($.datepicker._datepickerShowing && $.datepicker._lastInput == input[0])
					$.datepicker._hideDatepicker();
				else
					$.datepicker._showDatepicker(input[0]);
				return false;
			});
		}
	},

	/* Apply the maximum length for the date format. */
	_autoSize: function(inst) {
		if (this._get(inst, 'autoSize') && !inst.inline) {
			var date = new Date(2009, 12 - 1, 20); // Ensure double digits
			var dateFormat = this._get(inst, 'dateFormat');
			if (dateFormat.match(/[DM]/)) {
				var findMax = function(names) {
					var max = 0;
					var maxI = 0;
					for (var i = 0; i < names.length; i++) {
						if (names[i].length > max) {
							max = names[i].length;
							maxI = i;
						}
					}
					return maxI;
				};
				date.setMonth(findMax(this._get(inst, (dateFormat.match(/MM/) ?
					'monthNames' : 'monthNamesShort'))));
				date.setDate(findMax(this._get(inst, (dateFormat.match(/DD/) ?
					'dayNames' : 'dayNamesShort'))) + 20 - date.getDay());
			}
			inst.input.attr('size', this._formatDate(inst, date).length);
		}
	},

	/* Attach an inline date picker to a div. */
	_inlineDatepicker: function(target, inst) {
		var divSpan = $(target);
		if (divSpan.hasClass(this.markerClassName))
			return;
		divSpan.addClass(this.markerClassName).append(inst.dpDiv).
			bind("setData.datepicker", function(event, key, value){
				inst.settings[key] = value;
			}).bind("getData.datepicker", function(event, key){
				return this._get(inst, key);
			});
		$.data(target, PROP_NAME, inst);
		this._setDate(inst, this._getDefaultDate(inst), true);
		this._updateDatepicker(inst);
		this._updateAlternate(inst);
	},

	/* Pop-up the date picker in a "dialog" box.
	   @param  input     element - ignored
	   @param  date      string or Date - the initial date to display
	   @param  onSelect  function - the function to call when a date is selected
	   @param  settings  object - update the dialog date picker instance's settings (anonymous object)
	   @param  pos       int[2] - coordinates for the dialog's position within the screen or
	                     event - with x/y coordinates or
	                     leave empty for default (screen centre)
	   @return the manager object */
	_dialogDatepicker: function(input, date, onSelect, settings, pos) {
		var inst = this._dialogInst; // internal instance
		if (!inst) {
			this.uuid += 1;
			var id = 'dp' + this.uuid;
			this._dialogInput = $('<input type="text" id="' + id +
				'" style="position: absolute; top: -100px; width: 0px; z-index: -10;"/>');
			this._dialogInput.keydown(this._doKeyDown);
			$('body').append(this._dialogInput);
			inst = this._dialogInst = this._newInst(this._dialogInput, false);
			inst.settings = {};
			$.data(this._dialogInput[0], PROP_NAME, inst);
		}
		extendRemove(inst.settings, settings || {});
		date = (date && date.constructor == Date ? this._formatDate(inst, date) : date);
		this._dialogInput.val(date);

		this._pos = (pos ? (pos.length ? pos : [pos.pageX, pos.pageY]) : null);
		if (!this._pos) {
			var browserWidth = document.documentElement.clientWidth;
			var browserHeight = document.documentElement.clientHeight;
			var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
			var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
			this._pos = // should use actual width/height below
				[(browserWidth / 2) - 100 + scrollX, (browserHeight / 2) - 150 + scrollY];
		}

		// move input on screen for focus, but hidden behind dialog
		this._dialogInput.css('left', (this._pos[0] + 20) + 'px').css('top', this._pos[1] + 'px');
		inst.settings.onSelect = onSelect;
		this._inDialog = true;
		this.dpDiv.addClass(this._dialogClass);
		this._showDatepicker(this._dialogInput[0]);
		if ($.blockUI)
			$.blockUI(this.dpDiv);
		$.data(this._dialogInput[0], PROP_NAME, inst);
		return this;
	},

	/* Detach a datepicker from its control.
	   @param  target    element - the target input field or division or span */
	_destroyDatepicker: function(target) {
		var $target = $(target);
		var inst = $.data(target, PROP_NAME);
		if (!$target.hasClass(this.markerClassName)) {
			return;
		}
		var nodeName = target.nodeName.toLowerCase();
		$.removeData(target, PROP_NAME);
		if (nodeName == 'input') {
			inst.append.remove();
			inst.trigger.remove();
			$target.removeClass(this.markerClassName).
				unbind('focus', this._showDatepicker).
				unbind('keydown', this._doKeyDown).
				unbind('keypress', this._doKeyPress).
				unbind('keyup', this._doKeyUp);
		} else if (nodeName == 'div' || nodeName == 'span')
			$target.removeClass(this.markerClassName).empty();
	},

	/* Enable the date picker to a jQuery selection.
	   @param  target    element - the target input field or division or span */
	_enableDatepicker: function(target) {
		var $target = $(target);
		var inst = $.data(target, PROP_NAME);
		if (!$target.hasClass(this.markerClassName)) {
			return;
		}
		var nodeName = target.nodeName.toLowerCase();
		if (nodeName == 'input') {
			target.disabled = false;
			inst.trigger.filter('button').
				each(function() { this.disabled = false; }).end().
				filter('img').css({opacity: '1.0', cursor: ''});
		}
		else if (nodeName == 'div' || nodeName == 'span') {
			var inline = $target.children('.' + this._inlineClass);
			inline.children().removeClass('ui-state-disabled');
		}
		this._disabledInputs = $.map(this._disabledInputs,
			function(value) { return (value == target ? null : value); }); // delete entry
	},

	/* Disable the date picker to a jQuery selection.
	   @param  target    element - the target input field or division or span */
	_disableDatepicker: function(target) {
		var $target = $(target);
		var inst = $.data(target, PROP_NAME);
		if (!$target.hasClass(this.markerClassName)) {
			return;
		}
		var nodeName = target.nodeName.toLowerCase();
		if (nodeName == 'input') {
			target.disabled = true;
			inst.trigger.filter('button').
				each(function() { this.disabled = true; }).end().
				filter('img').css({opacity: '0.5', cursor: 'default'});
		}
		else if (nodeName == 'div' || nodeName == 'span') {
			var inline = $target.children('.' + this._inlineClass);
			inline.children().addClass('ui-state-disabled');
		}
		this._disabledInputs = $.map(this._disabledInputs,
			function(value) { return (value == target ? null : value); }); // delete entry
		this._disabledInputs[this._disabledInputs.length] = target;
	},

	/* Is the first field in a jQuery collection disabled as a datepicker?
	   @param  target    element - the target input field or division or span
	   @return boolean - true if disabled, false if enabled */
	_isDisabledDatepicker: function(target) {
		if (!target) {
			return false;
		}
		for (var i = 0; i < this._disabledInputs.length; i++) {
			if (this._disabledInputs[i] == target)
				return true;
		}
		return false;
	},

	/* Retrieve the instance data for the target control.
	   @param  target  element - the target input field or division or span
	   @return  object - the associated instance data
	   @throws  error if a jQuery problem getting data */
	_getInst: function(target) {
		try {
			return $.data(target, PROP_NAME);
		}
		catch (err) {
			throw 'Missing instance data for this datepicker';
		}
	},

	/* Update or retrieve the settings for a date picker attached to an input field or division.
	   @param  target  element - the target input field or division or span
	   @param  name    object - the new settings to update or
	                   string - the name of the setting to change or retrieve,
	                   when retrieving also 'all' for all instance settings or
	                   'defaults' for all global defaults
	   @param  value   any - the new value for the setting
	                   (omit if above is an object or to retrieve a value) */
	_optionDatepicker: function(target, name, value) {
		var inst = this._getInst(target);
		if (arguments.length == 2 && typeof name == 'string') {
			return (name == 'defaults' ? $.extend({}, $.datepicker._defaults) :
				(inst ? (name == 'all' ? $.extend({}, inst.settings) :
				this._get(inst, name)) : null));
		}
		var settings = name || {};
		if (typeof name == 'string') {
			settings = {};
			settings[name] = value;
		}
		if (inst) {
			if (this._curInst == inst) {
				this._hideDatepicker();
			}
			var date = this._getDateDatepicker(target, true);
			extendRemove(inst.settings, settings);
			this._attachments($(target), inst);
			this._autoSize(inst);
			this._setDateDatepicker(target, date);
			this._updateDatepicker(inst);
		}
	},

	// change method deprecated
	_changeDatepicker: function(target, name, value) {
		this._optionDatepicker(target, name, value);
	},

	/* Redraw the date picker attached to an input field or division.
	   @param  target  element - the target input field or division or span */
	_refreshDatepicker: function(target) {
		var inst = this._getInst(target);
		if (inst) {
			this._updateDatepicker(inst);
		}
	},

	/* Set the dates for a jQuery selection.
	   @param  target   element - the target input field or division or span
	   @param  date     Date - the new date */
	_setDateDatepicker: function(target, date) {
		var inst = this._getInst(target);
		if (inst) {
			this._setDate(inst, date);
			this._updateDatepicker(inst);
			this._updateAlternate(inst);
		}
	},

	/* Get the date(s) for the first entry in a jQuery selection.
	   @param  target     element - the target input field or division or span
	   @param  noDefault  boolean - true if no default date is to be used
	   @return Date - the current date */
	_getDateDatepicker: function(target, noDefault) {
		var inst = this._getInst(target);
		if (inst && !inst.inline)
			this._setDateFromField(inst, noDefault);
		return (inst ? this._getDate(inst) : null);
	},

	/* Handle keystrokes. */
	_doKeyDown: function(event) {
		var inst = $.datepicker._getInst(event.target);
		var handled = true;
		var isRTL = inst.dpDiv.is('.ui-datepicker-rtl');
		inst._keyEvent = true;
		if ($.datepicker._datepickerShowing)
			switch (event.keyCode) {
				case 9: $.datepicker._hideDatepicker();
						handled = false;
						break; // hide on tab out
				case 13: var sel = $('td.' + $.datepicker._dayOverClass, inst.dpDiv).
							add($('td.' + $.datepicker._currentClass, inst.dpDiv));
						if (sel[0])
							$.datepicker._selectDay(event.target, inst.selectedMonth, inst.selectedYear, sel[0]);
						else
							$.datepicker._hideDatepicker();
						return false; // don't submit the form
						break; // select the value on enter
				case 27: $.datepicker._hideDatepicker();
						break; // hide on escape
				case 33: $.datepicker._adjustDate(event.target, (event.ctrlKey ?
							-$.datepicker._get(inst, 'stepBigMonths') :
							-$.datepicker._get(inst, 'stepMonths')), 'M');
						break; // previous month/year on page up/+ ctrl
				case 34: $.datepicker._adjustDate(event.target, (event.ctrlKey ?
							+$.datepicker._get(inst, 'stepBigMonths') :
							+$.datepicker._get(inst, 'stepMonths')), 'M');
						break; // next month/year on page down/+ ctrl
				case 35: if (event.ctrlKey || event.metaKey) $.datepicker._clearDate(event.target);
						handled = event.ctrlKey || event.metaKey;
						break; // clear on ctrl or command +end
				case 36: if (event.ctrlKey || event.metaKey) $.datepicker._gotoToday(event.target);
						handled = event.ctrlKey || event.metaKey;
						break; // current on ctrl or command +home
				case 37: if (event.ctrlKey || event.metaKey) $.datepicker._adjustDate(event.target, (isRTL ? +1 : -1), 'D');
						handled = event.ctrlKey || event.metaKey;
						// -1 day on ctrl or command +left
						if (event.originalEvent.altKey) $.datepicker._adjustDate(event.target, (event.ctrlKey ?
									-$.datepicker._get(inst, 'stepBigMonths') :
									-$.datepicker._get(inst, 'stepMonths')), 'M');
						// next month/year on alt +left on Mac
						break;
				case 38: if (event.ctrlKey || event.metaKey) $.datepicker._adjustDate(event.target, -7, 'D');
						handled = event.ctrlKey || event.metaKey;
						break; // -1 week on ctrl or command +up
				case 39: if (event.ctrlKey || event.metaKey) $.datepicker._adjustDate(event.target, (isRTL ? -1 : +1), 'D');
						handled = event.ctrlKey || event.metaKey;
						// +1 day on ctrl or command +right
						if (event.originalEvent.altKey) $.datepicker._adjustDate(event.target, (event.ctrlKey ?
									+$.datepicker._get(inst, 'stepBigMonths') :
									+$.datepicker._get(inst, 'stepMonths')), 'M');
						// next month/year on alt +right
						break;
				case 40: if (event.ctrlKey || event.metaKey) $.datepicker._adjustDate(event.target, +7, 'D');
						handled = event.ctrlKey || event.metaKey;
						break; // +1 week on ctrl or command +down
				default: handled = false;
			}
		else if (event.keyCode == 36 && event.ctrlKey) // display the date picker on ctrl+home
			$.datepicker._showDatepicker(this);
		else {
			handled = false;
		}
		if (handled) {
			event.preventDefault();
			event.stopPropagation();
		}
	},

	/* Filter entered characters - based on date format. */
	_doKeyPress: function(event) {
		var inst = $.datepicker._getInst(event.target);
		if ($.datepicker._get(inst, 'constrainInput')) {
			var chars = $.datepicker._possibleChars($.datepicker._get(inst, 'dateFormat'));
			var chr = String.fromCharCode(event.charCode == undefined ? event.keyCode : event.charCode);
			return event.ctrlKey || (chr < ' ' || !chars || chars.indexOf(chr) > -1);
		}
	},

	/* Synchronise manual entry and field/alternate field. */
	_doKeyUp: function(event) {
		var inst = $.datepicker._getInst(event.target);
		if (inst.input.val() != inst.lastVal) {
			try {
				var date = $.datepicker.parseDate($.datepicker._get(inst, 'dateFormat'),
					(inst.input ? inst.input.val() : null),
					$.datepicker._getFormatConfig(inst));
				if (date) { // only if valid
					$.datepicker._setDateFromField(inst);
					$.datepicker._updateAlternate(inst);
					$.datepicker._updateDatepicker(inst);
				}
			}
			catch (event) {
				$.datepicker.log(event);
			}
		}
		return true;
	},

	/* Pop-up the date picker for a given input field.
	   @param  input  element - the input field attached to the date picker or
	                  event - if triggered by focus */
	_showDatepicker: function(input) {
		input = input.target || input;
		if (input.nodeName.toLowerCase() != 'input') // find from button/image trigger
			input = $('input', input.parentNode)[0];
		if ($.datepicker._isDisabledDatepicker(input) || $.datepicker._lastInput == input) // already here
			return;
		var inst = $.datepicker._getInst(input);
		if ($.datepicker._curInst && $.datepicker._curInst != inst) {
			$.datepicker._curInst.dpDiv.stop(true, true);
		}
		var beforeShow = $.datepicker._get(inst, 'beforeShow');
		extendRemove(inst.settings, (beforeShow ? beforeShow.apply(input, [input, inst]) : {}));
		inst.lastVal = null;
		$.datepicker._lastInput = input;
		$.datepicker._setDateFromField(inst);
		if ($.datepicker._inDialog) // hide cursor
			input.value = '';
		if (!$.datepicker._pos) { // position below input
			$.datepicker._pos = $.datepicker._findPos(input);
			$.datepicker._pos[1] += input.offsetHeight; // add the height
		}
		var isFixed = false;
		$(input).parents().each(function() {
			isFixed |= $(this).css('position') == 'fixed';
			return !isFixed;
		});
		if (isFixed && $.browser.opera) { // correction for Opera when fixed and scrolled
			$.datepicker._pos[0] -= document.documentElement.scrollLeft;
			$.datepicker._pos[1] -= document.documentElement.scrollTop;
		}
		var offset = {left: $.datepicker._pos[0], top: $.datepicker._pos[1]};
		$.datepicker._pos = null;
		// determine sizing offscreen
		inst.dpDiv.css({position: 'absolute', display: 'block', top: '-1000px'});
		$.datepicker._updateDatepicker(inst);
		// fix width for dynamic number of date pickers
		// and adjust position before showing
		offset = $.datepicker._checkOffset(inst, offset, isFixed);
		inst.dpDiv.css({position: ($.datepicker._inDialog && $.blockUI ?
			'static' : (isFixed ? 'fixed' : 'absolute')), display: 'none',
			left: offset.left + 'px', top: offset.top + 'px'});
		if (!inst.inline) {
			var showAnim = $.datepicker._get(inst, 'showAnim');
			var duration = $.datepicker._get(inst, 'duration');
			var postProcess = function() {
				$.datepicker._datepickerShowing = true;
				var borders = $.datepicker._getBorders(inst.dpDiv);
				inst.dpDiv.find('iframe.ui-datepicker-cover'). // IE6- only
					css({left: -borders[0], top: -borders[1],
						width: inst.dpDiv.outerWidth(), height: inst.dpDiv.outerHeight()});
			};
			inst.dpDiv.zIndex($(input).zIndex()+1);
			if ($.effects && $.effects[showAnim])
				inst.dpDiv.show(showAnim, $.datepicker._get(inst, 'showOptions'), duration, postProcess);
			else
				inst.dpDiv[showAnim || 'show']((showAnim ? duration : null), postProcess);
			if (!showAnim || !duration)
				postProcess();
			if (inst.input.is(':visible') && !inst.input.is(':disabled'))
				inst.input.focus();
			$.datepicker._curInst = inst;
		}
	},

	/* Generate the date picker content. */
	_updateDatepicker: function(inst) {
		var self = this;
		var borders = $.datepicker._getBorders(inst.dpDiv);
		inst.dpDiv.empty().append(this._generateHTML(inst))
			.find('iframe.ui-datepicker-cover') // IE6- only
				.css({left: -borders[0], top: -borders[1],
					width: inst.dpDiv.outerWidth(), height: inst.dpDiv.outerHeight()})
			.end()
			.find('button, .ui-datepicker-prev, .ui-datepicker-next, .ui-datepicker-calendar td a')
				.bind('mouseout', function(){
					$(this).removeClass('ui-state-hover');
					if(this.className.indexOf('ui-datepicker-prev') != -1) $(this).removeClass('ui-datepicker-prev-hover');
					if(this.className.indexOf('ui-datepicker-next') != -1) $(this).removeClass('ui-datepicker-next-hover');
				})
				.bind('mouseover', function(){
					if (!self._isDisabledDatepicker( inst.inline ? inst.dpDiv.parent()[0] : inst.input[0])) {
						$(this).parents('.ui-datepicker-calendar').find('a').removeClass('ui-state-hover');
						$(this).addClass('ui-state-hover');
						if(this.className.indexOf('ui-datepicker-prev') != -1) $(this).addClass('ui-datepicker-prev-hover');
						if(this.className.indexOf('ui-datepicker-next') != -1) $(this).addClass('ui-datepicker-next-hover');
					}
				})
			.end()
			.find('.' + this._dayOverClass + ' a')
				.trigger('mouseover')
			.end();
		var numMonths = this._getNumberOfMonths(inst);
		var cols = numMonths[1];
		var width = 17;
		if (cols > 1)
			inst.dpDiv.addClass('ui-datepicker-multi-' + cols).css('width', (width * cols) + 'em');
		else
			inst.dpDiv.removeClass('ui-datepicker-multi-2 ui-datepicker-multi-3 ui-datepicker-multi-4').width('');
		inst.dpDiv[(numMonths[0] != 1 || numMonths[1] != 1 ? 'add' : 'remove') +
			'Class']('ui-datepicker-multi');
		inst.dpDiv[(this._get(inst, 'isRTL') ? 'add' : 'remove') +
			'Class']('ui-datepicker-rtl');
		if (inst == $.datepicker._curInst && $.datepicker._datepickerShowing && inst.input &&
				inst.input.is(':visible') && !inst.input.is(':disabled'))
			inst.input.focus();
	},

	/* Retrieve the size of left and top borders for an element.
	   @param  elem  (jQuery object) the element of interest
	   @return  (number[2]) the left and top borders */
	_getBorders: function(elem) {
		var convert = function(value) {
			return {thin: 1, medium: 2, thick: 3}[value] || value;
		};
		return [parseFloat(convert(elem.css('border-left-width'))),
			parseFloat(convert(elem.css('border-top-width')))];
	},

	/* Check positioning to remain on screen. */
	_checkOffset: function(inst, offset, isFixed) {
		var dpWidth = inst.dpDiv.outerWidth();
		var dpHeight = inst.dpDiv.outerHeight();
		var inputWidth = inst.input ? inst.input.outerWidth() : 0;
		var inputHeight = inst.input ? inst.input.outerHeight() : 0;
		var viewWidth = document.documentElement.clientWidth + $(document).scrollLeft();
		var viewHeight = document.documentElement.clientHeight + $(document).scrollTop();

		offset.left -= (this._get(inst, 'isRTL') ? (dpWidth - inputWidth) : 0);
		offset.left -= (isFixed && offset.left == inst.input.offset().left) ? $(document).scrollLeft() : 0;
		offset.top -= (isFixed && offset.top == (inst.input.offset().top + inputHeight)) ? $(document).scrollTop() : 0;

		// now check if datepicker is showing outside window viewport - move to a better place if so.
		offset.left -= Math.min(offset.left, (offset.left + dpWidth > viewWidth && viewWidth > dpWidth) ?
			Math.abs(offset.left + dpWidth - viewWidth) : 0);
		offset.top -= Math.min(offset.top, (offset.top + dpHeight > viewHeight && viewHeight > dpHeight) ?
			Math.abs(dpHeight + inputHeight) : 0);

		return offset;
	},

	/* Find an object's position on the screen. */
	_findPos: function(obj) {
		var inst = this._getInst(obj);
		var isRTL = this._get(inst, 'isRTL');
        while (obj && (obj.type == 'hidden' || obj.nodeType != 1)) {
            obj = obj[isRTL ? 'previousSibling' : 'nextSibling'];
        }
        var position = $(obj).offset();
	    return [position.left, position.top];
	},

	/* Hide the date picker from view.
	   @param  input  element - the input field attached to the date picker */
	_hideDatepicker: function(input) {
		var inst = this._curInst;
		if (!inst || (input && inst != $.data(input, PROP_NAME)))
			return;
		if (this._datepickerShowing) {
			var showAnim = this._get(inst, 'showAnim');
			var duration = this._get(inst, 'duration');
			var postProcess = function() {
				$.datepicker._tidyDialog(inst);
				this._curInst = null;
			};
			if ($.effects && $.effects[showAnim])
				inst.dpDiv.hide(showAnim, $.datepicker._get(inst, 'showOptions'), duration, postProcess);
			else
				inst.dpDiv[(showAnim == 'slideDown' ? 'slideUp' :
					(showAnim == 'fadeIn' ? 'fadeOut' : 'hide'))]((showAnim ? duration : null), postProcess);
			if (!showAnim)
				postProcess();
			var onClose = this._get(inst, 'onClose');
			if (onClose)
				onClose.apply((inst.input ? inst.input[0] : null),
					[(inst.input ? inst.input.val() : ''), inst]);  // trigger custom callback
			this._datepickerShowing = false;
			this._lastInput = null;
			if (this._inDialog) {
				this._dialogInput.css({ position: 'absolute', left: '0', top: '-100px' });
				if ($.blockUI) {
					$.unblockUI();
					$('body').append(this.dpDiv);
				}
			}
			this._inDialog = false;
		}
	},

	/* Tidy up after a dialog display. */
	_tidyDialog: function(inst) {
		inst.dpDiv.removeClass(this._dialogClass).unbind('.ui-datepicker-calendar');
	},

	/* Close date picker if clicked elsewhere. */
	_checkExternalClick: function(event) {
		if (!$.datepicker._curInst)
			return;
		var $target = $(event.target);
		if ($target[0].id != $.datepicker._mainDivId &&
				$target.parents('#' + $.datepicker._mainDivId).length == 0 &&
				!$target.hasClass($.datepicker.markerClassName) &&
				!$target.hasClass($.datepicker._triggerClass) &&
				$.datepicker._datepickerShowing && !($.datepicker._inDialog && $.blockUI))
			$.datepicker._hideDatepicker();
	},

	/* Adjust one of the date sub-fields. */
	_adjustDate: function(id, offset, period) {
		var target = $(id);
		var inst = this._getInst(target[0]);
		if (this._isDisabledDatepicker(target[0])) {
			return;
		}
		this._adjustInstDate(inst, offset +
			(period == 'M' ? this._get(inst, 'showCurrentAtPos') : 0), // undo positioning
			period);
		this._updateDatepicker(inst);
	},

	/* Action for current link. */
	_gotoToday: function(id) {
		var target = $(id);
		var inst = this._getInst(target[0]);
		if (this._get(inst, 'gotoCurrent') && inst.currentDay) {
			inst.selectedDay = inst.currentDay;
			inst.drawMonth = inst.selectedMonth = inst.currentMonth;
			inst.drawYear = inst.selectedYear = inst.currentYear;
		}
		else {
			var date = new Date();
			inst.selectedDay = date.getDate();
			inst.drawMonth = inst.selectedMonth = date.getMonth();
			inst.drawYear = inst.selectedYear = date.getFullYear();
		}
		this._notifyChange(inst);
		this._adjustDate(target);
	},

	/* Action for selecting a new month/year. */
	_selectMonthYear: function(id, select, period) {
		var target = $(id);
		var inst = this._getInst(target[0]);
		inst._selectingMonthYear = false;
		inst['selected' + (period == 'M' ? 'Month' : 'Year')] =
		inst['draw' + (period == 'M' ? 'Month' : 'Year')] =
			parseInt(select.options[select.selectedIndex].value,10);
		this._notifyChange(inst);
		this._adjustDate(target);
	},

	/* Restore input focus after not changing month/year. */
	_clickMonthYear: function(id) {
		var target = $(id);
		var inst = this._getInst(target[0]);
		if (inst.input && inst._selectingMonthYear) {
			setTimeout(function() {
				inst.input.focus();
			}, 0);
		}
		inst._selectingMonthYear = !inst._selectingMonthYear;
	},

	/* Action for selecting a day. */
	_selectDay: function(id, month, year, td) {
		var target = $(id);
		if ($(td).hasClass(this._unselectableClass) || this._isDisabledDatepicker(target[0])) {
			return;
		}
		var inst = this._getInst(target[0]);
		inst.selectedDay = inst.currentDay = $('a', td).html();
		inst.selectedMonth = inst.currentMonth = month;
		inst.selectedYear = inst.currentYear = year;
		this._selectDate(id, this._formatDate(inst,
			inst.currentDay, inst.currentMonth, inst.currentYear));
	},

	/* Erase the input field and hide the date picker. */
	_clearDate: function(id) {
		var target = $(id);
		var inst = this._getInst(target[0]);
		this._selectDate(target, '');
	},

	/* Update the input field with the selected date. */
	_selectDate: function(id, dateStr) {
		var target = $(id);
		var inst = this._getInst(target[0]);
		dateStr = (dateStr != null ? dateStr : this._formatDate(inst));
		if (inst.input)
			inst.input.val(dateStr);
		this._updateAlternate(inst);
		var onSelect = this._get(inst, 'onSelect');
		if (onSelect)
			onSelect.apply((inst.input ? inst.input[0] : null), [dateStr, inst]);  // trigger custom callback
		else if (inst.input)
			inst.input.trigger('change'); // fire the change event
		if (inst.inline)
			this._updateDatepicker(inst);
		else {
			this._hideDatepicker();
			this._lastInput = inst.input[0];
			if (typeof(inst.input[0]) != 'object')
				inst.input.focus(); // restore focus
			this._lastInput = null;
		}
	},

	/* Update any alternate field to synchronise with the main field. */
	_updateAlternate: function(inst) {
		var altField = this._get(inst, 'altField');
		if (altField) { // update alternate field too
			var altFormat = this._get(inst, 'altFormat') || this._get(inst, 'dateFormat');
			var date = this._getDate(inst);
			var dateStr = this.formatDate(altFormat, date, this._getFormatConfig(inst));
			$(altField).each(function() { $(this).val(dateStr); });
		}
	},

	/* Set as beforeShowDay function to prevent selection of weekends.
	   @param  date  Date - the date to customise
	   @return [boolean, string] - is this date selectable?, what is its CSS class? */
	noWeekends: function(date) {
		var day = date.getDay();
		return [(day > 0 && day < 6), ''];
	},

	/* Set as calculateWeek to determine the week of the year based on the ISO 8601 definition.
	   @param  date  Date - the date to get the week for
	   @return  number - the number of the week within the year that contains this date */
	iso8601Week: function(date) {
		var checkDate = new Date(date.getTime());
		// Find Thursday of this week starting on Monday
		checkDate.setDate(checkDate.getDate() + 4 - (checkDate.getDay() || 7));
		var time = checkDate.getTime();
		checkDate.setMonth(0); // Compare with Jan 1
		checkDate.setDate(1);
		return Math.floor(Math.round((time - checkDate) / 86400000) / 7) + 1;
	},

	/* Parse a string value into a date object.
	   See formatDate below for the possible formats.

	   @param  format    string - the expected format of the date
	   @param  value     string - the date in the above format
	   @param  settings  Object - attributes include:
	                     shortYearCutoff  number - the cutoff year for determining the century (optional)
	                     dayNamesShort    string[7] - abbreviated names of the days from Sunday (optional)
	                     dayNames         string[7] - names of the days from Sunday (optional)
	                     monthNamesShort  string[12] - abbreviated names of the months (optional)
	                     monthNames       string[12] - names of the months (optional)
	   @return  Date - the extracted date value or null if value is blank */
	parseDate: function (format, value, settings) {
		if (format == null || value == null)
			throw 'Invalid arguments';
		value = (typeof value == 'object' ? value.toString() : value + '');
		if (value == '')
			return null;
		var shortYearCutoff = (settings ? settings.shortYearCutoff : null) || this._defaults.shortYearCutoff;
		var dayNamesShort = (settings ? settings.dayNamesShort : null) || this._defaults.dayNamesShort;
		var dayNames = (settings ? settings.dayNames : null) || this._defaults.dayNames;
		var monthNamesShort = (settings ? settings.monthNamesShort : null) || this._defaults.monthNamesShort;
		var monthNames = (settings ? settings.monthNames : null) || this._defaults.monthNames;
		var year = -1;
		var month = -1;
		var day = -1;
		var doy = -1;
		var literal = false;
		// Check whether a format character is doubled
		var lookAhead = function(match) {
			var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) == match);
			if (matches)
				iFormat++;
			return matches;
		};
		// Extract a number from the string value
		var getNumber = function(match) {
			lookAhead(match);
			var size = (match == '@' ? 14 : (match == '!' ? 20 :
				(match == 'y' ? 4 : (match == 'o' ? 3 : 2))));
			var digits = new RegExp('^\\d{1,' + size + '}');
			var num = value.substring(iValue).match(digits);
			if (!num)
				throw 'Missing number at position ' + iValue;
			iValue += num[0].length;
			return parseInt(num[0], 10);
		};
		// Extract a name from the string value and convert to an index
		var getName = function(match, shortNames, longNames) {
			var names = (lookAhead(match) ? longNames : shortNames);
			for (var i = 0; i < names.length; i++) {
				if (value.substr(iValue, names[i].length).toLowerCase() == names[i].toLowerCase()) {
					iValue += names[i].length;
					return i + 1;
				}
			}
			throw 'Unknown name at position ' + iValue;
		};
		// Confirm that a literal character matches the string value
		var checkLiteral = function() {
			if (value.charAt(iValue) != format.charAt(iFormat))
				throw 'Unexpected literal at position ' + iValue;
			iValue++;
		};
		var iValue = 0;
		for (var iFormat = 0; iFormat < format.length; iFormat++) {
			if (literal)
				if (format.charAt(iFormat) == "'" && !lookAhead("'"))
					literal = false;
				else
					checkLiteral();
			else
				switch (format.charAt(iFormat)) {
					case 'd':
						day = getNumber('d');
						break;
					case 'D':
						getName('D', dayNamesShort, dayNames);
						break;
					case 'o':
						doy = getNumber('o');
						break;
					case 'm':
						month = getNumber('m');
						break;
					case 'M':
						month = getName('M', monthNamesShort, monthNames);
						break;
					case 'y':
						year = getNumber('y');
						break;
					case '@':
						var date = new Date(getNumber('@'));
						year = date.getFullYear();
						month = date.getMonth() + 1;
						day = date.getDate();
						break;
					case '!':
						var date = new Date((getNumber('!') - this._ticksTo1970) / 10000);
						year = date.getFullYear();
						month = date.getMonth() + 1;
						day = date.getDate();
						break;
					case "'":
						if (lookAhead("'"))
							checkLiteral();
						else
							literal = true;
						break;
					default:
						checkLiteral();
				}
		}
		if (year == -1)
			year = new Date().getFullYear();
		else if (year < 100)
			year += new Date().getFullYear() - new Date().getFullYear() % 100 +
				(year <= shortYearCutoff ? 0 : -100);
		if (doy > -1) {
			month = 1;
			day = doy;
			do {
				var dim = this._getDaysInMonth(year, month - 1);
				if (day <= dim)
					break;
				month++;
				day -= dim;
			} while (true);
		}
		var date = this._daylightSavingAdjust(new Date(year, month - 1, day));
		if (date.getFullYear() != year || date.getMonth() + 1 != month || date.getDate() != day)
			throw 'Invalid date'; // E.g. 31/02/*
		return date;
	},

	/* Standard date formats. */
	ATOM: 'yy-mm-dd', // RFC 3339 (ISO 8601)
	COOKIE: 'D, dd M yy',
	ISO_8601: 'yy-mm-dd',
	RFC_822: 'D, d M y',
	RFC_850: 'DD, dd-M-y',
	RFC_1036: 'D, d M y',
	RFC_1123: 'D, d M yy',
	RFC_2822: 'D, d M yy',
	RSS: 'D, d M y', // RFC 822
	TICKS: '!',
	TIMESTAMP: '@',
	W3C: 'yy-mm-dd', // ISO 8601

	_ticksTo1970: (((1970 - 1) * 365 + Math.floor(1970 / 4) - Math.floor(1970 / 100) +
		Math.floor(1970 / 400)) * 24 * 60 * 60 * 10000000),

	/* Format a date object into a string value.
	   The format can be combinations of the following:
	   d  - day of month (no leading zero)
	   dd - day of month (two digit)
	   o  - day of year (no leading zeros)
	   oo - day of year (three digit)
	   D  - day name short
	   DD - day name long
	   m  - month of year (no leading zero)
	   mm - month of year (two digit)
	   M  - month name short
	   MM - month name long
	   y  - year (two digit)
	   yy - year (four digit)
	   @ - Unix timestamp (ms since 01/01/1970)
	   ! - Windows ticks (100ns since 01/01/0001)
	   '...' - literal text
	   '' - single quote

	   @param  format    string - the desired format of the date
	   @param  date      Date - the date value to format
	   @param  settings  Object - attributes include:
	                     dayNamesShort    string[7] - abbreviated names of the days from Sunday (optional)
	                     dayNames         string[7] - names of the days from Sunday (optional)
	                     monthNamesShort  string[12] - abbreviated names of the months (optional)
	                     monthNames       string[12] - names of the months (optional)
	   @return  string - the date in the above format */
	formatDate: function (format, date, settings) {
		if (!date)
			return '';
		var dayNamesShort = (settings ? settings.dayNamesShort : null) || this._defaults.dayNamesShort;
		var dayNames = (settings ? settings.dayNames : null) || this._defaults.dayNames;
		var monthNamesShort = (settings ? settings.monthNamesShort : null) || this._defaults.monthNamesShort;
		var monthNames = (settings ? settings.monthNames : null) || this._defaults.monthNames;
		// Check whether a format character is doubled
		var lookAhead = function(match) {
			var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) == match);
			if (matches)
				iFormat++;
			return matches;
		};
		// Format a number, with leading zero if necessary
		var formatNumber = function(match, value, len) {
			var num = '' + value;
			if (lookAhead(match))
				while (num.length < len)
					num = '0' + num;
			return num;
		};
		// Format a name, short or long as requested
		var formatName = function(match, value, shortNames, longNames) {
			return (lookAhead(match) ? longNames[value] : shortNames[value]);
		};
		var output = '';
		var literal = false;
		if (date)
			for (var iFormat = 0; iFormat < format.length; iFormat++) {
				if (literal)
					if (format.charAt(iFormat) == "'" && !lookAhead("'"))
						literal = false;
					else
						output += format.charAt(iFormat);
				else
					switch (format.charAt(iFormat)) {
						case 'd':
							output += formatNumber('d', date.getDate(), 2);
							break;
						case 'D':
							output += formatName('D', date.getDay(), dayNamesShort, dayNames);
							break;
						case 'o':
							output += formatNumber('o',
								(date.getTime() - new Date(date.getFullYear(), 0, 0).getTime()) / 86400000, 3);
							break;
						case 'm':
							output += formatNumber('m', date.getMonth() + 1, 2);
							break;
						case 'M':
							output += formatName('M', date.getMonth(), monthNamesShort, monthNames);
							break;
						case 'y':
							output += (lookAhead('y') ? date.getFullYear() :
								(date.getYear() % 100 < 10 ? '0' : '') + date.getYear() % 100);
							break;
						case '@':
							output += date.getTime();
							break;
						case '!':
							output += date.getTime() * 10000 + this._ticksTo1970;
							break;
						case "'":
							if (lookAhead("'"))
								output += "'";
							else
								literal = true;
							break;
						default:
							output += format.charAt(iFormat);
					}
			}
		return output;
	},

	/* Extract all possible characters from the date format. */
	_possibleChars: function (format) {
		var chars = '';
		var literal = false;
		// Check whether a format character is doubled
		var lookAhead = function(match) {
			var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) == match);
			if (matches)
				iFormat++;
			return matches;
		};
		for (var iFormat = 0; iFormat < format.length; iFormat++)
			if (literal)
				if (format.charAt(iFormat) == "'" && !lookAhead("'"))
					literal = false;
				else
					chars += format.charAt(iFormat);
			else
				switch (format.charAt(iFormat)) {
					case 'd': case 'm': case 'y': case '@':
						chars += '0123456789';
						break;
					case 'D': case 'M':
						return null; // Accept anything
					case "'":
						if (lookAhead("'"))
							chars += "'";
						else
							literal = true;
						break;
					default:
						chars += format.charAt(iFormat);
				}
		return chars;
	},

	/* Get a setting value, defaulting if necessary. */
	_get: function(inst, name) {
		return inst.settings[name] !== undefined ?
			inst.settings[name] : this._defaults[name];
	},

	/* Parse existing date and initialise date picker. */
	_setDateFromField: function(inst, noDefault) {
		if (inst.input.val() == inst.lastVal) {
			return;
		}
		var dateFormat = this._get(inst, 'dateFormat');
		var dates = inst.lastVal = inst.input ? inst.input.val() : null;
		var date, defaultDate;
		date = defaultDate = this._getDefaultDate(inst);
		var settings = this._getFormatConfig(inst);
		try {
			date = this.parseDate(dateFormat, dates, settings) || defaultDate;
		} catch (event) {
			this.log(event);
			dates = (noDefault ? '' : dates);
		}
		inst.selectedDay = date.getDate();
		inst.drawMonth = inst.selectedMonth = date.getMonth();
		inst.drawYear = inst.selectedYear = date.getFullYear();
		inst.currentDay = (dates ? date.getDate() : 0);
		inst.currentMonth = (dates ? date.getMonth() : 0);
		inst.currentYear = (dates ? date.getFullYear() : 0);
		this._adjustInstDate(inst);
	},

	/* Retrieve the default date shown on opening. */
	_getDefaultDate: function(inst) {
		return this._restrictMinMax(inst,
			this._determineDate(inst, this._get(inst, 'defaultDate'), new Date()));
	},

	/* A date may be specified as an exact value or a relative one. */
	_determineDate: function(inst, date, defaultDate) {
		var offsetNumeric = function(offset) {
			var date = new Date();
			date.setDate(date.getDate() + offset);
			return date;
		};
		var offsetString = function(offset) {
			try {
				return $.datepicker.parseDate($.datepicker._get(inst, 'dateFormat'),
					offset, $.datepicker._getFormatConfig(inst));
			}
			catch (e) {
				// Ignore
			}
			var date = (offset.toLowerCase().match(/^c/) ?
				$.datepicker._getDate(inst) : null) || new Date();
			var year = date.getFullYear();
			var month = date.getMonth();
			var day = date.getDate();
			var pattern = /([+-]?[0-9]+)\s*(d|D|w|W|m|M|y|Y)?/g;
			var matches = pattern.exec(offset);
			while (matches) {
				switch (matches[2] || 'd') {
					case 'd' : case 'D' :
						day += parseInt(matches[1],10); break;
					case 'w' : case 'W' :
						day += parseInt(matches[1],10) * 7; break;
					case 'm' : case 'M' :
						month += parseInt(matches[1],10);
						day = Math.min(day, $.datepicker._getDaysInMonth(year, month));
						break;
					case 'y': case 'Y' :
						year += parseInt(matches[1],10);
						day = Math.min(day, $.datepicker._getDaysInMonth(year, month));
						break;
				}
				matches = pattern.exec(offset);
			}
			return new Date(year, month, day);
		};
		date = (date == null ? defaultDate : (typeof date == 'string' ? offsetString(date) :
			(typeof date == 'number' ? (isNaN(date) ? defaultDate : offsetNumeric(date)) : date)));
		date = (date && date.toString() == 'Invalid Date' ? defaultDate : date);
		if (date) {
			date.setHours(0);
			date.setMinutes(0);
			date.setSeconds(0);
			date.setMilliseconds(0);
		}
		return this._daylightSavingAdjust(date);
	},

	/* Handle switch to/from daylight saving.
	   Hours may be non-zero on daylight saving cut-over:
	   > 12 when midnight changeover, but then cannot generate
	   midnight datetime, so jump to 1AM, otherwise reset.
	   @param  date  (Date) the date to check
	   @return  (Date) the corrected date */
	_daylightSavingAdjust: function(date) {
		if (!date) return null;
		date.setHours(date.getHours() > 12 ? date.getHours() + 2 : 0);
		return date;
	},

	/* Set the date(s) directly. */
	_setDate: function(inst, date, noChange) {
		var clear = !(date);
		var origMonth = inst.selectedMonth;
		var origYear = inst.selectedYear;
		date = this._restrictMinMax(inst, this._determineDate(inst, date, new Date()));
		inst.selectedDay = inst.currentDay = date.getDate();
		inst.drawMonth = inst.selectedMonth = inst.currentMonth = date.getMonth();
		inst.drawYear = inst.selectedYear = inst.currentYear = date.getFullYear();
		if ((origMonth != inst.selectedMonth || origYear != inst.selectedYear) && !noChange)
			this._notifyChange(inst);
		this._adjustInstDate(inst);
		if (inst.input) {
			inst.input.val(clear ? '' : this._formatDate(inst));
		}
	},

	/* Retrieve the date(s) directly. */
	_getDate: function(inst) {
		var startDate = (!inst.currentYear || (inst.input && inst.input.val() == '') ? null :
			this._daylightSavingAdjust(new Date(
			inst.currentYear, inst.currentMonth, inst.currentDay)));
			return startDate;
	},

	/* Generate the HTML for the current state of the date picker. */
	_generateHTML: function(inst) {
		var today = new Date();
		today = this._daylightSavingAdjust(
			new Date(today.getFullYear(), today.getMonth(), today.getDate())); // clear time
		var isRTL = this._get(inst, 'isRTL');
		var showButtonPanel = this._get(inst, 'showButtonPanel');
		var hideIfNoPrevNext = this._get(inst, 'hideIfNoPrevNext');
		var navigationAsDateFormat = this._get(inst, 'navigationAsDateFormat');
		var numMonths = this._getNumberOfMonths(inst);
		var showCurrentAtPos = this._get(inst, 'showCurrentAtPos');
		var stepMonths = this._get(inst, 'stepMonths');
		var isMultiMonth = (numMonths[0] != 1 || numMonths[1] != 1);
		var currentDate = this._daylightSavingAdjust((!inst.currentDay ? new Date(9999, 9, 9) :
			new Date(inst.currentYear, inst.currentMonth, inst.currentDay)));
		var minDate = this._getMinMaxDate(inst, 'min');
		var maxDate = this._getMinMaxDate(inst, 'max');
		var drawMonth = inst.drawMonth - showCurrentAtPos;
		var drawYear = inst.drawYear;
		if (drawMonth < 0) {
			drawMonth += 12;
			drawYear--;
		}
		if (maxDate) {
			var maxDraw = this._daylightSavingAdjust(new Date(maxDate.getFullYear(),
				maxDate.getMonth() - (numMonths[0] * numMonths[1]) + 1, maxDate.getDate()));
			maxDraw = (minDate && maxDraw < minDate ? minDate : maxDraw);
			while (this._daylightSavingAdjust(new Date(drawYear, drawMonth, 1)) > maxDraw) {
				drawMonth--;
				if (drawMonth < 0) {
					drawMonth = 11;
					drawYear--;
				}
			}
		}
		inst.drawMonth = drawMonth;
		inst.drawYear = drawYear;
		var prevText = this._get(inst, 'prevText');
		prevText = (!navigationAsDateFormat ? prevText : this.formatDate(prevText,
			this._daylightSavingAdjust(new Date(drawYear, drawMonth - stepMonths, 1)),
			this._getFormatConfig(inst)));
		var prev = (this._canAdjustMonth(inst, -1, drawYear, drawMonth) ?
			'<a class="ui-datepicker-prev ui-corner-all" onclick="DP_jQuery_' + dpuuid +
			'.datepicker._adjustDate(\'#' + inst.id + '\', -' + stepMonths + ', \'M\');"' +
			' title="' + prevText + '"><span class="ui-icon ui-icon-circle-triangle-' + ( isRTL ? 'e' : 'w') + '">' + prevText + '</span></a>' :
			(hideIfNoPrevNext ? '' : '<a class="ui-datepicker-prev ui-corner-all ui-state-disabled" title="'+ prevText +'"><span class="ui-icon ui-icon-circle-triangle-' + ( isRTL ? 'e' : 'w') + '">' + prevText + '</span></a>'));
		var nextText = this._get(inst, 'nextText');
		nextText = (!navigationAsDateFormat ? nextText : this.formatDate(nextText,
			this._daylightSavingAdjust(new Date(drawYear, drawMonth + stepMonths, 1)),
			this._getFormatConfig(inst)));
		var next = (this._canAdjustMonth(inst, +1, drawYear, drawMonth) ?
			'<a class="ui-datepicker-next ui-corner-all" onclick="DP_jQuery_' + dpuuid +
			'.datepicker._adjustDate(\'#' + inst.id + '\', +' + stepMonths + ', \'M\');"' +
			' title="' + nextText + '"><span class="ui-icon ui-icon-circle-triangle-' + ( isRTL ? 'w' : 'e') + '">' + nextText + '</span></a>' :
			(hideIfNoPrevNext ? '' : '<a class="ui-datepicker-next ui-corner-all ui-state-disabled" title="'+ nextText + '"><span class="ui-icon ui-icon-circle-triangle-' + ( isRTL ? 'w' : 'e') + '">' + nextText + '</span></a>'));
		var currentText = this._get(inst, 'currentText');
		var gotoDate = (this._get(inst, 'gotoCurrent') && inst.currentDay ? currentDate : today);
		currentText = (!navigationAsDateFormat ? currentText :
			this.formatDate(currentText, gotoDate, this._getFormatConfig(inst)));
		var controls = (!inst.inline ? '<button type="button" class="ui-datepicker-close ui-state-default ui-priority-primary ui-corner-all" onclick="DP_jQuery_' + dpuuid +
			'.datepicker._hideDatepicker();">' + this._get(inst, 'closeText') + '</button>' : '');
		var buttonPanel = (showButtonPanel) ? '<div class="ui-datepicker-buttonpane ui-widget-content">' + (isRTL ? controls : '') +
			(this._isInRange(inst, gotoDate) ? '<button type="button" class="ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all" onclick="DP_jQuery_' + dpuuid +
			'.datepicker._gotoToday(\'#' + inst.id + '\');"' +
			'>' + currentText + '</button>' : '') + (isRTL ? '' : controls) + '</div>' : '';
		var firstDay = parseInt(this._get(inst, 'firstDay'),10);
		firstDay = (isNaN(firstDay) ? 0 : firstDay);
		var showWeek = this._get(inst, 'showWeek');
		var dayNames = this._get(inst, 'dayNames');
		var dayNamesShort = this._get(inst, 'dayNamesShort');
		var dayNamesMin = this._get(inst, 'dayNamesMin');
		var monthNames = this._get(inst, 'monthNames');
		var monthNamesShort = this._get(inst, 'monthNamesShort');
		var beforeShowDay = this._get(inst, 'beforeShowDay');
		var showOtherMonths = this._get(inst, 'showOtherMonths');
		var selectOtherMonths = this._get(inst, 'selectOtherMonths');
		var calculateWeek = this._get(inst, 'calculateWeek') || this.iso8601Week;
		var defaultDate = this._getDefaultDate(inst);
		var html = '';
		for (var row = 0; row < numMonths[0]; row++) {
			var group = '';
			for (var col = 0; col < numMonths[1]; col++) {
				var selectedDate = this._daylightSavingAdjust(new Date(drawYear, drawMonth, inst.selectedDay));
				var cornerClass = ' ui-corner-all';
				var calender = '';
				if (isMultiMonth) {
					calender += '<div class="ui-datepicker-group';
					if (numMonths[1] > 1)
						switch (col) {
							case 0: calender += ' ui-datepicker-group-first';
								cornerClass = ' ui-corner-' + (isRTL ? 'right' : 'left'); break;
							case numMonths[1]-1: calender += ' ui-datepicker-group-last';
								cornerClass = ' ui-corner-' + (isRTL ? 'left' : 'right'); break;
							default: calender += ' ui-datepicker-group-middle'; cornerClass = ''; break;
						}
					calender += '">';
				}
				calender += '<div class="ui-datepicker-header ui-widget-header ui-helper-clearfix' + cornerClass + '">' +
					(/all|left/.test(cornerClass) && row == 0 ? (isRTL ? next : prev) : '') +
					(/all|right/.test(cornerClass) && row == 0 ? (isRTL ? prev : next) : '') +
					this._generateMonthYearHeader(inst, drawMonth, drawYear, minDate, maxDate,
					row > 0 || col > 0, monthNames, monthNamesShort) + // draw month headers
					'</div><table class="ui-datepicker-calendar"><thead>' +
					'<tr>';
				var thead = (showWeek ? '<th class="ui-datepicker-week-col">' + this._get(inst, 'weekHeader') + '</th>' : '');
				for (var dow = 0; dow < 7; dow++) { // days of the week
					var day = (dow + firstDay) % 7;
					thead += '<th' + ((dow + firstDay + 6) % 7 >= 5 ? ' class="ui-datepicker-week-end"' : '') + '>' +
						'<span title="' + dayNames[day] + '">' + dayNamesMin[day] + '</span></th>';
				}
				calender += thead + '</tr></thead><tbody>';
				var daysInMonth = this._getDaysInMonth(drawYear, drawMonth);
				if (drawYear == inst.selectedYear && drawMonth == inst.selectedMonth)
					inst.selectedDay = Math.min(inst.selectedDay, daysInMonth);
				var leadDays = (this._getFirstDayOfMonth(drawYear, drawMonth) - firstDay + 7) % 7;
				var numRows = (isMultiMonth ? 6 : Math.ceil((leadDays + daysInMonth) / 7)); // calculate the number of rows to generate
				var printDate = this._daylightSavingAdjust(new Date(drawYear, drawMonth, 1 - leadDays));
				for (var dRow = 0; dRow < numRows; dRow++) { // create date picker rows
					calender += '<tr>';
					var tbody = (!showWeek ? '' : '<td class="ui-datepicker-week-col">' +
						this._get(inst, 'calculateWeek')(printDate) + '</td>');
					for (var dow = 0; dow < 7; dow++) { // create date picker days
						var daySettings = (beforeShowDay ?
							beforeShowDay.apply((inst.input ? inst.input[0] : null), [printDate]) : [true, '']);
						var otherMonth = (printDate.getMonth() != drawMonth);
						var unselectable = (otherMonth && !selectOtherMonths) || !daySettings[0] ||
							(minDate && printDate < minDate) || (maxDate && printDate > maxDate);
						tbody += '<td class="' +
							((dow + firstDay + 6) % 7 >= 5 ? ' ui-datepicker-week-end' : '') + // highlight weekends
							(otherMonth ? ' ui-datepicker-other-month' : '') + // highlight days from other months
							((printDate.getTime() == selectedDate.getTime() && drawMonth == inst.selectedMonth && inst._keyEvent) || // user pressed key
							(defaultDate.getTime() == printDate.getTime() && defaultDate.getTime() == selectedDate.getTime()) ?
							// or defaultDate is current printedDate and defaultDate is selectedDate
							' ' + this._dayOverClass : '') + // highlight selected day
							(unselectable ? ' ' + this._unselectableClass + ' ui-state-disabled': '') +  // highlight unselectable days
							(otherMonth && !showOtherMonths ? '' : ' ' + daySettings[1] + // highlight custom dates
							(printDate.getTime() == currentDate.getTime() ? ' ' + this._currentClass : '') + // highlight selected day
							(printDate.getTime() == today.getTime() ? ' ui-datepicker-today' : '')) + '"' + // highlight today (if different)
							((!otherMonth || showOtherMonths) && daySettings[2] ? ' title="' + daySettings[2] + '"' : '') + // cell title
							(unselectable ? '' : ' onclick="DP_jQuery_' + dpuuid + '.datepicker._selectDay(\'#' +
							inst.id + '\',' + printDate.getMonth() + ',' + printDate.getFullYear() + ', this);return false;"') + '>' + // actions
							(otherMonth && !showOtherMonths ? '&#xa0;' : // display for other months
							(unselectable ? '<span class="ui-state-default">' + printDate.getDate() + '</span>' : '<a class="ui-state-default' +
							(printDate.getTime() == today.getTime() ? ' ui-state-highlight' : '') +
							(printDate.getTime() == selectedDate.getTime() ? ' ui-state-active' : '') + // highlight selected day
							(otherMonth ? ' ui-priority-secondary' : '') + // distinguish dates from other months
							'" href="#">' + printDate.getDate() + '</a>')) + '</td>'; // display selectable date
						printDate.setDate(printDate.getDate() + 1);
						printDate = this._daylightSavingAdjust(printDate);
					}
					calender += tbody + '</tr>';
				}
				drawMonth++;
				if (drawMonth > 11) {
					drawMonth = 0;
					drawYear++;
				}
				calender += '</tbody></table>' + (isMultiMonth ? '</div>' + 
							((numMonths[0] > 0 && col == numMonths[1]-1) ? '<div class="ui-datepicker-row-break"></div>' : '') : '');
				group += calender;
			}
			html += group;
		}
		html += buttonPanel + ($.browser.msie && parseInt($.browser.version,10) < 7 && !inst.inline ?
			'<iframe src="javascript:false;" class="ui-datepicker-cover" frameborder="0"></iframe>' : '');
		inst._keyEvent = false;
		return html;
	},

	/* Generate the month and year header. */
	_generateMonthYearHeader: function(inst, drawMonth, drawYear, minDate, maxDate,
			secondary, monthNames, monthNamesShort) {
		var changeMonth = this._get(inst, 'changeMonth');
		var changeYear = this._get(inst, 'changeYear');
		var showMonthAfterYear = this._get(inst, 'showMonthAfterYear');
		var html = '<div class="ui-datepicker-title">';
		var monthHtml = '';
		// month selection
		if (secondary || !changeMonth)
			monthHtml += '<span class="ui-datepicker-month">' + monthNames[drawMonth] + '</span>';
		else {
			var inMinYear = (minDate && minDate.getFullYear() == drawYear);
			var inMaxYear = (maxDate && maxDate.getFullYear() == drawYear);
			monthHtml += '<select class="ui-datepicker-month" ' +
				'onchange="DP_jQuery_' + dpuuid + '.datepicker._selectMonthYear(\'#' + inst.id + '\', this, \'M\');" ' +
				'onclick="DP_jQuery_' + dpuuid + '.datepicker._clickMonthYear(\'#' + inst.id + '\');"' +
			 	'>';
			for (var month = 0; month < 12; month++) {
				if ((!inMinYear || month >= minDate.getMonth()) &&
						(!inMaxYear || month <= maxDate.getMonth()))
					monthHtml += '<option value="' + month + '"' +
						(month == drawMonth ? ' selected="selected"' : '') +
						'>' + monthNamesShort[month] + '</option>';
			}
			monthHtml += '</select>';
		}
		if (!showMonthAfterYear)
			html += monthHtml + (secondary || !(changeMonth && changeYear) ? '&#xa0;' : '');
		// year selection
		if (secondary || !changeYear)
			html += '<span class="ui-datepicker-year">' + drawYear + '</span>';
		else {
			// determine range of years to display
			var years = this._get(inst, 'yearRange').split(':');
			var thisYear = new Date().getFullYear();
			var determineYear = function(value) {
				var year = (value.match(/c[+-].*/) ? drawYear + parseInt(value.substring(1), 10) :
					(value.match(/[+-].*/) ? thisYear + parseInt(value, 10) :
					parseInt(value, 10)));
				return (isNaN(year) ? thisYear : year);
			};
			var year = determineYear(years[0]);
			var endYear = Math.max(year, determineYear(years[1] || ''));
			year = (minDate ? Math.max(year, minDate.getFullYear()) : year);
			endYear = (maxDate ? Math.min(endYear, maxDate.getFullYear()) : endYear);
			html += '<select class="ui-datepicker-year" ' +
				'onchange="DP_jQuery_' + dpuuid + '.datepicker._selectMonthYear(\'#' + inst.id + '\', this, \'Y\');" ' +
				'onclick="DP_jQuery_' + dpuuid + '.datepicker._clickMonthYear(\'#' + inst.id + '\');"' +
				'>';
			for (; year <= endYear; year++) {
				html += '<option value="' + year + '"' +
					(year == drawYear ? ' selected="selected"' : '') +
					'>' + year + '</option>';
			}
			html += '</select>';
		}
		html += this._get(inst, 'yearSuffix');
		if (showMonthAfterYear)
			html += (secondary || !(changeMonth && changeYear) ? '&#xa0;' : '') + monthHtml;
		html += '</div>'; // Close datepicker_header
		return html;
	},

	/* Adjust one of the date sub-fields. */
	_adjustInstDate: function(inst, offset, period) {
		var year = inst.drawYear + (period == 'Y' ? offset : 0);
		var month = inst.drawMonth + (period == 'M' ? offset : 0);
		var day = Math.min(inst.selectedDay, this._getDaysInMonth(year, month)) +
			(period == 'D' ? offset : 0);
		var date = this._restrictMinMax(inst,
			this._daylightSavingAdjust(new Date(year, month, day)));
		inst.selectedDay = date.getDate();
		inst.drawMonth = inst.selectedMonth = date.getMonth();
		inst.drawYear = inst.selectedYear = date.getFullYear();
		if (period == 'M' || period == 'Y')
			this._notifyChange(inst);
	},

	/* Ensure a date is within any min/max bounds. */
	_restrictMinMax: function(inst, date) {
		var minDate = this._getMinMaxDate(inst, 'min');
		var maxDate = this._getMinMaxDate(inst, 'max');
		date = (minDate && date < minDate ? minDate : date);
		date = (maxDate && date > maxDate ? maxDate : date);
		return date;
	},

	/* Notify change of month/year. */
	_notifyChange: function(inst) {
		var onChange = this._get(inst, 'onChangeMonthYear');
		if (onChange)
			onChange.apply((inst.input ? inst.input[0] : null),
				[inst.selectedYear, inst.selectedMonth + 1, inst]);
	},

	/* Determine the number of months to show. */
	_getNumberOfMonths: function(inst) {
		var numMonths = this._get(inst, 'numberOfMonths');
		return (numMonths == null ? [1, 1] : (typeof numMonths == 'number' ? [1, numMonths] : numMonths));
	},

	/* Determine the current maximum date - ensure no time components are set. */
	_getMinMaxDate: function(inst, minMax) {
		return this._determineDate(inst, this._get(inst, minMax + 'Date'), null);
	},

	/* Find the number of days in a given month. */
	_getDaysInMonth: function(year, month) {
		return 32 - new Date(year, month, 32).getDate();
	},

	/* Find the day of the week of the first of a month. */
	_getFirstDayOfMonth: function(year, month) {
		return new Date(year, month, 1).getDay();
	},

	/* Determines if we should allow a "next/prev" month display change. */
	_canAdjustMonth: function(inst, offset, curYear, curMonth) {
		var numMonths = this._getNumberOfMonths(inst);
		var date = this._daylightSavingAdjust(new Date(curYear,
			curMonth + (offset < 0 ? offset : numMonths[0] * numMonths[1]), 1));
		if (offset < 0)
			date.setDate(this._getDaysInMonth(date.getFullYear(), date.getMonth()));
		return this._isInRange(inst, date);
	},

	/* Is the given date in the accepted range? */
	_isInRange: function(inst, date) {
		var minDate = this._getMinMaxDate(inst, 'min');
		var maxDate = this._getMinMaxDate(inst, 'max');
		return ((!minDate || date.getTime() >= minDate.getTime()) &&
			(!maxDate || date.getTime() <= maxDate.getTime()));
	},

	/* Provide the configuration settings for formatting/parsing. */
	_getFormatConfig: function(inst) {
		var shortYearCutoff = this._get(inst, 'shortYearCutoff');
		shortYearCutoff = (typeof shortYearCutoff != 'string' ? shortYearCutoff :
			new Date().getFullYear() % 100 + parseInt(shortYearCutoff, 10));
		return {shortYearCutoff: shortYearCutoff,
			dayNamesShort: this._get(inst, 'dayNamesShort'), dayNames: this._get(inst, 'dayNames'),
			monthNamesShort: this._get(inst, 'monthNamesShort'), monthNames: this._get(inst, 'monthNames')};
	},

	/* Format the given date for display. */
	_formatDate: function(inst, day, month, year) {
		if (!day) {
			inst.currentDay = inst.selectedDay;
			inst.currentMonth = inst.selectedMonth;
			inst.currentYear = inst.selectedYear;
		}
		var date = (day ? (typeof day == 'object' ? day :
			this._daylightSavingAdjust(new Date(year, month, day))) :
			this._daylightSavingAdjust(new Date(inst.currentYear, inst.currentMonth, inst.currentDay)));
		return this.formatDate(this._get(inst, 'dateFormat'), date, this._getFormatConfig(inst));
	}
});

/* jQuery extend now ignores nulls! */
function extendRemove(target, props) {
	$.extend(target, props);
	for (var name in props)
		if (props[name] == null || props[name] == undefined)
			target[name] = props[name];
	return target;
};

/* Determine whether an object is an array. */
function isArray(a) {
	return (a && (($.browser.safari && typeof a == 'object' && a.length) ||
		(a.constructor && a.constructor.toString().match(/\Array\(\)/))));
};

/* Invoke the datepicker functionality.
   @param  options  string - a command, optionally followed by additional parameters or
                    Object - settings for attaching new datepicker functionality
   @return  jQuery object */
$.fn.datepicker = function(options){

	/* Initialise the date picker. */
	if (!$.datepicker.initialized) {
		$(document).mousedown($.datepicker._checkExternalClick).
			find('body').append($.datepicker.dpDiv);
		$.datepicker.initialized = true;
	}

	var otherArgs = Array.prototype.slice.call(arguments, 1);
	if (typeof options == 'string' && (options == 'isDisabled' || options == 'getDate' || options == 'widget'))
		return $.datepicker['_' + options + 'Datepicker'].
			apply($.datepicker, [this[0]].concat(otherArgs));
	if (options == 'option' && arguments.length == 2 && typeof arguments[1] == 'string')
		return $.datepicker['_' + options + 'Datepicker'].
			apply($.datepicker, [this[0]].concat(otherArgs));
	return this.each(function() {
		typeof options == 'string' ?
			$.datepicker['_' + options + 'Datepicker'].
				apply($.datepicker, [this].concat(otherArgs)) :
			$.datepicker._attachDatepicker(this, options);
	});
};

$.datepicker = new Datepicker(); // singleton instance
$.datepicker.initialized = false;
$.datepicker.uuid = new Date().getTime();
$.datepicker.version = "1.8.5";

// Workaround for #4055
// Add another global to avoid noConflict issues with inline event handlers
window['DP_jQuery_' + dpuuid] = $;

})(jQuery);

//End of order list /swc/xpedx/js/jquery.ui.datepicker.js

//Adding order list swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js

/*!
 * jQuery UI 1.8.2
 *
 * Copyright (c) 2010 AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * http://docs.jquery.com/UI
 */

(function($) {

// prevent duplicate loading
// this is only a problem because we proxy existing functions
// and we don't want to double proxy them
$.ui = $.ui || {};
if ($.ui.version) {
	return;
}

//Helper functions and ui object
$.extend($.ui, {
	version: "1.8.2",

	// $.ui.plugin is deprecated.  Use the proxy pattern instead.
	plugin: {
		add: function(module, option, set) {
			var proto = $.ui[module].prototype;
			for(var i in set) {
				proto.plugins[i] = proto.plugins[i] || [];
				proto.plugins[i].push([option, set[i]]);
			}
		},
		call: function(instance, name, args) {
			var set = instance.plugins[name];
			if(!set || !instance.element[0].parentNode) { return; }

			for (var i = 0; i < set.length; i++) {
				if (instance.options[set[i][0]]) {
					set[i][1].apply(instance.element, args);
				}
			}
		}
	},

	contains: function(a, b) {
		return document.compareDocumentPosition
			? a.compareDocumentPosition(b) & 16
			: a !== b && a.contains(b);
	},

	hasScroll: function(el, a) {

		//If overflow is hidden, the element might have extra content, but the user wants to hide it
		if ($(el).css('overflow') == 'hidden') { return false; }

		var scroll = (a && a == 'left') ? 'scrollLeft' : 'scrollTop',
			has = false;

		if (el[scroll] > 0) { return true; }

		// TODO: determine which cases actually cause this to happen
		// if the element doesn't have the scroll set, see if it's possible to
		// set the scroll
		el[scroll] = 1;
		has = (el[scroll] > 0);
		el[scroll] = 0;
		return has;
	},

	isOverAxis: function(x, reference, size) {
		//Determines when x coordinate is over "b" element axis
		return (x > reference) && (x < (reference + size));
	},

	isOver: function(y, x, top, left, height, width) {
		//Determines when x, y coordinates is over "b" element
		return $.ui.isOverAxis(y, top, height) && $.ui.isOverAxis(x, left, width);
	},

	keyCode: {
		ALT: 18,
		BACKSPACE: 8,
		CAPS_LOCK: 20,
		COMMA: 188,
		COMMAND: 91,
		COMMAND_LEFT: 91, // COMMAND
		COMMAND_RIGHT: 93,
		CONTROL: 17,
		DELETE: 46,
		DOWN: 40,
		END: 35,
		ENTER: 13,
		ESCAPE: 27,
		HOME: 36,
		INSERT: 45,
		LEFT: 37,
		MENU: 93, // COMMAND_RIGHT
		NUMPAD_ADD: 107,
		NUMPAD_DECIMAL: 110,
		NUMPAD_DIVIDE: 111,
		NUMPAD_ENTER: 108,
		NUMPAD_MULTIPLY: 106,
		NUMPAD_SUBTRACT: 109,
		PAGE_DOWN: 34,
		PAGE_UP: 33,
		PERIOD: 190,
		RIGHT: 39,
		SHIFT: 16,
		SPACE: 32,
		TAB: 9,
		UP: 38,
		WINDOWS: 91 // COMMAND
	}
});

//jQuery plugins
$.fn.extend({
	_focus: $.fn.focus,
	focus: function(delay, fn) {
		return typeof delay === 'number'
			? this.each(function() {
				var elem = this;
				setTimeout(function() {
					$(elem).focus();
					(fn && fn.call(elem));
				}, delay);
			})
			: this._focus.apply(this, arguments);
	},
	
	enableSelection: function() {
		return this
			.attr('unselectable', 'off')
			.css('MozUserSelect', '');
	},

	disableSelection: function() {
		return this
			.attr('unselectable', 'on')
			.css('MozUserSelect', 'none');
	},

	scrollParent: function() {
		var scrollParent;
		if(($.browser.msie && (/(static|relative)/).test(this.css('position'))) || (/absolute/).test(this.css('position'))) {
			scrollParent = this.parents().filter(function() {
				return (/(relative|absolute|fixed)/).test($.curCSS(this,'position',1)) && (/(auto|scroll)/).test($.curCSS(this,'overflow',1)+$.curCSS(this,'overflow-y',1)+$.curCSS(this,'overflow-x',1));
			}).eq(0);
		} else {
			scrollParent = this.parents().filter(function() {
				return (/(auto|scroll)/).test($.curCSS(this,'overflow',1)+$.curCSS(this,'overflow-y',1)+$.curCSS(this,'overflow-x',1));
			}).eq(0);
		}

		return (/fixed/).test(this.css('position')) || !scrollParent.length ? $(document) : scrollParent;
	},

	zIndex: function(zIndex) {
		if (zIndex !== undefined) {
			return this.css('zIndex', zIndex);
		}
		
		if (this.length) {
			var elem = $(this[0]), position, value;
			while (elem.length && elem[0] !== document) {
				// Ignore z-index if position is set to a value where z-index is ignored by the browser
				// This makes behavior of this function consistent across browsers
				// WebKit always returns auto if the element is positioned
				position = elem.css('position');
				if (position == 'absolute' || position == 'relative' || position == 'fixed')
				{
					// IE returns 0 when zIndex is not specified
					// other browsers return a string
					// we ignore the case of nested elements with an explicit value of 0
					// <div style="z-index: -10;"><div style="z-index: 0;"></div></div>
					value = parseInt(elem.css('zIndex'));
					if (!isNaN(value) && value != 0) {
						return value;
					}
				}
				elem = elem.parent();
			}
		}

		return 0;
	}
});


//Additional selectors
$.extend($.expr[':'], {
	data: function(elem, i, match) {
		return !!$.data(elem, match[3]);
	},

	focusable: function(element) {
		var nodeName = element.nodeName.toLowerCase(),
			tabIndex = $.attr(element, 'tabindex');
		return (/input|select|textarea|button|object/.test(nodeName)
			? !element.disabled
			: 'a' == nodeName || 'area' == nodeName
				? element.href || !isNaN(tabIndex)
				: !isNaN(tabIndex))
			// the element and all of its ancestors must be visible
			// the browser may report that the area is hidden
			&& !$(element)['area' == nodeName ? 'parents' : 'closest'](':hidden').length;
	},

	tabbable: function(element) {
		var tabIndex = $.attr(element, 'tabindex');
		return (isNaN(tabIndex) || tabIndex >= 0) && $(element).is(':focusable');
	}
});

})(jQuery);

//End of order list swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js


//Adding order list swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js

/*!
 * jQuery UI Widget 1.8.2
 *
 * Copyright (c) 2010 AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * http://docs.jquery.com/UI/Widget
 */
(function( $ ) {

var _remove = $.fn.remove;

$.fn.remove = function( selector, keepData ) {
	return this.each(function() {
		if ( !keepData ) {
			if ( !selector || $.filter( selector, [ this ] ).length ) {
				$( "*", this ).add( this ).each(function() {
					$( this ).triggerHandler( "remove" );
				});
			}
		}
		return _remove.call( $(this), selector, keepData );
	});
};

$.widget = function( name, base, prototype ) {
	var namespace = name.split( "." )[ 0 ],
		fullName;
	name = name.split( "." )[ 1 ];
	fullName = namespace + "-" + name;

	if ( !prototype ) {
		prototype = base;
		base = $.Widget;
	}

	// create selector for plugin
	$.expr[ ":" ][ fullName ] = function( elem ) {
		return !!$.data( elem, name );
	};

	$[ namespace ] = $[ namespace ] || {};
	$[ namespace ][ name ] = function( options, element ) {
		// allow instantiation without initializing for simple inheritance
		if ( arguments.length ) {
			this._createWidget( options, element );
		}
	};

	var basePrototype = new base();
	// we need to make the options hash a property directly on the new instance
	// otherwise we'll modify the options hash on the prototype that we're
	// inheriting from
//	$.each( basePrototype, function( key, val ) {
//		if ( $.isPlainObject(val) ) {
//			basePrototype[ key ] = $.extend( {}, val );
//		}
//	});
	basePrototype.options = $.extend( {}, basePrototype.options );
	$[ namespace ][ name ].prototype = $.extend( true, basePrototype, {
		namespace: namespace,
		widgetName: name,
		widgetEventPrefix: $[ namespace ][ name ].prototype.widgetEventPrefix || name,
		widgetBaseClass: fullName
	}, prototype );

	$.widget.bridge( name, $[ namespace ][ name ] );
};

$.widget.bridge = function( name, object ) {
	$.fn[ name ] = function( options ) {
		var isMethodCall = typeof options === "string",
			args = Array.prototype.slice.call( arguments, 1 ),
			returnValue = this;

		// allow multiple hashes to be passed on init
		options = !isMethodCall && args.length ?
			$.extend.apply( null, [ true, options ].concat(args) ) :
			options;

		// prevent calls to internal methods
		if ( isMethodCall && options.substring( 0, 1 ) === "_" ) {
			return returnValue;
		}

		if ( isMethodCall ) {
			this.each(function() {
				var instance = $.data( this, name ),
					methodValue = instance && $.isFunction( instance[options] ) ?
						instance[ options ].apply( instance, args ) :
						instance;
				if ( methodValue !== instance && methodValue !== undefined ) {
					returnValue = methodValue;
					return false;
				}
			});
		} else {
			this.each(function() {
				var instance = $.data( this, name );
				if ( instance ) {
					if ( options ) {
						instance.option( options );
					}
					instance._init();
				} else {
					$.data( this, name, new object( options, this ) );
				}
			});
		}

		return returnValue;
	};
};

$.Widget = function( options, element ) {
	// allow instantiation without initializing for simple inheritance
	if ( arguments.length ) {
		this._createWidget( options, element );
	}
};

$.Widget.prototype = {
	widgetName: "widget",
	widgetEventPrefix: "",
	options: {
		disabled: false
	},
	_createWidget: function( options, element ) {
		// $.widget.bridge stores the plugin instance, but we do it anyway
		// so that it's stored even before the _create function runs
		this.element = $( element ).data( this.widgetName, this );
		this.options = $.extend( true, {},
			this.options,
			$.metadata && $.metadata.get( element )[ this.widgetName ],
			options );

		var self = this;
		this.element.bind( "remove." + this.widgetName, function() {
			self.destroy();
		});

		this._create();
		this._init();
	},
	_create: function() {},
	_init: function() {},

	destroy: function() {
		this.element
			.unbind( "." + this.widgetName )
			.removeData( this.widgetName );
		this.widget()
			.unbind( "." + this.widgetName )
			.removeAttr( "aria-disabled" )
			.removeClass(
				this.widgetBaseClass + "-disabled " +
				"ui-state-disabled" );
	},

	widget: function() {
		return this.element;
	},

	option: function( key, value ) {
		var options = key,
			self = this;

		if ( arguments.length === 0 ) {
			// don't return a reference to the internal hash
			return $.extend( {}, self.options );
		}

		if  (typeof key === "string" ) {
			if ( value === undefined ) {
				return this.options[ key ];
			}
			options = {};
			options[ key ] = value;
		}

		$.each( options, function( key, value ) {
			self._setOption( key, value );
		});

		return self;
	},
	_setOption: function( key, value ) {
		this.options[ key ] = value;

		if ( key === "disabled" ) {
			this.widget()
				[ value ? "addClass" : "removeClass"](
					this.widgetBaseClass + "-disabled" + " " +
					"ui-state-disabled" )
				.attr( "aria-disabled", value );
		}

		return this;
	},

	enable: function() {
		return this._setOption( "disabled", false );
	},
	disable: function() {
		return this._setOption( "disabled", true );
	},

	_trigger: function( type, event, data ) {
		var callback = this.options[ type ];

		event = $.Event( event );
		event.type = ( type === this.widgetEventPrefix ?
			type :
			this.widgetEventPrefix + type ).toLowerCase();
		data = data || {};

		// copy original event properties over to the new event
		// this would happen if we could call $.event.fix instead of $.Event
		// but we don't have a way to force an event to be fixed multiple times
		if ( event.originalEvent ) {
			for ( var i = $.event.props.length, prop; i; ) {
				prop = $.event.props[ --i ];
				event[ prop ] = event.originalEvent[ prop ];
			}
		}

		this.element.trigger( event, data );

		return !( $.isFunction(callback) &&
			callback.call( this.element[0], event, data ) === false ||
			event.isDefaultPrevented() );
	}
};

})( jQuery );

//Ending order list swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js

//Adding order list jquery.ui.tabs.js

/*
 * jQuery UI Tabs 1.8.2
 *
 * Copyright (c) 2010 AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * http://docs.jquery.com/UI/Tabs
 *
 * Depends:
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 */
(function($) {

var tabId = 0,
	listId = 0;

function getNextTabId() {
	return ++tabId;
}

function getNextListId() {
	return ++listId;
}

$.widget("ui.tabs", {
	options: {
		add: null,
		ajaxOptions: null,
		cache: false,
		cookie: null, // e.g. { expires: 7, path: '/', domain: 'jquery.com', secure: true }
		collapsible: false,
		disable: null,
		disabled: [],
		enable: null,
		event: 'click',
		fx: null, // e.g. { height: 'toggle', opacity: 'toggle', duration: 200 }
		idPrefix: 'ui-tabs-',
		load: null,
		panelTemplate: '<div></div>',
		remove: null,
		select: null,
		show: null,
		spinner: '<em>Loading&#8230;</em>',
		tabTemplate: '<li><a href="#{href}"><span>#{label}</span></a></li>'
	},
	_create: function() {
		this._tabify(true);
	},

	_setOption: function(key, value) {
		if (key == 'selected') {
			if (this.options.collapsible && value == this.options.selected) {
				return;
			}
			this.select(value);
		}
		else {
			this.options[key] = value;
			this._tabify();
		}
	},

	_tabId: function(a) {
		return a.title && a.title.replace(/\s/g, '_').replace(/[^A-Za-z0-9\-_:\.]/g, '') ||
			this.options.idPrefix + getNextTabId();
	},

	_sanitizeSelector: function(hash) {
		return hash.replace(/:/g, '\\:'); // we need this because an id may contain a ":"
	},

	_cookie: function() {
		var cookie = this.cookie || (this.cookie = this.options.cookie.name || 'ui-tabs-' + getNextListId());
		return $.cookie.apply(null, [cookie].concat($.makeArray(arguments)));
	},

	_ui: function(tab, panel) {
		return {
			tab: tab,
			panel: panel,
			index: this.anchors.index(tab)
		};
	},

	_cleanup: function() {
		// restore all former loading tabs labels
		this.lis.filter('.ui-state-processing').removeClass('ui-state-processing')
				.find('span:data(label.tabs)')
				.each(function() {
					var el = $(this);
					el.html(el.data('label.tabs')).removeData('label.tabs');
				});
	},

	_tabify: function(init) {

		this.list = this.element.find('ol,ul').eq(0);
		this.lis = $('li:has(a[href])', this.list);
		this.anchors = this.lis.map(function() { return $('a', this)[0]; });
		this.panels = $([]);

		var self = this, o = this.options;

		var fragmentId = /^#.+/; // Safari 2 reports '#' for an empty hash
		this.anchors.each(function(i, a) {
			var href = $(a).attr('href');

			// For dynamically created HTML that contains a hash as href IE < 8 expands
			// such href to the full page url with hash and then misinterprets tab as ajax.
			// Same consideration applies for an added tab with a fragment identifier
			// since a[href=#fragment-identifier] does unexpectedly not match.
			// Thus normalize href attribute...
			var hrefBase = href.split('#')[0], baseEl;
			if (hrefBase && (hrefBase === location.toString().split('#')[0] ||
					(baseEl = $('base')[0]) && hrefBase === baseEl.href)) {
				href = a.hash;
				a.href = href;
			}

			// inline tab
			if (fragmentId.test(href)) {
				self.panels = self.panels.add(self._sanitizeSelector(href));
			}

			// remote tab
			else if (href != '#') { // prevent loading the page itself if href is just "#"
				$.data(a, 'href.tabs', href); // required for restore on destroy

				// TODO until #3808 is fixed strip fragment identifier from url
				// (IE fails to load from such url)
				$.data(a, 'load.tabs', href.replace(/#.*$/, '')); // mutable data

				var id = self._tabId(a);
				a.href = '#' + id;
				var $panel = $('#' + id);
				if (!$panel.length) {
					$panel = $(o.panelTemplate).attr('id', id).addClass('ui-tabs-panel ui-widget-content ui-corner-bottom')
						.insertAfter(self.panels[i - 1] || self.list);
					$panel.data('destroy.tabs', true);
				}
				self.panels = self.panels.add($panel);
			}

			// invalid tab href
			else {
				o.disabled.push(i);
			}
		});

		// initialization from scratch
		if (init) {

			// attach necessary classes for styling
			this.element.addClass('ui-tabs ui-widget ui-widget-content ui-corner-all');
			this.list.addClass('ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all');
			this.lis.addClass('ui-state-default ui-corner-top');
			this.panels.addClass('ui-tabs-panel ui-widget-content ui-corner-bottom');

			// Selected tab
			// use "selected" option or try to retrieve:
			// 1. from fragment identifier in url
			// 2. from cookie
			// 3. from selected class attribute on <li>
			if (o.selected === undefined) {
				if (location.hash) {
					this.anchors.each(function(i, a) {
						if (a.hash == location.hash) {
							o.selected = i;
							return false; // break
						}
					});
				}
				if (typeof o.selected != 'number' && o.cookie) {
					o.selected = parseInt(self._cookie(), 10);
				}
				if (typeof o.selected != 'number' && this.lis.filter('.ui-tabs-selected').length) {
					o.selected = this.lis.index(this.lis.filter('.ui-tabs-selected'));
				}
				o.selected = o.selected || (this.lis.length ? 0 : -1);
			}
			else if (o.selected === null) { // usage of null is deprecated, TODO remove in next release
				o.selected = -1;
			}

			// sanity check - default to first tab...
			o.selected = ((o.selected >= 0 && this.anchors[o.selected]) || o.selected < 0) ? o.selected : 0;

			// Take disabling tabs via class attribute from HTML
			// into account and update option properly.
			// A selected tab cannot become disabled.
			o.disabled = $.unique(o.disabled.concat(
				$.map(this.lis.filter('.ui-state-disabled'),
					function(n, i) { return self.lis.index(n); } )
			)).sort();

			if ($.inArray(o.selected, o.disabled) != -1) {
				o.disabled.splice($.inArray(o.selected, o.disabled), 1);
			}

			// highlight selected tab
			this.panels.addClass('ui-tabs-hide');
			this.lis.removeClass('ui-tabs-selected ui-state-active');
			if (o.selected >= 0 && this.anchors.length) { // check for length avoids error when initializing empty list
				this.panels.eq(o.selected).removeClass('ui-tabs-hide');
				this.lis.eq(o.selected).addClass('ui-tabs-selected ui-state-active');

				// seems to be expected behavior that the show callback is fired
				self.element.queue("tabs", function() {
					self._trigger('show', null, self._ui(self.anchors[o.selected], self.panels[o.selected]));
				});
				
				this.load(o.selected);
			}

			// clean up to avoid memory leaks in certain versions of IE 6
			$(window).bind('unload', function() {
				self.lis.add(self.anchors).unbind('.tabs');
				self.lis = self.anchors = self.panels = null;
			});

		}
		// update selected after add/remove
		else {
			o.selected = this.lis.index(this.lis.filter('.ui-tabs-selected'));
		}

		// update collapsible
		this.element[o.collapsible ? 'addClass' : 'removeClass']('ui-tabs-collapsible');

		// set or update cookie after init and add/remove respectively
		if (o.cookie) {
			this._cookie(o.selected, o.cookie);
		}

		// disable tabs
		for (var i = 0, li; (li = this.lis[i]); i++) {
			$(li)[$.inArray(i, o.disabled) != -1 &&
				!$(li).hasClass('ui-tabs-selected') ? 'addClass' : 'removeClass']('ui-state-disabled');
		}

		// reset cache if switching from cached to not cached
		if (o.cache === false) {
			this.anchors.removeData('cache.tabs');
		}

		// remove all handlers before, tabify may run on existing tabs after add or option change
		this.lis.add(this.anchors).unbind('.tabs');

		if (o.event != 'mouseover') {
			var addState = function(state, el) {
				if (el.is(':not(.ui-state-disabled)')) {
					el.addClass('ui-state-' + state);
				}
			};
			var removeState = function(state, el) {
				el.removeClass('ui-state-' + state);
			};
			this.lis.bind('mouseover.tabs', function() {
				addState('hover', $(this));
			});
			this.lis.bind('mouseout.tabs', function() {
				removeState('hover', $(this));
			});
			this.anchors.bind('focus.tabs', function() {
				addState('focus', $(this).closest('li'));
			});
			this.anchors.bind('blur.tabs', function() {
				removeState('focus', $(this).closest('li'));
			});
		}

		// set up animations
		var hideFx, showFx;
		if (o.fx) {
			if ($.isArray(o.fx)) {
				hideFx = o.fx[0];
				showFx = o.fx[1];
			}
			else {
				hideFx = showFx = o.fx;
			}
		}

		// Reset certain styles left over from animation
		// and prevent IE's ClearType bug...
		function resetStyle($el, fx) {
			$el.css({ display: '' });
			if (!$.support.opacity && fx.opacity) {
				$el[0].style.removeAttribute('filter');
			}
		}

		// Show a tab...
		var showTab = showFx ?
			function(clicked, $show) {
				$(clicked).closest('li').addClass('ui-tabs-selected ui-state-active');
				$show.hide().removeClass('ui-tabs-hide') // avoid flicker that way
					.animate(showFx, showFx.duration || 'normal', function() {
						resetStyle($show, showFx);
						self._trigger('show', null, self._ui(clicked, $show[0]));
					});
			} :
			function(clicked, $show) {
				$(clicked).closest('li').addClass('ui-tabs-selected ui-state-active');
				$show.removeClass('ui-tabs-hide');
				self._trigger('show', null, self._ui(clicked, $show[0]));
			};

		// Hide a tab, $show is optional...
		var hideTab = hideFx ?
			function(clicked, $hide) {
				$hide.animate(hideFx, hideFx.duration || 'normal', function() {
					self.lis.removeClass('ui-tabs-selected ui-state-active');
					$hide.addClass('ui-tabs-hide');
					resetStyle($hide, hideFx);
					self.element.dequeue("tabs");
				});
			} :
			function(clicked, $hide, $show) {
				self.lis.removeClass('ui-tabs-selected ui-state-active');
				$hide.addClass('ui-tabs-hide');
				self.element.dequeue("tabs");
			};

		// attach tab event handler, unbind to avoid duplicates from former tabifying...
		this.anchors.bind(o.event + '.tabs', function() {
			var el = this, $li = $(this).closest('li'), $hide = self.panels.filter(':not(.ui-tabs-hide)'),
					$show = $(self._sanitizeSelector(this.hash));

			// If tab is already selected and not collapsible or tab disabled or
			// or is already loading or click callback returns false stop here.
			// Check if click handler returns false last so that it is not executed
			// for a disabled or loading tab!
			if (($li.hasClass('ui-tabs-selected') && !o.collapsible) ||
				$li.hasClass('ui-state-disabled') ||
				$li.hasClass('ui-state-processing') ||
				self._trigger('select', null, self._ui(this, $show[0])) === false) {
				this.blur();
				return false;
			}

			o.selected = self.anchors.index(this);

			self.abort();

			// if tab may be closed
			if (o.collapsible) {
				if ($li.hasClass('ui-tabs-selected')) {
					o.selected = -1;

					if (o.cookie) {
						self._cookie(o.selected, o.cookie);
					}

					self.element.queue("tabs", function() {
						hideTab(el, $hide);
					}).dequeue("tabs");
					
					this.blur();
					return false;
				}
				else if (!$hide.length) {
					if (o.cookie) {
						self._cookie(o.selected, o.cookie);
					}
					
					self.element.queue("tabs", function() {
						showTab(el, $show);
					});

					self.load(self.anchors.index(this)); // TODO make passing in node possible, see also http://dev.jqueryui.com/ticket/3171
					
					this.blur();
					return false;
				}
			}

			if (o.cookie) {
				self._cookie(o.selected, o.cookie);
			}

			// show new tab
			if ($show.length) {
				if ($hide.length) {
					self.element.queue("tabs", function() {
						hideTab(el, $hide);
					});
				}
				self.element.queue("tabs", function() {
					showTab(el, $show);
				});
				
				self.load(self.anchors.index(this));
			}
			else {
				throw 'jQuery UI Tabs: Mismatching fragment identifier.';
			}

			// Prevent IE from keeping other link focussed when using the back button
			// and remove dotted border from clicked link. This is controlled via CSS
			// in modern browsers; blur() removes focus from address bar in Firefox
			// which can become a usability and annoying problem with tabs('rotate').
			if ($.browser.msie) {
				this.blur();
			}

		});

		// disable click in any case
		this.anchors.bind('click.tabs', function(){return false;});

	},

	destroy: function() {
		var o = this.options;

		this.abort();
		
		this.element.unbind('.tabs')
			.removeClass('ui-tabs ui-widget ui-widget-content ui-corner-all ui-tabs-collapsible')
			.removeData('tabs');

		this.list.removeClass('ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all');

		this.anchors.each(function() {
			var href = $.data(this, 'href.tabs');
			if (href) {
				this.href = href;
			}
			var $this = $(this).unbind('.tabs');
			$.each(['href', 'load', 'cache'], function(i, prefix) {
				$this.removeData(prefix + '.tabs');
			});
		});

		this.lis.unbind('.tabs').add(this.panels).each(function() {
			if ($.data(this, 'destroy.tabs')) {
				$(this).remove();
			}
			else {
				$(this).removeClass([
					'ui-state-default',
					'ui-corner-top',
					'ui-tabs-selected',
					'ui-state-active',
					'ui-state-hover',
					'ui-state-focus',
					'ui-state-disabled',
					'ui-tabs-panel',
					'ui-widget-content',
					'ui-corner-bottom',
					'ui-tabs-hide'
				].join(' '));
			}
		});

		if (o.cookie) {
			this._cookie(null, o.cookie);
		}

		return this;
	},

	add: function(url, label, index) {
		if (index === undefined) {
			index = this.anchors.length; // append by default
		}

		var self = this, o = this.options,
			$li = $(o.tabTemplate.replace(/#\{href\}/g, url).replace(/#\{label\}/g, label)),
			id = !url.indexOf('#') ? url.replace('#', '') : this._tabId($('a', $li)[0]);

		$li.addClass('ui-state-default ui-corner-top').data('destroy.tabs', true);

		// try to find an existing element before creating a new one
		var $panel = $('#' + id);
		if (!$panel.length) {
			$panel = $(o.panelTemplate).attr('id', id).data('destroy.tabs', true);
		}
		$panel.addClass('ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide');

		if (index >= this.lis.length) {
			$li.appendTo(this.list);
			$panel.appendTo(this.list[0].parentNode);
		}
		else {
			$li.insertBefore(this.lis[index]);
			$panel.insertBefore(this.panels[index]);
		}

		o.disabled = $.map(o.disabled,
			function(n, i) { return n >= index ? ++n : n; });

		this._tabify();

		if (this.anchors.length == 1) { // after tabify
			o.selected = 0;
			$li.addClass('ui-tabs-selected ui-state-active');
			$panel.removeClass('ui-tabs-hide');
			this.element.queue("tabs", function() {
				self._trigger('show', null, self._ui(self.anchors[0], self.panels[0]));
			});
				
			this.load(0);
		}

		// callback
		this._trigger('add', null, this._ui(this.anchors[index], this.panels[index]));
		return this;
	},

	remove: function(index) {
		var o = this.options, $li = this.lis.eq(index).remove(),
			$panel = this.panels.eq(index).remove();

		// If selected tab was removed focus tab to the right or
		// in case the last tab was removed the tab to the left.
		if ($li.hasClass('ui-tabs-selected') && this.anchors.length > 1) {
			this.select(index + (index + 1 < this.anchors.length ? 1 : -1));
		}

		o.disabled = $.map($.grep(o.disabled, function(n, i) { return n != index; }),
			function(n, i) { return n >= index ? --n : n; });

		this._tabify();

		// callback
		this._trigger('remove', null, this._ui($li.find('a')[0], $panel[0]));
		return this;
	},

	enable: function(index) {
		var o = this.options;
		if ($.inArray(index, o.disabled) == -1) {
			return;
		}

		this.lis.eq(index).removeClass('ui-state-disabled');
		o.disabled = $.grep(o.disabled, function(n, i) { return n != index; });

		// callback
		this._trigger('enable', null, this._ui(this.anchors[index], this.panels[index]));
		return this;
	},

	disable: function(index) {
		var self = this, o = this.options;
		if (index != o.selected) { // cannot disable already selected tab
			this.lis.eq(index).addClass('ui-state-disabled');

			o.disabled.push(index);
			o.disabled.sort();

			// callback
			this._trigger('disable', null, this._ui(this.anchors[index], this.panels[index]));
		}

		return this;
	},

	select: function(index) {
		if (typeof index == 'string') {
			index = this.anchors.index(this.anchors.filter('[href$=' + index + ']'));
		}
		else if (index === null) { // usage of null is deprecated, TODO remove in next release
			index = -1;
		}
		if (index == -1 && this.options.collapsible) {
			index = this.options.selected;
		}

		this.anchors.eq(index).trigger(this.options.event + '.tabs');
		return this;
	},

	load: function(index) {
		var self = this, o = this.options, a = this.anchors.eq(index)[0], url = $.data(a, 'load.tabs');

		this.abort();

		// not remote or from cache
		if (!url || this.element.queue("tabs").length !== 0 && $.data(a, 'cache.tabs')) {
			this.element.dequeue("tabs");
			return;
		}

		// load remote from here on
		this.lis.eq(index).addClass('ui-state-processing');

		if (o.spinner) {
			var span = $('span', a);
			span.data('label.tabs', span.html()).html(o.spinner);
		}

		this.xhr = $.ajax($.extend({}, o.ajaxOptions, {
			url: url,
			success: function(r, s) {
				$(self._sanitizeSelector(a.hash)).html(r);

				// take care of tab labels
				self._cleanup();

				if (o.cache) {
					$.data(a, 'cache.tabs', true); // if loaded once do not load them again
				}

				// callbacks
				self._trigger('load', null, self._ui(self.anchors[index], self.panels[index]));
				try {
					o.ajaxOptions.success(r, s);
				}
				catch (e) {}
			},
			error: function(xhr, s, e) {
				// take care of tab labels
				self._cleanup();

				// callbacks
				self._trigger('load', null, self._ui(self.anchors[index], self.panels[index]));
				try {
					// Passing index avoid a race condition when this method is
					// called after the user has selected another tab.
					// Pass the anchor that initiated this request allows
					// loadError to manipulate the tab content panel via $(a.hash)
					o.ajaxOptions.error(xhr, s, index, a);
				}
				catch (e) {}
			}
		}));

		// last, so that load event is fired before show...
		self.element.dequeue("tabs");

		return this;
	},

	abort: function() {
		// stop possibly running animations
		this.element.queue([]);
		this.panels.stop(false, true);

		// "tabs" queue must not contain more than two elements,
		// which are the callbacks for the latest clicked tab...
		this.element.queue("tabs", this.element.queue("tabs").splice(-2, 2));

		// terminate pending requests from other tabs
		if (this.xhr) {
			this.xhr.abort();
			delete this.xhr;
		}

		// take care of tab labels
		this._cleanup();
		return this;
	},

	url: function(index, url) {
		this.anchors.eq(index).removeData('cache.tabs').data('load.tabs', url);
		return this;
	},

	length: function() {
		return this.anchors.length;
	}

});

$.extend($.ui.tabs, {
	version: '1.8.2'
});

/*
 * Tabs Extensions
 */

/*
 * Rotate
 */
$.extend($.ui.tabs.prototype, {
	rotation: null,
	rotate: function(ms, continuing) {

		var self = this, o = this.options;
		
		var rotate = self._rotate || (self._rotate = function(e) {
			clearTimeout(self.rotation);
			self.rotation = setTimeout(function() {
				var t = o.selected;
				self.select( ++t < self.anchors.length ? t : 0 );
			}, ms);
			
			if (e) {
				e.stopPropagation();
			}
		});
		
		var stop = self._unrotate || (self._unrotate = !continuing ?
			function(e) {
				if (e.clientX) { // in case of a true click
					self.rotate(null);
				}
			} :
			function(e) {
				t = o.selected;
				rotate();
			});

		// start rotation
		if (ms) {
			this.element.bind('tabsshow', rotate);
			this.anchors.bind(o.event + '.tabs', stop);
			rotate();
		}
		// stop rotation
		else {
			clearTimeout(self.rotation);
			this.element.unbind('tabsshow', rotate);
			this.anchors.unbind(o.event + '.tabs', stop);
			delete this._rotate;
			delete this._unrotate;
		}

		return this;
	}
});

})(jQuery);

//Ending order list jquery.ui.tabs.js

//Adding order list swc/xpedx/js/jquery.shorten.js

{ // a dummy block, so I can collapse all the meta stuff in the editor

	/*
	 * Shorten, a jQuery plugin to automatically shorten text to fit in a block or a pre-set width and configure how the text ends.
	 * Copyright (C) 2009-2011  Marc Diethelm
	 * License: (GPL 3, http://www.gnu.org/licenses/gpl-3.0.txt) see license.txt
	 */


	/****************************************************************************
	This jQuery plugin automatically shortens text to fit in a block or pre-set width while you can configure how the text ends. The default is an ellipsis  ("…", &hellip;, Unicode: 2026) but you can use anything you want, including markup.

	This is achieved using either of two methods: First the the text width of the 'selected' element (eg. span or div) is measured using Canvas or by placing it inside a temporary table cell. If it's too big to big to fit in the element's parent block it is shortened and measured again until it (and the appended ellipsis or text) fits inside the block. A tooltip on the 'selected' element displays the full original text.

	If the browser supports truncating text with CSS ('text-overflow:ellipsis') then that is used (but only if the text to append is the default ellipsis). http://www.w3.org/TR/2003/CR-css3-text-20030514/#text-overflow-props

	If the text is truncated by the plugin any markup in the text will be stripped (eg: "<a" starts stripping, "< a" does not). This behaviour is dictated by the jQuery .text(val) method. The appended text may contain HTML however (a link or span for example).


	Usage Example ('selecting' a div with an id of "element"):

	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
	<script type="text/javascript" src="jquery.shorten.js"></script>
	<script type="text/javascript">
	    $(function() {
	        $("#element").shorten();
	    });
	</script>


	By default the plugin will use the parent block's width as the maximum width and an ellipsis as appended text when the text is truncated.

	There are three ways of configuring the plugin:

	1) Passing a configuration hash as the plugin's argument, eg:

	.shorten({
	    width: 300,
	    tail: ' <a href="#">more</a>',
	    tooltip: false
	});

	2) Using two optional arguments (deprecated!):
	width = the desired pixel width, integer
	tail = text/html to append when truncating

	3) By changing the plugin defaults, eg:


	$.fn.shorten.defaults.tail = ' <a href="#">more</a>';


	Notes:

	There is no default width (unless you create one).

	You may want to set the element's CSS to {visibility:hidden;} so it won't
	initially flash at full width in slow browsers.

	jQuery < 1.4.4: Shorten doesn't work for elements who's parents have display:none, because .width() is broken. (Returns negative values)
	http://bugs.jquery.com/ticket/7225
	Workarounds:
	- Use jQuery 1.4.4+
	- Supply a target width in options.
	- Use better timing: Don't use display:none when shortening (maybe you can use visibility:hidden). Or shorten after changing display.

	Only supports ltr text for now.

	Tested with jQuery 1.3+


	Based on a creation by M. David Green (www.mdavidgreen.com) in 2009.

	Heavily modified/simplified/improved by Marc Diethelm (http://web5.me/).

	****************************************************************************/
	}


	(function ($) {

		//var $c = console;
		var
			_native = false,
			is_canvasTextSupported,
			measureContext, // canvas context or table cell
			measureText, // function that measures text width
			info_identifier = "shorten-info",
			options_identifier = "shorten-options";

		$.fn.shorten = function() {

			var userOptions = {},
				args = arguments, // for better minification
				func = args.callee // dito; and shorter than $.fn.shorten

			if ( args.length ) {

				if ( args[0].constructor == Object ) {
					userOptions = args[0];
				} else if ( args[0] == "options" ) {
					return $(this).eq(0).data(options_identifier);
				} else {
					userOptions = {
						width: parseInt(args[0]),
						tail: args[1]
					}
				}
			}

			this.css("visibility","hidden"); // Hide the element(s) while manipulating them

			// apply options vs. defaults
			var options = $.extend({}, func.defaults, userOptions);


			/**
			 * HERE WE GO!
			 **/
			return this.each(function () {

				var
					$this = $(this),
					text = $this.text(),
					numChars = text.length,
					targetWidth,
					tailText = $("<span/>").html(options.tail).text(), // convert html to text
					tailWidth,
					info = {
						shortened: false,
						textOverflow: false
					}

				if ($this.css("float") != "none") {
					targetWidth = options.width || $this.width(); // this let's correctly shorten text in floats, but fucks up the rest
				} else {
					targetWidth = options.width || $this.parent().width();
				}

				if (targetWidth < 0) { // jQuery versions < 1.4.4 return negative values for .width() if display:none is used.
					//$c.log("nonsense target width ", targetWidth);
					return true;
				}

				$this.data(options_identifier, options);

				// for consistency with the text-overflow method (which requires these properties), but not actually neccessary.
				if (!options.noblock)
				{
					this.style.display = "block";
				}
				//this.style.overflow = "hidden"; // firefox: a floated li will cause the ul to have a "bottom padding" if this is set.
				if (targetWidth <= $this.width())
				{
					this.style.whiteSpace = "nowrap";
				}

				// decide on a method for measuring text width
				if ( is_canvasTextSupported ) {
					//$c.log("canvas");
					measureContext = measureText_initCanvas.call( this );
					measureText = measureText_canvas;
				} else {
					//$c.log("table")
					measureContext = measureText_initTable.call( this );
					measureText = measureText_table;
				}

				var origLength = measureText.call( this, text, measureContext );

				// Try to re-trunc with original text taken from tooltip
				if ( origLength < targetWidth && this.getAttribute("title") ) {
					text = this.getAttribute("title");
					$this.text( text );
					numChars = text.length;
					origLength = measureText.call( this, text, measureContext );
				}

				if ( origLength < targetWidth ) {
					//$c.log("nothing to do");
					$this.text( text );
					this.style.visibility = "visible";

					$this.data(info_identifier, info);

					return true;
				}
				
				if ( options.tooltip && !this.getAttribute("title") ) {
					this.setAttribute("title", text);
				}

				/**
				 * If browser implements text-overflow:ellipsis in CSS and tail is &hellip;/Unicode 8230/(…), use it!
				 * In this case we're doing the measurement above to determine if we need the tooltip.
				 **/
				if ( func._native && !userOptions.width ) {
					//$c.log("css ellipsis");
					var rendered_tail = $("<span>"+options.tail+"</span>").text(); // render tail to find out if it's the ellipsis character.

					if ( rendered_tail.length == 1 && rendered_tail.charCodeAt(0) == 8230 ) {

						$this.text( text );

						// the following three properties are needed for text-overflow to work (tested in Chrome).
						// for consistency now I need to set this everywhere... which probably interferes with users' layout...
						//this.style.whiteSpace = "nowrap";
						this.style.overflow = "hidden";
						//this.style.display = "block";

						this.style[func._native] = "ellipsis";
						this.style.visibility = "visible";

						info.shortened = true;
						info.textOverflow = "ellipsis";
						$this.data(info_identifier, info);

						return true;
					}
				}
				
				tailWidth = measureText.call( this, tailText, measureContext ); // convert html to text and measure it
				targetWidth = targetWidth - tailWidth;

					//$c.log(text +" + "+ tailText);

				/**
				 * Before we start removing characters one by one, let's try to be more intelligent about this:
				 * If the original string is longer than targetWidth by at least 15% (for safety), then shorten it
				 * to targetWidth + 15% (and re-measure for safety). If the resulting text still is too long (as expected),
				 * use that for further shortening. Else use the original text. This saves a lot of time for text that is
				 * much longer than the desired width.
				 */
				var safeGuess = targetWidth * 1.15; // add 15% to targetWidth for safety before making the cut.

				if ( (origLength - safeGuess) > 0 ) { // if it's safe to cut, do it.
					
					var cut_ratio = safeGuess / origLength,
						num_guessText_chars = Math.ceil( numChars * cut_ratio ),
						// looking good: shorten and measure
						guessText = text.substring(0, num_guessText_chars),
						guessTextLength = measureText.call( this, guessText, measureContext );
						
						//$c.info("safe guess: remove " + (numChars - num_guessText_chars) +" chars");

					if ( guessTextLength > targetWidth ) { // make sure it's not too short!
						text = guessText;
						numChars = text.length;
					}
				}

				// Remove characters one by one until text width <= targetWidth
					//var count = 0;
				do {
					numChars--;
					text = text.substring(0, numChars);
						//count++;
				} while ( measureText.call( this, text, measureContext ) >= targetWidth );
				
				$this.html( $.trim( $("<span/>").text(text).html() ) + options.tail );
				this.style.visibility = "visible";
					//$c.info(count + " normal truncating cycles...")
					//$c.log("----------------------------------------------------------------------");

				info.shortened = true;
				$this.data(info_identifier, info);

				return true;
			});

			return true;

		};



		var css = document.documentElement.style;

		if ( "textOverflow" in css ) {
			_native = "textOverflow";
		} else if ( "OTextOverflow" in css ) {
			_native = "OTextOverflow";
		}

			// test for canvas support

		if ( typeof Modernizr != 'undefined' && Modernizr.canvastext ) { // if Modernizr has tested for this already use that.
			is_canvasTextSupported = Modernizr.canvastext;
		} else {
			var canvas = document.createElement("canvas");
			is_canvasTextSupported = !!(canvas.getContext && canvas.getContext("2d") && (typeof canvas.getContext("2d").fillText === 'function'));
		}
		
		$.fn.shorten._is_canvasTextSupported = is_canvasTextSupported;
		$.fn.shorten._native = _native;



		function measureText_initCanvas()
		{
			var $this = $(this);
			var canvas = document.createElement("canvas");
				//scanvas.setAttribute("width", 500); canvas.setAttribute("height", 40);
			ctx = canvas.getContext("2d");
			$this.html( canvas );

			/* the rounding is experimental. it fixes a problem with a font size specified as 0.7em which resulted in a computed size of 11.2px.
			  without rounding the measured font was too small. even with rounding the result differs slightly from the table method's results. */
			// Get the current text style. This string uses the same syntax as the CSS font specifier. The order matters!
			ctx.font = $this.css("font-style") +" "+ $this.css("font-variant") +" "+ $this.css("font-weight") +" "+ Math.ceil(parseFloat($this.css("font-size"))) +"px "+ $this.css("font-family");

			return ctx;
		}

		// measurement using canvas
		function measureText_canvas( text, ctx )
		{
				//ctx.fillStyle = "red"; ctx.fillRect (0, 0, 500, 40);
				//ctx.fillStyle = "black"; ctx.fillText(text, 0, 12);

			return ctx.measureText(text).width; // crucial, fast but called too often
		};

		function measureText_initTable()
		{
			var css = "padding:0; margin:0; border:none; font:inherit;";
			var $table = $('<table style="'+ css +'width:auto;zoom:1;position:absolute;"><tr style="'+ css +'"><td style="'+ css +'white-space:nowrap;"></td></tr></table>');
			$td = $("td", $table);

			$(this).html( $table );

			return $td;
		};

		// measurement using table
		function measureText_table( text, $td )
		{
			$td.text( text );

			return $td.width(); // crucial but expensive
		};


		$.fn.shorten.defaults = {
			tail: "&hellip;",
			tooltip: true,
			noblock: false
		};

	})(jQuery);

//End of order list swc/xpedx/js/jquery.shorten.js
	
//Adding order list swc/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js
	
	/*!
	 * jCarousel - Riding carousels with jQuery
	 *   http://sorgalla.com/jcarousel/
	 *
	 * Copyright (c) 2006 Jan Sorgalla (http://sorgalla.com)
	 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
	 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
	 *
	 * Built on top of the jQuery library
	 *   http://jquery.com
	 *
	 * Inspired by the "Carousel Component" by Bill Scott
	 *   http://billwscott.com/carousel/
	 */

	(function($){$.fn.jcarousel=function(o){if(typeof o=='string'){var instance=$(this).data('jcarousel'),args=Array.prototype.slice.call(arguments,1);return instance[o].apply(instance,args);}else
	return this.each(function(){$(this).data('jcarousel',new $jc(this,o));});};var defaults={vertical:false,start:1,offset:1,size:null,scroll:3,visible:null,animation:'normal',easing:'swing',auto:0,wrap:null,initCallback:null,reloadCallback:null,itemLoadCallback:null,itemFirstInCallback:null,itemFirstOutCallback:null,itemLastInCallback:null,itemLastOutCallback:null,itemVisibleInCallback:null,itemVisibleOutCallback:null,buttonNextHTML:'<div></div>',buttonPrevHTML:'<div></div>',buttonNextEvent:'click',buttonPrevEvent:'click',buttonNextCallback:null,buttonPrevCallback:null};$.jcarousel=function(e,o){this.options=$.extend({},defaults,o||{});this.locked=false;this.container=null;this.clip=null;this.list=null;this.buttonNext=null;this.buttonPrev=null;this.wh=!this.options.vertical?'width':'height';this.lt=!this.options.vertical?'left':'top';var skin='',split=e.className.split(' ');for(var i=0;i<split.length;i++){if(split[i].indexOf('jcarousel-skin')!=-1){$(e).removeClass(split[i]);skin=split[i];break;}}if(e.nodeName=='UL'||e.nodeName=='OL'){this.list=$(e);this.container=this.list.parent();if(this.container.hasClass('jcarousel-clip')){if(!this.container.parent().hasClass('jcarousel-container'))this.container=this.container.wrap('<div></div>');this.container=this.container.parent();}else if(!this.container.hasClass('jcarousel-container'))this.container=this.list.wrap('<div></div>').parent();}else{this.container=$(e);this.list=this.container.find('ul,ol').eq(0);}if(skin!=''&&this.container.parent()[0].className.indexOf('jcarousel-skin')==-1)this.container.wrap('<div class=" '+skin+'"></div>');this.clip=this.list.parent();if(!this.clip.length||!this.clip.hasClass('jcarousel-clip'))this.clip=this.list.wrap('<div></div>').parent();this.buttonNext=$('.jcarousel-next',this.container);if(this.buttonNext.size()==0&&this.options.buttonNextHTML!=null)this.buttonNext=this.clip.after(this.options.buttonNextHTML).next();this.buttonNext.addClass(this.className('jcarousel-next'));this.buttonPrev=$('.jcarousel-prev',this.container);if(this.buttonPrev.size()==0&&this.options.buttonPrevHTML!=null)this.buttonPrev=this.clip.after(this.options.buttonPrevHTML).next();this.buttonPrev.addClass(this.className('jcarousel-prev'));this.clip.addClass(this.className('jcarousel-clip')).css({overflow:'hidden',position:'relative'});this.list.addClass(this.className('jcarousel-list')).css({overflow:'hidden',position:'relative',top:0,left:0,margin:0,padding:0});this.container.addClass(this.className('jcarousel-container')).css({position:'relative'});var di=this.options.visible!=null?Math.ceil(this.clipping()/this.options.visible):null;var li=this.list.children('li');var self=this;if(li.size()>0){var wh=0,i=this.options.offset;li.each(function(){self.format(this,i++);wh+=self.dimension(this,di);});this.list.css(this.wh,wh+'px');if(!o||o.size===undefined)this.options.size=li.size();}this.container.css('display','block');this.buttonNext.css('display','block');this.buttonPrev.css('display','block');this.funcNext=function(){self.next();};this.funcPrev=function(){self.prev();};this.funcResize=function(){self.reload();};if(this.options.initCallback!=null)this.options.initCallback(this,'init');if($.browser.safari){this.buttons(false,false);$(window).bind('load.jcarousel',function(){self.setup();});}else
	this.setup();};var $jc=$.jcarousel;$jc.fn=$jc.prototype={jcarousel:'0.2.4'};$jc.fn.extend=$jc.extend=$.extend;$jc.fn.extend({setup:function(){this.first=null;this.last=null;this.prevFirst=null;this.prevLast=null;this.animating=false;this.timer=null;this.tail=null;this.inTail=false;if(this.locked)return;this.list.css(this.lt,this.pos(this.options.offset)+'px');var p=this.pos(this.options.start);this.prevFirst=this.prevLast=null;this.animate(p,false);$(window).unbind('resize.jcarousel',this.funcResize).bind('resize.jcarousel',this.funcResize);},reset:function(){this.list.empty();this.list.css(this.lt,'0px');this.list.css(this.wh,'10px');if(this.options.initCallback!=null)this.options.initCallback(this,'reset');this.setup();},reload:function(){if(this.tail!=null&&this.inTail)this.list.css(this.lt,$jc.intval(this.list.css(this.lt))+this.tail);this.tail=null;this.inTail=false;if(this.options.reloadCallback!=null)this.options.reloadCallback(this);if(this.options.visible!=null){var self=this;var di=Math.ceil(this.clipping()/this.options.visible),wh=0,lt=0;$('li',this.list).each(function(i){wh+=self.dimension(this,di);if(i+1<self.first)lt=wh;});this.list.css(this.wh,wh+'px');this.list.css(this.lt,-lt+'px');}this.scroll(this.first,false);},lock:function(){this.locked=true;this.buttons();},unlock:function(){this.locked=false;this.buttons();},size:function(s){if(s!=undefined){this.options.size=s;if(!this.locked)this.buttons();}return this.options.size;},has:function(i,i2){if(i2==undefined||!i2)i2=i;if(this.options.size!==null&&i2>this.options.size)i2=this.options.size;for(var j=i;j<=i2;j++){var e=this.get(j);if(!e.length||e.hasClass('jcarousel-item-placeholder'))return false;}return true;},get:function(i){return $('.jcarousel-item-'+i,this.list);},add:function(i,s){var e=this.get(i),old=0,add=0;if(e.length==0){var c,e=this.create(i),j=$jc.intval(i);while(c=this.get(--j)){if(j<=0||c.length){j<=0?this.list.prepend(e):c.after(e);break;}}}else
	old=this.dimension(e);e.removeClass(this.className('jcarousel-item-placeholder'));typeof s=='string'?e.html(s):e.empty().append(s);var di=this.options.visible!=null?Math.ceil(this.clipping()/this.options.visible):null;var wh=this.dimension(e,di)-old;if(i>0&&i<this.first)this.list.css(this.lt,$jc.intval(this.list.css(this.lt))-wh+'px');this.list.css(this.wh,$jc.intval(this.list.css(this.wh))+wh+'px');return e;},remove:function(i){var e=this.get(i);if(!e.length||(i>=this.first&&i<=this.last))return;var d=this.dimension(e);if(i<this.first)this.list.css(this.lt,$jc.intval(this.list.css(this.lt))+d+'px');e.remove();this.list.css(this.wh,$jc.intval(this.list.css(this.wh))-d+'px');},next:function(){this.stopAuto();if(this.tail!=null&&!this.inTail)this.scrollTail(false);else
	this.scroll(((this.options.wrap=='both'||this.options.wrap=='last')&&this.options.size!=null&&this.last==this.options.size)?1:this.first+this.options.scroll);},prev:function(){this.stopAuto();if(this.tail!=null&&this.inTail)this.scrollTail(true);else
	this.scroll(((this.options.wrap=='both'||this.options.wrap=='first')&&this.options.size!=null&&this.first==1)?this.options.size:this.first-this.options.scroll);},scrollTail:function(b){if(this.locked||this.animating||!this.tail)return;var pos=$jc.intval(this.list.css(this.lt));!b?pos-=this.tail:pos+=this.tail;this.inTail=!b;this.prevFirst=this.first;this.prevLast=this.last;this.animate(pos);},scroll:function(i,a){if(this.locked||this.animating)return;this.animate(this.pos(i),a);},pos:function(i){var pos=$jc.intval(this.list.css(this.lt));if(this.locked||this.animating)return pos;if(this.options.wrap!='circular')i=i<1?1:(this.options.size&&i>this.options.size?this.options.size:i);var back=this.first>i;var f=this.options.wrap!='circular'&&this.first<=1?1:this.first;var c=back?this.get(f):this.get(this.last);var j=back?f:f-1;var e=null,l=0,p=false,d=0,g;while(back?--j>=i:++j<i){e=this.get(j);p=!e.length;if(e.length==0){e=this.create(j).addClass(this.className('jcarousel-item-placeholder'));c[back?'before':'after'](e);if(this.first!=null&&this.options.wrap=='circular'&&this.options.size!==null&&(j<=0||j>this.options.size)){g=this.get(this.index(j));if(g.length)this.add(j,g.children().clone(true));}}c=e;d=this.dimension(e);if(p)l+=d;if(this.first!=null&&(this.options.wrap=='circular'||(j>=1&&(this.options.size==null||j<=this.options.size))))pos=back?pos+d:pos-d;}var clipping=this.clipping();var cache=[];var visible=0,j=i,v=0;var c=this.get(i-1);while(++visible){e=this.get(j);p=!e.length;if(e.length==0){e=this.create(j).addClass(this.className('jcarousel-item-placeholder'));c.length==0?this.list.prepend(e):c[back?'before':'after'](e);if(this.first!=null&&this.options.wrap=='circular'&&this.options.size!==null&&(j<=0||j>this.options.size)){g=this.get(this.index(j));if(g.length)this.add(j,g.find('>*').clone(true));}}c=e;var d=this.dimension(e);if(d==0){alert('jCarousel: No width/height set for items. This will cause an infinite loop. Aborting...');return 0;}if(this.options.wrap!='circular'&&this.options.size!==null&&j>this.options.size)cache.push(e);else if(p)l+=d;v+=d;if(v>=clipping)break;j++;}for(var x=0;x<cache.length;x++)cache[x].remove();if(l>0){this.list.css(this.wh,this.dimension(this.list)+l+'px');if(back){pos-=l;this.list.css(this.lt,$jc.intval(this.list.css(this.lt))-l+'px');}}var last=i+visible-1;if(this.options.wrap!='circular'&&this.options.size&&last>this.options.size)last=this.options.size;if(j>last){visible=0,j=last,v=0;while(++visible){var e=this.get(j--);if(!e.length)break;v+=this.dimension(e);if(v>=clipping)break;}}var first=last-visible+1;if(this.options.wrap!='circular'&&first<1)first=1;if(this.inTail&&back){pos+=this.tail;this.inTail=false;}this.tail=null;if(this.options.wrap!='circular'&&last==this.options.size&&(last-visible+1)>=1){var m=$jc.margin(this.get(last),!this.options.vertical?'marginRight':'marginBottom');if((v-m)>clipping)this.tail=v-clipping-m;}while(i-->first)pos+=this.dimension(this.get(i));this.prevFirst=this.first;this.prevLast=this.last;this.first=first;this.last=last;return pos;},animate:function(p,a){if(this.locked||this.animating)return;this.animating=true;var self=this;var scrolled=function(){self.animating=false;if(p==0)self.list.css(self.lt,0);if(self.options.wrap=='circular'||self.options.wrap=='both'||self.options.wrap=='last'||self.options.size==null||self.last<self.options.size)self.startAuto();self.buttons();self.notify('onAfterAnimation');};this.notify('onBeforeAnimation');if(!this.options.animation||a==false){this.list.css(this.lt,p+'px');scrolled();}else{var o=!this.options.vertical?{'left':p}:{'top':p};this.list.animate(o,this.options.animation,this.options.easing,scrolled);}},startAuto:function(s){if(s!=undefined)this.options.auto=s;if(this.options.auto==0)return this.stopAuto();if(this.timer!=null)return;var self=this;this.timer=setTimeout(function(){self.next();},this.options.auto*1000);},stopAuto:function(){if(this.timer==null)return;clearTimeout(this.timer);this.timer=null;},buttons:function(n,p){if(n==undefined||n==null){var n=!this.locked&&this.options.size!==0&&((this.options.wrap&&this.options.wrap!='first')||this.options.size==null||this.last<this.options.size);if(!this.locked&&(!this.options.wrap||this.options.wrap=='first')&&this.options.size!=null&&this.last>=this.options.size)n=this.tail!=null&&!this.inTail;}if(p==undefined||p==null){var p=!this.locked&&this.options.size!==0&&((this.options.wrap&&this.options.wrap!='last')||this.first>1);if(!this.locked&&(!this.options.wrap||this.options.wrap=='last')&&this.options.size!=null&&this.first==1)p=this.tail!=null&&this.inTail;}var self=this;this.buttonNext[n?'bind':'unbind'](this.options.buttonNextEvent+'.jcarousel',this.funcNext)[n?'removeClass':'addClass'](this.className('jcarousel-next-disabled')).attr('disabled',n?false:true);this.buttonPrev[p?'bind':'unbind'](this.options.buttonPrevEvent+'.jcarousel',this.funcPrev)[p?'removeClass':'addClass'](this.className('jcarousel-prev-disabled')).attr('disabled',p?false:true);if(this.buttonNext.length>0&&(this.buttonNext[0].jcarouselstate==undefined||this.buttonNext[0].jcarouselstate!=n)&&this.options.buttonNextCallback!=null){this.buttonNext.each(function(){self.options.buttonNextCallback(self,this,n);});this.buttonNext[0].jcarouselstate=n;}if(this.buttonPrev.length>0&&(this.buttonPrev[0].jcarouselstate==undefined||this.buttonPrev[0].jcarouselstate!=p)&&this.options.buttonPrevCallback!=null){this.buttonPrev.each(function(){self.options.buttonPrevCallback(self,this,p);});this.buttonPrev[0].jcarouselstate=p;}},notify:function(evt){var state=this.prevFirst==null?'init':(this.prevFirst<this.first?'next':'prev');this.callback('itemLoadCallback',evt,state);if(this.prevFirst!==this.first){this.callback('itemFirstInCallback',evt,state,this.first);this.callback('itemFirstOutCallback',evt,state,this.prevFirst);}if(this.prevLast!==this.last){this.callback('itemLastInCallback',evt,state,this.last);this.callback('itemLastOutCallback',evt,state,this.prevLast);}this.callback('itemVisibleInCallback',evt,state,this.first,this.last,this.prevFirst,this.prevLast);this.callback('itemVisibleOutCallback',evt,state,this.prevFirst,this.prevLast,this.first,this.last);},callback:function(cb,evt,state,i1,i2,i3,i4){if(this.options[cb]==undefined||(typeof this.options[cb]!='object'&&evt!='onAfterAnimation'))return;var callback=typeof this.options[cb]=='object'?this.options[cb][evt]:this.options[cb];if(!$.isFunction(callback))return;var self=this;if(i1===undefined)callback(self,state,evt);else if(i2===undefined)this.get(i1).each(function(){callback(self,this,i1,state,evt);});else{for(var i=i1;i<=i2;i++)if(i!==null&&!(i>=i3&&i<=i4))this.get(i).each(function(){callback(self,this,i,state,evt);});}},create:function(i){return this.format('<li></li>',i);},format:function(e,i){var $e=$(e).addClass(this.className('jcarousel-item')).addClass(this.className('jcarousel-item-'+i)).css({'float':'left','list-style':'none'});$e.attr('jcarouselindex',i);return $e;},className:function(c){return c+' '+c+(!this.options.vertical?'-horizontal':'-vertical');},dimension:function(e,d){var el=e.jquery!=undefined?e[0]:e;var old=!this.options.vertical?el.offsetWidth+$jc.margin(el,'marginLeft')+$jc.margin(el,'marginRight'):el.offsetHeight+$jc.margin(el,'marginTop')+$jc.margin(el,'marginBottom');if(d==undefined||old==d)return old;var w=!this.options.vertical?d-$jc.margin(el,'marginLeft')-$jc.margin(el,'marginRight'):d-$jc.margin(el,'marginTop')-$jc.margin(el,'marginBottom');$(el).css(this.wh,w+'px');return this.dimension(el);},clipping:function(){return!this.options.vertical?this.clip[0].offsetWidth-$jc.intval(this.clip.css('borderLeftWidth'))-$jc.intval(this.clip.css('borderRightWidth')):this.clip[0].offsetHeight-$jc.intval(this.clip.css('borderTopWidth'))-$jc.intval(this.clip.css('borderBottomWidth'));},index:function(i,s){if(s==undefined)s=this.options.size;return Math.round((((i-1)/s)-Math.floor((i-1)/s))*s)+1;}});$jc.extend({defaults:function(d){return $.extend(defaults,d||{});},margin:function(e,p){if(!e)return 0;var el=e.jquery!=undefined?e[0]:e;if(p=='marginRight'&&$.browser.safari){var old={'display':'block','float':'none','width':'auto'},oWidth,oWidth2;$.swap(el,old,function(){oWidth=el.offsetWidth;});old['marginRight']=0;$.swap(el,old,function(){oWidth2=el.offsetWidth;});return oWidth2-oWidth;}return $jc.intval($.css(el,p));},intval:function(v){v=parseInt(v);return isNaN(v)?0:v;}});})(jQuery);
	
//End of ordedr list swc/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js
	
//Add order list xpedx/js/modals/checkboxtree/jquery.checkboxtree.js
	
	/**
	 * jQuery Checkbox Tree
	 *
	 * @author Valerio Galano <valerio.galano@gmail.com>
	 *
	 * @see http://checkboxtree.googlecode.com
	 *
	 * @version 0.3.1
	 */
	(function($){

	    var checkboxTree = 0;

	    $.fn.checkboxTree = function(options) {

	        // build main options before element iteration
	        var options = $.extend({
	            checkChildren: true,
	            checkParents: true,
	            collapsable: true,
	            collapseAllButton: '',
	            collapsed: false,
	            collapseDuration: 500,
	            collapseEffect: 'blind',
	            collapseImage: '',
	            container: 'checkboxTree'+'['+ checkboxTree++ +']',
	            cssClass: 'checkboxTree',
	            expandAllButton: '',
	            expandDuration: 500,
	            expandEffect: 'blind',
	            expandImage: '',
	            leafImage: ''
	        }, options);

	        options.collapseAnchor = (options.collapseImage.length > 0) ? '<img src="'+options.collapseImage+'" />' : '-';
	        options.expandAnchor = (options.expandImage.length > 0) ? '<img src="'+options.expandImage+'" />' : '+';
	        options.leafAnchor = (options.leafImage.length > 0) ? '<img src="'+options.leafImage+'" />' : '';

	        // build collapse all button
	        if (options.collapseAllButton.length > 0) {

	            $collapseAllButton = $('<a/>', {
	                'class': options.cssClass+' all',
	                id:      options.container+'collapseAll',
	                href:    'javascript:void(0);',
	                html:    options.collapseAllButton,
	                click:   function(){
	                    $('[class*=' + options.container + '] span').each(function(){
	                        if ($(this).hasClass("expanded")) {
	                            collapse($(this), options);
	                        }
	                    });
	                }
	            });

	            this.parent().prepend($collapseAllButton);
	        }

	        // build expand all button
	        if (options.expandAllButton.length > 0) {

	            $expandAllButton = $('<a/>', {
	                'class': options.cssClass+' all',
	                id:      options.container+'expandAll',
	                href:    'javascript:void(0);',
	                html:    options.expandAllButton,
	                click:   function(){
	                    $('[class*=' + options.container + '] span').each(function(){
	                        if ($(this).hasClass("collapsed")) {
	                            expand($(this), options);
	                        }
	                    });
	                }
	            });

	            this.parent().prepend($expandAllButton);
	        }

	        // setup tree
	        $("li", this).each(function() {

	            if (options.collapsable) {

	                var $a;

	                if ($(this).is(":has(ul)")) {
	                    if (options.collapsed) {
	                        $(this).find("ul").hide();
	                        $a = $('<span></span>').html(options.expandAnchor).addClass("collapsed");
	                    } else {
	                        $a = $('<span></span>').html(options.collapseAnchor).addClass("expanded");
	                    }
	                } else {
	                     $a = $('<span></span>').html(options.leafAnchor).addClass("leaf");
	                }

	                $(this).prepend($a);
	            }

	        });

	        // handle single expand/collapse
	        this.find('span').bind("click", function(e, a){

	            if ($(this).hasClass("leaf") == undefined) {
	                return;
	            }

	            if ($(this).hasClass("collapsed")) {
	                expand($(this), options);
	            } else {
	                collapse($(this), options);
	            }
	        });

	        // handle tree select/unselect
	        this.find(':checkbox').bind("click", function(e, a) {

	            if (options.checkChildren) {
	                toggleChildren($(this));
	            }

	            if (options.checkParents && $(this).is(":checked")) {
	                checkParents($(this), options);
	            }
	        });

	        // add container class
	        this.addClass(options.container);

	        // add css class
	        this.addClass(options.cssClass);

	        return this;
	    };


	    /**
	     * Recursively check parents of passed checkbox
	     */
	    this.checkParents = function(checkbox, options)
	    {
	        var parentCheckbox = checkbox.parents("li:first").parents("li:first").find(" :checkbox:first");

	        if (!parentCheckbox.is(":checked")) {
	            parentCheckbox.attr("checked","checked");
	        }

	        if (parentCheckbox.parents('[class*=' + options.container + ']').attr('class') != undefined) {
	            checkParents(parentCheckbox, options);
	        }
	    }

	    /**
	     * Collapse tree element
	     */
	    this.collapse = function(img, options)
	    {
	        var listItem = img.parents("li:first");

	        if ($.ui !== undefined) {
	            listItem.children("ul").hide(options.collapseEffect, {}, options.collapseDuration);
	        } else {
	            listItem.children("ul").hide(options.collapseDuration);
	        }

	        listItem.children("span").html(options.expandAnchor).addClass("collapsed").removeClass("expanded");
	    }

	    /**
	     * Expand tree element
	     */
	    this.expand = function(img, options)
	    {
	        var listItem = img.parents("li:first");

	        if ($.ui !== undefined) {
	            listItem.children("ul").show(options.expandEffect, {}, options.expandDuration);
	        } else {
	            listItem.children("ul").show(options.expandDuration);
	        }

	        listItem.children("span").html(options.collapseAnchor).addClass("expanded").removeClass("collapsed");
	    }

	    /**
	     * Check/uncheck children of passed checkbox
	     */
	    this.toggleChildren = function(checkbox)
	    {
	        checkbox.parents('li:first').find('li :checkbox').attr('checked',checkbox.attr('checked') ? 'checked' : '');
	    }

	})(jQuery);

//End order list xpedx/js/modals/checkboxtree/jquery.checkboxtree.js
	
//Adding order list swc/xpedx/js/quick-add/jquery.form.js
	
	/*!
	 * jQuery Form Plugin
	 * version: 2.43 (12-MAR-2010)
	 * @requires jQuery v1.3.2 or later
	 *
	 * Examples and documentation at: http://malsup.com/jquery/form/
	 * Dual licensed under the MIT and GPL licenses:
	 *   http://www.opensource.org/licenses/mit-license.php
	 *   http://www.gnu.org/licenses/gpl.html
	 */
	;(function($) {

	/*
		Usage Note:
		-----------
		Do not use both ajaxSubmit and ajaxForm on the same form.  These
		functions are intended to be exclusive.  Use ajaxSubmit if you want
		to bind your own submit handler to the form.  For example,

		$(document).ready(function() {
			$('#myForm').bind('submit', function() {
				$(this).ajaxSubmit({
					target: '#output'
				});
				return false; // <-- important!
			});
		});

		Use ajaxForm when you want the plugin to manage all the event binding
		for you.  For example,

		$(document).ready(function() {
			$('#myForm').ajaxForm({
				target: '#output'
			});
		});

		When using ajaxForm, the ajaxSubmit function will be invoked for you
		at the appropriate time.
	*/

	/**
	 * ajaxSubmit() provides a mechanism for immediately submitting
	 * an HTML form using AJAX.
	 */
	$.fn.ajaxSubmit = function(options) {
		// fast fail if nothing selected (http://dev.jquery.com/ticket/2752)
		if (!this.length) {
			log('ajaxSubmit: skipping submit process - no element selected');
			return this;
		}

		if (typeof options == 'function')
			options = { success: options };

		var url = $.trim(this.attr('action'));
		if (url) {
			// clean url (don't include hash vaue)
			url = (url.match(/^([^#]+)/)||[])[1];
	   	}
	   	url = url || window.location.href || '';

		options = $.extend({
			url:  url,
			type: this.attr('method') || 'GET',
			iframeSrc: /^https/i.test(window.location.href || '') ? 'javascript:false' : 'about:blank'
		}, options || {});

		// hook for manipulating the form data before it is extracted;
		// convenient for use with rich editors like tinyMCE or FCKEditor
		var veto = {};
		this.trigger('form-pre-serialize', [this, options, veto]);
		if (veto.veto) {
			log('ajaxSubmit: submit vetoed via form-pre-serialize trigger');
			return this;
		}

		// provide opportunity to alter form data before it is serialized
		if (options.beforeSerialize && options.beforeSerialize(this, options) === false) {
			log('ajaxSubmit: submit aborted via beforeSerialize callback');
			return this;
		}

		var a = this.formToArray(options.semantic);
		if (options.data) {
			options.extraData = options.data;
			for (var n in options.data) {
			  if(options.data[n] instanceof Array) {
				for (var k in options.data[n])
				  a.push( { name: n, value: options.data[n][k] } );
			  }
			  else
				 a.push( { name: n, value: options.data[n] } );
			}
		}

		// give pre-submit callback an opportunity to abort the submit
		if (options.beforeSubmit && options.beforeSubmit(a, this, options) === false) {
			log('ajaxSubmit: submit aborted via beforeSubmit callback');
			return this;
		}

		// fire vetoable 'validate' event
		this.trigger('form-submit-validate', [a, this, options, veto]);
		if (veto.veto) {
			log('ajaxSubmit: submit vetoed via form-submit-validate trigger');
			return this;
		}

		var q = $.param(a);

		if (options.type.toUpperCase() == 'GET') {
			options.url += (options.url.indexOf('?') >= 0 ? '&' : '?') + q;
			options.data = null;  // data is null for 'get'
		}
		else
			options.data = q; // data is the query string for 'post'

		var $form = this, callbacks = [];
		if (options.resetForm) callbacks.push(function() { $form.resetForm(); });
		if (options.clearForm) callbacks.push(function() { $form.clearForm(); });

		// perform a load on the target only if dataType is not provided
		if (!options.dataType && options.target) {
			var oldSuccess = options.success || function(){};
			callbacks.push(function(data) {
				var fn = options.replaceTarget ? 'replaceWith' : 'html';
				$(options.target)[fn](data).each(oldSuccess, arguments);
			});
		}
		else if (options.success)
			callbacks.push(options.success);

		options.success = function(data, status, xhr) { // jQuery 1.4+ passes xhr as 3rd arg
			for (var i=0, max=callbacks.length; i < max; i++)
				callbacks[i].apply(options, [data, status, xhr || $form, $form]);
		};

		// are there files to upload?
		var files = $('input:file', this).fieldValue();
		var found = false;
		for (var j=0; j < files.length; j++)
			if (files[j])
				found = true;

		var multipart = false;
//		var mp = 'multipart/form-data';
//		multipart = ($form.attr('enctype') == mp || $form.attr('encoding') == mp);

		// options.iframe allows user to force iframe mode
		// 06-NOV-09: now defaulting to iframe mode if file input is detected
	   if ((files.length && options.iframe !== false) || options.iframe || found || multipart) {
		   // hack to fix Safari hang (thanks to Tim Molendijk for this)
		   // see:  http://groups.google.com/group/jquery-dev/browse_thread/thread/36395b7ab510dd5d
		   if (options.closeKeepAlive)
			   $.get(options.closeKeepAlive, fileUpload);
		   else
			   fileUpload();
		   }
	   else
		   $.ajax(options);

		// fire 'notify' event
		this.trigger('form-submit-notify', [this, options]);
		return this;


		// private function for handling file uploads (hat tip to YAHOO!)
		function fileUpload() {
			var form = $form[0];

			if ($(':input[name=submit]', form).length) {
				alert('Error: Form elements must not be named "submit".');
				return;
			}

			var opts = $.extend({}, $.ajaxSettings, options);
			var s = $.extend(true, {}, $.extend(true, {}, $.ajaxSettings), opts);

			var id = 'jqFormIO' + (new Date().getTime());
			var $io = $('<iframe id="' + id + '" name="' + id + '" src="'+ opts.iframeSrc +'" onload="(jQuery(this).data(\'form-plugin-onload\'))()" />');
			var io = $io[0];

			$io.css({ position: 'absolute', top: '-1000px', left: '-1000px' });

			var xhr = { // mock object
				aborted: 0,
				responseText: null,
				responseXML: null,
				status: 0,
				statusText: 'n/a',
				getAllResponseHeaders: function() {},
				getResponseHeader: function() {},
				setRequestHeader: function() {},
				abort: function() {
					this.aborted = 1;
					$io.attr('src', opts.iframeSrc); // abort op in progress
				}
			};

			var g = opts.global;
			// trigger ajax global events so that activity/block indicators work like normal
			if (g && ! $.active++) $.event.trigger("ajaxStart");
			if (g) $.event.trigger("ajaxSend", [xhr, opts]);

			if (s.beforeSend && s.beforeSend(xhr, s) === false) {
				s.global && $.active--;
				return;
			}
			if (xhr.aborted)
				return;

			var cbInvoked = false;
			var timedOut = 0;

			// add submitting element to data if we know it
			var sub = form.clk;
			if (sub) {
				var n = sub.name;
				if (n && !sub.disabled) {
					opts.extraData = opts.extraData || {};
					opts.extraData[n] = sub.value;
					if (sub.type == "image") {
						opts.extraData[n+'.x'] = form.clk_x;
						opts.extraData[n+'.y'] = form.clk_y;
					}
				}
			}

			// take a breath so that pending repaints get some cpu time before the upload starts
			function doSubmit() {
				// make sure form attrs are set
				var t = $form.attr('target'), a = $form.attr('action');

				// update form attrs in IE friendly way
				form.setAttribute('target',id);
				if (form.getAttribute('method') != 'POST')
					form.setAttribute('method', 'POST');
				if (form.getAttribute('action') != opts.url)
					form.setAttribute('action', opts.url);

				// ie borks in some cases when setting encoding
				if (! opts.skipEncodingOverride) {
					$form.attr({
						encoding: 'multipart/form-data',
						enctype:  'multipart/form-data'
					});
				}

				// support timout
				if (opts.timeout)
					setTimeout(function() { timedOut = true; cb(); }, opts.timeout);

				// add "extra" data to form if provided in options
				var extraInputs = [];
				try {
					if (opts.extraData)
						for (var n in opts.extraData)
							extraInputs.push(
								$('<input type="hidden" name="'+n+'" value="'+opts.extraData[n]+'" />')
									.appendTo(form)[0]);

					// add iframe to doc and submit the form
					$io.appendTo('body');
					$io.data('form-plugin-onload', cb);
					form.submit();
				}
				finally {
					// reset attrs and remove "extra" input elements
					form.setAttribute('action',a);
					t ? form.setAttribute('target', t) : $form.removeAttr('target');
					$(extraInputs).remove();
				}
			};

			if (opts.forceSync)
				doSubmit();
			else
				setTimeout(doSubmit, 10); // this lets dom updates render
		
			var domCheckCount = 100;

			function cb() {
				if (cbInvoked) 
					return;

				var ok = true;
				try {
					if (timedOut) throw 'timeout';
					// extract the server response from the iframe
					var data, doc;

					doc = io.contentWindow ? io.contentWindow.document : io.contentDocument ? io.contentDocument : io.document;
					
					var isXml = opts.dataType == 'xml' || doc.XMLDocument || $.isXMLDoc(doc);
					log('isXml='+isXml);
					if (!isXml && (doc.body == null || doc.body.innerHTML == '')) {
					 	if (--domCheckCount) {
							// in some browsers (Opera) the iframe DOM is not always traversable when
							// the onload callback fires, so we loop a bit to accommodate
					 		log('requeing onLoad callback, DOM not available');
							setTimeout(cb, 250);
							return;
						}
						log('Could not access iframe DOM after 100 tries.');
						return;
					}

					log('response detected');
					cbInvoked = true;
					xhr.responseText = doc.body ? doc.body.innerHTML : null;
					xhr.responseXML = doc.XMLDocument ? doc.XMLDocument : doc;
					xhr.getResponseHeader = function(header){
						var headers = {'content-type': opts.dataType};
						return headers[header];
					};

					if (opts.dataType == 'json' || opts.dataType == 'script') {
						// see if user embedded response in textarea
						var ta = doc.getElementsByTagName('textarea')[0];
						if (ta)
							xhr.responseText = ta.value;
						else {
							// account for browsers injecting pre around json response
							var pre = doc.getElementsByTagName('pre')[0];
							if (pre)
								xhr.responseText = pre.innerHTML;
						}			  
					}
					else if (opts.dataType == 'xml' && !xhr.responseXML && xhr.responseText != null) {
						xhr.responseXML = toXml(xhr.responseText);
					}
					data = $.httpData(xhr, opts.dataType);
				}
				catch(e){
					log('error caught:',e);
					ok = false;
					xhr.error = e;
					$.handleError(opts, xhr, 'error', e);
				}

				// ordering of these callbacks/triggers is odd, but that's how $.ajax does it
				if (ok) {
					opts.success(data, 'success');
					if (g) $.event.trigger("ajaxSuccess", [xhr, opts]);
				}
				if (g) $.event.trigger("ajaxComplete", [xhr, opts]);
				if (g && ! --$.active) $.event.trigger("ajaxStop");
				if (opts.complete) opts.complete(xhr, ok ? 'success' : 'error');

				// clean up
				setTimeout(function() {
					$io.removeData('form-plugin-onload');
					$io.remove();
					xhr.responseXML = null;
				}, 100);
			};

			function toXml(s, doc) {
				if (window.ActiveXObject) {
					doc = new ActiveXObject('Microsoft.XMLDOM');
					doc.async = 'false';
					doc.loadXML(s);
				}
				else
					doc = (new DOMParser()).parseFromString(s, 'text/xml');
				return (doc && doc.documentElement && doc.documentElement.tagName != 'parsererror') ? doc : null;
			};
		};
	};

	/**
	 * ajaxForm() provides a mechanism for fully automating form submission.
	 *
	 * The advantages of using this method instead of ajaxSubmit() are:
	 *
	 * 1: This method will include coordinates for <input type="image" /> elements (if the element
	 *	is used to submit the form).
	 * 2. This method will include the submit element's name/value data (for the element that was
	 *	used to submit the form).
	 * 3. This method binds the submit() method to the form for you.
	 *
	 * The options argument for ajaxForm works exactly as it does for ajaxSubmit.  ajaxForm merely
	 * passes the options argument along after properly binding events for submit elements and
	 * the form itself.
	 */
	$.fn.ajaxForm = function(options) {
		return this.ajaxFormUnbind().bind('submit.form-plugin', function(e) {
			e.preventDefault();
			$(this).ajaxSubmit(options);
		}).bind('click.form-plugin', function(e) {
			var target = e.target;
			var $el = $(target);
			if (!($el.is(":submit,input:image"))) {
				// is this a child element of the submit el?  (ex: a span within a button)
				var t = $el.closest(':submit');
				if (t.length == 0)
					return;
				target = t[0];
			}
			var form = this;
			form.clk = target;
			if (target.type == 'image') {
				if (e.offsetX != undefined) {
					form.clk_x = e.offsetX;
					form.clk_y = e.offsetY;
				} else if (typeof $.fn.offset == 'function') { // try to use dimensions plugin
					var offset = $el.offset();
					form.clk_x = e.pageX - offset.left;
					form.clk_y = e.pageY - offset.top;
				} else {
					form.clk_x = e.pageX - target.offsetLeft;
					form.clk_y = e.pageY - target.offsetTop;
				}
			}
			// clear form vars
			setTimeout(function() { form.clk = form.clk_x = form.clk_y = null; }, 100);
		});
	};

	// ajaxFormUnbind unbinds the event handlers that were bound by ajaxForm
	$.fn.ajaxFormUnbind = function() {
		return this.unbind('submit.form-plugin click.form-plugin');
	};

	/**
	 * formToArray() gathers form element data into an array of objects that can
	 * be passed to any of the following ajax functions: $.get, $.post, or load.
	 * Each object in the array has both a 'name' and 'value' property.  An example of
	 * an array for a simple login form might be:
	 *
	 * [ { name: 'username', value: 'jresig' }, { name: 'password', value: 'secret' } ]
	 *
	 * It is this array that is passed to pre-submit callback functions provided to the
	 * ajaxSubmit() and ajaxForm() methods.
	 */
	$.fn.formToArray = function(semantic) {
		var a = [];
		if (this.length == 0) return a;

		var form = this[0];
		var els = semantic ? form.getElementsByTagName('*') : form.elements;
		if (!els) return a;
		for(var i=0, max=els.length; i < max; i++) {
			var el = els[i];
			var n = el.name;
			if (!n) continue;

			if (semantic && form.clk && el.type == "image") {
				// handle image inputs on the fly when semantic == true
				if(!el.disabled && form.clk == el) {
					a.push({name: n, value: $(el).val()});
					a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
				}
				continue;
			}

			var v = $.fieldValue(el, true);
			if (v && v.constructor == Array) {
				for(var j=0, jmax=v.length; j < jmax; j++)
					a.push({name: n, value: v[j]});
			}
			else if (v !== null && typeof v != 'undefined')
				a.push({name: n, value: v});
		}

		if (!semantic && form.clk) {
			// input type=='image' are not found in elements array! handle it here
			var $input = $(form.clk), input = $input[0], n = input.name;
			if (n && !input.disabled && input.type == 'image') {
				a.push({name: n, value: $input.val()});
				a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
			}
		}
		return a;
	};

	/**
	 * Serializes form data into a 'submittable' string. This method will return a string
	 * in the format: name1=value1&amp;name2=value2
	 */
	$.fn.formSerialize = function(semantic) {
		//hand off to jQuery.param for proper encoding
		return $.param(this.formToArray(semantic));
	};

	/**
	 * Serializes all field elements in the jQuery object into a query string.
	 * This method will return a string in the format: name1=value1&amp;name2=value2
	 */
	$.fn.fieldSerialize = function(successful) {
		var a = [];
		this.each(function() {
			var n = this.name;
			if (!n) return;
			var v = $.fieldValue(this, successful);
			if (v && v.constructor == Array) {
				for (var i=0,max=v.length; i < max; i++)
					a.push({name: n, value: v[i]});
			}
			else if (v !== null && typeof v != 'undefined')
				a.push({name: this.name, value: v});
		});
		//hand off to jQuery.param for proper encoding
		return $.param(a);
	};

	/**
	 * Returns the value(s) of the element in the matched set.  For example, consider the following form:
	 *
	 *  <form><fieldset>
	 *	  <input name="A" type="text" />
	 *	  <input name="A" type="text" />
	 *	  <input name="B" type="checkbox" value="B1" />
	 *	  <input name="B" type="checkbox" value="B2"/>
	 *	  <input name="C" type="radio" value="C1" />
	 *	  <input name="C" type="radio" value="C2" />
	 *  </fieldset></form>
	 *
	 *  var v = $(':text').fieldValue();
	 *  // if no values are entered into the text inputs
	 *  v == ['','']
	 *  // if values entered into the text inputs are 'foo' and 'bar'
	 *  v == ['foo','bar']
	 *
	 *  var v = $(':checkbox').fieldValue();
	 *  // if neither checkbox is checked
	 *  v === undefined
	 *  // if both checkboxes are checked
	 *  v == ['B1', 'B2']
	 *
	 *  var v = $(':radio').fieldValue();
	 *  // if neither radio is checked
	 *  v === undefined
	 *  // if first radio is checked
	 *  v == ['C1']
	 *
	 * The successful argument controls whether or not the field element must be 'successful'
	 * (per http://www.w3.org/TR/html4/interact/forms.html#successful-controls).
	 * The default value of the successful argument is true.  If this value is false the value(s)
	 * for each element is returned.
	 *
	 * Note: This method *always* returns an array.  If no valid value can be determined the
	 *	   array will be empty, otherwise it will contain one or more values.
	 */
	$.fn.fieldValue = function(successful) {
		for (var val=[], i=0, max=this.length; i < max; i++) {
			var el = this[i];
			var v = $.fieldValue(el, successful);
			if (v === null || typeof v == 'undefined' || (v.constructor == Array && !v.length))
				continue;
			v.constructor == Array ? $.merge(val, v) : val.push(v);
		}
		return val;
	};

	/**
	 * Returns the value of the field element.
	 */
	$.fieldValue = function(el, successful) {
		var n = el.name, t = el.type, tag = el.tagName.toLowerCase();
		if (typeof successful == 'undefined') successful = true;

		if (successful && (!n || el.disabled || t == 'reset' || t == 'button' ||
			(t == 'checkbox' || t == 'radio') && !el.checked ||
			(t == 'submit' || t == 'image') && el.form && el.form.clk != el ||
			tag == 'select' && el.selectedIndex == -1))
				return null;

		if (tag == 'select') {
			var index = el.selectedIndex;
			if (index < 0) return null;
			var a = [], ops = el.options;
			var one = (t == 'select-one');
			var max = (one ? index+1 : ops.length);
			for(var i=(one ? index : 0); i < max; i++) {
				var op = ops[i];
				if (op.selected) {
					var v = op.value;
					if (!v) // extra pain for IE...
						v = (op.attributes && op.attributes['value'] && !(op.attributes['value'].specified)) ? op.text : op.value;
					if (one) return v;
					a.push(v);
				}
			}
			return a;
		}
		return el.value;
	};

	/**
	 * Clears the form data.  Takes the following actions on the form's input fields:
	 *  - input text fields will have their 'value' property set to the empty string
	 *  - select elements will have their 'selectedIndex' property set to -1
	 *  - checkbox and radio inputs will have their 'checked' property set to false
	 *  - inputs of type submit, button, reset, and hidden will *not* be effected
	 *  - button elements will *not* be effected
	 */
	$.fn.clearForm = function() {
		return this.each(function() {
			$('input,select,textarea', this).clearFields();
		});
	};

	/**
	 * Clears the selected form elements.
	 */
	$.fn.clearFields = $.fn.clearInputs = function() {
		return this.each(function() {
			var t = this.type, tag = this.tagName.toLowerCase();
			if (t == 'text' || t == 'password' || tag == 'textarea')
				this.value = '';
			else if (t == 'checkbox' || t == 'radio')
				this.checked = false;
			else if (tag == 'select')
				this.selectedIndex = -1;
		});
	};

	/**
	 * Resets the form data.  Causes all form elements to be reset to their original value.
	 */
	$.fn.resetForm = function() {
		return this.each(function() {
			// guard against an input with the name of 'reset'
			// note that IE reports the reset function as an 'object'
			if (typeof this.reset == 'function' || (typeof this.reset == 'object' && !this.reset.nodeType))
				this.reset();
		});
	};

	/**
	 * Enables or disables any matching elements.
	 */
	$.fn.enable = function(b) {
		if (b == undefined) b = true;
		return this.each(function() {
			this.disabled = !b;
		});
	};

	/**
	 * Checks/unchecks any matching checkboxes or radio buttons and
	 * selects/deselects and matching option elements.
	 */
	$.fn.selected = function(select) {
		if (select == undefined) select = true;
		return this.each(function() {
			var t = this.type;
			if (t == 'checkbox' || t == 'radio')
				this.checked = select;
			else if (this.tagName.toLowerCase() == 'option') {
				var $sel = $(this).parent('select');
				if (select && $sel[0] && $sel[0].type == 'select-one') {
					// deselect all other options
					$sel.find('option').selected(false);
				}
				this.selected = select;
			}
		});
	};

	// helper fn for console logging
	// set $.fn.ajaxSubmit.debug to true to enable debug logging
	function log() {
		if ($.fn.ajaxSubmit.debug) {
			var msg = '[jquery.form] ' + Array.prototype.join.call(arguments,'');
			if (window.console && window.console.log)
				window.console.log(msg);
			else if (window.opera && window.opera.postError)
				window.opera.postError(msg);
		}
	};

	})(jQuery);

//End order list swc/xpedx/js/quick-add/jquery.form.js
	
//Adding order list xpedx/js/fancybox/jquery.fancybox-1.3.1.js
	
	/*
	 * FancyBox - jQuery Plugin
	 * Simple and fancy lightbox alternative
	 *
	 * Examples and documentation at: http://fancybox.net
	 * 
	 * Copyright (c) 2008 - 2010 Janis Skarnelis
	 *
	 * Version: 1.3.1 (05/03/2010)
	 * Requires: jQuery v1.3+
	 *
	 * Dual licensed under the MIT and GPL licenses:
	 *   http://www.opensource.org/licenses/mit-license.php
	 *   http://www.gnu.org/licenses/gpl.html
	 */
/*
	(function($) {

		var tmp, loading, overlay, wrap, outer, inner, close, nav_left, nav_right,

			selectedIndex = 0, selectedOpts = {}, selectedArray = [], currentIndex = 0, currentOpts = {}, currentArray = [],

			ajaxLoader = null, imgPreloader = new Image(), imgRegExp = /\.(jpg|gif|png|bmp|jpeg)(.*)?$/i, swfRegExp = /[^\.]\.(swf)\s*$/i,

			loadingTimer, loadingFrame = 1,

			start_pos, final_pos, busy = false, shadow = 20, fx = $.extend($('<div/>')[0], { prop: 0 }), titleh = 0, 

			isIE6 = !$.support.opacity && !window.XMLHttpRequest,

			/*
			 * Private methods 
			 */
	/*
			fancybox_abort = function() {
				loading.hide();

				imgPreloader.onerror = imgPreloader.onload = null;

				if (ajaxLoader) {
					ajaxLoader.abort();
				}

				tmp.empty();
			},

			fancybox_error = function() {
				$.fancybox('<p id="fancybox_error">The requested content cannot be loaded.<br />Please try again later.</p>', {
					'scrolling'		: 'no',
					'padding'		: 20,
					'transitionIn'	: 'none',
					'transitionOut'	: 'none'
				});
			},

			fancybox_get_viewport = function() {
				return [ $(window).width(), $(window).height(), $(document).scrollLeft(), $(document).scrollTop() ];
			},

			fancybox_get_zoom_to = function () {
				var view	= fancybox_get_viewport(),
					to		= {},

					margin = currentOpts.margin,
					resize = currentOpts.autoScale,

					horizontal_space	= (shadow + margin) * 2,
					vertical_space		= (shadow + margin) * 2,
					double_padding		= (currentOpts.padding * 2),
					
					ratio;

				if (currentOpts.width.toString().indexOf('%') > -1) {
					to.width = ((view[0] * parseFloat(currentOpts.width)) / 100) - (shadow * 2) ;
					resize = false;

				} else {
					to.width = currentOpts.width + double_padding;
				}

				if (currentOpts.height.toString().indexOf('%') > -1) {
					to.height = ((view[1] * parseFloat(currentOpts.height)) / 100) - (shadow * 2);
					resize = false;

				} else {
					to.height = currentOpts.height + double_padding;
				}

				if (resize && (to.width > (view[0] - horizontal_space) || to.height > (view[1] - vertical_space))) {
					if (selectedOpts.type == 'image' || selectedOpts.type == 'swf') {
						horizontal_space	+= double_padding;
						vertical_space		+= double_padding;

						ratio = Math.min(Math.min( view[0] - horizontal_space, currentOpts.width) / currentOpts.width, Math.min( view[1] - vertical_space, currentOpts.height) / currentOpts.height);

						to.width	= Math.round(ratio * (to.width	- double_padding)) + double_padding;
						to.height	= Math.round(ratio * (to.height	- double_padding)) + double_padding;

					} else {
						to.width	= Math.min(to.width,	(view[0] - horizontal_space));
						to.height	= Math.min(to.height,	(view[1] - vertical_space));
					}
				}

				to.top	= view[3] + ((view[1] - (to.height	+ (shadow * 2 ))) * 0.5);
				to.left	= view[2] + ((view[0] - (to.width	+ (shadow * 2 ))) * 0.5);

				if (currentOpts.autoScale === false) {
					to.top	= Math.max(view[3] + margin, to.top);
					to.left	= Math.max(view[2] + margin, to.left);
				}

				return to;
			},

			fancybox_format_title = function(title) {
				if (title && title.length) {
					switch (currentOpts.titlePosition) {
						case 'inside':
							return title;
						case 'over':
							return '<span id="fancybox-title-over">' + title + '</span>';
						default:
							return '<span id="fancybox-title-wrap"><span id="fancybox-title-left"></span><span id="fancybox-title-main">' + title + '</span><span id="fancybox-title-right"></span></span>';
					}
				}

				return false;
			},

			fancybox_process_title = function() {
				var title	= currentOpts.title,
					width	= final_pos.width - (currentOpts.padding * 2),
					titlec	= 'fancybox-title-' + currentOpts.titlePosition;
					
				$('#fancybox-title').remove();

				titleh = 0;

				if (currentOpts.titleShow === false) {
					return;
				}

				title = $.isFunction(currentOpts.titleFormat) ? currentOpts.titleFormat(title, currentArray, currentIndex, currentOpts) : fancybox_format_title(title);

				if (!title || title === '') {
					return;
				}

				$('<div id="fancybox-title" class="' + titlec + '" />').css({
					'width'			: width,
					'paddingLeft'	: currentOpts.padding,
					'paddingRight'	: currentOpts.padding
				}).html(title).appendTo('body');

				switch (currentOpts.titlePosition) {
					case 'inside':
						titleh = $("#fancybox-title").outerHeight(true) - currentOpts.padding;
						final_pos.height += titleh;
					break;

					case 'over':
						$('#fancybox-title').css('bottom', currentOpts.padding);
					break;

					default:
						$('#fancybox-title').css('bottom', $("#fancybox-title").outerHeight(true) * -1);
					break;
				}

				$('#fancybox-title').appendTo( outer ).hide();
			},

			fancybox_set_navigation = function() {
				$(document).unbind('keydown.fb').bind('keydown.fb', function(e) {
					if (e.keyCode == 27 && currentOpts.enableEscapeButton) {
						e.preventDefault();
						$.fancybox.close();

					} else if (e.keyCode == 37) {
						e.preventDefault();
						$.fancybox.prev();

					} else if (e.keyCode == 39) {
						e.preventDefault();
						$.fancybox.next();
					}
				});

				if ($.fn.mousewheel) {
					wrap.unbind('mousewheel.fb');

					if (currentArray.length > 1) {
						wrap.bind('mousewheel.fb', function(e, delta) {
							e.preventDefault();

							if (busy || delta === 0) {
								return;
							}

							if (delta > 0) {
								$.fancybox.prev();
							} else {
								$.fancybox.next();
							}
						});
					}
				}

				if (!currentOpts.showNavArrows) { return; }

				if ((currentOpts.cyclic && currentArray.length > 1) || currentIndex !== 0) {
					nav_left.show();
				}

				if ((currentOpts.cyclic && currentArray.length > 1) || currentIndex != (currentArray.length -1)) {
					nav_right.show();
				}
			},

			fancybox_preload_images = function() {
				var href, 
					objNext;
					
				if ((currentArray.length -1) > currentIndex) {
					href = currentArray[ currentIndex + 1 ].href;

					if (typeof href !== 'undefined' && href.match(imgRegExp)) {
						objNext = new Image();
						objNext.src = href;
					}
				}

				if (currentIndex > 0) {
					href = currentArray[ currentIndex - 1 ].href;

					if (typeof href !== 'undefined' && href.match(imgRegExp)) {
						objNext = new Image();
						objNext.src = href;
					}
				}
			},

			_finish = function () {
				inner.css('overflow', (currentOpts.scrolling == 'auto' ? (currentOpts.type == 'image' || currentOpts.type == 'iframe' || currentOpts.type == 'swf' ? 'hidden' : 'auto') : (currentOpts.scrolling == 'yes' ? 'auto' : 'visible')));

				if (!$.support.opacity) {
					inner.get(0).style.removeAttribute('filter');
					wrap.get(0).style.removeAttribute('filter');
				}

				$('#fancybox-title').show();

				if (currentOpts.hideOnContentClick)	{
					inner.one('click', $.fancybox.close);
				}
				if (currentOpts.hideOnOverlayClick)	{
					overlay.one('click', $.fancybox.close);
				}

				if (currentOpts.showCloseButton) {
					close.show();
				}

				fancybox_set_navigation();

				$(window).bind("resize.fb", $.fancybox.center);

				if (currentOpts.centerOnScroll) {
					$(window).bind("scroll.fb", $.fancybox.center);
				} else {
					$(window).unbind("scroll.fb");
				}

				if ($.isFunction(currentOpts.onComplete)) {
					currentOpts.onComplete(currentArray, currentIndex, currentOpts);
				}

				busy = false;

				fancybox_preload_images();
			},

			fancybox_draw = function(pos) {
				var width	= Math.round(start_pos.width	+ (final_pos.width	- start_pos.width)	* pos),
					height	= Math.round(start_pos.height	+ (final_pos.height	- start_pos.height)	* pos),

					top		= Math.round(start_pos.top	+ (final_pos.top	- start_pos.top)	* pos),
					left	= Math.round(start_pos.left	+ (final_pos.left	- start_pos.left)	* pos);

				wrap.css({
					'width'		: width		+ 'px',
					'height'	: height	+ 'px',
					'top'		: top		+ 'px',
					'left'		: left		+ 'px'
				});

				width	= Math.max(width - currentOpts.padding * 2, 0);
				height	= Math.max(height - (currentOpts.padding * 2 + (titleh * pos)), 0);

				inner.css({
					'width'		: width		+ 'px',
					'height'	: height	+ 'px'
				});

				if (typeof final_pos.opacity !== 'undefined') {
					wrap.css('opacity', (pos < 0.5 ? 0.5 : pos));
				}
			},

			fancybox_get_obj_pos = function(obj) {
				var pos		= obj.offset();

				pos.top		+= parseFloat( obj.css('paddingTop') )	|| 0;
				pos.left	+= parseFloat( obj.css('paddingLeft') )	|| 0;

				pos.top		+= parseFloat( obj.css('border-top-width') )	|| 0;
				pos.left	+= parseFloat( obj.css('border-left-width') )	|| 0;

				pos.width	= obj.width();
				pos.height	= obj.height();

				return pos;
			},

			fancybox_get_zoom_from = function() {
				var orig = selectedOpts.orig ? $(selectedOpts.orig) : false,
					from = {},
					pos,
					view;

				if (orig && orig.length) {
					pos = fancybox_get_obj_pos(orig);

					from = {
						width	: (pos.width	+ (currentOpts.padding * 2)),
						height	: (pos.height	+ (currentOpts.padding * 2)),
						top		: (pos.top		- currentOpts.padding - shadow),
						left	: (pos.left		- currentOpts.padding - shadow)
					};
					
				} else {
					view = fancybox_get_viewport();

					from = {
						width	: 1,
						height	: 1,
						top		: view[3] + view[1] * 0.5,
						left	: view[2] + view[0] * 0.5
					};
				}

				return from;
			},

			fancybox_show = function() {
				loading.hide();

				if (wrap.is(":visible") && $.isFunction(currentOpts.onCleanup)) {
					if (currentOpts.onCleanup(currentArray, currentIndex, currentOpts) === false) {
						$.event.trigger('fancybox-cancel');

						busy = false;
						return;
					}
				}

				currentArray	= selectedArray;
				currentIndex	= selectedIndex;
				currentOpts		= selectedOpts;

				inner.get(0).scrollTop	= 0;
				inner.get(0).scrollLeft	= 0;

				if (currentOpts.overlayShow) {
					if (isIE6) {
						$('select:not(#fancybox-tmp select)').filter(function() {
							return this.style.visibility !== 'hidden';
						}).css({'visibility':'hidden'}).one('fancybox-cleanup', function() {
							this.style.visibility = 'inherit';
						});
					}

					overlay.css({
						'background-color'	: currentOpts.overlayColor,
						'opacity'			: currentOpts.overlayOpacity
					}).unbind().show();
				}

				final_pos = fancybox_get_zoom_to();

				fancybox_process_title();

				if (wrap.is(":visible")) {
					$( close.add( nav_left ).add( nav_right ) ).hide();

					var pos = wrap.position(),
						equal;

					start_pos = {
						top		:	pos.top ,
						left	:	pos.left,
						width	:	wrap.width(),
						height	:	wrap.height()
					};

					equal = (start_pos.width == final_pos.width && start_pos.height == final_pos.height);

					inner.fadeOut(currentOpts.changeFade, function() {
						var finish_resizing = function() {
							inner.html( tmp.contents() ).fadeIn(currentOpts.changeFade, _finish);
						};
						
						$.event.trigger('fancybox-change');

						inner.empty().css('overflow', 'hidden');

						if (equal) {
							inner.css({
								top			: currentOpts.padding,
								left		: currentOpts.padding,
								width		: Math.max(final_pos.width	- (currentOpts.padding * 2), 1),
								height		: Math.max(final_pos.height	- (currentOpts.padding * 2) - titleh, 1)
							});
							
							finish_resizing();

						} else {
							inner.css({
								top			: currentOpts.padding,
								left		: currentOpts.padding,
								width		: Math.max(start_pos.width	- (currentOpts.padding * 2), 1),
								height		: Math.max(start_pos.height	- (currentOpts.padding * 2), 1)
							});
							
							fx.prop = 0;

							$(fx).animate({ prop: 1 }, {
								 duration	: currentOpts.changeSpeed,
								 easing		: currentOpts.easingChange,
								 step		: fancybox_draw,
								 complete	: finish_resizing
							});
						}
					});

					return;
				}

				wrap.css('opacity', 1);

				if (currentOpts.transitionIn == 'elastic') {
					start_pos = fancybox_get_zoom_from();

					inner.css({
							top			: currentOpts.padding,
							left		: currentOpts.padding,
							width		: Math.max(start_pos.width	- (currentOpts.padding * 2), 1),
							height		: Math.max(start_pos.height	- (currentOpts.padding * 2), 1)
						})
						.html( tmp.contents() );

					wrap.css(start_pos).show();

					if (currentOpts.opacity) {
						final_pos.opacity = 0;
					}

					fx.prop = 0;

					$(fx).animate({ prop: 1 }, {
						 duration	: currentOpts.speedIn,
						 easing		: currentOpts.easingIn,
						 step		: fancybox_draw,
						 complete	: _finish
					});

				} else {
					inner.css({
							top			: currentOpts.padding,
							left		: currentOpts.padding,
							width		: Math.max(final_pos.width	- (currentOpts.padding * 2), 1),
							height		: Math.max(final_pos.height	- (currentOpts.padding * 2) - titleh, 1)
						})
						.html( tmp.contents() );

					wrap.css( final_pos ).fadeIn( currentOpts.transitionIn == 'none' ? 0 : currentOpts.speedIn, _finish );
				}
			},

			fancybox_process_inline = function() {
				tmp.width(	selectedOpts.width );
				tmp.height(	selectedOpts.height );

				if (selectedOpts.width	== 'auto') {
					selectedOpts.width = tmp.width();
				}
				if (selectedOpts.height	== 'auto') {
					selectedOpts.height	= tmp.height();
				}

				fancybox_show();
			},
			
			fancybox_process_image = function() {
				busy = true;

				selectedOpts.width	= imgPreloader.width;
				selectedOpts.height	= imgPreloader.height;

				$("<img />").attr({
					'id'	: 'fancybox-img',
					'src'	: imgPreloader.src,
					'alt'	: selectedOpts.title
				}).appendTo( tmp );

				fancybox_show();
			},

			fancybox_start = function() {
				fancybox_abort();

				var obj	= selectedArray[ selectedIndex ],
					href, 
					type, 
					title,
					str,
					emb,
					selector,
					data;

				selectedOpts = $.extend({}, $.fn.fancybox.defaults, (typeof $(obj).data('fancybox') == 'undefined' ? selectedOpts : $(obj).data('fancybox')));
				title = obj.title || $(obj).title || selectedOpts.title || '';
				
				if (obj.nodeName && !selectedOpts.orig) {
					selectedOpts.orig = $(obj).children("img:first").length ? $(obj).children("img:first") : $(obj);
				}

				if (title === '' && selectedOpts.orig) {
					title = selectedOpts.orig.attr('alt');
				}

				if (obj.nodeName && (/^(?:javascript|#)/i).test(obj.href)) {
					href = selectedOpts.href || null;
				} else {
					href = selectedOpts.href || obj.href || null;
				}

				if (selectedOpts.type) {
					type = selectedOpts.type;

					if (!href) {
						href = selectedOpts.content;
					}
					
				} else if (selectedOpts.content) {
					type	= 'html';

				} else if (href) {
					if (href.match(imgRegExp)) {
						type = 'image';

					} else if (href.match(swfRegExp)) {
						type = 'swf';

					} else if ($(obj).hasClass("iframe")) {
						type = 'iframe';

					} else if (href.match(/#/)) {
						obj = href.substr(href.indexOf("#"));

						type = $(obj).length > 0 ? 'inline' : 'ajax';
					} else {
						type = 'ajax';
					}
				} else {
					type = 'inline';
				}

				selectedOpts.type	= type;
				selectedOpts.href	= href;
				selectedOpts.title	= title;

				if (selectedOpts.autoDimensions && selectedOpts.type !== 'iframe' && selectedOpts.type !== 'swf') {
					selectedOpts.width		= 'auto';
					selectedOpts.height		= 'auto';
				}

				if (selectedOpts.modal) {
					selectedOpts.overlayShow		= true;
					selectedOpts.hideOnOverlayClick	= false;
					selectedOpts.hideOnContentClick	= false;
					selectedOpts.enableEscapeButton	= false;
					selectedOpts.showCloseButton	= false;
				}

				if ($.isFunction(selectedOpts.onStart)) {
					if (selectedOpts.onStart(selectedArray, selectedIndex, selectedOpts) === false) {
						busy = false;
						return;
					}
				}

				tmp.css('padding', (shadow + selectedOpts.padding + selectedOpts.margin));

				$('.fancybox-inline-tmp').unbind('fancybox-cancel').bind('fancybox-change', function() {
					$(this).replaceWith(inner.children());
				});

				switch (type) {
					case 'html' :
						tmp.html( selectedOpts.content );
						fancybox_process_inline();
					break;

					case 'inline' :
						$('<div class="fancybox-inline-tmp" />').hide().insertBefore( $(obj) ).bind('fancybox-cleanup', function() {
							$(this).replaceWith(inner.children());
						}).bind('fancybox-cancel', function() {
							$(this).replaceWith(tmp.children());
						});

						$(obj).appendTo(tmp);

						fancybox_process_inline();
					break;

					case 'image':
						busy = false;

						$.fancybox.showActivity();

						imgPreloader = new Image();

						imgPreloader.onerror = function() {
							fancybox_error();
						};

						imgPreloader.onload = function() {
							imgPreloader.onerror = null;
							imgPreloader.onload = null;
							fancybox_process_image();
						};

						imgPreloader.src = href;
			
					break;

					case 'swf':
						str = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="' + selectedOpts.width + '" height="' + selectedOpts.height + '"><param name="movie" value="' + href + '"></param>';
						emb = '';
						
						$.each(selectedOpts.swf, function(name, val) {
							str += '<param name="' + name + '" value="' + val + '"></param>';
							emb += ' ' + name + '="' + val + '"';
						});

						str += '<embed src="' + href + '" type="application/x-shockwave-flash" width="' + selectedOpts.width + '" height="' + selectedOpts.height + '"' + emb + '></embed></object>';

						tmp.html(str);

						fancybox_process_inline();
					break;

					case 'ajax':
						selector	= href.split('#', 2);
						data		= selectedOpts.ajax.data || {};

						if (selector.length > 1) {
							href = selector[0];

							if (typeof data == "string") {
								data += '&selector=' + selector[1];
							} else {
								data.selector = selector[1];
							}
						}

						busy = false;
						$.fancybox.showActivity();

						ajaxLoader = $.ajax($.extend(selectedOpts.ajax, {
							url		: href,
							data	: data,
							error	: fancybox_error,
							success : function(data, textStatus, XMLHttpRequest) {
								if (ajaxLoader.status == 200) {
									tmp.html( data );
									fancybox_process_inline();
								}
							}
						}));

					break;

					case 'iframe' :
						$('<iframe id="fancybox-frame" name="fancybox-frame' + new Date().getTime() + '" frameborder="0" hspace="0" scrolling="' + selectedOpts.scrolling + '" src="' + selectedOpts.href + '"></iframe>').appendTo(tmp);
						fancybox_show();
					break;
				}
			},

			fancybox_animate_loading = function() {
				if (!loading.is(':visible')){
					clearInterval(loadingTimer);
					return;
				}

				$('div', loading).css('top', (loadingFrame * -40) + 'px');

				loadingFrame = (loadingFrame + 1) % 12;
			},

			fancybox_init = function() {
				if ($("#fancybox-wrap").length) {
					return;
				}

				$('body').append(
					tmp			= $('<div id="fancybox-tmp"></div>'),
					loading		= $('<div id="fancybox-loading"><div></div></div>'),
					overlay		= $('<div id="fancybox-overlay"></div>'),
					wrap		= $('<div id="fancybox-wrap"></div>')
				);

				if (!$.support.opacity) {
					wrap.addClass('fancybox-ie');
					loading.addClass('fancybox-ie');
				}

				outer = $('<div id="fancybox-outer"></div>')
					.append('<div class="fancy-bg" id="fancy-bg-n"></div><div class="fancy-bg" id="fancy-bg-ne"></div><div class="fancy-bg" id="fancy-bg-e"></div><div class="fancy-bg" id="fancy-bg-se"></div><div class="fancy-bg" id="fancy-bg-s"></div><div class="fancy-bg" id="fancy-bg-sw"></div><div class="fancy-bg" id="fancy-bg-w"></div><div class="fancy-bg" id="fancy-bg-nw"></div>')
					.appendTo( wrap );

				outer.append(
					inner		= $('<div id="fancybox-inner"></div>'),
					close		= $('<a id="fancybox-close"></a>'),

					nav_left	= $('<a href="javascript:;" id="fancybox-left"><span class="fancy-ico" id="fancybox-left-ico"></span></a>'),
					nav_right	= $('<a href="javascript:;" id="fancybox-right"><span class="fancy-ico" id="fancybox-right-ico"></span></a>')
				);

				close.click($.fancybox.close);
				loading.click($.fancybox.cancel);

				nav_left.click(function(e) {
					e.preventDefault();
					$.fancybox.prev();
				});

				nav_right.click(function(e) {
					e.preventDefault();
					$.fancybox.next();
				});

				if (isIE6) {
					overlay.get(0).style.setExpression('height',	"document.body.scrollHeight > document.body.offsetHeight ? document.body.scrollHeight : document.body.offsetHeight + 'px'");
					loading.get(0).style.setExpression('top',		"(-20 + (document.documentElement.clientHeight ? document.documentElement.clientHeight/2 : document.body.clientHeight/2 ) + ( ignoreMe = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop )) + 'px'");

					outer.prepend('<iframe id="fancybox-hide-sel-frame" src="javascript:\'\';" scrolling="no" frameborder="0" ></iframe>');
				}
			};

		/*
		 * Public methods 
		 */

		/*$.fn.fancybox = function(options) {
			$(this)
				.data('fancybox', $.extend({}, options, ($.metadata ? $(this).metadata() : {})))
				.unbind('click.fb').bind('click.fb', function(e) {
					e.preventDefault();

					if (busy) {
						return;
					}

					busy = true;

					$(this).blur();

					selectedArray	= [];
					selectedIndex	= 0;

					var rel = $(this).attr('rel') || '';

					if (!rel || rel == '' || rel === 'nofollow') {
						selectedArray.push(this);

					} else {
						selectedArray	= $("a[rel=" + rel + "], area[rel=" + rel + "]");
						selectedIndex	= selectedArray.index( this );
					}

					fancybox_start();

					return false;
				});

			return this;
		};

		$.fancybox = function(obj) {
			if (busy) {
				return;
			}

			busy = true;

			var opts = typeof arguments[1] !== 'undefined' ? arguments[1] : {};

			selectedArray	= [];
			selectedIndex	= opts.index || 0;

			if ($.isArray(obj)) {
				for (var i = 0, j = obj.length; i < j; i++) {
					if (typeof obj[i] == 'object') {
						$(obj[i]).data('fancybox', $.extend({}, opts, obj[i]));
					} else {
						obj[i] = $({}).data('fancybox', $.extend({content : obj[i]}, opts));
					}
				}

				selectedArray = jQuery.merge(selectedArray, obj);

			} else {
				if (typeof obj == 'object') {
					$(obj).data('fancybox', $.extend({}, opts, obj));
				} else {
					obj = $({}).data('fancybox', $.extend({content : obj}, opts));
				}

				selectedArray.push(obj);
			}

			if (selectedIndex > selectedArray.length || selectedIndex < 0) {
				selectedIndex = 0;
			}

			fancybox_start();
		};

		$.fancybox.showActivity = function() {
			clearInterval(loadingTimer);

			loading.show();
			loadingTimer = setInterval(fancybox_animate_loading, 66);
		};

		$.fancybox.hideActivity = function() {
			loading.hide();
		};

		$.fancybox.next = function() {
			return $.fancybox.pos( currentIndex + 1);
		};
		
		$.fancybox.prev = function() {
			return $.fancybox.pos( currentIndex - 1);
		};

		$.fancybox.pos = function(pos) {
			if (busy) {
				return;
			}

			pos = parseInt(pos, 10);

			if (pos > -1 && currentArray.length > pos) {
				selectedIndex = pos;
				fancybox_start();
			}

			if (currentOpts.cyclic && currentArray.length > 1 && pos < 0) {
				selectedIndex = currentArray.length - 1;
				fancybox_start();
			}

			if (currentOpts.cyclic && currentArray.length > 1 && pos >= currentArray.length) {
				selectedIndex = 0;
				fancybox_start();
			}

			return;
		};

		$.fancybox.cancel = function() {
			if (busy) {
				return;
			}

			busy = true;

			$.event.trigger('fancybox-cancel');

			fancybox_abort();

			if (selectedOpts && $.isFunction(selectedOpts.onCancel)) {
				selectedOpts.onCancel(selectedArray, selectedIndex, selectedOpts);
			}

			busy = false;
		};

		// Note: within an iframe use - parent.$.fancybox.close();
		$.fancybox.close = function() {
			if (busy || wrap.is(':hidden')) {
				return;
			}

			busy = true;

			if (currentOpts && $.isFunction(currentOpts.onCleanup)) {
				if (currentOpts.onCleanup(currentArray, currentIndex, currentOpts) === false) {
					busy = false;
					return;
				}
			}

			fancybox_abort();

			$(close.add( nav_left ).add( nav_right )).hide();

			$('#fancybox-title').remove();

			wrap.add(inner).add(overlay).unbind();

			$(window).unbind("resize.fb scroll.fb");
			$(document).unbind('keydown.fb');

			function _cleanup() {
				overlay.fadeOut('fast');

				wrap.hide();

				$.event.trigger('fancybox-cleanup');

				inner.empty();

				if ($.isFunction(currentOpts.onClosed)) {
					currentOpts.onClosed(currentArray, currentIndex, currentOpts);
				}

				currentArray	= selectedOpts	= [];
				currentIndex	= selectedIndex	= 0;
				currentOpts		= selectedOpts	= {};

				busy = false;
			}

			inner.css('overflow', 'hidden');

			if (currentOpts.transitionOut == 'elastic') {
				start_pos = fancybox_get_zoom_from();

				var pos = wrap.position();

				final_pos = {
					top		:	pos.top ,
					left	:	pos.left,
					width	:	wrap.width(),
					height	:	wrap.height()
				};

				if (currentOpts.opacity) {
					final_pos.opacity = 1;
				}

				fx.prop = 1;

				$(fx).animate({ prop: 0 }, {
					 duration	: currentOpts.speedOut,
					 easing		: currentOpts.easingOut,
					 step		: fancybox_draw,
					 complete	: _cleanup
				});

			} else {
				wrap.fadeOut( currentOpts.transitionOut == 'none' ? 0 : currentOpts.speedOut, _cleanup);
			}
		};

		$.fancybox.resize = function() {
			var c, h;
			
			if (busy || wrap.is(':hidden')) {
				return;
			}

			busy = true;

			c = inner.wrapInner("<div style='overflow:auto'></div>").children();
			h = c.height();

			wrap.css({height:	h + (currentOpts.padding * 2) + titleh});
			inner.css({height:	h});

			c.replaceWith(c.children());

			$.fancybox.center();
		};

		$.fancybox.center = function() {
			busy = true;

			var view	= fancybox_get_viewport(),
				margin	= currentOpts.margin,
				to		= {};

			to.top	= view[3] + ((view[1] - ((wrap.height() - titleh) + (shadow * 2 ))) * 0.5);
			to.left	= view[2] + ((view[0] - (wrap.width() + (shadow * 2 ))) * 0.5);

			to.top	= Math.max(view[3] + margin, to.top);
			to.left	= Math.max(view[2] + margin, to.left);

			wrap.css(to);

			busy = false;
		};

		$.fn.fancybox.defaults = {
			padding				:	10,
			margin				:	20,
			opacity				:	false,
			modal				:	false,
			cyclic				:	false,
			scrolling			:	'auto',	// 'auto', 'yes' or 'no'

			width				:	560,
			height				:	340,

			autoScale			:	true,
			autoDimensions		:	true,
			centerOnScroll		:	false,

			ajax				:	{},
			swf					:	{ wmode: 'transparent' },

			hideOnOverlayClick	:	true,
			hideOnContentClick	:	false,

			overlayShow			:	true,
			overlayOpacity		:	0.3,
			overlayColor		:	'#666',

			titleShow			:	true,
			titlePosition		:	'outside',	// 'outside', 'inside' or 'over'
			titleFormat			:	null,

			transitionIn		:	'fade',	// 'elastic', 'fade' or 'none'
			transitionOut		:	'fade',	// 'elastic', 'fade' or 'none'

			speedIn				:	300,
			speedOut			:	300,

			changeSpeed			:	300,
			changeFade			:	'fast',

			easingIn			:	'swing',
			easingOut			:	'swing',

			showCloseButton		:	true,
			showNavArrows		:	true,
			enableEscapeButton	:	true,

			onStart				:	null,
			onCancel			:	null,
			onComplete			:	null,
			onCleanup			:	null,
			onClosed			:	null
		};

		$(document).ready(function() {
			fancybox_init();
		});

	})(jQuery);*/
//End of order list xpedx/js/fancybox/jquery.fancybox-1.3.1.js
	

$(document).ready(function() {
	$("#varous1").fancybox();
	$("#various2").fancybox();
	$("#various3").fancybox();
	$("#various4").fancybox();
	$("#various98").fancybox();
	$("#various99").fancybox();
	$("#import-lightbox").fancybox();
	$(".modal").fancybox();
	$(document).pngFix();
//	$('.jq-shorten').shorten();
	
	/* $('#tree').checkboxTree();
	$('#collapseAllButtonsTree').checkboxTree({
			collapseAllButton: 'Collapse all',
			expandAllButton: 'Expand all'
	}); */
	
	//for div alt row zebra stripes
	//$("div.mil-wrap-condensed-container:odd").css("background-color", "#fafafa");
	//$("div.mil-wrap-condensed-container:even").css("background-color", "#fff");
	
	$(".btn-slide").click(function(){
		$("#panel").slideToggle("slow");
		$(this).toggleClass("slide"); return false;
	});
});
/*  roundy corners for IE */ 
	/* IE only */
//	DD_roundies.addRule('.mil-wrap-condensed', '6px 6px 0px 0px');
//	DD_roundies.addRule('.mil-wrap-condensed-bot', '0px 0px 6px 6px');

function expandPriceAndAvailability (el)
{
	jQuery(el).parents("dd").find('.price-and-availability').toggle();
	return false;
}

function showCost (el)
{
	if(el.innerHTML == '[Show]')
	  el.innerHTML = '[Hide]';
	else
	  el.innerHTML = '[Show]';
	  
	jQuery('#cost').toggle();
	
	return false;
}

function toggleCost(id)
{
   var e = document.getElementById(id);
   if(e.innerHTML = 'N/A')
	  e.style.display = 'none';
   else
	  e.style.display = 'block';

}
function restrictTextareaMaxLength(Object, maxLen){
 var txtTextArea = Object.value;
 
 if( txtTextArea.length > maxLen ) { 
 alert(Object.title + ' should not exceed '+ maxLen + ' characters');
 var txtTruckTextArea = txtTextArea.substring(0, maxLen);
 Object.value = txtTruckTextArea;
 return false;
 }
 
 return true;
 
}
/*CR 3999 Start-Done the changes*/
function restrictTextareaMaxLengthAlert(Object, maxLen){
	 var txtTextArea = Object.value;

if( txtTextArea.length > maxLen ) { 
var txtTruckTextArea = txtTextArea.substring(0, maxLen);
Object.value = txtTruckTextArea;
return false;
}

return true;

}
/*CR 3999 End */
//added for jira 3241

function doGetCaretPosition (ctrl) {

var CaretPos = 0;
// IE Support
if (document.selection) {

ctrl.focus ();
var Sel = document.selection.createRange ();

Sel.moveStart ('character', -ctrl.value.length);

CaretPos = Sel.text.length;
}
// Firefox support
else if (ctrl.selectionStart || ctrl.selectionStart == '0')
CaretPos = ctrl.selectionStart;

return (CaretPos);

}


function setCaretPosition(ctrl, pos)
{

if(ctrl.setSelectionRange)
{
ctrl.focus();
ctrl.setSelectionRange(pos,pos);
}
else if (ctrl.createTextRange) {
var range = ctrl.createTextRange();
range.collapse(true);
range.moveEnd('character', pos);
range.moveStart('character', pos);
range.select();
}
}

function isValidQuantityRemoveAlpha(component,e){
var characterCode;
var position=doGetCaretPosition(component);

if(e && e.which){ // NN4 specific code
e = e;
characterCode = e.which;
}
else {
e = event;
characterCode = e.keyCode; // IE specific code
}
//characterCode for left and right arrows and backSpace buttons for onkeyup events
if(characterCode==37 || characterCode==38 || characterCode==39 || characterCode==40 || characterCode==8 ){
return;
}

var quantity = component.value.trim();
var qtyLen = quantity.length;
var validVals = "0123456789";
var char;
var isError = false;

for (i = 0; i < qtyLen ; i++) {
char = quantity.charAt(i);
if (validVals.indexOf(char) == -1)
{
var quantity1 = quantity.substr(i+1,qtyLen) ;
quantity = quantity.substr(0,i) +quantity1;

isError = true;

}
}
component.value = quantity;


if (quantity.length > 7){
var val = quantity.substr(0,7);
quantity = val;
component.value = quantity;
}

if(position!=0 && isError == true)
{
setCaretPosition(component,position-1);
}
else if(position!=0 && isError == false)
{
setCaretPosition(component,position);
}

return true;
}


//end for jira 3241

function isValidQtyRemoveAlpha(component){

	var quantity = component.value.trim();
    var qtyLen = quantity.length;
    var validVals = "0123456789";
    //var isValid=true;
    var char;
    for (i = 0; i < qtyLen ; i++) {
       char = quantity.charAt(i); 
       if (validVals.indexOf(char) == -1) 
       {
    	quantity = quantity.substr(0,i) ;
    	//alert ("Quantity After: " + quantity)
         // isValid = false;
       }
   	}
    component.value = quantity;

    if (quantity.length > 7){
        var val = quantity.substr(0,7);
		quantity = val;
        component.value = quantity;
    }
    return true;
}


// Our "please wait" function
function xpedx_working_start ()
{
	$.blockUI({
			message: $('<div id="spinner" style="display: none;">&nbsp;</div>'),
			css: {
				top:	($(window).height() - 11) / 2 + 'px',
				left:	($(window).width() - 43) / 2 + 'px',
				width:'43px',
				background:	'transparent',
				border: 'none'
			}
	});
}
function xpedx_working_stop ()
{
	$.unblockUI();
}

/*
	Password Validator 0.1
	(c) 2007 Steven Levithan <stevenlevithan.com>
	MIT License
*/

function validatePassword (pw, options) {
	// default options (allows any password)
	var o = {
		lower:    0,
		upper:    0,
		alpha:    0, /* lower + upper */
		numeric:  0,
		special:  0,
		length:   [0, Infinity],
		custom:   [ /* regexes and/or functions */ ],
		badWords: [],
		badSequenceLength: 0,
		noQwertySequences: false,
		noSequential:      false
	};

	for (var property in options)
		o[property] = options[property];

	var	re = {
			lower:   /[a-z]/g,
			upper:   /[A-Z]/g,
			alpha:   /[A-Z]/gi,
			numeric: /[0-9]/g,
			special: /[\W_]/g
		},
		rule, i;

	// enforce min/max length
	if (pw.length < o.length[0] || pw.length > o.length[1])
		return false;

	// enforce lower/upper/alpha/numeric/special rules
	for (rule in re) {
		if ((pw.match(re[rule]) || []).length < o[rule])
			return false;
	}

	// enforce word ban (case insensitive)
	for (i = 0; i < o.badWords.length; i++) {
		if (pw.toLowerCase().indexOf(o.badWords[i].toLowerCase()) > -1)
			return false;
	}

	// enforce the no sequential, identical characters rule
	if (o.noSequential && /([\S\s])\1/.test(pw))
		return false;

	// enforce alphanumeric/qwerty sequence ban rules
	if (o.badSequenceLength) {
		var	lower   = "abcdefghijklmnopqrstuvwxyz",
			upper   = lower.toUpperCase(),
			numbers = "0123456789",
			qwerty  = "qwertyuiopasdfghjklzxcvbnm",
			start   = o.badSequenceLength - 1,
			seq     = "_" + pw.slice(0, start);
		for (i = start; i < pw.length; i++) {
			seq = seq.slice(1) + pw.charAt(i);
			if (
				lower.indexOf(seq)   > -1 ||
				upper.indexOf(seq)   > -1 ||
				numbers.indexOf(seq) > -1 ||
				(o.noQwertySequences && qwerty.indexOf(seq) > -1)
			) {
				return false;
			}
		}
	}

	// enforce custom regex/function rules
	for (i = 0; i < o.custom.length; i++) {
		rule = o.custom[i];
		if (rule instanceof RegExp) {
			if (!rule.test(pw))
				return false;
		} else if (rule instanceof Function) {
			if (!rule(pw))
				return false;
		}
	}

	// great success!
	return true;
}


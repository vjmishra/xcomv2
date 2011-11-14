/* This file is sepcific to any methods related to New User page.
 * 
 */

/**
 * @author vbandhu
 * 
 */

/**
 * onReady function. Does the following tasks:
 *  1. expands and collpases the panels based on the use case. Uses slider1 function defined in profile.js
 *  2. Applies SVG after expand/collpase of panels. Calls svg_classhandlers_decoratePage method in theme-javascript 
 * 
 */

Ext.onReady(function() {
        //slider1('generalInfoTable','toggleUserInfo','sliderIn','sliderOut', true);
        svg_classhandlers_decoratePage();
});
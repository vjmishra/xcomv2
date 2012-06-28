/**
 * 
 * SVG annotators: the following functions annotate style of selected elements
 * 
 * to set "fancy" backgrounds. Many of these functions take a "props" argument.
 * 
 * This is a javascript object the members of which correspond to the properties
 * 
 * of the ShapeAction in the imagebuilder library. Generally, this is to provide
 * 
 * detailed control of the shape's characteristics. The props argument works
 * this
 * 
 * way:
 * 
 * a. if a property is set, it will be used when constructing the shape URL.
 * 
 * b. if a property is named, but initialized to null, a correct value will be
 * 
 * found by inference on some characteristic of the page context. For
 * 
 * example, width and height will be determined by the selected element's
 * 
 * width and height, fillColor from the element's backgroundColor, aand so on.
 * 
 * 
 * 
 * Depends on the extjs core library.
 * 
 */

/* global Ext */

var SVGAnnotator = {

    imageDirectory: "/images",



    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes a rounded background with a border. This function selects html
	 * elements
	 * 
	 * based on css selector and, for each, modifies the background to reference
	 * a
	 * 
	 * rounded background sized to fit the dimensions of the element. The color
	 * 
	 * of the area outside the rounded corners is transparent.
	 * 
	 * 
	 * 
	 * Parameters:
	 * 
	 * selector: a css selector refering to elements that may be in the page.
	 * E.g.
	 * 
	 * ".box" selects elements with the "box" css class.
	 * 
	 * curve: the radius of rounded corners. If null, If null, defaults to a
	 * value
	 * 
	 * computed from the left padding of the region.
	 * 
	 * 
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 */

    makeRoundedBackground: function(selector, curve) {

        if (Ext) {

            // if old api used implement in old way, otherwise delegate to new
			// api

            if (curve) {

                var selectedArr = Ext.query(selector);

                for (var i = 0; i < selectedArr.length; i++) {

                    var extEl = Ext.get(selectedArr[i]);

                    extEl.setStyle("borderWidth", "0");

                    var p = this.inferFromContext(extEl,

                    {

                        width:null,

                        height:null,

                        curve:curve,

                        fillColor:null,

                        borderColor:null,

                        outsideOpacityPct:null

                    });

                    var url = this.getImageDirectory() + "/shape.svg_png?" +

                              Ext.urlEncode(p);

                    extEl.setStyle("backgroundImage", "url(" + url + ")");

                    extEl.setStyle("backgroundRepeat", "no-repeat");

                    extEl.setStyle("backgroundColor", "transparent");

                    // extEl.setStyle("backgroundColor", "transparent");

                }

            }

            else {

                this.mkRoundedBackground(selector);

            }

        }

    },



    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes a rounded background with a border. This function selects html
	 * elements
	 * 
	 * based on css selector and, for each, modifies the background to reference
	 * a
	 * 
	 * rounded background sized to fit the dimensions of the element. Color
	 * outside
	 * 
	 * the rounded corners is inferred from the parent element.
	 * 
	 * 
	 * 
	 * Parameters:
	 * 
	 * selector: a css selector refering to elements that may be in the page.
	 * E.g.
	 * 
	 * ".box" selects elements with the "box" css class.
	 * 
	 * props: An object defining additional properties. See class commentary.
	 * 
	 * 
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 */

    mkRoundedBackground: function(selector, props, classSuffix) {

        var selectedArr, extEl, p, url, id, myStyle, cssClass, ssId;

        if (Ext) {

            if (props == null) {

                props = {};

            }

            if (!classSuffix) {

                classSuffix = "";

            }



            selectedArr = Ext.query(selector);

            props = this.mergeOnto(props, {

                width:null,

                height: null,

                fillColor:null,

                borderColor:null,

                curve:null,

                outsideColor:null,

                outsideOpacityPct:null

            });

            for (var i = 0; i < selectedArr.length; i++) {

                extEl = Ext.get(selectedArr[i]);

                id = Ext.id(extEl);

                cssClass = id + classSuffix;

                ssId = "ss-" + cssClass;

                Ext.util.CSS.removeStyleSheet("ss-" + id);



                extEl.setStyle("borderWidth", "0");

                p = this.inferFromContext(extEl, props);



                url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);



                myStyle = "#" + cssClass + " {";

                myStyle += "background-image: url(" + url + ");";

                myStyle += "background-repeat: no-repeat;";

                if (p.outsideOpacityPct==0) {

                    myStyle += "background-color: transparent;"

                }

                myStyle += "}";

                Ext.util.CSS.createStyleSheet(myStyle, ssId);

// Ext.util.CSS.updateRule(cssClass, "background-image", "url(" + url + ")");

// Ext.util.CSS.updateRule(cssClass, "background-repeat", "no-repeat");

// if (p.outsideOpacityPct==0) {

// Ext.util.CSS.updateRule(cssClass, "background-color", "transparent");

// }

            }

        }

    },



    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes a rectangular background rounded on the top. This function selects
	 * 
	 * html elements based on css selector and, for each, modifies the
	 * 
	 * background to reference a rounded background sized to fit the dimensions
	 * 
	 * of the element.
	 * 
	 * 
	 * 
	 * Parameters:
	 * 
	 * selector: a css selector refering to elements that may be in the page.
	 * E.g.
	 * 
	 * ".box" selects elements with the "box" css class.
	 * 
	 * isOpen: if true, a bottom border is omitted on the figure. If false,
	 * 
	 * bottom border is generated. Defaults to true.
	 * 
	 * curve: the radius of rounded corners. If null, defaults to a value
	 * 
	 * computed from the left padding of the region.
	 * 
	 * 
	 * 
	 * DEPRECATED: please use mkTopRoundedRuleSet
	 * 
	 */

    makeTopRoundedBackground: function(selector, isOpen, curve) {

        if (Ext) {

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                if (!isOpen) {

                    isOpen = true;

                }

                var extEl = Ext.get(selectedArr[i]);

                extEl.setStyle("borderWidth", "0");

                var p = this.inferFromContext(extEl,

                {

                    width:null,

                    height:null,

                    curve:curve,

                    fillColor:null,

                    borderColor:null,

                    borderWidth:1

                });

                p.clipY2 = p.height;

                p.height = p.height + p.curve + 2;

                var url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);

                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "no-repeat");

                extEl.setStyle("backgroundColor", "transparent");

            }

        }

    },



    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes a rectangular background rounded on the top. This function selects
	 * 
	 * html elements based on css selector and, for each, modifies the
	 * 
	 * background to reference a rounded background sized to fit the dimensions
	 * 
	 * of the element.
	 * 
	 * 
	 * 
	 * Parameters:
	 * 
	 * selector: a css selector refering to elements that may be in the page.
	 * E.g.
	 * 
	 * ".box" selects elements with the "box" css class.
	 * 
	 * props: An object defining additional properties. See class commentary.
	 * 
	 */

    mkTopRoundedBackground: function(selector, props) {

        if (Ext) {

            if (props == null) {

                props = {};

            }

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                var extEl = Ext.get(selectedArr[i]);

                extEl.setStyle("borderWidth", "0");

                props = this.mergeOnto(props, {

                    width:null,

                    height: null,

                    fillColor:null,

                    borderColor:null,

                    curve:null,

                    outsideColor:null,

                    outsideOpacityPct:100,

                    borderWidth:1

                });

                var p = this.inferFromContext(extEl, props);

                p.clipY2 = p.height;

                p.height = p.height + p.curve + 2;

                var url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);

                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "no-repeat");

                // extEl.setStyle("backgroundColor", "transparent");

            }

        }

    },



    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes a rectangular background rounded on all but the upper left corner.
	 * 
	 * This function selects html elements based on css selector and, for each,
	 * 
	 * modifies the background to reference a rounded background sized to fit
	 * 
	 * the dimensions of the element.
	 * 
	 * 
	 * 
	 * Parameters:
	 * 
	 * selector: a css selector refering to elements that may be in the page.
	 * E.g.
	 * 
	 * ".box" selects elements with the "box" css class.
	 * 
	 * curve: the radius of rounded corners. If null, defaults to a value
	 * 
	 * computed from the left padding of the region.
	 * 
	 * 
	 * 
	 * DEPRECATED: please convert to mkGeneralShapeRuleSet
	 * 
	 */

    make3QRoundedBackground: function (selector, curve, dropShadow) {

        if (Ext) {

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                var extEl = Ext.get(selectedArr[i]);

                extEl.setStyle("borderWidth", "0");

                var p = this.inferFromContext(extEl,

                {

                    width:null,

                    height:null,

                    curve:curve,

                    fillColor:null,

                    borderColor:null,

                    outsideOpacityPct:0,

                    shape:"3qRoundedRect",

                    dropShadowOffset:dropShadow,

                    borderWidth:1

                });



                var url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);



                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "no-repeat");

                extEl.setStyle("backgroundColor", "transparent");

            }

        }

    },



    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes a rectangular background rounded on all but the upper left corner.
	 * 
	 * This function selects html elements based on css selector and, for each,
	 * 
	 * modifies the background to reference a rounded background sized to fit
	 * 
	 * the dimensions of the element.
	 * 
	 * 
	 * 
	 * Parameters:
	 * 
	 * selector: a css selector refering to elements that may be in the page.
	 * E.g.
	 * 
	 * ".box" selects elements with the "box" css class.
	 * 
	 * props: An object defining additional properties. See class commentary.
	 * 
	 * 
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 */

    mk3QRoundedBackground: function (selector, props) {

        if (Ext) {

            if (props == null) {

                props = {};

            }

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                var extEl = Ext.get(selectedArr[i]);

                extEl.setStyle("borderWidth", "0");

                var p = this.inferFromContext(extEl, this.mergeOnto(props,

                            {

                                width:null,

                                height:null,

                                curve:null,

                                fillColor:null,

                                outsideColor:null,

                                outsideOpacityPct:100,

                                borderColor:null,

                                shape:"3qRoundedRect",

                                dropShadowOffset:null,

                                borderWidth:1

                            })

                        );



                var url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);



                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "no-repeat");

                // extEl.setStyle("backgroundColor", "transparent");

            }

        }

    },



    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes a rectangular background rounded on all corners with no border.
	 * 
	 * This function selects html elements based on css selector and, for each,
	 * 
	 * modifies the background to reference a rounded background sized to fit
	 * 
	 * the dimensions of the element.
	 * 
	 * 
	 * 
	 * Parameters:
	 * 
	 * selector: a css selector refering to elements that may be in the page.
	 * E.g.
	 * 
	 * ".box" selects elements with the "box" css class.
	 * 
	 * curve: the radius of rounded corners. If null, defaults to a value
	 * 
	 * computed from the left padding of the region.
	 * 
	 * 
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 */

    makeRoundedNoBorder: function(selector, curve) {

        if (Ext) {

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                var extEl = Ext.get(selectedArr[i]);

                var p = this.inferFromContext(extEl,

                {

                    width:null,

                    height:null,

                    curve:curve,

                    outsideColor:null,

                    outsideOpacityPct:100,

                    fillColor:null,

                    borderWidth:0

                });

                var url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);



                // alert("url="+url);

                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "no-repeat");

                extEl.setStyle("backgroundColor", "transparent");

                // extEl.setStyle("borderWidth", "0");

            }

        }

    },

    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes a rectangular background rounded on all corners with no border and
	 * 
	 * simulated lighting.
	 * 
	 * This function selects html elements based on css selector and, for each,
	 * 
	 * modifies the background to reference a rounded background sized to fit
	 * 
	 * the dimensions of the element.
	 * 
	 * 
	 * 
	 * Parameters:
	 * 
	 * selector: a css selector refering to elements that may be in the page.
	 * E.g.
	 * 
	 * ".box" selects elements with the "box" css class.
	 * 
	 * curve: the radius of rounded corners. If null, defaults to a value
	 * 
	 * computed from the left padding of the region.
	 * 
	 * fillColor: the background color
	 * 
	 * 
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 */

    makeLightedRoundedRect: function(selector, curve, fillColor) {

        if (Ext) {

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                var extEl = Ext.get(selectedArr[i]);

                var p = this.inferFromContext(extEl,

                {

                    width:null,

                    height:null,

                    curve:curve,

                    borderWidth:0,

                    fillColor:fillColor,

                    outsideColor:null,

                    outsideOpacityPct:100,

                    shadingStyle:"spotlight"

                });



                var url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);



                // alert("url="+url);

                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "no-repeat");

                extEl.setStyle("backgroundColor", "transparent");

                // extEl.setStyle("borderWidth", "0");

            }

        }

    },

    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes a rectangular background rounded on all corners with no border and
	 * 
	 * simulated lighting.
	 * 
	 * This function selects html elements based on css selector and, for each,
	 * 
	 * modifies the background to reference a rounded background sized to fit
	 * 
	 * the dimensions of the element.
	 * 
	 * 
	 * 
	 * Parameters:
	 * 
	 * selector: a css selector refering to elements that may be in the page.
	 * E.g.
	 * 
	 * ".box" selects elements with the "box" css class.
	 * 
	 * curve: the radius of rounded corners. If null, defaults to a value
	 * 
	 * computed from the left padding of the region.
	 * 
	 * shadingStyle: spotlight|highlight|topHalf|bottomHalf
	 * 
	 * shadingLevelPct: amount of shading
	 * 
	 * fillColor: the background color
	 * 
	 * 
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 */

    makeShadedRoundedRect: function(selector, curve, shadingStyle, shadingLevelPct, fillColor) {

        if (Ext) {

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                var extEl = Ext.get(selectedArr[i]);

                var p = this.inferFromContext(extEl,

                {

                    width:null,

                    height:null,

                    pShadeType:shadingStyle,

                    curve:curve,

                    borderWidth:null,

                    outsideColor:null,

                    outsideOpacityPct:100,

                    fillColor:fillColor,

                    shadingStyle:shadingStyle,

                    shadingLevelPct:shadingLevelPct

                });

                var url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);



                // alert("url="+url);

                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "no-repeat");

                extEl.setStyle("backgroundColor", "transparent");

                // extEl.setStyle("borderWidth", "0");

            }

        }

    },

    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes and sets a shaded background resembling a 3 dimensional horizontal
	 * bar for
	 * 
	 * selected elements.
	 * 
	 * 
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 */

    make3dBarBackground: function (selector, fillColor, shadingLevelPct) {

        // alert("start")

        if (shadingLevelPct == null) {

            shadingLevelPct = 90;

        }

        if (Ext) {

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                var extEl = Ext.get(selectedArr[i]);

                var p = this.inferFromContext(extEl,

                {

                    width:1,

                    height:null,

                    fillColor:fillColor,

                    shadingStyle:"highlight",

                    shadingLevelPct:shadingLevelPct,

                    curve:0,

                    borderWidth:0

                });

                // alert("p=" + this.toString(p));

                var url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);

                // alert("url="+url);

                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "repeat-x");

            }

        }

    },



    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes and sets a rounded box gradient background for selected elements.
	 * 
	 * 
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 */

    makeRoundedGradientBackground: function(selector, startColor, stopColor, curve, direction) {

        if (Ext) {

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                var extEl = Ext.get(selectedArr[i]);

                var p = this.inferFromContext(extEl,

                {

                    width:null,

                    height:null,

                    curve:curve,

                    fillColor:startColor,

                    fillGradientType:direction,

                    fillGradientTargetColor:stopColor,

                    outsideColor:null,

                    outsideOpacityPct:100,

                    borderColor:null

                });

                var url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);

                // alert("url="+url);

                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "no-repeat");

                extEl.setStyle("backgroundColor", "transparent");

                // extEl.setStyle("borderWidth", "0");

            }

        }

    },



    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes a url that will generate a simple rectangular gradient
	 * 
	 * 
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 */

    makeSimpleGradientBackground: function(selector, startColor, stopColor, direction) {

        if (Ext) {

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                var extEl = Ext.get(selectedArr[i]);

                var p = this.inferFromContext(extEl,

                {

                    width:null,

                    height:null,

                    curve:0,

                    borderWidth:0,

                    fillColor:startColor,

                    fillGradientType:direction,

                    fillGradientTargetColor:stopColor

                });

                var url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);

                // alert("url="+url);

                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "no-repeat");

            }

        }

    },



    /**
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 * 
	 * 
	 * Makes a "splat" shape sized for the selected element and sets it as the
	 * 
	 * background.
	 * 
	 * 
	 * 
	 * DEPRECATED: use mkGeneralShapeRuleSet
	 * 
	 */

    makeSplat: function (selector, shadowColor) {

        if (Ext) {

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                var extEl = Ext.get(selectedArr[i]);

                var p = this.inferFromContext(extEl,

                {

                    width:null,

                    height:null,

                    shadowColor:shadowColor

                });

                var url = this.getImageDirectory() + "/splat.svg_png?" +

                          Ext.urlEncode(p);

                // alert("url="+url);

                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "no-repeat");

                extEl.setStyle("backgroundColor", "transparent");

                extEl.setStyle("borderWidth", "0");

            }

        }

    },





    /**
	 * 
	 * DEPRECATED: Alters style of element, so can cause problems on refresh.
	 * Use
	 * 
	 * mkGeneralShapeRuleSet().
	 * 
	 */

    makeGeneralShape: function(selector, values) {

        if (Ext) {

            var selectedArr = Ext.query(selector);

            for (var i = 0; i < selectedArr.length; i++) {

                var extEl = Ext.get(selectedArr[i]);

                var p = this.inferFromContext(extEl, values);

                var url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);

                // alert("url="+url);

                extEl.setStyle("backgroundImage", "url(" + url + ")");

                extEl.setStyle("backgroundRepeat", "no-repeat");

            }

        }



    },



    mkGeneralShapeRuleSet: function(selector, values, classSuffix) {

        var ruleSet, selectedArr, extEl, id, cssClass, url, i;



        if (!classSuffix) {

            classSuffix = "";

        }

        ruleSet = "";

        if (Ext) {

            selectedArr = Ext.query(selector);

            for (i = 0; i < selectedArr.length; i++) {

                extEl = Ext.get(selectedArr[i]);

                id = Ext.id(extEl);

                cssClass = id + classSuffix;



                var p = this.inferFromContext(extEl, values);

                url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);

                // alert("url="+url);



                ruleSet += "#" + cssClass + " {\n";

                ruleSet += "background-image: url(" + url + ");\n";

                if (p.outsideOpacityPct==0) {

                    ruleSet += "background-color: transparent;\n"

                }

                ruleSet += "}\n";

            }

        }

        return ruleSet;

    },



    mkTopRoundRuleSet: function(selector, values, classSuffix) {

        var ruleSet, selectedArr, extEl, id, cssClass, url, i;



        if (!classSuffix) {

            classSuffix = "";

        }

        ruleSet = "";

        if (Ext) {

            selectedArr = Ext.query(selector);

            for (i = 0; i < selectedArr.length; i++) {

                extEl = Ext.get(selectedArr[i]);

                id = Ext.id(extEl);

                cssClass = id + classSuffix;



                var p = this.inferFromContext(extEl, values);

                p.clipY2 = p.height;

                p.height = p.height + p.curve + 2;



                url = this.getImageDirectory() + "/shape.svg_png?" +

                          Ext.urlEncode(p);

                // alert("url="+url);



                ruleSet += "#" + cssClass + " {\n";

                ruleSet += "background-image: url(" + url + ");\n";

                ruleSet += "background-repeat: no-repeat;\n";

                if (p.outsideOpacityPct==0) {

                    ruleSet += "background-color: transparent;\n"

                }

                ruleSet += "}\n";

            }

        }

        return ruleSet;



    },



/**
 * 
 * Determines values for named shape parameters from a page context. For
 * 
 * example, determines width from element width. Parameters with a non-null
 * 
 * value are ignored.
 * 
 */

    inferFromContext: function(extEl, values) {

        // alert("inferFromContext-start:");

        var result = {};



        for (var key in values) {

            var value = values[key];

            if (value == null) {

                switch (key) {

                    case "width":

                        result.width = extEl.getComputedWidth();

                        if (result.width<5) {

// alert("width<5:" + extEl.id);

                            result.width=5;

                        }

                        break;

                    case "height":

                        result.height = extEl.getComputedHeight();

                        if (result.height<5) {

// alert("height<5:" + extEl.id);

                            result.height=5;

                        }

                        break;

                    case "curve":

                        var p = extEl.getPadding("l");

                        if (p) {

                            result.curve = p * 2.0;

                        } else {

                            result.curve = 5;

                        }

                        break;

                    case "fillColor":

                        result.fillColor = extEl.getColor("backgroundColor", "#ffffff");

                        break;

                    case "borderColor":

                    // infer border color from (a) Left side border color or

                    // foreground color, if not set.

                        result.borderColor = extEl.getColor("borderLeftColor",

                                extEl.getColor("color", "#000000"));

                        break;

                    case "borderWidth":

                        result.borderWidth = extEl.getBorderWidth('l');

                        if (result.borderWidth == "") {

                            result.borderWidth = "0";

                        }

                        break;

                    case "outsideColor":

                        var parent = extEl.parent();

                        if (parent) {

                            result.outsideColor = parent.getColor("backgroundColor", "#ffffff");

                        }

                        else {

// alert("no parent");

                            result.outsideColor = "white";

                        }

                        break;

                    case "outsideOpacityPct":

// var parent = extEl.parent();

                        if (parent) {

// result.outsideOpacityPct = parent.getStyle("backgroundImage")?0:100;

                            result.outsideOpacityPct = 100;

                        }

                        else {

                            result.outsideOpacityPct = 0;

                        }

                        break;

// case "stopColor":

// result.stopColor = "white";

// break;

                    case "style":

                        var t = this.inferFromContext(extEl, {fillColor:null,borderColor:null});

                        result.style = "fill:" + t["fillColor"] + ";stroke:" + t["borderColor"];

                        break;

                    case "fillGradientType":

                        result.fillGradientType = "ll2ur";

                        break;

                    case "fillGradientTargetColor":

                        result.fillGradientTargetColor = "white";

                        break;

                    case "shadingStyle":

                        result.shadingStyle = "highlight";

                        break;

                    case "shadingLevelPct":

                        result.shadingLevelPct = 50;

                        break;

                    case "dropShadowOffset":

                        result.dropShadowOffset = 10;

                        break;

                }

            } else {

                result[key] = value;

            }

        }

        if (result.outsideColor == "transparent") {

            result.outsideOpacityPct = "0";

            result.outsideColor = "white";

        }

        if (values.margin) {

            if (result.width)result.width-=(2*values.margin);

            if (result.height)result.height-=(2*values.margin);

        }

        return result;

    },



    /**
	 * 
	 * A utility function to load the image directory for URL construction.
	 * 
	 */

    getImageDirectory: function() {

        var context, contextEl;

        contextEl = Ext.query("meta[name='webapp-context']")[0];

        if (contextEl == null) {

            context = "";

        } else {

            context = contextEl.getAttribute('content');

        }

        return context + this.imageDirectory;

    },



    /**
	 * 
	 * Utility to merge obj2 onto obj1 any fields not already defined in obj1.
	 * 
	 * Returns the modified obj1
	 * 
	 */

    mergeOnto: function (obj1, obj2) {

        for (var i in obj2) {

            // print(i + "," + obj1[i] + "," + obj2[i]);

            if (obj1[i] == null && obj1[i] !== null) {

                obj1[i] = obj2[i];

            }

        }

        return obj1;

    },



    toString: function(obj) {

        var out = "{";

        for (var a in obj) {

            out += a + ":" + obj[a] + ","

        }

        out += "}";

        return out;

    }

};



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
							 * 
							 * if (
							 * this.parentNode.parentNode.className.indexOf("close") !=
							 * -1 )
							 *  {
							 * 
							 * this.parentNode.parentNode.className =
							 * this.parentNode.parentNode.className.replace("close",
							 * "");
							 *  }
							 * 
							 * else
							 *  {
							 * 
							 * this.parentNode.parentNode.className += " close";
							 *  }
							 * 
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

/*
 * ==================================================
 * 
 * $Id: swc.js,v 1.11 2011/10/19 16:15:03 rugrani Exp $
 * 
 * tabber.js by Patrick Fitzgerald pat@barelyfitz.com
 * 
 * 
 * 
 * Documentation can be found at the following URL:
 * 
 * http://www.barelyfitz.com/projects/tabber/
 * 
 * 
 * 
 * License (http://www.opensource.org/licenses/mit-license.php)
 * 
 * 
 * 
 * Copyright (c) 2006 Patrick Fitzgerald
 * 
 * 
 * 
 * Permission is hereby granted, free of charge, to any person
 * 
 * obtaining a copy of this software and associated documentation files
 * 
 * (the "Software"), to deal in the Software without restriction,
 * 
 * including without limitation the rights to use, copy, modify, merge,
 * 
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * 
 * and to permit persons to whom the Software is furnished to do so,
 * 
 * subject to the following conditions:
 * 
 * 
 * 
 * The above copyright notice and this permission notice shall be
 * 
 * included in all copies or substantial portions of the Software.
 * 
 * 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * 
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * 
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * 
 * SOFTWARE.
 * 
 * ==================================================
 */



function tabObj(argsObj)

{

  var arg; /* name of an argument to override */



  this.div = null;

  // "tabcontainer","tab_1_content_class","tabnavigation","hidetab","activetab"

  /*
	 * Rename classMain to classMainLive after tabifying (so a different style
	 * can be applied)
	 */

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

  // this.classNav = "tabnavigation";

  /* When a tab is to be hidden, we set the class of the div to classTabHide. */

  this.classTabHide = "hidetab";

  /*
	 * Class to set the navigation LI when the tab is active, so a different
	 * style can be used on the active tab.
	 */

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



  /* Get the child node with matching class name */

  d = Ext.DomQuery.selectNode("."+this.mainClass);



  childNodes = d.childNodes;// Ext.DomQuery.select("."+this.classTab,d);



  // childNodes = Ext.query("*[class="+this.mainClass+"]
	// >*[class="+this.classTab+"]"); extjs is junk



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



		  /*
			 * If the class name contains classTabDefault,
			 * 
			 * then select this tab by default.
			 * 
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



    /*
	 * Get the label to use for this tab:
	 * 
	 * From the title attribute on the DIV,
	 * 
	 * Or from one of the this.titleElements[] elements,
	 * 
	 * Or use an automatically generated number.
	 * 
	 */

    t.headingText = t.div.title;



    /* Remove the title attribute to prevent a tooltip from appearing */

	t.div.title = '';



	/* Create a list element for the tab */

    TAB_li = document.createElement("li");

    TAB_li.className = this.classNavInactive;



    /*
	 * Save a reference to this list item so we can later change it to
	 * 
	 * the "active" class
	 */

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



    /*
	 * Add some properties to the link so we can identify which tab
	 * 
	 * was clicked. Later the navClick method will need this.
	 * 
	 */

    TAB_a.tabber = this;

    TAB_a.tabIndex = i + this.startindex;

    // TAB_a.tabIndex = i;



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

  /*
	 * there is no need for this ...so removing it(inline javascript method call
	 * is not advisable)
	 * 
	 * if (typeof self.onClick == 'function') {
	 * 
	 * 
	 * 
	 * onClickArgs = {'tabber':self, 'index':tabberIndex, 'event':event};
	 * 
	 * 
	 * 
	 * if (!event) { onClickArgs.event = window.event; }
	 * 
	 * 
	 * 
	 * rVal = self.onClick(onClickArgs);
	 * 
	 * if (rVal === false) { return false; }
	 *  }
	 */



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





/* ================================================== */





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

		/*
		 * Rename classMain to classMainLive after tabifying (so a different
		 * style can be applied)
		 */

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



		/*
		 * When a tab is to be hidden, we set the class of the div to
		 * classTabHide.
		 */

		if(customClasses[3] != null && customClasses[3] != ""){

			tabberArgs.classTabHide = customClasses[3];

		}



		/*
		 * Class to set the navigation LI when the tab is active, so a different
		 * style can be used on the active tab.
		 */

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





/* ================================================== */





function tabifyOnLoad(mainClassName, startindex, customClasses, defaultTab, eventHandlers)

{

  /*
	 * This function adds tabify to the window.onload event,
	 * 
	 * so it will run after the document has finished loading.
	 * 
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





/* ================================================== *//**
														 * 
														 * DialogPanel: This
														 * object provides
														 * functions supporting
														 * a modal or
														 * 
														 */

var DialogPanel = {

    

    toggleDialogVisibility: function(id) {

        var el = Ext.get(id);

        if (el != null) {

            if (el.isVisible()) {

                this.hide(id);

            }

            else {

                this.show(id);

            }

        }

    },



    hide: function(id) {

        var cmp = Ext.getCmp(id);

        cmp.hide();

    },



    show: function(id) {

        var cmp = Ext.getCmp(id);

        cmp.show();

    }

}

function addErrorForRepeatingElements(form, errorData){

    var errorStrings = errorData.errorStrings;

    var resetValues = errorData.resetValueStrings;

    if(errorStrings.length>1){

        for(var i=0; i<errorStrings.length; i++){

            if(errorStrings[i] != null && errorStrings[i] != ""){

                addError(form.elements[errorData.fieldName][i], errorStrings[i]);

            }

            if(resetValues!=null)

                if(resetValues[i]!=null){

                    form.elements[errorData.fieldName][i].value = resetValues[i];

                }

        }

    }

    else if(errorStrings.length=1){

        if(errorStrings[0] != null && errorStrings[0] != ""){

            addError(form.elements[errorData.fieldName], errorStrings[0]);

        }

        if(resetValues!=null)

            if(resetValues[0]!=null){

                form.elements[errorData.fieldName].value = resetValues[0];

            }

    }

}





function swc_validateForm(formName, resetErrors) {

        var errors = false;

        form = document.getElementById(formName);

        if( (resetErrors === undefined) || (resetErrors == true) )

        {

            clearErrorMessages(form);

            clearErrorLabels(form);

        }

        dojo.io.bind({

            url :ajaxValidationURL, // URL for ajaxValidateJson is defined in
									// head.tag using s:url tag to remove the
									// error of hard-coded context-root

            load : function(type, data, evt) {

                if (data.count > 0) {

                    var i;

                    var errorCount = 0;

                    for(i = 0; i < data.count; i++) {

                        if(data.list[i].errorMesage != null){

                            if((form.elements[data.list[i].fieldName])!=null){

                                if(isFieldValidforAddError(form.elements[data.list[i].fieldName])){

                                    errorCount = errorCount + 1;

                                    addError(form.elements[data.list[i].fieldName], data.list[i].errorMesage);

                                }

                            }

                            else if((document.getElementById(data.list[i].fieldName))!= null){

                                if(isFieldValidforAddError(document.getElementById(data.list[i].fieldName))){

                                    errorCount = errorCount + 1;

                                    addError(document.getElementById(data.list[i].fieldName), data.list[i].errorMesage);

                                }

                            }

                        }

                        if(data.list[i].resetValue!=null){

                            form.elements[data.list[i].fieldName].value = data.list[i].resetValue;

                        }

                        else if(data.list[i].errorStrings != null){

                            errorCount = errorCount + 1;

                            addErrorForRepeatingElements(form, data.list[i]);

                        }

                    }

                    if(errorCount>0)

                        errors = true;

                    try{

                    svg_classhandlers_decoratePage();

                    }catch (err){

                        alert("There is an error in Svg-apply: "+err.message);

                    }

                }

            },

            mimetype : "text/json",

            sync : true,

            // form: "myForm",

            // content: { test: ["test1", "test2"] }

            formNode: form,

            method :"POST"

        });

        return !errors;

    }



function isFieldValidforAddError(field){

    var ctrlDiv = field.parentNode; // wwctrl_ div or span

    var enclosingDiv = ctrlDiv.parentNode; // wwgrp_ div

    if (!ctrlDiv || (ctrlDiv.nodeName != "DIV" && ctrlDiv.nodeName != "SPAN") || !enclosingDiv || enclosingDiv.nodeName != "DIV") {

        return false;

    }

    else{

        return true;

    }

}

/*
 * 
 * QuickView is a component providing the following behaviors
 * 
 * 1. Hover over a target area, a small clickable tooltip is displayed.
 * 
 * 2. Click on the tooltip a window is launched to display designated content.
 * 
 * 3. Unless the user move their mouse over the launched window, the window is
 * 
 * closed automatically within certain configurable time.
 * 
 * This component is used to avoid the hover over event invoking the window
 * directly
 * 
 * which is heavy weight and can be annoying sometimes.
 * 
 * 
 * 
 * Parameter:
 * 
 * anchor: Mixed - id/element/Elemet
 * 
 * this is the target area to trap the hover event. It is also used
 * 
 * as anchor to the tool tip and window display
 * 
 * link: String/url configuration
 * 
 * link is the url string or object to access content for the quick view window;
 * 
 * conf: configuration object, if not set, the following default values
 * 
 * are used
 *  {
 * 
 * title: 'QuickView',
 * 
 * showTimeout: 1000,
 * 
 * hideTimeout: 3000,
 * 
 * quickViewLaunchConf:
 *  {
 * 
 * border: false, // not configurable
 * 
 * header:false, // not configurable
 * 
 * hideBorders:true, // not configurable
 * 
 * closable:false, // not configurable
 * 
 * autoWidth:true, // not configurable
 * 
 * autoHeight:true, // not configurable
 * 
 * floating: true, // not configurable
 * 
 * shadow:false, // not configurable
 * 
 * shim:true, // not configurable
 * 
 * renderTo: null, // not configurable
 * 
 * html: null // not configurable
 *  },
 * 
 * quickViewWindowConf:
 *  {
 * 
 * initTrigger: 'i', // not configurable
 * 
 * alignToAnchor: false// not configurable
 * 
 * 
 *  // See QuickViewWindow for other configurable options
 *  }
 *  }
 * 
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



// if((t = Ext.get(anchor)) !== undefined)
// {
// t.on('mouseover', showLaunch);
// t.on('mouseout', function(){clearTimeout(tm1);});
// }



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
 * 
 * QuickViewWindow is a component wrap around Ext.Window with
 * 
 * default settings. It will display the content specified
 * 
 * by the 'link' paarmeter and anchor the window to the 'anchor'
 * 
 * paarmeter. It can be configured with a timeout period
 * 
 * to automatically close the window with animation.
 * 
 * 
 * 
 * anchor: Mixed - id/element/Elemet
 * 
 * this is the target area to trap the hover event or align the window to.
 * 
 * link: String/url configuration
 * 
 * link is the url string or object to access content for the quick view window;
 * 
 * conf: configuration object, if not set, the following default values
 * 
 * are used
 * 
 * 
 *  {
 * 
 * initTrigger: 'ch', //c:click; h: hover over anchor; i: instantiate;
 * 
 * showTimeout: 1000,
 * 
 * closeTimeout: 5000,
 * 
 * alignToAnchor: true,
 * 
 * animateFromAnchor: true,
 * 
 * offsetTop: 25,
 * 
 * offsetLeft: 0,
 * 
 * windowConf:
 *  {
 * 
 * title: 'Quick View',
 * 
 * width: 300,
 * 
 * height: 450,
 * 
 * modal: false,
 * 
 * autoScroll: true,
 * 
 * animCollapse: true,
 * 
 * animateTarget: null,
 *  // autoLoad.url will be overwritten
 * 
 * autoLoad: {
 * 
 * url: null
 *  }
 *  }
 *  };
 * 
 * 
 * 
 */

QuickViewWindow.defaultConfiguration = function()

{

    var toReturn = {

    		initTrigger: 'ch', 		// c:click; h: hover over anchor; i:
									// instantiate;

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

        	toReturn.windowConf = conf.windowConf; // in case there are more
													// configuration than
													// default

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

// if(t !== undefined)
// {
// t.on('click', this.createWindow);
// }

    }

    if(this.myConf.initTrigger.indexOf('h') >= 0)

    {

// if(t !== undefined)
// {
// t.on('mouseover', showWindow);
// t.on('mouseout', function(){clearTimeout(tm1);});
// }

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



/**
 * 
 * Javascript file for the mini cart display page.
 * 
 * 
 * 
 */



var minimalMiniCart = false;

var minimalMiniCartMessage = "";

var isGuestUser = false;

var canExpandMiniCart = false;

// var miniCartExpanded = false;

// var miniCartTimer = 0;

// var clickPending = false;



Ext.onReady(function() {
	
	if( document.miniCartForm == null) return;

    if(document.miniCartForm.minimalMiniCart.value == "true")

    {

        minimalMiniCart = true;

    }

    if(document.miniCartForm.isGuestUser.value == "true")

    {

        isGuestUser = true;

    }

    minimalMiniCartMessage = document.miniCartForm.minimalMiniCartMessage.value;

    if(document.miniCartForm.canExpandMiniCart.value == "true")

    {

// canExpandMiniCart = true;
// Changed to jquery cluetip don't show this ever
    	 canExpandMiniCart = false;

    }

// showMiniCartLink();

    setCartEventHandlers("h");

})



function showMiniCartLink()

{

    Ext.Ajax.request({

        url : document.getElementById('miniCartLinkDisplayURL').href,

        params : {

        },

        method: 'GET',

        // if we get a successful response, parse the data then render the panel

        success: function ( response, request ) {

            var linkDiv = document.getElementById("miniCartLinkDiv");

            linkDiv.innerHTML = response.responseText;

            setCartEventHandlers("h");

        },

        failure: function ( response, request ) {

            alert(document.miniCartForm.miniCartDisplayError.value);

        }

    });

}



var qvw = null;

function setCartEventHandlers(initTriggerVal) {

    // We only want to display mini-cart if there will be something in it.

    // The case where there wouldn't be is if it is the minimalMiniCart, for

    // the guest user, AND there's no minimalMiniCartMessage to display.

    if(!(minimalMiniCart && isGuestUser && (minimalMiniCartMessage == "")) &&

       (canExpandMiniCart == true) )

    {

        /*
		 * 
		 * Ext.get('miniCartLinkDiv').on('mouseover',
		 * 
		 * function(e,t)
		 *  {
		 * 
		 * if(!clickPending && !miniCartExpanded)
		 *  {
		 * 
		 * miniCartTimer = setTimeout("showMiniCartWindow();", 2000);
		 *  }
		 *  }
		 *  );
		 * 
		 * Ext.get('miniCartLinkDiv').on('mouseout',
		 * 
		 * function(e,t)
		 *  {
		 * 
		 * clearTimeout(miniCartTimer);
		 *  }
		 *  );
		 * 
		 * 
		 * 
		 * Ext.get('miniCartLinkDiv').on('click',
		 * 
		 * function(e,t)
		 *  {
		 * 
		 * clearTimeout(miniCartTimer);
		 * 
		 * clickPending = true;
		 *  }
		 *  );
		 * 
		 */

    	if(qvw === null)

    	{

	    	qvw = new QuickViewWindow('miniCartMouseoverArea', 

		    	{

		            url : document.getElementById('miniCartDisplayURL').href,

		            params: {

		                minimalMiniCart: minimalMiniCart,

		                minimalMiniCartMessage: minimalMiniCartMessage

		            },

		            method: 'POST',

	                callback:function(el, success, response){

		                if(success)

		                {
		                		
		                      svg_classhandlers_decoratePage();



		                }

		                else

		                {

		                    // el.dom.innerHTML = response.responseText;

                            // el.dom.innerHTML =
							// document.miniCartForm.miniCartDisplayError.value;

                            alert(document.miniCartForm.miniCartDisplayError.value);

		                    qvw.closeWindow();

		                }

		             }

		        },

		        {

		        	initTrigger: initTriggerVal,

		        	offsetTop: 25,

		        	offsetLeft: -240,

	                animateFromAnchor:false,

                    windowConf:

                    {

                        width: 465,
                        height: 285,
                        modal: false,
                        title: document.getElementById("miniCartTitle").value

                    }

		        }

	        );

    	}

    }

}
var hideMiniCart = 'miniCartMouseoverArea1';
var stIsIE = /*@cc_on!@*/false;
function refreshMiniCartLink(forceRefresh)

{

	if(typeof forceRefresh == "undefined")

    {

        forceRefresh = false;

    }



    Ext.Ajax.request({

        url : document.getElementById('miniCartLinkDisplayURL').href,

        params : {

            forceRefresh: forceRefresh

        },

        method: 'GET',

        // if we get a successful response, parse the data then render the panel

        success: function ( response, request ) {
        	var anchorToreplace = document.getElementById("XPEDXMiniCartLinkDisplayDiv");
        	anchorToreplace.innerHTML= Ext.util.Format.trim(response.responseText);
           //   $('.mini-cart-trigger').trigger("mouseenter.cluetip");
        	//$('.mini-cart-trigger').fireEvent('mouseenter.cluetip');
        	//$('.mini-cart-trigger').trigger('mouseover');
        	//$('.mini-cart-trigger').trigger('click');
        	//document.getElementById("miniCartMouseoverArea1").click();
        	if(!stIsIE)
            	$('.mini-cart-trigger').trigger('click');
            	else
            	{
            	//if(hideMiniCart =='miniCartMouseoverArea2')
            		document.getElementById("miniCartMouseoverArea2").click();
            		document.getElementById("miniCartMouseoverArea1").click();
            	}
// Have to include jqquery implementation to refresh cart
// if(qvw !== null)
// {
// qvw.getWindowHandle().destroy();
// qvw = null;
// setCartEventHandlers("hi");
// }

        },

        failure: function ( response, request ) {

            alert(document.miniCartForm.miniCartDisplayError.value);

        }

    });



}



function showMiniCartWindow() {

	if(qvw !== null)

	{

		qvw.createWindow();

	}

	/*
	 * 
	 * Ext.Ajax.request({
	 * 
	 * url : document.getElementById('miniCartDisplayURL').href,
	 * 
	 * params: {
	 * 
	 * minimalMiniCart: minimalMiniCart,
	 * 
	 * minimalMiniCartMessage: minimalMiniCartMessage
	 *  },
	 * 
	 * method: 'GET',
	 * 
	 * success: function (response, request){
	 * 
	 * var cartDiv = document.getElementById("miniCartDiv");
	 * 
	 * cartDiv.innerHTML = response.responseText;
	 * 
	 * cartDiv.style.display = "inline";
	 * 
	 * miniCartExpanded = true;
	 * 
	 * svg_classhandlers_decoratePage();
	 *  },
	 * 
	 * failure: function (response, request){
	 * 
	 * alert(document.miniCartForm.miniCartDisplayError.value);
	 *  }
	 * 
	 * });
	 * 
	 */

}



function hideMiniCartWindow(){

    var cartDiv = document.getElementById("miniCartDiv");

    cartDiv.style.display="none";

    miniCartExpanded = false;

}



function toggleVisibility(divID)

{

    var divElement = document.getElementById(divID);

    if(divElement.style.display != "none")

    {

        divElement.style.display = "none";

    }

    else

    {

        divElement.style.display = "inline";

    }

}



function deleteLine(orderHeaderKey, lineKey){

    // alert(lineKey);

    Ext.Ajax.request({

        url: document.getElementById('miniCartDeleteURL').href,

        params: {

            delLineKey:lineKey,

            orderHeaderKey: orderHeaderKey,

            operation:"DeleteLine"

        },

        method: 'POST',

        success: function (response, request){

        	var anchorToreplace = document.getElementById("XPEDXMiniCartLinkDisplayDiv");
        	anchorToreplace.innerHTML= Ext.util.Format.trim(response.responseText);

        },

        failure: function (response, request){

            alert(document.miniCartForm.miniCartGeneralAJAXError.value);

        }

    });



}



function updateLines() {
    if(swc_validateForm("miniCartData") == false)
    {
        return;
    }
    document.miniCartData.action = document.miniCartData.updateURL.value;

    Ext.Ajax.request({

        url: document.getElementById('miniCartUpdateURL').href,

        form: 'miniCartData',

        method: 'POST',

        success: function (response, request){

            refreshMiniCartLink();
        },

        failure: function (response, request){
        	// The web server is returning Failure even though the app server
        	//is successful. Just refresh the mini cart always.The application error if it happens, should
        	//be handled separately
        	refreshMiniCartLink();
        }

    });

}

function onEnter(evt){
	
	var evt = (evt) ? evt : ((event) ? event : null); 
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
	
	  if ((evt.keyCode == 13) && (node.type=="text")) {
		  
		  return false;
		  } 
}


function miniCartCheckout() {

    if(swc_validateForm("miniCartData") == false)

    {

        return;

    }

    

    document.miniCartData.action = document.miniCartData.checkoutURL.value;

    document.miniCartData.submit();

}


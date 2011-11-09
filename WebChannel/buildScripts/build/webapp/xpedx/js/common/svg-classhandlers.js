/**
 * SVG annotators: the following functions annotate style of selected elements
 * to set "fancy" backgrounds. Many of these functions take a "props" argument.
 * This is a javascript object the members of which correspond to the properties
 * of the ShapeAction in the imagebuilder library. Generally, this is to provide
 * detailed control of the shape's characteristics. The props argument works this
 * way:
 *   a. if a property is set, it will be used when constructing the shape URL.
 *   b. if a property is named, but initialized to null, a correct value will be
 *      found by inference on some characteristic of the page context. For
 *      example, width and height will be determined by the selected element's
 *      width and height, fillColor from the element's backgroundColor, aand so on.
 *
 * Depends on the extjs core library.
 */
/*global Ext*/
var SVGAnnotator = {
    imageDirectory: "/images",

    /**
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes a rounded background with a border. This function selects html elements
     * based on css selector and, for each, modifies the background to reference a
     * rounded background sized to fit the dimensions of the element. The color
     * of the area outside the rounded corners is transparent.
     *
     * Parameters:
     *   selector: a css selector refering to elements that may be in the page. E.g.
     *          ".box" selects elements with the "box" css class.
     *   curve: the radius of rounded corners. If null, If null, defaults to a value
     *           computed from the left padding of the region.
     *
     * DEPRECATED: use mkGeneralShapeRuleSet
     */
    makeRoundedBackground: function(selector, curve) {
        if (Ext) {
            // if old api used implement in old way, otherwise delegate to new api
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
                    //                extEl.setStyle("backgroundColor", "transparent");
                }
            }
            else {
                this.mkRoundedBackground(selector);
            }
        }
    },

    /**
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes a rounded background with a border. This function selects html elements
     * based on css selector and, for each, modifies the background to reference a
     * rounded background sized to fit the dimensions of the element. Color outside
     * the rounded corners is inferred from the parent element.
     *
     * Parameters:
     *   selector: a css selector refering to elements that may be in the page. E.g.
     *          ".box" selects elements with the "box" css class.
     *   props: An object defining additional properties. See class commentary.
     *
     * DEPRECATED: use mkGeneralShapeRuleSet
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
//                Ext.util.CSS.updateRule(cssClass, "background-image", "url(" + url + ")");
//                Ext.util.CSS.updateRule(cssClass, "background-repeat", "no-repeat");
//                if (p.outsideOpacityPct==0) {
//                    Ext.util.CSS.updateRule(cssClass, "background-color", "transparent");
//                }
            }
        }
    },

    /**
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes a rectangular background rounded on the top. This function selects
     * html elements based on css selector and, for each, modifies the
     * background to reference a rounded background sized to fit the dimensions
     * of the element.
     *
     * Parameters:
     *   selector: a css selector refering to elements that may be in the page. E.g.
     *           ".box" selects elements with the "box" css class.
     *   isOpen: if true, a bottom border is omitted on the figure. If false,
     *           bottom border is generated. Defaults to true.
     *   curve:  the radius of rounded corners. If null, defaults to a value
     *           computed from the left padding of the region.
     *
     * DEPRECATED: please use mkTopRoundedRuleSet
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
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes a rectangular background rounded on the top. This function selects
     * html elements based on css selector and, for each, modifies the
     * background to reference a rounded background sized to fit the dimensions
     * of the element.
     *
     * Parameters:
     *   selector: a css selector refering to elements that may be in the page. E.g.
     *          ".box" selects elements with the "box" css class.
     *   props: An object defining additional properties. See class commentary.
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
                //                extEl.setStyle("backgroundColor", "transparent");
            }
        }
    },

    /**
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes a rectangular background rounded on all but the upper left corner.
     * This function selects html elements based on css selector and, for each,
     * modifies the background to reference a rounded background sized to fit
     * the dimensions of the element.
     *
     * Parameters:
     *   selector: a css selector refering to elements that may be in the page. E.g.
     *           ".box" selects elements with the "box" css class.
     *   curve:  the radius of rounded corners. If null, defaults to a value
     *           computed from the left padding of the region.
     *
     * DEPRECATED: please convert to mkGeneralShapeRuleSet
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
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes a rectangular background rounded on all but the upper left corner.
     * This function selects html elements based on css selector and, for each,
     * modifies the background to reference a rounded background sized to fit
     * the dimensions of the element.
     *
     * Parameters:
     *   selector: a css selector refering to elements that may be in the page. E.g.
     *           ".box" selects elements with the "box" css class.
     *   props: An object defining additional properties. See class commentary.
     *
     * DEPRECATED: use mkGeneralShapeRuleSet
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
                //                extEl.setStyle("backgroundColor", "transparent");
            }
        }
    },

    /**
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes a rectangular background rounded on all corners with no border.
     * This function selects html elements based on css selector and, for each,
     * modifies the background to reference a rounded background sized to fit
     * the dimensions of the element.
     *
     * Parameters:
     *   selector: a css selector refering to elements that may be in the page. E.g.
     *           ".box" selects elements with the "box" css class.
     *   curve:  the radius of rounded corners. If null, defaults to a value
     *           computed from the left padding of the region.
     *
     * DEPRECATED: use mkGeneralShapeRuleSet
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

                //                alert("url="+url);
                extEl.setStyle("backgroundImage", "url(" + url + ")");
                extEl.setStyle("backgroundRepeat", "no-repeat");
                extEl.setStyle("backgroundColor", "transparent");
                //                extEl.setStyle("borderWidth", "0");
            }
        }
    },
    /**
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes a rectangular background rounded on all corners with no border and
     * simulated lighting.
     * This function selects html elements based on css selector and, for each,
     * modifies the background to reference a rounded background sized to fit
     * the dimensions of the element.
     *
     * Parameters:
     *   selector: a css selector refering to elements that may be in the page. E.g.
     *           ".box" selects elements with the "box" css class.
     *   curve:  the radius of rounded corners. If null, defaults to a value
     *           computed from the left padding of the region.
     *   fillColor: the background color
     *
     * DEPRECATED: use mkGeneralShapeRuleSet
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

                //                alert("url="+url);
                extEl.setStyle("backgroundImage", "url(" + url + ")");
                extEl.setStyle("backgroundRepeat", "no-repeat");
                extEl.setStyle("backgroundColor", "transparent");
                //                extEl.setStyle("borderWidth", "0");
            }
        }
    },
    /**
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes a rectangular background rounded on all corners with no border and
     * simulated lighting.
     * This function selects html elements based on css selector and, for each,
     * modifies the background to reference a rounded background sized to fit
     * the dimensions of the element.
     *
     * Parameters:
     *   selector: a css selector refering to elements that may be in the page. E.g.
     *           ".box" selects elements with the "box" css class.
     *   curve:  the radius of rounded corners. If null, defaults to a value
     *           computed from the left padding of the region.
     *   shadingStyle: spotlight|highlight|topHalf|bottomHalf
     *   shadingLevelPct: amount of shading
     *   fillColor: the background color
     *
     * DEPRECATED: use mkGeneralShapeRuleSet
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

                //                alert("url="+url);
                extEl.setStyle("backgroundImage", "url(" + url + ")");
                extEl.setStyle("backgroundRepeat", "no-repeat");
                extEl.setStyle("backgroundColor", "transparent");
                //                extEl.setStyle("borderWidth", "0");
            }
        }
    },
    /**
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes and sets a shaded background resembling a 3 dimensional horizontal bar for
     * selected elements.
     *
     * DEPRECATED: use mkGeneralShapeRuleSet
     */
    make3dBarBackground: function (selector, fillColor, shadingLevelPct) {
        //        alert("start")
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
                //                alert("p=" + this.toString(p));
                var url = this.getImageDirectory() + "/shape.svg_png?" +
                          Ext.urlEncode(p);
                //                alert("url="+url);
                extEl.setStyle("backgroundImage", "url(" + url + ")");
                extEl.setStyle("backgroundRepeat", "repeat-x");
            }
        }
    },

    /**
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes and sets a rounded box gradient background for selected elements.
     *
     * DEPRECATED: use mkGeneralShapeRuleSet
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
                //                alert("url="+url);
                extEl.setStyle("backgroundImage", "url(" + url + ")");
                extEl.setStyle("backgroundRepeat", "no-repeat");
                extEl.setStyle("backgroundColor", "transparent");
                //                extEl.setStyle("borderWidth", "0");
            }
        }
    },

    /**
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes a url that will generate a simple rectangular gradient
     *
     * DEPRECATED: use mkGeneralShapeRuleSet
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
                //                alert("url="+url);
                extEl.setStyle("backgroundImage", "url(" + url + ")");
                extEl.setStyle("backgroundRepeat", "no-repeat");
            }
        }
    },

    /**
     * DEPRECATED: use mkGeneralShapeRuleSet
     *
     * Makes a "splat" shape sized for the selected element and sets it as the
     * background.
     *
     * DEPRECATED: use mkGeneralShapeRuleSet
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
                //                alert("url="+url);
                extEl.setStyle("backgroundImage", "url(" + url + ")");
                extEl.setStyle("backgroundRepeat", "no-repeat");
                extEl.setStyle("backgroundColor", "transparent");
                extEl.setStyle("borderWidth", "0");
            }
        }
    },


    /**
     * DEPRECATED: Alters style of element, so can cause problems on refresh. Use
     * mkGeneralShapeRuleSet().
     */
    makeGeneralShape: function(selector, values) {
        if (Ext) {
            var selectedArr = Ext.query(selector);
            for (var i = 0; i < selectedArr.length; i++) {
                var extEl = Ext.get(selectedArr[i]);
                var p = this.inferFromContext(extEl, values);
                var url = this.getImageDirectory() + "/shape.svg_png?" +
                          Ext.urlEncode(p);
                //                alert("url="+url);
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
                //                alert("url="+url);

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
                //                alert("url="+url);

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
 * Determines values for named shape parameters from a page context. For
 * example, determines width from element width. Parameters with a non-null
 * value are ignored.
 */
    inferFromContext: function(extEl, values) {
        //        alert("inferFromContext-start:");
        var result = {};

        for (var key in values) {
            var value = values[key];
            if (value == null) {
                switch (key) {
                    case "width":
                        result.width = extEl.getComputedWidth();
                        if (result.width<5) {
//                            alert("width<5:" + extEl.id);
                            result.width=5;
                        }
                        break;
                    case "height":
                        result.height = extEl.getComputedHeight();
                        if (result.height<5) {
//                            alert("height<5:" + extEl.id);
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
//                            alert("no parent");
                            result.outsideColor = "white";
                        }
                        break;
                    case "outsideOpacityPct":
//                        var parent = extEl.parent();
                        if (parent) {
//                            result.outsideOpacityPct = parent.getStyle("backgroundImage")?0:100;
                            result.outsideOpacityPct = 100;
                        }
                        else {
                            result.outsideOpacityPct = 0;
                        }
                        break;
//                    case "stopColor":
//                        result.stopColor = "white";
//                        break;
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
     * A utility function to load the image directory for URL construction.
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
     * Utility to merge obj2 onto obj1 any fields not already defined in obj1.
     * Returns the modified obj1
     */
    mergeOnto: function (obj1, obj2) {
        for (var i in obj2) {
            //print(i + "," + obj1[i] + "," + obj2[i]);
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


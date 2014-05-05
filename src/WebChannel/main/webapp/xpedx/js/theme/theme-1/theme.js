Ext.onReady(function(){
  //  decorators.initialize();
});

function svg_classhandlers_decoratePage() {
//    decorators.decoratePage();
}

var decorators= {
    imagesLoaded: false,

    /*
     * This method handles image loading detection. It loads an image and when and
     * if it returns it sets a variable and decorates the page. It is derived from:
     * http://www.quirksmode.org/dom/fir.html
     */
    initialize: function() {
        var W3CDOM = (document.createElement && document.getElementsByTagName);
	if (!W3CDOM) return;
	var test = new Image();
	var tmp = new Date();
	var suffix = tmp.getTime();
	test.src = this.getContext() + '' + XPEDXWCUtils_STATIC_FILE_LOCATION + '/xpedx/images/common/fir_assumptions.gif?'+suffix;
	test.onload = function() {
            decorators.imagesLoaded = true;
            decorators.decoratePage();
        };

    },
    getContext: function() {
        var context, contextEl;
        contextEl = Ext.query("meta[name='webapp-context']")[0];
        if (contextEl == null) {
            context = "";
        } else {
            context = contextEl.getAttribute('content');
        }
        return context;
    },

    decoratePage: function() {
        if (this.imagesLoaded) {
            var stylesheet = "\n";
            var stylesheetName = "backgrounds-stylesheet";
            Ext.util.CSS.removeStyleSheet(stylesheetName);
        //    decorators.checkImagesLoaded();
            stylesheet+=decorators.decorateTabs();
            stylesheet+=decorators.decorateButtons();
            stylesheet+=decorators.decorateBoxes();
            stylesheet+=decorators.decorateDialogContainer();
            stylesheet+=decorators.decorateListTable();
            stylesheet+=decorators.decorateNavBars();
            stylesheet+=decorators.decorateHeaders();
            Ext.util.CSS.createStyleSheet(stylesheet, stylesheetName);
            SVGAnnotator.make3dBarBackground("#navigateSteps");
        }
    },
//    checkImagesLoaded: function() {
//        if (document.images.length > 0) {
//            var i = document.images[0];
//            if (i.offsetWidth>0) {
//                this.imagesLoaded = true;
//                alert("images loaded: " + i.src + "(complete=" + i.complete + ", width:" + i.width + ", offsetWidth:" + i.offsetWidth +")");
//            }
//        }
//    },
//
    decorateHeaders: function() {
        var rules = "";
        //var headerElements = ".t4-header";
        var headerElements = ".commonHeader";
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(headerElements, {
            cssClass:'headerBG',
            width:null,
            height: null,
            borderWidth:1,
            borderColor:"white",
            curve:5,
            dropShadowOffsetX:-2,
            dropShadowOffsetY:3,
            dropShadowOpacityPct:25,
            fillGradientType:'b2t',
            fillColor:"#e5e5e5",
            fillOpacityPct:95
        })
        rules += SVGAnnotator.mkTopRoundRuleSet(".searchbox-1", {
            cssClass:'searchboxBG',
            shape:'CutCornerRect',
            width:null,
            height: 80,
            fillColor:'#cccccc',
            borderColor:"#dddddd",
            outsideColor:null,
            outsideOpacityPct:0,
            borderWidth:1,
            shadingStyle:'#halfSineSqB',
            shadingStartPct:15,
            shadingEndPct:30,
            shadingLevelPct:70,
            curve:20, curveY:30 });
        rules += headerElements + ",.searchbox-1 {background-repeat:no-repeat;};\n";
        return rules;
    },

    decorateNavBars: function() {
        var rules = "";
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".progressBar li.active", {
            cssClass:'activeProgressBarBG',
            width:1,
            height:null,
            fillColor:'#aaaaaa',
            curve:0,
            borderWidth:0,
            shadingStyle:'#halfSineSqB',
            shadingStartPct:30,
            shadingEndPct:90,
            shadingLevelPct:90
        });
        rules+=SVGAnnotator.mkGeneralShapeRuleSet("#navigate11 li a span", {
            cssClass:'navigateHoverBG',
            width:1,
            height:null,
            fillColor:'#aaaaaa',
            curve:0,
            borderWidth:0,
            shadingStyle:'#halfSineSqB',
            shadingStartPct:30,
            shadingEndPct:90,
            shadingLevelPct:90
        }, ":hover");
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".progressBar, #navigate11", {
            cssClass:'navigateBG',
            width:1,
            height:null,
            fillColor:'#cccccc',
            curve:0,
            borderWidth:0,
            shadingStyle:'#halfSineSqB',
            shadingStartPct:30,
            shadingEndPct:90,
            shadingLevelPct:100
        });
        rules+=".progressBar {background-color: transparent; }\n";
        rules+=".progressBar li.active {background-repeat:repeat-x; }\n";
        return rules;
    },

    decorateTabs: function(){
        var rules = "";
        Ext.select(".tabContent").setStyle("borderWidth", "0");
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".tabContent", {
            cssClass:'tabContentBG',
            width:null,
            height:null,
            fillColor:null,
            borderColor:"#dddddd",
            curve:8,
            outsideColor:null,
            shape:"3qRoundedRect",
            borderWidth: 1,
            outsideOpacityPct: 0
        });
        rules+= ".tabContent {background-color: transparent;}";

        rules += SVGAnnotator.mkTopRoundRuleSet(".tab-1 li.selectable", {
            cssClass:'tab1SelectableBG',
            width:null,
            height: null,
            fillColor:null,
            borderColor:"#dddddd",
            outsideColor:null,
            outsideOpacityPct:100,
            borderWidth:1,
            fillGradientTargetColor:"#d0d0d0",
            fillGradientType:"t2b",
            curve:5 }, ":hover");
        rules += SVGAnnotator.mkTopRoundRuleSet(".tab-1 li", {
            cssClass:'tab1BG',
            width:null,
            height: null,
            fillColor:null,
            borderColor:"#dddddd",
            outsideColor:null,
            outsideOpacityPct:100,
            borderWidth:1,
            fillGradientTargetColor:"#b0b0b0",
            fillGradientType:"t2b",
            curve:5 });
        rules += SVGAnnotator.mkTopRoundRuleSet(".tab-1 li.selected", {
            cssClass:'tab1SelectedBG',
            width:null,
            height: null,
            fillColor:null,
            borderColor:null,
            outsideColor:null,
            outsideOpacityPct:100,
            borderWidth:1,
            fillGradientTargetColor:"#b0b0b0",
            fillGradientType:"b2t",
            shadingStyle:"highlight",
            shadingLevelPct:90,
            curve:5 });
        return rules;
    },

    decorateButtons: function() {
        var rules = "";
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".submitBtnBg1", {
            cssClass:'submitBtnBg1BG',
            width:null,
            height:null,
            fillColor:"#bbbbbb",
            fillGradientType:"t2b",
            outsideColor:null,
            outsideOpacityPct:0,
            shadingStyle:'sine',
            shadingLevelPct: 98,
            borderWidth: 1,
            borderColor: '#bbbbbb',
//            margin: 1,
            curve: 5}, ":hover");

        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".submitBtnBg1", {
            cssClass:'submitBtnBg1BG',
            width:null,
            height:null,
            fillColor:"#bbbbbb",
            fillGradientType:"b2t",
            outsideColor:null,
            outsideOpacityPct:0,
            shadingStyle:'sine',
            shadingLevelPct: 98,
            borderWidth: 1,
            borderColor: '#bbbbbb',
//            margin:1,
            curve: 5 });

        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnBg2', {
            cssClass:'submitBtnBg2BG',
            width:null,
            height:null,
            fillColor:"#0A5691",
            outsideColor:null,
            outsideOpacityPct:0,
            fillGradientType:"b2t",
            fillGradientTargetColor:"#0C95E6",
            borderWidth: 0,
//            margin: 1,
            curve:5 }, ":hover");

        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnBg2', {
            cssClass:'submitBtnBg2BG',
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacityPct:0,
            fillGradientType:"b2t",
            fillGradientTargetColor:"#037FC8",
            borderWidth: 0,
//            margin: 1,
            curve:5 });
        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnBg3', {
            cssClass:'submitBtnBg3BG',
            width:null,
            height:null,
            fillColor:"#bbbbbb",
            fillGradientType:"t2b",
            outsideColor:null,
            outsideOpacityPct:0,
            shadingStyle:'sine',
            shadingLevelPct: 98,
            borderWidth: 1,
            borderColor: '#bbbbbb',
//            margin: 1,
            curve: 5}, ":hover");
        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnBg3', {
            cssClass:'submitBtnBg3BG',
            width:null,
            height:null,
            fillColor:null,
            fillGradientType:"b2t",
            outsideColor:null,
            outsideOpacityPct:0,
            shadingStyle:'sine',
            shadingLevelPct: 98,
            borderWidth: 1,
            borderColor: '#bbbbbb',
//            margin:1,
            curve: 5 });
        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnBg4', {
            cssClass:'submitBtnBg4BG',
            width:null,
            height:null,
            fillColor:null,
            fillGradientType:"t2b",
            outsideColor:null,
            outsideOpacityPct:0,
            shadingStyle:'sine',
            shadingLevelPct: 98,
            borderWidth: 1,
            borderColor: '#bbbbbb',
//            margin: 1,
            curve: 5}, ":hover");
        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnBg4', {
            cssClass:'submitBtnBg4BG',
            width:null,
            height:null,
            fillColor:null,
            borderColor:null,
            outsideColor:null,
            outsideOpacityPct:0,
            shadingStyle:'sine',
            shadingLevelPct:90,
            borderWidth:0,
//            margin: 1,
            curve:5
        });
        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnCheckout', {
            cssClass:'submitBtnCheckoutBG',
            width:null,
            height:null,
            fillColor:"#00E600",
            outsideColor:null,
            outsideOpacityPct:0,
            fillGradientType:"b2t",
            fillGradientTargetColor:"#00cc00",
            borderWidth: 0,
//            margin: 1,
            curve:5 }, ":hover");

        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnCheckout', {
            cssClass:'submitBtnCheckoutBG',
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacityPct:0,
            fillGradientType:"t2b",
            fillGradientTargetColor:"#00B300",
            borderWidth: 0,
//            margin: 1,
            curve:5 });
        rules+=".submitBtnBg1,.submitBtnBg2,.submitBtnBg3,.submitBtnBg4,.btn-go{background-repeat:no-repeat;}\n";
        rules+=".submitBtnBg1,.submitBtnBg2,.submitBtnBg3,.submitBtnBg4,.btn-go{background-position:center center;}\n";
        rules+=    SVGAnnotator.mkGeneralShapeRuleSet(".btn-go", {
            cssClass:'btnGoBG',
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacityPct:0,
            shadingStyle:'sine',
            shadingLevelPct: 98,
            borderWidth: 1,
            borderColor: '#004e50',
            curve: 5,
//            margin: 1,
            dropShadowOffset: 1
        }, ":hover");
        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.btn-go', {
            cssClass:'btnGoBG',
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacityPct:0,
            shadingStyle:'sine',
            shadingLevelPct: 80,
            borderWidth: 1,
            borderColor: 'darkgray',
//            margin: 1,
            curve: 5
        });
        rules+=".btn-go {background-color:transparent;}\n"
        return rules;
    },

    decorateListTable: function() {
        var rules = "";
        rules+=".listTableContainer,.listTableHeader {border-width:0;}\n";
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".listTableContainer", {
            cssClass:'listTableContainerBG',
            width:null,
            height:null,
            fillColor:null,
            borderColor:"lightgray",
            curve:10,
            outsideColor:null,
            outsideOpacityPct:null,
            borderWidth:1});
        rules += SVGAnnotator.mkTopRoundRuleSet('.listTableHeader', {
            cssClass:'listTableHeaderBG',
            width:null,
            height: null,
            fillColor:null,
            borderColor:"gray",
            curve:10,
            outsideColor:null,
            outsideOpacityPct:100,
            borderWidth:1});
        rules+=".listTableContainer,.listTableHeader {background-repeat:no-repeat;}\n";
        return rules;
    },

    decorateBoxes:function() {
        var rules = "";
        var listTableContent='.panel-n-bg-bottom,.contentLeftBottom';
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(listTableContent, {
            cssClass:'panelNBG',
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            curve:5,
            borderWidth:1,
            borderColor: '#d0d0d0',
            dropShadowOffsetX:1,
            dropShadowOffsetY:1,
            dropShadowOpacityPct:50,
            outsideOpacityPct:0
        });
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".sign-in", {
            cssClass:'signInBG',
            width:null,
            height:null,
            fillColor:null,
            fillGradientType:'t2b',
            fillGradientTargetColor:'#eeeeee',
            curve:5,
            borderWidth:0,
            outsideOpacityPct:100,
            outsideColor:null
        });
//        rules+=".sign-in {background-color: transparent; }\n";

        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.t4-prom-content', {
            cssClass:'t4PromContentBG',
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            curve:5,
            borderWidth:1,
            borderColor: '#d0d0d0',
            fillGradientType:'t2b',
            fillGradientTargetColor:'#f7f4ee',
            dropShadowOffsetX:1,
            dropShadowOffsetY:1,
            dropShadowOpacityPct:50,
            outsideOpacityPct:0
        });
        rules += SVGAnnotator.mkTopRoundRuleSet('.subPanelBox .header', {
            cssClass:'subPanelBoxHeaderBG',
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacityPct:0,
            borderWidth: 1,
            borderColor: null,
            curve:5});

        rules += SVGAnnotator.mkGeneralShapeRuleSet('.subPanelBox, .groupPanelBox .content', {
            cssClass:'subPanelBoxBG',
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacityPct:100,
            borderWidth: 1,
            borderColor: null,
            curve:5
        });

        rules += SVGAnnotator.mkGeneralShapeRuleSet('.panelBox, .groupPanelBox', {
            cssClass:'grayBoxBG',
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacityPct:100,
            borderWidth: 0,
            curve:5
        });

        rules += SVGAnnotator.mkGeneralShapeRuleSet('.breadcrumb', {
            cssClass:'breadcrumbBG',
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacityPct:100,
            borderWidth: 1,
            borderColor: '#e2e2e2',
            fillGradientType:"b2t",
            curve:5
        });

        rules += SVGAnnotator.mkGeneralShapeRuleSet('#item-ct dd.itemdiv', {
            cssClass:'itemCtDdOverBG',
            width:10,
            height:null,
            fillColor:null,
            fillGradientType:'b2t',
            outsideColor:null,
            curve:0,
            borderWidth:0
        });
        return rules;
    },

    decorateDialogContainer: function() {
        var rules = "";
        rules += SVGAnnotator.mkGeneralShapeRuleSet(".dialog-container", {
            cssClass:'dialogContainerBG',
            width:null,
            height:null,
            fillColor:null,
            borderColor:null,
            outsideColor:null,
            outsideOpacityPct:null,
            curve:0,
            dropShadowOffset:5,
            borderWidth:1});
            return rules;
        }
}

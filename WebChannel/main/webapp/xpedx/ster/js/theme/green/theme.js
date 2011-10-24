Ext.onReady(function(){
    svg_classhandlers_decoratePage();
});

function svg_classhandlers_decoratePage() {
    var stylesheet = "\n";
    var stylesheetName = "backgrounds-stylesheet";
    Ext.util.CSS.removeStyleSheet(stylesheetName);
    stylesheet+=decorators.decorateTabs();
    stylesheet+=decorators.decorateButtons();
    stylesheet+=decorators.decorateBoxes();
    stylesheet+=decorators.decorateDialogContainer();
    stylesheet+=decorators.decorateListTable();
    stylesheet+=decorators.decorateNavBars();
    Ext.util.CSS.createStyleSheet(stylesheet, stylesheetName);
    SVGAnnotator.make3dBarBackground("#navigateSteps");
}

var decorators= {
    decorateNavBars: function() {
        var rules = "";
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".progressBar", {
            width:1,
            height:null,
            fillColor:"gray",
            shadingStyle:"highlight",
            shadingLevelPct:"90",
            curve:0,
            borderWidth:0
        });
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".progressBar li.active", {
            width:1,
            height:null,
            fillColor:"black",
            shadingStyle:"sine",
            shadingLevelPct:"40",
            curve:0,
            borderWidth:0
        });
        rules+=".progressBar {background-color: transparent; }\n";
        rules+=".progressBar li.active {background-repeat:repeat-x; }\n";
        return rules;
    },
    decorateTabs: function(){
        var rules = "";
        Ext.select(".tabContent").setStyle("borderWidth", "0");
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".tabContent", {
            width:null,
            height:null,
            fillColor:null,
            borderColor:"gray",
            curve:null,
            outsideColor:null,
            shape:"3qRoundedRect",
            borderWidth: 1,
            outsideOpacity: 0,
            dropShadowOffset: 10 });
        rules+= ".tabContent {background-color: transparent;}";

        rules += SVGAnnotator.mkTopRoundRuleSet(".tab-1 li.selectable", {
            width:null,
            height: null,
            fillColor:'#E5F1EF',
            borderColor:null,
            outsideColor:null,
            outsideOpacity:0,
            borderWidth:1,
            curve:5 }, ":hover");
        rules += SVGAnnotator.mkTopRoundRuleSet(".tab-1 li", {
            width:null,
            height: null,
            fillColor:null,
            borderColor:null,
            outsideColor:null,
            outsideOpacity:0,
            borderWidth:1,
            curve:5 });
        return rules;
    },

    decorateButtons: function() {
        var rules = "";
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".submitBtnBg1", {
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacity:null,
            shadingStyle:'sine',
            shadingLevelPct: 98,
            borderWidth: 1,
            borderColor: '#004e50',
            curve: 5,
            dropShadowOffset: 1 }, ":hover");

        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".submitBtnBg1", {
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacity:null,
            shadingStyle:'sine',
            shadingLevelPct: 80,
            borderWidth: 1,
            borderColor: 'darkgray',
            curve: 5 });

        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnBg2', {
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacity:null,
            shadingStyle:'sine',
            shadingLevelPct: 90,
            borderColor: '#004e50',
            borderWidth: 0,
            curve:5 }, ":hover");

        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnBg2', {
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacity:null,
            shadingStyle:'sine',
            shadingLevelPct: 60,
            borderColor: 'darkgray',
            borderWidth: 1,
            curve:5 });
        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnBg3', {
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacity:null,
            shadingStyle:'sine',
            shadingLevelPct: 100,
            borderColor: '#004e50',
            borderWidth:1,
            curve:5 }, ":hover");
        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnBg3', {
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacity:null,
            shadingStyle:'sine',
            shadingLevelPct: 90,
            borderColor: 'darkgray',
            borderWidth:1,
            curve:5 });
        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.submitBtnBg4', {
            width:null,
            height:null,
            fillColor:null,
            borderColor:null,
            outsideColor:null,
            outsideOpacity:null,
            shadingStyle:'sine',
            shadingLevelPct:90,
            borderWidth:0,
            curve:5
        });
        rules+=".submitBtnBg1,.submitBtnBg2,.submitBtnBg3,.submitBtnBg4{background-repeat:no-repeat;}\n";
        rules+=    SVGAnnotator.mkGeneralShapeRuleSet(".btn-go", {
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacity:null,
                shadingStyle:'sine',
                shadingLevelPct: 98,
                borderWidth: 1,
                borderColor: '#004e50',
                curve: 5,
                dropShadowOffset: 1
            }, ":hover");
        rules+=SVGAnnotator.mkGeneralShapeRuleSet('.btn-go', {
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacity:null,
            shadingStyle:'sine',
            shadingLevelPct: 80,
            borderWidth: 1,
            borderColor: 'darkgray',
            curve: 5
        });
        rules+=".btn-go {background-color:transparent;}\n";
        return rules;
    },

    decorateListTable: function() {
        var rules = "";
        rules+=".listTableContainer,listTableHeader {border-width:0;}\n";
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".listTableContainer", {
            width:null,
            height:null,
            fillColor:null,
            borderColor:"lightgray",
            curve:10,
            outsideColor:null,
            outsideOpacity:null,
            borderWidth:1});
        rules += SVGAnnotator.mkTopRoundRuleSet('.listTableHeader', {
            width:null,
            height: null,
            fillColor:null,
            borderColor:"gray",
            curve:10,
            outsideColor:null,
            outsideOpacity:0,
            borderWidth:1});
        return rules;
    },

    decorateBoxes:function() {
        var rules = "";
        var listTableContent='.sign-in-bg-bottom,.panel-n-bg-bottom';
        rules+=SVGAnnotator.mkGeneralShapeRuleSet(listTableContent, {
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            curve:10,
            borderWidth:1,
            borderColor: '#ccc',
            outsideOpacity:0 });

        rules+=SVGAnnotator.mkGeneralShapeRuleSet(".searchbox-form", {
            width:null,
            height:null,
            fillColor:null,
            outsideColor:null,
            outsideOpacity:0,
            curve:6,
            borderWidth:0,
            shadingStyle:'sine',
            shadingLevelPct:30
        });
        return rules;
    },

    decorateDialogContainer: function() {
        var rules = "";
        rules += SVGAnnotator.mkGeneralShapeRuleSet(".dialog-container", {
        width:null,
        height:null,
        fillColor:null,
        borderColor:null,
        outsideColor:null,
        outsideOpacity:null,
        curve:0,
        dropShadowOffset:5,
        borderWidth:1});
        return rules;
    }
}

Ext.namespace("sc.sbc.util");

sc.sbc.util.ScreenPopupUtils = function() {

	var storedConfig = {};
	var screen = {};

	function callBackHandler(res, options) {
		options.extraParams = options.extraParams || {};
		if(!(options.extraParams.cacheable === false)) {
			screen[options.extraParams.entity] = res.responseText;
		}
		eval(res.responseText);
	}
	
	var popupActions = {
		"coupon" : function(conf) {
			seaAjaxUtils.request({
				action : "couponSearchPopup",
				actionNS : "/sbc/pricing",
				success : callBackHandler,
				scope : this,
				extraParams : conf
			});
		},

		"item" : function(config){
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.ItemStrutsNS,
				action : "itemsearchpopup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},

		"node" : function(config){
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.ItemStrutsNS,
				action : "nodesearchpopup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		
		"Organization" : function(config){
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.CommonStrutsNS,
				action : "organizationsearchpopup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		
		"AttributeLookup": function(config){
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.ItemStrutsNS,
				action : "openattributelookup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		
		"CommonCodeManagement" :function(config){
			if(config.popupParams.commonCodeType == null){
	    		Ext.MessageBox.alert("Error", "The 'commonCodeType' parameter must be specified.");
	    	}else{
			seaAjaxUtils.request({
				actionNS : "sbc/common",
					action : "commoncodepopup-get"+config.popupParams.commonCodeType,
					inputNS : "getCommonCode_input",
					inputObj : {"CommonCode":{}},
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
	    	}
		},
		"NewCommonCode":function(config){
			var screenPopupUtils = sc.sbc.util.ScreenPopupUtils;
			var entity = screenPopupUtils.NewCommonCode;
			var popupParams = screenPopupUtils.getPopupParams(entity);
			var commonCode = new sc.sbc.common.commoncode.newCommonCode(popupParams);
			commonCode.height=125;
			commonCode.width=390;
			commonCode.title=commonCode.b_NewCommonCode;
			Ext.callback(screenPopupUtils.registerPopupScreen, screenPopupUtils, [entity, commonCode]);
		},
		
		"NewAllowedValue":function(config){
			var screenPopupUtils = sc.sbc.util.ScreenPopupUtils;
			var entity = screenPopupUtils.NewAllowedValue;
			var popupParams = screenPopupUtils.getPopupParams(entity);
			var allowedValue = new sc.sbc.itemadmin.attribute.manage.attrdetails.newallowedvalue(popupParams);
			Ext.callback(screenPopupUtils.registerPopupScreen, screenPopupUtils, [entity, allowedValue]);
		},
		
		"storefront" : function(config){
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.ItemStrutsNS,
				action : "storefrontsearchpopup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		"PriceList" : function(config){
			seaAjaxUtils.request({
				actionNS : "/sbc/pricing",
				action : "priceListSearchPopup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});

		},
		"DeleteOldIndexes" : function(config){
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.SystemStrutsNS,
				action : "openDeleteOldIndexPopup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		"CatalogSelection" : function(config){
			var input = {"CategoryDomainList":{}};
			if(sc.sbc.core.context.JSContext.isCatalogOrg){
				input.OrganizationCode=sc.sbc.core.context.JSContext.getCurrentOrganizationCode();
				input.AuthorizedSubCatalogOrganizationCodeQryType="ISNULL"; 
			}else{
				input.AuthorizedSubCatalogOrganizationCode=sc.sbc.core.context.JSContext.currentOrg;
			}
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.SystemStrutsNS,
				action : "openCatalogSelectionPopup",
				inputNS : "getCategoryDomainList_input",
				inputObj : input,
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		"serviceOption" : function(config){
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.ItemStrutsNS,
				action : "serviceoptionsearchpopup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		"service" : function(config){
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.ItemStrutsNS,
				action : "servicesearchpopup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		"CustomerSearch" : function(config){
			seaAjaxUtils.request({
				actionNS : "/sbc",
				action : "customerListSearchPopup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		"Asset" : function(config){
			var screenPopupUtils = sc.sbc.util.ScreenPopupUtils;
			var entity = screenPopupUtils.Asset;
			var popupParams = screenPopupUtils.getPopupParams(entity);
			var assetDetail = new sc.sbc.itemadmin.common.asset.assetdetail(popupParams);
			assetDetail.showFields(true);
			assetDetail.height=200;
			assetDetail.width=500;
			Ext.callback(screenPopupUtils.registerPopupScreen, screenPopupUtils, [entity, assetDetail]);
		},
		"SellingCatalog" : function(config){
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.ItemStrutsNS,
				action : "sellingcataloglookup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		"SellingRegion" :  function(config){
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.CommonStrutsNS,
				action : "sellingregiontreelookup",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		"AssetManagement" : function(config){
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.ItemStrutsNS,
				action : "attributevalueassets-getattributevalueassets",
				success : callBackHandler,
				scope : this,
				extraParams : config
			});
		},
		"CatalogTree" : function(conf){
			
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.ItemStrutsNS,
				action : "catalogdetailtreelookup",
				inputNS : "getCategoryDomainDetails_input",
				params : conf.actionParam,
				inputObj : conf.actionInputObj,
				success : callBackHandler,
				scope : this,
				extraParams : conf
			});
		},
		
		"ClassificationTree" : function(conf){
			
			seaAjaxUtils.request({
				actionNS : sc.sbc.App.ItemStrutsNS,
				action : "classificationdetailtreelookup",
				inputNS : "getCategoryDomainDetails_input",
				params : conf.actionParam,
				inputObj : conf.actionInputObj,
				success : callBackHandler,
				scope : this,
				extraParams : conf
			});
		}

	};
	
	function getScreen(conf) {
		if (popupActions[conf.entity]) {
			storedConfig[conf.entity] = {};
			Ext.apply(storedConfig[conf.entity], conf);
			popupActions[conf.entity](conf);
		}
	}
	
	return {
		Coupon : "coupon",
		AttributeLookup : "AttributeLookup",
		CommonCodeManagement : "CommonCodeManagement",
		Item : "item",
		Node : "node",
		Organization : "Organization",
		PriceList : "PriceList",
		Storefront : "storefront",
		DeleteOldIndexes : "DeleteOldIndexes",
		CatalogSelection : "CatalogSelection",
		ServiceOption : "serviceOption",
		Service : "service",
		CustomerSearch : "CustomerSearch",
		Asset : "Asset",
		SellingCatalog : "SellingCatalog",
		SellingRegion : "SellingRegion",
		AssetManagement : "AssetManagement",
		CatalogTree : "CatalogTree",
		ClassificationTree : "ClassificationTree",
		NewCommonCode: "NewCommonCode",
		NewAllowedValue:"NewAllowedValue",
		
		launchPopup : function(conf) {
			if (screen[conf.entity]) {
				storedConfig[conf.entity] = {};
				Ext.apply(storedConfig[conf.entity], conf);
				eval(screen[conf.entity]);
			} else {
				getScreen(conf);
			}
		},
		getPopupParams : function(entity) {
			storedConfig[entity] = storedConfig[entity] || {};
			storedConfig[entity].popupParams = storedConfig[entity].popupParams || {};
			return storedConfig[entity].popupParams;
		},
		registerPopupScreen : function(entity, popupScreen) {
			this.openWindow(entity, popupScreen);
			if (storedConfig[entity].cacheable === false) {
				delete storedConfig[entity];
				if(screen[entity])
					delete screen[entity];
			}
		},
		openWindow : function(entity, popScreen) {
			var conf = storedConfig[entity];
			var winConfig = {screen : popScreen};
			winConfig.windowConfig = winConfig.windowConfig || winConfig.screen.windowConfig || {};
			Ext.apply(winConfig, conf.popupConfig);
			if (!conf.callback) {
			if (winConfig.windowConfig.hasOwnProperty("closeAction")) {
				delete winConfig.windowConfig["closeAction"];
			}
			Ext.apply(winConfig.windowConfig, {closeAction : "close"});
			}
			var popupWindow = sc.sbc.util.WindowUtils.getWindow(winConfig);
			if(conf.callback)
				Ext.callback(conf.callback, conf.scope, [popupWindow, winConfig]);
			else
				popupWindow.show();
		}
	}
}();

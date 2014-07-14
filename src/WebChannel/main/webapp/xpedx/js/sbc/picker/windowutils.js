Ext.namespace("sc.sbc.util");

sc.sbc.util.WindowUtils = function() {	
	
	

	var closeHandler = function(win, e) {
		if(win.fireEvent("beforeclose", win) === false){
            return;
        }
        var opt = this.opt;
        if(opt.handlers["close"]) {
        	var popupData = {};
        	if(opt.getDataHandler) {
        		popupData = opt.getDataHandler.apply(opt.screen);
        	} else if(opt.screen.getPopupData) {
        		popupData = opt.screen.getPopupData.apply(opt.screen);
        	}
        	var handler = opt.handlers["close"] || Ext.emptyFn;
        	var close = handler.apply(opt.scope||window, [popupData, opt.screen, opt.parent]);
        	if(close)
				this.win[this.win.closeAction]();
        } else {
			this.win[this.win.closeAction]();
        }
	};

	var okHandler = function(win, e) {
		var opt = this.opt;
		var screen = opt.screen;
        win.fireEvent("afterOK", win);
        if(opt.handlers["ok"] || opt.handlers["select"] || opt.handlers["save"] || opt.handlers["create"] || opt.handlers["confirm"]) {
        	if(sc.sbc.util.CoreUtils.screenHasMethod(screen, "hasError") && screen.hasError()){
        		sc.sbc.util.CoreUtils.showErrorFromScreenComponent(screen);
        		return;
        	}
        	var popupData = {};
        	if(opt.getDataHandler) {
        		popupData = opt.getDataHandler.apply(opt.screen);
        	} else if(opt.screen.getPopupData) {
        		popupData = opt.screen.getPopupData.apply(opt.screen);
        	}
        	var handler = opt.handlers["ok"] || opt.handlers["select"] || opt.handlers["save"] || opt.handlers["create"] || opt.handlers["confirm"] || Ext.emptyFn;
        	var close = handler.apply(opt.scope||window, [popupData, opt.screen, opt.parent, this.win]);
        	if(close)
				Ext.callback(closeHandler, this, [win, e]);
        }
	};
	
	var dblClickHandler = function() {
		var opt = this.opt;
		var popupData = {};
		if (opt.getDataHandler) {
			popupData = opt.getDataHandler.apply(opt.screen);
		} else if (opt.screen.getPopupData) {
			popupData = opt.screen.getPopupData.apply(opt.screen);
		}
		var handler = opt.dblClickHandler;
		var close = handler.apply(opt.scope || window, [popupData, opt.screen, opt.parent, this.win]);
		if (close)
			this.win[this.win.closeAction]();
	}
	
	var onWindowShow = function(win) {
		if(!Ext.isEmpty(this.initWindow) && this.initWindow instanceof Function) {
			this.initWindow.apply(this, [win]);
		}
	}
	
	var continueHandler = function(){
		var opt = this.opt;
		var popupData = {};
		if (opt.getDataHandler) {
			popupData = opt.getDataHandler.apply(opt.screen);
		} else if (opt.screen.getPopupData) {
			popupData = opt.screen.getPopupData.apply(opt.screen);
		}
		var handler = opt.handlers["savecontinue"];
		var close = handler.apply(opt.scope || window, [popupData, opt.screen, opt.parent, this.win]);
		if (close)
			this.win[this.win.closeAction]();
	}

	var getCloseButtonConfig = function(scope) {
		return {
			sciId : "btnClose",
			text : sc.plat.bundle["b_WindowClose"]
		}
	};

	var getOKButtonConfig = function() {
		return {
			sciId : "btnOK",
			text : sc.plat.bundle["b_WindowOk"]
		}
	};

	var getSaveButtonConfig = function() {
		return {
			sciId : "btnSave",
			text : sc.plat.bundle["b_WindowSave"]
		}
	};

	var getSelectButtonConfig = function() {
		return {
			sciId : "btnSelect",
			text : sc.plat.bundle["b_WindowSelect"]
		}
	};

	var getCreateButtonConfig = function() {
		return {
			sciId : "btnCreate",
			text : sc.plat.bundle["b_WindowCreate"]
		}
	};
	
	var getContinueButtonConfig = function(){
		return {
			sciId : "btnSaveContinue",
			text : sc.plat.bundle["b_WindowSaveNCntnu"]
		}
	}
	
	var getSaveCloseButtonConfig = function(){
		return {
			sciId : "btnContinue",
			text : sc.plat.bundle["b_WindowSaveNClose"]
		}
	}
	
	var getConfirmButtonConfig = function(){
		return {
			sciId : "btnConfirm",
			text : sc.plat.bundle["b_WindowConfirm"]
		}
	}
	

	var buttons = {};

	buttons["close"] = {
		config : getCloseButtonConfig(),
		handler : closeHandler
	}

	buttons["ok"] = {
		config : getOKButtonConfig(),
		handler : okHandler
	}

	buttons["select"] = {
		config : getSelectButtonConfig(),
		handler : okHandler
	}
	
	buttons["save"] = {
		config : getSaveButtonConfig(),
		handler : okHandler
	}
	
	buttons["create"] = {
		config : getCreateButtonConfig(),
		handler : okHandler
	}
	
	buttons["savecontinue"] = {
		config : getContinueButtonConfig(),
		handler : continueHandler
	}
	
	buttons["saveclose"] = {
		config : getSaveCloseButtonConfig(),
		handler : okHandler
	}
	buttons["confirm"] = {
		config : getConfirmButtonConfig(),
		handler : okHandler
	}

	var addButtonsToWindow = function(win, opt){
		var bs = opt.buttons;
        for(var i = 0, len = bs.length; i < len; i++) {
			var bd = buttons[bs[i]];
			var scope = {win:win, opt:opt};
			win.addButton(bd.config, bd.handler, scope);
        }
	}

	return {
		OKCLOSE : ["ok", "close"],
		CLOSE : ["close"],
		SELECTCLOSE : ["select", "close"],
		SELECT : ["select"],
		SAVECLOSE : ["save", "close"],
		CREATECLOSE : ["create", "close"],
		CONTINUESAVECLOSE : ["savecontinue", "saveclose", "close"],
		CONFIRMCLOSE : ["confirm","close"],

		setButton : function() {
		},

		getButton : function() {
		},
		getWindow : function(config) {
			var msgPanel = new sc.sbc.container.MessagePanel({title : false , border : false, cls : 'sbc-error-panel'});
			if(config.setDataHandler)
				Ext.callback(config.setDataHandler, config.scope, [config.inputData]);
			else if(config.screen.setPopupData)
				Ext.callback(config.screen.setPopupData, config.scope, [config.inputData]);
/*			if(!Ext.isEmpty(config.screen.height)) {
				delete config.screen.height;
			}*/
			var cnfg = {
				items : [msgPanel, config.screen],
				title : config.screen.title,
				width : config.screen.width || 800,
				height : config.screen.height || 500,
				closable : true,
				closeAction : "hide",
				hideBorders : true,
				maximizable : false,
				layout : "anchor",
				autoHeight : true,
				modal : true
			};
			Ext.apply(cnfg, config.windowConfig);
			if(config.screen.helpRequired === true) {
				Ext.apply(cnfg, {
							tools : [{
								id : 'help',
								qtip : sc.plat.bundle['b_Help'],
								handler : function(event, toolEl, panel) {
									sc.sbc.core.OnlineHelpLauncher.loadHelpWindow(config.screen);
								},
								scope : config.screen
							}]
						});
			}
			cnfg.resizable = false;
			var win = new Ext.Window(cnfg);
			if (config.buttons) {
				addButtonsToWindow(win, config);
			}
			if (config.dblclickevent) {
				Ext.applyIf(config.screen, {opt : config, win : win});
				for (var h in config.dblclickevent) {
					if(config.dblclickevent.hasOwnProperty(h)) {
						config.screen.on(h, dblClickHandler);
						config.screen.opt.dblClickHandler = config.dblclickevent[h];
					}
				}
			}
			win.on("show", onWindowShow, config.screen);
			return win;
		},
		getButtonByType : function(screen, btnType) {
			var btn = null;
			var scrWindow = screen.ownerCt;
			if (scrWindow instanceof Ext.Window) {
				var winButtons = scrWindow.buttons;
				if(!Ext.isEmpty(winButtons)) {
					for (var i = 0; i < winButtons.length; i++) {
						if (winButtons[i].sciId === buttons[btnType].config.sciId) {
							btn = winButtons[i];
							break;
						}
					}
				}
			}
			return btn;
		}
	}
}();

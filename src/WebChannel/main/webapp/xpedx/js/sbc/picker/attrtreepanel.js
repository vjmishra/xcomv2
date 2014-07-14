Ext.namespace('sc.swc.attribute.picker');

/**
 * Config options
 * messageText - <String> if passed a message text will be displayed above the tree to explain what the user needs to do.
 * DisplayNodeType - <Array> of nodes types which are passed to the sbc tree loader todisplay the tree nodes correctly.
 * 							see the documentation for the sbc loader for more information.
 * MultiSelect - <Boolean> used to allow multiple selection in the tree. When enabled the getPopupData method will return an array
 * 							of nodes rather than a single node.
 * selectableNodes -
 * isLookup
 * showAttrValueSelection -
 * preSelectedAttribute -
 */
sc.swc.attribute.picker.AttributePicker = function(config) {
	if(!config.treeTitle){
		this.showHeader=false;
	}else{
		this.showHeader=true;
		this.treeTitle= config.treeTitle;
		this.treeScType = "ScreenTreePanel";
	}
	var loaderConfig = {"sbcTreeLoaderData":{}};
	//Set the display classification for each node type
	//loaderConfig.sbcTreeLoaderData.DisplayNodeType=config.DisplayNodeType

	//If a message needs to be displayed then display it
	if(config.messageText){
		this.messageText = config.messageText;
		//this.messageCellCls = undefined;
		this.hideLabel = false;
	}else{
		this.hideLabel = true;
		this.messageText = "";
		//this.messageCellCls = "sea-PanelForHiddenFields";
	}

	//If the tree should allow multi select then set the specific tree selection model
	if(config.MultiSelect === true){
		this.selModel = new Ext.tree.MultiSelectionModel();
	}else{
		this.selModel = new Ext.tree.DefaultSelectionModel();
	}

	//Specify which nodes should be selectable when the tree is displayed.
	this.selectableNodes = {
		"NumberAttribute":true,
		"ComputedAttribute":true,
		"ReferenceAttribute":true,
		"DerivedAttribute":true,
		"BooleanAttribute":true,
		"TextAttribute":true
	}

	//set the properties that are different when the tree is loaded in a popup vs being loaded on the main panel
	this.hideAttrValueFields=true;
	//this.attrValuePanelCls = "sea-PanelForHiddenFields";

	if(config.isLookup === true){
		this.treeHeight = config.treeHeight;
		this.treeWidth = config.treeWidth;
		loaderConfig.sbcTreeLoaderData.LookupMode = true;
		this.toolbar = undefined;
		this.enableDragAndDrop = false;
		config.showAttrValueSelection = true;
		if(config.showAttrValueSelection === true){
			this.hideAttrValueFields=false;
			//this.attrValuePanelCls = undefined;

			if(config.allowMultipleValues === undefined || config.allowMultipleValues === null){
	        	this.allowMultipleValues = true;
	        }else{
				this.allowMultipleValues = config.allowMultipleValues;
	        }
		}
		if(config.SelectableNodes){
			Ext.apply(this.selectableNodes, config.SelectableNodes);
		}
		if(config.preSelectedAttribute){
			this.preSelectedAttribute = config.preSelectedAttribute;
		}
	}else{
		this.treeHeight = 592;
		this.enableDragAndDrop = true;
		this.toolbar = [{
            xtype: "tbbutton",
            sciId: "tbarBtnMoveUp",
            handler: this.moveUpSelected,
            scope: this,
           // iconCls: "sbc-moveup-icon",
            disabled: true,
            tooltip:this.b_moveupTooltip
        },
        {
            xtype: "tbbutton",
            sciId: "tbarBtnMoveDown",
            handler: this.moveDownSelected,
            scope: this,
          //  iconCls: "sbc-movedown-icon",
            disabled: true,
            tooltip:this.b_movedownTooltip
        }];
	}

	sc.swc.attribute.picker.AttributePicker.superclass.constructor.call(this, config);
    var tree = this.find("sciId", "attrTree")[0];
    this.treeLoader.setOwnerTree(tree);
}
Ext.extend(sc.swc.attribute.picker.AttributePicker, Ext.Panel, {
    className: 'sc.swc.attribute.picker.AttributePicker',

    selectButton:null,
	retrievedAttrDetailByKeys:{},
	attrDetailPanel:null,
	currentAttrKey:"",
    onLoad:function(apiResults, opt){
    	if(this.isLookup){
    		var tree = this.find("sciId", "attrTree")[0];
    		tree.on({
    			"click":{
    				fn:this.treeNodeSelected,
    				scope:this
    			}});
    		//if we need to specify the attribute value then don't return the value on double click.
    		if(!this.showAttrValueSelection){
	    		tree.on({
	    			"dblclick":{
	    				fn:this.selectForReturn,
	    				scope: this
	    			}});
    		}
    	}
    	//if there is a pre-selected attribute then we need to get that attribute configuration so that we can populate the screen accordingly
    	if(this.preSelectedAttribute){
    		var attrInput = {"Attribute":{}};
    		attrInput.Attribute.AttributeKey = this.preSelectedAttribute.Attribute.AttributeKey;
    		attrInput.Attribute.AttributeDomainID = this.preSelectedAttribute.Attribute.AttributeDomainID;
    		attrInput.Attribute.AttributeGroupID = this.preSelectedAttribute.Attribute.AttributeGroupID;
    		attrInput.Attribute.AttributeID = this.preSelectedAttribute.Attribute.AttributeID;
    		attrInput.Attribute.OrganizationCode = this.preSelectedAttribute.Attribute.OrganizationCode;
    		attrInput.Attribute.CallingOrganizationCode = this.preSelectedAttribute.Attribute.CallingOrganizationCode;

    		//sc.sbc.util.JsonUtils.clearBlankAttributes(attrInput);
			Ext.Ajax.request({  
				url : attrPickerActions[0],
				params : {"getAttributeList_input" : ext.encode(attrInput) },
				extraParams : {callFor:"preSelected"},
				success : this.handleGetAttrConfig,
				scope : this
			});
    	}
    	this.find("sciId", "attrTree")[0].getLoader().loadData(apiResults, undefined, false, false);
    },
    selectForReturn:function(node, e){
   		this.selectButton.handler.call(this.selectButton.scope, this.selectButton);
    },
    treeNodeSelected:function(node){
    	var selModel = this.find("sciId", "attrTree")[0].getSelectionModel();
    	var selections;
    	if(selModel instanceof Ext.tree.MultiSelectionModel){
    		selections = selModel.getSelectedNodes();
    	}else{
			selections = [];
			selections.push(node);
		}

		for(var i=0 ; i<selections.length ; i++){
			var nodetype = selections[i].attributes.nodetype.Type;
	    	if(!this.selectableNodes[nodetype] || selections[i].disabled){
	    		//this.selectButton.disable();
	    		this.find("sciId", "lblAttrVal")[0].hide();
	    		this.find("sciId", "pnlAttrValue")[0].hide();
	    		this.find("sciId", "lblSelectAttrMessage")[0].show();
	    		this.find("sciId", "pnlAttrValueSelection")[0].setTitle("Specify value for - ");
	    		return;
	    	}
		}

		//if the attribute value is required then populate
		if(this.showAttrValueSelection){
			this.addSelectedValuesToCurrentAttrDetail();
			var attrKey = node.attributes.key;
			//then determine if we have already retrieved the information for this attribute
			if(this.retrievedAttrDetailByKeys[attrKey]){
				//and load it in the value selection panel at the bottom
				this.loadAttributeValueSelectionPanelForAttrKey(attrKey);
			}else{
				//otherwise call the API to get the information
				var attrInput = {"Attribute":{"AttributeKey":attrKey}};
				Ext.Ajax.request({ 
					url : attrPickerActions[0],
					params : {"getAttributeList_input" : Ext.encode(attrInput)},
					success : this.handleGetAttrConfig,
					scope : this
				});
			}
		}

//		this.selectButton.enable();
    },

    addSelectedValuesToCurrentAttrDetail:function(){
    	if(this.currentAttrKey){
	    	var existingData = this.retrievedAttrDetailByKeys[this.currentAttrKey];
	    	//var modifiedData = this.getTargetModel("targetAttribute")[0];
			var component = this.find("sciId", "attrcomponent")[0];
			var value ="";
			if(component instanceof Ext.form.ComboBox){
				value = component.getValue();
			}

			if(component instanceof Ext.form.Checkbox){
				value= component.getValue();
				if(value == true){
					value= "Y";
				}else{
					value="N";
				}
			}

			if(component instanceof Ext.form.TextField){
				value=component.getValue();
			}
			var modifiedData = {"Attribute" : {"Value" : value }};
	    	Ext.apply(existingData.Attribute, modifiedData.Attribute);
    	}
    },

    handleGetAttrConfig:function(response, opt){
    	var res = Ext.decode(response.responseText);
		var attribute = res["attribute"]["AttributeList"];

		//since we know that only one attribute will be returned here. We remove the array aspect of the json
		//to make coding easier.
		var attribute = {"Attribute":attribute.Attribute[0]};

		var attrKey = attribute.Attribute.AttributeKey;
		//if(sc.plat.DataUtils.extractData(opt, "extraParams.callFor") === "preSelected"){
		/*if(opt.extraParams.callFor === "preSelected"){
			attribute.Attribute.Value = this.preSelectedAttribute.Attribute.Value;
			this.selectButton.enable();
		}*/
		this.retrievedAttrDetailByKeys[attrKey] = attribute;

		this.loadAttributeValueSelectionPanelForAttrKey(attrKey);
    },

    attributePanelConfig:{
    	"CheckBox":{
    		bindingData:{
    			checkedValue :"Y",
				unCheckedValue :"N",
				sourceBinding : "attribute:Attribute.Value",
				targetBinding : "targetAttribute:Attribute.Value"
    		}
    	},
		"Grid":{
			bindingData: {
	            sourceBinding: "attribute:Attribute.Value",
	            targetBinding: "targetAttribute:Attribute.Value"
	        },
	        tAttrBinding: "Value"
		},
		"Combo":{
			displayField :"ShortDescription",
			valueField :"Value",
			bindingData:{
    			optionsBinding :"attribute:Attribute.AttributeAllowedValueList.AttributeAllowedValue",
				sourceBinding : "attribute:Attribute.Value",
				targetBinding : "targetAttribute:Attribute.Value"
			},
			store:new Ext.data.JsonStore({
				defid :"jsonstore",
				fields : [ "ShortDescription", "Value" ],
				sortInfo: {
                    defid: "object",
                    field: "ShortDescription",
                    direction: "ASC"
                }
			})
		},
		"TextField":{
			bindingData:{
				sourceBinding : "attribute:Attribute.Value",
				targetBinding : "targetAttribute:Attribute.Value"
			}
		},
		"ReferenceLabel":{
			text:this.b_ReferenceAttributeTypeNotSupported
		},
		"DerivedLabel":{
			text:this.b_DerivedAttributeTypeNotSupported
		},
		"ComputedLabel":{
			text:this.b_ComputedAttributeTypeNotSupported
		}
    },

    loadAttributeValueSelectionPanelForAttrKey:function(attrKey){
    	this.find("sciId", "lblAttrVal")[0].show();
		this.find("sciId", "pnlAttrValue")[0].show();
		this.find("sciId", "lblSelectAttrMessage")[0].hide();

    	if(this.currentAttrKey != attrKey){
    		var attribute = this.retrievedAttrDetailByKeys[attrKey];
    		var pnlAttrValueHolder = this.find("sciId", "pnlAttrValue")[0];

	    	var item, items = [];
	        while((item = pnlAttrValueHolder.items.last())){
	            items.unshift(pnlAttrValueHolder.remove(item, true));
	        }

	        var additionalConfig = {
	    		allowMultipleValues:this.allowMultipleValues
    		}

	    	sc.sbc.itemadmin.utils.attributes.AttributeUtils.createField(attribute, this.attributePanelConfig, pnlAttrValueHolder, additionalConfig);
			pnlAttrValueHolder.doLayout();
			this.currentAttrKey = attrKey;
			//this.setModel(attribute, "attribute");
    		//this.find("sciId", "pnlAttrValueSelection")[0].setTitle(String.format(sc.plat.ui.Screen.prototype.b_SpecifyValueForAttribute, attribute.Attribute.ShortDescription));
			this.find("sciId", "pnlAttrValueSelection")[0].setTitle("Specify value For - " + attribute.Attribute.ShortDescription );
		}

    },

    getAttrTree:function(){
    	return this.find("sciId", "attrTree")[0];
    },

    getDomainID:function(){
    	var selModel = this.getAttrTree().getSelectionModel();
    	var domainID;
    	if(selModel instanceof Ext.tree.MultiSelectionModel){
    		var selections = selModel.getSelectedNodes();
    		domainID = selection[0].attributes.json.AttributeDomainID;
    	}else{
			domainID = selModel.getSelectedNode().attributes.json.AttributeDomainID;
		}
    	return domainID;
    },

    moveDownSelected:function(){
		this.treeLoader.moveNode("DOWN");
	},
    moveUpSelected:function(){
    	this.treeLoader.moveNode("UP");
	},
	setSelectButton:function(button){
		this.selectButton = button;
		button.disable();
	},

    /**
     * the method used by the window util to return the selected node.
     * @return {}
     */
    getPopupData:function(){
    	var selModel = this.getAttrTree().getSelectionModel();
    	var returnVal;
    	if(this.showAttrValueSelection){
    		this.addSelectedValuesToCurrentAttrDetail();
    		returnVal = this.retrievedAttrDetailByKeys[this.currentAttrKey];
    	}else if(selModel instanceof Ext.tree.MultiSelectionModel){
    		returnVal = [];
    		var nodes = selModel.getSelectedNodes();
    		for(var i=0; i <nodes.length; i++){
    			returnVal.push(nodes[i].attributes.json);
    		}
    	}else{
			returnVal = [];
			var selection = selModel.getSelectedNode();
			returnVal.push(selection.attributes.json);
		}

		this.appendOptionLast(returnVal);
		this.win.close();
    	return returnVal;
    },

	appendOptionLast : function (returnVal)
		{
		  var elOptNew = document.createElement('option');
		  elOptNew.text = returnVal.Attribute.ShortDescription + ":" + returnVal.Attribute.Value;
		  elOptNew.value =returnVal.Attribute.AttributeID + ":" + returnVal.Attribute.Value;

		  var elSel = document.getElementById(this.selectFieldId);


		  try {
		  	elSel.add(elOptNew); // IE only
		  }
		  catch(ex) {

		    elSel.add(elOptNew, null); // standards compliant; doesn't work in IE
		  }
		},
		setPickerWin : function(win){
			this.win = win;
		}

});
Ext.reg('attrtreepanel', sc.swc.attribute.picker.AttributePicker);
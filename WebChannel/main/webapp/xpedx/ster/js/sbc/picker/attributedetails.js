Ext.namespace('sc.sbc.itemadmin.attribute.manage');

sc.sbc.itemadmin.attribute.manage.attributedetails = function(config) {
    sc.sbc.itemadmin.attribute.manage.attributedetails.superclass.constructor.call(this, config);
}
Ext.extend(sc.sbc.itemadmin.attribute.manage.attributedetails, sc.plat.ui.ExtensibleScreen, {
    className: 'sc.sbc.itemadmin.attribute.manage.attributedetails',
    getUIConfig: sc.sbc.itemadmin.attribute.manage.attributedetailsUIConfig,
    numberSaveFormat: "INTEGER",
    radioSelection: "VALIDVALUES",
    attributeTypeInDB:"",
    attrTree:null,
    namespaces: {
    	source:["attribute","attributeFormat", "postFixList", "referenceAttributes", "aliasList"],
        target:["targetAttribute", "targetReferenceAttribute", "targetDerivedFromAttribute"]
    },
    setAttrTree:function(tree){
    	this.attrTree = tree;
    },
    attrTypeRadBtnSelected : function(radBtn, value){
    	if(value){
    		if(radBtn.sciId == "radValidValues"){
		    	this.radioSelection = "VALIDVALUES";
	    	}else if(radBtn.sciId == "radReferenceAttr"){
		    	this.radioSelection = "REFERENCE";
		    }else if(radBtn.sciId == "radDerivedAttr"){
		    	this.radioSelection = "DERIVED";
		    }else if(radBtn.sciId == "radComputedAttr"){
		    	this.radioSelection = "COMPUTED";
		    }
		    this.find("sciId", "txtAttrID")[0].validate();
	    	this.showAttributeTypeFieldFor();
    	}
    },
    referenceTypeRadioSelected:function(radBtn, value){
    	var typeSpecFieldsContainer = this.find("sciId", "pnlTypeSpecificFields")[0];
		if(value && typeSpecFieldsContainer.getLayout().activeItem.sciId === "pnlReferenceField"){
    		var pnlReferenceLabelHolder = this.find("sciId", "pnlReferenceLabelHolder")[0];
			var pnlReferenceFieldHolder = this.find("sciId", "pnlReferenceFieldHolder")[0];
			if(radBtn.sciId == "radItemReference"){
				var labelIndex = sc.sbc.util.CoreUtils.getCardLayoutIndexBySciId(pnlReferenceLabelHolder, "lblItemReference");
				this.find("sciId", "cmbReferenceAttribute")[0].enable();
				this.find("sciId", "cmbReferenceAlias")[0].disable();
				pnlReferenceLabelHolder.getLayout().setActiveItem(labelIndex);
				var fieldIndex = sc.sbc.util.CoreUtils.getCardLayoutIndexBySciId(pnlReferenceFieldHolder, "cmbReferenceAttribute");
				pnlReferenceFieldHolder.getLayout().setActiveItem(fieldIndex);
				
			}else{
				var labelIndex = sc.sbc.util.CoreUtils.getCardLayoutIndexBySciId(pnlReferenceLabelHolder, "lblAliasReference");
				this.find("sciId", "cmbReferenceAttribute")[0].disable();
				this.find("sciId", "cmbReferenceAlias")[0].enable();
				pnlReferenceLabelHolder.getLayout().setActiveItem(labelIndex);
				var fieldIndex = sc.sbc.util.CoreUtils.getCardLayoutIndexBySciId(pnlReferenceFieldHolder, "cmbReferenceAlias");
				pnlReferenceFieldHolder.getLayout().setActiveItem(fieldIndex);
				
			}
    	}
    },
    
    initScreen: function(apiResults){
		apiResults = Ext.decode(apiResults.responseText);
		this.setModel(this.prepareEntityAttributeList(apiResults.entityAttributeList), "referenceAttributes");
		this.setModel(apiResults.itemAliasList, "aliasList");
		this.setModel(apiResults.postFixList, "postFixList", {clearOldVals: true});

		var attrKey = sc.plat.DataUtils.extractData(apiResults, "attribute.Attribute.AttributeKey");
		if(attrKey){
			this.find("sciId", "txtAttrID")[0].hide();
			this.find("sciId", "lblAttrIdValue")[0].show();
			this.find("sciId", "lblAttrID")[0].getEl().removeClass("sc-mandatory");
			sc.sbc.util.CoreUtils.getBottomToolBarItemBySciId(this.find("sciId", "pnlDetails")[0], "bbarBtnDelete").enable();
			
			var attributeID = apiResults.attribute.Attribute.AttributeID;
    		this.find("sciId", "pnlDetails")[0].setTitle(String.format(this.b_AttributeDetails, attributeID));
		}else{
			this.find("sciId", "txtAttrID")[0].show();
			this.find("sciId", "lblAttrIdValue")[0].hide();
			this.find("sciId", "lblAttrID")[0].getEl().addClass("sc-mandatory");
			sc.sbc.util.CoreUtils.getBottomToolBarItemBySciId(this.find("sciId", "pnlDetails")[0], "bbarBtnDelete").disable();
			
			this.find("sciId", "pnlDetails")[0].setTitle(this.b_CreateAttribute);
		}
		this.disableNonSelectableAttributeTypes(apiResults.attribute);
		
		if(apiResults.attribute.Attribute.DataType === "DECIMAL"){
			this.numberSaveFormat = "DECIMAL";
		}else{
			this.numberSaveFormat = "INTEGER";
		}
		
		var formatModel={
	    	Formats:{
	    		Format:[
	    			{
	    				name: this.b_BooleanAttribute,
	    				value:'BOOLEAN'
	    			},{
	    				name: this.b_NumberAttribute,
	    				value:this.numberSaveFormat
	    			},{
	    				name: this.b_TextAttribute,
	    				value:'TEXT'
	    			}
	    			
	    		]
	    	}
    	}
		this.setModel(formatModel, "attributeFormat");
		
		//To ensure that two radio buttons are not selected we first make them all unchecked.
		this.find("sciId", "radValidValues")[0].setValue(false);
		this.find("sciId", "radReferenceAttr")[0].setValue(false);
		this.find("sciId", "radDerivedAttr")[0].setValue(false);
		this.find("sciId", "radComputedAttr")[0].setValue(false);
		
		//Then select the appropriate radio button
		this.attributeTypeInDB = sc.sbc.itemadmin.utils.attributes.AttributeUtils.getAttributeType(apiResults.attribute);
		var attributeID = apiResults.attribute.Attribute.AttributeID;
		if(!attributeID){
			attributeID = "";
		}
		if(this.attributeTypeInDB === "COMPUTED"){
			this.find("sciId", "radComputedAttr")[0].setValue(true);
		}else if(this.attributeTypeInDB === "REFERENCE"){
			this.find("sciId", "radReferenceAttr")[0].setValue(true);
		}else if(this.attributeTypeInDB === "DERIVED"){
			this.find("sciId", "radDerivedAttr")[0].setValue(true);
		}else{
			this.find("sciId", "radValidValues")[0].setValue(true);
		}
		
		var pnlReferenceLabelHolder = this.find("sciId", "pnlReferenceLabelHolder")[0];
		var pnlReferenceFieldHolder = this.find("sciId", "pnlReferenceFieldHolder")[0];
		if(this.attributeTypeInDB==="REFERENCE"){
			var tableName = apiResults.attribute.Attribute.AttributeReference.TableName;
			var value = apiResults.attribute.Attribute.AttributeReference.ReferenceName;
			if(tableName == "YFS_ITEM_ALIAS"){
				this.referenceTypeRadioSelected(this.find("sciId", "radAliasReference")[0], true);
				apiResults.attribute.Attribute.AttributeReference.AliasReferenceName=value;
			}else{
				this.referenceTypeRadioSelected(this.find("sciId", "radItemReference")[0], true);
				apiResults.attribute.Attribute.AttributeReference.ItemReferenceName=value;
			}
			delete apiResults.attribute.Attribute.AttributeReference.ReferenceName;
		}else{
			apiResults.attribute.Attribute.AttributeReference ={"TableName":"YFS_ITEM"};
		}

		this.setModel(apiResults.attribute, "attribute");
		sc.sbc.itemadmin.utils.attributes.AttributeUtils.updateAttributeRelatedTasks(this.getAttributeKey(), this.radioSelection, this.attributeTypeInDB);
    },
	relatedTaskCompletionHandler:function(){
		sc.sbc.itemadmin.utils.attributes.AttributeUtils.updateAttributeRelatedTasks(this.getAttributeKey(), this.radioSelection, this.attributeTypeInDB);
	},
	disableNonSelectableAttributeTypes:function(attribute){
		var id = sc.plat.DataUtils.extractData(attribute, "Attribute.AttributeID");
		var refAttr = sc.plat.DataUtils.extractData(attribute, "Attribute.AttributeReference.ReferenceName");
		if(!id){
			this.find("sciId", "radReferenceAttr")[0].enable();
			this.find("sciId", "radDerivedAttr")[0].enable();
			this.find("sciId", "radValidValues")[0].enable();
			this.find("sciId", "radComputedAttr")[0].enable();
		}else if(id.search("Price_") == 0){
			this.find("sciId", "radReferenceAttr")[0].disable();
			this.find("sciId", "radDerivedAttr")[0].disable();
			this.find("sciId", "radValidValues")[0].disable();
			this.find("sciId", "radComputedAttr")[0].enable();
		}else if(refAttr){
			this.find("sciId", "radReferenceAttr")[0].enable();
			this.find("sciId", "radDerivedAttr")[0].disable();
			this.find("sciId", "radValidValues")[0].disable();
			this.find("sciId", "radComputedAttr")[0].disable();
		}else{
			this.find("sciId", "radReferenceAttr")[0].enable();
			this.find("sciId", "radDerivedAttr")[0].enable();
			this.find("sciId", "radValidValues")[0].enable();
			this.find("sciId", "radComputedAttr")[0].disable();
		}
	},
    //we need to get the display names for all the attributes and merge the extended attributes so there is only one list
	prepareEntityAttributeList: function(entityAttributeIn){
		var attrElem = sc.plat.DataUtils.extractData(entityAttributeIn, "Entity.Attributes.Attribute");
		var extnAttrElem = sc.plat.DataUtils.extractData(entityAttributeIn, "Entity.ExtnAttributes.Attribute");
		if(extnAttrElem){
			for(var i=0 ; i<extnAttrElem.length ; i++){
				var extnAttr = extnAttrElem[i];
				extnAttr.IsExtnAttr='Y';
				attrElem.push(extnAttr);
			}
		}
		
		if(attrElem){
			for(var i=0 ; i<attrElem.length ; i++){
				var attr = attrElem[i];
				var displayVal = sc.plat.ui.Screen.prototype[attr.XMLName];
				if(!displayVal){
					displayVal = attr.XMLName;
				}
				attr.DisplayName = displayVal;
			}
		}
		
		return entityAttributeIn;
	},
    
	showAttributeTypeFieldFor: function(){
		var typeSpecFieldsContainer = this.find("sciId", "pnlTypeSpecificFields")[0];
		if(this.radioSelection == "VALIDVALUES"){
			var index = sc.sbc.util.CoreUtils.getCardLayoutIndexBySciId(typeSpecFieldsContainer, "pnlAttributeFormat");
			this.find("sciId", "cmbAttrFormat")[0].enable();
			typeSpecFieldsContainer.getLayout().setActiveItem(index);
		}else{
			this.find("sciId", "cmbAttrFormat")[0].disable();
		}
		
		if(this.radioSelection == "REFERENCE"){
			var index = sc.sbc.util.CoreUtils.getCardLayoutIndexBySciId(typeSpecFieldsContainer, "pnlReferenceField");
			if(this.find("sciId", "radItemReference")[0].getValue() === true){
				this.find("sciId", "cmbReferenceAttribute")[0].enable();
			}else{
				this.find("sciId", "cmbReferenceAlias")[0].enable();
			}
			typeSpecFieldsContainer.getLayout().setActiveItem(index);
		}else{
			this.find("sciId", "cmbReferenceAttribute")[0].disable();
			this.find("sciId", "cmbReferenceAlias")[0].disable();
		}
		
		if(this.radioSelection == "DERIVED"){
			var index = sc.sbc.util.CoreUtils.getCardLayoutIndexBySciId(typeSpecFieldsContainer, "pnlDerivedFrom");
			this.find("sciId", "txtDerivedFrom")[0].enable();
			typeSpecFieldsContainer.getLayout().setActiveItem(index);
		}else{
			this.find("sciId", "txtDerivedFrom")[0].disable();
		}
		
		if(this.radioSelection == "COMPUTED"){
			var index = sc.sbc.util.CoreUtils.getCardLayoutIndexBySciId(typeSpecFieldsContainer, "pnlComputed");
			typeSpecFieldsContainer.getLayout().setActiveItem(index);
		}
		
		sc.sbc.itemadmin.utils.attributes.AttributeUtils.updateAttributeRelatedTasks(this.getAttributeKey(), this.radioSelection, this.attributeTypeInDB);
		this.find("sciId", "pnlTypeSpecificFields")[0].doLayout();
	},
	
	saveButtonSelected:function(){
		var attrInput = null;
		//Get the correct values from the fields based on the selected attribute type
		if(this.radioSelection == "VALIDVALUES"){
	    	attrInput = this.getTargetModel("targetAttribute")[0];
	    }else if(this.radioSelection == "REFERENCE"){
	    	attrInput = this.getTargetModel("targetReferenceAttribute")[0];
	    	var value = "";
	    	if(this.find("sciId", "radItemReference")[0].getValue()){
	    		value = attrInput.AttributeList.Attribute.AttributeReference.ItemReferenceName;
	    	}else{
	    		value = attrInput.AttributeList.Attribute.AttributeReference.AliasReferenceName;
	    	}
	    	attrInput.AttributeList.Attribute.AttributeReference.ReferenceName = value;
	    	delete attrInput.AttributeList.Attribute.AttributeReference.ItemReferenceName;
	    	delete attrInput.AttributeList.Attribute.AttributeReference.AliasReferenceName;
	    }else if(this.radioSelection == "DERIVED"){
	    	attrInput = this.getTargetModel("targetDerivedFromAttribute")[0];
	    	attrInput.AttributeList.Attribute.DataType=this.find("sciId", "cmbAttrFormat")[0].getValue();
	    }else if(this.radioSelection == "COMPUTED"){
	    	attrInput = this.getTargetModel("targetDerivedFromAttribute")[0];
	    	attrInput.AttributeList.Attribute.DataType="DECIMAL";
	    }

	    //I the attribute type has changed then we need to clear the allowed values.
	    if(this.attributeTypeInDB === "REFERENCE"){
	    	if(this.radioSelection != "REFERENCE"){
	    		var attrRefKey = this.find("sciId", "hidAttrRefKey")[0].getValue();
				attrInput.AttributeList.Attribute.AttributeReference = {
	    			AttributeReferenceKey:attrRefKey,
	    			Operation:"Delete"
	    		}
	    	}
    	}else if(this.attributeTypeInDB === "DERIVED"){
    		if(this.radioSelection != "DERIVED"){
    			attrInput.AttributeList.Attribute.AttributeAllowedValueList = {"Reset":"Y"};
    			//when an attribute is created or changed to a number attribute the number type should be Integer
    			//even if the datatype when the attribute was a derived attribute was decimal.
    			if(this.find("sciId", "cmbAttrFormat")[0].getValue() == "DECIMAL"){
    				attrInput.AttributeList.Attribute.DataType = "INTEGER";
    			}
    			attrInput.AttributeList.Attribute.DerivedFromAttributeKey=" ";
    		}
    	}else if(this.attributeTypeInDB === "COMPUTED"){
    		//This should never happen because we disable the radio buttons.
    	}else if((this.radioSelection != "VALIDVALUES") || (this.attributeTypeInDB != this.find("sciId", "cmbAttrFormat")[0].getValue())){
    		//Since the attribute in the DB isn't Derived, Reference, or Computed 
    		//in addition to ensuring that the type hasn't change we need to ensure that the data type hasn't change
    		attrInput.AttributeList.Attribute.AttributeAllowedValueList = {"Reset":"Y"};
    	}
		
		//if we are creating an attribute then we need to pass the Operation as Create to ensure that we don't overwrite an existing attribute
		//with the same ID
		if(!this.find("sciId", "hidAttributeKey")[0].getValue()){
			attrInput.AttributeList.Attribute.Operation = "Create";
		}
		
        seaAjaxUtils.request({
			actionNS : sc.sbc.App.ItemStrutsNS,
			action : "attributedetails-manageattribute",
			inputNS : "manageAttribute_input",
			inputObj : {"AttributeList":attrInput.AttributeList},
			extraParams:{"saveData":attrInput},
			success : this.resHandlerForSave,
			scope : this,
			successMsg : this.b_SaveSuccessfull
		});
	}, 
	
	resHandlerForSave:function(apiResults, opt){
		apiResults = Ext.decode(apiResults.responseText);
		var attrKey = this.find("sciId", "hidAttributeKey")[0];
		var newAttr = false;
		if(attrKey.getValue() == ""){
			newAttr = true;
			var idDisplayLabel = this.find("sciId","lblAttrIdValue")[0];
			var idTxtField = this.find("sciId","txtAttrID")[0];
			idDisplayLabel.setText(idTxtField.getValue());
			
			idTxtField.hide();
			idDisplayLabel.show();
			this.find("sciId", "lblAttrID")[0].getEl().removeClass("sc-mandatory");
			sc.sbc.util.CoreUtils.getBottomToolBarItemBySciId(this.find("sciId", "pnlDetails")[0], "bbarBtnDelete").enable();
			var keyValue = sc.plat.DataUtils.extractData(apiResults, "attribute.AttributeList.Attribute[0].AttributeKey");
			attrKey.setValue(keyValue);
			
			var newAttr = sc.plat.DataUtils.extractData(opt, "extraParams.saveData.AttributeList.Attribute");
			if(newAttr.DerivedFromAttributeKey){
				newAttr.DerivedFromAttribute = {"AttributeKey":newAttr.DerivedFromAttributeKey};
			}
			
			var newNode = {"Attribute":[]};
			newNode.Attribute.push(newAttr);
			newNode.Attribute[0].AttributeKey=keyValue;
			delete newNode.Attribute[0].Operation;
			
			if(this.attrTree){
				var selNode = this.attrTree.selModel.getSelectedNode();
				this.attrTree.getLoader().loadData(newNode , selNode, true)
			}
			
			this.disableNonSelectableAttributeTypes(opt.extraParams.saveData.AttributeList);
			
			var attributeID = idTxtField.getValue();
    		this.find("sciId", "pnlDetails")[0].setTitle(String.format(this.b_AttributeDetails, attributeID));
		}else{
			if(this.attrTree){
				this.attrTree.getLoader().refreshData(opt.extraParams.saveData.AttributeList);
			}
		}
		
		if(this.radioSelection === "VALIDVALUES"){
			this.attributeTypeInDB = this.find("sciId", "cmbAttrFormat")[0].getValue();
		}else{
			this.attributeTypeInDB = this.radioSelection;
		}
		
		sc.sbc.itemadmin.utils.attributes.AttributeUtils.updateAttributeRelatedTasks(this.getAttributeKey(), this.radioSelection, this.attributeTypeInDB);
		
		this.clean();
		
		//if this is a new attribute then we want to open the corresponding detail screen
		if(newAttr){
			var action = sc.sbc.common.relatedtask.RelatedTaskActionMgr.getAction("sbcattributeproperties");
			action.execute(this);
		}
	},
	
	openPostFixPopup:function(){
    	sc.sbc.util.ScreenPopupUtils.launchPopup({
			entity : sc.sbc.util.ScreenPopupUtils.CommonCodeManagement,
			scope : this,
			popupParams : {
				commonCodeType: "ATTRIBUTE_POST_FIX",
			    title: this.b_Manage_Post_Fixes,
			    saveMethod: this.saveCommonCodeSelected,
			    closeMethod: this.closeCommonCodeSelected,
			    parentscrn: this
			},
			callback : function(win, winConfig) {
				winConfig.handlers={"save" : winConfig.screen.saveSelected,
							"close" : winConfig.screen.closeSelected
				},
				winConfig.scope = winConfig.screen;
				winConfig.screen.setPopupWindow(win);
				win.show();
			},
			popupConfig : {
		    	buttons : sc.sbc.util.WindowUtils.SAVECLOSE,
		    	scope : this
		    },
		    cacheable : false
		});
    },
    
    saveCommonCodeSelected:function(modifiedList){
   		this.parentscrn.reloadpostfix(modifiedList);
   	},
   	
   	closeCommonCodeSelected:function(){
   		seaAjaxUtils.request({
			actionNS : sc.sbc.App.ItemStrutsNS,
			action : "attributedetails-reloadpostfix",
			inputNS : "getAttrPostFix_input",
			inputObj : {"CommonCode":{}},
			success : this.parentscrn.handleReload,
			scope : this
		});
   	},
   	
   	handleReload:function(apiResults, opt){
   		apiResults = Ext.decode(apiResults.responseText);
    	this.parentscrn.reloadpostfix(apiResults.postFixList);
   	},
   	
   	reloadpostfix:function(newList){
   		this.setModel(newList, "postFixList", {clearOldVals: true});
   		//now we set the value of the combo to make sure the display value for the selected record is updated as well.
   		var postFixCombo = this.find("sciId", "cmbPostFix")[0];
   		var postFixValue = postFixCombo.getValue();
   		postFixCombo.setValue("");
   		postFixCombo.setValue(postFixValue);
   	},
   	
   	OpenAttrLookup:function(){
		sc.sbc.util.ScreenPopupUtils.launchPopup({
			entity : sc.sbc.util.ScreenPopupUtils.AttributeLookup,
			scope : this.parentscrn,
			popupParams : {
				isLookup : true,
				DisplayNodeType : [
					{"DisplayMode":"INCLUDE", "NodeType":"NumberAttribute"},
					{"DisplayMode":"INCLUDE", "NodeType":"AttributeDomain"},
					{"DisplayMode":"INCLUDE", "NodeType":"AttributeGroup"},
					{"DisplayMode":"INCLUDE", "NodeType":"ComputedAttribute"},
					{"DisplayMode":"DISABLE", "NodeType":"ReferenceAttribute"},
					{"DisplayMode":"DISABLE", "NodeType":"DerivedAttribute"},
					{"DisplayMode":"DISABLE", "NodeType":"BooleanAttribute"},
					{"DisplayMode":"DISABLE", "NodeType":"TextAttribute"}
				],
				SelectableNodes:{
					"NumberAttribute":true,
					"ComputedAttribute":true,
					"ReferenceAttribute":false,
					"DerivedAttribute":false,
					"BooleanAttribute":false,
					"TextAttribute":false
				},
				messageText:this.parentscrn.b_MustSelectNumberAttribute
			},
			callback : function(win, winConfig) {
				var buttons = win.buttons;
				for(var i=0 ; i<buttons.length ; i++){
					if(buttons[i].sciId === "btnSelect"){
						winConfig.screen.setSelectButton(buttons[i]);
						break;
					}
				}
				win.show();
			},
			popupConfig : {
		    	buttons : sc.sbc.util.WindowUtils.SELECTCLOSE,
				handlers : {"select" : this.parentscrn.handleAttributePopupSelection},
				scope : this.parentscrn
		    },
		    cacheable : false
		});
	},
	
	handleAttributePopupSelection : function(selectedAttribute) {
		var attribute = selectedAttribute[0];
		var selectedAttrKey = attribute.AttributeKey;
		if(selectedAttrKey === this.find("sciId", "hidAttributeKey")[0].getValue()){
			sc.plat.RequestUtils.handleErrors({em:this.b_AttributeCannotDeriveFromItself});
			return false;
		}else{
			this.find("sciId", "hidDerFrmAttrKey")[0].setValue(selectedAttrKey);
			this.find("sciId", "cmbAttrFormat")[0].setValue(attribute.DataType);
			this.find("sciId", "txtDerivedFrom")[0].setValue(attribute.ShortDescription);
			return true;
		}
	},
	
	handleDeleteAttribute:function(){
		this.clean();
		this.attrTree.getLoader().deleteNode(this.getAttributeKey());
	},
	
	deleteButtonSelected:function(){
		sc.sbc.ui.messages.showConfirmDelete(function(buttonId){
			if(buttonId === 'yes'){
				var groupInput = this.getTargetModel("targetAttribute")[0];
				groupInput.AttributeList.Attribute.Operation = "Delete";
		        seaAjaxUtils.request({
					actionNS : sc.sbc.App.ItemStrutsNS,
					action : "attributedetails-deleteattribute",
					inputNS : "manageAttribute_input",
					inputObj : {"AttributeList":groupInput.AttributeList},
					success : this.handleDeleteAttribute,
					scope : this,
					successMsg : this.b_DeleteSuccessfull
				});
			}
		},this)
	},
	getDataType:function(){
		return this.attributeTypeInDB;
	},
	
	validateAttrID:function(value){
		var startsWithPrice = value.search("Price_");
		if(this.parentscrn.radioSelection == "COMPUTED"){
			if(startsWithPrice == 0){
				return true;
			}else{
				return this.parentscrn.b_ComputedAttrIDFormatError;
			}
		}else{
			if(startsWithPrice == 0){
				return this.parentscrn.b_NotComputedAttrIDFormatError;
			}
		}
		return true;
	},
	getAttributeKey:function(){
		return this.find("sciId", "hidAttributeKey")[0].getValue();
	}
});
Ext.reg('attributedetails', sc.sbc.itemadmin.attribute.manage.attributedetails);

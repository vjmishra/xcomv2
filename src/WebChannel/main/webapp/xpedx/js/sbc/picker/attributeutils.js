Ext.namespace("sc.sbc.itemadmin.utils.attributes");

sc.sbc.itemadmin.utils.attributes.AttributeUtils = function(){


	createCheckBoxField = function(configOptions,holderPanel) {

		var CheckBox = new Ext.form.Checkbox({
			xtype :"checkbox",
			sciId :"attrcomponent",
			colspan :1,
			ctCls :"sc-plat-checkboxlabel"
		});
		//Ext.apply(config, configOptions);
		holderPanel.add(CheckBox);
		holderPanel.doLayout();
		CheckBox.getValue();
	};

	createTextField = function(configOptions,holderPanel) {

		var textField = new Ext.form.TextField({
			xtype :"textfield",
			sciId :"attrcomponent",
			colspan :1,
			width : 150,
			cls :"sc-plat-editable-text"
		});

		holderPanel.add(textField);
		holderPanel.doLayout();

	};

	createIntegerField = function(configOptions,holderPanel) {

		var config= {
			xtype :"numberfield",
			sciId :"attrcomponent",
			colspan :1,
			width : 150,
			allowDecimals : false,
			cls :"sc-plat-editable-text",
			scuiDataType : "IntegerField"
		};

		Ext.apply(config, configOptions);
		holderPanel.add(config);
	};

	createDecimalField = function(configOptions,holderPanel) {

		var config= {
			xtype :"numberfield",
			sciId :"attrcomponent",
			colspan :1,
			width : 150,
			allowDecimals : true,
			cls :"sc-plat-editable-text",
			scuiDataType : "DoubleValue"
		};

		Ext.apply(config, configOptions);
		holderPanel.add(config);
	};

	createTextAreaField  = function(configOptions,holderPanel) {
		var config = {
			xtype :"textarea",
			sciId :"attrcomponent",
			colspan :1,
			width : 150,
			height : 75,
			autoScroll : true,
			cls :"sc-plat-editable-text"
		};

		Ext.apply(config, configOptions);
		holderPanel.add(config);
	};

	createComboField = function(configOptions,holderPanel, attr) {
		var store = new Ext.data.JsonStore( {
				defid :"jsonstore",
				fields : [ "ShortDescription", "Value" ],
				sortInfo: {
                    defid: "object",
                    field: "Value",
                    direction: "ASC"
                }
			});
		var combo = new Ext.form.ComboBox({
			displayField :"ShortDescription",
			width : 150,
			valueField :"Value",
			xtype :"combo",
			 mode : 'local',
			sciId :"attrcomponent",
			readOnly : true,
			triggerAction :"all",
			store : store,
			resizable:true
		});
		holderPanel.add(combo);
		holderPanel.doLayout();
		if(attr.Attribute.ComputedAttributeAllowedValueList && attr.Attribute.ComputedAttributeAllowedValueList.ComputedAttributeAllowedValue) 
			store.loadData(attr.Attribute.ComputedAttributeAllowedValueList.ComputedAttributeAllowedValue);
        else if(attr.Attribute.AttributeAllowedValueList.AttributeAllowedValue) 
			store.loadData(attr.Attribute.AttributeAllowedValueList.AttributeAllowedValue);
		  
		// combo.selectByValue(attr.Attribute.Value);

	};

	creatGridField  = function(configOptions,holderPanel) {

		var store = new Ext.data.JsonStore( {
				defid :"jsonstore",
				fields : [ "Value" ],
				sortInfo: {
                    defid: "object",
                    field: "Value",
                    direction: "ASC"
                }
			});
			var grid = new Ext.grid.GridPanel({
	            sciId: "attrcomponent",
	            header : false,
	            hideHeaders : true,
	            height : 80,
	            width : 150,
	            stripeRows : true,
	            columns: [{
	                defid: "grid-column",
	                sciId: "seaAssignedValue",
	                header: false,
	                sortable: true,
	                width : 130,
	                dataIndex: "Value"
	            }],
	            tbar: ["->",{
	                xtype: "label",
	                sciId: "seaModifyLink",
	                text: "Modify"
	              /*  handler: configOptions.handler,
	                scope: configOptions.scope*/
	            }
	            ],
	            border:false,
	            stripeRows:true,
	            viewConfig:{forceFit:true},
				store : store
	        });

		holderPanel.add(grid);
		holderPanel.doLayout();

		//store.loadData(attr.Attribute.Value);
	};

	createLabelField = function(configOptions,holderPanel) {
		var config = {
            xtype: "label",
            sciId: "seaReferenceLabel",
            cls: "sc-plat-noneditable-text ",
            ctCls: "sea-align-top"
        };

        Ext.apply(config, configOptions);
		holderPanel.add(config);
	};

	/**
	 * additonalConfig options:
	 * 	- allowMultipleValues : When passed as false if the attribute allowed multiple values a grid will not be displayed
	 * 							instead we will display a single textfield or combo based on the whether allowed values are defined
	 * 							When passed as true or if left blank a grid will be displayed allowing the user to specify multiple values.
	 *  - allowEntryOfAdvAttrTypes : When passed as true, advanced attribute type values can be specified
	 *  							 Reference and Computed attributes will be displayed as a text field
	 *  							 Derived attributes will be displayed a combo with valid values of the ranges
	 *  							 When passed as false or when left blank a message text passed in the config options will be displayed.
	 */
	return{
		createField : function(input,configOptions,holderPanel, additionalConfig){
			var attributeElem = input.Attribute;
			sCreateField = attributeElem.CreateField;
			var sDataType = attributeElem.DataType;

			if(additionalConfig){
				if( sCreateField=="Grid"){
					var popupType = attributeElem.ModifyPopUP;
					if(popupType === "COMBO"){
						sCreateField = "Combo";
					}else if(popupType === "TEXT"){
						sCreateField = "TextBox";
					}
				}
				if(additionalConfig.allowEntryOfAdvAttrTypes === true){
					if(sCreateField === "ReferenceLabel" || sCreateField === "ComputedLabel"){
						sCreateField = "TextBox";
					}else if(sCreateField === "DerivedLabel"){
						sCreateField = "Combo";
					}
				}
			}

			if(sCreateField=="CheckBox"){
				createCheckBoxField(configOptions.CheckBox,holderPanel);
			}
			//else if(sCreateField=="Grid"){
			//	creatGridField(configOptions.Grid,holderPanel);
			//}
			else if(sCreateField=="Combo" || sCreateField=="Grid" ){
				createComboField(configOptions.Combo,holderPanel,input);

			}else if(sCreateField=="TextBox"){
				if(sDataType=="INTEGER"){
					createIntegerField(configOptions.TextField,holderPanel);
				}else if(sDataType=="DECIMAL"){
					createDecimalField(configOptions.TextField,holderPanel);
				}else{
				createTextField(configOptions.TextField,holderPanel);
				}
			}else if(sCreateField=="TextArea"){
				if(sDataType=="INTEGER"){
					createIntegerField(configOptions.TextField,holderPanel);
				}else if(sDataType=="DECIMAL"){
					createDecimalField(configOptions.TextField,holderPanel);
				}else{
				createTextAreaField(configOptions.TextField,holderPanel);
				}
			}else if(sCreateField=="ReferenceLabel"){
				createLabelField(configOptions.ReferenceLabel,holderPanel);
			}else if(sCreateField=="DerivedLabel"){
				createLabelField(configOptions.DerivedLabel,holderPanel);
			}else if(sCreateField=="ComputedLabel"){
				createLabelField(configOptions.ComputedLabel,holderPanel);
			}
		},

		getAttributeType:function(attribute){
			var attributeID = attribute.Attribute.AttributeID;
			if(!attributeID){
				attributeID = "";
			}
			if(attributeID.search("Price_") == 0){
				return "COMPUTED";
			}else if(attribute.Attribute.AttributeReference != null && attribute.Attribute.AttributeReference.ReferenceName != null){
				return "REFERENCE";
			}else if(attribute.Attribute.DerivedFromAttribute != null && attribute.Attribute.DerivedFromAttribute.AttributeKey != null){
				return "DERIVED";
			}else{
				return attribute.Attribute.DataType;
			}
		},

		updateAttributeRelatedTasks:function(attrKey, type, attributeTypeInDB){
			if(!attrKey){
				sc.sbc.helper.RelatedTaskHelper.disableTask("SBCITM00013"); //properties
				sc.sbc.helper.RelatedTaskHelper.disableTask("SBCITM00011"); //assets
				sc.sbc.helper.RelatedTaskHelper.disableTask("SBCITM00010"); //derivedattrlist
				sc.sbc.helper.RelatedTaskHelper.disableTask("SBCITM00019"); //Localize
			}else{
				if(type == "VALIDVALUES" || type == "INTEGER" || type == "DECIMAL"
				   || type == "BOOLEAN" || type == "TEXT"){
					sc.sbc.helper.RelatedTaskHelper.enableTask("SBCITM00013");//properties
					sc.sbc.helper.RelatedTaskHelper.enableTask("SBCITM00011");//assets
					if(attributeTypeInDB === "INTEGER" || attributeTypeInDB === "DECIMAL"){
						sc.sbc.helper.RelatedTaskHelper.enableTask("SBCITM00010");//derivedattrlist
					}else{
						sc.sbc.helper.RelatedTaskHelper.disableTask("SBCITM00010");//derivedattrlist
					}
				}else if(type == "REFERENCE"){
					sc.sbc.helper.RelatedTaskHelper.disableTask("SBCITM00013"); //properties
					sc.sbc.helper.RelatedTaskHelper.enableTask("SBCITM00011"); //assets
					sc.sbc.helper.RelatedTaskHelper.disableTask("SBCITM00010"); //derivedattrlist
				}else if(type == "DERIVED"){
					sc.sbc.helper.RelatedTaskHelper.enableTask("SBCITM00013"); //properties
					sc.sbc.helper.RelatedTaskHelper.disableTask("SBCITM00011"); //assets
					sc.sbc.helper.RelatedTaskHelper.disableTask("SBCITM00010"); //derivedattrlist
				}else if(type == "COMPUTED"){
					sc.sbc.helper.RelatedTaskHelper.disableTask("SBCITM00013"); //properties
					sc.sbc.helper.RelatedTaskHelper.disableTask("SBCITM00011"); //assets
					sc.sbc.helper.RelatedTaskHelper.enableTask("SBCITM00010"); //derivedattrlist
				}
				sc.sbc.helper.RelatedTaskHelper.enableTask("SBCITM00019"); //Localize
			}
		},

		deLocalizeNumberAttributeValue:function(value){
			if(value && Ext.type(value) === "string"){
				value = value.replace(sc.plat.Userprefs.getDecimalSeparator(), ".");
			}
			return value;
		},
		localizeNumberAttributeValue:function(value){
			if(value){
				value = value.replace(".", sc.plat.Userprefs.getDecimalSeparator());
			}
			return value;
		},
		getBoolAttrDisplayValue:function(value, scope){
			if(value === 'Y' || value === 'y'){
				return scope["b_BoolTrue"];
			}else{
				return scope["b_BoolFalse"];
			}
		},
		getBoolAttrDBValue:function(value, scope){
			if(value === 'b_BoolTrue' || value === scope['b_BoolTrue'] || value === 'Y' || value === value === 'y' || value === true){
				return 'Y';
			}else{
				return 'N';
			}
		}
	}
}();

attributeUtils = sc.sbc.itemadmin.utils.attributes.AttributeUtils;

Ext.namespace("sc.swc.attribute.picker");

sc.swc.attribute.picker.TreeLoader = function(config) {
	if(!config || !config.sbcTreeLoaderData){
		alert("No sbcTreeLoaderData Specified");
		return;
	}
	config.clearOnLoad=false;
	config.preloadChildren=false;

    sc.swc.attribute.picker.TreeLoader.superclass.constructor.call(this, config);
    this.sbcTreeLoaderData = config.sbcTreeLoaderData;
}
/**
 * Loader Object Configuration
 * sbcTreeLoaderData={
 * 		LookupMode:<Boolean> when set to true we will automatically disable certain functionality like dragging and dropping, multi-select,
 * 							edit, etc if any of these are enabled.
 * 		LoadData:{ Optional (If specified, the tree will call call this action to load the root nodes.
 * 							 If not specified the tree will not make a call and the parent screen will be responsible for providing
 * 							 the information needed to load the root nodes.)
 * 			ActionNS:<String> Required (The namespace which will be used to load the root node(s))
 * 			LoadAction:<String> Required (The action id of the strut which is used to load the root node(s))
 * 			ReturnConfiguration:[{ Required (An array of output namespace and bindings which will be used to process the return in the jsp
 *									and format them in a way that is understood by the sbctreeloader.)
 *				OutputNS:<String> Required. (The output namespace which contains information to be added to the tree.
 *				ElementPath:<String> Required. (The xpath to the elements which are to be added to the tree. Path delimter must be a '/' For example, 'AttributeList\Attribute'
 *			}]
 * 		}
 * 		NodeTypes:{ Required (Arbitrary types of nodes which will vary from tree to tree. These are used to characterize the look
 *		  					  and feel of each node and to modify the behavior for each node when it is selected.
 *		  					  If more than one node type exists this can be passed as an array.)
 *			Type:<String>Required (Just a internal string to identify the node type),
 *			IsLeafNode:true/false, (set certain properties which prevent API's from being called when it is selected
 *			Identification:{(Used to identify the type of node being added to the tree so that the properties are set correctly)
 *				ElementName:<String>Required(Element name for the nodes corresponding entity),
 *				Attribute:{(The attributes found on the <code>ElementName</code> what can be used to uniquely identify the node type.
 *							If there are multiple attributes needed to uniquely identify the node an array can be passed. In this case
 *							if any of the attributes do not match the expected value then identification will fail.)
 *					AttributeName:<String>Required (The XMLName of the attribute),
 *					ExpectedValue:<String>Required (The value that should be matched.
 *													If multiple values apply this can be passed as an array where the objects have a property called Value=<String>.
 *													For regular expression processing, add an attribute in the array for 'RegExp=true')
 *				},
 *				NestedElement:{(If the identification is pased on a nested element this property can be passed
 *								For multiple nested element identification use an array. If multiple all passed they must all match
 *								in order fro the identification to succeed. If there are multiple children, (ex. an attribute group can have
 *								multiple attributes) we will only validate that one of the children meets the criteria
 *								The contents of this object will be the same as those of the <code>Identification</code> element
 *				}
 *			},
 *			BindingData:{Required
 *				Display:<String>Required (The XMLName of the attribute on the <code>Identification/ElementName</code> which will be used as the
 *										  display value for the node,
 *				Key:<String>Required (The XMLName of the attribute on the <code>Identification/ElementName</code> which wil be passed into
 *									  apis which are called and to listening screens when inquiring for the selected node)
 *				SortByAttribute:<String> the attribute which will be used to sort by. Default to the text value on the node.
 *			},
 *			ImageCLS:<String> (The Image cld entry that should be used to display the image for the node in the tree),
 *			ExpandAction:<String> (When the node is selected this action will be fired),
 *			ActionNS:<String> Required For Non Leaf Nodes (the namespace to be used when calling the actions,
 *			ReturnConfiguration:[{ Required (An array of output namespace and bindings which will be used to process the return in the jsp
 *									and format them in a way that is understood by the sbctreeloader.)
 *				OutputNS:<String> Required. (The output namespace which contains information to be added to the tree.
 *				ElementPath:<String> Required. (The xpath to the elements which are to be added to the tree. Path delimter must be a '/' For example, 'AttributeList\Attribute'
 *			}]
 *			Sequencing:{
 *				allowDrag:<Boolean>This node can be dragged but cannot have other nodes dropped into it. Default is false.
 *				allowDrop:<Boolean>This node cannot be dragged but can have other nodes dropped into it. Default is false
 *				CanBeDragged: <String> Where the node can be dropped if it is dragged.
 *									ANYWHERE: This node can be dragged and dropped anywhere in the tree. This is the default.
 *									SAMEPARENT: This node can only be dropped in a place where it has the same parent.
 *				ChangeSequenceAction:<String> the action which should be run when a node's sequence is changed
 *				ActionNS:<String> the namespace to be used when changing the sequence.
 *			}
 *			QTipConfig:{
 *				bundleKey:<String>The bundle key that will be used in the tooltip
 *						  if the value is determined by a setting on the sbcTreeLoaderData NodeType object it can be specified by
 *						  starting the value with an '@' followed by the path.name of the value to be used.
 *						  the evaluated expression will be prepended by "b_TreeQTip"
 *				params:[<Array> An array of object to be evaluated and passed when evaluating the bundleKey. The order the object are specified here is how they will be passed
 *							    when being evaluated.
 *					{
 *						bundleKey:<String> The bundle key which needs to be evaluated before being passed as a param. This value is evaluated
 *										   using the same logic used for the bundleKey directly under the QTipConfig
 *						attribute:<String> The path.name of the attribute inside the json object that should be passed as a param
 *						defaultBundleKey:<String> The bundle key that should be used if the bundleKey and attribute cannot be evaluated to a value.
 *					}
 *				]
 *			}
 *		},
 *		DisplayNodeType:{ (In some instances you may want to filter down which nodes are displayed in the tree. All node types are INCLUDED
 *							by default. each node type which should behavior differently needs to be passed here. If multiple nodes need
 *							to be passed. Pass as an array.
 *			DisplayMode:'INCLUDE'/'EXCLUDE'/'DISABLE'
 *												   (INCLUDE will only display the node type in the list as selectable. This is the default.,
 *													EXCLUDE will not display the node type in the tree.,
 *													DISABLE will display the node in the tree but it will show as disabled.)
 *			NodeType:<String> The node type which the DisplayMode will apply to.)
 *		}
 *	}
 */
sc.swc.attribute.picker.TreeLoader = Ext.extend(sc.swc.attribute.picker.TreeLoader, Ext.tree.TreeLoader, {
	/**
	 * The tree which uses this instance of the sbcTreeLoader class.
	 * @type Ext.tree.TreePanel
	 */
	ownerTree:null,
	/**
	 * The sbcTreeLoaderData that is used by this instance of the sbcTreeLoader class.
	 * @type Object
	 */
	sbcTreeLoaderData:null,
	/**
	 * a map of all the nodes in the tree based on the nodes key attribute specified in the sbcTreeLoaderData
	 */
	nodeByKey:{},
	/**
	 * When a load is requested which requires a request to get a the list of children. We will store the data being loaded in this variable
	 * so that when the request returns we know which of the new nodes needs to be selected.
	 */
	nodeToSelectAfterLoad:null,
	/**
	 * Sets the ownerTree property, adds a rendered which will select the first node when the tree is rendered, and adds the tree sorter.
	 * @param {} tree
	 */
	setOwnerTree:function(tree){
    	this.ownerTree = tree;
    	if(this.sbcTreeLoaderData.LookupMode != true){
	    	this.ownerTree.on({
	    		"render": {
	    			fn: this.selectFirstRoot,
				    scope:this
				},
				"nodedragover":{
					fn: this.canNodeBeDropped,
					scope:this
				},
				"movenode":{
					fn:this.nodeMoved,
					scope:this
				},
				"click":{
					fn:this.updateTBar,
					scope:this
				}
	    	});
    	}
    	this.ownerTree.getSelectionModel().on('selectionchange', function(selModel, node) {
				if(selModel instanceof Ext.tree.MultiSelectionModel){
					node = node[0];
				}
				if(this.getNode(node)) {
					selModel.select(this.getNode(node),false);
					this.ownerTree.fireEvent("click", node);
				}
			},
			this,
			{
				buffer:1000
			}
		);

    	new Ext.tree.TreeSorter(this.ownerTree, {sortType:this.getSortValue, folderSort:true});
    },
    selectFirstRoot:function(){
		var firstNode = this.ownerTree.getRootNode().firstChild
		this.ownerTree.getSelectionModel().select(this.getNode(firstNode),false);
		this.ownerTree.fireEvent("click", firstNode);
	},
	canNodeBeDropped:function(eventData){
		var dropNode = eventData.dropNode;
		var drpNdDnDProp = dropNode.attributes.nodetype.Sequencing;
		if(!drpNdDnDProp){
			drpNdDnDProp = {}
		}

		//If the node must be dropped within the same parent then we need to verify which node it is being dropped into.
		if(drpNdDnDProp.CanBeDragged == "SAMEPARENT"){
			//we don't want to allow append so we will change the point to be below
			if(eventData.point === "append"){
				return false;
			}

			var targetNode = eventData.target;
			var targetParentId = targetNode.parentNode.attributes.key;
			var drpNdParentId = dropNode.parentNode.attributes.key;
			if(targetParentId != drpNdParentId){
				return false;
			}

			//If the targetNode and drop node are not the same then we need to confirm that the place where the node is being dropped
			//If the node is being dropped between a folder and leaf node it should be allowed. Otherwise it should not.
			if(targetNode.leaf != dropNode.leaf){
				//if the targetNode is not a leaf, the point is below, and the next node is a leaf. Then it should be allowed
				if(!targetNode.leaf && eventData.point === "below" && targetNode.nextSibling.leaf){
					return;
				//if the targetNode is a leaf, the point is above, and the previous node is not a leaf then it should be allowed
				}else if(targetNode.leaf && eventData.point === "above " && !targetNode.nextSibling.leaf){
					return;
				//All other instances should return false
				}else{
					return false;
				}
			}
		}
	},
	nodeMoved:function(tree, nodeMoved, oldParent, newParent, newIndex){
		var sequencingInfo = nodeMoved.attributes.nodetype.Sequencing;
		//due to timing issues sometimes the node at the newIndex is the node being dragged and sometimes it isn't.
		//These conditions catch these scenarios and adjust accordingly
		var newPreviousNode = newParent.item(newIndex-1);
		var newNextNode = newParent.item(newIndex);
		if(newNextNode === nodeMoved){
			newNextNode = newParent.item(newIndex+1);
		}
		var newSeqNo = this.getSequenceNoBetweenNodes(newPreviousNode, newNextNode, nodeMoved.isLeaf())
		var inputObj = this.getChangeSequenceNoInput(nodeMoved, newSeqNo, newNextNode, newPreviousNode, newParent);
		var ajaxArgs = {"node": nodeMoved, "newSeqNo":newSeqNo, "noUpdateRequired":true};
		if(newSeqNo < 0){
			ajaxArgs.fireRefresh = true;
		}
		if(inputObj){
			seaAjaxUtils.request({
				actionNS : sequencingInfo.ActionNS,
				action : sequencingInfo.ChangeSequenceAction,
				params : inputObj,
				success : this.handleResponse,
				failure : this.handleFailure,
				argument: ajaxArgs,
				scope : this,
				successMsg : this.b_SaveSuccessfull
			});
		}
	},
	getSortValue:function(node){
		var sortByAttribute = node.attributes.nodetype.BindingData.SortByAttribute;
		if(!sortByAttribute){
			return node.text;
		}else{
			var sequenceVal = node.attributes.json[sortByAttribute];
			if(sortByAttribute === "SequenceNo" && Ext.type(sequenceVal) === "string"){
				sequenceVal = parseInt(sequenceVal);
			}
	    	return sequenceVal;
		}
	},
	/**
     * This method converts the data passed into nodes based on the sbcTreeLoaderData and appends the new nodes under the parent
     * @param {} data json object containing the data from the API output
     * @param {} parent Ext.tree.TreeNode The parent tree node that the children will be appended to.
     * @param {} selectNewNode <Boolean> selects the node in the tree that was just crated. If multiple nodes are created it will select the first one.
     * @param {} loadAllChildren <Boolean> if the parent node has not requested child data then fire the request to load all the children for the parent.
     */
    loadData:function(data, parent, selectNewNode, loadAllChildren){
    	if(!parent){
    		parent="root"
    	}
    	if(Ext.type(parent) === "string"){
	    	parent=this.getNode(parent);
    	}

    	if(!parent.childrenRendered){
    		//If loadAllChildren is not passed we need to default the value to true
	    	if(!loadAllChildren && loadAllChildren != false){
	    		loadAllChildren = true;
	    	}
	    	if(loadAllChildren){
    			this.requestData(parent, parent.loadComplete.createDelegate(parent, [parent.deep, parent.anim, parent.callback]));
    			this.nodeToSelectAfterLoad = data;
    			return;
    		}
    	}

    	var newNodes = this.createNode(data);
	    if(newNodes){
		    for(var i=0 ; i<newNodes.length ; i++){
	    	    var newNode = newNodes[i];
	    	    parent.appendChild(newNode);
	    	    this.nodeByKey[newNode.attributes.key] = newNode;
			    if(parent.attributes.key === "root"){
		    		this.requestData(newNode, newNode.loadComplete.createDelegate(newNode, [newNode.deep, newNode.anim, newNode.callback]));
			    }
		    }
		    if(selectNewNode){
		    	parent.expand();
				if(this.getNode(newNodes[0]))
		    	  this.ownerTree.getSelectionModel().select(this.getNode(newNodes[0]),false);
		    }else if(this.nodeToSelectAfterLoad){
				//if there is a node to select we need to add a listener to the parent nodes expand event
		    	// if we try to select the node before the parent is expanded the selection will not work.
		    	parent.on('expand',
	        			  	function(){ 
					        	var nodeToSelect = this.createNode(this.nodeToSelectAfterLoad);
							    if(nodeToSelect){
							    	var existingNode = this.getNode(nodeToSelect[0].attributes.key);
						    		if(existingNode){
						    			this.ownerTree.getSelectionModel().select(this.getNode(existingNode),false);
						    		}
								}
					        	this.nodeToSelectAfterLoad = null;
				  		  	},
						  	this
				);
	        }
	    }

    },
    refreshData:function(data){
    	var selModel = this.ownerTree.getSelectionModel();
    	var selectedNode = selModel.getSelectedNode().attributes.key;

    	var newNodes = this.createNode(data);
		
	    if(newNodes){
		    for(var i=0 ; i<newNodes.length ; newNodes.splice(i,1)){
		    	var existingNode = this.getNode(newNodes[i].attributes.key); 
	    		if(existingNode){
	    			//if the nodetype changes we need to replace the node because the images, themese, style could have changed as well
	    			if(existingNode.attributes.nodetype.Type === newNodes[i].attributes.nodetype.Type){
		    			existingNode.attributes.nodetype = newNodes[i].attributes.nodetype;
		    			existingNode.attributes.json = newNodes[i].attributes.json;
		    			existingNode.setText(newNodes[i].text);
		    			existingNode.qtip = sc.swc.attribute.picker.sbcTreeNodeCreator.evaluateTooltip(existingNode.attributes.json, existingNode.attributes.nodetype);
	    			}else{
	    				var parent = existingNode.parentNode;
	    				existingNode.remove();
	    				parent.appendChild(newNodes[i]);
	    				this.nodeByKey[newNodes[i].attributes.key] = newNodes[i];
	    			}
	    		}
	    	}
	    }
		if(selectedNode)
			selModel.select(this.getNode(selectedNode),false);
    },
    /**
     * Gets the node from the tree with the id passed as input.
     * @param {} id String the node id.
     * @return {} Ext.tree.TreeNode
     */
    getNode:function(id){
    	var root = this.ownerTree.getRootNode();
    	if(id=="root"){
	    	return root;
    	}else{
    		return this.nodeByKey[id];
    	}
    },
    /**
     * overridden method from Ext.tree.TreeLoader. This method is called when data needs to be retrieved from the server to populate
     * the children of the selected node.
     * @param {} node Ext.tree.TreeNode - the selected node which the data needs to be retrieved for.
     * @param {} callback
     */
    load : function(node, callback, isRefresh){
        this.requestData(node, callback, isRefresh);
    },
    /**
     * Overridden method from Ext.tree.TreeLoader. This method is actually creates the Ajax request and submits the request to the server.
     * @param {} node Ext.tree.TreeNode the node for which the data is being requested.
     * @param {} callback
     */
    requestData : function(node, callback, isRefresh){
		var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Please wait..."});
		mask.show();
        if(this.fireEvent("beforeload", this, node, callback) !== false){
	        if(node){
	        	//because we hide the root node, the tree automatically tries to request data for the first node.
	        	//So if we specify this load request info then we will use it. Otherwise we will do nothing
	        	//and let the parent screen provide the data.
	        	if((node.attributes.key === "root" && this.sbcTreeLoaderData.LoadData)){
	        		var inputObj = this.getLoadParams();
	        		inputObj.sbctreeloader=Ext.encode(this.sbcTreeLoaderData.LoadData.ReturnConfiguration);
					if(inputObj){
						Ext.Ajax.request({
							url : this.sbcTreeLoaderData.LoadData.url,
							params : inputObj,
							success : this.handleResponse,
							failure : this.handleFailure,
							argument: {callback: callback, node: node, isRefreshCall:isRefresh, mask:mask},
							scope : this
						});
					}
	        	}else if(node.attributes.key != "root"){
		        	var inputObj = eval("this.get"+node.attributes.nodetype.Type+"Params(node.attributes.json)");
					inputObj.sbctreeloader=Ext.encode(node.attributes.nodetype.ReturnConfiguration);
					if(inputObj){
						Ext.Ajax.request({
							url : node.attributes.nodetype.url,
							params : inputObj,
							success : this.handleResponse,
							failure : this.handleFailure,
							argument: {callback: callback, node: node, isRefreshCall:isRefresh, mask:mask},
							scope : this
						});
					}
	        	}
	        }
        }else{
            if(typeof callback == "function"){
                callback();
            }
        }

    },
    /**
     * Overridden method from Ext.tree.TreeNode. converts the data passed into an Ext.tree.TreeNode object.
     * @param {} data object - the json object from the api output.
     * @return {}
     */
    createNode : function(data){
    	return sc.swc.attribute.picker.sbcTreeNodeCreator.create(this.sbcTreeLoaderData, data);
    },
    /**
     * Overridden method from Ext.tree.TreeNode. This method process the response from the request sent using the requestData method.
     * @param {} response
     * @param {} node
     * @param {} callback
     */
    processResponse : function(response, node, callback){

        if(!response.argument.noUpdateRequired){
        	var responseText = response.responseText;
	        if(responseText){
	        	responseText = Ext.decode(responseText);
		        node.beginUpdate();
		        //if(response.argument.isRefreshCall){
		        //	this.refreshData(responseText.treedata);
		        //}else{
			        this.loadData(responseText.treedata, node, false, false);

		        //}
		        node.endUpdate();
	        }
        }

        var newSeqNo = response.argument.newSeqNo;
        if(newSeqNo){
        	node.attributes.json.SequenceNo = newSeqNo;
        }
        if(response.argument.fireRefresh){
        	this.load(node.parentNode, callback, true);
        }else if(typeof callback == "function"){
            callback(this, node);
        }
		response.argument.mask.hide();
    },
    /**
     * This method will only work if the <code>SortByAttribute</code> for the <code>node</codes>'s <code>nodetype</code> is "SequenceNo".
     * This method get the last child node of <code>node</code> and return that child nodes sequence number incremented by 100.
     * If the next sequence no cannot be determined it will be returned as -1. The second parameter determines which sequence to use.
     * passing true will return the next leaf sequenceNo. Passing false will return the next folder sequenceNo
     * @param {} node
     * @param {} isLeaf
     */
    getNewChildSequenceNoForNode:function(parentNode, isLeaf){
    	var seqNo = -1;
    	if(Ext.type(parentNode)==="string"){
    		parentNode = this.getNode(parentNode);
    	}
    	if(parentNode){
	    	var children = parentNode.childNodes;
	    	if(children.length > 0){
	    		for(var i=0 ; i<children.length ; i++){
	    			if(children[i].isLeaf() === isLeaf){
	    				seqNo = children[i].attributes.json.SequenceNo;
	    			}
	    		}
	    		seqNo = parseInt(seqNo)+100;
	    	}else{
	    		seqNo = 100;
	    	}
    	}
    	return seqNo;
    },
    /**
     * determines if there is an available sequenceValue between the two nodes. If there is no integer value between the two -1 will be returned.
     * @param {} previousNode
     * @param {} nextNode
     * @return {}
     */
    getSequenceNoBetweenNodes:function(previousNode, nextNode, forLeafNode){
    	var sequenceNo = -1;
    	//to ensure that the sequence numbers of the leaf nodes vs folder nodes doesn't cross over we make the different types null
    	//if the do not apply.
    	if(nextNode){
    		if(nextNode.isLeaf() != forLeafNode){
    			nextNode = null;
    		}
    	}
    	if(previousNode){
    		if(previousNode.isLeaf() != forLeafNode){
    			previousNode = null;
    		}
    	}
    	if(nextNode){
			var previousSequenceNo = 0;
			if(previousNode){
				previousSequenceNo = previousNode.attributes.json.SequenceNo;
			}
			var nextSequenceNo = nextNode.attributes.json.SequenceNo;
			var space = nextSequenceNo-previousSequenceNo;
			if(space > 1){
				var middle = Math.floor(space/2);
				return parseInt(previousSequenceNo)+middle;
			}
		}else{
			if(previousNode){
				sequenceNo = this.getNewChildSequenceNoForNode(previousNode.parentNode, forLeafNode);
			}else{
				sequenceNo = 100;
			}
		}
		return sequenceNo;

    },
    /**
     *
     * @param {} direction : Required  The direction of the move. Values are 'UP' and 'DOWN'
     * @param {} node : The node to be moved. This can be either a TreeNode object or the key of the node.
     * 				    If multiple nodes need to be moved pass an array.
     * 					This defaults to the selected node according to the selection model.
     * @param {} positions :  the Integer value for the number of positions the node should be moved.
     * 					The default is 1;
     */
    moveNode:function(direction, node, positions){
		if(!node){
			node = [this.ownerTree.getSelectionModel().getSelectedNode()];
		}else if(Ext.type(node) === "string"){
    		node = [this.getNode(node)];
    	}else if(node.length > 0){
    		if(Ext.type(node[0]) === "string"){
    			for(var i=0 ; i<node.length ; i++){
    				var existingNode = this.getNode(node);
    				if(existingNode){
	    				node[i] = existingNode;
    				}else{
    					node.splice(i, 1);
    					i--;
    				}
    			}
    		}
    	}

    	if(node){
    		if(node.length > 0){
    			if(!positions){
    				positions = 1;
    			}
    			//we have to make a seperate call for each node because if upon move one node, the sequence numbers for all the nodes
    			//change we the updates for the other nodes will also need to change as well. This is ok because the trees we provide
    			// OOB will only support single node selection.
    			if(direction === "UP"){
	    			for(var i=0 ; i<node.length ; i++){
	    				var parent = node[i].parentNode;
	    				var oldIndex = parent.indexOf(node[i]);
	    				parent.insertBefore(node[i], parent.item(oldIndex-1));
	    			}
    			}else if(direction === "DOWN"){
    				for(var i=node.length-1 ; i>=0 ; i--){
	    				var parent = node[i].parentNode;
	    				var oldIndex = parent.indexOf(node[i]);
	    				//It is plus two because itsert before + 1 would eb the same loation!
	    				parent.insertBefore(node[i], parent.item(oldIndex+2));
	    			}
    			}
    		}
			if(this.getNode(node[0]))
	    		this.ownerTree.getSelectionModel().select(this.getNode(node[0]),false);
    	}
    },
    updateTBar:function(node){
    	var enableUp = false;
    	//var upButton = sc.sbc.util.CoreUtils.getTopToolBarItemBySciId(this.ownerTree, "tbarBtnMoveUp");
    	var enableDown = false;
    	//var downButton = sc.sbc.util.CoreUtils.getTopToolBarItemBySciId(this.ownerTree, "tbarBtnMoveDown");

    	if(node.attributes.nodetype.Sequencing.allowDrag){
    		if(node.previousSibling){
    			if(node.previousSibling.isLeaf() === node.isLeaf()){
    				enableUp=true;
    			}
    		}
    		if(node.nextSibling ){
    			if(node.nextSibling.isLeaf()  === node.isLeaf() ){
    				enableDown=true;
    			}
    		}
    	}
    	if(upButton){
    		if(enableUp){
    		upButton.enable();
	    	}else{
	    		upButton.disable();
	    	}
    	}
    	if(downButton){
    		if(enableDown){
    		downButton.enable();
	    	}else{
	    		downButton.disable();
	    	}
    	}

    },
    /**
     * This node will remove the node from the tree and fire the selection event for the parent node. This will not fire the API
     * to remove the selected record. The corresponding detail screen will need to do this.
     * @param {} node : the node to be removed. This can be either the node object or the key of the node.
     */
    deleteNode:function(node){
    	if(Ext.type(node) === "string"){
    		node = this.getNode(node);
    	}
    	if(node){
    		var parent = node.parentNode;
    		var nodeKey = node.attributes.key;
    		delete this.nodeByKey[nodeKey];
    		node.remove();
    		this.ownerTree.getSelectionModel().select(parent,false);
    		this.ownerTree.fireEvent("click", parent);
    	}

    }
});

/**
 * The root node for all sbc tree's. This node is always hidden. thie first level of a displayed tree is the conceptual tree root.
 * This allows us to have trees which have multiple root nodes.
 */
sc.swc.attribute.picker.getSBCTreeRoot = function(){
	return new Ext.tree.AsyncTreeNode({
		key:"root",
		text:"root"
	});
}

/**
 * This takes an sbcTreeLoaderData and tries to find the node types in the data that is passed.
 * If all criteria for a node type is made a new node will be created. The result is always an array of nodes. If no nodes are created
 * an empty array is returned.
 * @param {} sbcTreeLoaderData
 * @param {} data
 */
sc.swc.attribute.picker.sbcTreeNodeCreator = {
	create:function(sbcTreeLoaderData, data){
		//first we need to determine what node type we are trying to create
		var newNodes = [];

		var getDataForNodeType = function(identData, data){
			//see if the data passed contains any data for this node type
			var returnNodeList = [];
			var newNodeList = null;
			if(identData.ElementName)
			 	newNodeList = data[identData.ElementName];
			else if (identData.jsonBinding){
				newNodeList = data[identData.jsonBinding];
			}
			if(newNodeList){
				//In order for the loop below to work we need to make sure newNodeData is a list of at least one data element
				if(newNodeList.length === undefined){
					newNodeList = [newNodeList];
				}
				if(newNodeList.length > 0){
					//if more criteria is specified we will remove records which do not match.
					for(var j=0 ; j<newNodeList.length ; j++){
						var nodeData = newNodeList[j];
						//if the node matches both the nestedElement and attribute criteria then it can be returned as a match
						if(meetsAttributeCriteria(identData.Attribute, nodeData) && meetsElementCriteria(identData.NestedElement, nodeData)){
							returnNodeList.push(nodeData);
							//once the node has been classified we remove to help increase performance and ensure that it doesn't get identified as another node type.
							newNodeList.splice(j, 1);
							j--;
						}
					}
				}
			}
			return returnNodeList;
		}
		var meetsElementCriteria = function(identification, data){
			var isMatch=true;
			if(identification){
				if(!identification.length){
					identification=[identification];
				}
				//All of these elements must match in order for us to return true
				for(var i=0 ; i<identification.length ; i++){
					var elemList = data[identification[i].ElementName];
					var matchFound=false;
					if(elemList){
						//In order for the loop below to work we need to make sure newNodeData is a list of at least one data element
						if(!elemList.length){
							elemList = [elemList];
						}
						//only one of these elements needs to match in order for the inner loop to be true.
						for(var j=0 ; j<elemList.length ; j++){
							var matchingElem = elemList[j];
							//if the node matches both the nestedElement and attribute criteria then it can be returned as a match
							if(meetsAttributeCriteria(identification[i].Attribute, matchingElem) && meetsElementCriteria(identification[i].NestedElement, matchingElem)){
								matchFound=true;
								break;
							}
						}
					}
					if(!matchFound){
						isMatch=false;
						break;
					}
				}
			}
			return isMatch;
		}
		var meetsAttributeCriteria = function(attributes, data){
			var isMatch = true;
			if(attributes){
				if(!attributes.length){
					attributes=[attributes];
				}
				//All these attributes must have a value that matches or we can return false
				for(var i=0 ; i<attributes.length ; i++){
					var attribute = attributes[i];
					var attrVal = data[attributes[i].AttributeName];
					if(!attrVal){
						isMatch=false;
						break;
					}
					//if we find the attribute we need to verify the value
					var expValue = attribute.ExpectedValue;
					if(Ext.type(expValue) === "string"){
						if(!(attrVal == expValue)){
							isMatch=false;
							break;
						}
					}else{
						if(!expValue.length){
							expValue=[expValue];
						}
						var matchFound = false;
						//only one of these values needs to match correctly because an attribute can't have more than one value!
						for(var j=0 ; j<expValue.length ; j++){
							var expVal = expValue[j];
							if(expVal.RegExp){
								if(attrVal.search(expVal.Value) != -1){
									matchFound=true;
									break;
								}
							}else{
								if(attrVal == expVal.Value){
									matchFound=true;
									break;
								}
							}
						}
						if(!matchFound){
							isMatch=false;
							break;
						}
					}
				}
			}
			return isMatch;
		}
		var createNewNode = function(nodetype, nodedata, displayType){
			var Sequencing = nodetype.Sequencing ;
			if(!Sequencing){
				Sequencing={};
			}
			var disabled = false;
			if(displayType === "DISABLE"){
				disabled = true;
			}

			var qtipValue = sc.swc.attribute.picker.sbcTreeNodeCreator.evaluateTooltip(nodedata, nodetype);

			var config = {
				nodetype:nodetype,
				json:nodedata,
				key:nodedata[nodetype.BindingData.Key],
				text:nodedata[nodetype.BindingData.Display],
				leaf:nodetype.IsLeafNode,
				allowChildren:nodetype.IsLeafNode,
				draggable:Sequencing.allowDrag || false,
				allowDrag:Sequencing.allowDrag || false,
				isTarget:Sequencing.allowDrop || false,
				allowDrop:Sequencing.allowDrop || false,
				//iconCls:nodetype.ImageCLS,
				disabled:disabled,
				qtip:qtipValue
			};

			if(nodetype.IsLeafNode){
				return new Ext.tree.TreeNode(config);
			}else{
				if(nodedata.isNewNode === true){
					config.children=[];
					config.expanded=true;
				}
				return new Ext.tree.AsyncTreeNode(config);
			}
		}
		var getDisplayModeForType = function(type, sbcTreeLoaderData){
			var displayData = sbcTreeLoaderData.DisplayNodeType;
			if(displayData){
				if(!displayData.length){
					displayData = [displayData];
				}

				for(var i=0 ; i<displayData.length ; i++){
					var DisplayMode = displayData[i].DisplayMode;
					var nodeType = displayData[i].NodeType;


					if(type === nodeType){
						return DisplayMode;
					}
				}
			}
			return "INCLUDE";
		}
		for(var i=0 ; i<sbcTreeLoaderData.NodeTypes.length ; i++){
			var type = sbcTreeLoaderData.NodeTypes[i];
			var identData = type.Identification;
			var nodeTypeData = getDataForNodeType(identData, data);
			//we find the data first because part of identifying the nodes is removing the matched nodes from the list.
			//this keeps the nodes that shouldn't be displayed from being matched as a different node type.
			var displayType = getDisplayModeForType(type.Type, sbcTreeLoaderData)
			if(displayType != "EXCLUDE"){
				if(nodeTypeData.length > 0){
					for(var j=0 ; j<nodeTypeData.length ; j++){
						newNodes.push(createNewNode(type, nodeTypeData[j], displayType));
					}
				}
			}
		}

		if(newNodes.length === 0){
			return;
		}
		return newNodes;
	},
	evaluateTooltip:function(nodedata, nodetype){
		var evaluateBundleKey = function(bundleKey, params){
			if(Ext.type(bundleKey) === "string"){
				if(bundleKey.match("^@")=="@"){
					var evalBundleKey = bundleKey.substring(1);
					evalBundleKey = nodetype[evalBundleKey];
					return evaluateBundleKey(evalBundleKey, params)
				}else{
					if(params){
						return "To be changed";
					}else{
						return "To be changed";
					}

				}
			}else{
				return undefined;
			}
		}

		if(nodetype.QTipConfig){
			var params = nodetype.QTipConfig.params;
			var evaluatedParams = "";
			if(params && Ext.type(params)==="array"){
				for(var i=0 ; i<params.length ; i++){
					var paramBundleKey = params[i].bundleKey;
					paramBundleKey = evaluateBundleKey(paramBundleKey);
					if(paramBundleKey){
						evaluatedParams = evaluatedParams+",'"+paramBundleKey+"'";
					}else{
						var paramAttr = params[i].attribute;
						paramAttr = nodedata[paramAttr];

						if(paramAttr){
							evaluatedParams = evaluatedParams+",'"+paramAttr+"'";
						}else{
							var defaultBundleKey = params[i].defaultBundleKey;
							defaultBundleKey = evaluateBundleKey(defaultBundleKey);
							evaluatedParams = evaluatedParams+",'"+defaultBundleKey+"'";
						}
					}
				}
			}
			var bundleKey = nodetype.QTipConfig.bundleKey;
			return evaluateBundleKey(bundleKey, evaluatedParams);
		}
	}
}
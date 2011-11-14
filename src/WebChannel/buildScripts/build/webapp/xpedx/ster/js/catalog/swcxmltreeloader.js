
/*
 * Sterling Commerce Web Framework
 * Copyright(c) 2006-2008, Sterling Commerce, INC.
 * @author adodia
 */

 Ext.namespace("sc.swc.widget");

sc.swc.widget.SWCXMLTreePathLoader = function(config){
    this.baseParams = {};
    Ext.apply(this, config);

    this.addEvents(
        /**
         * @event beforeload
         * Fires before a network request is made to retrieve the Json text which specifies a node's children.
         * @param {Object} This TreeLoader object.
         * @param {Object} node The {@link Ext.tree.TreeNode} object being loaded.
         * @param {Object} callback The callback function specified in the {@link #load} call.
         */
        "beforeload",
        /**
         * @event load
         * Fires when the node has been successfuly loaded.
         * @param {Object} This TreeLoader object.
         * @param {Object} node The {@link Ext.tree.TreeNode} object being loaded.
         * @param {Object} response The response object containing the data from the server.
         */
        "load",
        /**
         * @event loadexception
         * Fires if the network request failed.
         * @param {Object} This TreeLoader object.
         * @param {Object} node The {@link Ext.tree.TreeNode} object being loaded.
         * @param {Object} response The response object containing the data from the server.
         */
        "loadexception"
    );

    sc.swc.widget.SWCXMLTreePathLoader.superclass.constructor.call(this);
};

/**
 * @class Ext.ux.XmlTreeLoader
 * @extends Ext.tree.TreeLoader
 * <p>A TreeLoader that can convert an XML document into a hierarchy of {@link Ext.tree.TreeNode}s.
 * Any text value included as a text node in the XML will be added to the parent node as an attribute
 * called <tt>innerText</tt>.  Also, the tag name of each XML node will be added to the tree node as
 * an attribute called <tt>tagName</tt>.</p>
 * <p>By default, this class expects that your source XML will provide the necessary attributes on each
 * node as expected by the {@link Ext.tree.TreePanel} to display and load properly.  However, you can
 * provide your own custom processing of node attributes by overriding the {@link #processNode} method
 * and modifying the attributes as needed before they are used to create the associated TreeNode.</p>
 * @constructor
 * Creates a new XmlTreeloader.
 * @param {Object} config A config object containing config properties.
 */
sc.swc.widget.SEATreePathLoader = Ext.extend(sc.swc.widget.SWCXMLTreePathLoader, Ext.tree.TreeLoader, {
    /**
     * @property  pathSeparator
     * String path Separator default to "/"
     * @type String
     */
    pathSeparator : "/",
    /**
     * @property  XML_NODE_TEXT
     * XML text node (value 3, read-only)
     * @type Number
     */
    XML_NODE_TEXT : 3,



   // private override
    requestData : function(node, callback){
        if(this.fireEvent("beforeload", this, node, callback) !== false){
            this.transId = Ext.Ajax.request({
                method:this.requestMethod,
                url: this.dataUrl||this.url,
                success: this.handleResponse,
                failure: this.handleFailure,
                scope: this,
                argument: {callback: callback, node: node},
                params: this.getParams(node)
            });
        }else{
            // if the load is cancelled, make sure we notify
            // the node that we are done
            if(typeof callback == "function"){
                callback();
            }
        }
    },



    // private override
     processResponse : function(response, node, callback){
        var json = response.responseText;
        try {
            var output = Ext.decode(json);
            //node.beginUpdate();
            var attributes = output["outputAttributeList.Attribute"];
            var data = [];
            for(var i = 0, len = attributes.length; i < len; i++){
            	data[i] = attributes[i].AttributeDescription;
            }
            data.sort();
            for(var i = 0, len = data.length; i < len; i++){
            	 this.parseString(data[i]);
            }

           // node.endUpdate();
            console.log(this.nodeArray);
            node.beginUpdate();
            for(var i = 0, len = this.nodeArray.length; i < len; i++){
            	this.addAttr(this.nodeArray[i]);
                var n = this.createNode(this.nodeArray[i]);
                if(n){
                    node.appendChild(n);
                }
            }
            node.endUpdate();
            if(typeof callback == "function"){
                callback(this, node);
            }
        }catch(e){
            this.handleFailure(response);
        }
    },

    addAttr : function (node){
    	if(node.children){
    		node.children.sort();
    		Ext.each(node.children, this.addAttr, this);
    	}else{
    		node.leaf = true;
    	}
    },



    parseString: function (str){
    	console.log(str);
    	var keys = str.split(this.pathSeparator);

    	if(!keys){
    		return null;
    	}else if(!keys[0]){
    		keys.remove(keys[0]);
    	}
    	if(str == "")
    		return;
    	var parent = this.getRootNode(keys[0]);
    	keys.remove(keys[0]);
    	if(keys.length > 0){
    		Ext.each(keys, function(n, i){
				var nodeObj = this.appendChild(parent, n, i == (keys.length-1));
				parent = nodeObj;
//				parent = this.getNodeObj(parent, n);
	     	},this);
    	}
    },

    getNodeObj : function(node,keyStr ){
    	    console.log(keyStr);
    		var i=Ext.each(node.children, function(n){
    			if( n.text === keyStr){
    				return false;
    			}
    			return true;
    		},this);
   			return node.children[i];
    },

    getRootNode: function(keyStr){

    	if(this.nodeArray){
    		var i = Ext.each(this.nodeArray, function(node){
    			if( node.text === keyStr){
    				return false;
    			}
    			return true;
    		}, this);
    		if(typeof i == "number" ){
    			return this.nodeArray[i];
    		}else{
    			var obj = this.creaeNodeObj(keyStr)
    			this.nodeArray.push(obj);
    			return obj;
    		}
    	}else{
    		var obj = this.creaeNodeObj(keyStr);
    		this.nodeArray = [obj];
    		return obj;
    	}
    },



    creaeNodeObj : function(keyStr){
    	var o = {
    		 text: keyStr
    	};
    	return o;
    },

    appendChild : function(node,keyStr, isLast ){
    	var nodeObj;
    	if(node.children){
    		if(isLast){
    			nodeObj = this.creaeNodeObj(keyStr)
    			node.children.push(nodeObj);
    			return node.children[node.children.length -1];
    		}
    		var i =Ext.each(node.children, function(node){
    			if( node.text === keyStr){
    				return false;
    			}
    			return true;
    		}, this);
    		if(! (typeof i == "number")){
    			nodeObj = this.creaeNodeObj(keyStr)
    			node.children.push(nodeObj);
    			return node.children[node.children.length -1];
    		}else{
    			return node.children[i];
    		}

    	}else{
    		nodeObj = this.creaeNodeObj(keyStr)
    		node.children = [nodeObj];
    		return node.children[0];
    	}
    	return nodeObj;
    }
});




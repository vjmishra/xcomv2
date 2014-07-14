Ext.namespace('sc.sbc.itemadmin.attribute.search');

sc.sbc.itemadmin.attribute.search.attrtreepanelUIConfig = function() {
    return {
        xtype: "screen",
        sciId: "attrtreepanel",
        layout: "anchor",
        items: [{
            xtype: "panel",
            sciId: "messagePanel",
            layout: "table",
            items: [{
                xtype: "label",
                sciId: "messageText",
                text: this.messageText,
                cls: "sea-instruction-text-label"
            }],
            cls: this.messageCellCls,
            hidden: this.hideLabel,
            border: false,
            layoutConfig: {
                defid: "tableLayoutConfig",
                columns: 4
            }
        },
        {
            xtype: "treepanel",
            sciId: "attrTree",
            autoWidth: false,
            autoScroll: true,
            autoHeight: false,
            root: sc.sbc.widget.getSBCTreeRoot(),
            rootVisible: false,
            border: false,
            loader: this.treeLoader,
            enableDD: this.enableDragAndDrop,
            containerScroll: true,
            tbar: this.toolbar,
            height: this.treeHeight,
            width: this.treeWidth,
            selModel: this.selModel,
            header: this.showHeader,
            title: this.treeTitle,
            sctype: this.treeScType
        },
        {
            xtype: "panel",
            sciId: "pnlAttrValueSelection",
            layout: "table",
            cls: this.attrValuePanelCls,
            hidden: this.hideAttrValueFields,
            border: false,
            items: [{
                xtype: "label",
                sciId: "lblAttrVal",
                text: this.b_AttributeValue,
                cls: "sc-plat-label",
                cellCls: "sea-align-top",
                hidden: true
            },
            {
                xtype: "panel",
                sciId: "pnlAttrValue",
                layout: "anchor",
                border: false,
                hidden: true,
                layoutConfig: {
                    defid: "layoutConfig",
                    columns: 4
                }
            },
            {
                xtype: "label",
                sciId: "lblSelectAttrMessage",
                text: this.b_SelectAttributeMessage,
                cls: "sc-plat-important-label",
                hidden: false
            }],
            sctype: "GroupPanel",
            title: String.format(sc.plat.ui.Screen.prototype.b_SpecifyValueForAttribute, ""),
            layoutConfig: {
                defid: "tableLayoutConfig",
                columns: 2
            }
        }],
        autoHeight: false,
        border: false,
        monitorResize: true,
        header: false
    };
}
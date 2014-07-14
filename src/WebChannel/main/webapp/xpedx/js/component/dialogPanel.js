/**
 * DialogPanel: This object provides functions supporting a modal or
 */
var DialogPanel = {
    
    toggleDialogVisibility: function(id) {
        var el = Ext.get(id);
        if (el != null) {
            if (el.isVisible()) {
                this.hide(id);
            }
            else {
                this.show(id);
            }
        }
    },

    hide: function(id) {
        var cmp = Ext.getCmp(id);
        cmp.hide();
    },

    show: function(id) {
        var cmp = Ext.getCmp(id);
        cmp.show();
    }
}

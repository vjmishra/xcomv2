function selectAll(){
	var checkboxes = Ext.query('input[id*=selectedLineItem]');
	Ext.each(checkboxes, function(obj_item){
		obj_item.checked = true;
	});
}

function deSelectAll(){
	var checkboxes = Ext.query('input[id*=selectedLineItem]');
	Ext.each(checkboxes, function(obj_item){
		obj_item.checked = false;
	});
}

$(document).ready(function() {
	$("#selAll1").click(function() {
		if($("#selAll1").attr('checked')) {
			selectAll();
			$("#selAll2").attr('checked', true);
		} else {
			deSelectAll();
			$("#selAll2").attr('checked', false);
		}
	});
	$("#selAll2").click(function() {
		if($("#selAll2").attr('checked')) {
			selectAll();
			$("#selAll1").attr('checked', true);
		} else {
			deSelectAll();
			$("#selAll1").attr('checked', false);
		}
	});
});
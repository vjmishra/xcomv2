
	function displayLineAdjustments(panelId, lineKey) {
			var d = Ext.DomQuery.selectNode(".lineAdj_"+lineKey);
			var e = Ext.DomQuery.selectNode(".adjustment-body");

			e.innerHTML = d.innerHTML;
			svg_classhandlers_decoratePage();
			DialogPanel.show(panelId);
	}

	function displayLightbox(sourceClassName) {
			var d = Ext.DomQuery.selectNode("."+sourceClassName);
			var e = Ext.DomQuery.selectNode(".adjustment-body");
			document.getElementById("adjustment-body").value=d.innerHTML;
			e.innerHTML = d.innerHTML;
			$('#adjustmentsLightBox').trigger('click');
	}
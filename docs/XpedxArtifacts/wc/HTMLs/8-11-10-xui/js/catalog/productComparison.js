Ext.onReady(function() {
		//javascript:SVGAnnotator.makeRoundedNoBorder(".submitBtnBg2");
		//javascript:SVGAnnotator.makeShadedRoundedRect(".submitBtnBg2", null, "bottomHalf", .05);
    	//javascript:SVGAnnotator.makeShadedRoundedRect(".submitBtnBg2");
    	//javascript:SVGAnnotator.makeRoundedBackground(".submitBtnBg2");
//    	javascript:SVGAnnotator.makeRoundedBackground(".submitBtnBg2");
    	//javascript:SVGAnnotator.makeRoundedBackground(".submitBtnBg2");


});



  function showDifferences()
  {
		document.prodCompare.showAttributesWithDifferentValuesOnly.value ="Y";
		document.prodCompare.submit();
  }

  function showAll()
  {
		document.prodCompare.showAttributesWithDifferentValuesOnly.value ="N";
		document.prodCompare.submit();
  }








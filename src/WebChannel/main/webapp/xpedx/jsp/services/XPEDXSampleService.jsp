<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:set name='wcContext' value="wCContext" />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<swc:html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<meta name="WT.ti" content='<s:property value="wCContext.storefrontId" /> - <s:text name='Services' />'></meta>



<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/RESOURCES<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->

<!-- javascript -->

<script type="text/javascript">
	var attrPickerActions  = new Array(3);
	attrPickerActions[0] = '<s:url action="attributemanagement-getattributeConfiguration" namespace="/catalog" escapeAmp="false" includeParams="none" />';
	attrPickerActions[1] = '<s:url action="attributemanagement-getAttributeGroupListForDomain" namespace="/catalog" escapeAmp="false" includeParams="none" />';
	attrPickerActions[2] = '<s:url action="attributemanagement-getAttributeGroupListForGroup" namespace="/catalog" escapeAmp="false" includeParams="none" />';               
</script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<%-- <script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/catalog/itemdetails.js"></script> --%>
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/catalog/XPEDXItemdetails<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/catalog/advancedSearch<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/catalog/swcxmltreeloader<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/sbc/picker/sbctreeloader<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/sbc/picker/itemattrtreeloader<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/sbc/picker/attrtreepanel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/sbc/picker/attributeutils<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils<s:property value='#wcUtil.xpedxBuildKey' />.js"></script><!-- javascript --> 

<script type="text/javascript"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/jquery-1.4.2<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript"	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
    

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"></script> 

<link href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/SpryTabbedPanels<s:property value='#wcUtil.xpedxBuildKey' />.css" rel="stylesheet" type="text/css" />





<script type="text/javascript">

			function onPageLoad(){
				if(document.getElementById('serviceRequestForm_serviceProviderNone').checked==false &&
				   document.getElementById('serviceRequestForm_serviceProviderFedEx').checked==false &&
				   document.getElementById('serviceRequestForm_serviceProviderUPS').checked==false)
				{
					document.getElementById('serviceRequestForm_serviceProviderNone').checked=true;
				}				
			}
			
			
			function validateAndAddDataRecord (tableRef) 
			{
				
				
				var fs_tbody = document.getElementById("tbl_data_facilitySupplies");
        		var fs_RowCount = fs_tbody.rows.length;         		
        		
        		var ps_tbody = document.getElementById("tbl_data_paperSupplies");
        		var ps_RowCount = ps_tbody.rows.length;         		
        
        		
        		if(tableRef=='tbl_data_paperSupplies') {
        			
        			
        			// for paper tab        			
        			if(ps_RowCount > 11)//extra row is present as empty row initially doesn't look good
		        		{
		            		alert("Cannot add more than 10 samples for Paper Supplies");
		            		return false;
		        		}
        			        			
					/* //Checking only for description and qty for Paper Tab
						if(isEmpty(document.getElementById('mil')))
						{
						 return false;
						}
						if(isEmpty(document.getElementById('mfg')))
						{
						    return false;
						}
						*/
						
      	        		if(isEmpty(document.getElementById('description')))
      	                 {
      	                      return false;
      	                 }
						if(isEmpty(document.getElementById('qty')))
						{
						    return false;
						}
        			
        		}
        		else if(tableRef=='tbl_data_facilitySupplies')
        		{
        			
        			
           			if(fs_RowCount > 11)//extra row is present as empty row initially doesn't look good
	        		{
	            		alert("Cannot add more than 10 samples for Facility Supplies");
	            		return false;
	        		}
        			
						/*
       	                if(isEmpty(document.getElementById('rmfg1')))
       	                {
       	                   	return false;
       	                }
       	                if(isEmpty(document.getElementById('rManufacturer1')))
       	                {
       	                    return false;
       	                } */
      	                    if(isEmpty(document.getElementById('rmfg1')))
       	                    {
       	                        return false;
       	                    }     
      	                    if(isEmpty(document.getElementById('rManufacturer1')))
   	                    {
   	                        return false;
   	                    }   

      	                    if(isEmpty(document.getElementById('rDescription1')))
       	                    {
       	                        return false;
       	                    }   
    	                    if(isEmpty(document.getElementById('rQty')))
   	                    {
   	                        return false;
   	                    }     

        			
        		} else
        			{
        			alert("Got an unexpected table ... You have only Facilities and Paper Supplies Tabs...");
        			 return false;
        			}
        		
        		//Adding data after validation
        		 addNewDataRecord(tableRef) 
			}
			
			
			function addNewDataRecord(tableRef) 
			{
				
				
			 if(tableRef=='tbl_data_facilitySupplies')
				{
				 $('div#facility-results-table').removeAttr('style');				
				 
        		var tbody = document.getElementById("tbl_data_facilitySupplies").getElementsByTagName("tbody")[0];
        		
           		var row = document.createElement("tr");
        		var data0 = document.createElement("td");
				data0.setAttribute('class','border-right-gray');				
        		var data1 = document.createElement("td");
				data1.setAttribute('class','border-left-gray');				
        		var data2 = document.createElement("td");				
        		var data3 = document.createElement("td"); 				
        		var data4 = document.createElement("td");     				
        		var data5 = document.createElement("td");    				
        		var data6 = document.createElement("td");
        		
				var table = document.getElementById("tbl_data_facilitySupplies").getElementsByTagName("tbody")[0];
				var rowCount = table.rows.length;
				//var deleteLink = document.createElement('a');
				var deleteLink = document.createElement('a');
				//modified for jira 3636
				deleteLink.setAttribute('src','../../xpedx/images/common/icon_delete.gif');
				deleteLink.setAttribute('alt','delete');
				deleteLink.setAttribute('width','16');
				deleteLink.setAttribute('height','16');
				deleteLink.setAttribute('border','0');
				deleteLink.id = 'removeID'+(rowCount);
				
				//deleteLink.setAttribute('style','float:left');
				deleteLink.setAttribute('style','float:left;margin-left: 2px;');
				deleteLink.setAttribute('class','grey-ui-btn');
				deleteLink.innerHTML = '<span> Remove</span>';
				//deleteLink.appendChild (deleteLink);
				//end of jira 3636
				deleteLink.setAttribute('href', 'javascript:deleteRowIconFS('+rowCount+');');
				data0.appendChild(deleteLink);
        		
   
				data1.setAttribute('style', 'word-wrap: break-word; width:130px;');
				data2.setAttribute('style', 'word-wrap: break-word; width:130px;');
				data3.setAttribute('style', 'word-wrap: break-word; width:130px;');
				data4.setAttribute('style', 'word-wrap: break-word; width:290px;');
				data5.setAttribute('style', 'word-wrap: break-word; width:55px;');
				
				data1.appendChild (document.createTextNode(document.getElementById("rmfg1").value))
				data2.appendChild (document.createTextNode(document.getElementById("rManufacturer1").value))
				data3.appendChild (document.createTextNode(document.getElementById("rManufacturer2").value))
				data4.appendChild (document.createTextNode(document.getElementById("rDescription1").value))
				data5.appendChild (document.createTextNode(addComma(document.getElementById("rQty").value)))
				data6.appendChild (document.createTextNode("")) 
				
				/*
     			data1.innerHTML = '<p style="word-wrap: break-word; width:130px;">'+document.getElementById("rmfg1").value+'</p>';
				data2.innerHTML = '<p style="word-wrap: break-word; width:130px;">'+document.getElementById("rManufacturer1").value+'</p>';
				data3.innerHTML = '<p style="word-wrap: break-word; width:130px;">'+document.getElementById("rManufacturer2").value+'</p>';
				data4.innerHTML = '<p style="word-wrap: break-word; width:290px;">'+document.getElementById("rDescription1").value+'</p>';	
				data5.innerHTML = '<p style="word-wrap: break-word; width:55px;">'+document.getElementById("rQty").value+'</p>';
        		data6.appendChild (document.createTextNode("")) 
   				*/
        		       		
        		row.appendChild(data1);
        		row.appendChild(data2);
        		row.appendChild(data3);
        		row.appendChild(data4);
        		row.appendChild(data5);
        		row.appendChild(data0);        		
        		tbody.appendChild(row);  
        		
        		document.getElementById('qty').value=''; 
                document.getElementById('description').value='';
                document.getElementById('mil').value='';
                document.getElementById('mfg').value='';
                document.getElementById('mfgsku').value='';
                
                document.getElementById('rmfg1').value=''; 
                document.getElementById('rManufacturer1').value='';
                document.getElementById('rDescription1').value='';
                document.getElementById('rManufacturer2').value='';
                document.getElementById('rQty').value='';
                

				}
			 else if (tableRef=='tbl_data_paperSupplies')
				 {
				 $('div#paper-results-table').removeAttr('style');
				 
					var tbody = document.getElementById("tbl_data_paperSupplies").getElementsByTagName("tbody")[0];
	        		
	           		var row = document.createElement("tr");
	        		var data0 = document.createElement("td");
					data0.setAttribute('class','border-right-gray');				
	        		var data1 = document.createElement("td");
					data1.setAttribute('class','border-left-gray');				
	        		var data2 = document.createElement("td");				
	        		var data3 = document.createElement("td"); 				
	        		var data4 = document.createElement("td");     				
	        		var data5 = document.createElement("td");    				
	        		var data6 = document.createElement("td");
	        		

					var deleteLink = document.createElement('a');
					//modified for jira 3636
					deleteLink.setAttribute('src','../../xpedx/images/common/icon_delete.gif');
					deleteLink.setAttribute('alt','delete');
					deleteLink.setAttribute('width','16');
					deleteLink.setAttribute('height','16');
					deleteLink.setAttribute('border','0');
		
					
					deleteLink.setAttribute('style','float:left;margin-left: 2px;');
					deleteLink.setAttribute('class','grey-ui-btn');
					deleteLink.innerHTML = '<span> Remove</span>';
					//deleteLink.appendChild (deleteLink);
					//end of jira 3636
					var table = document.getElementById("tbl_data_paperSupplies").getElementsByTagName("tbody")[0];
					var rowCount = table.rows.length;
					deleteLink.setAttribute('href', 'javascript:deleteRowIconPS('+rowCount+');');
					data0.appendChild(deleteLink);
	
        			
        			var description = "";
        			var myParameters = new Array();
					myParameters[0] = document.getElementById("description").value;					        			   
        			for (i=0; i<myParameters.length; i++) {
            			if (myParameters[i] != "" && myParameters[i] != null) {
							if(description == "") {
								description =  description + myParameters[i];
							}
							else {
								description =  description + ", " + myParameters[i];																
							}                			 
            			}            			
        			}
        			         
        			
    				data1.setAttribute('style', 'word-wrap: break-word; width:130px;');
    				data2.setAttribute('style', 'word-wrap: break-word; width:130px;');
    				data3.setAttribute('style', 'word-wrap: break-word; width:130px;');
    				data4.setAttribute('style', 'word-wrap: break-word; width:290px;');
    				data5.setAttribute('style', 'word-wrap: break-word; width:55px;');
    				
    				data1.appendChild (document.createTextNode(document.getElementById("mfg").value));
    				data2.appendChild (document.createTextNode(document.getElementById("mil").value));
    				data3.appendChild (document.createTextNode(document.getElementById("mfgsku").value));
    				data4.appendChild (document.createTextNode(document.getElementById("description").value));
    				data5.appendChild (document.createTextNode(addComma(document.getElementById("qty").value)));
        			data6.appendChild (document.createTextNode(" "));
    				
    				/*
        			data1.innerHTML = '<p style="word-wrap: break-word; width:130px;">'+document.getElementById("mfg").value+'</p>';
					data2.innerHTML = '<p style="word-wrap: break-word; width:130px;">'+document.getElementById("mil").value+'</p>';
					data3.innerHTML = '<p style="word-wrap: break-word; width:130px;">'+document.getElementById("mfgsku").value+'</p>';
					data4.innerHTML = '<p style="word-wrap: break-word; width:290px;">'+document.getElementById("description").value+'</p>';	
					data5.innerHTML = '<p style="word-wrap: break-word; width:55px;">'+document.getElementById("qty").value+'</p>';
        			data6.appendChild (document.createTextNode(" "))
        			*/
        	
	        	
				
				//       		
        		row.appendChild(data1);
        		row.appendChild(data2);
        		row.appendChild(data3);
        		row.appendChild(data4);
        		row.appendChild(data5);
        		row.appendChild(data0); 
        		tbody.appendChild(row);  
        		
        		document.getElementById('qty').value=''; 
                document.getElementById('description').value='';
                document.getElementById('mil').value='';
                document.getElementById('mfg').value='';
                document.getElementById('mfgsku').value='';
                
                document.getElementById('rmfg1').value=''; 
                document.getElementById('rManufacturer1').value='';
                document.getElementById('rDescription1').value='';
                document.getElementById('rManufacturer2').value='';
                document.getElementById('rQty').value='';
				 }
        		return true;
				
			}
			
		
        	function selectAll(tableRef) {
        		var tbody = document.getElementById(tableRef);
        		var rowCount = tbody.rows.length;        		
        		for(var i=1; i<rowCount; i++) {
        			var row = tbody.rows[i]; 
        			if (null != row) {
        				var chkbox = row.cells[0].childNodes[0];
            			chkbox.checked = true;
            		}
        		}		
            }

        	function deleteRow(tableRef) {
        		var tbody = document.getElementById(tableRef);
        		var rowCount = tbody.rows.length;        		
        		for(var i=rowCount; i>=1; i--) {
        			var row = tbody.rows[i];
        			if (null != row) {
        				var chkbox = row.cells[0].childNodes[0];
            			if(null != chkbox && true == chkbox.checked) {							
            				tbody.deleteRow(i);
            			}
        			}         			
        		}
        	}      
        	
        	
        	function deleteRowIconPS(rowId) {
        		try {
       
        			
        				var table = document.getElementById("tbl_data_paperSupplies").getElementsByTagName("tbody")[0];
            			var rowCount = table.rows.length;
            			if(rowId > 0 && rowId < rowCount){
            				var deletedSeqIndex = 0;
                            deletedSeqIndex = table.rows[rowId].cells[3].childNodes[0].selectedIndex;
            				document.getElementById("tbl_data_paperSupplies").deleteRow(rowId);
            				rowCount = table.rows.length;
            				for(var i=rowId; i<rowCount; i++) {
            	                var row = table.rows[i];
                                row.cells[5].childNodes[0].setAttribute('href', 'javascript:deleteRowIconPS('+(i)+');');
            				}
					//added for jira 3636
            				for(var i=1; i<rowCount;i++){
        						var row = table.rows[i];
        						var removeSeq = row.cells[5].childNodes[0];
        	                    var removeIndex = removeSeq.selectedIndex;
        	                    if(removeIndex > deletedSeqIndex){
        	                    	removeSeq.selectedIndex = removeSeq.selectedIndex - 1;
        		                    //selectSeq.setAttribute('name', 'QuickLinksSeq' + i);
        		                    removeSeq.setAttribute('id', 'removeID' + i);
        	                    }
            				
            			}
				//end of jira 3636
            			}
        			
        		}catch(e) {
        			alert(e);
        		}
        	}
        	
        	function deleteRowIconFS(rowId) {
        		try {
       
        			
        				var table = document.getElementById("tbl_data_facilitySupplies").getElementsByTagName("tbody")[0];
            			var rowCount = table.rows.length;
            			if(rowId > 0 && rowId < rowCount){
            				var deletedSeqIndex = 0;
                            deletedSeqIndex = table.rows[rowId].cells[5].childNodes[0].selectedIndex;
            				document.getElementById("tbl_data_facilitySupplies").deleteRow(rowId);
            				rowCount = table.rows.length;
            				for(var i=rowId; i<rowCount; i++) {
            	                var row = table.rows[i];
                                row.cells[5].childNodes[0].setAttribute('href', 'javascript:deleteRowIconFS('+(i)+');');
            				}
            				//added for jira 3636
            				for(var i=1; i<rowCount;i++){
        						var row = table.rows[i];
        						var removeSeq = row.cells[5].childNodes[0];
        	                    var removeIndex = removeSeq.selectedIndex;
        	                    if(removeIndex > deletedSeqIndex){
        	                    	removeSeq.selectedIndex = removeSeq.selectedIndex - 1;
        		                    //selectSeq.setAttribute('name', 'QuickLinksSeq' + i);
        		                    removeSeq.setAttribute('id', 'removeID' + i);
        	                    }
            				
            				}
							//end of jira 3636
            			}        			
        		}catch(e) {
        			alert(e);
        		}
        	}
        	

        	function isEmpty(elem) {
       		  var str = elem.value;
       		    var re = /.+/;
       		    if(!str.match(re)) {
       		    	/*Start- Jira 3109  */
       		        alert("Required fields missing. Please review and try again.");
       		    	/*End- Jira 3109  */
       		        setTimeout("focusElement('" + elem.form.name + "', '" + elem.name + "')", 0);
       		        return true;
       		    } else {
       		        return false;
       		    }
        	}
        	
        	function isEmptyField(elem) {
         		  var str = elem.value;
         		    var re = /.+/;
         		    if(!str.match(re)) {
         		        alert("Please fill in the required field "+elem.name+".");
         		        setTimeout("focusElement('" + elem.form.name + "', '" + elem.name + "')", 0);
         		        return true;
         		    } else {
         		        return false;
         		    }
          	}
        	function focusElement(formName, elemName) {
        	    var elem = document.forms[formName].elements[elemName];
        	    elem.focus();
        	    elem.select();
        	}
        	
        	//--- Refactored JavaScript for form validation ---
        	
        	function isCompleteData(tableRefOne, tableRefTwo ){
        		
        	    var tbodyOne = document.getElementById(tableRefOne);
        		var rowCountTableRefOne = tbodyOne.rows.length;
        		
        		var completeData = "";
        		var completeDataPaper = "";
        		var finalCompleteData = "";
        		var FacilitySupplies = "";
        		var PaperSupplies = "";
        	    var tbodyTwo = document.getElementById(tableRefTwo);
        		var rowCountTableRefTwo = tbodyTwo.rows.length;
        	
        		
        		for(var i=1; i<rowCountTableRefOne; i++) {
        			var row = tbodyOne.rows[i];
        			if (null != row) {
            			var cellsCount = row.cells.length;
            			for(var j=0; j < cellsCount-1; j++) {                			
                			var temp ;
                				temp = row.cells[j].childNodes[0].nodeValue;
                			if(temp == "") {
                    			temp = "*#?";
                			}
                			completeData = completeData + "+=_" + temp;                			                			
            			}
        			}
        			FacilitySupplies = completeData + "+=_facilitySupplies";
            } 
        		
        		for(var i=1; i<rowCountTableRefTwo; i++) {
        			var row = tbodyTwo.rows[i];
        			if (null != row) {
            			var cellsCount = row.cells.length;
            			for(var j=0; j < cellsCount-1; j++) {                			
                			var temp ;
                			temp = row.cells[j].childNodes[0].nodeValue;
                				//---- Debug ---
                				//alert( "row.cells[j] " + row.cells[j] );
                				//alert( "row " + row );
                				//alert( "row.cells[j].innerHTML " + row.cells[j].innerHTML );
                				//---- Debug ---
                			if(temp == "") {
                    			temp = "*#?";
                			}
                			//alert("completeData-----------"+completeData);
                			completeDataPaper = completeDataPaper + "+=_" + temp;    
                			
            			}
            			//alert("completeDataPaper"+completeDataPaper);
        			}
        			PaperSupplies = completeDataPaper + "+=_PaperSupplies";
        			} 
        		if(FacilitySupplies!='' && PaperSupplies == '' ){
        			finalCompleteData = FacilitySupplies;
        		}else if(PaperSupplies!='' && FacilitySupplies == ''){
        			
        			finalCompleteData = PaperSupplies;
        		}else if(FacilitySupplies!='' && PaperSupplies!=''){
        			
        			finalCompleteData = FacilitySupplies + "+=_true" + PaperSupplies;
        		}
        		if(finalCompleteData!='' || rowCountTableRefOne > 1 || rowCountTableRefTwo > 1)
             	{
                 	//alert("Data succes..completeData : " + completeData );	
             	}
        		else{
        			/*Start- Jira 3109  */
                 	alert('Please enter at least one row of General or Paper items to the list.');
        			/*End- Jira 3109  */
             	}
        		
        		document.getElementById('bodyData').value = finalCompleteData;    
        		return finalCompleteData;
        		}
        	
        	//Form Submit function
        	function submitForm(tableRefOne, tableRefTwo){
			    var phoneField = document.serviceRequestForm.phone;
			    var emailField = document.serviceRequestForm.email;
			    var contactField = document.getElementById("serviceRequestForm_contact");
			    var attnField = document.getElementById("serviceRequestForm_attn");
			    var fedExServiceProviderNumberField = document.serviceRequestForm.serviceRequestForm_serviceProviderNumber_FedEx; 
			    var upsServiceProviderNumberField = document.serviceRequestForm.serviceRequestForm_serviceProviderNumber_UPS; 

			   // alert(" submitForm ");
			    var errorDiv = document.getElementById("errorMsgForRequestSample");
			    //error messgae changed for jira 3648
			    var errorDivMessage = "Required fields missing. Please review and try again.";
			    var returnval = true;
			    
			    var completeDataNew = isCompleteData(tableRefOne, tableRefTwo );
			    
			    errorDiv.innerHTML = "";
			    errorDiv.style.display = "none";
			    
			    if (contactField.value.trim().length == 0)
			    {
			        //commented for jira 3648 errorDivMessage= errorDivMessage + "-Company ";
			        contactField.style.borderColor="#FF0000";
			        errorDiv.style.display = 'inline';
			        returnval = false;
			    }else{
			    	contactField.style.borderColor="";
			    }
			    
			    if (attnField.value.trim().length == 0)
			    {
			       //commented for jira 3648 errorDivMessage= errorDivMessage + "-Attention ";
			        attnField.style.borderColor="#FF0000";
			        errorDiv.style.display = 'inline';
			        returnval = false;
			    }else{
			    	attnField.style.borderColor="";
			    }
			    
			    if(phoneField.value.trim().length == 0)
			    {
			        //commented for jira 3648 errorDivMessage= errorDivMessage + "-Phone ";
			        phoneField.style.borderColor="#FF0000";
			        errorDiv.style.display = 'inline';
			        returnval = false;
			    }else{
			    	phoneField.style.borderColor="";
			    }
			    
			    if (emailField.value.trim().length == 0)
			    {
			        //commented for jira 3648 errorDivMessage= errorDivMessage + "-Email Address ";
			        emailField.style.borderColor="#FF0000";
			        errorDiv.style.display = 'inline';
			        returnval = false;
			    }else{
			    	emailField.style.borderColor="";
			    }
			    
			    //FedEx or UPS Tracking number Conditional display
			    
			    if ( (document.getElementById('serviceRequestForm_serviceProviderFedEx').checked==true) || (document.getElementById('serviceRequestForm_serviceProviderUPS').checked==true) ) {
			    	
			    	if(( fedExServiceProviderNumberField.value.trim().length ==0 ) && ( upsServiceProviderNumberField.value.trim().length ==0 )) 
			    		{
					        //commented for jira 3648 errorDivMessage= errorDivMessage + "-FedEx/UPS Number ";
					        fedExServiceProviderNumberField.style.borderColor="#FF0000";
					        errorDiv.style.display = 'inline';
					        returnval = false;
					        
					       // errorDivMessage= errorDivMessage + "-FedEx/UPS Number ";
					        upsServiceProviderNumberField.style.borderColor="#FF0000";
					        errorDiv.style.display = 'inline';
					        returnval = false;
			    		}else
			    			{
			    			 fedExServiceProviderNumberField.style.borderColor="";
			    			 upsServiceProviderNumberField.style.borderColor="";
			    		}
		    	}
			   
			    //Check for completeness of data
			    if(completeDataNew.trim().length == 0)
             	{
			    	//errorDivMessage = errorDivMessage + "  - At least one item should be added in the list";	
                 	returnval =  false;
             	}
        		
        		//
			    if(returnval == false ){
			    	errorDiv.innerHTML = errorDivMessage
			    }			    
			    else {
				   	errorDiv.innerHTML ="";
				    document.serviceRequestForm.submit();
				    returnval = true;
			    }
        		
			    return returnval;
			}
        	
</script>

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
    		    This is to avoid a defect in Struts that?s creating contention under load. 
    		    The explicit call style will also help the performance in evaluating Struts? OGNL statements. --%>
<s:set name='_action' value='[0]' />
<s:set name="xutil" value="XMLUtils" />
<s:set name='categoryListElem' value="categoryListElement" />
<s:set name='childCategoryListElem' value="childCategoryListElement" />
<s:set name='fieldListElem'
	value="searchableIndexFieldListOutPutElement" />

<!-- End of Original Code -->


<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/SpryTabbedPanels<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript"></script>
<script type="text/javascript" 
	src='<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js'> </script>
<script type="text/javascript" 
	src='<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.maskedinput-1.3<s:property value='#wcUtil.xpedxBuildKey' />.js'></script>
<script type="text/javascript">
	$(document).ready(function() {
		//$(document).pngFix();
		$("#varous1").fancybox();

$("#various2").fancybox();
$("#various3").fancybox();
$("#various4").fancybox();
$("#various5").fancybox();
$("#various6").fancybox();
$("#various7").fancybox();
$("#various8").fancybox();
$("#various9").fancybox();
$("#various10").fancybox();
$("#various11").fancybox();
$("#various12").fancybox();
$("#various13").fancybox();
$("#various14").fancybox();
$("#various15").fancybox();
$("#various16").fancybox();
$("#various17").fancybox();
$("#various18").fancybox();
$("#various19").fancybox();
$("#various20").fancybox();
$("#various21").fancybox();
$("#various22").fancybox();

$('.phone-numeric').numeric(false); 
$("#serviceRequestForm_phone").mask("999 999-9999");
});
	
	
	
</script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
<title><s:property value="wCContext.storefrontId" /> - <s:text name='Services' /></title>
<link href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/SpryTabbedPanels<s:property value='#wcUtil.xpedxBuildKey' />.css" rel="stylesheet"	type="text/css" />
</head>
<!-- END swc:head -->
<body class="ext-gecko ext-gecko3">


<!-- Start of Original Code -->
<s:set name='outDoc2' value='%{outDoc.documentElement}' />
<s:url id='targetURL' namespace='/common'
	action='xpedxGetAssignedCustomers' />

<s:url id='attrPicker' action='openattributelookup.action'
	namespace='/catalog' escapeAmp="false" includeParams='none' />


<s:url action='home.action' namespace='/home' id='myUrl'
	includeParams='none' />
<s:set name='advancedSearchParameters'
	value='{"path", "", "indexField_1","","searchTerm_1","indexField_2","","searchTerm_2","indexField_3","","searchTerm_3","","priceRange_From","","priceRange_To","","productFeature_3",
            														"","productFeature_1","","productFeature_2","","isSupersededIncluded","","productFeature_1","","productFeature_2","","productFeature_3"}' />
<swc:breadcrumb rootURL='#myUrl' group='catalog'
	displayGroup='advancedSearch' displayParam='#advancedSearchParameters' />
<s:set name="diname" value="%{'advancedSearch'}" />
<!-- End of Original Code -->


<div id="main-container">
<div id="main"><s:action name="xpedxHeader" executeResult="true"
	namespace="/common" />
<div class="container"><!-- breadcrumb --> <s:url
	id='servicesLink' namespace="/services"
	action='MyServicesHome'>
</s:url>
<div class="clearview">&nbsp;</div>
<div id="mid-col-mil">
			<s:if test="%{#_action.getErrorMesage()!=null}">
				<h2><font size="2" color="red"><s:property
					value='%{#_action.getErrorMesage()}' /></font></h2>
			</s:if> 
			<s:set name="currentShipTo"	value="#wcUtil.getShipToAdress(wCContext.customerId, wCContext.storefrontId)" />

	<div>
		  <h2> </h2>
          <span class="page-title">Request Sample</span>
          <br />
          <span class="smallfont"> * - Required Field </span>
          <s:form id="serviceRequestForm" name="serviceRequestForm" namespace="/services" method="post" action="saveServiceRequest">
          <s:property value="%{#errorMesage}" />
          <div class="clearview"> &nbsp;</div>
      		<fieldset class="feild-service" >
      		<legend>Shipping Information</legend>
      		<div id="requestservice">
              <table width="100%" class="form-service" border="0" cellspacing="0" cellpadding="0">
         <!--      <tr>
            	<div class="error" id="errorMsgForRequestSample" style="display : none"></div>
          		</tr> -->
                <tr>
                  <td valign="top"><div class="form-service"> 
                      
                      
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
	          			<td style="padding-left:3px;"><span class="red">*</span>&nbsp;Company:</td>
	          			<td><s:textfield tabindex="50" id="serviceRequestForm_contact" name="contact" cssClass="x-input width-250px" maxlength="30"  value="%{#currentShipTo.firstName} %{#currentShipTo.lastName}" /></td>
	          			<td colspan="2"><div class="margin-15" style="padding-bottom:5px;">To expedite the shipment of this request, please provide your FedEx or UPS number.</div></td>
          			</tr>	
                      <tr>
                        <td width="13%"  style="padding-left:3px;"><span class="red">*</span>&nbsp;Attention:</td>
                        
                        <td width="33%"><s:textfield tabindex="50" id="serviceRequestForm_attn" name="attention" cssClass="x-input width-250px" maxlength="30"  value="%{#currentShipTo.firstName} %{#currentShipTo.lastName}" /></td>
                       </td>
                        <td colspan="2" valign="top">
                               
                      <label>&nbsp;<input type="radio" class="margin-right" id="serviceRequestForm_serviceProviderFedEx" name="serviceProviderNone" onclick="Toggle('div1');" />FedEx </label>  
                      <label class="margin-left7"><input type="radio"class="margin-right"  id="serviceRequestForm_serviceProviderUPS" name="serviceProviderNone" onclick="Toggle('div2');" />UPS </label>
					  <label class="margin-left7">					  
					  <input type="radio" class="margin-right" name="serviceProviderNone" id="serviceRequestForm_serviceProviderNone" onclick="Toggle('div3');" checked="checked" />None</label>
                         
									<div id="div1"  class=" float-right  textAlignLeft" style="width:278px; text-align:left;"> 													 
														<s:textfield cssClass="x-input width-160px margin-left7" tabindex="50"  id="serviceRequestForm_serviceProviderNumber_FedEx" name="serviceProviderNumberFEDEX"  value="" maxlength="15" />
														</div>
									<div id="div2"  class=" float-right textAlignLeft" style="width:278px; text-align:left;"> 
									<s:textfield cssClass="x-input width-160px margin-left7" tabindex="50" id="serviceRequestForm_serviceProviderNumber_UPS" name="serviceProviderNumberUPS"  value="" maxlength="15" />
									</div>
									
									<div id="div3"  class="float-right"> </div> 
				</td>
                      </tr>
                      <tr>
                        <td>&nbsp;Address Line 1:</td>
                        <td><s:textfield tabindex="50" name="address1"	cssClass="x-input" cssStyle="width: 250px;" maxlength="30" value="%{#currentShipTo.addressList[0]}" />
                        </td>
                        <td width="12%" rowspan="3"  valign="top" >&nbsp;Comments:</td>
                        <td width="42%" rowspan="3" valign="top" >
                        
                        <textarea name="notes" cols="" rows="3"  class="x-input width-230px" onkeyup="javascript:restrictTextareaMaxLength(this,250);"  ></textarea></td>
                      </tr>
                      <tr>
                        <td>&nbsp;Address Line 2:</td>
                        <td><s:textfield tabindex="51" cssClass="x-input" name="address2" cssStyle="width: 250px;" maxlength="35" value="%{#currentShipTo.addressList[1]}" />
                        </td>
                      </tr>
                      <tr>
                        <td>&nbsp;Address Line 3:</td>
                        <td><s:textfield tabindex="52" cssClass="x-input" name="address3" cssStyle="width: 250px;" maxlength="35" value="%{#currentShipTo.addressList[2]}" />
                        </td>
                      </tr>
                      
                      <tr>
                        <td>&nbsp;City:</td>
                        <td><s:textfield tabindex="53" cssClass="x-input width-160px" name="city"  maxlength="20"	value="%{#currentShipTo.city}" />
                        </td>
                        <td>&nbsp;State / Province:</td>
                        <td><s:textfield tabindex="54" cssClass="x-input width-160px" name="state"	maxlength="20" value="%{#currentShipTo.state}" />
                        </td>
                      </tr>
                      <tr>
                        <td>&nbsp;Postal Code:</td>
                        <td><s:textfield tabindex="55" cssClass="x-input width-160px"
						name="zipCode" maxlength="10"
						onkeyup="formatPhone('Postal Code',serviceRequestForm.zipcode);"
						value="%{#currentShipTo.zipCode}" />
                        </td>
                        <td>&nbsp;Country:</td>
                        <td><s:textfield tabindex="56" cssClass="x-input"
						name="country" maxlength="20" value="%{#currentShipTo.country}" />
                        </td>
                      </tr>
                      <tr>
                        <td   style="padding-left:3px;"><span class="red">*</span>&nbsp;Phone:</td>
                        <td>
                        <s:textfield tabindex="57" cssClass="x-input width-160px phone-numeric" id="serviceRequestForm_phone" name="phone"	maxlength="10" value="%{#currentShipTo.dayPhone}"	 /> 
                        <input type="hidden" id="txtstran" name="txtstran" />
                         </td>
                        <td   style="padding-left:3px;"><span class="red">*</span>&nbsp;Email Address:</td>
                        <td><s:textfield tabindex="58" cssClass="x-input width-160px" id="serviceRequestForm_email" name="email"	maxlength="500" value="" />
                        </td>
                      </tr>
                    </table>
                    </div>
                  </td>
                  </tr>
                   
              </table></div>
      </fieldset>  
      <s:hidden id="bodyData" name="bodyData" />
       <div class="clearview"> &nbsp;</div>
        <div id="TabbedPanels1" class="TabbedPanels">
          <ul class="TabbedPanelsTabGroup" style="margin-left:5px;">
            <li class="TabbedPanelsTab" tabindex="0">General</li>
            <li class="TabbedPanelsTab" tabindex="0">Paper</li>
          </ul>
          <div class="TabbedPanelsContentGroup">
            <div class="TabbedPanelsContent"> <table width="100%" border="0" cellspacing="0" cellpadding="0" class="standard-table">
                       <tbody>
                              <tr class="table-header-bar" id="none">
                                <td  width="16%" class=" table-header-bar-left padding8"><span class="red">*&nbsp;</span><span class="white">Mfg. Item # </span></td>
                                <td width="16%" class="border-left-gray padding8"><span class="red">*&nbsp;</span><span class="white"> Manufacturer</span></td>
                                <td width="16%" class="border-left-gray padding8"><span class="white">Item #</span></td>
                                <td  class="border-left-gray padding8"><span class="red">*&nbsp;</span><span class="white"> Description</span></td>
                                <td width="8%" class="border-left-gray padding8"><span class="red">*&nbsp;</span><span class="white">Qty</span> </td>
                                <td width="10%" class="border-left-gray table-header-bar-right"><span class="white">Action</span></td>
                              </tr>
                              
                              <tr>
                                <td width="16%" class="padding8 border-left-gray">
                                <input name="rmfg1" type="text" class="x-input width-100px" id="rmfg1" maxlength="27" />
                                
                               </td>
                                <td width="16%" class="padding8 border-left-gray">
                                  <input name="rManufacturer1" type="text" class="x-input width-100px" id="rManufacturer1"  maxlength="255" />
                                </td>
                                <td width="16%" class="padding8 border-left-gray"><input name="rManufacturer2" type="text" class="x-input width-120px" id="rManufacturer2" maxlength="8" /></td>
                                <td class="padding8 border-left-gray"> 
                                  <input name="rDescription1" type="text" class="x-input width-300px" id="rDescription1" maxlength="255" />
                               </td>
                                <td width="8%" class="padding8 border-left-gray"><input name="rQty" type="text" class="x-input width-50px" id="rQty" maxlength="7" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);"/>
                               </td>
                                <td width="10%" class="padding8 border-left-gray  border-right-gray"><ul id="cart-actions">
                                  <li><a href="javascript:void(0)" class="grey-ui-btn" onclick=" return validateAndAddDataRecord('tbl_data_facilitySupplies');"><span>Add</span></a></li> 
                                </ul></td>
                              </tr>
                            </tbody>
                           
                          </table>
                          <%-- <div id="table-bottom-bar" style="width:99%; clear:both;">
                            <div id="table-bottom-bar-L"></div>
                            <div id="table-bottom-bar-R"></div>
             			 </div> --%>
                           <br></br>
                           <div id="facility-results-table" style="display:none;"><!-- begin facility-results-table -->
                        <table width="99%" cellspacing="0" cellpadding="0" border="0" align="center" class="standard-table" style="background: none repeat scroll 0% 0% transparent;" id="tbl_data_facilitySupplies">
                            <tbody>
                              <tr id="none" class="padding8 table-header-bar" >
                                <td  width="16%"  class="padding8  table-header-bar-left"  style="word-wrap: break-word; width:130px;"><span class="white">Mfg. Item #</span></td>
                                <td  width="16%" class="padding8 border-left-gray "><span class="white">Manufacturer</span></td>
                                <td  width="16%" class="padding8 border-left-gray "><span class="white">Item #</span></td>
                                <td   class="padding8 border-left-gray "  style="word-wrap: break-word; width:130px;"><span class="white"> Description</span></td>
                                <td  width="8%" class="padding8 border-left-gray "><span class="white">Qty</span></td>
                          		 <td width="10%" align="left" class="padding8 border-left-gray table-header-bar-right">&nbsp;<span class="white">Action</span></td>
                              </tr>
             
                            </tbody>
                          </table>
                          <s:url id='emailSampleLink' namespace='/services'	action='MyServicesHome'>
							<s:param name="selectedHeaderTab">ServicesTab</s:param>
						</s:url>
                       <div id="table-bottom-bar" style="width:99%; clear:both;">
							<div id="table-bottom-bar-L"></div>
                            <div id="table-bottom-bar-R"></div>
        </div>    
        </div> <!-- end facility-results-table -->
               
           			 </div>
            <div class="TabbedPanelsContent"> <table width="100%" border="0" cellspacing="0" cellpadding="0" class="standard-table">
                                    <tbody>
                              <tr class="table-header-bar" id="none">
                                <td width="16%" class=" table-header-bar-left padding8"><span class="white">Mfg. Item # </span></td>
                                <td width="16%" class="border-left-gray padding8"><span class="white">Mill</span></td>
                                 <td width="16%" class="border-left-gray padding8"><span class="white">Item #</span></td>
                                <td class="border-left-gray padding8"><span class="red">*&nbsp;</span><span class="white"> Description</span></td>                               
                                <td  width="8%" class="border-left-gray padding8"><span class="red">*&nbsp;</span><span class="white"> Qty</span> </td>
                                <td width="10%" class="border-left-gray table-header-bar-right"><span class="white">Action</span></td>
                               <!-- <td class="no-border-right table-header-bar-right"> </td>  --> 
                              </tr>
                              <tr>
	                                <td class="padding8 border-left-gray" style="word-wrap: break-word; width:130px;"><s:textfield id='mfg' name='mfg'  cssClass="x-input width-120px"	tabindex="" value='' size="10" />
	                                </td>
	                                <td class="padding8 border-left-gray"><s:textfield id='mil' name='mil' maxlength="10" cssClass="x-input width-100px"	tabindex="" value='' size="10" />
	                                 </td>
	                                <td class="padding8 border-left-gray"><s:textfield id='mfgsku' name='mfgsku' cssClass="x-input width-120px" tabindex="" value='' maxlength="8" size="10" />  
	                                         </td>
	                                <td class="padding8 border-left-gray" style="word-wrap: break-word; width:130px;"><s:textfield id='description'	cssClass="x-input width-300px"   name='description' tabindex="" value='' maxlength="255" size="10" /> 
	                                 </td>
	                                <td class="padding8 border-left-gray"><s:textfield id='qty' name='qty' cssClass="x-input width-50px" tabindex="" value='' maxlength="7" size="10" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" /> 
	                                </td>
	                                <td class="padding8 border-left-gray border-right-gray"><ul id="cart-actions">
	             							 <li><a href="javascript:void(0)" class="grey-ui-btn" onclick="return validateAndAddDataRecord('tbl_data_paperSupplies');"><span>Add</span></a></li> 
	           							 </ul>
	           						</td>
                              </tr>
                            </tbody>
                          </table>
                          <%-- <div id="table-bottom-bar" style="width:100%; clear:both;">
                            <div id="table-bottom-bar-L"></div>
                            <div id="table-bottom-bar-R"></div>
             			 </div> --%>
               <br></br>                          
                <div id="paper-results-table" style="display:none;"><!-- begin paper-results-table -->
                        <table width="100%" cellspacing="0" cellpadding="0" border="0" align="center" class="standard-table" style="background: none repeat scroll 0% 0% transparent;" id="tbl_data_paperSupplies">
                            <tbody>
                              <tr id="none" class="padding8 table-header-bar">
                                <td  width="16%" class="padding8  table-header-bar-left"><span class="white">Mfg. Item #</span></td>
                                <td  width="16%" class="padding8 border-left-gray "><span class="white">Mil</span></td>
                                <td  width="16%" class="padding8 border-left-gray "><span class="white">Item #</span></td>
                                <td class="padding8 border-left-gray "><span class="white"> Description</span></td>
                                <td width="8%" class="padding8 border-left-gray "><span class="white">Qty</span></td>
                          		 <td width="10%" align="left" class="padding8 border-left-gray table-header-bar-right">&nbsp;<span class="white">Action</span></td>
                              </tr>
                            </tbody>
                          </table>
                          <s:url id='emailSampleLink' namespace='/services'	action='MyServicesHome'>
							<s:param name="selectedHeaderTab">ServicesTab</s:param>
						</s:url>
                       <div id="table-bottom-bar" style="width:100%; clear:both;">
							<div id="table-bottom-bar-L"></div>
                            <div id="table-bottom-bar-R"></div>
        </div>    
        </div> <!--  end paper-results-table -->
            </div>
             
          </div>
        
        </div> 
        <div class="clearview"> &nbsp;</div>
        <center><div class="error" id="errorMsgForRequestSample" style="display : none"></div></center>
           
        <div style="width:96%; margin:auto;">
	</div>
                          
          <div class="clearview">&nbsp;</div>
          
               
          		
          		
          <div class="form-service"> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>&nbsp;</td>
                <td>  <div id="cart-actions" >
            <ul id="cart-actions" class="float-right">
            <s:url id='RequestProdSampleLink' namespace='/services'
					action='MyServices'></s:url>
           
              <li class="float-left"><s:a href="%{RequestProdSampleLink}" cssClass="grey-ui-btn"><span>Cancel</span></s:a></li>
              <li ><a class="orange-ui-btn" href="#" onclick="return submitForm('tbl_data_facilitySupplies','tbl_data_paperSupplies');">             
              <span>Submit Request</span></a></li>
            </ul>
          </div></td>
              </tr>
            </table>
          </div> 
          </s:form>
</div>
<div class="clearview">&nbsp;</div>
</div>
</div>

<script type="text/javascript">
<!--
var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
onPageLoad();
//-->
   </script></div>
<!-- end main  -->
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />

</div>

</body>
</swc:html>
var lnkName ;
var urlName;	

function deleteRow(obj) {
	//alert('delete selected...!');
	//var tbody = document.getElementById("tbl").getElementsByTagName("tbody")[0];
	//tbody.removeChild(obj);
	//obj.value
	//changeTableCss();
}

function changeTableCss(){
	var tbody = document.getElementById("tbl").getElementsByTagName("tbody")[0];
	var rowCount = tbody.rows.length;	
	for(var i=rowCount; i>=1; i--) {
		var row = tbody.rows[i];
		if (null != row) {
			if (i%2 != 0)
			{
				row.setAttribute('class', 'odd');
			}
			else
			{
				row.setAttribute('class', '');
			}			
		}         			
	}
}

function addRow() {
	
	var link = document.getElementById("linkName").value;
	var urlN = 	document.getElementById("url").value;
	
	if(urlN.indexOf("htt",0) < 0)
	{
		urlN = "http://" + urlN;
	}
	
	$.fancybox.close();
	var tbody = document.getElementById("tbl").getElementsByTagName("tbody")[0];
	var rowCount = tbody.rows.length;
	
	var row = null;
	
	if (rowCount%2 == 0) {
		row = document.createElement("tr")
		row.setAttribute('class', 'odd');
	} else {
		row = document.createElement("tr")
	}
	
	var data1 = document.createElement("td");		
	data1.setAttribute('class', ' noBorders padding-left1');

	var deleteLink = document.createElement('a');
	var imageIcon = document.createElement('img');
	imageIcon.setAttribute('src','../../xpedx/images/icons/12x12_red_x.png');
	imageIcon.setAttribute('alt','delete');
	imageIcon.setAttribute('title','Remove');
	imageIcon.setAttribute('width','16');
	imageIcon.setAttribute('height','16');
	imageIcon.setAttribute('border','0');
	imageIcon.setAttribute('style','float:left');
	deleteLink.appendChild (imageIcon);
	var table = document.getElementById("tbl").getElementsByTagName("tbody")[0];
	var rowCount = table.rows.length;
	deleteLink.setAttribute('href', 'javascript:deleteQuickLinkRow('+rowCount+');');
	data1.appendChild(deleteLink);
	var element2 = document.createElement("input");
	element2.type = "text";
	element2.name = "urlName";
	element2.value = link;
	element2.setAttribute('class','x-input user-profile-input');	
	element2.setAttribute('style','width:220px;');
	element2.setAttribute('tabindex','12');
	element2.setAttribute('maxlength','35');
	data1.appendChild(element2);

	var data2 = document.createElement("td");
	data2.setAttribute('valign', 'top');
	var element3 = document.createElement("input");
	element3.type = "text";
	element3.name = "urlValue";	
	element3.value = urlN;
	element3.setAttribute('class','x-input user-profile-input');	
	element3.setAttribute('style','width: 330px;');
	element3.setAttribute('tabindex','12');		
	data2.appendChild(element3);

	var data3 = document.createElement("td");
	data3.setAttribute('valign', 'top');
	var element4 = document.createElement("input");  
	element4.type = "checkbox";
	element4.name = "showURL"; 
	element4.checked = true;
	element4.setAttribute('class', ' margin-15');
	data3.appendChild(element4);

	var data4 = document.createElement("td");
	data4.setAttribute('valign', 'top');

	var combo = document.createElement("select");
	combo.setAttribute('class', 'x-input');
	combo.setAttribute('onfocus','this.oldvalue = this.value;');
	combo.setAttribute('onchange', 'javascript:onChangeItemOrder(this, this.oldvalue, this.value);');
	combo.name = "combo";
	combo.id = 'QuickLinksSeq'+rowCount;

	for(var i=1; i<=rowCount; i++) {
		var option = document.createElement("option");  
	    option.text = i;  
	    option.value = i;  
	    if(i == rowCount) {
		    option.selected = true;
	    }
	    try {  
	        combo.add(option, null); //Standard  
	    }catch(error) {  
	        combo.add(option); // IE only  
	    }
	}
	
	//Adding one more option to the previous rows select seq options 
	for(var i=1; i<rowCount; i++){
		var selectSeq = tbody.rows[i].cells[3].childNodes[0];
		var option = document.createElement("option");
		option.text = rowCount;  
	    option.value = rowCount;
	    try {  
	    	selectSeq.add(option, null); //Standard  
	    }catch(error) {  
	    	selectSeq.add(option); // IE only  
	    }
	}
     			
	data4.appendChild (combo)
	        		
	row.appendChild(data1);
	row.appendChild(data2);
	row.appendChild(data3);
	row.appendChild(data4);
	tbody.appendChild(row);
}



function selectAll(tableRef) {
		var tbody1 = document.getElementById(tableRef);
		var rowCount = tbody1.rows.length;        		
		for(var i=1; i<rowCount; i++) {
			var row = tbody1.rows[i]; 
			if (null != row) {
				var chkbox = row.cells[0].childNodes[0];
				chkbox.checked = true;
			}
		}		
	}

	function showCreateNewDialog(attr)
	{
		document.getElementById("operation").value = "addoperation";
		document.getElementById("linkName").value = "";
		document.getElementById("url").value = "";		
		DialogPanel.show(attr);
	    svg_classhandlers_decoratePage();
	}

	/*function editSelected() {
		var tbody = document.getElementById("tbl");
		document.getElementById("operation").value = "editoperation"; 
		var rowCount = tbody.rows.length;
		var countSelected = 0;        		
		for(var i=rowCount; i>=1; i--) {
			var row = tbody.rows[i];
			if (null != row) {
				var chkbox = row.cells[0].childNodes[0];
    			if(null != chkbox && true == chkbox.checked) {							
    				countSelected++;
    			}
			}         			
		}
		if (countSelected == 0) {
			alert('Please select one QuickLink to Edit');
			return false;
		}
		if (countSelected > 1) {
			alert('Please select only one Quick Link to Edit');
			return false;
		} else {
			for(var i=rowCount; i>=1; i--) {
				var row = tbody.rows[i];
				if (null != row) {
					var chkbox = row.cells[0].childNodes[0];
	    			if(null != chkbox && true == chkbox.checked) {							
	    				document.getElementById("linkName").value = row.cells[0].childNodes[1].nodeValue;
	    				document.getElementById("url").value = row.cells[1].childNodes[0].nodeValue;
	    				}
					}         			
				}
			}	
		}*/
	
	function editSelected(tableRef,uniqueId) {
		  
		document.getElementById("operation").value ="editoperation";
		
		var table = document.getElementById(tableRef);
		var tbody = table.getElementsByTagName("tbody")[0];
		var row = document.getElementById(uniqueId);
		 
		 lnkName = document.getElementById("linkName").value = row.cells[0].childNodes[1].nodeValue;
		 urlName = document.getElementById("url").value = row.cells[1].childNodes[0].nodeValue;
		 
		
		$('#addNewQL').click();
	  }

	function deleteSelected(tableRef,uniqueId){	
		var table = document.getElementById(tableRef);
		var tbody = table.getElementsByTagName("tbody")[0];
		var row = document.getElementById(uniqueId);
		var answer = confirm ("Are you sure You want to delete the Quick Link " + row.cells[0].childNodes[1].nodeValue)
		if(answer){
			tbody.removeChild(row);
		}
	}

	function cancelClick(attr) {
		DialogPanel.hide(attr);		
	}
	
	function closefancyBox(){	   
		$.fancybox.close();		    				
	}
	
	function deleteQuickLinkRow(rowId) {
		try {
			var table = document.getElementById("tbl").getElementsByTagName("tbody")[0];
			var rowCount = table.rows.length;
			if(rowId > 0 && rowId < rowCount){
				var deletedSeqIndex = 0;
                deletedSeqIndex = table.rows[rowId].cells[3].childNodes[0].selectedIndex;
				document.getElementById("tbl").deleteRow(rowId);
				rowCount = table.rows.length;
				for(var i=rowId; i<rowCount; i++) {
	                var row = table.rows[i];
                    row.cells[0].childNodes[0].setAttribute('href', 'javascript:deleteQuickLinkRow('+(i)+');');
				}
				for(var i=1; i<rowCount;i++){
					var row = table.rows[i];
					var selectSeq = row.cells[3].childNodes[0];
                    var selectSeqIndex = selectSeq.selectedIndex;
                    var options = selectSeq.options;
                    if(selectSeqIndex > deletedSeqIndex){
	                    selectSeq.selectedIndex = selectSeq.selectedIndex - 1; 
                    }
                    options[options.length-1] = null;
				}
			}
		}catch(e) {
			alert(e);
		}
	}
	
	function changeSequenceForAll(rowId, oldValue, newValue){
		var table = document.getElementById("tbl").getElementsByTagName("tbody")[0];
		var rowCount = table.rows.length; 
		if(rowId > 0 && rowId < rowCount){
            var changedSeqIndex = parseInt(newValue)-1;
          //Change remaining Sequences by moving back one place to the changed Index  
			for(var i=1; i<rowCount; i++){
    			if(i!= rowId){
					var selectSeq = table.rows[i].cells[3].childNodes[0];
					var selectSeqIndex = selectSeq.selectedIndex;
					if(selectSeqIndex > (oldValue-1) && selectSeqIndex <= changedSeqIndex){
						selectSeq.selectedIndex = selectSeqIndex - 1;
					}
					else if(selectSeqIndex < (oldValue-1) && selectSeqIndex >= changedSeqIndex){
						selectSeq.selectedIndex = selectSeqIndex + 1;
					}
    			}
			}
		}
	}
	
	function saveClick() {
		
		if (document.getElementById("operation").value == "editoperation") {
					
			var link = document.getElementById("linkName").value;
			var urlN = 	document.getElementById("url").value;
			if(urlN.indexOf("htt",0) < 0)
			{
				urlN = "http://" + urlN;
			}	
			var tbody = document.getElementById("tbl");
			var rowCount = tbody.rows.length;
			for(var i=rowCount; i>=1; i--) {
				var row = tbody.rows[i];
				if (null != row && row.cells[0].childNodes[1].nodeValue == lnkName) {
					var chkbox = row.cells[0].childNodes[0];
					
					if(link.trim().length <= 0 ){
                           alert("Please enter the Link Name");
                           document.getElementById("linkName").focus();                               
                           return;
					}
								
					if(urlN.trim().length <= 0 ){
                           alert("Please enter the URL Name");
                           document.getElementById("url").focus();                               
                           return;
					}

					if(link.trim().length > 0 && urlN.trim().length >0 ){
    					row.cells[0].childNodes[1].nodeValue = link.trim();
    					row.cells[1].childNodes[0].nodeValue = urlN.trim();
    					i = 0;
    					$.fancybox.close();
    					document.getElementById("operation").value = "";
    					document.getElementById("linkName").value = "";	
    					document.getElementById("url").value = "";		    				
				     }
						
				}         			
			}
		}
    		
	else if (document.getElementById("operation").value == "addoperation") {
					
		var linkValue = document.getElementById("linkName").value; 
		var urlValue = document.getElementById("url").value;						
		if(urlN.indexOf("htt",0) < 0)
		{
			urlN = "http://" + urlN;
		}

		if(linkValue.trim().length <= 0 ){
            alert("Please enter the Link Name");
            document.getElementById("linkName").focus();                               
            return;
		}
					
		if(urlValue.trim().length <= 0 ){
            alert("Please enter the URL Name");
            document.getElementById("url").focus();                               
            return;
		}
			$.fancybox.close();
	
		
		var tbody = document.getElementById("tbl").getElementsByTagName("tbody")[0];
		var rowCount = tbody.rows.length;
		
		var row = null;
		
		if (rowCount%2 == 0) {
			row = document.createElement("tr")
			row.setAttribute('class', 'odd');
		} else {
			row = document.createElement("tr")
		}
		
		
		var uniqueId = Math.floor(Math.random()*5000000000);
		row.setAttribute('id',uniqueId);
		
		
		var data1 = document.createElement("td")		
		var element1 = document.createElement("input");  
		element1.type = "checkbox";
		element1.name = "list";  	
		element1.setAttribute('class', ' margin-15');		
		data1.appendChild(element1)
			
		data1.appendChild (document.createTextNode(linkValue))			
		
		var data2 = null;
		var data3 = null;
		var data4 = null;
		
		if(rowCount == 1) {
			data2 = document.createElement("td")
			data2.setAttribute('width', '40%');
			data3 = document.createElement("td")
			data3.setAttribute('width', '9%');
			data4 = document.createElement("td")
			data4.setAttribute('width', '12%');
		} else {
			data2 = document.createElement("td")
			data3 = document.createElement("td")
			data4 = document.createElement("td")
		}
		
		data2.appendChild (document.createTextNode(urlValue))
		
		var editLink = document.createElement('a');				
		editLink.appendChild (document.createTextNode ('Edit'));
		editLink.setAttribute('id','editQL');
		editLink.setAttribute('href', 'javascript:editSelected("tbl","'+uniqueId+'");');
	
		data3.appendChild(editLink);
		
		var deleteLink = document.createElement('a');
		deleteLink.appendChild (document.createTextNode ('Delete'));
		deleteLink.setAttribute('href', 'javascript:deleteSelected("tbl","'+uniqueId+'");');
					
		data4.appendChild(deleteLink);
					
					
		var data5 = document.createElement("td")
	
		for(var j=1; j<=rowCount-1; j++) {
			var rows = tbody.rows[j];																
			var option = document.createElement("option");  
		    option.text = rowCount;  
		    option.value = rowCount;  				    
		    try {  
		    	rows.cells[4].childNodes[0].options.add(option, null); //Standard  
		    }catch(error) {  
		    	rows.cells[4].childNodes[0].options.add(option); // IE only  
		    }									
		}			

		var combo = document.createElement("select");  
		combo.name = "combo";
		combo.id = "combo";

		for(var i=1; i<=rowCount; i++) {
			var option = document.createElement("option");  
		    option.text = i;  
		    option.value = i;  
		    try {  
		        combo.add(option, null); //Standard  
		    }catch(error) {  
		        combo.add(option); // IE only  
		    }
		}
         			
		data5.appendChild (combo)
		        		
		row.appendChild(data1);
		row.appendChild(data2);
		row.appendChild(data3);
		row.appendChild(data4);
		row.appendChild(data5);
		tbody.appendChild(row);

		document.getElementById("operation").value = "";
		document.getElementById("linkName").value = "";	
		document.getElementById("url").value = "";		    				

		
	}		
	document.getElementById("operation").value = '';
}

	function callSave() {            	
		var tbody = document.getElementById("tbl");
		var rowCount = tbody.rows.length;
		var comleteData = "";
		
		var tbody = document.getElementById("tbl").getElementsByTagName("tbody")[0];
		var rowCount = tbody.rows.length;
		var invalidOrdering = false;
		//Checking if two quick links have the same order
		for(var i=1; i<rowCount-1; i++){
			var selectSeq = tbody.rows[i].cells[3].childNodes[0];
			var selectedOption = selectSeq.selectedIndex;
			for(var j=i+1; j<rowCount;j++){
				var selectSeqJ = tbody.rows[j].cells[3].childNodes[0];
				var selectedOptionJ = selectSeqJ.selectedIndex;
				if(selectedOption == selectedOptionJ)
					invalidOrdering = true;
			}
		}
		
		if(invalidOrdering)
		{
			alert("Please enter a valid ordering of Quick Links.");
			document.getElementById("tbl").getElementsByTagName("tbody")[0].rows[1].cells[3].childNodes[0].focus();
	        return;
		}

		for(var i=1; i<=rowCount; i++) {
			var row = tbody.rows[i];
			if (null != row) {
    			var cellsCount = row.cells.length;    			
    			for(var j=0; j<cellsCount; j++) {
    				var temp = "";        				 
        			if (j==3) {
        				temp = row.cells[j].childNodes[0].value;
        			} else {
        				if(j==2) {
            				var checkbox = row.cells[j].childNodes[0];
            				if(null != checkbox && true == checkbox.checked){
            					temp = "Y" ;
            				}
            				else
            					temp = "N" ;
            				
            			}
        				else if(j==0){
        					temp = row.cells[j].childNodes[1].value;
        				}
            			else {
            				temp = row.cells[j].childNodes[0].value;
            			}        				
        			}               		
        			
        			if(temp == "") {
            			temp = "*#?";
        			}        
			
        			comleteData = comleteData + "+=_" + temp;                			                			
    			}
			}
			            		
		}        
		document.getElementById('bodyData').value = comleteData;		

		document.getElementById("custInfoForm").submit();
	    return true;
		  	            	                        	            	
	}
	function setURL()
	{	
		
		if(document.getElementById("operation").value == '' ){			
			document.getElementById("operation").value = "addoperation"; 
		} 
		else {			
			document.getElementById("operation").value = "editoperation";
		}
			
	}
	
	
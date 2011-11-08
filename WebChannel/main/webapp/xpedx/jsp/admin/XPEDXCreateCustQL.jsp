<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that�s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>

<script type="text/javascript">

	

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

	function editSelected() {
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
		}
		if (countSelected > 1) {
			alert('Please select only one Quick Link to Edit');
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
		}
	function deleteSelected(tableRef) {
		var tbody = document.getElementById(tableRef);
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
			alert('Please select the option to delete.');
		} else {
			for(var i=rowCount; i>=1; i--) {
				var row = tbody.rows[i];
				if (null != row) {
					var chkbox = row.cells[0].childNodes[0];					
	    			if(null != chkbox && true == chkbox.checked) {			    									
	    				var answer = confirm ("Are you sure You want to delete the Quick Link "+'"'+ row.cells[0].childNodes[1].nodeValue +'"')
	    				if(answer){
	    					tbody.deleteRow(i);
	    				}
	    			}
				}         			
			}			
			for (var j=1; j<=countSelected; j++) {
				rowCount = tbody.rows.length;
				for(var i=1; i<=rowCount; i++) {
					var row = tbody.rows[i];
					if (null != row) {
						row.cells[4].childNodes[0].remove(rowCount - 1);
					}
				}				
			}
		}
				
	}


	function cancelClick(attr) {
		DialogPanel.hide(attr);
	}

	function saveClick() {
		if (document.getElementById("operation").value == "editoperation") {
			var tbody = document.getElementById("tbl");
			var rowCount = tbody.rows.length;
			for(var i=rowCount; i>=1; i--) {
				var row = tbody.rows[i];
				if (null != row) {
					var chkbox = row.cells[0].childNodes[0];
	    			if(null != chkbox && true == chkbox.checked) {							
	    				row.cells[0].childNodes[1].nodeValue = document.getElementById("linkName").value;
	    				row.cells[1].childNodes[0].nodeValue = document.getElementById("url").value;
	    				$.fancybox.close();    			    
	    			}
				}         			
			}
		}

		if (document.getElementById("operation").value == "addoperation") {			
			var linkValue = document.getElementById("linkName").value; 
			var urlValue = document.getElementById("url").value;						

			$.fancybox.close();
			document.getElementById("linkName").value = "";	
			document.getElementById("url").value = "";
			
			var tbody = document.getElementById("tbl").getElementsByTagName("tbody")[0];
			var rowCount = tbody.rows.length;
			
			var row = null;
			
			if (rowCount%2 == 0) {
				row = document.createElement("tr")
				row.setAttribute('class', 'odd');
			} else {
				row = document.createElement("tr")
			}
			
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
			editLink.setAttribute('href', '#newQL');
			editLink.setAttribute('class', 'editQL');

			data3.appendChild(editLink);
			
			var deleteLink = document.createElement('a');
			deleteLink.appendChild (document.createTextNode ('Delete'));
			deleteLink.setAttribute('href', 'javascript:deleteSelected("tbl");');
						
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
			combo.setAttribute('class', 'x-input');
			combo.name = "combo";
			combo.id = "combo";

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
             			
			data5.appendChild (combo)
			        		
			row.appendChild(data1);
			row.appendChild(data2);
			row.appendChild(data3);
			row.appendChild(data4);
			row.appendChild(data5);
			tbody.appendChild(row);						
		}		
	}

	function callSave() {
		var isSelfAdmin = document.getElementById("selfAdminId").value;
		if(isSelfAdmin == "true"){
			
			if(document.getElementById("userpassword").value=="")
		    {
		        alert('Please enter Your Password');
		        document.getElementById("userpassword").focus();
		        return false;    
		    }
			if(document.getElementById("confirmpassword").value=="")
		    {
		        alert('Please enter Your Password in the Confirm Password field also');
		        document.getElementById("confirmpassword").focus();
		        return false;    
		    }	
			if(document.getElementById("userpassword").value!=document.getElementById("confirmpassword").value)
		    {
		        alert('Please enter the same password in both password and confirm password fields');
		        document.getElementById("userpassword").focus();
		        return false;    
		    }
			if(document.getElementById("secretQuestion").value=="")
		    {
		        alert('Please select Your Secret Question');
		        document.getElementById("secretQuestion").focus();
		        return false;    
		    }	
			if(document.getElementById("secretAnswer").value=="")
		    {
		        alert('Please enter Your Secret Answer');
		        document.getElementById("secretAnswer").focus();
		        return false;    
		    }
			if(document.getElementById("confirmAnswer").value=="")
		    {
		        alert('Please enter Your Secret Answer in the Confirm Answer field also');
		        document.getElementById("confirmAnswer").focus();
		        return false;    
		    }
			if(document.getElementById("secretAnswer").value!=document.getElementById("confirmAnswer").value)
		    {
		        alert('Please enter the same answers in both answer and confirm answer fields');
		        document.getElementById("secretAnswer").focus();
		        return false;    
		    }
		}	
		if(document.getElementById("firstName").value=="")
	    {
	        alert('Please enter Your First Name');
	        document.getElementById("firstName").focus();
	        return false;    
	    }
		if(document.getElementById("lastName").value=="")
	    {
	        alert('Please enter Your Last Name');
	        document.getElementById("lastName").focus();
	        return false;    
	    }	 
	 	
		var email=document.getElementById("emailId").value;
	    len=email.length;
	    var no=0;
	    for(var i=0;i<len;i++)
	    {
	        if(email.charAt(i)=="@" || email.charAt(i)==",")
	        {
	            //alert(i);
	            no=no+1;
	            //alert(no);
	        }
	    }
	    var dat=Date();
	    //alert(dat);
	    if (email == 0 || email=="" )
	    {
	        alert("Please enter your email address!");
	        document.getElementById("emailId").focus();
	        return false;
	    }
	    else if (email.indexOf("@") < 0)
	    {
	        alert("Please enter valid Email address!");
	        document.getElementById("emailId").focus();
	        return false;
	    }
	    // Email Checker
	    else if (email.indexOf(".") < 0)
	    {
	        alert("Incorrect email address. Please re-enter!");
	        document.getElementById("emailId").focus();
	        return false;
	    }
	    // Email Checker
	    else if (email.indexOf(" ") >= 0)
	    {
	        alert("Incorrect email address. Please re-enter!");
	        document.getElementById("emailId").focus();
	        return false;
	    }
	    else if (len > 50)
	    {
	        //length1=email.length;
	        //alert(length1);
	        alert("Email should not exceed more than 50 characters!");
	        document.getElementById("emailId").focus();
	        return false;
	    }
	    else if (no >= 2)
	    {
	        //length1=email.length;
	        //alert(length1);
	        alert("Please do not enter more than one email address!");
	        document.getElementById("emailId").focus();
	        return false;
	    }            	

	    var lboTo=document.myAccount.customers2;
		for ( var i=0; i < lboTo.options.length; i++ )
		{
			lboTo.options[i].selected = true;
		}
		lboTo=document.myAccount.customers1;
		for ( var i=0; i < lboTo.options.length; i++ )
		{
			lboTo.options[i].selected = true;
		}

	    
		var tbody = document.getElementById("tbl");
		var rowCount = tbody.rows.length;
		var comleteData = "";

		
		for(var i=1; i<=rowCount; i++) {
			var row = tbody.rows[i];
			if (null != row) {
    			var cellsCount = row.cells.length;    			
    			for(var j=0; j<cellsCount; j++) {
    				var temp = "";        				 
        			if (j==3 || j==2) { 
        			} else if (j==4) {
        				temp = row.cells[j].childNodes[0].value;
        			} else {
        				if(j==0) {
            				var checkbox = row.cells[j].childNodes[0];
            				if(null != checkbox && true == checkbox.checked){
            					temp = "Y" + "%";
            				}
            				else
            					temp = "N" + "%";
            				
            				temp = temp + row.cells[j].childNodes[1].nodeValue;
            			}
            			else {
            				temp = row.cells[j].childNodes[0].nodeValue;
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

		document.getElementById("myAccount").submit();
	    return true;
		  	            	                        	            	
	}
	
	
</script>	

<s:set name='_action' value='[0]' />

			<tr>
				<td colspan="2" valign="top" class="no-borders">
				<h2>Manage Quick Links</h2>
				<div class="clearview">&nbsp;</div>				
				<s:a id="addNewQL" cssClass="txt-lnk-sml-1" href="#newQL">Add New</s:a> <br />
				<div class="clearview">&nbsp;</div>
				<table width="90%" border="0" align="left" cellpadding="0"
					cellspacing="0" id="tbl"
					style="background: none repeat scroll 0% 0% transparent;">
					<tbody>
						<tr class="table-header-bar" id="none">
							<td class="no-border table-header-bar-left"><span
								class="white"> 								
								<s:a id="selectAllLink" cssStyle="color: #fff;" cssClass="white" href="javascript:selectAll('tbl');">Show</s:a> Link Name</td>
							<td class="no-border "><span class="white"> URL</span></td>
							<td class="no-border ">&nbsp;</td>
							<td class="no-border ">&nbsp;</td>
							<td width="13%" colspan="2" align="left"
								class="no-border-right table-header-bar-right"><span
								class="white">Sequence</span></td>
						</tr>
					</tbody>
				</table>
				<div id="table-bottom-bar" style="width: 90%; clear: both;">
				<div id="table-bottom-bar-L"></div>
				<div id="table-bottom-bar-R"></div>
				</div>
				<div class="clearview">&nbsp;</div>
				</td>
			</tr>
			
		
		<s:hidden id="bodyData" name="bodyData"/>


<script>

	function loadDataOnStart() {
		var tbody = document.getElementById("tbl").getElementsByTagName("tbody")[0];
		
		<s:iterator value="quickLinkBeanArray" status="arrStatus">

			var row = null;
		
			<s:if test='#arrStatus.modulus(2) == 0'>
				row = document.createElement("tr")
		    </s:if>
		    <s:else>
				row = document.createElement("tr")
				row.setAttribute('class', 'odd');		    	
		    </s:else>
			
			var data1 = document.createElement("td")		
			var element1 = document.createElement("input");  
			element1.type = "checkbox";
			element1.name = "list";  	
			element1.setAttribute('class', ' margin-15');		
			var showLink = '<s:property value="showQuickLink"/>';
		    if (showLink == 'Y')
				 element1.checked = true;
			data1.appendChild(element1)
				
			var linkName = '<s:property value="urlName"/>';	
			data1.appendChild (document.createTextNode(linkName))			
			
			var data2 = null;
			var data3 = null;
			var data4 = null;
			
			<s:if test="#arrStatus.first == true">
				data2 = document.createElement("td")
				data2.setAttribute('width', '40%');
				data3 = document.createElement("td")
				data3.setAttribute('width', '9%');
				data4 = document.createElement("td")
				data4.setAttribute('width', '12%');
			</s:if>
			<s:else>
				data2 = document.createElement("td")
				data3 = document.createElement("td")
				data4 = document.createElement("td")
			</s:else>			
			
			var urlName = '<s:property value="quickLinkURL"/>';
			data2.appendChild (document.createTextNode(urlName))
			
			var editLink = document.createElement('a');				
			editLink.appendChild (document.createTextNode ('Edit'));
			editLink.setAttribute('href', '#newQL');
			editLink.setAttribute('class', 'editQL');

			data3.appendChild(editLink);
			
			var deleteLink = document.createElement('a');
			deleteLink.appendChild (document.createTextNode ('Delete'));
			deleteLink.setAttribute('href', 'javascript:deleteSelected("tbl");');
						
			data4.appendChild(deleteLink);
						
			var data5 = document.createElement("td")
			var orderValue = '<s:property value="urlOrder"/>';  
			var linkSize = '<s:property value="quickLinkBeanArray.length" />';

			var combo = document.createElement("select");
			combo.setAttribute('class', 'x-input');
			combo.name = "combo";
			combo.id = "combo";

			for(var i=1; i<=linkSize; i++) {
				var option = document.createElement("option");  
			    option.text = i;  
			    option.value = i;  
			    if(i == orderValue) {
				    option.selected = true;
			    }
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
		</s:iterator>      
		
	}

	loadDataOnStart();
	
</script>
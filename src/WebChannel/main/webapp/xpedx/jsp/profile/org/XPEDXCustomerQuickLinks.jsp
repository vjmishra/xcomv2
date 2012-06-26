<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
    This is to avoid a defect in Struts that?s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts? OGNL statements. --%>
    
    <s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
    <td width="100%" class="no-border-right padding0" valign="top">
    	<div class="question">
    		<ul class="padding-top3">
            	<li class="no-margin"> <strong>Quick Links</strong></li>
                <li><a href="#"><img height="12" width="12" border="0" title="Account defined bookmarked links which display on the homepage." alt="Account defined bookmarked links which display on the homepage." src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"></a></li>
			</ul>
	        <s:if test="%{#isUserAdmin}" >
	        	<div class="txt-small clearview padding-bottom2"><s:a id="addNewQL" name="addNewQL" href="#newQL" cssClass="underlines">[Add New]</s:a></div>          	                                      
			</s:if>
		</div> 
      	<table width="100%" cellspacing="0" cellpadding="0" border="0" id="tbl" class="standard-table">
  			<tbody>
  				<tr class="table-header-bar">
    				<td width="35%" class="no-border table-header-bar-left"><span class="white txt-small"> Name</span></td>
    				<td width="48%" align="left" class="no-border  "><span class="white txt-small">URL</span></td>
    				<td width="8%" align="left" class="no-border"><span class="white txt-small">Show</span></td>
    				<td width="9%" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small">Sequence</span></td>
				</tr>				
			</tbody>
		</table> 
      <%-- 	<div id="table-bottom-bar" style="width:100%;">
        	<div id="table-bottom-bar-L"></div>
        	<div id="table-bottom-bar-R"></div>
        </div> --%>  

	</td>   
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

//		var cT = new Date();
//		var uniqueId = ""+cT.getDate() + cT.getDay() + cT.getFullYear() + cT.getHours() + cT.getMinutes()+ cT.getSeconds() ;
//		var uniqueId = Math.floor(Math.random()*5000000000);	
//		row.setAttribute('id',uniqueId);
		 			
		var data1 = document.createElement("td");		
		data1.setAttribute('class', ' noBorders padding-left1');

		var deleteLink = document.createElement('a');
		var imageIcon = document.createElement('img');
		imageIcon.setAttribute('src','<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/icon_delete<s:property value='#wcUtil.xpedxBuildKey' />.gif');
		imageIcon.setAttribute('alt','delete');
		imageIcon.setAttribute('width','16');
		imageIcon.setAttribute('height','16');
		imageIcon.setAttribute('border','0');
		imageIcon.setAttribute('style','float:left');
		deleteLink.appendChild (imageIcon);
		deleteLink.setAttribute('href', 'javascript:deleteQuickLinkRow("<s:property value="%{#arrStatus.index+1}" escape='false'/>");');
		data1.appendChild(deleteLink);
		var element2 = document.createElement("input");
		element2.name = "urlName";
		element2.value = $('<div/>').html("<s:property value='urlName'/>").text();	
		element2.setAttribute('class','x-input user-profile-input');	
		element2.setAttribute('style','width:220px;');
		element2.setAttribute('tabindex','12');
		element2.setAttribute('maxlength','35');
		data1.appendChild(element2);

		var data2 = document.createElement("td");
		data2.setAttribute('valign', 'top');
		var element3 = document.createElement("input");
		element3.name = "urlValue";
		element3.value = $('<div/>').html("<s:property value='quickLinkURL'/>").text();		
		element3.setAttribute('class','x-input user-profile-input');	
		element3.setAttribute('style','width: 330px;');
		element3.setAttribute('tabindex','12');	
		element3.setAttribute('maxlength','250');
		data2.appendChild(element3);

		var data3 = document.createElement("td");
		data3.setAttribute('valign', 'top');
		var element4 = document.createElement("input");  
		element4.type = "checkbox";
		element4.name = "showURL";  	
		element4.setAttribute('class', ' margin-15');
		var showLink = '<s:property value="showQuickLink"/>';
	    if (showLink == 'Y')
			 element4.checked = true;
		data3.appendChild(element4);

		var data4 = document.createElement("td");
		data4.setAttribute('valign', 'top');
		var orderValue = '<s:property value="urlOrder"/>';  
		var linkSize = '<s:property value="quickLinkBeanArray.length" />';

		var combo = document.createElement("select");
		combo.setAttribute('class', 'x-input');
		combo.setAttribute('onfocus','this.oldvalue = this.value;');
		combo.setAttribute('onchange', 'javascript:onChangeItemOrder(this, this.oldvalue, this.value);');
		combo.name = "combo";
		combo.id = 'QuickLinksSeq'+"<s:property value="%{#arrStatus.index+1}" escape='false'/>";

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
         			
		data4.appendChild (combo)
		        		
		row.appendChild(data1);
		row.appendChild(data2);
		row.appendChild(data3);
		row.appendChild(data4);
		tbody.appendChild(row);
	</s:iterator>      
	
}

loadDataOnStart();
	
</script>